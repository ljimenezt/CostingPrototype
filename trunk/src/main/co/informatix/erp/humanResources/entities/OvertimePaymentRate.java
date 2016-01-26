package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the overtime_payment_rate table in the database.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "overtime_payment_rate", schema = "human_resources")
public class OvertimePaymentRate implements Serializable {

	private int overtimepaymentid;
	private String overtimeRateType;
	private Double overtimeRateRatio;
	private String description;
	private Boolean byDefault;

	/**
	 * @return overtimepaymentid: overtime payment rate identifier
	 */
	@Id
	@Column(name = "overtimepaymentid", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getOvertimepaymentid() {
		return overtimepaymentid;
	}

	/**
	 * @param overtimepaymentid
	 *            :overtime payment rate identifier
	 */
	public void setOvertimepaymentid(int overtimepaymentid) {
		this.overtimepaymentid = overtimepaymentid;
	}

	/**
	 * @return overtimeRateType: overtime rate type
	 */
	@Column(name = "overtime_rate_type", length = 100, nullable = false)
	public String getOvertimeRateType() {
		return overtimeRateType;
	}

	/**
	 * @param overtimeRateType
	 *            :overtime rate type
	 */
	public void setOvertimeRateType(String overtimeRateType) {
		this.overtimeRateType = overtimeRateType;
	}

	/**
	 * @return overtimeRateRatio: value overtime rate ratio
	 */
	@Column(name = "overtime_rate_ratio")
	public Double getOvertimeRateRatio() {
		return overtimeRateRatio;
	}

	/**
	 * @param overtimeRateRatio
	 *            : value overtime rate ratio
	 */
	public void setOvertimeRateRatio(Double overtimeRateRatio) {
		this.overtimeRateRatio = overtimeRateRatio;
	}

	/**
	 * @return description: description overtime payment rate
	 */
	@Column(name = "description", length = 250)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :description overtime payment rate
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return byDefault: field to know if the overtime payment rate is default
	 */
	@Column(name = "default_rate")
	public Boolean getByDefault() {
		return byDefault;
	}

	/**
	 * @param byDefault
	 *            :field to know if the overtime payment rate is default
	 */
	public void setByDefault(Boolean byDefault) {
		this.byDefault = byDefault;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((byDefault == null) ? 0 : byDefault.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime
				* result
				+ ((overtimeRateRatio == null) ? 0 : overtimeRateRatio
						.hashCode());
		result = prime
				* result
				+ ((overtimeRateType == null) ? 0 : overtimeRateType.hashCode());
		result = prime * result + overtimepaymentid;
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
		OvertimePaymentRate other = (OvertimePaymentRate) obj;
		if (byDefault == null) {
			if (other.byDefault != null)
				return false;
		} else if (!byDefault.equals(other.byDefault))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (overtimeRateRatio == null) {
			if (other.overtimeRateRatio != null)
				return false;
		} else if (!overtimeRateRatio.equals(other.overtimeRateRatio))
			return false;
		if (overtimeRateType == null) {
			if (other.overtimeRateType != null)
				return false;
		} else if (!overtimeRateType.equals(other.overtimeRateType))
			return false;
		if (overtimepaymentid != other.overtimepaymentid)
			return false;
		return true;
	}

}
