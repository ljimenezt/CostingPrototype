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
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class InvoiceItemsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Returns the number of existing invoices items in the database that are
	 * existing or not existing.
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
	 * @param start
	 *            : Registry where consultation begins
	 * @param range
	 *            : Range of records
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
	public List<InvoiceItems> consultInvoiceItems(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT it FROM InvoiceItems it ");
		query.append("JOIN FETCH it.material ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}
}
