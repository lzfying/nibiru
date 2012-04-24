package ar.com.oxen.nibiru.ui.utils.extension;

import ar.com.oxen.nibiru.ui.api.extension.SubMenuExtension;

public class SimpleSubMenuExtension implements SubMenuExtension {
	private String name;
	private String extensionPoint;
	private int position;

	public SimpleSubMenuExtension() {
		super();
	}

	public SimpleSubMenuExtension(String name, String extensionPoint,
			int position) {
		super();
		this.name = name;
		this.extensionPoint = extensionPoint;
		this.position = position;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setExtensionPoint(String extensionPoint) {
		this.extensionPoint = extensionPoint;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getExtensionPoint() {
		return this.extensionPoint;
	}

	@Override
	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}
}
