package co.informatix.erp.utils;

import java.awt.Color;
import java.io.Serializable;

import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * This class contains general methods used for the generation of reports in
 * Excel (.xlsx)
 * 
 * @author william.garnica
 */
@SuppressWarnings("serial")
public class ControladorGenericoApachePOI implements Serializable {

	/**
	 * Method that creates a basic document XSSFCellStyle to cells Excel (.xlsx)
	 * 
	 * @param libro
	 *            :HSSFWorkbook from which creates the HSSFCellStyle
	 * @return XSSFCellStyle: style created
	 */
	public static XSSFCellStyle crearCeldaFormatoBasico(XSSFWorkbook libro) {
		XSSFCellStyle cellStyle = libro.createCellStyle();
		XSSFFont fontTitulo = libro.createFont();
		cellStyle.setFont(fontTitulo);
		cellStyle.setWrapText(true);
		cellStyle.setShrinkToFit(true);
		return cellStyle;
	}

	/**
	 * Method that receives a cell style (XSSFCellStyle) Excel and adds
	 * characteristics table title
	 * 
	 * @param cellStyle
	 *            : XSSFCellStyle to which are added the features
	 */
	public static void cellStyleTituloDeTabla(XSSFCellStyle cellStyle) {
		cellStyle.getFont().setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		cellStyle.getFont().setColor(IndexedColors.WHITE.getIndex());
		cellStyle.setAlignment(XSSFCellStyle.ALIGN_CENTER);
		cellStyleCeldaTabla(cellStyle);
		cellStyleColorCelda(cellStyle, Color.decode("#8f002a"));
	}

	/**
	 * Method that receives a cell style (XSSFCellStyle) excel and are added
	 * features of a table cell
	 * 
	 * @param cellStyle
	 *            : XSSFCellStyle to which are added the features
	 */
	public static void cellStyleCeldaTabla(XSSFCellStyle cellStyle) {
		cellStyle.setVerticalAlignment(XSSFCellStyle.VERTICAL_CENTER);
		cellStyle.setBorderBottom(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderLeft(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderRight(XSSFCellStyle.BORDER_THIN);
		cellStyle.setBorderTop(XSSFCellStyle.BORDER_THIN);
	}

	/**
	 * Method that receives a cell style (XSSFCellStyle) excel and add a
	 * background color
	 * 
	 * @param cellStyle
	 *            : XSSFCellStyle to which are added the features
	 * @param color
	 *            : color to get to the cell
	 */
	public static void cellStyleColorCelda(XSSFCellStyle cellStyle, Color color) {
		cellStyle.setFillPattern(XSSFCellStyle.SOLID_FOREGROUND);
		cellStyle.setFillForegroundColor(new XSSFColor(color));
	}

	/**
	 * Method that receives a cell style (HSSFCellStyle) Excel features are
	 * added and size, bold and orientation (inclination)
	 * 
	 * @param cellStyle
	 *            : XSSFCellStyle to which are added the features
	 * @param tamanyoFuente
	 *            : font size of the cell
	 * @param usarNegrita
	 *            : boolean indicating whether bold (BOLD) or not used
	 * @param orientacionEnGrados
	 *            : orientation or inclination in degrees (0: Horizontal, 90:
	 *            vertical)
	 */
	public static void cellStyleTamanyoNegritaOrientacionFuente(
			XSSFCellStyle cellStyle, int tamanyoFuente, boolean usarNegrita,
			short orientacionEnGrados) {
		if (usarNegrita == true) {
			cellStyle.getFont().setBoldweight(XSSFFont.BOLDWEIGHT_BOLD);
		} else if (usarNegrita == false) {
			cellStyle.getFont().setBoldweight(XSSFFont.BOLDWEIGHT_NORMAL);
		}
		if (tamanyoFuente > 0) {
			cellStyle.getFont().setFontHeightInPoints((short) tamanyoFuente);
		}
		cellStyle.setRotation(orientacionEnGrados);
	}
}
