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
}