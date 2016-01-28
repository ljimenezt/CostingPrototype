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
 * database. SuppliersAction used for managing Suppliers
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
	 * Edit the track information for a vendor
	 * 
	 * @param suppliers
	 *            : Supplier to edit
	 * @throws Exception
	 */
	public void editarSuppliers(Suppliers suppliers) throws Exception {
		em.merge(suppliers);
	}

	/**
	 * Save the vendor in the database
	 * 
	 * @param suppliers
	 *            : Provider to save
	 * @throws Exception
	 */
	public void guardarSuppliers(Suppliers suppliers) throws Exception {
		em.persist(suppliers);
	}

	/**
	 * Deleting a provider BD
	 * 
	 * @param suppliers
	 *            : Supplier to remove
	 * @throws Exception
	 */
	public void eliminarSuppliers(Suppliers suppliers) throws Exception {
		em.remove(em.merge(suppliers));
	}

	/**
	 * Returns the number of existing suppliers on the BD filtering the
	 * information by the values sent search
	 * 
	 * @param consulta
	 *            : String containing the query why the names of the suppliers
	 *            are filtered
	 * @param parametros
	 *            : Query parameters
	 * @return Long: Number of records found name providers
	 * @throws Exception
	 */
	public Long cantidadSuppliers(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(s) FROM Suppliers s ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the names of the suppliers with a certain range sent
	 * as a parameter and filtering the information by the values sent search
	 * 
	 * @param start
	 *            : Registry where consultation begins
	 * @param range
	 *            : Range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user
	 * @param parameters
	 *            : Query parameters
	 * @return List<Suppliers>: List of names of suppliers
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Suppliers> consultarSuppliers(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT s FROM Suppliers s ");
		query.append(consult);
		query.append("ORDER BY s.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Suppliers> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation If the vendor name exists in the database when storing or
	 * editing
	 * 
	 * @param nombre
	 *            : Supplier name to verify
	 * @param id
	 *            : id provider to verify
	 * @return Suppliers: Object suppliers found with the search parameters name
	 *         and id
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Suppliers nombreExiste(String nombre, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT s FROM Suppliers s ");
		query.append("WHERE UPPER(s.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND s.idSupplier <>:idSupplier ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", nombre);
		if (id != 0) {
			q.setParameter("idSupplier", id);
		}
		List<Suppliers> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Queries all currently DB providers.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<Suppliers>: List of suppliers
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Suppliers> consultarComboSuppliers() throws Exception {
		return em
				.createQuery("SELECT s FROM Suppliers s " + "ORDER BY s.name ")
				.getResultList();
	}

}
