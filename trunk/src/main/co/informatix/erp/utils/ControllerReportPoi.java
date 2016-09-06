package co.informatix.erp.utils;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPalette;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;

/**
 * This class handles the logic to create reports in excel by POI
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ControllerReportPoi implements Serializable {

	private HSSFWorkbook workBook;
	private HSSFSheet sheet;
	private HSSFFont fontGeneralStyle;
	private HSSFPalette palette;
	private HSSFCellStyle generalStyle;

	/**
	 * @return workBook: Workbook of excel.
	 */
	public HSSFWorkbook getWorkBook() {
		return workBook;
	}

	/**
	 * @param workBook
	 *            : Workbook of excel.
	 */
	public void setLibro(HSSFWorkbook workBook) {
		this.workBook = workBook;
	}

	/**
	 * @return sheet: Sheet associated to workbook.
	 */
	public HSSFSheet getSheet() {
		return sheet;
	}

	/**
	 * @param sheet
	 *            : Sheet associated to workbook.
	 */
	public void setSheet(HSSFSheet sheet) {
		this.sheet = sheet;
	}

	/**
	 * @return fontGeneralStyle: Font for general style
	 */
	public HSSFFont getFontGeneralStyle() {
		return fontGeneralStyle;
	}

	/**
	 * @param fontGeneralStyle
	 *            : Font for general style
	 */
	public void setFontGeneralStyle(HSSFFont fontGeneralStyle) {
		this.fontGeneralStyle = fontGeneralStyle;
	}

	/**
	 * @return palette: Palette associated to workbook.
	 */
	public HSSFPalette getPalette() {
		return palette;
	}

	/**
	 * @param palette
	 *            : Palette associated to workbook.
	 */
	public void setPalette(HSSFPalette palette) {
		this.palette = palette;
	}

	/**
	 * @return generalStyle: General cell style
	 */
	public HSSFCellStyle getGeneralStyle() {
		return generalStyle;
	}

	/**
	 * @param generalStyle
	 *            : General cell style
	 */
	public void setGeneralStyle(HSSFCellStyle generalStyle) {
		this.generalStyle = generalStyle;
	}

	/**
	 * Initialize variables and assign attributes to general style
	 * 
	 */
	public void loadStylesXls() {
		workBook = new HSSFWorkbook();
		sheet = workBook.createSheet();
		palette = workBook.getCustomPalette();

		fontGeneralStyle = workBook.createFont();
		fontGeneralStyle.setFontHeightInPoints((short) 24);
		fontGeneralStyle.setFontName(Constantes.FONT_COOPER_BLACK);
		fontGeneralStyle.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		fontGeneralStyle.setColor(HSSFColor.BLACK.index);

		generalStyle = workBook.createCellStyle();
		generalStyle.setWrapText(true);
		generalStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
		generalStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		generalStyle.setFont(fontGeneralStyle);
	}

	/**
	 * 
	 * This method change cell style.
	 * 
	 * @param cellStyle
	 *            : Cell style poi.
	 * @param fontColor
	 *            : String where each rgb value is separated for comma.
	 * @param fontType
	 *            : String with font type name.
	 * @param size
	 *            : Number of size.
	 * @param bold
	 *            : Its a boolean if this is 'true' font is bold.
	 * @param backgroundColor
	 *            : String where each rgb value is separated for comma.
	 * @param border
	 *            : String with the letters 'LRTB' (L=Left, R=Right, T=Top,
	 *            B=bottom) to put border cell.
	 * @param align
	 *            : Align text
	 */
	public void changeCellStyle(CellStyle cellStyle, String fontColor,
			String fontType, int size, boolean bold, String backgroundColor,
			String border, String align) {
		HSSFFont font = this.workBook.createFont();
		font.setFontName(HSSFFont.FONT_ARIAL);
		if (Constantes.FONT_CALIBRI.equals(fontType)
				|| Constantes.FONT_COOPER_BLACK.equals(fontType)) {
			font.setFontName(fontType);
		}
		alignText(cellStyle, align);
		Integer[] rgbFont = colorRGB(fontColor);
		HSSFColor colorFont = palette.findSimilarColor(rgbFont[0], rgbFont[1],
				rgbFont[2]);
		font.setFontHeightInPoints((short) size);
		font.setBoldweight(bold ? HSSFFont.BOLDWEIGHT_BOLD
				: HSSFFont.BOLDWEIGHT_NORMAL);
		font.setColor(colorFont.getIndex());

		if (backgroundColor != null) {
			cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			Integer[] rgbBackground = colorRGB(backgroundColor);
			HSSFColor colorBackground = palette.findSimilarColor(
					rgbBackground[0], rgbBackground[1], rgbBackground[2]);
			cellStyle.setFillForegroundColor(colorBackground.getIndex());
		}

		borderCell(cellStyle, border);
		cellStyle.setFont(font);
	}

	/**
	 * Divide a comma-separated and returns the first three integers in a string
	 * arrangement, the first three numbers correspond to the amount of color in
	 * the pattern rgb.
	 * 
	 * @param color
	 *            : String separated for comma.
	 * @return Integer[]: Array with values in rgb.
	 */
	private Integer[] colorRGB(String color) {
		int cont = 0;
		Integer[] rgbListColor = new Integer[3];
		String[] rgbList = color.split(",");
		if (rgbList.length > 2)
			for (String rgb : rgbList) {
				if (ValidacionesAction.isNumber(rgb)) {
					rgbListColor[cont] = Integer.parseInt(rgb);
					cont++;
				}
			}
		if (rgbListColor.length < 3) {
			rgbListColor[0] = 0;
			rgbListColor[1] = 0;
			rgbListColor[2] = 0;
		}
		return rgbListColor;
	}

	/**
	 * Assign the alignment to cell
	 * 
	 * @param cellStyle
	 *            : Cell style
	 * @param align
	 *            : Type alignment, example: LEFT, CENTER, RIGHT.
	 */
	private void alignText(CellStyle cellStyle, String align) {
		if (Constantes.LEFT.equals(align)) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_LEFT);
		}

		if (Constantes.RIGHT.equals(align)) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_RIGHT);
		}

		if (Constantes.CENTER.equals(align)) {
			cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		}
	}

	/**
	 * Assign border to cell style, the edge according to the last chain, for
	 * example the string "LR" is assigned to the edge of the left and right are
	 * assigned.
	 * 
	 * @param cellStyle
	 *            : Cell style.
	 * @param sides
	 *            : String with sides for border.
	 */
	private void borderCell(CellStyle cellStyle, String sides) {
		if (sides != null) {
			String[] sideList = sides.split("");
			for (String side : sideList) {
				if (Constantes.LEFT.equals(side)) {
					cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
					cellStyle.setLeftBorderColor((short) 8);
				}

				if (Constantes.RIGHT.equals(side)) {
					cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
					cellStyle.setRightBorderColor((short) 8);
				}

				if (Constantes.TOP.equals(side)) {
					cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
					cellStyle.setTopBorderColor((short) 8);
				}

				if (Constantes.BOTTOM.equals(side)) {
					cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
					cellStyle.setBottomBorderColor((short) 8);
				}
			}
		}
	}

	/**
	 * Method that allows a clone of a style, a style receives as Parameter and
	 * returns a new style with the same characteristics.
	 * 
	 * @param originalStyle
	 *            : Style for make clon
	 * @return newStyle: Clon style
	 */
	public CellStyle clonStylePoi(CellStyle originalStyle) {
		CellStyle newStyle = workBook.createCellStyle();
		newStyle.cloneStyleFrom(originalStyle);
		return newStyle;
	}
}
