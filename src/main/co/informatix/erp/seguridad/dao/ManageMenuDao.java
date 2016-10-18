package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.security.entities.Menu;
import co.informatix.security.entities.Rol;

/**
 * DAO class that establishes the connection between business logic and
 * database. GestionarMenuAction used for managing the application menus.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@Stateless
public class ManageMenuDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows consult the amount of available menus in the database.
	 * 
	 * @param consult
	 *            : Consult the register depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Parameters of the query.
	 * @return Long: Number of menus found.
	 * @throws Exception
	 */
	public Long quantityMenus(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(m) FROM Menu m ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Menu method to consult with a beginning, a range of filter parameters and
	 * if they are required.
	 * 
	 * @modify 03/03/2014 Marcela.Chaparro
	 * 
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param order
	 *            : Order in which the records are shown in the listing.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Menu>: List of existing menus in the database according to
	 *         the search.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> consultMenus(Integer start, Integer range,
			StringBuilder consult, StringBuilder order,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Menu m ");
		query.append(consult);
		query.append(order);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		if (start != null && range != null) {
			q.setFirstResult(start).setMaxResults(range);
		}
		return q.getResultList();
	}

	/**
	 * Menu allows you to consult the father of a menu.
	 * 
	 * @param idMenu
	 *            : Menu ID to consult.
	 * @return Menu: Menu parent information menu.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Menu consultMenuFather(int idMenu) throws Exception {
		List<Menu> results = em
				.createQuery(
						"SELECT m.menuPadre FROM Menu m "
								+ "WHERE m.id=:idMenu AND m.fechaFinVigencia IS NULL ")
				.setParameter("idMenu", idMenu).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Saves a menu in the database.
	 * 
	 * @param menu
	 *            : menu to save.
	 * @throws Exception
	 */
	public void saveMenu(Menu menu) throws Exception {
		em.persist(menu);
	}

	/**
	 * Modifies a menu in the database.
	 * 
	 * @param menu
	 *            : menu to modify
	 * @throws Exception
	 */
	public void editMenu(Menu menu) throws Exception {
		em.merge(menu);
	}

	/**
	 * Consult the menus that exist in the system identifier icon.
	 * 
	 * @modify 23/12/2014 Cristhian.Pico
	 * 
	 * @param idIcono
	 *            : Icon identifier by which the menus are queried.
	 * @return List<Menu>: Menus list objects associated with the icon.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> consultMenuXIdIcon(int idIcono) throws Exception {
		return this.em
				.createQuery(
						"FROM Menu m WHERE m.icono.id = :idIcono "
								+ "AND m.fechaFinVigencia IS NULL")
				.setParameter("idIcono", idIcono).getResultList();
	}

	/**
	 * Method to consult menus with a role associated.
	 * 
	 * @param rol
	 *            : Role to consult the menus.
	 * @return List<Menu>: List of menus role.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> consultMenusRol(Rol rol) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT rm.rolMenuPK.menu FROM RolMenu rm ");
		query.append("WHERE rm.rolMenuPK.rol=:rol ");
		query.append("AND rm.fechaFinVigencia IS NULL ");
		query.append("AND rm.rolMenuPK.menu.fechaFinVigencia IS NULL ");
		Query q = em.createQuery(query.toString());
		q.setParameter("rol", rol);
		return q.getResultList();
	}

	/**
	 * This method looking a menu by identifier.
	 * 
	 * @param id
	 *            : Menu identifier.
	 * @return Menu: Menu object.
	 * @throws Exception
	 */
	public Menu consultMenuXId(int id) throws Exception {
		return em.find(Menu.class, id);
	}

	/**
	 * This method returns the list of all menus.
	 * 
	 * @modify 23/12/2014 Cristhian.Pico
	 * 
	 * @param menusSelected
	 *            : List of menus to filter the query.
	 * @return List<Menu>: List of menus found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> consultAllMenusAction(List<Menu> menusSelected)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Menu m ");
		query.append("WHERE m.fechaFinVigencia IS NULL ");
		query.append("AND m.visible=:visible ");
		query.append("AND m.url IS NOT NULL ");
		query.append("AND m.menuPadre IS NOT NULL ");
		if (menusSelected != null && menusSelected.size() > 0) {
			query.append("AND m NOT IN(:menusSelected) ");
		}
		query.append("AND (SELECT COUNT(men) FROM Menu men ");
		query.append("WHERE men.fechaFinVigencia IS NULL ");
		query.append("AND men.menuPadre=m) <= 0 ");
		query.append("ORDER BY m.nombre");
		Query q = em.createQuery(query.toString());
		q.setParameter("visible", true);
		if (menusSelected != null && menusSelected.size() > 0) {
			q.setParameter("menusSelected", menusSelected);
		}
		return q.getResultList();
	}

	/**
	 * Method to consult menus that are associated with a method.
	 * 
	 * @modify 23/12/2014 Cristhian.Pico
	 * 
	 * @param idMetodo
	 *            : Method identifier to find menus.
	 * @return List<Menu>: Method identifier to find menus.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Menu> consultMenusMethod(int idMetodo) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mm.menu FROM MetodoMenu mm ");
		query.append("WHERE mm.metodo.id=:idMetodo ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMetodo", idMetodo);
		return q.getResultList();
	}

	/**
	 * Method that you are looking menus children associated with a menu to be
	 * deleted.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param idMenu
	 *            : id of the menu you want to remove.
	 * @return boolean: indicates whether the menu to delete or not associated
	 *         menus.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean sonsMenu(int idMenu) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Menu m ");
		query.append("WHERE m.menuPadre.id=:idMenu ");
		query.append("AND m.fechaFinVigencia IS NULL ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMenu", idMenu);
		List<Menu> listMenu = q.getResultList();
		if (listMenu.size() > 0) {
			return true;
		}
		return false;
	}
}