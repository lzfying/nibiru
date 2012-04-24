package ar.com.oxen.nibiru.crud.ui.generic;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.conversation.api.ConversationCallback;
import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.manager.api.EditCrudEntityEvent;
import ar.com.oxen.nibiru.crud.manager.api.ModifiedCrudEntityEvent;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.ui.api.mvp.View;
import ar.com.oxen.nibiru.ui.utils.mvp.AbstractPresenter;

public abstract class AbstractGenericCrudPresenter<V extends View> extends
		AbstractPresenter<V> {
	private CrudManager<?> crudManager;
	private Conversation conversation;
	private ExtensionPointManager extensionPointManager;

	public AbstractGenericCrudPresenter(CrudManager<?> crudManager,
			EventBus eventBus, Conversation conversation,
			ExtensionPointManager extensionPointManager) {
		super(eventBus);
		this.crudManager = crudManager;
		this.conversation = conversation;
		this.extensionPointManager = extensionPointManager;
	}

	@SuppressWarnings("unchecked")
	protected void performAction(final CrudAction action,
			final CrudEntity<?> entity,
			final CrudActionExtension<?> actionExtension) {

		CrudEntity<Object> returnedEntity = this.getConversation().execute(
				new ConversationCallback<CrudEntity<Object>>() {
					@Override
					public CrudEntity<Object> doInConversation(
							Conversation conversation) throws Exception {
						return ((CrudActionExtension<Object>) actionExtension)
								.performAction(action,
										(CrudEntity<Object>) entity);
					}
				});

		if (entity != null) {
			this.getEventBus().fireEvent(
					new ModifiedCrudEntityEvent(entity.getId()),
					this.crudManager.getEntityTypeName());
		}

		if (returnedEntity != null) {
			this.onReturnedEntity(returnedEntity);
			this.getEventBus().fireEvent(
					new EditCrudEntityEvent(returnedEntity,
							this.getConversation()),
					this.crudManager.getEntityTypeName());
		}
	}

	protected abstract <K> void onReturnedEntity(CrudEntity<K> returnedEntity);

	protected CrudManager<?> getCrudManager() {
		return crudManager;
	}

	protected String getTopic() {
		return this.crudManager.getEntityTypeName();
	}

	protected ExtensionPointManager getExtensionPointManager() {
		return extensionPointManager;
	}

	protected Conversation getConversation() {
		return conversation;
	}
}
