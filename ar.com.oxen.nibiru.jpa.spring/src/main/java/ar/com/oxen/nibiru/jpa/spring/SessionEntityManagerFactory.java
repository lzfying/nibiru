package ar.com.oxen.nibiru.jpa.spring;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import ar.com.oxen.nibiru.session.api.Session;

// TODO: esta clase es generica, no depende de Spring, podria ir en un modulo separado
// TODO: No se si es correcto lo que hace esta clase, crea un proxy que no se cierra y lo cierra solo al removerlo de la sesion
public class SessionEntityManagerFactory implements EntityManagerFactory {
	private EntityManagerFactory decorated;
	private Session session;
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
		if (this.session.isValid()) {
			EntityManager sessionEntityManager = this.session.get(ENTITY_MANAGER_KEY);
			if (sessionEntityManager == null) {
				synchronized (this) {
					sessionEntityManager = this.session.get(ENTITY_MANAGER_KEY);
					if (sessionEntityManager == null) {
						sessionEntityManager = creationCallback.create();
						this.session.put(ENTITY_MANAGER_KEY, sessionEntityManager);

						final EntityManager finalEntityManager = sessionEntityManager;
						this.session.registerDestructionCallback(ENTITY_MANAGER_KEY,
								new Runnable() {
									@Override
									public void run() {
										finalEntityManager.close();
									}
								});
					}
				}
			}
			return EntityManagerHandler.buidlProxy(sessionEntityManager);
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

	public void setSession(Session session) {
		this.session = session;
	}
}
