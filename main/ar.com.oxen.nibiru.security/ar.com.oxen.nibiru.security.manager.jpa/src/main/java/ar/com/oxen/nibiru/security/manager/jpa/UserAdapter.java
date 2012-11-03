package ar.com.oxen.nibiru.security.manager.jpa;

import java.util.Collection;
import java.util.HashSet;

import ar.com.oxen.nibiru.security.manager.api.UserData;
import ar.com.oxen.nibiru.security.manager.jpa.domain.Role;
import ar.com.oxen.nibiru.security.manager.jpa.domain.RoleGroup;
import ar.com.oxen.nibiru.security.manager.jpa.domain.User;

class UserAdapter implements UserData {
	private User user;

	public UserAdapter(User user) {
		super();
		this.user = user;
	}

	@Override
	public String getUsername() {
		return this.user.getUsername();
	}

	@Override
	public String getPassword() {
		return this.user.getPassword();
	}

	@Override
	public String getFirstName() {
		return this.user.getFirstName();
	}

	@Override
	public String getLastName() {
		return this.user.getLastName();
	}

	@Override
	public Iterable<String> getRoles() {
		Collection<String> roles = new HashSet<String>();
		for (Role role : this.user.getRoles()) {
			roles.add(role.getName());
		}
		for (RoleGroup group : this.user.getGroups()) {
			for (Role role : group.getRoles()) {
				roles.add(role.getName());
			}
		}

		return roles;
	}
}
