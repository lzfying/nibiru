package ar.com.oxen.nibiru.security.db.spring;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import ar.com.oxen.nibiru.security.db.domain.RoleGroup;
import ar.com.oxen.nibiru.security.db.domain.Role;
import ar.com.oxen.nibiru.security.db.domain.User;

public class JpaUserDetailsService implements UserDetailsService {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Override
	public UserDetails loadUserByUsername(String username)
			throws UsernameNotFoundException {
		try {
			Query query = this.entityManager
					.createQuery("from User where name = ?");
			query.setParameter(1, username);
			User user = (User) query.getSingleResult();
			
			this.entityManager.refresh(user); // Damn Hibernate cache

			Collection<GrantedAuthority> authorities = new HashSet<GrantedAuthority>(
					user.getRoles().size());

			for (Role role : user.getRoles()) {
				authorities.add(new SimpleGrantedAuthority(role.getName()));
			}
			for (RoleGroup group: user.getGroups()) {
				for (Role role : group.getRoles()) {
					authorities.add(new SimpleGrantedAuthority(role.getName()));
				}
			}

			return new org.springframework.security.core.userdetails.User(
					user.getName(), user.getPassword(), authorities);
		} catch (NoResultException e) {
			throw new UsernameNotFoundException(e.getMessage(), e);
		}
	}
}
