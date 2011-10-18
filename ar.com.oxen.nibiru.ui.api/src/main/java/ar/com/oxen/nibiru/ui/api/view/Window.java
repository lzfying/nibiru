package ar.com.oxen.nibiru.ui.api.view;

import ar.com.oxen.nibiru.ui.api.mvp.HasValue;

public interface Window extends HasValue<String>, ComponentContainer {
	void show();

	void close();
}
