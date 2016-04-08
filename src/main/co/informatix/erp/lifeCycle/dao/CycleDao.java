package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Cycle;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the Cycle entity.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class CycleDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an cycle in database
	 * 
	 * @param cycle
	 *            : cycle to save.
	 * @throws Exception
	 */
	public void saveCycle(Cycle cycle) throws Exception {
		em.persist(cycle);
	}

	/**
	 * Edit an cycle in database
	 * 
	 * @param cycle
	 *            : cycle to edit.
	 * @throws Exception
	 */
	public void editCycle(Cycle cycle) throws Exception {
		em.merge(cycle);
	}

	/**
	 * Delete the cycles of the database.
	 * 
	 * @param cycle
	 *            : cycle to remove.
	 * @throws Exception
	 */
	public void deleteCycle(Cycle cycle) throws Exception {
		em.remove(em.merge(cycle));
	}

	/**
	 * This method consultation cycles that are associated with a crop.
	 * 
	 * @param start
	 *            : where it initiates the consultation record.
	 * @param range
	 *            : range of the records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @param idCrop
	 *            : crop identifier.
	 * @return List<Cycle>: List of cycles.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Cycle> consultCycleByCrop(int start, int range,
			StringBuilder consult, List<SelectItem> parameters, int idCrop)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM  Cycle c ");
		query.append("JOIN FETCH c.activiyNames an ");
		query.append("JOIN c.crops cr ");
		query.append("WHERE cr.idCrop =:idCrop ");
		query.append(consult);
		query.append("ORDER by an.activityName ASC, c.initialDateTime ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Cycle> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing cycles in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the leak.
	 * @param parameters
	 *            :query parameters.
	 * @param idCrop
	 *            :integer of the crop associated a one cycle.
	 * @return Long: number of cycles records found.
	 * @throws Exception
	 */
	public Long amountCycleCrop(StringBuilder consult,
			List<SelectItem> parameters, int idCrop) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(c) FROM Cycle c ");
		query.append("JOIN c.activiyNames an ");
		query.append("JOIN c.crops cr ");
		query.append("WHERE cr.idCrop =:idCrop ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}
}
