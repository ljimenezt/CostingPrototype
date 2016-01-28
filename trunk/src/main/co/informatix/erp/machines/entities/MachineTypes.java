package co.informatix.erp.machines.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of the types of machine
 * 
 * @author Dario.Lopez
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "machine_types", schema = "machines")
public class MachineTypes implements Serializable {

	private int idMachineType;
	private String name;
	private String description;

	/**
	 * @return idMachineType: Machine type identifier
	 */
	@Id
	@Column(name = "idmachinetype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdMachineType() {
		return idMachineType;
	}

	/**
	 * @param idMachineType
	 *            : Machine type identifier
	 */
	public void setIdMachineType(int idMachineType) {
		this.idMachineType = idMachineType;
	}

	/**
	 * @return name: Name type of machine.
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name type of machine.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description which is a type of machine.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Description which is a type of machine.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idMachineType;
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
		MachineTypes other = (MachineTypes) obj;
		if (idMachineType != other.idMachineType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
