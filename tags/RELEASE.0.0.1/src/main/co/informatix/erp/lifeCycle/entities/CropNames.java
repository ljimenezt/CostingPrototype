package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the table crop_names
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "crop_names", schema = "life_cycle")
public class CropNames implements Serializable {
	private int idCropName;
	private String cropName;
	private String description;

	/**
	 * @return idCropName: Id crop name
	 */
	@Id
	@Column(name = "idcropname", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdCropName() {
		return idCropName;
	}

	/**
	 * @param idCropName
	 *            : Id crop name
	 */
	public void setIdCropName(int idCropName) {
		this.idCropName = idCropName;
	}

	/**
	 * @return cropName: Crop name
	 */
	@Column(name = "crop_name", length = 100, nullable = false)
	public String getCropName() {
		return cropName;
	}

	/**
	 * @param cropName
	 *            : Crop name
	 */
	public void setCropName(String cropName) {
		this.cropName = cropName;
	}

	/**
	 * @return description: Description that has the name of the crop.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Description that has the name of the crop.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cropName == null) ? 0 : cropName.hashCode());
		result = prime * result + idCropName;
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
		CropNames other = (CropNames) obj;
		if (cropName == null) {
			if (other.cropName != null)
				return false;
		} else if (!cropName.equals(other.cropName))
			return false;
		if (idCropName != other.idCropName)
			return false;
		return true;
	}

}
