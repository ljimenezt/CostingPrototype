package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This entity represents the classification with the units of measure.
 * 
 * @author Angelica Amaya
 * @modify 13/12/2011 marisol.calderon
 * @modify 29/03/2016 Jhair.Leal
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "type_unit", schema = "general")
public class TypeUnit implements Serializable {

	private int id;
	private String name;
	private String description;

	/**
	 * @return id: Identifier type unit of measurement.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Identifier type unit of measurement.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: name the type of unit of measure.
	 */
	@Column(name = "name", length = 50, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : name the type of unit of measure.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: variable that stores the description or additional
	 *         information on the type of unit.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : variable that stores the description or additional
	 *            information on the type of unit.
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
		TypeUnit other = (TypeUnit) obj;
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
