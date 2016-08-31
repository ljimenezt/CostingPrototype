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
import co.informatix.erp.organizaciones.action.EmpresaAction;
import co.informatix.erp.organizaciones.dao.EmpresaDao;
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.seguridad.dao.PermisoPersonaEmpresaDao;
import co.informatix.erp.seguridad.entities.PermisoPersonaEmpresa;
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
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PermisoPersonaEmpresaAction implements Serializable {

	@EJB
	private EmpresaDao empresaDao;
	@EJB
	private PermisoPersonaEmpresaDao permisoPersonaEmpresaDao;
	@EJB
	private FarmDao farmDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private Paginador pagination = new Paginador();
	private PermisoPersonaEmpresa permisoPersonaEmpresa;
	private Persona persona;
	private Empresa selectedCompany;

	private List<PermisoPersonaEmpresa> listPermisoPersonaEmpresa;
	private List<PermisoPersonaEmpresa> listPermisoPersonaEmpresaTemp;
	private List<Empresa> empresas;

	private List<SelectItem> itemsBranchOfficesCompany;
	private List<SelectItem> itemsFarmsCompany;
	private List<SelectItem> itemsBranchOffices;
	private List<SelectItem> itemsFarms;

	private String searchCompany;
	private String searchCompanyManage;
	private String validity = Constantes.SI;
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
	 * @return listPermisoPersonaEmpresa: list of companies to which the person
	 *         has access.
	 */
	public List<PermisoPersonaEmpresa> getListPermisoPersonaEmpresa() {
		return listPermisoPersonaEmpresa;
	}

	/**
	 * @param listPermisoPersonaEmpresa
	 *            : list of companies to which the person has access.
	 */
	public void setListPermisoPersonaEmpresa(
			List<PermisoPersonaEmpresa> listPermisoPersonaEmpresa) {
		this.listPermisoPersonaEmpresa = listPermisoPersonaEmpresa;
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
	 * @return empresas: Companies that are queried to be associated with the
	 *         person.
	 */
	public List<Empresa> getEmpresas() {
		return empresas;
	}

	/**
	 * @param empresas
	 *            : Companies that are queried to be associated with the person.
	 */
	public void setEmpresas(List<Empresa> empresas) {
		this.empresas = empresas;
	}

	/**
	 * @return permisoPersonaEmpresa: PermisoPersonaEmpresa object that is used
	 *         to associate companies, branches or farms to which the person has
	 *         access.
	 */
	public PermisoPersonaEmpresa getPermisoPersonaEmpresa() {
		return permisoPersonaEmpresa;
	}

	/**
	 * @param permisoPersonaEmpresa
	 *            : PermisoPersonaEmpresa object that is used to associate
	 *            companies, branches or farms to which the person has access.
	 */
	public void setPermisoPersonaEmpresa(
			PermisoPersonaEmpresa permisoPersonaEmpresa) {
		this.permisoPersonaEmpresa = permisoPersonaEmpresa;
	}

	/**
	 * @return listPermisoPersonaEmpresaTemp: Temporary list of the selected to
	 *         give businesses access to a person.
	 */
	public List<PermisoPersonaEmpresa> getListPermisoPersonaEmpresaTemp() {
		return listPermisoPersonaEmpresaTemp;
	}

	/**
	 * @param listPermisoPersonaEmpresaTemp
	 *            : Temporary list of the selected to give businesses access to
	 *            a person.
	 */
	public void setListPermisoPersonaEmpresaTemp(
			List<PermisoPersonaEmpresa> listPermisoPersonaEmpresaTemp) {
		this.listPermisoPersonaEmpresaTemp = listPermisoPersonaEmpresaTemp;
	}

	/**
	 * @return persona: gets person to whom it is going to assign access
	 *         permissions businesses.
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * 
	 * @param persona
	 *            : sets person to whom it is going to assign access permissions
	 *            businesses.
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
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
	 * @return validity: allows gets the selected value 'yes' of existing and
	 *         'no' for not applicable
	 */
	public String getValidity() {
		return validity;
	}

	/**
	 * @param validity
	 *            : allows gets the selected value 'yes' of existing and 'no'
	 *            for not applicable
	 */
	public void setValidity(String validity) {
		this.validity = validity;
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
	public Empresa getSelectedCompany() {
		return selectedCompany;
	}

	/**
	 * @param selectedCompany
	 *            : company is selected to associate permissions.
	 */
	public void setSelectedCompany(Empresa selectedCompany) {
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
	 * @return gesPermisoPersonaEmpresa: navigation rule load the template for
	 *         managing access permissions per person.
	 */
	public String consultPermissionPersonCompany() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		listPermisoPersonaEmpresa = new ArrayList<PermisoPersonaEmpresa>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		String panelId = "frmGestionarPermisosEmpresa:panelEmpresaPermiso";
		try {
			if (persona != null) {
				advanceSearch(consult, parameters, bundle, unionMessagesSearch);
				pagination.paginar(permisoPersonaEmpresaDao
						.cantidadEmpresasAccesoPersona(consult, parameters));
				List<PermisoPersonaEmpresa> listPermisoPersonaEmpresaTemp = permisoPersonaEmpresaDao
						.consultarEmpresasAccesoPersona(consult, parameters,
								pagination.getInicio(), pagination.getRango());
				if ((listPermisoPersonaEmpresaTemp == null || listPermisoPersonaEmpresaTemp
						.size() <= 0)
						&& !"".equals(unionMessagesSearch.toString())) {
					messageSearch = MessageFormat
							.format(bundle
									.getString("message_no_existen_registros_criterio_busqueda"),
									unionMessagesSearch);
				} else if (listPermisoPersonaEmpresaTemp == null
						|| listPermisoPersonaEmpresaTemp.size() <= 0) {
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
				detailsPermissionPersonCompany(listPermisoPersonaEmpresaTemp);
				loadCombos();
			} else {
				ControladorContexto
						.mensajeError(bundleSecurity
								.getString("person_permission_company_message_validate_select_user"));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesPermisoPersonaEmpresa";
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

		consult.append("WHERE ppe.persona.id=:idPerson ");
		SelectItem itemPer = new SelectItem(persona.getId(), "idPerson");
		parameters.add(itemPer);

		if (Constantes.NOT.equals(validity)) {
			consult.append("AND (ppe.fechaFinVigencia IS NOT NULL ");
			consult.append("AND ppe.fechaFinVigencia <= :actualDate) ");
		} else if (Constantes.SI.equals(validity)) {
			consult.append("AND (ppe.fechaFinVigencia IS NULL ");
			consult.append("OR ppe.fechaFinVigencia > :actualDate) ");
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
			consult.append("AND UPPER(ppe.empresa.nombre) LIKE UPPER(:keyword) ");
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
	 * @param listPermisoPersonaEmpresaTemp
	 *            : Listed companies with access to the system of the person.
	 * @throws Exception
	 */
	private void detailsPermissionPersonCompany(
			List<PermisoPersonaEmpresa> listPermisoPersonaEmpresaTemp)
			throws Exception {
		if (listPermisoPersonaEmpresaTemp != null) {
			for (PermisoPersonaEmpresa permisoPersonaEmpresa : listPermisoPersonaEmpresaTemp) {
				permisoPersonaEmpresa = permisoPersonaEmpresaDao
						.consultarDetallesPermisoPersonaEmpresa(permisoPersonaEmpresa);
				EmpresaAction empresaAction = ControladorContexto
						.getContextBean(EmpresaAction.class);
				Empresa empresaPermiso = permisoPersonaEmpresa.getEmpresa();
				empresaAction.cargarDetallesUnaEmpresa(empresaPermiso);
				permisoPersonaEmpresa.setEmpresa(empresaPermiso);
				listPermisoPersonaEmpresa.add(permisoPersonaEmpresa);
			}
		}
	}

	/**
	 * A new object instance to associate the individual companies.
	 * 
	 * @return navigation rule that loads the template to permit registration of
	 *         the individual companies.
	 */
	public String newPermissionPersonCompany() {
		try {
			empresas = new ArrayList<Empresa>();
			listPermisoPersonaEmpresaTemp = new ArrayList<PermisoPersonaEmpresa>();
			loadCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPermisoPersonaEmpresa";
	}

	/**
	 * This method looks for a list of companies search parameter system (ILS /
	 * name).
	 */
	public void searchCompanies() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			empresas = empresaDao.buscarEmpresaXNombreONit(searchCompany);
			if (empresas == null || empresas.size() <= 0) {
				ControladorContexto.mensajeInformacion(
						"frmAsociarPermisos:empresas",
						bundle.getString("message_no_existen_registros"));
			} else {
				EmpresaAction empresaAction = ControladorContexto
						.getContextBean(EmpresaAction.class);
				empresaAction.setListaEmpresas(new ArrayList<Empresa>());
				empresaAction.setListaEmpresas(empresas);
				empresaAction.cargarDetallesEmpresas();
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
			if (permisoPersonaEmpresa != null) {
				nullValidate(permisoPersonaEmpresa);
				permisoPersonaEmpresa.setUserName(identity.getUserName());
				if (validity) {
					permisoPersonaEmpresa.setPredeterminado(false);
					permisoPersonaEmpresa.setFechaFinVigencia(new Date());
				} else {
					messageChangeValidity = "message_inicio_vigencia_satisfactorio";
					permisoPersonaEmpresa.setFechaFinVigencia(null);
				}
				permisoPersonaEmpresaDao
						.modificarPermisoPersonaEmpresa(permisoPersonaEmpresa);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(messageChangeValidity)
								+ ": {0}", this.permisoPersonaEmpresa
								.getEmpresa().getNombre()));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultPermissionPersonCompany();
	}

	/**
	 * Allows to validate the records that the object instance is beginning but
	 * his id is 0 or null, if null if so assigned to permisoPersonaEmpresa.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @param permisoPersonaEmpresaVal
	 *            : null object to validate.
	 */
	public void nullValidate(PermisoPersonaEmpresa permisoPersonaEmpresaVal) {
		if (permisoPersonaEmpresaVal != null) {
			Farm farm = permisoPersonaEmpresaVal.getFarm();
			if (farm != null && farm.getIdFarm() == 0) {
				permisoPersonaEmpresaVal.setFarm(null);
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
			if (listPermisoPersonaEmpresaTemp != null) {
				this.userTransaction.begin();
				String personName = persona.getNombres() + " "
						+ persona.getApellidos();
				StringBuilder messageCompaniesFarms = new StringBuilder();
				String messageFarmCompany = "person_permission_company_message_associate_company_farm";
				for (PermisoPersonaEmpresa permisoPersonaEmpresaAdd : listPermisoPersonaEmpresaTemp) {
					boolean predetermined = permisoPersonaEmpresaAdd
							.isPredeterminado();
					PermisoPersonaEmpresa predeterminedPermision = permisoPersonaEmpresaDao
							.consultarExistePredeterminado(persona
									.getDocumento());
					if (predetermined && predeterminedPermision != null) {
						predeterminedPermision.setPredeterminado(false);
						permisoPersonaEmpresaDao
								.modificarPermisoPersonaEmpresa(predeterminedPermision);
					}
					nullValidate(permisoPersonaEmpresaAdd);
					permisoPersonaEmpresaAdd.setFechaCreacion(new Date());
					permisoPersonaEmpresaAdd
							.setUserName(identity.getUserName());
					permisoPersonaEmpresaDao
							.guardarPermisoPersonaEmpresa(permisoPersonaEmpresaAdd);
					if (messageCompaniesFarms.length() > 1) {
						messageCompaniesFarms.append(", ");
					}
					String nitCompany = permisoPersonaEmpresaAdd.getEmpresa()
							.getNit();
					String nameFarm = permisoPersonaEmpresaAdd.getFarm()
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
	 * @param objPermisoPersonaEmpresa
	 *            : Object of company permission person you want to remove from
	 *            the list.
	 */
	public void controllerListPermission(String option,
			PermisoPersonaEmpresa objPermisoPersonaEmpresa) {
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (Constantes.NEW_PERMISO.equals(option)) {
				this.permisoPersonaEmpresa = new PermisoPersonaEmpresa();
				this.permisoPersonaEmpresa.setFarm(new Farm());
				this.permisoPersonaEmpresa.setPersona(persona);
				this.permisoPersonaEmpresa.setEmpresa(selectedCompany);
				idBranchOffice = 0;
				idFarm = 0;
			} else if (Constantes.ADD_PERMISO.equals(option)) {
				String message = "person_permission_company_label_associated";
				if (idBranchOffice != 0) {
					message = "person_permission_company_label_associated_branch";
				} else if (idFarm != 0) {
					message = "person_permission_company_label_associated_farm";
					Farm farm = farmDao.farmXId(idFarm);
					permisoPersonaEmpresa.setFarm(farm);
				}
				if (!validateExistsPermissionAssociated(this.selectedCompany,
						permisoPersonaEmpresa.getFarm())) {
					listPermisoPersonaEmpresaTemp.add(permisoPersonaEmpresa);
				} else {
					ControladorContexto.mensajeError("popupForm:mensajesPopup",
							bundleSecurity.getString(message));
				}
			} else if (Constantes.BORRAR_PERMISO.equals(option)) {
				listPermisoPersonaEmpresaTemp.remove(objPermisoPersonaEmpresa);
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
	 * @param empresaSel
	 *            : company selected to associate permissions.
	 * @param farm
	 *            : Selected finance company.
	 * @return boolean to true if it is already associated or false otherwise.
	 */
	private boolean validateExistsPermissionAssociated(Empresa empresaSel,
			Farm farm) {
		if (listPermisoPersonaEmpresaTemp != null) {
			for (PermisoPersonaEmpresa permPersonaEmp : listPermisoPersonaEmpresaTemp) {
				Empresa companyList = permPersonaEmp.getEmpresa();
				Farm farmList = permPersonaEmp.getFarm();
				if (farm != null && farm.getIdFarm() != 0 && farmList != null
						&& farmList.equals(farm)
						&& companyList.equals(empresaSel)) {
					return true;
				}
				if ((farm == null || farm.getIdFarm() == 0)
						&& companyList.equals(empresaSel)) {
					return true;
				}
			}
		}
		if (listPermisoPersonaEmpresa != null) {
			for (PermisoPersonaEmpresa permisoPerEmp : listPermisoPersonaEmpresa) {
				Date endDateValidity = permisoPerEmp.getFechaFinVigencia();
				Empresa empresaList = permisoPerEmp.getEmpresa();

				Farm farmList = permisoPerEmp.getFarm();

				if (farm != null
						&& farm.getIdFarm() != 0
						&& farmList != null
						&& farmList.equals(farm)
						&& empresaList.equals(empresaSel)
						&& (endDateValidity == null || endDateValidity
								.after(new Date()))) {
					return true;
				}
				if ((farm == null || farm.getIdFarm() == 0)
						&& empresaList.equals(empresaSel)
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
		if (listPermisoPersonaEmpresaTemp == null
				|| listPermisoPersonaEmpresaTemp.size() <= 0) {
			ControladorContexto
					.mensajeError(
							"frmAsociarPermisos:extDTPermisoPersonaEmpresa",
							bundleSecurity
									.getString("person_permission_company_message_validate_select_company"));
		}
	}
}
