package co.informatix.erp.reports.json;

import java.io.Serializable;

/**
 * This class contains the necessary fields to build json columns (Google
 * Charts) with the data for the dashboard. The documentation in
 * https://developers.google.com/chart/interactive/docs/reference#dataparam
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
public class ColsJson implements Serializable {

	private String id;
	private String label;
	private String pattern;
	private String type;
	private String p;

	/**
	 * @return id: Optional), String ID of the column. Must be unique in the
	 *         table. Use basic alphanumeric characters, so the host page does
	 *         not require fancy escapes to access the column in JavaScript.
	 */
	public String getId() {
		return id;
	}

	/**
	 * @param id
	 *            : (Optional), String ID of the column. Must be unique in the
	 *            table. Use basic alphanumeric characters, so the host page
	 *            does not require fancy escapes to access the column in
	 *            JavaScript.
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * @return label: (Optional), String value that some visualizations display
	 *         for this column.
	 */
	public String getLabel() {
		return label;
	}

	/**
	 * @param label
	 *            : (Optional), String value that some visualizations display
	 *            for this column.
	 */
	public void setLabel(String label) {
		this.label = label;
	}

	/**
	 * @return pattern: (Optional), String pattern that was used by a data
	 *         source to format numeric, date, or time column values.
	 */
	public String getPattern() {
		return pattern;
	}

	/**
	 * @param pattern
	 *            : (Optional), String pattern that was used by a data source to
	 *            format numeric, date, or time column values.
	 */
	public void setPattern(String pattern) {
		this.pattern = pattern;
	}

	/**
	 * @return type: (Required), Data type of the data in the column, can be:
	 *         boolean, number, string, date, datetime, timeofday.
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param type
	 *            : (Required), Data type of the data in the column, can be:
	 *            boolean, number, string, date, datetime, timeofday.
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return p: (Optional), An object that is a map of custom values applied
	 *         to the cell.
	 */
	public String getP() {
		return p;
	}

	/**
	 * @param p
	 *            : (Optional), An object that is a map of custom values applied
	 *            to the cell.
	 */
	public void setP(String p) {
		this.p = p;
	}

}
