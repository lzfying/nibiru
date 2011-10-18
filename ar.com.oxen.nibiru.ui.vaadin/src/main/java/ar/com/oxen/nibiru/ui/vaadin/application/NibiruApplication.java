package ar.com.oxen.nibiru.ui.vaadin.application;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.application.api.ApplicationStartEvent;
import ar.com.oxen.nibiru.i18n.api.LocaleHolder;

import com.vaadin.Application;

public class NibiruApplication extends Application {
	/**
	 * Serial ID.
	 */
	private static final long serialVersionUID = -8241304827319878154L;
	private EventBus eventBus;
	private LocaleHolder localeHolder;

	@Override
	public void init() {
		this.localeHolder.setLocale(this.getLocale());
		this.eventBus.fireEvent(new ApplicationStartEvent());
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setLocaleHolder(LocaleHolder localeHolder) {
		this.localeHolder = localeHolder;
	}

}
