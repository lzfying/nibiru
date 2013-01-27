package ar.com.oxen.nibiru.transaction.jta;

import javax.persistence.EntityManager;
import javax.transaction.HeuristicMixedException;
import javax.transaction.HeuristicRollbackException;
import javax.transaction.NotSupportedException;
import javax.transaction.RollbackException;
import javax.transaction.Status;
import javax.transaction.SystemException;
import javax.transaction.UserTransaction;

import ar.com.oxen.nibiru.transaction.api.TransactionCallback;
import ar.com.oxen.nibiru.transaction.api.TransactionTemplate;

public class JtaTransactionTemplate implements TransactionTemplate {
	private UserTransaction userTransaction;

	@Override
	public <T> T execute(EntityManager entityManager,
			TransactionCallback<T> callback) {
		try {
			boolean newTransaction = this.userTransaction.getStatus() == Status.STATUS_NO_TRANSACTION;
			if (newTransaction) {
				this.userTransaction.begin();
			}
			entityManager.joinTransaction();
			T returnValue = callback.doInTransaction();
			if (newTransaction) {
				this.userTransaction.commit();
			}
			return returnValue;
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

	public void setUserTransaction(UserTransaction userTransaction) {
		this.userTransaction = userTransaction;
	}
}
