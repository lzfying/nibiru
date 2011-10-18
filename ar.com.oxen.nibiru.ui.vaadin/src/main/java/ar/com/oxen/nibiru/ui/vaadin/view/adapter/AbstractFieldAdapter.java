package ar.com.oxen.nibiru.ui.vaadin.view.adapter;

import com.vaadin.terminal.UserError;
import com.vaadin.ui.AbstractField;

public abstract class AbstractFieldAdapter<T, K extends AbstractField> extends
		AbstractComponentAdapter<K> implements
		ar.com.oxen.nibiru.ui.api.view.FormField<T> {
	public AbstractFieldAdapter(K adapted) {
		super(adapted);
	}

	@Override
	public String getCaption() {
		return this.getAdapted().getCaption();
	}

	@Override
	public void setCaption(String caption) {
		this.getAdapted().setCaption(caption);
	}

	@Override
	public boolean isReadOnly() {
		return this.getAdapted().isReadOnly();
	}

	@Override
	public void setReadOnly(boolean readOnly) {
		this.getAdapted().setReadOnly(readOnly);
	}

	@Override
	public void setErrorMessage(String errorMessage) {
		if (errorMessage != null) {
			this.getAdapted().setComponentError(new UserError(errorMessage));
		} else {
			this.getAdapted().setComponentError(null);
		}
	}
}
