package co.informatix.erp.seguridad.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class contains information system profile.
 * 
 * @author Wilhelm.Boada
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "system_profile", schema = "general")
public class SystemProfile implements Serializable {

	private short id;
	private double breakDuration;
	private double activityDefaultDuration;
	private Date breakStart;
	private Date breakEnd;
	private Date activityDefaultStart;
	private Date activityDefaultEnd;

	/**
	 * @return id: Identifier system profile.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public short getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Identifier system profile.
	 */
	public void setId(short id) {
		this.id = id;
	}

	/**
	 * @return breakDuration: Break duration.
	 */
	@Column(name = "break_duration", nullable = false)
	public double getBreakDuration() {
		return breakDuration;
	}

	/**
	 * @param breakDuration
	 *            : Break duration.
	 */
	public void setBreakDuration(double breakDuration) {
		this.breakDuration = breakDuration;
	}

	/**
	 * @return activityDefaultDuration: Activity default duration.
	 */
	@Column(name = "activity_default_duration", nullable = false)
	public double getActivityDefaultDuration() {
		return activityDefaultDuration;
	}

	/**
	 * @param activityDefaultDuration
	 *            : Activity default duration.
	 */
	public void setActivityDefaultDuration(double activityDefaultDuration) {
		this.activityDefaultDuration = activityDefaultDuration;
	}

	/**
	 * @return breakStart: Start time break.
	 */
	@Column(name = "break_start", nullable = false)
	@Temporal(TemporalType.TIME)
	public Date getBreakStart() {
		return breakStart;
	}

	/**
	 * @param breakStart
	 *            : Start time break.
	 */
	public void setBreakStart(Date breakStart) {
		this.breakStart = breakStart;
	}

	/**
	 * @return breakEnd: End time break.
	 */
	@Column(name = "break_end", nullable = false)
	@Temporal(TemporalType.TIME)
	public Date getBreakEnd() {
		return breakEnd;
	}

	/**
	 * @param breakEnd
	 *            : End time break.
	 */
	public void setBreakEnd(Date breakEnd) {
		this.breakEnd = breakEnd;
	}

	/**
	 * @return activityDefaultStart: Start time activity.
	 */
	@Column(name = "activity_default_start", nullable = false)
	@Temporal(TemporalType.TIME)
	public Date getActivityDefaultStart() {
		return activityDefaultStart;
	}

	/**
	 * @param activityDefaultStart
	 *            : Start time activity.
	 */
	public void setActivityDefaultStart(Date activityDefaultStart) {
		this.activityDefaultStart = activityDefaultStart;
	}

	/**
	 * @return activityDefaultEnd: End time activity.
	 */
	@Column(name = "activity_default_end", nullable = false)
	@Temporal(TemporalType.TIME)
	public Date getActivityDefaultEnd() {
		return activityDefaultEnd;
	}

	/**
	 * @param activityDefaultEnd
	 *            : End time activity.
	 */
	public void setActivityDefaultEnd(Date activityDefaultEnd) {
		this.activityDefaultEnd = activityDefaultEnd;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(activityDefaultDuration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((activityDefaultEnd == null) ? 0 : activityDefaultEnd
						.hashCode());
		result = prime
				* result
				+ ((activityDefaultStart == null) ? 0 : activityDefaultStart
						.hashCode());
		temp = Double.doubleToLongBits(breakDuration);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((breakEnd == null) ? 0 : breakEnd.hashCode());
		result = prime * result
				+ ((breakStart == null) ? 0 : breakStart.hashCode());
		result = prime * result + id;
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
		SystemProfile other = (SystemProfile) obj;
		if (Double.doubleToLongBits(activityDefaultDuration) != Double
				.doubleToLongBits(other.activityDefaultDuration))
			return false;
		if (activityDefaultEnd == null) {
			if (other.activityDefaultEnd != null)
				return false;
		} else if (!activityDefaultEnd.equals(other.activityDefaultEnd))
			return false;
		if (activityDefaultStart == null) {
			if (other.activityDefaultStart != null)
				return false;
		} else if (!activityDefaultStart.equals(other.activityDefaultStart))
			return false;
		if (Double.doubleToLongBits(breakDuration) != Double
				.doubleToLongBits(other.breakDuration))
			return false;
		if (breakEnd == null) {
			if (other.breakEnd != null)
				return false;
		} else if (!breakEnd.equals(other.breakEnd))
			return false;
		if (breakStart == null) {
			if (other.breakStart != null)
				return false;
		} else if (!breakStart.equals(other.breakStart))
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
