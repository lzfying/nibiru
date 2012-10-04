package ar.com.oxen.nibiru.license.module.ui.impl;

import ar.com.oxen.commons.license.api.HardwareIdProvider;
import ar.com.oxen.commons.license.api.LicenseSerializer;
import ar.com.oxen.commons.license.impl.DefaultLicenseInfo;
import ar.com.oxen.nibiru.license.module.ui.LicenseRequestPresenter;
import ar.com.oxen.nibiru.license.module.ui.LicenseRequestView;
import ar.com.oxen.nibiru.ui.api.mvp.ValueChangeHandler;
import ar.com.oxen.nibiru.ui.utils.mvp.AbstractPresenter;

public class GenericLicenseRequestPresenter extends
		AbstractPresenter<LicenseRequestView> implements
		LicenseRequestPresenter {

	private LicenseSerializer<DefaultLicenseInfo> licenseSerializer;
	private HardwareIdProvider hardwareIdProvider;

	public GenericLicenseRequestPresenter(
			LicenseSerializer<DefaultLicenseInfo> licenseSerializer,
			HardwareIdProvider hardwareIdProvider) {
		super(null);
		this.licenseSerializer = licenseSerializer;
		this.hardwareIdProvider = hardwareIdProvider;
	}

	@Override
	public void go() {
		LicenceValueChangeHandler chengeHandler = new LicenceValueChangeHandler();
		getView().getCompanyChangeHandler()
				.setValueChangeHandler(chengeHandler);
		getView().getExpirationChangeHandler().setValueChangeHandler(
				chengeHandler);
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
