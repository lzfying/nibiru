package ar.com.oxen.nibiru.dynamicbundle.module;

import ar.com.oxen.commons.eventbus.api.EventHandlerMethod;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.manager.api.EditCrudEntityEvent;
import ar.com.oxen.nibiru.crud.ui.api.CrudPresenterFactory;
import ar.com.oxen.nibiru.crud.ui.api.CrudViewFactory;
import ar.com.oxen.nibiru.dynamicbundle.domain.DynamicBundle;
import ar.com.oxen.nibiru.module.utils.AbstractModuleConfigurator;
import ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension;
import ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleMenuItemExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleSubMenuExtension;
import ar.com.oxen.nibiru.ui.utils.mvp.SimpleEventBusClickHandler;


public class ModuleConfigurator extends
		AbstractModuleConfigurator<CrudViewFactory, CrudPresenterFactory> {
	private CrudManager<DynamicBundle> dynamicBundleCrudManager;
	public static final String TOPIC = "ar.com.oxen.nibiru.dynamicbundle.domain.DynamicBundle";

	@Override
	protected void configure() {
		this.registerExtension(new SimpleSubMenuExtension("dynamicbundle",
				"ar.com.oxen.nibiru.menu.dynamicbundle",0 ), "ar.com.oxen.nibiru.menu",
				SubMenuExtension.class);

		this.registerExtension(new SimpleMenuItemExtension("dynamicbundle.manage", 0,
				new SimpleEventBusClickHandler(this.getEventBus(),
						ManageDynamicBundlesEvent.class, TOPIC)),
				"ar.com.oxen.nibiru.menu.dynamicbundle", MenuItemExtension.class);
	}

	
	@EventHandlerMethod(topic = TOPIC)
	public void onManageEntity(ManageDynamicBundlesEvent event) {
		
		activate(getViewFactory().buildListView(), getPresenterFactory()
				.buildListPresenter(dynamicBundleCrudManager));
	}

	@EventHandlerMethod(topic = TOPIC)
	public void onEditEntity(EditCrudEntityEvent event) {
		activate(
				getViewFactory().buildFormView(),
				getPresenterFactory().buildFormPresenter(
						dynamicBundleCrudManager, event));
	}

	public void setDynamicBundleCrudManager(
			CrudManager<DynamicBundle> dynamicBundleCrudManager) {
		this.dynamicBundleCrudManager = dynamicBundleCrudManager;
	}
}
