package co.informatix.erp.diesel.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.diesel.entities.EngineLog;

/**
 * DAO class that establishes the connection between business logic and data
 * base for engine_log.
 * 
 * @author Patricia.Patinio
 */
@SuppressWarnings("serial")
@Stateless
public class EngineLogDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an engineLog register in the database.
	 * 
	 * @param engineLog
	 *            : engineLog object to save.
	 * @throws Exception
	 */
	public void saveEngineLog(EngineLog engineLog) throws Exception {
		em.persist(engineLog);
	}

	/**
	 * Edit an engineLog register in database.
	 * 
	 * @param engineLog
	 *            : engineLog object to edit.
	 * @throws Exception
	 */
	public void editEngineLog(EngineLog engineLog) throws Exception {
		em.merge(engineLog);
	}

	/**
	 * Remove an engineLog register of the database.
	 * 
	 * @param engineLog
	 *            : engineLog object to remove.
	 * @throws Exception
	 */
	public void deleteEngineLog(EngineLog engineLog) throws Exception {
		em.remove(em.merge(engineLog));
	}

}
