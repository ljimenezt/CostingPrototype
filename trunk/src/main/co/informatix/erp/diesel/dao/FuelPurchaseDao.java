package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.diesel.entities.FuelPurchase;

/**
 * This method takes care of the administration in the database of fuel purchase
 * 
 * @author Claudia.Rey
 */
@SuppressWarnings("serial")
@Stateless
public class FuelPurchaseDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method makes the registration of the fuel purchase in the database.
	 * 
	 * @param fuelPurcharse
	 *            : object recorded in the database.
	 * @throws Exception
	 */
	public void createFuelPurchase(FuelPurchase fuelPurcharse) throws Exception {
		em.persist(fuelPurcharse);
	}

	/**
	 * This method is the one to make the update in the database for a given
	 * fuel purchase
	 * 
	 * @param fuelPurcharse
	 *            : object to updating.
	 * @throws Exception
	 */
	public void editFuelPurchase(FuelPurchase fuelPurcharse) throws Exception {
		em.merge(fuelPurcharse);
	}

	/**
	 * Consult the list of fuel purchase in the database.
	 * 
	 * @return List<FuelPurchase>: list of all Fuel Purchase found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelPurchase> consultFuelUsage() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT fp FROM FuelPurchase fp ");
		Query q = em.createQuery(query.toString());
		return q.getResultList();
	}

	/**
	 * Consult a fuel purchase by id.
	 * 
	 * @param idFuelPurchase
	 *            : Identifier of fuel purchase.
	 * @return idFuelPurchase: Object Fuel Purchase found.
	 * @throws Exception
	 */
	public FuelPurchase FuelUsageById(int idFuelPurchase) throws Exception {
		return em.find(FuelPurchase.class, idFuelPurchase);
	}

	/**
	 * Consult if the number of the invoice exist in the database when saving or
	 * editing.
	 * 
	 * @param invoiceNumber
	 *            : number of the invoice to verify.
	 * @param idSupplier
	 *            :identifier of supplier associated with the fuel purchase
	 * @return FuelPurchase: Returns object of the fuel purchase.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public FuelPurchase numberExists(String invoiceNumber, Integer idSupplier)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT fp FROM FuelPurchase fp ");
		query.append("JOIN FETCH fp.supplier s ");
		query.append("WHERE UPPER(fp.invoiceNumber)=UPPER(:invoiceNumber) ");
		query.append("AND (s.idSupplier =:idSupplier) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("invoiceNumber", invoiceNumber);
		q.setParameter("idSupplier", idSupplier);
		List<FuelPurchase> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;

	}

}
