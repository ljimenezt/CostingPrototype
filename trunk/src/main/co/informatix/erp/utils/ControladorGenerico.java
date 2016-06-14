package co.informatix.erp.utils;

import java.io.Serializable;

import org.apache.commons.io.FilenameUtils;

/**
 * Esta clase permite realizar la logica del negocio para los metodos genericos
 * que se necesitan en el sistema.
 * 
 * @author william.garnica
 */
@SuppressWarnings("serial")
public class ControladorGenerico implements Serializable {

	/**
	 * This method allows you to consult the route prefix 'C: /' windows server
	 * or '/' in unix and concatenated with the path to the local folder for the
	 * file properties PrototipoCostos.properties .
	 * 
	 * @author marisol.calderon
	 * 
	 * @return path to the local system folder.
	 */
	public static String LOCAL_PATH() {
		String path = FilenameUtils.getPrefix(Utils.RUTA_SERVIDOR())
				+ PropertiesManager.getProperty("locate.path.local");
		return path;
	}
}