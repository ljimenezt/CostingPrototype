package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.utils.ConstantesErp;
import co.informatix.security.entities.PerfilSistema;

/**
 * class DAO that establishes the connection between the business logic and
 * database for handling the PerfilSistema entity.
 * 
 * @author Jhair.Leal
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class GestionarPerfilSistemaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method allows consulting profile system with a certain range sent as
	 * a parameter and filtering the information sent search values.
	 * 
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<PerfilSistema>: list of the profile system.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilSistema> consultProfileSystem(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ps FROM PerfilSistema ps ");
		query.append(consult);
		query.append("ORDER BY ps.emailServerUser ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<PerfilSistema> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a profile system for users in database.
	 * 
	 * @param perfilSistema
	 *            : profile system to save.
	 * @throws Exception
	 */
	public void saveProfileSystem(PerfilSistema perfilSistema) throws Exception {
		em.persist(perfilSistema);
	}

	/**
	 * Edit a profile system in database.
	 * 
	 * @param perfilSistema
	 *            : profile system to edit.
	 * @throws Exception
	 */
	public void editProfileSystem(PerfilSistema perfilSistema) throws Exception {
		em.merge(perfilSistema);
	}

	/**
	 * Removes a profile system in database.
	 * 
	 * @param perfilSistema
	 *            : Profile system eliminate.
	 * @throws Exception
	 */
	public void removeProfileSystem(PerfilSistema perfilSistema)
			throws Exception {
		em.remove(em.merge(perfilSistema));
	}

	/**
	 * Returns the number of existing profile systems in the database by
	 * filtering information sent lookup values.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of profile systems records found.
	 * @throws Exception
	 */
	public Long quantityProfileSystem(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ps) FROM PerfilSistema ps ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Profile system method that queries the object and saves it to a list.
	 * 
	 * @return List<PerfilSistema>: List of profile system.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PerfilSistema> listProfileSystem() throws Exception {
		Query q = em.createQuery("SELECT ps FROM PerfilSistema ps "
				+ " ORDER BY ps.emailServerUser ASC");
		return q.getResultList();

	}

	/**
	 * Consult a profile system by id profile system by ConstantesErp.
	 * 
	 * @return PerfilSistema: object of profile system found by identifier.
	 * @throws Exception
	 */
	public PerfilSistema findProfileSystem() throws Exception {
		return em.find(PerfilSistema.class, ConstantesErp.PERFIL_SISTEMA_ID);
	}

}
