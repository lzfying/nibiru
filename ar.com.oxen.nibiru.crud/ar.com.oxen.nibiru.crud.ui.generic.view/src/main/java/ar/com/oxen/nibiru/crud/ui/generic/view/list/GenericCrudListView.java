package ar.com.oxen.nibiru.crud.ui.generic.view.list;

import ar.com.oxen.nibiru.crud.ui.api.CrudViewFactory;
import ar.com.oxen.nibiru.crud.ui.api.list.CrudListView;
import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.view.Button;
import ar.com.oxen.nibiru.ui.api.view.MenuItem;
import ar.com.oxen.nibiru.ui.api.view.Panel;
import ar.com.oxen.nibiru.ui.api.view.Table;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;
import ar.com.oxen.nibiru.ui.api.view.Window;
import ar.com.oxen.nibiru.ui.utils.view.AbstractWindowViewAdapter;

public class GenericCrudListView extends AbstractWindowViewAdapter<Window>
		implements CrudListView {
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
	public void addGlobalAction(String label, ClickHandler clickHandler) {
		Button button = this.getViewFactory().buildButton();
		button.setValue(this.getMessageSource().getMessage(
				CrudViewFactory.I18N_ACTION_PREFIX + label));
		button.setClickHandler(clickHandler);
		this.globalActionsPanel.addComponent(button);
	}

	@Override
	public void addEntityAction(String label, ClickHandler clickHandler) {
		MenuItem menu = this.table.addMenuItem(this.getMessageSource()
				.getMessage(CrudViewFactory.I18N_ACTION_PREFIX + label), 999);
		menu.setClickHandler(clickHandler);
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

}
