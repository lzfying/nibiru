package ar.com.oxen.nibiru.sample.showcase;

import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.sample.mail.MailSamplePresenter;
import ar.com.oxen.nibiru.sample.mail.MailSampleView;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;

public class ShowcaseViewFactory {
	private ViewFactory viewFactory;
	private MessageSource messageSource;

	public MailSamplePresenter.Display createMailSampleDisplay() {
		return new MailSampleView(this.viewFactory, this.messageSource);
	}

	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
