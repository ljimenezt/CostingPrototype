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
 * This class is all the logic related to the creation, updating, and deleting
 * the meal control that may exist.
 * 
 * @author Wilhelm.Boada
 * @modify Andres.Gomez
 */
@SuppressWarnings("serial")
@Stateless
public class FoodControlDao implements Serializable {
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
	public boolean consultFoodControlByIdTypeFood(int idTypeFood)
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
	public FoodControl consultFoodControlByIdTypeIdHrAndDate(int idTypeFood,
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
	public List<FoodControl> listHrOfFoodControl(int idHr,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM FoodControl a ");
		query.append("LEFT JOIN FETCH  a.hr h ");
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
	public Long countDateFoodControl(Date startDate, Date endDate) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(DISTINCT tf) FROM FoodControl a ");
		query.append("JOIN a.hr h ");
		query.append("JOIN a.typeFood tf ");
		query.append("WHERE a.date BETWEEN :startDate AND :endDate ");
		Query q = em.createQuery(query.toString());
		q.setParameter("startDate", startDate).setParameter("endDate", endDate);
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consult the number of assistants HR since the table food
	 * control.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param consult
	 *            : Query records depend of the dates initial and final of week.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Quantity of register found.
	 * @throws Exception
	 */
	public Long hrFoodControlAmount(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(DISTINCT a.hr) FROM  FoodControl a ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consult the number of assistants Other since the table food
	 * control.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param consult
	 *            : Query records depend of the dates initial and final of week.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Quantity of register found.
	 * @throws Exception
	 */
	public Long otherFoodControlAmount(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(DISTINCT a.other) FROM  FoodControl a ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consult the assistants Hr and Other of food control
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param start
	 *            : First record of the query result that is retrieved.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depend of the dates initial and final of week.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Object[]>: List of records found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> listHrOtherOfFoodControl(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT(a.hr.idHr), a.other, hr.name FROM FoodControl a ");
		query.append("LEFT JOIN a.hr hr ");
		query.append(consult);
		query.append("ORDER BY hr.name, a.other ASC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Object[]> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method consult the food control that have other associated.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param other
	 *            : Name of other associated.
	 * @param consult
	 *            : Query records depend of the dates initial and final of week.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<FoodControl>: List of food control found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FoodControl> listHrOfFoodControlXOther(String other,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM FoodControl a ");
		query.append("LEFT JOIN a.hr ");
		query.append("JOIN FETCH a.typeFood ");
		query.append(consult);
		if (other != null) {
			query.append("AND a.other = :other ");
		} else {
			query.append(" AND a.other !='' ");
		}
		Query q = em.createQuery(query.toString());
		if (other != null) {
			q.setParameter("other", other);
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
	 * This method consult foodControl filtering the information by date of sent
	 * search.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param other
	 *            : Name of other.
	 * @param date
	 *            : Date to the consult food control.
	 * @return FoodControl: Object found with the search parameters
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public FoodControl consultFoodControlXDate(String other, String date)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT f FROM FoodControl f ");
		query.append("WHERE f.other =:other ");
		query.append("AND TO_CHAR(f.date,'YYYY-MM-dd')= :date ");
		Query q = em.createQuery(query.toString());
		q.setParameter("other", other);
		q.setParameter("date", date);
		List<FoodControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList.get(0);
		}
		return new FoodControl();
	}

}