package ar.com.oxen.nibiru.security.generic;

import static ar.com.oxen.nibiru.security.generic.GenericAuthenticationService.USER_DATA_KEY;
import ar.com.oxen.nibiru.security.api.AuthorizationService;
import ar.com.oxen.nibiru.security.manager.api.UserData;
import ar.com.oxen.nibiru.session.api.Session;

public class GenericAuthorizationService implements AuthorizationService {
	private Session session;

	@Override
	public boolean isCallerInRole(String role) {
		UserData userData = this.session.get(USER_DATA_KEY);

		if (userData != null) {
			for (String currentRole : userData.getRoles()) {
				if (role.equals(currentRole)) {
					return true;
				}
			}
		}
		return false;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
