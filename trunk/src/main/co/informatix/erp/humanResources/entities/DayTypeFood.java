package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.informatix.erp.informacionBase.entities.Day;
import co.informatix.erp.informacionBase.entities.TypeFood;

/**
 * This class maps the type day type food table table, which contains the
 * information of the day.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "day_type_food", schema = "human_resources")
public class DayTypeFood implements Serializable {

	private int id;
	private boolean afterHoliday;

	private Day day;
	private TypeFood typeFood;

	/**
	 * @return id: Day type food identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :Day type food identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return afterHoliday: This field identify whether the current day is
	 *         after of a holiday.
	 */
	@Column(name = "after_holiday")
	public boolean isAfterHoliday() {
		return afterHoliday;
	}

	/**
	 * @param afterHoliday
	 *            : This field identify whether the current day is after of a
	 *            holiday.
	 */
	public void setAfterHoliday(boolean afterHoliday) {
		this.afterHoliday = afterHoliday;
	}

	/**
	 * @return day: Day to which the dayTypeFood belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_day", referencedColumnName = "id", nullable = false)
	public Day getDay() {
		return day;
	}

	/**
	 * @param day
	 *            : Day to which the dayTypeFood belong.
	 */
	public void setDay(Day day) {
		this.day = day;
	}

	/**
	 * @return typeFood: TypeFood to which the dayTypeFood belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_type_food", referencedColumnName = "id", nullable = false)
	public TypeFood getTypeFood() {
		return typeFood;
	}

	/**
	 * @param typeFood
	 *            : TypeFood to which the dayTypeFood belong.
	 */
	public void setTypeFood(TypeFood typeFood) {
		this.typeFood = typeFood;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (afterHoliday ? 1231 : 1237);
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
		DayTypeFood other = (DayTypeFood) obj;
		if (afterHoliday != other.afterHoliday)
			return false;
		if (id != other.id)
			return false;
		return true;
	}
}
