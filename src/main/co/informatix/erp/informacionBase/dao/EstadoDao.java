package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.Estado;

/**
 * DAO class that establishes the connection between business logic and
 * database. EstadoAction used for managing the states of processes.
 * 
 * 
 * @author Gerson.Cespedes
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class EstadoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Returns the number of existing states in the database that are in force.
	 * 
	 * @param condicionVigencia
	 *            : Option to count current or not current registrations.
	 * @param nombreBuscar
	 *            : Parameter entered by keyboard to search the name of the
	 *            state.
	 * @return Long: Number of states found in the database.
	 * @throws Exception
	 */
	public Long cantidadEstados(String condicionVigencia, String nombreBuscar)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(e) FROM Estado e ");
		query.append("WHERE e.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(e.nombre) LIKE UPPER(:keyword) ");
		}
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}

		return (Long) q.getSingleResult();
	}

	/**
	 * Allows consulting existing states in the database, in relation to the
	 * condition of validity sent.
	 * 
	 * @param start
	 *            : Where it initiates the consultation record.
	 * @param range
	 *            : Maximum number of records in the query.
	 * @param condicionVigencia
	 *            : Option to display current or not current registrations.
	 * @param nombreBuscar
	 *            : Parameter entered by keyboard to search the name of the
	 *            state.
	 * @return List<Estado>: List of states have with the condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Estado> consultarEstados(int start, int range,
			String condicionVigencia, String nombreBuscar) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT e FROM Estado e ");
		query.append("WHERE e.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(e.nombre) LIKE UPPER(:keyword) ");
		}
		query.append(" ORDER BY e.nombre");
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Edit the track information for a state.
	 * 
	 * @param estado
	 *            : Status Object to be edited.
	 * @throws Exception
	 */
	public void editarEstado(Estado estado) throws Exception {
		em.merge(estado);
	}

	/**
	 * Consultation If the state name exists in the database at the time of
	 * editing.
	 * 
	 * @param nombre
	 *            : State name to verify.
	 * @param id
	 *            : State identifier.
	 * @return Estado: Status object found with the name or null otherwise.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Estado nombreExisteActualizar(String nombre, int id)
			throws Exception {
		List<Estado> results = em
				.createQuery("FROM Estado WHERE nombre=:nombre AND id<>:id")
				.setParameter("nombre", nombre).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consultation If the state name exists in the database when you save it.
	 * 
	 * @param nombre
	 *            : Name of State to verify
	 * @return Estado: Status object found with the name or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Estado nombreExiste(String nombre) throws Exception {
		List<Estado> results = em
				.createQuery("FROM Estado WHERE nombre=:nombre")
				.setParameter("nombre", nombre).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Save the states in the database.
	 * 
	 * @param estado
	 *            : State object to save
	 * @throws Exception
	 */
	public void guardarEstado(Estado estado) throws Exception {
		em.persist(estado);
	}

}
