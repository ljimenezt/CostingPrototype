package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.security.entities.Rol;
import co.informatix.security.entities.Usuario;

/**
 * Allows the basic management (CRUD) for Role entity.
 * 
 * @author Oscar.Amaya
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class RolDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method queries a range of records of roles depending on the
	 * condition of validity.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param start
	 *            : start range.
	 * @param range
	 *            : number of records in the range.
	 * @param validityCondition
	 *            : validity condition.
	 * @param nameSearch
	 *            role name to search.
	 * @return List<Rol>: list of roles found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Rol> queryRoles(int start, int range, String validityCondition,
			String nameSearch) throws Exception {
		return em
				.createQuery(
						"SELECT r FROM Rol r WHERE r.fechaFinVigencia "
								+ validityCondition
								+ " AND UPPER(r.nombre) LIKE UPPER(:keyword) ORDER BY r.nombre")
				.setParameter("keyword", "%" + nameSearch + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * It counts roles in the database.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param validityCondition
	 *            : manage the condition of validity in the query.
	 * @param nameSearch
	 *            : role name to search.
	 * @return Long: Amount of roles in the database.
	 * @throws Exception
	 */
	public Long rolesAmount(String validityCondition, String nameSearch)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(r) FROM Rol r WHERE r.fechaFinVigencia "
								+ validityCondition
								+ " AND UPPER(r.nombre) LIKE UPPER(:keyword) ")
				.setParameter("keyword", "%" + nameSearch + "%")
				.getSingleResult();
	}

	/**
	 * Saves a new role in the database.
	 * 
	 * @param role
	 *            : role to save.
	 * @throws Exception
	 */
	public void saveRole(Rol role) throws Exception {
		em.persist(role);
	}

	/**
	 * Edit the information of an existing role.
	 * 
	 * @param role
	 *            : role to edit.
	 * @throws Exception
	 */
	public void editRole(Rol role) throws Exception {
		em.merge(role);
	}

	/**
	 * Query If the role name exists in the database before saving.
	 * 
	 * @param name
	 *            : name to verify
	 * @return: role object found with the name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Rol nameExists(String name) throws Exception {
		List<Rol> results = em.createQuery("FROM Rol WHERE nombre=:nombre")
				.setParameter("nombre", name).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Query if the role name exists in the database when editing.
	 * 
	 * @param name
	 *            : name in which you query the role.
	 * @param id
	 *            : id of the role object that is being edited.
	 * @return role object according to the name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Rol updateNameExists(String name, short id) throws Exception {
		List<Rol> results = em
				.createQuery("FROM Rol WHERE nombre=:nombre AND id <>:id")
				.setParameter("nombre", name).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Reports whether a role is being used or not.
	 * 
	 * @param id
	 *            : role identifier.
	 * @param relatedEntity
	 *            : entity that is related to the role.
	 * @return: true whether it is related or false otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean relatedRole(short id, String relatedEntity) throws Exception {

		List<Rol> roles = em
				.createQuery(
						"SELECT r FROM Rol r JOIN r." + relatedEntity
								+ " WHERE r.id=:id ").setParameter("id", id)
				.getResultList();
		if (roles.size() > 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * Returns a list of existing roles that are different from those assigned
	 * to the user.
	 * 
	 * @param user
	 *            : User roles which are queried.
	 * @return List<Rol>: list of filtered user roles records or all existing.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Rol> queryNotAssignedRoles(Usuario user) throws Exception {
		List<Rol> roles = em
				.createQuery(
						"SELECT rs.rolUsuarioPK.rol FROM RolUsuario rs "
								+ "WHERE rs.rolUsuarioPK.usuario=:usuario")
				.setParameter("usuario", user).getResultList();
		if (roles.size() > 0)
			return em
					.createQuery(
							"SELECT r FROM Rol r "
									+ "WHERE r.fechaFinVigencia IS NULL "
									+ "AND r NOT IN (:roles)")
					.setParameter("roles", roles).getResultList();
		else
			return em.createQuery(
					"SELECT r FROM Rol r WHERE r.fechaFinVigencia IS NULL")
					.getResultList();
	}
}
