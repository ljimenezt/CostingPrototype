package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizaciones.entities.Empresa;

/**
 * Class DAO that establishes the connection between business logic and database
 * to manage the Company entity.
 * 
 * @author Oscar.Amaya
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class EmpresaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consults the amount of firms depending on the condition of
	 * validity
	 * 
	 * @modify: Gerson.Cespedes 23/05/2012
	 * @modify: Adonay.Mantilla 11/10/2012
	 * 
	 * @param condicionVigencia
	 *            : condition that allows whether existing or not existing are
	 *            queried.
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : parameters for the query.
	 * @return Long: number of existing companies in the database.
	 * @throws Exception
	 */
	public Long cantidadEmpresas(String condicionVigencia,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(e) FROM Empresa e WHERE e.fechaFinVigencia ");
		query.append(condicionVigencia);
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries a range of companies depending on the condition of
	 * validity
	 * 
	 * @modify: Gerson.Cespedes 23/05/2012
	 * @modify: Adonay.Mantilla 11/10/2012
	 * 
	 * @param start
	 *            : Number of first record where the query begins.
	 * @param range
	 *            : Number of the last record in the query.
	 * @param condicionVigencia
	 *            : condition to know whether existing or not existing are
	 *            queried.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : parameters for the query.
	 * @return List<Empresa>: Companies found or null if not present.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Empresa> consultarEmpresas(int start, int range,
			String condicionVigencia, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT e FROM Empresa e WHERE e.fechaFinVigencia ");
		query.append(condicionVigencia);
		query.append(consult);
		query.append(" ORDER BY e.nombre");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Check whether the NIT of a company already exists
	 * 
	 * @param nit
	 *            : NIT-check
	 * @param id
	 *            : id of the company to compare
	 * @return boolean to true if there is a NIT or false otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean nitExiste(String nit, int id) throws Exception {
		List<Empresa> results = em
				.createQuery("FROM Empresa WHERE nit=:nit AND id <>:id")
				.setParameter("nit", nit).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * allows you to create a business database
	 * 
	 * @param empresa
	 *            : company store
	 * @throws Exception
	 */
	public void crearEmpresa(Empresa empresa) throws Exception {
		em.persist(empresa);
	}

	/**
	 * Allows a company to modify the database
	 * 
	 * @param empresa
	 *            : company to edit the database.
	 * @throws Exception
	 */
	public void modificarEmpresa(Empresa empresa) throws Exception {
		em.merge(empresa);
	}

	/**
	 * See also article assigned to a company, considering that they are only
	 * those that are not null in the table
	 * 
	 * @modify 01/02/2012 marisol.calderon
	 * 
	 * @param nomObject
	 *            : in order to consult the company
	 * @param idEmpresa
	 *            : Company ID being queried
	 * @return Object information associated with the company or null but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoEmpresa(String nomObject, int idEmpresa)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT e." + nomObject
								+ " FROM Empresa e WHERE e.id=:idEmpresa")
				.setParameter("idEmpresa", idEmpresa).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}

	/**
	 * Method that allows a list of objects depending on the condition sent as a
	 * parameter
	 * 
	 * @author marisol.calderon
	 * 
	 * @param nomObject
	 *            : name of the object to be found on the company
	 * @param idEmpresa
	 *            : id of the company to consult
	 * @return List of Objects with information.
	 * @throws Exception
	 */
	public List<?> consultarListaObjetosDeEmpresa(String nomObject,
			int idEmpresa) throws Exception {
		return em
				.createQuery(
						"SELECT em." + nomObject
								+ " FROM Empresa em WHERE em.id=:idEmpresa")
				.setParameter("idEmpresa", idEmpresa).getResultList();
	}

	/**
	 * Returns the current list of companies that exist in the database
	 * 
	 * @author marisol.calderon
	 * 
	 * @return List of existing businesses.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Empresa> consultarEmpresasVigentes() throws Exception {
		return em.createQuery(
				"SELECT e FROM Empresa e "
						+ "WHERE e.fechaFinVigencia IS NULL "
						+ "ORDER BY e.nombre").getResultList();
	}

	/**
	 * Method that allows check the number of companies that have estates
	 * 
	 * @author marisol.calderon
	 * 
	 * @param condicionVigencia
	 *            : condition to know whether existing or not existing records
	 *            are queried
	 * @return: Long with the number of companies with estates.
	 * @throws Exception
	 */
	public Long cantidadEmpresasConHaciendas(String condicionVigencia)
			throws Exception {
		return (Long) em.createQuery(
				"SELECT COUNT(DISTINCT em) FROM Empresa em "
						+ "JOIN em.haciendas WHERE em.fechaFinVigencia "
						+ condicionVigencia).getSingleResult();
	}

	/**
	 * Method for consulting companies that have farms depending on the
	 * beginning and range.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param start
	 *            : Number of first record where the query begins.
	 * @param range
	 *            : Number of the last record in the query.
	 * @param validityCondition
	 *            : condition to know whether existing or not existing are
	 *            queried.
	 * @return: list of companies with estates.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Empresa> consultarEmpresasConHaciendas(int start, int range,
			String validityCondition) throws Exception {
		return em
				.createQuery(
						"SELECT DISTINCT em FROM Empresa em JOIN FETCH em.haciendas "
								+ "WHERE em.fechaFinVigencia "
								+ validityCondition + " ORDER BY em.nombre")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Allows to consult companies with its name or the word NIT sent as a
	 * parameter.
	 * 
	 * @author Gerson.Cespedes
	 * @modify 25/03/2012 marisol.calderon
	 * 
	 * @param nombreBuscar
	 *            : The word you want to search the names of the companies or
	 *            NIT.
	 * @return List of companies found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Empresa> buscarEmpresaXNombreONit(String nombreBuscar)
			throws Exception {
		return em
				.createQuery(
						"SELECT DISTINCT(e) FROM Empresa e "
								+ "WHERE UPPER(e.nombre) LIKE UPPER(:keyword) "
								+ "OR UPPER(e.nit) LIKE UPPER(:keyword) "
								+ "AND e.fechaFinVigencia IS NULL "
								+ "ORDER BY e.nombre, e.nit")
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.getResultList();
	}

	/**
	 * Consult a company by identifier.
	 * 
	 * @modify Liseth.Jimenez 02/15/2012
	 * 
	 * @param idEmpresa
	 *            : identifier of the company that wants to consult.
	 * @return Empresa: purpose of the company whether or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Empresa obtenerEmpresa(int idEmpresa) throws Exception {
		List<Empresa> results = em
				.createQuery("SELECT e FROM Empresa e WHERE e.id=:idEmpresa")
				.setParameter("idEmpresa", idEmpresa).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}
}