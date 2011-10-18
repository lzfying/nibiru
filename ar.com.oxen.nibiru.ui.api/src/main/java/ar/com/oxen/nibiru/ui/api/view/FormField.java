package ar.com.oxen.nibiru.ui.api.view;

import ar.com.oxen.nibiru.ui.api.mvp.HasValue;

public interface FormField<T> extends Component, HasValue<T> {
	String getCaption();

	void setCaption(String caption);

	boolean isReadOnly();

	void setReadOnly(boolean readOnly);
	
	void setErrorMessage(String errorMessage);
}
