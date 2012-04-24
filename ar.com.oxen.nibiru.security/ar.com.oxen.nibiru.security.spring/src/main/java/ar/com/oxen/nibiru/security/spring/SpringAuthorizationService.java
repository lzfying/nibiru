package ar.com.oxen.nibiru.security.spring;

import static ar.com.oxen.nibiru.security.spring.SpringAuthenticationService.AUTHENTICATION_KEY;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;

import ar.com.oxen.nibiru.security.api.AuthorizationService;
import ar.com.oxen.nibiru.session.api.Session;

public class SpringAuthorizationService implements AuthorizationService {
	private Session session;

	@Override
	public boolean isCallerInRole(String role) {
		Authentication authentication = this.session.get(AUTHENTICATION_KEY);

		if (authentication != null) {
			for (GrantedAuthority authority : authentication.getAuthorities()) {
				if (role.equals(authority.getAuthority())) {
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
