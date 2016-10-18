package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.security.entities.Metodo;

/**
 * DAO class that establishes the connection between business logic and
 * database. MetodoAction manages methods that have specific permissions on the
 * system
 * 
 * @author marisol.calderon
 * @modify 19/06/2014 Gabriel.Moreno
 */
@SuppressWarnings("serial")
@Stateless
public class MethodDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows to query methods in the database
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param start
	 *            : The first record that is retrieve of the query result.
	 * @param range
	 *            : Range of records.
	 * @param nameSearch
	 *            : Name that the method is going to query.
	 * @return List<Metodo>: List of methods that were found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Metodo> queryMethods(int start, int range, String nameSearch)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT m FROM Metodo m ");
		queryBuilder.append("WHERE m.fechaFinVigencia IS NULL ");
		if (nameSearch != null && !"".equals(nameSearch)) {
			queryBuilder.append(" AND UPPER(m.nombre) LIKE UPPER(:keyword) ");
		}
		queryBuilder.append(" ORDER BY m.nombre");

		Query queryResult = em.createQuery(queryBuilder.toString());
		if (nameSearch != null && !"".equals(nameSearch)) {
			queryResult.setParameter("keyword", "%" + nameSearch + "%");
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		return queryResult.getResultList();
	}

	/**
	 * Query the amount of existing methods in the database.
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param name
	 *            : Name that the method is going to query.
	 * @return Long: Number of methods that were found in the database.
	 * @throws Exception
	 */
	public Long methodsAmount(String name) throws Exception {
		StringBuilder query = new StringBuilder();

		query.append("SELECT COUNT(m) FROM Metodo m");
		query.append(" WHERE m.fechaFinVigencia IS NULL ");
		if (name != null && !"".equals(name)) {
			query.append(" AND UPPER(m.nombre) LIKE UPPER(:keyword) ");
		}

		Query queryResult = em.createQuery(query.toString());
		if (name != null && !"".equals(name)) {
			queryResult.setParameter("keyword", "%" + name + "%");
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * Save a method in the database.
	 * 
	 * @param method
	 *            : method to save.
	 * @throws Exception
	 */
	public void saveMethod(Metodo method) throws Exception {
		em.persist(method);
	}

	/**
	 * Modifies a method in the database.
	 * 
	 * @param method
	 *            : method to modify
	 * @throws Exception
	 */
	public void editMethod(Metodo method) throws Exception {
		em.merge(method);
	}

	/**
	 * Check if the method name exists in the database when it is being saved.
	 * 
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param name
	 *            : Name of the method to find.
	 * @param id
	 *            : Method identifier edition.
	 * @return Method: Method object that was found, otherwise null.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Metodo methodNameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Metodo m ");
		query.append("WHERE UPPER(m.nombre)=UPPER(:name) ");
		query.append(" AND m.fechaFinVigencia IS NULL ");
		if (id != 0) {
			query.append("AND m.id <>:id ");
		}
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("name", name);
		if (id != 0) {
			queryResult.setParameter("id", id);
		}
		List<Metodo> results = queryResult.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Check if the name of the action in the method exists in the database when
	 * it is being saved.
	 * 
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @param methodName
	 *            : Action name of the method to find.
	 * @param id
	 *            : ID of the method.
	 * @return Method: Method object that matches<s the action name, null
	 *         otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Metodo actionNameExists(String methodName, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Metodo m ");
		query.append("WHERE UPPER(m.nombreMetodo)=UPPER(:methodName) ");
		query.append(" AND m.fechaFinVigencia IS NULL ");
		if (id != 0) {
			query.append("AND m.id <>:id ");
		}
		Query queryResult = em.createQuery(query.toString());
		queryResult.setParameter("methodName", methodName);
		if (id != 0) {
			queryResult.setParameter("id", id);
		}
		List<Metodo> results = queryResult.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}