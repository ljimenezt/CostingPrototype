package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.humanResources.entities.ChargeType;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the ChargeType entity.
 * 
 * @author Oscar.Amaya
 */
@SuppressWarnings("serial")
@Stateless
public class ChargeTypeDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows consult the amount of jobs types that match the query condition
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param validityCondition
	 *            : sets the condition of validity (existing or not existing)
	 *            records on request.
	 * @param variableSearch
	 *            : Word you want to search the records of types of jobs.
	 * @return Long Numbers of types of existing office in the database.
	 * @throws Exception
	 */
	public Long countChargeType(String validityCondition, String variableSearch)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(ct) FROM ChargeType ct "
								+ "WHERE (UPPER(ct.name) LIKE UPPER (:keyword) "
								+ " OR UPPER(ct.functions) LIKE UPPER (:keyword)) "
								+ " AND ct.dateEndValidity "
								+ validityCondition)
				.setParameter("keyword", "%" + variableSearch + "%")
				.getSingleResult();
	}

	/**
	 * Allows to consult to the database a list of types of functions that take
	 * his name as a parameter sent word 'variableSearch'
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param start
	 *            : Starting the record
	 * @param range
	 *            : End in the range of records to check
	 * @param validityCondition
	 *            : sets the condition of validity of the records to see
	 * @param variableSearch
	 *            : Word you want to search the records of types of jobs
	 * @return List<TypeCharge>: Word you want to search the records of types of
	 *         jobs
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ChargeType> searchChargeTypes(int start, int range,
			String validityCondition, String variableSearch) throws Exception {
		return em
				.createQuery(
						"SELECT ct FROM ChargeType ct "
								+ "WHERE (UPPER(ct.name) LIKE UPPER (:keyword) "
								+ " OR UPPER(ct.functions) LIKE UPPER (:keyword)) "
								+ " AND ct.dateEndValidity "
								+ validityCondition + " ORDER BY ct.name")
				.setParameter("keyword", "%" + variableSearch + "%")
				.setFirstResult(start).setMaxResults(range).getResultList();
	}

	/**
	 * Check if the name of a type of jobs is already registered
	 * 
	 * @modify 15/03/2012 marisol.calderon
	 * 
	 * @param name
	 *            : name to verify
	 * @return Object of the type of job already exists
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ChargeType nameExists(String name) throws Exception {
		List<ChargeType> results = em
				.createQuery("FROM ChargeType WHERE name=:name")
				.setParameter("name", name).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Check if the name of a type of jobs is already registered
	 * 
	 * @modify 15/03/2012 marisol.calderon
	 * 
	 * @param name
	 *            : name to verify
	 * @param id
	 *            :identifier of type jobs
	 * @return Object of the type of job already exists
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ChargeType nameExists(String name, int id) throws Exception {
		List<ChargeType> results = em
				.createQuery("FROM ChargeType WHERE name=:name AND id <>:id")
				.setParameter("name", name).setParameter("id", id)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method for creating a type of job in the database.
	 * 
	 * @param chargeType
	 *            : type of job to save
	 * @throws Exception
	 */
	public void saveChargeType(ChargeType chargeType) throws Exception {
		em.persist(chargeType);
		em.flush();
	}

	/**
	 * Allows you to modify the basic information of a type of jobs
	 * 
	 * @param chargeType
	 *            : type of job to change
	 * @throws Exception
	 */
	public void editChargeType(ChargeType chargeType) throws Exception {
		em.merge(chargeType);
		em.flush();
	}
}