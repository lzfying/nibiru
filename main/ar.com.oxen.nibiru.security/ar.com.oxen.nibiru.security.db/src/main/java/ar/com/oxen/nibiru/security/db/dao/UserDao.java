package ar.com.oxen.nibiru.security.db.dao;

import ar.com.oxen.nibiru.security.db.domain.User;

public interface UserDao {
	User findUserByUsername(String username);

	void saveUser(User user);
}
