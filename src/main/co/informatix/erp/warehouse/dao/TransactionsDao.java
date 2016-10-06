package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.utils.Constantes;
import co.informatix.erp.warehouse.entities.Transactions;

/**
 * DAO class that establishes the connection between business logic and
 * database. TransactionAction used for managing transactions.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class TransactionsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a Transaction in the database.
	 * 
	 * @param transaction
	 *            : Save transaction.
	 * @throws Exception
	 */
	public void saveTransaction(Transactions transaction) throws Exception {
		em.persist(transaction);
	}

	/**
	 * Consult the list of transactions that comply with the option of force.
	 * 
	 * @modify 06/10/2016 Andres.Gomez
	 * 
	 * @param start
	 *            : Registry where consultation begins
	 * @param range
	 *            : Range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user
	 * @param parameters
	 *            : Query parameters
	 * @return List<Transactions>:List of transactions that comply with the
	 *         condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Transactions> consultTransactions(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM Transactions t ");
		query.append("LEFT JOIN FETCH t.activities a ");
		query.append("LEFT JOIN FETCH a.activityName ");
		query.append("LEFT JOIN FETCH t.hr ");
		query.append("LEFT JOIN FETCH t.deposits d ");
		query.append("LEFT JOIN FETCH d.materials ");
		query.append("JOIN FETCH t.transactionType tt ");
		query.append(consult);
		query.append(" ORDER BY t.idTransaction");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing transactions in the database.
	 * 
	 * @modify 06/10/2016 Andres.Gomez
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Long: quantity of registers.
	 * @throws Exception
	 */
	public Long quantityTransactions(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(t) FROM Transactions t ");
		query.append("JOIN t.transactionType tt ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method allows consult a transaction in the database filtering
	 * information search by the values sent.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param idDeposit
	 *            : deposit identifier.
	 * @param idActivity
	 *            : activity identifier.
	 * @param idTransactionType
	 *            : transactionType identifier.
	 * @return Transactions: Transactions found with the parameters sent.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Transactions consultTransactionsByDepositsActivityAndTransactionType(
			int idDeposit, int idActivity, int idTransactionType)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM Transactions t ");
		query.append("WHERE t.deposits.idDeposit=:idDeposit ");
		query.append("AND t.activities.idActivity=:idActivity ");
		query.append("AND t.transactionType.idTransactionType=:idTransactionType ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idDeposit", idDeposit);
		q.setParameter("idActivity", idActivity);
		q.setParameter("idTransactionType", idTransactionType);
		List<Transactions> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}

	/**
	 * This method allows consult a transaction in the database filtering
	 * information search by the values sent.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param idActivity
	 *            : activity identifier.
	 * @param idTransactionType
	 *            : transactionType identifier.
	 * @return List<Transactions>:List of transactions that comply with the
	 *         condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Transactions> consultTransactionsByActivityAndTransactionType(
			int idActivity, int idTransactionType) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM Transactions t ");
		query.append("JOIN FETCH t.deposits d ");
		query.append("JOIN FETCH d.materials ");
		query.append("JOIN FETCH t.hr ");
		query.append("WHERE t.activities.idActivity=:idActivity ");
		query.append("AND t.transactionType.idTransactionType=:idTransactionType ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		q.setParameter("idTransactionType", idTransactionType);
		List<Transactions> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows sum of the total quantity withdrawn according a
	 * activity materials identifier and transaction type withdraw
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idActivity
	 *            : Activity identifier.
	 * @param idMaterial
	 *            : Material identifier.
	 * @return Double: Sum of the total quantity withdrawn.
	 * @throws Exception
	 */
	public Double totalQuantityWithdrawn(int idActivity, int idMaterial)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(t.quantity) FROM  Transactions t ");
		query.append("JOIN t.deposits d ");
		query.append("JOIN d.materials m ");
		query.append("WHERE t.transactionType.idTransactionType=:idTransactionType ");
		query.append("AND t.activities.idActivity=:idActivity ");
		query.append("AND m.idMaterial = :idMaterial ");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("idTransactionType",
				Constantes.TRANSACTION_TYPE_WITHDRAWAL);
		queryResult.setParameter("idActivity", idActivity);
		queryResult.setParameter("idMaterial", idMaterial);
		if (queryResult.getSingleResult() != null) {
			return (Double) queryResult.getSingleResult();
		}
		return (0.0);
	}

}