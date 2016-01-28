package co.informatix.erp.services.entities;

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

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * This class maps the external_services_and_activities table, which contains
 * the information of services and activities.
 * 
 * @author Andres.Gomez
 * 
 */
@Entity
@Table(name = "external_services_and_activities", schema = "services")
@SuppressWarnings("serial")
public class ActivitiesAndServices implements Serializable {

	private int idService;

	private String description;

	private Date initialDt;
	private Date finalDt;

	private Double durationActual;
	private Double durationBudget;
	private Double hourCostBudget;
	private Double hourCostActual;
	private Double totalCostBudget;
	private Double totalCostActual;

	private boolean taskWork;

	private Activities activities;
	private ServiceType serviceType;
	private Suppliers suppliers;

	/**
	 * Constructor method.
	 */
	public ActivitiesAndServices() {
		this.activities = new Activities();
		this.serviceType = new ServiceType();
		this.suppliers = new Suppliers();
	}

	/**
	 * @return idService: Table identifier activities and services
	 */
	@Id
	@Column(name = "idservice", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdService() {
		return idService;
	}

	/**
	 * @param idService
	 *            :Table identifier activities and services
	 */
	public void setIdService(int idService) {
		this.idService = idService;
	}

	/**
	 * @return description: Service description.
	 */
	@Column(name = "description", length = 200, nullable = true)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Service description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return initialDt: Date of commencement of activity and service.
	 */
	@Column(name = "initial_date_time")
	@Temporal(TemporalType.DATE)
	public Date getInitialDt() {
		return initialDt;
	}

	/**
	 * @param initialDt
	 *            :Date of commencement of activity and service.
	 */
	public void setInitialDt(Date initialDt) {
		this.initialDt = initialDt;
	}

	/**
	 * @return finalDt: Ending date of the activity and service.
	 */
	@Column(name = "final_date_time")
	@Temporal(TemporalType.DATE)
	public Date getFinalDt() {
		return finalDt;
	}

	/**
	 * @param finalDt
	 *            :Ending date of the activity and service.
	 */
	public void setFinalDt(Date finalDt) {
		this.finalDt = finalDt;
	}

	/**
	 * @return durationActual: Duration of current activity and service.
	 */
	@Column(name = "duration_actual")
	public Double getDurationActual() {
		return durationActual;
	}

	/**
	 * @param durationActual
	 *            :Duration of current activity and service.
	 */
	public void setDurationActual(Double durationActual) {
		this.durationActual = durationActual;
	}

	/**
	 * @return durationBudget: Duration of activity and budgeted service.
	 */
	@Column(name = "duration_budget")
	public Double getDurationBudget() {
		return durationBudget;
	}

	/**
	 * @param durationBudget
	 *            :Duration of activity and budgeted service.
	 */
	public void setDurationBudget(Double durationBudget) {
		this.durationBudget = durationBudget;
	}

	/**
	 * @return hourCostBudget :cost budget for the activity and service.
	 */
	@Column(name = "hour_cost_budget")
	public Double getHourCostBudget() {
		return hourCostBudget;
	}

	/**
	 * @param hourCostBudget
	 *            :cost budget for the activity and service.
	 */
	public void setHourCostBudget(Double hourCostBudget) {
		this.hourCostBudget = hourCostBudget;
	}

	/**
	 * @return hourCostActual: Actual cost of the activity and service.
	 */
	@Column(name = "hour_cost_actual")
	public Double getHourCostActual() {
		return hourCostActual;
	}

	/**
	 * @param hourCostActual
	 *            :Actual cost of the activity and service.
	 */
	public void setHourCostActual(Double hourCostActual) {
		this.hourCostActual = hourCostActual;
	}

	/**
	 * @return totalCostBudget: Budget of the total cost of the activity and
	 *         service.
	 */
	@Column(name = "total_cost_budget")
	public Double getTotalCostBudget() {
		return totalCostBudget;
	}

	/**
	 * @param totalCostBudget
	 *            : Budget of the total cost of the activity and service.
	 */
	public void setTotalCostBudget(Double totalCostBudget) {
		this.totalCostBudget = totalCostBudget;
	}

	/**
	 * @return totalCostActual: Current cost total activity and service.
	 */
	@Column(name = "total_cost_actual")
	public Double getTotalCostActual() {
		return totalCostActual;
	}

	/**
	 * @param totalCostActual
	 *            :Current cost total activity and service.
	 */
	public void setTotalCostActual(Double totalCostActual) {
		this.totalCostActual = totalCostActual;
	}

	/**
	 * @return taskWork: True value that determines whether the job is assigned
	 *         as homework.
	 */
	@Column(name = "task_work")
	public boolean isTaskWork() {
		return taskWork;
	}

	/**
	 * @param taskWork
	 *            :True value that determines whether the job is assigned as
	 *            homework.
	 */
	public void setTaskWork(boolean taskWork) {
		this.taskWork = taskWork;
	}

	/**
	 * @return activities: Gets the service activity.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactivity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            :Sets the service activity.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return serviceType: Gets the type of service for the service.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idservicetype", referencedColumnName = "idservicetype", nullable = false)
	public ServiceType getServiceType() {
		return serviceType;
	}

	/**
	 * @param serviceType
	 *            :Sets the type of service for the service.
	 */
	public void setServiceType(ServiceType serviceType) {
		this.serviceType = serviceType;
	}

	/**
	 * @return suppliers: Gets the supplier for activity and service.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsupplier", referencedColumnName = "idsupplier", nullable = false)
	public Suppliers getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            :Sets the supplier for activity and service.
	 */
	public void setSuppliers(Suppliers suppliers) {
		this.suppliers = suppliers;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((durationActual == null) ? 0 : durationActual.hashCode());
		result = prime * result
				+ ((durationBudget == null) ? 0 : durationBudget.hashCode());
		result = prime * result + ((finalDt == null) ? 0 : finalDt.hashCode());
		result = prime * result
				+ ((hourCostActual == null) ? 0 : hourCostActual.hashCode());
		result = prime * result
				+ ((hourCostBudget == null) ? 0 : hourCostBudget.hashCode());
		result = prime * result + idService;
		result = prime * result
				+ ((initialDt == null) ? 0 : initialDt.hashCode());
		result = prime * result + (taskWork ? 1231 : 1237);
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
		ActivitiesAndServices other = (ActivitiesAndServices) obj;
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
		if (finalDt == null) {
			if (other.finalDt != null)
				return false;
		} else if (!finalDt.equals(other.finalDt))
			return false;
		if (hourCostActual == null) {
			if (other.hourCostActual != null)
				return false;
		} else if (!hourCostActual.equals(other.hourCostActual))
			return false;
		if (hourCostBudget == null) {
			if (other.hourCostBudget != null)
				return false;
		} else if (!hourCostBudget.equals(other.hourCostBudget))
			return false;
		if (idService != other.idService)
			return false;
		if (initialDt == null) {
			if (other.initialDt != null)
				return false;
		} else if (!initialDt.equals(other.initialDt))
			return false;
		if (taskWork != other.taskWork)
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
