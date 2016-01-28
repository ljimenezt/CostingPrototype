package co.informatix.erp.machines.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class maps the machine_usage table, which contains the information of
 * machine usage.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "machine_usage", schema = "machines")
public class MachineUsage implements Serializable {

	private Integer usage;
	private Double hourlyMaintenance;
	private Double hourlyInsurance;
	private Double hourlyDepreciation;

	private MachineUsagePK machineUsagePK;

	/**
	 * Empty constructor to initialize the primary key of the entity.
	 */
	public MachineUsage() {
		this.machineUsagePK = new MachineUsagePK();
	}

	/**
	 * @return usage: determines the usage for the machine is used
	 */
	@Column(name = "usage")
	public Integer getUsage() {
		return usage;
	}

	/**
	 * @param usage
	 *            :determines the usage for the machine is used
	 */
	public void setUsage(Integer usage) {
		this.usage = usage;
	}

	/**
	 * @return hourlyMaintenance: hourly maintenance of the machine
	 */
	@Column(name = "hourly_maintenance")
	public Double getHourlyMaintenance() {
		return hourlyMaintenance;
	}

	/**
	 * @param hourlyMaintenance
	 *            :hourly maintenance of the machine
	 */
	public void setHourlyMaintenance(Double hourlyMaintenance) {
		this.hourlyMaintenance = hourlyMaintenance;
	}

	/**
	 * @return hourlyInsurance: hourly insurance of the machine
	 */
	@Column(name = "hourly_insurance")
	public Double getHourlyInsurance() {
		return hourlyInsurance;
	}

	/**
	 * @param hourlyInsurance
	 *            :hourly insurance of the machine
	 */
	public void setHourlyInsurance(Double hourlyInsurance) {
		this.hourlyInsurance = hourlyInsurance;
	}

	/**
	 * @return hourlyDepreciation: hourly depreciation of the machine
	 */
	@Column(name = "hourly_depreciation")
	public Double getHourlyDepreciation() {
		return hourlyDepreciation;
	}

	/**
	 * @param hourlyDepreciation
	 *            :hourly depreciation of the machine
	 */
	public void setHourlyDepreciation(Double hourlyDepreciation) {
		this.hourlyDepreciation = hourlyDepreciation;
	}

	/**
	 * @return machineUsagePK: object containing the composite key machine usage
	 *         table
	 */
	@EmbeddedId
	public MachineUsagePK getMachineUsagePK() {
		return machineUsagePK;
	}

	/**
	 * @param machineUsagePK
	 *            :object containing the composite key machine usage table
	 */
	public void setMachineUsagePK(MachineUsagePK machineUsagePK) {
		this.machineUsagePK = machineUsagePK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((hourlyDepreciation == null) ? 0 : hourlyDepreciation
						.hashCode());
		result = prime * result
				+ ((hourlyInsurance == null) ? 0 : hourlyInsurance.hashCode());
		result = prime
				* result
				+ ((hourlyMaintenance == null) ? 0 : hourlyMaintenance
						.hashCode());
		result = prime * result + ((usage == null) ? 0 : usage.hashCode());
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
		MachineUsage other = (MachineUsage) obj;
		if (hourlyDepreciation == null) {
			if (other.hourlyDepreciation != null)
				return false;
		} else if (!hourlyDepreciation.equals(other.hourlyDepreciation))
			return false;
		if (hourlyInsurance == null) {
			if (other.hourlyInsurance != null)
				return false;
		} else if (!hourlyInsurance.equals(other.hourlyInsurance))
			return false;
		if (hourlyMaintenance == null) {
			if (other.hourlyMaintenance != null)
				return false;
		} else if (!hourlyMaintenance.equals(other.hourlyMaintenance))
			return false;
		if (usage == null) {
			if (other.usage != null)
				return false;
		} else if (!usage.equals(other.usage))
			return false;
		return true;
	}

}
