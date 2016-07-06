package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.ActivityMaterials;

/**
 * DAO class that establishes the connection between business logic and data
 * base information management activities and materials.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Stateless
public class ActivityMaterialsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an ActivityMaterial in database
	 * 
	 * @param activityMaterials
	 *            : activityMaterials to save.
	 * @throws Exception
	 */
	public void saveActivityMaterials(ActivityMaterials activityMaterials)
			throws Exception {
		em.persist(activityMaterials);
	}

	/**
	 * Edit the relation of the activity with a materials in the database
	 * 
	 * @param activityMaterials
	 *            : Relation between activity and materials to edit.
	 * @throws Exception
	 */
	public void editActivityMaterials(ActivityMaterials activityMaterials)
			throws Exception {
		em.merge(activityMaterials);
	}

	/**
	 * Removes the relation between the activity and the materials in the
	 * database.
	 * 
	 * @param activityMaterials
	 *            : Relation between activity and materials to delete.
	 * @throws Exception
	 */
	public void deleteActivityMaterials(ActivityMaterials activityMaterials)
			throws Exception {
		em.remove(em.merge(activityMaterials));
	}

	/**
	 * Returns the number of the relations between human resources and
	 * activities existing in the database they are filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query why seep
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: number of activity records found
	 * @throws Exception
	 */
	public Long amountMaterialsByActivity(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(am) FROM ActivityMaterials am ");
		queryBuilder.append("JOIN am.activityMaterialsPK.materials m ");
		queryBuilder.append("JOIN am.activityMaterialsPK.activities ac ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * This method queries the relationship between activities and human
	 * resources sending a certain range as a parameter and filtering
	 * information search for the values sent.
	 * 
	 * @param start
	 *            : The first record that is retrieved from the result.
	 * @param range
	 *            : Range of the records to be retrieved.
	 * @param query
	 *            : A query with record filters depending on the parameters
	 *            selected by the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<ActivitiesAndHr>: List of human resources and activities.
	 *         associations found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMaterials> queryMaterialsByActivity(int start,
			int range, StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT am FROM ActivityMaterials am ");
		queryBuilder.append("JOIN FETCH am.activityMaterialsPK.materials m ");
		queryBuilder.append("JOIN FETCH am.activityMaterialsPK.activities ac ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER by m.name ");

		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<ActivityMaterials> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult the sum of the total cost of each of the materials activity
	 * 
	 * @param idActivity
	 *            : Activity identifier.
	 * @return Double: Sum of materials cost
	 * @throws Exception
	 */
	public Double calculateTotalCostMaterials(int idActivity) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(am.costActual) FROM  ActivityMaterials am ");
		query.append("JOIN am.activityMaterialsPK.activities a ");
		query.append("WHERE a.idActivity = :idActivity  ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		return (Double) q.getSingleResult();
	}
}
