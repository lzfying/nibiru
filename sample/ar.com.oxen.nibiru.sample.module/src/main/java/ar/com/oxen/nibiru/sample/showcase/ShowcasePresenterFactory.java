package ar.com.oxen.nibiru.sample.showcase;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.mail.api.MailService;
import ar.com.oxen.nibiru.sample.eventbus.EventBusSamplePresenter;
import ar.com.oxen.nibiru.sample.mail.MailSamplePresenter;

public class ShowcasePresenterFactory {
	private EventBus eventBus;
	private MailService mailService;

	public MailSamplePresenter createMailSamplePresenter() {
		return new MailSamplePresenter(this.eventBus, this.mailService);
	}

	public EventBusSamplePresenter createEventBusSamplePresenter() {
		return new EventBusSamplePresenter(this.eventBus);
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setMailService(MailService mailService) {
		this.mailService = mailService;
	}
}
