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
 * DAO Class that establishes the connection between the business logic and the
 * database for managing activity names.
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
	 * This method allows to look for activity names with a certain range as a
	 * parameter and to filter the information with search values.
	 * 
	 * @param start
	 *            : Where it is started the query result.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<ActivityNames>: list of the names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> queryActivityNames(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM ActivityNames an ");
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
	 * This method allows to look for all activity names.
	 * 
	 * @author Luna.Granados
	 * 
	 * @return List<ActivityNames>: list of the names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> queryActivityNames() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT an FROM ActivityNames an ");
		query.append("ORDER BY an.activityName ");
		Query q = em.createQuery(query.toString());
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save an ActivityName in the DataBase.
	 * 
	 * @param activityNames
	 *            : ActivityName to save.
	 * @throws Exception
	 */
	public void saveActivityNames(ActivityNames activityNames) throws Exception {
		em.persist(activityNames);
	}

	/**
	 * Editing a name in ActivityName table
	 * 
	 * @param activityNames
	 *            : activity name to edit.
	 * @throws Exception
	 */
	public void modifyActivityNames(ActivityNames activityNames)
			throws Exception {
		em.merge(activityNames);
	}

	/**
	 * Remove an ActivityName from the table.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @param activityNames
	 *            : Name of the activity to remove.
	 * @throws Exception
	 */
	public void deleteActivityNames(ActivityNames activityNames)
			throws Exception {
		em.remove(em.merge(activityNames));
	}

	/**
	 * Returns the amount of existing activity names in the database by
	 * filtering information with lookup values.
	 * 
	 * @param consult
	 *            : String containing the query that filters ActivityNames.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of ActivityName records found
	 * @throws Exception
	 */
	public Long amountActivityNames(StringBuilder consult,
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
	 *            : Name of the activity to verify.
	 * @param id
	 *            : Identifier of the activity to verify.
	 * @return ActivityNames: activityName object found with the search
	 *         parameters name and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ActivityNames nameExists(String name, int id) throws Exception {
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
	 * This method queries the names of eliminated activities that are already
	 * associated with a name of harvest, a specified range parameter and
	 * filtered information with search values.
	 * 
	 * @author Mabell.Boada
	 * @modify 24/07/2015 Andres.Gomez
	 * 
	 * @param start
	 *            : The numbered record where the query result starts.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Search parameters.
	 * @return List<ActivityNames>: List of the activity names.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> queryActivityNamesXIdCropNames(int start,
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
	 * This query method eliminates activity names which are already associated
	 * with a harvest, with a certain range and with filtered information
	 * according to search values.
	 * 
	 * @author Andres.Gomez
	 * @modify 16/05/2017 Liseth.Jimenez
	 * 
	 * @param start
	 *            : The initial record the query retrieves.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<ActivityNames>: list of the names of activities.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> queryActivityNamesXIdCrop(int start, int range,
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
	 * Returns the amount of names of existing activities in the database
	 * according to filtering search values.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consult
	 *            : String containing the query with properties that are going
	 *            to do the filtering.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of the activity name records found
	 * @throws Exception
	 */
	public Long amountActivityNameCrops(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(an) FROM  ActivityNames an ");
		query.append("WHERE an.cycle IS FALSE ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the names of the activities associated with a crop.
	 * 
	 * @author Gerardo.Herrera
	 * @modify 20/06/2016 Liseth.Jimenez
	 * 
	 * @param idCrop
	 *            : Culture identifier.
	 * @return List<ActivityNames>: List of activity names.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> queryActivityNamesXCrop(int idCrop)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT a.activityName FROM Activities a ");
		query.append("JOIN a.activityName an ");
		query.append("WHERE a.crop.idCrop =:idCrop ");
		query.append("AND an.cycle IS FALSE ");
		query.append(" ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		query.append("ORDER BY an.activityName ASC ");
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult the ActivityNames list that is associated to a
	 * crop and are registered in cycles.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param idCrop
	 *            : Culture identifier.
	 * @return List<ActivityNames>: ActivityNames list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> queryActivityNamesInCycles(int idCrop)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM ActivityNames a ");
		query.append("WHERE a.idActivityName IN ( ");
		query.append("SELECT DISTINCT an.idActivityName FROM Cycle c ");
		query.append("JOIN c.activiyNames an ");
		query.append("WHERE c.crops.idCrop =:idCrop ) ");
		query.append("ORDER BY a.activityName ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		List<ActivityNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method that I see all ActivityNames object and stores it in a list
	 * 
	 * @author Wilhelm.Boada
	 * @modify 06/07/2016 Gerardo.Herrera
	 * 
	 * @return List<ActivityNames>: List of activity names.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityNames> activityNamesList() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM ActivityNames a ");
		query.append("WHERE a.cycle IS TRUE ");
		query.append("ORDER BY a.activityName ");
		Query q = em.createQuery(query.toString());
		return q.getResultList();
	}

}
