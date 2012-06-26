package ar.com.oxen.nibiru.report.module.ui.generic;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.report.module.ui.ReportPresenterFactory;
import ar.com.oxen.nibiru.report.module.ui.ReportView;
import ar.com.oxen.nibiru.ui.api.mvp.Presenter;

public class GenericReportPresenterFactory implements ReportPresenterFactory {
	private EventBus eventBus;

	@Override
	public Presenter<ReportView> buildReportPresenter(byte[] data) {
		return new GenericReportPresenter(this.eventBus, data);
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}
}
