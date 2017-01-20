package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.Holiday;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the holiday entity.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Stateless
public class HolidayDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consult holiday filtering the information by the values of
	 * sent search.
	 * 
	 * @param date
	 *            : Date to the consult.
	 * @return boolean: It is true if there are holiday and false in another
	 *         case.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean consultHolidayByDate(String date) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT h FROM Holiday h ");
		query.append("WHERE TO_CHAR(h.date,'YYYY-MM-dd')= :date ");
		Query q = em.createQuery(query.toString());
		q.setParameter("date", date);
		List<Holiday> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return true;
		}
		return false;
	}
}