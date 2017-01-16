package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.Day;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the day entity.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Stateless
public class DayDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consult the day Object by identifier.
	 * 
	 * @param id
	 *            : Day identifier to consult.
	 * 
	 * @return: Day object found with the search parameter identifier.
	 * @throws Exception
	 */
	public Day dayById(int id) throws Exception {
		return em.find(Day.class, id);
	}

	/**
	 * Returns the number of existing days in the database filtering information
	 * search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter names of day.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of records found.
	 * @throws Exception
	 */
	public Long quantityDay(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(d) FROM Day d ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation days with a certain range sent as a parameter
	 * and filtering the information by the values of sent search.
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consult the logs depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Day>: Day list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Day> consultDay(int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Day d ");
		query.append(consult);
		query.append("ORDER BY d.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Day> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method consultation days filtering the information by the values of
	 * sent search.
	 * 
	 * @param consult
	 *            : Consult the logs depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Day>: Day list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Day> consultDay(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Day d ");
		query.append(consult);
		query.append("ORDER BY d.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<Day> resultList = q.getResultList();
		if (resultList.size() <= 0) {
			resultList = new ArrayList<Day>();
		}
		return resultList;
	}
}