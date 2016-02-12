package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of the types of human resources
 * 
 * @author Dario.Lopez
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "hr_types", schema = "human_resources")
public class HrTypes implements Serializable {

	private int idHrType;
	private String name;
	private String description;

	/**
	 * @return idHrType: Identifier type of human resources
	 */
	@Id
	@Column(name = "idhrtype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdHrType() {
		return idHrType;
	}

	/**
	 * @param idHrType
	 *            : Identifier type of human resources
	 */
	public void setIdHrType(int idHrType) {
		this.idHrType = idHrType;
	}

	/**
	 * @return name: Name the type of human resources
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name the type of human resources
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description of the type of human resources
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of the type of human resources
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
		result = prime * result + idHrType;
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
		HrTypes other = (HrTypes) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idHrType != other.idHrType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
