package co.informatix.erp.lifeCycle.entities;

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
 * This class maps the cycle table, which contains the information of the cycle.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cycle", schema = "life_cycle")
public class Cycle implements Serializable {

	private int idCycle;
	private Integer cycleNumber;
	private Date initialDateTime;
	private Date finalDateTime;
	private Double costHrBudget;
	private Double costHrActual;
	private Double costMachinesEqBudget;
	private Double costMachinesEqActual;
	private Double costServicesBudget;
	private Double costServicesActual;
	private Double costMaterialsBudget;
	private Double costMaterialsActual;
	private Boolean hrRequired;
	private Boolean materialsRequired;
	private Boolean serviceRequired;
	private Boolean machineRequired;

	private Crops crops;
	private ActivityNames activiyNames;

	/**
	 * @return idCycle: Identifier cycle.
	 */
	@Id
	@Column(name = "idcycle", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdCycle() {
		return idCycle;
	}

	/**
	 * @param idCycle
	 *            : Identifier cycle.
	 */
	public void setIdCycle(int idCycle) {
		this.idCycle = idCycle;
	}

	/**
	 * @return cycleNumber: Number of the cycle.
	 */
	@Column(name = "cycle_number", nullable = false)
	public Integer getCycleNumber() {
		return cycleNumber;
	}

	/**
	 * @param cycleNumber
	 *            : Number of the cycle.
	 */
	public void setCycleNumber(Integer cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	/**
	 * @return initialDateTime: time of the start date.
	 */
	@Column(name = "initial_date_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDateTime() {
		return initialDateTime;
	}

	/**
	 * @param initialDateTime
	 *            : time of the start date.
	 */
	public void setInitialDateTime(Date initialDateTime) {
		this.initialDateTime = initialDateTime;
	}

	/**
	 * @return finalDateTime: time of the final date.
	 */
	@Column(name = "final_date_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDateTime() {
		return finalDateTime;
	}

	/**
	 * @param finalDateTime
	 *            : time of the final date.
	 */
	public void setFinalDateTime(Date finalDateTime) {
		this.finalDateTime = finalDateTime;
	}

	/**
	 * @return costHrBudget: cost budget for the cycle.
	 */
	@Column(name = "cost_hr_budget")
	public Double getCostHrBudget() {
		return costHrBudget;
	}

	/**
	 * @param costHrBudget
	 *            : cost budget for the cycle.
	 */
	public void setCostHrBudget(Double costHrBudget) {
		this.costHrBudget = costHrBudget;
	}

	/**
	 * @return costHrActual: Actual cost of the cycle.
	 */
	@Column(name = "cost_hr_actual")
	public Double getCostHrActual() {
		return costHrActual;
	}

	/**
	 * @param costHrActual
	 *            : Actual cost of the cycle.
	 */
	public void setCostHrActual(Double costHrActual) {
		this.costHrActual = costHrActual;
	}

	/**
	 * @return costMachinesEqBudget: Budget cost of machines and equipment.
	 */
	@Column(name = "cost_machines_eq_budget")
	public Double getCostMachinesEqBudget() {
		return costMachinesEqBudget;
	}

	/**
	 * @param costMachinesEqBudget
	 *            : Budget cost of machines and equipment.
	 */
	public void setCostMachinesEqBudget(Double costMachinesEqBudget) {
		this.costMachinesEqBudget = costMachinesEqBudget;
	}

	/**
	 * @return costMachinesEqActual: Actual cost of machines and equipment.
	 */
	@Column(name = "cost_machines_eq_actual")
	public Double getCostMachinesEqActual() {
		return costMachinesEqActual;
	}

	/**
	 * @param costMachinesEqActual
	 *            : Actual cost of machines and equipment.
	 */
	public void setCostMachinesEqActual(Double costMachinesEqActual) {
		this.costMachinesEqActual = costMachinesEqActual;
	}

	/**
	 * @return costServicesBudget: Budget cost of services.
	 */
	@Column(name = "cost_services_budget")
	public Double getCostServicesBudget() {
		return costServicesBudget;
	}

	/**
	 * @param costServicesBudget
	 *            : Budget cost of services.
	 */
	public void setCostServicesBudget(Double costServicesBudget) {
		this.costServicesBudget = costServicesBudget;
	}

	/**
	 * @return costServicesActual: Actual cost of services.
	 */
	@Column(name = "cost_services_actual")
	public Double getCostServicesActual() {
		return costServicesActual;
	}

	/**
	 * @param costServicesActual
	 *            : Actual cost of services.
	 */
	public void setCostServicesActual(Double costServicesActual) {
		this.costServicesActual = costServicesActual;
	}

	/**
	 * @return costMaterialsBudget: Budget cost of materials.
	 */
	@Column(name = "cost_materials_budget")
	public Double getCostMaterialsBudget() {
		return costMaterialsBudget;
	}

	/**
	 * @param costMaterialsBudget
	 *            : Budget cost of materials.
	 */
	public void setCostMaterialsBudget(Double costMaterialsBudget) {
		this.costMaterialsBudget = costMaterialsBudget;
	}

	/**
	 * @return costMaterialsActual: Actual cost of materials.
	 */
	@Column(name = "cost_materials_actual")
	public Double getCostMaterialsActual() {
		return costMaterialsActual;
	}

	/**
	 * @param costMaterialsActual
	 *            : Actual cost of materials.
	 */
	public void setCostMaterialsActual(Double costMaterialsActual) {
		this.costMaterialsActual = costMaterialsActual;
	}

	/**
	 * @return hrRequired: Human resource required in the cycle.
	 */
	@Column(name = "hr_required")
	public Boolean getHrRequired() {
		return hrRequired;
	}

	/**
	 * @param hrRequired
	 *            Human resource required in the cycle.
	 */
	public void setHrRequired(Boolean hrRequired) {
		this.hrRequired = hrRequired;
	}

	/**
	 * @return materialsRequired: : Materials required in the cycle.
	 */
	@Column(name = "materials_required")
	public Boolean getMaterialsRequired() {
		return materialsRequired;
	}

	/**
	 * @param materialsRequired
	 *            : Materials required in the cycle.
	 */
	public void setMaterialsRequired(Boolean materialsRequired) {
		this.materialsRequired = materialsRequired;
	}

	/**
	 * @return serviceRequired: Service required in the cycle.
	 */
	@Column(name = "service_required")
	public Boolean getServiceRequired() {
		return serviceRequired;
	}

	/**
	 * @param serviceRequired
	 *            : Service required in the cycle.
	 */
	public void setServiceRequired(Boolean serviceRequired) {
		this.serviceRequired = serviceRequired;
	}

	/**
	 * @return machineRequired: Machinery required in the cycle.
	 */
	@Column(name = "machine_required")
	public Boolean getMachineRequired() {
		return machineRequired;
	}

	/**
	 * @param machineRequired
	 *            : Machinery required in the cycle.
	 */
	public void setMachineRequired(Boolean machineRequired) {
		this.machineRequired = machineRequired;
	}

	/**
	 * @return crops: Crop to which the cycle belong
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_crop", referencedColumnName = "idcrop", nullable = false)
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : Crop to which the cycle belong.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return activiyNames: Activity to the which the cycle belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_activity_name", referencedColumnName = "idactivityname", nullable = false)
	public ActivityNames getActiviyNames() {
		return activiyNames;
	}

	/**
	 * @param activiyNames
	 *            : Activity to the which the cycle belong.
	 */
	public void setActiviyNames(ActivityNames activiyNames) {
		this.activiyNames = activiyNames;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((costHrActual == null) ? 0 : costHrActual.hashCode());
		result = prime * result
				+ ((costHrBudget == null) ? 0 : costHrBudget.hashCode());
		result = prime
				* result
				+ ((costMachinesEqActual == null) ? 0 : costMachinesEqActual
						.hashCode());
		result = prime
				* result
				+ ((costMachinesEqBudget == null) ? 0 : costMachinesEqBudget
						.hashCode());
		result = prime
				* result
				+ ((costMaterialsActual == null) ? 0 : costMaterialsActual
						.hashCode());
		result = prime
				* result
				+ ((costMaterialsBudget == null) ? 0 : costMaterialsBudget
						.hashCode());
		result = prime
				* result
				+ ((costServicesActual == null) ? 0 : costServicesActual
						.hashCode());
		result = prime
				* result
				+ ((costServicesBudget == null) ? 0 : costServicesBudget
						.hashCode());
		result = prime * result
				+ ((cycleNumber == null) ? 0 : cycleNumber.hashCode());
		result = prime * result
				+ ((finalDateTime == null) ? 0 : finalDateTime.hashCode());
		result = prime * result
				+ ((hrRequired == null) ? 0 : hrRequired.hashCode());
		result = prime * result + idCycle;
		result = prime * result
				+ ((initialDateTime == null) ? 0 : initialDateTime.hashCode());
		result = prime * result
				+ ((machineRequired == null) ? 0 : machineRequired.hashCode());
		result = prime
				* result
				+ ((materialsRequired == null) ? 0 : materialsRequired
						.hashCode());
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
		Cycle other = (Cycle) obj;
		if (costHrActual == null) {
			if (other.costHrActual != null)
				return false;
		} else if (!costHrActual.equals(other.costHrActual))
			return false;
		if (costHrBudget == null) {
			if (other.costHrBudget != null)
				return false;
		} else if (!costHrBudget.equals(other.costHrBudget))
			return false;
		if (costMachinesEqActual == null) {
			if (other.costMachinesEqActual != null)
				return false;
		} else if (!costMachinesEqActual.equals(other.costMachinesEqActual))
			return false;
		if (costMachinesEqBudget == null) {
			if (other.costMachinesEqBudget != null)
				return false;
		} else if (!costMachinesEqBudget.equals(other.costMachinesEqBudget))
			return false;
		if (costMaterialsActual == null) {
			if (other.costMaterialsActual != null)
				return false;
		} else if (!costMaterialsActual.equals(other.costMaterialsActual))
			return false;
		if (costMaterialsBudget == null) {
			if (other.costMaterialsBudget != null)
				return false;
		} else if (!costMaterialsBudget.equals(other.costMaterialsBudget))
			return false;
		if (costServicesActual == null) {
			if (other.costServicesActual != null)
				return false;
		} else if (!costServicesActual.equals(other.costServicesActual))
			return false;
		if (costServicesBudget == null) {
			if (other.costServicesBudget != null)
				return false;
		} else if (!costServicesBudget.equals(other.costServicesBudget))
			return false;
		if (cycleNumber == null) {
			if (other.cycleNumber != null)
				return false;
		} else if (!cycleNumber.equals(other.cycleNumber))
			return false;
		if (finalDateTime == null) {
			if (other.finalDateTime != null)
				return false;
		} else if (!finalDateTime.equals(other.finalDateTime))
			return false;
		if (hrRequired == null) {
			if (other.hrRequired != null)
				return false;
		} else if (!hrRequired.equals(other.hrRequired))
			return false;
		if (idCycle != other.idCycle)
			return false;
		if (initialDateTime == null) {
			if (other.initialDateTime != null)
				return false;
		} else if (!initialDateTime.equals(other.initialDateTime))
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
		if (serviceRequired == null) {
			if (other.serviceRequired != null)
				return false;
		} else if (!serviceRequired.equals(other.serviceRequired))
			return false;
		return true;
	}

}
