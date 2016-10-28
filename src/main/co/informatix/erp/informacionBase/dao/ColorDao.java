package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;

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

}
