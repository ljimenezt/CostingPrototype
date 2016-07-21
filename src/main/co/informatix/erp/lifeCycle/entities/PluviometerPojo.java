package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * This class contains the fields necessary for the report of the rain gauge.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
public class PluviometerPojo implements Serializable {

	private int week;
	private int total;
	private int vector[];
	private Date startWeek;
	private Date endWeek;

	/**
	 * @return week: week number.
	 */
	public int getWeek() {
		return week;
	}

	/**
	 * @param week
	 *            : week number.
	 */
	public void setWeek(int week) {
		this.week = week;
	}

	/**
	 * @return total: Total readings.
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            : Total readings.
	 */
	public void setTotal(int total) {
		this.total = total;
	}

	/**
	 * @return vector: Vector having the readings of the week.
	 */
	public int[] getVector() {
		return vector;
	}

	/**
	 * @param vector
	 *            : Vector having the readings of the week.
	 */
	public void setVector(int[] vector) {
		this.vector = vector;
	}

	/**
	 * @param position
	 *            : Vector position.
	 * @param value
	 *            : value of the position.
	 */
	public void setVectorPos(int position, int value) {
		this.vector[position] = value;
	}

	/**
	 * @param position
	 *            : Vector position.
	 * 
	 * @return vector[position]: value of the position.
	 */
	public int getVectorPos(int position) {
		return vector[position];
	}

	/**
	 * @return startWeek: Start date of the week.
	 */
	public Date getStartWeek() {
		return startWeek;
	}

	/**
	 * @param startWeek
	 *            : Start date of the week.
	 */
	public void setStartWeek(Date startWeek) {
		this.startWeek = startWeek;
	}

	/**
	 * @return endWeek: End date of the week.
	 */
	public Date getEndWeek() {
		return endWeek;
	}

	/**
	 * @param endWeek
	 *            : End date of the week.
	 */
	public void setEndWeek(Date endWeek) {
		this.endWeek = endWeek;
	}
}
