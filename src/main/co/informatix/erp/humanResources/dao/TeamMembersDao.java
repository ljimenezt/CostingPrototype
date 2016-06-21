package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.Team;
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

}