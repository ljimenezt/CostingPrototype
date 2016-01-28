package co.informatix.erp.sales.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the customer table in the database.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "customer", schema = "sales")
public class Customer implements Serializable {

	private int idCustomer;

	private String name;
	private String address;
	private String city;
	private String postalCode;
	private String phone;
	private String phone2;
	private String fax;

	/**
	 * @return idCustomer: Customer Id.
	 */
	@Id
	@Column(name = "idcustomer", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdCustomer() {
		return idCustomer;
	}

	/**
	 * @param idCustomer
	 *            :Customer Id.
	 */
	public void setIdCustomer(int idCustomer) {
		this.idCustomer = idCustomer;
	}

	/**
	 * @return name: Customer name.
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :Customer name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return address: Address customer.
	 */
	@Column(name = "address", length = 100)
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            :Address customer.
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return city: Town Customer.
	 */
	@Column(name = "city", length = 100)
	public String getCity() {
		return city;
	}

	/**
	 * @param city
	 *            :Town Customer.
	 */
	public void setCity(String city) {
		this.city = city;
	}

	/**
	 * @return postalCode: Zip customer.
	 */
	@Column(name = "postal_code", length = 30)
	public String getPostalCode() {
		return postalCode;
	}

	/**
	 * @param postalCode
	 *            :Zip customer.
	 */
	public void setPostalCode(String postalCode) {
		this.postalCode = postalCode;
	}

	/**
	 * @return phone: Phone customer.
	 */
	@Column(name = "phone", length = 30)
	public String getPhone() {
		return phone;
	}

	/**
	 * @param phone
	 *            :Phone customer.
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * @return phone2: Second phone customer.
	 */
	@Column(name = "phone_2", length = 30)
	public String getPhone2() {
		return phone2;
	}

	/**
	 * @param phone2
	 *            :Second phone customer.
	 */
	public void setPhone2(String phone2) {
		this.phone2 = phone2;
	}

	/**
	 * @return fax: Fax number of the customer.
	 */
	@Column(name = "fax", length = 30)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            :Fax number of the customer.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + idCustomer;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((phone2 == null) ? 0 : phone2.hashCode());
		result = prime * result
				+ ((postalCode == null) ? 0 : postalCode.hashCode());
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
		Customer other = (Customer) obj;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (idCustomer != other.idCustomer)
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
		if (phone2 == null) {
			if (other.phone2 != null)
				return false;
		} else if (!phone2.equals(other.phone2))
			return false;
		if (postalCode == null) {
			if (other.postalCode != null)
				return false;
		} else if (!postalCode.equals(other.postalCode))
			return false;
		return true;
	}

}
