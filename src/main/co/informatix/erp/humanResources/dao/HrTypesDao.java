package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.HrTypes;

/**
 * DAO class that establishes the connection between business logic and base
 * data. HrTypesAction used for managing HrTypes
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class HrTypesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for one type of human resources.
	 * 
	 * @param hrTypes
	 *            : Human resource type to edit.
	 * @throws Exception
	 */
	public void editarHrTypes(HrTypes hrTypes) throws Exception {
		em.merge(hrTypes);
	}

	/**
	 * Save the kind of human resources in the database
	 * 
	 * @param hrTypes
	 *            : Type of human resources to keep
	 * @throws Exception
	 */
	public void guardarHrTypes(HrTypes hrTypes) throws Exception {
		em.persist(hrTypes);
	}

	/**
	 * Removes a type of human resource BD
	 * 
	 * @param hrTypes
	 *            : Type of human resources to eliminate
	 * @throws Exception
	 */
	public void eliminarHrTypes(HrTypes hrTypes) throws Exception {
		em.remove(em.merge(hrTypes));
	}

	/**
	 * Returns the number of type names existing human resources BD filtering
	 * the information by the values sent search.
	 * 
	 * @param consulta
	 *            : String containing the query why the filter names types of
	 *            human resources.
	 * @param parametros
	 *            : query parameters.
	 * @return Long: amount of name records found of resource human types
	 * @throws Exception
	 */
	public Long cantidadHrTypes(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ht) FROM HrTypes ht ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method see the names of the types of human resources with
	 * determinate sent as a parameter range and filtering the information, Sent
	 * search values.
	 * 
	 * @param inicio
	 *            : where it initiates the query record
	 * @param rango
	 *            : range of records
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : query parameters.
	 * @return List<HrTypes>: list of the names of the types of resources
	 *         humans.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HrTypes> consultarHrTypes(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ht FROM HrTypes ht ");
		query.append(consulta);
		query.append("ORDER BY ht.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<HrTypes> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult whether the type name exists in the human resource base data when
	 * storing or editing.
	 * 
	 * @param nombre
	 *            : type name to verify human resources
	 * @param id
	 *            : id the type of human resources to verify
	 * @return HrTypes: hrTypes object found with the search parameters id and
	 *         name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HrTypes nombreExiste(String nombre, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ht FROM HrTypes ht ");
		query.append("WHERE UPPER(ht.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND ht.idHrType <>:idHrType ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", nombre);
		if (id != 0) {
			q.setParameter("idHrType", id);
		}
		List<HrTypes> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult the types of human resources that are in force.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @return List<HrTypes>: List of human resources types of existing found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HrTypes> consultarHrTypesVigentes() throws Exception {
		return em.createQuery("SELECT ht FROM HrTypes ht ORDER BY ht.name")
				.getResultList();
	}

}
