package co.informatix.erp.machines.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the fuel_types table, which contains the information of fuel
 * types.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "fuel_types", schema = "machines")
public class FuelTypes implements Serializable {

	private int idFuelType;
	private String name;
	private String description;

	/**
	 * @return idFuelType: Fuel type identifier
	 */
	@Id
	@Column(name = "idfueltype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdFuelType() {
		return idFuelType;
	}

	/**
	 * @param idFuelType
	 *            : Fuel type identifier
	 */
	public void setIdFuelType(int idFuelType) {
		this.idFuelType = idFuelType;
	}

	/**
	 * @return name: Fuel type name
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :Fuel type name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Fuel type description
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Fuel type description
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
		result = prime * result + idFuelType;
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
		FuelTypes other = (FuelTypes) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idFuelType != other.idFuelType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
