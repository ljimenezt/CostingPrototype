package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.NoveltyType;

/**
 * DAO class that establishes the connection between business logic and
 * database. NoveltyTypeDao manages NoveltyType.
 * 
 * @author Claudia.Rey
 */
@SuppressWarnings("serial")
@Stateless
public class NoveltyTypeDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves the type of novelty in the database.
	 * 
	 * @param noveltyType
	 *            : novelty to save.
	 * @throws Exception
	 */
	public void saveNoveltyType(NoveltyType noveltyType) throws Exception {
		em.persist(noveltyType);
	}

	/**
	 * Edit the information for one novelty type.
	 * 
	 * @param noveltyType
	 *            : Novelty to edit.
	 * @throws Exception
	 */
	public void editNoveltyType(NoveltyType noveltyType) throws Exception {
		em.merge(noveltyType);
	}

	/**
	 * Removes a type of novelty from the database.
	 * 
	 * @param noveltyType
	 *            : novelty to remove.
	 * @throws Exception
	 */
	public void removeNoveltyType(NoveltyType noveltyType) throws Exception {
		em.remove(em.merge(noveltyType));
	}

	/**
	 * Returns the number of existing novelty types in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter names types of
	 *            novelty.
	 * @param parameters
	 *            : Query parameters.
	 * @return Long: Number of records found types of novelty.
	 * @throws Exception
	 */
	public Long quantityNoveltyType(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(nt) FROM NoveltyType nt ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult if the name of the novelty type exist in the database when saving
	 * or editing.
	 * 
	 * @author Luna.Granados
	 * 
	 * @param name
	 *            : Name the type of novelty to verify.
	 * @param id
	 *            : id the type of novelty to verify.
	 * @return NoveltyType: Object found with the search parameters.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public NoveltyType nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT nt FROM NoveltyType nt ");
		query.append("WHERE UPPER(nt.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND nt.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<NoveltyType> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult if the color exist in the database when saving or editing.
	 * 
	 * @author Luna.Granados
	 * 
	 * @param idNovelType
	 *            : id of novelty type to verify.
	 * @param idColor
	 *            : id of color to verify.
	 * @return NoveltyType: Object found with the search parameters.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public NoveltyType colorExists(int idNovelType, int idColor)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT nt FROM NoveltyType nt ");
		query.append("JOIN FETCH nt.color c ");
		query.append("WHERE c.id = :idColor ");
		if (idNovelType != 0) {
			query.append("AND nt.id <> :idNovelType ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("idColor", idColor);
		if (idNovelType != 0) {
			q.setParameter("idNovelType", idNovelType);
		}
		List<NoveltyType> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method consultation novelty types with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param start
	 *            : Registry where consultation begins.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Consult the logs depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<NoveltyType>: List of novelty types.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<NoveltyType> consultNoveltyType(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT nt FROM NoveltyType nt ");
		query.append("JOIN FETCH nt.color c ");
		query.append(consult);
		query.append("ORDER BY nt.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<NoveltyType> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method that I see all kinds NoveltyType object and stores it in a list
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @return List<NoveltyType>: NoveltyType List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<NoveltyType> listNoveltyType() throws Exception {
		Query q = em
				.createQuery("SELECT nt FROM NoveltyType nt ORDER BY nt.name ");
		return q.getResultList();
	}
}
