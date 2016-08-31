package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.Diseases;

/**
 * DAO class that establishes the connection between business logic and
 * database. DiseasesAction used for managing Diseases.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class DiseasesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for a disease.
	 * 
	 * @param diseases
	 *            : Disease edit.
	 * @throws Exception
	 */
	public void editDiseases(Diseases diseases) throws Exception {
		em.merge(diseases);
	}

	/**
	 * Save the disease in BD.
	 * 
	 * @param diseases
	 *            : Disease to save.
	 * @throws Exception
	 */
	public void saveDiseases(Diseases diseases) throws Exception {
		em.persist(diseases);
	}

	/**
	 * Eliminates a disease of the BD.
	 * 
	 * @param diseases
	 *            : Eliminate disease.
	 * @throws Exception
	 */
	public void removeDiseases(Diseases diseases) throws Exception {
		em.remove(em.merge(diseases));
	}

	/**
	 * Returns the number of existing diseases in the BD filtering the
	 * information by the values sent search.
	 * 
	 * @param consult
	 *            : String containing the query why the names of diseases are
	 *            filtered.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of records diseases found.
	 * @throws Exception
	 */
	public Long quantityDiseases(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(d) FROM Diseases d ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method see the names of diseases with a certain range sent as a
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
	 * @return List<Diseases>: Listing the names of diseases.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Diseases> consultDiseases(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Diseases d ");
		query.append(consult);
		query.append("ORDER BY d.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Diseases> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation if the name of the disease exists in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : Name of the disease to verify.
	 * @param id
	 *            : id disease to verify.
	 * @return Diseases: Object found with the search parameters name and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Diseases nameExist(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Diseases d ");
		query.append("WHERE UPPER(d.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND d.idDisease <>:idDisease ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idDisease", id);
		}
		List<Diseases> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}