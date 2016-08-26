package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class containing the registration table mapping crops
 * 
 * @author Johnatan.Naranjo
 * @modify Dario.Lopez 07-04-15
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "crops", schema = "life_cycle")
public class Crops implements Serializable {
	private int idCrop;
	private CropNames cropNames;
	private String description;
	private Date initialDate;
	private Date finalDate;
	private Date registrationYear;

	/**
	 * @return idCrop: Identification Crop
	 */
	@Id
	@Column(name = "idcrop", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdCrop() {
		return idCrop;
	}

	/**
	 * @param idCrop
	 *            : Identification Crop
	 */
	public void setIdCrop(int idCrop) {
		this.idCrop = idCrop;
	}

	/**
	 * @return cropNames: Get the name of the crop
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_crop_name", referencedColumnName = "idcropname", nullable = false)
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            :Set the name of the crop
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return description: Crop description
	 */
	@Column(name = "description", length = 200)
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Crop description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return initialDate: initial date of crop
	 */
	@Column(name = "initial_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getInitialDate() {
		return initialDate;
	}

	/**
	 * @param initialDate
	 *            :initial date of crop
	 */
	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	/**
	 * @return finalDate: end date of crop
	 */
	@Column(name = "final_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFinalDate() {
		return finalDate;
	}

	/**
	 * @param finalDate
	 *            : end date of crop
	 */
	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	/**
	 * @return registrationYear: Registration year crop
	 */
	@Column(name = "registration_year")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getRegistrationYear() {
		return registrationYear;
	}

	/**
	 * @param registrationYear
	 *            :Registration year crop
	 */
	public void setRegistrationYear(Date registrationYear) {
		this.registrationYear = registrationYear;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result
				+ ((finalDate == null) ? 0 : finalDate.hashCode());
		result = prime * result + idCrop;
		result = prime * result
				+ ((initialDate == null) ? 0 : initialDate.hashCode());
		result = prime
				* result
				+ ((registrationYear == null) ? 0 : registrationYear.hashCode());
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
		Crops other = (Crops) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (finalDate == null) {
			if (other.finalDate != null)
				return false;
		} else if (!finalDate.equals(other.finalDate))
			return false;
		if (idCrop != other.idCrop)
			return false;
		if (initialDate == null) {
			if (other.initialDate != null)
				return false;
		} else if (!initialDate.equals(other.initialDate))
			return false;
		if (registrationYear == null) {
			if (other.registrationYear != null)
				return false;
		} else if (!registrationYear.equals(other.registrationYear))
			return false;
		return true;
	}

}
