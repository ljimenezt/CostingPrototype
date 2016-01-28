package co.informatix.erp.humanResources.entities;

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

import co.informatix.erp.recursosHumanos.entities.Contrato;

/**
 * Class containing the record of payments (payments) and the relationship with
 * the contracts and hr.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "payments", schema = "human_resources")
public class Payments implements Serializable {

	private int idPayment;

	private boolean taskWork;

	private Date emissionDate;
	private Date initiaWorkDay;
	private Date finalWorkDay;

	private Double totalAmount;
	private Double netAmount;
	private Double grossAmount;

	private Contrato contrato;
	private Hr hr;

	/**
	 * @return idPayment: Payment identifier
	 */
	@Id
	@Column(name = "idpayment", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdPayment() {
		return idPayment;
	}

	/**
	 * @param idPayment
	 *            : Payment identifier
	 */
	public void setIdPayment(int idPayment) {
		this.idPayment = idPayment;
	}

	/**
	 * @return taskWork: value that determines True if whether the job is
	 *         assigned as homework.
	 */
	@Column(name = "task_work")
	public boolean isTaskWork() {
		return taskWork;
	}

	/**
	 * @param taskWork
	 *            : value that determines True if whether the job is assigned as
	 *            homework.
	 */
	public void setTaskWork(boolean taskWork) {
		this.taskWork = taskWork;
	}

	/**
	 * @return emissionDate: Issue date of payment.
	 */
	@Column(name = "emission_date")
	@Temporal(TemporalType.DATE)
	public Date getEmissionDate() {
		return emissionDate;
	}

	/**
	 * @param emissionDate
	 *            : Issue date of payment.
	 */
	public void setEmissionDate(Date emissionDate) {
		this.emissionDate = emissionDate;
	}

	/**
	 * @return initiaWorkDay: Start Date of work.
	 */
	@Column(name = "initial_work_day")
	@Temporal(TemporalType.DATE)
	public Date getInitiaWorkDay() {
		return initiaWorkDay;
	}

	/**
	 * @param initiaWorkDay
	 *            : Start Date of work.
	 */
	public void setInitiaWorkDay(Date initiaWorkDay) {
		this.initiaWorkDay = initiaWorkDay;
	}

	/**
	 * @return finalWorkDay: Day after work.
	 */
	@Column(name = "final_work_day")
	@Temporal(TemporalType.DATE)
	public Date getFinalWorkDay() {
		return finalWorkDay;
	}

	/**
	 * @param finalWorkDay
	 *            : Day after work.
	 */
	public void setFinalWorkDay(Date finalWorkDay) {
		this.finalWorkDay = finalWorkDay;
	}

	/**
	 * @return totalAmount: Total value of the payment.
	 */
	@Column(name = "total_amount")
	public Double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount
	 *            : Total value of the payment.
	 */
	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
	}

	/**
	 * @return netAmount: Net amount of the value of the payment.
	 */
	@Column(name = "net_amount")
	public Double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            : Net amount of the value of the payment.
	 */
	public void setNetAmount(Double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return grossAmount: Gross amount of the value of the payment.
	 */
	@Column(name = "gross_amount")
	public Double getGrossAmount() {
		return grossAmount;
	}

	/**
	 * @param grossAmount
	 *            : Gross amount of the value of the payment.
	 */
	public void setGrossAmount(Double grossAmount) {
		this.grossAmount = grossAmount;
	}

	/**
	 * @return contrato: Contract related to the payment order.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcontract", referencedColumnName = "id", nullable = false)
	public Contrato getContrato() {
		return contrato;
	}

	/**
	 * @param contrato
	 *            : Contract related to the payment order.
	 */
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return hr: Hr object related to payment.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idhr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Hr object related to payment.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contrato == null) ? 0 : contrato.hashCode());
		result = prime * result
				+ ((emissionDate == null) ? 0 : emissionDate.hashCode());
		result = prime * result
				+ ((finalWorkDay == null) ? 0 : finalWorkDay.hashCode());
		result = prime * result
				+ ((grossAmount == null) ? 0 : grossAmount.hashCode());
		result = prime * result + ((hr == null) ? 0 : hr.hashCode());
		result = prime * result + idPayment;
		result = prime * result
				+ ((initiaWorkDay == null) ? 0 : initiaWorkDay.hashCode());
		result = prime * result
				+ ((netAmount == null) ? 0 : netAmount.hashCode());
		result = prime * result + (taskWork ? 1231 : 1237);
		result = prime * result
				+ ((totalAmount == null) ? 0 : totalAmount.hashCode());
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
		Payments other = (Payments) obj;
		if (contrato == null) {
			if (other.contrato != null)
				return false;
		} else if (!contrato.equals(other.contrato))
			return false;
		if (emissionDate == null) {
			if (other.emissionDate != null)
				return false;
		} else if (!emissionDate.equals(other.emissionDate))
			return false;
		if (finalWorkDay == null) {
			if (other.finalWorkDay != null)
				return false;
		} else if (!finalWorkDay.equals(other.finalWorkDay))
			return false;
		if (grossAmount == null) {
			if (other.grossAmount != null)
				return false;
		} else if (!grossAmount.equals(other.grossAmount))
			return false;
		if (hr == null) {
			if (other.hr != null)
				return false;
		} else if (!hr.equals(other.hr))
			return false;
		if (idPayment != other.idPayment)
			return false;
		if (initiaWorkDay == null) {
			if (other.initiaWorkDay != null)
				return false;
		} else if (!initiaWorkDay.equals(other.initiaWorkDay))
			return false;
		if (netAmount == null) {
			if (other.netAmount != null)
				return false;
		} else if (!netAmount.equals(other.netAmount))
			return false;
		if (taskWork != other.taskWork)
			return false;
		if (totalAmount == null) {
			if (other.totalAmount != null)
				return false;
		} else if (!totalAmount.equals(other.totalAmount))
			return false;
		return true;
	}

}
