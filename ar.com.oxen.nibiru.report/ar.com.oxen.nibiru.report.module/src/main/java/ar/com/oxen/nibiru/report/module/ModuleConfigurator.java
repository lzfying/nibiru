package ar.com.oxen.nibiru.report.module;

import ar.com.oxen.nibiru.crud.utils.AbstractCrudModuleConfigurator;
import ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleSubMenuExtension;
import ar.com.oxen.nibiru.report.crud.ReportCrudActionExtension;
import ar.com.oxen.nibiru.report.crud.ReportCrudManager;

public class ModuleConfigurator extends AbstractCrudModuleConfigurator {
	private static final String MENU_EXTENSION = "ar.com.oxen.nibiru.menu.reports";

	@Override
	protected void configure() {
		this.registerExtension(new SimpleSubMenuExtension("reports",
				MENU_EXTENSION, 1), "ar.com.oxen.nibiru.menu",
				SubMenuExtension.class);

		this.addCrudWithMenu("reports.manage", MENU_EXTENSION,
				new ReportCrudManager(this.getExtensionPointManager()),
				new ReportCrudActionExtension());
	}
}
