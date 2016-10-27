package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class map the color table, which contains the information of the color.
 * 
 * @author Claudia.Rey
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "color", schema = "general")
public class Color implements Serializable {
	private int id;
	private String code;
	private String name;
	private String description;

	/**
	 * @return id: color identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : color identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return code: color code.
	 */
	@Column(name = "code", length = 7, nullable = false, unique = false)
	public String getCode() {
		return code;
	}

	/**
	 * @param code
	 *            : color code.
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * @return name: color name.
	 */
	@Column(name = "name", length = 50, nullable = false, unique = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : color name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: color description.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : color description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((code == null) ? 0 : code.hashCode());
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
		Color other = (Color) obj;
		if (code == null) {
			if (other.code != null)
				return false;
		} else if (!code.equals(other.code))
			return false;
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
