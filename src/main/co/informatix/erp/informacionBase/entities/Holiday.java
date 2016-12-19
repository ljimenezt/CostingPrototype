package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class maps the holiday table, which contains the information of the
 * holiday.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "holiday", schema = "general")
public class Holiday implements Serializable {

	private int id;
	private Date date;
	private String description;

	/**
	 * @return id: Holiday identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :Holiday identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return date: Holiday date.
	 */
	@Column(name = "date", nullable = false, unique = true)
	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            : Holiday date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return description: Holiday description.
	 */
	@Column(name = "description", nullable = false, length = 300)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Holiday description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
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
		Holiday other = (Holiday) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
