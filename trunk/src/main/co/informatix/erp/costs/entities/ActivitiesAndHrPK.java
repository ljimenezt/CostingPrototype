package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.humanResources.entities.Hr;

/**
 * This class is created by the need to form a compound key in the table
 * activities_and_hr
 * 
 * @author Dario Lopez
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ActivitiesAndHrPK implements Serializable {

	private Activities activities;
	private Hr hr;

	/**
	 * @return activities: relationship to form primary key activities
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactivity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : relationship to form primary key activities
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return hr: relationship hr to form primary key
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idhr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : hr relationship
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime * result + ((hr == null) ? 0 : hr.hashCode());
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
		ActivitiesAndHrPK other = (ActivitiesAndHrPK) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (hr == null) {
			if (other.hr != null)
				return false;
		} else if (!hr.equals(other.hr))
			return false;
		return true;
	}

}
