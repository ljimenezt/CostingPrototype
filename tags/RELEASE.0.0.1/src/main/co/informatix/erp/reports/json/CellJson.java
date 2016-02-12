package co.informatix.erp.reports.json;

import java.io.Serializable;

/**
 * This class contains each cell in the Rows for to build json rows (Google
 * Charts) with the data for the dashboard. The documentation in
 * https://developers.google.com/chart/interactive/docs/reference#dataparam
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
public class CellJson implements Serializable {

	private Object v;
	private String f;
	private String p;

	/**
	 * @return v: (Optional), The cell value. The data type should match the
	 *         column data type. If the cell is null, the v property should be
	 *         null, though it can still have f and p properties.
	 */
	public Object getV() {
		return v;
	}

	/**
	 * @param v
	 *            : (Optional), The cell value. The data type should match the
	 *            column data type. If the cell is null, the v property should
	 *            be null, though it can still have f and p properties.
	 */
	public void setV(Object v) {
		if (v != null) {
			if (v.getClass().equals(String.class)) {
				v = v.toString();
			}
			if (v.getClass().equals(Integer.class)) {
				v = Integer.parseInt(v.toString());
			}
			if (v.getClass().equals(Double.class)) {
				v = Double.parseDouble(v.toString());
			}
		}
		this.v = v;
	}

	/**
	 * @return f: (Optional), A string version of the v value, formatted for
	 *         display.
	 */
	public String getF() {
		return f;
	}

	/**
	 * @param f
	 *            : (Optional), A string version of the v value, formatted for
	 *            display.
	 */
	public void setF(String f) {
		this.f = f;
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
