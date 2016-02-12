package co.informatix.erp.utz.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.humanResources.entities.Hr;

/**
 * That represents the primary key of the entity HrCertificationsAndRoles.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class HrCertificationsAndRolesPK implements Serializable {
	private Hr hr;
	private CertificationsAndRoles certificationsAndRoles;

	/**
	 * Method constructor that initializes the variables.
	 */
	public HrCertificationsAndRolesPK() {
		this.hr = new Hr();
		this.certificationsAndRoles = new CertificationsAndRoles();
	}

	/**
	 * @return hr: Object that relates to the human resources table.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idhr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Object that relates to the human resources table.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return certificationsAndRoles: Object that relates to the table
	 *         certifications and roles.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcertificationsandroles", referencedColumnName = "idcertificactionsandroles", nullable = false)
	public CertificationsAndRoles getCertificationsAndRoles() {
		return certificationsAndRoles;
	}

	/**
	 * @param certificationsAndRoles
	 *            : Object that relates to the table certifications and roles.
	 * 
	 */
	public void setCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		this.certificationsAndRoles = certificationsAndRoles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((certificationsAndRoles == null) ? 0
						: certificationsAndRoles.hashCode());
		result = prime * result + ((hr == null) ? 0 : hr.hashCode());
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
		HrCertificationsAndRolesPK other = (HrCertificationsAndRolesPK) obj;
		if (certificationsAndRoles == null) {
			if (other.certificationsAndRoles != null)
				return false;
		} else if (!certificationsAndRoles.equals(other.certificationsAndRoles))
			return false;
		if (hr == null) {
			if (other.hr != null)
				return false;
		} else if (!hr.equals(other.hr))
			return false;
		return true;
	}

}
