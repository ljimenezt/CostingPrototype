package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.TypeOfManagement;

/**
 * DAO class that establishes the connection between business logic and
 * database. TypeOfManagementAction used for managing TypeOfManagement
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class TypeOfManagementDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for one type of management.
	 * 
	 * @param typeOfManagement
	 *            : Type of management to edit.
	 * @throws Exception
	 */
	public void editTypeOfManagement(TypeOfManagement typeOfManagement)
			throws Exception {
		em.merge(typeOfManagement);
	}

	/**
	 * Save the type of management in the database.
	 * 
	 * @param typeOfManagement
	 *            : Type of management to save.
	 * @throws Exception
	 */
	public void saveTypeOfManagement(TypeOfManagement typeOfManagement)
			throws Exception {
		em.persist(typeOfManagement);
	}

	/**
	 * Removes a type of management of the database.
	 * 
	 * @param typeOfManagement
	 *            : Type of management to eliminate.
	 * @throws Exception
	 */
	public void deleteTypeOfManagement(TypeOfManagement typeOfManagement)
			throws Exception {
		em.remove(em.merge(typeOfManagement));
	}

	/**
	 * Returns the number of existing types of management in the database and
	 * the information is filtered with some search value.
	 * 
	 * @param query
	 *            : String containing the filtering query with the names of the
	 *            types of management.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of management type records found.
	 * @throws Exception
	 */
	public Long amountTypeOfManagement(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(tm) FROM TypeOfManagement tm ");
		queryBuilder.append(query);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries management types with a certain range sent as a
	 * parameter and the information is filtered with search values.
	 * 
	 * @param start
	 *            : Record where the result begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depending on the parameters selected by the
	 *            user
	 * @param parameters
	 *            : Query parameters
	 * @return List<TypeOfManagement>: List of types of management
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TypeOfManagement> searchTypeOfManagement(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tm FROM TypeOfManagement tm ");
		query.append(consult);
		query.append("ORDER BY tm.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<TypeOfManagement> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Query whether the management type name exists in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : Name the type of management to verify.
	 * @param id
	 *            : id the type of management to verify.
	 * @return TypeOfManagement: Object found with the search parameters name
	 *         and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TypeOfManagement nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tm FROM TypeOfManagement tm ");
		query.append("WHERE UPPER(tm.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND tm.idTypeOfManagement <>:idTypeOfManagement ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idTypeOfManagement", id);
		}
		List<TypeOfManagement> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult the types of managements.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<TypeOfManagement>: list of types of managements.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TypeOfManagement> queryTypesOfManagements() throws Exception {
		return em.createQuery(
				"SELECT tm FROM TypeOfManagement tm ORDER BY tm.name ")
				.getResultList();
	}
}