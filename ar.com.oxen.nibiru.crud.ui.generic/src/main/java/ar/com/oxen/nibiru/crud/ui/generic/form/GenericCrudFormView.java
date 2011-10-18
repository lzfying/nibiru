package ar.com.oxen.nibiru.crud.ui.generic.form;

import java.util.HashMap;
import java.util.Map;

import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;
import ar.com.oxen.nibiru.crud.ui.api.CrudViewFactory;
import ar.com.oxen.nibiru.crud.ui.api.form.CrudFormView;
import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.view.Button;
import ar.com.oxen.nibiru.ui.api.view.ComboBox;
import ar.com.oxen.nibiru.ui.api.view.FormField;
import ar.com.oxen.nibiru.ui.api.view.ListSelect;
import ar.com.oxen.nibiru.ui.api.view.Panel;
import ar.com.oxen.nibiru.ui.api.view.PasswordField;
import ar.com.oxen.nibiru.ui.api.view.TextArea;
import ar.com.oxen.nibiru.ui.api.view.TextField;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;
import ar.com.oxen.nibiru.ui.api.view.Window;
import ar.com.oxen.nibiru.ui.utils.view.AbstractWindowViewAdapter;

public class GenericCrudFormView extends AbstractWindowViewAdapter<Window>
		implements CrudFormView {
	private Panel fieldsTabPanel;
	private Map<String, Panel> fieldsPanels;
	private Panel actionsPanel;
	private Map<String, FormField<Object>> fieldToValue;

	public GenericCrudFormView(ViewFactory viewFactory,
			MessageSource messageSource) {
		super(viewFactory.buildWindow(), viewFactory, messageSource);

		this.fieldsTabPanel = viewFactory.buildTabPanel();
		this.fieldsTabPanel.setWidth(400);
		this.getAdapted().addComponent(fieldsTabPanel);

		this.fieldsPanels = new HashMap<String, Panel>();
		this.fieldToValue = new HashMap<String, FormField<Object>>();

		Panel buttonsPanel = viewFactory.buildHorizontalPanel();
		this.getAdapted().addComponent(buttonsPanel);

		actionsPanel = viewFactory.buildHorizontalPanel();
		buttonsPanel.addComponent(actionsPanel);

		this.addClose(buttonsPanel);
	}

	@Override
	public void setEntityName(String entityName) {
		String translatedEntityName = this.getMessageSource().getMessage(
				CrudViewFactory.I18N_ENTITY_PREFIX + entityName);
		this.getAdapted().setValue(
				this.getMessageSource().getMessage(
						"ar.com.oxen.nibiru.app.edit", translatedEntityName));
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addField(CrudField crudField, CrudEntity<?> crudEntity) {
		FormField<?> field;

		CrudField.FormInfo formInfo = crudField.getFormInfo();

		switch (formInfo.getWidgetType()) {
		case TEXT_FIELD:
			TextField<?> textField = this.getViewFactory().buildTextField(
					(Class<?>) crudField.getType());

			textField.setMaxLength(formInfo.getMaxLength());

			field = textField;
			break;

		case PASSWORD_FIELD:
			PasswordField<?> passowrdField = this.getViewFactory()
					.buildPasswordField((Class<?>) crudField.getType());
			passowrdField.setMaxLength(formInfo.getMaxLength());

			field = passowrdField;
			break;

		case TEXT_AREA:
			TextArea<?> textArea = this.getViewFactory().buildTextArea(
					(Class<?>) crudField.getType());
			textArea.setMaxLength(formInfo.getMaxLength());
			textArea.setWidth(400);
			textArea.setHeight(200);
			this.fieldsTabPanel.setWidth(600);

			field = textArea;
			break;

		case DATE_FIELD:
			field = (FormField<?>) this.getViewFactory().buildDateField();
			break;

		case TIME_FIELD:
			field = (FormField<?>) this.getViewFactory().buildTimeField();
			break;

		case CHECK_BOX:
			field = (FormField<?>) this.getViewFactory().buildCheckBox();
			break;

		case COMBO_BOX:
			ComboBox<?> combo = this.getViewFactory().buildComboBox(
					(Class<?>) crudField.getType());

			for (Object item : crudEntity.getAvailableValues(crudField)) {
				((ComboBox<Object>) combo).addItem(item);
			}

			field = combo;
			break;

		case MULTISELECT:
			ListSelect<?> select = this.getViewFactory().buildListSelect(
					(Class<?>) crudField.getType());
			select.setMultiSelect(true);

			for (Object item : crudEntity.getAvailableValues(crudField)) {
				((ListSelect<Object>) select).addItem(item);
			}

			field = select;
			break;

		default:
			throw new IllegalArgumentException("invalid widget type:"
					+ formInfo.getWidgetType());
		}

		field.setCaption(this.getMessageSource().getMessage(
				CrudViewFactory.I18N_FIELD_PREFIX + crudField.getName()));
		field.setReadOnly(formInfo.isReadonly());

		String tabName = crudField.getFormInfo().getTab();
		Panel fieldsPanel = this.fieldsPanels.get(tabName);

		if (fieldsPanel == null) {
			fieldsPanel = this.getViewFactory().buildFormPanel();
			fieldsPanel.setValue(this.getMessageSource().getMessage(
					CrudViewFactory.I18N_TAB_PREFIX + tabName));
			this.fieldsTabPanel.addComponent(fieldsPanel);
			this.fieldsPanels.put(tabName, fieldsPanel);
		}

		fieldsPanel.addComponent(field);
		this.fieldToValue.put(crudField.getName(), (FormField<Object>) field);
	}

	@Override
	public void setFieldValue(String fieldName, Object value) {
		this.fieldToValue.get(fieldName).setValue(value);
	}

	@Override
	public Object getFieldValue(String fieldName) {
		return this.fieldToValue.get(fieldName).getValue();
	}

	@Override
	public void setFieldError(String fieldName, String errorCode) {
		this.fieldToValue.get(fieldName).setErrorMessage(
				this.getMessageSource().getMessage(
						CrudViewFactory.I18N_ERROR_PREFIX + errorCode));
	}

	@Override
	public void addEntityAction(String label, ClickHandler clickHandler) {
		Button button = this.getViewFactory().buildButton();
		button.setValue(this.getMessageSource().getMessage(
				CrudViewFactory.I18N_ACTION_PREFIX + label));
		button.setClickHandler(clickHandler);
		this.actionsPanel.addComponent(button);
	}
}
