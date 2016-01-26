package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.security.entities.Metodo;

/**
 * DAO class that establishes the connection between business logic and
 * database. MetodoAction used for management methods that have specific
 * permissions on the system
 * 
 * @author marisol.calderon
 * @modify 19/06/2014 Gabriel.Moreno
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MetodoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows to consult methods in the database
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param nameSearch
	 *            : Which name by the method is to consult.
	 * @return List<Metodo>: list of methods found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Metodo> consultarMetodos(int start, int range, String nameSearch)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Metodo m ");
		query.append("WHERE m.fechaFinVigencia IS NULL ");
		if (nameSearch != null && !"".equals(nameSearch)) {
			query.append(" AND UPPER(m.nombre) LIKE UPPER(:keyword) ");
		}
		query.append(" ORDER BY m.nombre");

		Query q = em.createQuery(query.toString());
		if (nameSearch != null && !"".equals(nameSearch)) {
			q.setParameter("keyword", "%" + nameSearch + "%");
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();

	}

	/**
	 * Allows consult the amount of existing methods in the database
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param nombreBuscar
	 *            : name by which the method is to consult.
	 * 
	 * @return Long: number of methods found in the database.
	 * @throws Exception
	 */
	public Long cantidadMetodos(String nombreBuscar) throws Exception {
		StringBuilder query = new StringBuilder();

		query.append("SELECT COUNT(m) FROM Metodo m");
		query.append(" WHERE m.fechaFinVigencia IS NULL ");
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(m.nombre) LIKE UPPER(:keyword) ");
		}

		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		return (Long) q.getSingleResult();

	}

	/**
	 * Save a method in the database.
	 * 
	 * @param metodo
	 *            : method to save
	 * @throws Exception
	 */
	public void guardarMetodo(Metodo metodo) throws Exception {
		em.persist(metodo);
	}

	/**
	 * Modifies a method in the database.
	 * 
	 * @param metodo
	 *            : method to modify
	 * @throws Exception
	 */
	public void editarMetodo(Metodo metodo) throws Exception {
		em.merge(metodo);
	}

	/**
	 * Check if the method name exists in the database when saved.
	 * 
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param nombre
	 *            : Name of the method to find.
	 * @param id
	 *            : Method identifier edition.
	 * 
	 * @return Metodo: method found with the name method, otherwise null.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Metodo nombreMetodoExiste(String nombre, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Metodo m ");
		query.append("WHERE UPPER(m.nombre)=UPPER(:nombre) ");
		query.append(" AND m.fechaFinVigencia IS NULL ");
		if (id != 0) {
			query.append("AND m.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("nombre", nombre);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<Metodo> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Check if the name of the action in the method exists in the database when
	 * you save it.
	 * 
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param nombreMetodo
	 *            : Action name of the method to find.
	 * @param id
	 *            : ID of the method at issue.
	 * 
	 * @return Metodo: method method met the action name, null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Metodo nombreActionExiste(String nombreMetodo, int id)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Metodo m ");
		query.append("WHERE UPPER(m.nombreMetodo)=UPPER(:nombreMetodo) ");
		query.append(" AND m.fechaFinVigencia IS NULL ");
		if (id != 0) {
			query.append("AND m.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("nombreMetodo", nombreMetodo);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<Metodo> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}