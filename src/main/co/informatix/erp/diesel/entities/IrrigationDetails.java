package co.informatix.erp.diesel.entities;

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

import co.informatix.erp.machines.entities.Machines;

/**
 * This class maps the irrigation_details class, which contains the information
 * about a engine if is irrigation engine.
 * 
 * @author Patricia.Patinio
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "irrigation_details", schema = "diesel")
public class IrrigationDetails implements Serializable {

	private int idIrrigationDetails;
	private Date hidrometerOn;
	private Date hidrometerOff;
	private Double waterUsage;
	private Double waterCost;
	private Double duration;
	private Double depreciationCost;
	private Double maintenanceCost;
	private String observations;
	private EngineLog engineLog;
	private Machines machine;
	private Zone zone;

	/**
	 * @return idIrrigationDetails: irrigation_details table identifier.
	 */
	@Id
	@Column(name = "id_irrigation_details", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdIrrigationDetails() {
		return idIrrigationDetails;
	}

	/**
	 * @param idIrrigationDetails
	 *            : irrigation_details table identifier.
	 */
	public void setIdIrrigationDetails(int idIrrigationDetails) {
		this.idIrrigationDetails = idIrrigationDetails;
	}

	/**
	 * @return hidrometerOn: ignition of water count for the irrigation.
	 */
	@Column(name = "hidrometer_on", nullable = true)
	@Temporal(TemporalType.TIME)
	public Date getHidrometerOn() {
		return hidrometerOn;
	}

	/**
	 * @param hidrometerOn
	 *            : ignition of water count for the irrigation.
	 */
	public void setHidrometerOn(Date hidrometerOn) {
		this.hidrometerOn = hidrometerOn;
	}

	/**
	 * @return hidrometerOff: ending of water count for the irrigation.
	 */
	@Column(name = "hidrometer_off", nullable = true)
	@Temporal(TemporalType.TIME)
	public Date getHidrometerOff() {
		return hidrometerOff;
	}

	/**
	 * @param hidrometerOff
	 *            : ending of water count for the irrigation.
	 */
	public void setHidrometerOff(Date hidrometerOff) {
		this.hidrometerOff = hidrometerOff;
	}

	/**
	 * @return waterUsage: water quantity used in the irrigation.
	 */
	@Column(name = "water_usage", nullable = true)
	public Double getWaterUsage() {
		return waterUsage;
	}

	/**
	 * @param waterUsage
	 *            : water quantity used in the irrigation.
	 */
	public void setWaterUsage(Double waterUsage) {
		this.waterUsage = waterUsage;
	}

	/**
	 * @return waterCost: water cost used in the irrigation.
	 */
	@Column(name = "water_cost", nullable = true)
	public Double getWaterCost() {
		return waterCost;
	}

	/**
	 * @param waterCost
	 *            : water cost used in the irrigation.
	 */
	public void setWaterCost(Double waterCost) {
		this.waterCost = waterCost;
	}

	/**
	 * @return duration: indicate the difference between hidrometerOn and
	 *         hidrometerOff.
	 */
	@Column(name = "duration", nullable = true)
	public Double getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            : indicate the difference between hidrometerOn and
	 *            hidrometerOff.
	 */
	public void setDuration(Double duration) {
		this.duration = duration;
	}

	/**
	 * @return depreciationCost: it is the depreciation cost caused for the
	 *         irrigation.
	 */
	@Column(name = "depreciation_cost", nullable = true)
	public Double getDepreciationCost() {
		return depreciationCost;
	}

	/**
	 * @param depreciationCost
	 *            : it is the depreciation cost caused for the irrigation.
	 */
	public void setDepreciationCost(Double depreciationCost) {
		this.depreciationCost = depreciationCost;
	}

	/**
	 * @return maintenanceCost: maintenance cost caused for the irrigation.
	 */
	@Column(name = "maintenance_cost", nullable = true)
	public Double getMaintenanceCost() {
		return maintenanceCost;
	}

	/**
	 * @param maintenanceCost
	 *            : maintenance cost caused for the irrigation.
	 */
	public void setMaintenanceCost(Double maintenanceCost) {
		this.maintenanceCost = maintenanceCost;
	}

	/**
	 * @return observations: the observation about the irrigation details
	 *         register.
	 */
	@Column(name = "observations", nullable = true)
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations
	 *            : the observation about the irrigation details register.
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return idEngineLog: it is the engine log related with the irrigation
	 *         details information.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_engine_log", referencedColumnName = "id_engine_log", nullable = false)
	public EngineLog getEngineLog() {
		return engineLog;
	}

	/**
	 * @param idEngineLog
	 *            : it is the engine log related with the irrigation details
	 *            information.
	 */
	public void setEngineLog(EngineLog engineLog) {
		this.engineLog = engineLog;
	}

	/**
	 * @return machine: it is the machine related with the irrigation details
	 *         information.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_machine", referencedColumnName = "idmachine", nullable = false)
	public Machines getMachine() {
		return machine;
	}

	/**
	 * @param machine
	 *            : it is the machine related with the irrigation details
	 *            information.
	 */
	public void setMachine(Machines machine) {
		this.machine = machine;
	}

	/**
	 * @return zone: it is the zone related with the irrigation details
	 *         information.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_zone", referencedColumnName = "id", nullable = false)
	public Zone getZone() {
		return zone;
	}

	/**
	 * @param zone
	 *            : it is the zone related with the irrigation details
	 *            information.
	 */
	public void setZone(Zone zone) {
		this.zone = zone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((depreciationCost == null) ? 0 : depreciationCost.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result
				+ ((hidrometerOff == null) ? 0 : hidrometerOff.hashCode());
		result = prime * result
				+ ((hidrometerOn == null) ? 0 : hidrometerOn.hashCode());
		result = prime * result + idIrrigationDetails;
		result = prime * result
				+ ((maintenanceCost == null) ? 0 : maintenanceCost.hashCode());
		result = prime * result
				+ ((observations == null) ? 0 : observations.hashCode());
		result = prime * result
				+ ((waterCost == null) ? 0 : waterCost.hashCode());
		result = prime * result
				+ ((waterUsage == null) ? 0 : waterUsage.hashCode());
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
		IrrigationDetails other = (IrrigationDetails) obj;
		if (depreciationCost == null) {
			if (other.depreciationCost != null)
				return false;
		} else if (!depreciationCost.equals(other.depreciationCost))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (hidrometerOff == null) {
			if (other.hidrometerOff != null)
				return false;
		} else if (!hidrometerOff.equals(other.hidrometerOff))
			return false;
		if (hidrometerOn == null) {
			if (other.hidrometerOn != null)
				return false;
		} else if (!hidrometerOn.equals(other.hidrometerOn))
			return false;
		if (idIrrigationDetails != other.idIrrigationDetails)
			return false;
		if (maintenanceCost == null) {
			if (other.maintenanceCost != null)
				return false;
		} else if (!maintenanceCost.equals(other.maintenanceCost))
			return false;
		if (observations == null) {
			if (other.observations != null)
				return false;
		} else if (!observations.equals(other.observations))
			return false;
		if (waterCost == null) {
			if (other.waterCost != null)
				return false;
		} else if (!waterCost.equals(other.waterCost))
			return false;
		if (waterUsage == null) {
			if (other.waterUsage != null)
				return false;
		} else if (!waterUsage.equals(other.waterUsage))
			return false;
		return true;
	}

}
