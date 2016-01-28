package co.informatix.erp.utils;

import java.io.InputStream;
import java.io.Serializable;
import java.util.Properties;

/**
 * This class can read the file properties PrototipoCostos.properties
 * 
 * @author marisol.calderon
 * @modify 07/21/2015 Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
public class PropertiesManager implements Serializable {

	protected static Properties systemParameters;

	static {
		try {
			/* Parameters system variable is initialized */
			InputStream systemParametersFile = PropertiesManager.class
					.getClassLoader().getResourceAsStream(
							"PrototipoCostos.properties");
			systemParameters = new Properties();
			systemParameters.load(systemParametersFile);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method gets the current value of the property Inag.properties file.
	 * This file must be located in the application classpath
	 * 
	 * @param property
	 *            : property you want to search
	 * @return the value of the property; null if not found
	 */
	public static String getProperty(String property) {
		return PropertiesManager.getProperty(property, null);
	}

	/**
	 * This method gets the current value of the property Inag.properties file,
	 * assigning a default value should not be found. This file must be located
	 * in the application classpath
	 * 
	 * @param property
	 *            : property you want to search
	 * @param defaultValue
	 *            : the default value for the property
	 * @return the value of the property; Default value if not found
	 */
	public static String getProperty(String property, String defaultValue) {
		return systemParameters.getProperty(property, defaultValue);
	}

}
