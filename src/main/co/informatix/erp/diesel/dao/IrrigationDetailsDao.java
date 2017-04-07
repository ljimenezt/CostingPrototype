package co.informatix.erp.diesel.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.diesel.entities.IrrigationDetails;

/**
 * DAO class that establishes the connection between business logic and data
 * base for irrigation_details.
 * 
 * @author Patricia.Patinio
 */
@SuppressWarnings("serial")
@Stateless
public class IrrigationDetailsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an irrigationDetails register in the database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to save.
	 * @throws Exception
	 */
	public void saveIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.persist(irrigationDetails);
	}

	/**
	 * Edit an irrigationDetails register in database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to edit.
	 * @throws Exception
	 */
	public void editIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.merge(irrigationDetails);
	}

	/**
	 * Remove an irrigationDetails register of the database.
	 * 
	 * @param irrigationDetails
	 *            : irrigationDetails object to remove.
	 * @throws Exception
	 */
	public void deleteIrrigationDetails(IrrigationDetails irrigationDetails)
			throws Exception {
		em.remove(em.merge(irrigationDetails));
	}

	/**
	 * Returns the number of rows that exist in the database that are existing
	 * or not existing.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param consult
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long quantityIrrigationDetails(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(id) FROM IrrigationDetails id ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult the list of irrigation details that comply with the option of
	 * force.
	 * 
	 * @author Liseth.Jimenez
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
	 * @return List<IrrigationDetails>:List of irrigation details that comply
	 *         with the condition of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<IrrigationDetails> consultIrrigationDetails(int start,
			int range, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM IrrigationDetails id ");
		query.append("LEFT JOIN FETCH id.engineLog el ");
		query.append("LEFT JOIN FETCH id.machine m ");
		query.append("LEFT JOIN FETCH id.zone z ");
		query.append(consult);
		query.append(" ORDER BY el.date DESC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Consult the list of irrigation details for identifier
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param idEngine
	 *            : identifier that receive for the consult
	 * @return IrrigationDetails: return an object of type IrrigationDetails
	 * @throws Exception
	 */
	public IrrigationDetails consultIrrigationDetails(int idEngine)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT id FROM IrrigationDetails id ");
		query.append("LEFT JOIN FETCH id.engineLog el ");
		query.append("LEFT JOIN FETCH id.machine m ");
		query.append("LEFT JOIN FETCH id.zone z ");
		query.append("WHERE id.engineLog.idEngineLog =:idEngine");
		Query q = em.createQuery(query.toString());
		q.setParameter("idEngine", idEngine);
		return (IrrigationDetails) q.getSingleResult();
	}

	/**
	 * This method allows consult of the irrigation engine by every zone and
	 * machine. This native query, because is information of the report.
	 * 
	 * @author Luna.Granados
	 * 
	 * @param consult
	 *            :Consultation records depending on the parameters selected by
	 *            the user
	 * @param parameters
	 *            :Query parameters
	 * @return List<Object[]>: list of object with information the zone,
	 *         machine, engine, fuel usage, and irrigation detail.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> consultIrrigationEngineReport(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT z.name as zone, ");
		query.append("		 m.name as machine, m.serial_number, ");
		query.append("		 el.date, el.hour_on, el.hour_off, el.hourmeter_on, el.hourmeter_off, el.duration, ");
		query.append("		 ful.consumption, ");
		query.append("		 id.hidrometer_on, id.hidrometer_off, id.water_usage ");
		query.append("FROM diesel.zone z, ");
		query.append("	   machines.machines m, ");
		query.append("	   diesel.engine_log el, ");
		query.append("	   diesel.fuel_usage_log ful, ");
		query.append("	   diesel.irrigation_details id ");
		query.append("WHERE el.irrigation is true ");
		query.append("AND z.id = id.id_zone ");
		query.append("AND m.idmachine = id.id_machine ");
		query.append("AND ful.id_engine_log = el.id_engine_log ");
		query.append("AND id.id_engine_log = el.id_engine_log ");
		query.append(consult);
		query.append("ORDER BY el.date ASC");
		Query q = em.createNativeQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return q.getResultList();
	}

}