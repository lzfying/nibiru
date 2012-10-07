package ar.com.oxen.nibiru.security.module.ui.impl;

import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.security.module.ui.ChangePasswordView;
import ar.com.oxen.nibiru.security.module.ui.SecurityViewFactory;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;

public class GenericSecurityViewFactory implements SecurityViewFactory {
	private ViewFactory viewFactory;
	private MessageSource messageSource;

	@Override
	public ChangePasswordView createChangePasswordView() {
		return new GenericChangePasswordView(this.viewFactory,
				this.messageSource);
	}

	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
