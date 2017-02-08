package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class maps the type food table, which contains the information of the
 * type food.
 * 
 * @author Wilhelm.Boada
 * @modify 07/02/2017 Claudia.Rey
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "type_food", schema = "general")
public class TypeFood implements Serializable {

	private int id;
	private String name;
	private String description;
	private boolean selected;
	private String abbreviation;

	/**
	 * @return id: Type food identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :Type food identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: Type food name.
	 */
	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :Type food name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return name: Type food name.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Type food description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return selected: Flag to see if it is selected typeFood, true is
	 *         selected and false is not selected.
	 */
	@Transient
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            : Flag to see if it is selected typeFood, true is selected and
	 *            false is not selected.
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return abbreviation: abbreviation of the name
	 */
	@Column(name = "abbreviation", length = 3, nullable = false, unique = true)
	public String getAbbreviation() {
		return abbreviation;
	}

	/**
	 * @param abbreviation
	 *            : abbreviation of the name
	 */
	public void setAbbreviation(String abbreviation) {
		this.abbreviation = abbreviation;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abbreviation == null) ? 0 : abbreviation.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (selected ? 1231 : 1237);
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
		TypeFood other = (TypeFood) obj;
		if (abbreviation == null) {
			if (other.abbreviation != null)
				return false;
		} else if (!abbreviation.equals(other.abbreviation))
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
		if (selected != other.selected)
			return false;
		return true;
	}

}