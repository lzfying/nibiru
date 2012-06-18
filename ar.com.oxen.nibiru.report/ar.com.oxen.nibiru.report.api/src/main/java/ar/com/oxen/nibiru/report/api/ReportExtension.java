package ar.com.oxen.nibiru.report.api;

/**
 * Extension for exposing a report.
 */
public interface ReportExtension {
	String getName();

	byte[] render(String format);
}
