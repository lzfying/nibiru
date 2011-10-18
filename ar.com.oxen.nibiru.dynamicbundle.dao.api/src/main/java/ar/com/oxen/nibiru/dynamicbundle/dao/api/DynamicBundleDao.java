package ar.com.oxen.nibiru.dynamicbundle.dao.api;

import ar.com.oxen.commons.dao.api.ReadDao;
import ar.com.oxen.commons.dao.api.UpdateDao;
import ar.com.oxen.nibiru.dynamicbundle.domain.DynamicBundle;

/**
 * DAO for accessing dynamic bundles.
 */
public interface DynamicBundleDao extends ReadDao<DynamicBundle>,
		UpdateDao<DynamicBundle> {
}
