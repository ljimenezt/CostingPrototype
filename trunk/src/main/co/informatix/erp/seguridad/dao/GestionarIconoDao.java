package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
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
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class GestionarIconoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows consult the icons in the database.
	 * 
	 * @param start
	 *            : Start Registry.
	 * @param range
	 *            : end in the range of records to consult.
	 * @return List<Icono>: list of icons found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Icono> consultIcons(int start, int range) throws Exception {
		return em.createQuery("SELECT i FROM Icono i ORDER BY i.nombre")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Allows consult the amount of icons on the database.
	 * 
	 * @return Long: number of icons found in the database.
	 * @throws Exception
	 */
	public Long quantityIcons() throws Exception {
		return (Long) em.createQuery("SELECT COUNT(i) FROM Icono i ")
				.getSingleResult();
	}

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
	 * @return Icono: Icon object found with the name or null but there
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
	 * Consultation icons that meet the search criteria name, considering the
	 * beginning and end of the list.
	 * 
	 * @param start
	 *            : where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param nameSearch
	 *            : name by which the icon is sought.
	 * @return List of icons found with that name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Icono> searchIconsXNamePaginated(int start, int range,
			String nameSearch) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT i FROM Icono i ");
		query.append("WHERE i.fechaFinVigencia IS NULL ");
		if (nameSearch != null && !"".equals(nameSearch)) {
			query.append("AND UPPER(i.nombre) LIKE  UPPER(:keyword) ");
		}
		query.append("ORDER BY i.nombre ");
		Query q = em.createQuery(query.toString());
		if (nameSearch != null && !"".equals(nameSearch)) {
			q.setParameter("keyword", "%" + nameSearch + "%");
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Allows consult the amount of icons on the database, considering the name
	 * submitted as a parameter.
	 * 
	 * @param nameSearch
	 *            : name by which the icon is sought.
	 * 
	 * @return Long: number of icons found in the database.
	 * @throws Exception
	 */
	public Long quantityIconsByName(String nameSearch) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(i) FROM Icono i ");
		query.append("WHERE i.fechaFinVigencia IS NULL ");
		if (nameSearch != null && !"".equals(nameSearch)) {
			query.append("AND UPPER(i.nombre) LIKE  UPPER(:keyword) ");
		}
		Query q = em.createQuery(query.toString());
		if (nameSearch != null && !"".equals(nameSearch)) {
			q.setParameter("keyword", "%" + nameSearch + "%");
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Allows consult the amount of icons by name in the existing database.
	 * 
	 * @param nameSearch
	 *            : name by which the icon is sought.
	 * @return Long: number of icons found.
	 * @throws Exception
	 */
	public Long quantityIconsXName(String nameSearch) throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(i) FROM Icono i "
								+ "WHERE UPPER(i.nombre) LIKE :keyword ")
				.setParameter("keyword", "%" + nameSearch + "%")
				.getSingleResult();
	}

	/**
	 * Method for querying name icons considering a beginning and a range.
	 * 
	 * @param nameSearch
	 *            : Method that allows consulting name icons considering a
	 *            beginning and a range.
	 * @param start
	 *            : Start the registration.
	 * @param range
	 *            : end in the range of records to consult.
	 * @return List<Icono>: list of icons found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Icono> consultIconsXNamePaginator(String nameSearch, int start,
			int range) throws Exception {
		return em
				.createQuery(
						"SELECT i FROM Icono i "
								+ "WHERE UPPER(i.nombre) LIKE :keyword "
								+ "ORDER BY i.nombre")
				.setParameter("keyword", "%" + nameSearch + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

}
