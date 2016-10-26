package co.informatix.erp.humanResources.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.humanResources.entities.NoveltyType;

/**
 * DAO class that establishes the connection between business logic and
 * database. NoveltyTypeDao manages NoveltyType.
 * 
 * @author Claudia.Rey
 */
public class NoveltyTypeDao {

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
	
}
