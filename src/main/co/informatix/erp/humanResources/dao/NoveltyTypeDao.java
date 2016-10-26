package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
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
	 * Edit the track information for one type of contract.
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
}
