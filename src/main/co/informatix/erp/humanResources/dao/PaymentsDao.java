package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.Payments;

/**
 * DAO class that establishes the connection between business logic and
 * database. PaymentsAction used for the management of payments.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class PaymentsDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the record information for payment.
	 * 
	 * @param payments
	 *            : edit payment.
	 * @throws Exception
	 */
	public void editPayments(Payments payments) throws Exception {
		em.merge(payments);
	}

	/**
	 * Save payment in the BD.
	 * 
	 * @param payments
	 *            : Pay to save.
	 * @throws Exception
	 */
	public void savePayments(Payments payments) throws Exception {
		em.persist(payments);
	}

	/**
	 * Removes payment in BD.
	 * 
	 * @param payments
	 *            : payment to eliminate.
	 * @throws Exception
	 */
	public void removePayments(Payments payments) throws Exception {
		Payments payment = em.find(Payments.class, payments.getIdPayment());
		em.remove(em.merge(payment));
	}

	/**
	 * Returns the number of existing payments in the DB by filtering
	 * information sent search values.
	 * 
	 * @param consult
	 *            : String containing the query why the filter payments.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of payment records found.
	 * @throws Exception
	 */
	public Long quantityPayments(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(p) FROM Payments p ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This payment method consultation with a range determined sent as and
	 * filtering the information parameter values for search sent.
	 * 
	 * @param start
	 *            : where it initiates the query record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @return List <Payments> : list of payments.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Payments> consultPayments(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM Payments p ");
		query.append(consult);
		query.append("ORDER BY p.idPayment ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Payments> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult object assigned to a payment, considering that they are just
	 * those that are not null in the table.
	 * 
	 * @param namObject
	 *            : object to check payment.
	 * @param idPayment
	 *            : id payment is consulted.
	 * @return Object information associated with the payment or null if not
	 *         present.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectPayments(String namObject, int idPayment)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT p."
								+ namObject
								+ " FROM Payments p WHERE p.idPayment=:idPayment")
				.setParameter("idPayment", idPayment).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}
