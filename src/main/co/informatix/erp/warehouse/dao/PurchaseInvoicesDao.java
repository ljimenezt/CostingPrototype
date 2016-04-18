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
	 * Modify a Purchase Invoices in the database.
	 * 
	 * @author Andres.Gomez
	 * @param invoices
	 *            : invoices to edit.
	 * @throws Exception
	 */
	public void editInvoices(PurchaseInvoices invoices) throws Exception {
		em.merge(invoices);
	}

	/**
	 * Saves a Invoices in the database.
	 * 
	 * @author Andres.Gomez
	 * @param invoices
	 *            : invoices to save.
	 * @throws Exception
	 */
	public void saveInvoices(PurchaseInvoices invoices) throws Exception {
		em.persist(invoices);
	}

	/**
	 * Returns the number of existing purchase invoices in the database
	 * filtering information search by the values sent.
	 * 
	 * @author Liseth.Jimenez
	 * @modify 29/03/2016 Wilhelm.Boada
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
		query.append("JOIN pi.suppliers s ");
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
	 * @modify 29/03/2016 Wilhelm.Boada
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
		query.append("JOIN FETCH pi.suppliers s ");
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

	/**
	 * Consult if the number of the invoice exist in the database when saving or
	 * editing.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param invoiceNumber
	 *            : number of the invoice to verify.
	 * @param idSupplier
	 *            :identifier of supplier associated with the purchase invoice
	 * @return boolean: Returns true if the relationship exists, false
	 *         otherwise.
	 * @throws Exception
	 */
	public boolean nameExists(String invoiceNumber, int idSupplier)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT pi FROM PurchaseInvoices pi ");
		query.append("JOIN FETCH pi.suppliers s ");
		query.append("WHERE UPPER(pi.invoiceNumber)=UPPER(:invoiceNumber) ");
		query.append("AND s.idSupplier =:idSupplier ");
		Query q = em.createQuery(query.toString());
		q.setParameter("invoiceNumber", invoiceNumber).setParameter(
				"idSupplier", idSupplier);
		if (q.getResultList().size() > 0) {
			return true;
		}
		return false;
	}
}