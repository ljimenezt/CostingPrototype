package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.CycleStandardActivities;

/**
 * DAO class that establishes the connection between business logic and base
 * data. CycleStandardActivitiesAction used for managing CycleStandardActivities
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class CycleStandardActivitiesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save the standard cycle activities in the database
	 * 
	 * @param cycleStandardActivities
	 *            : Standard cycle activities to save
	 * @throws Exception
	 */
	public void guardarCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) throws Exception {
		em.persist(cycleStandardActivities);
	}

	/**
	 * Delete the standar cycle activities in the database
	 * 
	 * @param cycleStandardActivities
	 *            : Standard cycle activities to delete
	 * @throws Exception
	 */
	public void deleteCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) throws Exception {
		em.remove(em.merge(cycleStandardActivities));
	}

	/**
	 * Edit the standard cycle activities in the database
	 * 
	 * @param cycleStandardActivities
	 *            : Standard editing cycle activities
	 * @throws Exception
	 */
	public void editarCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) throws Exception {
		em.merge(cycleStandardActivities);
	}

	/**
	 * This method query standard cycle activities there related crop name.
	 * 
	 * @modify 24/07/2015 Andres.Gomez
	 * 
	 * @param idCropName
	 *            : identifier cropName
	 * 
	 * @return List<CycleStandardActivities>:List of activities standard cycle
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CycleStandardActivities> consultarCycleStandardActivities(
			int idCropName) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT csa FROM CycleStandardActivities csa ");
		query.append("JOIN FETCH csa.cropNames cn ");
		query.append("JOIN FETCH csa.activityNames an ");
		query.append("WHERE cn.idCropName=:idCropName ");
		query.append("ORDER BY csa.idCycleActivity ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCropName", idCropName);
		return q.getResultList();
	}

	/**
	 * This method see also the standard cycle activities whose relationship
	 * Harvest and name Activity name already exist on the basis of data
	 * 
	 * @param idCropName
	 *            : identifier cropName
	 * @param idActivityName
	 *            : Identifier name of the activity
	 * @return boolean: Returns true if the relationship exists otherwise false
	 * 
	 * @throws Exception
	 */
	public boolean relacionCropNamesActivityNames(int idCropName,
			int idActivityName) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT csa FROM CycleStandardActivities csa ");
		query.append("JOIN FETCH csa.cropNames cn ");
		query.append("JOIN FETCH csa.activityNames an ");
		query.append("WHERE cn.idCropName=:idCropName ");
		query.append("AND an.idActivityName=:idActivityName ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCropName", idCropName).setParameter("idActivityName",
				idActivityName);
		if (q.getResultList().size() > 0) {
			return true;
		}
		return false;

	}

}
