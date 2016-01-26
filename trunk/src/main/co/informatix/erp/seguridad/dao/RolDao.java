package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.security.entities.Rol;
import co.informatix.security.entities.Usuario;

/**
 * Allows the basic management Role (CRUD) entity.
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
	 * condition of validity
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param start
	 *            : start range
	 * @param range
	 *            : number of records in the range
	 * @param condicionVigencia
	 *            : validity condition
	 * @param nombreBuscar
	 *            role name to search
	 * @return List<Rol>: list of roles found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Rol> consultarRoles(int start, int range,
			String condicionVigencia, String nombreBuscar) throws Exception {
		return em
				.createQuery(
						"SELECT r FROM Rol r WHERE r.fechaFinVigencia "
								+ condicionVigencia
								+ " AND UPPER(r.nombre) LIKE UPPER(:keyword) ORDER BY r.nombre")
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Allows for roles in the database.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param condicionVigencia
	 *            : manage the condition of validity in consultation.
	 * @param nombreBuscar
	 *            role name to search
	 * @return Long: Record number of roles in the database.
	 * @throws Exception
	 */
	public Long cantidadRoles(String condicionVigencia, String nombreBuscar)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(r) FROM Rol r WHERE r.fechaFinVigencia "
								+ condicionVigencia
								+ " AND UPPER(r.nombre) LIKE UPPER(:keyword) ")
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.getSingleResult();
	}

	/**
	 * Saves a new role in BD
	 * 
	 * @param rol
	 *            :role to save
	 * @throws Exception
	 */
	public void guardarRol(Rol rol) throws Exception {
		em.persist(rol);
	}

	/**
	 * Edit the information of an existing role
	 * 
	 * @param rol
	 *            : role to edit
	 * @throws Exception
	 */
	public void editarRol(Rol rol) throws Exception {
		em.merge(rol);
	}

	/**
	 * Consultation If the role name exists in the database when you save
	 * 
	 * @param nombre
	 *            : name to verify
	 * @return Rol: role object found with the name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Rol nombreExiste(String nombre) throws Exception {
		List<Rol> results = em.createQuery("FROM Rol WHERE nombre=:nombre")
				.setParameter("nombre", nombre).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consultation If the role name exists in the database when editing
	 * 
	 * @param nombre
	 *            : name you want to query the role.
	 * @param id
	 *            : id of the role being edited.
	 * @return Rol: role object found with the name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Rol nombreExisteActualizar(String nombre, short id) throws Exception {
		List<Rol> results = em
				.createQuery("FROM Rol WHERE nombre=:nombre AND id <>:id")
				.setParameter("nombre", nombre).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Reports whether a role is being used
	 * 
	 * @param id
	 *            : role identifier
	 * @param entidadRelacionada
	 *            : entity that is related to the role.
	 * @return: true whether this related or false otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean rolRelacionado(short id, String entidadRelacionada)
			throws Exception {

		List<Rol> roles = em
				.createQuery(
						"SELECT r FROM Rol r JOIN r." + entidadRelacionada
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
	 * @param usuario
	 *            : User roles which are queried.
	 * @return List<Rol>: list of user roles whether or all existing roles.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Rol> consultarRolesNoAsignados(Usuario usuario)
			throws Exception {
		List<Rol> roles = em
				.createQuery(
						"SELECT rs.rolUsuarioPK.rol FROM RolUsuario rs "
								+ "WHERE rs.rolUsuarioPK.usuario=:usuario")
				.setParameter("usuario", usuario).getResultList();
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
