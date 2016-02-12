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
 * 
 */
@Stateless
@SuppressWarnings("serial")
public class RolUsuarioDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Returns a list of user roles in force or not in force for a certain role.
	 * 
	 * @param usuario
	 *            : User who wants to consult.
	 * @return: List of user roles.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolUsuario> consultarUsuarioRoles(Usuario usuario)
			throws Exception {
		return em
				.createQuery(
						"SELECT rs FROM RolUsuario rs "
								+ "JOIN FETCH rs.rolUsuarioPK.rol r "
								+ "JOIN FETCH rs.rolUsuarioPK.usuario u "
								+ "WHERE u=:usuario")
				.setParameter("usuario", usuario).getResultList();
	}

	/**
	 * Save a user role in the database.
	 * 
	 * @param rolUsuario
	 *            : User Roles saved.
	 * @throws Exception
	 */
	public void guardarRolUsuario(RolUsuario rolUsuario) throws Exception {
		em.persist(rolUsuario);
	}

	/**
	 * To edit a user's role.
	 * 
	 * @param rolUsuario
	 *            : object editing.
	 */
	public void editarRolUsuario(RolUsuario rolUsuario) throws Exception {
		em.merge(rolUsuario);
	}

}