package ar.com.oxen.nibiru.crud.ui.generic.presenter.list;

import java.util.LinkedList;
import java.util.List;

import ar.com.oxen.commons.eventbus.api.EventBus;
import ar.com.oxen.commons.eventbus.api.EventHandler;
import ar.com.oxen.nibiru.conversation.api.Conversation;
import ar.com.oxen.nibiru.conversation.api.ConversationCallback;
import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.manager.api.CrudField;
import ar.com.oxen.nibiru.crud.manager.api.CrudManager;
import ar.com.oxen.nibiru.crud.manager.api.ModifiedCrudEntityEvent;
import ar.com.oxen.nibiru.crud.ui.api.list.CrudListView;
import ar.com.oxen.nibiru.crud.ui.generic.presenter.AbstractGenericCrudPresenter;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionPointManager;
import ar.com.oxen.nibiru.extensionpoint.api.ExtensionTracker;
import ar.com.oxen.nibiru.ui.api.mvp.ClickHandler;

public abstract class AbstractGenericCrudListPresenter extends
		AbstractGenericCrudPresenter<CrudListView> {
	private EventHandler<ModifiedCrudEntityEvent> modifiedEventHandler;
	private List<CrudEntity<?>> entities = new LinkedList<CrudEntity<?>>();

	public AbstractGenericCrudListPresenter(CrudManager<?> crudManager,
			EventBus eventBus, Conversation conversation,
			ExtensionPointManager extensionPointManager) {
		super(crudManager, eventBus, conversation, extensionPointManager);
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

		this.refreshData();

		this.modifiedEventHandler = new EventHandler<ModifiedCrudEntityEvent>() {
			@Override
			public void onEvent(ModifiedCrudEntityEvent event) {
				refreshEntity(event.getId());
			}
		};

		this.getEventBus().addHandler(ModifiedCrudEntityEvent.class,
				this.modifiedEventHandler, this.getTopic());
	}

	private void refreshData() {
		this.entities.clear();

		for (CrudEntity<?> entity : this.findEntities()) {
			this.entities.add(entity);
		}

		this.refreshTable();
	}

	private void refreshEntity(Object id) {
		if (id != null) {
			CrudEntity<?> entity = this.getCrudManager().findById(id);

			for (int n = 0; n < this.entities.size(); n++) {
				if (this.entities.get(n).getId().equals(id)) {
					this.entities.set(n, entity);
					break;
				}
				this.refreshTable();
			}
		} else {
			this.refreshData();
		}
	}

	private void refreshTable() {
		this.getView().clearTable();
		List<CrudField> fields = this.getConversation().execute(
				new ConversationCallback<List<CrudField>>() {
					@Override
					public List<CrudField> doInConversation(
							Conversation conversation) throws Exception {
						return getCrudManager().getListFields();
					}
				});
		for (CrudField field : fields) {
			this.getView().addColumn(field.getName(), field.getType(),
					field.getListInfo().getColumnWidth());
		}

		for (CrudEntity<?> entity : this.entities) {
			Object[] values = new Object[fields.size()];
			for (int n = 0; n < values.length; n++) {
				values[n] = entity.getValue(fields.get(n));
			}
			this.getView().addRow(values);
		}

	}

	protected abstract <K> List<CrudEntity<K>> findEntities();

	private void addActions(final CrudActionExtension<?> extension) {
		for (final CrudAction action : extension.getActions()) {
			if (action.isVisibleInList()) {
				if (action.isEntityRequired()) {
					this.getView().addEntityAction(action.getName(),
							new ClickHandler() {
								@Override
								public void onClick() {
									performAction(action, entities
											.get(getView().getSelectedRow()),
											extension);
								}
							});
				} else {
					this.getView().addGlobalAction(action.getName(),
							new ClickHandler() {
								@Override
								public void onClick() {
									performAction(action, null, extension);
								}
							});
				}
			}
		}

	}
}
