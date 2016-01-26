package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizaciones.entities.Organizacion;

/**
 * Class DAO that establishes the connection between business logic and
 * database. OrganizacionAction used for the management of organizations and
 * individuals in organizations of any use
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class OrganizacionDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows organizations to consult the database
	 * 
	 * @modify Liseth.Jimenez 11/07/2012
	 * 
	 * @param start
	 *            : Number of first record where the query begins.
	 * @param range
	 *            : Number of the last record in the query.
	 * @param validityCondition
	 *            : condition to know whether existing or not existing are
	 *            queried.
	 * @param nameSearch
	 *            : Parameter name by which departments are filtered.
	 * @return List<Organizacion>: list of existing organizations in the
	 *         database, or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Organizacion> consultarOrganizaciones(int start, int range,
			String validityCondition, String nameSearch) throws Exception {
		return em
				.createQuery(
						"SELECT o FROM Organizacion o WHERE "
								+ "(UPPER(o.razonSocial) LIKE UPPER(:keyword) "
								+ "OR UPPER(o.nit) LIKE UPPER(:keyword)) "
								+ "AND o.fechaFinVigencia " + validityCondition
								+ " ORDER BY o.razonSocial ")
				.setParameter("keyword", "%" + nameSearch + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Returns the current list of organizations that exist in the database.
	 * 
	 * @return List of existing organizations in the database, or null
	 *         otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Organizacion> consultarOrganizacionesVigentes()
			throws Exception {
		return em.createQuery(
				"SELECT o FROM Organizacion o "
						+ "WHERE o.fechaFinVigencia IS NULL "
						+ "ORDER BY o.razonSocial").getResultList();
	}

	/**
	 * Allow consult the amount of existing organizations in the database
	 * 
	 * @modify Liseth.Jimenez 11/07/2012
	 * 
	 * @param condicionVigencia
	 *            : to select existing or not existing records
	 * 
	 * @param nombreBuscar
	 *            Word you want to search in the names of organizations and NIT
	 * 
	 * @return Long: number of existing organizations in the database, or null
	 *         otherwise.
	 * @throws Exception
	 */
	public Long cantidadOrganizaciones(String condicionVigencia,
			String nombreBuscar) throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(o) FROM Organizacion o WHERE "
								+ "(UPPER(o.razonSocial) LIKE UPPER(:keyword) "
								+ "OR UPPER(o.nit) LIKE UPPER(:keyword)) "
								+ "AND  o.fechaFinVigencia "
								+ condicionVigencia)
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.getSingleResult();
	}

	/**
	 * Save an organization in the database.
	 * 
	 * @param organizacion
	 *            : organization to save
	 * @throws Exception
	 */
	public void guardarOrganizacion(Organizacion organizacion) throws Exception {
		em.persist(organizacion);
	}

	/**
	 * Modifies an organization in the database.
	 * 
	 * @param organizacion
	 *            : organization to change
	 * @throws Exception
	 */
	public void editarOrganizacion(Organizacion organizacion) throws Exception {
		em.merge(organizacion);
	}

	/**
	 * Method that allows check if the value sent by parameter exists in the
	 * list of organizations database
	 * 
	 * @param valConsultar
	 *            : Value to check in the database
	 * @param cadComparar
	 *            : string that identifies what you want to consult, example:
	 *            razonSocial or NIT
	 * @return: The Organization if that value exists or null otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Organizacion consultarExiste(String valConsultar, String cadComparar)
			throws Exception {
		List<Organizacion> results = em
				.createQuery(
						"FROM Organizacion o WHERE o." + cadComparar + "=:"
								+ cadComparar)
				.setParameter(cadComparar, valConsultar).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that allows check if the value sent by parameter exists in the
	 * list of organizations database unless the parameter sent by
	 * 
	 * @param valConsultar
	 *            : Value to consult in the database
	 * @param cadComparar
	 *            : string that identifies what you want to consult, example:
	 *            razonSocial or NIT
	 * @param id
	 *            : Organization ID is excluded
	 * @return: The Organization if that value exists or null otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Organizacion consultarExisteActualizar(String valConsultar,
			String cadComparar, int id) throws Exception {
		Query query = em
				.createQuery(
						"FROM Organizacion o WHERE o." + cadComparar + "=:"
								+ cadComparar + " AND o.id <>:id")
				.setParameter(cadComparar, valConsultar).setParameter("id", id);
		List<Organizacion> results = query.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that allows consulting the organization with the information on
	 * the document type to be loaded in edition
	 * 
	 * @param idOrganizacion
	 *            : id of the organization on application
	 * @return Organizacion: Purpose of the organization of the document or null
	 *         but there.
	 * @throws Exception
	 */
	public Organizacion consultarOrganizacionConTipoDocumento(int idOrganizacion)
			throws Exception {
		return (Organizacion) em
				.createQuery(
						"SELECT o FROM Organizacion o JOIN FETCH o.tipoDocumento "
								+ "WHERE o.id=:idOrganizacion")
				.setParameter("idOrganizacion", idOrganizacion)
				.getSingleResult();
	}

}