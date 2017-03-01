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
	 *            : Object recorded in the database.
	 * @throws Exception
	 */
	public void createFuelPurchase(FuelPurchase fuelPurcharse) throws Exception {
		em.persist(fuelPurcharse);
	}

	/**
	 * Consult the list of fuel purchase in the database.
	 * 
	 * @return List<FuelPurchase>: List of all Fuel Purchase found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelPurchase> consultFuelPurchase() throws Exception {
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
	 * @return FuelPurchase: Object Fuel Purchase found.
	 * @throws Exception
	 */
	public FuelPurchase fuelPurchaseById(int idFuelPurchase) throws Exception {
		return em.find(FuelPurchase.class, idFuelPurchase);
	}

	/**
	 * Consult if the number of the invoice exist in the database when saving.
	 * 
	 * @param invoiceNumber
	 *            : Number of the invoice to verify.
	 * @param idSupplier
	 *            : Identifier of supplier associated with the fuel purchase
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
