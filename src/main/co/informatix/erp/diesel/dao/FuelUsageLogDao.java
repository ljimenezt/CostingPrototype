package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
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

}