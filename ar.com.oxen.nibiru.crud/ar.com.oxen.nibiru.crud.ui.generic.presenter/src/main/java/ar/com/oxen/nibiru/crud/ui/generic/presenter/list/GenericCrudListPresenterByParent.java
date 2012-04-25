package ar.com.oxen.nibiru.crud.ui.generic.presenter.list;

import java.util.List;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;

public class GenericCrudListPresenterByParent extends
		AbstractGenericCrudListPresenter {
	private String parentField;
	private Object parent;

	public GenericCrudListPresenterByParent(CrudManager<?> crudManager,
			EventBus eventBus, Conversation conversation,
			ExtensionPointManager extensionPointManager, String parentField,
			Object parent) {
		super(crudManager, eventBus, conversation, extensionPointManager);

		this.parentField = parentField;
		this.parent = parent;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <K> List<CrudEntity<K>> findEntities() {
		return ((CrudManager<K>) this.getCrudManager()).findByfield(
				this.parentField, this.parent);
	}

	@Override
	protected <K> void onReturnedEntity(CrudEntity<K> returnedEntity) {
		// TODO: Esto esta medio feo. A toda entidad retornada por una accion
		// que sea compatible con el crud manager le asocio el valor del campo
		// padre. A lo mejor deberia establecerse alguna otra condicion.
		if (returnedEntity.getEntityTypeName().equals(
				this.getCrudManager().getEntityTypeName())) {
			returnedEntity.setValue(this.parentField, this.parent);
		}
	}
}
