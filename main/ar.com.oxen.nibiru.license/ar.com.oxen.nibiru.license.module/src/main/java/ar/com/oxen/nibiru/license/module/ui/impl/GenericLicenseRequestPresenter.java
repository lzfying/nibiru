package ar.com.oxen.nibiru.license.module.ui.impl;

import ar.com.oxen.commons.license.api.HardwareIdProvider;
import ar.com.oxen.commons.license.api.LicenseSerializer;
import ar.com.oxen.commons.license.impl.DefaultLicenseInfo;
import ar.com.oxen.nibiru.license.module.ui.LicenseRequestPresenter;
import ar.com.oxen.nibiru.license.module.ui.LicenseRequestView;
import ar.com.oxen.nibiru.license.store.api.LicenseStoreManager;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.mvp.ValueChangeHandler;
import ar.com.oxen.nibiru.ui.utils.mvp.AbstractPresenter;

public class GenericLicenseRequestPresenter extends
		AbstractPresenter<LicenseRequestView> implements
		LicenseRequestPresenter {
	private LicenseStoreManager licenseStoreManager;
	private LicenseSerializer<DefaultLicenseInfo> licenseSerializer;
	private HardwareIdProvider hardwareIdProvider;

	public GenericLicenseRequestPresenter(
			LicenseStoreManager licenseStoreManager,
			LicenseSerializer<DefaultLicenseInfo> licenseSerializer,
			HardwareIdProvider hardwareIdProvider) {
		super(null);
		this.licenseStoreManager = licenseStoreManager;
		this.licenseSerializer = licenseSerializer;
		this.hardwareIdProvider = hardwareIdProvider;
	}

	@Override
	public void go() {
		LicenceValueChangeHandler chengeHandler = new LicenceValueChangeHandler();
		this.getView().getCompanyChangeHandler()
				.setValueChangeHandler(chengeHandler);
		this.getView().getExpirationChangeHandler()
				.setValueChangeHandler(chengeHandler);

		String license = this.licenseStoreManager
				.loadLicense(LicenseStoreManager.GENERIC_MODULE);
		if (license != null) {
			this.getView().getLicenseAuthorization().setValue(license);
		}

		this.getView().getAuthorize().setClickHandler(new ClickHandler() {
			@Override
			public void onClick() {
				licenseStoreManager.saveLicense(
						LicenseStoreManager.GENERIC_MODULE, getView()
								.getLicenseAuthorization().getValue());
			}
		});
	}

	private class LicenceValueChangeHandler implements ValueChangeHandler {
		@Override
		public void onValueChange() {
			getView().getLicenseRequest().setValue(
					licenseSerializer
							.serializeLicenceInfo(new DefaultLicenseInfo(
									getView().getComanyName().getValue(),
									getView().getExpirationDate().getValue(),
									hardwareIdProvider.getHardwareId())));
		}
	}
}
