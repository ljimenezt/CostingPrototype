package co.informatix.erp.utz.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity is responsible for mapping the activities table and HR
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "hr_certifications_and_roles", schema = "utz")
public class HrCertificationsAndRoles implements Serializable {
	private HrCertificationsAndRolesPK hrCertificationsAndRolesPK;

	private Date releaseDate;
	private Date endDate;
	private String linkToCertificate;

	/**
	 * @return hrCertificationsAndRolesPK: Object of the composite key table
	 *         HrAndCertificationsAndRoles
	 */
	@EmbeddedId
	public HrCertificationsAndRolesPK getHrCertificationsAndRolesPK() {
		return hrCertificationsAndRolesPK;
	}

	/**
	 * @param hrCertificationsAndRolesPK
	 *            : Object of the composite key table
	 *            HrAndCertificationsAndRoles
	 */
	public void setHrCertificationsAndRolesPK(
			HrCertificationsAndRolesPK hrCertificationsAndRolesPK) {
		this.hrCertificationsAndRolesPK = hrCertificationsAndRolesPK;
	}

	/**
	 * @return releaseDate: Initial date of registration.
	 */
	@Column(name = "release_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getReleaseDate() {
		return releaseDate;
	}

	/**
	 * @param releaseDate
	 *            : Initial date of registration.
	 */
	public void setReleaseDate(Date releaseDate) {
		this.releaseDate = releaseDate;
	}

	/**
	 * @return endDate: End date of the record.
	 */
	@Column(name = "end_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getEndDate() {
		return endDate;
	}

	/**
	 * @param endDate
	 *            : End date of the record.
	 */
	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	/**
	 * @return linkToCertificate: Path where there is the certificate.
	 */
	@Column(name = "link_to_certificate")
	public String getLinkToCertificate() {
		return linkToCertificate;
	}

	/**
	 * @param linkToCertificate
	 *            : Path where there is the certificate.
	 * 
	 */
	public void setLinkToCertificate(String linkToCertificate) {
		this.linkToCertificate = linkToCertificate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((endDate == null) ? 0 : endDate.hashCode());
		result = prime
				* result
				+ ((hrCertificationsAndRolesPK == null) ? 0
						: hrCertificationsAndRolesPK.hashCode());
		result = prime
				* result
				+ ((linkToCertificate == null) ? 0 : linkToCertificate
						.hashCode());
		result = prime * result
				+ ((releaseDate == null) ? 0 : releaseDate.hashCode());
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
		HrCertificationsAndRoles other = (HrCertificationsAndRoles) obj;
		if (endDate == null) {
			if (other.endDate != null)
				return false;
		} else if (!endDate.equals(other.endDate))
			return false;
		if (hrCertificationsAndRolesPK == null) {
			if (other.hrCertificationsAndRolesPK != null)
				return false;
		} else if (!hrCertificationsAndRolesPK
				.equals(other.hrCertificationsAndRolesPK))
			return false;
		if (linkToCertificate == null) {
			if (other.linkToCertificate != null)
				return false;
		} else if (!linkToCertificate.equals(other.linkToCertificate))
			return false;
		if (releaseDate == null) {
			if (other.releaseDate != null)
				return false;
		} else if (!releaseDate.equals(other.releaseDate))
			return false;
		return true;
	}

}
