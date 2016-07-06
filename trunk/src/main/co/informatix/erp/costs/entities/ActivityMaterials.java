package co.informatix.erp.costs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity contains information of materials that need activity.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activity_materials", schema = "costs")
public class ActivityMaterials implements Serializable, Cloneable {

	private double quantityBudget;
	private double costBudget;
	private Double quantityActual;
	private Double costActual;
	private Date dateTime;

	private ActivityMaterialsPK activityMaterialsPK;

	/**
	 * @return quantityBudget: Budgeted amount of materials that need the
	 *         activity.
	 */
	@Column(name = "quantity_budget", nullable = false)
	public double getQuantityBudget() {
		return quantityBudget;
	}

	/**
	 * @param quantityBudget
	 *            : Budgeted amount of materials that need the activity.
	 */
	public void setQuantityBudget(double quantityBudget) {
		this.quantityBudget = quantityBudget;
	}

	/**
	 * @return costBudget: Budgeted cost of materials that need the activity.
	 */
	@Column(name = "cost_budget", nullable = false)
	public double getCostBudget() {
		return costBudget;
	}

	/**
	 * @param costBudget
	 *            : Budgeted cost of materials that need the activity.
	 */
	public void setCostBudget(double costBudget) {
		this.costBudget = costBudget;
	}

	/**
	 * @return quantityActual: Actual amount of the materials.
	 */
	@Column(name = "quantity_actual")
	public Double getQuantityActual() {
		return quantityActual;
	}

	/**
	 * @param quantityActual
	 *            : Actual amount of the materials.
	 */
	public void setQuantityActual(Double quantityActual) {
		this.quantityActual = quantityActual;
	}

	/**
	 * @return costActual: Actual cost of the materials.
	 */
	@Column(name = "cost_actual")
	public Double getCostActual() {
		return costActual;
	}

	/**
	 * @param costActual
	 *            : Actual cost of the materials.
	 */
	public void setCostActual(Double costActual) {
		this.costActual = costActual;
	}

	/**
	 * @return dateTime: Create date of the register.
	 */
	@Column(name = "date_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            : Create date of the register.
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return activityMaterialsPK: composite primary key of activities and
	 *         materials.
	 */
	@EmbeddedId
	public ActivityMaterialsPK getActivityMaterialsPK() {
		return activityMaterialsPK;
	}

	/**
	 * @param activityMaterialsPK
	 *            : composite primary key of activities and materials.
	 */
	public void setActivityMaterialsPK(ActivityMaterialsPK activityMaterialsPK) {
		this.activityMaterialsPK = activityMaterialsPK;
	}

	public ActivityMaterials clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (ActivityMaterials) clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((costActual == null) ? 0 : costActual.hashCode());
		long temp;
		temp = Double.doubleToLongBits(costBudget);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result
				+ ((quantityActual == null) ? 0 : quantityActual.hashCode());
		temp = Double.doubleToLongBits(quantityBudget);
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
		ActivityMaterials other = (ActivityMaterials) obj;
		if (costActual == null) {
			if (other.costActual != null)
				return false;
		} else if (!costActual.equals(other.costActual))
			return false;
		if (Double.doubleToLongBits(costBudget) != Double
				.doubleToLongBits(other.costBudget))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (quantityActual == null) {
			if (other.quantityActual != null)
				return false;
		} else if (!quantityActual.equals(other.quantityActual))
			return false;
		if (Double.doubleToLongBits(quantityBudget) != Double
				.doubleToLongBits(other.quantityBudget))
			return false;
		return true;
	}
}
