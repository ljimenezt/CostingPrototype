package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.TransactionType;

/**
 * DAO class that establishes the connection between business logic and
 * database. TransactionTypeAction used for managing transactionType.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class TransactionTypeDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a TransactionType in the database.
	 * 
	 * @author Sergio.Ortiz
	 * @param transactionType
	 *            : Save transactionType.
	 * @throws Exception
	 */
	public void saveTransactionType(TransactionType transactionType)
			throws Exception {
		em.persist(transactionType);
	}

	/**
	 * Modify a TransactionType in the database.
	 * 
	 * @author Sergio.Ortiz
	 * @param transactionType
	 *            : TransactionType to edit.
	 * @throws Exception
	 */
	public void editTransactionType(TransactionType transactionType)
			throws Exception {
		em.merge(transactionType);
	}

	/**
	 * Removes the BD TransactionType.
	 * 
	 * @author Sergio.Ortiz
	 * @param transactionType
	 *            : TransactionType to be removed
	 * @throws Exception
	 */
	public void deleteTransactionType(TransactionType transactionType)
			throws Exception {
		em.remove(em.merge(transactionType));
	}

	/**
	 * Consult the list of TransactionType that comply with the option of force.
	 * 
	 * @author Sergio.Ortiz
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
	 * @return List<TransactionType>:List of TransactionType that comply with
	 *         the condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TransactionType> consultarTransactionType(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM TransactionType t ");
		query.append(consult);
		query.append(" ORDER BY t.transactionType");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing transactionType in the database that are
	 * existing or not existing.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param consulta
	 *            : Query running on SQL.
	 * @param parametros
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long cantidadtransactionType(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(t) FROM TransactionType t ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

}
