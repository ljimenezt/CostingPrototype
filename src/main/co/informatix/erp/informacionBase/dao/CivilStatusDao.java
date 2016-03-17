package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.CivilStatus;

/**
 * DAO class that establishes the connection between business logic and base
 * data. EstadoCivilAction used for managing civil status
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class CivilStatusDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for a civil state
	 * 
	 * @param civilStatus
	 *            : Civil status to edit
	 * @throws Exception
	 */
	public void editarEstadoCivil(CivilStatus civilStatus) throws Exception {
		em.merge(civilStatus);
	}

	/**
	 * Save civil status in the database
	 * 
	 * @param civilStatus
	 *            : Civil status to save
	 * @throws Exception
	 */
	public void guardarEstadoCivil(CivilStatus civilStatus) throws Exception {
		em.persist(civilStatus);
	}

	/**
	 * Removes a civil state of the BD
	 * 
	 * @param civilStatus
	 *            : Civil status to delete
	 * @throws Exception
	 */
	public void eliminarEstadoCivil(CivilStatus civilStatus) throws Exception {
		em.remove(em.merge(civilStatus));
	}

	/**
	 * Returns the number of existing civil states in the BD filtering
	 * information search by the values sent
	 * 
	 * @param consulta
	 *            : String containing the query why the filter names of civil
	 *            states
	 * @param parametros
	 *            : Query parameters
	 * @return Long: Number of civil status records found
	 * @throws Exception
	 */
	public Long cantidadEstadoCivil(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(cs) FROM CivilStatus cs ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation with civil states sent a certain range as a
	 * parameter and filtering the information by the values of search sent
	 * 
	 * @param inicio
	 *            : Registry where consultation begins
	 * @param rango
	 *            : Range of records
	 * @param consulta
	 *            : Consultation records the parameters depending selected by
	 *            the user
	 * @param parametros
	 *            : Query parameters
	 * @return List<CivilStatus>: List of civil status
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CivilStatus> consultarEstadoCivil(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cs FROM CivilStatus cs ");
		query.append(consulta);
		query.append("ORDER BY cs.nombre ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<CivilStatus> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation if the name of civil states exist in database when saving or
	 * editing
	 * 
	 * @param nombre
	 *            : Name of civil status to check
	 * @param id
	 *            : id to verify the civil status
	 * @return CivilStatus: Object found with the search parameters id and name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CivilStatus nombreExiste(String nombre, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cs FROM CivilStatus cs ");
		query.append("WHERE UPPER(cs.nombre)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND cs.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", nombre);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<CivilStatus> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}
