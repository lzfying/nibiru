package ar.com.oxen.nibiru.crud.ui.generic;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.conversation.api.ConversationFactory;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.manager.api.EditCrudEntityEvent;
import ar.com.oxen.nibiru.crud.ui.api.CrudPresenterFactory;
import ar.com.oxen.nibiru.crud.ui.api.form.CrudFormView;
import ar.com.oxen.nibiru.crud.ui.api.list.CrudListView;
import ar.com.oxen.nibiru.crud.ui.generic.form.GenericCrudFormPresenter;
import ar.com.oxen.nibiru.crud.ui.generic.list.GenericCrudListPresenter;
import ar.com.oxen.nibiru.crud.ui.generic.list.GenericCrudListPresenterByParent;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.ui.api.mvp.Presenter;

public class GenericCrudPresenterFactory implements CrudPresenterFactory {
	private EventBus eventBus;
	private ExtensionPointManager extensionPointManager;
	private ConversationFactory conversationFactory;

	@Override
	public Presenter<CrudListView> buildListPresenter(
			CrudManager<?> crudManager) {
		return new GenericCrudListPresenter(crudManager, this.eventBus, 
				this.conversationFactory.buildConversation(),
				this.extensionPointManager);
	}

	@Override
	public Presenter<CrudListView> buildListPresenter(
			CrudManager<?> crudManager, String parentField, Object parentValue) {
		return new GenericCrudListPresenterByParent(crudManager, this.eventBus,
				this.conversationFactory.buildConversation(),
				this.extensionPointManager, parentField, parentValue);
	}

	@Override
	public Presenter<CrudFormView> buildFormPresenter(
			CrudManager<?> crudManager, EditCrudEntityEvent event) {
		return new GenericCrudFormPresenter(crudManager, this.eventBus, 
				event.getConversation(), event.getCrudEntity(),
				this.extensionPointManager);
	}

	public void setEventBus(EventBus eventBus) {
		this.eventBus = eventBus;
	}

	public void setExtensionPointManager(
			ExtensionPointManager extensionPointManager) {
		this.extensionPointManager = extensionPointManager;
	}

	public void setConversationFactory(ConversationFactory conversationFactory) {
		this.conversationFactory = conversationFactory;
	}

}
