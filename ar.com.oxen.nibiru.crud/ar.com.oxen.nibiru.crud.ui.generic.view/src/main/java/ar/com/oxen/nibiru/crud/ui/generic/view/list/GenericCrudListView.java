package ar.com.oxen.nibiru.crud.ui.generic.view.list;

import ar.com.oxen.nibiru.crud.ui.api.CrudViewFactory;
import ar.com.oxen.nibiru.crud.ui.api.list.CrudListView;
import ar.com.oxen.nibiru.crud.ui.generic.view.AbstractCrudView;
import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.mvp.HasCloseHandler;
import ar.com.oxen.nibiru.ui.api.view.MenuItem;
import ar.com.oxen.nibiru.ui.api.view.Panel;
import ar.com.oxen.nibiru.ui.api.view.Table;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;

public class GenericCrudListView extends AbstractCrudView implements
		CrudListView {
	private Table table;
	private Panel globalActionsPanel;

	public GenericCrudListView(ViewFactory viewFactory,
			MessageSource messageSource) {
		super(viewFactory.buildWindow(), viewFactory, messageSource);

		this.table = this.getViewFactory().buildTable();
		this.getAdapted().addComponent(table);

		Panel buttonsPanel = viewFactory.buildHorizontalPanel();
		this.getAdapted().addComponent(buttonsPanel);

		this.globalActionsPanel = viewFactory.buildHorizontalPanel();
		buttonsPanel.addComponent(this.globalActionsPanel);

		this.addClose(buttonsPanel);
	}

	@Override
	public void setEntityName(String entityName) {
		String translatedEntityName = this.getMessageSource().getMessage(
				CrudViewFactory.I18N_ENTITY_PREFIX + entityName);
		this.getAdapted().setValue(
				this.getMessageSource().getMessage(
						"ar.com.oxen.nibiru.app.manage", translatedEntityName));
	}

	@Override
	public void addGlobalAction(String label, boolean requiresConfirmation,
			ClickHandler clickHandler) {
		this.globalActionsPanel.addComponent(createActionButton(label,
				requiresConfirmation, clickHandler));
	}

	@Override
	public void addEntityAction(String label, boolean requiresConfirmation,
			ClickHandler clickHandler) {
		String action = this.i18nAction(label);
		MenuItem menu = this.table.addMenuItem(action, 999);
		menu.setClickHandler(this.addConfirmation(action, requiresConfirmation,
				clickHandler));
	}

	@Override
	public void clearTable() {
		this.table.removeAllRows();
	}

	@Override
	public void addColumn(String name, Class<?> type, int width) {
		String label = this.getMessageSource().getMessage(
				CrudViewFactory.I18N_FIELD_PREFIX + name);

		if (width >= 0) {
			this.table.addColumn(label, type, width);
		} else {
			this.table.addColumn(label, type);
		}
	}

	@Override
	public void addRow(Object[] values) {
		this.table.addRow(values);
	}

	@Override
	public int getSelectedRow() {
		return this.table.getSelectedRow();
	}

	@Override
	public HasCloseHandler getCloseHander() {
		return this.getAdapted();
	}
}
