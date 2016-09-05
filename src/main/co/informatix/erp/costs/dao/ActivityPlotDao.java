package co.informatix.erp.costs.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.costs.entities.ActivityPlot;

/**
 * DAO class that establishes the connection between business logic and data
 * base information management activities and plots.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Stateless
public class ActivityPlotDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method save the relation between activity and plot.
	 * 
	 * @param activityPlot
	 *            : Relation between activity and plots.
	 * @throws Exception
	 */
	public void saveActivityPlot(ActivityPlot activityPlot) throws Exception {
		em.persist(activityPlot);
	}

	/**
	 * This method edit the relation between activity and plot.
	 * 
	 * @param activityPlot
	 *            : Relation between activity and plots.
	 * @throws Exception
	 */
	public void editActivityPlot(ActivityPlot activityPlot) throws Exception {
		em.merge(activityPlot);
	}

	/**
	 * This method delete the relation between activity and plot
	 * 
	 * @param activityPlot
	 *            : Relation between activity and plots.
	 * @throws Exception
	 */
	public void deleteActivityPlot(ActivityPlot activityPlot) throws Exception {
		em.remove(em.merge(activityPlot));
	}

	/**
	 * Returns the number of the relations between activities and plots existing
	 * in the database they are filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query why seep
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: number of plots associated to activity
	 * @throws Exception
	 */
	public Long amountPlotActivity(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(ap) FROM ActivityPlot ap ");
		queryBuilder.append("JOIN ap.activityPlotPK.activity a ");
		queryBuilder.append("JOIN ap.activityPlotPK.plot p ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * This method queries the relationship between activities and plots sending
	 * a certain range as a parameter and filtering information search for the
	 * values sent.
	 * 
	 * @param start
	 *            : The first record that is retrieved from the result.
	 * @param range
	 *            : Range of the records to be retrieved.
	 * @param query
	 *            : A query with record filters depending on the parameters
	 *            selected by the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<ActivityPlot>: List of plots associated to activity
	 *         associations found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ActivityPlot> queryActivityPlot(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ap FROM ActivityPlot ap ");
		queryBuilder.append("JOIN FETCH ap.activityPlotPK.activity a ");
		queryBuilder.append("JOIN FETCH a.activityName an ");
		queryBuilder.append("JOIN FETCH ap.activityPlotPK.plot p ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<ActivityPlot> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}