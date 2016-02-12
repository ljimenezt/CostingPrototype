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
 * This class manages access to data to record, view, edit and complete
 * observance of rolesMenu
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
	 * This method saves a rolMenu
	 * 
	 * @param rolMenu
	 *            : rolMenu to Save
	 * @throws Exception
	 */
	public void guardarRolMenu(RolMenu rolMenu) throws Exception {
		em.persist(rolMenu);
	}

	/**
	 * This method edits the menu roles
	 * 
	 * @param rolMenu
	 *            : rolMenu to edit
	 * @throws Exception
	 */
	public void editarRolMenu(RolMenu rolMenu) throws Exception {
		em.merge(rolMenu);
	}

	/**
	 * Method to consult all the menus associated with a role, regardless of
	 * duration.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param rol
	 *            : Role to find menus.
	 * 
	 * @return Role menu list, regardless of duration.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolMenu> consultarTodosRolMenu(Rol rol) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT rm FROM RolMenu rm ");
		query.append("WHERE rm.rolMenuPK.rol=:rol ");
		Query q = em.createQuery(query.toString());
		q.setParameter("rol", rol);
		return q.getResultList();
	}

	/**
	 * This method allows to verify if the menu has relationships with the role
	 * in the database.
	 * 
	 * @author Luz.Jaimes
	 * 
	 * @param idMenu
	 *            : Identifier of the menu to which you consult relationships.
	 * @return boolean: True if the menu is related to the role, otherwise
	 *         false.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean relacionesRolMenu(int idMenu) throws Exception {
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
