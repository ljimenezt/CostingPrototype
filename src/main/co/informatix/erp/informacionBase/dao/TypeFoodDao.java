package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.TypeFood;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the TypeFood entity.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Stateless
public class TypeFoodDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves the typeFood in the database.
	 * 
	 * @param typeFood
	 *            : typeFood to save.
	 * @throws Exception
	 */
	public void saveTypeFood(TypeFood typeFood) throws Exception {
		em.persist(typeFood);
	}

	/**
	 * Edit the information for one typeFood.
	 * 
	 * @param typeFood
	 *            : typeFood to edit.
	 * @throws Exception
	 */
	public void editTypeFood(TypeFood typeFood) throws Exception {
		em.merge(typeFood);
	}

	/**
	 * Delete the typeFood of the database.
	 * 
	 * @param typeFood
	 *            : typeFood to remove.
	 * @throws Exception
	 */
	public void removeTypeFood(TypeFood typeFood) throws Exception {
		em.remove(em.merge(typeFood));
	}

	/**
	 * Returns the number of existing typeFood in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter names of
	 *            typeFood.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of records found.
	 * @throws Exception
	 */
	public Long quantityTypeFood(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(t) FROM TypeFood t ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation typeFood with a certain range sent as a
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
	 * @return List<TypeFood>: TypeFood list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TypeFood> consultTypeFood(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM TypeFood t ");
		query.append(consult);
		query.append("ORDER BY t.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<TypeFood> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult if the name of the typeFood exist in the database when saving or
	 * editing.
	 * 
	 * @param name
	 *            : Name of the typeFood to verify.
	 * @param id
	 *            : id of the typeFood to verify.
	 * @return TypeFood: Object found with the search parameters.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TypeFood nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM TypeFood t ");
		query.append("WHERE UPPER(t.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND t.id <> :id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<TypeFood> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method consultation typeFood with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param consult
	 *            : Consult the logs depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<TypeFood>: TypeFood list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TypeFood> consultTypeFood() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM TypeFood t ");
		query.append("ORDER BY t.name ");
		Query q = em.createQuery(query.toString());
		List<TypeFood> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}