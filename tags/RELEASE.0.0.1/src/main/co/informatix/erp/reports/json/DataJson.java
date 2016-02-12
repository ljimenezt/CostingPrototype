package co.informatix.erp.reports.json;

import java.io.Serializable;
import java.util.List;

/**
 * This class contains the information the rows and columns forming the json
 * loaded into the dashboard
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
public class DataJson implements Serializable {

	private List<ColsJson> cols;
	private List<RowsJson> rows;

	/**
	 * @return cols: the columns forming dashboard data.
	 */
	public List<ColsJson> getCols() {
		return cols;
	}

	/**
	 * @param cols
	 *            : the columns forming dashboard data.
	 */
	public void setCols(List<ColsJson> cols) {
		this.cols = cols;
	}

	/**
	 * @return rows: the rows forming dashboard data.
	 */
	public List<RowsJson> getRows() {
		return rows;
	}

	/**
	 * @param rows
	 *            : the rows forming dashboard data.
	 */
	public void setRows(List<RowsJson> rows) {
		this.rows = rows;
	}

	/**
	 * This method allows add the column in the list the columns.
	 * 
	 * @param cols
	 *            : list to columns.
	 * @param label
	 *            : name the column to add.
	 * @param type
	 *            : type the column.
	 */
	public static void addCol(List<ColsJson> cols, String label, String type) {
		ColsJson tdc = new ColsJson();
		tdc.setLabel(label);
		tdc.setType(type);
		cols.add(tdc);
	}

	/**
	 * This method allows add the cell in the list the cells.
	 * 
	 * @param cl
	 *            : list to cells.
	 * @param value
	 *            : name the cell to add.
	 */
	public static void addCel(List<CellJson> cl, Object value) {
		CellJson cell = new CellJson();
		cell.setV(value);
		cl.add(cell);
	}

}
