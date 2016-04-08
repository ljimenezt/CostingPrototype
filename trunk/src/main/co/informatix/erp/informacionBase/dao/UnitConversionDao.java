package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.UnitConversion;

/**
 * This class manages measurement unit conversions records that are stored in
 * the database.
 * 
 * @author Sergio.Gelves
 */
@SuppressWarnings("serial")
@Stateless
public class UnitConversionDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Unit Conversions to store in the database.
	 * 
	 * @param unitConversion
	 *            : UnitConversion to save.
	 * @throws Exception
	 */
	public void saveUnitConversion(UnitConversion unitConversion)
			throws Exception {
		em.persist(unitConversion);
	}

	/**
	 * Edit a UnitConversion in the database.
	 * 
	 * @param unitConversion
	 *            : UnitConversion to edit.
	 * @throws Exception
	 */
	public void editUnitConversion(UnitConversion unitConversion)
			throws Exception {
		em.merge(unitConversion);
	}

	/**
	 * Delete a UnitConversion in the database.
	 * 
	 * @param unitConversion
	 *            : UnitConversion to delete.
	 * @throws Exception
	 */
	public void removeUnitConversion(UnitConversion unitConversion)
			throws Exception {
		em.remove(em.merge(unitConversion));
	}

	/**
	 * It returns the number of existing UnitConversion in the database, and it
	 * is filtered with search values.
	 * 
	 * @param query
	 *            : String containing the query with the filtered UnitConversion
	 *            records.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of UnitConvertion records found.
	 * @throws Exception
	 */
	public Long unitConversionAmount(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(uc) FROM UnitConversion uc ");
		queryBuilder.append(query);
		Query queryResult = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			queryResult.setParameter(parameter.getLabel(),
					Integer.parseInt((String) parameter.getValue()));
		}
		return (Long) queryResult.getSingleResult();
	}

	/**
	 * This method queries unit conversions according to some search values and
	 * retrieves a specified record range of the query.
	 * 
	 * @param start
	 *            : First record to retrieved.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Retrieved records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<UnitConversion>: List of Unit Conversions.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<UnitConversion> queryUnitConversion(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder
				.append("SELECT uc FROM UnitConversion uc JOIN FETCH uc.unitConversionPk pk ");
		queryBuilder
				.append("JOIN FETCH pk.originalUnit JOIN FETCH pk.finalUnit ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY uc.unitConversionPk.originalUnit.name ");
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(),
					Integer.parseInt((String) parameter.getValue()));
		}
		q.setFirstResult(start).setMaxResults(range);
		List<UnitConversion> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * It looks for an unit conversion record that matches with the specified
	 * measurement units that correspond to the originalUnit's id and
	 * finalUnit's id parameters.
	 * 
	 * @param originalUnitId
	 *            : Identifier of the measurement unit that corresponds to the
	 *            original unit in the conversion
	 * @param finalUnitId
	 *            : Identifier of the measurement unit that corresponds to the
	 *            final unit in the conversion
	 * @return The corresponding UnitConvertion Object or null if there is no
	 *         match.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public UnitConversion queryPrimaryKey(int originalUnitId, int finalUnitId)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT uc FROM UnitConversion uc ");
		queryBuilder
				.append("WHERE uc.unitConversionPk.originalUnit.idMeasurementUnits=:originalUnitId ");
		queryBuilder
				.append("AND uc.unitConversionPk.finalUnit.idMeasurementUnits=:finalUnitId");
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("originalUnitId", originalUnitId);
		query.setParameter("finalUnitId", finalUnitId);
		List<UnitConversion> result = query.getResultList();
		if (result.size() > 0) {
			return (UnitConversion) result.get(0);
		}
		return null;
	}

}
