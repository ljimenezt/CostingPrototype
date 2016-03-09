package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.HrTypes;

/**
 * DAO class that establishes the connection between business logic and base
 * data. HrTypesAction manages HrTypes.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class HrTypesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the information for one type of human resources.
	 * 
	 * @param hrTypes
	 *            : Human resource type to edit.
	 * @throws Exception
	 */
	public void editHrTypes(HrTypes hrTypes) throws Exception {
		em.merge(hrTypes);
	}

	/**
	 * Save the kind of human resources in the database.
	 * 
	 * @param hrTypes
	 *            : Type of human resources to keep.
	 * @throws Exception
	 */
	public void saveHrTypes(HrTypes hrTypes) throws Exception {
		em.persist(hrTypes);
	}

	/**
	 * Removes a type of human resource from database.
	 * 
	 * @param hrTypes
	 *            : Type of human resources to eliminate.
	 * @throws Exception
	 */
	public void deleteHrTypes(HrTypes hrTypes) throws Exception {
		em.remove(em.merge(hrTypes));
	}

	/**
	 * Returns the number of human resources types existing existing in the
	 * database, they are filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query why the filter names types of
	 *            human resources.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of human resources types records found.
	 * @throws Exception
	 */
	public Long amountHrTypes(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(ht) FROM HrTypes ht ");
		queryBuilder.append(query);
		Query result = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			result.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) result.getSingleResult();
	}

	/**
	 * This method look for human resources types with a certain range sent as a
	 * parameter and the result has its information filtered with search values.
	 * 
	 * @param first
	 *            : where it initiates the query record
	 * @param range
	 *            : range of records
	 * @param query
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @return List<HrTypes>: list of the names of the types of resources
	 *         humans.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HrTypes> searchHrTypes(int first, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ht FROM HrTypes ht ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY ht.name ");
		Query result = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			result.setParameter(parameter.getLabel(), parameter.getValue());
		}
		result.setFirstResult(first).setMaxResults(range);
		List<HrTypes> resultList = result.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Query whether the type name exists in the human resources types database
	 * when storing or editing.
	 * 
	 * @param name
	 *            : Name to verify the human resources type.
	 * @param id
	 *            : Id of the human resources type to verify
	 * @return HrTypes: HrTypes object found with the search parameters id and
	 *         name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public HrTypes nameExists(String name, int id) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ht FROM HrTypes ht ");
		queryBuilder.append("WHERE UPPER(ht.name)=UPPER(:name) ");
		if (id != 0) {
			queryBuilder.append("AND ht.idHrType <>:idHrType ");
		}
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("name", name);
		if (id != 0) {
			query.setParameter("idHrType", id);
		}
		List<HrTypes> result = query.getResultList();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

	/**
	 * Query the types of human resources that are in force.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @return List<HrTypes>: List of existing human resources types found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HrTypes> queryHrTypes() throws Exception {
		return em.createQuery("SELECT ht FROM HrTypes ht ORDER BY ht.name")
				.getResultList();
	}
}
