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
	 *            : Registry where consultation begins
	 * @param range
	 *            : Range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user
	 * @param parameters
	 *            : Query parameters
	 * @return List<MeasurementUnits>: List of Measurement Units.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MeasurementUnits> consultarMeasurementUnits(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mu FROM  MeasurementUnits mu ");
		query.append(consult);
		query.append("ORDER BY mu.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MeasurementUnits> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Measurement Units stores a BD
	 * 
	 * @param measurementUnits
	 *            : MeasurementUnits to save.
	 * @throws Exception
	 */
	public void guardarMeasurementUnits(MeasurementUnits measurementUnits)
			throws Exception {
		em.persist(measurementUnits);
	}

	/**
	 * Edit a MeasurementUnits in the DB
	 * 
	 * @param measurementUnits
	 *            : MeasurementUnits to edit
	 * @throws Exception
	 */
	public void editarMeasurementUnits(MeasurementUnits measurementUnits)
			throws Exception {
		em.merge(measurementUnits);
	}

	/**
	 * Delete a MeasurementUnits in the DB
	 * 
	 * @param measurementUnits
	 *            : MeasurementUnits to delete
	 * @throws Exception
	 */
	public void eliminarMeasurementUnits(MeasurementUnits measurementUnits)
			throws Exception {
		em.remove(em.merge(measurementUnits));
	}

	/**
	 * Returns the number of existing BD MeasurementUnits in filtering the
	 * information by the values sent search.
	 * 
	 * @param consulta
	 *            : String containing the query why the MeasurementUnits
	 *            filtered.
	 * @param parametros
	 *            : query parameters.
	 * @return Long: number of records found Measurement Units
	 * @throws Exception
	 */
	public Long cantidadMeasurementUnits(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(mu) FROM MeasurementUnits mu ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the types of measurement units
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<MeasurementUnits>: List of measurement units
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MeasurementUnits> consultarMeasurementsUnits() throws Exception {
		return em.createQuery(
				"SELECT mu FROM MeasurementUnits mu " + "ORDER BY mu.name ")
				.getResultList();
	}
}
