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
	 * Save an assignment in the database
	 * 
	 * @param allocation
	 *            : Assignment to save.
	 * @throws Exception
	 */
	public void guardarAllocation(Allocation allocation) throws Exception {
		em.persist(allocation);
	}

	/**
	 * Edit an assignment in the database
	 * 
	 * @param allocation
	 *            : Assignment to edit.
	 * @throws Exception
	 */
	public void editarAllocation(Allocation allocation) throws Exception {
		em.merge(allocation);
	}

	/**
	 * Removes an assignment in the database
	 * 
	 * @param allocation
	 *            : Assignment to remove
	 * @throws Exception
	 */
	public void eliminarAllocation(Allocation allocation) throws Exception {
		em.remove(em.merge(allocation));
	}

	/**
	 * This method consulting assignments with a certain range sent as a
	 * parameter and filtering the information by the values of search sent.
	 * 
	 * @param inicio
	 *            : where it initiates the consultation record
	 * @param rango
	 *            : range of records
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : query parameters.
	 * @return List<Allocation>: List of assignments
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Allocation> consultarAllocation(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ec FROM Allocation ec ");
		query.append(consulta);
		query.append("ORDER BY ec.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
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
	 * @param consulta
	 *            : String containing the query why the filter assignments.
	 * @param parametros
	 *            : query parameters.
	 * @return long: number of records found allocations.
	 * @throws Exception
	 */
	public Long cantidadAllocation(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ec) FROM Allocation ec ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

}
