package ar.com.oxen.nibiru.extensionpoint.generic;

import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionTracker;

/**
 * Generic Extension Point manager implementation.
 * 
 */
public class GenericExtensionPointManager implements ExtensionPointManager {

	@Override
	public <K> void registerExtension(K extension, String extensionPointName,
			Class<K> extensionPointInterface) {
		// TODO Auto-generated method stub

	}

	@Override
	public void unregisterExtension(Object extension) {
		// TODO Auto-generated method stub

	}

	@Override
	public <T, K extends T> void registerTracker(ExtensionTracker<T> tracker,
			String extensionPointName, Class<K> extensionPointInterface) {
		// TODO Auto-generated method stub

	}

}
