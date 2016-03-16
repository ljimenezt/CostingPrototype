package co.informatix.erp.warehouse.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class that contains the records of Measurement Units
 * 
 * @author Sergio.Ortiz
 * 
 */
@Entity
@Table(name = "measurement_units", schema = "warehouse")
@SuppressWarnings("serial")
public class MeasurementUnits implements Serializable {
	private int idMeasurementUnits;

	private String name;
	private String description;

	/**
	 * @return idMeasurementUnits: Identifier Measurement Units
	 */
	@Id
	@Column(name = "idmeasurementunits", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdMeasurementUnits() {
		return idMeasurementUnits;
	}

	/**
	 * @param idMeasurementUnits
	 *            : Identifier Measurement Units
	 */
	public void setIdMeasurementUnits(int idMeasurementUnits) {
		this.idMeasurementUnits = idMeasurementUnits;
	}

	/**
	 * @return name:name MeasurementUnits
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : name MeasurementUnits
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: description MeasurementUnits
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : description MeasurementUnits
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
		result = prime * result + idMeasurementUnits;
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
		MeasurementUnits other = (MeasurementUnits) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idMeasurementUnits != other.idMeasurementUnits)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
