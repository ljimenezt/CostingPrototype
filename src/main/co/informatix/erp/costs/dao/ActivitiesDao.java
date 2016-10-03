package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.entities.Cycle;

/**
 * DAO class that establishes the connection between business logic and data
 * base for activities.
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ActivitiesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an activity in the database.
	 * 
	 * @param activities
	 *            : Activity to save.
	 * @throws Exception
	 */
	public void saveActivities(Activities activities) throws Exception {
		em.persist(activities);
	}

	/**
	 * Edit an activity in database.
	 * 
	 * @param activities
	 *            : Activity to edit.
	 * @throws Exception
	 */
	public void editActivities(Activities activities) throws Exception {
		em.merge(activities);
	}

	/**
	 * Remove an activity.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param activities
	 *            : Activity to remove.
	 * @throws Exception
	 */
	public void deleteActivities(Activities activities) throws Exception {
		em.remove(em.merge(activities));
	}

	/**
	 * Returns the number of existing activities in the database, and it filters
	 * the records with search values.
	 * 
	 * @author Andres.Gomez
	 * @modify 04/05/2016 Gerardo.Herrera
	 * @modify 12/07/2016 Wilhelm.Boada
	 * 
	 * @param query
	 *            : String containing the query why the leak.
	 * @param parameters
	 *            : Query parameters.
	 * @param stringCycle
	 *            : This parameter identifies whether the query needs the cycle.
	 * @return Long: Number of activity records found.
	 * @throws Exception
	 */
	public Long amountActivities(StringBuilder query,
			List<SelectItem> parameters, StringBuilder stringCycle)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(a) FROM Activities a ");
		queryBuilder.append("JOIN a.activityName an ");
		queryBuilder.append("JOIN a.crop c ");
		queryBuilder.append(stringCycle);
		queryBuilder.append(query);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method query activities with a certain range sent as a parameter and
	 * it filters the records with search values.
	 * 
	 * @author Andres.Gomez
	 * @modify 04/05/2016 Gerardo.Herrera
	 * 
	 * @param start
	 *            : The first record that is retrieved from the result.
	 * @param range
	 *            : Range of the records to be retrieved.
	 * @param query
	 *            : Record filters depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @param stringCycle
	 *            : This parameter identifies whether the query needs the cycle.
	 * @return List<Activities>: List of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> queryActivities(int start, int range,
			StringBuilder query, List<SelectItem> parameters,
			StringBuilder stringCycle) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT a FROM  Activities a ");
		queryBuilder.append("JOIN FETCH a.activityName an ");
		queryBuilder.append("JOIN a.crop c ");
		queryBuilder.append(stringCycle);
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parametro : parameters) {
			queryResult
					.setParameter(parametro.getLabel(), parametro.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<Activities> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Query an activity by its identifier.
	 * 
	 * @author Andres.Gomez
	 * @modify 22/06/2016 Sergio.Gelves
	 * 
	 * @param idActivity
	 *            : identifier of the activity.
	 * @return Activities: Activity found according to the identifier sent as a
	 *         parameter.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Activities activityById(int idActivity) throws Exception {
		List<Activities> results = em
				.createQuery(
						"SELECT a FROM Activities a JOIN FETCH a.activityName"
								+ " an WHERE a.idActivity=:idActivity")
				.setParameter("idActivity", idActivity).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method queries activities that are related to certifications; it
	 * filters the information with search values and retrieves an amount
	 * according to the specified range.
	 * 
	 * @author Mabell.Boada
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @param start
	 *            : The first record that is retrieved from the result.
	 * @param range
	 *            : Range of the records to be retrieved.
	 * @param query
	 *            : Record filters depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Activities>: List of names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> queryActivityNamesByIdCert(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT a FROM Activities a ");
		queryBuilder.append("JOIN FETCH a.activityName an ");
		queryBuilder.append("JOIN FETCH a.crop c ");
		queryBuilder.append("JOIN FETCH c.cropNames cn ");
		queryBuilder
				.append("WHERE a IN (SELECT at FROM ActivitiesAndCertifications ac ");
		queryBuilder
				.append("JOIN ac.activitiesAndCertificationsPK.activities at ");
		queryBuilder
				.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles cr ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY a.idActivity ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<Activities> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing activities in the database; it filters the
	 * records with search values.
	 * 
	 * @author Mabell.Boada
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @param query
	 *            : String containing the query that filters activities.
	 * @param parameters
	 *            : Parameters of the query.
	 * @return Long: Number of activities records found.
	 * @throws Exception
	 */
	public Long queryActivitiesByIdCert(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(a) FROM Activities a ");
		queryBuilder.append("JOIN a.activityName an ");
		queryBuilder.append("JOIN a.crop c ");
		queryBuilder.append("JOIN c.cropNames cn ");
		queryBuilder
				.append("WHERE a IN (SELECT at FROM ActivitiesAndCertifications ac ");
		queryBuilder
				.append("JOIN ac.activitiesAndCertificationsPK.activities at ");
		queryBuilder
				.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles cr ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * Return the list of activities.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @return List<Activities>: List of all activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> queryAllActivities() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM Activities a ");
		query.append("JOIN FETCH a.activityName an ");
		query.append("ORDER BY an.activityName ASC");
		Query q = em.createQuery(query.toString());
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Query an activity that is related to an activity name.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param actNameId
	 *            : Identifier of the activity name.
	 * @return Activities: Activity found according to the identifier sent as a
	 *         parameter.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Activities activityByActNameId(int actNameId) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT a FROM Activities a ");
		queryBuilder.append("JOIN FETCH a.activityName an ");
		queryBuilder.append("WHERE an.idActivityName=:idActName ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idActName", actNameId);
		List<Activities> results = queryResult.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Look for the amount of activities that require at least one
	 * certification.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param activityId
	 *            : Activity identifier.
	 * @return List<Activities>: List of activities with at least one
	 *         certification.
	 * @throws Exception
	 */
	public Long queryCertifiedActivities(int activityId) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(a) FROM  Activities a ");
		queryBuilder.append("WHERE a IN ");
		queryBuilder.append("(SELECT aca FROM ActivitiesAndCertifications ac ");
		queryBuilder
				.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
		queryBuilder.append("WHERE aca.idActivity = :idActividad) ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idActividad", activityId);
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * This method returns the amount of activities associated to a crop.
	 * 
	 * @param query
	 *            : String containing the query that has filters according to
	 *            some parameters.
	 * @param parameters
	 *            : Query parameters.
	 * @param cropId
	 *            : Identifier of the crop associated to the activities that are
	 *            going to be searched.
	 * @return Long: Amount of activities records found.
	 * @throws Exception
	 */
	public Long amountActivities(StringBuilder query,
			List<SelectItem> parameters, int cropId) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(a) FROM Activities a ");
		queryBuilder.append("JOIN a.activityName an ");
		queryBuilder.append("JOIN a.crop c ");
		queryBuilder.append("WHERE c.idCrop =:idCrop ");
		queryBuilder.append("AND a.serviceRequired is true ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idCrop", cropId);
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * This method queries activities with a certain range and it filters the
	 * information with search values.
	 * 
	 * @param start
	 *            : The first record that is retrieved from the result.
	 * @param range
	 *            : Range of the records to be retrieved.
	 * @param query
	 *            : Record filters depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @param cropId
	 *            : Identifier of the crop associated to the activities that are
	 *            going to be searched.
	 * @return List<Activities>: List of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> queryActivities(int start, int range,
			StringBuilder query, List<SelectItem> parameters, int cropId)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT a FROM Activities a ");
		queryBuilder.append("JOIN FETCH a.activityName an ");
		queryBuilder.append("JOIN FETCH a.crop c ");
		queryBuilder.append("WHERE c.idCrop =:idCrop ");
		queryBuilder.append("AND a.serviceRequired is true ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY an.activityName ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idCrop", cropId);
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<Activities> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Query all activities and activity names which have a reference to a
	 * specified cycle, the result is filtered by quantity and ordered according
	 * to the newer initial date of budget.
	 * 
	 * @author Sergio.Gelves
	 * 
	 * @param start
	 *            : The initial record that is retrieved.
	 * @param range
	 *            : The range of records to be retrieved.
	 * @param cycleId
	 *            : Cycle identifier.
	 * @return A list with the activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> searchActivitiesCycle(int start, int range,
			int cycleId) throws Exception {
		StringBuilder q = new StringBuilder();
		q.append("SELECT a FROM Activities a JOIN FETCH a.activityName an ");
		q.append(" WHERE a.cycle.idCycle =:cycleId ");
		q.append("ORDER BY a.initialDtBudget DESC");
		Query queryResult = em.createQuery(q.toString());
		return queryResult.setParameter("cycleId", cycleId)
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Query the amount of activities which are associated to the specified
	 * cycle.
	 * 
	 * @author Sergio.Gelves
	 * 
	 * @param cycleId
	 *            : The cycle identifier.
	 * @return: Amount of cycles.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public long amountActivitiesCycle(int cycleId) throws Exception {
		Query q = em.createQuery("SELECT COUNT(a) FROM Activities a WHERE "
				+ " a.cycle.idCycle =:cycleId GROUP BY a.cycle.idCycle ");
		List<Long> result = q.setParameter("cycleId", cycleId).getResultList();
		if (result != null && result.size() > 0) {
			return result.get(0);
		}
		return (long) 0;
	}

	/**
	 * This method returns the activities associated to the cycle.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param cycleId
	 *            : Cycle identifier.
	 * @return Date: The last activity date for cycle
	 * @throws Exception
	 */
	public Date activitiesByCycle(int cycleId) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT MAX(a.initialDtBudget) FROM Activities a ");
		queryBuilder.append("JOIN a.cycle c ");
		queryBuilder.append("WHERE c.idCycle=:idCycle ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idCycle", cycleId);
		return (Date) queryResult.getSingleResult();
	}

	/**
	 * Consult all the activities out of range of the cycle
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param cycle
	 *            : Cycle of crop.
	 * @param finalDate
	 *            : Final date of cycle.
	 * @return List<Activities>: Activities list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> activitiesOutCycle(Cycle cycle, Date finalDate)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM  Activities a ");
		query.append("WHERE a.initialDtBudget NOT BETWEEN :initialDate and :finalDate ");
		query.append("AND a.finalDtBudget NOT BETWEEN :initialDate and :finalDate ");
		query.append("AND a.cycle.idCycle = :idCycle ");
		Query q = em.createQuery(query.toString());
		q.setParameter("initialDate", cycle.getInitialDateTime());
		q.setParameter("finalDate", finalDate);
		q.setParameter("idCycle", cycle.getIdCycle());
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult all the activities into the date range and by cycle.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param cycle
	 *            : Crop cycle
	 * @param finalDate
	 *            : Final date for query
	 * @return List<Activities>: Activities list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> activitiesByCycle(Cycle cycle, Date finalDate)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM  Activities a ");
		query.append("WHERE a.initialDtBudget BETWEEN :initialDate and :finalDate ");
		query.append("AND a.finalDtBudget BETWEEN :initialDate and :finalDate ");
		query.append("AND a.cycle.idCycle = :idCycle ");
		Query q = em.createQuery(query.toString());
		q.setParameter("initialDate", cycle.getInitialDateTime());
		q.setParameter("finalDate", finalDate);
		q.setParameter("idCycle", cycle.getIdCycle());
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing activities in the database, and it filters
	 * the records with search values.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param query
	 *            : String containing the query why the leak.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of activity records found.
	 * @throws Exception
	 */
	public Long amountActivitiesByActivityMaterials(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(a) FROM Activities a ");
		queryBuilder.append("JOIN a.activityName an ");
		queryBuilder.append("WHERE a.materialsRequired = true ");
		queryBuilder.append(query);
		queryBuilder.append("AND a.idActivity IN ");
		queryBuilder
				.append("(SELECT ac.idActivity FROM  ActivityMaterials am ");
		queryBuilder.append("JOIN am.activityMaterialsPK.activities ac ");
		queryBuilder.append("WHERE am.quantityBudget >0 ");
		queryBuilder.append(") ");
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method query activities with a certain range sent as a parameter and
	 * it filters the records with search values.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param start
	 *            : The first record that is retrieved from the result.
	 * @param range
	 *            : Range of the records to be retrieved.
	 * @param query
	 *            : Record filters depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Activities>: List of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> queryActivitiesByActivityMaterials(int start,
			int range, StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT a FROM  Activities a ");
		queryBuilder.append("JOIN FETCH a.activityName an ");
		queryBuilder.append("WHERE a.materialsRequired = true ");
		queryBuilder.append(query);
		queryBuilder.append("AND a.idActivity IN ");
		queryBuilder
				.append("(SELECT ac.idActivity FROM  ActivityMaterials am ");
		queryBuilder.append("JOIN am.activityMaterialsPK.activities ac ");
		queryBuilder.append("WHERE am.quantityBudget >0 ");
		queryBuilder.append(") ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parametro : parameters) {
			queryResult
					.setParameter(parametro.getLabel(), parametro.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<Activities> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}