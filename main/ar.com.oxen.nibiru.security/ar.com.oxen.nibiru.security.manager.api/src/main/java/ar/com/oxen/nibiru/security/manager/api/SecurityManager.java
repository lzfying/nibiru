package ar.com.oxen.nibiru.security.manager.api;

public interface SecurityManager {
	void changePassword(String username, String oldPassword, String newPassword)
			throws UserNotFoundException, InvalidOldPassword;

	UserData getUserData(String username) throws UserNotFoundException;
}
