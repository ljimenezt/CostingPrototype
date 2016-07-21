package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Pluviometer;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the rain gauge readings that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class PluviometerDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an rain gauge readings in database
	 * 
	 * @param pluviometer
	 *            : pluviometer to save.
	 * @throws Exception
	 */
	public void savePluviometer(Pluviometer pluviometer) throws Exception {
		em.persist(pluviometer);
	}

	/**
	 * Edit an rain gauge readings in database
	 * 
	 * @param pluviometer
	 *            : pluviometer to edit.
	 * @throws Exception
	 */
	public void editPluviometer(Pluviometer pluviometer) throws Exception {
		em.merge(pluviometer);
	}

	/**
	 * This method consult rain gauge readings list with a certain range sent as
	 * a parameter and filtering the information by the values of sent search.
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Pluviometer>: rain gauge readings list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Pluviometer> consultPluviometer(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM Pluviometer p ");
		query.append(consult);
		query.append("ORDER BY p.dateRecord ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<Pluviometer> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation if the date reading of the pluviometer units there in the
	 * database when storing or editing.
	 * 
	 * @param date
	 *            : date reading to verify.
	 * @param id
	 *            : identifier pluviometer to verify.
	 * @return pluviometer: Pluviometer object found with the search parameters
	 *         name and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Pluviometer dateExists(Date date, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM Pluviometer p ");
		query.append("WHERE p.dateRecord=:date ");
		if (id != 0) {
			query.append("AND p.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("date", date);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<Pluviometer> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consultation if the date reading of the pluviometer units there in the
	 * database when storing or editing.
	 * 
	 * @param date
	 *            : date reading to verify.
	 * @return pluviometer: Pluviometer object found with the search parameters
	 *         name and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Pluviometer findDate(Date date) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM Pluviometer p ");
		query.append("WHERE p.dateRecord=:date ");
		Query q = em.createQuery(query.toString());
		q.setParameter("date", date);
		List<Pluviometer> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}
