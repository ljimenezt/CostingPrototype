package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.ContractType;

/**
 * DAO class that establishes the connection between business logic and
 * database. ContractTypeAction used for managing ContractType
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ContractTypeDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for one type of contract
	 * 
	 * @param contractType
	 *            : Contract to edit
	 * @throws Exception
	 */
	public void editarContractType(ContractType contractType) throws Exception {
		em.merge(contractType);
	}

	/**
	 * 
	 Saves the type of contract in the database
	 * 
	 * @param contractType
	 *            : Contract to save
	 * @throws Exception
	 */
	public void guardarContractType(ContractType contractType) throws Exception {
		em.persist(contractType);
	}

	/**
	 * Removes a type of contract BD
	 * 
	 * @param contractType
	 *            : Contract to remove
	 * @throws Exception
	 */
	public void eliminarContractType(ContractType contractType)
			throws Exception {
		em.remove(em.merge(contractType));
	}

	/**
	 * Returns the number of existing contract types in the database filtering
	 * information search by the values sent
	 * 
	 * @param consulta
	 *            : String containing the query why the filter names types of
	 *            contract
	 * @param parametros
	 *            : Query parameters
	 * @return Long: Number of records found types of contract
	 * @throws Exception
	 */
	public Long cantidadContractType(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ct) FROM ContractType ct ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation contract types with a certain range sent as a
	 * parameter and filtering the information by the values of sent search
	 * 
	 * @param inicio
	 *            : Registry where consultation begins
	 * @param rango
	 *            : Range of records
	 * @param consulta
	 *            : Consult the logs depending on the parameters selected by the
	 *            user
	 * @param parametros
	 *            : Query parameters
	 * @return List<ContractType>: List of contract types
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ContractType> consultarContractType(int inicio, int rango,
			StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM ContractType ct ");
		query.append(consulta);
		query.append("ORDER BY ct.nombre ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<ContractType> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult if the name of the contract types exist in the database when
	 * saving or editing
	 * 
	 * @param nombre
	 *            : Name the type of contract to verify
	 * @param id
	 *            : id the type of contract to verify
	 * @return ContractType: Object found with the search parameters id and name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public ContractType nombreExiste(String nombre, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM ContractType ct ");
		query.append("WHERE UPPER(ct.nombre)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND ct.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", nombre);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<ContractType> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
