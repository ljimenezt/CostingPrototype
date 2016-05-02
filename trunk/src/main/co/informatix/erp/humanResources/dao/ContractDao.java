package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.Contract;

/**
 * DAO Class that establishes the connection between business logic and database
 * management. Contracts are handled in the payroll of the company.
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ContractDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save a contract in database.
	 * 
	 * @param contract
	 *            : Contract to keep.
	 * @throws Exception
	 */
	public void saveContract(Contract contract) throws Exception {
		em.persist(contract);
	}

	/**
	 * Edit the information of an existing contract in database.
	 * 
	 * @param contract
	 *            : Contract edited.
	 * @throws Exception
	 */
	public void editContract(Contract contract) throws Exception {
		em.merge(contract);
	}

	/**
	 * Removes a contract in database.
	 * 
	 * @param contract
	 *            : Contract to remove.
	 * @throws Exception
	 */
	public void deleteContract(Contract contract) throws Exception {
		em.remove(em.merge(contract));
	}

	/**
	 * Method for querying an object of Contract.
	 * 
	 * @param objectName
	 *            : Object property you want to query.
	 * @param contractId
	 *            : Id of the Contract to get.
	 * @return Contract Object for the query results.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object searchContract(String objectName, int contractId)
			throws Exception {
		List<Object> result = (List<Object>) em
				.createQuery(
						"SELECT c." + objectName + " FROM Contract c "
								+ "WHERE c.id=:idContrato")
				.setParameter("idContrato", contractId).getResultList();
		if (result.size() > 0) {
			return (Object) result.get(0);
		}
		return null;
	}

	/**
	 * Returns the number of existing contracts in the database; the information
	 * is filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query with filtered contracts.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of contract records found.
	 * @throws Exception
	 */
	public Long amountContracts(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(c) FROM Contract c ");
		queryBuilder.append(query);
		Query result = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			result.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) result.getSingleResult();
	}

	/**
	 * This method looks for contracts with a given range sent as a parameter
	 * and filters the information with search values.
	 * 
	 * @param first
	 *            : The first record selected of the query.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Contract>: Contracts list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Contract> searchContracts(int first, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT c FROM Contract c ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY c.id ");
		Query resultQuery = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			resultQuery
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		resultQuery.setFirstResult(first).setMaxResults(range);
		List<Contract> resultList = resultQuery.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
