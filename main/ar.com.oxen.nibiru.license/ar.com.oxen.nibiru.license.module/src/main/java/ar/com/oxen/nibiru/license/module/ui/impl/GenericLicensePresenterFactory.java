package ar.com.oxen.nibiru.license.module.ui.impl;

import javax.inject.Inject;

import com.google.inject.Guice;

import ar.com.oxen.commons.license.api.HardwareIdProvider;
import ar.com.oxen.commons.license.api.LicenseSerializer;
import ar.com.oxen.commons.license.impl.DefaultLicenseInfo;
import ar.com.oxen.nibiru.license.module.LicenseModule;
import ar.com.oxen.nibiru.license.module.ui.LicensePresenterFactory;
import ar.com.oxen.nibiru.license.module.ui.LicenseRequestPresenter;
import ar.com.oxen.nibiru.license.store.api.LicenseStoreManager;

public class GenericLicensePresenterFactory implements LicensePresenterFactory {
	private LicenseStoreManager licenseStoreManager;

	@Inject
	private LicenseSerializer<DefaultLicenseInfo> licenseSerializer;

	@Inject
	private HardwareIdProvider hardwareIdProvider;

	public GenericLicensePresenterFactory() {
		super();
		Guice.createInjector(new LicenseModule()).injectMembers(this);
	}

	@Override
	public LicenseRequestPresenter createLicenseRequestPresenter() {
		return new GenericLicenseRequestPresenter(this.licenseStoreManager,
				this.licenseSerializer, this.hardwareIdProvider);
	}

	public void setLicenseStoreManager(LicenseStoreManager licenseStoreManager) {
		this.licenseStoreManager = licenseStoreManager;
	}
}
