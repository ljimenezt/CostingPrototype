package co.informatix.erp.seguridad.entities;

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

import co.informatix.erp.humanResources.entities.Person;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.organizations.entities.Business;

/**
 * This class maps the table permiso_empresa_persona_sucursal_hacienda, which
 * can store the information of the access rights of individuals to a company or
 * companies and their subsidiaries and / or farms.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "permiso_persona_empresa", schema = "seguridad")
public class PermissionPersonBusiness implements Serializable {

	private int id;
	private Date dateCreation;
	private Date dateStartValidity;
	private Date dateEndValidity;
	private boolean predetermined;
	private String userName;

	private Business business;
	private Person person;
	private Farm farm;

	/**
	 * @return id: identifier permit for the person in the company.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : identifier permit for the person in the company.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return dateCreation: Date of creating access permission for the person.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * 
	 * @param dateCreation
	 *            : Date of creating access permission for the person.
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return dateStartValidity: current start date of the permit for access to
	 *         the person.
	 */
	@Column(name = "fecha_inicio_vigencia", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateStartValidity() {
		return dateStartValidity;
	}

	/**
	 * @param dateStartValidity
	 *            : current start date of the permit for access to the person.
	 */
	public void setDateStartValidity(Date dateStartValidity) {
		this.dateStartValidity = dateStartValidity;
	}

	/**
	 * @return dateEndValidity: current ending date of the permit for access to
	 *         the person.
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getDateEndValidity() {
		return dateEndValidity;
	}

	/**
	 * @param dateEndValidity
	 *            : current ending date of the permit for access to the person.
	 */
	public void setDateEndValidity(Date dateEndValidity) {
		this.dateEndValidity = dateEndValidity;
	}

	/**
	 * @return predetermined: Indicates the default company to be charged to log
	 *         into the system.
	 */
	@Column(name = "predeterminada")
	public boolean isPredetermined() {
		return predetermined;
	}

	/**
	 * @param predetermined
	 *            : Indicates the default company to be charged to log into the
	 *            system.
	 */
	public void setPredetermined(boolean predetermined) {
		this.predetermined = predetermined;
	}

	/**
	 * @return userName: User name in session running the action on the
	 *         registration of the transfer movement son.
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name in session running the action on the registration
	 *            of the transfer movement son.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return business: Company which the person has access permissions.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
	public Business getBusiness() {
		return business;
	}

	/**
	 * @param business
	 *            : Company which the person has access permissions.
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

	/**
	 * @return person: person who is assigned access permissions
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona", referencedColumnName = "id", nullable = false)
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            : person who is assigned access permissions
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return farm: farm to which the person has permission to view the
	 *         information.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_farm", referencedColumnName = "idfarm", nullable = false)
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            : farm to which the person has permission to view the
	 *            information.
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateCreation == null) ? 0 : dateCreation.hashCode());
		result = prime * result
				+ ((dateEndValidity == null) ? 0 : dateEndValidity.hashCode());
		result = prime
				* result
				+ ((dateStartValidity == null) ? 0 : dateStartValidity
						.hashCode());
		result = prime * result + id;
		result = prime * result + (predetermined ? 1231 : 1237);
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
		PermissionPersonBusiness other = (PermissionPersonBusiness) obj;
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
		if (dateStartValidity == null) {
			if (other.dateStartValidity != null)
				return false;
		} else if (!dateStartValidity.equals(other.dateStartValidity))
			return false;
		if (id != other.id)
			return false;
		if (predetermined != other.predetermined)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}