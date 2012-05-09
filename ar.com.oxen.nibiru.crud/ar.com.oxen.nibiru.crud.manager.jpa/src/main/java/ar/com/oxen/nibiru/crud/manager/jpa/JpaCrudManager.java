package ar.com.oxen.nibiru.crud.manager.jpa;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import ar.com.oxen.commons.bean.api.PropertyDescriptor;
import ar.com.oxen.commons.bean.api.WrapperFactory;
import ar.com.oxen.nibiru.crud.bean.annotation.Action;
import ar.com.oxen.nibiru.crud.bean.annotation.Actions;
import ar.com.oxen.nibiru.crud.bean.annotation.Show;
import ar.com.oxen.nibiru.crud.bean.annotation.Widget;
import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.manager.api.WidgetType;
import ar.com.oxen.nibiru.crud.utils.SimpleCrudAction;
import ar.com.oxen.nibiru.crud.utils.SimpleCrudField;

public class JpaCrudManager<T> implements CrudManager<T>,
		CrudActionExtension<T> {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;
	private Class<T> persistentClass;
	private WrapperFactory wrapperFactory;
	private String pkName;

	@Override
	public String getEntityTypeName() {
		return this.persistentClass.getName();
	}

	@Override
	public List<CrudField> getListFields() {
		return this.fieldNamesToCrudFields(new ShowValidator() {
			@Override
			public boolean mustShow(Show show) {
				return show.inList();
			}
		});
	}

	@Override
	public List<CrudField> getFormFields() {
		return this.fieldNamesToCrudFields(new ShowValidator() {
			@Override
			public boolean mustShow(Show show) {
				return show.inForm();
			}
		});
	}

	private List<CrudField> fieldNamesToCrudFields(ShowValidator showValidator) {
		List<PropertyDescriptor> descriptors = new LinkedList<PropertyDescriptor>();

		for (PropertyDescriptor descriptor : this.wrapperFactory.wrapClass(
				this.persistentClass).getPropertyDescriptors()) {
			Show show = descriptor.getAnnotation(Show.class);
			if (show != null && showValidator.mustShow(show)) {
				descriptors.add(descriptor);
			}
		}

		Collections.sort(descriptors, new Comparator<PropertyDescriptor>() {
			@Override
			public int compare(PropertyDescriptor o1, PropertyDescriptor o2) {
				int order1 = o1.getAnnotation(Show.class).order();
				int order2 = o2.getAnnotation(Show.class).order();

				if (order1 > order2) {
					return 1;
				} else if (order1 > order2) {
					return -1;
				} else {
					return 0;
				}
			}
		});

		List<CrudField> fields = new ArrayList<CrudField>(descriptors.size());
		for (PropertyDescriptor descriptor : descriptors) {
			boolean readonly = false;
			int maxLength = -1;
			WidgetType widgetType = null;
			String tab = "general";

			Show show = descriptor.getAnnotation(Show.class);
			Widget crudWidget = descriptor.getAnnotation(Widget.class);
			if (crudWidget != null) {
				readonly = crudWidget.readonly();
				widgetType = crudWidget.type();
				maxLength = crudWidget.maxLength();
				tab = crudWidget.tab();
			}

			if (!this.isBasicType(descriptor.getType())) {
				if (Collection.class.isAssignableFrom(descriptor.getType())) {
					if (widgetType == null) {
						widgetType = WidgetType.MULTISELECT;
					}
				} else {
					if (widgetType == null) {
						widgetType = WidgetType.COMBO_BOX;
					}
				}
			}

			if (widgetType == null) {
				if (Date.class.equals(descriptor.getType())) {
					widgetType = WidgetType.DATE_FIELD;
				} else if (Boolean.class.equals(descriptor.getType())) {
					widgetType = WidgetType.CHECK_BOX;
				} else {
					widgetType = WidgetType.TEXT_FIELD;
				}
			}

			fields.add(new SimpleCrudField(descriptor.getName(), descriptor
					.getType(), new SimpleCrudField.SimpleListInfo(show
					.columWidth()), new SimpleCrudField.SimpleFormInfo(
					widgetType, readonly, maxLength, tab)));
		}

		return fields;
	}

	@Override
	public List<CrudEntity<T>> findAll() {
		List<T> beans = this.findAll(this.persistentClass);
		return this.beansToCrudEntities(beans);
	}

	@Override
	public CrudEntity<T> findById(Object id) {
		T bean = this
				.refresh(this.entityManager.find(this.persistentClass, id));
		if (bean != null) {
			return this.beanToCrudEntity(bean);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrudEntity<T>> findByfield(String field, Object value) {
		Query query = this.entityManager.createQuery("from "
				+ this.persistentClass.getName() + " where " + field
				+ " = :field");
		query.setParameter("field", value);
		return this.beansToCrudEntities(this.refresh(query.getResultList()));
	}

	@Override
	public List<CrudAction> getActions() {
		Actions actions = this.persistentClass.getAnnotation(Actions.class);
		List<CrudAction> crudActions;

		if (actions != null) {
			crudActions = new ArrayList<CrudAction>(actions.value().length);
			for (Action action : actions.value()) {
				crudActions.add(new SimpleCrudAction(action.name(), action
						.requiresEntity(), action.requiresConfirmation(),
						action.showInList(), action.showInForm()));
			}
		} else {
			crudActions = new ArrayList<CrudAction>(0);
		}

		return crudActions;
	}

	@Override
	public CrudEntity<T> performAction(CrudAction action, CrudEntity<T> entity) {
		if (CrudAction.NEW.equals(action.getName())) {
			return new JpaCrudEntity<T>(
					this.wrapperFactory.wrapNewBean(this.persistentClass),
					this.entityManager, this.pkName);
		} else if (CrudAction.EDIT.equals(action.getName())) {
			return entity;
		} else if (CrudAction.UPDATE.equals(action.getName())) {
			this.entityManager.merge(entity.getEntity());
			return null;
		} else if (CrudAction.DELETE.equals(action.getName())) {
			this.entityManager.remove(this.entityManager.merge(entity
					.getEntity()));
			return null;
		} else {
			throw new IllegalArgumentException("Invalid action: " + action);
		}
	}

	private boolean isBasicType(Class<?> type) {
		return type.isPrimitive() || String.class.isAssignableFrom(type)
				|| Number.class.isAssignableFrom(type)
				|| Boolean.class.isAssignableFrom(type)
				|| Date.class.isAssignableFrom(type);
	}

	@SuppressWarnings("unchecked")
	private <K> List<K> findAll(Class<?> type) {
		Query query = this.entityManager.createQuery("from " + type.getName());
		return this.refresh(query.getResultList());
	}
	
	// TODO: estos dos metodos es un workaround feo, pero el cacheo
	private <K> List<K> refresh(List<K> beans) {
		List<K> refreshedList = new ArrayList<K>(beans.size());
		for (K bean:beans) {
			K refreshedBean = this.refresh(bean);
			if (refreshedBean != null) {
				refreshedList.add(refreshedBean);
			}
		}
		return refreshedList;
	}

	private <K> K refresh(K bean) {
		try {
			this.entityManager.refresh(bean);
			return bean;
		} catch (IllegalArgumentException e) {
			/*
			 * entityManager.refresh throws this exception if a previously
			 * deleted entity is intended to be read. Since this is equivalent
			 * to not finding the entity, null is returned.
			 */
			return null;
		}
	}

	private <K> List<CrudEntity<K>> beansToCrudEntities(List<K> beans) {
		List<CrudEntity<K>> crudEntities = new ArrayList<CrudEntity<K>>(
				beans.size());
		for (K bean : beans) {
			crudEntities.add(this.beanToCrudEntity(bean));
		}

		return crudEntities;
	}

	private <K> CrudEntity<K> beanToCrudEntity(K bean) {
		return new JpaCrudEntity<K>(this.wrapperFactory.wrapBean(bean),
				this.entityManager, this.pkName);
	}

	/* Setters */
	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;

		for (Field field : persistentClass.getDeclaredFields()) {
			if (field.isAnnotationPresent(Id.class)) {
				this.pkName = field.getName();
				break;
			}
		}
	}

	public void setWrapperFactory(WrapperFactory wrapperFactory) {
		this.wrapperFactory = wrapperFactory;
	}

	private interface ShowValidator {
		boolean mustShow(Show show);
	}
}
