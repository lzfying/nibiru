package ar.com.oxen.nibiru.jpa;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

import javax.persistence.EntityManager;

import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.conversation.api.ConversationAccessor;
import ar.com.oxen.nibiru.conversation.api.ConversationTracker;

class ConversationEntityManagerHandler implements InvocationHandler {
	private final static String ENTITY_MANAGER_KEY = "ar.com.oxen.nibiru.jpa.EntityManager";
	private ConversationAccessor conversationAccessor;
	private EntityManagerCreator entityManagerCreator;

	static EntityManager buidlProxy(ConversationAccessor conversationAccessor,
			EntityManagerCreator entityManagerCreator) {
		return (EntityManager) Proxy.newProxyInstance(
				ConversationEntityManagerHandler.class.getClassLoader(),
				new Class<?>[] { EntityManager.class },
				new ConversationEntityManagerHandler(conversationAccessor,
						entityManagerCreator));
	}

	ConversationEntityManagerHandler(ConversationAccessor conversationAccessor,
			EntityManagerCreator entityManagerCreator) {
		super();
		this.conversationAccessor = conversationAccessor;
		this.entityManagerCreator = entityManagerCreator;
	}

	@Override
	public Object invoke(Object proxy, Method method, Object[] args)
			throws Throwable {
		if (method.getName().equals("close")) {
			return null;
		} else {
			return method.invoke(this.getConversationEntityManager(), args);
		}
	}

	private EntityManager getConversationEntityManager() {
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
						conversationEntityManager = this.entityManagerCreator
								.create();
						currentConversation.put(ENTITY_MANAGER_KEY,
								conversationEntityManager);

						currentConversation
								.registerTracker(new EntityManagerConversationTracker(
										conversationEntityManager));
					}
				}
			}
			return conversationEntityManager;
		} else {
			throw new IllegalStateException("No conversation active");
		}
	}

	private static class EntityManagerConversationTracker implements
			ConversationTracker {
		private EntityManager entityManager;

		public EntityManagerConversationTracker(EntityManager entityManager) {
			super();
			this.entityManager = entityManager;
		}

		@Override
		public void onEnd(Conversation conversation) {
			entityManager.getTransaction().begin();
			entityManager.flush();
			entityManager.getTransaction().commit();
			entityManager.close();
		}

		@Override
		public void onCancel(Conversation conversation) {
			entityManager.close();
		}
	}
}