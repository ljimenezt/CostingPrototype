package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.Team;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the Team entity.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class TeamDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an team in database
	 * 
	 * @param team
	 *            : team to save.
	 * @throws Exception
	 */
	public void saveTeam(Team team) throws Exception {
		em.persist(team);
	}

	/**
	 * Edit an team in database
	 * 
	 * @param team
	 *            : team to edit.
	 * @throws Exception
	 */
	public void editTeam(Team team) throws Exception {
		em.merge(team);
	}

	/**
	 * Delete the teams of the database.
	 * 
	 * @param team
	 *            : team to remove.
	 * @throws Exception
	 */
	public void deleteTeam(Team team) throws Exception {
		em.remove(em.merge(team));
	}

	/**
	 * Returns the number of existing team in the database filtering information
	 * search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter purchase
	 *            invoices.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of teams records found.
	 * @throws Exception
	 */
	public Long amountTeam(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(t) FROM Team t ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consult team list with a certain range sent as a parameter
	 * and filtering the information by the values of sent search.
	 * 
	 * @param start
	 *            : The record where the query result starts.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Team>: teams list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Team> consultTeams(int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM Team t ");
		query.append(consult);
		query.append("ORDER BY t.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Team> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation if the name of the team units there in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : team name to verify.
	 * @param id
	 *            : identifier team to verify.
	 * @return Team: Team object found with the search parameters name and
	 *         identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Team nameExists(String name, short idTeam) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT t FROM Team t ");
		query.append("WHERE UPPER(t.name)=UPPER(:name) ");
		if (idTeam != 0) {
			query.append("AND t.idTeam <>:idTeam ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (idTeam != 0) {
			q.setParameter("idTeam", idTeam);
		}
		List<Team> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
