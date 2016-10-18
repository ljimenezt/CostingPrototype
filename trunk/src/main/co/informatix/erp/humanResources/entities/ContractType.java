package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of Contract
 * 
 * @author Mabell.Boada
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "contract_type", schema = "human_resources")
public class ContractType implements Serializable {
	private int id;
	private String name;
	private String description;

	/**
	 * @return id: Contract type identifier
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Contract type identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: Name the type of contract
	 */
	@Column(name = "nombre", length = 50, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name the type of contract
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description of the type of contract
	 */
	@Column(name = "descripcion", length = 255)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of the type of contract
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
		ContractType other = (ContractType) obj;
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