package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.TipoUnidad;

/**
 * This class in responsible for the administration in the database of the types
 * of measurement units.
 * 
 * @author marisol.calderon
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class TipoUnidadDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method makes the record in the database a new type of unit.
	 * 
	 * @param tipoUnidad
	 *            : type of unit to be recorded in the database.
	 * 
	 * @throws Exception
	 */
	public void crearTipoUnidad(TipoUnidad tipoUnidad) throws Exception {
		em.persist(tipoUnidad);
	}

	/**
	 * This method makes the update on the type of unit in the database
	 * information system.
	 * 
	 * @param tipoUnidad
	 *            : type of unit to update.
	 * 
	 * @throws Exception
	 */
	public void modificarTipoUnidad(TipoUnidad tipoUnidad) throws Exception {
		em.merge(tipoUnidad);
	}

	/**
	 * This method makes the query types of units available in the system
	 * information.
	 * 
	 * @modify 04/10/2012 Adonay.Mantilla
	 * 
	 * @param start
	 *            : where he started the consultation record
	 * @param range
	 *            : range of the query.
	 * @param tipoVigencia
	 *            : type of effect you want by consulting the records.
	 * @param consulta
	 *            : the query string.
	 * @param parametros
	 *            : query parameters.
	 * @return List<TipoUnidad>: list of types of units.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TipoUnidad> consultarTipoUnidades(int start, int range,
			int tipoVigencia, StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		if (tipoVigencia == 1) {
			StringBuilder query = new StringBuilder();
			query.append("FROM TipoUnidad tu ");
			query.append("WHERE tu.fechaFinVigencia > :fecha ");
			query.append("OR tu.fechaFinVigencia = null ");
			query.append(consulta);
			query.append(" ORDER BY tu.nombre");
			Query q = em.createQuery(query.toString());
			for (SelectItem parametro : parametros) {
				q.setParameter(parametro.getLabel(), parametro.getValue());
			}
			q.setParameter("fecha", new Date()).setFirstResult(start)
					.setMaxResults(range);
			return q.getResultList();

		} else {
			StringBuilder query = new StringBuilder();
			query.append("FROM TipoUnidad tu ");
			query.append("WHERE tu.fechaFinVigencia <= :fecha ");
			query.append("OR tu.fechaFinVigencia = null ");
			query.append(consulta);
			query.append(" ORDER BY tu.nombre");
			Query q = em.createQuery(query.toString());
			for (SelectItem parametro : parametros) {
				q.setParameter(parametro.getLabel(), parametro.getValue());
			}
			q.setParameter("fecha", new Date()).setFirstResult(start)
					.setMaxResults(range);
			return q.getResultList();
		}
	}

	/**
	 * Method for querying current drive types
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @return: List the types of units that exist in the current database or
	 *          null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TipoUnidad> consultarTipoUnidadesVigentes() throws Exception {
		return em.createQuery(
				"SELECT tu FROM TipoUnidad tu "
						+ "WHERE tu.fechaFinVigencia IS NULL "
						+ "ORDER BY tu.nombre").getResultList();
	}

	/**
	 * Method to check the number of the types of unit of measure.
	 * 
	 * @param tipoVigencia
	 *            : current for which records are consulted.
	 * @param consulta
	 *            : query to perform the query.
	 * @param parametros
	 *            : query parameters
	 * @return many types of existing unit in the database.
	 * @throws Exception
	 */
	public Long cantidadTipoUnidad(int tipoVigencia, StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		if (tipoVigencia == 1) {
			StringBuilder query = new StringBuilder();
			query.append("SELECT COUNT(tu) FROM TipoUnidad tu ");
			query.append("WHERE tu.fechaFinVigencia > :fecha ");
			query.append("OR tu.fechaFinVigencia = null ");
			query.append(consulta);
			Query q = em.createQuery(query.toString());
			for (SelectItem parametro : parametros) {
				q.setParameter(parametro.getLabel(), parametro.getValue());
			}
			q.setParameter("fecha", new Date());
			return (Long) q.getSingleResult();
		} else {
			StringBuilder query = new StringBuilder();
			query.append("SELECT COUNT(tu) FROM TipoUnidad tu ");
			query.append("WHERE tu.fechaFinVigencia <= :fecha ");
			query.append("OR tu.fechaFinVigencia = null ");
			query.append(consulta);
			Query q = em.createQuery(query.toString());
			for (SelectItem parametro : parametros) {
				q.setParameter(parametro.getLabel(), parametro.getValue());
			}
			q.setParameter("fecha", new Date());
			return (Long) q.getSingleResult();
		}
	}

	/**
	 * Method to check if the name of the type of measurement unit already
	 * exists, when the record be edited
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param nombre
	 *            : name to consult
	 * @param id
	 *            : id the type of unit of measure
	 * @return: Unit object type if you find a record or null otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TipoUnidad nombreExisteTipoUnidadActualizar(String nombre, int id)
			throws Exception {
		List<TipoUnidad> results = em
				.createQuery(
						"SELECT i FROM TipoUnidad i "
								+ "WHERE UPPER (i.nombre) = UPPER (:nombre) "
								+ "AND i.id != :id")
				.setParameter("nombre", nombre).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		} else
			return null;
	}

	/**
	 * Method that allows check if the name of the type of measurement unit
	 * already exists
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param nombre
	 *            : name check
	 * @return: object unitType if found a record or null otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TipoUnidad nombreTipoUnidadExiste(String nombre) throws Exception {
		List<TipoUnidad> results = em
				.createQuery(
						"SELECT d FROM TipoUnidad d "
								+ "WHERE  UPPER (d.nombre)=  UPPER (:nombre)")
				.setParameter("nombre", nombre).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		} else
			return null;
	}

}