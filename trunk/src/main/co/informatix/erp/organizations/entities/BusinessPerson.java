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

import co.informatix.erp.humanResources.entities.ChargeType;
import co.informatix.erp.humanResources.entities.Person;
import co.informatix.erp.lifeCycle.entities.Farm;

/**
 * This class maps the empresa_persona table, which contains the information of
 * persons belonging to a company.
 * 
 * @author Luz.Jaimes
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "empresa_persona", schema = "organizaciones")
public class BusinessPerson implements Serializable {

	private int id;
	private boolean contact;
	private Date creationDate;
	private Date dateInitialValidity;
	private Date dateEndValidity;
	private String userName;

	private Person person;
	private Business business;
	private Farm farm;
	private ChargeType typeCharge;

	/**
	 * @return id: Unique identifier of the table or entity.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :Unique identifier of the table or entity.s
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return contact: Attribute that indicates whether the person is business
	 *         contact
	 */
	@Column(name = "contacto", nullable = false)
	public boolean isContact() {
		return contact;
	}

	/**
	 * @param contact
	 *            :Attribute that indicates whether the person is business
	 *            contact
	 */
	public void setContact(boolean contact) {
		this.contact = contact;
	}

	/**
	 * @return creationDate: Creation date of registration of the person
	 *         associated with the company
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getCreationDate() {
		return creationDate;
	}

	/**
	 * @param creationDate
	 *            :Creation date of registration of the person associated with
	 *            the company
	 */
	public void setCreationDate(Date creationDate) {
		this.creationDate = creationDate;
	}

	/**
	 * @return dateInitialValidity: Start date of validity of the company for
	 *         the person.
	 */
	@Column(name = "fecha_inicio_vigencia", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateInitialValidity() {
		return dateInitialValidity;
	}

	/**
	 * @param dateInitialValidity
	 *            :Start date of validity of the company for the person.
	 */
	public void setDateInitialValidity(Date dateInitialValidity) {
		this.dateInitialValidity = dateInitialValidity;
	}

	/**
	 * @return fechaFinVigencia: End date validity the company to the person.
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getDateEndValidity() {
		return dateEndValidity;
	}

	/**
	 * @param fechaFinVigencia
	 *            : End date validity the company to the person.
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
	 *            :User name session running in action on recording
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return person: Reference to the person who is related to the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona", referencedColumnName = "id", nullable = false)
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            :Reference to the person who is related to the company.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return business: Reference to the company to which the person belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
	public Business getBusiness() {
		return business;
	}

	/**
	 * @param business
	 *            : Reference to the company to which the person belongs.
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

	/**
	 * @return farm: ID of the farm if you work exclusively on a farm, whether
	 *         it is null is because not directly working on the farm but for
	 *         the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_farm", referencedColumnName = "idfarm", nullable = true)
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            :ID of the farm if you work exclusively on a farm, whether it
	 *            is null is because not directly working on the farm but for
	 *            the company.
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	/**
	 * @return typeCharge: Reference to the type of jobs held by the person in
	 *         the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_cargo", referencedColumnName = "id", nullable = false)
	public ChargeType getTypeCharge() {
		return typeCharge;
	}

	/**
	 * @param typeCharge
	 *            :Reference to the type of jobs held by the person in the
	 *            company.
	 */
	public void setTypeCharge(ChargeType typeCharge) {
		this.typeCharge = typeCharge;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contact ? 1231 : 1237);
		result = prime * result
				+ ((creationDate == null) ? 0 : creationDate.hashCode());
		result = prime * result
				+ ((dateEndValidity == null) ? 0 : dateEndValidity.hashCode());
		result = prime
				* result
				+ ((dateInitialValidity == null) ? 0 : dateInitialValidity
						.hashCode());
		result = prime * result + id;
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
		BusinessPerson other = (BusinessPerson) obj;
		if (contact != other.contact)
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
		if (dateInitialValidity == null) {
			if (other.dateInitialValidity != null)
				return false;
		} else if (!dateInitialValidity.equals(other.dateInitialValidity))
			return false;
		if (id != other.id)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
