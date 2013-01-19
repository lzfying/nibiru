package ar.com.oxen.nibiru.jpa.blueprint;

import java.util.Collection;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.persistence.EntityManagerFactory;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;

import ar.com.oxen.nibiru.conversation.api.ConversationAccessor;
import ar.com.oxen.nibiru.jpa.ConversationEntityManagerFactory;

public class ConversationEntityManagerExtender {
	private BundleContext bundleContext;
	private ConversationAccessor conversationAccessor;

	private Map<EntityManagerFactory, ServiceRegistration<EntityManagerFactory>> serviceMapping = new HashMap<EntityManagerFactory, ServiceRegistration<EntityManagerFactory>>();

	public void init() {
		String filter = null;
		try {

			ServiceListener serviceListener = new ServiceListener() {
				public void serviceChanged(ServiceEvent ev) {
					@SuppressWarnings("unchecked")
					ServiceReference<EntityManagerFactory> reference = (ServiceReference<EntityManagerFactory>) ev
							.getServiceReference();
					switch (ev.getType()) {
					case ServiceEvent.REGISTERED:
						startConverstionEntityManagerFactory(reference);
						break;
					case ServiceEvent.UNREGISTERING:
						stopConverstionEntityManagerFactory(bundleContext
								.getService(reference));
						break;
					}
				}
			};

			filter = "(objectclass=" + EntityManagerFactory.class.getName()
					+ ")";
			this.bundleContext.addServiceListener(serviceListener, filter);

			Collection<ServiceReference<EntityManagerFactory>> references = this.bundleContext
					.getServiceReferences(EntityManagerFactory.class, filter);

			if (references != null) {
				for (ServiceReference<EntityManagerFactory> reference : references) {
					this.startConverstionEntityManagerFactory(reference);
				}
			}

		} catch (InvalidSyntaxException e) {
			throw new IllegalArgumentException("Erro building filter: "
					+ filter, e);
		}
	}

	public void destroy() {
		for (ServiceRegistration<EntityManagerFactory> registration : serviceMapping
				.values()) {
			registration.unregister();
		}
		serviceMapping.clear();
	}

	private void startConverstionEntityManagerFactory(
			ServiceReference<EntityManagerFactory> reference) {
		EntityManagerFactory entityManagerFactory = this.bundleContext
				.getService(reference);

		if (!(entityManagerFactory instanceof ConversationEntityManagerFactory)) {
			EntityManagerFactory conversationEntityManagerFactory = new ConversationEntityManagerFactory(
					entityManagerFactory, this.conversationAccessor);

			Dictionary<String, Object> props = new Hashtable<String, Object>();
			for (String propertyKey : reference.getPropertyKeys()) {
				props.put(propertyKey, reference.getProperty(propertyKey));
			}
			props.put("osgi.unit.name", reference.getProperty("osgi.unit.name")
					+ "_conversation");

			ServiceRegistration<EntityManagerFactory> registration = this.bundleContext
					.registerService(EntityManagerFactory.class,
							conversationEntityManagerFactory, props);

			this.serviceMapping.put(entityManagerFactory, registration);
		}
	}

	private void stopConverstionEntityManagerFactory(
			EntityManagerFactory entityManagerFactory) {
		if (this.serviceMapping.containsKey(entityManagerFactory)) {
			this.serviceMapping.remove(entityManagerFactory).unregister();
		}
	}

	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}

	public void setConversationAccessor(
			ConversationAccessor conversationAccessor) {
		this.conversationAccessor = conversationAccessor;
	}
}
