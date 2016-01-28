package co.informatix.erp.reports.json;

import java.io.Serializable;
import java.util.List;

/**
 * This class contains the necessary fields to build json rows (Google Charts)
 * with the data for the dashboard. The documentation in
 * https://developers.google.com/chart/interactive/docs/reference#dataparam
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
public class RowsJson implements Serializable {

	private List<CellJson> c;
	private String p;

	/**
	 * @return c: Each row object has one required property called c, which is
	 *         an array of cells in that row.
	 */
	public List<CellJson> getC() {
		return c;
	}

	/**
	 * @param c
	 *            : Each row object has one required property called c, which is
	 *            an array of cells in that row.
	 */
	public void setC(List<CellJson> c) {
		this.c = c;
	}

	/**
	 * @return p: (Optional), defines a map of arbitrary custom values to assign
	 *         to the whole row.
	 */
	public String getP() {
		return p;
	}

	/**
	 * @param p
	 *            : (Optional), defines a map of arbitrary custom values to
	 *            assign to the whole row.
	 */
	public void setP(String p) {
		this.p = p;
	}

}
