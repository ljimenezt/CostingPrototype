package co.informatix.erp.machines.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class maps the maintenance_and_calibration table, which contains the
 * information of maintenance and calibration.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "maintenance_and_calibration", schema = "machines")
public class MaintenanceAndCalibration implements Serializable {

	private int idMaintenance;
	private Date dateTime;
	private Double totalCostBudget;
	private Double totalCostActual;
	private Boolean machineEquipementInGoodCondition;
	private Machines machines;

	/**
	 * Class constructor
	 */
	public MaintenanceAndCalibration() {
		this.machines = new Machines();
	}

	/**
	 * @return idMaintenance: Maintenance and calibration ID
	 */
	@Id
	@Column(name = "idmaintenance", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdMaintenance() {
		return idMaintenance;
	}

	/**
	 * @param idMaintenance
	 *            : Maintenance and calibration ID
	 */
	public void setIdMaintenance(int idMaintenance) {
		this.idMaintenance = idMaintenance;
	}

	/**
	 * @return dateTime: gets the date of the maintenance and calibration
	 */
	@Column(name = "date_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            : sets the date of the maintenance and calibration
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return totalCostBudget: gets the total budget cost
	 */
	@Column(name = "total_cost_budget")
	public Double getTotalCostBudget() {
		return totalCostBudget;
	}

	/**
	 * @param totalCostBudget
	 *            : sets the total budget cost
	 */
	public void setTotalCostBudget(Double totalCostBudget) {
		this.totalCostBudget = totalCostBudget;
	}

	/**
	 * @return totalCostActual: gets the current total cost
	 */
	@Column(name = "total_cost_actual")
	public Double getTotalCostActual() {
		return totalCostActual;
	}

	/**
	 * @param totalCostActual
	 *            : sets the current total cost
	 */
	public void setTotalCostActual(Double totalCostActual) {
		this.totalCostActual = totalCostActual;
	}

	/**
	 * @return machineEquipementInGoodCondition: gets a Boolean value "true" if
	 *         the machine equipment is in good condition otherwise false
	 */
	@Column(name = "machine_equipement_in_good_condition")
	public Boolean getMachineEquipementInGoodCondition() {
		return machineEquipementInGoodCondition;
	}

	/**
	 * @param machineEquipementInGoodCondition
	 *            : sets a Boolean value "true" if the machine equipment is in
	 *            good condition otherwise false
	 */
	public void setMachineEquipementInGoodCondition(
			Boolean machineEquipementInGoodCondition) {
		this.machineEquipementInGoodCondition = machineEquipementInGoodCondition;
	}

	/**
	 * @return machines: gets the relationship of the machines with the
	 *         maintenance and calibration
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmachine", referencedColumnName = "idmachine", nullable = false)
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : sets the relationship of the machines with the maintenance
	 *            and calibration
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + idMaintenance;
		result = prime
				* result
				+ ((machineEquipementInGoodCondition == null) ? 0
						: machineEquipementInGoodCondition.hashCode());
		result = prime * result
				+ ((totalCostActual == null) ? 0 : totalCostActual.hashCode());
		result = prime * result
				+ ((totalCostBudget == null) ? 0 : totalCostBudget.hashCode());
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
		MaintenanceAndCalibration other = (MaintenanceAndCalibration) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (idMaintenance != other.idMaintenance)
			return false;
		if (machineEquipementInGoodCondition == null) {
			if (other.machineEquipementInGoodCondition != null)
				return false;
		} else if (!machineEquipementInGoodCondition
				.equals(other.machineEquipementInGoodCondition))
			return false;
		if (totalCostActual == null) {
			if (other.totalCostActual != null)
				return false;
		} else if (!totalCostActual.equals(other.totalCostActual))
			return false;
		if (totalCostBudget == null) {
			if (other.totalCostBudget != null)
				return false;
		} else if (!totalCostBudget.equals(other.totalCostBudget))
			return false;
		return true;
	}

}
