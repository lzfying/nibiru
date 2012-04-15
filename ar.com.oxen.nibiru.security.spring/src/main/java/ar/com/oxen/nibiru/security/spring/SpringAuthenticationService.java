package ar.com.oxen.nibiru.security.spring;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import ar.com.oxen.nibiru.security.api.AuthenticationService;
import ar.com.oxen.nibiru.security.api.BadCredentialsException;
import ar.com.oxen.nibiru.session.api.Session;

public class SpringAuthenticationService implements AuthenticationService {
	private Session session;
	final static String AUTHENTICATION_KEY = "ar.com.oxen.nibiru.security.spring.authentication";
	private AuthenticationManager authenticationManager;

	@Override
	public void login(String user, String password)
			throws BadCredentialsException {
		try {
			Authentication authentication = this.authenticationManager
					.authenticate(new UsernamePasswordAuthenticationToken(user,
							password));
			this.session.put(AUTHENTICATION_KEY, authentication);
		} catch (AuthenticationException e) {
			throw new BadCredentialsException();
		}
	}

	@Override
	public void logout() {
		this.session.remove(AUTHENTICATION_KEY);
	}

	@Override
	public String getLoggedUserName() {
		UsernamePasswordAuthenticationToken authentication = this.session.get(AUTHENTICATION_KEY);
		return authentication.getName();
	}

	public void setSession(Session session) {
		this.session = session;
	}

	public void setAuthenticationManager(
			AuthenticationManager authenticationManager) {
		this.authenticationManager = authenticationManager;
	}
}
