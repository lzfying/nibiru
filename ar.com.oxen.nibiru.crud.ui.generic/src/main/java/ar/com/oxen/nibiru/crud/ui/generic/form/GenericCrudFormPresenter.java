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
import ar.com.oxen.nibiru.validation.api.Validator;

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

		// TOOD: Falta des-registrar el tracker cuando se cierra el presenter!!
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

		for (final CrudField field : this.getCrudManager().getFormFields()) {
			Object value = this.entity.getValue(field);
			
			/* Readonly and null fields (i.e. IDs) are not shown */
			if (value != null || !field.getFormInfo().isReadonly()) {
				this.getView().addField(field, this.entity);

				if (value != null) {
					this.getView().setFieldValue(field.getName(), value);
				}
				
				/* Register validator tracker for this field */
				// TOOD: Falta des-registrar el tracker cuando se cierra el presenter!!
				this.getExtensionPointManager().registerTracker(
						new ExtensionTracker<Validator<?>>() {
							@Override
							public void onRegister(Validator<?> validator) {
								getView().addValidator(field.getName(),
										validator);
							}

							@Override
							public void onUnregister(Validator<?> validator) {
								getView().removeValidator(field.getName(),
										validator);
							}
						}, this.getTopic() + "." + field.getName(),
						Validator.class);
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
