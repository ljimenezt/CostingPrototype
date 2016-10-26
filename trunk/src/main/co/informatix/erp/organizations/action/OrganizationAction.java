package co.informatix.erp.organizations.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.text.WordUtils;
import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.humanResources.entities.Person;
import co.informatix.erp.informacionBase.dao.TipoDocumentoDao;
import co.informatix.erp.informacionBase.entities.TipoDocumento;
import co.informatix.erp.organizations.dao.OrganizationDao;
import co.informatix.erp.organizations.entities.Organization;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class allows business logic of the application organizations, as well as
 * people who are in the organization
 * 
 * The logic is to see, edit, add or change the validity of an organization and
 * add people to the organization, terminate the contract or term of the person
 * in the organization.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class OrganizationAction implements Serializable {

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	@EJB
	private OrganizationDao organizationDao;

	@EJB
	private TipoDocumentoDao typeDocumentDao;
	@EJB
	private FileUploadBean fileUploadBean;

	private List<Organization> organizations;
	private List<Person> persons;
	private HashMap<String, Short> itemsTypeDocuments;
	private Paginador pagination = new Paginador();
	private Organization organizationModifyValidation;
	private Organization organization;
	private Person person;
	private String vigencia = Constantes.SI;

	private String nameSearch;
	private String fileFolder;
	private String fileFolderTemporary;
	private String filterSearch;
	private boolean loadPhotoTemporary;

	/**
	 * Gets the object reference where the organization is positioned to be the
	 * change the current
	 * 
	 * @return organizationModifyValidation: object reference to the
	 *         organization that you are going to change the current
	 */
	public Organization getOrganizationModifyValidation() {
		return organizationModifyValidation;
	}

	/**
	 * Sets the object reference where the organization is positioned to be the
	 * change the current
	 * 
	 * @param organizationModifyValidation
	 *            : object reference to the organization that you are going to
	 *            change the current
	 */
	public void setOrganizationModifyValidation(
			Organization organizationModifyValidation) {
		this.organizationModifyValidation = organizationModifyValidation;
	}

	/**
	 * @return fileFolder: Variable that gets the path to the folder where the
	 *         logo of the organization are stored
	 */
	public String getFileFolder() {
		this.fileFolder = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_LOGOS_ORGANIZACIONES;
		return fileFolder;
	}

	/**
	 * @param fileFolder
	 *            :Variable that gets the path to the folder where the logo of
	 *            the organization are stored
	 */
	public void setFileFolder(String fileFolder) {
		this.fileFolder = fileFolder;
	}

	/**
	 * @return fileFolderTemporary: path of the temporary folder where logo or
	 *         photos of the organization are loaded.
	 */
	public String getFileFolderTemporary() {
		this.fileFolderTemporary = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return fileFolderTemporary;
	}

	/**
	 * @return loadPhotoTemporary: Flag indicating whether the picture is loaded
	 *         from the temporary location or not
	 */
	public boolean isLoadPhotoTemporary() {
		return loadPhotoTemporary;
	}

	/**
	 * @param loadPhotoTemporary
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not
	 */
	public void setLoadPhotoTemporary(boolean loadPhotoTemporary) {
		this.loadPhotoTemporary = loadPhotoTemporary;
	}

	/**
	 * @return filterSearch: Filter search of organizations
	 */
	public String getFilterSearch() {
		return filterSearch;
	}

	/**
	 * @param filterSearch
	 *            : Filter search of organizations
	 */
	public void setFilterSearch(String filterSearch) {
		this.filterSearch = filterSearch;
	}

	/**
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
	 * @return organization: Object representing the Organization
	 */
	public Organization getOrganization() {
		return organization;
	}

	/**
	 * @param organization
	 *            : Object representing the Organization
	 */
	public void setOrganization(Organization organization) {
		this.organization = organization;
	}

	/**
	 * @return organizations: Object list of organizations that are loaded into
	 *         the interface table.
	 */
	public List<Organization> getOrganizations() {
		return organizations;
	}

	/**
	 * @param organizations
	 *            : Object list of organizations that are loaded into the
	 *            interface table.
	 */
	public void setOrganizations(List<Organization> organizations) {
		this.organizations = organizations;
	}

	/**
	 * @return pagination: Pager object list of Organizations
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :Pager object list of Organizations
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return vigencia: It is giving the selected value 'yes' of existing and
	 *         'no' for not applicable
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : It is giving the selected value 'yes' of existing and 'no'
	 *            for not applicable
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return nameSearch: Name to search for the person who wants to
	 *         associate with the organization
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name to search for the person who wants to associate with
	 *            the organization
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return persons: List of persons encountered by name
	 */
	public List<Person> getPersons() {
		return persons;
	}

	/**
	 * @param persons
	 *            : List of persons encountered by name
	 */
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	/**
	 * @return person: get the person associated with the organization.
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            : set the person associated with the organization.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * List of items that get the document types in the combo of the user
	 * interface
	 * 
	 * @return itemsTypeDocuments : Items of the types of documents that are
	 *         displayed in the user interface
	 */
	public HashMap<String, Short> getItemsTypeDocuments() {
		return itemsTypeDocuments;
	}

	/**
	 * List of items that establishes the types of documents in the combo of the
	 * user interface
	 * 
	 * @param itemsTypeDocuments
	 *            : Items of the types of documents that are displayed in the
	 *            user interface
	 */
	public void setItemsTypeDocuments(
			HashMap<String, Short> itemsTypeDocuments) {
		this.itemsTypeDocuments = itemsTypeDocuments;
	}

	/**
	 * Initializes search parameters and load the initial list of organizations
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @return consultOrganizations(): method consulting organizations of the
	 *         system and returns to the template management.
	 */
	public String searchInitialize() {
		this.filterSearch = "";
		return this.consultOrganizations();
	}

	/**
	 * Allows to consult existing organizations in the database according to
	 * user is asked: current or not current
	 * 
	 * @modify Liseth.Jimenez 11/07/2012
	 * 
	 * @return gesOrganization: Navigation rule that redirects to the Manage organization
	 */
	public String consultOrganizations() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("messageOrganizations");
		String messageSearch = "";
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			String cadena = bundle.getString("label_razon_social") + " / "
					+ bundle.getString("label_identification");
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			Long cantidadOrganizaciones = organizationDao.amountOrganizations(
					condicionVigencia, this.filterSearch);
			if (cantidadOrganizaciones != null) {
				pagination.paginar(cantidadOrganizaciones);
			}
			organizations = organizationDao.consultOrganizations(
					pagination.getInicio(), pagination.getRango(),
					condicionVigencia, this.filterSearch);

			if ((this.organizations == null || this.organizations.size() <= 0)
					&& (this.filterSearch != null && !""
							.equals(this.filterSearch))) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								cadena + ": " + '"' + this.filterSearch + '"');
			} else if (this.organizations == null
					|| this.organizations.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.filterSearch != null
					&& !"".equals(this.filterSearch)) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleOrg.getString("organization_label_s"),
								cadena + ": " + '"' + this.filterSearch + '"');
			}
			validaciones.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesOrganization";
	}

	/**
	 * Method that allow load the user interface for editing or registration of
	 * an organization with its affiliated persons.
	 * 
	 * @param organization
	 *            : object of the organization that is loaded into the template
	 *            if editing.
	 * @return regOrganization: rule navigation that redirects to register the
	 *         organization, which is loaded into editing or empty to add a new
	 *         organization.
	 */
	public String registerOrganization(Organization organization) {
		fileUploadBean = new FileUploadBean();
		this.persons = new ArrayList<Person>();
		this.nameSearch = "";
		try {
			loadCombosTypeChargeTypeDocument();
			if (organization != null) {
				organization = organizationDao
						.consultOrganizationWithTypeDocument(organization
								.getId());
				this.organization = organization;
				fileUploadBean.setFileName(organization.getLogo());
				this.loadPhotoTemporary = false;
			} else {
				this.loadPhotoTemporary = true;
				this.organization = new Organization();
				this.organization.setDocumentType(new TipoDocumento());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regOrganization";
	}

	/**
	 * Method that allows charging information of the types of charge current in
	 * a combo to the user interface.
	 * 
	 * @throws Exception
	 */
	private void loadCombosTypeChargeTypeDocument() throws Exception {
		itemsTypeDocuments = new HashMap<String, Short>();
		List<TipoDocumento> typeDocuments = typeDocumentDao
				.consultarTiposDocumentoVigentes();
		if (typeDocuments != null) {
			for (TipoDocumento td : typeDocuments) {
				itemsTypeDocuments.put(td.getNombre(), td.getId());
			}
		}
	}

	/**
	 * Method that allows you to load detailed organizational information in the
	 * user interface
	 * 
	 * @param organization
	 *            : identification of the organization you want to see the
	 *            information
	 */
	public void seeDetailsOrganization(Organization organization) {
		this.organization = new Organization();
		try {
			organization = organizationDao
					.consultOrganizationWithTypeDocument(organization.getId());
			this.organization = organization;

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that allows you to save or edit an organization, validating the
	 * social reason not repeated in the database.
	 * 
	 * @return regOrganization: Navigation rule that redirects to page register the organization.
	 */
	public String saveOrganization() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String namePhotoDelete = null;
		String key = "message_registro_modificar";
		boolean isChangedPhoto = false;
		boolean isEdition = false;
		try {
			userTransaction.begin();
			organization.setBusinessName(WordUtils.capitalizeFully(organization
					.getBusinessName()));
			organization.setNit(organization.getNit().toUpperCase());
			organization.setUserName(identity.getUserName());
			if (organization.getId() != 0) {
				isEdition = true;
				if (organization.getLogo() != null
						&& !"".equals(organization.getLogo())
						&& !organization.getLogo().equals(
								fileUploadBean.getFileName())) {
					isChangedPhoto = true;
					this.deleteFileReal(organization.getLogo());
				} else if (organization.getLogo() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					isChangedPhoto = true;
				}
				organization.setLogo(fileUploadBean.getFileName());
				if (organization.getLogo() != null && isChangedPhoto) {
					namePhotoDelete = fileUploadBean.getFileName();
					loadImageLocationReal();
				}
				organizationDao.editOrganization(organization);

			} else {
				key = "message_registro_guardar";
				if (fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName().trim())) {
					namePhotoDelete = fileUploadBean.getFileName();
					loadImageLocationReal();
				}
				organization.setLogo(fileUploadBean.getFileName());
				organization.setCreationDate(new Date());
				organizationDao.saveOrganization(organization);
			}
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							organization.getBusinessName()));
			if (namePhotoDelete != null && !"".equals(namePhotoDelete)) {
				this.deleteFile(namePhotoDelete);
			}
		} catch (Exception e) {
			if (!isEdition && fileUploadBean.getFileName() != null
					&& !"".equals(fileUploadBean.getFileName())) {
				this.deleteFileReal(fileUploadBean.getFileName());
			}
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
			return "regOrganization";
		}
		return searchInitialize();
	}

	/**
	 * Method that validate the trade name of the organization, so it is not
	 * repeated in the database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validateBusinessNameXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String businessName = (String) value;
		String clientId = toValidate.getClientId(context);
		int idOrg = organization.getId();
		String businessNameCapitalize = WordUtils.capitalizeFully(businessName);
		Organization organizationTemp = new Organization();
		try {
			if (idOrg != 0) {
				organizationTemp = organizationDao.consultExistUpdate(
						businessNameCapitalize, Constantes.RAZON_SOCIAL, idOrg);
			} else {
				organizationTemp = organizationDao.consultExist(
						businessNameCapitalize, Constantes.RAZON_SOCIAL);
			}
			if (organizationTemp != null) {
				if (organizationTemp.getDateEndValidity() == null) {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);
				} else {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(businessName, clientId,
					"locate.regex.letras.numeros.caracteres")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that validates the NIT of the organization, so it is not repeated
	 * in the database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validateNitXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nit = (String) value;
		String clientId = toValidate.getClientId(context);
		int idOrg = organization.getId();
		Organization organizationTemp = new Organization();
		try {
			if (idOrg != 0) {
				organizationTemp = organizationDao.consultExistUpdate(
						nit.toUpperCase(), Constantes.NIT, idOrg);
			} else {
				organizationTemp = organizationDao.consultExist(
						nit.toUpperCase(), Constantes.NIT);
			}
			if (organizationTemp != null) {
				if (organizationTemp.getDateEndValidity() == null) {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);
				} else {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(nit, clientId,
					"locate.regex.numeros.guion")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that changes the effect of organizations that are not associated
	 * with another entity in the database
	 * 
	 * @modify Luis.Ruiz
	 * 
	 * @param validity
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            INICA with 'false', the selected record in the user interface.
	 * @return consultOrganizations: page redirects to manage organizations
	 */
	public String validityOrganizationes(boolean validity) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		StringBuilder regValidUsed = new StringBuilder();
		StringBuilder regExito = new StringBuilder();
		String messageChangedValidity = "";
		try {
			if (validity) {
				messageChangedValidity = "message_fin_vigencia_satisfactorio";

				organizationModifyValidation.setDateEndValidity(new Date());
				organizationModifyValidation.setUserName(identity
						.getUserName());
				organizationDao
						.editOrganization(organizationModifyValidation);
				regExito.append(organizationModifyValidation
						.getBusinessName() + ", ");

			} else {
				messageChangedValidity = "message_inicio_vigencia_satisfactorio";
				organizationModifyValidation.setDateEndValidity(null);
				organizationModifyValidation.setUserName(identity
						.getUserName());
				organizationDao
						.editOrganization(organizationModifyValidation);
				regExito.append(organizationModifyValidation
						.getBusinessName() + ", ");
			}
			if (regValidUsed.length() > 0) {
				String message = bundle
						.getString("message_registro_vigencia_con_relaciones")
						+ ": "
						+ regValidUsed.substring(0,
								regValidUsed.length() - 2);
				ControladorContexto.mensajeError(message);
			}
			if (regExito.length() > 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString(messageChangedValidity) + ": "
								+ regExito.substring(0, regExito.length() - 2));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultOrganizations();
	}

	/**
	 * Method that allows add the person is created in the modal to the list of
	 * people to be involved in organizing
	 */
	public void registerPerson() {
		if (ControladorContexto.getMaxSeverity() == null) {
			this.persons.add(this.person);
		}
	}

	/**
	 * Method that allows delete the file name.
	 * 
	 */
	public void deleteFilename() {
		if (fileUploadBean.getFileName() != null
				&& !"".equals(fileUploadBean.getFileName())
				&& !fileUploadBean.getFileName().equals(organization.getLogo())
				&& this.loadPhotoTemporary) {
			deleteFile(fileUploadBean.getFileName());
		}
		fileUploadBean.setFileName(null);
	}

	/**
	 * Method that allows delete files.
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 */
	public void deleteFile(String fileName) {
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFileFolderTemporary() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * Method that allows you to load the logo image of the organization.
	 * 
	 * @modify 02/02/2015 Jonathan.Arias
	 * 
	 * @param e
	 *            : File upload event for the file to be uploaded to the server
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAccepted[] = Constantes.EXT_IMG.split(", ");
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFileFolderTemporary() };
		fileUploadBean.setUploadedFile(e.getFile());
		long maximunFileSize = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(extAccepted,
				locations, maximunFileSize);
		String message = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			message = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"),
					maximunFileSize, "MB");
			ControladorContexto.mensajeError("organizacionForm:uploadFile",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			message = "error_carga_archivo";
		}
		if (!"".equals(message)) {
			ControladorContexto.mensajeError("organizacionForm:uploadFile",
					bundle.getString(message));
		}
		if (organization.getId() != 0) {
			loadPhotoTemporary = true;
		}
	}

	/**
	 * method that allows delete files from the actual location
	 * 
	 * @author marisol.calderon
	 * 
	 * @param fileName
	 *            :Name of the file to delete.
	 */
	public void deleteFileReal(String fileName) {
		String ubicaciones[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH + this.getFileFolder(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ this.getFileFolder() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Add photo image to the actual folder
	 * 
	 * @author marisol.calderon
	 * 
	 * @throws Exception
	 */
	private void loadImageLocationReal() throws Exception {
		String origin = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFileFolderTemporary();
		String destination1 = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFileFolder();
		String destination2 = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getFileFolder();
		FileUploadBean.fileExist(destination1);
		FileUploadBean.fileExist(destination2);

		File fileOrigen = new File(origin, fileUploadBean.getFileName());
		File fileDestino1 = new File(destination1, fileUploadBean.getFileName());
		File fileDestino2 = new File(destination2, fileUploadBean.getFileName());

		FileUploadBean.copyFile(fileOrigen, fileDestino1);
		FileUploadBean.copyFile(fileOrigen, fileDestino2);
	}

	/**
	 * Method that allows assign the organization to change its validity
	 * 
	 * @param organization
	 *            : organization you want to change the effect.
	 */
	public void assignOrganizationModify(Organization organization) {
		if (organization instanceof Organization) {
			this.organizationModifyValidation = organization;
		}
	}

	/**
	 * Validates fields that are required in the view so that you can load
	 * regardless logo that are not filled out these fields
	 * 
	 * @author marisol.calderon
	 */
	public void requiredOk() {
		try {
			if (organization.getDocumentType() == null
					|| organization.getDocumentType().getId() == 0) {
				ControladorContexto
						.mensajeRequeridos("organizacionForm:cmbtipodocumento");
			}
			if (organization.getBusinessName() == null
					|| "".equals(organization.getBusinessName())) {
				ControladorContexto
						.mensajeRequeridos("organizacionForm:txtRazonSocial");
			}
			if (organization.getNit() == null
					|| "".equals(organization.getNit())) {
				ControladorContexto
						.mensajeRequeridos("organizacionForm:txtNit");
			}
			/* Validation Scripting in the name of the file to upload */
			EncodeFilter.validarXSS(fileUploadBean.getFileName(),
					"organizacionForm:uploadFile", null);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}