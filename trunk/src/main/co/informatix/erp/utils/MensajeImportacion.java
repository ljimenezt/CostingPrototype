package co.informatix.erp.utils;

import java.io.Serializable;
import java.text.MessageFormat;

import org.apache.commons.lang3.StringUtils;

/**
 * Allows management of the error messages in the import file.
 * 
 * @author Luz.Jaimes
 * 
 */
@SuppressWarnings("serial")
public class MensajeImportacion implements Serializable {
	private String mensaje;
	private String tipo;
	private String celda;
	private String campo;

	/**
	 * Builder messages.
	 * 
	 * @param columna
	 *            : Column in which there is an error.
	 * @param fila
	 *            : Row in which there is an error.
	 * @param bundleCampo
	 *            : Bundle the field with the error.
	 * @param labelCampo
	 *            : Name field with the error.
	 * @param bundleMensaje
	 *            : Bundle error message.
	 * @param labelMensaje
	 *            : Error message.
	 */
	public MensajeImportacion(int columna, int fila, String bundleCampo,
			String labelCampo, String bundleMensaje, String labelMensaje) {
		String campo = ControladorContexto.getBundle(bundleCampo).getString(
				labelCampo);
		String mensaje = ControladorContexto.getBundle(bundleMensaje)
				.getString(labelMensaje);
		setCelda(nombreCelda(columna, fila));
		setCampo(StringUtils.capitalize(campo));
		setMensaje(StringUtils.capitalize(mensaje));
	}

	/**
	 * Builder messages.
	 * 
	 * @param columna
	 *            : Column in which there is an error.
	 * @param fila
	 *            : Row in which there is an error.
	 * @param bundleCampo
	 *            : Bundle the field with the error.
	 * @param labelCampo
	 *            : Name field with the error.
	 * @param bundleMensaje
	 *            : Bundle error message.
	 * @param labelMensaje
	 *            : Error message.
	 */
	public MensajeImportacion(int columna, int fila, String bundleCampo,
			String labelCampo, String bundleMensaje, String labelMensaje,
			Object... arguments) {
		String campo = ControladorContexto.getBundle(bundleCampo).getString(
				labelCampo);
		String mensaje = MessageFormat.format(
				ControladorContexto.getBundle(bundleMensaje).getString(
						labelMensaje), arguments);
		setCelda(nombreCelda(columna, fila));
		setCampo(StringUtils.capitalize(campo));
		setMensaje(StringUtils.capitalize(mensaje));
	}

	/**
	 * Builder messages with message type.
	 * 
	 * @param celda
	 *            : Name of the cell in which there is an error.
	 * @param tipo
	 *            : Type the message.
	 * @param campo
	 *            : Name field with the error.
	 * @param mensaje
	 *            : Error message.
	 */
	public MensajeImportacion(String celda, String tipo, String campo,
			String mensaje) {
		setCelda(celda);
		setTipo(tipo);
		setCampo(campo);
		setMensaje(mensaje);
	}

	/**
	 * 
	 * @return mensaje: Text of the message.
	 */
	public String getMensaje() {
		return mensaje;
	}

	/**
	 * 
	 * @param mensaje
	 *            : Text of the message.
	 */
	public void setMensaje(String mensaje) {
		this.mensaje = mensaje;
	}

	/**
	 * 
	 * @return tipo: Message type.
	 */
	public String getTipo() {
		return tipo;
	}

	/**
	 * 
	 * @param tipo
	 *            : Message type.
	 */
	public void setTipo(String tipo) {
		this.tipo = tipo;
	}

	/**
	 * 
	 * @return celda: Cell error.
	 */
	public String getCelda() {
		return celda;
	}

	/**
	 * 
	 * @param celda
	 *            : Cell error.
	 */
	public void setCelda(String celda) {
		this.celda = celda;
	}

	/**
	 * 
	 * @return campo: Name field error.
	 */
	public String getCampo() {
		return campo;
	}

	/**
	 * 
	 * @param campo
	 *            : Name field error.
	 */
	public void setCampo(String campo) {
		this.campo = campo;
	}

	/**
	 * allows get the name of the cell in excel giving the number of the row and
	 * column.
	 * 
	 * @param columna
	 *            : Column value.
	 * @param fila
	 *            : Row value.
	 * @return nombreCelda: Name of the cell.
	 */
	private String nombreCelda(int columna, int fila) {
		String[] abecedario = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
				"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
				"V", "W", "X", "Y", "Z" };
		columna = columna - 1;
		double val = columna / abecedario.length;
		int pos = (int) val;
		int col = columna - (26 * pos);
		String nombreCelda = "";
		if (pos > 0) {
			nombreCelda = abecedario[pos - 1];
		}
		nombreCelda += abecedario[col];
		return nombreCelda + fila;
	}

}
