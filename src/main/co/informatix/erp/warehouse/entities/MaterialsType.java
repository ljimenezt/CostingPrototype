package co.informatix.erp.warehouse.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing records Material Type
 * 
 * @author Sergio.Ortiz
 * 
 */
@Entity
@Table(name = "materials_types", schema = "warehouse")
@SuppressWarnings("serial")
public class MaterialsType implements Serializable {
	private int idMaterialsType;

	private String name;
	private String description;

	/**
	 * @return idMaterialType: Identifier Material Type
	 */
	@Id
	@Column(name = "idmaterialtype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdMaterialsType() {
		return idMaterialsType;
	}

	/**
	 * @param idMaterialType
	 *            : Identifier Material Type
	 */
	public void setIdMaterialsType(int idMaterialsType) {
		this.idMaterialsType = idMaterialsType;
	}

	/**
	 * @return name: name MaterialsType
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : name MaterialsType
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: description MaterialsType
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : description MaterialsType
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
		result = prime * result + idMaterialsType;
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
		MaterialsType other = (MaterialsType) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idMaterialsType != other.idMaterialsType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
