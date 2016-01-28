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
	 * This method consultation with customers determined range sent as a
	 * parameter range and filtering the information by the values sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Customer>: list of customers.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Customer> consultarCustomers(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM  Customer c ");
		query.append(consult);
		query.append("ORDER BY c.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Customer> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a client in BD
	 * 
	 * @param customer
	 *            : customer to save.
	 * @throws Exception
	 */
	public void guardarCustomer(Customer customer) throws Exception {
		em.persist(customer);
	}

	/**
	 * Edits a client in BD
	 * 
	 * @param customer
	 *            : edit customer.
	 * @throws Exception
	 */
	public void editarCustomer(Customer customer) throws Exception {
		em.merge(customer);
	}

	/**
	 * Removes BD client
	 * 
	 * @param customer
	 *            : client to remove
	 * @throws Exception
	 */
	public void eliminarCustomer(Customer customer) throws Exception {
		em.remove(em.merge(customer));
	}

	/**
	 * Returns the number of existing customers in the database information by
	 * filtering the search values sent.
	 * 
	 * @param consulta
	 *            : String containing the query why customers are filtered.
	 * @param parametros
	 *            : query parameters.
	 * @return Long: number of customer records found
	 * @throws Exception
	 */
	public Long cantidadCustomers(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(c) FROM Customer c ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consultation If the client name exists in the database when storing or
	 * editing.
	 * 
	 * @param name
	 *            : customer name to verify
	 * @param id
	 *            : to verify customer id
	 * @return Customer: Customer object found with the search parameters name
	 *         and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Customer nombreExiste(String name, int id) throws Exception {
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
