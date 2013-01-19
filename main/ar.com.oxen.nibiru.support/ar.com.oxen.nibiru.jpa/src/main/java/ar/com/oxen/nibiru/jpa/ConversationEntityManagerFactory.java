package ar.com.oxen.nibiru.jpa;

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import ar.com.oxen.nibiru.conversation.api.ConversationAccessor;

public class ConversationEntityManagerFactory implements EntityManagerFactory {
	private EntityManagerFactory entityManagerFactory;
	private ConversationAccessor conversationAccessor;	
	

	public ConversationEntityManagerFactory(
			EntityManagerFactory entityManagerFactory,
			ConversationAccessor conversationAccessor) {
		super();
		this.entityManagerFactory = entityManagerFactory;
		this.conversationAccessor = conversationAccessor;
	}

	@Override
	public EntityManager createEntityManager() {
		return ConversationEntityManagerHandler.buidlProxy(
				this.conversationAccessor, new EntityManagerCreator() {
					@Override
					public EntityManager create() {
						return entityManagerFactory.createEntityManager();
					}
				});
	}

	@Override
	public EntityManager createEntityManager(
			@SuppressWarnings("rawtypes") final Map map) {
		return ConversationEntityManagerHandler.buidlProxy(
				this.conversationAccessor, new EntityManagerCreator() {
					@Override
					public EntityManager create() {
						return entityManagerFactory.createEntityManager(map);
					}
				});
	}

	@Override
	public void close() {
		this.entityManagerFactory.close();
	}

	@Override
	public boolean isOpen() {
		return this.entityManagerFactory.isOpen();
	}

	@Override
	public Cache getCache() {
		return this.entityManagerFactory.getCache();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return this.entityManagerFactory.getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return this.entityManagerFactory.getMetamodel();
	}

	@Override
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return this.entityManagerFactory.getPersistenceUnitUtil();
	}

	@Override
	public Map<String, Object> getProperties() {
		return this.entityManagerFactory.getProperties();
	}
}
