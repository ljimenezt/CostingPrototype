package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.DetailsHarvest;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the harvest details.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class DetailsHarvestDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an DetailsHarvest in database
	 * 
	 * @param detailsHarvest
	 *            : detailsHarvest to save.
	 * @throws Exception
	 */
	public void saveDetailsHarvest(DetailsHarvest detailsHarvest)
			throws Exception {
		em.persist(detailsHarvest);
	}

	/**
	 * Edit an detailsHarvest in database
	 * 
	 * @param detailsHarvest
	 *            : detailsHarvest to edit.
	 * @throws Exception
	 */
	public void editDetailsHarvest(DetailsHarvest detailsHarvest)
			throws Exception {
		em.merge(detailsHarvest);
	}

	/**
	 * Consult a specific detail harvest by passing the id of the activity as a
	 * parameter.
	 * 
	 * @param idActivityName
	 *            : identifier of the activity name to search.
	 * @param idCycle
	 *            :identifier of the cycle of the activity is executed.
	 * @return Double: value of the previous details harvest object consulted.
	 * @throws Exception
	 */
	public Double consultPreviousSacksHarvestActivity(int idActivityName,
			int idCycle) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT dh.totalSacks FROM DetailsHarvest dh ");
		query.append("JOIN dh.activities a ");
		query.append("JOIN a.activityName an ");
		query.append("JOIN a.cycle c ");
		query.append("WHERE an.idActivityName =:idActivityName ");
		query.append("AND c.idCycle =:idCycle ");
		query.append("AND a.initialDtBudget = (SELECT MAX(a1.initialDtBudget) ");
		query.append("	FROM DetailsHarvest dh1 JOIN dh1.activities a1 ");
		query.append("	JOIN a1.activityName an1 ");
		query.append("	WHERE an1.idActivityName =:idActivityName ");
		query.append("	AND a1.cycle.idCycle =:idCycle ");
		query.append("	AND an1.harvest = true ) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivityName", idActivityName).setParameter(
				"idCycle", idCycle);
		if (q.getSingleResult() != null) {
			return (Double) q.getSingleResult();
		}
		return (0.0);
	}

	/**
	 * This method allows consult the details harvest object according a
	 * identifier of the activity
	 * 
	 * @param idActivity
	 *            : identifier of the activity to search.
	 * @return DetailsHarvest : Value of the object details harvest consulted.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DetailsHarvest detailsHarvestXActivity(int idActivity)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT dh FROM DetailsHarvest dh ");
		query.append("JOIN dh.activities a ");
		query.append("WHERE a.idActivity =:idActivity ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		List<DetailsHarvest> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
