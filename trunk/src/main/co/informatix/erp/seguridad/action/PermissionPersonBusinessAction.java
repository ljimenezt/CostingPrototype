package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.organizations.action.BusinessAction;
import co.informatix.erp.organizations.dao.BusinessDao;
import co.informatix.erp.organizations.entities.Business;
import co.informatix.erp.humanResources.entities.Person;
import co.informatix.erp.seguridad.dao.PermissionPersonBusinessDao;
import co.informatix.erp.seguridad.entities.PermissionPersonBusiness;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class allows business logic of companies with its subsidiaries and / or
 * farms that have access to information, people in the system.
 * 
 * The logic is to see, add, change the life of people access to businesses.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PermissionPersonBusinessAction implements Serializable {

	@EJB
	private BusinessDao businessDao;
	@EJB
	private PermissionPersonBusinessDao permissionPersonBusinessDao;
	@EJB
	private FarmDao farmDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private Paginador pagination = new Paginador();
	private PermissionPersonBusiness permissionPersonBusiness;
	private Person person;
	private Business selectedCompany;

	private List<PermissionPersonBusiness> listPermissionPersonBusiness;
	private List<PermissionPersonBusiness> listPermissionPersonBusinessTemp;
	private List<Business> business;

	private List<SelectItem> itemsBranchOfficesCompany;
	private List<SelectItem> itemsFarmsCompany;
	private List<SelectItem> itemsBranchOffices;
	private List<SelectItem> itemsFarms;

	private String searchCompany;
	private String searchCompanyManage;
	private String vigencia = Constantes.SI;
	private int idBranchOffice;
	private int idFarm;
	private int idSearchBranchOffice;
	private int idFarmSearch;

	/**
	 * @return pagination: Object pager functions from the list of companies
	 *         with permissions to the person.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :Object pager functions from the list of companies with
	 *            permissions to the person.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return listPermissionPersonBusiness: list of companies to which the
	 *         person has access.
	 */
	public List<PermissionPersonBusiness> getListPermissionPersonBusiness() {
		return listPermissionPersonBusiness;
	}

	/**
	 * @param listPermissionPersonBusiness
	 *            : list of companies to which the person has access.
	 */
	public void setListPermissionPersonBusiness(
			List<PermissionPersonBusiness> listPermissionPersonBusiness) {
		this.listPermissionPersonBusiness = listPermissionPersonBusiness;
	}

	/**
	 * @return searchCompany: word by which companies want to search.
	 */
	public String getSearchCompany() {
		return searchCompany;
	}

	/**
	 * @param searchCompany
	 *            : word by which companies want to search.
	 */
	public void setsearchCompany(String searchCompany) {
		this.searchCompany = searchCompany;
	}

	/**
	 * @return business: Companies that are queried to be associated with the
	 *         person.
	 */
	public List<Business> getBusiness() {
		return business;
	}

	/**
	 * @param business
	 *            : Companies that are queried to be associated with the person.
	 */
	public void setBusiness(List<Business> business) {
		this.business = business;
	}

	/**
	 * @return permissionPersonBusiness: PermissionPersonBusiness object that is
	 *         used to associate companies, branches or farms to which the
	 *         person has access.
	 */
	public PermissionPersonBusiness getPermissionPersonBusiness() {
		return permissionPersonBusiness;
	}

	/**
	 * @param permissionPersonBusiness
	 *            : PermissionPersonBusiness object that is used to associate
	 *            companies, branches or farms to which the person has access.
	 */
	public void setPermissionPersonBusiness(
			PermissionPersonBusiness permissionPersonBusiness) {
		this.permissionPersonBusiness = permissionPersonBusiness;
	}

	/**
	 * @return listPermissionPersonBusinessTemp: Temporary list of the selected
	 *         to give businesses access to a person.
	 */
	public List<PermissionPersonBusiness> getListPermissionPersonBusinessTemp() {
		return listPermissionPersonBusinessTemp;
	}

	/**
	 * @param listPermissionPersonBusinessTemp
	 *            : Temporary list of the selected to give businesses access to
	 *            a person.
	 */
	public void setLlistPermissionPersonBusinessTemp(
			List<PermissionPersonBusiness> listPermissionPersonBusinessTemp) {
		this.listPermissionPersonBusinessTemp = listPermissionPersonBusinessTemp;
	}

	/**
	 * @return person: gets person to whom it is going to assign access
	 *         permissions businesses.
	 */
	public Person getPerson() {
		return person;
	}

	/**
	 * 
	 * @param person
	 *            : sets person to whom it is going to assign access permissions
	 *            businesses.
	 */
	public void setPerson(Person person) {
		this.person = person;
	}

	/**
	 * @return itemsBranchOfficesCompany: Items branch of the company that are
	 *         shown in the combo of the user interface.
	 */
	public List<SelectItem> getItemsBranchOfficesCompany() {
		return itemsBranchOfficesCompany;
	}

	/**
	 * @return itemsFarmsCompany: items from the farm of the company is selected
	 *         to associate permissions to the person.
	 */
	public List<SelectItem> getItemsFarmsCompany() {
		return itemsFarmsCompany;
	}

	/**
	 * @return itemsFarms: farms items shown in the search combo in the user
	 *         interface.
	 */
	public List<SelectItem> getItemsFarms() {
		return itemsFarms;
	}

	/**
	 * @return itemsBranchOffices: branch items shown in the search combo in the
	 *         user interface.
	 */
	public List<SelectItem> getItemsBranchOffices() {
		return itemsBranchOffices;
	}

	/**
	 * @return vigencia: allows gets the selected value 'yes' of existing and
	 *         'no' for not applicable
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : allows gets the selected value 'yes' of existing and 'no'
	 *            for not applicable
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return searchCompanyManage: variable which the company seeks from
	 *         management of permits.
	 */
	public String getSearchCompanyManage() {
		return searchCompanyManage;
	}

	/**
	 * @param searchCompanyManage
	 *            : variable which the company seeks from management of permits.
	 */
	public void setSearchCompanyManage(String searchCompanyManage) {
		this.searchCompanyManage = searchCompanyManage;
	}

	/**
	 * @return idBranchOffice: id of the branch that selects the user
	 *         permissions associated with the company.
	 */
	public int getIdBranchOffice() {
		return idBranchOffice;
	}

	/**
	 * @param idBranchOffice
	 *            : id of the branch that selects the user permissions
	 *            associated with the company.
	 */
	public void setIdBranchOffice(int idBranchOffice) {
		this.idBranchOffice = idBranchOffice;
	}

	/**
	 * @return idFarm: id of the farm that selects the user permissions to
	 *         associate with the company.
	 */
	public int getIdFarm() {
		return idFarm;
	}

	/**
	 * @param idFarm
	 *            : id of the farm that selects the user permissions to
	 *            associate with the company.
	 */
	public void setIdFarm(int idFarm) {
		this.idFarm = idFarm;
	}

	/**
	 * @return idSearchBranchOffice: id of the branch in which the company wants
	 *         to consult partnered with permissions to the person.
	 */
	public int getIdSearchBranchOffice() {
		return idSearchBranchOffice;
	}

	/**
	 * @param idSearchBranchOffice
	 *            : id of the branch in which the company wants to consult
	 *            partnered with permissions to the person.
	 */
	public void setIdSearchBranchOffice(int idSearchBranchOffice) {
		this.idSearchBranchOffice = idSearchBranchOffice;
	}

	/**
	 * @return idFarmSearch: id of the farm for which you want to consult the
	 *         company that was associated with the individual permissions.
	 */
	public int getIdFarmSearch() {
		return idFarmSearch;
	}

	/**
	 * @param idFarmSearch
	 *            : id of the farm for which you want to consult the company
	 *            that was associated with the individual permissions.
	 */
	public void setIdFarmSearch(int idFarmSearch) {
		this.idFarmSearch = idFarmSearch;
	}

	/**
	 * @return selectedCompany: company is selected to associate permissions.
	 */
	public Business getSelectedCompany() {
		return selectedCompany;
	}

	/**
	 * @param selectedCompany
	 *            : company is selected to associate permissions.
	 */
	public void setSelectedCompany(Business selectedCompany) {
		this.selectedCompany = selectedCompany;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of permissions person company.
	 * 
	 * @return consultPermissionPersonCompany: method consulting firms with
	 *         access rights of the person and load management template.
	 */
	public String initializeSearch() {
		this.searchCompanyManage = "";
		this.idSearchBranchOffice = 0;
		this.idFarmSearch = 0;
		return consultPermissionPersonCompany();
	}

	/**
	 * Browse companies that have access to the person.
	 * 
	 * @return gesPermissionPersonBusiness: navigation rule load the template
	 *         for managing access permissions per person.
	 */
	public String consultPermissionPersonCompany() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		listPermissionPersonBusiness = new ArrayList<PermissionPersonBusiness>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		String panelId = "frmGestionarPermisosEmpresa:panelEmpresaPermiso";
		try {
			if (person != null) {
				advanceSearch(consult, parameters, bundle, unionMessagesSearch);
				pagination.paginar(permissionPersonBusinessDao
						.amountBusinessAccessPerson(consult, parameters));
				List<PermissionPersonBusiness> listPermissionPersonBusinessTemp = permissionPersonBusinessDao
						.consultBusinessAccessPerson(consult, parameters,
								pagination.getInicio(), pagination.getRango());
				if ((listPermissionPersonBusinessTemp == null || listPermissionPersonBusinessTemp
						.size() <= 0)
						&& !"".equals(unionMessagesSearch.toString())) {
					messageSearch = MessageFormat
							.format(bundle
									.getString("message_no_existen_registros_criterio_busqueda"),
									unionMessagesSearch);
				} else if (listPermissionPersonBusinessTemp == null
						|| listPermissionPersonBusinessTemp.size() <= 0) {
					ControladorContexto.mensajeInformacion(panelId,
							bundle.getString("message_no_existen_registros"));
				} else if (!"".equals(unionMessagesSearch.toString())) {
					messageSearch = MessageFormat
							.format(bundle
									.getString("message_existen_registros_criterio_busqueda"),
									bundleSecurity
											.getString("person_permission_company_label"),
									unionMessagesSearch);
				}
				if (!"".equals(messageSearch)) {
					ControladorContexto.mensajeInformacion(panelId,
							messageSearch);
				}
				detailsPermissionPersonCompany(listPermissionPersonBusinessTemp);
				loadCombos();
			} else {
				ControladorContexto
						.mensajeError(bundleSecurity
								.getString("person_permission_company_message_validate_select_user"));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesPermissionPersonBusiness";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
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
	private void advanceSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("messageOrganizations");
		boolean messageAdd = false;
		String comaEspacio = ", ";

		consult.append("WHERE ppe.person.id=:idPerson ");
		SelectItem itemPer = new SelectItem(person.getId(), "idPerson");
		parameters.add(itemPer);

		if (Constantes.NOT.equals(vigencia)) {
			consult.append("AND (ppe.dateEndValidity IS NOT NULL ");
			consult.append("AND ppe.dateEndValidity <= :actualDate) ");
		} else if (Constantes.SI.equals(vigencia)) {
			consult.append("AND (ppe.dateEndValidity IS NULL ");
			consult.append("OR ppe.dateEndValidity > :actualDate) ");
		}
		SelectItem itemVig = new SelectItem(new Date(), "actualDate");
		parameters.add(itemVig);

		if (idSearchBranchOffice != 0) {
			SelectItem item = new SelectItem(idSearchBranchOffice,
					"idSearchBranchOffice");
			consult.append("AND ppe.sucursal.id=:idSearchBranchOffice ");
			parameters.add(item);
			String branchOfficeName = (String) ValidacionesAction.getLabel(
					itemsBranchOffices, idSearchBranchOffice);
			unionMessagesSearch.append(bundleOrg.getString("branch_label")
					+ ": " + '"' + branchOfficeName + '"');
			messageAdd = true;
		}
		if (idFarmSearch != 0) {
			SelectItem item = new SelectItem(idFarmSearch, "idFarm");
			consult.append("AND ppe.farm.idFarm=:idFarm ");
			parameters.add(item);
			String farmName = (String) ValidacionesAction.getLabel(itemsFarms,
					idFarmSearch);
			unionMessagesSearch.append((messageAdd ? comaEspacio : "")
					+ bundleOrg.getString("farm_label") + ": " + '"' + farmName
					+ '"');
			messageAdd = true;
		}
		if (this.searchCompanyManage != null
				&& !"".equals(this.searchCompanyManage)) {
			SelectItem item = new SelectItem("%" + searchCompanyManage + "%",
					"keyword");
			consult.append("AND UPPER(ppe.business.name) LIKE UPPER(:keyword) ");
			parameters.add(item);
			unionMessagesSearch.append((messageAdd ? comaEspacio : "")
					+ bundle.getString("label_name") + ": " + '"'
					+ this.searchCompanyManage + '"');
		}
	}

	/**
	 * This method loads the details of the undertakings to which the person has
	 * access.
	 * 
	 * @param listPermissionPersonBusinessTemp
	 *            : Listed companies with access to the system of the person.
	 * @throws Exception
	 */
	private void detailsPermissionPersonCompany(
			List<PermissionPersonBusiness> listPermissionPersonBusinessTemp)
			throws Exception {
		if (listPermissionPersonBusinessTemp != null) {
			for (PermissionPersonBusiness permissionPersonBusiness : listPermissionPersonBusinessTemp) {
				permissionPersonBusiness = permissionPersonBusinessDao
						.consultDetailsPermissionPersonBusiness(permissionPersonBusiness);
				BusinessAction businessAction = ControladorContexto
						.getContextBean(BusinessAction.class);
				Business businessPermission = permissionPersonBusiness
						.getBusiness();
				businessAction.loadDetailsOneBusiness(businessPermission);
				permissionPersonBusiness.setBusiness(businessPermission);
				listPermissionPersonBusiness.add(permissionPersonBusiness);
			}
		}
	}

	/**
	 * A new object instance to associate the individual companies.
	 * 
	 * @return regPermissionPersonBusiness: navigation rule that loads the
	 *         template to permit registration of the individual companies.
	 */
	public String newPermissionPersonCompany() {
		try {
			business = new ArrayList<Business>();
			listPermissionPersonBusinessTemp = new ArrayList<PermissionPersonBusiness>();
			loadCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPermissionPersonBusiness";
	}

	/**
	 * This method looks for a list of companies search parameter system (ILS /
	 * name).
	 */
	public void searchCompanies() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			business = businessDao.searchBusinessForNameOrNit(searchCompany);
			if (business == null || business.size() <= 0) {
				ControladorContexto.mensajeInformacion(
						"frmAsociarPermisos:empresas",
						bundle.getString("message_no_existen_registros"));
			} else {
				BusinessAction businessAction = ControladorContexto
						.getContextBean(BusinessAction.class);
				businessAction.setListBusiness(new ArrayList<Business>());
				businessAction.setListBusiness(business);
				businessAction.loadDetailsBusiness();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Ends the validity of the access permission of the person to the company.
	 * 
	 * @param validity
	 *            : value that indicates whether the term begins or ends
	 * @return consultation existing records and returns to the management of
	 *         the company permits.
	 */
	public String changeValidityPermissionCompany(boolean validity) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageChangeValidity = "message_fin_vigencia_satisfactorio";
		try {
			if (permissionPersonBusiness != null) {
				nullValidate(permissionPersonBusiness);
				permissionPersonBusiness.setUserName(identity.getUserName());
				if (validity) {
					permissionPersonBusiness.setPredetermined(false);
					permissionPersonBusiness.setDateEndValidity(new Date());
				} else {
					messageChangeValidity = "message_inicio_vigencia_satisfactorio";
					permissionPersonBusiness.setDateEndValidity(null);
				}
				permissionPersonBusinessDao
						.editPermissionPersonCompany(permissionPersonBusiness);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(messageChangeValidity)
								+ ": {0}", this.permissionPersonBusiness
								.getBusiness().getName()));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultPermissionPersonCompany();
	}

	/**
	 * Allows to validate the records that the object instance is beginning but
	 * his id is 0 or null, if null if so assigned to PermissionPersonBusiness.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @param permissionPersonBusinessVal
	 *            : null object to validate.
	 */
	public void nullValidate(
			PermissionPersonBusiness permissionPersonBusinessVal) {
		if (permissionPersonBusinessVal != null) {
			Farm farm = permissionPersonBusinessVal.getFarm();
			if (farm != null && farm.getIdFarm() == 0) {
				permissionPersonBusinessVal.setFarm(null);
			}
		}
	}

	/**
	 * This method saves the information of the companies with the person and
	 * the assigned permissions.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @return consultPermissionPersonCompany: method consulting firms with
	 *         access rights of the person and load management template.
	 */
	public String savePermissionPersonCompany() {
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (listPermissionPersonBusinessTemp != null) {
				this.userTransaction.begin();
				String personName = person.getNames() + " "
						+ person.getSurnames();
				StringBuilder messageCompaniesFarms = new StringBuilder();
				String messageFarmCompany = "person_permission_company_message_associate_company_farm";
				for (PermissionPersonBusiness permissionPersonBusinessAdd : listPermissionPersonBusinessTemp) {
					boolean predetermined = permissionPersonBusinessAdd
							.isPredetermined();
					PermissionPersonBusiness predeterminedPermision = permissionPersonBusinessDao
							.consultExistPredetermined(person.getDocument());
					if (predetermined && predeterminedPermision != null) {
						predeterminedPermision.setPredetermined(false);
						permissionPersonBusinessDao
								.editPermissionPersonCompany(predeterminedPermision);
					}
					nullValidate(permissionPersonBusinessAdd);
					permissionPersonBusinessAdd.setDateCreation(new Date());
					permissionPersonBusinessAdd.setUserName(identity
							.getUserName());
					permissionPersonBusinessDao
							.savePermissionPersonCompany(permissionPersonBusinessAdd);
					if (messageCompaniesFarms.length() > 1) {
						messageCompaniesFarms.append(", ");
					}
					String nitCompany = permissionPersonBusinessAdd
							.getBusiness().getNit();
					String nameFarm = permissionPersonBusinessAdd.getFarm()
							.getName();
					String msg = MessageFormat.format(
							bundleSecurity.getString(messageFarmCompany),
							nitCompany, nameFarm);
					messageCompaniesFarms.append(msg);
				}
				this.userTransaction.commit();
				String format = MessageFormat
						.format(bundleSecurity
								.getString("person_permission_company_message_associate_company"),
								personName, messageCompaniesFarms);
				ControladorContexto.mensajeInformacion(null, format);
			}
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return consultPermissionPersonCompany();
	}

	/**
	 * Loads charges combo items to relate to a person in a company
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void loadCombos() throws Exception {
		itemsBranchOffices = new ArrayList<SelectItem>();
		itemsFarms = new ArrayList<SelectItem>();
		List<Farm> farmsCurrent = farmDao.farmsList();
		if (farmsCurrent != null) {
			for (Farm farm : farmsCurrent) {
				itemsFarms
						.add(new SelectItem(farm.getIdFarm(), farm.getName()));
			}
		}
	}

	/**
	 * Allows to control the addition and subtraction of companies in the list
	 * of companies with access rights of the individual.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @param option
	 *            : Option to know that is made in the list: add, delete, or
	 *            create a new one.
	 * @param objPermissionPersonBusiness
	 *            : Object of company permission person you want to remove from
	 *            the list.
	 */
	public void controllerListPermission(String option,
			PermissionPersonBusiness objPermissionPersonBusiness) {
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (Constantes.NEW_PERMISO.equals(option)) {
				this.permissionPersonBusiness = new PermissionPersonBusiness();
				this.permissionPersonBusiness.setFarm(new Farm());
				this.permissionPersonBusiness.setPerson(person);
				this.permissionPersonBusiness.setBusiness(selectedCompany);
				idBranchOffice = 0;
				idFarm = 0;
			} else if (Constantes.ADD_PERMISO.equals(option)) {
				String message = "person_permission_company_label_associated";
				if (idBranchOffice != 0) {
					message = "person_permission_company_label_associated_branch";
				} else if (idFarm != 0) {
					message = "person_permission_company_label_associated_farm";
					Farm farm = farmDao.farmXId(idFarm);
					permissionPersonBusiness.setFarm(farm);
				}
				if (!validateExistsPermissionAssociated(this.selectedCompany,
						permissionPersonBusiness.getFarm())) {
					listPermissionPersonBusinessTemp
							.add(permissionPersonBusiness);
				} else {
					ControladorContexto.mensajeError("popupForm:mensajesPopup",
							bundleSecurity.getString(message));
				}
			} else if (Constantes.BORRAR_PERMISO.equals(option)) {
				listPermissionPersonBusinessTemp
						.remove(objPermissionPersonBusiness);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Allows to validate if the company you want to associate already in the
	 * list of permits with the same branch or property and that this force,
	 * then the boolean value true is sent to not be added again.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @param businessSelect
	 *            : company selected to associate permissions.
	 * @param farm
	 *            : Selected finance company.
	 * @return boolean to true if it is already associated or false otherwise.
	 */
	private boolean validateExistsPermissionAssociated(Business businessSelect,
			Farm farm) {
		if (listPermissionPersonBusinessTemp != null) {
			for (PermissionPersonBusiness permPersonaEmp : listPermissionPersonBusinessTemp) {
				Business companyList = permPersonaEmp.getBusiness();
				Farm farmList = permPersonaEmp.getFarm();
				if (farm != null && farm.getIdFarm() != 0 && farmList != null
						&& farmList.equals(farm)
						&& companyList.equals(businessSelect)) {
					return true;
				}
				if ((farm == null || farm.getIdFarm() == 0)
						&& companyList.equals(businessSelect)) {
					return true;
				}
			}
		}
		if (listPermissionPersonBusiness != null) {
			for (PermissionPersonBusiness permisoPerEmp : listPermissionPersonBusiness) {
				Date endDateValidity = permisoPerEmp.getDateEndValidity();
				Business empresaList = permisoPerEmp.getBusiness();

				Farm farmList = permisoPerEmp.getFarm();

				if (farm != null
						&& farm.getIdFarm() != 0
						&& farmList != null
						&& farmList.equals(farm)
						&& empresaList.equals(businessSelect)
						&& (endDateValidity == null || endDateValidity
								.after(new Date()))) {
					return true;
				}
				if ((farm == null || farm.getIdFarm() == 0)
						&& empresaList.equals(businessSelect)
						&& (endDateValidity == null || endDateValidity
								.after(new Date()))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method for validating the selection of at least one company, to be
	 * associate access permissions to the person.
	 */
	public void validatePermissionCompaniesSelected() {
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		if (listPermissionPersonBusinessTemp == null
				|| listPermissionPersonBusinessTemp.size() <= 0) {
			ControladorContexto
					.mensajeError(
							"frmAsociarPermisos:extDTPermisoPersonaEmpresa",
							bundleSecurity
									.getString("person_permission_company_message_validate_select_company"));
		}
	}
}
