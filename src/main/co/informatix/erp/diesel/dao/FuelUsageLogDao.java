package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.diesel.entities.FuelUsageLog;

/**
 * DAO class that establishes the connection between business logic and
 * database. FuelUsageLogAction used for managing FuelUsageLog.
 * 
 * @author Luna.Granados
 */
@SuppressWarnings("serial")
@Stateless
public class FuelUsageLogDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a Fuel Usage in the database.
	 * 
	 * @param FuelUsage
	 *            : Save FuelUsage.
	 * @throws Exception
	 */
	public void saveFuelUsage(FuelUsageLog FuelUsage) throws Exception {
		em.persist(FuelUsage);
	}

	/**
	 * Consult a Fuel Usage by id.
	 * 
	 * @param id
	 *            : Identifier of Fuel Usage
	 * @return FuelUsage: Object found
	 * @throws Exception
	 */
	public FuelUsageLog FuelUsageById(int id) throws Exception {
		return em.find(FuelUsageLog.class, id);
	}

	/**
	 * Consult the list of FuelUsage in the database.
	 * 
	 * @return List<FuelUsageLog>: list of all FuelUsage.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelUsageLog> consultFuelUsage() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT fu FROM FuelUsageLog fu ");
		Query q = em.createQuery(query.toString());
		return q.getResultList();
	}

	/**
	 * Consult the last record of FuelUsage in the database.
	 * 
	 * @return FuelUsageLog: object found
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public FuelUsageLog consultLastFuelUsage() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT fu FROM FuelUsageLog fu ");
		query.append("WHERE fu.idFuelUsage = ");
		query.append("(SELECT MAX(fu.idFuelUsage) FROM FuelUsageLog fu)");
		Query q = em.createQuery(query.toString());
		List<FuelUsageLog> result = q.getResultList();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Consult the list of fuel usage logs that comply with the option of force.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<FuelUsageLog>:List of fuel usage logs that comply with the
	 *         condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelUsageLog> consultFuelUsageLog(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ful FROM FuelUsageLog ful ");
		query.append("LEFT JOIN FETCH ful.transactionType tp ");
		query.append(consult);
		query.append("ORDER BY ful.date DESC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of rows that exist in the database that are existing
	 * or not existing.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long quantityFuelUsageLog(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ful) FROM FuelUsageLog ful ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Returns the number rows of engine log that exist in the database that are
	 * existing or not existing.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long quantityEngineLog(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ful) FROM FuelUsageLog ful ");
		query.append("JOIN ful.engineLog el ");
		query.append("WHERE el.idEngineLog IS NOT NULL ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the list of engine log that comply with the option of force.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<FuelUsageLog>:List of engine log that comply with the
	 *         condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelUsageLog> consultEngineLog(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ful FROM FuelUsageLog ful ");
		query.append("JOIN FETCH ful.engineLog el ");
		query.append("LEFT JOIN FETCH el.deliveredBy db ");
		query.append("LEFT JOIN FETCH el.receivedBy rb ");
		query.append("LEFT JOIN FETCH el.activityMachine am ");
		query.append("LEFT JOIN FETCH am.activityMachinePK ampk ");
		query.append("LEFT JOIN FETCH ampk.machines m ");
		query.append("LEFT JOIN FETCH ampk.activities a ");
		query.append("WHERE el.idEngineLog IS NOT NULL ");
		query.append(consult);
		query.append("ORDER BY el.date DESC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

}