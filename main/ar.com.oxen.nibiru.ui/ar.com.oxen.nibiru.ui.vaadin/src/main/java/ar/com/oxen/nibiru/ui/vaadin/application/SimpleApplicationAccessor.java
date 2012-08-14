package ar.com.oxen.nibiru.ui.vaadin.application;

import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.i18n.api.LocaleHolder;
import ar.com.oxen.nibiru.ui.vaadin.api.ApplicationAccessor;

import com.vaadin.Application;
import com.vaadin.terminal.gwt.server.WebApplicationContext;

public class SimpleApplicationAccessor implements ApplicationAccessor {
	private EventBus eventBus;
	private LocaleHolder localeHolder;

	@Override
	public Application createApplication() {
		NibiruApplication nibiruApplication = new NibiruApplication();
		nibiruApplication.setEventBus(this.eventBus);
		nibiruApplication.setLocaleHolder(this.localeHolder);
		return nibiruApplication;
	}

	@Override
	public Application getApplication() {
		WebApplicationContext context = WebApplicationContext
				.getApplicationContext(this.currentSession());

		if (context.getApplications().size() > 0) {
			return context.getApplications().iterator().next();
		} else {
			throw new IllegalStateException("No Vaadin App on context");
		}
	}

	private ServletRequestAttributes currentAttributes() {
		return (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
	}

	private HttpSession currentSession() {
		return this.currentAttributes().getRequest().getSession(true);
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setLocaleHolder(LocaleHolder localeHolder) {
		this.localeHolder = localeHolder;
	}
}
