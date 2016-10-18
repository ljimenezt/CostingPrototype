package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.security.entities.Metodo;
import co.informatix.security.entities.Rol;
import co.informatix.security.entities.RolMetodo;

/**
 * This class manages the access to data to record, view, edit and complete
 * observance of rolesMethods.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@Stateless
public class RoleMethodDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method saves a rolMethod.
	 * 
	 * @param roleMethod
	 *            : roleMethod to save.
	 * @throws Exception
	 */
	public void saveRoleMethod(RolMetodo roleMethod) throws Exception {
		em.persist(roleMethod);
	}

	/**
	 * This method edits the rolMethods.
	 * 
	 * @param roleMethod
	 *            : roleMethod to edit.
	 * @throws Exception
	 */
	public void editRoleMethod(RolMetodo roleMethod) throws Exception {
		em.merge(roleMethod);
	}

	/**
	 * Method to consult the permissions associated with a role.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param role
	 *            : Role to seek its permissions.
	 * @return List<RolMetodo>: RoleMetodo list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolMetodo> queryRolMethods(Rol role) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT rm FROM RolMetodo rm ");
		query.append("WHERE rm.rolMetodoPK.rol=:role ");
		query.append("AND rm.fechaFinVigencia IS NULL");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("role", role);
		return queryResult.getResultList();
	}

	/**
	 * Method to look for every method that has permissions associated with a
	 * role, regardless of its validity.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param role
	 *            : Role permissions to find the corresponding method.
	 * @return List<RolMetodo>: List of roleMethod permissions, regardless of
	 *         the term.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolMetodo> queryAllRolMethods(Rol role) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT rm FROM RolMetodo rm ");
		query.append("WHERE rm.rolMetodoPK.rol=:role ");
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("role", role);
		return queryResult.getResultList();
	}

	/**
	 * This method checks if a method has any relations with the roles.
	 * 
	 * @author Marcela.Chaparro
	 * 
	 * @param idMetodo
	 *            : Identifier of the method to query the relations.
	 * @return boolean: true if the method has an associated role, false
	 *         otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean rolMethodRelation(int idMetodo) throws Exception {
		List<Metodo> results = em
				.createQuery(
						"SELECT rm FROM RolMetodo rm "
								+ "WHERE rm.rolMetodoPK.metodo.id=:id "
								+ "AND rm.fechaFinVigencia IS NULL")
				.setParameter("id", idMetodo).getResultList();
		if (results.size() > 0) {
			return true;
		}
		return false;
	}
}