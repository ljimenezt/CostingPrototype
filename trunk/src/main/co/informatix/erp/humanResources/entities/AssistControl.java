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
import javax.persistence.Transient;

/**
 * This class maps the assist_control table, which contains the information of
 * the assist control.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "assist_control", schema = "human_resources")
public class AssistControl implements Serializable, Cloneable {

	private int id;
	private Date date;
	private boolean absent;
	private String observations;
	private Hr hr;
	private Novelty novelty;

	/**
	 * @return id: Assist control identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Assist control identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return date: : Date of attendance.
	 */
	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            : Date of attendance.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return absent: This field is true if the worker is absent.
	 */
	@Column(name = "absent", nullable = false)
	public boolean isAbsent() {
		return absent;
	}

	/**
	 * @param absent
	 *            : This field is true if the worker is absent.
	 */
	public void setAbsent(boolean absent) {
		this.absent = absent;
	}

	/**
	 * @return observations: : This field stored observations about the
	 *         attendance.
	 */
	@Column(name = "observations")
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations
	 *            : This field stored observations about the attendance.
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return hr: Worker who checks her attendance.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idhr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Worker who checks her attendance.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return novelty: if the worker is absent this field have the novelty of
	 *         the absent.
	 */
	@Transient
	public Novelty getNovelty() {
		return novelty;
	}

	/**
	 * @param novelty
	 *            :if the worker is absent this field have the novelty of the
	 *            absent.
	 */
	public void setNovelty(Novelty novelty) {
		this.novelty = novelty;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (absent ? 1231 : 1237);
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((observations == null) ? 0 : observations.hashCode());
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
		AssistControl other = (AssistControl) obj;
		if (absent != other.absent)
			return false;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (observations == null) {
			if (other.observations != null)
				return false;
		} else if (!observations.equals(other.observations))
			return false;
		return true;
	}

	public AssistControl clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (AssistControl) clone;
	}

}