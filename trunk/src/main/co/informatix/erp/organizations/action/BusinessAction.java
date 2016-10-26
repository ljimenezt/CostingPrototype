package co.informatix.erp.organizations.action;

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

import org.primefaces.event.FileUploadEvent;
import org.richfaces.event.ItemChangeEvent;

import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.organizations.dao.BusinessDao;
import co.informatix.erp.organizations.dao.OrganizationDao;
import co.informatix.erp.organizations.entities.Business;
import co.informatix.erp.organizations.entities.Organization;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class lets you handle the business logic of the companies that exist in
 * the system, which allows you to insert, modify, and query.
 * 
 * @author Oscar.Amaya
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class BusinessAction implements Serializable {

	@EJB
	private BusinessDao businessDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private OrganizationDao organizationDao;
	@EJB
	private PaisDao countryDao;
	@EJB
	private DepartamentoDao departmentDao;
	@EJB
	private MunicipioDao townDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private Paginador pagination = new Paginador();
	private Business businessValidity;
	private Business business;
	private List<Business> listBusiness;
	private List<SelectItem> itemsOrganizations;
	private List<SelectItem> itemsCountries;
	private List<SelectItem> itemDepartments;
	private List<SelectItem> itemsMunicipalities;
	private String vigencia = Constantes.SI;
	private String tabSelect = Constantes.N_TAB;
	private String option;
	private String fileFolder;
	private String fileFolderTemporal;
	private String namePhotoLogo;
	private String labelCreate;
	private String messageMiga;
	private String nameSearch;
	private boolean loadPhotoTemporal;

	/**
	 * @return labelCreate: Label to show when a company is created
	 */
	public String getLabelCreate() {
		return labelCreate;
	}

	/**
	 * @return namePhotoLogo: filename of the logo associated with the company
	 */
	public String getNamePhotoLogo() {
		return namePhotoLogo;
	}

	/**
	 * @param namePhotoLogo
	 *            : filename of the logo associated with the company
	 */
	public void setNamePhotoLogo(String namePhotoLogo) {
		this.namePhotoLogo = namePhotoLogo;
	}

	/**
	 * @return itemsOrganizations: Items organizations that are uploaded the
	 *         combo of the user interface
	 */
	public List<SelectItem> getItemsOrganizations() {
		return itemsOrganizations;
	}

	/**
	 * @return fileFolder: the actual folder path where you load the photos of
	 *         company logos
	 */
	public String getFileFolder() {
		this.fileFolder = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_LOGOS_EMPRESAS;
		return fileFolder;
	}

	/**
	 * @return fileFolderTemporal: temporary folder path where carry the logos
	 *         of companies
	 */
	public String getFileFolderTemporal() {
		this.fileFolderTemporal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return fileFolderTemporal;
	}

	/**
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
	 * @return vigencia: gets the value for the management of the current of the
	 *         registers
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : sets the value for the management of the current of the
	 *            registers
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return businessValidity: company to modify the current
	 */
	public Business getBusinessValidity() {
		return businessValidity;
	}

	/**
	 * @param businessValidity
	 *            : company to modify the current
	 */
	public void setBusinessValidity(Business businessValidity) {
		this.businessValidity = businessValidity;
	}

	/**
	 * @return nameSearch: Variable that gets the name of the company that is
	 *         searches the user interface.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Variable that sets the name of the company that is searches
	 *            the user interface.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return option: allows to add or edit a new company existing
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param option
	 *            : allows to add or edit a new company existing
	 */
	public void setOption(String option) {
		this.option = option;
	}

	/**
	 * @return tabSelect: Variable that gets the name of the tab you want to
	 *         load as selected.
	 */
	public String getTabSelect() {
		return tabSelect;
	}

	/**
	 * @param tabSelect
	 *            : Variable that gets the name of the tab you want to load as
	 *            selected.
	 */
	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}

	/**
	 * @return pagination: Object to the functions of the pager companies
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Object to the functions of the pager companies
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return business: object of the company to which the record or editing is
	 *         done.
	 */
	public Business getBusiness() {
		return business;
	}

	/**
	 * @param business
	 *            : object of the company to which the record or editing is
	 *            done.
	 */
	public void setBusiness(Business business) {
		this.business = business;
	}

	/**
	 * @return itemsCountries: Items of the countries that are displayed in a
	 *         combo in the user interface
	 */
	public List<SelectItem> getItemsCountries() {
		return itemsCountries;
	}

	/**
	 * @return itemDepartments: Items of the countries that are displayed in a
	 *         combo in the user interface
	 */
	public List<SelectItem> getItemDepartments() {
		return itemDepartments;
	}

	/**
	 * @return itemsMunicipalities: Items of the municipalities that are
	 *         displayed in a combo in the user interface
	 */
	public List<SelectItem> getItemsMunicipalities() {
		return itemsMunicipalities;
	}

	/**
	 * @return listBusiness: list of companies that are loaded into the user
	 *         interface.
	 */
	public List<Business> getListBusiness() {
		return listBusiness;
	}

	/**
	 * @param listBusiness
	 *            : list of companies that are loaded into the user interface.
	 */
	public void setListBusiness(List<Business> listBusiness) {
		this.listBusiness = listBusiness;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on the role from which the company releases
	 * 
	 * @return messageMiga: message crumb of bread in the template
	 */
	public String getMessageMiga() {
		return messageMiga;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on the role from which the company releases
	 * 
	 * @param messageMiga
	 *            : message crumb of bread in the template
	 */
	public void setMessageMiga(String messageMiga) {
		this.messageMiga = messageMiga;
	}

	/**
	 * @return loadPhotoTemporal: Flag indicating whether the picture is loaded
	 *         from the temporary location or not
	 */
	public boolean isLoadPhotoTemporal() {
		return loadPhotoTemporal;
	}

	/**
	 * @param loadPhotoTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not
	 */
	public void setLoadPhotoTemporal(boolean loadPhotoTemporal) {
		this.loadPhotoTemporal = loadPhotoTemporal;
	}

	/**
	 * This method establishes a joint company instance, also validates the
	 * selection of at least one role and required fields Company
	 * 
	 * @param business
	 *            : company establish
	 * @throws Exception
	 */
	public void establishBusinessValidateRoleAndRequired(Business business)
			throws Exception {
		if (requiredOk("formRegistrarEmpresa")) {
			this.business = business;
		}
	}

	/**
	 * This method creates a new object class Business and load combo. This
	 * method is used when loading a page to register a new company.
	 * 
	 * @modify 26/01/2012 marisol.calderon
	 * @modify 16/02/2012 marisol.calderon
	 * 
	 * @param role
	 *            : This role is to create the company, these can be ... Normal
	 *            (n), customer (c), insurance (s), transportation (t), provider
	 *            (p)
	 * @return page: Navigation rule that directs to the View to record the
	 *         company.
	 */
	public String newBusiness(char role) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("messageOrganizations");
		String page = "regBusiness";
		try {
			this.option = Constantes.NUEVO;
			this.business = new Business();
			this.loadCombos();
			this.namePhotoLogo = null;
			this.fileUploadBean = new FileUploadBean();
			this.loadPhotoTemporal = true;
			if (Constantes.N_TAB.equals(role)) {
				this.labelCreate = bundleOrg
						.getString("company_label_register");
				this.messageMiga = "messageOrganizations.company_label_create";
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return page;
	}

	/**
	 * This method is used when loading a page to edit a business. Edits always
	 * be loaded in the same registrarEmpresa template to associate a new role
	 * at the company to edit
	 * 
	 * @modify Luis.Ruiz
	 * @modify 25/01/2012 marisol.calderon
	 * @modify 16/02/2012 marisol.calderon
	 * 
	 * @param businessEdit
	 *            : The company that publishes
	 * @param role
	 *            : role or type of company that is selected for editing, in the
	 *            case 'f' the same procedure is performed 'n'
	 * @return "regBusiness": returns to the template registrarEmpresa
	 */
	public String editBusiness(Business businessEdit, char role) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("messageOrganizations");
		try {
			option = Constantes.EDITAR;
			this.business = businessEdit;
			this.namePhotoLogo = this.business.getLogo();
			this.loadPhotoTemporal = false;
			loadCombos();
			/*
			 * Message displayed depending on the role in registrarEmpresa
			 * template.
			 */
			if (Constantes.N_TAB.equals(role)) {
				this.labelCreate = bundleOrg
						.getString("company_label_register");
				messageMiga = "messageOrganizations.company_label_modify";
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regBusiness";
	}

	/**
	 * Method for consulting companies from the pager, the method takes no
	 * parameters for the template used Pager
	 */
	public void consultBusiness() {
		String roleBusiness = ControladorContexto.getParam("param2");
		consultBusiness(roleBusiness);
	}

	/**
	 * Initializes the name in enterprise search.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultBusiness(): Method consulting firms present in the system
	 *         and returns to the management staff with the search results.
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		return consultBusiness(Constantes.N_TAB);
	}

	/**
	 * This method consulting firms depending on the type of company that has
	 * each.
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 * @modify 11/10/2012 Adonay.Mantilla
	 * 
	 * @param role
	 *            : establishes the role of business for companies on request.
	 * @return gesBusiness: Navigation rule that routes to the template list
	 *         management companies.
	 */
	public String consultBusiness(String role) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleOrganizationes = ControladorContexto
				.getBundle("messageOrganizations");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		this.business = new Business();
		try {
			String conditionValidity = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				conditionValidity = Constantes.IS_NOT_NULL;
			}
			setTabSelect(role);
			advancedSearch(consult, parameters, bundle, unionMessagesSearch);
			if (Constantes.N_TAB.equals(role)) {
				/* All companies are queried */
				this.pagination.paginar(this.businessDao.mountBusiness(
						conditionValidity, consult, parameters));
				this.listBusiness = this.businessDao.consultBusiness(
						this.pagination.getInicio(),
						this.pagination.getRango(), conditionValidity, consult,
						parameters);
			} else if (Constantes.F_TAB.equals(role)) {
				/* Businesses are consulted with estates */
				this.pagination.paginar(this.businessDao
						.amountBusinessWithEstates(conditionValidity));
				this.listBusiness = this.businessDao
						.consultBusinessWithEstates(
								this.pagination.getInicio(),
								this.pagination.getRango(), conditionValidity);
			}
			this.businessValidity = new Business();
			loadDetailsBusiness();
			/* Search posts are build */
			if ((this.listBusiness == null || this.listBusiness.size() <= 0)
					&& (this.nameSearch != null && !"".equals(this.nameSearch))) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (this.listBusiness == null
					|| this.listBusiness.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleOrganizationes
										.getString("company_label_s"),
										unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesBusiness";
	}

	/**
	 * This method build the consult for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @author Adonay.Mantilla
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
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			SelectItem item = new SelectItem("%" + nameSearch + "%", "keyword");
			consult.append(" AND UPPER(e.name) LIKE UPPER(:keyword) ");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * This method fills the various objects associated with a business
	 * 
	 * @modify 01/02/2012 marisol.calderon
	 * 
	 * @throws Exception
	 */
	public void loadDetailsBusiness() throws Exception {
		List<Business> listBusiness = new ArrayList<Business>();
		listBusiness.addAll(this.listBusiness);
		this.listBusiness = new ArrayList<Business>();
		for (Business business : listBusiness) {
			loadDetailsOneBusiness(business);
			this.listBusiness.add(business);
		}
	}

	/**
	 * Method of uploading the details of a company.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param business
	 *            : company which will carry the details.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void loadDetailsOneBusiness(Business business) throws Exception {
		int idBusiness = business.getId();
		/* Objects are consulted */
		Pais country = (Pais) this.businessDao.consultObjectBusiness("country",
				idBusiness);
		Departamento department = (Departamento) this.businessDao
				.consultObjectBusiness("department", idBusiness);
		Municipio town = (Municipio) this.businessDao.consultObjectBusiness(
				"town", idBusiness);
		List<Organization> organization = (List<Organization>) this.businessDao
				.consultListObjectOfBusiness("organization", idBusiness);
		/* They are assigned to the company */
		business.setCountry(country);
		business.setDepartment(department);
		business.setTown(town);
		if (organization.size() > 0) {
			business.setOrganization(organization.get(0));
		} else {
			business.setOrganization(new Organization());
		}
	}

	/**
	 * Method to execute the action of the tabs to be selected by the user
	 * 
	 * @param event
	 *            : event that runs when a tab is selected
	 */
	public void changedTab(ItemChangeEvent event) {
		String idTab = event.getNewItemName();
		if (idTab != null && !"".equals(idTab)) {
			consultBusiness(idTab);
		}
	}

	/**
	 * This method modifies the current of a business
	 * 
	 * @param validity
	 *            : boolean that allows to know if the current ends with 'true'
	 *            or INICA with 'false', the selected record in the user
	 *            interface.
	 * @param role
	 *            : establishes the role of business for companies on request.
	 * @return consultBusiness(): Consulting companies present in the system
	 *         according to the selected role and returns to the management
	 *         staff.
	 */
	public String validityBusiness(boolean validity, String role) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_fin_vigencia_satisfactorio";
		try {
			if (Constantes.N_TAB.equals(role)) {
				this.businessValidity.setUserName(this.identity.getUserName());
				/* The organization is not required field */
				if (this.businessValidity.getOrganization() != null
						&& this.businessValidity.getOrganization().getId() == 0) {
					this.businessValidity.setOrganization(null);
				}
				if (validity) {
					this.businessValidity.setDateEndValidity(new Date());
					this.businessDao.modifyBusiness(this.businessValidity);
				} else {
					key = "message_inicio_vigencia_satisfactorio";
					this.businessValidity.setDateEndValidity(null);
					this.businessDao.modifyBusiness(this.businessValidity);
				}
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(key) + ": {0}",
								this.businessValidity.getName()));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultBusiness(role);
	}

	/**
	 * This method allows you to register a new company, obtains the data stored
	 * in the form and stored in the database
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 * @modify 20/09/2012 Gerson.Cespedes
	 * 
	 * @param role
	 *            : Type of business that the company you select to save, this
	 *            value is used so that the tab for the type of company could be
	 *            loading the list.
	 * @return String: Navigation rule that returns to the template record or
	 *         check depending on the case company.
	 */
	public String addEditBusiness(String role) {
		ResourceBundle bundle2 = ControladorContexto
				.getBundle("messageOrganizations");
		String message = "";
		String namePhotoDelete = null;
		String exit = "regBusiness";
		boolean edition = false;
		boolean logoChanged = false;
		boolean error = false;
		try {
			this.userTransaction.begin();
			/* The organization is not required field */
			if (this.business.getOrganization().getId() == 0) {
				this.business.setOrganization(null);
			}
			this.business.setUserName(this.identity.getUserName());
			if (this.business.getId() != 0) {
				edition = true;
				if (this.business.getLogo() != null
						&& !"".equals(this.business.getLogo())
						&& !this.business.getLogo().equals(this.namePhotoLogo)) {
					this.deleteFileReal(this.business.getLogo());
					logoChanged = true;
				} else if (business.getLogo() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					logoChanged = true;
				}
				this.business.setLogo(this.namePhotoLogo);
				if (business.getLogo() != null && logoChanged) {
					namePhotoDelete = this.namePhotoLogo;
					/* The image is loaded into the actual location */
					uploadImageLocationReal();
				}
				this.businessDao.modifyBusiness(this.business);

				message = MessageFormat.format(bundle2
						.getString("company_message_successfully_modified"),
						this.business.getName(), this.business.getNit());
			} else {
				if (this.namePhotoLogo != null
						&& !"".equals(this.namePhotoLogo.trim())) {
					namePhotoDelete = this.namePhotoLogo;
					uploadImageLocationReal();
				}
				this.business.setDateCreation(new Date());
				this.business.setLogo(this.namePhotoLogo);
				this.businessDao.createBusiness(this.business);
				message = MessageFormat.format(bundle2
						.getString("company_message_successfully_created"),
						business.getName(), business.getNit());
			}
			this.userTransaction.commit();
			/* The temporary file is deleted */
			if (namePhotoDelete != null && !"".equals(namePhotoDelete)) {
				this.deleteFile(namePhotoDelete);
			}
			ControladorContexto.mensajeInformacion(null, message);
			exit = consultBusiness(role);
		} catch (Exception e) {
			error = true;
			if (this.namePhotoLogo != null && !"".equals(this.namePhotoLogo)
					&& !edition) {
				this.deleteFileReal(this.namePhotoLogo);
			}
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		if (!error) {
			this.business = new Business();
		}
		return exit;
	}

	/**
	 * This method allows you to load combo country, department, municipality
	 * company, nature of business, organization and unit of measure
	 * 
	 * @modify Luis.Ruiz
	 * @modify 01/02/2012 marisol.calderon
	 * @modify 21/01/2013 Adonay.Mantilla
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
		loadDepartments();
		loadMunicipalities();
		itemsOrganizations = new ArrayList<SelectItem>();
		List<Organization> listOrganizationsValidity = organizationDao
				.consultActiveOrganizations();
		if (listOrganizationsValidity != null) {
			for (Organization organization : listOrganizationsValidity) {
				itemsOrganizations.add(new SelectItem(organization.getId(),
						organization.getBusinessName()));
			}
		}
	}

	/**
	 * This method makes the request of the departments registered in the
	 * database, associated with a selected country.
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 */
	public void loadDepartments() {
		itemDepartments = new ArrayList<SelectItem>();
		itemsMunicipalities = new ArrayList<SelectItem>();
		try {
			Pais country = business.getCountry();
			if (country != null && country.getId() > 0) {
				short idPais = country.getId();
				List<Departamento> departments = departmentDao
						.consultarDepartamentosPaisVigentes(idPais);
				if (departments != null) {
					for (Departamento d : departments) {
						itemDepartments.add(new SelectItem(d.getId(), d
								.getNombre()));
					}
				}
				loadMunicipalities();
			} else {
				business.setDepartment(new Departamento());
				business.setTown(new Municipio());
				business.getDepartment().setId(0);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered in the
	 * database, associated with a department selected a partner country.
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 * @modify 30/03/2012 angelica.amaya
	 */
	public void loadMunicipalities() {
		itemsMunicipalities = new ArrayList<SelectItem>();
		try {
			Departamento department = business.getDepartment();
			if (department != null && department.getId() > 0
					&& this.itemDepartments.size() > 0) {
				int idDepartamento = department.getId();
				List<Municipio> municipalities = townDao
						.consultarMunicipiosVigentes(idDepartamento);
				if (municipalities != null) {
					for (Municipio m : municipalities) {
						itemsMunicipalities.add(new SelectItem(m.getId(), m
								.getNombre()));
					}
				}
			} else {
				itemsMunicipalities = new ArrayList<SelectItem>();
				business.setDepartment(new Departamento());
				business.setTown(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method validates the NIT of a company
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validateNitBusinessXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle2 = ControladorContexto
				.getBundle("messageOrganizations");
		String nit = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			Integer id = 0;
			if (this.business.getId() != 0) {
				id = this.business.getId();
			}
			boolean validateNit = businessDao.nitExist(nit, id);
			if (validateNit) {
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle2
								.getString("company_message_validate_nit"),
								null));
			}
			if (!EncodeFilter.validarXSS(nit, clientId, "locate.regex.nit")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allow delete the file name.
	 * 
	 * @author Luis.Ruiz
	 */
	public void deleteFilename() {
		if (namePhotoLogo != null && !"".equals(namePhotoLogo)
				&& !namePhotoLogo.equals(business.getLogo())
				&& this.loadPhotoTemporal) {
			deleteFile(namePhotoLogo);
		}
		namePhotoLogo = null;
		fileUploadBean.setFileName(null);
	}

	/**
	 * method that allows delete files.
	 * 
	 * @author Luis.Ruiz
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 */
	public void deleteFile(String fileName) {
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFileFolderTemporal() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * Delete files from the actual location
	 * 
	 * @author marisol.calderon
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 */
	public void deleteFileReal(String fileName) {
		String locations[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH + this.getFileFolder(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE + this.getFileFolder() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * Allows you to load the file system
	 * 
	 * @author Luis.Ruiz
	 * @modify 02/02/2015 Jonathan.Arias
	 * 
	 * @param e
	 *            : File upload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAccepted[] = Constantes.EXT_IMG.split(", ");
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFileFolderTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		long maximumFileSize = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(extAccepted,
				locations, maximumFileSize);
		String message = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			message = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"),
					maximumFileSize, "MB");
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			message = "error_carga_archivo";
		}
		if (!"".equals(message)) {
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					bundle.getString(message));
		}
		if (business.getId() != 0) {
			loadPhotoTemporal = true;
		}
		namePhotoLogo = fileUploadBean.getFileName();
	}

	/**
	 * Add the logo image to the actual folder
	 * 
	 * @author marisol.calderon
	 * 
	 * @throws Exception
	 */
	private void uploadImageLocationReal() throws Exception {
		String origen = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFileFolderTemporal();
		String destino1 = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFileFolder();
		String destino2 = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getFileFolder();

		/* It checks whether the destinations are created there but */
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
	 * Validates fields that are required in the view so that you can load
	 * regardless logo that are not filled out these fields
	 * 
	 * @author marisol.calderon
	 * 
	 * @param nameForm
	 *            : name or id form in which the message is displayed.
	 * @return result: boolean to true if all fields are good or false
	 *         otherwise.
	 * @throws Exception
	 */
	public boolean requiredOk(String nameForm) throws Exception {
		boolean result = true;
		if (business.getNit() == null || "".equals(business.getNit())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":nit");
			result = false;
		}
		if (business.getName() == null || "".equals(business.getName())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":name");
			result = false;
		}
		if (business.getAddress() == null || "".equals(business.getAddress())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":address");
			result = false;
		}
		if (business.getPhone() == null || "".equals(business.getPhone())) {
			ControladorContexto.mensajeRequeridos(nameForm + ":telefono");
			result = false;
		}
		if (business.getCountry().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboPaisEmpresa");
			result = false;
		}
		if (business.getDepartment().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboDepartamentoEmpresa");
			result = false;
		}
		if (business.getTown().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nameForm
					+ ":comboMunicipioEmpresa");
			result = false;
		}
		if (!EncodeFilter.validarXSS(namePhotoLogo, nameForm + ":uploadLogo",
				null)) {
			result = false;
		}

		return result;
	}

	/**
	 * This method makes the registration of a company from a modal
	 * 
	 * @author marisol.calderon
	 */
	public void saveBusinessFromDialog() {
		try {
			if (this.requiredOk("formRegistrarEmpresa")
					&& ControladorContexto.getMaxSeverity() == null) {
				addEditBusiness(Constantes.N_TAB);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}