package ar.com.oxen.nibiru.license.module.ui.impl;

import ar.com.oxen.nibiru.license.module.ui.LicensePresenterFactory;
import ar.com.oxen.nibiru.license.module.ui.LicenseRequestPresenter;

public class GenericLicensePresenterFactory  implements
		LicensePresenterFactory {
	@Override
	public LicenseRequestPresenter createLicenseRequestPresenter() {
		return new GenericLicenseRequestPresenter();
	}
}
