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
 * DAO Class that establishes the connection between business logic and
 * database. ActivitiesAndServicesAction manages activities and services.
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
	 * Save an activity and service in the database.
	 * 
	 * @param activitiesAndServices
	 *            : Activity and service to save.
	 * @throws Exception
	 */
	public void saveActivitiesAndServices(
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
	public void editActivitiesAndServices(
			ActivitiesAndServices activitiesAndServices) throws Exception {
		em.merge(activitiesAndServices);
	}

	/**
	 * This method queries activities that are associated with a service.
	 * 
	 * @param activities
	 *            : Associated activity and service.
	 * @return List<ActivitiesAndServices>: List of activities and services.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivitiesAndServices> searchXActivities(Activities activities)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT aas FROM  ActivitiesAndServices aas ");
		query.append("JOIN aas.activities a ");
		query.append("WHERE a =:activities ");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("activities", activities);
		List<ActivitiesAndServices> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Look for an specified property of a service; the result retrieves only
	 * those which are not null in the table.
	 * 
	 * 
	 * @param propertyName
	 *            : Property name of the ActivitiesAndService entity.
	 * @param idService
	 *            : Identifier for the service that is being queried.
	 * @return Property information or object associated with the service. Null
	 *         if there is nothing.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object searchServiceProperty(String propertyName, int idService)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT aas." + propertyName
								+ " FROM ActivitiesAndServices aas WHERE"
								+ " aas.idService=:idService")
				.setParameter("idService", idService).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
