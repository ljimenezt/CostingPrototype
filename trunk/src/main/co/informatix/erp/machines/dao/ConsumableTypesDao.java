package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.ConsumableTypes;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the types of consumables.
 * 
 * @author Dario.Lopez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ConsumableTypesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method build the consult the types of consumables with determined
	 * range sent as a parameter and filtering the information by the values
	 * sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<ConsumableTypes>: list of the types of consumables.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumableTypes> consultarConsumableTypes(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM  ConsumableTypes ct ");
		query.append(consult);
		query.append("ORDER BY ct.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<ConsumableTypes> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a type of consumable in DB
	 * 
	 * @param consumableTypes
	 *            : Consumable type to save.
	 * @throws Exception
	 */
	public void guardarConsumableTypes(ConsumableTypes consumableTypes)
			throws Exception {
		em.persist(consumableTypes);
	}

	/**
	 * Edits a consumable type DB
	 * 
	 * @param consumableTypes
	 *            : consumable type to edit.
	 * @throws Exception
	 */
	public void editarConsumableTypes(ConsumableTypes consumableTypes)
			throws Exception {
		em.merge(consumableTypes);
	}

	/**
	 * removes a consumable type BD
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consumableTypes
	 *            : consumable type remove
	 * @throws Exception
	 */
	public void eliminarConsumableTypes(ConsumableTypes consumableTypes)
			throws Exception {
		em.remove(em.merge(consumableTypes));
	}

	/**
	 * Returns the number of existing types of consumables in the database
	 * information by filtering the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found Consumable Type
	 * @throws Exception
	 */
	public Long cantidadConsumableTypes(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ct) FROM ConsumableTypes ct ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Query whether the type of supply exists in the database when storing or
	 * editing.
	 * 
	 * @param name
	 *            : consumable type to verify
	 * @param id
	 *            : consumable type id to verify
	 * @return ConsumableTypes: consumableType object found with the search
	 *         parameters name and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ConsumableTypes nombreExiste(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM ConsumableTypes ct ");
		query.append("WHERE UPPER(ct.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND ct.idConsumableType <>:idConsumableType ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idConsumableType", id);
		}
		List<ConsumableTypes> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
