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
 * DAO class that establishes the connection between business logic and database
 * for managing the entity PaymentMethods.
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
	 * Saves a PaymentMethods in the database.
	 * 
	 * @param paymentMethods
	 *            : Object of PaymentMethods class to store in the database.
	 * @throws Exception
	 */
	public void savePaymentMethods(PaymentMethods paymentMethods)
			throws Exception {
		em.persist(paymentMethods);
	}

	/**
	 * Edits PaymentMethods in the database.
	 * 
	 * @param paymentMethods
	 * @throws Exception
	 */
	public void editPaymentMethods(PaymentMethods paymentMethods)
			throws Exception {
		em.merge(paymentMethods);
	}

	/**
	 * Removes PaymentMethods from the database.
	 * 
	 * @param paymentMethods
	 *            : PaymentMethods to eliminate.
	 * @throws Exception
	 */
	public void deletePaymentMethods(PaymentMethods paymentMethods)
			throws Exception {
		em.remove(em.merge(paymentMethods));
	}

	/**
	 * This method queries the PaymentMethods for a certain range and filters
	 * the information with search values sent as parameters.
	 * 
	 * @param first
	 *            : The record where the result query starts.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query the payments depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<PaymentMethods>: List of PaymentMethods.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaymentMethods> searchPaymentMethods(int first, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT pm FROM  PaymentMethods pm ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY pm.name ");
		Query resultQuery = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			resultQuery
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		resultQuery.setFirstResult(first).setMaxResults(range);
		List<PaymentMethods> resultList = resultQuery.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing PaymentMethods in the database and it
	 * filters the information with search values sent as parameters.
	 * 
	 * @param query
	 *            : String containing the query filters.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of paymentMethods records that were found.
	 * @throws Exception
	 */
	public Long amountPaymentMethods(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(pm) FROM PaymentMethods pm ");
		queryBuilder.append(query);
		Query resultQuery = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			resultQuery
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) resultQuery.getSingleResult();
	}

	/**
	 * Look for available payment methods.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @return List<PaymentMethods>: List of current payment types found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PaymentMethods> queryPaymentMethods() throws Exception {
		return em.createQuery(
				"SELECT pm FROM PaymentMethods pm " + "ORDER BY pm.name")
				.getResultList();
	}

	/**
	 * Query whether the payment method name exists in the database when storing
	 * or editing.
	 * 
	 * @author Sergio.Gelves
	 * 
	 * @param name
	 *            : Name to verify the PaymentMethods.
	 * @param id
	 *            : Payment method id to verify
	 * @return paymentMethods: PaymentMethods object found with the search
	 *         parameters id and name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PaymentMethods nameExists(String name, int id) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT pt FROM PaymentMethods pt ");
		queryBuilder.append("WHERE UPPER(pt.name)=UPPER(:name) ");
		if (id != 0) {
			queryBuilder.append("AND pt.idPaymentMethod <>:idPaymentMethod ");
		}
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("name", name);
		if (id != 0) {
			query.setParameter("idPaymentMethod", id);
		}
		List<PaymentMethods> result = query.getResultList();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

}
