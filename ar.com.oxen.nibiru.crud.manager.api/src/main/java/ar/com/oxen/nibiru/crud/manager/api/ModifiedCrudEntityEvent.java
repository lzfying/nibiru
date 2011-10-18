package ar.com.oxen.nibiru.crud.manager.api;

public class ModifiedCrudEntityEvent {
	private CrudEntity<?> entity;

	public ModifiedCrudEntityEvent(CrudEntity<?> entity) {
		super();
		this.entity = entity;
	}

	public CrudEntity<?> getCrudEntity() {
		return entity;
	}
}
