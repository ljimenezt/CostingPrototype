package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
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
	 * Modify a Fuel Purchase in the database.
	 * 
	 * @param fuelPurcharse
	 *            : FuelPurcharse to edit.
	 * @throws Exception
	 */
	public void editFuelPurchase(FuelPurchase fuelPurchase) throws Exception {
		em.merge(fuelPurchase);
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

	/**
	 * Returns the number of existing FuelPurchase in the database filtering
	 * information search by the values sent.
	 * 
	 * @author Luna.Granados
	 * 
	 * @param consult
	 *            : String containing the query why the FuelPurchase filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found.
	 * @throws Exception
	 */
	public Long quantityFuelPurchase(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(fp) FROM FuelPurchase fp ");
		query.append("JOIN fp.supplier s ");
		query.append("JOIN fp.fuelType ft ");
		query.append("LEFT JOIN fp.ivaRate ir ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method of consultation with a range determining FuelPurchase sent as
	 * a parameter and filtering the information by the values sent search.
	 * 
	 * @author Luna.Granados
	 * @modify 12/04/2017 Fabian.Diaz
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<FuelPurchase>: List of Fuel Purchase.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<FuelPurchase> consultFuelPurchase(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT fp FROM FuelPurchase fp ");
		query.append("JOIN FETCH fp.supplier s ");
		query.append("JOIN FETCH fp.fuelType ft ");
		query.append("LEFT JOIN FETCH fp.ivaRate ir ");
		query.append(consult);
		query.append("ORDER BY fp.idFuelPurchase DESC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<FuelPurchase> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult the last record of FuelPurchase in the database.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @return FuelPurchase: object found
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public FuelPurchase consultLastFuelPurchase() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT fp FROM FuelPurchase fp ");
		query.append("WHERE fp.idFuelPurchase = ");
		query.append("(SELECT MAX(fp.idFuelPurchase) FROM FuelPurchase fp)");
		Query q = em.createQuery(query.toString());
		List<FuelPurchase> result = q.getResultList();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}
}
