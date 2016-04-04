package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.MeasurementUnits;

/**
 * DAO class that establishes the connection between business logic and database
 * management MeasurementUnits.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MeasurementUnitsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method of consultation with a range determining MeasurementUnits
	 * sent as a parameter and filtering the information by the values sent
	 * search.
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
	 * @return List<MeasurementUnits>: List of Measurement Units.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MeasurementUnits> consultMeasurementUnits(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mu FROM  MeasurementUnits mu ");
		query.append(consult);
		query.append("ORDER BY mu.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MeasurementUnits> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Measurement Units stores a BD.
	 * 
	 * @param measurementUnits
	 *            : MeasurementUnits to save.
	 * @throws Exception
	 */
	public void saveMeasurementUnits(MeasurementUnits measurementUnits)
			throws Exception {
		em.persist(measurementUnits);
	}

	/**
	 * Edit a MeasurementUnits in the DB.
	 * 
	 * @param measurementUnits
	 *            : MeasurementUnits to edit.
	 * @throws Exception
	 */
	public void editMeasurementUnits(MeasurementUnits measurementUnits)
			throws Exception {
		em.merge(measurementUnits);
	}

	/**
	 * Delete a MeasurementUnits in the DB.
	 * 
	 * @param measurementUnits
	 *            : MeasurementUnits to delete.
	 * @throws Exception
	 */
	public void removeMeasurementUnits(MeasurementUnits measurementUnits)
			throws Exception {
		em.remove(em.merge(measurementUnits));
	}

	/**
	 * Returns the number of existing BD MeasurementUnits in filtering the
	 * information by the values sent search.
	 * 
	 * @param consult
	 *            : String containing the query why the MeasurementUnits
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found Measurement Units.
	 * @throws Exception
	 */
	public Long quantityMeasurementUnits(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(mu) FROM MeasurementUnits mu ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the types of measurement units.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<MeasurementUnits>: List of measurement units.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MeasurementUnits> consultMeasurementsUnits() throws Exception {
		return em.createQuery(
				"SELECT mu FROM MeasurementUnits mu " + "ORDER BY mu.name ")
				.getResultList();
	}

	/**
	 * Consultation if the name of the measurent units there in the database
	 * when storing or editing.
	 * 
	 * @author Jhair.Leal
	 * 
	 * @param name
	 *            : measurent units name to verify.
	 * @param id
	 *            : identifier harvest to verify.
	 * @return MeasurementUnits: MeasurementUnits object found with the search
	 *         parameters name and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MeasurementUnits nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mu FROM MeasurementUnits mu ");
		query.append("WHERE UPPER(mu.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND mu.idMeasurementUnits <>:idMeasurementUnits ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idMeasurementUnits", id);
		}
		List<MeasurementUnits> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * It returns a measurement unit according to a specified ID.
	 * 
	 * @param id
	 *            : The identifier corresponding to a unit.
	 * @return The MeasurementUnit Object.
	 */
	public MeasurementUnits measurementUnitByID(int id) {
		return em.find(MeasurementUnits.class, id);
	}
}
