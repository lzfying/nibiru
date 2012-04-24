package ar.com.oxen.nibiru.crud.utils;

import java.util.LinkedList;
import java.util.List;

import ar.com.oxen.commons.eventbus.api.EventHandler;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.manager.api.EditCrudEntityEvent;
import ar.com.oxen.nibiru.crud.manager.api.ManageChildCrudEntitiesEvent;
import ar.com.oxen.nibiru.crud.manager.api.ManageCrudEntitiesEvent;
import ar.com.oxen.nibiru.crud.ui.api.CrudPresenterFactory;
import ar.com.oxen.nibiru.crud.ui.api.CrudViewFactory;
import ar.com.oxen.nibiru.module.utils.AbstractModuleConfigurator;
import ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension;
import ar.com.oxen.nibiru.ui.utils.extension.SimpleMenuItemExtension;
import ar.com.oxen.nibiru.ui.utils.mvp.SimpleEventBusClickHandler;

public abstract class AbstractCrudModuleConfigurator extends
		AbstractModuleConfigurator<CrudViewFactory, CrudPresenterFactory> {
	private List<EventHandler<?>> registeredHandlers = new LinkedList<EventHandler<?>>();

	int menuPos = 0;

	/**
	 * Adds a CRUD menu
	 */
	protected <K> void addCrudMenu(String menuName, String parentMenuExtension,
			CrudManager<K> crudManager) {
		this.registerMenu(menuName, parentMenuExtension, crudManager);
	}

	/**
	 * Adds a CRUD without a menu option
	 */
	protected <K> void addCrud(CrudManager<K> crudManager,
			CrudActionExtension<K> crudActionExtension) {
		this.registerManageEntityEvent(crudManager);
		this.registerActions(crudManager, crudActionExtension);
		this.registerEditEntityEvent(crudManager);
	}

	/**
	 * Adds a CRUD with a menu option
	 */
	protected <K> void addCrudWithMenu(String menuName,
			String parentMenuExtension, CrudManager<K> crudManager,
			CrudActionExtension<K> crudActionExtension) {
		this.addCrudMenu(menuName, parentMenuExtension, crudManager);
		this.addCrud(crudManager, crudActionExtension);
	}

	/**
	 * Adds a child menu CRUD menu option
	 */
	protected <T> void addChildCrudMenu(String menuName,
			CrudManager<?> parentCrudManager, String parentField,
			CrudManager<T> childCrudManager) {

		this.registerManageChildrenAction(menuName, parentCrudManager,
				childCrudManager, parentField);
	}

	/**
	 * Adds a child menu CRUD without a menu option
	 */
	protected <T> void addChildCrud(CrudManager<?> parentCrudManager,
			CrudManager<T> childCrudManager,
			CrudActionExtension<T> childCrudActionExtension) {

		this.registerActions(childCrudManager, childCrudActionExtension);
		this.registerManageChildEntitiesEvent(parentCrudManager,
				childCrudManager);
		this.registerEditEntityEvent(childCrudManager);
	}

	/**
	 * Adds a child menu CRUD with a menu option
	 */
	protected <T> void addChildCrudWithMenu(String menuName,
			CrudManager<?> parentCrudManager, String parentField,
			CrudManager<T> childCrudManager,
			CrudActionExtension<T> childCrudActionExtension) {

		this.addChildCrudMenu(menuName, parentCrudManager, parentField,
				childCrudManager);
		this.addChildCrud(parentCrudManager, childCrudManager,
				childCrudActionExtension);
	}

	@Override
	public void shutdown() {
		super.shutdown();
		for (EventHandler<?> handler : this.registeredHandlers) {
			this.getEventBus().removeHandler(handler);
		}
	}

	private void registerMenu(String menuName, String parentMenuExtension,
			CrudManager<?> crudManager) {
		this.registerExtension(new SimpleMenuItemExtension(menuName, menuPos++,
				new SimpleEventBusClickHandler(this.getEventBus(),
						ManageCrudEntitiesEvent.class, crudManager
								.getEntityTypeName())), parentMenuExtension,
				MenuItemExtension.class);
	}

	private <K> void registerActions(CrudManager<K> crudManager,
			CrudActionExtension<K> crudActionExtension) {
		this.registerExtension(crudActionExtension, crudManager
				.getEntityTypeName(), CrudActionExtension.class);
	}

	private <K> void registerManageChildrenAction(String menuName,
			CrudManager<?> parentCrudManager,
			final CrudManager<?> childCrudManager, String parentField) {
		this.registerExtension(new ManageChildrenCrudActionExtension<Object>(
				menuName, parentField, childCrudManager.getEntityTypeName(),
				this.getEventBus()), parentCrudManager.getEntityTypeName(),
				CrudActionExtension.class);
	}

	private void registerManageEntityEvent(final CrudManager<?> crudManager) {
		this.addEventHandler(ManageCrudEntitiesEvent.class,
				new EventHandler<ManageCrudEntitiesEvent>() {

					@Override
					public void onEvent(ManageCrudEntitiesEvent event) {
						activate(getViewFactory().buildListView(),
								getPresenterFactory().buildListPresenter(
										crudManager));
					}
				}, crudManager.getEntityTypeName());
	}

	private void registerEditEntityEvent(final CrudManager<?> crudManager) {
		this.addEventHandler(EditCrudEntityEvent.class,
				new EventHandler<EditCrudEntityEvent>() {

					@Override
					public void onEvent(EditCrudEntityEvent event) {
						activate(getViewFactory().buildFormView(),
								getPresenterFactory().buildFormPresenter(
										crudManager, event));
					}
				}, crudManager.getEntityTypeName());
	}

	private void registerManageChildEntitiesEvent(
			CrudManager<?> parentCrudManager,
			final CrudManager<?> childCrudManager) {
		this.addEventHandler(ManageChildCrudEntitiesEvent.class,
				new EventHandler<ManageChildCrudEntitiesEvent>() {

					@Override
					public void onEvent(ManageChildCrudEntitiesEvent event) {
						activate(getViewFactory().buildListView(),
								getPresenterFactory().buildListPresenter(
										childCrudManager,
										event.getParentField(),
										event.getParentEntity()));
					}
				}, childCrudManager.getEntityTypeName());
	}

	private <T> void addEventHandler(Class<T> eventClass,
			EventHandler<T> handler, String topic) {
		this.registeredHandlers.add(handler);
		this.getEventBus().addHandler(eventClass, handler, topic);
	}
}
