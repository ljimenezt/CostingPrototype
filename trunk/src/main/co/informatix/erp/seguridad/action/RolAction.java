package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.seguridad.dao.ManageMenuDao;
import co.informatix.erp.seguridad.dao.RoleDao;
import co.informatix.erp.seguridad.dao.RoleMenuDao;
import co.informatix.erp.seguridad.dao.RoleMethodDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.entities.Menu;
import co.informatix.security.entities.Metodo;
import co.informatix.security.entities.Rol;
import co.informatix.security.entities.RolMenu;
import co.informatix.security.entities.RolMenuPK;
import co.informatix.security.entities.RolMetodo;
import co.informatix.security.entities.RolMetodoPK;

/**
 * It implements business logic in the Role entity to register and manage.
 * 
 * @author marisol.calderon
 * @modify 19/06/2014 Gabriel.Moreno
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class RolAction implements Serializable {

	private List<Rol> rolesList;
	private List<Menu> listaMenus;
	private List<RolMenu> rolMenus;
	private List<Integer> menuViewNoSelected;

	private HashMap<Integer, HashMap<String, Boolean>> methodsPermissions;

	private Paginador pagination = new Paginador();
	private Paginador paginationMethod = new Paginador();
	private Paginador paginationMenu = new Paginador();

	private Rol rol;

	private String vigencia = Constantes.SI;
	private String nameSearch = "";

	private boolean editable = true;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;
	@EJB
	private RoleDao rolDao;
	@EJB
	private RoleMenuDao rolMenuDao;
	@EJB
	private ManageMenuDao menuDao;
	@EJB
	private RoleMethodDao rolMetodoDao;

	/**
	 * @return rolesList: List of roles that are loaded into the user interface.
	 */
	public List<Rol> getRolesList() {
		return rolesList;
	}

	/**
	 * @param rolesList
	 *            : List of roles that are loaded into the user interface.
	 */
	public void setRolesList(List<Rol> rolesList) {
		this.rolesList = rolesList;
	}

	/**
	 * @return methodsPermissions: HashMap that stores the identifiers of the
	 *         methods that you want to assign permissions.
	 */
	public HashMap<Integer, HashMap<String, Boolean>> getMethodsPermissions() {
		return methodsPermissions;
	}

	/**
	 * @return rol: variable that represents a role object in which is made the
	 *         management.
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * @param rol
	 *            : variable representing a role object in which is made the
	 *            management.
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * @return vigencia : option for modifying the validity of a role
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : option for modifying the validity of a role
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return pagination : to manage the paging of the roles table
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : to manage the paging of the roles table
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationMethod: page manages the allocation of permits.
	 */
	public Paginador getPaginationMethod() {
		return paginationMethod;
	}

	/**
	 * @param paginationMethod
	 *            : page manages the allocation of permits.
	 */
	public void setPaginationMethod(Paginador paginationMethod) {
		this.paginationMethod = paginationMethod;
	}

	/**
	 * @return paginationMenu: Manages the pagination of the menus related to
	 *         the allocation of permissions.
	 */
	public Paginador getPaginationMenu() {
		return paginationMenu;
	}

	/**
	 * @param paginationMenu
	 *            : Manages the pagination of the menus related to the
	 *            allocation of permits.
	 */
	public void setPaginationMenu(Paginador paginationMenu) {
		this.paginationMenu = paginationMenu;
	}

	/**
	 * @return perma: "A" represents all permissions.
	 */
	public static String getPermA() {
		return Constantes.PERM_A;
	}

	/**
	 * @return perms: "S" represents the permissions to query.
	 */
	public static String getPermS() {
		return Constantes.PERM_S;
	}

	/**
	 * @return permu: "U" represents the permissions to update.
	 */
	public static String getPermU() {
		return Constantes.PERM_U;
	}

	/**
	 * @return permd: "D" represents the permission to delete.
	 */
	public static String getPermD() {
		return Constantes.PERM_D;
	}

	/**
	 * @return permi: "I" represents the permissions to insert.
	 */
	public static String getPermI() {
		return Constantes.PERM_I;
	}

	/**
	 * @return perml: "L" represents the permissions to generate reports.
	 */
	public static String getPermL() {
		return Constantes.PERM_L;
	}

	/**
	 * Get a boolean that shows whether the role can or can not edit the name
	 * and it expires after.
	 * 
	 * @return editable: true if the variable can be edited or false otherwise.
	 */
	public boolean isEditable() {
		return editable;
	}

	/**
	 * Set a boolean that shows whether the role can or can not edit the name
	 * and it expires after.
	 * 
	 * @param editable
	 *            : true if the variable can be edited or false otherwise.
	 */
	public void setEditable(boolean editable) {
		this.editable = editable;
	}

	/**
	 * @return nameSearch: Name to find the type of roles
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Name to find the type of role
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method that prepares the view to add a new role or edit an existing one.
	 * 
	 * modify 27/09/2012 marisol.calderon
	 * 
	 * @param rol
	 *            : role to be edited.
	 * @return "regRol": redirects to the template to register a role.
	 */
	public String addEditRol(Rol rol) {
		methodsPermissions = new HashMap<Integer, HashMap<String, Boolean>>();
		menuViewNoSelected = new ArrayList<Integer>();
		try {
			if (rol != null) {
				this.rol = rol;
				validateRolEdition(this.rol);
				loadMethodsPermissions();
			} else {
				this.rol = new Rol();
			}
			initializeMethodsPermissions();
			initializeRelatedMethods();
			loadNotSelectedViewMenu();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regRol";
	}

	/**
	 * This method allows to identify invisible menus.
	 * 
	 * @throws Exception
	 */
	private void loadNotSelectedViewMenu() throws Exception {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		List<Menu> allMenusList = menuAction.getListAllMenus();
		if (allMenusList != null && allMenusList.size() > 0 && this.rol != null
				&& this.rol.getId() > 0) {
			List<Menu> listMenusRol = menuDao.consultMenusRole(this.rol);
			menu: for (Menu menu : allMenusList) {
				for (Menu menuRol : listMenusRol) {
					if (menu.getId() == menuRol.getId()) {
						continue menu;
					}
				}
				this.menuViewNoSelected.add(menu.getId());
			}
		}
	}

	/**
	 * This method identifies which methods permissions have roles.
	 * 
	 * @throws Exception
	 */
	private void loadMethodsPermissions() throws Exception {
		List<RolMetodo> methodRoles = this.rolMetodoDao
				.queryRolMethods(this.rol);
		for (RolMetodo methodRole : methodRoles) {
			RolMetodoPK methodRolPk = methodRole.getRolMetodoPK();
			int idMetodo = methodRolPk.getMetodo().getId();
			HashMap<String, Boolean> permissions = this.methodsPermissions
					.get(idMetodo);
			if (permissions == null) {
				permissions = new HashMap<String, Boolean>();
			}
			permissions.put(methodRolPk.getPermiso(), true);
			this.methodsPermissions.put(idMetodo, permissions);
		}
	}

	/**
	 * Method to validate if the role id is in the range of roles that you can
	 * not rename or terminate their validity.
	 * 
	 * @param rol
	 *            : role to be validated
	 */
	public void validateRolEdition(Rol rol) {
		if (rol.getId() <= Constantes.MAXIMO_ROLES_SIN_MODIFICAR) {
			editable = false;
		} else {
			editable = true;
		}
	}

	/**
	 * It initializes search parameters and load the initial list of roles.
	 * 
	 * @author Liseth Jimenez
	 * 
	 * @return searchRoles(): Consult a list of roles and redirects to the rol
	 *         management.
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		return this.searchRoles();
	}

	/**
	 * This method queries the list of roles according to their validity.
	 * 
	 * @modify 27/09/2012 marisol.calderon
	 * 
	 * @return gesRol: navigation rule that redirects to the product management
	 *         roles template.
	 */
	public String searchRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		String searchMessage = "";
		rolesList = new ArrayList<Rol>();
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			this.rol = new Rol();
			String validityCondition = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				validityCondition = Constantes.IS_NOT_NULL;
			}
			Long rolesAmount = rolDao.rolesAmount(validityCondition,
					this.nameSearch);
			if (rolesAmount != null) {
				pagination.paginar(rolesAmount);
			}
			rolesList = rolDao.queryRoles(pagination.getInicio(),
					pagination.getRango(), validityCondition, this.nameSearch);
			if ((rolesList == null || rolesList.size() <= 0)
					&& (this.nameSearch != null && !"".equals(this.nameSearch))) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nameSearch + '"');
			} else if (rolesList == null || rolesList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSecurity.getString("role_label_s"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nameSearch + '"');
			}
			validations.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesRol";
	}

	/**
	 * This method adds or edits a role.
	 * 
	 * @return searchRoles: method to query roles.
	 */
	public String addRol() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String shownMessage = "message_registro_modificar";
		try {
			userTransaction.begin();
			rol.setNombre(rol.getNombre());
			rol.setUserName(identity.getUserName());
			if (rol.getId() != 0) {
				rolDao.editRole(rol);
			} else {
				shownMessage = "message_registro_guardar";
				rol.setFechaCreacion(new Date());
				rol.setFechaInicioVigencia(new Date());
				rolDao.saveRole(rol);
			}
			addRolMethodMenu();
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(shownMessage),
							rol.getNombre()));
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return searchRoles();
	}

	/**
	 * This method allows you to add, edit, and register a rolMenu and rolMethod
	 * in the database.
	 * 
	 * @throws Exception
	 */
	private void addRolMethodMenu() throws Exception {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		List<RolMetodo> rolMethods = this.rolMetodoDao
				.queryAllRolMethods(this.rol);
		for (Integer methodId : this.methodsPermissions.keySet()) {
			HashMap<String, Boolean> permissions = this.methodsPermissions
					.get(methodId);
			permiso: for (String perm : permissions.keySet()) {
				if (permissions.get(perm)) {
					for (RolMetodo rolMethod : rolMethods) {
						RolMetodoPK rolMetodoPK = rolMethod.getRolMetodoPK();
						Metodo method = rolMetodoPK.getMetodo();
						if (method.getId() == methodId
								&& perm.equals(rolMetodoPK.getPermiso())) {
							rolMethod.setFechaFinVigencia(null);
							rolMethod.setUserName(identity.getUserName());
							rolMetodoDao.editRoleMethod(rolMethod);
							rolMethods.remove(rolMethod);
							continue permiso;
						}
					}

					RolMetodo rolMethod = loadRolMethod(perm, methodId);
					rolMethod.setFechaCreacion(new Date());
					rolMetodoDao.saveRoleMethod(rolMethod);
				}
			}
		}
		for (RolMetodo rolMethod : rolMethods) {
			rolMethod.setFechaFinVigencia(new Date());
			rolMethod.setUserName(identity.getUserName());
			rolMetodoDao.editRoleMethod(rolMethod);
		}

		this.listaMenus = new ArrayList<Menu>();
		this.rolMenus = rolMenuDao.queryAllRolMenu(this.rol);
		List<Menu> allMenusList = menuAction.getListAllMenus();
		if (allMenusList != null) {
			for (Menu menu : allMenusList) {
				if (!this.menuViewNoSelected.contains(menu.getId())) {
					searchSaveFatherMenu(menu);
				}
			}
		}
		for (RolMenu rolMenu : this.rolMenus) {
			rolMenu.setFechaFinVigencia(new Date());
			rolMenu.setUserName(identity.getUserName());
			rolMenuDao.editRoleMenu(rolMenu);
		}
	}

	/**
	 * This method inserts or edits the menu in the rolMetodo father menu and
	 * inserts it as well.
	 * 
	 * @param menu
	 *            : Menu to be added or edited and there will be found its
	 *            father.
	 * @throws Exception
	 */
	private void searchSaveFatherMenu(Menu menu) throws Exception {
		this.listaMenus.add(menu);
		boolean createMenu = true;
		for (RolMenu rolMenu : this.rolMenus) {
			RolMenuPK rolMenuPK = rolMenu.getRolMenuPK();
			Menu menuRol = rolMenuPK.getMenu();
			if (menuRol.getId() == menu.getId()) {
				rolMenu.setFechaFinVigencia(null);
				rolMenu.setUserName(identity.getUserName());
				rolMenuDao.editRoleMenu(rolMenu);
				this.rolMenus.remove(rolMenu);
				createMenu = false;
				break;
			}
		}
		if (createMenu) {
			RolMenu rolMenu = loadRolMenu(menu);
			rolMenu.setFechaCreacion(new Date());
			rolMenuDao.saveRoleMenu(rolMenu);
		}
		Menu menuPadre = menuDao.consultMenuFather(menu.getId());
		if (menuPadre != null) {
			for (Menu listaMenu : this.listaMenus) {
				if (listaMenu.getId() == menuPadre.getId()) {
					return;
				}
			}
			searchSaveFatherMenu(menuPadre);
		}
	}

	/**
	 * Handles the change effective in the role records.
	 * 
	 * @param validity
	 *            : boolean option.
	 * @return searchRoles: navigation rule consulting roles method.
	 */
	public String rolesValidity(boolean validity) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		StringBuilder regValidUsers = new StringBuilder();
		StringBuilder regSuccess = new StringBuilder();
		StringBuilder regNotEditRoles = new StringBuilder();
		String validityChangeMess = "message_inicio_vigencia_satisfactorio";
		try {
			if (validity) {
				validityChangeMess = "message_fin_vigencia_satisfactorio";
				boolean relaciones = searchRelations(rol.getId());
				if (!relaciones) {
					validateRolEdition(rol);
					if (editable) {
						rol.setFechaFinVigencia(new Date());
						rol.setUserName(identity.getUserName());
						rolDao.editRole(rol);
						regSuccess.append(rol.getNombre() + ", ");
					} else {
						regNotEditRoles.append(rol.getNombre() + ", ");
					}
				} else {
					regValidUsers.append(rol.getNombre() + ", ");
				}
			} else {
				rol.setFechaFinVigencia(null);
				rol.setUserName(identity.getUserName());
				rolDao.editRole(rol);
				regSuccess.append(rol.getNombre() + ", ");
			}
			if (regNotEditRoles.length() > 0) {
				ControladorContexto.mensajeError(bundleSecurity
						.getString("rol_message_no_editable_validity")
						+ ": "
						+ regNotEditRoles.substring(0,
								regNotEditRoles.length() - 2));
			}
			if (regValidUsers.length() > 0) {
				ControladorContexto
						.mensajeError(bundle
								.getString("message_registro_vigencia_con_relaciones")
								+ ": "
								+ regValidUsers.substring(0,
										regValidUsers.length() - 2));
			}
			if (regSuccess.length() > 0) {
				ControladorContexto.mensajeInformacion(
						null,
						bundle.getString(validityChangeMess)
								+ ": "
								+ regSuccess.substring(0,
										regSuccess.length() - 2));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchRoles();
	}

	/**
	 * Examines whether a role has relations with a user menu.
	 * 
	 * @param id
	 *            : role identifier.
	 * @return boolean: it returns true if the relation was found in the role,
	 *         false otherwise.
	 * @throws Exception
	 */
	private boolean searchRelations(short id) throws Exception {
		boolean result = false;
		result = rolDao.relatedRole(id, Constantes.ROLES_USUARIO);
		if (!result) {
			result = rolDao.relatedRole(id, Constantes.ROLES_MENU);
		}
		if (!result) {
			result = rolDao.relatedRole(id, Constantes.ROLES_METODO);
		}
		return result;
	}

	/**
	 * Validates if the role name exists in the database..
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 * @throws Exception
	 */
	public void validateName(FacesContext context, UIComponent toValidate,
			Object value) throws Exception {
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			Rol rolAux = new Rol();

			if (rol.getId() != 0) {
				rolAux = rolDao.updateNameExists(name, rol.getId());
			} else {
				rolAux = rolDao.nameExists(name);
			}
			String result = validateValidity("", rolAux);

			if (Constantes.VIGENTE.equals(result)) {
				ControladorContexto.mensajeErrorEspecifico(clientId,
						"message_ya_existe_verifique", "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (Constantes.SIN_VIGENTE.equals(result)) {
				ControladorContexto.mensajeErrorEspecifico(clientId,
						"message_ya_existe_sin_vigencia", "mensaje");
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
	 * Allows to validate if the role is in force.
	 * 
	 * @param result
	 *            : Chain to check for.
	 * @param rolAux
	 *            : It is a role object brought from the database.
	 * @return result: It returns 'sinVigencia' if registration is not in force
	 *         or returns 'force' when it's in force.
	 */
	private String validateValidity(String result, Rol rolAux) {
		if (rolAux != null) {
			if (rolAux.getFechaFinVigencia() != null) {
				result = Constantes.SIN_VIGENTE;
			} else {
				result = Constantes.VIGENTE;
			}
		}
		return result;
	}

	/**
	 * Initializes the list of methods to select their permissions.
	 */
	public void initializeMethodsPermissions() {
		MetodoAction methodAction = ControladorContexto
				.getContextBean(MetodoAction.class);
		methodAction.initializeSearch();
		adjustMethodPermissions(methodAction);
	}

	/**
	 * It loads the list of methods to select their permissions.
	 */
	public void loadMethodPermissions() {
		MetodoAction methodAction = ControladorContexto
				.getContextBean(MetodoAction.class);
		methodAction.searchMethods();
		adjustMethodPermissions(methodAction);
	}

	/**
	 * Adjust the list of methods of recording role.
	 * 
	 * @param metodoAction
	 *            : Action method to reuse the functions for the list of
	 *            methods.
	 */
	private void adjustMethodPermissions(MetodoAction metodoAction) {
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.paginationMethod = metodoAction.getPagination();
		String mensajeBusquedaPopUp = validations.getMensajeBusqueda();
		validations.setMensajeBusquedaPopUp(mensajeBusquedaPopUp);
	}

	/**
	 * It loads the object with the method RolMetod and assigned permissions.
	 * 
	 * @param permission
	 *            : Permission associated with the method.
	 * @param methodId
	 *            : Method identifier associated with permission.
	 * @return: Permission object and the loaded method.
	 */
	public RolMetodo loadRolMethod(String permission, Integer methodId) {
		RolMetodo rolMethod = new RolMetodo();
		RolMetodoPK methodRolPk = new RolMetodoPK();
		methodRolPk.setRol(this.rol);
		Metodo method = new Metodo();
		method.setId(methodId);
		methodRolPk.setMetodo(method);
		methodRolPk.setPermiso(permission);
		rolMethod.setRolMetodoPK(methodRolPk);
		rolMethod.setUserName(identity.getUserName());
		return rolMethod;
	}

	/**
	 * It loads the rolMenu object, the menu assigned to the role.
	 * 
	 * @param menu
	 *            : Menu that is associated with the role.
	 * @return: RolMenu object associated with the menu and the role.
	 */
	public RolMenu loadRolMenu(Menu menu) {
		RolMenu rolMenu = new RolMenu();
		RolMenuPK rolMenuPK = new RolMenuPK();
		rolMenuPK.setRol(this.rol);
		rolMenuPK.setMenu(menu);
		rolMenu.setRolMenuPK(rolMenuPK);
		rolMenu.setUserName(identity.getUserName());
		return rolMenu;
	}

	/**
	 * It adds a menu to the list of selected identifiers that associate a
	 * permission.
	 * 
	 * @param id
	 *            : ID of the selected method.
	 * @param permission
	 *            : Permission related to the method.
	 * @param selected
	 *            : It indicates whether it is selected or not.
	 */
	public void addMenuCheck(Integer id, String permission, boolean selected) {
		try {
			HashMap<String, Boolean> permissions = this.methodsPermissions
					.get(id);
			if (selected) {
				if (permissions == null) {
					permissions = new HashMap<String, Boolean>();
				}
				if (Constantes.PERM_A.equals(permission)) {
					permissions.put(Constantes.PERM_A, selected);
					permissions.put(Constantes.PERM_S, selected);
					permissions.put(Constantes.PERM_U, selected);
					permissions.put(Constantes.PERM_D, selected);
					permissions.put(Constantes.PERM_I, selected);
					permissions.put(Constantes.PERM_L, selected);
				} else {
					permissions.put(permission, selected);
					if (permSel(permissions, Constantes.PERM_S)
							&& permSel(permissions, Constantes.PERM_U)
							&& permSel(permissions, Constantes.PERM_D)
							&& permSel(permissions, Constantes.PERM_I)
							&& permSel(permissions, Constantes.PERM_L)) {
						permissions.put(Constantes.PERM_A, selected);
					}
				}
				this.methodsPermissions.put(id, permissions);
			} else {
				if (Constantes.PERM_A.equals(permission)) {
					this.methodsPermissions.remove(id);
				} else {
					permissions.remove(permission);
					if (permSel(permissions, Constantes.PERM_S)
							|| permSel(permissions, Constantes.PERM_U)
							|| permSel(permissions, Constantes.PERM_D)
							|| permSel(permissions, Constantes.PERM_I)
							|| permSel(permissions, Constantes.PERM_L)) {
						permissions.remove(Constantes.PERM_A);
						this.methodsPermissions.put(id, permissions);
					} else {
						this.methodsPermissions.remove(id);
					}
				}
			}
			loadRelatedMenus();
			removeMenuViewNoSelected();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Disclosed if permission is selected or not.
	 * 
	 * @param permissions
	 *            : List to seek a permission.
	 * @param permission
	 *            : Permission to look for.
	 * @return boolean: True if the permission is selected, false otherwise.
	 */
	private boolean permSel(HashMap<String, Boolean> permissions,
			String permission) {
		return permissions.get(permission) != null
				&& permissions.get(permission);
	}

	/**
	 * It discloses if permission is selected for the method that was sent.
	 * 
	 * @param id
	 *            : ID of the selected method.
	 * @param permission
	 *            : Permission to query select.
	 * @return True if the permission in the method is selected, otherwise
	 *         false.
	 */
	public boolean selectedPermission(Integer id, String permission) {
		HashMap<String, Boolean> permissions = this.methodsPermissions.get(id);
		if (permissions != null) {
			return permissions.get(permission) != null
					&& permissions.get(permission);
		}
		return false;
	}

	/**
	 * This method initializes the list of menus related to the role.
	 */
	public void initializeRelatedMethods() {
		try {
			GestionarMenuAction menuAction = ControladorContexto
					.getContextBean(GestionarMenuAction.class);
			menuAction.initialData();
			menuAction.setFromRol(true);
			menuAction.setFromMethod(false);
			loadRelatedMenus();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method loads the list of menus related to the role.
	 */
	public void loadRelatedMenus() {
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		menuAction.consultMenus();
		this.paginationMenu = menuAction.getPagination();
		String searchPopupMessage = validations.getMensajeBusqueda();
		validations.setMensajeBusquedaPopUp(searchPopupMessage);
	}

	/**
	 * This method verifies menus which are not selected in the view.
	 */
	private void removeMenuViewNoSelected() {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		List<Integer> menuViewNoSelectedTemp = new ArrayList<Integer>();
		menuViewNoSelectedTemp.addAll(this.menuViewNoSelected);
		this.menuViewNoSelected = new ArrayList<Integer>();
		for (Menu menu : menuAction.getListAllMenus()) {
			for (Integer integer : menuViewNoSelectedTemp) {
				if (menu.getId() == integer) {
					this.menuViewNoSelected.add(menu.getId());
				}
			}
		}
	}

	/**
	 * Adds or removes a menu from the list of selected identifiers to know
	 * whether or not it is displayed.
	 * 
	 * @param id
	 *            : ID of the selected menu.
	 * @param selected
	 *            : Indicates whether it is selected or not.
	 */
	public void hideMenuCheck(Integer id, boolean selected) {
		if (selected) {
			this.menuViewNoSelected.add(id);
		} else {
			this.menuViewNoSelected.remove(id);
		}
	}

	/**
	 * It discloses if the menu has permission to view or not.
	 * 
	 * @param id
	 *            : Menu identifier to see if it has permission to be displayed
	 *            or not.
	 * @return boolean: True if it has permissions for viewing, otherwise false.
	 */
	public boolean notSelectedMenu(Integer id) {
		return this.menuViewNoSelected.contains(id);
	}
}