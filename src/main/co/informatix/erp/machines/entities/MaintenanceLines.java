package co.informatix.erp.machines.entities;

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

/**
 * This class maps the maintenance_lines table, which contains the information
 * lines maintenance.
 * 
 * @author Mabell.Boada
 * 
 */
@Entity
@Table(name = "maintenance_lines", schema = "machines")
@SuppressWarnings("serial")
public class MaintenanceLines implements Serializable {

	private int idMaintenanceline;
	private String description;
	private Double costBudget;
	private Double costActual;
	private Machines machines;
	private MaintenanceAndCalibration maintenanceAndCalibration;

	/**
	 * @return idMaintenanceline: ID Maintenance lines
	 */
	@Id
	@Column(name = "idmaintenanceline", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdMaintenanceline() {
		return idMaintenanceline;
	}

	/**
	 * @param idMaintenanceline
	 *            : ID Maintenance lines
	 */
	public void setIdMaintenanceline(int idMaintenanceline) {
		this.idMaintenanceline = idMaintenanceline;
	}

	/**
	 * @return description: gets the description of the lines Maintenance
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : sets the description of the lines Maintenance
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return costBudget: gets the cost estimate
	 */
	@Column(name = "cost_budget")
	public Double getCostBudget() {
		return costBudget;
	}

	/**
	 * @param costBudget
	 *            : sets the cost estimate
	 */
	public void setCostBudget(Double costBudget) {
		this.costBudget = costBudget;
	}

	/**
	 * @return costActual: gets the current cost
	 */
	@Column(name = "cost_actual")
	public Double getCostActual() {
		return costActual;
	}

	/**
	 * @param costActual
	 *            : sets the current cost
	 */
	public void setCostActual(Double costActual) {
		this.costActual = costActual;
	}

	/**
	 * @return machines: gets the relationship of the machines with maintenance
	 *         lines
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmachine", referencedColumnName = "idmachine", nullable = false)
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : sets the relationship of the machines with maintenance lines
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	/**
	 * @return maintenanceAndCalibration: gets the relationship of the
	 *         maintenance and calibration lines maintenance
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmaintenance", referencedColumnName = "idmaintenance", nullable = false)
	public MaintenanceAndCalibration getMaintenanceAndCalibration() {
		return maintenanceAndCalibration;
	}

	/**
	 * @param maintenanceAndCalibration
	 *            : sets the relationship of the maintenance and calibration
	 *            lines maintenance
	 */
	public void setMaintenanceAndCalibration(
			MaintenanceAndCalibration maintenanceAndCalibration) {
		this.maintenanceAndCalibration = maintenanceAndCalibration;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((costActual == null) ? 0 : costActual.hashCode());
		result = prime * result
				+ ((costBudget == null) ? 0 : costBudget.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + idMaintenanceline;
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
		MaintenanceLines other = (MaintenanceLines) obj;
		if (costActual == null) {
			if (other.costActual != null)
				return false;
		} else if (!costActual.equals(other.costActual))
			return false;
		if (costBudget == null) {
			if (other.costBudget != null)
				return false;
		} else if (!costBudget.equals(other.costBudget))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idMaintenanceline != other.idMaintenanceline)
			return false;
		return true;
	}

}
