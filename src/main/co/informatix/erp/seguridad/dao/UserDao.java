package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.security.entities.Usuario;

/**
 * DAO class that establishes the connection between business logic and
 * database. UsuarioActionmanages users and roles in the application users.
 * 
 * @author Gabriel.Moreno
 */
@Stateless
@SuppressWarnings("serial")
public class UserDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Query users in the database.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param start
	 *            : The first record that is retrieve of the query result.
	 * @param range
	 *            : Range of records.
	 * @param validity
	 *            : To select existing or not existing records.
	 * @param nameSearch
	 *            : Name that the method is going to query.
	 * @return List<Usuario>: Users list found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Usuario> queryUsers(int start, int range, String validity,
			String nameSearch) throws Exception {
		return em
				.createQuery(
						"SELECT u FROM Usuario u "
								+ "WHERE UPPER(u.nombreUsuario) LIKE UPPER(:keyword) "
								+ "AND u.fechaFinVigencia " + validity
								+ " ORDER BY u.nombreUsuario")
				.setParameter("keyword", "%" + nameSearch + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Query the amount of existing users in the database.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param validity
	 *            : To select existing or not existing registrations.
	 * @param nameSearch
	 *            : User name to search.
	 * @return Long: Amount of users.
	 * @throws Exception
	 */
	public Long usersAmount(String validity, String nameSearch)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(u) FROM Usuario u "
								+ "WHERE UPPER(u.nombreUsuario) LIKE UPPER(:keyword)"
								+ " AND  u.fechaFinVigencia " + validity)
				.setParameter("keyword", "%" + nameSearch + "%")
				.getSingleResult();
	}

	/**
	 * Query a user name in the database, then it retrieves the corresponding
	 * user record.
	 * 
	 * @param userName
	 *            : User name to query.
	 * @return User: It is only null if there is no such user name in the
	 *         database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario userNameExists(String userName) throws Exception {
		List<Usuario> results = em
				.createQuery("FROM Usuario u WHERE u.nombreUsuario=:userName")
				.setParameter("userName", userName).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Query if the user name and id of a specific user exists in the database,
	 * then it retrieves the corresponding user record.
	 * 
	 * @param userName
	 *            : User name to query.
	 * @param id
	 *            : User identifier.
	 * @return User: It is only null if there is no such user name in the
	 *         database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario userNameExists(String userName, Integer id) throws Exception {
		List<Usuario> results = em
				.createQuery(
						"FROM Usuario u WHERE u.nombreUsuario=:userName "
								+ "AND u.id <>:id")
				.setParameter("userName", userName).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Save a user in the database.
	 * 
	 * @param user
	 *            : user to save.
	 * @throws Exception
	 */
	public void saveUser(Usuario user) throws Exception {
		em.persist(user);
	}

	/**
	 * Modify a user in the database.
	 * 
	 * @param user
	 *            : user change.
	 * @throws Exception
	 */
	public void editUser(Usuario user) throws Exception {
		em.merge(user);
	}

	/**
	 * Checks if the user name is associated to a user.
	 * 
	 * @param userName
	 *            : User name to be queried.
	 * @return User: User object that casts the query.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario searchUsuario(String userName) throws Exception {
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
	 * Query the user that is related to the person in the database.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param idPerson
	 *            : Person identifier that is related to the user.
	 * @return User: User object found or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Usuario searchPersonUser(int idPerson) throws Exception {
		List<Usuario> results = em
				.createQuery(
						"SELECT p.user FROM Person p "
								+ "WHERE p.id=:idPerson ")
				.setParameter("idPerson", idPerson).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}