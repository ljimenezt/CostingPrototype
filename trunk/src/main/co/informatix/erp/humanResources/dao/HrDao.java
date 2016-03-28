package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.Hr;

/**
 * Class DAO that implements the connection between business logic and database.
 * HrAction manages Human Resources.
 * 
 * @author Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class HrDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for a human resource.
	 * 
	 * @param hr
	 *            : Human resource to edit.
	 * @throws Exception
	 */
	public void editHr(Hr hr) throws Exception {
		em.merge(hr);
	}

	/**
	 * Save the human resource in the database.
	 * 
	 * @param hr
	 *            : Human resources to save.
	 * @throws Exception
	 */
	public void saveHr(Hr hr) throws Exception {
		em.persist(hr);
	}

	/**
	 * Deletes the human resource of the DataBase.
	 * 
	 * @param hr
	 *            : Human resource to eliminate.
	 * @throws Exception
	 */
	public void deleteHr(Hr hr) throws Exception {
		em.remove(em.merge(hr));
	}

	/**
	 * Returns the number of existing human resources in the database, the query
	 * has filters.
	 * 
	 * @param query
	 *            : String containing the query with filters.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of HR records found.
	 * @throws Exception
	 */
	public Long hrAmount(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(h) FROM Hr h ");
		queryBuilder.append(query);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * It makes a query with a certain records and it filters them with search
	 * values.
	 * 
	 * @param start
	 *            : First record of the query result that is retrieved.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List
	 *         <Hr>
	 *         : List of human resources.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hr> queryHr(int start, int range, StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT h FROM Hr h ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY h.name ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<Hr> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Query if the full human resource name exists in database when storing or
	 * editing.
	 * 
	 * @modify 11/09/2015 Dario.Lopez
	 * 
	 * @param name
	 *            : Human resource name to verify.
	 * @param lastName
	 *            : Human resource last Name to verify.
	 * @param id
	 *            : Human resource id to verify.
	 * @return Hr: Hr object found with the search parameters surname and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Hr fullNameExists(String name, String lastName, int id)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT h FROM Hr h ");
		queryBuilder.append("WHERE UPPER(h.familyName)=UPPER(:apellido) ");
		queryBuilder.append("AND  UPPER(h.name)=UPPER(:nombre)   ");
		if (id != 0) {
			queryBuilder.append("AND h.idHr <>:idHr ");
		}
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("apellido", lastName);
		queryResult.setParameter("nombre", name);
		if (id != 0) {
			queryResult.setParameter("idHr", id);
		}
		List<Hr> results = queryResult.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Query article assigned to a human resource, it takes only those that are
	 * not null in the table.
	 * 
	 * @param objectName
	 *            : Object name that is a human resources property.
	 * @param idHr
	 *            : Human Resource id that is being queried.
	 * @return Object information associated with human resource or null if it
	 *         does not exist.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object queryHrObject(String objectName, int idHr) throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT h." + objectName
								+ " FROM Hr h WHERE h.idHr=:idHr")
				.setParameter("idHr", idHr).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that returns the list of human resources.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @return List
	 *         <Hr>
	 *         : List of all human resources.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hr> queryHr() throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT hr FROM Hr hr ");
		queryBuilder.append("ORDER BY hr.name ASC");
		Query query = em.createQuery(queryBuilder.toString());
		List<Hr> resultList = query.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Query the amount of certified human resources that can be assigned to a
	 * specific activity to have certain certifications.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param idActivity
	 *            : Activity identifier.
	 * @param idHrTypes
	 *            : Human resource type identifier
	 * @return Long: Amounts of activities with at least one certified.
	 * @throws Exception
	 */
	public Long queryHrCertifications(int idActivity, int idHrTypes)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(h) FROM Hr h ");
		query.append("JOIN h.hrTypes ht ");
		query.append("WHERE h IN ");
		query.append("(SELECT h FROM HrCertificationsAndRoles hrc ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.certificationsAndRoles ca ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.hr h ");
		query.append("WHERE ca IN ");
		query.append("(SELECT acr FROM ActivitiesAndCertifications ac ");
		query.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles acr ");
		query.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
		query.append("WHERE aca.idActivity = :idActividad)) ");
		if (idHrTypes > 0)
			query.append("AND ht.idHrType = :idHrTypes ");
		Query q = em.createQuery(query.toString());
		if (idHrTypes > 0)
			q.setParameter("idHrTypes", idHrTypes);
		q.setParameter("idActividad", idActivity);
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult if certified human resources and state of mother hood that can be
	 * assigned to a specific activity.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param idActivity
	 *            : Activity identifier.
	 * @param idHrTypes
	 *            : Human resource type identifier
	 * @return Long: list of activities with at least one certified.
	 * @throws Exception
	 */
	public Long hrCertifiedAndMaternityAmount(int idActivity, int idHrTypes)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(h) FROM Hr h ");
		query.append("JOIN h.hrTypes ht ");
		query.append("WHERE h IN ");
		query.append("(SELECT h FROM HrCertificationsAndRoles hrc ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.certificationsAndRoles ca ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.hr h ");
		query.append("WHERE ca IN ");
		query.append("(SELECT acr FROM ActivitiesAndCertifications ac ");
		query.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles acr ");
		query.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
		query.append("WHERE aca.idActivity = :idActividad)) ");
		query.append("AND h.maternityBreastFeeding = true ");
		if (idHrTypes > 0)
			query.append("AND ht.idHrType = :idHrTypes ");
		Query q = em.createQuery(query.toString());
		if (idHrTypes > 0)
			q.setParameter("idHrTypes", idHrTypes);
		q.setParameter("idActividad", idActivity);
		return (Long) q.getSingleResult();
	}

}
