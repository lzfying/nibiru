package ar.com.oxen.nibiru.crud.manager.jpa;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.Id;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.mvel2.MVEL;

import ar.com.oxen.commons.bean.api.PropertyDescriptor;
import ar.com.oxen.commons.bean.api.WrapperFactory;
import ar.com.oxen.nibiru.crud.bean.annotation.Action;
import ar.com.oxen.nibiru.crud.bean.annotation.Actions;
import ar.com.oxen.nibiru.crud.bean.annotation.Filter;
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
import ar.com.oxen.nibiru.security.api.AuthorizationService;

public class JpaCrudManager<T> implements CrudManager<T>,
		CrudActionExtension<T> {
	@PersistenceContext
	private EntityManager entityManager;
	private Class<T> persistentClass;
	private WrapperFactory wrapperFactory;
	private String pkName;
	private String filter;
	private AuthorizationService authorizationService;

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

	private List<CrudField> getFormFields() {
		return this.fieldNamesToCrudFields(new ShowValidator() {
			@Override
			public boolean mustShow(Show show) {
				return show.inForm();
			}
		});
	}

	private List<PropertyDescriptor> getPersistentProperties() {
		List<PropertyDescriptor> descriptors = new LinkedList<PropertyDescriptor>();
		Class<?> currentClass = this.persistentClass;
		while (currentClass != null
				&& currentClass.isAnnotationPresent(Entity.class)) {
			Collections.addAll(descriptors,
					this.wrapperFactory.wrapClass(currentClass)
							.getPropertyDescriptors());
			currentClass = currentClass.getSuperclass();
		}

		return descriptors;
	}

	private List<CrudField> fieldNamesToCrudFields(ShowValidator showValidator) {
		List<PropertyDescriptor> descriptors = new LinkedList<PropertyDescriptor>();

		for (PropertyDescriptor descriptor : this.getPersistentProperties()) {
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
			String tab = CrudField.FormInfo.GENERAL_TAB;

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
		T bean = this.entityManager.find(this.persistentClass, id);
		if (bean != null) {
			return this.beanToCrudEntity(bean);
		} else {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<CrudEntity<T>> findByfield(String field, Object value) {
		StringBuilder sb = new StringBuilder();

		sb.append("from ");
		sb.append(this.persistentClass.getName());
		sb.append(" where ");
		sb.append(field);
		sb.append(" = :field");

		String computedFilter = this.computeFilter();
		if (computedFilter != null) {
			sb.append(" and ");
			sb.append(computedFilter);
		}

		Query query = this.entityManager.createQuery(sb.toString());
		query.setParameter("field", value);
		return this.beansToCrudEntities(query.getResultList());
	}

	@Override
	public List<CrudAction> getGlobalActions() {
		return this.buildActionList(false);
	}

	@Override
	public List<CrudAction> getEntityActions(CrudEntity<T> entity) {
		return this.buildActionList(true);
	}

	private List<CrudAction> buildActionList(boolean requiresEntity) {
		Actions actions = this.persistentClass.getAnnotation(Actions.class);
		List<CrudAction> crudActions;

		if (actions != null) {
			crudActions = new ArrayList<CrudAction>(actions.value().length);
			for (Action action : actions.value()) {
				if (action.requiresEntity() == requiresEntity) {
					boolean modifiesEntity = action.name().equals(
							CrudAction.UPDATE)
							|| action.name().equals(CrudAction.DELETE);
					
					crudActions.add(new SimpleCrudAction(action.name(), action
							.requiresEntity(), action.requiresConfirmation(),
							action.showInList(), modifiesEntity, action
									.showInForm(), action.allowedRoles()));
				}
			}
		} else {
			crudActions = new ArrayList<CrudAction>(0);
		}

		return crudActions;
	}	

	@Override
	public CrudEntity<T> performGlobalAction(CrudAction action) {
		if (CrudAction.NEW.equals(action.getName())) {
			return new JpaCrudEntity<T>(
					this.wrapperFactory.wrapNewBean(this.persistentClass),
					this.entityManager, this.pkName, this.getFormFields());
		} else {
			throw new IllegalArgumentException("Invalid action: " + action);
		}
	}
	
	@Override
	public CrudEntity<T> performEntityAction(CrudAction action, CrudEntity<T> entity) {
		if (CrudAction.EDIT.equals(action.getName())) {
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
		StringBuilder sb = new StringBuilder();

		sb.append("from ");
		sb.append(type.getName());

		String computedFilter = this.computeFilter();
		if (computedFilter != null) {
			sb.append(" where ");
			sb.append(computedFilter);
		}

		Query query = this.entityManager.createQuery(sb.toString());
		return query.getResultList();
	}

	private String computeFilter() {
		if (this.filter != null) {
			Map<String, Object> environment = new HashMap<String, Object>();
			environment.put("authz", this.authorizationService);

			Object computedFilter = MVEL.eval(this.filter, environment);

			if (computedFilter == null
					|| computedFilter.toString().trim().equals("")) {
				return null;
			} else {
				return computedFilter.toString();
			}
		} else {
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
				this.entityManager, this.pkName, this.getFormFields());
	}

	/* Setters */
	public void setPersistentClass(Class<T> persistentClass) {
		this.persistentClass = persistentClass;
		this.updatePkName();

		Filter filterAnnotation = persistentClass.getAnnotation(Filter.class);
		if (filterAnnotation != null) {
			this.filter = filterAnnotation.value();
		}
	}

	public void setWrapperFactory(WrapperFactory wrapperFactory) {
		this.wrapperFactory = wrapperFactory;
		this.updatePkName();
	}
	
	private void updatePkName() {
		if (this.pkName == null && this.wrapperFactory != null
				&& this.persistentClass != null) {
			for (PropertyDescriptor descriptor : this.getPersistentProperties()) {
				if (descriptor.getAnnotation(Id.class) != null) {
					this.pkName = descriptor.getName();
					break;
				}
			}
		}
	}

	public void setAuthorizationService(
			AuthorizationService authorizationService) {
		this.authorizationService = authorizationService;
	}

	private interface ShowValidator {
		boolean mustShow(Show show);
	}

	@Override
	public String[] getAllowedRoles() {
		return null;
	}
}
