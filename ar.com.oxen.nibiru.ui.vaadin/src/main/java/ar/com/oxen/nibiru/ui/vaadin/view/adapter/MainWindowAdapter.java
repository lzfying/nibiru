package ar.com.oxen.nibiru.ui.vaadin.view.adapter;

import java.util.LinkedList;
import java.util.List;

import ar.com.oxen.nibiru.ui.api.view.ComponentContainer;

import com.vaadin.Application;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Layout;
import com.vaadin.ui.MenuBar;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import com.vaadin.ui.MenuBar.MenuItem;

public class MainWindowAdapter extends AbstractWindowAdapter implements
		ar.com.oxen.nibiru.ui.api.view.MainWindow {
	private MenuBar mainMenu;
	private Application vaadinApplication;
	private List<Integer> positions = new LinkedList<Integer>();
	private Layout menuContainer;
	private Layout infoContainer;

	public MainWindowAdapter(Window adapted, Application vaadinApplication) {
		super(adapted);
		this.vaadinApplication = vaadinApplication;

		Layout upperContainer = new HorizontalLayout();
		upperContainer.setWidth("100%");
		getAdapted().addComponent(upperContainer);

		this.menuContainer = new HorizontalLayout();
		upperContainer.addComponent(this.menuContainer);

		this.infoContainer = new VerticalLayout();
		this.infoContainer.setWidth("100%");
		upperContainer.addComponent(this.infoContainer);
	}

	private MenuBar getMainMenu() {
		if (this.mainMenu == null) {
			synchronized (this) {
				if (this.mainMenu == null) {
					this.mainMenu = new MenuBar();
					this.menuContainer.addComponent(this.mainMenu);
				}
			}
		}
		return mainMenu;
	}

	@Override
	public ar.com.oxen.nibiru.ui.api.view.MenuItem addMenuItem(String caption,
			int position) {
		int listPosition = 0;
		while (listPosition < positions.size()
				&& positions.get(listPosition) < position) {
			listPosition++;
		}

		MenuItem menuItem;
		if (listPosition < positions.size()) {
			menuItem = this.getMainMenu().addItemBefore(caption, null, null,
					this.getMainMenu().getItems().get(listPosition));
		} else {
			menuItem = this.getMainMenu().addItem(caption, null);
		}

		this.positions.add(listPosition, position);

		return new MenuItemAdapter(menuItem);
	}

	@Override
	public void removeMenuItem(ar.com.oxen.nibiru.ui.api.view.MenuItem menuItem) {
		MenuItemAdapter menuItemAdapter = (MenuItemAdapter) menuItem;
		this.positions.remove(this.getMainMenu().getItems().indexOf(
				menuItemAdapter.getAdapted()));
		this.getMainMenu().removeItem(menuItemAdapter.getAdapted());
	}

	@Override
	public void show() {
		Window currentMainWindow = this.vaadinApplication.getMainWindow();
		if (currentMainWindow != null) {
			this.vaadinApplication.removeWindow(currentMainWindow);
		}
		this.vaadinApplication.setMainWindow(this.getAdapted());
	}

	@Override
	public ComponentContainer getInfoContainer() {
		return new LayoutPanelAdapter(this.infoContainer);
	}
}
