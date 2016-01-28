package co.informatix.erp.services.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.services.entities.ServiceType;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the ServiceType entity.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class ServiceTypeDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a service type in the database
	 * 
	 * @param serviceType
	 *            : service type to save
	 * @throws Exception
	 */
	public void guardarServiceType(ServiceType serviceType) throws Exception {
		em.persist(serviceType);
	}

	/**
	 * Edit a service type in the database
	 * 
	 * @param serviceType
	 *            : service type to edit
	 * @throws Exception
	 */
	public void editarServiceType(ServiceType serviceType) throws Exception {
		em.merge(serviceType);
	}

	/**
	 * Removes a service type in the database
	 * 
	 * @param serviceType
	 *            : service type to removed
	 * @throws Exception
	 */
	public void eliminarServiceType(ServiceType serviceType) throws Exception {
		em.remove(em.merge(serviceType));
	}

	/**
	 * This method allows consulting service types with a certain range sent as
	 * a parameter and filtering the information sent search values.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<ServiceType>: list of types of service
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ServiceType> consultarServiceType(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ec FROM ServiceType ec ");
		query.append(consult);
		query.append("ORDER BY ec.descripcion ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<ServiceType> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing service types in the database information
	 * by filtering the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of activities records found
	 * @throws Exception
	 */
	public Long cantidadServiceType(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ec) FROM ServiceType ec ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Check out the types of services that are in the database.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<ServiceType>: list of types of services.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ServiceType> consultarServicesTypes() throws Exception {
		return em.createQuery(
				"SELECT st FROM ServiceType st ORDER BY st.descripcion ")
				.getResultList();
	}

}
