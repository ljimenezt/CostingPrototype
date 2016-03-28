package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.ContractType;

/**
 * DAO class that establishes the connection between business logic and
 * database. ContractTypeAction manages ContractType.
 * 
 * @author Mabell.Boada
 */
@SuppressWarnings("serial")
@Stateless
public class ContractTypeDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for one type of contract.
	 * 
	 * @param contractType
	 *            : Contract to edit.
	 * @throws Exception
	 */
	public void editContractType(ContractType contractType) throws Exception {
		em.merge(contractType);
	}

	/**
	 * Saves the type of contract in the database.
	 * 
	 * @param contractType
	 *            : Contract to save.
	 * @throws Exception
	 */
	public void saveContractType(ContractType contractType) throws Exception {
		em.persist(contractType);
	}

	/**
	 * Removes a type of contract from the database.
	 * 
	 * @param contractType
	 *            : Contract to remove.
	 * @throws Exception
	 */
	public void removeContractType(ContractType contractType) throws Exception {
		em.remove(em.merge(contractType));
	}

	/**
	 * Returns the number of existing contract types in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter names types of
	 *            contract.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of records found types of contract.
	 * @throws Exception
	 */
	public Long quantityContractType(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ct) FROM ContractType ct ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation contract types with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consult the logs depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<ContractType>: List of contract types.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ContractType> consultContractType(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM ContractType ct ");
		query.append(consult);
		query.append("ORDER BY ct.nombre ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<ContractType> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult if the name of the contract types exist in the database when
	 * saving or editing.
	 * 
	 * @param name
	 *            : Name the type of contract to verify.
	 * @param id
	 *            : id the type of contract to verify.
	 * @return ContractType: Object found with the search parameters id and
	 *         name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ContractType nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM ContractType ct ");
		query.append("WHERE UPPER(ct.nombre)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND ct.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<ContractType> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
