package co.informatix.erp.organizations.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizations.entities.Business;

/**
 * Class DAO that establishes the connection between business logic and database
 * to manage the Company entity.
 * 
 * @author Oscar.Amaya
 */
@SuppressWarnings("serial")
@Stateless
public class BusinessDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consults the amount of firms depending on the condition of
	 * validity
	 * 
	 * @modify: Gerson.Cespedes 23/05/2012
	 * @modify: Adonay.Mantilla 11/10/2012
	 * 
	 * @param conditionValidity
	 *            : condition that allows whether existing or not existing are
	 *            queried.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : parameters for the query.
	 * @return Long: number of existing companies in the database.
	 * @throws Exception
	 */
	public Long mountBusiness(String conditionValidity, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(b) FROM Business b WHERE b.dateEndValidity ");
		query.append(conditionValidity);
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries a range of companies depending on the condition of
	 * validity
	 * 
	 * @modify: Gerson.Cespedes 23/05/2012
	 * @modify: Adonay.Mantilla 11/10/2012
	 * 
	 * @param start
	 *            : Number of first record where the query begins.
	 * @param range
	 *            : Number of the last record in the query.
	 * @param conditionValidity
	 *            : condition to know whether existing or not existing are
	 *            queried.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : parameters for the query.
	 * @return List<Business>: Companies found or null if not present.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Business> consultBusiness(int start, int range,
			String conditionValidity, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT b FROM Business b WHERE b.dateEndValidity ");
		query.append(conditionValidity);
		query.append(consult);
		query.append(" ORDER BY b.name");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Check whether the NIT of a company already exists
	 * 
	 * @param nit
	 *            : NIT-check
	 * @param id
	 *            : id of the company to compare
	 * @return boolean to true if there is a NIT or false otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean nitExist(String nit, int id) throws Exception {
		List<Business> results = em
				.createQuery("FROM Business WHERE nit=:nit AND id <>:id")
				.setParameter("nit", nit).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * allows you to create a business database
	 * 
	 * @param business
	 *            : company store
	 * @throws Exception
	 */
	public void createBusiness(Business business) throws Exception {
		em.persist(business);
	}

	/**
	 * Allows a company to modify the database
	 * 
	 * @param business
	 *            : company to edit the database.
	 * @throws Exception
	 */
	public void modifyBusiness(Business business) throws Exception {
		em.merge(business);
	}

	/**
	 * See also article assigned to a company, considering that they are only
	 * those that are not null in the table
	 * 
	 * @modify 01/02/2012 marisol.calderon
	 * 
	 * @param nomObject
	 *            : in order to consult the company
	 * @param idBusiness
	 *            : Company ID being queried
	 * @return Object: information associated with the company or null but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectBusiness(String nomObject, int idBusiness)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT b." + nomObject
								+ " FROM Business b WHERE b.id=:idBusiness")
				.setParameter("idBusiness", idBusiness).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}

	/**
	 * Method that allows a list of objects depending on the condition sent as a
	 * parameter
	 * 
	 * @author marisol.calderon
	 * 
	 * @param nomObject
	 *            : name of the object to be found on the company
	 * @param idBusiness
	 *            : id of the company to consult
	 * @return List of Objects with information.
	 * @throws Exception
	 */
	public List<?> consultListObjectOfBusiness(String nomObject, int idBusiness)
			throws Exception {
		return em
				.createQuery(
						"SELECT b." + nomObject
								+ " FROM Business b WHERE b.id=:idBusiness")
				.setParameter("idBusiness", idBusiness).getResultList();
	}

	/**
	 * Returns the current list of companies that exist in the database
	 * 
	 * @author marisol.calderon
	 * 
	 * @return List<Business>: List of existing businesses.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Business> consultBusinessActive() throws Exception {
		return em.createQuery(
				"SELECT b FROM Business b "
						+ "WHERE b.dateEndValidity IS NULL "
						+ "ORDER BY b.name").getResultList();
	}

	/**
	 * Method that allows check the number of companies that have estates
	 * 
	 * @author marisol.calderon
	 * 
	 * @param conditionValidity
	 *            : condition to know whether existing or not existing records
	 *            are queried
	 * @return Long: Size the number of companies with estates.
	 * @throws Exception
	 */
	public Long amountBusinessWithEstates(String conditionValidity)
			throws Exception {
		return (Long) em.createQuery(
				"SELECT COUNT(DISTINCT b) FROM Business b "
						+ "JOIN b.haciendas WHERE b.dateEndValidity "
						+ conditionValidity).getSingleResult();
	}

	/**
	 * Method for consulting companies that have farms depending on the
	 * beginning and range.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param start
	 *            : Number of first record where the query begins.
	 * @param range
	 *            : Number of the last record in the query.
	 * @param validityCondition
	 *            : condition to know whether existing or not existing are
	 *            queried.
	 * @return List<Business>: list of companies with estates.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Business> consultBusinessWithEstates(int start, int range,
			String validityCondition) throws Exception {
		return em
				.createQuery(
						"SELECT DISTINCT b FROM Business b JOIN FETCH b.haciendas "
								+ "WHERE b.dateEndValidity "
								+ validityCondition + " ORDER BY b.name")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Allows to consult companies with its name or the word NIT sent as a
	 * parameter.
	 * 
	 * @author Gerson.Cespedes
	 * @modify 25/03/2012 marisol.calderon
	 * 
	 * @param nameSearch
	 *            : The word you want to search the names of the companies or
	 *            NIT.
	 * @return List<Business>: List of companies found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Business> searchBusinessForNameOrNit(String nameSearch)
			throws Exception {
		return em
				.createQuery(
						"SELECT DISTINCT(b) FROM Business b "
								+ "WHERE UPPER(b.name) LIKE UPPER(:keyword) "
								+ "OR UPPER(b.nit) LIKE UPPER(:keyword) "
								+ "AND b.dateEndValidity IS NULL "
								+ "ORDER BY b.name, b.nit")
				.setParameter("keyword", "%" + nameSearch + "%")
				.getResultList();
	}

	/**
	 * Consult a company by identifier.
	 * 
	 * @modify Liseth.Jimenez 02/15/2012
	 * 
	 * @param idBusiness
	 *            : identifier of the company that wants to consult.
	 * @return Business: purpose of the company whether or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Business getBusiness(int idBusiness) throws Exception {
		List<Business> results = em
				.createQuery("SELECT b FROM Business b WHERE b.id=:idBusiness")
				.setParameter("idBusiness", idBusiness).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}
}