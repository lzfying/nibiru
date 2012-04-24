package ar.com.oxen.nibiru.crud.ui.generic.list;

import java.util.List;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;

public class GenericCrudListPresenter extends AbstractGenericCrudListPresenter {

	public GenericCrudListPresenter(CrudManager<?> crudManager,
			EventBus eventBus, Conversation conversation,
			ExtensionPointManager extensionPointManager) {
		super(crudManager, eventBus, conversation, extensionPointManager);
	}

	@SuppressWarnings("unchecked")
	@Override
	protected <K> List<CrudEntity<K>> findEntities() {
		return ((CrudManager<K>) this.getCrudManager()).findAll();
	}

	@Override
	protected <K> void onReturnedEntity(CrudEntity<K> returnedEntity) {
	}
}
