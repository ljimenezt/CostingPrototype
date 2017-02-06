package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.FoodControl;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the meal control that may exist.
 * 
 * @author Wilhelm.Boada
 * @modify Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MealControlDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves the foodControl in the database.
	 * 
	 * @param foodControl
	 *            : foodControl to save.
	 * @throws Exception
	 */
	public void saveFoodControl(FoodControl foodControl) throws Exception {
		em.persist(foodControl);
	}

	/**
	 * Edit the information for one foodControl.
	 * 
	 * @param foodControl
	 *            : foodControl to edit.
	 * @throws Exception
	 */
	public void editFoodControl(FoodControl foodControl) throws Exception {
		em.merge(foodControl);
	}

	/**
	 * Delete the foodControl of the database.
	 * 
	 * @param foodControl
	 *            : foodControl to remove.
	 * @throws Exception
	 */
	public void removeFoodControl(FoodControl foodControl) throws Exception {
		em.remove(em.merge(foodControl));
	}

	/**
	 * This method consult foodControl list filtering the information by the
	 * values of sent search.
	 * 
	 * @param idTypeFood
	 *            : identifier typeFood.
	 * @return boolean: It is true if there are foodControl and false in another
	 *         case.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean consultMealControlByIdTypeFood(int idTypeFood)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM FoodControl f ");
		query.append("WHERE f.typeFood.id =:idTypeFood ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTypeFood", idTypeFood);
		List<FoodControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method consult foodControl filtering the information by the values
	 * of sent search.
	 * 
	 * @param idTypeFood
	 *            : identifier typeFood.
	 * @param idHr
	 *            : identifier hr.
	 * @param date
	 *            : Date to the consult food control.
	 * @return FoodControl: Object found with the search parameters
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public FoodControl consultMealControlByIdTypeIdHrAndDate(int idTypeFood,
			int idHr, String date) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM FoodControl f ");
		query.append("WHERE f.typeFood.id =:idTypeFood ");
		query.append("AND f.hr.idHr =:idHr ");
		query.append("AND TO_CHAR(f.date,'YYYY-MM-dd')= :date ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTypeFood", idTypeFood);
		q.setParameter("idHr", idHr);
		q.setParameter("date", date);
		List<FoodControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList.get(0);
		}
		return new FoodControl();
	}

	/**
	 * This method allows get a list of the dates in a interval in the meal
	 * control table without repeat values
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Date>: date assist control list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Date> consultMealControlDates(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT TO_DATE(TO_CHAR(fc.date,'YYYY-MM-dd'), 'YYYY-MM-dd') ");
		query.append("FROM FoodControl fc ");
		query.append(consult);
		query.append("ORDER BY 1 ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<Date> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult the human resources relate with the control
	 * assist table.
	 * 
	 * @modify Andres.Gomez 08/11/2016
	 * 
	 * @param idHr
	 *            : identifier to search
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<FoodControl>: assist control list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FoodControl> listHrOfMealControl(int idHr,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM FoodControl a ");
		query.append("JOIN FETCH a.hr h ");
		query.append("JOIN FETCH a.typeFood ");
		query.append(consult);
		if (idHr != 0) {
			query.append("AND h.idHr = :idHr ");
		}
		Query q = em.createQuery(query.toString());
		if (idHr != 0) {
			q.setParameter("idHr", idHr);
		}
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<FoodControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult the number of type food what is register by
	 * the date
	 * 
	 * @param startDate
	 *            : initial date to filter
	 * @param endDate
	 *            : final date to filter
	 * @return Long : count of the number the type food according a date
	 */
	public Long countDateMealControl(Date startDate, Date endDate) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(DISTINCT tf) FROM FoodControl a ");
		query.append("JOIN a.hr h ");
		query.append("JOIN a.typeFood tf ");
		query.append("WHERE a.date BETWEEN :startDate AND :endDate ");
		Query q = em.createQuery(query.toString());
		q.setParameter("startDate", startDate).setParameter("endDate", endDate);
		return (Long) q.getSingleResult();
	}

}