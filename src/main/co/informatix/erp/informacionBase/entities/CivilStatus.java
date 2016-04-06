package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of Civil Status
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "civil_status", schema = "general")
public class CivilStatus implements Serializable {
	private int id;
	private String name;
	private String description;

	/**
	 * @return id: Civil status identifier
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Civil status identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: Name civil status
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name civil status
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description of civil status
	 */
	@Column(name = "description", length = 200)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of civil status
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		CivilStatus other = (CivilStatus) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
