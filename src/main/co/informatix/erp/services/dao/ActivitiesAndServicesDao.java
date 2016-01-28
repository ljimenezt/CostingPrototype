package co.informatix.erp.services.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.services.entities.ActivitiesAndServices;

/**
 * Class DAO that establishes the connection between business logic and
 * database. ActivitiesAndServicesAction used for the management of activities
 * and services.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ActivitiesAndServicesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an Activity and service in the database.
	 * 
	 * @param activitiesAndServices
	 *            : Activity and service to save.
	 * @throws Exception
	 */
	public void guardarActivitiesAndServices(
			ActivitiesAndServices activitiesAndServices) throws Exception {
		em.persist(activitiesAndServices);
	}

	/**
	 * Modifies an activity and service in the database.
	 * 
	 * @param activitiesAndServices
	 *            : Activity and service to edit.
	 * @throws Exception
	 */
	public void editarActivitiesAndServices(
			ActivitiesAndServices activitiesAndServices) throws Exception {
		em.merge(activitiesAndServices);
	}

	/**
	 * This method consultation activities that are associated with a service.
	 * 
	 * @param activities
	 *            : associated service activity.
	 * @return List<ActivitiesAndServices>: list of activities and services.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivitiesAndServices> consultarXActivities(
			Activities activities) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT aas FROM  ActivitiesAndServices aas ");
		query.append("JOIN aas.activities a ");
		query.append("WHERE a =:activities ");
		Query q = em.createQuery(query.toString());
		q.setParameter("activities", activities);
		List<ActivitiesAndServices> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult object assigned to a service, considering that are only those who
	 * are not null in the table
	 * 
	 * 
	 * @param nomObject
	 *            : object consulting service
	 * @param idService
	 *            : id service being queried
	 * @return Object information associated with the service or null if not
	 *         present.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoServices(String nomObject, int idService)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT aas."
								+ nomObject
								+ " FROM ActivitiesAndServices aas WHERE aas.idService=:idService")
				.setParameter("idService", idService).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
