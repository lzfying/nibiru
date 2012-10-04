package ar.com.oxen.nibiru.license.cli;

import javax.inject.Inject;

import ar.com.oxen.commons.license.api.LicenseAuthorizer;
import ar.com.oxen.commons.license.api.LicenseSerializer;
import ar.com.oxen.commons.license.impl.DefaultLicenseInfo;

import com.google.inject.Guice;

/**
 * Command-line authorizer
 */
public class Authorizer {
	private LicenseSerializer<DefaultLicenseInfo> licenseSerializer;
	private LicenseAuthorizer<DefaultLicenseInfo> licenseAuthorizer;

	@Inject
	public Authorizer(LicenseSerializer<DefaultLicenseInfo> licenseSerializer,
			LicenseAuthorizer<DefaultLicenseInfo> licenseAuthorizer) {
		super();
		this.licenseSerializer = licenseSerializer;
		this.licenseAuthorizer = licenseAuthorizer;
	}

	public void authorize(String infoString) {
		System.out.println("License info string: " + infoString);
		DefaultLicenseInfo licenseInfo = this.licenseSerializer
				.deserializeLicenceInfo(infoString);
		System.out.println("License info: " + licenseInfo);
		String license = this.licenseSerializer
				.serializeLicence(this.licenseAuthorizer.authorize(licenseInfo));
		System.out.println("Authorized license stirng: " + license);
	}

	public static void main(String[] args) {
		if (args.length != 3) {
			System.out.println("Format: Auhtortizer {key_alias password license_info_string}");
			System.exit(-1);
		}

		Guice.createInjector(new LicenseModule(args[0], args[1])).getInstance(Authorizer.class)
				.authorize(args[2]);
	}
}
