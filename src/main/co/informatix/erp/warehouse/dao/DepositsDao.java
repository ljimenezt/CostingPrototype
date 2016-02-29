package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.Deposits;

/**
 * DAO class that establishes the connection between business logic and
 * database. DepositsAction used for managing Deposits.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class DepositsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a Deposits in the database.
	 * 
	 * @author Sergio.Ortiz
	 * @param deposits
	 *            : Save Deposits.
	 * @throws Exception
	 */
	public void saveDeposits(Deposits deposits) throws Exception {
		em.persist(deposits);
	}

	/**
	 * Modify a Deposits in the database.
	 * 
	 * @author Sergio.Ortiz
	 * @param deposits
	 *            : Deposits to edit.
	 * @throws Exception
	 */
	public void editDeposits(Deposits deposits) throws Exception {
		em.merge(deposits);
	}

	/**
	 * Removes the BD Deposits.
	 * 
	 * @author Sergio.Ortiz
	 * @param deposits
	 *            : Deposits to be removed
	 * @throws Exception
	 */
	public void deleteDeposits(Deposits deposits) throws Exception {
		em.remove(em.merge(deposits));
	}

	/**
	 * Consult the list of Deposits that comply with the option of force.
	 * 
	 * @author Sergio.Ortiz
	 * @param start
	 *            : Registry where consultation begins
	 * @param range
	 *            : Range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user
	 * @param parameters
	 *            : Query parameters
	 * @return List<Deposits>:List of Deposits that comply with the condition of
	 *         validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Deposits> consultDeposits(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Deposits d ");
		query.append(consult);
		query.append(" ORDER BY d.dateTime");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing Deposits in the database that are existing
	 * or not existing.
	 * 
	 * @author Sergio.Ortiz
	 * @param consulta
	 *            : Query running on SQL.
	 * @param parametros
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long amountDeposits(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(d) FROM Deposits d ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult a deposits object assigned to sending its identifier.
	 * 
	 * @param nomObject
	 *            : subject to consultation in the company.
	 * @param idDeposits
	 *            : id consultation deposits.
	 * @return Object related to deposits information.
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectDeposits(String nomObject, int idDeposits)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT d." + nomObject + " FROM Deposits d "
								+ "WHERE d.id=:idDeposits")
				.setParameter("idDeposits", idDeposits).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}
