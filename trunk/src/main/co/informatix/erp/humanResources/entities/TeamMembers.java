package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This is the entity that is responsible for mapping the team_members table.
 * 
 * @author Liseth.Jimenez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "team_members", schema = "human_resources")
public class TeamMembers implements Serializable {

	private TeamMembersPK teamMembersPK;
	private boolean lead;
	private boolean statistician;

	/**
	 * @return teamMembersPK: The composite order table key team_members.
	 */
	@EmbeddedId
	public TeamMembersPK getTeamMembersPK() {
		return teamMembersPK;
	}

	/**
	 * @param teamMembersPK
	 *            : : The composite order table key team_members.
	 */
	public void setTeamMembersPK(TeamMembersPK teamMembersPK) {
		this.teamMembersPK = teamMembersPK;
	}

	/**
	 * @return lead : Person charge to lead the team
	 */
	@Column(name = "lead", nullable = false)
	public boolean isLead() {
		return lead;
	}

	/**
	 * @param lead
	 *            : Person charge to lead the team
	 */
	public void setLead(boolean lead) {
		this.lead = lead;
	}

	/**
	 * @return statistician : Person charge of save the statictics
	 */
	@Column(name = "statistician", nullable = false)
	public boolean isStatistician() {
		return statistician;
	}

	/**
	 * @param statistician
	 *            : Person charge of save the statictics
	 */
	public void setStatistician(boolean statistician) {
		this.statistician = statistician;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (lead ? 1231 : 1237);
		result = prime * result + (statistician ? 1231 : 1237);
		result = prime * result
				+ ((teamMembersPK == null) ? 0 : teamMembersPK.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		TeamMembers other = (TeamMembers) obj;
		if (lead != other.lead)
			return false;
		if (statistician != other.statistician)
			return false;
		if (teamMembersPK == null) {
			if (other.teamMembersPK != null)
				return false;
		} else if (!teamMembersPK.equals(other.teamMembersPK))
			return false;
		return true;
	}
}