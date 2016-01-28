package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the section table, which contains section information.
 * 
 * @author Mabell.Boada
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "section", schema = "life_cycle")
public class Section implements Serializable {
	private int idSection;
	private String name;
	private String description;

	/**
	 * @return idSection: Identifier of Section.
	 */
	@Id
	@Column(name = "idsection", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdSection() {
		return idSection;
	}

	/**
	 * @param idSection
	 *            : Identifier of Section.
	 */
	public void setIdSection(int idSection) {
		this.idSection = idSection;
	}

	/**
	 * @return name: Name of Section.
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name of Section.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description of Section.
	 */
	@Column(name = "description", length = 250)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of Section.
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
		result = prime * result + idSection;
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
		Section other = (Section) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idSection != other.idSection)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}
}
