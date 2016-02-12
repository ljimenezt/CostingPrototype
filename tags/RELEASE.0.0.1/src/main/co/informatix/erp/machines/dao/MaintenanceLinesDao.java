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
 * Class DAO that establishes the connection between business logic and
 * database. MaintenanceLinesAction used for managing MaintenanceLines
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
	 * Edit the information of a maintenance lines records
	 * 
	 * @param maintenanceLines
	 *            : maintenance lines to edit.
	 * @throws Exception
	 */
	public void editarMaintenanceLines(MaintenanceLines maintenanceLines)
			throws Exception {
		em.merge(maintenanceLines);
	}

	/**
	 * Saved maintenance lines in the database
	 * 
	 * @param maintenanceLines
	 *            :lines Maintenance to save
	 * @throws Exception
	 */
	public void guardarMaintenanceLines(MaintenanceLines maintenanceLines)
			throws Exception {
		em.persist(maintenanceLines);
	}

	/**
	 * Returns the number of lines in the database maintenance existing
	 * filtering the information by the values sent search
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: Number of lines Maintenance records found
	 * @throws Exception
	 */
	public Long cantidadMaintenanceLines(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ml) FROM MaintenanceLines ml ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method query maintenance lines sent as a defined parameter range and
	 * filtering the information by the values sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<MaintenanceLines>: List lines maintenance
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaintenanceLines> consultarMaintenanceLines(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ml FROM MaintenanceLines ml ");
		query.append("JOIN FETCH ml.machines ");
		query.append("JOIN FETCH ml.maintenanceAndCalibration ");
		query.append(consult);
		query.append("ORDER BY ml.description ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MaintenanceLines> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of lines in the database maintenance existing
	 * filtering the information by the values sent search
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @param idMaintenance
	 *            : identifier of the maintenance to search
	 * @return Long: Number of lines Maintenance records found
	 * @throws Exception
	 */
	public Long amountMaintenanceLines(StringBuilder consult,
			List<SelectItem> parameters, int idMaintenance) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ml) FROM MaintenanceLines ml ");
		query.append("JOIN ml.maintenanceAndCalibration mc ");
		query.append("WHERE mc.idMaintenance =:idMaintenance ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaintenance", idMaintenance);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method that consult all activity and machine object and stores it in a
	 * list
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @param idMaintenance
	 *            : identifier of the maintenance to search
	 * @return List<MaintenanceLines>: Maintenance Lines list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaintenanceLines> listMaintenanceLinesXCalibration(int start,
			int range, StringBuilder consult, List<SelectItem> parameters,
			int idMaintenance) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ml FROM MaintenanceLines ml ");
		query.append("JOIN FETCH ml.maintenanceAndCalibration mc ");
		query.append("JOIN FETCH ml.machines m ");
		query.append("WHERE mc.idMaintenance =:idMaintenance ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaintenance", idMaintenance);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MaintenanceLines> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
