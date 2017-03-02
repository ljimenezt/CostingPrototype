package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.diesel.entities.Zone;

/**
 * DAO class that establishes the connection between business logic and
 * database. ZoneAction used for managing zone.
 * 
 * @author Fabian.Diaz
 */
@SuppressWarnings("serial")
@Stateless
public class ZoneDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a Zone in the database.
	 * 
	 * @param zone
	 *            : Save zone.
	 * @throws Exception
	 */
	public void saveZone(Zone zone) throws Exception {
		em.persist(zone);
	}

	/**
	 * Modify a Zone in the database.
	 * 
	 * @param zone
	 *            : Zone to edit.
	 * @throws Exception
	 */
	public void editZone(Zone zone) throws Exception {
		em.merge(zone);
	}

	/**
	 * Removes the BD Zone.
	 * 
	 * @param zone
	 *            : Zone to be removed.
	 * @throws Exception
	 */
	public void deleteZone(Zone zone) throws Exception {
		em.remove(em.merge(zone));
	}

	/**
	 * Consult the list of Zone that comply with the option of force.
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
	 * @return List<Zone>:List of Zone that comply with the condition of
	 *         validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Zone> consultZone(int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT z FROM Zone z ");
		query.append(consult);
		query.append(" ORDER BY z.name");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing zone in the database that are existing or
	 * not existing.
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long quantityZone(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(z) FROM Zone z ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Query whether the zone name exists in the database when storing or
	 * editing.
	 * 
	 * @param name
	 *            : name of the zone to verify.
	 * @param id
	 *            : identifier of the zone to verify.
	 * @return Zone : Zone object found with the search parameters name and
	 *         identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Zone nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT z FROM Zone z ");
		query.append("WHERE UPPER(z.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND z.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<Zone> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult a zone by id
	 * 
	 * @param id
	 *            : Identifier of zone
	 * @return Zone: Object Zone
	 * @throws Exception
	 */
	public Zone zoneById(int id) throws Exception {
		return em.find(Zone.class, id);
	}

	/**
	 * Consult the zones existing.
	 * 
	 * @author Patricia.Patinio
	 * 
	 * @return List<Zone>: list of zone objects.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Zone> consultZonesList() throws Exception {
		return em.createQuery("SELECT z FROM Zone z " + "ORDER BY z.name ")
				.getResultList();
	}

}
