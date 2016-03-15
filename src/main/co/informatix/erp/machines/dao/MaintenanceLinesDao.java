package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.MaintenanceLines;

/**
 * Class DAO that implements the connection between business logic and database.
 * MaintenanceLinesAction manages MaintenanceLines.
 * 
 * @author Mabell.Boada
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class MaintenanceLinesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the information of maintenance lines records.
	 * 
	 * @param maintenanceLines
	 *            : Maintenance lines to edit.
	 * @throws Exception
	 */
	public void editMaintenanceLines(MaintenanceLines maintenanceLines)
			throws Exception {
		em.merge(maintenanceLines);
	}

	/**
	 * Save maintenance lines in the database.
	 * 
	 * @param maintenanceLines
	 *            : Maintenance lines to save.
	 * @throws Exception
	 */
	public void saveMaintenanceLines(MaintenanceLines maintenanceLines)
			throws Exception {
		em.persist(maintenanceLines);
	}

	/**
	 * Returns the amount of maintenance lines in the database; they are
	 * filtered with search values.
	 * 
	 * @param query
	 *            : String that contains the query where properties have
	 *            filters.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Amount of maintenance lines records found.
	 * @throws Exception
	 */
	public Long maintenanceLinesAmount(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(ml) FROM MaintenanceLines ml ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * This method queries maintenance lines with a certain range and they are
	 * filtered with search values.
	 * 
	 * @param start
	 *            : First record of the query result that is retrieved.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<MaintenanceLines>: List maintenance lines.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaintenanceLines> queryMaintenanceLines(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ml FROM MaintenanceLines ml ");
		queryBuilder.append("JOIN FETCH ml.machines ");
		queryBuilder.append("JOIN FETCH ml.maintenanceAndCalibration ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY ml.description ");
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MaintenanceLines> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the amount of maintenance lines in the database for a known
	 * maintenanceAndCalibration identifier, but it is done without a JOIN
	 * FETCH.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param query
	 *            : String containing the query that has filters.
	 * @param parameters
	 *            : Query parameters.
	 * @param idMaintenance
	 *            : Identifier of the maintenance lines to search.
	 * @return Long: Amount of lines Maintenance records found.
	 * @throws Exception
	 */
	public Long amountMaintenanceLines(StringBuilder query,
			List<SelectItem> parameters, int idMaintenance) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(ml) FROM MaintenanceLines ml ");
		queryBuilder.append("JOIN ml.maintenanceAndCalibration mc ");
		queryBuilder.append("WHERE mc.idMaintenance =:idMaintenance ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idMaintenance", idMaintenance);
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * Method that queries all activity and machine objects and it stores them
	 * in a list.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param start
	 *            : First record of the query result that is retrieved.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @param idMaintenance
	 *            : identifier of the maintenance line to search.
	 * 
	 * @return List<MaintenanceLines>: Maintenance Lines list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaintenanceLines> listMaintenanceLinesXCalibration(int start,
			int range, StringBuilder query, List<SelectItem> parameters,
			int idMaintenance) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT ml FROM MaintenanceLines ml ");
		queryBuilder.append("JOIN FETCH ml.maintenanceAndCalibration mc ");
		queryBuilder.append("JOIN FETCH ml.machines m ");
		queryBuilder.append("WHERE mc.idMaintenance =:idMaintenance ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		queryResult.setParameter("idMaintenance", idMaintenance);
		for (SelectItem parameter : parameters) {
			queryResult
					.setParameter(parameter.getLabel(), parameter.getValue());
		}
		queryResult.setFirstResult(start).setMaxResults(range);
		List<MaintenanceLines> resultList = queryResult.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
