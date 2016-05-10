package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.informacionBase.entities.TypeUnit;

/**
 * This class in responsible for the administration in the database of the type
 * unit.
 * 
 * @author Jhair.Leal
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class TypeUnitDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method makes the record in the database a new type of unit.
	 * 
	 * @param typeUnit
	 *            : type of unit to be recorded in the database.
	 * 
	 * @throws Exception
	 */
	public void saveTypeUnit(TypeUnit typeUnit) throws Exception {
		em.persist(typeUnit);
	}

	/**
	 * This method makes the update on the type of unit in the database
	 * information system.
	 * 
	 * @param typeUnit
	 *            : type of unit to update.
	 * 
	 * @throws Exception
	 */
	public void editTypeUnit(TypeUnit typeUnit) throws Exception {
		em.merge(typeUnit);
	}

	/**
	 * This method of consultation with a range determining TypeUnit sent as a
	 * parameter and filtering the information by the values sent search.
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
	 * @return List<TypeUnit>: List of Type Unit.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TypeUnit> consultTypeUnit(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tu FROM TypeUnit tu ");
		query.append(consult);
		query.append("ORDER BY tu.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<TypeUnit> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of existing in database TypeUnit in filtering the
	 * information by the values sent search.
	 * 
	 * @param consult
	 *            : String containing the query why the TypeUnit filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found Type Unit.
	 * @throws Exception
	 */
	public Long quantityTypeUnit(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(tu) FROM TypeUnit tu ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Delete a TypeUnit in the database.
	 * 
	 * @param typeUnit
	 *            : typeUnit to delete.
	 * @throws Exception
	 */
	public void removeTypeUnit(TypeUnit typeUnit) throws Exception {
		em.remove(em.merge(typeUnit));
	}

	/**
	 * Consultation if the name of the type unit there in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : type unit name to verify.
	 * @param id
	 *            : identifier type unit to verify.
	 * @return TypeUnit: TypeUnit object found with the search parameters name
	 *         and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TypeUnit nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tu FROM TypeUnit tu ");
		query.append("WHERE UPPER(tu.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND tu.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<TypeUnit> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}