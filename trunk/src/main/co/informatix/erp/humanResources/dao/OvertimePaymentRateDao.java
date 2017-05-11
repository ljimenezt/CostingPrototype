package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.OvertimePaymentRate;

/**
 * DAO class that establishes the connection between business logic and database
 * for handling the OvertimePaymentRate entity.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class OvertimePaymentRateDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save a overtime payment rate in database.
	 * 
	 * @param overtimePaymentRate
	 *            : overtime payment rate to save.
	 * @throws Exception
	 */
	public void saveOvertimePaymentRate(OvertimePaymentRate overtimePaymentRate)
			throws Exception {
		em.persist(overtimePaymentRate);
	}

	/**
	 * Edits a overtime payment rate in database.
	 * 
	 * @param overtimePaymentRate
	 *            : edit overtime payment rate.
	 * @throws Exception
	 */
	public void editOvertimePaymentRate(OvertimePaymentRate overtimePaymentRate)
			throws Exception {
		em.merge(overtimePaymentRate);
	}

	/**
	 * Removes overtime payment Rate in database.
	 * 
	 * @param overtimePaymentRate
	 *            : overtime payment rate to remove.
	 * @throws Exception
	 */
	public void deleteOvertimePaymentRate(
			OvertimePaymentRate overtimePaymentRate) throws Exception {
		em.remove(em.merge(overtimePaymentRate));
	}

	/**
	 * Returns the number of existing overtime payment rate in the database
	 * filtering the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query why customers are filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of customer records found.
	 * @throws Exception
	 */
	public Long amountOvertimePaymentRate(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(op) FROM OvertimePaymentRate op ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method allows to consult the overtime payment rate determined range
	 * sent as a parameter range and filtering the information by the values
	 * sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<OvertimePaymentRate>: list of overtime payment rate.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OvertimePaymentRate> consultOvertimePaymentRate(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT op FROM OvertimePaymentRate op ");
		query.append(consult);
		query.append("ORDER BY op.overtimeRateType ASC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<OvertimePaymentRate> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult the OvertimePaymentRate for default rate.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param defaultRate
	 *            : parameter to search overtime payment rate.
	 * @return OvertimePaymentRate: OvertimePaymentRate object.
	 * @throws Exception
	 */
	public OvertimePaymentRate overtimePaymentRateXDefaultRate(
			Boolean defaultRate) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT op FROM OvertimePaymentRate op ");
		query.append("WHERE op.byDefault IS :defaultRate ");
		Query q = em.createQuery(query.toString());
		q.setParameter("defaultRate", defaultRate);
		return (OvertimePaymentRate) q.getSingleResult();
	}

	/**
	 * Consult the OvertimePaymentRate list.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @return List<OvertimePaymentRate>: OvertimePaymentRate list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<OvertimePaymentRate> listOvertimePaymentRate() throws Exception {
		Query q = em.createQuery("SELECT op FROM OvertimePaymentRate op ");
		return q.getResultList();
	}

	/**
	 * Consult overtime payment rate for Id.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param overtimePaymentRate
	 *            : id for Overtime Payment Rate.
	 * @return OvertimePaymentRate: OvertimePaymentRate object.
	 * @throws Exception
	 */
	public OvertimePaymentRate overtimePaymentRateXId(int overtimePaymentRateId)
			throws Exception {
		return em.find(OvertimePaymentRate.class, overtimePaymentRateId);
	}

	/**
	 * Consult if the name of the overtime payment rate exist in the database
	 * when saving or editing.
	 * 
	 * @author Jhair.Leal
	 * 
	 * @param overtimeRateType
	 *            : Name the overtime payment rate to verify.
	 * @param overtimepaymentid
	 *            : id the overtime payment rate to verify.
	 * @return OvertimePaymentRate: Object found with the search parameters id
	 *         and name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public OvertimePaymentRate nameExists(String overtimeRateType,
			int overtimepaymentid) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT op FROM OvertimePaymentRate op ");
		query.append("WHERE UPPER(op.overtimeRateType)=UPPER(:overtimeRateType) ");
		if (overtimepaymentid != 0) {
			query.append("AND op.overtimepaymentid <>:overtimepaymentid ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("overtimeRateType", overtimeRateType);
		if (overtimepaymentid != 0) {
			q.setParameter("overtimepaymentid", overtimepaymentid);
		}
		List<OvertimePaymentRate> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
