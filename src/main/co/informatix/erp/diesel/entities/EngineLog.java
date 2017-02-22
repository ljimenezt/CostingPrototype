package co.informatix.erp.diesel.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.humanResources.entities.Hr;

/**
 * This class maps the engine_log class, which contains the information about
 * login and logout of the engine.
 * 
 * @author Patricia.Patinio
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "engine_log", schema = "diesel")
public class EngineLog implements Serializable {

	private int idEngineLog;
	private boolean irrigation;
	private Date date;
	private Date hourOn;
	private Date hourOff;
	private Date hourmeterOn;
	private Date hourmeterOff;
	private Date duration;
	private Hr deliveredBy;
	private Hr receivedBy;
	private ActivityMachine activityMachine;

	/**
	 * @return idEngineLog: engine_log table identifier.
	 */
	@Id
	@Column(name = "id_engine_log", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdEngineLog() {
		return idEngineLog;
	}

	/**
	 * @param idEngineLog
	 *            : engine_log table identifier.
	 */
	public void setIdEngineLog(int idEngineLog) {
		this.idEngineLog = idEngineLog;
	}

	/**
	 * @return irrigation: indicate machine type, if it is a irrigation machine
	 *         return true and false otherwise.
	 */
	@Column(name = "irrigation", nullable = false)
	public boolean isIrrigation() {
		return irrigation;
	}

	/**
	 * @param irrigation
	 *            : indicate machine type, if it is a irrigation machine return
	 *            true and false otherwise.
	 */
	public void setIrrigation(boolean irrigation) {
		this.irrigation = irrigation;
	}

	/**
	 * @return date: indicate the engine log date.
	 */
	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            : indicate the engine log date.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return hourOn: it is a ignition hour for engine log.
	 */
	@Column(name = "hour_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHourOn() {
		return hourOn;
	}

	/**
	 * @param hourOn
	 *            : it is a ignition hour for engine log.
	 */
	public void setHourOn(Date hourOn) {
		this.hourOn = hourOn;
	}

	/**
	 * @return hourOff: it is a ending hour for engine log.
	 */
	@Column(name = "hour_off", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHourOff() {
		return hourOff;
	}

	/**
	 * @param hourOff
	 *            : it is a ending hour for engine log.
	 */
	public void setHourOff(Date hourOff) {
		this.hourOff = hourOff;
	}

	/**
	 * @return hourmeterOn: it is a ignition hour for engine log, which is
	 *         watched by a engine hour meter.
	 */
	@Column(name = "hourmeter_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHourmeterOn() {
		return hourmeterOn;
	}

	/**
	 * @param hourmeterOn
	 *            : it is a ignition hour for engine log, which is watched by a
	 *            engine hour meter.
	 */
	public void setHourmeterOn(Date hourmeterOn) {
		this.hourmeterOn = hourmeterOn;
	}

	/**
	 * @return hourmeterOff: it is a ending hour for engine log, which is
	 *         watched by a engine hour meter.
	 */
	@Column(name = "hourmeter_off", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getHourmeterOff() {
		return hourmeterOff;
	}

	/**
	 * @param hourmeterOff
	 *            : it is a ending hour for engine log, which is watched by a
	 *            engine hour meter.
	 */
	public void setHourmeterOff(Date hourmeterOff) {
		this.hourmeterOff = hourmeterOff;
	}

	/**
	 * @return duration: indicate the difference between hourOn and hourOff.
	 */
	@Column(name = "duration", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            : indicate the difference between hourOn and hourOff.
	 */
	public void setDuration(Date duration) {
		this.duration = duration;
	}

	/**
	 * @return deliveredBy: human resource which delivery the engine.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "delivered_by", referencedColumnName = "idhr")
	public Hr getDeliveredBy() {
		return deliveredBy;
	}

	/**
	 * @param deliveredBy
	 *            : human resource which delivery the engine.
	 */
	public void setDeliveredBy(Hr deliveredBy) {
		this.deliveredBy = deliveredBy;
	}

	/**
	 * @return receivedBy: human resource which receive the engine.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "received_by", referencedColumnName = "idhr")
	public Hr getReceivedBy() {
		return receivedBy;
	}

	/**
	 * @param receivedBy
	 *            : human resource which receive the engine.
	 */
	public void setReceivedBy(Hr receivedBy) {
		this.receivedBy = receivedBy;
	}

	/**
	 * @return activityMachine: activity_machine relationed with the engine log.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumns({
			@JoinColumn(name = "id_activity", referencedColumnName = "id_activity", nullable = false),
			@JoinColumn(name = "id_machine", referencedColumnName = "id_machine", nullable = false) })
	public ActivityMachine getActivityMachine() {
		return activityMachine;
	}

	/**
	 * @param activityMachine
	 *            : activity_machine relationed with the engine log.
	 */
	public void setActivityMachine(ActivityMachine activityMachine) {
		this.activityMachine = activityMachine;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result
				+ ((duration == null) ? 0 : duration.hashCode());
		result = prime * result + ((hourOff == null) ? 0 : hourOff.hashCode());
		result = prime * result + ((hourOn == null) ? 0 : hourOn.hashCode());
		result = prime * result
				+ ((hourmeterOff == null) ? 0 : hourmeterOff.hashCode());
		result = prime * result
				+ ((hourmeterOn == null) ? 0 : hourmeterOn.hashCode());
		result = prime * result + idEngineLog;
		result = prime * result + (irrigation ? 1231 : 1237);
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
		EngineLog other = (EngineLog) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (duration == null) {
			if (other.duration != null)
				return false;
		} else if (!duration.equals(other.duration))
			return false;
		if (hourOff == null) {
			if (other.hourOff != null)
				return false;
		} else if (!hourOff.equals(other.hourOff))
			return false;
		if (hourOn == null) {
			if (other.hourOn != null)
				return false;
		} else if (!hourOn.equals(other.hourOn))
			return false;
		if (hourmeterOff == null) {
			if (other.hourmeterOff != null)
				return false;
		} else if (!hourmeterOff.equals(other.hourmeterOff))
			return false;
		if (hourmeterOn == null) {
			if (other.hourmeterOn != null)
				return false;
		} else if (!hourmeterOn.equals(other.hourmeterOn))
			return false;
		if (idEngineLog != other.idEngineLog)
			return false;
		if (irrigation != other.irrigation)
			return false;
		return true;
	}
}
