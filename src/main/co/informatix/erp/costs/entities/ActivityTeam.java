package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This is the entity that is responsible for mapping the activity_team table.
 * 
 * @author Liseth.Jimenez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activity_team", schema = "costs")
public class ActivityTeam implements Serializable {

	private ActivityTeamPK activityTeamPK;

	/**
	 * @return activityTeamPK : Object of the activity table composite team key.
	 */
	@EmbeddedId
	public ActivityTeamPK getActivityTeamPK() {
		return activityTeamPK;
	}

	/**
	 * @param activityTeamPK
	 *            : Object of the activity table composite team key.
	 */
	public void setActivityTeamPK(ActivityTeamPK activityTeamPK) {
		this.activityTeamPK = activityTeamPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activityTeamPK == null) ? 0 : activityTeamPK.hashCode());
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
		ActivityTeam other = (ActivityTeam) obj;
		if (activityTeamPK == null) {
			if (other.activityTeamPK != null)
				return false;
		} else if (!activityTeamPK.equals(other.activityTeamPK))
			return false;
		return true;
	}

}