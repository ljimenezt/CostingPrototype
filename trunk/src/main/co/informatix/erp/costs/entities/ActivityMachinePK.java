package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.machines.entities.Machines;

/**
 * This class is created to form a compound key in the table activity_machine.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ActivityMachinePK implements Serializable {

	private Activities activities;
	private Machines machines;

	/**
	 * @return activities: relationship activities.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_activity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : relationship activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return machines: machine relationship.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_machine", referencedColumnName = "idmachine", nullable = false)
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : machine relationship.
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime * result
				+ ((machines == null) ? 0 : machines.hashCode());
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
		ActivityMachinePK other = (ActivityMachinePK) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (machines == null) {
			if (other.machines != null)
				return false;
		} else if (!machines.equals(other.machines))
			return false;
		return true;
	}

}
