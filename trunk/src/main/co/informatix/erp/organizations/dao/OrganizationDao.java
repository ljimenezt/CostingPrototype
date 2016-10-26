package co.informatix.erp.organizations.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizations.entities.Organization;

/**
 * Class DAO that establishes the connection between business logic and
 * database. OrganizationAction used for the management of organizations and
 * individuals in organizations of any use
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@Stateless
public class OrganizationDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows organizations to consult the database
	 * 
	 * @modify Liseth.Jimenez 11/07/2012
	 * 
	 * @param start
	 *            : Number of first record where the query begins.
	 * @param range
	 *            : Number of the last record in the query.
	 * @param validityCondition
	 *            : condition to know whether existing or not existing are
	 *            queried.
	 * @param nameSearch
	 *            : Parameter name by which departments are filtered.
	 * @return List<Organization>: list of existing organizations in the
	 *         database, or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> consultOrganizations(int start, int range,
			String validityCondition, String nameSearch) throws Exception {
		return em
				.createQuery(
						"SELECT o FROM Organization o WHERE "
								+ "(UPPER(o.businessName) LIKE UPPER(:keyword) "
								+ "OR UPPER(o.nit) LIKE UPPER(:keyword)) "
								+ "AND o.dateEndValidity " + validityCondition
								+ " ORDER BY o.businessName ")
				.setParameter("keyword", "%" + nameSearch + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Returns the current list of organizations that exist in the database.
	 * 
	 * @return List of existing organizations in the database, or null
	 *         otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Organization> consultActiveOrganizations()
			throws Exception {
		return em.createQuery(
				"SELECT o FROM Organization o "
						+ "WHERE o.dateEndValidity IS NULL "
						+ "ORDER BY o.businessName").getResultList();
	}

	/**
	 * Allow consult the amount of existing organizations in the database
	 * 
	 * @modify Liseth.Jimenez 11/07/2012
	 * 
	 * @param conditionValidity
	 *            : to select existing or not existing records
	 * @param nameSearch
	 *            Word you want to search in the names of organizations and NIT
	 * @return Long: number of existing organizations in the database, or null
	 *         otherwise.
	 * @throws Exception
	 */
	public Long amountOrganizations(String conditionValidity,
			String nameSearch) throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(o) FROM Organization o WHERE "
								+ "(UPPER(o.businessName) LIKE UPPER(:keyword) "
								+ "OR UPPER(o.nit) LIKE UPPER(:keyword)) "
								+ "AND  o.dateEndValidity "
								+ conditionValidity)
				.setParameter("keyword", "%" + nameSearch + "%")
				.getSingleResult();
	}

	/**
	 * Save an organization in the database.
	 * 
	 * @param organization
	 *            : organization to save
	 * @throws Exception
	 */
	public void saveOrganization(Organization organization) throws Exception {
		em.persist(organization);
	}

	/**
	 * Modifies an organization in the database.
	 * 
	 * @param organization
	 *            : organization to change
	 * @throws Exception
	 */
	public void editOrganization(Organization organization) throws Exception {
		em.merge(organization);
	}

	/**
	 * Method that allows check if the value sent by parameter exists in the
	 * list of organizations database
	 * 
	 * @param valueConsult
	 *            : Value to check in the database
	 * @param wordCompare
	 *            : string that identifies what you want to consult, example:
	 *            businessName or NIT
	 * @return Organization: The Organization if that value exists or null otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Organization consultExist(String valueConsult, String wordCompare)
			throws Exception {
		List<Organization> results = em
				.createQuery(
						"FROM Organization o WHERE o." + wordCompare + "=:"
								+ wordCompare)
				.setParameter(wordCompare, valueConsult).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that allows check if the value sent by parameter exists in the
	 * list of organizations database unless the parameter sent by
	 * 
	 * @param valueConsult
	 *            : Value to consult in the database
	 * @param wordCompare
	 *            : string that identifies what you want to consult, example:
	 *            businessName or NIT
	 * @param id
	 *            : Organization ID is excluded
	 * @return: The Organization if that value exists or null otherwise
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Organization consultExistUpdate(String valueConsult,
			String wordCompare, int id) throws Exception {
		Query query = em
				.createQuery(
						"FROM Organization o WHERE o." + wordCompare + "=:"
								+ wordCompare + " AND o.id <>:id")
				.setParameter(wordCompare, valueConsult).setParameter("id", id);
		List<Organization> results = query.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that allows consulting the organization with the information on
	 * the document type to be loaded in edition
	 * 
	 * @param idOrganization
	 *            : id of the organization on application
	 * @return Organization: Purpose of the organization of the document or null
	 *         but there.
	 * @throws Exception
	 */
	public Organization consultOrganizationWithTypeDocument(int idOrganization)
			throws Exception {
		return (Organization) em
				.createQuery(
						"SELECT o FROM Organization o JOIN FETCH o.documentType "
								+ "WHERE o.id=:idOrganization")
				.setParameter("idOrganization", idOrganization)
				.getSingleResult();
	}
}