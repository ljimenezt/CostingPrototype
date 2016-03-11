package co.informatix.erp.sales.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.sales.entities.Customer;

/**
 * DAO class that establishes the connection between business logic and database
 * for handling the Customer entity.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class CustomerDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method queries customers with a certain range sent as a parameter
	 * and it filters the information with search values.
	 * 
	 * @param first
	 *            : The first record that is retrieved from the query result.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Customer>: List of customers.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> searchCustomers(int first, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT c FROM  Customer c ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY c.name ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(first).setMaxResults(range);
		List<Customer> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a client in the database.
	 * 
	 * @param customer
	 *            : Customer to save.
	 * @throws Exception
	 */
	public void saveCustomer(Customer customer) throws Exception {
		em.persist(customer);
	}

	/**
	 * Edits a client in the database.
	 * 
	 * @param customer
	 *            : Editable customer.
	 * @throws Exception
	 */
	public void editCustomer(Customer customer) throws Exception {
		em.merge(customer);
	}

	/**
	 * Removes client from the database.
	 * 
	 * @param customer
	 *            : Client to remove.
	 * @throws Exception
	 */
	public void deleteCustomer(Customer customer) throws Exception {
		em.remove(em.merge(customer));
	}

	/**
	 * Returns the number of existing customers in the database with its
	 * information filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query and its filters.
	 * @param parameters
	 *            : Query parameters for filtering.
	 * @return Long: Amount of customer records found.
	 * @throws Exception
	 */
	public Long customersAmount(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(c) FROM Customer c ");
		queryBuilder.append(query);
		Query result = em.createQuery(queryBuilder.toString());
		for (SelectItem parametro : parameters) {
			result.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) result.getSingleResult();
	}

	/**
	 * Check if the client name exists in the database when storing or editing.
	 * 
	 * @param name
	 *            : Customer name to verify.
	 * @param id
	 *            : Customer id to verify.
	 * @return Customer: Customer object found with the search parameters.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Customer cutomerExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM Customer c ");
		query.append("WHERE UPPER(c.name)=UPPER(:nombre) ");
		if (id != 0) {
			query.append("AND c.idCustomer <>:idCustomer ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("nombre", name);
		if (id != 0) {
			q.setParameter("idCustomer", id);
		}
		List<Customer> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
