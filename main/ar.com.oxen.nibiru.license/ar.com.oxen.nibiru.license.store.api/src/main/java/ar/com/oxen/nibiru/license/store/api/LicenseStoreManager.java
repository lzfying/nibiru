package ar.com.oxen.nibiru.license.store.api;

public interface LicenseStoreManager {
	String GENERIC_MODULE = "genericModule";

	String loadLicense(String module);

	void saveLicense(String module, String license);
}
