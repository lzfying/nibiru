package ar.com.oxen.nibiru.security.manager.api;

public interface SecurityManager {
	void changePassword(String username, String oldPassword, String newPassword)
			throws UserNotFoundException, InvalidOldPassword;

	Iterable<String> getRoles(String username) throws UserNotFoundException;
}
