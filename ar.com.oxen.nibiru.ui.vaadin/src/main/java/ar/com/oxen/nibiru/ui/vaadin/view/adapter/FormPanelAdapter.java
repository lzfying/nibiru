package ar.com.oxen.nibiru.ui.vaadin.view.adapter;

import ar.com.oxen.nibiru.ui.api.view.FormPanel;
import ar.com.oxen.nibiru.ui.utils.view.AbstractAdapter;

import com.vaadin.ui.Component;
import com.vaadin.ui.Field;
import com.vaadin.ui.Form;

public class FormPanelAdapter extends AbstractComponentAdapter<Form> implements
		FormPanel {
	private int fieldCount = 0;

	public FormPanelAdapter(Form form) {
		super(form);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addComponent(ar.com.oxen.nibiru.ui.api.view.Component component) {
		AbstractAdapter<Component> adaptedComponent = (AbstractAdapter<Component>) component;

		Component vaadinComponent = adaptedComponent.getAdapted();

		this.getAdapted().getLayout().addComponent(vaadinComponent);

		if (vaadinComponent instanceof Field) {
			this.getAdapted().addField(this.fieldCount++,
					(Field) vaadinComponent);
		}
	}

	@Override
	public void setValue(String value) {
		this.getAdapted().setCaption(value);
	}

	@Override
	public String getValue() {
		return this.getAdapted().getCaption();
	}

	@Override
	public boolean isValid() {
		return this.getAdapted().isValid();
	}
}
