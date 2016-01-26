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
 * Class DAO that establishes the connection between business logic and database
 * management Insurance (Insurance).
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
	 * This method build the consult the Insurance with determined range sent as
	 * a parameter filtering the information by the values sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Insurance>: List of Insurance.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Insurance> consultarInsurance(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ins FROM  Insurance ins ");
		query.append("JOIN ins.machines m ");
		query.append(consult);
		query.append("ORDER BY ins.dateTime ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Insurance> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Saves a DB insurance
	 * 
	 * @param insurance
	 *            : insurance saving.
	 * @throws Exception
	 */
	public void guardarInsurance(Insurance insurance) throws Exception {
		em.persist(insurance);
	}

	/**
	 * Edits a DB insurance
	 * 
	 * @param insurance
	 *            : insurance editing.
	 * @throws Exception
	 */
	public void editarInsurance(Insurance insurance) throws Exception {
		em.merge(insurance);
	}

	/**
	 * Insurance removes the database
	 * 
	 * @param insurance
	 *            : eliminate insurance
	 * @throws Exception
	 */
	public void eliminarInsurance(Insurance insurance) throws Exception {
		em.remove(em.merge(insurance));
	}

	/**
	 * Returns the number of existing insurance in the database by filtering
	 * information sent search values.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of insurance records found
	 * @throws Exception
	 */
	public Long cantidadInsurance(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ins) FROM Insurance ins ");
		query.append("JOIN ins.machines m ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method that consult all insurance object depend search criteria and
	 * stores it in a list
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idMachine
	 *            : Identifier of the machine to search
	 * @param year
	 *            : year to search a machine
	 * 
	 * @return List<ActivityMachine>: ActivityMachine list
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
