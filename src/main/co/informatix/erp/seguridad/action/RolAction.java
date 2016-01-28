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
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.seguridad.dao.GestionarMenuDao;
import co.informatix.erp.seguridad.dao.RolDao;
import co.informatix.erp.seguridad.dao.RolMenuDao;
import co.informatix.erp.seguridad.dao.RolMetodoDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Mensaje;
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
 * Allows implementation of business logic in the Role entity to record and
 * manage.
 * 
 * @author marisol.calderon
 * @modify 19/06/2014 Gabriel.Moreno
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class RolAction implements Serializable {

	private List<Rol> listaRoles;
	private List<Menu> listaMenus;
	private List<RolMenu> rolMenus;
	private List<Integer> menuViewNoSelected;

	private HashMap<Integer, HashMap<String, Boolean>> selChecksPermisos;

	private Paginador paginador = new Paginador();
	private Paginador paginadorMetodo = new Paginador();
	private Paginador paginadorMenu = new Paginador();

	private Rol rol;

	private String vigencia = Constantes.SI;
	private String nombreBuscar = "";

	private boolean esEditable = true;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;
	@EJB
	private RolDao rolDao;
	@EJB
	private RolMenuDao rolMenuDao;
	@EJB
	private GestionarMenuDao menuDao;
	@EJB
	private RolMetodoDao rolMetodoDao;

	/**
	 * 
	 * @return listaRoles: list of roles that are loaded into the user
	 *         interface.
	 */
	public List<Rol> getListaRoles() {
		return listaRoles;
	}

	/**
	 * @param listaRoles
	 *            : list of roles that are loaded into the user interface.
	 */
	public void setListaRoles(List<Rol> listaRoles) {
		this.listaRoles = listaRoles;
	}

	/**
	 * @return selChecksPermisos: HashMap that stores the identifiers of the
	 *         methods that you want to assign permissions.
	 */
	public HashMap<Integer, HashMap<String, Boolean>> getSelChecksPermisos() {
		return selChecksPermisos;
	}

	/**
	 * @return rol: variable representing a role object to which it is made the
	 *         management.
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * @param rol
	 *            : variable representing a role object to which it is made the
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
	 * @return paginador : to manage the paging of the roles table
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : to manage the paging of the roles table
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paginadorMetodo: page manages the allocation of permits.
	 */
	public Paginador getPaginadorMetodo() {
		return paginadorMetodo;
	}

	/**
	 * @param pagerMethod
	 *            : page manages the allocation of permits.
	 */
	public void setPaginadorMetodo(Paginador pagerMethod) {
		this.paginadorMetodo = pagerMethod;
	}

	/**
	 * @return paginadorMenu: Manages the pagination of the menus related to the
	 *         allocation of permits.
	 */
	public Paginador getPaginadorMenu() {
		return paginadorMenu;
	}

	/**
	 * @param pagerMenu
	 *            : Manages the pagination of the menus related to the
	 *            allocation of permits.
	 */
	public void setPaginadorMenu(Paginador pagerMenu) {
		this.paginadorMenu = pagerMenu;
	}

	/**
	 * @return perma: "A" represents all permissions.
	 */
	public static String getPermA() {
		return Constantes.PERM_A;
	}

	/**
	 * @return perms: "S" represents permissions to query.
	 */
	public static String getPermS() {
		return Constantes.PERM_S;
	}

	/**
	 * @return permu: "U" represents permissions to update.
	 */
	public static String getPermU() {
		return Constantes.PERM_U;
	}

	/**
	 * @return permd: "D" represents permission to delete.
	 */
	public static String getPermD() {
		return Constantes.PERM_D;
	}

	/**
	 * @return permi: "I" represents permissions to insert.
	 */
	public static String getPermI() {
		return Constantes.PERM_I;
	}

	/**
	 * @return perml: "L" represents permissions to generate reports.
	 */
	public static String getPermL() {
		return Constantes.PERM_L;
	}

	/**
	 * Variable get a boolean that shows whether the role can or can not edit
	 * the name and expired after
	 * 
	 * @return esEditable: true if variable can be edited or false otherwise
	 */
	public boolean isEsEditable() {
		return esEditable;
	}

	/**
	 * Variable get a boolean that shows whether the role can or can not edit
	 * the name and expired afters
	 * 
	 * @param esEditable
	 *            : true if variable can be edited or false otherwise
	 */
	public void setEsEditable(boolean esEditable) {
		this.esEditable = esEditable;
	}

	/**
	 * @return nombreBuscar: Name to find the type of roles
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Name to find the type of role
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method prepares the view to add a new role or edit an existing one.
	 * 
	 * modify 27/09/2012 marisol.calderon
	 * 
	 * @param rol
	 *            : role to be edited if editing.
	 * 
	 * @return "regRol": redirects to the template to record a role.
	 */
	public String agregarEditarRol(Rol rol) {
		selChecksPermisos = new HashMap<Integer, HashMap<String, Boolean>>();
		menuViewNoSelected = new ArrayList<Integer>();
		try {
			if (rol != null) {
				this.rol = rol;
				validarEdicionRol(this.rol);
				cargarChecksPermisos();
			} else {
				this.rol = new Rol();
			}
			inicializarMetodosPermisos();
			inicializarMenusRelacionados();
			cargarMenuViewNoSelected();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regRol";
	}

	/**
	 * This method allows them to identify invisible menus.
	 * 
	 * @throws Exception
	 */
	private void cargarMenuViewNoSelected() throws Exception {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		List<Menu> listaTodosMenus = menuAction.getListaTodosMenus();
		if (listaTodosMenus != null && listaTodosMenus.size() > 0
				&& this.rol != null && this.rol.getId() > 0) {
			List<Menu> listMenusRol = menuDao.consultarMenusRol(this.rol);
			menu: for (Menu menu : listaTodosMenus) {
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
	 * This method allows to identify which permissions method has a role.s
	 * 
	 * @throws Exception
	 */
	private void cargarChecksPermisos() throws Exception {
		List<RolMetodo> rolMetodos = this.rolMetodoDao
				.consultarRolMetodos(this.rol);
		for (RolMetodo rolMetodo : rolMetodos) {
			RolMetodoPK rolMetodoPK = rolMetodo.getRolMetodoPK();
			int idMetodo = rolMetodoPK.getMetodo().getId();
			HashMap<String, Boolean> permisos = this.selChecksPermisos
					.get(idMetodo);
			if (permisos == null) {
				permisos = new HashMap<String, Boolean>();
			}
			permisos.put(rolMetodoPK.getPermiso(), true);
			this.selChecksPermisos.put(idMetodo, permisos);
		}
	}

	/**
	 * Method to validate if the role id is in the range of roles that you can
	 * not rename or terminate their validity
	 * 
	 * @param rol
	 *            : role to be validated
	 */
	public void validarEdicionRol(Rol rol) {
		if (rol.getId() <= Constantes.MAXIMO_ROLES_SIN_MODIFICAR) {
			esEditable = false;
		} else {
			esEditable = true;
		}
	}

	/**
	 * Initializes search parameters and load the initial list of types of roles
	 * 
	 * @author Liseth Jimenez
	 * 
	 * @return consultarRoles: navigation rule that returns the product
	 *         management roles
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return this.consultarRoles();
	}

	/**
	 * This method queries the list of roles according to their validity
	 * 
	 * @modify 27/09/2012 marisol.calderon
	 * 
	 * @return gesRol: navigation rule that returns the product management roles
	 */
	public String consultarRoles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeg = ControladorContexto
				.getBundle("mensajeSeguridad");
		String mensajeBusqueda = "";
		listaRoles = new ArrayList<Rol>();
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			this.rol = new Rol();
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			Long cantidadRoles = rolDao.cantidadRoles(condicionVigencia,
					this.nombreBuscar);
			if (cantidadRoles != null) {
				paginador.paginar(cantidadRoles);
			}
			listaRoles = rolDao.consultarRoles(paginador.getInicio(),
					paginador.getRango(), condicionVigencia, this.nombreBuscar);
			if ((listaRoles == null || listaRoles.size() <= 0)
					&& (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			} else if (listaRoles == null || listaRoles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeg.getString("rol_label_s"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesRol";
	}

	/**
	 * This method adds or edits a role.
	 * 
	 * @return consultarRoles: method of consulting roles
	 */
	public String agregarRol() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeMostrar = "message_registro_modificar";
		try {
			userTransaction.begin();
			rol.setNombre(rol.getNombre());
			rol.setUserName(identity.getUserName());
			if (rol.getId() != 0) {
				rolDao.editarRol(rol);
			} else {
				mensajeMostrar = "message_registro_guardar";
				rol.setFechaCreacion(new Date());
				rol.setFechaInicioVigencia(new Date());
				rolDao.guardarRol(rol);
			}
			agregarRolMenuMetodo();
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(mensajeMostrar),
							rol.getNombre()));
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return consultarRoles();
	}

	/**
	 * This method allows you to add or edit a rolMenu and rolMetodo and
	 * register it in the database.
	 * 
	 * @throws Exception
	 */
	private void agregarRolMenuMetodo() throws Exception {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		List<RolMetodo> rolMetodos = this.rolMetodoDao
				.consultarTodosRolMetodos(this.rol);
		for (Integer idMetodo : this.selChecksPermisos.keySet()) {
			HashMap<String, Boolean> permisos = this.selChecksPermisos
					.get(idMetodo);
			permiso: for (String permiso : permisos.keySet()) {
				if (permisos.get(permiso)) {
					for (RolMetodo rolMetodo : rolMetodos) {
						RolMetodoPK rolMetodoPK = rolMetodo.getRolMetodoPK();
						Metodo metodo = rolMetodoPK.getMetodo();
						if (metodo.getId() == idMetodo
								&& permiso.equals(rolMetodoPK.getPermiso())) {
							rolMetodo.setFechaFinVigencia(null);
							rolMetodo.setUserName(identity.getUserName());
							rolMetodoDao.editarRolMetodo(rolMetodo);
							rolMetodos.remove(rolMetodo);
							continue permiso;
						}
					}

					RolMetodo rolMetodo = llenarRolMetodo(permiso, idMetodo);
					rolMetodo.setFechaCreacion(new Date());
					rolMetodoDao.guardarRolMetodo(rolMetodo);
				}
			}
		}
		for (RolMetodo rolMetodo : rolMetodos) {
			rolMetodo.setFechaFinVigencia(new Date());
			rolMetodo.setUserName(identity.getUserName());
			rolMetodoDao.editarRolMetodo(rolMetodo);
		}

		this.listaMenus = new ArrayList<Menu>();
		this.rolMenus = rolMenuDao.consultarTodosRolMenu(this.rol);
		List<Menu> listaTodosMenus = menuAction.getListaTodosMenus();
		if (listaTodosMenus != null) {
			for (Menu menu : listaTodosMenus) {
				if (!this.menuViewNoSelected.contains(menu.getId())) {
					consultarGuardarPadreMenu(menu);
				}
			}
		}
		for (RolMenu rolMenu : this.rolMenus) {
			rolMenu.setFechaFinVigencia(new Date());
			rolMenu.setUserName(identity.getUserName());
			rolMenuDao.editarRolMenu(rolMenu);
		}
	}

	/**
	 * This method allows you to insert or edit the menu in the menu rolMetodo
	 * father and seek to insert as well.
	 * 
	 * @param menu
	 *            : Menu to add or edit menu and find the father.
	 * 
	 * @throws Exception
	 */
	private void consultarGuardarPadreMenu(Menu menu) throws Exception {
		this.listaMenus.add(menu);
		boolean crearMenu = true;
		for (RolMenu rolMenu : this.rolMenus) {
			RolMenuPK rolMenuPK = rolMenu.getRolMenuPK();
			Menu menuRol = rolMenuPK.getMenu();
			if (menuRol.getId() == menu.getId()) {
				rolMenu.setFechaFinVigencia(null);
				rolMenu.setUserName(identity.getUserName());
				rolMenuDao.editarRolMenu(rolMenu);
				this.rolMenus.remove(rolMenu);
				crearMenu = false;
				break;
			}
		}
		if (crearMenu) {
			RolMenu rolMenu = llenarRolMenu(menu);
			rolMenu.setFechaCreacion(new Date());
			rolMenuDao.guardarRolMenu(rolMenu);
		}
		Menu menuPadre = menuDao.consultarMenuPadre(menu.getId());
		if (menuPadre != null) {
			for (Menu listaMenu : this.listaMenus) {
				if (listaMenu.getId() == menuPadre.getId()) {
					return;
				}
			}
			consultarGuardarPadreMenu(menuPadre);
		}
	}

	/**
	 * Handles the change effective in the role records
	 * 
	 * @param vigente
	 *            : boolean option
	 * @return consultarRoles: navigation rule consulting roles method
	 */
	public String vigenciaRoles(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		StringBuilder regVigUsados = new StringBuilder();
		StringBuilder regExito = new StringBuilder();
		StringBuilder regRolesNoEdit = new StringBuilder();
		String mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
		try {
			if (vigente) {
				mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";
				boolean relaciones = buscarRelaciones(rol.getId());
				if (!relaciones) {
					validarEdicionRol(rol);
					if (esEditable) {
						rol.setFechaFinVigencia(new Date());
						rol.setUserName(identity.getUserName());
						rolDao.editarRol(rol);
						regExito.append(rol.getNombre() + ", ");
					} else {
						regRolesNoEdit.append(rol.getNombre() + ", ");
					}
				} else {
					regVigUsados.append(rol.getNombre() + ", ");
				}
			} else {
				rol.setFechaFinVigencia(null);
				rol.setUserName(identity.getUserName());
				rolDao.editarRol(rol);
				regExito.append(rol.getNombre() + ", ");
			}
			if (regRolesNoEdit.length() > 0) {
				ControladorContexto.mensajeError(bundleSeguridad
						.getString("rol_message_no_editable_vigencia")
						+ ": "
						+ regRolesNoEdit.substring(0,
								regRolesNoEdit.length() - 2));
			}
			if (regVigUsados.length() > 0) {
				ControladorContexto.mensajeError(bundle
						.getString("message_registro_vigencia_con_relaciones")
						+ ": "
						+ regVigUsados.substring(0, regVigUsados.length() - 2));
			}
			if (regExito.length() > 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString(mensajeCambioVigencia) + ": "
								+ regExito.substring(0, regExito.length() - 2));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarRoles();
	}

	/**
	 * Examines whether a role has relationships with user menu
	 * 
	 * @param id
	 *            : role identifier
	 * @return boolean: allows know the relation was found in the role it
	 *         returns true if the case returns false otherwise
	 * @throws Exception
	 */
	private boolean buscarRelaciones(short id) throws Exception {
		boolean result = false;
		result = rolDao.rolRelacionado(id, Constantes.ROLES_USUARIO);
		if (!result) {
			result = rolDao.rolRelacionado(id, Constantes.ROLES_MENU);
		}
		if (!result) {
			result = rolDao.rolRelacionado(id, Constantes.ROLES_METODO);
		}
		return result;
	}

	/**
	 * Validates if the role name exists in the database
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 * 
	 * @throws Exception
	 */
	public void validarNombre(FacesContext context, UIComponent toValidate,
			Object value) throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			Rol rolAux = new Rol();
			String resultado = "";

			if (rol.getId() != 0) {
				rolAux = rolDao.nombreExisteActualizar(nombre, rol.getId());
			} else {
				rolAux = rolDao.nombreExiste(nombre);
			}
			resultado = validarVigencia(resultado, rolAux);

			if (Constantes.VIGENTE.equals(resultado)) {
				context.addMessage(
						clientId,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								Mensaje.mensajeMostrar(bundle,
										"label_el,label_nombre,message_ya_existe_verifique"),
								null));
				((UIInput) toValidate).setValid(false);
			}
			if (Constantes.SIN_VIGENTE.equals(resultado)) {
				context.addMessage(
						clientId,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								Mensaje.mensajeMostrar(bundle,
										"label_el,label_nombre,message_ya_existe_sin_vigencia"),
								null));
				((UIInput) toValidate).setValid(false);
			}

			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Allows to validate if the role is in force
	 * 
	 * @param result
	 *            : Chain to check for
	 * @param rolAux
	 *            : It is a role object brought from the BD
	 * @return result: Variable value 'sinVigencia' if registration is not in
	 *         force or 'force' when this force
	 */
	private String validarVigencia(String result, Rol rolAux) {
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
	 * Initializes the list of methods to select you permissions.
	 */
	public void inicializarMetodosPermisos() {
		MetodoAction metodoAction = ControladorContexto
				.getContextBean(MetodoAction.class);
		metodoAction.inicializarBusqueda();
		ajustarMetodosPermisos(metodoAction);
	}

	/**
	 * This method loads the list of methods to select them permissions.
	 */
	public void cargarMetodosPermisos() {
		MetodoAction metodoAction = ControladorContexto
				.getContextBean(MetodoAction.class);
		metodoAction.consultarMetodos();
		ajustarMetodosPermisos(metodoAction);
	}

	/**
	 * Adjust the list of methods of recording role.
	 * 
	 * @param metodoAction
	 *            : Action method to reuse the functions for the list of
	 *            methods.
	 */
	private void ajustarMetodosPermisos(MetodoAction metodoAction) {
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.paginadorMetodo = metodoAction.getPaginador();
		String mensajeBusquedaPopUp = validaciones.getMensajeBusqueda();
		validaciones.setMensajeBusquedaPopUp(mensajeBusquedaPopUp);
	}

	/**
	 * Method allows loading the object with the method RolMetod and assigned
	 * permissions.
	 * 
	 * @param permiso
	 *            :Permission associated with the method.
	 * @param idMetodo
	 *            : Method identifier associated with permission.
	 * 
	 * @return: Permission object and the loaded method.
	 */
	public RolMetodo llenarRolMetodo(String permiso, Integer idMetodo) {
		RolMetodo rolMetodo = new RolMetodo();
		RolMetodoPK rolMetodoPK = new RolMetodoPK();
		rolMetodoPK.setRol(this.rol);
		Metodo metodo = new Metodo();
		metodo.setId(idMetodo);
		rolMetodoPK.setMetodo(metodo);
		rolMetodoPK.setPermiso(permiso);
		rolMetodo.setRolMetodoPK(rolMetodoPK);
		rolMetodo.setUserName(identity.getUserName());
		return rolMetodo;
	}

	/**
	 * allows load the rolMenu object, the menu assigned to the role.
	 * 
	 * @param menu
	 *            : Menu that is associated with the role.
	 * 
	 * @return: RolMenu object associated with the menu and the role.
	 */
	public RolMenu llenarRolMenu(Menu menu) {
		RolMenu rolMenu = new RolMenu();
		RolMenuPK rolMenuPK = new RolMenuPK();
		rolMenuPK.setRol(this.rol);
		rolMenuPK.setMenu(menu);
		rolMenu.setRolMenuPK(rolMenuPK);
		rolMenu.setUserName(identity.getUserName());
		return rolMenu;
	}

	/**
	 * Method allows add a menu to the list of selected identifiers that
	 * associate a permit.
	 * 
	 * @param id
	 *            : ID of the selected method.
	 * @param permiso
	 *            : Permission related to the method.
	 * @param selected
	 *            : It indicates whether it is selected or not.
	 */
	public void agregarMenuCheck(Integer id, String permiso, boolean selected) {
		try {
			HashMap<String, Boolean> permisos = this.selChecksPermisos.get(id);
			if (selected) {
				if (permisos == null) {
					permisos = new HashMap<String, Boolean>();
				}
				if (Constantes.PERM_A.equals(permiso)) {
					permisos.put(Constantes.PERM_A, selected);
					permisos.put(Constantes.PERM_S, selected);
					permisos.put(Constantes.PERM_U, selected);
					permisos.put(Constantes.PERM_D, selected);
					permisos.put(Constantes.PERM_I, selected);
					permisos.put(Constantes.PERM_L, selected);
				} else {
					permisos.put(permiso, selected);
					if (permSel(permisos, Constantes.PERM_S)
							&& permSel(permisos, Constantes.PERM_U)
							&& permSel(permisos, Constantes.PERM_D)
							&& permSel(permisos, Constantes.PERM_I)
							&& permSel(permisos, Constantes.PERM_L)) {
						permisos.put(Constantes.PERM_A, selected);
					}
				}
				this.selChecksPermisos.put(id, permisos);
			} else {
				if (Constantes.PERM_A.equals(permiso)) {
					this.selChecksPermisos.remove(id);
				} else {
					permisos.remove(permiso);
					if (permSel(permisos, Constantes.PERM_S)
							|| permSel(permisos, Constantes.PERM_U)
							|| permSel(permisos, Constantes.PERM_D)
							|| permSel(permisos, Constantes.PERM_I)
							|| permSel(permisos, Constantes.PERM_L)) {
						permisos.remove(Constantes.PERM_A);
						this.selChecksPermisos.put(id, permisos);
					} else {
						this.selChecksPermisos.remove(id);
					}
				}
			}
			cargarMenusRelacionados();
			removeMenuViewNoSelected();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Disclosed if permission is selected or not.
	 * 
	 * @param permisos
	 *            : List of permits to seek permission.
	 * @param permiso
	 *            : Permission looking for.
	 * 
	 * @return boolean: True if the permission is selected, otherwise false.s
	 */
	private boolean permSel(HashMap<String, Boolean> permisos, String permiso) {
		return permisos.get(permiso) != null && permisos.get(permiso);
	}

	/**
	 * Disclosed if permission is selected for the method sent.
	 * 
	 * @param id
	 *            : ID of the selected method.
	 * @param permiso
	 *            : Permission to query select.
	 * 
	 * @return True if the permission in the method is selected, otherwise
	 *         false.
	 */
	public boolean permisoSeleccionado(Integer id, String permiso) {
		HashMap<String, Boolean> permisos = this.selChecksPermisos.get(id);
		if (permisos != null) {
			return permisos.get(permiso) != null && permisos.get(permiso);
		}
		return false;
	}

	/**
	 * This method initializes the list of menus related the role.
	 */
	public void inicializarMenusRelacionados() {
		try {
			GestionarMenuAction menuAction = ControladorContexto
					.getContextBean(GestionarMenuAction.class);
			menuAction.datosIniciales();
			menuAction.setDesdeRol(true);
			cargarMenusRelacionados();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method loads the list of menus to relate to the role.
	 */
	public void cargarMenusRelacionados() {
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		menuAction.consultarMenus();
		this.paginadorMenu = menuAction.getPaginador();
		String mensajeBusquedaPopUp = validaciones.getMensajeBusqueda();
		validaciones.setMensajeBusquedaPopUp(mensajeBusquedaPopUp);
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
		for (Menu menu : menuAction.getListaTodosMenus()) {
			for (Integer integer : menuViewNoSelectedTemp) {
				if (menu.getId() == integer) {
					this.menuViewNoSelected.add(menu.getId());
				}
			}
		}
	}

	/**
	 * Adds or removes a menu from the list of selected identifiers to know
	 * whether or not displayed.
	 * 
	 * @param id
	 *            : ID of the selected menu.
	 * @param selected
	 *            : Indicates whether it is selected or not.
	 */
	public void noVerMenuCheck(Integer id, boolean selected) {
		if (selected) {
			this.menuViewNoSelected.add(id);
		} else {
			this.menuViewNoSelected.remove(id);
		}
	}

	/**
	 * Disclosed if the menu has permission to view or not.
	 * 
	 * @param id
	 *            : Menu identifier to see if you have permission to be
	 *            displayed or not.
	 * 
	 * @return boolean: True if you have permissions for viewing, otherwise
	 *         false.
	 */
	public boolean menuNoSeleccionado(Integer id) {
		return this.menuViewNoSelected.contains(id);
	}
}