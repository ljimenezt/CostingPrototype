package co.informatix.erp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.Serializable;

import javax.faces.context.FacesContext;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

/**
 * Esta clase permite realizar la logica del negocio para los metodos genericos
 * que se necesitan en el sistema.
 * 
 * @author william.garnica
 */
@SuppressWarnings("serial")
public class ControladorGenerico implements Serializable {

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

	/**
	 * Este metodo permite consultar el prefijo de la ruta del servidor 'C:/' en
	 * windows o '/' en unix y la concatena con la ruta de la carpeta local para
	 * el archivo de propiedades PrototipoCostos.properties.
	 * 
	 * @author marisol.calderon
	 * 
	 * @return ruta de la carpeta local del sistema.
	 */
	public static String RUTA_LOCAL() {
		String ruta = FilenameUtils.getPrefix(Utils.RUTA_SERVIDOR())
				+ PropertiesManagerUtils.getProperty("locate.carpeta.local");
		return ruta;
	}
}
