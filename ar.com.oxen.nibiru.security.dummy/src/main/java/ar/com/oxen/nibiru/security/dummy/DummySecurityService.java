package ar.com.oxen.nibiru.security.dummy;

import ar.com.oxen.nibiru.security.api.AuthenticationService;
import ar.com.oxen.nibiru.security.api.AuthorizationService;
import ar.com.oxen.nibiru.security.api.BadCredentialsException;

public class DummySecurityService implements AuthenticationService,
		AuthorizationService {

	@Override
	public void login(String user, String password) {
		if (!"pepe".equals(user) || !"popo".equals(password)) {
			throw new BadCredentialsException();
		}
		System.out.println("- El usuario ingreso al sistema");
	}

	@Override
	public void logout() {
		System.out.println("- El usuario salio del sistema");
	}

	@Override
	public boolean isCallerInRole(String role) {
		System.out.println("- Se pregunto si el usuario tiene rol " + role);
		return true;
	}

	@Override
	public String getLoggedUserName() {
		return "pepe";
	}

}
