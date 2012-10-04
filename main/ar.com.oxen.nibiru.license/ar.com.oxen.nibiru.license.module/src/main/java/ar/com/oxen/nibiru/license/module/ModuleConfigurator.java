package ar.com.oxen.nibiru.license.module;

import ar.com.oxen.commons.eventbus.api.EventHandlerMethod;
import ar.com.oxen.nibiru.license.module.ui.LicensePresenterFactory;
import ar.com.oxen.nibiru.license.module.ui.LicenseViewFactory;
import ar.com.oxen.nibiru.module.utils.AbstractModuleConfigurator;

public class ModuleConfigurator extends
		AbstractModuleConfigurator<LicenseViewFactory, LicensePresenterFactory> {

	@EventHandlerMethod
	public void onLicenseRequest(LicenseRequestEvent event) {
	}
}
