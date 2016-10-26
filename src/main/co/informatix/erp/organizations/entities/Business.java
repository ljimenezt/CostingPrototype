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

import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;

/**
 * This class represents the business information system.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "empresa", schema = "organizaciones")
public class Business implements Serializable {

	private int id;
	private String nit;
	private String name;
	private String logo;
	private String address;
	private String phone;
	private String email;
	private String fax;
	private String mobile;
	private Date dateCreation;
	private Date dateEndValidity;
	private String userName;

	private Organization organization;
	private Pais country;
	private Departamento department;
	private Municipio town;

	/**
	 * Empty constructor to initialize the necessary variables.
	 */
	public Business() {
		this.organization = new Organization();
		this.country = new Pais();
		this.department = new Departamento();
		this.town = new Municipio();
	}

	/**
	 * @return id: gets the identifier of the company.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : sets the identifier of the company.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return nit: tax identification number of a company.
	 */
	@Column(name = "nit", length = 25, nullable = false)
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit
	 *            : tax identification number of a company.
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return name: gets the name of the company.
	 */
	@Column(name = "nombre", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : sets the name of the company.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return logo: company logo
	 */
	@Column(name = "logo", length = 200, nullable = true)
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            : company logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return address: gets the address of the company.
	 */
	@Column(name = "direccion", length = 50, nullable = false)
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            : sets the address of the company.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return phone: gets the phone company.
	 */
	@Column(name = "telefono", length = 150, nullable = false)
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            : sets the phone company.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return email: gets mail of a company.
	 */
	@Column(name = "correo", length = 50, nullable = true)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            : sets mail of a company.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return fax: gets the fax number of a company
	 */
	@Column(name = "fax", length = 20)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            : gets the fax number of a company
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return mobile: mobile phone company.
	 */
	@Column(name = "movil", length = 20, nullable = true)
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            : mobile phone company.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return dateCreation: get creation date of a company.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param dateCreation
	 *            : sets the date for registration of a company.
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return dateEndValidity: gets the end validity date of the company
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getDateEndValidity() {
		return dateEndValidity;
	}

	/**
	 * @param dateEndValidity
	 *            : gets the end validity date of the company
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
	 * @return organization: organization to which a company belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_organizacion", referencedColumnName = "id")
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            : organization to which a company belongs.
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return country: Country to which the company belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais", referencedColumnName = "id", nullable = false)
	public Pais getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            : Country to which the company belongs
	 */
	public void setCountry(Pais country) {
		this.country = country;
	}

	/**
	 * @return department : Department to which the company belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
	public Departamento getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            : Department to which the company belongs
	 */
	public void setDepartment(Departamento department) {
		this.department = department;
	}

	/**
	 * @return town: gets the municipalities of a business related.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio", referencedColumnName = "id", nullable = false)
	public Municipio getTown() {
		return town;
	}

	/**
	 * @param town
	 *            : gets the municipalities of a business related.
	 */
	public void setTown(Municipio town) {
		this.town = town;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result
				+ ((dateCreation == null) ? 0 : dateCreation.hashCode());
		result = prime * result
				+ ((dateEndValidity == null) ? 0 : dateEndValidity.hashCode());
		result = prime * result + id;
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
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
		Business other = (Business) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (dateCreation == null) {
			if (other.dateCreation != null)
				return false;
		} else if (!dateCreation.equals(other.dateCreation))
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
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
