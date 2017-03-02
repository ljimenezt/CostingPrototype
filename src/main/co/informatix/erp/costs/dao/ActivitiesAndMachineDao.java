package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.costs.entities.ActivityMachinePK;

/**
 * DAO class that establishes the connection between business logic and data
 * base information management activities and machines.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class ActivitiesAndMachineDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save the relationship between the activity and machines in the database
	 * 
	 * @param activityMachine
	 *            : machine and activity association to save.
	 * @throws Exception
	 */
	public void saveActivitiesAndMachine(ActivityMachine activityMachine)
			throws Exception {
		em.persist(activityMachine);
	}

	/**
	 * Edit the relationship of the activity with a machines in the database
	 * 
	 * @param activityMachine
	 *            : relationship between the activity and machine to edit.
	 * @throws Exception
	 */
	public void editActivitiesAndMachine(ActivityMachine activityMachine)
			throws Exception {
		em.merge(activityMachine);
	}

	/**
	 * Removes the relationship between the activity and machines in the
	 * database
	 * 
	 * @param activityMachine
	 *            : relationship between activity and machines to eliminate
	 * @throws Exception
	 */
	public void deleteActivitiesAndMachine(ActivityMachine activityMachine)
			throws Exception {
		em.remove(em.merge(activityMachine));
	}

	/**
	 * Returns the number of the relationship between machine and activities
	 * existing in the database by filtering the information by the values of
	 * search sent.
	 * 
	 * 
	 * @param consult
	 *            : String containing the query why seep
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of activity records found
	 * @throws Exception
	 */
	public Long quantityActivitiesAndMachine(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(am) FROM ActivityMachine am ");
		query.append("JOIN am.activityMachinePK.machines m ");
		query.append("JOIN am.activityMachinePK.activities ac ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the relationship between activities and machines
	 * sending a certain range as a parameter and filtering information search
	 * for the values sent.
	 * 
	 * @modify 28/02/2017 Patricia.Patinio
	 * 
	 * @param start
	 *            : where it started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @return List<ActivityMachine>: List of machines and activities
	 *         associations found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMachine> consultingActivitiesAndMachine(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT am FROM ActivityMachine am ");
		query.append("JOIN FETCH am.activityMachinePK.machines m ");
		query.append("JOIN FETCH am.activityMachinePK.activities ac ");
		query.append("JOIN FETCH ac.activityName an ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<ActivityMachine> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method that consult all activity and machine object and stores it in a
	 * list
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param year
	 *            : year parameter to search a machine
	 * @return List<ActivityMachine>: ActivityMachine list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMachine> listActivitiesMachineXYear(String year)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT am FROM ActivityMachine am ");
		query.append("JOIN FETCH am.activityMachinePK.activities apk ");
		query.append("JOIN FETCH am.activityMachinePK.machines mpk ");
		query.append("JOIN apk.crop c ");
		query.append("JOIN FETCH apk.activityName ");
		query.append("WHERE TO_CHAR(c.registrationYear,'YYYY')= :year ");
		query.append("AND am.durationActual IS NULL ");
		Query q = em.createQuery(query.toString());
		q.setParameter("year", year);
		List<ActivityMachine> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method that consult all activity and machine object and stores it in a
	 * list
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param year
	 *            : year parameter to search a machine
	 * @return List<ActivityMachine>: ActivityMachine list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMachine> listActivitiesMachine(String year)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT am FROM ActivityMachine am ");
		query.append("JOIN FETCH am.activityMachinePK.activities apk ");
		query.append("JOIN FETCH am.activityMachinePK.machines mpk ");
		query.append("JOIN apk.crop c ");
		query.append("JOIN FETCH apk.activityName ");
		query.append("WHERE TO_CHAR(c.registrationYear,'YYYY')= :year ");
		Query q = em.createQuery(query.toString());
		q.setParameter("year", year);
		List<ActivityMachine> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult the sum of the total cost of each of the machines activity
	 * 
	 * @modify 06/10/2016 Luna.Granados
	 * 
	 * @param idActivity
	 *            : Activity identifier.
	 * @return Double: Sum of machine costs
	 * @throws Exception
	 */
	public Double calculateTotalCostMachine(int idActivity) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(am.consumablesCostActual) FROM  ActivityMachine am ");
		query.append("JOIN am.activityMachinePK.activities a ");
		query.append("WHERE a.idActivity = :idActivity  ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		if (q.getSingleResult() != null) {
			return (Double) q.getSingleResult();
		}
		return (0.0);
	}

	/**
	 * Consult the relation between activities and machines for his identifier.
	 * 
	 * @param activityMachinePK
	 *            : Indentifier for relation between activities and machines.
	 * @return ActivityMachine: Relation between activities and machines.
	 * @throws Exception
	 */
	public ActivityMachine activityMachineById(
			ActivityMachinePK activityMachinePK) throws Exception {
		return em.find(ActivityMachine.class, activityMachinePK);
	}

	/**
	 * Consult the relation between activities and machines
	 * 
	 * @author Luna.Granados
	 * 
	 * @param idActivity
	 *            : Identifier of activity.
	 * @return List<ActivityMachine>: List of relation between activities and
	 *         machines
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityMachine> listActivitiesAndMachine(int idActivity)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT am FROM ActivityMachine am ");
		query.append("JOIN FETCH am.activityMachinePK.machines m ");
		query.append("JOIN FETCH am.activityMachinePK.activities ac ");
		query.append("WHERE ac.idActivity = :idActivity ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idActivity", idActivity);
		List<ActivityMachine> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}