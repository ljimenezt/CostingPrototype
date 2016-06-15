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
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ControladorContable implements Serializable {

	public static int escalaDecimal = 2;
	public static int escalaAsignada;

	/**
	 * Method that allows make the division operation with BigDecimal values to
	 * get more accurate estimates.
	 * 
	 * @param valor1
	 *            : value of the dividend.
	 * @param valor2
	 *            : divider value.
	 * 
	 * @return division as a result of double value.
	 */
	public static double dividir(Double valor1, Double valor2) {
		int escala = escalaDecimal;
		BigDecimal result = new BigDecimal("0");
		if (valor2 != 0) {
			BigDecimal v1 = new BigDecimal(String.valueOf(valor1));
			BigDecimal v2 = new BigDecimal(String.valueOf(valor2));
			if (escalaAsignada != 0) {
				escala = escalaAsignada;
			}
			result = v1.divide(v2, escala, RoundingMode.HALF_UP);
			result.setScale(escala, RoundingMode.HALF_UP);
		}
		return result.doubleValue();
	}

}