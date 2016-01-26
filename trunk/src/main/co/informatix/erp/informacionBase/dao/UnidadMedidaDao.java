package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.TipoUnidad;
import co.informatix.erp.informacionBase.entities.UnidadMedida;

/**
 * This class in responsible for the administration in the database of
 * measurement units, their classification and their respective conversion to
 * themselves.
 * 
 * @author Angelica Amaya
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class UnidadMedidaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method makes the record in the database of a new unit of measurement
	 * 
	 * @param unidadMedida
	 *            : measurement unit to record in the database.
	 * 
	 * @throws Exception
	 */
	public void agregarUnidadMedida(UnidadMedida unidadMedida) throws Exception {
		em.persist(unidadMedida);
	}

	/**
	 * Edit a unit of measurement in the database.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param unidadMedida
	 *            : Unit of measure to edit the database.
	 * @throws Exception
	 */
	public void editarUnidadMedida(UnidadMedida unidadMedida) throws Exception {
		em.merge(unidadMedida);
	}

	/**
	 * Method for querying the unit of measure by identifier.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param identificadorUnidad
	 *            : id of the unit of measure on request.
	 * @return object measurement unit with the information or null if not
	 *         exist.
	 * @throws Exception
	 */
	public UnidadMedida consultarUnidadMedida(int identificadorUnidad)
			throws Exception {
		return em.find(UnidadMedida.class, identificadorUnidad);
	}

	/**
	 * Method that allows consult the units of measure in the database. If the
	 * drive type is not null consultation units by unit type as a parameter
	 * sent
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * @modify 04/10/2012 Adonay.Mantilla
	 * 
	 * @param start
	 *            : Where it initiates the consultation record.
	 * @param range
	 *            : Maximum number of records in the query.
	 * @param condicionVigencia
	 *            : Option to display current or not current registrations.
	 * @param tipoUnidad
	 *            : object type measurement unit
	 * @param nombreBuscar
	 *            : name by which measurement units are consulted.
	 * @param idTipoUnidadMedida
	 *            : id the type of unit for which measurement units are
	 *            consulted.
	 * @return List<UnidadMedida>: list of measurement units.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UnidadMedida> consultarUnidadesMedida(int start, int range,
			StringBuilder condicionVigencia, TipoUnidad tipoUnidad,
			String nombreBuscar, Integer idTipoUnidadMedida) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT um FROM UnidadMedida um ");
		query.append("JOIN FETCH um.tipoUnidad ");
		query.append("WHERE um.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(um.nombre) LIKE UPPER(:keyword) ");
		}
		if (idTipoUnidadMedida != 0) {
			query.append(" AND um.tipoUnidad.id=:idTipoUnidadMedida ");
		}
		query.append("ORDER BY um.tipoUnidad.nombre, um.nombre ");
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		if (idTipoUnidadMedida != 0) {
			q.setParameter("idTipoUnidadMedida", idTipoUnidadMedida);
		}
		q.setFirstResult(start).setMaxResults(range);
		if (tipoUnidad != null) {
			q.setParameter("tipoUnidad", tipoUnidad);
		}
		return q.getResultList();
	}

	/**
	 * Method that allows to consult the amount of units of measurement in the
	 * database. If the drive type is not null consultation units by unit type
	 * as a parameter sent
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * @modify 04/10/2012 Adonay.Mantilla
	 * 
	 * @param condicionVigencia
	 *            : To select existing or not existing registrations or with a
	 *            specific type of unit
	 * @param tipoUnidad
	 *            : object type measurement unit by which you want to filter the
	 *            query.
	 * @param nombreBuscar
	 *            : name by which measurement units are consulted.
	 * @param idTipoUnidadMedida
	 *            : id the type of unit for which measurement units are
	 *            consulted.
	 * @return Long: Number of units of measurement records in the database.
	 * @throws Exception
	 */
	public Long cantidadUnidadesMedida(StringBuilder condicionVigencia,
			TipoUnidad tipoUnidad, String nombreBuscar,
			Integer idTipoUnidadMedida) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(um) FROM UnidadMedida um ");
		query.append(" WHERE  um.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(um.nombre) LIKE UPPER(:keyword) ");
		}
		if (idTipoUnidadMedida != 0) {
			query.append(" AND um.tipoUnidad.id=:idTipoUnidadMedida ");
		}
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		if (idTipoUnidadMedida != 0) {
			q.setParameter("idTipoUnidadMedida", idTipoUnidadMedida);
		}
		if (tipoUnidad != null) {
			q.setParameter("tipoUnidad", tipoUnidad);
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method makes the query measurement units available in the
	 * information system
	 * 
	 * @return List<UnidadMedida>: list of measurement units.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UnidadMedida> consultarUnidadesMedidas() throws Exception {
		return em
				.createQuery(
						"FROM UnidadMedida u "
								+ "WHERE u.fechaFinVigencia < :fecha "
								+ "OR u.fechaFinVigencia IS NULL")
				.setParameter("fecha", new Date()).getResultList();
	}

	/**
	 * Consultation if the name of the unit of measurement exists in the
	 * database for the type selected when editing unit.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param nombre
	 *            : name that is registered in the unit of measurement
	 * @param id
	 *            : id of the unit of measurement to edit
	 * @param idTipoUnidad
	 *            : id type selected unit
	 * @return UnidadMedida: If a record exists with that name, or null
	 *         otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public UnidadMedida nombreExisteUnidadActualizar(String nombre, int id,
			int idTipoUnidad) throws Exception {
		List<UnidadMedida> results = em
				.createQuery(
						"SELECT um FROM UnidadMedida um "
								+ "WHERE um.nombre=:nombre "
								+ "AND um.tipoUnidad.id=:idTipoUnidad "
								+ "AND um.id <> :id")
				.setParameter("nombre", nombre).setParameter("id", id)
				.setParameter("idTipoUnidad", idTipoUnidad).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that allows consult if the unit of measure already exists for the
	 * unit type selected when creating the record
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param nombre
	 *            : name of the unit to consult
	 * @param idTipoUnidad
	 *            : id type selected measuring unit
	 * @return UnidadMedida: If a record exists with that name, or null
	 *         otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public UnidadMedida nombreUnidadExiste(String nombre, int idTipoUnidad)
			throws Exception {
		List<UnidadMedida> results = em
				.createQuery(
						"SELECT um FROM UnidadMedida um "
								+ "WHERE um.nombre=:nombre "
								+ "AND um.tipoUnidad.id=:idTipoUnidad")
				.setParameter("nombre", nombre)
				.setParameter("idTipoUnidad", idTipoUnidad).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		} else
			return null;
	}

	/**
	 * Query whether the abbreviated name of the unit of measurement exists in
	 * the database for the type selected when editing unit.
	 * 
	 * @author marisol.calderon
	 * @modify 21/12/2011 Gabriel.Moreno
	 * 
	 * @param abreviatura
	 *            : short name that is registered in the unit of measurement.
	 * @param id
	 *            : id of the unit of measure to be edited.
	 * @param idTipoUnidad
	 *            : id type selected unit.
	 * @return If there is a unit of measure that name or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public UnidadMedida nombreExisteAbreviaturaActualizar(String abreviatura,
			int id, int idTipoUnidad) throws Exception {
		List<UnidadMedida> results = em
				.createQuery(
						"SELECT um FROM UnidadMedida um "
								+ "WHERE UPPER (um.abreviatura) = UPPER (:abreviatura) "
								+ "AND um.tipoUnidad.id=:idTipoUnidad "
								+ "AND um.id <> :id")
				.setParameter("abreviatura", abreviatura)
				.setParameter("id", id)
				.setParameter("idTipoUnidad", idTipoUnidad).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that allows consult if the abbreviation of the measurement unit
	 * already exists for the type selected when creating the record unit.
	 * 
	 * @author marisol.calderon
	 * @modify 21/12/2011 Gabriel.Moreno
	 * 
	 * @param abreviatura
	 *            : short name for the unit.
	 * @param idTipoUnidad
	 *            : id type selected measuring unit.
	 * @return If there is a unit of measure that name or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public UnidadMedida nombreAbreviaturaExiste(String abreviatura,
			int idTipoUnidad) throws Exception {
		List<UnidadMedida> results = em
				.createQuery(
						"SELECT um FROM UnidadMedida um "
								+ "WHERE UPPER (um.abreviatura) = UPPER (:abreviatura) "
								+ "AND um.tipoUnidad.id=:idTipoUnidad")
				.setParameter("abreviatura", abreviatura)
				.setParameter("idTipoUnidad", idTipoUnidad).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		} else
			return null;
	}

	/**
	 * This method makes querying the measurement units belonging to another
	 * given the same primary key.
	 * 
	 * @param indicador
	 *            : primary key of a unit of measurement.
	 * 
	 * @return List<UnidadMedida>: list of measurement units.s
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UnidadMedida> consultarUnidadesSimilares(int indicador)
			throws Exception {
		UnidadMedida unidad = em.find(UnidadMedida.class, indicador);
		String jpql = "SELECT u FROM UnidadMedida u "
				+ "WHERE u.tipoUnidad = :tu "
				+ "AND u NOT IN(SELECT c.llavePrimaria.unidadFinal "
				+ "FROM ConversionUnidad c	"
				+ "WHERE c.llavePrimaria.unidadInicial = :unidad)";
		Query query = em.createQuery(jpql).setParameter("unidad", unidad)
				.setParameter("tu", unidad.getTipoUnidad());
		List<UnidadMedida> unidadesMedidasEquivalentes = query.getResultList();
		return unidadesMedidasEquivalentes;
	}

	/**
	 * Provides access current measurement units in the database.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param nombreTipoUnidad
	 *            : Name the type of unit.
	 * @return List<UnidadMedida>: list of measurement units.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UnidadMedida> consultarUnidadesMedidaVigentes(
			String nombreTipoUnidad) throws Exception {
		return em
				.createQuery(
						"SELECT um FROM UnidadMedida um JOIN um.tipoUnidad tu WHERE "
								+ "tu.nombre=:nombreTipoUnidad "
								+ "AND um.fechaFinVigencia IS NULL "
								+ "ORDER BY um.nombre ")
				.setParameter("nombreTipoUnidad", nombreTipoUnidad)
				.getResultList();
	}

}