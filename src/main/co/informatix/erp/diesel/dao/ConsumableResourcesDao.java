package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.diesel.entities.ConsumableResources;

/**
 * DAO class that establishes the connection between business logic and
 * database. ConsumableResourcesAction used for managing consumableResources.
 * 
 * @author Fabian.Diaz
 */
@SuppressWarnings("serial")
@Stateless
public class ConsumableResourcesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a consumableResource in the database.
	 * 
	 * @param consumableResources
	 *            : Save consumableResource.
	 * @throws Exception
	 */
	public void saveConsumableResources(ConsumableResources consumableResources)
			throws Exception {
		em.persist(consumableResources);
	}

	/**
	 * Modify a consumableResource in the database.
	 * 
	 * @param consumableResources
	 *            : consumableResources to edit.
	 * @throws Exception
	 */
	public void editConsumableResources(ConsumableResources consumableResources)
			throws Exception {
		em.merge(consumableResources);
	}

	/**
	 * Removes the BD Zone.
	 * 
	 * @param consumableResources
	 *            : consumableResources to be removed.
	 * @throws Exception
	 */
	public void deleteConsumableResources(
			ConsumableResources consumableResources) throws Exception {
		em.remove(em.merge(consumableResources));
	}

	/**
	 * Consult the list of consumable resources that comply with the option of
	 * force.
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<ConsumableResources>:List of consumable resources that
	 *         comply with the condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ConsumableResources> consultConsumableResources(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cr FROM ConsumableResources cr ");
		query.append("JOIN FETCH cr.measurementUnits ");
		query.append(consult);
		query.append("ORDER BY cr.name");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of consumable resources that exist in the database
	 * that are existing or not existing.
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long quantityConsumableResources(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(cr) FROM ConsumableResources cr ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Query whether the consumable resources name exists in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : name of the consumable resource to verify.
	 * @param id
	 *            : identifier of the consumable resource to verify.
	 * @return ConsumableResources : ConsumableResources object found with the
	 *         search parameters name and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ConsumableResources nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cr FROM ConsumableResources cr ");
		query.append("WHERE UPPER(cr.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND cr.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<ConsumableResources> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult a consumable resource by id
	 * 
	 * @param id
	 *            : Identifier of consumable resource
	 * @return ConsumableResource: Object ConsumableResource
	 * @throws Exception
	 */
	public ConsumableResources consumableResourceById(int id) throws Exception {
		return em.find(ConsumableResources.class, id);
	}

}
