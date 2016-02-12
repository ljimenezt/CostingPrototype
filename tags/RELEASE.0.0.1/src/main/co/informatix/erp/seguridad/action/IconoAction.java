package co.informatix.erp.seguridad.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.seguridad.dao.GestionarIconoDao;
import co.informatix.erp.seguridad.dao.GestionarMenuDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.entities.Icono;

/**
 * This class allows business logic of the icons used in the system menu
 * 
 * The logic is to see, edit, add or remove an icon
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class IconoAction implements Serializable {

	@EJB
	private GestionarIconoDao iconoDao;
	@EJB
	private GestionarMenuDao menuDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@Inject
	private IdentityAction identity;

	private List<Icono> iconos;

	private Paginador paginador = new Paginador();
	private Icono icono;

	private String carpetaArchivosReal;
	private String carpetaArchivosTemporal;
	private String nombreAchivoAntesEdicion;
	private String nombreIconoAntesEdicion;
	private String nombreBuscar;

	private boolean cargarFotoTemporal;

	/**
	 * 
	 * @return icono: Icon object that apply to permits
	 */
	public Icono getIcono() {
		return icono;
	}

	/**
	 * 
	 * @param icono
	 *            : Icon object that apply to permits
	 */
	public void setIcono(Icono icono) {
		this.icono = icono;
	}

	/**
	 * 
	 * @return iconos: List of icons that are loaded into the user interface.
	 */
	public List<Icono> getIconos() {
		return iconos;
	}

	/**
	 * 
	 * @param iconos
	 *            :List of icons that are loaded into the user interface.
	 */
	public void setIconos(List<Icono> iconos) {
		this.iconos = iconos;
	}

	/**
	 * 
	 * @return paginador: allows the handling of the pagination of table icons
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * 
	 * @param paginador
	 *            : allows the handling of the pagination of table icons
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return carpetaArchivosReal: The actual folder path where the menu icons
	 */
	public String getCarpetaArchivosReal() {
		this.carpetaArchivosReal = Constantes.RUTA_IMG
				+ Constantes.CARPETA_ICONOS_MENU_CABECERA;
		return carpetaArchivosReal;
	}

	/**
	 * @param carpetaArchivosReal
	 *            : The actual folder path where the menu icons
	 */
	public void setCarpetaArchivosReal(String carpetaArchivosReal) {
		this.carpetaArchivosReal = carpetaArchivosReal;
	}

	/**
	 * @return carpetaArchivosTemporal: path of the temporary folder where the
	 *         photos are loaded farm
	 */
	public String getCarpetaArchivosTemporal() {
		this.carpetaArchivosTemporal = Constantes.CARPETA_ARCHIVOS_TEMP;
		return carpetaArchivosTemporal;
	}

	/**
	 * 
	 * @return fileUploadBean: variable that gets the object for file uploads
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : variable that gets the object for file uploads
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return cargarFotoTemporal: Flag indicating whether the picture is loaded
	 *         from the temporary location or not
	 */
	public boolean isCargarFotoTemporal() {
		return cargarFotoTemporal;
	}

	/**
	 * @param cargarFotoTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not
	 */
	public void setCargarFotoTemporal(boolean cargarFotoTemporal) {
		this.cargarFotoTemporal = cargarFotoTemporal;
	}

	/**
	 * @return nombreBuscar: variable that gets the icon name is sought in the
	 *         user interface
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : variable that Sets the icon name is sought in the user
	 *            interface
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Initializes the name on the search icon.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultarIconos: Consultation icons in the system and returns to
	 *         the management template with search results.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarIconos();
	}

	/**
	 * Provides access existing icons in the database
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * 
	 * @return gesIcono: redirects to the Manage icon
	 */
	public String consultarIconos() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String mensajeBusqueda = "";
		iconos = new ArrayList<Icono>();
		try {
			validarIconosCarpeta();
			if (nombreBuscar != null && !"".equals(nombreBuscar)) {
				String nombreupperCase = nombreBuscar.toUpperCase();
				paginador.paginar(iconoDao
						.cantidadIconosXNombre(nombreupperCase));
				iconos = iconoDao.consultarIconosXNombrePaginador(
						nombreupperCase, paginador.getInicio(),
						paginador.getRango());
			} else {
				paginador.paginar(iconoDao.cantidadIconos());
				iconos = iconoDao.consultarIconos(paginador.getInicio(),
						paginador.getRango());
			}

			for (Icono i : iconos) {
				i.setMenus(menuDao.consultarMenuXIdIcono(i.getId()));
			}
			if ((iconos == null || iconos.size() <= 0)
					&& this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');

			} else if (iconos == null || iconos.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("icono_label_s"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesIcono";
	}

	/**
	 * Method used to read files and folder icons, but icons are in the table
	 * adds database records
	 * 
	 * @throws Exception
	 */
	public void validarIconosCarpeta() throws Exception {
		String ruta = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ getCarpetaArchivosReal();
		String rutaServidor = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosReal();
		File directorioIconos = new File(ruta);
		File[] listIconos = directorioIconos.listFiles();
		if (listIconos != null) {
			for (File file : listIconos) {
				String nombreIcono = file.getName();
				String ext = FilenameUtils.getExtension(nombreIcono);
				if (validarExtension(ext)) {
					Icono iconoExiste = iconoDao.nombreExiste(nombreIcono
							.toUpperCase());
					if (iconoExiste == null) {
						Icono nuevoIcono = new Icono();
						nuevoIcono.setFechaCreacion(new Date());
						nuevoIcono.setNombre(nombreIcono);
						nuevoIcono.setUserName(identity.getUserName());
						iconoDao.guardarIcono(nuevoIcono);
						File fileDestino = new File(rutaServidor, nombreIcono);
						FileUploadBean.copyFile(file, fileDestino);
					}
				}
			}
		}
	}

	/**
	 * Method to validate the extension of the icons in the folder Icons
	 * 
	 * @param ext
	 *            : file extension to be validated
	 * @return boolean to true if it is valid and false otherwise.
	 */
	private boolean validarExtension(String ext) {
		String extAceptadas[] = { "jpg", "jpeg", "bmp", "png", "gif" };
		boolean ban = false;
		for (String extAcep : extAceptadas) {
			if (extAcep.equals(ext)) {
				ban = true;
				break;
			}
		}
		return ban;
	}

	/**
	 * Method to load the template to add or edit an icon
	 * 
	 * @param icono
	 *            : Object of the icon you want to register or edit
	 * @return regIcono: page redirects to register the icon, which you can add
	 *         or edit an icon
	 */
	public String registrarIcono(Icono icono) {
		fileUploadBean = new FileUploadBean();
		if (icono != null) {
			this.icono = icono;
			fileUploadBean.setFileName(this.icono.getNombre());
			this.cargarFotoTemporal = false;
			nombreAchivoAntesEdicion = this.icono.getNombre();
			nombreIconoAntesEdicion = fileUploadBean.getFileName();
		} else {
			this.icono = new Icono();
			this.cargarFotoTemporal = true;
		}
		return "regIcono";
	}

	/**
	 * Method Allows save or edit an icon
	 * 
	 * @return: return to a page to register or manage icons according to what
	 *          happened.
	 */
	public String guardarIcono() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nomArchivoActual = this.fileUploadBean.getFileName();
		String nombreFotoBorrar = null;
		boolean seBorroIcono = false;
		boolean edicion = false;
		String key = "message_registro_guardar";
		try {
			icono.setUserName(identity.getUserName());
			if (icono.getId() != 0) {
				key = "message_registro_modificar";
				edicion = true;
				if (nombreIconoAntesEdicion != null
						&& !"".equals(nombreIconoAntesEdicion)
						&& !nombreIconoAntesEdicion.equals(nomArchivoActual)) {
					/* changes image */
					borrarArchivoReal(nombreIconoAntesEdicion);
					nombreFotoBorrar = nombreIconoAntesEdicion;
					seBorroIcono = true;
					/* renames the new image */
					if (nomValido()) {
						nomArchivoActual = renombrarArchivo(nomArchivoActual,
								icono.getNombre(), false);
					}
				} else if (nomValido()
						&& !icono.getNombre().equals(nombreAchivoAntesEdicion)) {
					/* changes the name, renames the icon */
					nomArchivoActual = renombrarArchivo(nomArchivoActual,
							icono.getNombre(), true);
				}
				icono.setNombre(nomArchivoActual);
				if (icono.getNombre() != null && seBorroIcono) {
					/* the image is load in a real path */
					subirImagenUbicacionReal();
				}
				iconoDao.editarIcono(icono);
			} else {
				if (nomValido() && !icono.getNombre().equals(nomArchivoActual)) {
					/* changes the name, renames the icon */
					nomArchivoActual = renombrarArchivo(nomArchivoActual,
							icono.getNombre(), false);
				}
				if (nomArchivoActual != null
						&& !"".equals(nomArchivoActual.trim())) {
					nombreFotoBorrar = nomArchivoActual;
					subirImagenUbicacionReal();
				}
				icono.setNombre(nomArchivoActual);
				icono.setFechaCreacion(new Date());
				iconoDao.guardarIcono(icono);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							icono.getNombre()));
			/* eliminates the temporal file */
			if (nombreFotoBorrar != null && !"".equals(nombreFotoBorrar)) {
				this.borrarArchivo(nombreFotoBorrar);
			}
		} catch (Exception e) {
			if (nomArchivoActual != null && !"".equals(nomArchivoActual)
					&& !edicion) {
				this.borrarArchivoReal(nomArchivoActual);
			}
			ControladorContexto.mensajeError(e);
			return "regIcono";
		}
		return consultarIconos();
	}

	/**
	 * Method to validate if the name is valid icon
	 * 
	 * @return boolean to true if valid or false otherwise
	 */
	private boolean nomValido() {
		return icono.getNombre() != null && !"".equals(icono.getNombre());
	}

	/**
	 * Method to rename the file in case of placing a name in the user interface
	 * 
	 * @param nomArchivoActual
	 *            :Name of the file to be loaded
	 * @param nombreIconoNuevo
	 *            : Name that is typed into the user interface and for which the
	 *            file is renamed
	 * @param edicion
	 *            : flag to see if the file is renamed in the two destinations
	 *            in case of editing
	 * @return String renamed icon
	 */
	private String renombrarArchivo(String nomArchivoActual,
			String nombreIconoNuevo, boolean edicion) {
		String[] splitNombreActual = nomArchivoActual.split("_");
		String[] splitNombreNuevo = nombreIconoNuevo.split("_");
		int lengthActual = splitNombreActual.length;
		/* The last string of the current example take 1331211123291.png */
		String splitActual = lengthActual > 1 ? splitNombreActual[lengthActual - 1]
				: nomArchivoActual;
		int lengthNuevo = splitNombreNuevo.length;
		/* Only the first word before _ */
		String splitNuevo = lengthNuevo > 0 ? splitNombreNuevo[0]
				: nombreIconoNuevo;
		String tamanyo = lengthNuevo > 1 ? "_" + splitNombreNuevo[1] + "_"
				: "_";
		/*
		 * SplitNuevo is concatenated to the number generated example
		 * 1331211123291.png
		 */
		String nombreReal = splitNuevo + tamanyo + splitActual;

		if (!nombreReal.equals(nomArchivoActual)) {
			String ubicaciones[] = new String[2];
			if (edicion) {
				ubicaciones[0] = Constantes.RUTA_UPLOADFILE_GLASFISH
						+ getCarpetaArchivosReal();
				ubicaciones[1] = Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ getCarpetaArchivosReal();
			} else {
				ubicaciones[0] = Constantes.RUTA_UPLOADFILE_GLASFISH
						+ getCarpetaArchivosTemporal();
			}
			boolean exito = false;
			for (String ubicacion : ubicaciones) {
				if (!"".equals(ubicacion)) {
					/* the file is rename */
					File fileNombreNuevo = new File(ubicacion, nombreReal);
					File fileOrigen = new File(ubicacion, nomArchivoActual);
					if (fileOrigen.exists()) {
						exito = fileOrigen.renameTo(fileNombreNuevo);
					}
				}
			}
			if (exito) {
				nomArchivoActual = nombreReal;
				fileUploadBean.setFileName(nombreReal);
			}
		}
		return nomArchivoActual;
	}

	/**
	 * allows load the icon image to the initially temporary folder.
	 * 
	 * @modify Hernan.Laguado 27/11/2014
	 * 
	 * @param e
	 *            : File upload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAceptadas[] = { "jpg", "jpeg", "bmp", "png", "gif" };
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		String resultUpload = fileUploadBean.upload(extAceptadas, ubicaciones);
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			ControladorContexto.mensajeError("iconoForm:uploadFile",
					bundle.getString("error_ext_invalida"));
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			ControladorContexto.mensajeError("iconoForm:uploadFile",
					bundle.getString("error_carga_archivo"));
		}
		if (icono.getId() != 0) {
			cargarFotoTemporal = true;
		}
	}

	/**
	 * Delete the file name.
	 * 
	 */
	public void borrarFilename() {
		if (fileUploadBean.getFileName() != null
				&& !"".equals(fileUploadBean.getFileName())
				&& !fileUploadBean.getFileName().equals(icono.getNombre())
				&& this.cargarFotoTemporal) {
			borrarArchivo(fileUploadBean.getFileName());
		}
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void borrarArchivo(String fileName) {
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Delete files from the actual location
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void borrarArchivoReal(String fileName) {
		String ubicaciones[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH
						+ this.getCarpetaArchivosReal(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ this.getCarpetaArchivosReal() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Add the image to the real folder
	 * 
	 * @throws Exception
	 */
	private void subirImagenUbicacionReal() throws Exception {
		String origen = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getCarpetaArchivosTemporal();
		String destino1 = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getCarpetaArchivosReal();
		String destino2 = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getCarpetaArchivosReal();

		/* Checks whether the destinations are created there but */
		FileUploadBean.fileExist(destino1);
		FileUploadBean.fileExist(destino2);

		File fileOrigen = new File(origen, fileUploadBean.getFileName());
		File fileDestino1 = new File(destino1, fileUploadBean.getFileName());
		File fileDestino2 = new File(destino2, fileUploadBean.getFileName());

		/* Copies of temporal at 2 real destinations */
		FileUploadBean.copyFile(fileOrigen, fileDestino1);
		FileUploadBean.copyFile(fileOrigen, fileDestino2);
	}

	/**
	 * Method that validates the required fields in the view
	 */
	public void requeridosOk() {
		try {
			if (fileUploadBean.getFileName() == null
					|| "".equals(fileUploadBean.getFileName())) {
				ControladorContexto.mensajeRequeridos("iconoForm:uploadFile");
			}
			/* Validation Scripting in the name of the file to upload */
			EncodeFilter.validarXSS(fileUploadBean.getFileName(),
					"iconoForm:uploadFile", null);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to remove the icons that are not associated with a menu
	 * 
	 * @return consultarIconos(): icons consulting method and returns to the
	 *         management of the icons
	 */
	public String eliminarIcono() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		boolean existe = false;
		try {
			if (this.icono != null) {
				String ubicaciones[] = {
						Constantes.RUTA_UPLOADFILE_GLASFISH
								+ this.getCarpetaArchivosReal(),
						Constantes.RUTA_UPLOADFILE_WORKSPACE
								+ this.getCarpetaArchivosReal() };
				for (String ubicacion : ubicaciones) {
					File fileExiste = new File(ubicacion, icono.getNombre());
					if (fileExiste.exists()) {
						existe = true;
					}
				}
				if (existe) {
					borrarArchivoReal(icono.getNombre());
					icono.setUserName(identity.getUserName());
					iconoDao.eliminarIcono(icono);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle
									.getString("message_registro_eliminar"),
									icono.getNombre()));
				} else {
					ControladorContexto.mensajeError(MessageFormat.format(
							bundleSeguridad
									.getString("icono_message_no_existe"),
							icono.getNombre()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarIconos();
	}
}
