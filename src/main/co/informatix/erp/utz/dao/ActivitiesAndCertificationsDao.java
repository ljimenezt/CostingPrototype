package co.informatix.erp.utz.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.utz.entities.ActivitiesAndCertifications;

/**
 * DAO class that establishes the connection between business logic and
 * database. ActivitiesAndCertificationsAction used for managing
 * ActivitiesAndCertifications.
 * 
 * @author Johnatan.Naranjo
 * 
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

}
