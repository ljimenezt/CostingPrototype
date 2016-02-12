package co.informatix.erp.machines.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of the types of consumable
 * 
 * @author Dario.Lopez
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "consumable_types", schema = "machines")
public class ConsumableTypes implements Serializable {

	private int idConsumableType;
	private String name;
	private String description;

	/**
	 * @return idConsumableType: Consumable type identifier
	 */
	@Id
	@Column(name = "idconsumabletype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdConsumableType() {
		return idConsumableType;
	}

	/**
	 * @param idConsumableType
	 *            : Consumable type identifier
	 */
	public void setIdConsumableType(int idConsumableType) {
		this.idConsumableType = idConsumableType;
	}

	/**
	 * @return name: Consumable type name.
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Consumable type name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description having a consumable type.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Description having a consumable type.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idConsumableType;
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
		ConsumableTypes other = (ConsumableTypes) obj;
		if (idConsumableType != other.idConsumableType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
