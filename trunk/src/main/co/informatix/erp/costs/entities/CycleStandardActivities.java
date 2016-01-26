package co.informatix.erp.costs.entities;

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

import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;

/**
 * This class maps the cycle_standard_activities table, which contains the
 * information standards activities.
 * 
 * @author Marcela.Chaparro
 * @modify 23/06/2015 Andres.Gomez
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cycle_standard_activities", schema = "costs")
public class CycleStandardActivities implements Serializable {

	private int idCycleActivity;
	private Integer sequenceNumber;
	private Double duration;
	private Double costHrHa;
	private Double costMachinesEqHa;
	private Double costServicesHa;
	private Double costMaterialsHa;
	private Double generalCostHa;
	private Boolean dangerous;
	private Boolean hrRequired;
	private Boolean machineRequired;
	private Boolean serviceRequired;
	private Boolean materialsRequired;

	private CropNames cropNames;
	private ActivityNames activityNames;

	/**
	 * Constructor method.
	 */
	public CycleStandardActivities() {
		this.cropNames = new CropNames();
		this.activityNames = new ActivityNames();
	}

	/**
	 * @return idCycleActivity: identifier the table cycle_standard_activities.
	 */
	@Id
	@Column(name = "idcycleactivity", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdCycleActivity() {
		return idCycleActivity;
	}

	/**
	 * @param idCycleActivity
	 *            : identifier the table cycle_standard_activities.
	 */
	public void setIdCycleActivity(int idCycleActivity) {
		this.idCycleActivity = idCycleActivity;
	}

	/**
	 * @return hrRequired: Human resource required in the activity.
	 */
	@Column(name = "hr_required")
	public Boolean getHrRequired() {
		return hrRequired;
	}

	/**
	 * @param hrRequired
	 *            : Human resource required in the activity.
	 */
	public void setHrRequired(Boolean hrRequired) {
		this.hrRequired = hrRequired;
	}

	/**
	 * @return machineRequired: Machinery required.
	 */
	@Column(name = "machine_required")
	public Boolean getMachineRequired() {
		return machineRequired;
	}

	/**
	 * @param machineRequired
	 *            : Machinery required.
	 */
	public void setMachineRequired(Boolean machineRequired) {
		this.machineRequired = machineRequired;
	}

	/**
	 * @return serviceRequired: Service required.
	 */
	@Column(name = "service_required")
	public Boolean getServiceRequired() {
		return serviceRequired;
	}

	/**
	 * @param serviceRequired
	 *            : Service required.
	 */
	public void setServiceRequired(Boolean serviceRequired) {
		this.serviceRequired = serviceRequired;
	}

	/**
	 * @return materialsRequired: Required materials.
	 */
	@Column(name = "materials_required")
	public Boolean getMaterialsRequired() {
		return materialsRequired;
	}

	/**
	 * @param materialsRequired
	 *            : Required materials.
	 */
	public void setMaterialsRequired(Boolean materialsRequired) {
		this.materialsRequired = materialsRequired;
	}

	/**
	 * @return sequenceNumber: Sequence number.
	 */
	@Column(name = "sequence_number")
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            : Sequence number.
	 */
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return duration: Duration of the activity.
	 */
	@Column(name = "duration")
	public Double getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            : Duration of the activity.
	 */
	public void setDuration(Double duration) {
		this.duration = duration;
	}

	/**
	 * @return costHrHa: Human Resource Cost per hectare.
	 */
	@Column(name = "cost_hr_ha")
	public Double getCostHrHa() {
		return costHrHa;
	}

	/**
	 * @param costHrHa
	 *            : Human Resource Cost per hectare.
	 */
	public void setCostHrHa(Double costHrHa) {
		this.costHrHa = costHrHa;
	}

	/**
	 * @return costMachinesEqHa: of machines and equipment Cost per hectare.
	 */
	@Column(name = "cost_machines_eq_ha")
	public Double getCostMachinesEqHa() {
		return costMachinesEqHa;
	}

	/**
	 * @param costMachinesEqHa
	 *            : of machines and equipment Cost per hectare.
	 */
	public void setCostMachinesEqHa(Double costMachinesEqHa) {
		this.costMachinesEqHa = costMachinesEqHa;
	}

	/**
	 * @return costServicesHa: Cost of services per hectare.
	 */
	@Column(name = "cost_services_ha")
	public Double getCostServicesHa() {
		return costServicesHa;
	}

	/**
	 * @param costServicesHa
	 *            : Cost of services per hectare.
	 */
	public void setCostServicesHa(Double costServicesHa) {
		this.costServicesHa = costServicesHa;
	}

	/**
	 * @return costMaterialsHa: Cost of materials per hectare.
	 */
	@Column(name = "cost_materials_ha")
	public Double getCostMaterialsHa() {
		return costMaterialsHa;
	}

	/**
	 * @param costMaterialsHa
	 *            : Cost of materials per hectare.
	 */
	public void setCostMaterialsHa(Double costMaterialsHa) {
		this.costMaterialsHa = costMaterialsHa;
	}

	/**
	 * @return generalCostHa: Overall costs per hectare.
	 */
	@Column(name = "general_cost_ha")
	public Double getGeneralCostHa() {
		return generalCostHa;
	}

	/**
	 * @param generalCostHa
	 *            : Overall costs per hectare.
	 */
	public void setGeneralCostHa(Double generalCostHa) {
		this.generalCostHa = generalCostHa;
	}

	/**
	 * @return dangerous: It indicates whether the activity is dangerous or not.
	 */
	@Column(name = "dangerous")
	public Boolean getDangerous() {
		return dangerous;
	}

	/**
	 * @param dangerous
	 *            : It indicates whether the activity is dangerous or not.
	 */
	public void setDangerous(Boolean dangerous) {
		this.dangerous = dangerous;
	}

	/**
	 * @return cropNames: Name associated with crop activity.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_crop_name", referencedColumnName = "idcropname", nullable = false)
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            : Name associated with crop activity.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return activityNames: Name of the related activity.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_activity_name", referencedColumnName = "idactivityname", nullable = false)
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Name of the related activity.
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((costHrHa == null) ? 0 : costHrHa.hashCode());
		result = prime
				* result
				+ ((costMachinesEqHa == null) ? 0 : costMachinesEqHa.hashCode());
		result = prime * result
				+ ((costMaterialsHa == null) ? 0 : costMaterialsHa.hashCode());
		result = prime * result
				+ ((costServicesHa == null) ? 0 : costServicesHa.hashCode());
		result = prime * result
				+ ((dangerous == null) ? 0 : dangerous.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result
				+ ((generalCostHa == null) ? 0 : generalCostHa.hashCode());
		result = prime * result
				+ ((hrRequired == null) ? 0 : hrRequired.hashCode());
		result = prime * result + idCycleActivity;
		result = prime * result
				+ ((machineRequired == null) ? 0 : machineRequired.hashCode());
		result = prime
				* result
				+ ((materialsRequired == null) ? 0 : materialsRequired
						.hashCode());
		result = prime * result
				+ ((sequenceNumber == null) ? 0 : sequenceNumber.hashCode());
		result = prime * result
				+ ((serviceRequired == null) ? 0 : serviceRequired.hashCode());
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
		CycleStandardActivities other = (CycleStandardActivities) obj;
		if (costHrHa == null) {
			if (other.costHrHa != null)
				return false;
		} else if (!costHrHa.equals(other.costHrHa))
			return false;
		if (costMachinesEqHa == null) {
			if (other.costMachinesEqHa != null)
				return false;
		} else if (!costMachinesEqHa.equals(other.costMachinesEqHa))
			return false;
		if (costMaterialsHa == null) {
			if (other.costMaterialsHa != null)
				return false;
		} else if (!costMaterialsHa.equals(other.costMaterialsHa))
			return false;
		if (costServicesHa == null) {
			if (other.costServicesHa != null)
				return false;
		} else if (!costServicesHa.equals(other.costServicesHa))
			return false;
		if (dangerous == null) {
			if (other.dangerous != null)
				return false;
		} else if (!dangerous.equals(other.dangerous))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (generalCostHa == null) {
			if (other.generalCostHa != null)
				return false;
		} else if (!generalCostHa.equals(other.generalCostHa))
			return false;
		if (hrRequired == null) {
			if (other.hrRequired != null)
				return false;
		} else if (!hrRequired.equals(other.hrRequired))
			return false;
		if (idCycleActivity != other.idCycleActivity)
			return false;
		if (machineRequired == null) {
			if (other.machineRequired != null)
				return false;
		} else if (!machineRequired.equals(other.machineRequired))
			return false;
		if (materialsRequired == null) {
			if (other.materialsRequired != null)
				return false;
		} else if (!materialsRequired.equals(other.materialsRequired))
			return false;
		if (sequenceNumber == null) {
			if (other.sequenceNumber != null)
				return false;
		} else if (!sequenceNumber.equals(other.sequenceNumber))
			return false;
		if (serviceRequired == null) {
			if (other.serviceRequired != null)
				return false;
		} else if (!serviceRequired.equals(other.serviceRequired))
			return false;
		return true;
	}

}
