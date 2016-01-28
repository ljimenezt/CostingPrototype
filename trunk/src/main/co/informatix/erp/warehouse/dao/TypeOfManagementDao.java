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
	 * Edit the track information for one type of management
	 * 
	 * @param typeOfManagement
	 *            : Type of management to edit
	 * @throws Exception
	 */
	public void editarTypeOfManagement(TypeOfManagement typeOfManagement)
			throws Exception {
		em.merge(typeOfManagement);
	}

	/**
	 * Save the type of management in the database
	 * 
	 * @param typeOfManagement
	 *            : Type of management to save
	 * @throws Exception
	 */
	public void guardarTypeOfManagement(TypeOfManagement typeOfManagement)
			throws Exception {
		em.persist(typeOfManagement);
	}

	/**
	 * Removes a type of management of the BD
	 * 
	 * @param typeOfManagement
	 *            : Type of management to eliminate
	 * @throws Exception
	 */
	public void eliminarTypeOfManagement(TypeOfManagement typeOfManagement)
			throws Exception {
		em.remove(em.merge(typeOfManagement));
	}

	/**
	 * Returns the number of type names in the database management existing
	 * filtering the information by the values sent search
	 * 
	 * @param consulta
	 *            : String containing the query why the names of the types of
	 *            management filtered
	 * @param parametros
	 *            : Query parameters
	 * @return Long: Number of records name management types found
	 * @throws Exception
	 */
	public Long cantidadTypeOfManagement(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(tm) FROM TypeOfManagement tm ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the names of the types of management with a certain
	 * range sent as a parameter and filtering the information by the values
	 * sent search
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
	 * @return List<TypeOfManagement>: List of names of the types of management
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TypeOfManagement> consultarTypeOfManagement(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
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
	 * storing or editing
	 * 
	 * @param nombre
	 *            : Name the type of management to verify
	 * @param id
	 *            : id the type of management to verify
	 * @return TypeOfManagement: Object found with the search parameters name
	 *         and id
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TypeOfManagement nombreExiste(String nombre, int id)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tm FROM TypeOfManagement tm ");
		query.append("WHERE UPPER(tm.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND tm.idTypeOfManagement <>:idTypeOfManagement ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", nombre);
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
	 * Consult the types of arrangements
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<TypeOfManagement>: list of types of arrangements
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TypeOfManagement> consultarTypesOfManagements()
			throws Exception {
		return em.createQuery(
				"SELECT tm FROM TypeOfManagement tm " + "ORDER BY tm.name ")
				.getResultList();
	}

}
