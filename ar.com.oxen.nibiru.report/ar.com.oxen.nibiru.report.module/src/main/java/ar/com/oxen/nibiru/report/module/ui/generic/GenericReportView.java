package ar.com.oxen.nibiru.report.module.ui.generic;

import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.report.module.ui.ReportView;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;
import ar.com.oxen.nibiru.ui.api.view.Window;
import ar.com.oxen.nibiru.ui.utils.view.AbstractWindowViewAdapter;

public class GenericReportView extends AbstractWindowViewAdapter<Window>
		implements ReportView {

	public GenericReportView(ViewFactory viewFactory,
			MessageSource messageSource) {
		super(viewFactory.buildWindow(), viewFactory, messageSource);

		this.getAdapted()
				.setValue(
						messageSource
								.getMessage("ar.com.oxen.nibiru.crud.entity.ar.com.oxen.nibiru.report.api.Report"));
		this.addClose();
	}

}
