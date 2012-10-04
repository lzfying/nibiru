package ar.com.oxen.nibiru.license.module.ui.impl;

import java.util.Date;

import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.license.module.ui.LicenseRequestView;
import ar.com.oxen.nibiru.ui.api.mvp.HasValue;
import ar.com.oxen.nibiru.ui.api.mvp.HasValueChangeHandler;
import ar.com.oxen.nibiru.ui.api.view.TextField;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;
import ar.com.oxen.nibiru.ui.api.view.Window;
import ar.com.oxen.nibiru.ui.utils.view.AbstractWindowViewAdapter;

public class GenericLicenseRequestView extends
		AbstractWindowViewAdapter<Window> implements LicenseRequestView {
	private TextField<String> companyName;
	private TextField<Date> expirationDate;
	private TextField<String> licenseRequest;

	public GenericLicenseRequestView(ViewFactory viewFactory,
			MessageSource messageSource) {
		super(viewFactory.buildWindow(), viewFactory, messageSource);
		this.getTitle()
				.setValue(
						messageSource
								.getMessage("ar.com.oxen.nibiru.license.licenseRequest"));

		this.companyName = viewFactory.buildTextField(String.class);
		this.companyName.setCaption(messageSource
				.getMessage("ar.com.oxen.nibiru.license.companyName"));
		this.getAdapted().addComponent(this.companyName);

		this.expirationDate = viewFactory.buildTextField(Date.class);
		this.expirationDate.setCaption(messageSource
				.getMessage("ar.com.oxen.nibiru.license.expirationDate"));
		this.getAdapted().addComponent(this.expirationDate);

		this.licenseRequest = viewFactory.buildTextField(String.class);
		this.licenseRequest.setCaption(messageSource
				.getMessage("ar.com.oxen.nibiru.license.licenseRequest"));
		this.getAdapted().addComponent(this.licenseRequest);

		this.getAdapted().setModal(true);
	}

	@Override
	public HasValue<String> getComanyName() {
		return this.companyName;
	}

	@Override
	public HasValueChangeHandler getCompanyChangeHandler() {
		return this.companyName;
	}

	@Override
	public HasValue<Date> getExpirationDate() {
		return this.expirationDate;
	}

	@Override
	public HasValueChangeHandler getExpirationChangeHandler() {
		return this.expirationDate;
	}

	@Override
	public HasValue<String> getLicenseRequest() {
		return this.licenseRequest;
	}
}
