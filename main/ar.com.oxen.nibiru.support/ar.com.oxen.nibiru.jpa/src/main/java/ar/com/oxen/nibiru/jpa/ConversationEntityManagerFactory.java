package ar.com.oxen.nibiru.jpa;

import java.util.Map;

import javax.persistence.Cache;
import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.Query;
import javax.persistence.SynchronizationType;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

import ar.com.oxen.nibiru.conversation.api.ConversationAccessor;
import ar.com.oxen.nibiru.transaction.api.TransactionTemplate;

public class ConversationEntityManagerFactory implements EntityManagerFactory {
	private EntityManagerFactory entityManagerFactory;
	private ConversationAccessor conversationAccessor;
	private TransactionTemplate transactionTemplate;

	public ConversationEntityManagerFactory(
			EntityManagerFactory entityManagerFactory,
			ConversationAccessor conversationAccessor,
			TransactionTemplate transactionTemplate) {
		this.entityManagerFactory = entityManagerFactory;
		this.conversationAccessor = conversationAccessor;
		this.transactionTemplate = transactionTemplate;
	}

	@Override
	public EntityManager createEntityManager() {
		return ConversationEntityManagerHandler.buidlProxy(
				this.conversationAccessor, this.transactionTemplate,
				new EntityManagerCreator() {
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
				this.conversationAccessor, this.transactionTemplate,
				new EntityManagerCreator() {
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

    @Override
    public <T> void addNamedEntityGraph(String arg0, EntityGraph<T> arg1) {
        entityManagerFactory.addNamedEntityGraph(arg0, arg1);
    }

    @Override
    public void addNamedQuery(String arg0, Query arg1) {
        entityManagerFactory.addNamedQuery(arg0, arg1);
    }

    @Override
    public EntityManager createEntityManager(
            final SynchronizationType synchronizationType) {
        return ConversationEntityManagerHandler.buidlProxy(
                this.conversationAccessor, this.transactionTemplate,
                new EntityManagerCreator() {
                    @Override
                    public EntityManager create() {
                        return entityManagerFactory
                                .createEntityManager(synchronizationType);
                    }
                });
    }

    @Override
    public EntityManager createEntityManager(
            final SynchronizationType synchronizationType,
            @SuppressWarnings("rawtypes") final Map map) {
        return ConversationEntityManagerHandler.buidlProxy(
                this.conversationAccessor, this.transactionTemplate,
                new EntityManagerCreator() {
                    @Override
                    public EntityManager create() {
                        return entityManagerFactory.createEntityManager(
                                synchronizationType, map);
                    }
                });
    }

    @Override
    public <T> T unwrap(Class<T> arg0) {
        return entityManagerFactory.unwrap(arg0);
    }
}
