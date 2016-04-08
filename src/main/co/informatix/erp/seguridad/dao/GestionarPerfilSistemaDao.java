package co.informatix.erp.seguridad.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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
	 * Consult a profile system by id profile system by ConstantesErp.
	 * 
	 * @return PerfilSistema: object of profile system found by identifier.
	 * @throws Exception
	 */
	public PerfilSistema findProfileSystem() throws Exception {
		return em.find(PerfilSistema.class, ConstantesErp.PERFIL_SISTEMA_ID);
	}

}
