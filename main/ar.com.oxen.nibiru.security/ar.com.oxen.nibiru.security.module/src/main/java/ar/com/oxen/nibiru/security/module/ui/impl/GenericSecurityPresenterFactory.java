package ar.com.oxen.nibiru.security.module.ui.impl;

import ar.com.oxen.nibiru.security.api.AuthenticationService;
import ar.com.oxen.nibiru.security.api.HashService;
import ar.com.oxen.nibiru.security.manager.api.SecurityManager;
import ar.com.oxen.nibiru.security.module.ui.ChangePasswordPresenter;
import ar.com.oxen.nibiru.security.module.ui.SecurityPresenterFactory;

public class GenericSecurityPresenterFactory implements
		SecurityPresenterFactory {
	private AuthenticationService authenticationService;
	private SecurityManager securityManager;
	private HashService hashService;

	public GenericSecurityPresenterFactory() {
		super();
	}

	@Override
	public ChangePasswordPresenter createChangePasswordPresenter() {
		return new GenericChangePasswordPresenter(this.authenticationService,
				this.securityManager, this.hashService);
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setHashService(HashService hashService) {
		this.hashService = hashService;
	}
}
