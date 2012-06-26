package ar.com.oxen.nibiru.report.module.ui.generic;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.report.module.ui.ReportView;
import ar.com.oxen.nibiru.ui.utils.mvp.AbstractPresenter;

public class GenericReportPresenter extends AbstractPresenter<ReportView> {
	private byte[] data;
	public GenericReportPresenter(EventBus eventBus, byte[] data) {
		super(eventBus);
		this.data = data;
	}

	@Override
	public void go() {
		this.getView().showReport(this.data);
		this.configureClose(this.getView());
	}
}
