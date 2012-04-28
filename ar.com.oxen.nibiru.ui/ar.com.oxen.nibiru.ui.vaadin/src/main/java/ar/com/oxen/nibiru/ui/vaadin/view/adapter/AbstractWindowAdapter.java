package ar.com.oxen.nibiru.ui.vaadin.view.adapter;

import ar.com.oxen.nibiru.ui.utils.view.AbstractAdapter;

import com.vaadin.ui.Window;
import com.vaadin.ui.Component;

public abstract class AbstractWindowAdapter extends AbstractAdapter<Window>
		implements ar.com.oxen.nibiru.ui.api.view.Window {

	public AbstractWindowAdapter(Window adapted) {
		super(adapted);
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
	public void close() {
		if (this.getAdapted().getParent() != null) {
			this.getAdapted().getParent().removeWindow(this.getAdapted());
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void addComponent(ar.com.oxen.nibiru.ui.api.view.Component component) {
		AbstractAdapter<Component> adaptedComponent = (AbstractAdapter<Component>) component;
		this.getAdapted().addComponent(adaptedComponent.getAdapted());
	}
}
