package co.informatix.erp.diesel.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.diesel.entities.IrrigationDetails;

/**
 * DAO class that establishes the connection between business logic and data
 * base for irrigation_details.
 * 
 * @author Patricia.Patinio
 */
@SuppressWarnings("serial")
@Stateless
public class IrrigationDetailsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an irrigationDetails register in the database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to save.
	 * @throws Exception
	 */
	public void saveIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.persist(irrigationDetails);
	}

	/**
	 * Edit an irrigationDetails register in database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to edit.
	 * @throws Exception
	 */
	public void editIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.merge(irrigationDetails);
	}

	/**
	 * Remove an irrigationDetails register of the database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to remove.
	 * @throws Exception
	 */
	public void deleteIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.remove(em.merge(irrigationDetails));
	}

}
