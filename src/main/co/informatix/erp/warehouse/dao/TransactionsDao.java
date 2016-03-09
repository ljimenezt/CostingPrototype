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
		query.append("JOIN FETCH t.activities a ");
		query.append("JOIN FETCH a.activityName ");
		query.append("JOIN FETCH t.hr ");
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
	 * Returns the number of existing transactions in the database that are
	 * existing or not existing.
	 * 
	 * @param consulta
	 *            : Query running on SQL.
	 * @param parametros
	 *            :Parameters of the query.
	 * @return Long: quantity of registers.
	 * @throws Exception
	 */
	public Long quantityTransactions(StringBuilder consult,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(t) FROM Transactions t ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

}
