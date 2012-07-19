package ar.com.oxen.nibiru.crud.ui.generic.view;

import ar.com.oxen.nibiru.crud.ui.api.CrudViewFactory;
import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.view.Button;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;
import ar.com.oxen.nibiru.ui.api.view.Window;
import ar.com.oxen.nibiru.ui.utils.view.AbstractWindowViewAdapter;
import ar.com.oxen.nibiru.ui.utils.dialog.DialogBuilder;

public abstract class AbstractCrudView extends
		AbstractWindowViewAdapter<Window> {

	public AbstractCrudView(Window adapted, ViewFactory viewFactory,
			MessageSource messageSource) {
		super(adapted, viewFactory, messageSource);
	}

	protected String i18nAction(String action) {
		return this.getMessageSource().getMessage(
				CrudViewFactory.I18N_ACTION_PREFIX + action);
	}

	protected Button createActionButton(String label,
			boolean requiresConfirmation, ClickHandler clickHandler) {
		Button button = this.getViewFactory().buildButton();
		String action = this.i18nAction(label);
		button.setValue(action);
		button.setClickHandler(this.addConfirmation(action,
				requiresConfirmation, clickHandler));
		return button;
	}

	protected ClickHandler addConfirmation(final String action,
			boolean requiresConfirmation, final ClickHandler clickHandler) {
		if (requiresConfirmation) {
			return new ClickHandler() {

				@Override
				public void onClick() {
					new DialogBuilder(getViewFactory())
							.title(getMessageSource().getMessage(
									"ar.com.oxen.nibiru.app.confirmation"))
							.message(
									getMessageSource()
											.getMessage(
													"ar.com.oxen.nibiru.app.areYouSure",
													action.toLowerCase()))
							.button(getMessageSource().getMessage(
									"ar.com.oxen.nibiru.app.ok"), clickHandler)
							.button(getMessageSource().getMessage(
									"ar.com.oxen.nibiru.app.cancel")).build()
							.show();
				}
			};
		} else {
			return clickHandler;
		}
	}
}
