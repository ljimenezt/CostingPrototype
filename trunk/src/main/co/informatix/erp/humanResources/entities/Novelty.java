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
 * This class maps the novelty table, which contains the information of the
 * novelty.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "novelty", schema = "human_resources")
public class Novelty implements Serializable {

	private int id;
	private Date initialDateTime;
	private Date finalDateTime;
	private String observations;
	private NoveltyType noveltyType;
	private Hr hr;

	/**
	 * @return id: Novelty identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Novelty identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return initialDateTime: Novelty start date.
	 */
	@Column(name = "initial_date_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDateTime() {
		return initialDateTime;
	}

	/**
	 * @param initialDateTime
	 *            : Novelty start date.
	 */
	public void setInitialDateTime(Date initialDateTime) {
		this.initialDateTime = initialDateTime;
	}

	/**
	 * @return finalDateTime: Novelty end date.
	 */
	@Column(name = "final_date_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDateTime() {
		return finalDateTime;
	}

	/**
	 * @param finalDateTime
	 *            : Novelty end date.
	 */
	public void setFinalDateTime(Date finalDateTime) {
		this.finalDateTime = finalDateTime;
	}

	/**
	 * @return observations: Description of the novelty.
	 */
	@Column(name = "observations")
	public String getObservations() {
		return observations;
	}

	/**
	 * @param observations
	 *            : Description of the novelty.
	 */
	public void setObservations(String observations) {
		this.observations = observations;
	}

	/**
	 * @return noveltyType: NoveltyType to which the novelty belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idnoveltytype", referencedColumnName = "id", nullable = false)
	public NoveltyType getNoveltyType() {
		return noveltyType;
	}

	/**
	 * @param noveltyType
	 *            : NoveltyType to which the novelty belong.
	 */
	public void setNoveltyType(NoveltyType noveltyType) {
		this.noveltyType = noveltyType;
	}

	/**
	 * @return hr: Hr to which the novelty belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idhr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Hr to which the novelty belong.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((finalDateTime == null) ? 0 : finalDateTime.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((initialDateTime == null) ? 0 : initialDateTime.hashCode());
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
		Novelty other = (Novelty) obj;
		if (finalDateTime == null) {
			if (other.finalDateTime != null)
				return false;
		} else if (!finalDateTime.equals(other.finalDateTime))
			return false;
		if (id != other.id)
			return false;
		if (initialDateTime == null) {
			if (other.initialDateTime != null)
				return false;
		} else if (!initialDateTime.equals(other.initialDateTime))
			return false;
		if (observations == null) {
			if (other.observations != null)
				return false;
		} else if (!observations.equals(other.observations))
			return false;
		return true;
	}
}
