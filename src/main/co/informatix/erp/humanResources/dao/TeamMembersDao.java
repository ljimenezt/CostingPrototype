package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.TeamMembers;

/**
 * class DAO that establishes the connection between business logic and database
 * for handling the TeamMembers entity.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class TeamMembersDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save an teamMembers in database
	 * 
	 * @param teamMembers
	 *            : team to save.
	 * @throws Exception
	 */
	public void saveTeamMembers(TeamMembers teamMembers) throws Exception {
		em.persist(teamMembers);
	}

	/**
	 * Edit an teamMembers in database
	 * 
	 * @param teamMembers
	 *            : teamMembers to edit.
	 * @throws Exception
	 */
	public void editTeamMembers(TeamMembers teamMembers) throws Exception {
		em.merge(teamMembers);
	}

	/**
	 * Delete the teamMembers of the database.
	 * 
	 * @param teamMembers
	 *            : teamMembers to remove.
	 * @throws Exception
	 */
	public void deleteTeamMembers(TeamMembers teamMembers) throws Exception {
		em.remove(em.merge(teamMembers));
	}

	/**
	 * Returns the number of existing teamMembers in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consult
	 *            : String containing the query why the filter purchase
	 *            invoices.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of teamMembers records found.
	 * @throws Exception
	 */
	public Long amountTeamMembers(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(tm) FROM TeamMembers tm ");
		query.append("JOIN tm.teamMembersPK.team t ");
		query.append("JOIN tm.teamMembersPK.hr h ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consult TeamMembers list with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param start
	 *            : The record where the query result starts.
	 * @param range
	 *            : Range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<TeamMembers>: TeamMembers list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TeamMembers> consultTeamsMembers(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tm FROM TeamMembers tm ");
		query.append("JOIN FETCH tm.teamMembersPK.team t ");
		query.append("JOIN FETCH tm.teamMembersPK.hr h ");
		query.append(consult);
		query.append("ORDER BY t.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<TeamMembers> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method to consult teamMembers that are associated with a team and hr.
	 * 
	 * @param idTeam
	 *            :idTeam identifier to find teamMembers.
	 * @param idHr
	 *            :idHr identifier to find teamMembers.
	 * @return TeamMembers: TeamMembers found by team and hr.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public TeamMembers associatedTeamMembersByIds(Short idTeam, int idHr)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tm FROM TeamMembers tm ");
		query.append("JOIN FETCH tm.teamMembersPK.team t ");
		query.append("JOIN FETCH tm.teamMembersPK.hr h ");
		query.append("WHERE t.idTeam=:idTeam ");
		query.append("AND h.idHr=:idHr ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTeam", idTeam);
		q.setParameter("idHr", idHr);
		List<TeamMembers> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method consult TeamMembers list with filtering the information by
	 * the values of sent search.
	 * 
	 * @param idTeam
	 *            :idTeam identifier to find teamMembers.
	 * @return List<TeamMembers>: TeamMembers list found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<TeamMembers> consultTeamsMembersByIdTeam(Short idTeam)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT tm FROM TeamMembers tm ");
		query.append("JOIN tm.teamMembersPK.team t ");
		query.append("WHERE t.idTeam=:idTeam ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTeam", idTeam);
		List<TeamMembers> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method consult the amount workers for a team.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param idTeam
	 *            :idTeam identifier to find teamMembers.
	 * @return Long: Quantity for team members.
	 * @throws Exception
	 */
	public Long quantityWorkersByIdTeam(Short idTeam) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(tm) FROM TeamMembers tm ");
		query.append("JOIN tm.teamMembersPK.team t ");
		query.append("WHERE t.idTeam=:idTeam ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idTeam", idTeam);
		return (Long) q.getSingleResult();
	}
}