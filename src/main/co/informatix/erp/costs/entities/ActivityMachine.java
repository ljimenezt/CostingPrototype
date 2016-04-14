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
 * This is the entity that is responsible for mapping the activity_machine
 * table.
 * 
 * @author Andres.Gomez
 * @modify 30/09/2015 Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activity_machine", schema = "costs")
public class ActivityMachine implements Serializable, Cloneable {

	private ActivityMachinePK activityMachinePK;

	private Date initialDateTime;
	private Date finalDateTime;

	private Double durationActual;
	private Double durationBudget;
	private Double depreciationCostBudget;
	private Double depreciationCostActual;
	private Double maintenanceCostBudget;
	private Double maintenanceCostActual;
	private Double consumablesCostBudget;
	private Double consumablesCostActual;
	private Double insuranceCostActual;
	private Double insuranceCostBudget;

	/**
	 * Constructor that initializes the primary key
	 */
	public ActivityMachine() {
		this.activityMachinePK = new ActivityMachinePK();
	}

	/**
	 * @return activityMachinePK: object of the activity table composite machine
	 *         key.
	 */
	@EmbeddedId
	public ActivityMachinePK getActivityMachinePK() {
		return activityMachinePK;
	}

	/**
	 * @param activityMachinePK
	 *            : object of the activity table composite machine key.
	 */
	public void setActivityMachinePK(ActivityMachinePK activityMachinePK) {
		this.activityMachinePK = activityMachinePK;
	}

	/**
	 * @return initialDateTime: Time of the start date.
	 */
	@Column(name = "initial_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDateTime() {
		return initialDateTime;
	}

	/**
	 * @param initialDateTime
	 *            : Time of the start date.
	 */
	public void setInitialDateTime(Date initialDateTime) {
		this.initialDateTime = initialDateTime;
	}

	/**
	 * @return finalDateTime: Time deadline.
	 */
	@Column(name = "final_date_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDateTime() {
		return finalDateTime;
	}

	/**
	 * @param finalDateTime
	 *            : Time deadline.
	 */
	public void setFinalDateTime(Date finalDateTime) {
		this.finalDateTime = finalDateTime;
	}

	/**
	 * @return durationActual: Current duration.
	 */
	@Column(name = "duration_actual")
	public Double getDurationActual() {
		return durationActual;
	}

	/**
	 * @param durationActual
	 *            : Current duration.
	 */
	public void setDurationActual(Double durationActual) {
		this.durationActual = durationActual;
	}

	/**
	 * @return durationBudget: Duration budgeted.
	 */
	@Column(name = "duration_budget")
	public Double getDurationBudget() {
		return durationBudget;
	}

	/**
	 * @param durationBudget
	 *            : Duration budgeted.
	 */
	public void setDurationBudget(Double durationBudget) {
		this.durationBudget = durationBudget;
	}

	/**
	 * @return depreciationCostBudget: budgeted cost of depreciation.
	 */
	@Column(name = "depreciation_cost_budget")
	public Double getDepreciationCostBudget() {
		return depreciationCostBudget;
	}

	/**
	 * @param depreciationCostBudget
	 *            : budgeted cost of depreciation.
	 */
	public void setDepreciationCostBudget(Double depreciationCostBudget) {
		this.depreciationCostBudget = depreciationCostBudget;
	}

	/**
	 * @return depreciationCostActual: current cost depreciation.
	 */
	@Column(name = "depreciation_cost_actual")
	public Double getDepreciationCostActual() {
		return depreciationCostActual;
	}

	/**
	 * @param depreciationCostActual
	 *            : current cost depreciation.
	 */
	public void setDepreciationCostActual(Double depreciationCostActual) {
		this.depreciationCostActual = depreciationCostActual;
	}

	/**
	 * @return maintenanceCostBudget: Budgeted cost of maintenance.
	 */
	@Column(name = "maintenance_cost_budget")
	public Double getMaintenanceCostBudget() {
		return maintenanceCostBudget;
	}

	/**
	 * @param maintenanceCostBudget
	 *            : Budgeted cost of maintenance.
	 */
	public void setMaintenanceCostBudget(Double maintenanceCostBudget) {
		this.maintenanceCostBudget = maintenanceCostBudget;
	}

	/**
	 * @return maintenanceCostActual : Current maintenance costs.
	 */
	@Column(name = "maintenance_cost_actual")
	public Double getMaintenanceCostActual() {
		return maintenanceCostActual;
	}

	/**
	 * @param maintenanceCostActual
	 *            : Current maintenance costs.
	 */
	public void setMaintenanceCostActual(Double maintenanceCostActual) {
		this.maintenanceCostActual = maintenanceCostActual;
	}

	/**
	 * @return consumablesCostBudget: budget costs of consumables.
	 * 
	 */
	@Column(name = "consumables_cost_budget")
	public Double getConsumablesCostBudget() {
		return consumablesCostBudget;
	}

	/**
	 * @param consumablesCostBudget
	 *            : budget costs of consumables.
	 */
	public void setConsumablesCostBudget(Double consumablesCostBudget) {
		this.consumablesCostBudget = consumablesCostBudget;
	}

	/**
	 * @return consumablesCostActual: current cost of supplies.
	 */
	@Column(name = "consumables_cost_actual")
	public Double getConsumablesCostActual() {
		return consumablesCostActual;
	}

	/**
	 * @param consumablesCostActual
	 *            : current cost of supplies.
	 */
	public void setConsumablesCostActual(Double consumablesCostActual) {
		this.consumablesCostActual = consumablesCostActual;
	}

	/**
	 * @return insuranceCostActual: Insurance Cost Actual.
	 */
	@Column(name = "insurance_cost_actual")
	public Double getInsuranceCostActual() {
		return insuranceCostActual;
	}

	/**
	 * @param insuranceCostActual
	 *            : Insurance Cost Actual.
	 */
	public void setInsuranceCostActual(Double insuranceCostActual) {
		this.insuranceCostActual = insuranceCostActual;
	}

	/**
	 * @return insuranceCostBudget: Insurance Cost Budget
	 */
	@Column(name = "insurance_cost_budget")
	public Double getInsuranceCostBudget() {
		return insuranceCostBudget;
	}

	/**
	 * @param insuranceCostBudget
	 *            : Insurance Cost Budget.
	 */
	public void setInsuranceCostBudget(Double insuranceCostBudget) {
		this.insuranceCostBudget = insuranceCostBudget;
	}

	public ActivityMachine clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (ActivityMachine) clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((consumablesCostActual == null) ? 0 : consumablesCostActual
						.hashCode());
		result = prime
				* result
				+ ((consumablesCostBudget == null) ? 0 : consumablesCostBudget
						.hashCode());
		result = prime
				* result
				+ ((depreciationCostActual == null) ? 0
						: depreciationCostActual.hashCode());
		result = prime
				* result
				+ ((depreciationCostBudget == null) ? 0
						: depreciationCostBudget.hashCode());
		result = prime * result
				+ ((durationActual == null) ? 0 : durationActual.hashCode());
		result = prime * result
				+ ((durationBudget == null) ? 0 : durationBudget.hashCode());
		result = prime * result
				+ ((finalDateTime == null) ? 0 : finalDateTime.hashCode());
		result = prime * result
				+ ((initialDateTime == null) ? 0 : initialDateTime.hashCode());
		result = prime
				* result
				+ ((maintenanceCostActual == null) ? 0 : maintenanceCostActual
						.hashCode());
		result = prime
				* result
				+ ((maintenanceCostBudget == null) ? 0 : maintenanceCostBudget
						.hashCode());
		result = prime
				* result
				+ ((insuranceCostActual == null) ? 0 : insuranceCostActual
						.hashCode());
		result = prime
				* result
				+ ((insuranceCostBudget == null) ? 0 : insuranceCostBudget
						.hashCode());
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
		ActivityMachine other = (ActivityMachine) obj;
		if (consumablesCostActual == null) {
			if (other.consumablesCostActual != null)
				return false;
		} else if (!consumablesCostActual.equals(other.consumablesCostActual))
			return false;
		if (consumablesCostBudget == null) {
			if (other.consumablesCostBudget != null)
				return false;
		} else if (!consumablesCostBudget.equals(other.consumablesCostBudget))
			return false;
		if (depreciationCostActual == null) {
			if (other.depreciationCostActual != null)
				return false;
		} else if (!depreciationCostActual.equals(other.depreciationCostActual))
			return false;
		if (depreciationCostBudget == null) {
			if (other.depreciationCostBudget != null)
				return false;
		} else if (!depreciationCostBudget.equals(other.depreciationCostBudget))
			return false;
		if (durationActual == null) {
			if (other.durationActual != null)
				return false;
		} else if (!durationActual.equals(other.durationActual))
			return false;
		if (durationBudget == null) {
			if (other.durationBudget != null)
				return false;
		} else if (!durationBudget.equals(other.durationBudget))
			return false;
		if (finalDateTime == null) {
			if (other.finalDateTime != null)
				return false;
		} else if (!finalDateTime.equals(other.finalDateTime))
			return false;
		if (initialDateTime == null) {
			if (other.initialDateTime != null)
				return false;
		} else if (!initialDateTime.equals(other.initialDateTime))
			return false;
		if (maintenanceCostActual == null) {
			if (other.maintenanceCostActual != null)
				return false;
		} else if (!maintenanceCostActual.equals(other.maintenanceCostActual))
			return false;
		if (maintenanceCostBudget == null) {
			if (other.maintenanceCostBudget != null)
				return false;
		} else if (!maintenanceCostBudget.equals(other.maintenanceCostBudget))
			return false;
		if (insuranceCostActual == null) {
			if (other.insuranceCostActual != null)
				return false;
		} else if (!insuranceCostActual.equals(other.insuranceCostActual))
			return false;
		if (insuranceCostBudget == null) {
			if (other.insuranceCostBudget != null)
				return false;
		} else if (!insuranceCostBudget.equals(other.insuranceCostBudget))
			return false;
		return true;
	}

}
