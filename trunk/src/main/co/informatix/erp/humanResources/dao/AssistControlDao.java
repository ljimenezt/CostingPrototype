package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.AssistControl;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the assist control that may exist.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Stateless
public class AssistControlDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save the assistControl in the database.
	 * 
	 * @param assistControl
	 *            : assistControl to save.
	 * @throws Exception
	 */
	public void saveAssistControl(AssistControl assistControl) throws Exception {
		em.persist(assistControl);
	}

	/**
	 * Edit the assistControl in the database.
	 * 
	 * @param assistControl
	 *            : assistControl to edit.
	 * @throws Exception
	 */
	public void editAssistControl(AssistControl assistControl) throws Exception {
		em.merge(assistControl);
	}

	/**
	 * This method consult assist control list with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<AssistControl>: assist control list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AssistControl> consultAssistControl(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ac FROM AssistControl ac ");
		query.append("JOIN FETCH ac.hr h ");
		query.append(consult);
		query.append("ORDER BY h.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<AssistControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult if the worker is absent in the current day.
	 * 
	 * @param idHr
	 *            : Human resource identifier.
	 * @param date
	 *            : Date of the absent.
	 * @return AssistControl: AssistControl object found with the search
	 *         parameters idHr and date.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public AssistControl consultAssistControlByHrAndDate(int idHr, String date)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ac FROM AssistControl ac ");
		query.append("JOIN FETCH ac.hr h ");
		query.append("WHERE h.idHr = :idHr ");
		query.append("AND TO_CHAR(ac.date,'YYYY-MM-dd')= :date ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idHr", idHr);
		q.setParameter("date", date);
		List<AssistControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}

	/**
	 * This method allows get a list of the dates in a interval in the assist
	 * control table without repeat values
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @param source
	 *            : name of the entity that indicate the source of the human
	 *            resource.
	 * @return List<Date>: date assist control list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Date> consultAssistControlDates(StringBuilder consult,
			List<SelectItem> parameters, String source) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT TO_DATE(TO_CHAR(a.date,'YYYY-MM-dd'), 'YYYY-MM-dd') ");
		query.append("FROM " + source + " a ");
		query.append(consult);
		query.append("ORDER BY 1 ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<Date> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult the human resources relate with the control
	 * assist table.
	 * 
	 * @modify Andres.Gomez 08/11/2016
	 * 
	 * @param idHr
	 *            : identifier to search
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<AssistControl>: assist control list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<AssistControl> listHrOfAssistControl(int idHr,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT a FROM AssistControl a ");
		query.append("JOIN FETCH a.hr h ");
		query.append(consult);
		if (idHr != 0) {
			query.append("AND h.idHr = :idHr ");
		}
		Query q = em.createQuery(query.toString());
		if (idHr != 0) {
			q.setParameter("idHr", idHr);
		}
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		List<AssistControl> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows to consult the highest date according the dates sent
	 * by user.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return Date: Date found with the parameters sent.
	 * @throws Exception
	 */
	public Date consultMaxDate(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT MAX(a.date) FROM AssistControl a ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		if (q.getSingleResult() != null) {
			return (Date) q.getSingleResult();
		}
		return null;
	}

}