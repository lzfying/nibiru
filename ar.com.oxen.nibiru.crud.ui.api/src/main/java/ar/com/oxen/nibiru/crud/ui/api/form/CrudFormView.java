package ar.com.oxen.nibiru.crud.ui.api.form;

import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.mvp.HasCloseWidget;
import ar.com.oxen.nibiru.ui.api.mvp.View;

public interface CrudFormView extends View, HasCloseWidget {
	void setEntityName(String entityName);

	void addField(CrudField crudField, CrudEntity<?> crudEntity);

	void setFieldValue(String fieldName, Object value);
	
	Object getFieldValue(String fieldName);
	
	void setFieldError(String fieldName, String errorCode);

	void addEntityAction(String label, ClickHandler clickHandler);
}
