package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
}
