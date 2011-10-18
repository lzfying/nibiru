package ar.com.oxen.nibiru.crud.manager.api;

import ar.com.oxen.nibiru.conversation.api.Conversation;

public class EditCrudEntityEvent {
	private CrudEntity<?> entity;
	private Conversation conversation;

	public EditCrudEntityEvent(CrudEntity<?> entity, Conversation conversation) {
		super();
		this.entity = entity;
		this.conversation = conversation;
	}

	public CrudEntity<?> getCrudEntity() {
		return entity;
	}

	public Conversation getConversation() {
		return conversation;
	}
}
