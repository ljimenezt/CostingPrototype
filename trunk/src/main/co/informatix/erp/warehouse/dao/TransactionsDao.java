package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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
		query.append("JOIN FETCH t.transactionType ");
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
}