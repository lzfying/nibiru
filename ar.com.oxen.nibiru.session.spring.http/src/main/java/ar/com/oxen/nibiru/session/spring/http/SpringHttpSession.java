package ar.com.oxen.nibiru.session.spring.http;

import java.io.Serializable;

import ar.com.oxen.nibiru.session.api.Session;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionBindingEvent;
import javax.servlet.http.HttpSessionBindingListener;

import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

public class SpringHttpSession implements Session {
	private static final String DESTRUCTION_CALLBACK_NAME_PREFIX = ServletRequestAttributes.class
			.getName() + ".DESTRUCTION_CALLBACK.";

	@SuppressWarnings("unchecked")
	@Override
	public <T> T get(String name) {
		return (T) this.currentSession().getAttribute(name);
	}

	@Override
	public void put(String name, Object object) {
		this.currentSession().setAttribute(name, object);
	}

	@Override
	public void remove(String name) {
		this.currentSession().removeAttribute(name);
	}

	@Override
	public String getId() {
		return this.currentSession().getId();
	}

	@Override
	public Object getMutex() {
		return this.currentAttributes().getSessionMutex();
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback) {
		this.currentSession().setAttribute(
				DESTRUCTION_CALLBACK_NAME_PREFIX + name,
				new DestructionCallbackBindingListener(callback));
	}

	@Override
	public boolean isValid() {
		return RequestContextHolder.getRequestAttributes() != null;
	}

	private ServletRequestAttributes currentAttributes() {
		return (ServletRequestAttributes) RequestContextHolder
				.currentRequestAttributes();
	}

	private HttpSession currentSession() {
		return this.currentAttributes().getRequest().getSession(true);
	}

	private static class DestructionCallbackBindingListener implements
			HttpSessionBindingListener, Serializable {
		private static final long serialVersionUID = -6555086284897255312L;
		private final Runnable destructionCallback;

		/**
		 * Create a new DestructionCallbackBindingListener for the given
		 * callback.
		 * 
		 * @param destructionCallback
		 *            the Runnable to execute when this listener object gets
		 *            unbound from the session
		 */
		public DestructionCallbackBindingListener(Runnable destructionCallback) {
			this.destructionCallback = destructionCallback;
		}

		public void valueBound(HttpSessionBindingEvent event) {
		}

		public void valueUnbound(HttpSessionBindingEvent event) {
			this.destructionCallback.run();
		}

	}

}
