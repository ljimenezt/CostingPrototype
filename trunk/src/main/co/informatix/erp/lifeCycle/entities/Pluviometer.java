package co.informatix.erp.lifeCycle.entities;

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
 * This class contains the information of the measures taken daily of the
 * pluviometer.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "pluviometer", schema = "life_cycle")
public class Pluviometer implements Serializable {

	private int id;
	private int reading;
	private int week;
	private int year;
	private int day;

	private Date dateRecord;

	/**
	 * @return id: Pluviometer identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Pluviometer identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return reading: Reading of the pluviometer.
	 */
	@Column(name = "reading", nullable = false)
	public int getReading() {
		return reading;
	}

	/**
	 * @param reading
	 *            : Reading of the pluviometer.
	 */
	public void setReading(int reading) {
		this.reading = reading;
	}

	/**
	 * @return week: Week number.
	 */
	@Column(name = "week", nullable = false)
	public int getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            : Week number.
	 */
	public void setWeek(int week) {
		this.week = week;
	}

	/**
	 * @return year: Year in progress.
	 */
	@Column(name = "year", nullable = false)
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            : Year in progress.
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return day: Number of weekday.
	 */
	@Column(name = "day", nullable = false)
	public int getDay() {
		return day;
	}

	/**
	 * @param day
	 *            : Number of weekday.
	 */
	public void setDay(int day) {
		this.day = day;
	}

	/**
	 * @return dateRecord: Day that the reading was taken.
	 */
	@Column(name = "date_record", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateRecord() {
		return dateRecord;
	}

	/**
	 * @param dateRecord
	 *            : Day that the reading was taken.
	 */
	public void setDateRecord(Date dateRecord) {
		this.dateRecord = dateRecord;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateRecord == null) ? 0 : dateRecord.hashCode());
		result = prime * result + day;
		result = prime * result + id;
		result = prime * result + reading;
		result = prime * result + week;
		result = prime * result + year;
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
		Pluviometer other = (Pluviometer) obj;
		if (dateRecord == null) {
			if (other.dateRecord != null)
				return false;
		} else if (!dateRecord.equals(other.dateRecord))
			return false;
		if (day != other.day)
			return false;
		if (id != other.id)
			return false;
		if (reading != other.reading)
			return false;
		if (week != other.week)
			return false;
		if (year != other.year)
			return false;
		return true;
	}
}