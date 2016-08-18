package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.CycleStandardActivities;

/**
 * DAO class that establishes the connection between business logic and
 * database. CycleStandardActivitiesAction manages CycleStandardActivities.
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
	 * Save a standard cycle activities in the database.
	 * 
	 * @param cycleStandardActivities
	 *            : Standard cycle activities to save.
	 * @throws Exception
	 */
	public void saveCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) throws Exception {
		em.persist(cycleStandardActivities);
	}

	/**
	 * Delete a standard cycle activities in the database.
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
	 * Edit a standard cycle activities in the database.
	 * 
	 * @param cycleStandardActivities
	 *            : Standard editing cycle activities
	 * @throws Exception
	 */
	public void editCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) throws Exception {
		em.merge(cycleStandardActivities);
	}

	/**
	 * This method query standard cycle activities that are related to crop
	 * name.
	 * 
	 * @modify 24/07/2015 Andres.Gomez
	 * 
	 * @param idCropName
	 *            : CropName identifier.
	 * @return List<CycleStandardActivities>:List of cycle standard activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CycleStandardActivities> queryCycleStandardActivities(
			int idCropName) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT csa FROM CycleStandardActivities csa ");
		query.append("JOIN FETCH csa.cropNames cn ");
		query.append("JOIN FETCH csa.activityNames an ");
		query.append("WHERE cn.idCropName=:idCropName ");
		query.append("ORDER BY csa.idCycleActivity ");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("idCropName", idCropName);
		return queryResult.getResultList();
	}

	/**
	 * This method see also the cycle standard activities and its relations with
	 * Crop name and Activity name that already exist in the data base.
	 * 
	 * @param idCropName
	 *            : CropName identifier.
	 * @param idActivityName
	 *            : Identifier of the activity name.
	 * @return boolean: Returns true if the relationship exists, false
	 *         otherwise.
	 * @throws Exception
	 */
	public boolean relateCropNamesActivityNames(int idCropName,
			int idActivityName) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT csa FROM CycleStandardActivities csa ");
		queryBuilder.append("JOIN FETCH csa.cropNames cn ");
		queryBuilder.append("JOIN FETCH csa.activityNames an ");
		queryBuilder.append("WHERE cn.idCropName=:idCropName ");
		queryBuilder.append("AND an.idActivityName=:idActivityName ");
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("idCropName", idCropName).setParameter(
				"idActivityName", idActivityName);
		if (query.getResultList().size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the amount of cycle standard - activities
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param consult
	 *            : String containing the query why the filter assignments.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: quantity of activities for the cycle
	 * @throws Exception
	 */
	public Long quantityCycleStandard(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(csa) FROM CycleStandardActivities csa ");
		query.append("JOIN csa.cropNames cn ");
		query.append("JOIN csa.activityNames an ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consulting CycleStandardActivities with a certain range sent
	 * as a parameter and filtering the information by the values of search
	 * sent.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param start
	 *            : where it initiates the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<CycleStandardActivities> : list of cycle standard activities
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CycleStandardActivities> queryCycleStandarActivities(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT csa FROM CycleStandardActivities csa ");
		query.append("JOIN FETCH csa.cropNames cn ");
		query.append("JOIN FETCH csa.activityNames an ");
		query.append(consult);
		query.append("ORDER BY csa.id");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<CycleStandardActivities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}