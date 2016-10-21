package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.Date;
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
	 * This method look for farms with a certain range sent as a parameter and
	 * the result is filtered according to some search values.
	 * 
	 * @param start
	 *            : The record where the query result starts.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Farm>: farms list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Farm> searchFarms(int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM Farm f ");
		query.append(consult);
		query.append("ORDER BY f.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Farm> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a farm in the database.
	 * 
	 * @param farm
	 *            : Farm to save.
	 * @throws Exception
	 */
	public void saveFarm(Farm farm) throws Exception {
		em.persist(farm);
	}

	/**
	 * Edits a farm in the database.
	 * 
	 * @param farm
	 *            : Farm to edit.
	 * @throws Exception
	 */
	public void editFarm(Farm farm) throws Exception {
		em.merge(farm);
	}

	/**
	 * Remove a farm of the database.
	 * 
	 * @param farm
	 *            : Farm to remove.
	 * @throws Exception
	 */
	public void deleteFarm(Farm farm) throws Exception {
		em.remove(em.merge(farm));
	}

	/**
	 * Returns the number of existing farms in the database (information
	 * filtered with search values)
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of crop records found.
	 * @throws Exception
	 */
	public Long amountFarms(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(f) FROM Farm f ");
		queryBuilder.append(consult);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Look for the name of the farm whether existing in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : Name of the farm to verify.
	 * @param id
	 *            : Identifier of the property to check
	 * @return Farm: Farm object found with the search parameters name and
	 *         identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Farm nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM Farm f ");
		query.append("WHERE UPPER(f.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND f.idFarm <>:idFarm ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
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
	 * Query objects related with a farm, it is also queried the not null
	 * objects.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param objectName
	 *            : Object found on the farm.
	 * @param idFarm
	 *            : Identifier of the farm being queried.
	 * @return Object information associated with the farm or null but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object searchFarmObject(String objectName, int idFarm)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT f." + objectName
								+ " FROM Farm f WHERE f.id=:idFarm")
				.setParameter("idFarm", idFarm).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}

	/**
	 * Method to search the Farm object and store it in a List.
	 * 
	 * @author Andres.Gomez
	 * @modify 15/03/2016 Wilhelm.Boada
	 * 
	 * @return List<Farm>: Farms list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Farm> farmsList() throws Exception {
		Query q = em.createQuery("SELECT f FROM Farm f ORDER BY f.name ");
		return q.getResultList();

	}

	/**
	 * Method to search the Farm for id.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param farmId
	 *            : Identifier farm
	 * @return Farm: farm Object
	 * @throws Exception
	 */
	public Farm farmXId(int farmId) throws Exception {
		return em.find(Farm.class, farmId);
	}

	/**
	 * Method that allows check the properties of a company with access
	 * permissions a person has on the system.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param idCompany
	 *            : company id in session.
	 * @param idPersonSession
	 *            : identifier of the person in session at which it is consulted
	 *            by the farms that have permission to access.
	 * @return List of farms to which the company has access.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Farm> consultFarmsWithPermissionAccessCompany(int idCompany,
			int idPersonSession) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT ppe.farm FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.empresa.id=:idCompany ");
		query.append("AND ppe.persona.id = :idPersonSession ");
		query.append("AND (ppe.fechaFinVigencia IS NULL ");
		query.append("OR ppe.fechaFinVigencia >= :actualDate) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCompany", idCompany);
		q.setParameter("idPersonSession", idPersonSession);
		q.setParameter("actualDate", new Date());
		return q.getResultList();
	}
}
