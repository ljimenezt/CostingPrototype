package co.informatix.erp.utz.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.costs.entities.Activities;

/**
 * That represents the primary key of the table activities_and_certifications
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ActivitiesAndCertificationsPK implements Serializable {
	private Activities activities;
	private CertificationsAndRoles certificationsAndRoles;

	/**
	 * @return activities:activities of ActivitiesAndCertifications
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactivity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            :activities of ActivitiesAndCertifications
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return certificationsAndRoles:certificationsAndRoles of
	 *         ActivitiesAndCertifications
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcertificationsandroles", referencedColumnName = "idcertificactionsandroles", nullable = false)
	public CertificationsAndRoles getCertificationsAndRoles() {
		return certificationsAndRoles;
	}

	/**
	 * @param certificationsAndRoles
	 *            :certificationsAndRoles of ActivitiesAndCertifications
	 */
	public void setCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		this.certificationsAndRoles = certificationsAndRoles;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime
				* result
				+ ((certificationsAndRoles == null) ? 0
						: certificationsAndRoles.hashCode());
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
		ActivitiesAndCertificationsPK other = (ActivitiesAndCertificationsPK) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (certificationsAndRoles == null) {
			if (other.certificationsAndRoles != null)
				return false;
		} else if (!certificationsAndRoles.equals(other.certificationsAndRoles))
			return false;
		return true;
	}

}
