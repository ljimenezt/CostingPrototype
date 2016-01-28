package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.MachineUsage;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the manage usage.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MachineUsageDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a machine usage in the DB
	 * 
	 * @param machineUsage
	 *            : one machine usage to save.
	 * @throws Exception
	 */
	public void saveMachineUsage(MachineUsage machineUsage) throws Exception {
		em.persist(machineUsage);
	}

	/**
	 * Edits a machine usage BD
	 * 
	 * @param machineUsage
	 *            : machine usage to edit.
	 * @throws Exception
	 */
	public void editMachineUsage(MachineUsage machineUsage) throws Exception {
		em.merge(machineUsage);
	}

	/**
	 * Deletes a machine usage BD
	 * 
	 * @param machineUsage
	 *            : machine usage to remove
	 * @throws Exception
	 */
	public void deleteMachineUsage(MachineUsage machineUsage) throws Exception {
		em.remove(em.merge(machineUsage));
	}

	/**
	 * Consult the machine usage with a identifier
	 * 
	 * @param machineUsage
	 *            : identifier of the machine usage to found
	 * @return machineUsage: Object founded for the identifier
	 * @throws Exception
	 */
	public MachineUsage machineUsageXId(int machineUsage) throws Exception {
		return em.find(MachineUsage.class, machineUsage);
	}

	/**
	 * Returns the number of machine usages in the database information by
	 * filtering the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found machine usage
	 * @throws Exception
	 */
	public Long amountMachineUsage(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(mu) FROM MachineUsage mu ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<MachineUsage>: list of the machine usages.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MachineUsage> consultMachineUsage(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mu FROM MachineUsage mu ");
		query.append("JOIN mu.machineUsagePK.machine ma ");
		query.append(consult);
		query.append("ORDER BY mu.usage ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MachineUsage> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method that consult all Machine Usages object and stores it in a list
	 * 
	 * @return List<MachineUsage>: Machine Usages list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MachineUsage> listMachineUsage() throws Exception {
		Query q = em.createQuery("SELECT mu FROM MachineUsage mu ");
		return q.getResultList();
	}

	/**
	 * Consultation if the machine there in the database when storing or
	 * editing.
	 * 
	 * @param idMachine
	 *            : identifier machine to verify
	 * @param year
	 *            : year harvest to verify
	 * @return MachineUsage: Object machine usage
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MachineUsage machineUsageExists(int idMachine, int year)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mu FROM MachineUsage mu ");
		query.append("WHERE mu.machineUsagePK.machine.idMachine = :idMachine ");
		query.append("AND mu.machineUsagePK.year = :year ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMachine", idMachine);
		q.setParameter("year", year);
		List<MachineUsage> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
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
	 *            : year to search a machine usage
	 * 
	 * @return List<MachineUsage>: Machine Usage list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MachineUsage> listMachineUsageXYear(int year) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mu FROM MachineUsage mu ");
		query.append("JOIN FETCH mu.machineUsagePK.machine ma ");
		query.append("WHERE mu.machineUsagePK.year = :year ");
		query.append("AND mu.usage IS NOT NULL ");
		Query q = em.createQuery(query.toString());
		q.setParameter("year", year);
		List<MachineUsage> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
