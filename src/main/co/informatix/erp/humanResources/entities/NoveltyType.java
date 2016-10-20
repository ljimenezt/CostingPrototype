package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the novelty_type table, which contains the information of the
 * novelty type.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "novelty_Type", schema = "human_resources")
public class NoveltyType implements Serializable {

	private int id;
	private String name;
	private String description;

	/**
	 * @return id: Novelty type identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Novelty type identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: Novelty type name.
	 */
	@Column(name = "name", length = 250, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Novelty type name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Novelty type description.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Novelty type description.
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
		NoveltyType other = (NoveltyType) obj;
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
