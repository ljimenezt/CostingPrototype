package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.CostTypes;

/**
 * DAO class that establishes the connection between business logic and base
 * data for managing the types of cost.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class CostTypesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save a type of cost in the database
	 * 
	 * @param costTypes
	 *            : type of cost saving.
	 * @throws Exception
	 */
	public void guardarCostTypes(CostTypes costTypes) throws Exception {
		em.persist(costTypes);
	}

	/**
	 * Edit a type of cost in the database
	 * 
	 * @param costTypes
	 *            : cost type to edit.
	 * @throws Exception
	 */
	public void editarCostTypes(CostTypes costTypes) throws Exception {
		em.merge(costTypes);
	}

	/**
	 * Removes a type of cost in the database
	 * 
	 * @param costTypes
	 *            : cost type to remove.
	 * @throws Exception
	 */
	public void eliminarCostTypes(CostTypes costTypes) throws Exception {
		em.remove(em.merge(costTypes));
	}

	/**
	 * Returns the amount of such costs in the BD existing filtering information
	 * search by the values sent.
	 * 
	 * @param consulta
	 *            : String containing the query why the filter assignments.
	 * @param parametros
	 *            : query parameters.
	 * @return long: number of records found such costs.
	 * @throws Exception
	 */
	public Long cantidadCostTypes(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(c) FROM CostTypes c ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the type of costs sent to a particular range as a
	 * parameter and filtering the information by the values of search sent.
	 * 
	 * @param inicio
	 *            : where it initiates the consultation record
	 * @param rango
	 *            : range of records
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : Query parameters.
	 * @return List<CostTypes>:list of types of costs
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CostTypes> consultarCostTypes(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM CostTypes c ");
		query.append(consulta);
		query.append("ORDER BY c.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<CostTypes> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult article assigned to a type of cost depending on the parameters
	 * sent.
	 * 
	 * @param nomObject
	 *            : object to consult the type of cost
	 * @param idCostTypes
	 *            : id cost type which can be consulted.
	 * @return Object: Object with consultation.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoCostTypes(String nomObject, int idCostTypes)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT m." + nomObject + " FROM CostTypes m "
								+ "WHERE m.id=:idCostTypes")
				.setParameter("idCostTypes", idCostTypes).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
