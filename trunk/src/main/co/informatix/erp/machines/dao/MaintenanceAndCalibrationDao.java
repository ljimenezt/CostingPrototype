package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.MaintenanceAndCalibration;

/**
 * Class DAO that establishes the connection between business logic and
 * database. MaintenanceAndCalibrationAction used for managing
 * MaintenanceAndCalibration.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MaintenanceAndCalibrationDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the information of a maintenance and calibration record.
	 * 
	 * @param maintenanceAndCalibration
	 *            : Maintenance and calibration to edit.
	 * @throws Exception
	 */
	public void editMaintenanceAndCalibration(
			MaintenanceAndCalibration maintenanceAndCalibration)
			throws Exception {
		em.merge(maintenanceAndCalibration);
	}

	/**
	 * Save the maintenance and calibration in the database.
	 * 
	 * @param maintenanceAndCalibration
	 *            :Maintenance and calibration to save.
	 * @throws Exception
	 */
	public void saveMaintenanceAndCalibration(
			MaintenanceAndCalibration maintenanceAndCalibration)
			throws Exception {
		em.persist(maintenanceAndCalibration);
	}

	/**
	 * Returns the number of existing maintenance and calibration in the
	 * database by filtering the information by the values sent search.
	 * 
	 * @modify 13/11/2015 cristhian.pico
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: Number of calibration and maintenance records found.
	 * @throws Exception
	 */
	public Long quantityMaintenanceAndCalibration(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(mc) FROM MaintenanceAndCalibration mc ");
		query.append("LEFT JOIN mc.machines m ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method queries the maintenance and calibration with a range defined
	 * as a parameter sent and filtering the information by the values sent
	 * search.
	 * 
	 * @modify 13/11/2015 cristhian.pico
	 * @modify 04/12/2015 Andres.Gomez
	 * 
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<MaintenanceAndCalibration>: List of maintenance and
	 *         calibration.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaintenanceAndCalibration> consultMaintenanceAndCalibration(
			int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mc FROM MaintenanceAndCalibration mc ");
		query.append("JOIN FETCH mc.machines m ");
		query.append(consult);
		query.append("ORDER BY mc.dateTime DESC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MaintenanceAndCalibration> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method allows consult the last maintenance date of specific machine.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idMachine
	 *            : identifier machine to consult the last maintenance date.
	 * @return Date: date to the last maintenance.
	 */
	public Date lastMaintenance(int idMachine) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT MAX(mc.dateTime) FROM MaintenanceAndCalibration mc ");
		query.append("JOIN mc.machines m ");
		query.append("WHERE m.idMachine = :idMachine");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMachine", idMachine);
		if (q.getSingleResult() != null) {
			return (Date) q.getSingleResult();
		}
		return null;
	}

	/**
	 * Consult a record of the class by identifier MaintenanceLines.
	 * 
	 * @param id
	 *            : MaintenanceLines primary key class.
	 * @return MaintenanceAndCalibration: Object Maintenance and calibration.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MaintenanceAndCalibration> maintenanceCalibrationXId(int id)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mc FROM MaintenanceAndCalibration mc ");
		query.append("JOIN FETCH mc.machines m  ");
		query.append("WHERE m.idMachine=:id  ");
		Query q = em.createQuery(query.toString());
		q.setParameter("id", id);
		return q.getResultList();
	}

	/**
	 * Method that consult all maintenance object depend search criteria and
	 * stores it in a list.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idMachine
	 *            : Identifier of the machine to search.
	 * @param year
	 *            : year to search a machine.
	 * @return Double: double with the plus of the cost actual
	 * @throws Exception
	 */
	public Double calculateMaintenance(int idMachine, String year)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(mc.totalCostActual) FROM  MaintenanceAndCalibration mc ");
		query.append("JOIN mc.machines m ");
		query.append("WHERE TO_CHAR(mc.dateTime,'YYYY')= :year ");
		query.append("AND m.idMachine = :idMachine");
		Query q = em.createQuery(query.toString());
		q.setParameter("year", year);
		q.setParameter("idMachine", idMachine);
		if (q.getSingleResult() != null) {
			return (Double) q.getSingleResult();
		}
		return (0.0);
	}
}