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
 * This class allows business logic of the icons used in the system menu.
 * 
 * The logic is to see, edit, add or remove an icon.
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

	private List<Icono> icons;

	private Paginador paginador = new Paginador();
	private Icono icon;

	private String folderFilesReal;
	private String folderFilesTemporal;
	private String nameFileBeforeEdition;
	private String nameIconBeforeEdition;
	private String nameSearch;

	private boolean loadPhotoTemporal;

	/**
	 * 
	 * @return icon: Icon object that apply to permits.
	 */
	public Icono getIcon() {
		return icon;
	}

	/**
	 * 
	 * @param icon
	 *            : Icon object that apply to permits.
	 */
	public void setIcon(Icono icon) {
		this.icon = icon;
	}

	/**
	 * 
	 * @return icons: List of icons that are loaded into the user interface.
	 */
	public List<Icono> getIcons() {
		return icons;
	}

	/**
	 * 
	 * @param icons
	 *            :List of icons that are loaded into the user interface.
	 */
	public void setIcons(List<Icono> icons) {
		this.icons = icons;
	}

	/**
	 * 
	 * @return paginador: allows the handling of the pagination of table icons.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * 
	 * @param paginador
	 *            : allows the handling of the pagination of table icons.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return folderFilesReal: The actual folder path where the menu icons.
	 */
	public String getFolderFilesReal() {
		this.folderFilesReal = Constantes.RUTA_IMG
				+ Constantes.CARPETA_ICONOS_MENU_CABECERA;
		return folderFilesReal;
	}

	/**
	 * @param folderFilesReal
	 *            : The actual folder path where the menu icons.
	 */
	public void setFolderFilesReal(String folderFilesReal) {
		this.folderFilesReal = folderFilesReal;
	}

	/**
	 * @return folderFilesTemporal: path of the temporary folder where the
	 *         photos are loaded farm.
	 */
	public String getFolderFilesTemporal() {
		this.folderFilesTemporal = Constantes.CARPETA_ARCHIVOS_TEMP;
		return folderFilesTemporal;
	}

	/**
	 * 
	 * @return fileUploadBean: variable that gets the object for file uploads.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : variable that gets the object for file uploads.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return loadPhotoTemporal: Flag indicating whether the picture is loaded
	 *         from the temporary location or not.
	 */
	public boolean isLoadPhotoTemporal() {
		return loadPhotoTemporal;
	}

	/**
	 * @param loadPhotoTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not.
	 */
	public void setLoadPhotoTemporal(boolean loadPhotoTemporal) {
		this.loadPhotoTemporal = loadPhotoTemporal;
	}

	/**
	 * @return nameSearch: variable that gets the icon name is sought in the
	 *         user interface.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : variable that Sets the icon name is sought in the user
	 *            interface.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Initializes the name on the search icon.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultIconos: Consultation icons in the system and returns to
	 *         the management template with search results.
	 */
	public String searchInitialization() {
		this.nameSearch = "";
		return consultIcons();
	}

	/**
	 * Provides access existing icons in the database.
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * 
	 * @return gesIcono: redirects to the Manage icon.
	 */
	public String consultIcons() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String messageSearch = "";
		icons = new ArrayList<Icono>();
		try {
			validateIconsFolder();
			if (nameSearch != null && !"".equals(nameSearch)) {
				String nameUpperCase = nameSearch.toUpperCase();
				paginador.paginar(iconoDao.quantityIconsXName(nameUpperCase));
				icons = iconoDao.consultIconsXNamePaginator(nameUpperCase,
						paginador.getInicio(), paginador.getRango());
			} else {
				paginador.paginar(iconoDao.quantityIcons());
				icons = iconoDao.consultIcons(paginador.getInicio(),
						paginador.getRango());
			}

			for (Icono i : icons) {
				i.setMenus(menuDao.consultarMenuXIdIcono(i.getId()));
			}
			if ((icons == null || icons.size() <= 0) && this.nameSearch != null
					&& !"".equals(this.nameSearch)) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nameSearch + '"');

			} else if (icons == null || icons.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("icon_label_s"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nameSearch + '"');
			}
			validations.setMensajeBusqueda(messageSearch);

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesIcono";
	}

	/**
	 * Method used to read files and folder icons, but icons are in the table
	 * adds database records.
	 * 
	 * @throws Exception
	 */
	public void validateIconsFolder() throws Exception {
		String route = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ getFolderFilesReal();
		String routeServer = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFilesReal();
		File directoryIcons = new File(route);
		File[] listIcons = directoryIcons.listFiles();
		if (listIcons != null) {
			for (File file : listIcons) {
				String nameIcon = file.getName();
				String ext = FilenameUtils.getExtension(nameIcon);
				if (validateExtension(ext)) {
					Icono iconExist = iconoDao
							.nameExist(nameIcon.toUpperCase());
					if (iconExist == null) {
						Icono newIcon = new Icono();
						newIcon.setFechaCreacion(new Date());
						newIcon.setNombre(nameIcon);
						newIcon.setUserName(identity.getUserName());
						iconoDao.saveIcon(newIcon);
						File fileDestiny = new File(routeServer, nameIcon);
						FileUploadBean.copyFile(file, fileDestiny);
					}
				}
			}
		}
	}

	/**
	 * Method to validate the extension of the icons in the folder Icons.
	 * 
	 * @param ext
	 *            : file extension to be validated.
	 * @return boolean to true if it is valid and false otherwise.
	 */
	private boolean validateExtension(String ext) {
		String extAccepted[] = { "jpg", "jpeg", "bmp", "png", "gif" };
		boolean ban = false;
		for (String extAcep : extAccepted) {
			if (extAcep.equals(ext)) {
				ban = true;
				break;
			}
		}
		return ban;
	}

	/**
	 * Method to load the template to add or edit an icon.
	 * 
	 * @param icon
	 *            : Object of the icon you want to register or edit.
	 * @return regIcono: page redirects to register the icon, which you can add
	 *         or edit an icon.
	 */
	public String registerIcon(Icono icon) {
		fileUploadBean = new FileUploadBean();
		if (icon != null) {
			this.icon = icon;
			fileUploadBean.setFileName(this.icon.getNombre());
			this.loadPhotoTemporal = false;
			nameFileBeforeEdition = this.icon.getNombre();
			nameIconBeforeEdition = fileUploadBean.getFileName();
		} else {
			this.icon = new Icono();
			this.loadPhotoTemporal = true;
		}
		return "regIcono";
	}

	/**
	 * Method Allows save or edit an icon.
	 * 
	 * @return: return to a page to register or manage icons according to what
	 *          happened.
	 */
	public String saveIcon() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nameFileActual = this.fileUploadBean.getFileName();
		String namePhotoDelete = null;
		boolean erasedIcon = false;
		boolean edition = false;
		String key = "message_registro_guardar";
		try {
			icon.setUserName(identity.getUserName());
			if (icon.getId() != 0) {
				key = "message_registro_modificar";
				edition = true;
				if (nameIconBeforeEdition != null
						&& !"".equals(nameIconBeforeEdition)
						&& !nameIconBeforeEdition.equals(nameFileActual)) {
					/* changes image */
					deleteFileReal(nameIconBeforeEdition);
					namePhotoDelete = nameIconBeforeEdition;
					erasedIcon = true;
					/* renames the new image */
					if (namValid()) {
						nameFileActual = renameFile(nameFileActual,
								icon.getNombre(), false);
					}
				} else if (namValid()
						&& !icon.getNombre().equals(nameFileBeforeEdition)) {
					/* changes the name, renames the icon */
					nameFileActual = renameFile(nameFileActual,
							icon.getNombre(), true);
				}
				icon.setNombre(nameFileActual);
				if (icon.getNombre() != null && erasedIcon) {
					/* the image is load in a real path */
					subirImageLocationReal();
				}
				iconoDao.editIcon(icon);
			} else {
				if (namValid() && !icon.getNombre().equals(nameFileActual)) {
					/* changes the name, renames the icon */
					nameFileActual = renameFile(nameFileActual,
							icon.getNombre(), false);
				}
				if (nameFileActual != null && !"".equals(nameFileActual.trim())) {
					namePhotoDelete = nameFileActual;
					subirImageLocationReal();
				}
				icon.setNombre(nameFileActual);
				icon.setFechaCreacion(new Date());
				iconoDao.saveIcon(icon);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							icon.getNombre()));
			/* eliminates the temporal file */
			if (namePhotoDelete != null && !"".equals(namePhotoDelete)) {
				this.deleteFile(namePhotoDelete);
			}
		} catch (Exception e) {
			if (nameFileActual != null && !"".equals(nameFileActual)
					&& !edition) {
				this.deleteFileReal(nameFileActual);
			}
			ControladorContexto.mensajeError(e);
			return "regIcono";
		}
		return consultIcons();
	}

	/**
	 * Method to validate if the name is valid icon.
	 * 
	 * @return boolean to true if valid or false otherwise.
	 */
	private boolean namValid() {
		return icon.getNombre() != null && !"".equals(icon.getNombre());
	}

	/**
	 * Method to rename the file in case of placing a name in the user interface.
	 * 
	 * @param namFileActual
	 *            :Name of the file to be loaded.
	 * @param nameIconNew
	 *            : Name that is typed into the user interface and for which the
	 *            file is renamed.
	 * @param edition
	 *            : flag to see if the file is renamed in the two destinations
	 *            in case of editing.
	 * @return String renamed icon.
	 */
	private String renameFile(String namFileActual, String nameIconNew,
			boolean edition) {
		String[] splitNameActual = namFileActual.split("_");
		String[] splitNameNew = nameIconNew.split("_");
		int lengthActual = splitNameActual.length;
		/* The last string of the current example take 1331211123291.png */
		String splitActual = lengthActual > 1 ? splitNameActual[lengthActual - 1]
				: namFileActual;
		int lengthNew = splitNameNew.length;
		/* Only the first word before _ */
		String splitNew = lengthNew > 0 ? splitNameNew[0] : nameIconNew;
		String size = lengthNew > 1 ? "_" + splitNameNew[1] + "_" : "_";
		/*
		 * SplitNuevo is concatenated to the number generated example
		 * 1331211123291.png
		 */
		String nameReal = splitNew + size + splitActual;

		if (!nameReal.equals(namFileActual)) {
			String locations[] = new String[2];
			if (edition) {
				locations[0] = Constantes.RUTA_UPLOADFILE_GLASFISH
						+ getFolderFilesReal();
				locations[1] = Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ getFolderFilesReal();
			} else {
				locations[0] = Constantes.RUTA_UPLOADFILE_GLASFISH
						+ getFolderFilesTemporal();
			}
			boolean success = false;
			for (String location : locations) {
				if (!"".equals(location)) {
					/* the file is rename */
					File fileNameNew = new File(location, nameReal);
					File fileOrigin = new File(location, namFileActual);
					if (fileOrigin.exists()) {
						success = fileOrigin.renameTo(fileNameNew);
					}
				}
			}
			if (success) {
				namFileActual = nameReal;
				fileUploadBean.setFileName(nameReal);
			}
		}
		return namFileActual;
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
		String extAccepted[] = { "jpg", "jpeg", "bmp", "png", "gif" };
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFilesTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		String resultUpload = fileUploadBean.upload(extAccepted, locations);
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			ControladorContexto.mensajeError("iconoForm:uploadFile",
					bundle.getString("error_ext_invalida"));
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			ControladorContexto.mensajeError("iconoForm:uploadFile",
					bundle.getString("error_carga_archivo"));
		}
		if (icon.getId() != 0) {
			loadPhotoTemporal = true;
		}
	}

	/**
	 * Delete the file name.
	 * 
	 */
	public void deleteFileName() {
		if (fileUploadBean.getFileName() != null
				&& !"".equals(fileUploadBean.getFileName())
				&& !fileUploadBean.getFileName().equals(icon.getNombre())
				&& this.loadPhotoTemporal) {
			deleteFile(fileUploadBean.getFileName());
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
	public void deleteFile(String fileName) {
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFilesTemporal() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Delete files from the actual location.
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void deleteFileReal(String fileName) {
		String locations[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH + this.getFolderFilesReal(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ this.getFolderFilesReal() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * Add the image to the real folder
	 * 
	 * @throws Exception
	 */
	private void subirImageLocationReal() throws Exception {
		String origin = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFolderFilesTemporal();
		String destiny1 = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFolderFilesReal();
		String destiny2 = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getFolderFilesReal();

		/* Checks whether the destinations are created there but */
		FileUploadBean.fileExist(destiny1);
		FileUploadBean.fileExist(destiny2);

		File fileOrigin = new File(origin, fileUploadBean.getFileName());
		File fileDestiny1 = new File(destiny1, fileUploadBean.getFileName());
		File fileDestiny2 = new File(destiny2, fileUploadBean.getFileName());

		/* Copies of temporal at 2 real destinations */
		FileUploadBean.copyFile(fileOrigin, fileDestiny1);
		FileUploadBean.copyFile(fileOrigin, fileDestiny2);
	}

	/**
	 * Method that validates the required fields in the view
	 */
	public void requiredOk() {
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
	 * @return consultIcons(): icons consulting method and returns to the
	 *         management of the icons
	 */
	public String deleteIcon() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		boolean exist = false;
		try {
			if (this.icon != null) {
				String locations[] = {
						Constantes.RUTA_UPLOADFILE_GLASFISH
								+ this.getFolderFilesReal(),
						Constantes.RUTA_UPLOADFILE_WORKSPACE
								+ this.getFolderFilesReal() };
				for (String location : locations) {
					File fileExist = new File(location, icon.getNombre());
					if (fileExist.exists()) {
						exist = true;
					}
				}
				if (exist) {
					deleteFileReal(icon.getNombre());
					icon.setUserName(identity.getUserName());
					iconoDao.removeIcon(icon);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle
									.getString("message_registro_eliminar"),
									icon.getNombre()));
				} else {
					ControladorContexto
							.mensajeError(MessageFormat.format(bundleSeguridad
									.getString("icon_message_not_exist"), icon
									.getNombre()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultIcons();
	}
}
