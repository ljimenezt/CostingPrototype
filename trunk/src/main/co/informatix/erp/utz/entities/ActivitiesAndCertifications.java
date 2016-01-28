package co.informatix.erp.utz.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * Class representing table maps the activities _and_ certifications
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activities_and_certifications", schema = "utz")
public class ActivitiesAndCertifications implements Serializable {
	private ActivitiesAndCertificationsPK activitiesAndCertificationsPK;

	/**
	 * Constructor that initializes the primary key
	 */
	public ActivitiesAndCertifications() {
		this.activitiesAndCertificationsPK = new ActivitiesAndCertificationsPK();
	}

	/**
	 * @return activitiesAndCertificationsPK: activities_and_certifications
	 *         primary key of the table
	 * 
	 */
	@EmbeddedId
	public ActivitiesAndCertificationsPK getActivitiesAndCertificationsPK() {
		return activitiesAndCertificationsPK;
	}

	/**
	 * @param activitiesAndCertificationsPK
	 *            :activities_and_certifications primary key of the table
	 */
	public void setActivitiesAndCertificationsPK(
			ActivitiesAndCertificationsPK activitiesAndCertificationsPK) {
		this.activitiesAndCertificationsPK = activitiesAndCertificationsPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activitiesAndCertificationsPK == null) ? 0
						: activitiesAndCertificationsPK.hashCode());
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
		ActivitiesAndCertifications other = (ActivitiesAndCertifications) obj;
		if (activitiesAndCertificationsPK == null) {
			if (other.activitiesAndCertificationsPK != null)
				return false;
		} else if (!activitiesAndCertificationsPK
				.equals(other.activitiesAndCertificationsPK))
			return false;
		return true;
	}

}
