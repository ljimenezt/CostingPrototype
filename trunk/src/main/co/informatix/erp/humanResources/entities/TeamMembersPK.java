package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * This class is created to form a compound key in the table team_members.
 * 
 * @author Liseth.Jimenez
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class TeamMembersPK implements Serializable {

	private Team team;
	private Hr hr;

	/**
	 * @return team: Team for the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idteam", referencedColumnName = "idteam", nullable = false)
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            : Team for the person
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * @return hr: Person associated with the team
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idhr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Person associated with the team
	 * 
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((hr == null) ? 0 : hr.hashCode());
		result = prime * result + ((team == null) ? 0 : team.hashCode());
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
		TeamMembersPK other = (TeamMembersPK) obj;
		if (hr == null) {
			if (other.hr != null)
				return false;
		} else if (!hr.equals(other.hr))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}
}