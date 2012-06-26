package ar.com.oxen.nibiru.report.crud;

import ar.com.oxen.nibiru.report.api.Report;

public class ReportExecutedEvent {
	private Report report;
	private String format;
	private byte[] data;

	public ReportExecutedEvent(Report report, String format, byte[] data) {
		super();
		this.report = report;
		this.format = format;
		this.data = data;
	}

	public Report getReport() {
		return report;
	}

	public String getFormat() {
		return format;
	}

	public byte[] getData() {
		return data;
	}
}
