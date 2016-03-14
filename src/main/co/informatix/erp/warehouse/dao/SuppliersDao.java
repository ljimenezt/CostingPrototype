package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * DAO class that establishes the connection between business logic and
 * database. SuppliersAction manages Suppliers.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class SuppliersDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for a vendor.
	 * 
	 * @param suppliers
	 *            : Supplier to edit
	 * @throws Exception
	 */
	public void editSuppliers(Suppliers suppliers) throws Exception {
		em.merge(suppliers);
	}

	/**
	 * Save the vendor in the database.
	 * 
	 * @param suppliers
	 *            : Provider to save.
	 * @throws Exception
	 */
	public void saveSuppliers(Suppliers suppliers) throws Exception {
		em.persist(suppliers);
	}

	/**
	 * Delete a provider of the database.
	 * 
	 * @param suppliers
	 *            : Supplier to remove.
	 * @throws Exception
	 */
	public void deleteSuppliers(Suppliers suppliers) throws Exception {
		em.remove(em.merge(suppliers));
	}

	/**
	 * Returns the number of existing suppliers on the database filtering the
	 * information with search values.
	 * 
	 * @param query
	 *            : String containing the query with filters.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of records found.
	 * @throws Exception
	 */
	public Long suppliersAmount(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(s) FROM Suppliers s ");
		queryBuilder.append(query);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the names of the suppliers with a certain range sent
	 * as a parameter and it filters the information with search values.
	 * 
	 * @param start
	 *            : Record where the query result begins.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Suppliers>: List of suppliers.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Suppliers> suppliersFilteredSearch(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT s FROM Suppliers s ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY s.name ");
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<Suppliers> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Check If the vendor name exists in the database when storing or editing.
	 * 
	 * @param name
	 *            : Supplier name to verify.
	 * @param id
	 *            : id provider to verify.
	 * @return Suppliers: Object suppliers found with the search parameters name
	 *         and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Suppliers nameExists(String name, int id) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT s FROM Suppliers s ");
		queryBuilder.append("WHERE UPPER(s.name)=UPPER(:name) ");
		if (id != 0) {
			queryBuilder.append("AND s.idSupplier <>:idSupplier ");
		}
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("name", name);
		if (id != 0) {
			query.setParameter("idSupplier", id);
		}
		List<Suppliers> results = query.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Queries all providers who are currently in the database.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<Suppliers>: List of suppliers.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Suppliers> querySuppliers() throws Exception {
		return em
				.createQuery("SELECT s FROM Suppliers s " + "ORDER BY s.name ")
				.getResultList();
	}

	/**
	 * Suppliers method that queries an object by its ID.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param id
	 *            : Suppliers identifier to query.
	 * @return: Suppliers object found with the search parameter identifier.
	 * @throws Exception
	 */
	public Suppliers suppliersById(int id) throws Exception {
		return em.find(Suppliers.class, id);
	}
}
