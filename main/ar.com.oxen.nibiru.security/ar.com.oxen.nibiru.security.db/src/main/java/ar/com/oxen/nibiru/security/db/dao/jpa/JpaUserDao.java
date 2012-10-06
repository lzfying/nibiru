package ar.com.oxen.nibiru.security.db.dao.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceContextType;
import javax.persistence.Query;

import ar.com.oxen.nibiru.security.db.dao.UserDao;
import ar.com.oxen.nibiru.security.db.domain.User;

public class JpaUserDao implements UserDao {
	@PersistenceContext(type = PersistenceContextType.EXTENDED)
	private EntityManager entityManager;

	@Override
	public User findUserByUsername(String username) {
		Query query = this.entityManager
				.createQuery("from User where name = ?");
		query.setParameter(1, username);
		User user = (User) query.getSingleResult();

		this.entityManager.refresh(user); // Damn Hibernate cache
		return user;
	}

	@Override
	public void saveUser(User user) {
		this.entityManager.persist(user);
	}
}
