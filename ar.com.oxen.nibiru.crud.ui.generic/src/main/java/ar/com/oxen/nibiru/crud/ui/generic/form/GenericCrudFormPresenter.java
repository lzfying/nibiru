package ar.com.oxen.nibiru.crud.ui.generic.form;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.ui.api.form.CrudFormView;
import ar.com.oxen.nibiru.crud.ui.generic.AbstractGenericCrudPresenter;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionTracker;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;

public class GenericCrudFormPresenter extends
		AbstractGenericCrudPresenter<CrudFormView> {
	private CrudEntity<?> entity;

	public GenericCrudFormPresenter(CrudManager<?> crudManager,
			EventBus eventBus, Conversation conversation, CrudEntity<?> entity,
			ExtensionPointManager extensionPointManager) {
		super(crudManager, eventBus, conversation, extensionPointManager);
		this.entity = entity;
	}

	@Override
	public void go() {
		this.getView().setEntityName(this.getCrudManager().getEntityTypeName());

		this.configureClose(this.getView());

		this.getExtensionPointManager().registerTracker(
				new ExtensionTracker<CrudActionExtension<?>>() {

					@Override
					public void onRegister(CrudActionExtension<?> extension) {
						addActions(extension);
					}

					@Override
					public void onUnregister(CrudActionExtension<?> extension) {
						// TODO remover acciones
					}

				}, this.getTopic(), CrudActionExtension.class);

		for (CrudField field : this.getCrudManager().getFormFields()) {
			Object value = this.entity.getValue(field);
			if (value != null || !field.getFormInfo().isReadonly()) {
				this.getView().addField(field, this.entity);

				if (value != null) {
					this.getView().setFieldValue(field.getName(), value);
				}
			}
		}

	}

	private void addActions(final CrudActionExtension<?> extension) {
		for (final CrudAction action : extension.getActions()) {
			if (action.isVisibleInForm()) {
				this.getView().addEntityAction(action.getName(),
						new ClickHandler() {
							@Override
							public void onClick() {
								for (CrudField field : getCrudManager()
										.getFormFields()) {
									if (!field.getFormInfo().isReadonly()) {
										Object value = getView().getFieldValue(
												field.getName());
										entity.setValue(field, value);
									}
								}

								performAction(action, entity, extension);

								getView().close();
							}
						});
			}
		}
	}

	@Override
	protected <K> void onReturnedEntity(CrudEntity<K> returnedEntity) {
	}
}
