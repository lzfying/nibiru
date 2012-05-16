package ar.com.oxen.nibiru.crud.ui.api.list;

import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.mvp.HasCloseHandler;
import ar.com.oxen.nibiru.ui.api.mvp.HasCloseWidget;
import ar.com.oxen.nibiru.ui.api.mvp.View;

public interface CrudListView extends View, HasCloseWidget {
	void setEntityName(String entityName);

	void addGlobalAction(String label, ClickHandler clickHandler);

	void addEntityAction(String label, ClickHandler clickHandler);

	void clearTable();

	void addColumn(String name, Class<?> type, int width);

	void addRow(Object[] values);
	
	int getSelectedRow();
	
	HasCloseHandler getCloseHander();
}
