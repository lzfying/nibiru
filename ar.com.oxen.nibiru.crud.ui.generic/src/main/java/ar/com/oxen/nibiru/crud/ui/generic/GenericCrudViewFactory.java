package ar.com.oxen.nibiru.crud.ui.generic;

import ar.com.oxen.nibiru.crud.ui.api.CrudViewFactory;
import ar.com.oxen.nibiru.crud.ui.api.form.CrudFormView;
import ar.com.oxen.nibiru.crud.ui.api.list.CrudListView;
import ar.com.oxen.nibiru.crud.ui.generic.form.GenericCrudFormView;
import ar.com.oxen.nibiru.crud.ui.generic.list.GenericCrudListView;
import ar.com.oxen.nibiru.i18n.api.MessageSource;
import ar.com.oxen.nibiru.ui.api.view.ViewFactory;

public class GenericCrudViewFactory implements CrudViewFactory {
	private ViewFactory viewFactory;
	private MessageSource messageSource;

	@Override
	public CrudListView buildListView() {
		return new GenericCrudListView(this.viewFactory, this.messageSource);
	}

	@Override
	public CrudFormView buildFormView() {
		return new GenericCrudFormView(this.viewFactory, this.messageSource);
	}

	public void setViewFactory(ViewFactory viewFactory) {
		this.viewFactory = viewFactory;
	}

	public void setMessageSource(MessageSource messageSource) {
		this.messageSource = messageSource;
	}
}
