package co.informatix.erp.lifeCycle.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the logic business related to the view and the
 * database.
 * 
 * The logic is to query, edit ,or add farm objects.
 * 
 * @author Johnatan.Naranjo
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class FarmAction implements Serializable {
	@EJB
	private FarmDao farmDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;

	private Farm farm;
	private Paginador pagination = new Paginador();

	private String nameSearch;
	private String filesFolder;
	private String temporalFilesFolder;
	private String logoPicName;
	private boolean temporalPicLoading;

	private List<Farm> farmsList;
	private List<SelectItem> countryItems;
	private List<SelectItem> departmentItems;
	private List<SelectItem> municipalityItems;

	/**
	 * Gets data of a farm.
	 * 
	 * @return Farm: Object containing the data of a farm.
	 */
	public Farm getFarm() {
		return farm;
	}

	/**
	 * Sets data of a farm.
	 * 
	 * @param farm
	 *            : Object containing the data of a farm.
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	/**
	 * @return Paginador: Management paged list for estates.
	 * 
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list for estates.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Name of the farm to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name of the farm to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return List<Farm>: List of farms that are displayed in the user
	 *         interface.
	 */
	public List<Farm> getFarmsList() {
		return farmsList;
	}

	/**
	 * @param farmsList
	 *            : List of farms that are displayed in the user interface.
	 */
	public void setFarmsList(List<Farm> farmsList) {
		this.farmsList = farmsList;
	}

	/**
	 * @return countryItems: Items of the countries that are displayed in a
	 *         combo in the user interface.
	 */
	public List<SelectItem> getCountryItems() {
		return countryItems;
	}

	/**
	 * @return departmentItems: Items of the countries that are displayed in a
	 *         combo in the user interface.
	 */
	public List<SelectItem> getDepartmentItems() {
		return departmentItems;
	}

	/**
	 * @return municipalityItems: Items of the municipalities that are displayed
	 *         in a combo in the user interface.
	 */
	public List<SelectItem> getMunicipalityItems() {
		return municipalityItems;
	}

	/**
	 * @return logoPicName: Items of the municipalities that are displayed in a
	 *         combo in the user interface.
	 */
	public String getLogoPicName() {
		return logoPicName;
	}

	/**
	 * @param logoPicName
	 *            : Name of a logo-picture.
	 */
	public void setLogoPicName(String logoPicName) {
		this.logoPicName = logoPicName;
	}

	/**
	 * @return filesFolder: Route for the real folder where pictures of farm
	 *         logos are loaded.
	 */
	public String getFilesFolder() {
		this.filesFolder = Constantes.FOLDER_FILES
				+ Constantes.FOLDER_FILES_FARMS;
		return filesFolder;
	}

	/**
	 * @return temporalFilesFolder: Path to the temporary folder where the farm
	 *         logos are loaded.
	 */
	public String getTemporalFilesFolder() {
		this.temporalFilesFolder = Constantes.FOLDER_FILES
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return temporalFilesFolder;
	}

	/**
	 * 
	 * @return fileUploadBean: Variable that gets the object for uploading
	 *         files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : Variable that gets the object for uploading files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return temporalPicLoading: Flag indicating whether the picture is loaded
	 *         from the temporary location or not.
	 */
	public boolean isTemporalPicLoading() {
		return temporalPicLoading;
	}

	/**
	 * @param temporalPicLoading
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not.
	 */
	public void setTemporalPicLoading(boolean temporalPicLoading) {
		this.temporalPicLoading = temporalPicLoading;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the farms.
	 * 
	 * @return searchFarms: Estates query method that returns to the template
	 *         management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return searchFarms();
	}

	/**
	 * Consult the list of farms.
	 * 
	 * @modify 09/03/2016 Sergio.Gelves
	 * 
	 * @return "gesFarm": Redirects to manage farms.
	 */
	public String searchFarms() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validate = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		farmsList = new ArrayList<Farm>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointQueryMessage = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(queryBuilder, parameters, bundle, jointQueryMessage);
			Long amount = farmDao.amountFarms(queryBuilder, parameters);
			if (amount != null) {
				pagination.paginar(amount);
			}
			farmsList = farmDao.searchFarms(pagination.getInicio(),
					pagination.getRango(), queryBuilder, parameters);
			if ((farmsList == null || farmsList.size() <= 0)
					&& !"".equals(jointQueryMessage.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointQueryMessage);
			} else if (farmsList == null || farmsList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointQueryMessage.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("farm_label_s"),
								jointQueryMessage);
				loadFarmDetails();
			}
			validate.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesFarm";
	}

	/**
	 * This method builds a query with advanced search; it also render the
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param queryBuilder
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param searchMessage
	 *            : Message search.
	 * 
	 */
	private void advancedSearch(StringBuilder queryBuilder,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder searchMessage) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			queryBuilder.append("WHERE UPPER(f.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			searchMessage.append(bundle.getString("label_name") + ": " + '"'
					+ this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new farm.
	 * 
	 * @modify 09/03/2016 Sergio.Gelves
	 * 
	 * @param farm
	 *            : Property that you are adding or editing.
	 * 
	 * @return "regFarm": Redirects to the record template farm.
	 */
	public String addEditFarm(Farm farm) {
		try {
			if (farm != null) {
				this.farm = farm;
				// Managing fetch.LAZY for foreign keys
				if (this.farm.getPais() != null) {
					Pais knownCountry;

					knownCountry = paisDao
							.consultarPais(farm.getPais().getId());

					this.farm.setPais(knownCountry);
				} else {
					this.farm.setPais(new Pais());
				}
				if (this.farm.getDepartamento() != null) {
					Departamento knownDepartment = departamentoDao
							.consultarDepartamentoXId(farm.getDepartamento()
									.getId());
					this.farm.setDepartamento(knownDepartment);
				} else {
					this.farm.setDepartamento(new Departamento());
				}
				if (this.farm.getMunicipio() != null) {
					Municipio knownMunicipality = municipioDao
							.consultarMunicipio(farm.getMunicipio().getId());
					this.farm.setMunicipio(knownMunicipality);
				} else {
					this.farm.setMunicipio(new Municipio());
				}

				this.logoPicName = this.farm.getLogo();
				this.temporalPicLoading = false;
			} else {
				this.farm = new Farm();
				this.farm.setPais(new Pais());
				this.farm.setDepartamento(new Departamento());
				this.farm.setMunicipio(new Municipio());
				this.logoPicName = null;
				this.fileUploadBean = new FileUploadBean();
				this.temporalPicLoading = true;
			}
			loadComboBoxes();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regFarm";
	}

	/**
	 * Method used to save or edit the properties.
	 * 
	 * @return searchFarms: Redirects to manage farms with updated list of
	 *         farms.
	 */
	public String saveFarm() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		boolean isLogoChanged = false;
		String deletePicName = null;
		try {
			/* They are not required fields */
			if (this.farm.getPais().getId() == 0) {
				this.farm.setPais(null);
			}
			if (this.farm.getDepartamento().getId() == 0) {
				this.farm.setDepartamento(null);
			}
			if (this.farm.getMunicipio().getId() == 0) {
				this.farm.setMunicipio(null);
			}
			if (farm.getIdFarm() != 0) {
				if (this.farm.getLogo() != null
						&& !"".equals(this.farm.getLogo())
						&& !this.farm.getLogo().equals(this.logoPicName)) {
					this.deleteRealFile(this.farm.getLogo());
					isLogoChanged = true;
				} else if (farm.getLogo() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					isLogoChanged = true;
				}
				this.farm.setLogo(this.logoPicName);
				if (farm.getLogo() != null && isLogoChanged) {
					deletePicName = this.logoPicName;
					/* The image is loaded into the actual location */
					uploadPicRealFolder();
				}
				farmDao.editFarm(farm);
			} else {
				if (this.logoPicName != null
						&& !"".equals(this.logoPicName.trim())) {
					deletePicName = this.logoPicName;
					uploadPicRealFolder();
				}
				this.farm.setLogo(this.logoPicName);
				registerMessage = "message_registro_guardar";
				farmDao.saveFarm(farm);
			}
			/* The temporary file is deleted */
			if (deletePicName != null && !"".equals(deletePicName)) {
				this.deleteFile(deletePicName);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), farm.getName()));
		} catch (Exception e) {
			if (this.logoPicName != null && !"".equals(this.logoPicName)
					&& farm.getIdFarm() == 0) {
				this.deleteRealFile(this.logoPicName);
			}
			ControladorContexto.mensajeError(e);
		}
		return searchFarms();
	}

	/**
	 * To validate the name of the farm, so it is not repeated in the database
	 * and valid against XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * 
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = farm.getIdFarm();
			Farm auxFarm = farmDao.nameExists(name, id);
			if (auxFarm != null) {
				String existenceMessage = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(existenceMessage), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(name, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows you to load combo country, department and
	 * municipality.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @throws Exception
	 * 
	 */
	private void loadComboBoxes() throws Exception {
		countryItems = new ArrayList<SelectItem>();
		List<Pais> countries = paisDao.consultarPaisesVigentes();
		if (countries != null) {
			for (Pais country : countries) {
				countryItems.add(new SelectItem(country.getId(), country
						.getNombre()));
			}
		}
		loadDepartments();
	}

	/**
	 * This method performs the query of the departments registered in the
	 * database, associated with a selected country.
	 * 
	 * @author Cristhian.Pico
	 * 
	 */
	public void loadDepartments() {
		departmentItems = new ArrayList<SelectItem>();
		try {
			Pais country = farm.getPais();
			if (country != null && country.getId() > 0) {
				short countryId = country.getId();
				List<Departamento> departments = departamentoDao
						.consultarDepartamentosPaisVigentes(countryId);
				if (departments != null) {
					for (Departamento d : departments) {
						departmentItems.add(new SelectItem(d.getId(), d
								.getNombre()));
					}
				}
				loadMunicipalities();
			} else {
				farm.setDepartamento(new Departamento());
				farm.setMunicipio(new Municipio());
				farm.getDepartamento().setId(0);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered in the
	 * database, associated with a department selected a partner country.
	 * 
	 * @author Cristhian.Pico
	 * 
	 */
	public void loadMunicipalities() {
		municipalityItems = new ArrayList<SelectItem>();
		try {
			Departamento department = farm.getDepartamento();
			if (department != null && department.getId() > 0
					&& this.departmentItems.size() > 0) {
				int departmentId = department.getId();
				List<Municipio> municipalities = municipioDao
						.consultarMunicipiosVigentes(departmentId);
				if (municipalities != null) {
					for (Municipio m : municipalities) {
						municipalityItems.add(new SelectItem(m.getId(), m
								.getNombre()));
					}
				}
			} else {
				municipalityItems = new ArrayList<SelectItem>();
				farm.setDepartamento(new Departamento());
				farm.setMunicipio(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method fills the various objects associated to a farm.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @throws Exception
	 */
	private void loadFarmDetails() throws Exception {
		List<Farm> farms = new ArrayList<Farm>();
		farms.addAll(this.farmsList);
		this.farmsList = new ArrayList<Farm>();
		for (Farm farm : farms) {
			loadFarmDetails(farm);
			this.farmsList.add(farm);
		}
	}

	/**
	 * Method to upload the details of a farm.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param farm
	 *            : Farm to which the details will be loaded.
	 * @throws Exception
	 */
	private void loadFarmDetails(Farm farm) throws Exception {
		int farmId = farm.getIdFarm();
		Pais country = (Pais) this.farmDao.searchFarmObject("pais", farmId);
		Departamento department = (Departamento) this.farmDao.searchFarmObject(
				"departamento", farmId);
		Municipio municipality = (Municipio) this.farmDao.searchFarmObject(
				"municipio", farmId);
		farm.setPais(country);
		farm.setDepartamento(department);
		farm.setMunicipio(municipality);
	}

	/**
	 * Delete the file name.
	 * 
	 * @author Cristhian.Pico
	 */
	public void deleteFileName() {
		if (logoPicName != null && !"".equals(logoPicName)
				&& !logoPicName.equals(farm.getLogo())
				&& this.temporalPicLoading) {
			deleteFile(logoPicName);
		}
		logoPicName = null;
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void deleteFile(String fileName) {
		String paths[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getTemporalFilesFolder() };
		fileUploadBean.delete(paths, fileName);
	}

	/**
	 * Delete files from the actual location.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void deleteRealFile(String fileName) {
		String paths[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH + this.getFilesFolder(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE + this.getFilesFolder() };
		fileUploadBean.delete(paths, fileName);
	}

	/**
	 * Method allows you to load the file system.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param e
	 *            : Fileupload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String allowedExt[] = Constantes.EXT_IMG.split(", ");
		String paths[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getTemporalFilesFolder() };
		fileUploadBean.setUploadedFile(e.getFile());
		long maxFileSize = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(allowedExt,
				paths, maxFileSize);
		String message = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			message = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"), maxFileSize,
					"MB");
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			message = "error_carga_archivo";
		}
		if (!"".equals(message)) {
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					bundle.getString(message));
		}
		if (farm.getIdFarm() != 0) {
			temporalPicLoading = true;
		}
		logoPicName = fileUploadBean.getFileName();
	}

	/**
	 * Upload the logo image to the actual folder.
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @throws Exception
	 */
	private void uploadPicRealFolder() throws Exception {
		String source = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getTemporalFilesFolder();
		String firstDestFolder = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFilesFolder();
		String secondDestFolder = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getFilesFolder();

		/* It checks whether the destinations are created there but */
		FileUploadBean.fileExist(firstDestFolder);
		FileUploadBean.fileExist(secondDestFolder);

		File sourceFile = new File(source, fileUploadBean.getFileName());
		File firstFolderFile = new File(firstDestFolder,
				fileUploadBean.getFileName());
		File secondFolderFile = new File(secondDestFolder,
				fileUploadBean.getFileName());

		/* Copies of temporal at 2 real destinations */
		FileUploadBean.copyFile(sourceFile, firstFolderFile);
		FileUploadBean.copyFile(sourceFile, secondFolderFile);
	}

	/**
	 * Method for removing a farm of the database.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return searchFarms: See the list of the properties and returns to manage
	 *         finances.
	 */
	public String deleteFarm() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			farmDao.deleteFarm(farm);
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(
							bundle.getString("message_registro_eliminar"),
							farm.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					farm.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return searchFarms();
	}
}
