package ar.com.oxen.nibiru.ui.vaadin.application;

import ar.com.oxen.nibiru.ui.vaadin.api.ApplicationAccessor;

import com.vaadin.Application;

public class SimpleApplicationAccessor implements ApplicationAccessor {
	private Application application;

	@Override
	public Application getApplication() {
		return this.application;
	}

	public void setApplication(Application application) {
		this.application = application;
	}
}
