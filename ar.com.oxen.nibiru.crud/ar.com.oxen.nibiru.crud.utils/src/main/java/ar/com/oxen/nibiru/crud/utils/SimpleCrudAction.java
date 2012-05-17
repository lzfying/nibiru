package ar.com.oxen.nibiru.crud.utils;

import ar.com.oxen.nibiru.crud.manager.api.CrudAction;

public class SimpleCrudAction implements CrudAction {
	private String name;
	private boolean requiresEntity;
	private boolean requiresConfirmation;
	private boolean showInList;
	private boolean showInForm;
	private String[] allowedRoles;

	public SimpleCrudAction(String name, boolean requiresEntity,
			boolean requiresConfirmation, boolean showInList, boolean showInForm, String[] allowedRoles) {
		super();
		this.name = name;
		this.requiresEntity = requiresEntity;
		this.requiresConfirmation = requiresConfirmation;
		this.showInList = showInList;
		this.showInForm = showInForm;
		this.allowedRoles = allowedRoles;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public boolean isEntityRequired() {
		return this.requiresEntity;
	}

	@Override
	public boolean isConfirmationRequired() {
		return this.requiresConfirmation;
	}

	@Override
	public boolean isVisibleInList() {
		return this.showInList;
	}

	@Override
	public boolean isVisibleInForm() {
		return this.showInForm;
	}

	@Override
	public String[] getAllowedRoles() {
		return this.allowedRoles;
	}
}
