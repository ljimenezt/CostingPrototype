package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

import co.informatix.erp.costs.entities.ActivitiesAndHr;

/**
 * This class maps the activity_plot table, which contains the information for
 * relation between actvities and plots.
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activity_plot", schema = "life_cycle")
public class ActivityPlot implements Serializable {

	private ActivityPlotPK activityPlotPK;
	private Integer tachosCollected;
	private Integer replatedPlants;

	/**
	 * @return activityPlotPK: Primary key for activity_plot table
	 * 
	 */
	@EmbeddedId
	public ActivityPlotPK getActivityPlotPK() {
		return activityPlotPK;
	}

	/**
	 * @param activityPlotPK
	 *            : Primary key for activity_plot table
	 */
	public void setActivityPlotPK(ActivityPlotPK activityPlotPK) {
		this.activityPlotPK = activityPlotPK;
	}

	/**
	 * @return tachosCollected: Number of tachos recollected.
	 */
	@Column(name = "tachos_collected")
	public Integer getTachosCollected() {
		return tachosCollected;
	}

	/**
	 * @param tachosCollected
	 *            : Number of tachos recollected.
	 */
	public void setTachosCollected(Integer tachosCollected) {
		this.tachosCollected = tachosCollected;
	}

	/**
	 * @return replatedPlants: Number of replants
	 */
	@Column(name = "replated_plants")
	public Integer getReplatedPlants() {
		return replatedPlants;
	}

	/**
	 * @param replatedPlants
	 *            : Number of replants
	 */
	public void setReplatedPlants(Integer replatedPlants) {
		this.replatedPlants = replatedPlants;
	}
	
	public ActivityPlot clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (ActivityPlot) clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activityPlotPK == null) ? 0 : activityPlotPK.hashCode());
		result = prime * result
				+ ((replatedPlants == null) ? 0 : replatedPlants.hashCode());
		result = prime * result
				+ ((tachosCollected == null) ? 0 : tachosCollected.hashCode());
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
		ActivityPlot other = (ActivityPlot) obj;
		if (activityPlotPK == null) {
			if (other.activityPlotPK != null)
				return false;
		} else if (!activityPlotPK.equals(other.activityPlotPK))
			return false;
		if (replatedPlants == null) {
			if (other.replatedPlants != null)
				return false;
		} else if (!replatedPlants.equals(other.replatedPlants))
			return false;
		if (tachosCollected == null) {
			if (other.tachosCollected != null)
				return false;
		} else if (!tachosCollected.equals(other.tachosCollected))
			return false;
		return true;
	}

	
}
