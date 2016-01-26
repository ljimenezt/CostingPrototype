package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.ActivityNames;

/**
 * Class DAO that establishes the connection between the business logic and the
 * database for managing the names of activities.
 * 
 * @author Dario.Lopez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ActivityNamesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method allows consulting activity names with a certain range sent as
	 * a parameter and filtering the information sent search values.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<ActivityNames>: list of the names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> consultarActivityNames(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM  ActivityNames an ");
		query.append(consult);
		query.append("ORDER BY an.activityName ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a name of activity in BD
	 * 
	 * @param activityNames
	 *            : activity name to save.
	 * @throws Exception
	 */
	public void guardarActivityNames(ActivityNames activityNames)
			throws Exception {
		em.persist(activityNames);
	}

	/**
	 * editing a name in BD activity
	 * 
	 * @param activityNames
	 *            : activity name to edit.
	 * @throws Exception
	 */
	public void editarActivityNames(ActivityNames activityNames)
			throws Exception {
		em.merge(activityNames);
	}

	/**
	 * Remove a name from the database activity
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param activityNames
	 *            : Name of the activity to remove
	 * @throws Exception
	 */
	public void eliminarActivityNames(ActivityNames activityNames)
			throws Exception {
		em.remove(em.merge(activityNames));
	}

	/**
	 * Returns the number of names of existing activities in the database by
	 * filtering information sent lookup values.
	 * 
	 * @param consult
	 *            : String containing the query by which names are filtered
	 *            activities.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of name registrations activities found
	 * @throws Exception
	 */
	public Long cantidadActivityNames(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(an) FROM ActivityNames an ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Query whether the activity name exists in the database when storing or
	 * editing.
	 * 
	 * @param name
	 *            : name of the activity to verify
	 * @param id
	 *            : identifier of the activity to verify
	 * @return ActivityNames: activityName object found with the search
	 *         parameters name and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActivityNames nombreExiste(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM ActivityNames an ");
		query.append("WHERE UPPER(an.activityName)=UPPER(:activityName) ");
		if (id != 0) {
			query.append("AND an.idActivityName <>:idActivityName ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("activityName", name);
		if (id != 0) {
			q.setParameter("idActivityName", id);
		}
		List<ActivityNames> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method queries the names of eliminating activities that are already
	 * associated with a name of harvest; with a certain range sent as parameter
	 * and filtering the information by the values sent search
	 * 
	 * @author Mabell.Boada
	 * @modify 24/07/2015 Andres.Gomez
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<ActivityNames>: list of the names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> consultarActivityNamesXIdCropNames(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM  ActivityNames an ");
		query.append(consult);
		query.append("ORDER BY an.activityName ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This query method names eliminating activities that are already
	 * associated with a harvest; with a certain range sent as parameter and
	 * filtering the information by the values sent search.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<ActivityNames>: list of the names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> consultarActivityNamesXIdCrop(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM  ActivityNames an ");
		query.append(consult);
		query.append("ORDER BY an.activityName ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of names of existing activities in the database by
	 * filtering search values sent.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of name registrations activities found
	 * @throws Exception
	 */
	public Long cantidadActivityNamesCrops(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(an) FROM  ActivityNames an ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the name of the activities That are Associated with a
	 * culture.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idCrop
	 *            : culture identifier.
	 * @return List<ActivityNames>: name list of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> consultarActivityNamesCrop(int idCrop)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM  ActivityNames an ");
		query.append("WHERE an IN ");
		query.append("(SELECT an1 FROM  Activities a ");
		query.append("JOIN a.crop c ");
		query.append("JOIN a.activityName an1 ");
		query.append("WHERE c.idCrop=:idCrop) ");
		query.append("ORDER BY an.activityName ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method queries the name of the activities associated with a crop.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param idCrop
	 *            : culture identifier.
	 * @return List<ActivityNames>: List of activity names.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> consultarActivityNamesXCrop(int idCrop)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT a.activityName FROM Activities a ");
		query.append("JOIN a.activityName an ");
		query.append("WHERE a.crop.idCrop =:idCrop ");
		query.append(" ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method to return the list of names of activities
	 * 
	 * @author Mabell.Boada
	 * 
	 * @return List<ActivityNames>: List of all the activities.
	 * @throws Exception
	 * 
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> consultarActivityNames() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM ActivityNames an ");
		Query q = em.createQuery(query.toString());
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
