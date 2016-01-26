package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.security.entities.Usuario;

/**
 * DAO class that establishes the connection between business logic and
 * database. UsuarioAction used for managing users and roles in the application
 * users.
 * 
 * @author Gabriel.Moreno
 * 
 */
@Stateless
@SuppressWarnings("serial")
public class UsuarioDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows users consult the database.
	 * 
	 * @modify Liseth Jimenez 19/06/2012
	 * 
	 * @param start
	 *            : Start of registration.
	 * @param range
	 *            : End in the range of records to consult.
	 * @param condicionVigencia
	 *            : To select existing or not existing registrations.
	 * @param nombreBuscar
	 *            Parameter name
	 * 
	 * @return User list found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> consultarUsuarios(int start, int range,
			String condicionVigencia, String nombreBuscar) throws Exception {
		return em
				.createQuery(
						"SELECT u FROM Usuario u "
								+ "WHERE UPPER(u.nombreUsuario) LIKE UPPER(:keyword) "
								+ "AND u.fechaFinVigencia " + condicionVigencia
								+ " ORDER BY u.nombreUsuario")
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Allows consult the amount of existing users in the database.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param condicionVigencia
	 *            :to select existing or not existing registrations.
	 * @param nombreBuscar
	 *            User name to search
	 * 
	 * @return Long: number of users.
	 * @throws Exception
	 */
	public Long cantidadUsuarios(String condicionVigencia, String nombreBuscar)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(u) FROM Usuario u "
								+ "WHERE UPPER(u.nombreUsuario) LIKE UPPER(:keyword)"
								+ " AND  u.fechaFinVigencia "
								+ condicionVigencia)
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.getSingleResult();
	}

	/**
	 * Consultation If the user name exists in the database.
	 * 
	 * @param nombreUsuario
	 *            : User name to query.
	 * @return: List<Usuario>: Null if no user name in the database or the user
	 *          having the user name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario nombreUsuarioExiste(String nombreUsuario) throws Exception {
		List<Usuario> results = em
				.createQuery(
						"FROM Usuario u WHERE u.nombreUsuario=:nombreUsuario")
				.setParameter("nombreUsuario", nombreUsuario).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consultation If the user name of a specific user exists in the database.
	 * 
	 * @param nombreUsuario
	 *            : User name to query.
	 * @param id
	 *            : user identifier.
	 * @return: null if there is no such user name in the database or the user
	 *          having the user name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario nombreUsuarioExiste(String nombreUsuario, Integer id)
			throws Exception {
		List<Usuario> results = em
				.createQuery(
						"FROM Usuario u WHERE u.nombreUsuario=:nombreUsuario "
								+ "AND u.id <>:id")
				.setParameter("nombreUsuario", nombreUsuario)
				.setParameter("id", id).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Save a user in the database.
	 * 
	 * @param usuario
	 *            : user to save.
	 * @throws Exception
	 */
	public void guardarUsuario(Usuario usuario) throws Exception {
		em.persist(usuario);
	}

	/**
	 * Modify a user in the database.
	 * 
	 * @param usuario
	 *            : user change.
	 * @throws Exception
	 */
	public void editarUsuario(Usuario usuario) throws Exception {
		em.merge(usuario);
	}

	/**
	 * Allows check the user associated with a user name
	 * 
	 * @param userName
	 *            : User name to be consulted.
	 * 
	 * @return Usuario: User object that casts the query.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario consultarUsuario(String userName) throws Exception {
		List<Usuario> usuarios = em
				.createQuery(
						"SELECT u FROM Usuario u "
								+ "WHERE u.nombreUsuario=:userName")
				.setParameter("userName", userName).getResultList();

		if (usuarios.size() > 0) {
			return usuarios.get(0);
		}
		return null;
	}

	/**
	 * Allows the user to consult related to the person in the database.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param idPersona
	 *            : Person identifier related to the user.
	 * @return Usuario: user object found whether or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario consultarUsuarioPersona(int idPersona) throws Exception {
		List<Usuario> results = em
				.createQuery(
						"SELECT p.usuario FROM Persona p "
								+ "WHERE p.id=:idPersona ")
				.setParameter("idPersona", idPersona).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}