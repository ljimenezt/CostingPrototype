package co.informatix.erp.seguridad.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.seguridad.entities.SystemProfile;
import co.informatix.erp.utils.Constantes;

/**
 * DAO class that establishes the connection between business logic and database
 * in the table SystemProfile.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class SystemProfileDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method allow consult a systemProfile Object
	 * 
	 * @return object SystemProfile with the information
	 * @throws Exception
	 */
	public SystemProfile findSystemProfile() throws Exception {
		return em.find(SystemProfile.class, Constantes.SYSTEM_PROFILE_ID);
	}
}
