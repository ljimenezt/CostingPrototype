package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.MaterialsType;

/**
 * DAO class that establishes the connection between business logic and database
 * management MaterialsType.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MaterialsTypeDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method of consultation with a range determining MaterialsType sent
	 * as a parameter and filtering the information by the values sent search.
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
	 * @return List<MaterialsType>: List of Materials Type.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaterialsType> consultMaterialsType(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mt FROM  MaterialsType mt ");
		query.append(consult);
		query.append("ORDER BY mt.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MaterialsType> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Materials Type Saves a BD
	 * 
	 * @param materialsType
	 *            : Materials Type to save.
	 * @throws Exception
	 */
	public void saveMaterialsType(MaterialsType materialsType) throws Exception {
		em.persist(materialsType);
	}

	/**
	 * Edit a BD materials type
	 * 
	 * @param materialsType
	 *            : materialsType to edit.
	 * @throws Exception
	 */
	public void editMaterialsType(MaterialsType materialsType) throws Exception {
		em.merge(materialsType);
	}

	/**
	 * Delete materialsType
	 * 
	 * @param materialsType
	 *            : materialsType to delete
	 * @throws Exception
	 */
	public void removeMaterialsType(MaterialsType materialsType)
			throws Exception {
		em.remove(em.merge(materialsType));
	}

	/**
	 * Returns the number of existing BD materialsType in filtering the
	 * information by the values sent search.
	 * 
	 * @param consult
	 *            : String containing the query why the materialsType filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of material Type records found
	 * @throws Exception
	 */
	public Long quantityMaterialsType(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(mt) FROM MaterialsType mt ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the types of materials
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<MaterialsType>: list of types of materials
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaterialsType> consultMaterialsTypes() throws Exception {
		return em.createQuery(
				"SELECT mt FROM MaterialsType mt " + "ORDER BY mt.name ")
				.getResultList();
	}

	/**
	 * Consultation if the name of the materials type exists in the database
	 * when storing or editing.
	 * 
	 * @author Jhair.Leal
	 * 
	 * @param name
	 *            : materials type name to verify
	 * @param id
	 *            : identifier to verify the section
	 * @return Section: section object found with the search parameters name and
	 *         identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MaterialsType nameExist(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mt FROM MaterialsType mt ");
		query.append("WHERE UPPER(mt.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND mt.idMaterialsType <>:idMaterialsType ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idMaterialsType", id);
		}
		List<MaterialsType> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
