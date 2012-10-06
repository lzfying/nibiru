package ar.com.oxen.nibiru.security.db.ui.impl;

import org.springframework.security.authentication.encoding.PasswordEncoder;
import org.springframework.security.core.context.SecurityContextHolder;

import ar.com.oxen.nibiru.security.db.dao.UserDao;
import ar.com.oxen.nibiru.security.db.domain.User;
import ar.com.oxen.nibiru.security.db.ui.ChangePasswordPresenter;
import ar.com.oxen.nibiru.security.db.ui.ChangePasswordView;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.utils.mvp.AbstractPresenter;

public class GenericChangePasswordPresenter extends
		AbstractPresenter<ChangePasswordView> implements
		ChangePasswordPresenter {
	private UserDao userDao;
	private PasswordEncoder passwordEncoder;

	public GenericChangePasswordPresenter(UserDao userDao,
			PasswordEncoder passwordEncoder) {
		super(null);
		this.userDao = userDao;
		this.passwordEncoder = passwordEncoder;
	}

	@Override
	public void go() {
		this.getView().getChangePassword().setClickHandler(new ClickHandler() {

			@Override
			public void onClick() {
				String oldPassword = getView().getOldPassword().getValue();
				if (oldPassword == null) {
					oldPassword = "";
				}
				String newPassword = getView().getNewPassword().getValue();
				if (newPassword == null) {
					newPassword = "";
				}

				if (!newPassword.equals(getView().getPasswordConfirmation()
						.getValue())) {
					getView().notifyPasswordMismatch();
					return;
				}
				
				String username = SecurityContextHolder.getContext()
						.getAuthentication().getPrincipal().toString();

				User user = userDao.findUserByUsername(username);
				if (!passwordEncoder.encodePassword(oldPassword, null).equals(
						user.getPassword())) {
					getView().notifyBadOldPassword();
					return;
				}

				user.setPassword(passwordEncoder.encodePassword(newPassword,
						null));

				userDao.saveUser(user);
			}
		});
	}
}
