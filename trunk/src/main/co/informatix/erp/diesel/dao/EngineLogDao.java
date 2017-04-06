package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

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

	/**
	 * Consult all engineLog objects on database.
	 * 
	 * @return List<EngineLog>: return a engineLog list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EngineLog> allEngineLog() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT e FROM EngineLog e ");
		query.append("JOIN FETCH e.activityMachine.activityMachinePK.activities a ");
		query.append("JOIN FETCH e.activityMachine.activityMachinePK.machines m ");
		Query q = em.createQuery(query.toString());
		List<EngineLog> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}
