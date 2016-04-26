package co.informatix.erp.recursosHumanos.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.recursosHumanos.entities.TipoCargo;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the TipoCargo entity.
 * 
 * @author Oscar.Amaya
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class TipoCargoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows consult the amount of jobs types that match the query condition
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * 
	 * @param condicionVigencia
	 *            : sets the condition of validity (existing or not existing)
	 *            records on request.
	 * @param variableBuscar
	 *            : Word you want to search the records of types of jobs.
	 * @return Long Numbers of types of existing office in the database.
	 * 
	 * @throws Exception
	 */
	public Long contarTiposCargo(String condicionVigencia, String variableBuscar)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(tc) FROM TipoCargo tc "
								+ "WHERE (UPPER(tc.nombre) LIKE UPPER (:keyword) "
								+ " OR UPPER(tc.funciones) LIKE UPPER (:keyword)) "
								+ " AND tc.fechaFinVigencia "
								+ condicionVigencia)
				.setParameter("keyword", "%" + variableBuscar + "%")
				.getSingleResult();
	}

	/**
	 * @author Liseth.Jimenez
	 * 
	 *         Allows to consult to the database a list of types of functions
	 *         that take his name as a parameter sent word 'variableBuscar'
	 * 
	 * @param start
	 *            Starting the record
	 * @param range
	 *            End in the range of records to check
	 * @param condicionVigencia
	 *            sets the condition of validity of the records to see
	 * @param variableBuscar
	 *            Word you want to search the records of types of jobs
	 * @return List<TipoCargo> Word you want to search the records of types of
	 *         jobs
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TipoCargo> buscarTiposCargo(int start, int range,
			String condicionVigencia, String variableBuscar) throws Exception {
		return em
				.createQuery(
						"SELECT tc FROM TipoCargo tc "
								+ "WHERE (UPPER(tc.nombre) LIKE UPPER (:keyword) "
								+ " OR UPPER(tc.funciones) LIKE UPPER (:keyword)) "
								+ " AND tc.fechaFinVigencia "
								+ condicionVigencia + " ORDER BY tc.nombre")
				.setParameter("keyword", "%" + variableBuscar + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Check if the name of a type of jobs is already registered
	 * 
	 * @modify 15/03/2012 marisol.calderon
	 * 
	 * @param name
	 *            : name to verify
	 * @return Object of the type of job already exists
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TipoCargo nombreExiste(String name) throws Exception {
		List<TipoCargo> results = em
				.createQuery("FROM TipoCargo WHERE nombre=:nombre")
				.setParameter("nombre", name).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Check if the name of a type of jobs is already registered
	 * 
	 * @modify 15/03/2012 marisol.calderon
	 * 
	 * @param name
	 *            : name to verify
	 * @param id
	 *            :identifier of type jobs
	 * @return Object of the type of job already exists
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TipoCargo nombreExiste(String name, int id) throws Exception {
		List<TipoCargo> results = em
				.createQuery("FROM TipoCargo WHERE nombre=:nombre AND id <>:id")
				.setParameter("nombre", name).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method for creating a type of job in the database.
	 * 
	 * @param tipoCargo
	 *            : type of job to save
	 * @throws Exception
	 */
	public void crearTipoCargo(TipoCargo tipoCargo) throws Exception {
		em.persist(tipoCargo);
		em.flush();
	}

	/**
	 * Allows you to modify the basic information of a type of jobs
	 * 
	 * @param tipoCargo
	 *            : type of job to change
	 * @throws Exception
	 */
	public void modificarTipoCargo(TipoCargo tipoCargo) throws Exception {
		em.merge(tipoCargo);
		em.flush();
	}

	/**
	 * Consult the types of job that are current
	 * 
	 * @return List<TipoCargo>: list of types of job
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TipoCargo> consultarTiposCargo() throws Exception {
		return em.createQuery(
				"SELECT tc FROM TipoCargo tc "
						+ "WHERE tc.fechaFinVigencia IS NULL "
						+ "ORDER BY tc.nombre ").getResultList();
	}

	/**
	 * Method to query a type of jobs by identifier.
	 * 
	 * @param id
	 *            : type identifier jobs
	 * @return TipoCargo: TipoCargo object found or null if not present.
	 * @throws Exception
	 */
	public TipoCargo consultarTipoCargo(int id) throws Exception {
		return em.find(TipoCargo.class, id);
	}
}