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
	public void editarPayments(Payments payments) throws Exception {
		em.merge(payments);
	}

	/**
	 * Save payment in the BD.
	 * 
	 * @param payments
	 *            : Pay to save
	 * @throws Exception
	 */
	public void guardarPayments(Payments payments) throws Exception {
		em.persist(payments);
	}

	/**
	 * Removes payment in BD.
	 * 
	 * @param payments
	 *            : payment to eliminate
	 * @throws Exception
	 */
	public void eliminarPayments(Payments payments) throws Exception {
		Payments payment = em.find(Payments.class, payments.getIdPayment());
		em.remove(em.merge(payment));
	}

	/**
	 * Returns the number of existing payments in the DB by filtering
	 * information sent search values.
	 * 
	 * @param consulta
	 *            : String containing the query why the filter payments.
	 * @param parametros
	 *            : query parameters.
	 * @return Long: amount of payment records found.
	 * @throws Exception
	 */
	public Long cantidadPayments(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(p) FROM Payments p ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This payment method consultation with a range determined sent as and
	 * filtering the information parameter values for search sent.
	 * 
	 * @param inicio
	 *            : where it initiates the query record
	 * @param rango
	 *            : range of records
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : query parameters.
	 * @return List <Payments> : list of payments.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Payments> consultarPayments(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM Payments p ");
		query.append(consulta);
		query.append("ORDER BY p.idPayment ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<Payments> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * 
	 Consult object assigned to a payment, considering that they are just
	 * those that are not null in the table
	 * 
	 * 
	 * @param nomObject
	 *            : object to check payment
	 * @param idPayment
	 *            : id payment is consulted
	 * @return Object information associated with the payment or null if not
	 *         present.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoPayments(String nomObject, int idPayment)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT p."
								+ nomObject
								+ " FROM Payments p WHERE p.idPayment=:idPayment")
				.setParameter("idPayment", idPayment).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}
