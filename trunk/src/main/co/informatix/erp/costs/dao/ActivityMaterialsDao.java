package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.ActivityMaterials;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorFechas;

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
	 * This method queries the relationship between activities and materials
	 * sending a certain range as a parameter and filtering information search
	 * for the values sent.
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
	 * @return List<ActivityMaterials>: ActivityMaterials associations found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMaterials> queryMaterialsByActivity(int start,
			int range, StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT am FROM ActivityMaterials am ");
		queryBuilder.append("JOIN FETCH am.activityMaterialsPK.materials m ");
		queryBuilder.append("JOIN FETCH m.measurementUnits ");
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
	 * @modify 06/10/2016 Luna.Granados
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
		if (q.getSingleResult() != null) {
			return (Double) q.getSingleResult();
		}
		return (0.0);
	}

	/**
	 * This method allows consult the list of the activity materials according a
	 * identifier activity
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idActivity
	 *            identifier of the activity
	 * @return List<ActivityMaterials>: List of activity materials found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMaterials> consultMaterialsByActivity(int idActivity)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT am FROM ActivityMaterials am ");
		queryBuilder.append("JOIN FETCH am.activityMaterialsPK.materials m ");
		queryBuilder.append("JOIN FETCH m.measurementUnits ");
		queryBuilder.append("JOIN FETCH am.activityMaterialsPK.activities ac ");
		queryBuilder.append("WHERE ac.idActivity =:idActivity ");
		queryBuilder.append("ORDER by m.name ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idActivity", idActivity);
		List<ActivityMaterials> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Sum total budgeted amount of material all activities.
	 * 
	 * @param idMaterial
	 *            : Material identifier.
	 * @return Double: sum of the amount of materials budgeted.
	 * @throws Exception
	 */
	public Double calculateTotalQuantityBudgetByMaterial(int idMaterial)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(am.quantityBudget) FROM  ActivityMaterials am ");
		query.append("JOIN am.activityMaterialsPK.materials m ");
		query.append("WHERE m.idMaterial = :idMaterial  ");
		query.append("AND am.quantityActual IS NULL ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", idMaterial);
		return (Double) q.getSingleResult();
	}

	/**
	 * Consult the relation between activities and materials
	 * 
	 * @author Luna.Granados
	 * 
	 * @param idActivity
	 *            : Identifier of activity.
	 * @return List<ActivityMaterials>: List of relation between activities and
	 *         materials
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMaterials> listActivitiesAndMaterials(int idActivity)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT am FROM ActivityMaterials am ");
		query.append("JOIN FETCH am.activityMaterialsPK.materials m ");
		query.append("JOIN FETCH am.activityMaterialsPK.activities ac ");
		query.append("WHERE ac.idActivity =:idActivity ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		List<ActivityMaterials> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult the list of the activity materials in
	 * transaction withdraw
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<ActivityMaterials>: List of activity materials found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMaterials> consultActivityMaterialInTrasaction()
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT am FROM ActivityMaterials am  ");
		query.append("JOIN FETCH am.activityMaterialsPK.materials m ");
		query.append("JOIN FETCH am.activityMaterialsPK.activities a ");
		query.append("JOIN FETCH m.measurementUnits ");
		query.append("JOIN FETCH a.activityName ");
		query.append("WHERE m.idMaterial IN (SELECT ma.idMaterial FROM Transactions t  ");
		query.append("		JOIN t.deposits d ");
		query.append("		JOIN d.materials ma ");
		query.append("		JOIN t.activities ac ");
		query.append("		WHERE a.idActivity = ac.idActivity  ");
		query.append("		AND t.transactionType.idTransactionType = :idTransaction ");
		query.append("		AND TO_CHAR(t.dateTime,'YYYY-mm-dd') = :dateTime )");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("idTransaction",
				Constantes.TRANSACTION_TYPE_WITHDRAWAL).setParameter(
				"dateTime",
				ControladorFechas
						.getFechaActual(Constantes.DATE_FORMAT_CONSULT));
		List<ActivityMaterials> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}