package ar.com.oxen.nibiru.ui.api.view;

import ar.com.oxen.nibiru.ui.api.mvp.HasCloseHandler;
import ar.com.oxen.nibiru.ui.api.mvp.HasValue;

public interface Window extends HasValue<String>, ComponentContainer, HasCloseHandler {
	void show();

	void close();
}
