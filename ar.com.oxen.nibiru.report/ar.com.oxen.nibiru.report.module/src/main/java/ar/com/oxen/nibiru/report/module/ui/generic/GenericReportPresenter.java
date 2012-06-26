package ar.com.oxen.nibiru.report.module.ui.generic;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.report.module.ui.ReportView;
import ar.com.oxen.nibiru.ui.utils.mvp.AbstractPresenter;

public class GenericReportPresenter extends AbstractPresenter<ReportView> {
	public GenericReportPresenter(EventBus eventBus) {
		super(eventBus);
	}

	@Override
	public void go() {
		
		this.configureClose(this.getView());
		// TODO Auto-generated method stub
		
	}

}
