package ar.com.oxen.nibiru.report.api;

public interface Report {
	String EXTENSION_POINT_NAME = "ar.com.oxen.nibiru.reports";

	String getName();

	Iterable<String> getFormats();

	byte[] render(String format);
}
