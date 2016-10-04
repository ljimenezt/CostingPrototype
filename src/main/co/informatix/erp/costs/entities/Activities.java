package co.informatix.erp.costs.entities;

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
import javax.persistence.Transient;

import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Cycle;

/**
 * This class maps the activities table, which contains the information of the
 * system activities costs.
 * 
 * @author Marcela.Chaparro
 * 
 * @modify 23/06/2015 Mabell.Boada
 * @modify 22/07/2015 Gerardo.Herrera
 * @modify 04/10/2016 Luna.Granados
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activities", schema = "costs")
public class Activities implements Serializable {

	private int idActivity;
	private String description;
	private Integer sequenceNumber;
	private Date initialDtBudget;
	private Date finalDtBudget;
	private Date initialDtActual;
	private Date finalDtActual;
	private Double durationBudget;
	private Double costHrBudget;
	private Double costHrActual;
	private Double costMachinesEqBudget;
	private Double costMachinesEqActual;
	private Double costServicesBudget;
	private Double costServicesActual;
	private Double costMaterialsBudget;
	private Double costMaterialsActual;
	private Double generalCostBudget;
	private Double generalCostActual;
	private Double durationActual;
	private Boolean dangerous;
	private Boolean hrRequired;
	private Boolean machineRequired;
	private Boolean serviceRequired;
	private Boolean materialsRequired;
	private boolean routine;
	private boolean seleccionado;
	private boolean calculateCosts;

	private Crops crop;
	private ActivityNames activityName;
	private Cycle cycle;

	/**
	 * Constructor method.
	 */
	public Activities() {
		this.crop = new Crops();
		this.activityName = new ActivityNames();
	}

	/**
	 * 
	 * @return idActivity: Activities table identifier.
	 */
	@Id
	@Column(name = "idactivity", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdActivity() {
		return idActivity;
	}

	/**
	 * @param idActivity
	 *            : Activities table identifier.
	 */
	public void setIdActivity(int idActivity) {
		this.idActivity = idActivity;
	}

	/**
	 * @return description: Description of the activity.
	 */
	@Column(name = "description", nullable = true)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of the activity.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return sequenceNumber: Sequence number of the activity.
	 */
	@Column(name = "sequence_number")
	public Integer getSequenceNumber() {
		return sequenceNumber;
	}

	/**
	 * @param sequenceNumber
	 *            : Sequence number of the activity.
	 */
	public void setSequenceNumber(Integer sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	/**
	 * @return initialDtBudget: Estimated start date of the activity.
	 */
	@Column(name = "initial_date_time_budget")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDtBudget() {
		return initialDtBudget;
	}

	/**
	 * @param initialDtBudget
	 *            : Estimated start date of the activity.
	 */
	public void setInitialDtBudget(Date initialDtBudget) {
		this.initialDtBudget = initialDtBudget;
	}

	/**
	 * @return finalDtBudget: Estimated end date of the activity.
	 */
	@Column(name = "final_date_time_budget")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDtBudget() {
		return finalDtBudget;
	}

	/**
	 * @param finalDtBudget
	 *            : Estimated end date of the activity.
	 */
	public void setFinalDtBudget(Date finalDtBudget) {
		this.finalDtBudget = finalDtBudget;
	}

	/**
	 * @return initialDtActual: Actual start date of the activity.
	 */
	@Column(name = "initial_date_time_actual")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDtActual() {
		return initialDtActual;
	}

	/**
	 * @param initialDtActual
	 *            : Actual start date of the activity.
	 */
	public void setInitialDtActual(Date initialDtActual) {
		this.initialDtActual = initialDtActual;
	}

	/**
	 * @return finalDtActual: Actual end date of the activity.
	 */
	@Column(name = "final_date_time_actual")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDtActual() {
		return finalDtActual;
	}

	/**
	 * @param finalDtActual
	 *            : Actual end date of the activity.
	 */
	public void setFinalDtActual(Date finalDtActual) {
		this.finalDtActual = finalDtActual;
	}

	/**
	 * @return durationBudget: Duration of the activity.
	 */
	@Column(name = "duration_budget")
	public Double getDurationBudget() {
		return durationBudget;
	}

	/**
	 * @param durationBudget
	 *            : Duration of the activity.
	 */
	public void setDurationBudget(Double durationBudget) {
		this.durationBudget = durationBudget;
	}

	/**
	 * @return costHrBudget: cost budget for the activity.
	 */
	@Column(name = "cost_hr_budget")
	public Double getCostHrBudget() {
		return costHrBudget;
	}

	/**
	 * @param costHrBudget
	 *            : cost budget for the activity.
	 */
	public void setCostHrBudget(Double costHrBudget) {
		this.costHrBudget = costHrBudget;
	}

	/**
	 * @return costHrActual: Actual cost of the activity.
	 */
	@Column(name = "cost_hr_actual")
	public Double getCostHrActual() {
		return costHrActual;
	}

	/**
	 * @param costHrActual
	 *            : Actual cost of the activity.
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
	 * @return generalCostBudget: General budget of costs.
	 */
	@Column(name = "general_cost_budget")
	public Double getGeneralCostBudget() {
		return generalCostBudget;
	}

	/**
	 * @param generalCostBudget
	 *            : General budget of costs.
	 */
	public void setGeneralCostBudget(Double generalCostBudget) {
		this.generalCostBudget = generalCostBudget;
	}

	/**
	 * @return generalCostActual: Actual overall cost.
	 */
	@Column(name = "general_cost_actual")
	public Double getGeneralCostActual() {
		return generalCostActual;
	}

	/**
	 * @param generalCostActual
	 *            : Actual overall cost.
	 */
	public void setGeneralCostActual(Double generalCostActual) {
		this.generalCostActual = generalCostActual;
	}

	/**
	 * @return durationActual: Actual duration of the activity.
	 */
	@Column(name = "duration_actual")
	public Double getDurationActual() {
		return durationActual;
	}

	/**
	 * @param durationActual
	 *            : Actual duration of the activity.
	 */
	public void setDurationActual(Double durationActual) {
		this.durationActual = durationActual;
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
	 * @return machineRequired: Machinery required activity.
	 */
	@Column(name = "machine_required")
	public Boolean getMachineRequired() {
		return machineRequired;
	}

	/**
	 * @param machineRequired
	 *            : Machinery required activity.
	 */
	public void setMachineRequired(Boolean machineRequired) {
		this.machineRequired = machineRequired;
	}

	/**
	 * @return serviceRequired: Service activity required.
	 */
	@Column(name = "service_required")
	public Boolean getServiceRequired() {
		return serviceRequired;
	}

	/**
	 * @param serviceRequired
	 *            : Service activity required.
	 */
	public void setServiceRequired(Boolean serviceRequired) {
		this.serviceRequired = serviceRequired;
	}

	/**
	 * @return materialsRequired: Materials required in the activity.
	 */
	@Column(name = "materials_required")
	public Boolean getMaterialsRequired() {
		return materialsRequired;
	}

	/**
	 * @param materialsRequired
	 *            : Materials required in the activity.
	 */
	public void setMaterialsRequired(Boolean materialsRequired) {
		this.materialsRequired = materialsRequired;
	}

	/**
	 * @return routine: Duration of activity is routine.
	 */
	@Column(name = "routine")
	public boolean isRoutine() {
		return routine;
	}

	/**
	 * @param routine
	 *            :Duration of activity is routine.
	 */
	public void setRoutine(boolean routine) {
		this.routine = routine;
	}

	/**
	 * @return seleccionado: Object selected from the list of activities
	 */
	@Transient
	public boolean isSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado
	 *            : Object selected from the list of activities
	 */
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	/**
	 * @return calculateCosts: Object calculateCosts from the list of activities
	 */
	@Transient
	public boolean isCalculateCosts() {
		return calculateCosts;
	}

	/**
	 * @param calculateCosts
	 *            : Object calculateCosts from the list of activities
	 */
	public void setCalculateCosts(boolean calculateCosts) {
		this.calculateCosts = calculateCosts;
	}

	/**
	 * @return crop: Crop related to the activity.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_crop", referencedColumnName = "idcrop", nullable = false)
	public Crops getCrop() {
		return crop;
	}

	/**
	 * @param crop
	 *            : Crop related to the activity.
	 */
	public void setCrop(Crops crop) {
		this.crop = crop;
	}

	/**
	 * @return activityName: Name of the activities related to it.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_activity_name", referencedColumnName = "idactivityname", nullable = false)
	public ActivityNames getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName
	 *            : Name of the activities related to it.
	 */
	public void setActivityName(ActivityNames activityName) {
		this.activityName = activityName;
	}

	/**
	 * @return cycle: Cycle related to the activity.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cycle", referencedColumnName = "idcycle")
	public Cycle getCycle() {
		return cycle;
	}

	/**
	 * @param cycle
	 *            : Cycle related to the activity.
	 */
	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
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
				+ ((dangerous == null) ? 0 : dangerous.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((durationActual == null) ? 0 : durationActual.hashCode());
		result = prime * result
				+ ((durationBudget == null) ? 0 : durationBudget.hashCode());
		result = prime * result
				+ ((finalDtActual == null) ? 0 : finalDtActual.hashCode());
		result = prime * result
				+ ((finalDtBudget == null) ? 0 : finalDtBudget.hashCode());
		result = prime
				* result
				+ ((generalCostActual == null) ? 0 : generalCostActual
						.hashCode());
		result = prime
				* result
				+ ((generalCostBudget == null) ? 0 : generalCostBudget
						.hashCode());
		result = prime * result
				+ ((hrRequired == null) ? 0 : hrRequired.hashCode());
		result = prime * result + idActivity;
		result = prime * result
				+ ((initialDtActual == null) ? 0 : initialDtActual.hashCode());
		result = prime * result
				+ ((initialDtBudget == null) ? 0 : initialDtBudget.hashCode());
		result = prime * result
				+ ((machineRequired == null) ? 0 : machineRequired.hashCode());
		result = prime
				* result
				+ ((materialsRequired == null) ? 0 : materialsRequired
						.hashCode());
		result = prime * result + (routine ? 1231 : 1237);
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
		Activities other = (Activities) obj;
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
		if (dangerous == null) {
			if (other.dangerous != null)
				return false;
		} else if (!dangerous.equals(other.dangerous))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
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
		if (finalDtActual == null) {
			if (other.finalDtActual != null)
				return false;
		} else if (!finalDtActual.equals(other.finalDtActual))
			return false;
		if (finalDtBudget == null) {
			if (other.finalDtBudget != null)
				return false;
		} else if (!finalDtBudget.equals(other.finalDtBudget))
			return false;
		if (generalCostActual == null) {
			if (other.generalCostActual != null)
				return false;
		} else if (!generalCostActual.equals(other.generalCostActual))
			return false;
		if (generalCostBudget == null) {
			if (other.generalCostBudget != null)
				return false;
		} else if (!generalCostBudget.equals(other.generalCostBudget))
			return false;
		if (hrRequired == null) {
			if (other.hrRequired != null)
				return false;
		} else if (!hrRequired.equals(other.hrRequired))
			return false;
		if (idActivity != other.idActivity)
			return false;
		if (initialDtActual == null) {
			if (other.initialDtActual != null)
				return false;
		} else if (!initialDtActual.equals(other.initialDtActual))
			return false;
		if (initialDtBudget == null) {
			if (other.initialDtBudget != null)
				return false;
		} else if (!initialDtBudget.equals(other.initialDtBudget))
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
		if (routine != other.routine)
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
