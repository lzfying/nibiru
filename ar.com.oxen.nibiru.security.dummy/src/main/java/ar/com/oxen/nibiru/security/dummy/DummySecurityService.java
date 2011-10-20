package ar.com.oxen.nibiru.security.dummy;

import ar.com.oxen.nibiru.security.api.AuthenticationService;
import ar.com.oxen.nibiru.security.api.AuthorizationService;
import ar.com.oxen.nibiru.security.api.BadCredentialsException;

public class DummySecurityService implements AuthenticationService,
		AuthorizationService {

	@Override
	public void login(String user, String password) {
		if (!"guest".equals(user) || !"guest".equals(password)) {
			throw new BadCredentialsException();
		}
	}

	@Override
	public void logout() {
		System.out.println("- El usuario salio del sistema");
	}

	@Override
	public boolean isCallerInRole(String role) {
		return true;
	}

	@Override
	public String getLoggedUserName() {
		return "guest";
	}

}
