package co.informatix.erp.humanResources.action;

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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.HrTypesDao;
import co.informatix.erp.humanResources.dao.PaymentMethodsDao;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.HrTypes;
import co.informatix.erp.humanResources.entities.PaymentMethods;
import co.informatix.erp.informacionBase.dao.CivilStatusDao;
import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the business logic: creating, deleting and updating
 * maintenance lines in the system.
 * 
 * @author Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class HrAction implements Serializable {
	@EJB
	private HrDao hrDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;
	@EJB
	private CivilStatusDao civilStatusDao;
	@EJB
	private PaymentMethodsDao paymentMethodsDao;
	@EJB
	private HrTypesDao hrTypesDao;

	private Hr hr;
	private Paginador paginador = new Paginador();
	private Paginador paginadorForm = new Paginador();
	private String nameSearch;
	private String lastNameSearch;
	private String filesFolder;
	private String temporalFilesFolder;
	private String hrPicName;
	private Date currentDate = new Date();

	private List<Hr> hrList;
	private List<SelectItem> birthCountryItems;
	private List<SelectItem> birthStateItems;
	private List<SelectItem> birthMunicipalityItems;
	private List<SelectItem> residenceCountryItems;
	private List<SelectItem> residenceStateItems;
	private List<SelectItem> residenceMunicipalityItems;
	private List<SelectItem> maritalStatusItems;
	private List<SelectItem> hrTypesItems;
	private List<SelectItem> paymentMethodsItems;

	private int hrTypeSearch;
	private int paymentTypeSearch;

	private boolean temporalPicLoaded;

	/**
	 * @return hrList: gets the list of human resources
	 */
	public List<Hr> getHrList() {
		return hrList;
	}

	/**
	 * @param hrList
	 *            : gets the list of human resources
	 */
	public void setHrList(List<Hr> hrList) {
		this.hrList = hrList;
	}

	/**
	 * @return paginador: paginated list of human resources which can be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : paginated list of human resources which can be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paginadorForm : management responsible paged list from search
	 *         engine.
	 */
	public Paginador getPaginadorForm() {
		return paginadorForm;
	}

	/**
	 * @param paginadorForm
	 *            : management responsible paged list from search engine.
	 */
	public void setPaginadorForm(Paginador paginadorForm) {
		this.paginadorForm = paginadorForm;
	}

	/**
	 * @return hr: HR object
	 */
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : HR object
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return nameSearch: gets the name by which you want to consult Human
	 *         resources
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : gets the name by which you want to consult Human resources
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return lastNameSearch: Name by which you want to consult human Resources
	 */
	public String getLastNameSearch() {
		return lastNameSearch;
	}

	/**
	 * @param lastNameSearch
	 *            : Name by which you want to consult human Resources
	 */
	public void setLastNameSearch(String lastNameSearch) {
		this.lastNameSearch = lastNameSearch;
	}

	/**
	 * @return hrTypeSearch: Type of human resources for which they want search
	 *         for human resources.
	 */
	public int getHrTypeSearch() {
		return hrTypeSearch;
	}

	/**
	 * @param hrTypeSearch
	 *            : Type of human resources for which they want search for human
	 *            resources.
	 */
	public void setHrTypeSearch(int hrTypeSearch) {
		this.hrTypeSearch = hrTypeSearch;
	}

	/**
	 * @return paymentTypeSearch: Payment type for which you want to search for
	 *         human Resources.
	 */
	public int getPaymentTypeSearch() {
		return paymentTypeSearch;
	}

	/**
	 * @param paymentTypeSearch
	 *            : Payment type for which you want to search for human
	 *            Resources.
	 */
	public void setPaymentTypeSearch(int paymentTypeSearch) {
		this.paymentTypeSearch = paymentTypeSearch;
	}

	/**
	 * @return birthCountryItems: Items of the countries shown in the combo of
	 *         country of birth in the user interface
	 */
	public List<SelectItem> getBirthCountryItems() {
		return birthCountryItems;
	}

	/**
	 * @return birthStateItems: Items Items of the countries shown in the combo
	 *         of country of birth in the user interface
	 */
	public List<SelectItem> getBirthStateItems() {
		return birthStateItems;
	}

	/**
	 * @return birthMunicipalityItems: Items of the municipalities that are
	 *         shown in the combo municipality of birth interface user
	 */
	public List<SelectItem> getBirthMunicipalityItems() {
		return birthMunicipalityItems;
	}

	/**
	 * @return residenceCountryItems: Items of the municipalities that are shown
	 *         in the combo municipality of birth interface user
	 */
	public List<SelectItem> getResidenceCountryItems() {
		return residenceCountryItems;
	}

	/**
	 * @return residenceStateItems: Items of the departments shown in the
	 *         department of residence combo box interface user
	 */
	public List<SelectItem> getResidenceStateItems() {
		return residenceStateItems;
	}

	/**
	 * @return residenceMunicipalityItems: Items of the departments shown in the
	 *         department of residence combo box interface user
	 */
	public List<SelectItem> getResidenceMunicipalityItems() {
		return residenceMunicipalityItems;
	}

	/**
	 * @return maritalStatusItems: civil status items that are loaded into the
	 *         combo in the user interface.
	 */
	public List<SelectItem> getMaritalStatusItems() {
		return maritalStatusItems;
	}

	/**
	 * @return hrTypesItems: civil status items that are loaded into the combo
	 *         in the user interface.
	 */
	public List<SelectItem> getHrTypesItems() {
		return hrTypesItems;
	}

	/**
	 * @return paymentMethodsItems: the method of payment items that are loaded
	 *         into the combo in the user interface.
	 */
	public List<SelectItem> getPaymentMethodsItems() {
		return paymentMethodsItems;
	}

	/**
	 * @return hrPicName: filename of the picture that is associated with human
	 *         resource
	 */
	public String getHrPicName() {
		return hrPicName;
	}

	/**
	 * @param hrPicName
	 *            : filename of the picture that is associated with human
	 *            resource
	 */
	public void setHrPicName(String hrPicName) {
		this.hrPicName = hrPicName;
	}

	/**
	 * @return filesFolder: the actual folder path where you load the Photos of
	 *         human resources
	 */
	public String getFilesFolder() {
		this.filesFolder = Constantes.CARPETA_ARCHIVOS_RECURSOS_HUMANOS
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_PERSONAS;
		return filesFolder;
	}

	/**
	 * @return temporalFilesFolder: temporary folder path where Photo charge of
	 *         human resources
	 */
	public String getTemporalFilesFolder() {
		this.temporalFilesFolder = Constantes.CARPETA_ARCHIVOS_RECURSOS_HUMANOS
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return temporalFilesFolder;
	}

	/**
	 * 
	 * @return fileUploadBean: Variable that gets the object for loading files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : Variable that gets the object for loading files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return temporalPicLoaded: Flag indicating whether the picture is loaded
	 *         from temporary location or not
	 */
	public boolean isTemporalPicLoaded() {
		return temporalPicLoaded;
	}

	/**
	 * @param temporalPicLoaded
	 *            : Flag indicating whether the picture is loaded from temporary
	 *            location or not
	 */
	public void setTemporalPicLoaded(boolean temporalPicLoaded) {
		this.temporalPicLoaded = temporalPicLoaded;
	}

	/**
	 * @return currentDate: variable that gets the current system date.
	 */
	public Date getCurrentDate() {
		return currentDate;
	}

	/**
	 * @param currentDate
	 *            : variable that gets the current system date.
	 */
	public void setCurrentDate(Date currentDate) {
		this.currentDate = currentDate;
	}

	/**
	 * Initializes the search parameters to filter by hrTypes
	 * 
	 * @return searchHrs(): method that queries resource types human, returns to
	 *         the template management.
	 */
	public String initializeFilter() {
		nameSearch = "";
		lastNameSearch = "";
		paymentTypeSearch = 0;
		return searchHrs();
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of types of human resources
	 * 
	 * @modify 15/07/2015 Gerardo.Herrera
	 * 
	 * @return searchHrs: method to query the types of human resources, returns
	 *         to the template management.
	 */
	public String initializeSearch() {
		hrTypeSearch = 0;
		nameSearch = "";
		lastNameSearch = "";
		paymentTypeSearch = 0;
		return searchHrs();
	}

	/**
	 * Consult the list of hr
	 * 
	 * @modify 13/05/2015 Andres.Gomez
	 * @modify 14/07/2015 Gerardo.Herrera
	 * @modify 08/03/2016 Mabell.Boada
	 * 
	 * @return returns: Navigation rule that redirects to manage Human Resources
	 *         According condition
	 */
	public String searchHrs() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.hrList = new ArrayList<Hr>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		String returns = fromModal ? "" : "gesHumanResources";
		try {
			advancedSearch(queryBuilder, parameters, bundle,
					bundleRecursosHumanos, jointSearchMessages);
			Long amount = hrDao.hrAmount(queryBuilder, parameters);
			if (amount != null) {
				if (fromModal) {
					paginadorForm.paginarRangoDefinido(amount, 5);
					this.hrList = hrDao.queryHr(paginadorForm.getInicio(),
							paginadorForm.getRango(), queryBuilder, parameters);

				} else {
					paginador.paginar(amount);
					this.hrList = hrDao.queryHr(paginador.getInicio(),
							paginador.getRango(), queryBuilder, parameters);
				}
			}
			if ((hrList == null || hrList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (hrList == null || hrList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessage = MessageFormat
						.format(message, bundleRecursosHumanos
								.getString("recurso_humano_label"),
								jointSearchMessages);
			}
			if (amount != 0) {
				loadHrDetails();
			}
			loadHrTypesCombo();
			loadPaymentMethodsCombo();
			if (fromModal) {
				validations.setMensajeBusquedaPopUp(searchMessage);
			} else {
				validations.setMensajeBusqueda(searchMessage);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return returns;
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @modify 14/07/2015 Gerardo.Herrera
	 * 
	 * @param query
	 *            : query to concatenate
	 * @param parameter
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * 
	 * @param bundleHr
	 *            : access language tags HR
	 * 
	 * @param jointSearchMessages
	 *            : Message search
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameter, ResourceBundle bundle,
			ResourceBundle bundleHr, StringBuilder jointSearchMessages) {
		boolean addFilter = false;
		String hrType = "";
		String paymentType = "";
		if ((this.nameSearch != null && !"".equals(this.nameSearch))) {
			query.append(addFilter ? "AND " : "WHERE ");
			query.append(" UPPER(h.name) LIKE UPPER(:keywordNombre) ");
			SelectItem nameItem = new SelectItem("%" + this.nameSearch + "%",
					"keywordNombre");
			parameter.add(nameItem);
			jointSearchMessages.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"' + " ");
			addFilter = true;
		}
		if (this.lastNameSearch != null && !"".equals(this.lastNameSearch)) {
			query.append(addFilter ? "AND " : "WHERE ");
			query.append(" UPPER(h.familyName) LIKE UPPER(:keywordApellido) ");
			SelectItem lastNameItem = new SelectItem("%" + this.lastNameSearch
					+ "%", "keywordApellido");
			parameter.add(lastNameItem);
			jointSearchMessages.append(bundle.getString("label_apellido")
					+ ": " + '"' + this.lastNameSearch + '"' + " ");
			addFilter = true;
		}
		if (this.hrTypeSearch > 0) {
			query.append(addFilter ? "AND " : "WHERE ");
			query.append(" h.hrTypes.idHrType = :keywordHrType ");
			SelectItem hrTypeItem = new SelectItem(this.hrTypeSearch,
					"keywordHrType");
			parameter.add(hrTypeItem);
			hrType = searchTypeName(hrTypesItems, hrTypeSearch);
			jointSearchMessages.append(bundleHr
					.getString("tipo_recurso_humano_label")
					+ ": "
					+ '"'
					+ hrType + '"' + " ");
			addFilter = true;
		}
		if (this.paymentTypeSearch > 0) {
			query.append(addFilter ? "AND " : "WHERE ");
			query.append(" h.paymentMethods.idPaymentMethod = :keywordPaymentType ");
			SelectItem paymentTypeItem = new SelectItem(this.paymentTypeSearch,
					"keywordPaymentType");
			parameter.add(paymentTypeItem);
			paymentType = searchTypeName(paymentMethodsItems, paymentTypeSearch);
			jointSearchMessages.append(bundleHr
					.getString("recurso_humano_label_tipo_pago")
					+ ": "
					+ '"'
					+ paymentType + '"' + " ");
			addFilter = true;
		}
	}

	/**
	 * Lets get the value of an object in a List <SelectItem> passing as a
	 * search id
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param parameters
	 *            : List of objects type SelectItem
	 * @param id
	 *            : Object Identifier
	 * @return result: returns the value of the object sought
	 */
	private String searchTypeName(List<SelectItem> parameters, int id) {
		String result = "";
		for (SelectItem types : parameters) {
			if (types.getValue() == (Integer) id) {
				result = types.getLabel();
			}
		}
		return result;
	}

	/**
	 * Method to edit or create a new human resource.
	 * 
	 * @modify 15/03/2016 Sergio.Gelves
	 * 
	 * @param hr
	 *            : human resource that will add or edit
	 * 
	 * @return "regHumanResources": redirected to the template record human
	 *         Resources.
	 */
	public String addEditHr(Hr hr) {
		try {
			if (hr != null) {
				this.hr = hr;
				this.hrPicName = this.hr.getFoto();
				this.temporalPicLoaded = false;
				/*
				 * Just in case that there have been registered human resources
				 * without selecting a municipality or department, because they
				 * are foreign keys with a lazy retrieving
				 */
				if (this.hr.getMunicipioNacimiento() != null) {
					this.hr.setMunicipioNacimiento(this.hr
							.getMunicipioNacimiento());
				} else {
					this.hr.setMunicipioNacimiento(new Municipio());
				}
				if (this.hr.getMunicipioResidencia() != null) {
					this.hr.setMunicipioResidencia(this.hr
							.getMunicipioResidencia());
				} else {
					this.hr.setMunicipioResidencia(new Municipio());
				}
				if (this.hr.getDepartamentoNacimiento() != null) {
					this.hr.setDepartamentoNacimiento(this.hr
							.getDepartamentoNacimiento());
				} else {
					this.hr.setDepartamentoNacimiento(new Departamento());
				}
				if (this.hr.getDepartamentoResidencia() != null) {
					this.hr.setDepartamentoResidencia(this.hr
							.getDepartamentoResidencia());
				} else {
					this.hr.setDepartamentoResidencia(new Departamento());
				}
			} else {
				this.hr = new Hr();
				this.hrPicName = null;
				this.fileUploadBean = new FileUploadBean();
				this.temporalPicLoaded = true;
				this.hr.setMunicipioNacimiento(new Municipio());
				this.hr.setMunicipioResidencia(new Municipio());
				this.hr.setDepartamentoNacimiento(new Departamento());
				this.hr.setDepartamentoResidencia(new Departamento());
			}
			loadCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regHumanResources";
	}

	/**
	 * Delete method that allows a human resource database.
	 * 
	 * @return searchHrs(): human resources consulting the database and returns
	 *         to manage hr
	 */
	public String deleteHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			hrDao.deleteHr(hr);
			String format = MessageFormat
					.format(bundle.getString("message_registro_eliminar"),
							hr.getName());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchHrs();
	}

	/**
	 * Assigns the name of human resources to process validation, not to be
	 * repeated in the database.
	 * 
	 * @author Dario.Lopez
	 * 
	 * @param value
	 *            : field value is validated.
	 */
	public void validateAddNameXSS(Object value) {
		String nombre = (String) value;
		this.hr.setName(nombre);
	}

	/**
	 * Validates the full name of human resources, so that no is repeated in the
	 * database and validates against XSS.
	 * 
	 * @modify 11/09/2015 Dario.Lopez
	 * 
	 * @param contexto
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value is validated
	 */
	public void validateFullNameXSS(FacesContext contexto,
			UIComponent toValidate, Object value) {
		String lastName = (String) value;
		String name = (String) toValidate.getAttributes().get("nombre");
		String clientId = toValidate.getClientId(contexto);
		try {
			int id = hr.getIdHr();
			Hr hrAux = new Hr();
			hrAux = hrDao.fullNameExists(name, lastName, id);
			if (hrAux != null) {
				String existenceMessage = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						existenceMessage, "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(lastName, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Save or edit human resources
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @return searchHrs: Redirects to manage human resources in updated list
	 */
	public String saveHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		boolean picChanged = false;
		String picNameDelete = null;
		try {
			/* No son campos requeridos */
			if (this.hr.getDepartamentoNacimiento().getId() == 0) {
				this.hr.setDepartamentoNacimiento(null);
			}
			if (this.hr.getMunicipioNacimiento().getId() == 0) {
				this.hr.setMunicipioNacimiento(null);
			}
			if (this.hr.getDepartamentoResidencia().getId() == 0) {
				this.hr.setDepartamentoResidencia(null);
			}
			if (this.hr.getMunicipioResidencia().getId() == 0) {
				this.hr.setMunicipioResidencia(null);
			}
			if (this.hr.getCivilStatus().getId() == 0) {
				this.hr.setCivilStatus(null);
			}

			if (hr.getIdHr() != 0) {
				if (this.hr.getFoto() != null && !"".equals(this.hr.getFoto())
						&& !this.hr.getFoto().equals(this.hrPicName)) {
					this.deleteRealFile(this.hr.getFoto());
					picChanged = true;
				} else if (hr.getFoto() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					picChanged = true;
				}
				this.hr.setFoto(this.hrPicName);
				if (hr.getFoto() != null && picChanged) {
					picNameDelete = this.hrPicName;
					/* The image is loaded into the non-temporal folder */
					loadImageRealFolder();
				}
				hrDao.editHr(hr);
			} else {
				if (this.hrPicName != null && !"".equals(this.hrPicName.trim())) {
					picNameDelete = this.hrPicName;
					loadImageRealFolder();
				}
				this.hr.setFoto(this.hrPicName);
				registerMessage = "message_registro_guardar";
				hrDao.saveHr(hr);
			}
			/* Delete the temporal file */
			if (picNameDelete != null && !"".equals(picNameDelete)) {
				this.deleteFile(picNameDelete);
			}
			String format = MessageFormat.format(
					bundle.getString(registerMessage), hr.getName());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchHrs();
	}

	/**
	 * This method allows you to load country combos, department and
	 * municipality
	 * 
	 * @modify 14/07/2015 Gerardo.Herrera
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @throws Exception
	 * 
	 */
	private void loadCombos() throws Exception {
		/* Country */
		birthCountryItems = new ArrayList<SelectItem>();
		residenceCountryItems = new ArrayList<SelectItem>();
		List<Pais> countries = paisDao.consultarPaisesVigentes();
		if (countries != null) {
			for (Pais country : countries) {
				birthCountryItems.add(new SelectItem(country.getId(), country
						.getNombre()));
				residenceCountryItems.add(new SelectItem(country.getId(),
						country.getNombre()));
			}
		}
		/* Department or State */
		loadBirthStates();
		loadResidenceStates();
		/* Municipality */
		loadBirthMunicipality();
		loadResidenceMunicipality();
		/* Marital status */
		maritalStatusItems = new ArrayList<SelectItem>();
		List<CivilStatus> currentMaritalStatus = civilStatusDao
				.consultCivilStatus();
		if (currentMaritalStatus != null) {
			for (CivilStatus maritalStatus : currentMaritalStatus) {
				maritalStatusItems.add(new SelectItem(maritalStatus.getId(),
						maritalStatus.getName()));
			}
		}
		loadPaymentMethodsCombo();
		loadHrTypesCombo();
	}

	/**
	 * It allows charging combo types of human resources
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @throws Exception
	 */
	private void loadHrTypesCombo() throws Exception {
		hrTypesItems = new ArrayList<SelectItem>();
		List<HrTypes> currentHrTypes = hrTypesDao.queryHrTypes();
		if (currentHrTypes != null) {
			for (HrTypes hrType : currentHrTypes) {
				hrTypesItems.add(new SelectItem(hrType.getIdHrType(), hrType
						.getName()));
			}
		}
	}

	/**
	 * It allows loading the item select the types of payment
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @throws Exception
	 */
	private void loadPaymentMethodsCombo() throws Exception {
		paymentMethodsItems = new ArrayList<SelectItem>();
		List<PaymentMethods> currentPaymentTypes = paymentMethodsDao
				.queryPaymentMethods();
		if (currentPaymentTypes != null) {
			for (PaymentMethods paymentType : currentPaymentTypes) {
				paymentMethodsItems.add(new SelectItem(paymentType
						.getIdPaymentMethod(), paymentType.getName()));
			}
		}
	}

	/**
	 * This method makes the request of the departments registered in the
	 * database, a country associated with selected birth.
	 */
	public void loadBirthStates() {
		birthStateItems = new ArrayList<SelectItem>();
		birthMunicipalityItems = new ArrayList<SelectItem>();
		try {
			Pais birthCountry = hr.getPaisNacimiento();
			if (birthCountry != null && birthCountry.getId() > 0) {
				short idCountry = birthCountry.getId();
				List<Departamento> birthDepartments = departamentoDao
						.consultarDepartamentosPaisVigentes(idCountry);
				if (birthDepartments != null) {
					for (Departamento birthState : birthDepartments) {
						birthStateItems.add(new SelectItem(birthState.getId(),
								birthState.getNombre()));
					}
				}
				loadBirthMunicipality();
			} else {
				hr.setDepartamentoNacimiento(new Departamento());
				hr.setMunicipioNacimiento(new Municipio());
				hr.getDepartamentoNacimiento().setId(0);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the departments registered in the
	 * database, associated with a selected country residence.
	 */
	public void loadResidenceStates() {
		residenceStateItems = new ArrayList<SelectItem>();
		residenceMunicipalityItems = new ArrayList<SelectItem>();
		try {
			Pais residenceCountry = hr.getPaisResidencia();
			if (residenceCountry != null && residenceCountry.getId() > 0) {
				short idCountry = residenceCountry.getId();
				List<Departamento> residenceDepartment = departamentoDao
						.consultarDepartamentosPaisVigentes(idCountry);
				if (residenceDepartment != null) {
					for (Departamento residenceState : residenceDepartment) {
						residenceStateItems.add(new SelectItem(residenceState
								.getId(), residenceState.getNombre()));
					}
				}
				loadResidenceMunicipality();
			} else {
				hr.setDepartamentoResidencia(new Departamento());
				hr.setMunicipioResidencia(new Municipio());
				hr.getDepartamentoResidencia().setId(0);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered to the
	 * base data associated with the department of birth.
	 */
	public void loadBirthMunicipality() {
		birthMunicipalityItems = new ArrayList<SelectItem>();
		try {
			Departamento birthDepartment = hr.getDepartamentoNacimiento();
			if (birthDepartment != null && birthDepartment.getId() > 0
					&& this.birthStateItems.size() > 0) {
				int idDepartment = birthDepartment.getId();
				List<Municipio> birthMunicipalities = municipioDao
						.consultarMunicipiosVigentes(idDepartment);
				if (birthMunicipalities != null) {
					for (Municipio birthMunicipality : birthMunicipalities) {
						birthMunicipalityItems.add(new SelectItem(
								birthMunicipality.getId(), birthMunicipality
										.getNombre()));
					}
				}
			} else {
				birthMunicipalityItems = new ArrayList<SelectItem>();
				hr.setDepartamentoNacimiento(new Departamento());
				hr.setMunicipioNacimiento(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered to the
	 * base data associated with the department of residence.
	 */
	public void loadResidenceMunicipality() {
		residenceMunicipalityItems = new ArrayList<SelectItem>();
		try {
			Departamento residenceDepartment = hr.getDepartamentoResidencia();
			if (residenceDepartment != null && residenceDepartment.getId() > 0
					&& this.residenceStateItems.size() > 0) {
				int idDepartment = residenceDepartment.getId();
				List<Municipio> recidenceMunicipalities = municipioDao
						.consultarMunicipiosVigentes(idDepartment);
				if (recidenceMunicipalities != null) {
					for (Municipio municipality : recidenceMunicipalities) {
						residenceMunicipalityItems
								.add(new SelectItem(municipality.getId(),
										municipality.getNombre()));
					}
				}
			} else {
				residenceMunicipalityItems = new ArrayList<SelectItem>();
				hr.setDepartamentoResidencia(new Departamento());
				hr.setMunicipioResidencia(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method fills the various objects associated with a human resource
	 * 
	 * @throws Exception
	 */
	private void loadHrDetails() throws Exception {
		List<Hr> humanResources = new ArrayList<Hr>();
		humanResources.addAll(this.hrList);
		this.hrList = new ArrayList<Hr>();
		for (Hr hr : humanResources) {
			loadHrDetails(hr);
			this.hrList.add(hr);
		}
	}

	/**
	 * Method of uploading the details of a human resource.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @param hr
	 *            : human resources which will carry the details.
	 * @throws Exception
	 */
	private void loadHrDetails(Hr hr) throws Exception {
		int idHr = hr.getIdHr();
		/* Look for the Human resources objects */
		HrTypes hrTypes = (HrTypes) this.hrDao.queryHrObject("hrTypes", idHr);
		PaymentMethods paymentMethods = (PaymentMethods) this.hrDao
				.queryHrObject("paymentMethods", idHr);
		CivilStatus maritalStatus = (CivilStatus) this.hrDao.queryHrObject(
				"civilStatus", idHr);
		Pais birthCountry = (Pais) this.hrDao.queryHrObject("paisNacimiento",
				idHr);
		Departamento birthState = (Departamento) this.hrDao.queryHrObject(
				"departamentoNacimiento", idHr);
		Municipio birthMunicipality = (Municipio) this.hrDao.queryHrObject(
				"municipioNacimiento", idHr);
		Pais residenceCountry = (Pais) this.hrDao.queryHrObject(
				"paisResidencia", idHr);
		Departamento residenceState = (Departamento) this.hrDao.queryHrObject(
				"departamentoResidencia", idHr);
		Municipio residenceMunicipality = (Municipio) this.hrDao.queryHrObject(
				"municipioResidencia", idHr);
		/* Human resource properties will be filled up */
		hr.setHrTypes(hrTypes);
		hr.setPaymentMethods(paymentMethods);
		hr.setCivilStatus(maritalStatus);
		hr.setPaisNacimiento(birthCountry);
		hr.setDepartamentoNacimiento(birthState);
		hr.setMunicipioNacimiento(birthMunicipality);
		hr.setPaisResidencia(residenceCountry);
		hr.setDepartamentoResidencia(residenceState);
		hr.setMunicipioResidencia(residenceMunicipality);
	}

	/**
	 * Delete the file name.
	 */
	public void deleteFileName() {
		if (hrPicName != null && !"".equals(hrPicName)
				&& !hrPicName.equals(hr.getFoto()) && this.temporalPicLoaded) {
			deleteFile(hrPicName);
		}
		hrPicName = null;
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
		String path[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getTemporalFilesFolder() };
		fileUploadBean.delete(path, fileName);
	}

	/**
	 * Delete files from the actual location
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
	 * It allows you to load the file system
	 * 
	 * @param e
	 *            : Fileupload event for the file to be up server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String allowedExtns[] = Constantes.EXT_IMG.split(", ");
		String paths[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getTemporalFilesFolder() };
		fileUploadBean.setUploadedFile(e.getFile());
		long maxFileSize = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(allowedExtns,
				paths, maxFileSize);
		String message = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			message = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"), maxFileSize,
					"MB");
			ControladorContexto.mensajeError("formRegistrarHr:uploadFoto",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			message = "error_carga_archivo";
		}
		if (!"".equals(message)) {
			ControladorContexto.mensajeError("formRegistrarHr:uploadFoto",
					bundle.getString(message));
		}
		if (hr.getIdHr() != 0) {
			temporalPicLoaded = true;
		}
		hrPicName = fileUploadBean.getFileName();
	}

	/**
	 * Add photo image to the actual folder
	 * 
	 * @throws Exception
	 */
	private void loadImageRealFolder() throws Exception {
		String origin = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getTemporalFilesFolder();
		String firstTarget = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFilesFolder();
		String secondTarget = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getFilesFolder();

		FileUploadBean.fileExist(firstTarget);
		FileUploadBean.fileExist(secondTarget);

		File filePath = new File(origin, fileUploadBean.getFileName());
		File firstTargetFolder = new File(firstTarget,
				fileUploadBean.getFileName());
		File secondTargetFolder = new File(secondTarget,
				fileUploadBean.getFileName());

		FileUploadBean.copyFile(filePath, firstTargetFolder);
		FileUploadBean.copyFile(filePath, secondTargetFolder);
	}

	/**
	 * This method allows dynamically calculate the cost per hour from the
	 * information of the payment information in the form registrarHR view
	 * 
	 * @author Dario.Lopez
	 * 
	 * @return hourCost: cost per hour
	 */
	public double calculateCostPerHour() {
		double hourCost = 0;
		double annualWage = 0;
		double hoursPerDay = 0;
		double totalDays = 0;
		if (hr.getTotalNumbersDays() != null && hr.getTotalNumbersDays() != 0
				&& hr.getHoursPerDay() != 0) {
			annualWage = this.hr.getAnnualWage();
			hoursPerDay = this.hr.getHoursPerDay();
			totalDays = this.hr.getTotalNumbersDays();
			hourCost = (annualWage / (hoursPerDay * totalDays));
			hr.setHourCost(hourCost);
		} else {
			hourCost = 0;
			return hourCost;
		}
		return hourCost;
	}

	/**
	 * Assigns a state maternityBreastFeeding considering gender HR
	 * 
	 * @author Gerardo.Herrera
	 */
	public void assignMaternityBreastFeeding() {
		if (hr.getGenero().equals(Constantes.GENERO) && hr.getGenero() != null) {
			hr.setMaternityBreastFeeding(false);
		}
	}
}
