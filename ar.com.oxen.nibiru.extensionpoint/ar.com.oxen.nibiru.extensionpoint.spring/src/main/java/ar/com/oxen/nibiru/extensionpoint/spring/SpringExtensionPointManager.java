package ar.com.oxen.nibiru.extensionpoint.spring;

import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceEvent;
import org.osgi.framework.ServiceListener;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.springframework.osgi.context.BundleContextAware;

import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionTracker;

public class SpringExtensionPointManager implements ExtensionPointManager,
		BundleContextAware {
	private BundleContext bundleContext;
	private Map<Object, ServiceRegistration> registrations = new HashMap<Object, ServiceRegistration>();

	@Override
	public <K> void registerExtension(K extension, String extensionPointName,
			Class<K> extensionPointInterface) {
		Dictionary<String, Object> props = new Hashtable<String, Object>();
		props.put("extensionPoint", extensionPointName);
		this.registrations.put(extension, this.bundleContext.registerService(
				extensionPointInterface.getName(), extension, props));
	}

	@Override
	public void unregisterExtension(Object extension) {
		this.registrations.remove(extension).unregister();
	}

	@Override
	public <T, K extends T> void registerTracker(
			final ExtensionTracker<T> tracker, String extensionName,
			Class<K> extensionInterface) {
		String filter = null;
		try {

			ServiceListener serviceListener = new ServiceListener() {
				@SuppressWarnings("unchecked")
				public void serviceChanged(ServiceEvent ev) {
					T service = (T) bundleContext.getService(ev
							.getServiceReference());
					switch (ev.getType()) {
					case ServiceEvent.REGISTERED:
						tracker.onRegister(service);
						break;
					case ServiceEvent.UNREGISTERING:
						tracker.onUnregister(service);
						break;
					}
				}
			};

			filter = "(&(objectclass=" + extensionInterface.getName()
					+ ") (extensionPoint=" + extensionName + "))";
			this.bundleContext.addServiceListener(serviceListener, filter);
			ServiceReference[] references = this.bundleContext
					.getServiceReferences((String)null, filter);
			if (references != null) {
				for (ServiceReference serviceReference : references) {
					serviceListener.serviceChanged(new ServiceEvent(
							ServiceEvent.REGISTERED, serviceReference));
				}
			}
		} catch (InvalidSyntaxException e) {
			throw new IllegalArgumentException("Erro building filter: "
					+ filter, e);
		}
	}

	@Override
	public void setBundleContext(BundleContext bundleContext) {
		this.bundleContext = bundleContext;
	}
}
