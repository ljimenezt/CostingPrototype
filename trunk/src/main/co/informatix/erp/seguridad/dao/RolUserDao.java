package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.security.entities.RolUsuario;
import co.informatix.security.entities.Usuario;

/**
 * DAO class that establishes the connection between business logic and
 * database. UsuarioAction used for managing roles in application users.
 * 
 * @author Gabriel.Moreno
 */
@Stateless
@SuppressWarnings("serial")
public class RolUserDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Returns a list of user roles in force or not in force for a certain role.
	 * 
	 * @param user
	 *            : User who wants to consult.
	 * @return List<RolUsuario>: List of user roles.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolUsuario> consultUserRole(Usuario user) throws Exception {
		return em
				.createQuery(
						"SELECT rs FROM RolUsuario rs "
								+ "JOIN FETCH rs.rolUsuarioPK.rol r "
								+ "JOIN FETCH rs.rolUsuarioPK.usuario u "
								+ "WHERE u=:user").setParameter("user", user)
				.getResultList();
	}

	/**
	 * Save a user role in the database.
	 * 
	 * @param userRole
	 *            : User Roles saved.
	 * @throws Exception
	 */
	public void saveUserRole(RolUsuario userRole) throws Exception {
		em.persist(userRole);
	}

	/**
	 * To edit a user's role.
	 * 
	 * @param userRole
	 *            : object editing.
	 * @throws Exception
	 */
	public void editUserRole(RolUsuario userRole) throws Exception {
		em.merge(userRole);
	}

}