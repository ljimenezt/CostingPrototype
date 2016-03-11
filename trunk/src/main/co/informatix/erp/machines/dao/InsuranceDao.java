package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.Insurance;

/**
 * DAO Class that establishes the connection between business logic and the
 * database for insurance management.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class InsuranceDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method builds the insurance query with a determined range sent as a
	 * parameter and filtered information with search values.
	 * 
	 * @param first
	 *            : The first record that the result retrieve.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Insurance>: List of Insurances.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Insurance> searchInsurances(int first, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ins FROM  Insurance ins ");
		queryBuilder.append("JOIN ins.machines m ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY ins.dateTime ");
		Query resultQuery = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			resultQuery
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		resultQuery.setFirstResult(first).setMaxResults(range);
		List<Insurance> resultList = resultQuery.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Saves an insurance in the database.
	 * 
	 * @param insurance
	 *            : Insurance to save.
	 * @throws Exception
	 */
	public void saveInsurance(Insurance insurance) throws Exception {
		em.persist(insurance);
	}

	/**
	 * Edits a database insurance.
	 * 
	 * @param insurance
	 *            : insurance to edit.
	 * @throws Exception
	 */
	public void editInsurance(Insurance insurance) throws Exception {
		em.merge(insurance);
	}

	/**
	 * Insurance to remove of the database.
	 * 
	 * @param insurance
	 *            : Eliminated insurance.
	 * @throws Exception
	 */
	public void deleteInsurance(Insurance insurance) throws Exception {
		em.remove(em.merge(insurance));
	}

	/**
	 * Returns the number of existing insurances in the database with its
	 * information filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query with filtered properties.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: Amount of insurance records found.
	 * @throws Exception
	 */
	public Long insurancesAmount(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(ins) FROM Insurance ins ");
		queryBuilder.append("JOIN ins.machines m ");
		queryBuilder.append(query);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method to calculate an insurance for a known machine and year.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idMachine
	 *            : Machine identifier to search.
	 * @param year
	 *            : Year to filter the insurance.
	 * 
	 * @return Double: Insurance value.
	 * @throws Exception
	 */
	public Double calculateInsurance(int idMachine, String year)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(ins.totalCostActual) FROM  Insurance ins ");
		query.append("JOIN ins.machines m ");
		query.append("WHERE TO_CHAR(ins.dateTime,'YYYY')= :year ");
		query.append("AND m.idMachine = :idMachine");
		Query q = em.createQuery(query.toString());
		q.setParameter("year", year);
		q.setParameter("idMachine", idMachine);
		if (q.getSingleResult() != null) {
			return (Double) q.getSingleResult();
		}
		return (0.0);
	}

}
