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

/**
 * This class maps the contract table which contains a record of information
 * relating to the contracts that are handled in the payroll of the company.
 * 
 * @author Marcela.Chaparro
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "contract", schema = "human_resources")
public class Contract implements Serializable {

	private int id;
	private String observations;
	private Boolean isTestPeriod;
	private Date contractEndDate;
	private Date created;
	private String userName;

	private Hr hr;

	/**
	 * @return id: Contract identifier, primary key.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Contract identifier, primary key.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return observations: Additional observations of the contract.
	 */
	@Column(name = "observations", length = 300)
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations
	 *            : Additional observations of the contract.
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return isTestPeriod: Variable that identifies if the contract is on
	 *         probation.
	 */
	@Column(name = "is_test_period")
	public Boolean getIsTestPeriod() {
		return isTestPeriod;
	}

	/**
	 * @param isTestPeriod
	 *            : Variable that identifies if the contract is on probation.
	 */
	public void setIsTestPeriod(Boolean isTestPeriod) {
		this.isTestPeriod = isTestPeriod;
	}

	/**
	 * @return contractEndDate: End date of the trial period, if the contract is
	 *         of this type.
	 */
	@Column(name = "contract_end_date")
	@Temporal(TemporalType.DATE)
	public Date getContractEndDate() {
		return contractEndDate;
	}

	/**
	 * @param contractEndDate
	 *            : End date of the trial period, if the contract is of this
	 *            type.
	 */
	public void setContractEndDate(Date contractEndDate) {
		this.contractEndDate = contractEndDate;
	}

	/**
	 * @return created: Creation date of registration.
	 */
	@Column(name = "created", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getCreated() {
		return created;
	}

	/**
	 * @param created
	 *            : Creation date of registration.
	 */
	public void setCreated(Date created) {
		this.created = created;
	}

	/**
	 * @return userName: User name in session running the action on the record.
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name in session running the action on the record.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return hr: Reference human resource who has been associated with an
	 *         employment contract.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Reference human resource who has been associated with an
	 *            employment contract.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((contractEndDate == null) ? 0 : contractEndDate.hashCode());
		result = prime * result + ((created == null) ? 0 : created.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((isTestPeriod == null) ? 0 : isTestPeriod.hashCode());
		result = prime * result
				+ ((observations == null) ? 0 : observations.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		Contract other = (Contract) obj;
		if (contractEndDate == null) {
			if (other.contractEndDate != null)
				return false;
		} else if (!contractEndDate.equals(other.contractEndDate))
			return false;
		if (created == null) {
			if (other.created != null)
				return false;
		} else if (!created.equals(other.created))
			return false;
		if (id != other.id)
			return false;
		if (isTestPeriod == null) {
			if (other.isTestPeriod != null)
				return false;
		} else if (!isTestPeriod.equals(other.isTestPeriod))
			return false;
		if (observations == null) {
			if (other.observations != null)
				return false;
		} else if (!observations.equals(other.observations))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
