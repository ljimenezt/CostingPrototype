package co.informatix.erp.humanResources.entities;

import java.io.Serializable;
import java.util.Date;

/**
 * This class contains the fields necessary for the report of the rain gauge.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
public class AssistControlPojo implements Serializable {

	private int vector[];
	private Date startWeek;
	private Date endWeek;
	private String name;

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

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}
}
