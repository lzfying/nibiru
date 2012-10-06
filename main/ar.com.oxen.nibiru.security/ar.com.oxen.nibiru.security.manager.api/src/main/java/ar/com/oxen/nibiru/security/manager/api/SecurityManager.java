package ar.com.oxen.nibiru.security.manager.api;

public interface SecurityManager {
	void changePassword(String username, String oldPassword, String newPassword)
			throws UserNotFoundException, InvalidOldPassword;

	String getPassword(String username) throws UserNotFoundException;

	Iterable<String> getRoles(String username) throws UserNotFoundException;
}
