package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.Color;

/**
 * DAO class that establishes the connection between business logic and
 * database. ColorDao manages Color.
 * 
 * @author Claudia.Rey
 */
@SuppressWarnings("serial")
@Stateless
public class ColorDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves the color in the database.
	 * 
	 * @param color
	 *            : color to save.
	 * @throws Exception
	 */
	public void saveColor(Color color) throws Exception {
		em.persist(color);
	}

	/**
	 * Edit the information for one color.
	 * 
	 * @param color
	 *            : color to edit.
	 * @throws Exception
	 */
	public void editColor(Color color) throws Exception {
		em.merge(color);
	}

	/**
	 * Removes a color from the database.
	 * 
	 * @param color
	 *            : color to remove.
	 * @throws Exception
	 */
	public void removeColor(Color color) throws Exception {
		em.remove(em.merge(color));
	}

	/**
	 * This method consult the color Object by identifier.
	 * 
	 * @author Luna.Granados
	 * 
	 * @param id
	 *            : color identifier to consult.
	 * 
	 * @return Color: object found with the search parameter identifier.
	 * @throws Exception
	 */
	public Color colorById(int id) throws Exception {
		return em.find(Color.class, id);
	}

	/**
	 * Consult the existing colors in the database.
	 * 
	 * @author Luna.Granados
	 * 
	 * @return List<Color>: List of colors found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Color> queryColors() throws Exception {
		return em.createQuery("SELECT c FROM Color c ORDER BY c.name")
				.getResultList();
	}

	/**
	 * Returns the number of existing color in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter names of color.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of records found colors.
	 * @throws Exception
	 */
	public Long quantityColor(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(c) FROM Color c ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation colors with a certain range sent as a parameter
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
	 * @return List<Color>: List of colors.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Color> consultColor(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM Color c ");
		query.append(consult);
		query.append("ORDER BY c.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Color> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
