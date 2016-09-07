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
import co.informatix.erp.costs.entities.ActivitiesAndHr;
import co.informatix.erp.costs.entities.ActivitiesAndHrPK;
import co.informatix.erp.lifeCycle.entities.Cycle;
import co.informatix.erp.utils.ControladorFechas;

/**
 * DAO class that establishes the connection between business logic and database
 * for activities and human resources.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class ActivitiesAndHrDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save the relation between the activity and the human resources in the
	 * database.
	 * 
	 * @param activitiesAndHr
	 *            : Human resources and activity association to save.
	 * @throws Exception
	 */
	public void saveActivitiesAndHr(ActivitiesAndHr activitiesAndHr)
			throws Exception {
		em.persist(activitiesAndHr);
	}

	/**
	 * Edit the relation of the activity with a human resource in the database
	 * 
	 * @param activitiesAndHr
	 *            : Relation between the activity and human resource to edit.
	 * @throws Exception
	 */
	public void editActivitiesAndHr(ActivitiesAndHr activitiesAndHr)
			throws Exception {
		em.merge(activitiesAndHr);
	}

	/**
	 * Removes the relation between the activity and the human resources in the
	 * database.
	 * 
	 * @param activitiesAndHr
	 *            : Relation between activity and human resources to delete.
	 * @throws Exception
	 */
	public void deleteActivitiesAndHr(ActivitiesAndHr activitiesAndHr)
			throws Exception {
		em.remove(em.merge(activitiesAndHr));
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
	public Long amountActivitiesAndHr(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(ah) FROM ActivitiesAndHr ah ");
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
	public List<ActivitiesAndHr> queryActivitiesAndHr(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ah FROM ActivitiesAndHr ah ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<ActivitiesAndHr> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method calculates the amount of overtime hours in a range of dates a
	 * human resource sent as a parameter.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param idHr
	 *            : Id human resource that you want to query overtime hours.
	 * @param startDate
	 *            : Start point of the range of dates that you want to query.
	 * @param endDate
	 *            : End point of the date range that you want to query.
	 * @param idActivity
	 *            : Identifier of the activity.
	 * @return Double: Total overtime hours.
	 * @throws Exception
	 */
	public Double calculateOverTimeHours(int idHr, Date startDate,
			Date endDate, int idActivity) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(ah.overtimeHours) FROM ActivitiesAndHr ah ");
		query.append("WHERE ah.activitiesAndHrPK.hr.idHr = :idHr ");
		query.append("AND (ah.initialDateTimeActual > :startDate ");
		query.append("OR (ah.initialDateTimeActual is NULL AND"
				+ " ah.initialDateTimeBudget > :startDate) )");
		query.append("AND (ah.finalDateTimeActual < :endDate ");
		query.append("OR (ah.finalDateTimeActual is NULL AND"
				+ " ah.finalDateTimeBudget < :endDate) ) ");
		query.append("AND ah.activitiesAndHrPK.activities.idActivity <> :idActivity");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("idHr", idHr);
		queryResult.setParameter("startDate", startDate);
		queryResult.setParameter("endDate", endDate);
		queryResult.setParameter("idActivity", idActivity);
		if (queryResult.getSingleResult() != null) {
			return (Double) queryResult.getSingleResult();
		}
		return (0.0);
	}

	/**
	 * This method sums the average hours per day for a human resource.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param idHr
	 *            : Human resource id that you will use to calculate Normal
	 *            hours.
	 * @param activityDate
	 *            : Days that you will use in the computation.
	 * @param idActivity
	 *            : Identifier of the activity.
	 * @return Double: Total normal hours.
	 * @throws Exception
	 */
	public Double calculateNormalHours(int idHr, Date activityDate,
			int idActivity) throws Exception {
		Date minDateTime = ControladorFechas.inicioDeDia(activityDate);
		Date maxDateTime = ControladorFechas.finDeDia(activityDate);
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(ah.normalHours) FROM ActivitiesAndHr ah ");
		query.append("WHERE ah.activitiesAndHrPK.hr.idHr = :idHr ");
		query.append("AND (ah.initialDateTimeActual > :minDateTime ");
		query.append("OR (ah.initialDateTimeActual is NULL AND"
				+ " ah.initialDateTimeBudget > :minDateTime) ) ");
		query.append("AND ( (ah.finalDateTimeBudget < :maxDateTime ");
		query.append("AND (ah.finalDateTimeActual is NULL)) OR ");
		query.append(" (ah.finalDateTimeActual < :maxDateTime) ) ");
		query.append("AND ah.activitiesAndHrPK.activities.idActivity <> :idActivity");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("idHr", idHr);
		queryResult.setParameter("minDateTime", minDateTime);
		queryResult.setParameter("maxDateTime", maxDateTime);
		queryResult.setParameter("idActivity", idActivity);
		if (queryResult.getSingleResult() != null) {
			return (Double) queryResult.getSingleResult();
		}
		return (0.0);
	}

	/**
	 * Compute the sum of the total costs for every human resources of an
	 * activity.
	 * 
	 * @param idActivity
	 *            : Activity identifier.
	 * @return Double: Sum of the costs.
	 * @throws Exception
	 */
	public Double totalCost(int idActivity) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(ahr.totalCostActual) FROM  ActivitiesAndHr ahr ");
		query.append("JOIN ahr.activitiesAndHrPK.activities a ");
		query.append("WHERE a.idActivity = :idActivity  ");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("idActivity", idActivity);
		return (Double) queryResult.getSingleResult();
	}

	/**
	 * Consult the relations between activities and human resources for one
	 * human resources and one activity
	 * 
	 * @param idHr
	 *            : Human resources identifier
	 * @param activities
	 *            : Activity selected
	 * @return List<ActivitiesAndHr>: Activities and hr list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivitiesAndHr> hrOccupied(int idHr, Activities activities)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ahr FROM  ActivitiesAndHr ahr ");
		query.append("JOIN FETCH ahr.activitiesAndHrPK.hr hr ");
		query.append("JOIN FETCH ahr.activitiesAndHrPK.activities a ");
		query.append("JOIN FETCH a.activityName an ");
		query.append("WHERE hr.idHr = :idHr  ");
		query.append("AND (ahr.initialDateTimeBudget BETWEEN :initialDate AND :finalDate ");
		query.append("OR ahr.finalDateTimeBudget BETWEEN :initialDate AND :finalDate) ");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("idHr", idHr);
		queryResult
				.setParameter("initialDate", activities.getInitialDtBudget());
		queryResult.setParameter("finalDate", activities.getFinalDtBudget());
		List<ActivitiesAndHr> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method searches the relations between the human resource and the
	 * activity by Id.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param activitiesAndHrPK
	 *            : Primary key of the ActivitiesAndHr entity.
	 * @return ActivitiesAndHr object type or null if not found.
	 * @throws Exception
	 */
	public ActivitiesAndHr activitiesAndHrById(
			ActivitiesAndHrPK activitiesAndHrPK) throws Exception {
		return em.find(ActivitiesAndHr.class, activitiesAndHrPK);
	}

	/**
	 * Consult all the relations between activities and human resources for
	 * cycle, activity and date.
	 * 
	 * @param cycle
	 *            : Cycle of crop.
	 * @param finalDate
	 *            : Final Date.
	 * @return boolean: Its 'true' if it find data
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean activitiesAndHrByDate(Cycle cycle, Date finalDate)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ahr FROM  ActivitiesAndHr ahr ");
		query.append("JOIN FETCH ahr.activitiesAndHrPK.activities a ");
		query.append("WHERE a.cycle.idCycle = :idCycle ");
		query.append("AND ahr.initialDateTimeBudget NOT BETWEEN :initialDate and :finalDate ");
		query.append("AND ahr.finalDateTimeBudget NOT BETWEEN :initialDate and :finalDate ");
		Query q = em.createQuery(query.toString());
		q.setParameter("initialDate", cycle.getInitialDateTime());
		q.setParameter("finalDate", finalDate);
		q.setParameter("idCycle", cycle.getIdCycle());
		List<ActivitiesAndHr> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Consult the relation between activities and human resources for cycle
	 * 
	 * @param idActivity
	 *            : Identifier of activity.
	 * @return List<ActivitiesAndHr> : List of relation between activities and
	 *         human resources
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivitiesAndHr> activitiesAndHrByCycle(int idActivity)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ahr FROM  ActivitiesAndHr ahr ");
		query.append("JOIN FETCH ahr.activitiesAndHrPK.hr hr ");
		query.append("JOIN FETCH ahr.activitiesAndHrPK.activities a ");
		query.append("WHERE a.idActivity = :idActivity ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		List<ActivitiesAndHr> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}