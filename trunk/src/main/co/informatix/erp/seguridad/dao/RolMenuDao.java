package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.security.entities.Menu;
import co.informatix.security.entities.Rol;
import co.informatix.security.entities.RolMenu;

/**
 * This class manages the access to data to record, view, edit and complete
 * observance of rolesMenu.
 * 
 * @author Oscar.Amaya
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class RolMenuDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method saves a rolMenu.
	 * 
	 * @param rolMenu
	 *            : rolMenu to Save.
	 * @throws Exception
	 */
	public void saveRolMenu(RolMenu rolMenu) throws Exception {
		em.persist(rolMenu);
	}

	/**
	 * This method edits the rolMenu.
	 * 
	 * @param rolMenu
	 *            : rolMenu to edit.
	 * @throws Exception
	 */
	public void editRolMenu(RolMenu rolMenu) throws Exception {
		em.merge(rolMenu);
	}

	/**
	 * Method to consult all the menus associated with a role, regardless of
	 * duration.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param role
	 *            : Role to find menus.
	 * 
	 * @return Role menu list, regardless of duration.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolMenu> queryAllRolMenu(Rol role) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT rm FROM RolMenu rm ");
		query.append("WHERE rm.rolMenuPK.rol=:rol ");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("rol", role);
		return queryResult.getResultList();
	}

	/**
	 * This method verifies if the menu has relations with the role in the
	 * database.
	 * 
	 * @author Luz.Jaimes
	 * 
	 * @param idMenu
	 *            : Identifier of the menu in which you look for relations.
	 * @return boolean: True if the menu is related to the role, false
	 *         otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean rolMenuRelations(int idMenu) throws Exception {
		List<Menu> results = em
				.createQuery(
						"SELECT rm FROM RolMenu rm "
								+ "WHERE rm.rolMenuPK.menu.id=:idMenu AND "
								+ "rm.fechaFinVigencia IS NULL ")
				.setParameter("idMenu", idMenu).getResultList();
		if (results.size() > 0) {
			return true;
		}
		return false;
	}
}
