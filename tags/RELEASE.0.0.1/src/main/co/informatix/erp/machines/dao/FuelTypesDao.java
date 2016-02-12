package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.FuelTypes;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the types of fuels.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class FuelTypesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a fuel type in the DB
	 * 
	 * @param fuelTypes
	 *            : one fuel type to save.
	 * @throws Exception
	 */
	public void saveFuelTypes(FuelTypes fuelTypes) throws Exception {
		em.persist(fuelTypes);
	}

	/**
	 * Edits a fuel type BD
	 * 
	 * @param fuelTypes
	 *            : type of fuel to edit.
	 * @throws Exception
	 */
	public void editFuelTypes(FuelTypes fuelTypes) throws Exception {
		em.merge(fuelTypes);
	}

	/**
	 * Deletes a fuel type BD
	 * 
	 * @param fuelTypes
	 *            : type of fuel to remove
	 * @throws Exception
	 */
	public void deleteFuelTypes(FuelTypes fuelTypes) throws Exception {
		em.remove(em.merge(fuelTypes));
	}

	/**
	 * Returns the number of types of existing fuel in the database information
	 * by filtering the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found type of fuel
	 * @throws Exception
	 */
	public Long amountFuelTypes(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ft) FROM FuelTypes ft ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<FuelTypes>: list of the types of fuels.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelTypes> consultFuelTypes(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ft FROM FuelTypes ft ");
		query.append(consult);
		query.append("ORDER BY ft.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<FuelTypes> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation if one type of fuel exists in the database when storing or
	 * editing.
	 * 
	 * @param name
	 *            : type of fuel to verify
	 * @param id
	 *            : id the type of fuel to verify
	 * @return FuelTypes: fuel types object found with the search parameters
	 *         name and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public FuelTypes nameTypeFuelExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ft FROM FuelTypes ft ");
		query.append("WHERE UPPER(ft.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND ft.idFuelType <>:idfueltype ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idfueltype", id);
		}
		List<FuelTypes> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that consult all fuel types object and stores it in a list
	 * 
	 * @return List<FuelTypes>: Fuel types list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelTypes> listFuelType() throws Exception {
		Query q = em.createQuery("SELECT ft FROM FuelTypes ft ");
		return q.getResultList();
	}

}
