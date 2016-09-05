package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.lifeCycle.entities.Plot;

/**
 * This class represents the primary key for activity_plot table
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ActivityPlotPK implements Serializable {

	private Activities activity;
	private Plot plot;

	/**
	 * @return activity: Activity object represents the actions that develop in
	 *         crops.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactivity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            : Activity object represents the actions that develop in
	 *            crops.
	 */
	public void setActivity(Activities activity) {
		this.activity = activity;
	}

	/**
	 * @return plot: Object plot represents one of the areas of plantation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idplot", referencedColumnName = "idplot", nullable = false)
	public Plot getPlot() {
		return plot;
	}

	/**
	 * @param plot
	 *            : Object plot represents one of the areas of plantation
	 */
	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activity == null) ? 0 : activity.hashCode());
		result = prime * result + ((plot == null) ? 0 : plot.hashCode());
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
		ActivityPlotPK other = (ActivityPlotPK) obj;
		if (activity == null) {
			if (other.activity != null)
				return false;
		} else if (!activity.equals(other.activity))
			return false;
		if (plot == null) {
			if (other.plot != null)
				return false;
		} else if (!plot.equals(other.plot))
			return false;
		return true;
	}

}
