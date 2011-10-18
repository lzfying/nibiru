package ar.com.oxen.nibiru.dynamicbundle.manager.api;

import ar.com.oxen.nibiru.dynamicbundle.domain.DynamicBundle;

/**
 * Service for managing dynamic bundles.
 */
public interface DynamicBundleManager {
	/**
	 * Deploys and stars a dynamic bundle.
	 * 
	 * @param dynamicBundle
	 *            The dynamic bundle
	 */
	void start(DynamicBundle dynamicBundle);

	/**
	 * Stops and undeploys a dynamic bundle.
	 * 
	 * @param dynamicBundle
	 *            The dynamic bundle
	 */
	void stop(DynamicBundle dynamicBundle);
}
