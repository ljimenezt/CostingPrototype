package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Farm;

@SuppressWarnings("serial")
@Stateless
/**
 * class DAO that establishes the connection between business logic and database for handling the Farm entity.
 * 
 * @author Johnatan.Naranjo
 * 
 */
public class FarmDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consultation farm with a certain range sent as a parameter
	 * and filtering the information sent search values.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Farm>: farms list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Farm> consultarFarms(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM Farm f ");
		query.append(consult);
		query.append("ORDER BY f.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Farm> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a ranch in BD
	 * 
	 * @param farm
	 *            : farm to save.
	 * @throws Exception
	 */
	public void guardarFarm(Farm farm) throws Exception {
		em.persist(farm);
	}

	/**
	 * Edits a farm in BD
	 * 
	 * @param farm
	 *            : farm to edit.
	 * @throws Exception
	 */
	public void editarFarm(Farm farm) throws Exception {
		em.merge(farm);
	}

	/**
	 * Remove a farm of the DB
	 * 
	 * @param farm
	 *            : farm to remove
	 * @throws Exception
	 */
	public void eliminarFarm(Farm farm) throws Exception {
		em.remove(em.merge(farm));
	}

	/**
	 * Returns the number of existing farms in the database information by
	 * filtering the search values sent.
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return Long: amount of crop records found
	 * @throws Exception
	 */
	public Long cantidadFarms(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(f) FROM Farm f ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consultation if the name of the farm exists in the database when storing
	 * or editing.
	 * 
	 * @param name
	 *            : name of the farm to verify
	 * @param id
	 *            : identifier of the property to check
	 * @return Farm: farm object found with the search parameters name and
	 *         identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Farm nombreExiste(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM Farm f ");
		query.append("WHERE UPPER(f.name)=UPPER(:nombre) ");
		if (id != 0) {
			query.append("AND f.idFarm <>:idFarm ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("nombre", name);
		if (id != 0) {
			q.setParameter("idFarm", id);
		}
		List<Farm> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult also article assigned to a farm, considering that they are only
	 * those that are not null in the table
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param nomObject
	 *            : object found on the farm
	 * @param idFarm
	 *            : identifier of the farm being queried
	 * @return Object information associated with the farm or null but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoFarm(String nomObject, int idFarm)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT f." + nomObject
								+ " FROM Farm f WHERE f.id=:idFarm")
				.setParameter("idFarm", idFarm).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}

	/**
	 * Method consulting the Farm object and stores it in a list
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<Farm>: Farms lists
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Farm> listaFarms() throws Exception {
		Query q = em.createQuery("SELECT f FROM Farm f ");
		return q.getResultList();

	}

	/**
	 * This method allow consult all Farm stored in data base
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return List<Farm>: all Farm stored in data base
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Farm> consultarAllFarm() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM Farm f ");
		Query q = em.createQuery(query.toString());
		return q.getResultList();
	}
}
