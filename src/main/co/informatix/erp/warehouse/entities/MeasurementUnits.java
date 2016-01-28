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
	private String descripcion;

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
	 * @return descripcion: description MeasurementUnits
	 */
	@Column(name = "description")
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : description MeasurementUnits
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
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
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
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
