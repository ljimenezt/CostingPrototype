package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.security.entities.Icono;

/**
 * DAO class that establishes the connection between business logic and
 * database. IconoAction used for managing menu icons, has the name 'Manage'
 * that the jar contains security and IconoDao
 * 
 * @author marisol.calderon
 * @modify 18/05/2016 Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class ManageIconDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an icon in the database.
	 * 
	 * @param icon
	 *            : icon to save.
	 * @throws Exception
	 */
	public void saveIcon(Icono icon) throws Exception {
		em.persist(icon);
	}

	/**
	 * Modifies an icon in the database.
	 * 
	 * @param icon
	 *            : icon to modify.
	 * @throws Exception
	 */
	public void editIcon(Icono icon) throws Exception {
		em.merge(icon);
	}

	/**
	 * Consultation If the icon name exists in the database when you save.
	 * 
	 * @param name
	 *            : name to verify.
	 * @return Icon: Icon object found with the name or null but there
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Icono nameExist(String name) throws Exception {
		List<Icono> results = em
				.createQuery("FROM Icono i WHERE UPPER(i.nombre)=:name ")
				.setParameter("name", name).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Deletes an icon of the database.
	 * 
	 * @param icon
	 *            : Object icon to delete.
	 * @throws Exception
	 */
	public void removeIcon(Icono icon) throws Exception {
		Icono e = em.find(Icono.class, icon.getId());
		em.remove(e);
	}

	/**
	 * This method consulting icons with a certain range sent as a parameter and
	 * filtering the information by the values of search sent.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param start
	 *            : Where it initiates the consultation record.
	 * @param range
	 *            : Range records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Icono>: Icons list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Icono> queryIcons(int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT i FROM Icono i ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Icono> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing icons in the database filtering
	 * information search by the values sent.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param query
	 *            : String containing the query why the filter assignments.
	 * @param parameters
	 *            : Query parameters
	 * @return Long: Icons quantity
	 * @throws Exception
	 */
	public Long quantityIcons(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder consult = new StringBuilder();
		consult.append("SELECT COUNT(i) FROM Icono i ");
		consult.append(query);
		Query q = em.createQuery(consult.toString());
		for (SelectItem si : parameters) {
			q.setParameter(si.getLabel(), si.getValue());
		}
		return (Long) q.getSingleResult();
	}
}
