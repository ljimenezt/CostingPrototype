package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.PurchaseInvoices;

/**
 * DAO class that establishes the connection between business logic and
 * database. PurchaseInvoicesAction used for managing PurchaseInvoices.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class PurchaseInvoicesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method allow consult all PurchaseInvoices stored in data base
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return List<PurchaseInvoices>: all PurchaseInvoices stored in data base
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PurchaseInvoices> consultPurchaseInvoices() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM PurchaseInvoices p ");
		Query q = em.createQuery(query.toString());
		return q.getResultList();
	}
}
