package ar.com.oxen.nibiru.sample.showcase;

import ar.com.oxen.commons.eventbus.api.EventHandlerMethod;
import ar.com.oxen.nibiru.module.utils.AbstractModuleConfigurator;
import ar.com.oxen.nibiru.sample.eventbus.EventBusSampleEvent;
import ar.com.oxen.nibiru.sample.mail.MailSampleEvent;

public class ShowcaseModuleConfigurator
		extends
		AbstractModuleConfigurator<ShowcaseViewFactory, ShowcasePresenterFactory> {
	private static final String MENU_EXTENSION = "ar.com.oxen.nibiru.menu.sample.showcase";

	@Override
	protected void configure() {
		this.registerSubMenu("sample.showcase", MENU_EXTENSION,
				"ar.com.oxen.nibiru.menu", 3);

		this.registerMenu("sample.mail", MENU_EXTENSION, MailSampleEvent.class,
				null, null);

		this.registerMenu("sample.eventbus", MENU_EXTENSION,
				EventBusSampleEvent.class, null, null);
	}

	@EventHandlerMethod
	public void onMailSample(MailSampleEvent event) {
		this.activate(this.getViewFactory().createMailSampleDisplay(), this
				.getPresenterFactory().createMailSamplePresenter());
	}

	@EventHandlerMethod
	public void onEventBusSample(EventBusSampleEvent event) {
		this.activate(this.getViewFactory().createEventBusSampleDisplay(), this
				.getPresenterFactory().createEventBusSamplePresenter());
	}
}
