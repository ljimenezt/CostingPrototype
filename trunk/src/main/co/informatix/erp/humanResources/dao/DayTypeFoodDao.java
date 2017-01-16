package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.DayTypeFood;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the dayTypeFood entity.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class DayTypeFoodDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an dayTypeFood in database
	 * 
	 * @param dayTypeFood
	 *            : DayTypeFood to save.
	 * @throws Exception
	 */
	public void saveDayTypeFood(DayTypeFood dayTypeFood) throws Exception {
		em.persist(dayTypeFood);
	}

	/**
	 * Edit an dayTypeFood in database
	 * 
	 * @param dayTypeFood
	 *            : DayTypeFood to edit.
	 * @throws Exception
	 */
	public void editDayTypeFood(DayTypeFood dayTypeFood) throws Exception {
		em.merge(dayTypeFood);
	}

	/**
	 * Delete the dayTypeFood of the database.
	 * 
	 * @param dayTypeFood
	 *            : DayTypeFood to remove.
	 * @throws Exception
	 */
	public void deleteDayTypeFood(DayTypeFood dayTypeFood) throws Exception {
		em.remove(em.merge(dayTypeFood));
	}

	/**
	 * This method consult dayTypeFood list with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<DayTypeFood>: DayTypeFood list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DayTypeFood> consultDayTypeFood(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM DayTypeFood d ");
		query.append("JOIN FETCH d.typeFood t ");
		query.append("JOIN FETCH d.day da ");
		query.append(consult);
		query.append("ORDER BY da.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<DayTypeFood> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method consult dayTypeFood list filtering the information by the
	 * values of sent search.
	 * 
	 * @param idTypeFood
	 *            : identifier typeFood.
	 * @return List<DayTypeFood>: DayTypeFood list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DayTypeFood> consultDayTypeFoodByIdTypeFood(int idTypeFood)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM DayTypeFood d ");
		query.append("JOIN FETCH d.typeFood ");
		query.append("JOIN FETCH d.day ");
		query.append("WHERE d.typeFood.id =:idTypeFood ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTypeFood", idTypeFood);
		List<DayTypeFood> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method to consult dayTypeFood that are associated with a typeFood and
	 * day.
	 * 
	 * @param idTypeFood
	 *            :TypeFood identifier to find dayTypeFood.
	 * @param idDay
	 *            :Day identifier to find dayTypeFood.
	 * @return List<DayTypeFood>: DayTypeFood found by typeFood and day.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DayTypeFood findDayTypeFoodByIds(int idTypeFood, int idDay)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM DayTypeFood d ");
		query.append("WHERE d.typeFood.id=:idTypeFood ");
		query.append("AND d.day.id=:idDay ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTypeFood", idTypeFood);
		q.setParameter("idDay", idDay);
		List<DayTypeFood> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method to consult dayTypeFood that are associated with a typeFood and day
	 * the field after day true.
	 * 
	 * @param idTypeFood
	 *            :TypeFood identifier to find dayTypeFood.
	 * @return DayTypeFood: DayTypeFood found by typeFood and after day true.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public DayTypeFood consultAfterHoliday(int idTypeFood) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM DayTypeFood d ");
		query.append("JOIN FETCH d.typeFood t ");
		query.append("WHERE t.id=:idTypeFood ");
		query.append("AND d.afterHoliday=true ");
		query.append("AND d.day.id=null ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTypeFood", idTypeFood);
		List<DayTypeFood> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}