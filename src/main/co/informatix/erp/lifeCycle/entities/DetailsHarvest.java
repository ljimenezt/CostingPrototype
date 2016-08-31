package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.informatix.erp.costs.entities.Activities;

/**
 * This class maps the details harvest table, which contains the information of
 * the details harvest.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "details_harvest", schema = "life_cycle")
public class DetailsHarvest implements Serializable {

	private int id;
	private double previousSacks;
	private double sacksDay;
	private double currentSacks;
	private double dispatch;
	private double poundage;
	private double totalSacks;
	private Activities activities;

	/**
	 * @return id: Details harvest identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Details harvest identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return previousSacks: Number of previous sacks.
	 */
	@Column(name = "previous_sacks")
	public double getPreviousSacks() {
		return previousSacks;
	}

	/**
	 * @param previousSacks
	 *            : Number of previous sacks.
	 */
	public void setPreviousSacks(double previousSacks) {
		this.previousSacks = previousSacks;
	}

	/**
	 * @return sacksDay: Number of sacks day.
	 */
	@Column(name = "sacks_day")
	public double getSacksDay() {
		return sacksDay;
	}

	/**
	 * @param sacksDay
	 *            : Number of sacks day.
	 */
	public void setSacksDay(double sacksDay) {
		this.sacksDay = sacksDay;
	}

	/**
	 * @return currentSacks: Number of current Sack.
	 */
	@Column(name = "current_sacks")
	public double getCurrentSacks() {
		return currentSacks;
	}

	/**
	 * @param currentSacks
	 *            : Number of current Sack.
	 */
	public void setCurrentSacks(double currentSacks) {
		this.currentSacks = currentSacks;
	}

	/**
	 * @return dispatch: Quantity released
	 */
	@Column(name = "dispatch")
	public double getDispatch() {
		return dispatch;
	}

	/**
	 * @param dispatch
	 *            : Quantity released.
	 */
	public void setDispatch(double dispatch) {
		this.dispatch = dispatch;
	}

	/**
	 * @return poundage: Tax cost.
	 */
	@Column(name = "poundage")
	public double getPoundage() {
		return poundage;
	}

	/**
	 * @param poundage
	 *            : Tax cost.
	 */
	public void setPoundage(double poundage) {
		this.poundage = poundage;
	}

	/**
	 * @return totalSacks: Total sacks.
	 */
	@Column(name = "total_sacks")
	public double getTotalSacks() {
		return totalSacks;
	}

	/**
	 * @param totalSacks
	 *            : Total sacks.
	 */
	public void setTotalSacks(double totalSacks) {
		this.totalSacks = totalSacks;
	}

	/**
	 * @return Activities: Activities that the harvest belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactivity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param Activities
	 *            : Activities that the harvest belongs.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(currentSacks);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(dispatch);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + id;
		temp = Double.doubleToLongBits(poundage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(previousSacks);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(sacksDay);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalSacks);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		DetailsHarvest other = (DetailsHarvest) obj;
		if (Double.doubleToLongBits(currentSacks) != Double
				.doubleToLongBits(other.currentSacks))
			return false;
		if (Double.doubleToLongBits(dispatch) != Double
				.doubleToLongBits(other.dispatch))
			return false;
		if (id != other.id)
			return false;
		if (Double.doubleToLongBits(poundage) != Double
				.doubleToLongBits(other.poundage))
			return false;
		if (Double.doubleToLongBits(previousSacks) != Double
				.doubleToLongBits(other.previousSacks))
			return false;
		if (Double.doubleToLongBits(sacksDay) != Double
				.doubleToLongBits(other.sacksDay))
			return false;
		if (Double.doubleToLongBits(totalSacks) != Double
				.doubleToLongBits(other.totalSacks))
			return false;
		return true;
	}
}
