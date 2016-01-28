package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.EstadoCivil;

/**
 * DAO class that establishes the connection between business logic and base
 * data. EstadoCivilAction used for the management of the United civilians.
 * 
 * @author Gerson.Cespedes
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class EstadoCivilDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Returns the number of existing civil status in the database that are
	 * force.
	 * 
	 * @param condicionVigencia
	 *            : Option to count existing or not existing registrations.
	 * @param nombreBuscar
	 *            : Parameter entered by keyboard to search the name of civil
	 *            status.
	 * @return Long: number of civil status found in the database.
	 * @throws Exception
	 */
	public Long cantidadEstadosCiviles(String condicionVigencia,
			String nombreBuscar) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ec) FROM EstadoCivil ec ");
		query.append("WHERE ec.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(ec.nombre) LIKE UPPER(:keyword) ");
		}
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * 
	 * Provides access existing civil status in the database, in relation to the
	 * condition of validity sent.
	 * 
	 * @param inicio
	 *            : Where he started the consultation record.
	 * @param rango
	 *            : Maximum number of records in the query.
	 * @param condicionVigencia
	 *            : Option to display current or existing registrations.
	 * @param nombreBuscar
	 *            : Parameter entered by keyboard to search the name of civil
	 *            status.
	 * @return List<EstadoCivil>: List of civil states that meet the condition
	 *         of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> consultarEstadosCiviles(int inicio, int rango,
			String condicionVigencia, String nombreBuscar) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ec FROM EstadoCivil ec ");
		query.append("WHERE ec.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(ec.nombre) LIKE UPPER(:keyword) ");
		}
		query.append(" ORDER BY ec.nombre");
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		return q.getResultList();
	}

	/**
	 * Edit the a record information for a civil state.
	 * 
	 * @param estadoCivil
	 *            : Civil state to edit
	 * @throws Exception
	 */
	public void editarEstadoCivil(EstadoCivil estadoCivil) throws Exception {
		em.merge(estadoCivil);
	}

	/**
	 * Consultation if the name of civil status exist in the database at the
	 * time of editing.
	 * 
	 * @param nombre
	 *            : Name civil status checked.
	 * @param id
	 *            : civil status identifier.
	 * @return EstadoCivil:civil status object found with the name, or null
	 *         otherwise.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public EstadoCivil nombreExisteActualizar(String nombre, int id)
			throws Exception {
		List<EstadoCivil> results = em
				.createQuery(
						"FROM EstadoCivil WHERE nombre=:nombre AND id<>:id")
				.setParameter("nombre", nombre).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consultation if the name of civil status exist in the database when you
	 * save it.
	 * 
	 * @param nombre
	 *            : Name civil status checked.
	 * @return EstadoCivil: civil status object found with the name, or null
	 *         otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public EstadoCivil nombreExiste(String nombre) throws Exception {
		List<EstadoCivil> results = em
				.createQuery("FROM EstadoCivil WHERE nombre=:nombre")
				.setParameter("nombre", nombre).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Save civil status in the database.
	 * 
	 * @param estadoCivil
	 *            : civil status to save
	 * @throws Exception
	 */
	public void guardarEstadoCivil(EstadoCivil estadoCivil) throws Exception {
		em.persist(estadoCivil);
	}

	/**
	 * Consultation civilians states that are in currently.
	 * 
	 * @return List<EstadoCivil>: List of current civil status found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EstadoCivil> consultarEstadosCivilesVigentes() throws Exception {
		return em.createQuery(
				"SELECT ec FROM EstadoCivil ec "
						+ "WHERE ec.fechaFinVigencia IS NULL "
						+ "ORDER BY ec.nombre").getResultList();
	}

}
