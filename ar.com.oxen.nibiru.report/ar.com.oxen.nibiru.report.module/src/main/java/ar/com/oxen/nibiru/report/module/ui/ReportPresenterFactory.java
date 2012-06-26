package ar.com.oxen.nibiru.report.module.ui;

import ar.com.oxen.nibiru.ui.api.mvp.Presenter;

public interface ReportPresenterFactory {
	Presenter<ReportView> buildReportPresenter(byte[] data, String format);
}
