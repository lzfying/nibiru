package ar.com.oxen.nibiru.security.module;

import ar.com.oxen.commons.eventbus.api.EventHandlerMethod;
import ar.com.oxen.nibiru.application.api.ApplicationStartEvent;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.utils.AbstractCrudModuleConfigurator;
import ar.com.oxen.nibiru.security.api.AuthenticationService;
import ar.com.oxen.nibiru.security.api.SuccessfulLogoutEvent;
import ar.com.oxen.nibiru.security.manager.jpa.domain.Role;
import ar.com.oxen.nibiru.security.manager.jpa.domain.RoleGroup;
import ar.com.oxen.nibiru.security.manager.jpa.domain.User;
import ar.com.oxen.nibiru.security.ui.api.SecurityPresenterFactory;
import ar.com.oxen.nibiru.security.ui.api.SecurityViewFactory;
import ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension;
import ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleMenuItemExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleSubMenuExtension;
import ar.com.oxen.nibiru.ui.utils.mvp.SimpleEventBusClickHandler;

public class ModuleConfigurator extends AbstractCrudModuleConfigurator {
	private static final String MENU_EXTENSION = "ar.com.oxen.nibiru.security.db";

	private AuthenticationService authenticationService;

	private SecurityPresenterFactory securityPresenterFactory;
	private SecurityViewFactory securityViewFactory;

	private CrudManager<User> userCrudManager;
	private CrudActionExtension<User> userCrudActionExtension;

	private CrudManager<Role> roleCrudManager;
	private CrudActionExtension<Role> roleCrudActionExtension;

	private CrudManager<RoleGroup> groupCrudManager;
	private CrudActionExtension<RoleGroup> groupCrudActionExtension;

	@Override
	protected void configure() {
		this.registerExtension(new SimpleSubMenuExtension("security",
				MENU_EXTENSION, 80), "ar.com.oxen.nibiru.menu",
				SubMenuExtension.class);

		this.registerExtension(new SimpleMenuItemExtension("security.logout",
				-20, new LogoutClickHandler(this.getEventBus(),
						this.authenticationService)), MENU_EXTENSION,
				MenuItemExtension.class);

		this.registerExtension(new SimpleMenuItemExtension(
				"security.changePassword", -10, new SimpleEventBusClickHandler(
						this.getEventBus(), ChangePasswordEvent.class, null)),
				MENU_EXTENSION, MenuItemExtension.class);

		this.addCrudWithMenu("security.users", MENU_EXTENSION,
				this.userCrudManager, this.userCrudActionExtension);

		this.addCrudWithMenu("security.roles", MENU_EXTENSION,
				this.roleCrudManager, this.roleCrudActionExtension);

		this.addCrudWithMenu("security.groups", MENU_EXTENSION,
				this.groupCrudManager, this.groupCrudActionExtension);

	}

	@EventHandlerMethod
	public void onApplicationStart(ApplicationStartEvent event) {
		activate(this.securityViewFactory.createLoginView(),
				this.securityPresenterFactory.createLoginPresenter());
	}

	@EventHandlerMethod
	public void onChangePassword(ChangePasswordEvent event) {
		this.activate(this.securityViewFactory.createChangePasswordView(),
				this.securityPresenterFactory.createChangePasswordPresenter());
	}

	@EventHandlerMethod
	public void onSuccessfulLogout(SuccessfulLogoutEvent event) {
		activate(this.securityViewFactory.createLoginView(),
				this.securityPresenterFactory.createLoginPresenter());
	}

	public void setUserCrudManager(CrudManager<User> userCrudManager) {
		this.userCrudManager = userCrudManager;
	}

	public void setUserCrudActionExtension(
			CrudActionExtension<User> userCrudActionExtension) {
		this.userCrudActionExtension = userCrudActionExtension;
	}

	public void setRoleCrudManager(CrudManager<Role> roleCrudManager) {
		this.roleCrudManager = roleCrudManager;
	}

	public void setRoleCrudActionExtension(
			CrudActionExtension<Role> roleCrudActionExtension) {
		this.roleCrudActionExtension = roleCrudActionExtension;
	}

	public void setGroupCrudManager(CrudManager<RoleGroup> groupCrudManager) {
		this.groupCrudManager = groupCrudManager;
	}

	public void setGroupCrudActionExtension(
			CrudActionExtension<RoleGroup> groupCrudActionExtension) {
		this.groupCrudActionExtension = groupCrudActionExtension;
	}

	public void setSecurityPresenterFactory(
			SecurityPresenterFactory securityPresenterFactory) {
		this.securityPresenterFactory = securityPresenterFactory;
	}

	public void setSecurityViewFactory(SecurityViewFactory securityViewFactory) {
		this.securityViewFactory = securityViewFactory;
	}

	public void setAuthenticationService(
			AuthenticationService authenticationService) {
		this.authenticationService = authenticationService;
	}
}
