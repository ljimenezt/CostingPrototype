package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.humanResources.entities.Team;

/**
 * This class is created to form a compound key in the table activity_team.
 * 
 * @author Liseth.Jimenez
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ActivityTeamPK implements Serializable {

	private Activities activities;
	private Team team;

	/**
	 * @return activities: Activity with a team associated.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactivity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : Activity with a team associated.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return team: Team for the activity.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idteam", referencedColumnName = "idteam", nullable = false)
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            : Team for the activity.
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
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
		ActivityTeamPK other = (ActivityTeamPK) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (team == null) {
			if (other.team != null)
				return false;
		} else if (!team.equals(other.team))
			return false;
		return true;
	}
}