package ar.com.oxen.nibiru.ui.api.view;

public interface Table extends Component, HasMenuItems {
	void addColumn(String label, Class<?> type);

	void addColumn(String label, Class<?> type, int width);

	void addRow(Object... values);

	void removeAllRows();

	int getSelectedRow();
}
