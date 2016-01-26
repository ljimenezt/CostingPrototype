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
 * This class in charge of the administration in the database of the types of
 * measurement units.
 * 
 * @author marisol.calderon
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class TipoUnidadMedidaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method makes the record in the database a new type of unit.
	 * 
	 * @param tipoUnidad
	 *            : Unit Type to be recorded in the database.
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
	 *            : Unit type to update.
	 * 
	 * @throws Exception
	 */
	public void modificarTipoUnidad(TipoUnidad tipoUnidad) throws Exception {
		em.merge(tipoUnidad);
	}

	/**
	 * This method makes the query types of units available in the information
	 * system.
	 * 
	 * @modify 04/10/2012 Adonay.Mantilla
	 * 
	 * @param inicio
	 *            : Record where the query begins.
	 * @param rango
	 *            : Range of the query.
	 * @param tipoVigencia
	 *            : Type of effect by consulting the records.
	 * @param consulta
	 *            : Consult the logs depending on the parameters selected by the
	 *            user.
	 * @param parametros
	 *            : Query parameters.
	 * @return List<TipoUnidad>: List of types of units.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TipoUnidad> consultarTipoUnidades(int inicio, int rango,
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
			q.setParameter("fecha", new Date()).setFirstResult(inicio)
					.setMaxResults(rango);
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
			q.setParameter("fecha", new Date()).setFirstResult(inicio)
					.setMaxResults(rango);
			return q.getResultList();
		}
	}

	/**
	 * Method for querying current drive types.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @return List<TipoUnidad>: List the types of units that exist in the
	 *         current database or null otherwise.
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
	 *            : Type of effect you want by consulting the records.
	 * @param consulta
	 *            : The query string.
	 * @param parametros
	 *            : Query parameters.
	 * @return Long: Amount types of unit existing in the database.
	 * 
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
	 * exists, when the record be edited.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param nombre
	 *            : Name to consult.
	 * @param id
	 *            : Identifier of type unit of measure.
	 * @return TipoUnidad: Object of TipoUnidad if you find a record or null
	 *         otherwise.
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
	 * Method to check if the name of the type of measurement unit already
	 * exists.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param nombre
	 *            : Name to consult.
	 * @return TipoUnidad: Object of TipoUnidad if you find a record or null
	 *         otherwise.
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

	/**
	 * 
	 * Method that search a unit type by name.
	 * 
	 * @author Oscar.Amaya
	 * 
	 * @param nombre
	 *            : Name of unit type measure.
	 * @return TipoUnidad: Unit type found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TipoUnidad buscarTipoUnidadNombre(String nombre) throws Exception {
		List<TipoUnidad> results = em
				.createQuery("FROM TipoUnidad WHERE nombre=:nombre")
				.setParameter("nombre", nombre).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that search a unit type by Id.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @param idTipoUnidad
	 *            : Identifier of unit type.
	 * 
	 * @return TipoUnidad: Object of TipoUnidad if you find a record or null
	 *         otherwise.
	 * @throws Exception
	 */
	public TipoUnidad tipoUnidadXId(int idTipoUnidad) throws Exception {
		return em.find(TipoUnidad.class, idTipoUnidad);
	}
}