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
	 * Consult the last fuel usage log.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @return FuelUsageLog: object whit the last fuel usage log found
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public FuelUsageLog consultFuelUsageLogHigh() throws Exception {
		Query q = em.createQuery("SELECT MAX(fu.id) FROM FuelUsageLog fu ");
		List<FuelUsageLog> listaFuelUsageLog = q.getResultList();
		if (listaFuelUsageLog.size() > 0) {
			return listaFuelUsageLog.get(0);
		} else {
			return null;
		}
	}

}
