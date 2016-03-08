package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
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

	/**
	 * This method allow consult all PurchaseInvoices stored in data base
	 * associated with a supplier
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param idSupplier
	 *            : Supplier identifier for consult to purchase invoices
	 *            associated
	 * @return List<PurchaseInvoices>: all PurchaseInvoices stored in data base
	 *         for a supplier
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PurchaseInvoices> consultInvoicesBySupplier(int idSupplier)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM PurchaseInvoices p ");
		query.append("WHERE p.suppliers.id=:idSupplier ");
		query.append("ORDER BY p.dateTime ");
		Query q = em.createQuery(query.toString()).setParameter("idSupplier",
				idSupplier);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing purchase invoices in the database
	 * filtering information search by the values sent.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param consult
	 *            : String containing the query why the filter purchase
	 *            invoices.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of purchase invoices records found.
	 * @throws Exception
	 */
	public Long countPurchaseInvoices(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(pi) FROM PurchaseInvoices pi ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method purchase invoices consulting with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param start
	 *            : where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Consult records the parameters depending selected by the
	 *            user.
	 * @param parameters
	 *            : query parameters.
	 * @return List<PurchaseInvoices>: List of purchase invoices.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PurchaseInvoices> listPurchaseInvoices(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT pi FROM PurchaseInvoices pi ");
		query.append("JOIN FETCH pi.suppliers ");
		query.append(consult);
		query.append("ORDER BY pi.invoiceNumber ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<PurchaseInvoices> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}