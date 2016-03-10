package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.Allocation;

/**
 * DAO class that establishes the connection between business logic and base
 * data management assignments.
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class AllocationDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an assignment in the database.
	 * 
	 * @param allocation
	 *            : Assignment to save.
	 * @throws Exception
	 */
	public void saveAllocation(Allocation allocation) throws Exception {
		em.persist(allocation);
	}

	/**
	 * Edit an assignment in the database.
	 * 
	 * @param allocation
	 *            : Assignment to edit.
	 * @throws Exception
	 */
	public void editAllocation(Allocation allocation) throws Exception {
		em.merge(allocation);
	}

	/**
	 * Removes an assignment in the database.
	 * 
	 * @param allocation
	 *            : Assignment to remove.
	 * @throws Exception
	 */
	public void removeAllocation(Allocation allocation) throws Exception {
		em.remove(em.merge(allocation));
	}

	/**
	 * This method consulting assignments with a certain range sent as a
	 * parameter and filtering the information by the values of search sent.
	 * 
	 * @param start
	 *            : where it initiates the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @return List<Allocation>: List of assignments
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Allocation> consultAllocation(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM Allocation a ");
		query.append(consult);
		query.append("ORDER BY a.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Allocation> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing allocations in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter assignments.
	 * @param parameters
	 *            : query parameters.
	 * @return long: number of records found allocations.
	 * @throws Exception
	 */
	public Long quantityAllocation(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(a) FROM Allocation a ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult if the name of the allocations exist in the database when saving
	 * or editing.
	 * 
	 * @author Jhair.Leal
	 * 
	 * @param name
	 *            : Name the allocation to verify.
	 * @param id
	 *            : id the allocation to verify.
	 * @return Allocation: Object found with the search parameters id and name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Allocation nameExists(String name, int idAllocation)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM Allocation a ");
		query.append("WHERE UPPER(a.name)=UPPER(:name) ");
		if (idAllocation != 0) {
			query.append("AND a.idAllocation <>:idAllocation ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (idAllocation != 0) {
			q.setParameter("idAllocation", idAllocation);
		}
		List<Allocation> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
