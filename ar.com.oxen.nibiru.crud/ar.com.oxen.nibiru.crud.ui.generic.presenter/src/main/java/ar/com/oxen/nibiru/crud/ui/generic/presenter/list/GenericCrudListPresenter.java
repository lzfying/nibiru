package ar.com.oxen.nibiru.crud.ui.generic.presenter.list;

import java.util.List;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.conversation.api.ConversationCallback;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.security.api.AuthorizationService;
import ar.com.oxen.nibiru.ui.api.mvp.CloseHandler;

public class GenericCrudListPresenter extends AbstractGenericCrudListPresenter {

	public GenericCrudListPresenter(CrudManager<?> crudManager,
			EventBus eventBus, Conversation conversation,
			ExtensionPointManager extensionPointManager,
			AuthorizationService authorizationService) {
		super(crudManager, eventBus, conversation, extensionPointManager, authorizationService);
	}

	@Override
	protected <K> List<CrudEntity<K>> findEntities() {
		return this.getConversation().execute(
				new ConversationCallback<List<CrudEntity<K>>>() {

					@SuppressWarnings("unchecked")
					@Override
					public List<CrudEntity<K>> doInConversation(
							Conversation conversation) throws Exception {
						return ((CrudManager<K>) getCrudManager()).findAll();
					}
				});

	}

	@Override
	protected <K> void onReturnedEntity(CrudEntity<K> returnedEntity) {
	}

	@Override
	protected void customGo() {
		this.getView().getCloseHander().setCloseHandler(new CloseHandler() {

			@Override
			public void onClose() {
				getConversation().end();
			}
		});
	}
	
}
