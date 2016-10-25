package co.informatix.erp.organizations.entities;

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

import co.informatix.erp.informacionBase.entities.TipoDocumento;

/**
 * This class maps the organization table
 * 
 * @author Oscar.Amaya
 * @modify Luis.Ruiz
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "organizacion", schema = "organizaciones")
public class Organization implements Serializable, Comparable<Organization> {

	private int id;
	private String nit;
	private String businessName;
	private String address;
	private String telephono;
	private String logo;
	private Date creationDate;
	private Date dateEndValidity;
	private String userName;

	private TipoDocumento documentType;

	/**
	 * @return id: ID (primary key) of the table
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :ID (primary key) of the table
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return nit: NIT of the organization
	 */
	@Column(name = "nit", length = 100, nullable = false)
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit
	 *            : NIT of the organization
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return businessName: Business name of the organization
	 */
	@Column(name = "razon_social", length = 150, nullable = false)
	public String getBusinessName() {
		return businessName;
	}

	/**
	 * @param businessName
	 *            : Business name of the organization
	 */
	public void setBusinessName(String businessName) {
		this.businessName = businessName;
	}

	/**
	 * @return address: direction of the organization
	 */
	@Column(name = "direccion", length = 200, nullable = true)
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            : direction of the organizations
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return telephono: Organization Phone
	 */
	@Column(name = "telefono", length = 100, nullable = true)
	public String getTelephono() {
		return telephono;
	}

	/**
	 * @param telephono
	 *            : Organization Phone
	 */
	public void setTelephono(String telephono) {
		this.telephono = telephono;
	}

	/**
	 * @return logo: Image name of the logo of the organization.
	 */
	@Column(name = "logo", length = 200)
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            : Image name of the logo of the organization.
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return creationDate: creation date of record
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            : creation date of record
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return dateEndValidity: date of currency of the registration order
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getDateEndValidity() {
		return dateEndValidity;
	}

	/**
	 * @param fechaFinVigencia
	 *            : date of currency of the registration order
	 */
	public void setDateEndValidity(Date dateEndValidity) {
		this.dateEndValidity = dateEndValidity;
	}

	/**
	 * @return userName: User name session running in action on recording
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name session running in action on recording
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return documentType: gets the type of document of the organization.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
	public TipoDocumento getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType
	 *            : sets the type of document of the organization.
	 */
	public void setDocumentType(TipoDocumento documentType) {
		this.documentType = documentType;
	}

	public int compareTo(Organization organizacion) {
		return businessName.compareTo(organizacion.getBusinessName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime
				* result
				+ ((dateEndValidity == null) ? 0 : dateEndValidity.hashCode());
		result = prime * result + id;
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result
				+ ((businessName == null) ? 0 : businessName.hashCode());
		result = prime * result
				+ ((telephono == null) ? 0 : telephono.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		Organization other = (Organization) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (creationDate == null) {
			if (other.creationDate != null)
				return false;
		} else if (!creationDate.equals(other.creationDate))
			return false;
		if (dateEndValidity == null) {
			if (other.dateEndValidity != null)
				return false;
		} else if (!dateEndValidity.equals(other.dateEndValidity))
			return false;
		if (id != other.id)
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (businessName == null) {
			if (other.businessName != null)
				return false;
		} else if (!businessName.equals(other.businessName))
			return false;
		if (telephono == null) {
			if (other.telephono != null)
				return false;
		} else if (!telephono.equals(other.telephono))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
