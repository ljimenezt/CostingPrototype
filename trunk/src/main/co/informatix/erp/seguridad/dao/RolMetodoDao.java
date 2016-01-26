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
 * This class manages access to data to record, view, edit and complete
 * observance of rolesMetodos
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class RolMetodoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method saves a rolMetodo
	 * 
	 * @param rolMetodo
	 *            : rolMethod to save
	 * @throws Exception
	 */
	public void guardarRolMetodo(RolMetodo rolMetodo) throws Exception {
		em.persist(rolMetodo);
	}

	/**
	 * This method edits the roles method
	 * 
	 * @param rolMetodo
	 *            : rolMetodo to edit
	 * @throws Exception
	 */
	public void editarRolMetodo(RolMetodo rolMetodo) throws Exception {
		em.merge(rolMetodo);
	}

	/**
	 * Method to consult the permissions associated with a role.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param rol
	 *            : Role to seek permits.
	 * @return: RolMetodo list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolMetodo> consultarRolMetodos(Rol rol) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT rm FROM RolMetodo rm ");
		query.append("WHERE rm.rolMetodoPK.rol=:rol ");
		query.append("AND rm.fechaFinVigencia IS NULL");
		Query q = em.createQuery(query.toString());
		q.setParameter("rol", rol);
		return q.getResultList();
	}

	/**
	 * Method to consult every method that has permissions associated with a
	 * role, regardless of its validity.
	 * 
	 * @author Gabriel.Moreno
	 * 
	 * @param rol
	 *            : Role permissions to find method.
	 * 
	 * @return List<RolMetodo>: List method role permissions, regardless of the
	 *         term.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<RolMetodo> consultarTodosRolMetodos(Rol rol) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT rm FROM RolMetodo rm ");
		query.append("WHERE rm.rolMetodoPK.rol=:rol ");
		Query q = em.createQuery(query.toString());
		q.setParameter("rol", rol);
		return q.getResultList();
	}

	/**
	 * This method allows you to check whether the method has a relationship
	 * with some role.
	 * 
	 * @author Marcela.Chaparro
	 * 
	 * @param idMetodo
	 *            : Identifier of the method to query the relationship.
	 * @return boolean: true if the method has an associated role, otherwise
	 *         false.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean relacionRolMetodo(int idMetodo) throws Exception {
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