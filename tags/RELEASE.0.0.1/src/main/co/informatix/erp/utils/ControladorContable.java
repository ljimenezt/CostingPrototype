package co.informatix.erp.utils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ResourceBundle;
import java.util.regex.Pattern;

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
	private static final String[] UNIDADES = { "", "un ", "dos ", "tres ",
			"cuatro ", "cinco ", "seis ", "siete ", "ocho ", "nueve " };
	private static final String[] DECENAS = { "diez ", "once ", "doce ",
			"trece ", "catorce ", "quince ", "dieciseis ", "diecisiete ",
			"dieciocho ", "diecinueve", "veinte ", "treinta ", "cuarenta ",
			"cincuenta ", "sesenta ", "setenta ", "ochenta ", "noventa ",
			"veinti" };
	private static final String[] CENTENAS = { "", "ciento ", "doscientos ",
			"trecientos ", "cuatrocientos ", "quinientos ", "seiscientos ",
			"setecientos ", "ochocientos ", "novecientos " };

	/**
	 * Method to round a number according to the digits sent as parameter.
	 * 
	 * @param numero
	 *            : double number you want to round.
	 * @param digitos
	 *            : number of decimal places to round.
	 * @return rounded number.
	 */
	public static Double redondear(Double numero, int digitos) {
		return new BigDecimal(numero.toString()).setScale(digitos,
				RoundingMode.HALF_UP).doubleValue();
	}

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

	/**
	 * Method that allows make the multiplication operation with BigDecimal
	 * values to get more accurate estimates.
	 * 
	 * @param valor1
	 *            : first value to multiply.
	 * @param valor2
	 *            : second value to multiply.
	 * @return multiplication result as double value.
	 */
	public static double multiplicar(Double valor1, Double valor2) {
		int escala = escalaDecimal;
		BigDecimal result = new BigDecimal("0");
		BigDecimal v1 = new BigDecimal(String.valueOf(valor1));
		BigDecimal v2 = new BigDecimal(String.valueOf(valor2));
		result = v1.multiply(v2);
		if (escalaAsignada != 0) {
			escala = escalaAsignada;
		}
		result.setScale(escala, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * Allows method that make the addition operation with BigDecimal values to
	 * get more accurate estimates.
	 * 
	 * @param valor1
	 *            : First value to add.
	 * @param valor2
	 *            : second value to add.
	 * @return the sum as a result of double value.
	 */
	public static double sumar(Double valor1, Double valor2) {
		int escala = escalaDecimal;
		BigDecimal result = new BigDecimal("0");
		BigDecimal v1 = new BigDecimal(String.valueOf(valor1));
		BigDecimal v2 = new BigDecimal(String.valueOf(valor2));
		result = v1.add(v2);
		if (escalaAsignada != 0) {
			escala = escalaAsignada;
		}
		result.setScale(escala, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * allows method that perform subtraction with BigDecimal values to get more
	 * accurate estimates.
	 * 
	 * @param valor1
	 *            : first value to subtract.
	 * @param valor2
	 *            : second value to subtract.
	 * @return result of subtraction as double value.
	 */
	public static double restar(Double valor1, Double valor2) {
		int escala = escalaDecimal;
		BigDecimal result = new BigDecimal("0");
		BigDecimal v1 = new BigDecimal(String.valueOf(valor1));
		BigDecimal v2 = new BigDecimal(String.valueOf(valor2));
		result = v1.subtract(v2);
		if (escalaAsignada != 0) {
			escala = escalaAsignada;
		}
		result.setScale(escala, RoundingMode.HALF_UP);
		return result.doubleValue();
	}

	/**
	 * This method allows to receive input parameters to convert the numbers to
	 * letters.
	 * 
	 * @param numero
	 *            : string that brings the number to be converted to letters.
	 * @param mayusculas
	 *            : boolean indicating whether you want to present the result in
	 *            uppercase or lowercase.
	 * @return literal: string with the number in words.
	 */
	public static String convertirNumeroALetra(String numero, boolean mayusculas) {
		String literal = "";
		/* If the number used (,) instead of (.) -> Is replaced */
		numero = numero.replace(".", ",");
		/* If the number has no decimal part, he added, 00 */
		if (numero.indexOf(",") == -1) {
			numero = numero + ",00";
		}
		if (Pattern.matches("\\d{1,10},\\d{1,2}", numero)) {
			/* Divide the number 0000000.00 -> integer and decimal */
			String Num[] = numero.split(",");
			/* The number is converted to literal */
			if (Integer.parseInt(Num[0]) == 0) {/* If the value is zero */
				literal = "cero ";
			} else if (Integer.parseInt(Num[0]) > 999999) {/* If million */
				literal = getMillones(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 999) {/* If thousands */
				literal = getMiles(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 99) {/* If hundred */
				literal = getCentenas(Num[0]);
			} else if (Integer.parseInt(Num[0]) > 9) {/* If dozen */
				literal = getDecenas(Num[0]);
			} else {/* But units -> 9 */
				literal = getUnidades(Num[0]);
			}
			if (mayusculas) {
				return (literal).toUpperCase();
			} else {
				return (literal);
			}
		} else {/* Error, can not be converted */
			return literal = null;
		}
	}

	/**
	 * To convert the number between 1 and 9.
	 * 
	 * @param numero
	 *            : string that brings the number to be converted to letters.
	 * @return UNIDADES
	 */
	private static String getUnidades(String numero) {
		/* If had some 0 before the acquittal -> 9 or 09 = 009 = 9 */
		String num = numero.substring(numero.length() - 1);
		return UNIDADES[Integer.parseInt(num)];
	}

	/**
	 * It allows you to convert the numbers between 10 and 99.
	 * 
	 * @param num
	 *            : string that brings the number to be converted to letters.
	 * @return DECENAS
	 */
	private static String getDecenas(String num) {
		int n = Integer.parseInt(num);
		if (n < 10) {/* For cases like -> 01-09 */
			return getUnidades(num);
		} else if (n > 19) {/* For 20 ... 99 */
			String u = getUnidades(num);
			if (u.equals("")) { /* For 20,30,40,50,60,70,80,90 */
				return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8];
			} else if (n > 20 && n < 30) {
				return DECENAS[18] + u;
			} else {
				return DECENAS[Integer.parseInt(num.substring(0, 1)) + 8]
						+ "y " + u;
			}
		} else {
			return DECENAS[n - 10];
		}
	}

	/**
	 * This converts numbers between 100 and 999.
	 * 
	 * @param num
	 *            : string that brings the number to be converted to letters.
	 * @return CENTENAS
	 */
	private static String getCentenas(String num) {
		if (Integer.parseInt(num) > 99) {/* It hundred */
			if (Integer.parseInt(num) == 100) {/* Special case */
				return " cien ";
			} else {
				return CENTENAS[Integer.parseInt(num.substring(0, 1))]
						+ getDecenas(num.substring(1));
			}
		} else {
			return getDecenas(Integer.parseInt(num) + "");
		}
	}

	/**
	 * This converts the numbers of thousands.
	 * 
	 * @param numero
	 *            : string that brings the number to be converted to letters.
	 * @return string with the number in words.
	 */
	private static String getMiles(String numero) {
		/* gets the hundreds */
		String c = numero.substring(numero.length() - 3);
		/* Gets the thousands */
		String m = numero.substring(0, numero.length() - 3);
		String n = "";
		/* It is found that thousands have integer */
		if (Integer.parseInt(m) > 0) {
			n = getCentenas(m);
			return n + "mil " + getCentenas(c);
		} else {
			return "" + getCentenas(c);
		}
	}

	/**
	 * This converts the numbers of millions.
	 * 
	 * @param numero
	 *            : string that brings the number to be converted to letters.
	 * @return string with the number in words.
	 */
	private static String getMillones(String numero) {
		/* gets thousands */
		String miles = numero.substring(numero.length() - 6);
		/* gets the millions */
		String millon = numero.substring(0, numero.length() - 6);
		String n = "";
		if (millon.length() > 1 || !millon.equals("1")) {
			n = getCentenas(millon) + "millones ";
		} else {
			n = getUnidades(millon) + "millon ";
		}
		return n + getMiles(miles);
	}

	/**
	 * Method that allows the approach of a value to the next depending on the
	 * hundred thousand
	 * 
	 * @param valorAproximar
	 *            : value to be approximated
	 * 
	 * @return valorAproximar: Approximate value
	 * 
	 */
	public static double aproximacionMiles(double valorAproximar) {
		if (valorAproximar > 0) {
			double residuo = valorAproximar % 1000;
			if (residuo < 500)
				valorAproximar = ControladorContable.restar(valorAproximar,
						residuo);
			else {
				residuo = 1000 - residuo;
				valorAproximar = ControladorContable.sumar(valorAproximar,
						residuo);
			}
		}
		return valorAproximar;
	}

	/**
	 * Allows execute the corresponding operation of the operands of the
	 * formula.
	 * 
	 * @param sumaOperando
	 *            : Current value of the formula.
	 * @param operador
	 *            : Operator applied to the current value of the formula and the
	 *            value of the current operating
	 * @param valor
	 *            : The current operating value that operate with total current
	 *            formula.
	 * @return Value resulting from operating sumaOperando and indicated value
	 *         through sign (addition, subtraction, multiplication and
	 *         division).
	 * @throws Exception
	 */
	public static Double calcular(Double sumaOperando, String operador,
			Double valor) throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		if ("+".equals(operador)) {
			sumaOperando = ControladorContable.sumar(sumaOperando, valor);
		} else if ("-".equals(operador)) {
			sumaOperando = ControladorContable.restar(sumaOperando, valor);
		} else if ("*".equals(operador)) {
			sumaOperando = ControladorContable.multiplicar(sumaOperando, valor);
		} else if ("/".equals(operador)) {
			sumaOperando = ControladorContable.dividir(sumaOperando, valor);
		} else {
			throw new Exception(
					bundle.getString("message_signo_operador_no_existe"));
		}
		return sumaOperando;
	}

	/**
	 * Method that formats percentages in order to show them in the system views
	 * 
	 * @param porcentaje
	 *            : Decimal value that is passed as a parameter to be formatted
	 *            and returned.
	 * 
	 * @return Value formatted decimal established according to the method.
	 */
	public String formatoPorcentaje(Double porcentaje) {
		String resultado = "";
		try {
			DecimalFormat pf = new DecimalFormat("#0.00");
			resultado = pf.format(porcentaje * 100);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return resultado;
	}

}