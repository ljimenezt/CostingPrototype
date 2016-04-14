package co.informatix.erp.costs.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.informatix.erp.humanResources.entities.OvertimePaymentRate;

/**
 * This is the entity in charge of mapping the table activities_and_hr
 * 
 * @author Dario Lopez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activities_and_hr", schema = "costs")
public class ActivitiesAndHr implements Serializable, Cloneable {

	private ActivitiesAndHrPK activitiesAndHrPK;

	private Date initialDateTimeActual;
	private Date initialDateTimeBudget;
	private Date finalDateTimeActual;
	private Date finalDateTimeBudget;
	private Double totalCostActual;
	private Double totalCostBudget;
	private Double durationActual;
	private Double durationBudget;
	private Double totalHours;
	private Double normalHours;
	private Double overtimeHours;
	private boolean externalService;
	private boolean taskWork;

	private OvertimePaymentRate overtimePaymentRate;

	/**
	 * Constructor that initializes the foreign key
	 * 
	 * @author Gerardo.Herrera
	 */
	public ActivitiesAndHr() {
		this.overtimePaymentRate = new OvertimePaymentRate();
	}

	/**
	 * @return activitiesAndHrPK: the composite order table key activities and
	 *         HR
	 */
	@EmbeddedId
	public ActivitiesAndHrPK getActivitiesAndHrPK() {
		return activitiesAndHrPK;
	}

	/**
	 * @param activitiesAndHrPK
	 *            : the composite order table key activities and HR
	 */
	public void setActivitiesAndHrPK(ActivitiesAndHrPK activitiesAndHrPK) {
		this.activitiesAndHrPK = activitiesAndHrPK;
	}

	/**
	 * 
	 * @return initialDateTimeActual: current time of the start date
	 */
	@Column(name = "initial_date_time_actual")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDateTimeActual() {
		return initialDateTimeActual;
	}

	/**
	 * @param initialDateTimeActual
	 *            : current time of the start date
	 */
	public void setInitialDateTimeActual(Date initialDateTimeActual) {
		this.initialDateTimeActual = initialDateTimeActual;
	}

	/**
	 * 
	 * @return initialDateTimeBudget: time budget start date
	 */
	@Column(name = "initial_date_time_budget")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDateTimeBudget() {
		return initialDateTimeBudget;
	}

	/**
	 * @param initialDateTimeBudget
	 *            : time budget start date
	 */
	public void setInitialDateTimeBudget(Date initialDateTimeBudget) {
		this.initialDateTimeBudget = initialDateTimeBudget;
	}

	/**
	 * 
	 * @return finalDateTimeActual: Current time deadline
	 */
	@Column(name = "final_date_time_actual")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDateTimeActual() {
		return finalDateTimeActual;
	}

	/**
	 * @param finalDateTimeActual
	 *            : Current time deadline
	 */
	public void setFinalDateTimeActual(Date finalDateTimeActual) {
		this.finalDateTimeActual = finalDateTimeActual;
	}

	/**
	 * 
	 * @return finalDateTimeBudget: time budget end date
	 */
	@Column(name = "final_date_time_budget")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDateTimeBudget() {
		return finalDateTimeBudget;
	}

	/**
	 * @param finalDateTimeBudget
	 *            : time budget end date
	 */
	public void setFinalDateTimeBudget(Date finalDateTimeBudget) {
		this.finalDateTimeBudget = finalDateTimeBudget;
	}

	/**
	 * @return totalCostActual: Value indicating the current total costs
	 * 
	 */
	@Column(name = "total_cost_actual")
	public Double getTotalCostActual() {
		return totalCostActual;
	}

	/**
	 * @param totalCostActual
	 *            : Value indicating the current total costs
	 */
	public void setTotalCostActual(Double totalCostActual) {
		this.totalCostActual = totalCostActual;
	}

	/**
	 * @return totalCostBudget: Value indicating the total cost of the budget
	 */
	@Column(name = "total_cost_budget")
	public Double getTotalCostBudget() {
		return totalCostBudget;
	}

	/**
	 * @param totalCostBudget
	 *            : Value indicating the total cost of the budget
	 */
	public void setTotalCostBudget(Double totalCostBudget) {
		this.totalCostBudget = totalCostBudget;
	}

	/**
	 * @return durationActual: Current indicates the time duration
	 */
	@Column(name = "duration_actual")
	public Double getDurationActual() {
		return durationActual;
	}

	/**
	 * @param durationActual
	 *            : Current indicates the time duration
	 */
	public void setDurationActual(Double durationActual) {
		this.durationActual = durationActual;
	}

	/**
	 * @return durationBudget: It indicates the duration of the budget
	 */
	@Column(name = "duration_budget")
	public Double getDurationBudget() {
		return durationBudget;
	}

	/**
	 * @param durationBudget
	 *            : It indicates the duration of the budget
	 */
	public void setDurationBudget(Double durationBudget) {
		this.durationBudget = durationBudget;
	}

	/**
	 * @return totalHours: It indicates the total number of hours
	 */
	@Column(name = "total_hours")
	public Double getTotalHours() {
		return totalHours;
	}

	/**
	 * @param totalHours
	 *            : It indicates the total number of hours
	 */
	public void setTotalHours(Double totalHours) {
		this.totalHours = totalHours;
	}

	/**
	 * @return normalHours: normal hours
	 */
	@Column(name = "normal_hours")
	public Double getNormalHours() {
		return normalHours;
	}

	/**
	 * @param normalHours
	 *            : normal hours
	 */
	public void setNormalHours(Double normalHours) {
		this.normalHours = normalHours;
	}

	/**
	 * @return overtimeHours: value indicating the number of overtime
	 */
	@Column(name = "overtime_hours")
	public Double getOvertimeHours() {
		return overtimeHours;
	}

	/**
	 * @param overtimeHours
	 *            : value indicating the number of overtime
	 */
	public void setOvertimeHours(Double overtimeHours) {
		this.overtimeHours = overtimeHours;
	}

	/**
	 * @return externalService: external service
	 */
	@Column(name = "external_service")
	public boolean isExternalService() {
		return externalService;
	}

	/**
	 * @param externalService
	 *            : external service
	 */
	public void setExternalService(boolean externalService) {
		this.externalService = externalService;
	}

	/**
	 * @return taskWork: work tasks
	 */
	@Column(name = "task_work")
	public boolean isTaskWork() {
		return taskWork;
	}

	/**
	 * @param taskWork
	 *            : work tasks
	 */
	public void setTaskWork(boolean taskWork) {
		this.taskWork = taskWork;
	}

	/**
	 * @return overtimePaymentRate: factor for calculating overtime hours
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "overtimepaymentid", referencedColumnName = "overtimepaymentid")
	public OvertimePaymentRate getOvertimePaymentRate() {
		return overtimePaymentRate;
	}

	/**
	 * @param overtimePaymentRate
	 *            : factor for calculating overtime hours
	 */
	public void setOvertimePaymentRate(OvertimePaymentRate overtimePaymentRate) {
		this.overtimePaymentRate = overtimePaymentRate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activitiesAndHrPK == null) ? 0 : activitiesAndHrPK
						.hashCode());
		result = prime * result
				+ ((durationActual == null) ? 0 : durationActual.hashCode());
		result = prime * result
				+ ((durationBudget == null) ? 0 : durationBudget.hashCode());
		result = prime * result + (externalService ? 1231 : 1237);
		result = prime
				* result
				+ ((finalDateTimeActual == null) ? 0 : finalDateTimeActual
						.hashCode());
		result = prime
				* result
				+ ((finalDateTimeBudget == null) ? 0 : finalDateTimeBudget
						.hashCode());
		result = prime
				* result
				+ ((initialDateTimeActual == null) ? 0 : initialDateTimeActual
						.hashCode());
		result = prime
				* result
				+ ((initialDateTimeBudget == null) ? 0 : initialDateTimeBudget
						.hashCode());
		result = prime * result
				+ ((normalHours == null) ? 0 : normalHours.hashCode());
		result = prime * result
				+ ((overtimeHours == null) ? 0 : overtimeHours.hashCode());
		result = prime * result + (taskWork ? 1231 : 1237);
		result = prime * result
				+ ((totalCostActual == null) ? 0 : totalCostActual.hashCode());
		result = prime * result
				+ ((totalCostBudget == null) ? 0 : totalCostBudget.hashCode());
		result = prime * result
				+ ((totalHours == null) ? 0 : totalHours.hashCode());
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
		ActivitiesAndHr other = (ActivitiesAndHr) obj;
		if (activitiesAndHrPK == null) {
			if (other.activitiesAndHrPK != null)
				return false;
		} else if (!activitiesAndHrPK.equals(other.activitiesAndHrPK))
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
		if (externalService != other.externalService)
			return false;
		if (finalDateTimeActual == null) {
			if (other.finalDateTimeActual != null)
				return false;
		} else if (!finalDateTimeActual.equals(other.finalDateTimeActual))
			return false;
		if (finalDateTimeBudget == null) {
			if (other.finalDateTimeBudget != null)
				return false;
		} else if (!finalDateTimeBudget.equals(other.finalDateTimeBudget))
			return false;
		if (initialDateTimeActual == null) {
			if (other.initialDateTimeActual != null)
				return false;
		} else if (!initialDateTimeActual.equals(other.initialDateTimeActual))
			return false;
		if (initialDateTimeBudget == null) {
			if (other.initialDateTimeBudget != null)
				return false;
		} else if (!initialDateTimeBudget.equals(other.initialDateTimeBudget))
			return false;
		if (normalHours == null) {
			if (other.normalHours != null)
				return false;
		} else if (!normalHours.equals(other.normalHours))
			return false;
		if (overtimeHours == null) {
			if (other.overtimeHours != null)
				return false;
		} else if (!overtimeHours.equals(other.overtimeHours))
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
		if (totalHours == null) {
			if (other.totalHours != null)
				return false;
		} else if (!totalHours.equals(other.totalHours))
			return false;
		return true;
	}

	public ActivitiesAndHr clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (ActivitiesAndHr) clone;
	}

}
