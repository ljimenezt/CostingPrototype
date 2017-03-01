package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.diesel.entities.IrrigationDetails;

/**
 * DAO class that establishes the connection between business logic and data
 * base for irrigation_details.
 * 
 * @author Patricia.Patinio
 */
@SuppressWarnings("serial")
@Stateless
public class IrrigationDetailsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an irrigationDetails register in the database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to save.
	 * @throws Exception
	 */
	public void saveIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.persist(irrigationDetails);
	}

	/**
	 * Edit an irrigationDetails register in database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to edit.
	 * @throws Exception
	 */
	public void editIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.merge(irrigationDetails);
	}

	/**
	 * Remove an irrigationDetails register of the database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to remove.
	 * @throws Exception
	 */
	public void deleteIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.remove(em.merge(irrigationDetails));
	}

	/**
	 * Returns the number of rows that exist in the database that are existing
	 * or not existing.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long quantityIrrigationDetails(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(id) FROM IrrigationDetails id ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the list of irrigation details that comply with the option of
	 * force.
	 * 
	 * @author Liseth.Jimenez
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
	 * @return List<IrrigationDetails>:List of irrigation details that comply
	 *         with the condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<IrrigationDetails> consultIrrigationDetails(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM IrrigationDetails id ");
		query.append("LEFT JOIN FETCH id.engineLog el ");
		query.append("LEFT JOIN FETCH id.machine m ");
		query.append("LEFT JOIN FETCH id.zone z ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}
}