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

public class GenericCrudListPresenter<T> extends AbstractGenericCrudListPresenter<T> {

	public GenericCrudListPresenter(CrudManager<T> crudManager,
			EventBus eventBus, Conversation conversation,
			ExtensionPointManager extensionPointManager,
			AuthorizationService authorizationService) {
		super(crudManager, eventBus, conversation, extensionPointManager, authorizationService);
	}

	@Override
	protected List<CrudEntity<T>> findEntities() {
		return this.getConversation().execute(
				new ConversationCallback<List<CrudEntity<T>>>() {

					@Override
					public List<CrudEntity<T>> doInConversation(
							Conversation conversation) throws Exception {
						return getCrudManager().findAll();
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
