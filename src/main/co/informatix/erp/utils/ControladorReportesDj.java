package co.informatix.erp.utils;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ejb.Stateless;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.model.SelectItem;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.jasperreports.engine.JRDataSource;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRXlsExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimplePdfExporterConfiguration;
import net.sf.jasperreports.export.SimpleXlsReportConfiguration;
import ar.com.fdvs.dj.core.DynamicJasperHelper;
import ar.com.fdvs.dj.core.layout.ClassicLayoutManager;
import ar.com.fdvs.dj.core.layout.LayoutManager;
import ar.com.fdvs.dj.domain.DynamicReport;
import ar.com.fdvs.dj.domain.Style;
import ar.com.fdvs.dj.domain.builders.FastReportBuilder;
import ar.com.fdvs.dj.domain.constants.Font;
import ar.com.fdvs.dj.domain.constants.HorizontalAlign;
import ar.com.fdvs.dj.domain.constants.VerticalAlign;

/**
 * This class allows the generation of reports using Jasper Report so dynamic
 * that uses Dynamic libraries Jasper
 * 
 * @author Luz.Jaimes
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@Stateless
public class ControladorReportesDj implements Serializable {
	private Set<String> temporal;
	private List<SelectItem> opcionesExportar;
	private List<String> camposExportar;
	private List<String> camposExportarSinDuplicados;
	private String formato = "pdf";
	private String tipoPlantilla = "plantilla.jrxml";
	private JasperPrint jp;
	private JasperReport jr;
	private boolean esExogena = false;
	private String nombreFormExogena = "";
	private String carpetaReportes;
	private String cambiarRutaServidor;
	private String rutaReportesGlassfish = ConstantesErp.RUTA_UPLOADFILE_GLASFISH;

	/**
	 * @return opcionesExportar : list of fields to export options
	 */
	public Set<String> getTemporal() {
		return temporal;
	}

	/**
	 * @param opcionesExportar
	 *            : list of fields to export options
	 */
	public void setTemporal(Set<String> temporal) {
		this.temporal = temporal;
	}

	/**
	 * @return opcionesExportar : list of fields to export options
	 */
	public List<SelectItem> getOpcionesExportar() {
		return opcionesExportar;
	}

	/**
	 * @param opcionesExportar
	 *            : list of fields to export options
	 */
	public void setOpcionesExportar(List<SelectItem> opcionesExportar) {
		this.opcionesExportar = opcionesExportar;
	}

	/**
	 * @return camposExportar : user selected fields to export
	 */
	public List<String> getCamposExportar() {
		return camposExportar;
	}

	/**
	 * @param camposExportar
	 *            : user selected fields to export
	 */
	public void setCamposExportar(List<String> camposExportar) {
		this.camposExportar = camposExportar;
	}

	/**
	 * @return camposExportarSinDuplicados: selected by the user, without
	 *         duplicate fields to export
	 */
	public List<String> getCamposExportarSinDuplicados() {
		return camposExportarSinDuplicados;
	}

	/**
	 * @param camposExportarSinDuplicados
	 *            :selected by the user, without duplicate fields to export
	 */
	public void setCamposExportarSinDuplicados(
			List<String> camposExportarSinDuplicados) {

		if (this.camposExportarSinDuplicados == null
				|| camposExportarSinDuplicados == null) {
			this.camposExportarSinDuplicados = camposExportarSinDuplicados;

		} else {
			if (!this.camposExportarSinDuplicados.isEmpty()
					&& !camposExportarSinDuplicados.isEmpty()) {
				List<String> camposSeleccionados = new ArrayList<String>();
				for (SelectItem op : opcionesExportar)
					if (this.camposExportarSinDuplicados.contains((String) op
							.getValue()))
						camposSeleccionados.add((String) op.getValue());
				this.camposExportarSinDuplicados.removeAll(camposSeleccionados);

			}
			if (!this.camposExportarSinDuplicados
					.containsAll(camposExportarSinDuplicados)
					|| this.camposExportarSinDuplicados.isEmpty()) {
				camposExportarSinDuplicados
						.removeAll(this.camposExportarSinDuplicados);
				this.camposExportarSinDuplicados
						.addAll(camposExportarSinDuplicados);
			}
		}
	}

	/**
	 * @return formato : Format in which you want to export the report 'Excel'
	 *         or 'PDF'
	 */
	public String getFormato() {
		return formato;
	}

	/**
	 * @param formato
	 *            : Format in which you want to export the report 'Excel' or
	 *            'PDF'
	 */
	public void setFormato(String formato) {
		this.formato = formato;
	}

	/**
	 * @return TipoPlantilla : Type template to generate the report
	 * 
	 */
	public String getTipoPlantilla() {
		return tipoPlantilla;
	}

	/**
	 * @param tipoPlantilla
	 *            : Type template to generate the report
	 */
	public void setTipoPlantilla(String tipoPlantilla) {
		this.tipoPlantilla = tipoPlantilla;
	}

	/**
	 * @return jp: JasperPrint object used to generate the report
	 */
	public JasperPrint getJp() {
		return jp;
	}

	/**
	 * @param jp
	 *            : JasperPrint object used to generate the report
	 */
	public void setJp(JasperPrint jp) {
		this.jp = jp;
	}

	/**
	 * @return jr: JasperReport object used to generate the report
	 */
	public JasperReport getJr() {
		return jr;
	}

	/**
	 * @param jr
	 *            : JasperReport object used to generate the report
	 */
	public void setJr(JasperReport jr) {
		this.jr = jr;
	}

	/**
	 * @return esExogena: Variable that allows me to determine whether the
	 *         format will generate is an exogenous format to set the name.
	 */
	public boolean isEsExogena() {
		return esExogena;
	}

	/**
	 * @param esExogena
	 *            :Variable that allows me to determine whether the format will
	 *            generate is an exogenous format to set the name.
	 */
	public void setEsExogena(boolean esExogena) {
		this.esExogena = esExogena;
	}

	/**
	 * @return nombreFormExogena: Name exogenous generate format.
	 */
	public String getNombreFormExogena() {
		return nombreFormExogena;
	}

	/**
	 * @param nombreFormExogena
	 *            : Name exogenous generate format.
	 */
	public void setNombreFormExogena(String nombreFormExogena) {
		this.nombreFormExogena = nombreFormExogena;
	}

	/**
	 * @return carpetaReportes: Folder path where reports are stored for the
	 *         server
	 */
	public String getCarpetaReportes() {
		if (this.carpetaReportes == null || "".equals(this.carpetaReportes)) {
			carpetaReportes = rutaReportesGlassfish
					+ Constantes.CARPETA_ARCHIVOS_REPORTES
					+ ConstantesErp.CARPETA_ARCHIVOS_SUBIDOS
					+ Constantes.EXCEL_FORMATOS_EXOGENAS;
		}
		return carpetaReportes;
	}

	/**
	 * @param carpetaReportes
	 *            :Folder path where reports are stored for the server
	 */
	public void setCarpetaReportes(String carpetaReportes) {
		this.carpetaReportes = carpetaReportes;
	}

	/**
	 * @return cambiarRutaServidor: string to change the server path
	 */
	public String getCambiarRutaServidor() {
		return cambiarRutaServidor;
	}

	/**
	 * @param cambiarRutaServidor
	 *            :string to change the server path
	 */
	public void setCambiarRutaServidor(String cambiarRutaServidor) {
		this.cambiarRutaServidor = cambiarRutaServidor;
	}

	/**
	 * Allows the generation of a report
	 * 
	 * @param fbr
	 *            : dynamic report ('template')
	 * @param ds
	 *            : List of data
	 * @param params
	 *            : parameters
	 */
	public void generarReporte(FastReportBuilder frb, JRDataSource ds,
			HashMap<String, Object> params) {
		ResourceBundle bundleReportes = ControladorContexto
				.getBundle("mensajeReportes");
		try {
			/*
			 * Style created for the message that is displayed when no data is
			 */
			Font fontMensaje = new Font(
					25,
					"SansSerif",
					"arial.ttf",
					Font.PDF_ENCODING_Identity_H_Unicode_with_horizontal_writing,
					true);
			fontMensaje.setBold(true);

			Style estiloMensaje = new Style();
			estiloMensaje.setFont(fontMensaje);
			estiloMensaje.setHorizontalAlign(HorizontalAlign.CENTER);
			estiloMensaje.setVerticalAlign(VerticalAlign.MIDDLE);
			estiloMensaje.setPaddingTop(180);
			if (formato.equals("excel")) {
				if (camposExportar != null && camposExportar.size() > 5) {
					tipoPlantilla = "plantillaExcel.jrxml";
				}

				if (camposExportarSinDuplicados != null
						&& camposExportarSinDuplicados.size() > 5) {
					tipoPlantilla = "plantillaExcel.jrxml";
				}
			}
			frb.setTemplateFile(Constantes.RUTA_UPLOADFILE_GLASFISH
					+ Constantes.CARPETA_ARCHIVOS_REPORTES
					+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
					+ Constantes.CARPETA_REPORTES_JRXML + "/" + tipoPlantilla);

			frb.setWhenNoData(
					bundleReportes.getString("reporte_message_no_hay_datos")
							.toUpperCase(), estiloMensaje, true, false);

			jr = DynamicJasperHelper.generateJasperReport(frb.build(),
					getLayoutManager(), params);
			if (ds != null) {
				jp = JasperFillManager.fillReport(jr, params, ds);
			} else {
				jp = JasperFillManager.fillReport(jr, params);
			}
			exportarReporte();
			tipoPlantilla = "plantilla.jrxml";
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows initialize the class ClassicLayoutManager to create
	 * the report
	 * 
	 * @return object LayoutManager
	 */
	public LayoutManager getLayoutManager() {
		return new ClassicLayoutManager();
	}

	/**
	 * Allows to export the report in the desired format for the user to
	 * download it.
	 * 
	 * @throws Exception
	 */
	private void exportarReporte() throws Exception {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
		SimpleDateFormat hoursFormat = new SimpleDateFormat("HHmmssSSSS");
		String dateString = dateFormat.format(currentDate) + "+";
		String horaString = hoursFormat.format(currentDate) + "+";
		String extension = ".pdf";
		StringBuilder ruta = new StringBuilder();

		ruta.append(Constantes.CARPETA_TEMP_SISTEMA());
		if (esExogena) {
			ruta = new StringBuilder();
			ruta.append(Constantes.RUTA_UPLOADFILE_GLASFISH);
			ruta.append(Constantes.CARPETA_ARCHIVOS_REPORTES);
			ruta.append(Constantes.CARPETA_ARCHIVOS_SUBIDOS);
			ruta.append(Constantes.EXCEL_FORMATOS_EXOGENAS);
		}
		ruta.append("/");
		ruta.append("reporte+");
		if (esExogena) {
			ruta.append(nombreFormExogena + "+");
		}
		ruta.append(dateString);
		ruta.append(horaString);
		File outputFile;
		File parentFile;
		FileOutputStream fos;
		if (formato.equals("pdf")) {
			ruta.append(extension);
			JRPdfExporter exporter = new JRPdfExporter();
			outputFile = new File(ruta.toString());
			parentFile = outputFile.getParentFile();
			if (parentFile != null)
				parentFile.mkdirs();
			fos = new FileOutputStream(outputFile);
			exporter.setExporterInput(new SimpleExporterInput(jp));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fos));
			SimplePdfExporterConfiguration configuration = new SimplePdfExporterConfiguration();
			exporter.setConfiguration(configuration);

			exporter.exportReport();

			fos.close();

			FileInputStream fis = new FileInputStream(outputFile);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			byte[] buf = new byte[1024];
			for (int readNum; (readNum = fis.read(buf)) != -1;) {
				bos.write(buf, 0, readNum);
			}
			byte[] bytes = bos.toByteArray();
			ExternalContext ec = ControladorContexto.getExternalContext();
			HttpSession session = (HttpSession) ec.getSession(false);
			session.setAttribute("bytes", bytes);
			ControladorContexto.redirect("/Reportes");
			fis.close();

		} else {
			extension = ".xls";
			ruta.append(extension);
			JRXlsExporter exporter = new JRXlsExporter();
			outputFile = new File(ruta.toString());
			parentFile = outputFile.getParentFile();
			if (parentFile != null)
				parentFile.mkdirs();
			fos = new FileOutputStream(outputFile);
			exporter.setExporterInput(new SimpleExporterInput(jp));
			exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(fos));
			SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
			configuration.setOnePagePerSheet(true);
			configuration.setDetectCellType(true);
			configuration.setCollapseRowSpan(false);
			exporter.setConfiguration(configuration);

			exporter.exportReport();

			fos.close();
			descargarFormato(ruta.toString(), "xls");
			if (esExogena) {
				String fileName = outputFile.getName();
				copiarReporteDesdeServidor(fileName);
				esExogena = false;
			}
		}

	}

	/**
	 * This method allows the user to download a file located on the route which
	 * receives as parameter
	 * 
	 * @param URL
	 *            : file path
	 * @throws IOException
	 */
	@SuppressWarnings("resource")
	private void descargarFormato(String URL, String tipoArchivo)
			throws IOException {
		File reporte = new File(URL);
		FileInputStream fis = new FileInputStream(reporte);
		byte[] bytes = new byte[1000];
		int read = 0;
		if (!ControladorContexto.getFacesContext().getResponseComplete()) {
			String fileName = reporte.getName();
			String contentType;
			if (tipoArchivo.equals("pdf")) {
				contentType = "application/pdf";
			} else {
				contentType = "application/vnd.ms-excel";
			}
			HttpServletResponse response = (HttpServletResponse) ControladorContexto
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
			ControladorContexto.getFacesContext().responseComplete();
		}
	}

	/**
	 * Allows to export a report template created dynamically, the JRXML Archive
	 * 
	 * @param dr
	 *            : dynamic report ('template')
	 * @param params
	 *            : parameters
	 */
	public void exportarJRXML(DynamicReport dr, HashMap<String, String> params) {
		try {
			if (this.jr != null) {
				String propertie = System.getProperty("user.dir")
						+ "/target/reports/"
						+ this.getClass().getCanonicalName() + ".jrxml";
				DynamicJasperHelper.generateJRXML(this.jr, "UTF-8", propertie);
			} else {
				String propertie = System.getProperty("user.dir")
						+ "/target/reports/"
						+ this.getClass().getCanonicalName() + ".jrxml";
				DynamicJasperHelper.generateJRXML(dr, this.getLayoutManager(),
						params, "UTF-8", propertie);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Allows exporting reports in PDF and excel format
	 * 
	 * @param jasperPrint
	 *            JasperPrint object type, which contains the list of objects
	 *            (entities associated mapping) to generate the report
	 * 
	 */
	public void exportarReportes(JasperPrint jasperPrint) {
		Date currentDate = new Date();
		SimpleDateFormat dateFormat = new SimpleDateFormat("MMddyyyy");
		SimpleDateFormat hoursFormat = new SimpleDateFormat("HHmmssSSSS");
		String dateString = dateFormat.format(currentDate);
		String horaString = hoursFormat.format(currentDate);
		String extension = ".pdf";
		try {
			StringBuilder ruta = new StringBuilder();
			ruta.append(Constantes.CARPETA_TEMP_SISTEMA());
			ruta.append("/");
			ruta.append("reporte_");
			ruta.append(dateString);
			ruta.append(horaString);
			File outputFile;
			File parentFile;
			FileOutputStream fos;
			if (formato.equals("pdf")) {
				byte[] bytes = JasperExportManager
						.exportReportToPdf(jasperPrint);
				ExternalContext ec = ControladorContexto.getExternalContext();
				HttpSession session = (HttpSession) ec.getSession(false);
				session.setAttribute("bytes", bytes);
				ControladorContexto.redirect("/Reportes");
			} else {
				extension = ".xls";
				ruta.append(extension);

				JRXlsExporter exporter = new JRXlsExporter();
				outputFile = new File(ruta.toString());
				parentFile = outputFile.getParentFile();
				if (parentFile != null)
					parentFile.mkdirs();
				fos = new FileOutputStream(outputFile);
				exporter.setExporterInput(new SimpleExporterInput(jasperPrint));
				exporter.setExporterOutput(new SimpleOutputStreamExporterOutput(
						fos));
				SimpleXlsReportConfiguration configuration = new SimpleXlsReportConfiguration();
				configuration.setOnePagePerSheet(true);
				configuration.setDetectCellType(true);
				configuration.setCollapseRowSpan(false);
				exporter.setConfiguration(configuration);

				exporter.exportReport();

				fos.close();
				descargarFormato(ruta.toString(), "xls");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to copy the generated report Glassfish server to the local
	 * directory.
	 * 
	 * @param fileName
	 *            : name of the file to be copied
	 * @throws IOException
	 */
	private void copiarReporteDesdeServidor(String fileName) throws IOException {
		String rutaGenerica = getCarpetaReportes();
		String rutaLocal = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ Constantes.CARPETA_ARCHIVOS_REPORTES
				+ ConstantesErp.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.EXCEL_FORMATOS_EXOGENAS;
		/* If you want another path */
		if (this.cambiarRutaServidor != null
				&& !"".equals(this.cambiarRutaServidor)) {
			rutaGenerica = this.cambiarRutaServidor;
		}
		/* Path from where you are copying the file */
		File fileACopiar = new File(rutaGenerica + "/" + fileName);
		if (fileACopiar.exists()) {
			File fileLocalDir = new File(rutaLocal);
			/* Path where it will download the file */
			File fileServidor = new File(rutaLocal + "/" + fileName);
			if (!fileServidor.exists()) {
				if (!fileLocalDir.exists()) {
					fileLocalDir.mkdirs();
				}
				InputStream in = new FileInputStream(fileACopiar);
				OutputStream out = new FileOutputStream(fileServidor);

				byte[] buf = new byte[1024];
				int len;
				while ((len = in.read(buf)) > 0) {
					out.write(buf, 0, len);
				}
				in.close();
				out.close();
			}
		}
	}
}
