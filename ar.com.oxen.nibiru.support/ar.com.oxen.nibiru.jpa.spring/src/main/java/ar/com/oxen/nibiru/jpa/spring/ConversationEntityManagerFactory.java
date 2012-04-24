package ar.com.oxen.nibiru.jpa.spring;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.conversation.api.ConversationAccessor;
import ar.com.oxen.nibiru.conversation.api.ConversationTracker;

// TODO: esta clase es generica, no depende de Spring, podria ir en un modulo separado
// TODO: No se si es correcto lo que hace esta clase, crea un proxy que no se cierra y lo cierra solo al cerrar la conversacion
public class ConversationEntityManagerFactory implements EntityManagerFactory {
	private EntityManagerFactory decorated;
	private ConversationAccessor conversationAccessor;
	private final static String ENTITY_MANAGER_KEY = "ar.com.oxen.nibiru.jpa.EntityManager";

	@Override
	public EntityManager createEntityManager() {
		return this.getEntityManager(new EntityManagerCreator() {
			@Override
			public EntityManager create() {
				return decorated.createEntityManager();
			}
		});
	}

	@SuppressWarnings("rawtypes")
	@Override
	public EntityManager createEntityManager(final Map map) {
		return this.getEntityManager(new EntityManagerCreator() {
			@Override
			public EntityManager create() {
				return decorated.createEntityManager(map);
			}
		});
	}

	private EntityManager getEntityManager(EntityManagerCreator creationCallback) {
		Conversation currentConversation = this.conversationAccessor
				.getCurrentConversation();

		if (currentConversation != null) {
			EntityManager conversationEntityManager = currentConversation
					.get(ENTITY_MANAGER_KEY);
			if (conversationEntityManager == null) {
				synchronized (this) {
					conversationEntityManager = currentConversation
							.get(ENTITY_MANAGER_KEY);
					if (conversationEntityManager == null) {
						conversationEntityManager = creationCallback.create();
						currentConversation.put(ENTITY_MANAGER_KEY,
								conversationEntityManager);

						final EntityManager finalEntityManager = conversationEntityManager;
						currentConversation
								.registerTracker(new ConversationTracker() {
									@Override
									public void onEnd(Conversation conversation) {
										finalEntityManager.flush();
										finalEntityManager.close();
									}

									@Override
									public void onCancel(
											Conversation conversation) {
										finalEntityManager.close();
									}
								});
					}
				}
			}
			return EntityManagerHandler.buidlProxy(conversationEntityManager);
		} else {
			return creationCallback.create();
		}
	}

	@Override
	public void close() {
		this.decorated.close();
	}

	@Override
	public boolean isOpen() {
		return this.decorated.isOpen();
	}

	public void setDecorated(EntityManagerFactory decorated) {
		this.decorated = decorated;
	}

	public void setConversationAccessor(
			ConversationAccessor conversationAccessor) {
		this.conversationAccessor = conversationAccessor;
	}
}
