package ar.com.oxen.nibiru.security.db.module;

import ar.com.oxen.nibiru.crud.utils.AbstractCrudModuleConfigurator;
import ar.com.oxen.nibiru.security.db.domain.RoleGroup;
import ar.com.oxen.nibiru.security.db.domain.Role;
import ar.com.oxen.nibiru.security.db.domain.User;
import ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleSubMenuExtension;

import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;

public class ModuleConfigurator extends AbstractCrudModuleConfigurator {
	private static final String MENU_EXTENSION = "ar.com.oxen.nibiru.security.db";

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

		this.addCrudWithMenu("security.users", MENU_EXTENSION, this.userCrudManager,
				this.userCrudActionExtension);

		this.addCrudWithMenu("security.roles", MENU_EXTENSION, this.roleCrudManager,
				this.roleCrudActionExtension);

		this.addCrudWithMenu("security.groups", MENU_EXTENSION, this.groupCrudManager,
				this.groupCrudActionExtension);
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
	
}
