package ar.com.oxen.nibiru.security.spring;

import java.util.Collection;
import java.util.HashSet;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ar.com.oxen.nibiru.security.api.HashService;
import ar.com.oxen.nibiru.security.manager.api.SecurityManager;
import ar.com.oxen.nibiru.security.manager.api.UserNotFoundException;

public class SecurityManagerUserDetailsService implements UserDetailsService {
	private SecurityManager securityManager;
	private HashService hashService;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>();

			for (String role : this.securityManager.getRoles(username)) {
				authorities.add(new SimpleGrantedAuthority(role));
			}

			String password = this.securityManager.getPassword(username);

			if (password == null || password.equals("")) {
				password = this.hashService.hash("");
			}

			return new User(username, password, authorities);
		} catch (UserNotFoundException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}

	public void setSecurityManager(SecurityManager securityManager) {
		this.securityManager = securityManager;
	}

	public void setHashService(HashService hashService) {
		this.hashService = hashService;
	}
}
