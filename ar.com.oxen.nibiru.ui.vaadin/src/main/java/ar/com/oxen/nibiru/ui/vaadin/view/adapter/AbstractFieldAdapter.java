package ar.com.oxen.nibiru.ui.vaadin.view.adapter;

import ar.com.oxen.nibiru.validation.api.ValidationException;
import ar.com.oxen.nibiru.validation.api.Validator;

import com.vaadin.data.Validator.InvalidValueException;
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

	@Override
	public void addValidator(Validator<T> validator) {
		this.getAdapted().addValidator(new ValidatorAdapter<T>(validator));
	}

	@Override
	public void removeValidator(Validator<T> validator) {
		this.getAdapted().removeValidator(new ValidatorAdapter<T>(validator));
	}

	@Override
	public void validate() throws ValidationException {
		try {
			this.getAdapted().validate();
		} catch (InvalidValueExceptionAdapter e) {
			throw e.getValidationException();
		} catch (InvalidValueException e) {
			throw new ValidationException(e.getMessage());
		}
	}
}
