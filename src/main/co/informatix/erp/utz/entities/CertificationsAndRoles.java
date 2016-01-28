package co.informatix.erp.utz.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This table maps the class certifications_and_roles
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "certifications_and_roles", schema = "utz")
public class CertificationsAndRoles implements Serializable {
	private int idCertificactionsAndRoles;
	private String name;
	private String description;
	private Boolean certifications;
	private Boolean role;
	private Boolean dangerous;

	/**
	 * @return idCertificactionsAndRoles: id certificate and role
	 */
	@Id
	@Column(name = "idcertificactionsandroles", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdCertificactionsAndRoles() {
		return idCertificactionsAndRoles;
	}

	/**
	 * @param idCertificactionsAndRoles
	 *            :id certificate and role
	 */
	public void setIdCertificactionsAndRoles(int idCertificactionsAndRoles) {
		this.idCertificactionsAndRoles = idCertificactionsAndRoles;
	}

	/**
	 * @return name:certificate name and role
	 */
	@Column(name = "name")
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :certificate name and role
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: Description that has the certificate and role.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :Description that has the certificate and role.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return certifications: boolean certification certificate and role
	 */
	@Column(name = "certifications")
	public Boolean getCertifications() {
		return certifications;
	}

	/**
	 * @param certifications
	 *            :boolean certification certificate and role
	 * 
	 */
	public void setCertifications(Boolean certifications) {
		this.certifications = certifications;
	}

	/**
	 * @return role: Boolean role in certification and role
	 */
	@Column(name = "role")
	public Boolean getRole() {
		return role;
	}

	/**
	 * @param role
	 *            : Boolean role in certification and role
	 */
	public void setRole(Boolean role) {
		this.role = role;
	}

	/**
	 * @return dangerous: Boolean dangerous role in certification and
	 */
	@Column(name = "dangerous")
	public Boolean getDangerous() {
		return dangerous;
	}

	/**
	 * @param dangerous
	 *            :Boolean dangerous role in certification and
	 */
	public void setDangerous(Boolean dangerous) {
		this.dangerous = dangerous;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((certifications == null) ? 0 : certifications.hashCode());
		result = prime * result
				+ ((dangerous == null) ? 0 : dangerous.hashCode());
		result = prime * result + idCertificactionsAndRoles;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((role == null) ? 0 : role.hashCode());
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
		CertificationsAndRoles other = (CertificationsAndRoles) obj;
		if (certifications == null) {
			if (other.certifications != null)
				return false;
		} else if (!certifications.equals(other.certifications))
			return false;
		if (dangerous == null) {
			if (other.dangerous != null)
				return false;
		} else if (!dangerous.equals(other.dangerous))
			return false;
		if (idCertificactionsAndRoles != other.idCertificactionsAndRoles)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (role == null) {
			if (other.role != null)
				return false;
		} else if (!role.equals(other.role))
			return false;
		return true;
	}

}
