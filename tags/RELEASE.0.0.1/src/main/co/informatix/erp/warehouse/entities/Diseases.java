package co.informatix.erp.warehouse.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of diseases
 * 
 * @author Mabell.Boada
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "diseases", schema = "warehouse")
public class Diseases implements Serializable {
	private int idDisease;
	private String name;
	private String description;

	/**
	 * @return idDisease: Identifier disease
	 */
	@Id
	@Column(name = "iddisease", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdDisease() {
		return idDisease;
	}

	/**
	 * @param idDisease
	 *            : Identifier disease
	 */
	public void setIdDisease(int idDisease) {
		this.idDisease = idDisease;
	}

	/**
	 * @return name: Disease Name
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Disease Name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description of the disease
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of the disease
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
		result = prime * result + idDisease;
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
		Diseases other = (Diseases) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idDisease != other.idDisease)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
