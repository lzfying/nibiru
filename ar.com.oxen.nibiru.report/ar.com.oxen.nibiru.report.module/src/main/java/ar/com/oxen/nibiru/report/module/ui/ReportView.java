package ar.com.oxen.nibiru.report.module.ui;

import ar.com.oxen.nibiru.ui.api.mvp.HasCloseWidget;
import ar.com.oxen.nibiru.ui.api.mvp.View;

public interface ReportView extends View, HasCloseWidget {
	void showReport(byte[] data);

}
