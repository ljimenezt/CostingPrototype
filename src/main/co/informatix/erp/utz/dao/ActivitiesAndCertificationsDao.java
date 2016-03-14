package co.informatix.erp.utz.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
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
	public void guardarActivitiesAndCertifications(
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
	 *            : Activity and certification to delete
	 * @throws Exception
	 */
	public void eliminarActivitiesAndCertifications(
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
}
