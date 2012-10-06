package ar.com.oxen.nibiru.security.db.ui.impl;

import org.springframework.security.authentication.encoding.PasswordEncoder;

import ar.com.oxen.nibiru.security.db.dao.UserDao;
import ar.com.oxen.nibiru.security.db.ui.ChangePasswordPresenter;
import ar.com.oxen.nibiru.security.db.ui.SecurityPresenterFactory;

public class GenericSecurityPresenterFactory implements
		SecurityPresenterFactory {
	private UserDao userDao;
	private PasswordEncoder passwordEncoder;

	public GenericSecurityPresenterFactory() {
		super();
	}

	@Override
	public ChangePasswordPresenter createChangePasswordPresenter() {
		return new GenericChangePasswordPresenter(this.userDao,
				this.passwordEncoder);
	}

	public void setUserDao(UserDao userDao) {
		this.userDao = userDao;
	}

	public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
		this.passwordEncoder = passwordEncoder;
	}
}
