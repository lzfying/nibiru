package ar.com.oxen.nibiru.session.spring.scope;

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import ar.com.oxen.nibiru.session.api.Session;

public class SessionScope implements Scope {
	private Session session;

	@Override
	public String getConversationId() {
		return this.session.getId();
	}

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory) {
		synchronized (this.session.getMutex()) {
			Object object = this.session.get(name);
			if (object == null) {
				object = objectFactory.getObject();
				session.put(name, object);
			}

			return object;
		}
	}

	@Override
	public Object remove(String name) {
		synchronized (this.session.getMutex()) {
			Object object = this.session.get(name);
			this.session.remove(name);
			return object;
		}
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		this.session.registerDestructionCallback(name, callback);
	}

	@Override
	public Object resolveContextualObject(String key) {
		/* No contextual reference is supported */
		return null;
	}

	public void setSession(Session session) {
		this.session = session;
	}
}
