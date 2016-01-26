package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.Activities;

/**
 * DAO class that establishes the connection between business logic and base
 * data management activities.
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
	 * Save an activity in BD
	 * 
	 * @param activities
	 *            : activity to save.
	 * @throws Exception
	 */
	public void guardarActivities(Activities activities) throws Exception {
		em.persist(activities);
	}

	/**
	 * Edit an activity in BD
	 * 
	 * @param activities
	 *            : activity to edit.
	 * @throws Exception
	 */
	public void editarActivities(Activities activities) throws Exception {
		em.merge(activities);
	}

	/**
	 * Remove activity
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param activities
	 *            : activity to remove
	 * @throws Exception
	 */
	public void eliminarActivities(Activities activities) throws Exception {
		em.remove(em.merge(activities));
	}

	/**
	 * Returns the number of existing activities in the BD filtering information
	 * search by the values sent.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consulta
	 *            : String containing the query why the leak.
	 * @param parametros
	 *            :query parameters
	 * @return Long: number of activity records found
	 * @throws Exception
	 */
	public Long cantidadActivities(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(a) FROM Activities a ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation activities with a certain range sent as a
	 * parameter and filtering the information by the values of search sent.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param inicio
	 *            : where it initiates the consultation record
	 * @param rango
	 *            : range of the records
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : query parameters.
	 * @return List<Activities>: List of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> consultarActivities(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM  Activities a ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method consultation activities that are associated with a crop.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param start
	 *            : where it initiates the consultation record
	 * @param range
	 *            : range of the records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @param idCrop
	 *            : crop identifier.
	 * @return List<Activities>: List of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> consultarActivitiesXCrop(int start, int range,
			StringBuilder consult, List<SelectItem> parameters, int idCrop)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM  Activities a ");
		query.append("JOIN FETCH a.activityName an ");
		query.append("JOIN a.crop c ");
		query.append("WHERE c.idCrop =:idCrop ");
		query.append(consult);
		query.append("ORDER BY a.initialDtBudget DESC ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing activities in the BD filtering information
	 * search by the values sent.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consult
	 *            : String containing the query why the leak.
	 * @param parameters
	 *            :query parameters
	 * @param idCrop
	 *            :integer of the crop associated a one activity
	 * @return Long: number of activity records found
	 * @throws Exception
	 */
	public Long amountActivitiesCrop(StringBuilder consult,
			List<SelectItem> parameters, int idCrop) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(a) FROM Activities a ");
		query.append("JOIN a.activityName an ");
		query.append("JOIN a.crop c ");
		query.append("WHERE c.idCrop =:idCrop ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consultation activity by its identifier
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idActivity
	 *            : identifier of the activity
	 * 
	 * @return Activities: Activity found according to the identifier sent as a
	 *         parameter.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Activities obtenerActivity(int idActivity) throws Exception {
		List<Activities> results = em
				.createQuery(
						"SELECT a FROM Activities a WHERE a.idActivity=:idActivity")
				.setParameter("idActivity", idActivity).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method consultation names related activities certifications; with a
	 * certain range and sent as a parameter filtering the information by the
	 * values sent search.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param idCertAndRoles
	 *            : : ID certification and consulting roles
	 * @param inicio
	 *            : Registry where consultation begins
	 * @param rango
	 *            : Range of records
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : Parameters of the query.
	 * @return List<Activities>: List of names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> consultarActivityNamesXIdCert(int idCertAndRoles,
			int inicio, int rango, StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM Activities a ");
		query.append("JOIN FETCH a.activityName an ");
		query.append("JOIN FETCH a.crop c ");
		query.append("JOIN FETCH c.cropNames cn ");
		query.append("WHERE a IN (SELECT at FROM ActivitiesAndCertifications ac ");
		query.append("JOIN ac.activitiesAndCertificationsPK.activities at ");
		query.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles cr ");
		query.append("WHERE cr.idCertificactionsAndRoles=:idCertAndRoles ) ");
		query.append(consulta);
		query.append("ORDER BY a.idActivity ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCertAndRoles", idCertAndRoles);
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing activities in the database by filtering
	 * Sent search values.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param idCertAndRoles
	 *            : Name identifier crop
	 * @param consulta
	 *            : String containing the query why the filter names activities.
	 * @param parametros
	 *            : Parameters of the query.
	 * @return Long: Number of records found name activities.
	 * @throws Exception
	 */
	public Long cantidadActivitiesXIdCert(int idCertAndRoles,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(a) FROM Activities a ");
		query.append("JOIN a.activityName an ");
		query.append("JOIN a.crop c ");
		query.append("JOIN c.cropNames cn ");
		query.append("WHERE a IN (SELECT at FROM ActivitiesAndCertifications ac ");
		query.append("JOIN ac.activitiesAndCertificationsPK.activities at ");
		query.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles cr ");
		query.append("WHERE cr.idCertificactionsAndRoles=:idCertAndRoles ) ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		q.setParameter("idCertAndRoles", idCertAndRoles);
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method charge return the list of activities
	 * 
	 * @author Mabell.Boada
	 * 
	 * @return List<Activities>: List of all activities.
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> consultarActivities() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM Activities a ");
		query.append("JOIN FETCH a.activityName an ");
		Query q = em.createQuery(query.toString());
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult an activity that is related to an activity name
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param idActName
	 *            : Identifier name of the activity
	 * 
	 * @return Activities: Activity found according to the identifier sent as a
	 *         parameter.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Activities activityXIdActNames(int idActName) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM Activities a ");
		query.append("JOIN FETCH a.activityName an ");
		query.append("WHERE an.idActivityName=:idActName ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActName", idActName);
		List<Activities> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult the number of activities that require at least one certified.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param idActivity
	 *            : activity identifier.
	 * @return List<Activities>: list of activities with at least one certified.
	 * @throws Exception
	 */
	public Long consultarActivitiesCertificadas(int idActivity)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(a) FROM  Activities a ");
		query.append("WHERE a IN ");
		query.append("(SELECT aca FROM ActivitiesAndCertifications ac ");
		query.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
		query.append("WHERE aca.idActivity = :idActividad) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActividad", idActivity);
		return (Long) q.getSingleResult();
	}

	/**
	 * This method return the amount of the activities associated a crop
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @param idCrop
	 *            : id
	 * @return Long: amount of activities records found
	 * @throws Exception
	 */
	public Long amountActivities(StringBuilder consult,
			List<SelectItem> parameters, int idCrop) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(a) FROM Activities a ");
		query.append("JOIN a.activityName an ");
		query.append("JOIN a.crop c ");
		query.append("WHERE c.idCrop =:idCrop ");
		query.append("AND a.serviceRequired is true ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation activities with determined sent as a parameter
	 * range and filtering the information by the values sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Activities>: list of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Activities> consultActivities(int start, int range,
			StringBuilder consult, List<SelectItem> parameters, int idCrop)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM Activities a ");
		query.append("JOIN FETCH a.activityName an ");
		query.append("JOIN FETCH a.crop c ");
		query.append("WHERE c.idCrop =:idCrop ");
		query.append("AND a.serviceRequired is true ");
		query.append(consult);
		query.append("ORDER BY an.activityName ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Activities> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}
