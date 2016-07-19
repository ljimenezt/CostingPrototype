package co.informatix.erp.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.security.GeneralSecurityException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.openxml4j.opc.OPCPackage;
import org.apache.poi.openxml4j.opc.PackageAccess;
import org.apache.poi.poifs.crypt.EncryptionInfo;
import org.apache.poi.poifs.crypt.EncryptionMode;
import org.apache.poi.poifs.crypt.Encryptor;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.eclipse.birt.core.framework.Platform;
import org.eclipse.birt.report.engine.api.EXCELRenderOption;
import org.eclipse.birt.report.engine.api.EngineConfig;
import org.eclipse.birt.report.engine.api.IExcelRenderOption;
import org.eclipse.birt.report.engine.api.IRenderOption;
import org.eclipse.birt.report.engine.api.IReportEngine;
import org.eclipse.birt.report.engine.api.IReportEngineFactory;
import org.eclipse.birt.report.engine.api.IReportRunnable;
import org.eclipse.birt.report.engine.api.IRunAndRenderTask;
import org.eclipse.birt.report.engine.api.RenderOption;

/**
 * This class allows to process the parameters and contain the methods to
 * generate the reports.
 * 
 * @author Andres.Gomez
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ReportsController implements Serializable {

	private String reportFormat;
	private String fileName;
	private String password;

	/**
	 * @return reportFormat: obtained the Reports Format
	 */
	public String getReportFormat() {
		return reportFormat;
	}

	/**
	 * @param reportFormat
	 *            : obtained the Reports Format
	 */
	public void setReportFormat(String reportFormat) {
		this.reportFormat = reportFormat;
	}

	/**
	 * @return fileName:obtained the file name
	 */
	public String getFileName() {
		return fileName;
	}

	/**
	 * @param fileName
	 *            : establishes the file name
	 */
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	/**
	 * @return password: return password assigned by user to the reports.
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * @param password
	 *            : set the password to the XLSX reports.
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * Method that builds and runs the reports.
	 * 
	 * @param reportName
	 *            : report name to run
	 * @param mapAttribute
	 *            :map of attributes to display in the report
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	private void generateReports(String reportName,
			HashMap<String, Object> mapAttribute) throws Exception {
		Calendar now = Calendar.getInstance();
		String currentDate = String.valueOf(now.getTimeInMillis());
		EngineConfig config = new EngineConfig();
		Platform.startup(config);

		IReportEngineFactory factory = (IReportEngineFactory) Platform
				.createFactoryObject(IReportEngineFactory.EXTENSION_REPORT_ENGINE_FACTORY);
		IReportEngine engine = factory.createReportEngine(config);

		String pathGlassfishReport = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ Constantes.FOLDER_FILES + Constantes.PATH_FILES_REPORTS
				+ Constantes.PATH_REPORTS_RPT;
		IReportEngine birtReportEngine = BirtEngine.getBirtEngine();
		IReportRunnable design = birtReportEngine
				.openReportDesign(pathGlassfishReport + "/" + reportName);
		IRunAndRenderTask task = engine.createRunAndRenderTask(design);
		String reportFileName = currentDate + "_"
				+ reportName.substring(0, reportName.length() - 10);
		mapAttribute.put("reportFileName", reportFileName);
		Iterator it = mapAttribute.entrySet().iterator();
		while (it.hasNext()) {
			Map.Entry e = (Map.Entry) it.next();
			task.setParameterValue((String) e.getKey(), e.getValue());
		}
		String filePathLocal = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		FileController.fileExist(filePathLocal);
		String filePathReport = filePathLocal + "/" + reportFileName;

		this.fileName = Constantes.CARPETA_ARCHIVOS_TEMP + "/" + reportFileName;

		if (this.reportFormat.equals(Constantes.FORMAT_EXCEL)) {
			this.fileName += Constantes.EXTENSION_XLSX;
			excelOptions(task, Constantes.EXTENSION_XLS.substring(1),
					filePathReport + Constantes.EXTENSION_XLSX,
					Constantes.EMITTER_ID_XLS);
		} else if (this.reportFormat.equals(Constantes.FORMAT_EXCEL_XLSX)) {
			this.fileName += Constantes.EXTENSION_XLSX;
			excelOptions(task, Constantes.EXTENSION_XLSX.substring(1),
					filePathReport + Constantes.EXTENSION_XLSX,
					Constantes.EMITTER_ID_XLSX);
		} else if (this.reportFormat.equals(Constantes.FORMAT_PDF)) {
			this.fileName += Constantes.EXTENSION_PDF;
			pdfOptions(task, filePathReport + Constantes.EXTENSION_PDF);
		}
		task.close();
		engine.destroy();
	}

	/**
	 * Method that builds and runs the PDF report.
	 * 
	 * @param task
	 *            :Task for generate report.
	 * @param filePathLocal
	 *            :Local output file path and file name.
	 * @throws Exception
	 */
	private void pdfOptions(IRunAndRenderTask task, String filePathLocal)
			throws Exception {
		IRenderOption options = new RenderOption();
		options.setOutputFormat(IRenderOption.OUTPUT_FORMAT_PDF);
		options.setOutputFileName(filePathLocal);
		options.setOption(IRenderOption.HTML_PAGINATION, Boolean.TRUE);
		task.setRenderOption(options);
		task.run();
	}

	/**
	 * Method that builds and runs the excel report.
	 * 
	 * @param task
	 *            : Task for generate report.
	 * @param format
	 *            : Excel format the report (XLS/XLSX).
	 * @param filePathReport
	 *            : Local output file path and file name.
	 * @param emitterId
	 *            : Is the id of the Emitter and depends on the format.
	 * 
	 * @throws Exception
	 * @throws IOException
	 * @throws InvalidFormatException
	 * @throws GeneralSecurityException
	 */
	private void excelOptions(IRunAndRenderTask task, String format,
			String filePathReport, String emitterId) throws Exception,
			IOException, InvalidFormatException, GeneralSecurityException {

		EXCELRenderOption options = new EXCELRenderOption();
		options.setOutputFormat(format);
		options.setOutputFileName(filePathReport);
		options.setOption(IExcelRenderOption.OFFICE_VERSION,
				Constantes.OFFICE_VERSION);
		options.setOption(IRenderOption.EMITTER_ID, emitterId);

		task.setRenderOption(options);
		task.run();

		if (!("").equals(this.password)) {
			POIFSFileSystem fs = new POIFSFileSystem();
			EncryptionInfo info = new EncryptionInfo(EncryptionMode.standard);
			Encryptor enc = info.getEncryptor();
			enc.confirmPassword(this.password);
			OPCPackage opc = OPCPackage.open(filePathReport,
					PackageAccess.READ_WRITE);
			OutputStream os = enc.getDataStream(fs);
			opc.save(os);
			opc.close();
			FileOutputStream fos = new FileOutputStream(filePathReport);
			fs.writeFilesystem(fos);
			fos.close();
		}
	}

	/**
	 * This method allows to compile the report of Misapplied details with the
	 * parameters received from the action.
	 * 
	 * @throws Exception
	 */
	public void generateReportInventoryControl() throws Exception {
		String reportName = "inventoryControl.rptdesign";
		HashMap<String, Object> mapAttribute = new HashMap<String, Object>();
		this.reportFormat = Constantes.FORMAT_EXCEL;
		generateReports(reportName, mapAttribute);
	}

}
