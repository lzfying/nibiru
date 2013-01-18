package ar.com.oxen.nibiru.crud.manager.jpa;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ar.com.oxen.commons.bean.api.WrapperFactory;
import ar.com.oxen.nibiru.crud.bean.annotation.Action;
import ar.com.oxen.nibiru.crud.bean.annotation.Actions;
import ar.com.oxen.nibiru.crud.manager.api.CrudAction;
import ar.com.oxen.nibiru.crud.manager.api.CrudActionExtension;
import ar.com.oxen.nibiru.crud.manager.api.CrudEntity;
import ar.com.oxen.nibiru.crud.utils.SimpleCrudAction;

public class JpaCrudActionExtension<T> extends AbstractJpaCrudComponent<T>
		implements CrudActionExtension<T> {
	private UserTransaction userTransaction;

	public JpaCrudActionExtension(EntityManager entityManager,
			Class<T> persistentClass, WrapperFactory wrapperFactory,
			UserTransaction userTransaction) {
		super(entityManager, persistentClass, wrapperFactory);
		this.userTransaction = userTransaction;
	}

	@Override
	public List<CrudAction> getGlobalActions() {
		return this.buildActionList(false);
	}

	@Override
	public List<CrudAction> getEntityActions(CrudEntity<T> entity) {
		return this.buildActionList(true);
	}

	private List<CrudAction> buildActionList(boolean requiresEntity) {
		Actions actions = this.getPersistentClass()
				.getAnnotation(Actions.class);
		List<CrudAction> crudActions;

		if (actions != null) {
			crudActions = new ArrayList<CrudAction>(actions.value().length);
			for (Action action : actions.value()) {
				if (action.requiresEntity() == requiresEntity) {
					boolean modifiesEntity = action.name().equals(
							CrudAction.UPDATE)
							|| action.name().equals(CrudAction.DELETE);

					crudActions.add(new SimpleCrudAction(action.name(), action
							.requiresEntity(), action.requiresConfirmation(),
							action.showInList(), modifiesEntity, action
									.showInForm(), action.allowedRoles()));
				}
			}
		} else {
			crudActions = new ArrayList<CrudAction>(0);
		}

		return crudActions;
	}

	@Override
	public CrudEntity<T> performGlobalAction(CrudAction action) {
		if (CrudAction.NEW.equals(action.getName())) {
			return new JpaCrudEntity<T>(this.getWrapperFactory().wrapNewBean(
					this.getPersistentClass()), this.getEntityManager(),
					this.getPkName(), this.getFormFields());
		} else {
			throw new IllegalArgumentException("Invalid action: " + action);
		}
	}

	@Override
	public CrudEntity<T> performEntityAction(CrudAction action,
			final CrudEntity<T> entity) {
		if (CrudAction.EDIT.equals(action.getName())) {
			return entity;
		} else if (CrudAction.UPDATE.equals(action.getName())) {
			this.doInTransacction(new TransactionCallback() {
				@Override
				public void execute() {
					getEntityManager().merge(entity.getEntity());
				}
			});
			return null;
		} else if (CrudAction.DELETE.equals(action.getName())) {
			this.doInTransacction(new TransactionCallback() {
				@Override
				public void execute() {
					getEntityManager().remove(
							getEntityManager().merge(entity.getEntity()));
				}
			});
			return null;
		} else {
			throw new IllegalArgumentException("Invalid action: " + action);
		}
	}

	@Override
	public String[] getAllowedRoles() {
		return null;
	}

	private void doInTransacction(TransactionCallback callback) {
		try {
			this.userTransaction.begin();
			this.getEntityManager().joinTransaction();
			callback.execute();
			this.userTransaction.commit();
		} catch (NotSupportedException e) {
			throw new IllegalStateException(e);
		} catch (SystemException e) {
			throw new IllegalStateException(e);
		} catch (SecurityException e) {
			throw new IllegalStateException(e);
		} catch (RollbackException e) {
			throw new IllegalStateException(e);
		} catch (HeuristicMixedException e) {
			throw new IllegalStateException(e);
		} catch (HeuristicRollbackException e) {
			throw new IllegalStateException(e);
		}
	}

	private interface TransactionCallback {
		void execute();
	}
}
