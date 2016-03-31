package co.informatix.erp.recursosHumanos.action;

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
import co.informatix.erp.recursosHumanos.dao.PersonaDao;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.seguridad.action.PerfilUsuarioAction;
import co.informatix.erp.seguridad.dao.UsuarioDao;
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
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PersonaAction implements Serializable {

	@EJB
	private PersonaDao personaDao;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;
	@EJB
	private TipoDocumentoDao tipoDocumentoDao;
	@EJB
	private UsuarioDao usuarioDao;
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
	private List<SelectItem> itemDepartmentsRes;
	private List<SelectItem> itemsMunicipalitiesRes;
	private List<Persona> persons;
	private Persona person;
	private Paginador pagination = new Paginador();
	private Date actualDate = new Date();
	private String filesFolder;
	private String filesFolderTemporal;
	private String messageMiga;
	private String labelRichPanel;
	private String searchFilter;
	private String vigencia = Constantes.SI;
	private boolean esEdicion;
	private boolean uploadPhotoTemporal;
	private boolean personsWithoutUser = false;

	/**
	 * @return vigencia: allows gets the selected value 'yes' of existing and
	 *         'no' for not applicable.
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : allows sets the selected value 'yes' of existing and 'no'
	 *            for not applicable.
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
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
	public Persona getPerson() {
		return person;
	}

	/**
	 * @param person
	 *            : Object of the person for registration or implementation
	 *            edition.
	 */
	public void setPerson(Persona person) {
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
	 * @return itemDepartmentsRes: Items departments residence of the person.s
	 */
	public List<SelectItem> getItemDepartmentsRes() {
		return itemDepartmentsRes;
	}

	/**
	 * @return itemsMunicipalitiesRes: items in the municipalities of residence
	 *         of the person.
	 */
	public List<SelectItem> getItemsMunicipalitiesRes() {
		return itemsMunicipalitiesRes;
	}

	/**
	 * @return persons: List of people that are uploaded to the management.
	 */
	public List<Persona> getPersons() {
		return persons;
	}

	/**
	 * @param persons
	 *            :List of people that are uploaded to the management.
	 */
	public void setPersons(List<Persona> persons) {
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
	 * @return esEdicion: true if the load in editing, page false otherwise.
	 */
	public boolean isEsEdicion() {
		return esEdicion;
	}

	/**
	 * sets a boolean variable to see if the page is loaded or not editing.
	 * 
	 * @param esEdicion
	 *            : true if the load in editing, page false otherwise.
	 */
	public void setEsEdicion(boolean esEdicion) {
		this.esEdicion = esEdicion;
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
	 * @return regPersona: Navigation rule that directs the person form.
	 */
	public String registerPerson(Persona person) {
		ResourceBundle bundleRecHum = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		this.personsWithoutUser = false;
		try {
			fileUploadBean = new FileUploadBean();
			if (person == null) {
				labelRichPanel = bundleRecHum.getString("persona_label_crear");
				messageMiga = "mensajeRecursosHumanos.persona_label_crear";
				this.esEdicion = false;
				this.person = new Persona();
				this.uploadPhotoTemporal = true;
			} else {
				this.esEdicion = true;
				this.person = person;
				labelRichPanel = bundleRecHum
						.getString("persona_label_modificar");
				messageMiga = "mensajeRecursosHumanos.persona_label_modificar";
				fileUploadBean.setFileName(person.getFoto());
				this.uploadPhotoTemporal = false;
			}
			loadCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPersona";
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
		List<Pais> countries = paisDao.consultarPaisesVigentes();
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
		List<TipoDocumento> documentsType = tipoDocumentoDao
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
	 * 
	 */
	public void loadComboDepartment() {
		itemDepartments = new ArrayList<SelectItem>();
		itemsMunicipalities = new ArrayList<SelectItem>();
		try {
			Pais country = person.getPaisNac();
			if (country != null && country.getId() > 0) {
				fillDepartments(country, itemDepartments);
				loadComboMunicipality();
			} else {
				person.setDepartamentoNac(new Departamento());
				person.setMunicipioNac(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the query associated municipalities given department,
	 * the insertion flag and the name of the municipalities in the list of
	 * itemsMunicipios is made to be shown in the user interface.
	 * 
	 */
	public void loadComboMunicipality() {
		itemsMunicipalities = new ArrayList<SelectItem>();
		try {
			Departamento department = person.getDepartamentoNac();
			if (department != null && department.getId() > 0
					&& this.itemDepartments.size() > 0) {
				fillMunicipalities(department, itemsMunicipalities);
			} else {
				person.setDepartamentoNac(new Departamento());
				person.setMunicipioNac(new Municipio());
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
		List<Municipio> Municipalities = municipioDao
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
	 * 
	 */
	public void loadComboDepartmentRes() {
		itemDepartmentsRes = new ArrayList<SelectItem>();
		itemsMunicipalitiesRes = new ArrayList<SelectItem>();
		try {
			Pais country = person.getPaisRes();
			if (country != null && country.getId() > 0) {
				fillDepartments(country, itemDepartmentsRes);
				loadComboMunicipalityRes();
			} else {
				person.setDepartamentoRes(new Departamento());
				person.setMunicipioRes(new Municipio());
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
		List<Departamento> departments = departamentoDao
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
	 * 
	 */
	public void loadComboMunicipalityRes() {
		itemsMunicipalitiesRes = new ArrayList<SelectItem>();
		try {
			Departamento department = person.getDepartamentoRes();
			if (department != null && department.getId() > 0
					&& this.itemDepartmentsRes.size() > 0) {
				fillMunicipalities(department, itemsMunicipalitiesRes);
			} else {
				person.setDepartamentoRes(new Departamento());
				person.setMunicipioRes(new Municipio());
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
		PerfilUsuarioAction userProfileAction = ControladorContexto
				.getContextBean(PerfilUsuarioAction.class);
		String exit = "regPersona";
		String messageInfo = "message_registro_modificar";
		String namePhotoRemove = null;
		String nameShow = "";
		boolean changePhoto = false;
		try {
			userTransaction.begin();
			validateObjects();
			person.setNombres(WordUtils.capitalizeFully(person.getNombres()));
			person.setApellidos(WordUtils.capitalizeFully(person.getApellidos()));
			person.setUserName(identity.getUserName());
			if (esEdicion) {
				if (person.getFoto() != null
						&& !"".equals(person.getFoto())
						&& !person.getFoto().equals(
								fileUploadBean.getFileName())) {
					changePhoto = true;
					this.deleteFileReal(person.getFoto());
				} else if (person.getFoto() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					changePhoto = true;
				}
				person.setFoto(fileUploadBean.getFileName());
				if (person.getFoto() != null && changePhoto) {
					namePhotoRemove = fileUploadBean.getFileName();
					uploadImageLocationReal();
				}
				personaDao.editPerson(person);
				Usuario user = usuarioDao.searchPersonUser(person.getId());
				if (user != null) {
					user.setNombre(person.getNombres());
					user.setApellido(person.getApellidos());
					user.setCorreoElectronico(person.getCorreo());
					user.setUserName(identity.getUserName());
					usuarioDao.editUser(user);
				}
			} else {
				messageInfo = "message_registro_guardar";
				if (fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName().trim())) {
					namePhotoRemove = fileUploadBean.getFileName();
					uploadImageLocationReal();
				}
				person.setFoto(fileUploadBean.getFileName());
				this.person.setFechaCreacion(new Date());
				personaDao.createPerson(person);
			}
			nameShow = person.getDocumento();
			userTransaction.commit();
			if (namePhotoRemove != null && !"".equals(namePhotoRemove)) {
				this.deleteFile(namePhotoRemove);
			}
			if (userProfileAction.isGuardarPersonaDesdePerfil()) {
				nameShow = person.getNombres() + " " + person.getApellidos();
				exit = userProfileAction
						.cargarPerfilDeUsuario(Constantes.N_TAB);
			} else {
				exit = initializeConsultation();
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageInfo), nameShow));
		} catch (Exception e) {
			if (!esEdicion && fileUploadBean.getFileName() != null
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
	 * called the method editarPersona to save the update to the database and
	 * then query the current user is done with the method consultarPersonas.
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
				this.person.setFechaFinVigencia(new Date());
				this.person.setUserName(identity.getUserName());
				personaDao.editPerson(this.person);

			} else {
				this.person.setFechaFinVigencia(null);
				this.person.setUserName(identity.getUserName());
				personaDao.editPerson(this.person);
			}
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(
							bundle.getString(messageChangeValidity) + ": {0}",
							this.person.getNombres() + " "
									+ this.person.getApellidos()));
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
	 * @return back: Redirects output page listarPersonas
	 */
	public String consultPersons() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHumanResources = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String inModal = ControladorContexto.getParam("param2");
		this.persons = new ArrayList<Persona>();
		List<Persona> personasTemp = new ArrayList<Persona>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessageSearch = new StringBuilder();
		String messageSearch = "";
		boolean fromModal = (inModal != null && Constantes.SI.equals(inModal)) ? true
				: false;
		String back = fromModal ? "" : "gesPersonas";
		try {
			if (!fromModal)
				personsWithoutUser = false;
			advancedSearch(consult, parameters, bundleHumanResources,
					unionMessageSearch);
			Long quantityRegisters = this.personaDao.quantityPersons(consult,
					parameters);
			if (quantityRegisters != null) {
				if (fromModal)
					pagination.paginarRangoDefinido(quantityRegisters, 5);
				else
					this.pagination.paginar(quantityRegisters);
			}
			personasTemp = personaDao.consultPersons(pagination.getInicio(),
					pagination.getRango(), consult, parameters);
			loadInformationDetailPerson(personasTemp);
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
										.getString("persona_label_s"),
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
		consult.append("WHERE p.fechaFinVigencia ");
		if (Constantes.NOT.equals(vigencia)) {
			consult.append(Constantes.IS_NOT_NULL + " ");
		} else {
			consult.append(Constantes.IS_NULL + " ");
		}
		if (this.searchFilter != null && !"".equals(this.searchFilter)) {
			consult.append("AND (UPPER(p.nombres) LIKE UPPER(:parametro) ");
			consult.append("OR UPPER(p.apellidos) LIKE UPPER(:parametro) ");
			consult.append("OR UPPER(p.documento) LIKE UPPER(:parametro)) ");
			SelectItem item = new SelectItem("%" + this.searchFilter + "%",
					"parametro");
			parameters.add(item);
			unionMessagesSearch
					.append(bundle
							.getString("persona_message_consulta_nombre_apellido_identificacion")
							+ ": " + '"' + this.searchFilter + '"');
		}
		if (personsWithoutUser) {
			consult.append("AND p.usuario IS NULL ");
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
	public void loadInformationDetailPerson(List<Persona> personsTemp)
			throws Exception {
		if (personsTemp != null) {
			for (Persona person : personsTemp) {
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
	public void loadDetailsOnePerson(Persona person) throws Exception {
		int idPerson = person.getId();
		TipoDocumento documentType = (TipoDocumento) this.personaDao
				.consultObjectPerson("tipoDocumento", idPerson);
		Pais countryBirth = (Pais) this.personaDao.consultObjectPerson(
				"paisNac", idPerson);
		Departamento departmentBirth = (Departamento) this.personaDao
				.consultObjectPerson("departamentoNac", idPerson);
		Municipio municipalityBirth = (Municipio) this.personaDao
				.consultObjectPerson("municipioNac", idPerson);
		Pais countryRes = (Pais) this.personaDao.consultObjectPerson("paisRes",
				idPerson);
		Departamento departmentRes = (Departamento) this.personaDao
				.consultObjectPerson("departamentoRes", idPerson);
		Municipio municipalityRes = (Municipio) this.personaDao
				.consultObjectPerson("municipioRes", idPerson);
		CivilStatus civilStatus = (CivilStatus) this.personaDao
				.consultObjectPerson("civilStatus", idPerson);

		person.setTipoDocumento(documentType);
		person.setPaisNac(countryBirth);
		person.setDepartamentoNac(departmentBirth);
		person.setMunicipioNac(municipalityBirth);
		person.setPaisRes(countryRes);
		person.setDepartamentoRes(departmentRes);
		person.setMunicipioRes(municipalityRes);
		person.setCivilStatus(civilStatus != null ? civilStatus
				: new CivilStatus());
	}

	/**
	 * Allows erase the default filename.
	 * 
	 */
	public void deleteFilename() {
		if (fileUploadBean.getFileName() != null
				&& !"".equals(fileUploadBean.getFileName())
				&& !fileUploadBean.getFileName().equals(person.getFoto())
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
	 * 
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
	 * 
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
		if (esEdicion) {
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
		if (person.getTipoDocumento() == null
				|| person.getTipoDocumento().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":cmbTipoDocumento");
			exit = false;
		}
		if (person.getDocumento() == null || "".equals(person.getDocumento())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":txtId");
			exit = false;
		}
		if (person.getNombres() == null || "".equals(person.getNombres())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":txtNombre");
			exit = false;
		}
		if (person.getGenero() == null || "".equals(person.getGenero())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":radGenero");
			exit = false;
		}
		if (person.getPaisNac().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm + ":comboPais");
			exit = false;
		}
		if (person.getDepartamentoNac().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboDepartamento");
			exit = false;
		}
		if (person.getMunicipioNac().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm + ":comboMunicipio");
			exit = false;
		}
		if (person.getTelefono() == null || "".equals(person.getTelefono())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":txtTelefono");
			exit = false;
		}
		if (person.getPaisRes().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm + ":comboPaisRes");
			exit = false;
		}
		if (person.getDepartamentoRes().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboDepartamentoRes");
			exit = false;
		}
		if (person.getMunicipioRes().getId() == 0) {
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
	 * 
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
			Persona personBD = personaDao.validatePersonByDocument(
					document.toUpperCase(), idDocumentType);
			if (personBD != null) {
				if (personBD.getFechaFinVigencia() == null) {
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
