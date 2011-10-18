package ar.com.oxen.nibiru.ui.vaadin.view.adapter;

import org.vaadin.peter.contextmenu.ContextMenu;
import org.vaadin.peter.contextmenu.ContextMenu.ClickEvent;
import org.vaadin.peter.contextmenu.ContextMenu.ContextMenuItem;

import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;
import ar.com.oxen.nibiru.ui.api.view.HasMenuItems;
import ar.com.oxen.nibiru.ui.api.view.MenuItem;

public class ContextMenuAdapter implements HasMenuItems {
	private ContextMenu contextMenu;

	public ContextMenuAdapter(ContextMenu contextMenu) {
		super();
		this.contextMenu = contextMenu;
	}

	@Override
	public MenuItem addMenuItem(String caption, int position) {
		/*
		 * Se ignora la posicion porque el componente no tiene forma de
		 * cambiarla
		 */
		ContextMenuItem contextMenuItem = this.contextMenu.addItem(caption);
		return new ContextMenuItemAdapter(contextMenuItem);
	}

	@Override
	public void removeMenuItem(MenuItem menuItem) {
		ContextMenuItemAdapter contextMenuItemToRemove = (ContextMenuItemAdapter) menuItem;
		this.contextMenu.removeItem(contextMenuItemToRemove
				.getContextMenuItem());
	}

	private class ContextMenuItemAdapter implements MenuItem {
		private ContextMenuItem contextMenuItem;

		public ContextMenuItemAdapter(ContextMenuItem contextMenuItem) {
			super();
			this.contextMenuItem = contextMenuItem;
		}

		@Override
		public void setClickHandler(final ClickHandler clickHandler) {
			contextMenu.addListener(new ContextMenu.ClickListener() {
				private static final long serialVersionUID = 4738246495084843675L;

				public void contextItemClick(ClickEvent event) {
					if (contextMenuItem == event.getClickedItem()) {
						clickHandler.onClick();
					}
				}
			});
		}

		@Override
		public MenuItem addMenuItem(String caption, int position) {
			ContextMenuItem childContextMenuItem = this.contextMenuItem
					.addItem(caption);
			return new ContextMenuItemAdapter(childContextMenuItem);
		}

		@Override
		public void removeMenuItem(MenuItem menuItem) {
			ContextMenuItemAdapter contextMenuItemToRemove = (ContextMenuItemAdapter) menuItem;
			this.contextMenuItem.removeItem(contextMenuItemToRemove
					.getContextMenuItem());
		}

		public ContextMenuItem getContextMenuItem() {
			return contextMenuItem;
		}
	}
}
