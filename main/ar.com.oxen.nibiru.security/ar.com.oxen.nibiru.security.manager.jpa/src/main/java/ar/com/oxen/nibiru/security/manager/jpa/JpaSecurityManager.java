package ar.com.oxen.nibiru.security.manager.jpa;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.Query;

import ar.com.oxen.nibiru.security.manager.api.InvalidOldPassword;
import ar.com.oxen.nibiru.security.manager.api.SecurityManager;
import ar.com.oxen.nibiru.security.manager.api.UserData;
import ar.com.oxen.nibiru.security.manager.api.UserNotFoundException;
import ar.com.oxen.nibiru.security.manager.jpa.domain.User;

public class JpaSecurityManager implements SecurityManager {
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
	public UserData getUserData(String username) throws UserNotFoundException {
		return new UserAdapter(this.findUserByUsername(username));
	}

	private User findUserByUsername(String username) {
		try {
			Query query = this.entityManager
					.createQuery("select u from User u where u.username = :usernameParam");
			query.setParameter("usernameParam", username);
			User user = (User) query.getSingleResult();

			// TODO: check this
			this.entityManager.refresh(user); // Damn Hibernate cache
			return user;
		} catch (NoResultException e) {
			throw new UserNotFoundException();
		}
	}

	public void setEntityManager(EntityManager entityManager) {
		this.entityManager = entityManager;
	}
}
