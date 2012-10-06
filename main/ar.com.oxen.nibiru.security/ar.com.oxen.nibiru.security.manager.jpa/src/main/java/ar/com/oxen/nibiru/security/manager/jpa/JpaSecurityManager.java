package ar.com.oxen.nibiru.security.manager.jpa;

import java.util.Collection;
import java.util.HashSet;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import ar.com.oxen.nibiru.security.manager.api.InvalidOldPassword;
import ar.com.oxen.nibiru.security.manager.api.SecurityManager;
import ar.com.oxen.nibiru.security.manager.api.UserNotFoundException;
import ar.com.oxen.nibiru.security.manager.jpa.domain.Role;
import ar.com.oxen.nibiru.security.manager.jpa.domain.RoleGroup;
import ar.com.oxen.nibiru.security.manager.jpa.domain.User;

public class JpaSecurityManager implements SecurityManager {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Override
	public void changePassword(String username, String oldPassword,
			String newPassword) {
		User user = this.findUserByUsername(username);
		String currentPassword = user.getPassword() != null ? user
				.getPassword() : "";
		String validationPassword = oldPassword != null ? oldPassword : "";

		if (!currentPassword.equals(validationPassword)) {
			throw new InvalidOldPassword();
		}

		user.setPassword(newPassword);

		this.entityManager.persist(user);
	}

	@Override
	public String getPassword(String username) throws UserNotFoundException {
		return this.findUserByUsername(username).getPassword();
	}

	@Override
	public Iterable<String> getRoles(String username) {
		User user = this.findUserByUsername(username);

		Collection<String> roles = new HashSet<String>();
		for (Role role : user.getRoles()) {
			roles.add(role.getName());
		}
		for (RoleGroup group : user.getGroups()) {
			for (Role role : group.getRoles()) {
				roles.add(role.getName());
			}
		}

		return roles;
	}

	private User findUserByUsername(String username) {
		try {
			Query query = this.entityManager
					.createQuery("from User where name = ?");
			query.setParameter(1, username);
			User user = (User) query.getSingleResult();

			this.entityManager.refresh(user); // Damn Hibernate cache
			return user;
		} catch (NoResultException e) {
			throw new UserNotFoundException();
		}
	}

}
