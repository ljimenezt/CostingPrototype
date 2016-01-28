package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.ActivitiesAndHr;
import co.informatix.erp.costs.entities.ActivitiesAndHrPK;
import co.informatix.erp.utils.ControladorFechas;

/**
 * DAO class that establishes the connection between business logic and data
 * base information management activities and human resources.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class ActivitiesAndHrDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save the relationship between the activity and the human resources in the
	 * database
	 * 
	 * @param activitiesAndHr
	 *            : human resources and activity association to save.
	 * @throws Exception
	 */
	public void guardarActivitiesAndHr(ActivitiesAndHr activitiesAndHr)
			throws Exception {
		em.persist(activitiesAndHr);
	}

	/**
	 * Edit the relationship of the activity with a human resource in the
	 * database
	 * 
	 * @param activitiesAndHr
	 *            : relationship between the activity and human resource to
	 *            edit.
	 * @throws Exception
	 */
	public void editarActivitiesAndHr(ActivitiesAndHr activitiesAndHr)
			throws Exception {
		em.merge(activitiesAndHr);
	}

	/**
	 * Removes the relationship between the activity and the human resources in
	 * the database
	 * 
	 * @param activitiesAndHr
	 *            : relationship between activity and human resources to
	 *            eliminate
	 * @throws Exception
	 */
	public void eliminarActivitiesAndHr(ActivitiesAndHr activitiesAndHr)
			throws Exception {
		em.remove(em.merge(activitiesAndHr));
	}

	/**
	 * Returns the number of the relationship between human resources and
	 * activities existing in the database by filtering the information by the
	 * values of search sent.
	 * 
	 * 
	 * @param consulta
	 *            : String containing the query why seep
	 * @param parametros
	 *            : query parameters.
	 * @return Long: number of activity records found
	 * @throws Exception
	 */
	public Long cantidadActivitiesAndHr(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ah) FROM ActivitiesAndHr ah ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the relationship between activities and human
	 * resources sending a certain range as a parameter and filtering
	 * information search for the values sent.
	 * 
	 * @param inicio
	 *            : where it started the consultation record
	 * @param rango
	 *            : range of records
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : query parameters.
	 * @return List<ActivitiesAndHr>: List of human resources and activities
	 *         associations found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivitiesAndHr> consultarActivitiesAndHr(int inicio,
			int rango, StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ah FROM ActivitiesAndHr ah ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<ActivitiesAndHr> resultList = q.getResultList();
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
	 *            : Id human resource that you want to consult overtime hours
	 * @param startDate
	 *            : Start of the on the range of dates you want to do the
	 *            consultation
	 * @param endDate
	 *            : End of the date range in which you want to make the
	 *            consultation.
	 * @param idActivity
	 *            : identifier of the activity.
	 * 
	 * @return Double: total overtime hours.
	 * @throws Exception
	 */
	public Double calcularOverTimeHours(int idHr, Date startDate, Date endDate,
			int idActivity) throws Exception {
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
		Query q = em.createQuery(query.toString());
		q.setParameter("idHr", idHr);
		q.setParameter("startDate", startDate);
		q.setParameter("endDate", endDate);
		q.setParameter("idActivity", idActivity);
		if (q.getSingleResult() != null) {
			return (Double) q.getSingleResult();
		}
		return (0.0);
	}

	/**
	 * This method calculates the sum of the average hours in a day for a
	 * resource human, sent as a parameter.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param idHr
	 *            : Id human resource that you want to consult Normal hours
	 * @param activityDate
	 *            : Day where you want to consult
	 * @param idActivity
	 *            : identifier of the activity.
	 * 
	 * @return Double: Total normal hours.
	 * @throws Exception
	 */
	public Double calcularNormalHours(int idHr, Date activityDate,
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
		Query q = em.createQuery(query.toString());
		q.setParameter("idHr", idHr);
		q.setParameter("minDateTime", minDateTime);
		q.setParameter("maxDateTime", maxDateTime);
		q.setParameter("idActivity", idActivity);
		if (q.getSingleResult() != null) {
			return (Double) q.getSingleResult();
		}
		return (0.0);
	}

	/**
	 * Consult the sum of the total cost of each of the human resources activity
	 * 
	 * @param idActivity
	 *            : Activity identifier.
	 * @return Double: Sum of costs
	 * @throws Exception
	 */
	public Double totalCost(int idActivity) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(ahr.totalCostActual) FROM  ActivitiesAndHr ahr ");
		query.append("JOIN ahr.activitiesAndHrPK.activities a ");
		query.append("WHERE a.idActivity = :idActivity  ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		return (Double) q.getSingleResult();
	}

	/**
	 * This method searches the relationship between the human resource and the
	 * activity by Id.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param activitiesAndHrPK
	 *            : primary key of the ActivitiesAndHr entity.
	 * 
	 * @return ActivitiesAndHr object type or null if not found.
	 * @throws Exception
	 */
	public ActivitiesAndHr activitiesAndHrXId(
			ActivitiesAndHrPK activitiesAndHrPK) throws Exception {
		return em.find(ActivitiesAndHr.class, activitiesAndHrPK);
	}
}
