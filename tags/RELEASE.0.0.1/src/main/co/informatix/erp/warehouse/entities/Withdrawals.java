package co.informatix.erp.warehouse.entities;

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
import co.informatix.erp.humanResources.entities.Hr;

/**
 * This is the entity that is responsible for mapping the withdrawals table.
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "withdrawals", schema = "warehouse")
public class Withdrawals implements Serializable {
	private int idWithdrawal;
	private Date dateTime;
	private Date firstAllowedHarvestDate;
	private Date firstAllowedPeopleWorkingDate;
	private Date reentryDateAndTime;
	private Double quantityBudget;
	private Double quantityActual;
	private Boolean chargedOnMachine;
	private String justificationForApplication;
	private Materials materials;
	private Diseases diseases;
	private Hr hr;
	private Activities activities;
	private Hr hrRecommended;
	private Deposits deposits;

	/**
	 * @return idwithdrawal: Withdrawals identifier
	 */
	@Id
	@Column(name = "idwithdrawal", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdWithdrawal() {
		return idWithdrawal;
	}

	/**
	 * @param idwithdrawal
	 *            :Withdrawals identifier
	 */
	public void setIdWithdrawal(int idwithdrawal) {
		this.idWithdrawal = idwithdrawal;
	}

	/**
	 * @return dateTime: Date Time
	 */
	@Column(name = "date_time")
	@Temporal(TemporalType.DATE)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            : Date Time
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return materials: Materials
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_material", referencedColumnName = "idmaterial", nullable = false)
	public Materials getMaterials() {
		return materials;
	}

	/**
	 * @param materials
	 *            :Materials
	 */
	public void setMaterials(Materials materials) {
		this.materials = materials;
	}

	/**
	 * @return quantityBudget:Quantity Budget
	 */
	@Column(name = "quantity_budget")
	public Double getQuantityBudget() {
		return quantityBudget;
	}

	/**
	 * @param quantityBudget
	 *            :Quantity Budget
	 */
	public void setQuantityBudget(Double quantityBudget) {
		this.quantityBudget = quantityBudget;
	}

	/**
	 * @return quantityActual: Quantity Actual
	 */
	@Column(name = "quantity_actual")
	public Double getQuantityActual() {
		return quantityActual;
	}

	/**
	 * @param quantityActual
	 *            : Quantity Actual
	 */
	public void setQuantityActual(Double quantityActual) {
		this.quantityActual = quantityActual;
	}

	/**
	 * @return chargedOnMachine:Charged On Machine
	 */
	@Column(name = "charged_on_machine")
	public Boolean getChargedOnMachine() {
		return chargedOnMachine;
	}

	/**
	 * @param chargedOnMachine
	 *            :Charged On Machine
	 */
	public void setChargedOnMachine(Boolean chargedOnMachine) {
		this.chargedOnMachine = chargedOnMachine;
	}

	/**
	 * @return diseases:Diseases
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_disease", referencedColumnName = "iddisease", nullable = false)
	public Diseases getDiseases() {
		return diseases;
	}

	/**
	 * @param diseases
	 *            :Diseases
	 */
	public void setDiseases(Diseases diseases) {
		this.diseases = diseases;
	}

	/**
	 * @return hr: Human resources
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hr", referencedColumnName = "idhr")
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            :Human resources
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return activities: Activities
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_activity", referencedColumnName = "idactivity")
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            :Activities
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return hrRecommended: Human Resources Recommended
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hr_recommended", referencedColumnName = "idhr")
	public Hr getHrRecommended() {
		return hrRecommended;
	}

	/**
	 * @param hrRecommended
	 *            :Human Resources Recommended
	 */
	public void setHrRecommended(Hr hrRecommended) {
		this.hrRecommended = hrRecommended;
	}

	/**
	 * @return justificationForApplication: Justification For Application
	 */
	@Column(name = "justification_for_application")
	public String getJustificationForApplication() {
		return justificationForApplication;
	}

	/**
	 * @param justificationForApplication
	 *            :Justification For Application
	 */
	public void setJustificationForApplication(
			String justificationForApplication) {
		this.justificationForApplication = justificationForApplication;
	}

	/**
	 * @return firstAllowedHarvestDate:First Allowed Harvest Date
	 */
	@Column(name = "first_allowed_harvest_date")
	@Temporal(TemporalType.DATE)
	public Date getFirstAllowedHarvestDate() {
		return firstAllowedHarvestDate;
	}

	/**
	 * @param firstAllowedHarvestDate
	 *            :First Allowed Harvest Date
	 */
	public void setFirstAllowedHarvestDate(Date firstAllowedHarvestDate) {
		this.firstAllowedHarvestDate = firstAllowedHarvestDate;
	}

	/**
	 * @return firstAllowedPeopleWorkingDate: First Allowed People Working Date
	 */
	@Column(name = "first_allowed_people_working_date")
	@Temporal(TemporalType.DATE)
	public Date getFirstAllowedPeopleWorkingDate() {
		return firstAllowedPeopleWorkingDate;
	}

	/**
	 * @param firstAllowedPeopleWorkingDate
	 *            :First Allowed People Working Date
	 */
	public void setFirstAllowedPeopleWorkingDate(
			Date firstAllowedPeopleWorkingDate) {
		this.firstAllowedPeopleWorkingDate = firstAllowedPeopleWorkingDate;
	}

	/**
	 * @return reentryDateAndTime: Reentry Date And Time
	 */
	@Column(name = "reentry_date_and_time")
	@Temporal(TemporalType.DATE)
	public Date getReentryDateAndTime() {
		return reentryDateAndTime;
	}

	/**
	 * @param reentryDateAndTime
	 *            :Reentry Date And Time
	 */
	public void setReentryDateAndTime(Date reentryDateAndTime) {
		this.reentryDateAndTime = reentryDateAndTime;
	}

	/**
	 * @return deposits: gets deposits object related a withdrawals
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_deposit", referencedColumnName = "iddeposit")
	public Deposits getDeposits() {
		return deposits;
	}

	/**
	 * @param deposits
	 *            :sets deposits object related a withdrawals
	 */
	public void setDeposits(Deposits deposits) {
		this.deposits = deposits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime
				* result
				+ ((chargedOnMachine == null) ? 0 : chargedOnMachine.hashCode());
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result
				+ ((deposits == null) ? 0 : deposits.hashCode());
		result = prime * result
				+ ((diseases == null) ? 0 : diseases.hashCode());
		result = prime
				* result
				+ ((firstAllowedHarvestDate == null) ? 0
						: firstAllowedHarvestDate.hashCode());
		result = prime
				* result
				+ ((firstAllowedPeopleWorkingDate == null) ? 0
						: firstAllowedPeopleWorkingDate.hashCode());
		result = prime * result + ((hr == null) ? 0 : hr.hashCode());
		result = prime * result
				+ ((hrRecommended == null) ? 0 : hrRecommended.hashCode());
		result = prime * result + idWithdrawal;
		result = prime
				* result
				+ ((justificationForApplication == null) ? 0
						: justificationForApplication.hashCode());
		result = prime * result
				+ ((materials == null) ? 0 : materials.hashCode());
		result = prime * result
				+ ((quantityActual == null) ? 0 : quantityActual.hashCode());
		result = prime * result
				+ ((quantityBudget == null) ? 0 : quantityBudget.hashCode());
		result = prime
				* result
				+ ((reentryDateAndTime == null) ? 0 : reentryDateAndTime
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
		Withdrawals other = (Withdrawals) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (chargedOnMachine == null) {
			if (other.chargedOnMachine != null)
				return false;
		} else if (!chargedOnMachine.equals(other.chargedOnMachine))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (deposits == null) {
			if (other.deposits != null)
				return false;
		} else if (!deposits.equals(other.deposits))
			return false;
		if (diseases == null) {
			if (other.diseases != null)
				return false;
		} else if (!diseases.equals(other.diseases))
			return false;
		if (firstAllowedHarvestDate == null) {
			if (other.firstAllowedHarvestDate != null)
				return false;
		} else if (!firstAllowedHarvestDate
				.equals(other.firstAllowedHarvestDate))
			return false;
		if (firstAllowedPeopleWorkingDate == null) {
			if (other.firstAllowedPeopleWorkingDate != null)
				return false;
		} else if (!firstAllowedPeopleWorkingDate
				.equals(other.firstAllowedPeopleWorkingDate))
			return false;
		if (hr == null) {
			if (other.hr != null)
				return false;
		} else if (!hr.equals(other.hr))
			return false;
		if (hrRecommended == null) {
			if (other.hrRecommended != null)
				return false;
		} else if (!hrRecommended.equals(other.hrRecommended))
			return false;
		if (idWithdrawal != other.idWithdrawal)
			return false;
		if (justificationForApplication == null) {
			if (other.justificationForApplication != null)
				return false;
		} else if (!justificationForApplication
				.equals(other.justificationForApplication))
			return false;
		if (materials == null) {
			if (other.materials != null)
				return false;
		} else if (!materials.equals(other.materials))
			return false;
		if (quantityActual == null) {
			if (other.quantityActual != null)
				return false;
		} else if (!quantityActual.equals(other.quantityActual))
			return false;
		if (quantityBudget == null) {
			if (other.quantityBudget != null)
				return false;
		} else if (!quantityBudget.equals(other.quantityBudget))
			return false;
		if (reentryDateAndTime == null) {
			if (other.reentryDateAndTime != null)
				return false;
		} else if (!reentryDateAndTime.equals(other.reentryDateAndTime))
			return false;
		return true;
	}

}
