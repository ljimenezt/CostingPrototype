package co.informatix.erp.diesel.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

}
