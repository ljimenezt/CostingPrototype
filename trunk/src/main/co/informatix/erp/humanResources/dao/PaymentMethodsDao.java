package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.PaymentMethods;

/**
 * DAO class that establishes the connection between business logic and base
 * data for managing the entity PaymentMethods
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class PaymentMethodsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a PaymentMethods in BD
	 * 
	 * @param paymentMethods
	 *            object of PaymentMethods class to store in the database
	 * @throws Exception
	 */
	public void guardaPaymentMethods(PaymentMethods paymentMethods)
			throws Exception {
		em.persist(paymentMethods);
	}

	/**
	 * Edits PaymentMethods in BD
	 * 
	 * @param paymentMethods
	 * @throws Exception
	 */
	public void editarPaymentMethods(PaymentMethods paymentMethods)
			throws Exception {
		em.merge(paymentMethods);
	}

	/**
	 * Removes PaymentMethods
	 * 
	 * @param paymentMethods
	 *            : Payment methods eliminate
	 * @throws Exception
	 */
	public void eliminarPaymentMethods(PaymentMethods paymentMethods)
			throws Exception {
		em.remove(em.merge(paymentMethods));
	}

	/**
	 * This method queries the PaymentMethods sent a certain range as a
	 * parameter and filtering the information by the values of search sent.
	 ** 
	 * @param inicio
	 *            : where it initiates the query record
	 * @param rango
	 *            : range of records
	 * @param consulta
	 *            : Consult the logs depending on the parameters selected by the
	 *            user.
	 * @param parametros
	 *            : query parameters.
	 * @return List<PaymentMethods>: List PaymentMethods types.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaymentMethods> consultarPaymentMethods(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT pm FROM  PaymentMethods pm ");
		query.append(consulta);
		query.append("ORDER BY pm.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<PaymentMethods> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing PaymentMethods in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consulta
	 *            : String containing the query why the filter types of
	 *            consumables.
	 * @param parametros
	 *            : query parameters.
	 * @return Long: number of records found PaymentMethods type
	 * @throws Exception
	 */
	public Long cantidadPaymentMethods(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(pm) FROM PaymentMethods pm ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consultation payments are current.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @return List<PaymentMethods>: list of current payment types found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaymentMethods> consultarPaymentMethodsVigentes()
			throws Exception {
		return em.createQuery(
				"SELECT pm FROM PaymentMethods pm " + "ORDER BY pm.name")
				.getResultList();
	}
}
