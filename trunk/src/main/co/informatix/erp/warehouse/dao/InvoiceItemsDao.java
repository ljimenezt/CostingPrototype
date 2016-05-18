package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.InvoiceItems;

/**
 * DAO class that establishes the connection between business logic and
 * database. InvoiceItemsAction used for managing InvoiceItems.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Stateless
public class InvoiceItemsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an invoiceItem in database
	 * 
	 * @param invoiceItem
	 *            : invoiceItem to save.
	 * @throws Exception
	 */
	public void saveInvoiceItem(InvoiceItems invoiceItem) throws Exception {
		em.persist(invoiceItem);
	}

	/**
	 * Edit an invoiceItem in database
	 * 
	 * @param invoiceItem
	 *            : invoiceItem to edit.
	 * @throws Exception
	 */
	public void editInvoiceItem(InvoiceItems invoiceItem) throws Exception {
		em.merge(invoiceItem);
	}

	/**
	 * Eliminates a invoiceItem of the BD.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param invoiceItem
	 *            : Eliminate invoiceItem.
	 * @throws Exception
	 */
	public void removeInvoiceItems(InvoiceItems invoiceItem) throws Exception {
		em.remove(em.merge(invoiceItem));
	}

	/**
	 * Returns the number of existing invoices items in the database that are
	 * existing or not existing.
	 * 
	 * @modify 06/04/2016 Andres.Gomez
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Long: quantity of registers.
	 * @throws Exception
	 */
	public Long quantityInvoiceItems(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(it) FROM InvoiceItems it ");
		query.append("JOIN it.purchaseInvoice pi ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the list of invoices items that comply with the option of force.
	 * 
	 * @modify 06/04/2016 Andres.Gomez
	 * @modify 11/04/2016 Gerardo.Herrera
	 * @modify 18/04/2016 Wilhelm.Boada
	 * 
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user
	 * @param parameters
	 *            : Query parameters
	 * @return List<InvoiceItems>:List of invoices items that comply with the
	 *         condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<InvoiceItems> consultInvoiceItems(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT it FROM InvoiceItems it ");
		query.append("JOIN FETCH it.material m ");
		query.append("JOIN FETCH it.purchaseInvoice pi ");
		query.append("LEFT JOIN FETCH it.ivaRate i ");
		query.append("JOIN FETCH m.measurementUnits ");
		query.append("JOIN FETCH pi.suppliers ");
		query.append(consult);
		query.append("ORDER BY m.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return q.getResultList();
	}

	/**
	 * Consult the total values of invoices items according to a purchase
	 * invoice
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idPurchaseInvoice
	 *            : Identifier of the purchase invoice to filter the consult
	 * @param item
	 *            :parameter to consult the value of the one purchase invoice
	 * @return Double: value double of the parameter item
	 * @throws Exception
	 */
	public Double consultValuesItems(int idPurchaseInvoice, String item)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(it." + item + ") ");
		query.append("FROM InvoiceItems it ");
		query.append("JOIN  it.purchaseInvoice pi ");
		query.append("WHERE pi.idPurchaseInvoice =:idPurchaseInvoice ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idPurchaseInvoice", idPurchaseInvoice);
		return (Double) q.getSingleResult();
	}

	/**
	 * Consult the invoice item from purchase invoice and material.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param invoiceNumber
	 *            : Invoice number.
	 * @param idMaterial
	 *            : Material identifier.
	 * @return invoiceItems: invoice item.
	 */
	@SuppressWarnings("unchecked")
	public InvoiceItems invoiceItemByMaterial(String invoiceNumber,
			int idMaterial) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ii FROM InvoiceItems ii ");
		query.append("JOIN FETCH ii.purchaseInvoice pi ");
		query.append("WHERE pi.invoiceNumber LIKE :invoiceNumber ");
		query.append("AND ii.material.idMaterial = :idMaterial ");
		Query q = em.createQuery(query.toString());
		q.setParameter("invoiceNumber", invoiceNumber);
		q.setParameter("idMaterial", idMaterial);
		List<InvoiceItems> invoiceItems = q.getResultList();
		if (invoiceItems.size() > 1) {
			return null;
		}
		return invoiceItems.get(0);
	}

}
