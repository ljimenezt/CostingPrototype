package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.CivilStatus;

/**
 * DAO class that establishes the connection between business logic and base
 * data. CivilStatusAction used for managing civil status.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class CivilStatusDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for a civil state.
	 * 
	 * @param civilStatus
	 *            : Civil status to edit.
	 * @throws Exception
	 */
	public void editCivilStatus(CivilStatus civilStatus) throws Exception {
		em.merge(civilStatus);
	}

	/**
	 * Save civil status in the database.
	 * 
	 * @param civilStatus
	 *            : Civil status to save.
	 * @throws Exception
	 */
	public void saveCivilStatus(CivilStatus civilStatus) throws Exception {
		em.persist(civilStatus);
	}

	/**
	 * Removes a civil state of the BD.
	 * 
	 * @param civilStatus
	 *            : Civil status to delete.
	 * @throws Exception
	 */
	public void removeCivilStatus(CivilStatus civilStatus) throws Exception {
		em.remove(em.merge(civilStatus));
	}

	/**
	 * Returns the number of existing civil states in the BD filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter names of civil
	 *            states.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of civil status records found.
	 * @throws Exception
	 */
	public Long quantityCivilStatus(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(cs) FROM CivilStatus cs ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation with civil states sent a certain range as a
	 * parameter and filtering the information by the values of search sent.
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consultation records the parameters depending selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<CivilStatus>: List of civil status.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CivilStatus> consultCivilStatus(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cs FROM CivilStatus cs ");
		query.append(consult);
		query.append("ORDER BY cs.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<CivilStatus> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation if the name of civil states exist in database when saving or
	 * editing.
	 * 
	 * @param name
	 *            : Name of civil status to check.
	 * @param id
	 *            : id to verify the civil status.
	 * @return CivilStatus: Object found with the search parameters id and name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CivilStatus nameExist(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cs FROM CivilStatus cs ");
		query.append("WHERE UPPER(cs.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND cs.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<CivilStatus> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method allows consult the civil status list.
	 * 
	 * @author Wilhelm.Boada.
	 * 
	 * @return List<CivilStatus>: civil status list was found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CivilStatus> consultCivilStatus() throws Exception {
		return em.createQuery("SELECT cs FROM CivilStatus cs ORDER BY cs.name")
				.getResultList();
	}
}
