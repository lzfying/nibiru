package ar.com.oxen.nibiru.application.generic.presenter;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.application.api.ApplicationPresenterFactory;
import ar.com.oxen.nibiru.application.api.about.AboutView;
import ar.com.oxen.nibiru.application.api.login.LoginView;
import ar.com.oxen.nibiru.application.api.main.MainView;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.security.api.AuthenticationService;
import ar.com.oxen.nibiru.ui.api.mvp.Presenter;

public class GenericApplicationPresenterFactory implements
		ApplicationPresenterFactory {
	private ExtensionPointManager extensionPointManager;
	private AuthenticationService authenticationService;
	private EventBus eventBus;

	@Override
	public Presenter<LoginView> buildLoginPresenter() {
		return new GenericLoginPresenter(this.authenticationService,
				this.eventBus);
	}

	@Override
	public Presenter<MainView> buildMainPresenter() {
		return new GenericMainPresenter(this.extensionPointManager,
				this.authenticationService);
	}

	@Override
	public Presenter<AboutView> buildAboutPresenter() {
		return new GenericAboutPresenter();
	}

	public void setExtensionPointManager(
			ExtensionPointManager extensionPointManager) {
		this.extensionPointManager = extensionPointManager;
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
