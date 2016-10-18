package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;

/**
 * This class maps the farm table in the database
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "farm", schema = "life_cycle")
public class Farm implements Serializable, Comparable<Farm>, Cloneable {
	private int idFarm;
	private String name;
	private Double locationLinkToMap;
	private String otherFieldAddress;
	private String nit;
	private String commercialName;
	private String logo;
	private String address;
	private String phone;
	private String mail;
	private String fax;
	private String mobile;

	private Pais country;
	private Departamento department;
	private Municipio town;
	
	/**
	 * Empty constructor to initialize the necessary variables.
	 */
	public Farm() {
		this.country = new Pais();
		this.department = new Departamento();
		this.town = new Municipio();
	}

	/**
	 * @return idFarm: Identifier farm.
	 */
	@Id
	@Column(name = "idfarm", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdFarm() {
		return idFarm;
	}

	/**
	 * @param idFarm
	 *            : Identifier farm.
	 */
	public void setIdFarm(int idFarm) {
		this.idFarm = idFarm;
	}

	/**
	 * @return name: farm name
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :farm name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return locationLinkToMap: location of the farm
	 */
	@Column(name = "location_link_to_map")
	public Double getLocationLinkToMap() {
		return locationLinkToMap;
	}

	/**
	 * @param locationLinkToMap
	 *            :location of the farm
	 */
	public void setLocationLinkToMap(Double locationLinkToMap) {
		this.locationLinkToMap = locationLinkToMap;
	}

	/**
	 * @return otherFieldAddress: direction of the farm
	 */
	@Column(name = "other_field_address", length = 100)
	public String getOtherFieldAddress() {
		return otherFieldAddress;
	}

	/**
	 * @param otherFieldAddress
	 *            :direction of the farm
	 */
	public void setOtherFieldAddress(String otherFieldAddress) {
		this.otherFieldAddress = otherFieldAddress;
	}

	/**
	 * @return nit: tax identification number of a farm
	 */
	@Column(name = "nit", length = 25)
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit
	 *            : tax identification number of a farm
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return commercialName: Get the trade name of the estate.
	 */
	@Column(name = "nombre_comercial", length = 100)
	public String getCommercialName() {
		return commercialName;
	}

	/**
	 * @param commercialName
	 *            : Sets the trade name of the estate.
	 */
	public void setCommercialName(String commercialName) {
		this.commercialName = commercialName;
	}

	/**
	 * @return logo: logo of the farm
	 */
	@Column(name = "logo", length = 200)
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            : logo of the farm
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return address: gets the address of the farm.
	 */
	@Column(name = "direccion", length = 50)
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            : sets the address of the farm.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return phone: get the phone of a farm
	 */
	@Column(name = "telefono", length = 150)
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            : set the phone of a farm
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return mail: getting mail from a farm.
	 */
	@Column(name = "correo", length = 50)
	public String getMail() {
		return mail;
	}

	/**
	 * @param mail
	 *            : setting mail from a farm.
	 */
	public void setMail(String mail) {
		this.mail = mail;
	}

	/**
	 * @return fax: get the FAX of a farm.
	 */
	@Column(name = "fax", length = 20)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            : set the FAX of a farm.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return mobile: mobile phone farm.
	 */
	@Column(name = "movil", length = 20)
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            : mobile phone farm.
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return country: Country to which the farm belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais", referencedColumnName = "id")
	public Pais getCountry() {
		return country;
	}

	/**
	 * @param country
	 *            :Country to which the farm belongs
	 */
	public void setCountry(Pais country) {
		this.country = country;
	}

	/**
	 * @return department : Department to which the farm belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id")
	public Departamento getDepartment() {
		return department;
	}

	/**
	 * @param department
	 *            : Department to which the farm belongs
	 */
	public void setDepartment(Departamento department) {
		this.department = department;
	}

	/**
	 * @return town: get the town relate of a farm
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio", referencedColumnName = "id")
	public Municipio getTown() {
		return town;
	}


	/**
	 * @param town
	 *            : set the town relate of a farm
	 */
	public void setTown(Municipio town) {
		this.town = town;
	}

	@Override
	public int compareTo(Farm farm) {
		return name.compareTo(farm.getName());
	}

	public Farm clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (Farm) clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idFarm;
		result = prime
				* result
				+ ((locationLinkToMap == null) ? 0 : locationLinkToMap
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((otherFieldAddress == null) ? 0 : otherFieldAddress
						.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result
				+ ((commercialName == null) ? 0 : commercialName.hashCode());
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((mail == null) ? 0 : mail.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
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
		Farm other = (Farm) obj;
		if (idFarm != other.idFarm)
			return false;
		if (locationLinkToMap == null) {
			if (other.locationLinkToMap != null)
				return false;
		} else if (!locationLinkToMap.equals(other.locationLinkToMap))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (otherFieldAddress == null) {
			if (other.otherFieldAddress != null)
				return false;
		} else if (!otherFieldAddress.equals(other.otherFieldAddress))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (commercialName == null) {
			if (other.commercialName != null)
				return false;
		} else if (!commercialName.equals(other.commercialName))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (mail == null) {
			if (other.mail != null)
				return false;
		} else if (!mail.equals(other.mail))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		return true;
	}

}
