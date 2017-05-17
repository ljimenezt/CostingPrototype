package co.informatix.erp.utz.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.utz.entities.ActivitiesAndCertifications;

/**
 * DAO class that establishes the connection between business logic and
 * database. ActivitiesAndCertificationsAction used for managing
 * ActivitiesAndCertifications.
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ActivitiesAndCertificationsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a ActivitiesAndCertificationsen in DB.
	 * 
	 * @param activitiesAndCertifications
	 *            : activitiesAndCertifications to save.
	 * @throws Exception
	 */
	public void saveActivitiesAndCertifications(
			ActivitiesAndCertifications activitiesAndCertifications)
			throws Exception {
		em.persist(activitiesAndCertifications);
	}

	/**
	 * Delete a activity and certification of DB.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param activitiesAndCertifications
	 *            : Activity and certification to delete.
	 * @throws Exception
	 */
	public void removeActivitiesAndCertifications(
			ActivitiesAndCertifications activitiesAndCertifications)
			throws Exception {
		em.remove(em.merge(activitiesAndCertifications));
	}

	/**
	 * This method allows search the ActivitiesAndCertifications object whit the
	 * parameters of search.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param idActivities
	 *            : Identifier of activities.
	 * @param idCertAndRoles
	 *            : Identifier of certifications and roles.
	 * @return ActivitiesAndCertifications: Object of
	 *         ActivitiesAndCertifications that is related.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActivitiesAndCertifications activAndCertifXIdActiviAndIdCertAndRol(
			int idActivities, int idCertAndRoles) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ac FROM ActivitiesAndCertifications ac ");
		query.append("JOIN FETCH ac.activitiesAndCertificationsPK.activities a ");
		query.append("JOIN FETCH ac.activitiesAndCertificationsPK.certificationsAndRoles cr ");
		query.append("WHERE a.idActivity=:idActivities ");
		query.append("AND cr.idCertificactionsAndRoles=:idCertAndRoles ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivities", idActivities);
		q.setParameter("idCertAndRoles", idCertAndRoles);
		List<ActivitiesAndCertifications> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that consult the activities by certification
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param query
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: quantity of activities found
	 * @throws Exception
	 */
	public Long queryActivitiesByIdCert(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder
				.append("SELECT COUNT(ac) FROM ActivitiesAndCertifications ac ");
		queryBuilder.append("JOIN ac.activitiesAndCertificationsPK apk ");
		queryBuilder.append("JOIN apk.activities a ");
		queryBuilder.append("JOIN apk.certificationsAndRoles cr ");
		queryBuilder.append("JOIN a.activityName an ");
		queryBuilder.append("JOIN a.crop c ");
		queryBuilder.append("JOIN c.cropNames cn ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * Method that consult the activities by certification
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param start
	 *            : records where start the consult.
	 * @param range
	 *            : ranges to records.
	 * @param query
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<ActivitiesAndCertifications>: list of
	 *         ActivitiesAndCertifications found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivitiesAndCertifications> queryActivityNamesByIdCert(
			int start, int range, StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ac FROM ActivitiesAndCertifications ac ");
		queryBuilder.append("JOIN FETCH ac.activitiesAndCertificationsPK apk ");
		queryBuilder.append("JOIN FETCH apk.activities a ");
		queryBuilder.append("JOIN FETCH apk.certificationsAndRoles cr ");
		queryBuilder.append("JOIN FETCH a.activityName an ");
		queryBuilder.append("JOIN FETCH a.crop c ");
		queryBuilder.append("JOIN FETCH c.cropNames cn ");
		queryBuilder.append(query);
		queryBuilder
				.append("ORDER BY ac.activitiesAndCertificationsPK.activities.activityName.activityName ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<ActivitiesAndCertifications> resultList = queryResult
				.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}
