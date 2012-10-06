package ar.com.oxen.nibiru.security.db.spring;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.NoResultException;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ar.com.oxen.nibiru.security.db.dao.UserDao;
import ar.com.oxen.nibiru.security.db.domain.Role;
import ar.com.oxen.nibiru.security.db.domain.RoleGroup;
import ar.com.oxen.nibiru.security.db.domain.User;

public class DaoUserDetailsService implements UserDetailsService {
	private UserDao userDao;
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			User user = this.userDao.findUserByUsername(username);

			Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(
					user.getRoles().size());

			for (Role role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}
			for (RoleGroup group : user.getGroups()) {
				for (Role role : group.getRoles()) {
					authorities.add(new SimpleGrantedAuthority(role.getName()));
				}
			}

			String password;
			/*
			 * If password is null or empty, create a hash for empty string
			 * (login reset)
			 */
			if (user.getPassword() == null || user.getPassword().equals("")) {
				password = this.passwordEncoder.encodePassword("", null);
			} else {
				password = user.getPassword();
			}

			return new org.springframework.security.core.userdetails.User(
					user.getName(), password, authorities);
		} catch (NoResultException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

}
