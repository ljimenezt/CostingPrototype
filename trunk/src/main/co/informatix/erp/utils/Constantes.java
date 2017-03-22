package co.informatix.erp.utils;

import java.io.Serializable;

/**
 * 
 * This class allows create constants that are used in the application
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
public class Constantes implements Serializable {

	/* FOLDER PATHS */
	/* Generic */
	public static final String RUTA_UPLOADFILE_GLASFISH = Utils.RUTA_SERVIDOR();

	public static final String RUTA_UPLOADFILE_WORKSPACE = ControladorGenerico
			.LOCAL_PATH();

	public static final String CARPETA_ARCHIVOS_SUBIDOS = PropertiesManager
			.getProperty("locate.carpeta.archivos");
	public static final String RUTA_IMG = PropertiesManager
			.getProperty("locate.carpeta.img");
	public static final String CARPETA_ICONOS_MENU_CABECERA = PropertiesManagerUtils
			.getProperty("locate.carpeta.img.menu.cabecera");
	public static final String CARPETA_ARCHIVOS_TEMP = PropertiesManager
			.getProperty("locate.carpeta.archivos.temp");
	public static final String FOLDER_FILES = PropertiesManager
			.getProperty("locate.carpeta.archivos.files");
	public static final String FOLDER_INVOICES = PropertiesManager
			.getProperty("locate.carpeta.archivos.invoices");
	public static final String PATH_FILES_REPORTS = PropertiesManager
			.getProperty("locate.folder.files.reports");
	public static final String PATH_REPORTS_RPT = PropertiesManager
			.getProperty("locate.folder.files.rptdesign");

	/* Adjust to deposit */
	public static final Integer IDENTIFIER_ADJUSTEMENT_ADJUST_TYPE = 1;

	/* Organizations */
	public static final String CARPETA_ARCHIVOS_ORGANIZACIONES = PropertiesManager
			.getProperty("locate.carpeta.archivos.organizaciones");
	public static final String CARPETA_ARCHIVOS_LOGOS_EMPRESAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.logosEmpresas");
	public static final String CARPETA_ARCHIVOS_LOGOS_ORGANIZACIONES = PropertiesManager
			.getProperty("locate.carpeta.archivos.logosOrganizaciones");

	/* Human Resources */
	public static final String FOLDER_FILES_PERSONS = PropertiesManager
			.getProperty("locate.folder.files.persons");

	/* Farm */
	public static final String FOLDER_FILES_FARMS = PropertiesManager
			.getProperty("locate.folder.files.farm");

	/* Fuel */
	public static final String FOLDER_FILES_FUEL = PropertiesManager
			.getProperty("locate.carpeta.archivos.fuel");

	/* END FOLDER PATHS */

	/* System Variables */
	public static final int MAXIMO_ROLES_SIN_MODIFICAR = Integer
			.parseInt(PropertiesManager
					.getProperty("locate.maximo.roles.sinModificar"));

	/* Formats Types */
	public static final String FORMAT_EXCEL = PropertiesManager
			.getProperty("locate.format.excel");
	public static final String FORMAT_EXCEL_XLSX = PropertiesManager
			.getProperty("locate.format.excel.xlsx");
	public static final String FORMAT_PDF = PropertiesManager
			.getProperty("locate.format.pdf");

	/* System Constants */
	public static final String IS_NULL = "IS NULL";
	public static final String IS_NOT_NULL = "IS NOT NULL";
	public static final String NOT = "no";
	public static final String SI = "si";
	public static final String ADD = "add";
	public static final String VIGENTE = "vigente";
	public static final String SIN_VIGENTE = "sinVigencia";
	public static final String OK = "ok";

	/* Allowed extensions in file uploads */
	public static final String EXT_IMG = PropertiesManager
			.getProperty("locate.ext.permitted.img");
	public static final String EXT_DOC_PDF = PropertiesManager
			.getProperty("locate.ext.permitted.doc.docx.pdf");
	public static final String EXT_PDF_DOC_IMG = PropertiesManager
			.getProperty("locate.ext.permitted.doc.pdf.img");

	/* Maximum size in MB, for uploading files */
	public static final long TAMANYO_MAX_ARCHIVOS = Long
			.parseLong(PropertiesManager
					.getProperty("locate.tamanyo.maximo.archivos"));

	/* Activities and human resources */
	public static final int ID_CROP_DEFAULT = 2;
	public static final int EDAD_MINIMA_ACTIVIDAD_PELIGROSA = 18;
	public static final String STATE_CERTIFIED = PropertiesManager
			.getProperty("locate.activitiesandhr.certified");
	public static final String STATE_HR_CERTIFIED = PropertiesManager
			.getProperty("locate.activitiesandhr.hr.certified");
	public static final String STATE_CERTIFIED_MATERNITY = PropertiesManager
			.getProperty("locate.activitiesandhr.hr.certified.maternity");

	/* Human resources */
	public static final String GENERO = "m";

	/* Event File Upload */
	public static final String UPLOAD_EXT_INVALIDA = "extInvalida";
	public static final String UPLOAD_TAMANO_INVALIDA = "tamanyoInvalido";
	public static final String UPLOAD_NULL = "null";

	/* Query strings */
	public static final String QUERY_DELETE = "DELETE";
	public static final String QUERY_INSERT = "INSERT";

	/* Company */
	public static final String N_TAB = "n";
	public static final String F_TAB = "f";
	public static final String NUEVO = "nuevo";
	public static final String EDITAR = "editar";

	/* Organization */
	public static final String RAZON_SOCIAL = "razonSocial";
	public static final String NIT = "nit";

	/* Farm */
	public static final String BUSINESS = "business";
	public static final String PERSON = "person";
	public static final String SUCURSAL = "sucursal";
	public static final String Farm = "farm";

	/* Permits */
	public static final String NEW_PERMISO = "newPermiso";
	public static final String ADD_PERMISO = "addPermiso";
	public static final String BORRAR_PERMISO = "borrar";

	/* Rols */
	public static final String PERM_A = "A";
	public static final String PERM_S = "S";
	public static final String PERM_U = "U";
	public static final String PERM_D = "D";
	public static final String PERM_I = "I";
	public static final String PERM_L = "L";
	public static final String ROLES_USUARIO = "rolesUsuario";
	public static final String ROLES_MENU = "rolesMenu";
	public static final String ROLES_METODO = "rolesMetodo";
	public static final String NEW_ROL_USUARIO = "newRolUsuario";
	public static final String RE_NEW_ROL_USUARIO = "reNewRolUsuario";
	public static final String ADD_ROL_USUARIO = "addRolUsuario";
	public static final String CHANGE_ROL_USUARIO = "changeRolUsuario";

	/* Date Format */
	public static final String DATE_FORMAT_CREATION = "mm dd yyyy hh:mm:ss.SSS";
	public static final String DATE_FORMAT_MESSAGE = "dd/MM/yyyy HH:mm";
	public static final String DATE_FORMAT_MESSAGE_WITHOUT_TIME = "dd/MM/yyyy";
	public static final String DATE_FORMAT_MESSAGE_SIMPLE = "MMM dd yyyy";
	public static final String DATE_FORMAT_MESSAGE_MMDDYYYY = "MM/dd/yyyy";
	public static final String DATE_FORMAT_REPORT = "MMM yy";
	public static final String DATE_FORMAT_CONSULT = "yyyy-MM-dd";
	public static final String DATE_FORMAT_TABLE = "MMM dd, yyyy";
	public static final String DATE_FORMAT_COLUMN_TABLE = "EEE MMM d ''yy";
	public static final String DATE_FORMAT_DAY_OF_WEEK = "EEEE";
	public static final String DATE_FORMAT_DAY_WEEK_DAY = "EEEE dd";

	/* Sort */
	public static final String DESCENDING = "DESC";
	public static final String ASCENDING = "ASC";
	public static final String SORT_PROPERTY = "sortProperty";

	/* File Extension */
	public static final String FILE_EXT_PDF = "pdf";
	public static final String FILE_EXT_DOCX = "docx";
	public static final String FILE_EXT_DOC = "doc";

	/* Security */
	public static final int START_PORT = Integer.parseInt(PropertiesManager
			.getProperty("locate.size.start.port"));
	public static final int FINAL_PORT = Integer.parseInt(PropertiesManager
			.getProperty("locate.size.final.port"));

	/* Information Base */
	public static final int IVA_INITIAL = Integer.parseInt(PropertiesManager
			.getProperty("locate.size.iva.initial"));
	public static final int IVA_FINAL = Integer.parseInt(PropertiesManager
			.getProperty("locate.size.iva.final"));

	/* System profile */
	public static final short SYSTEM_PROFILE_ID = 1;

	/* Days calendar */
	public static final short SATURDAY = 7;
	public static final short SUNDAY = 1;

	/* Work Hours */
	public static final short MAX_HOURS = 12;

	/* Transaction type */
	public static final int TRANSACTION_TYPE_RETURN = 2;
	public static final int TRANSACTION_TYPE_EXPIRED = 4;
	public static final int TRANSACTION_TYPE_WITHDRAWAL = 3;
	public static final int TRANSACTION_TYPE_ADJUSTMENT_DOWN = 9;
	public static final int TRANSACTION_TYPE_ADJUSTMENT_UP = 10;
	public static final int TRANSACTION_TYPE_FUEL_PURCHASE = 12;

	/* Files */
	public static final String EXTENSION_XLSX = ".xlsx";
	public static final String EXTENSION_XLS = ".xls";
	public static final String EXTENSION_PDF = ".pdf";
	public static final String OFFICE_VERSION = "office2007";
	public static final String EMITTER_ID_XLS = "org.eclipse.birt.report.engine.emitter.nativexls";
	public static final String EMITTER_ID_XLSX = "uk.co.spudsoft.birt.emitters.excel.XlsxEmitter";

	/* Pluviometer */
	public static final short PLUVIOMETER_MAX_RANGE = 250;
	public static final short PLUVIOMETER_MIN_DAY = 4;
	public static final short PLUVIOMETER_SUM_DAY = 364;

	/* Value Generic */
	public static final short VALUE_ZERO = 0;
	public static final Double VALUE_FORNIGHT = 15d;

	/* Cycle report */
	public static final String COMPANY = "LANCACAO S.A.";

	/* Reports on POI */
	public static final String COLOR_BLACK = "BLACK";
	public static final String COLOR_BLUE = "BLUE";
	public static final String COLOR_RED = "RED";
	public static final String FONT_COOPER_BLACK = "Cooper Black";
	public static final String FONT_CALIBRI = "Calibri";
	public static final String LEFT = "L";
	public static final String RIGHT = "R";
	public static final String TOP = "T";
	public static final String BOTTOM = "B";
	public static final String CENTER = "C";

	/* plots and crops */
	public static final String FLAG_ACTION_SECTION = "SECTION";
	public static final String FLAG_ACTION_CROPS = "CROPS";

	/* Style Manage Assist Control */
	public static final String STYLE_ASSIST_OK = "assistControlOk";
	public static final String STYLE_ASSIST_NOT = "assistControlNot";

	/* Source of Control Manage */
	public static final String SOURCE_ASSIST_CONTROL = "AssistControl";
	public static final String SOURCE_FOOD_CONTROL = "FoodControl";

	/* Type Food */
	public static final String TYPE_FOOD_BREAKFAST = "Breakfast";
	public static final String TYPE_FOOD_LUNCH = "Lunch";
	public static final String TYPE_FOOD_DINNER = "Dinner";

	/* Fuel Purchase */
	public static final int FUEL_PURCHASE_ID_COSTO = 1;

}