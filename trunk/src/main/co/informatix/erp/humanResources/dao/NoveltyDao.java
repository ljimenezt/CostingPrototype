package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.Novelty;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the novelty that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class NoveltyDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves the novelty in the database.
	 * 
	 * @param novelty
	 *            : novelty to save.
	 * @throws Exception
	 */
	public void saveNovelty(Novelty novelty) throws Exception {
		em.persist(novelty);
	}

	/**
	 * Edit the novelty in the database.
	 * 
	 * @param novelty
	 *            : Novelty to edit.
	 * @throws Exception
	 */
	public void editNovelty(Novelty novelty) throws Exception {
		em.merge(novelty);
	}

	/**
	 * Remove the novelty in the database.
	 * 
	 * @param novelty
	 *            : Novelty to edit.
	 * @throws Exception
	 */
	public void removeNovelty(Novelty novelty) throws Exception {
		em.remove(em.merge(novelty));
	}

	/**
	 * This method allows consult the novelty list by current date.
	 * 
	 * @param date
	 *            : Current date.
	 * @return List<Novelty>: List of Novelty.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Novelty> findNoveltyByDate(String date) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT n FROM Novelty n ");
		query.append("JOIN FETCH n.hr ");
		query.append("JOIN FETCH n.noveltyType ");
		query.append("WHERE TO_CHAR(n.initialDateTime,'YYYY-MM-dd')<= :date ");
		query.append("AND TO_CHAR(n.finalDateTime,'YYYY-MM-dd')>= :date ");
		Query q = em.createQuery(query.toString());
		q.setParameter("date", date);
		List<Novelty> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult the novelty object by human resource and date.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idHr
	 *            : identifier of the human resource. .
	 * @param date
	 *            : Date of the novelty.
	 * @return List<Novelty>: List of Novelty.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Novelty noveltyByHrAndDate(int idHr, Date date) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT n FROM Novelty n ");
		query.append("JOIN FETCH n.hr hr ");
		query.append("JOIN FETCH n.noveltyType nt ");
		query.append("JOIN FETCH nt.color ");
		query.append("WHERE n.initialDateTime <= :date ");
		query.append("AND n.finalDateTime >= :date ");
		query.append("AND hr.idHr =:idHr ");
		Query q = em.createQuery(query.toString());
		q.setParameter("date", date).setParameter("idHr", idHr);
		List<Novelty> result = q.getResultList();
		if (result.size() > 0) {
			return result.get(0);
		}
		return null;
	}

}