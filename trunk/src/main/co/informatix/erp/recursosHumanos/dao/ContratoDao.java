package co.informatix.erp.recursosHumanos.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.recursosHumanos.entities.Contrato;

/**
 * Class DAO that establishes the connection between business logic and database
 * management contracts are handled in the payroll of the company.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ContratoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save a contract in BD.
	 * 
	 * @param contrato
	 *            : Contract to keep.
	 * @throws Exception
	 */
	public void guardarContrato(Contrato contrato) throws Exception {
		em.persist(contrato);
	}

	/**
	 * Edit the information of an existing contract in BD.
	 * 
	 * @param contrato
	 *            : Contract edited.
	 * @throws Exception
	 */
	public void editarContrato(Contrato contrato) throws Exception {
		em.merge(contrato);
	}

	/**
	 * Removes BD contract
	 * 
	 * @param contrato
	 *            : contract to remove
	 * @throws Exception
	 */
	public void eliminarContrato(Contrato contrato) throws Exception {
		em.remove(em.merge(contrato));
	}

	/**
	 * Method for querying an object of contract.
	 * 
	 * @param nomObject
	 *            : property that you want to query the object.
	 * @param idContrato
	 *            : id of the Contract to be consulted
	 * @return Contract Object query results.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoContrato(String nomObject, int idContrato)
			throws Exception {
		List<Object> result = (List<Object>) em
				.createQuery(
						"SELECT c." + nomObject + " FROM Contrato c "
								+ "WHERE c.id=:idContrato")
				.setParameter("idContrato", idContrato).getResultList();
		if (result.size() > 0) {
			return (Object) result.get(0);
		}
		return null;
	}

	/**
	 * Returns the number of existing contracts in the database information by
	 * filtering the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query for which contracts are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of contract records found
	 * @throws Exception
	 */
	public Long cantidadContratos(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(c) FROM Contrato c ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consultation contracts with a given range sent as a parameter
	 * and filtering the information by the values sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Contrato>: contracts list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Contrato> consultarContratos(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM Contrato c ");
		query.append(consult);
		query.append("ORDER BY c.id ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Contrato> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
