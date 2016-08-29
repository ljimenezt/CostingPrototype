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
 * DAO Class that establishes the connection between business logic and database
 * for handling the machine usages.
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
	 * Saves a machine usage in the DataBase.
	 * 
	 * @param machineUsage
	 *            : One machine usage to save.
	 * @throws Exception
	 */
	public void saveMachineUsage(MachineUsage machineUsage) throws Exception {
		em.persist(machineUsage);
	}

	/**
	 * Edits a machine usage in the database.
	 * 
	 * @param machineUsage
	 *            : Machine usage to edit.
	 * @throws Exception
	 */
	public void editMachineUsage(MachineUsage machineUsage) throws Exception {
		em.merge(machineUsage);
	}

	/**
	 * Deletes a machine usage of the database.
	 * 
	 * @param machineUsage
	 *            : Machine usage to remove.
	 * @throws Exception
	 */
	public void deleteMachineUsage(MachineUsage machineUsage) throws Exception {
		em.remove(em.merge(machineUsage));
	}

	/**
	 * Returns the number of machine usages in the database; its information is
	 * filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query with its properties filtered.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of machine usage records found.
	 * @throws Exception
	 */
	public Long machineUsageAmount(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(mu) FROM MachineUsage mu ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parametro : parameters) {
			queryResult
					.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * This method builds a query with an advanced search, it also makes display
	 * messages depending on the search criteria selected by the user.
	 * 
	 * @param start
	 *            : The first record retrieved of the query result.
	 * @param range
	 *            : Range of records
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<MachineUsage>: List of the machine usages.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MachineUsage> searchMachineUsage(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT mu FROM MachineUsage mu ");
		queryBuilder.append("JOIN mu.machineUsagePK.machine ma ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY mu.usage ");
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MachineUsage> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Check if the machine is in the database when storing or editing.
	 * 
	 * @param machineId
	 *            : Machine Identifier to verify.
	 * @param year
	 *            : Harvest year to verify.
	 * @return MachineUsage: Object machine usage.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MachineUsage machineUsageExists(int machineId, int year)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mu FROM MachineUsage mu ");
		query.append("WHERE mu.machineUsagePK.machine.idMachine = :idMachine ");
		query.append("AND mu.machineUsagePK.year = :year ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMachine", machineId);
		q.setParameter("year", year);
		List<MachineUsage> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that queries all activities and machine object and stores it in a
	 * list.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param year
	 *            : Year to search a machine usage.
	 * @return List<MachineUsage>: Machine Usage list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MachineUsage> listMachineUsageXYear(int year) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT mu FROM MachineUsage mu ");
		queryBuilder.append("JOIN FETCH mu.machineUsagePK.machine ma ");
		queryBuilder.append("WHERE mu.machineUsagePK.year = :year ");
		queryBuilder.append("AND mu.usage IS NOT NULL ");
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("year", year);
		List<MachineUsage> resultList = query.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}