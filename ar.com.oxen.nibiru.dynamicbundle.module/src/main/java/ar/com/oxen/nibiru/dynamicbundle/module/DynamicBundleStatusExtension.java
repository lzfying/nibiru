package ar.com.oxen.nibiru.dynamicbundle.module;

import java.util.ArrayList;
import java.util.List;

import ar.com.oxen.nibiru.crud.utils.SimpleCrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.dynamicbundle.domain.DynamicBundle;
import ar.com.oxen.nibiru.dynamicbundle.manager.api.DynamicBundleManager;

/**
 * CRUD action extension for starting and stopping dynamic bundles. It delegates
 * on {@link DynamicBundleManager}.
 * 
 */
public class DynamicBundleStatusExtension implements
		CrudActionExtension<DynamicBundle> {
	private List<CrudAction> actions;
	private final static String START = "start";
	private final static String STOP = "stop";
	private DynamicBundleManager dynamicBundleManager;

	public DynamicBundleStatusExtension() {
		super();
		this.actions = new ArrayList<CrudAction>(2);
		this.actions.add(new SimpleCrudAction(START, true, false, true, false));
		this.actions.add(new SimpleCrudAction(STOP, true, false, true, false));
	}

	@Override
	public List<CrudAction> getActions() {
		return this.actions;
	}

	@Override
	public CrudEntity<DynamicBundle> performAction(CrudAction action,
			CrudEntity<DynamicBundle> entity) {
		if (START.equals(action.getName())) {
			this.dynamicBundleManager.start(entity.getEntity());
			return null;
		} else if (STOP.equals(action.getName())) {
			this.dynamicBundleManager.stop(entity.getEntity());
			return null;
		} else {
			throw new IllegalArgumentException("Invalid action: " + action);
		}
	}

	public void setDynamicBundleManager(
			DynamicBundleManager dynamicBundleManager) {
		this.dynamicBundleManager = dynamicBundleManager;
	}
}
