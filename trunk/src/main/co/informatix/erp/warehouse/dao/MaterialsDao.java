package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.Materials;

/**
 * DAO class that establishes the connection between business logic and
 * database. MaterialsAction used for managing Materials.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MaterialsDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Consult the list of materials that comply with the option of force.
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
	 * @return List<Materials>:List of materials that comply with the condition
	 *         of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Materials> consultarMateriales(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Materials m ");
		query.append(consult);
		query.append(" ORDER BY m.name");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing materials in the database that are
	 * existing or not existing.
	 * 
	 * @param consulta
	 *            : Query running on SQL.
	 * @param parametros
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long cantidadMateriales(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(m) FROM Materials m ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Saves a material in the database.
	 * 
	 * @param materials
	 *            : Save material.
	 * @throws Exception
	 */
	public void guardarMaterials(Materials materials) throws Exception {
		em.persist(materials);
	}

	/**
	 * Modify a Material in the database.
	 * 
	 * @param materials
	 *            : Materials to edit.
	 * @throws Exception
	 */
	public void editarMaterials(Materials materials) throws Exception {
		em.merge(materials);
	}

	/**
	 * Removes the BD material.
	 * 
	 * @param materials
	 *            : material to be removed
	 * @throws Exception
	 */
	public void eliminarMateriales(Materials materials) throws Exception {
		em.remove(em.merge(materials));
	}

	/**
	 * Consult a material object assigned to sending its identifier.
	 * 
	 * @param nomObject
	 *            : subject to consultation in the company.
	 * @param idMaterials
	 *            : id consultation material.
	 * @return Object related to material information.
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoMaterials(String nomObject, int idMaterials)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT m." + nomObject + " FROM Materials m "
								+ "WHERE m.id=:idMaterials")
				.setParameter("idMaterials", idMaterials).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method allow consult all materials stored in data base
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return List<Materials>: all materials stored in data base
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Materials> consultarAllMateriales() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Materials m ");
		Query q = em.createQuery(query.toString());
		return q.getResultList();
	}
}
