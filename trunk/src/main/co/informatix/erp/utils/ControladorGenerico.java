package co.informatix.erp.utils;

import java.io.Serializable;

import javax.el.ELContext;
import javax.el.ExpressionFactory;
import javax.el.ValueExpression;
import javax.faces.application.Application;

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

	/**
	 * This method allows to form the expression in the parameter name allowing
	 * of internationalization.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param name
	 *            : name of the variable to format in the expression. * @param
	 * @param name2
	 *            : second name of the variable to format in the expression.
	 * @return The value expression with formatting.
	 */
	public static ValueExpression getValueExpression(String name, String name2) {
		Application app = ControladorContexto.getApplication();
		ELContext elContext = ControladorContexto.getELContext();
		ExpressionFactory factory = app.getExpressionFactory();
		ValueExpression ve = null;
		try {
			boolean flag = name2 == null ? false : true;
			String arg1 = "#{" + name + "} ";
			String arg2 = "#{" + name2 + "} ";
			String args = flag ? arg1 + arg2 : arg1;
			ve = factory.createValueExpression(elContext, args, String.class);
		} catch (Exception e) {
			ve = null;
		}
		return ve;
	}
}