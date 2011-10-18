package ar.com.oxen.nibiru.crud.bean.manager;

import java.util.Collection;

import ar.com.oxen.commons.bean.api.BeanWrapper;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;

public abstract class AbstractBeanCrudEntity<T> implements CrudEntity<T> {
	private BeanWrapper<T> bean;

	public AbstractBeanCrudEntity(BeanWrapper<T> bean) {
		super();
		this.bean = bean;
	}

	@Override
	public Object getValue(CrudField field) {
		return this.getValue(field.getName());
	}

	@Override
	public void setValue(CrudField field, Object value) {
		this.setValue(field.getName(), value);
	}

	@Override
	public Object getValue(String fieldName) {
		return this.bean.getProperty(fieldName);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setValue(String fieldName, Object value) {
		Collection<Object> oldCollection = null;
		if (value instanceof Collection<?>) {
			oldCollection = (Collection<Object>) this.bean
					.getProperty(fieldName);
		}
		if (oldCollection != null) {
			Collection<Object> newCollection = (Collection<Object>) value;
			oldCollection.clear();
			oldCollection.addAll(newCollection);
		} else {
			this.bean.setProperty(fieldName, value);
		}
	}

	@Override
	public T getEntity() {
		return this.bean.getBean();
	}

	@Override
	public String getEntityTypeName() {
		return this.bean.getBean().getClass().getName();
	}

	@Override
	public Iterable<Object> getAvailableValues(CrudField field) {
		return this.getAvailableValues(field.getName());
	}

	protected BeanWrapper<T> getBean() {
		return bean;
	}
}
