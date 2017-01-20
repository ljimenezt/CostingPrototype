package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
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
}