package co.informatix.erp.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * This class allows business logic for generic accounting methods that are
 * needed in the system.
 * 
 * @author Luz.Jaimes
 * @modify Liseth.Jimenez 18/07/2016
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ControllerAccounting implements Serializable {

	public static int decimalScale = 2;
	public static int assignedScale;

	/**
	 * Method that allows make the division operation with BigDecimal values to
	 * get more accurate estimates.
	 * 
	 * @param value1
	 *            : value of the dividend.
	 * @param value2
	 *            : divider value.
	 * 
	 * @return division as a result of double value.
	 */
	public static double divide(Double value1, Double value2) {
		int scale = decimalScale;
		BigDecimal result = new BigDecimal("0");
		if (value2 != 0) {
			BigDecimal v1 = new BigDecimal(String.valueOf(value1));
			BigDecimal v2 = new BigDecimal(String.valueOf(value2));
			if (assignedScale != 0) {
				scale = assignedScale;
			}
			result = v1.divide(v2, scale, RoundingMode.HALF_UP);
			result.setScale(scale, RoundingMode.HALF_UP);
		}
		return result.doubleValue();
	}

	/**
	 * Method that allows the addition operation with BigDecimal values to get
	 * more accurate estimates.
	 * 
	 * @param value1
	 *            : First value to add.
	 * @param value2
	 *            : Second value to add.
	 * @return result of the addition as a double value.
	 */
	public static double add(Double value1, Double value2) {
		int scale = decimalScale;
		BigDecimal result = new BigDecimal("0");
		BigDecimal v1 = new BigDecimal(String.valueOf(value1));
		BigDecimal v2 = new BigDecimal(String.valueOf(value2));
		result = v1.add(v2);
		if (assignedScale != 0) {
			scale = assignedScale;
		}
		result.setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * Method that allows the operation of subtraction BigDecimal values to get
	 * more accurate estimates.
	 * 
	 * @param value1
	 *            : First value to subtract.
	 * @param value2
	 *            : Second value to subtract.
	 * @return result of subtraction as double value.
	 */
	public static double subtract(Double value1, Double value2) {
		int scale = decimalScale;
		BigDecimal result = new BigDecimal("0");
		BigDecimal v1 = new BigDecimal(String.valueOf(value1));
		BigDecimal v2 = new BigDecimal(String.valueOf(value2));
		result = v1.subtract(v2);
		if (assignedScale != 0) {
			scale = assignedScale;
		}
		result.setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * Method that allows the multiplication operation with BigDecimal values to
	 * get more precise estimates.
	 * 
	 * @param value1
	 *            : First value to multiply.
	 * @param value2
	 *            : Second value to multiply.
	 * @return result of the multiplication value double.
	 */
	public static double multiply(Double value1, Double value2) {
		int scale = decimalScale;
		BigDecimal result = new BigDecimal("0");
		BigDecimal v1 = new BigDecimal(String.valueOf(value1));
		BigDecimal v2 = new BigDecimal(String.valueOf(value2));
		result = v1.multiply(v2);
		if (assignedScale != 0) {
			scale = assignedScale;
		}
		result.setScale(scale, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * Method to round a number according to the digits sent as a parameter.
	 * 
	 * @param number
	 *            : double number you want to round.
	 * @param digits
	 *            : number of decimal places to round.
	 * @return rounded number.
	 */
	public static Double round(Double number, int digits) {
		return new BigDecimal(number.toString()).setScale(digits,
				RoundingMode.HALF_UP).doubleValue();
	}
}