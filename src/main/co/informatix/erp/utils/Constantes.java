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
	public static final String CARPETA_REPORTES_JRXML = PropertiesManager
			.getProperty("locate.carpeta.archivos.jrxml");
	public static final String CARPETA_ICONOS_MENU = PropertiesManager
			.getProperty("locate.carpeta.img.menu");
	public static final String CARPETA_ICONOS_MENU_CABECERA = PropertiesManagerUtils
			.getProperty("locate.carpeta.img.menu.cabecera");
	public static final String CARPETA_ARCHIVOS_TEMP = PropertiesManager
			.getProperty("locate.carpeta.archivos.temp");
	public static final String IMG_REPORTE_LOGO = PropertiesManager
			.getProperty("locate.img.reporte.logo.informatix");
	public static final String IMG_REPORTE_LOGO_APLICACION = PropertiesManager
			.getProperty("locate.img.reporte.logo.aplicacion");
	public static final String IMG_REPORTE_FONDO = PropertiesManager
			.getProperty("locate.img.reporte.fondo");
	public static final String CARPETA_GESTION_DOCUMENTAL = PropertiesManager
			.getProperty("locate.carpeta.archivos.gestion.documental");
	public static final String FOLDER_FILES = PropertiesManager
			.getProperty("locate.carpeta.archivos.files");
	public static final String FOLDER_INVOICES = PropertiesManager
			.getProperty("locate.carpeta.archivos.invoices");

	public static final String CARPETA_TEMP_SISTEMA() throws Exception {
		String tempdir = System.getProperty("java.io.tmpdir");
		if (!(tempdir.endsWith("/") || tempdir.endsWith("\\")))
			tempdir += System.getProperty("file.separator");
		tempdir += Utils.getNombreAplicacion();
		tempdir += System.getProperty("file.separator");
		return tempdir;
	}

	/* Organizations */
	public static final String CARPETA_ARCHIVOS_ORGANIZACIONES = PropertiesManager
			.getProperty("locate.carpeta.archivos.organizaciones");
	public static final String CARPETA_ARCHIVOS_EMPRESAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.empresas");
	public static final String CARPETA_ARCHIVOS_LOGOS_EMPRESAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.logosEmpresas");
	public static final String CARPETA_ARCHIVOS_LOGOS_ORGANIZACIONES = PropertiesManager
			.getProperty("locate.carpeta.archivos.logosOrganizaciones");
	public static final String CARPETA_ARCHIVOS_FOTOS_FINCAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.fotosHaciendas");
	public static final String CARPETA_ARCHIVOS_MAPAS_FINCAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.mapasHaciendas");
	public static final String CARPETA_ARCHIVOS_FOTOS_ZONAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.fotosZonas");

	/* Reports */
	public static final String CARPETA_ARCHIVOS_REPORTES = PropertiesManager
			.getProperty("locate.carpeta.archivos.reportes");
	public static final String EXCEL_FORMATOS_EXOGENAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.reportes.formatos.exogena");
	public static final String REPORTES_ACTIVIDADES = PropertiesManager
			.getProperty("locate.carpeta.archivos.reportes.actividad");

	/* Activity reports */
	public static final String REPORTE_ACTIVIDAD_LABEL_ACTIVOS = PropertiesManager
			.getProperty("locate.reporte.actividad.label.activos");
	public static final String REPORTE_ACTIVIDAD_LABEL_INSUMOS = PropertiesManager
			.getProperty("locate.reporte.actividad.label.insumos");
	public static final String REPORTE_ACTIVIDAD_LABEL_PERSONAL = PropertiesManager
			.getProperty("locate.reporte.actividad.label.personal");
	public static final String REPORTE_ACTIVIDAD_LABEL_HISTORICO = PropertiesManager
			.getProperty("locate.reporte.actividad.label.historico");
	public static final String REPORTE_ACTIVIDAD_LABEL_HISTORICO_ESPECIFICA = PropertiesManager
			.getProperty("locate.reporte.actividad.label.historico.especifica");

	/* Human Resources */
	/*
	public static final String CARPETA_ARCHIVOS_PERSONAS = PropertiesManager
			.getProperty("locate.carpeta.archivos.personas");
			*/
	public static final String FOLDER_FILES_PERSONS = PropertiesManager
			.getProperty("locate.folder.files.persons");

	/* Validation Scripting */
	public static final String POLICY_FILE_LOCATION = RUTA_UPLOADFILE_GLASFISH
			+ PropertiesManager.getProperty("locate.archivo.policy.file");

	/* Graphic module */
	public static final Long TAMANYO_MAPA_FINCA = Long
			.parseLong(PropertiesManager.getProperty(
					"locate.tamanyo.mapa.hacienda", "0"));

	/* Active */
	public static final String CARPETA_ARCHIVOS_ACTIVOS = PropertiesManager
			.getProperty("locate.carpeta.archivos.activos");
	public static final String CARPETA_ARCHIVOS_FOTOS_ACTIVOS = PropertiesManager
			.getProperty("locate.carpeta.archivos.fotosActivos");
	public static final short ID_ESTADO_ACTIVO = 1;

	/* Inventory */
	public static final String CARPETA_ARCHIVOS_INVENTARIO = PropertiesManager
			.getProperty("locate.carpeta.archivos.inventario");
	public static final String CARPETA_ARCHIVOS_PRODUCTOS = PropertiesManager
			.getProperty("locate.carpeta.archivos.productos");
	public static final String COMPROBANTES_MOVIMIENTO_RETIRO = PropertiesManager
			.getProperty("locate.carpeta.archivos.comprobantes.movimiento.retiro");
	public static final String COMPROBANTES_MOVIMIENTO_ENTRADA = PropertiesManager
			.getProperty("locate.carpeta.archivos.comprobantes.movimiento.entrada");
	public static final String COMPROBANTES_MOVIMIENTO_CONSUMO = PropertiesManager
			.getProperty("locate.carpeta.archivos.comprobantes.movimiento.consumo");
	public static final String COMPROBANTES_MOVIMIENTO_TRASLADO = PropertiesManager
			.getProperty("locate.carpeta.archivos.comprobantes.movimiento.traslado");

	/* Farm */
	public static final String FOLDER_FILES_FARMS = PropertiesManager
			.getProperty("locate.folder.files.farm");

	/* END FOLDER PATHS */

	/* System Variables */
	public static final int MAXIMO_ROLES_SIN_MODIFICAR = Integer
			.parseInt(PropertiesManager
					.getProperty("locate.maximo.roles.sinModificar"));

	/* System Constants */
	public static final String ROL_ADMINISTRADOR = "Administrador";
	public static final String ROL_AUDITOR = "Auditor";
	public static final String FUNCION_STATUS = "RichFaces.$('migaPan:statPane').show();";
	public static final String TIPO_UNIDAD_LONGITUD = "Longitud";
	public static final String TIPO_UNIDAD_MASA = "Masa";
	public static final String TIPO_UNIDAD_TIEMPO = "Tiempo";
	public static final String TIPO_UNIDAD_LIQUIDO = "Volumen L\u00edquido";
	public static final String TIPO_UNIDAD_DENSIDAD = "Densidad";
	public static final String IS_NULL = "IS NULL";
	public static final String IS_NOT_NULL = "IS NOT NULL";
	public static final String NOT = "no";
	public static final String SI = "si";
	public static final String ADD = "add";
	public static final String VIGENTE = "vigente";
	public static final String SIN_VIGENTE = "sinVigencia";
	public static final String OK = "ok";

	/* States */
	public static final String ESTADO_CANCELADO = "Cancelado";
	public static final String ESTADO_FINALIZADO = "Finalizado";
	public static final String ESTADO_PENDIENTE = "Pendiente";
	public static final String ESTADO_VENCIDO = "Vencido";

	/* States icons names */
	public static final String IMG_ESTADO_CANCELADO = PropertiesManager
			.getProperty("locate.img.cancelado");
	public static final String IMG_ESTADO_FINALIZADO = PropertiesManager
			.getProperty("locate.img.finalizado");
	public static final String IMG_ESTADO_PENDIENTE = PropertiesManager
			.getProperty("locate.img.pendiente");

	/* Inventory */
	public static final short ID_TIPO_MOVIMIENTO_INVENTARIO_CONSUMO = 1;
	public static final short ID_TIPO_MOVIMIENTO_INVENTARIO_TRASLADO = 2;
	public static final short ID_TIPO_MOVIMIENTO_INVENTARIO_RETIRO = 3;
	public static final short ID_TIPO_MOVIMIENTO_INVENTARIO_ENTRADA = 5;
	public static final short ID_TIPO_COSTEO_INVENTARIO_PROMEDIO_PONDERADO = 1;
	public static final short ID_TIPO_COSTEO_INVENTARIO_PEPS = 2;

	/* Processes */
	public static final int ID_PROCESO_SIEMBRA = 2;
	public static final int ID_PROCESO_RESIEMBRA = 3;
	public static final int ID_PROCESO_RIEGO = 4;
	public static final int ID_PROCESO_PODA = 9;
	public static final int ID_PROCESO_PROCEDENCIA_SEMILLA = 10;
	public static final int ID_PROCESO_DESPATRONADA = 11;
	public static final int ID_PROCESO_GERMINACION_SEMILLA = 12;
	public static final int ID_PROCESO_ALISTAMIENTO_SUSTRATO = 13;
	public static final int ID_PROCESO_SALIDA_ACLIMATADOR = 14;
	public static final int ID_PROCESO_DESCARTE = 15;
	public static final int ID_PROCESO_DESBABE = 16;
	public static final int ID_PROCESO_ALMACENAMIENTO = 17;
	public static final int ID_PROCESO_VENTA = 18;
	public static final int ID_PROCESO_FERTILIZACION = 5;
	public static final int ID_PROCESO_LIMPIEZA = 7;
	public static final int ID_PROCESO_FUMIGACION = 1;
	public static final int ID_PROCESO_SIEMBRA_SEMILLA = 19;
	public static final int ID_PROCESO_OBTENCION_PLANTA = 20;
	public static final int ID_PROCESO_COSECHA = 21;
	public static final int ID_PROCESO_MIP = 22;

	public static final int ID_NIVEL_PUNTO_CONTROL_CAPITULO = 1;
	public static final int ID_NIVEL_PUNTO_CONTROL_SUBCAPITULO = 2;
	public static final int ID_NIVEL_PUNTO_CONTROL_ITEM_VERIFICACION = 3;

	public static final String NOMBRE_REPORTE_LISTA_VERIFICACION = "Reporte_lista_de_verificacion_UTZ_CERTIFIED";
	public static final String NOMBRE_REPORTE_LISTA_VERIFICACION_CONSOLIDADO = "Reporte_lista_de_verificacion_UTZ_CERTIFIED_CONSOLIDADO";

	/* Detail Consumer Movement Activity */
	public static final String DETALLE_ACTIVIDAD_SIEMBRA = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de siembra";
	public static final String DETALLE_ACTIVIDAD_FERTILIZACION = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de Fertilizaci\u00F3n";
	public static final String DETALLE_ACTIVIDAD_LIMPIEZA = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de Limpieza";
	public static final String DETALLE_ACTIVIDAD_DESPATRONADA = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de despatronada";
	public static final String DETALLE_ACTIVIDAD_SALIDA_ACLIMATADOR = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de salida aclimatador";
	public static final String DETALLE_ACTIVIDAD_DESCARTE = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de descarte";
	public static final String DETALLE_ACTIVIDAD_DESBABE = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de desbabe";
	public static final String DETALLE_ACTIVIDAD_ALMACENAMIENTO = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de almacenamiento";
	public static final String DETALLE_ACTIVIDAD_VENTA = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de venta";
	public static final String DETALLE_ACTIVIDAD_COSECHA = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de cosecha";
	public static final String DETALLE_ACTIVIDAD_MIP = "Utilizaci\u00F3n de productos"
			+ " para poder llevar a cabo la actividad de MIP";

	/* Allowed extensions in file uploads */
	public static final String EXT_IMG = PropertiesManager
			.getProperty("locate.ext.permitidas.img");
	public static final String EXT_IMG_PDF = PropertiesManager
			.getProperty("locate.ext.permitidas.img.pdf");
	public static final String EXT_XLS = PropertiesManager
			.getProperty("locate.ext.permitidas.xls");
	public static final String EXT_DOC = PropertiesManager
			.getProperty("locate.ext.permitidas.doc");
	public static final String EXT_DOC_PDF = PropertiesManager
			.getProperty("locate.ext.permitidas.doc.docx.pdf");

	/* Maximum size in MB, for uploading files */
	public static final long TAMANYO_MAX_ARCHIVOS = Long
			.parseLong(PropertiesManager
					.getProperty("locate.tamanyo.maximo.archivos"));

	/* Historical Response Options Compliance */
	public static final String ABR_SIN_RESPUESTA = PropertiesManager
			.getProperty("locate.opcion.sin.respuesta");
	public static final String ABR_CUMPLIDO = PropertiesManager
			.getProperty("locate.opcion.cumplido");
	public static final String ABR_NO_APLICA = PropertiesManager
			.getProperty("locate.opcion.no.aplica");

	/* Tree image certification */
	public static final String IMG_SELECCIONAR_ARBOL = PropertiesManager
			.getProperty("locate.img.seleccionar.arbol");

	public static final String IMG_SELECCIONAR_ARBOL_ASTERISCO = PropertiesManager
			.getProperty("locate.img.seleccionar.arbol.asterisco");

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
	public static final String EMPRESA = "empresa";
	public static final String CONTACTO = "contacto";
	public static final String PAIS = "pais";
	public static final String DEPARTAMENTO = "departamento";
	public static final String MUNICIPIO = "municipio";
	public static final String UNIDADMEDIDA = "unidadMedida";
	public static final String PERSONA = "persona";
	public static final String SUCURSAL = "sucursal";
	public static final String HACIENDA = "hacienda";

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
	public static final String DATE_FORMAT_MESSAGE_SIMPLE = "MMM dd yyyy";
	public static final String DATE_FORMAT_REPORT = "MMM yy";

	/* Sort */
	public static final String DESCENDING = "DESC";
	public static final String ASCENDING = "ASC";
	public static final String SORT_PROPERTY = "sortProperty";

}