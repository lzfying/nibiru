package ar.com.oxen.nibiru.ui.utils.extension;

import ar.com.oxen.nibiru.ui.api.extension.MenuItemExtension;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;

public class SimpleMenuItemExtension implements MenuItemExtension {
	private String name;
	private int position;
	private ClickHandler clickHandler;

	public SimpleMenuItemExtension() {
		super();
	}

	public SimpleMenuItemExtension(String name, int position,
			ClickHandler clickHandler) {
		super();
		this.name = name;
		this.position = position;
		this.clickHandler = clickHandler;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public void onClick() {
		this.clickHandler.onClick();
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setClickHandler(ClickHandler clickHandler) {
		this.clickHandler = clickHandler;
	}

	@Override
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
