package co.informatix.erp.humanResources.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.apache.commons.lang3.text.WordUtils;
import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.informacionBase.dao.CivilStatusDao;
import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.dao.TipoDocumentoDao;
import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.informacionBase.entities.TipoDocumento;
import co.informatix.erp.humanResources.dao.PersonDao;
import co.informatix.erp.humanResources.entities.Person;
import co.informatix.erp.seguridad.action.ProfileUserAction;
import co.informatix.erp.seguridad.dao.UserDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.entities.Usuario;

/**
 * This class is used to create, update, and query of a user in the system
 * information.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PersonAction implements Serializable {

	@EJB
	private PersonDao personDao;
	@EJB
	private PaisDao countryDao;
	@EJB
	private DepartamentoDao departmentDao;
	@EJB
	private MunicipioDao municipalityDao;
	@EJB
	private TipoDocumentoDao documentTypeDao;
	@EJB
	private UserDao userDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private CivilStatusDao civilStatusDao;

	@Resource
	private UserTransaction userTransaction;
	@Inject
	private IdentityAction identity;

	private List<SelectItem> itemsCountries;
	private List<SelectItem> itemDepartments;
	private List<SelectItem> itemsMunicipalities;
	private List<SelectItem> itemsDocumentsTypes;
	private List<SelectItem> itemsMaritalStatus;
	private List<SelectItem> itemDepartmentsHome;
	private List<SelectItem> itemsMunicipalitiesHome;
	private List<Person> persons;
	private Person person;
	private Paginador pagination = new Paginador();
	private Date actualDate = new Date();
	private String filesFolder;
	private String filesFolderTemporal;
	private String messageMiga;
	private String labelRichPanel;
	private String searchFilter;
	private String validity = Constantes.SI;
	private boolean edition;
	private boolean uploadPhotoTemporal;
	private boolean personsWithoutUser = false;

	/**
	 * @return validity: allows gets the selected value 'yes' of existing and
	 *         'no' for not applicable.
	 */
	public String getValidity() {
		return validity;
	}

	/**
	 * @param validity
	 *            : allows sets the selected value 'yes' of existing and 'no'
	 *            for not applicable.
	 */
	public void setValidity(String validity) {
		this.validity = validity;
	}

	/**
	 * @return actualDate: variable that gets the current system date.
	 */
	public Date getActualDate() {
		return actualDate;
	}

	/**
	 * @param actualDate
	 *            : variable that sets the current system date.
	 */
	public void setActualDate(Date actualDate) {
		this.actualDate = actualDate;
	}

	/**
	 * @return filesFolder: Variable that gets the path to the folder where the
	 *         pictures are saved person.
	 */
	public String getFilesFolder() {
		this.filesFolder = Constantes.FOLDER_FILES
				+ Constantes.FOLDER_FILES_PERSONS;
		return filesFolder;
	}

	/**
	 * @param filesFolder
	 *            : Variable that gets the path to the folder where the pictures
	 *            are saved person.
	 */
	public void setFilesFolder(String filesFolder) {
		this.filesFolder = filesFolder;
	}

	/**
	 * @return filesFolderTemporal: path of the temporary folder where photos of
	 *         people loaded.
	 */
	public String getFilesFolderTemporal() {
		this.filesFolderTemporal = Constantes.FOLDER_FILES
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return filesFolderTemporal;
	}

	/**
	 * @return uploadPhotoTemporal: Flag indicating whether the picture is
	 *         loaded from the temporary location or not.
	 */
	public boolean isUploadPhotoTemporal() {
		return uploadPhotoTemporal;
	}

	/**
	 * @param uploadPhotoTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not.
	 */
	public void setUploadPhotoTemporal(boolean uploadPhotoTemporal) {
		this.uploadPhotoTemporal = uploadPhotoTemporal;
	}

	/**
	 * @return fileUploadBean: gets the object variable to load files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : sets the object variable to load files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return pagination: pagination object that allows listing of people.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : pagination object that allows listing of people.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return person: Object of the person for registration or implementation
	 *         edition.
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            : Object of the person for registration or implementation
	 *            edition.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return itemsCountries: List of items from the countries that are loaded
	 *         into the combo in the user interface.
	 */
	public List<SelectItem> getItemsCountries() {
		return itemsCountries;
	}

	/**
	 * @return itemDepartments: List of items from the departments that are
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemDepartments() {
		return itemDepartments;
	}

	/**
	 * @return itemsMunicipalities :List of items of the municipalities that are
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsMunicipalities() {
		return itemsMunicipalities;
	}

	/**
	 * @return itemsDocumentsTypes: List of items for document types that are
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsDocumentsTypes() {
		return itemsDocumentsTypes;
	}

	/**
	 * @return itemsMaritalStatus: marital status items that are loaded into the
	 *         combo in the user interface.
	 */
	public List<SelectItem> getItemsMaritalStatus() {
		return itemsMaritalStatus;
	}

	/**
	 * @return itemDepartmentsHome: Items departments residence of the persons.
	 */
	public List<SelectItem> getItemDepartmentsHome() {
		return itemDepartmentsHome;
	}

	/**
	 * @return itemsMunicipalitiesHome: items in the municipalities of residence
	 *         of the person.
	 */
	public List<SelectItem> getItemsMunicipalitiesHome() {
		return itemsMunicipalitiesHome;
	}

	/**
	 * @return persons: List of people that are uploaded to the management.
	 */
	public List<Person> getPersons() {
		return persons;
	}

	/**
	 * @param persons
	 *            :List of people that are uploaded to the management.
	 */
	public void setPersons(List<Person> persons) {
		this.persons = persons;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on whether you create or modify a record.
	 * 
	 * @return messageMiga: message crumb of bread in the register or person
	 *         managing staff.
	 */
	public String getMessageMiga() {
		return messageMiga;
	}

	/**
	 * Variable that sets the value of the message to display in the
	 * breadcrumbs, depending on whether you create or modify a record.
	 * 
	 * @param messageMiga
	 *            : message crumb of bread in the register or person managing
	 *            staff.
	 */
	public void setMessageMiga(String messageMiga) {
		this.messageMiga = messageMiga;
	}

	/**
	 * You gets a Boolean variable to see if the page is loaded or not editing.
	 * 
	 * @return edition: true if the load in editing, page false otherwise.
	 */
	public boolean isEdition() {
		return edition;
	}

	/**
	 * sets a boolean variable to see if the page is loaded or not editing.
	 * 
	 * @param edition
	 *            : true if the load in editing, page false otherwise.
	 */
	public void setEdition(boolean edition) {
		this.edition = edition;
	}

	/**
	 * @return labelRichPanel: label shown in rich editing panel depending on
	 *         whether or not and if it comes from third.
	 */
	public String getLabelRichPanel() {
		return labelRichPanel;
	}

	/**
	 * @param labelRichPanel
	 *            : label shown in rich editing panel depending on whether or
	 *            not and if it comes from third.
	 */
	public void setLabelRichPanel(String labelRichPanel) {
		this.labelRichPanel = labelRichPanel;
	}

	/**
	 * @return searchFilter: Search filter records of the person.
	 */
	public String getSearchFilter() {
		return searchFilter;
	}

	/**
	 * @param searchFilter
	 *            : Search filter records of the person.
	 */
	public void setSearchFilter(String searchFilter) {
		this.searchFilter = searchFilter;
	}

	/**
	 * @return personsWithoutUser: allows to validate that the consultation is
	 *         made for those people who do not have user.
	 */
	public boolean isPersonsWithoutUser() {
		return personsWithoutUser;
	}

	/**
	 * @param personsWithoutUser
	 *            : allows to validate that the consultation is made for those
	 *            people who do not have user.
	 */
	public void setPersonsWithoutUser(boolean personsWithoutUser) {
		this.personsWithoutUser = personsWithoutUser;
	}

	/**
	 * Allows to load interface to record or edit a person.
	 * 
	 * @modify Adonay.Mantilla 25/01/2013
	 * 
	 * @param person
	 *            : Person edited in edit mode.
	 * @return regPerson: Navigation rule that directs the person form.
	 */
	public String registerPerson(Person person) {
		ResourceBundle bundleRecHum = ControladorContexto
				.getBundle("messageHumanResources");
		this.personsWithoutUser = false;
		try {
			fileUploadBean = new FileUploadBean();
			if (person == null) {
				labelRichPanel = bundleRecHum.getString("person_label_create");
				messageMiga = "messageHumanResources.person_label_create";
				this.edition = false;
				this.person = new Person();
				this.uploadPhotoTemporal = true;
			} else {
				this.edition = true;
				this.person = person;
				labelRichPanel = bundleRecHum.getString("person_label_edit");
				messageMiga = "messageHumanResources.person_label_edit";
				fileUploadBean.setFileName(person.getPhoto());
				this.uploadPhotoTemporal = false;
			}
			loadCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPerson";
	}

	/**
	 * This method allows you to load combos country, department, municipality
	 * and type of document.
	 * 
	 * @author marisol.calderon
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void loadCombos() throws Exception {
		itemsCountries = new ArrayList<SelectItem>();
		List<Pais> countries = countryDao.consultarPaisesVigentes();
		if (countries != null) {
			for (Pais country : countries) {
				itemsCountries.add(new SelectItem(country.getId(), country
						.getNombre()));
			}
		}
		loadComboDepartment();
		loadComboMunicipality();
		loadComboDepartmentRes();
		loadComboMunicipalityRes();

		itemsDocumentsTypes = new ArrayList<SelectItem>();
		List<TipoDocumento> documentsType = documentTypeDao
				.consultarTiposDocumentoVigentes();
		if (documentsType != null) {
			for (TipoDocumento td : documentsType) {
				itemsDocumentsTypes.add(new SelectItem(td.getId(), td
						.getNombre()));
			}
		}
		itemsMaritalStatus = new ArrayList<SelectItem>();
		List<CivilStatus> civilStatusList = civilStatusDao.consultCivilStatus();
		if (civilStatusList != null) {
			for (CivilStatus civilStatus : civilStatusList) {
				itemsMaritalStatus.add(new SelectItem(civilStatus.getId(),
						civilStatus.getName()));
			}
		}
	}

	/**
	 * This method makes the query associated departments selected a particular
	 * country from interface, makes the insertion of the indicator and the
	 * department name itemDepartamentos list for display in the user interface.
	 */
	public void loadComboDepartment() {
		itemDepartments = new ArrayList<SelectItem>();
		itemsMunicipalities = new ArrayList<SelectItem>();
		try {
			Pais country = person.getCountryBirth();
			if (country != null && country.getId() > 0) {
				fillDepartments(country, itemDepartments);
				loadComboMunicipality();
			} else {
				person.setDepartmentBirth(new Departamento());
				person.setMunicipalityBirth(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the query associated municipalities given department,
	 * the insertion flag and the name of the municipalities in the list of
	 * itemsMunicipios is made to be shown in the user interface.
	 */
	public void loadComboMunicipality() {
		itemsMunicipalities = new ArrayList<SelectItem>();
		try {
			Departamento department = person.getDepartmentBirth();
			if (department != null && department.getId() > 0
					&& this.itemDepartments.size() > 0) {
				fillMunicipalities(department, itemsMunicipalities);
			} else {
				person.setDepartmentBirth(new Departamento());
				person.setMunicipalityBirth(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to fill in the items of municipalities.
	 * 
	 * @param department
	 *            : selected by the department which municipalities are
	 *            filtered.
	 * @param itemsMunicipalities
	 *            : list of items of municipalities to fill displayed in the
	 *            user interface.
	 * @throws Exception
	 */
	private void fillMunicipalities(Departamento department,
			List<SelectItem> itemsMunicipalities) throws Exception {
		int idDepartment = department.getId();
		List<Municipio> Municipalities = municipalityDao
				.consultarMunicipiosVigentes(idDepartment);
		if (Municipalities != null) {
			for (Municipio m : Municipalities) {
				itemsMunicipalities
						.add(new SelectItem(m.getId(), m.getNombre()));
			}
		}
	}

	/**
	 * This method makes the query departments residence of the person
	 * associated with certain selected country from the interface, makes the
	 * insertion of the indicator and the department name itemDepartamentos list
	 * for display in the user interface.
	 */
	public void loadComboDepartmentRes() {
		itemDepartmentsHome = new ArrayList<SelectItem>();
		itemsMunicipalitiesHome = new ArrayList<SelectItem>();
		try {
			Pais country = person.getCountryHome();
			if (country != null && country.getId() > 0) {
				fillDepartments(country, itemDepartmentsHome);
				loadComboMunicipalityRes();
			} else {
				person.setDepartmentHome(new Departamento());
				person.setMunicipalityHome(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to fill in the items of the departments.
	 * 
	 * @param country
	 *            : country selected the which departments are filtered.
	 * @param itemDepartments
	 *            : list of items of departments to fill displayed in the user
	 *            interface.
	 * @throws Exception
	 */
	private void fillDepartments(Pais country, List<SelectItem> itemDepartments)
			throws Exception {
		short idCountry = country.getId();
		List<Departamento> departments = departmentDao
				.consultarDepartamentosPaisVigentes(idCountry);
		if (departments != null) {
			for (Departamento d : departments) {
				itemDepartments.add(new SelectItem(d.getId(), d.getNombre()));
			}
		}
	}

	/**
	 * This method makes the query associated municipalities of residence of a
	 * particular department, the insertion flag and the name of the
	 * municipalities in the list of itemsMunicipios is made to be shown in the
	 * user interface
	 */
	public void loadComboMunicipalityRes() {
		itemsMunicipalitiesHome = new ArrayList<SelectItem>();
		try {
			Departamento department = person.getDepartmentHome();
			if (department != null && department.getId() > 0
					&& this.itemDepartmentsHome.size() > 0) {
				fillMunicipalities(department, itemsMunicipalitiesHome);
			} else {
				person.setDepartmentHome(new Departamento());
				person.setMunicipalityHome(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allows you to save or edit a person in the database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @return exit: Navigation rule that addresses the management of people if
	 *         no validation errors, otherwise it returns to form person.
	 */
	public String addEditPerson() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ProfileUserAction userProfileAction = ControladorContexto
				.getContextBean(ProfileUserAction.class);
		String exit = "regPerson";
		String messageInfo = "message_registro_modificar";
		String namePhotoRemove = null;
		String nameShow = "";
		boolean changePhoto = false;
		try {
			userTransaction.begin();
			validateObjects();
			person.setNames(WordUtils.capitalizeFully(person.getNames()));
			person.setSurnames(WordUtils.capitalizeFully(person.getSurnames()));
			person.setUserName(identity.getUserName());
			if (edition) {
				if (person.getPhoto() != null
						&& !"".equals(person.getPhoto())
						&& !person.getPhoto().equals(
								fileUploadBean.getFileName())) {
					changePhoto = true;
					this.deleteFileReal(person.getPhoto());
				} else if (person.getPhoto() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					changePhoto = true;
				}
				person.setPhoto(fileUploadBean.getFileName());
				if (person.getPhoto() != null && changePhoto) {
					namePhotoRemove = fileUploadBean.getFileName();
					uploadImageLocationReal();
				}
				personDao.editPerson(person);
				Usuario user = userDao.searchPersonUser(person.getId());
				if (user != null) {
					user.setNombre(person.getNames());
					user.setApellido(person.getSurnames());
					user.setCorreoElectronico(person.getEmail());
					user.setUserName(identity.getUserName());
					userDao.editUser(user);
				}
			} else {
				messageInfo = "message_registro_guardar";
				if (fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName().trim())) {
					namePhotoRemove = fileUploadBean.getFileName();
					uploadImageLocationReal();
				}
				person.setPhoto(fileUploadBean.getFileName());
				this.person.setDateCreation(new Date());
				personDao.createPerson(person);
			}
			nameShow = person.getDocument();
			userTransaction.commit();
			if (namePhotoRemove != null && !"".equals(namePhotoRemove)) {
				this.deleteFile(namePhotoRemove);
			}
			if (userProfileAction.isSavePersonFromProfile()) {
				nameShow = person.getNames() + " " + person.getSurnames();
				exit = userProfileAction.loadProfileOfUser(Constantes.N_TAB);
			} else {
				exit = initializeConsultation();
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageInfo), nameShow));
		} catch (Exception e) {
			if (!edition && fileUploadBean.getFileName() != null
					&& !"".equals(fileUploadBean.getFileName())) {
				this.deleteFileReal(fileUploadBean.getFileName());
			}
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return exit;
	}

	/**
	 * This method starts or ends the validity of a particular user, it is
	 * called the method editPerson to save the update to the database and then
	 * query the current user is done with the method consultPersons.
	 * 
	 * @param validity
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            INICA with 'false', the selected record in the user interface.
	 * @return consultPersons. Page redirect to people query.
	 */
	public String changeValidityPersons(boolean validity) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageChangeValidity = "message_inicio_vigencia_satisfactorio";
		try {
			validateObjects();
			if (validity) {
				messageChangeValidity = "message_fin_vigencia_satisfactorio";
				this.person.setDateEndValidity(new Date());
				this.person.setUserName(identity.getUserName());
				personDao.editPerson(this.person);

			} else {
				this.person.setDateEndValidity(null);
				this.person.setUserName(identity.getUserName());
				personDao.editPerson(this.person);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageChangeValidity) + ": {0}",
					this.person.getNames() + " " + this.person.getSurnames()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultPersons();
	}

	/**
	 * Method to validate objects before save person in the database, to which
	 * an instance of the class was created but there is no record.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 */
	private void validateObjects() {
		if (this.person != null) {
			CivilStatus civilStatus = this.person.getCivilStatus();
			if (civilStatus != null && civilStatus.getId() == 0) {
				this.person.setCivilStatus(null);
			}
		}
	}

	/**
	 * This method makes consulting registered user of the information system,
	 * taking into account the type of validity that is required by the
	 * interface determined by the tipoVigenteSelect this variable.
	 * 
	 * @modify 04/06/2013 Luz.Jaimes
	 * @modify 08/03/2016 Mabell.Boada
	 * 
	 * @return back: Redirects output page managePersons
	 */
	public String consultPersons() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHumanResources = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String inModal = ControladorContexto.getParam("param2");
		this.persons = new ArrayList<Person>();
		List<Person> personsTemp = new ArrayList<Person>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessageSearch = new StringBuilder();
		String messageSearch = "";
		boolean fromModal = (inModal != null && Constantes.SI.equals(inModal)) ? true
				: false;
		String back = fromModal ? "" : "manPersons";
		try {
			if (!fromModal)
				personsWithoutUser = false;
			advancedSearch(consult, parameters, bundleHumanResources,
					unionMessageSearch);
			Long quantityRegisters = this.personDao.quantityPersons(consult,
					parameters);
			if (quantityRegisters != null) {
				if (fromModal)
					pagination.paginarRangoDefinido(quantityRegisters, 5);
				else
					this.pagination.paginar(quantityRegisters);
			}
			personsTemp = personDao.consultPersons(pagination.getInicio(),
					pagination.getRango(), consult, parameters);
			loadInformationDetailPerson(personsTemp);
			if ((this.persons == null || this.persons.size() <= 0)
					&& !"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessageSearch);
			} else if (this.persons == null || this.persons.size() <= 0) {
				messageSearch = bundle
						.getString("message_no_existen_registros");
				if (!fromModal) {
					ControladorContexto.mensajeInformacion(null, messageSearch);
					messageSearch = "";
				}
			} else if (!"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleHumanResources
										.getString("person_label_s"),
								unionMessageSearch);
			}
			if (fromModal) {
				validations.setMensajeBusquedaPopUp(messageSearch);
			} else {
				validations.setMensajeBusqueda(messageSearch);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return back;
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		consult.append("WHERE p.dateEndValidity ");
		if (Constantes.NOT.equals(validity)) {
			consult.append(Constantes.IS_NOT_NULL + " ");
		} else {
			consult.append(Constantes.IS_NULL + " ");
		}
		if (this.searchFilter != null && !"".equals(this.searchFilter)) {
			consult.append("AND (UPPER(p.names) LIKE UPPER(:parameter) ");
			consult.append("OR UPPER(p.surnames) LIKE UPPER(:parameter) ");
			consult.append("OR UPPER(p.document) LIKE UPPER(:parameter)) ");
			SelectItem item = new SelectItem("%" + this.searchFilter + "%",
					"parameter");
			parameters.add(item);
			unionMessagesSearch
					.append(bundle
							.getString("person_message_consult_name_surname_identification")
							+ ": " + '"' + this.searchFilter + '"');
		}
		if (personsWithoutUser) {
			consult.append("AND p.user IS NULL ");
		}
	}

	/**
	 * Method to load the detail information of the person.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param personsTemp
	 *            : initial list of people.
	 * @throws Exception
	 */
	public void loadInformationDetailPerson(List<Person> personsTemp)
			throws Exception {
		if (personsTemp != null) {
			for (Person person : personsTemp) {
				loadDetailsOnePerson(person);
				this.persons.add(person);
			}
		}
	}

	/**
	 * Allows load the details of the person sent as a parameter.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @param person
	 *            : person in charge details.
	 * @throws Exception
	 */
	public void loadDetailsOnePerson(Person person) throws Exception {
		int idPerson = person.getId();
		TipoDocumento documentType = (TipoDocumento) this.personDao
				.consultObjectPerson("documentType", idPerson);
		Pais countryBirth = (Pais) this.personDao.consultObjectPerson(
				"countryBirth", idPerson);
		Departamento departmentBirth = (Departamento) this.personDao
				.consultObjectPerson("departmentBirth", idPerson);
		Municipio municipalityBirth = (Municipio) this.personDao
				.consultObjectPerson("municipalityBirth", idPerson);
		Pais countryHome = (Pais) this.personDao.consultObjectPerson(
				"countryHome", idPerson);
		Departamento departmentHome = (Departamento) this.personDao
				.consultObjectPerson("departmentHome", idPerson);
		Municipio municipalityHome = (Municipio) this.personDao
				.consultObjectPerson("municipalityHome", idPerson);
		CivilStatus civilStatus = (CivilStatus) this.personDao
				.consultObjectPerson("civilStatus", idPerson);

		person.setDocumentType(documentType);
		person.setCountryBirth(countryBirth);
		person.setDepartmentBirth(departmentBirth);
		person.setMunicipalityBirth(municipalityBirth);
		person.setCountryHome(countryHome);
		person.setDepartmentHome(departmentHome);
		person.setMunicipalityHome(municipalityHome);
		person.setCivilStatus(civilStatus != null ? civilStatus
				: new CivilStatus());
	}

	/**
	 * Allows erase the default filename.
	 */
	public void deleteFilename() {
		if (fileUploadBean.getFileName() != null
				&& !"".equals(fileUploadBean.getFileName())
				&& !fileUploadBean.getFileName().equals(person.getPhoto())
				&& this.uploadPhotoTemporal) {
			deleteFile(fileUploadBean.getFileName());
		}
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 */
	public void deleteFile(String fileName) {
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFilesFolderTemporal() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * Delete files from the actual location.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 */
	public void deleteFileReal(String fileName) {
		String locations[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH + this.getFilesFolder(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE + this.getFilesFolder() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * Add photo image to the actual folder
	 * 
	 * @author marisol.calderon
	 * 
	 * @throws Exception
	 */
	private void uploadImageLocationReal() throws Exception {
		String origin = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFilesFolderTemporal();
		String destiny1 = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFilesFolder();
		String destiny2 = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getFilesFolder();

		FileUploadBean.fileExist(destiny1);
		FileUploadBean.fileExist(destiny2);

		File fileOrigin = new File(origin, fileUploadBean.getFileName());
		File fileDestiny1 = new File(destiny1, fileUploadBean.getFileName());
		File fileDestiny2 = new File(destiny2, fileUploadBean.getFileName());

		FileUploadBean.copyFile(fileOrigin, fileDestiny1);
		FileUploadBean.copyFile(fileOrigin, fileDestiny2);
	}

	/**
	 * Allows to load the photo image of the person.
	 * 
	 * @modify 03/02/2015 Marcela.Chaparro
	 * 
	 * @param e
	 *            : File upload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAccepted[] = Constantes.EXT_IMG.split(", ");
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFilesFolderTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		String resultUpload = fileUploadBean.upload(extAccepted, locations);
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			ControladorContexto.mensajeError("frmRegistrarPersona:uploadFile",
					bundle.getString("error_ext_invalida"));
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			ControladorContexto.mensajeError("frmRegistrarPersona:uploadFile",
					bundle.getString("error_carga_archivo"));
		}
		if (edition) {
			uploadPhotoTemporal = true;
		}
	}

	/**
	 * Validates fields that are required in the view so that you can load
	 * regardless picture that are not filled out these fields.
	 * 
	 * @author marisol.calderon
	 * @modify 08/03/2012 Gabriel.Moreno
	 * 
	 * @param nameForm
	 *            :template id where is valid people.
	 * @return exit: boolean to true if the required fields are filled out or
	 *         false otherwise.
	 */
	public boolean requiredOk(String nameForm) {
		boolean exit = true;
		if (person.getDocumentType() == null
				|| person.getDocumentType().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":cmbTipoDocumento");
			exit = false;
		}
		if (person.getDocument() == null || "".equals(person.getDocument())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":txtId");
			exit = false;
		}
		if (person.getNames() == null || "".equals(person.getNames())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":txtNombre");
			exit = false;
		}
		if (person.getGender() == null || "".equals(person.getGender())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":radGenero");
			exit = false;
		}
		if (person.getCountryBirth().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm + ":comboPais");
			exit = false;
		}
		if (person.getDepartmentBirth().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboDepartamento");
			exit = false;
		}
		if (person.getMunicipalityBirth().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm + ":comboMunicipio");
			exit = false;
		}
		if (person.getTelephone() == null || "".equals(person.getTelephone())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":txtTelefono");
			exit = false;
		}
		if (person.getCountryHome().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm + ":comboPaisRes");
			exit = false;
		}
		if (person.getDepartmentHome().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboDepartamentoRes");
			exit = false;
		}
		if (person.getMunicipalityHome().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboMunicipioRes");
			exit = false;
		}
		return exit;
	}

	/**
	 * Allows to validate the document of the person, so it is not repeated in
	 * the database and validates against XSS.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateDocumentXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String document = (String) value;
		String clientId = toValidate.getClientId(context);
		UIInput findComponent = (UIInput) toValidate
				.findComponent("cmbTipoDocumento");
		short idDocumentType = (Short) findComponent.getValue();
		try {
			Person personBD = personDao.validatePersonByDocument(
					document.toUpperCase(), idDocumentType);
			if (personBD != null) {
				if (personBD.getDateEndValidity() == null) {
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
			if (!EncodeFilter.validarXSS(document, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method is used to initialize the consultation of the people
	 * registered in the information system.
	 * 
	 * @return consultPersons: method that queries the information of the people
	 *         and returns to the template management.
	 */
	public String initializeConsultation() {
		this.searchFilter = "";
		return consultPersons();
	}
}