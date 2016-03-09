package co.informatix.erp.services.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the table service_type database.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "service_type", schema = "services")
public class ServiceType implements Serializable {

	private int idServiceType;
	private String description;

	/**
	 * @return idServiceType: Table identifier service_type
	 */
	@Id
	@Column(name = "idservicetype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdServiceType() {
		return idServiceType;
	}

	/**
	 * @param idServiceType
	 *            : Table identifier service_type
	 */
	public void setIdServiceType(int idServiceType) {
		this.idServiceType = idServiceType;
	}
	
	/**
	 * @return description: service_types description of the table
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : service_types description of the table
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
		result = prime * result + idServiceType;
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
		ServiceType other = (ServiceType) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idServiceType != other.idServiceType)
			return false;
		return true;
	}

	

}
