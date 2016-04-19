package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.IvaRate;

/**
 * This class in responsible for the administration in the database of the
 * IvaRate.
 * 
 * @author Jhair.Leal
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class IvaRateDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method makes the record in the database a new IvaRate.
	 * 
	 * @param ivaRate
	 *            : iva rate to be recorded in the database.
	 * 
	 * @throws Exception
	 */
	public void saveIvaRate(IvaRate ivaRate) throws Exception {
		em.persist(ivaRate);
	}

	/**
	 * This method makes the update on the IvaRate in the database information
	 * system.
	 * 
	 * @param ivaRate
	 *            : iva rate to update.
	 * 
	 * @throws Exception
	 */
	public void editIvaRate(IvaRate ivaRate) throws Exception {
		em.merge(ivaRate);
	}

	/**
	 * Method to search the IvaRate for id.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param ivaRate
	 *            : Identifier ivaRate
	 * @return IvaRate: ivaRate Object
	 * @throws Exception
	 */
	public IvaRate ivaRateXId(int ivaRate) throws Exception {
		return em.find(IvaRate.class, ivaRate);
	}

	/**
	 * This method of consultation with a range determining iva rate sent as a
	 * parameter and filtering the information by the values sent search.
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<IvaRate>: List of iva rate.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<IvaRate> consultIvaRate(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ir FROM IvaRate ir ");
		query.append(consult);
		query.append("ORDER BY ir.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<IvaRate> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing in database IvaRate in filtering the
	 * information by the values sent search.
	 * 
	 * @param consult
	 *            : String containing the query why the iva rate filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found iva rate.
	 * @throws Exception
	 */
	public Long quantityIvaRate(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ir) FROM IvaRate ir ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the iva rates.
	 * 
	 * @return List<IvaRate>: List of iva rate.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<IvaRate> consultIvaRate() throws Exception {
		return em.createQuery(
				"SELECT ir FROM IvaRate ir " + "ORDER BY ir.name ")
				.getResultList();
	}

	/**
	 * Delete a iva rate in the database.
	 * 
	 * @param ivaRate
	 *            : ivaRate to delete.
	 * @throws Exception
	 */
	public void removeIvaRate(IvaRate ivaRate) throws Exception {
		em.remove(em.merge(ivaRate));
	}

	/**
	 * Consultation if the name of the iva rate there in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : iva rate name to verify.
	 * @param id
	 *            : identifier iva rate to verify.
	 * @return IvaRate: iva rate object found with the search parameters name
	 *         and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public IvaRate nameExists(String name, int idIva) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ir FROM IvaRate ir ");
		query.append("WHERE UPPER(ir.name)=UPPER(:name) ");
		if (idIva != 0) {
			query.append("AND ir.idIva <>:idIva ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (idIva != 0) {
			q.setParameter("idIva", idIva);
		}
		List<IvaRate> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consultation if the percentage of the iva rate there in the database when
	 * storing or editing.
	 * 
	 * @param rate
	 *            : iva rate percentage to verify.
	 * @param id
	 *            : identifier iva rate to verify.
	 * @return IvaRate: iva rate object found with the search parameters rate
	 *         and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public IvaRate rateExists(Double rate, int idIva) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ir FROM IvaRate ir ");
		query.append("WHERE ir.rate=:rate ");
		if (idIva != 0) {
			query.append("AND ir.idIva <>:idIva ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("rate", rate);
		if (idIva != 0) {
			q.setParameter("idIva", idIva);
		}
		List<IvaRate> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}
