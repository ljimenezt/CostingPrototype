package co.informatix.erp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.ResourceBundle;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import co.informatix.security.utils.Parametros;

/**
 * Esta clase permite realizar la logica del negocio para los metodos genericos
 * que se necesitan en el sistema.
 * 
 * @author william.garnica
 */
@SuppressWarnings("serial")
public class ControladorGenerico implements Serializable {

	/**
	 * Metodo que permite visualizar un mensaje de FacesMessage.SEVERITY_INFO en
	 * la interfaz del usuario
	 * 
	 * @param formAMostrarMensaje
	 *            : form en donde se visualizara el mensaje
	 * @param mensajeAMostrar
	 *            : mensaje a visualizar
	 */
	public static void mensajeInformacion(String formAMostrarMensaje,
			String mensajeAMostrar) {
		FacesContext contexto = FacesContext.getCurrentInstance();

		if (formAMostrarMensaje != null && formAMostrarMensaje.equals("")) {
			formAMostrarMensaje = null;
		}
		contexto.addMessage(formAMostrarMensaje, new FacesMessage(
				FacesMessage.SEVERITY_INFO, mensajeAMostrar, mensajeAMostrar));
	}

	/**
	 * Metodo que cuando ocurre un error guarda el error en el log de la
	 * aplicacion y visualiza el mensaje de FacesMessage.SEVERITY_ERROR en la
	 * interfaz del usuario
	 * 
	 * @param e
	 *            : Exception a guardar en el log
	 * @param formAMostrarMensaje
	 *            : form en donde se visualizara el mensaje
	 * @param mensajeAMostrar
	 *            : mensaje a visualizar
	 */
	public static void mensajeError(Exception e, String formAMostrarMensaje,
			String mensajeAMostrar) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		ResourceBundle bundle = contexto.getApplication().getResourceBundle(
				contexto, "mensaje");

		if (mensajeAMostrar == null || mensajeAMostrar.equals("")) {
			mensajeAMostrar = bundle.getString("message_error");
		}
		if (formAMostrarMensaje != null && formAMostrarMensaje.equals("")) {
			formAMostrarMensaje = null;
		}
		if (e != null) {
			Parametros.getLog().error(
					Parametros.getExceptionStackTraceAsString(e));
			e.printStackTrace();
		}
		contexto.addMessage(formAMostrarMensaje, new FacesMessage(
				FacesMessage.SEVERITY_ERROR, mensajeAMostrar, mensajeAMostrar));
	}

	/**
	 * Metodo que obtiene el contexto de un Bean dependiendo de la Class enviada
	 * como parametro.
	 * 
	 * <p>
	 * ejemplo de uso:
	 * <p>
	 * Objet object = (Object)ControladorGenerico.getContextBean(Object.class);
	 * <p>
	 * donde Object es el objeto a obtener el contexto
	 * 
	 * @param objectClass
	 *            : clase del objeto a obtener el contexto
	 * @return: Contexto del objeto deseado
	 */
	@SuppressWarnings("rawtypes")
	public static Object getContextBean(Class objectClass) {
		FacesContext contexto = FacesContext.getCurrentInstance();

		/* Se pasa la primera letra del nombre del Bean a minuscula */
		String nombreBean = objectClass.getSimpleName();
		String primeraLetra = "" + nombreBean.charAt(0);
		nombreBean = "" + primeraLetra.toLowerCase() + nombreBean.substring(1);

		Object object = contexto.getApplication().getELResolver()
				.getValue(contexto.getELContext(), null, nombreBean);
		return object;
	}

	/**
	 * Metodo que genera un Bundle a partir de un parametro enviado
	 * 
	 * @param keyBundle
	 *            : String necesario para la generacion del bundle
	 * @return Objeto ResourceBundle generado
	 */
	public static ResourceBundle getBundle(String keyBundle) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		ResourceBundle bundle = contexto.getApplication().getResourceBundle(
				contexto, keyBundle);
		return bundle;
	}

	/**
	 * Este metodo permite retornar el parametro a travez del contexto.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param param
	 *            : param que se quiere consultar.
	 * @return valor del paremtro enviado.
	 */
	public static String getParam(String param) {
		FacesContext contexto = FacesContext.getCurrentInstance();
		String valor = contexto.getExternalContext().getRequestParameterMap()
				.get(param);
		return valor;
	}

	/**
	 * Este metodo permite al usuario descargar un archivo ubicado en la ruta
	 * que recibe como parametro
	 * 
	 * @param URL
	 *            Ruta del archivo a descargar
	 * 
	 * @param tipoArchivo
	 *            Indica la extension del archivo (pdf, xlsx)
	 * 
	 * @throws IOException
	 */
	public static void descargarFormato(String URL, String tipoArchivo)
			throws IOException {
		File reporte = new File(URL);
		FacesContext ctx = FacesContext.getCurrentInstance();
		FileInputStream fis = new FileInputStream(reporte);
		byte[] bytes = new byte[1000];
		int read = 0;
		if (!ctx.getResponseComplete()) {
			String fileName = reporte.getName();
			String contentType;
			if (tipoArchivo.equals("pdf")) {
				contentType = "application/pdf";
			} else {
				contentType = "application/vnd.ms-excel";
			}
			HttpServletResponse response = (HttpServletResponse) ctx
					.getExternalContext().getResponse();
			response.setContentType(contentType);
			response.setHeader("Content-Disposition", "attachment;filename=\""
					+ fileName + "\"");
			ServletOutputStream out = response.getOutputStream();
			while ((read = fis.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			out.flush();
			out.close();
			fis.close();
			ctx.responseComplete();
		}
	}

	/**
	 * Metodo que toma una imagen y la retorna lista para cargar en el archivo
	 * de excel
	 * 
	 * @param path
	 *            : Ruta de la imagen a cargar en el reporte
	 * @param wb
	 *            : libro donde se agregara la imagen referenciada
	 * @return int: Retorna un valor diferente de cero cuando el procedimiento
	 *         se ejecute correctamente
	 * @throws IOException
	 */
	public static int cargarImagen(File path, XSSFWorkbook wb)
			throws IOException {
		int pictureIndex;
		FileInputStream fis = null;
		ByteArrayOutputStream bos = null;
		try {
			fis = new FileInputStream(path);
			bos = new ByteArrayOutputStream();
			int c;
			while ((c = fis.read()) != -1)
				bos.write(c);
			pictureIndex = wb.addPicture(bos.toByteArray(),
					XSSFWorkbook.PICTURE_TYPE_PNG);
		} finally {
			if (fis != null)
				fis.close();
			if (bos != null)
				bos.close();
		}
		return pictureIndex;
	}
}
