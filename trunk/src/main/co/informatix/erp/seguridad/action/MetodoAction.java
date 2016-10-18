package co.informatix.erp.seguridad.action;

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
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.seguridad.dao.ManageMenuDao;
import co.informatix.erp.seguridad.dao.MetodoDao;
import co.informatix.erp.seguridad.dao.RolMetodoDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utils.ValidacionesAction.DatosGuardar;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.dao.MetodoMenuDao;
import co.informatix.security.entities.Menu;
import co.informatix.security.entities.Metodo;
import co.informatix.security.entities.MetodoMenu;

/**
 * This class implements business logic methods that apply specific permissions
 * on the system.
 * 
 * The logic is to consult, edit or add a method
 * 
 * @author marisol.calderon
 * @modify 19/06/2014 Gabriel.Moreno
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MetodoAction implements Serializable {

	@Inject
	private IdentityAction identity;
	@EJB
	private MetodoDao metodoDao;
	@EJB
	private RolMetodoDao rolMetodoDao;
	@EJB
	private ManageMenuDao menuDao;
	@EJB
	private MetodoMenuDao metodoMenuDao;
	@Resource
	private UserTransaction userTransaction;

	private List<Metodo> methods;
	private List<Menu> menus;
	private List<Menu> menusSelected = new ArrayList<Menu>();

	private Paginador pagination = new Paginador();
	private Paginador paginationMenus = new Paginador();
	private Metodo method;
	private String nameSearch;

	/**
	 * @return method: Method object and its permissions.
	 */
	public Metodo getMethod() {
		return method;
	}

	/**
	 * @param method
	 *            : Method object and its permissions.
	 */
	public void setMethod(Metodo method) {
		this.method = method;
	}

	/**
	 * @return methods: List of methods that are loaded into the user interface.
	 */
	public List<Metodo> getMethods() {
		return methods;
	}

	/**
	 * @param methods
	 *            : List of methods that are loaded into the user interface.
	 */
	public void setMethods(List<Metodo> methods) {
		this.methods = methods;
	}

	/**
	 * @return pagination: Management of the pagination of the list of menus in
	 *         record method.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management of the pagination of the list of menus in record
	 *            method.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationMenus: Mmanagement of the pagination of the list of
	 *         menus in record method.
	 */
	public Paginador getPaginationMenus() {
		return paginationMenus;
	}

	/**
	 * @param paginationMenus
	 *            : Management of the pagination of the list of menus in record
	 *            method.
	 */
	public void setPaginationMenus(Paginador paginationMenus) {
		this.paginationMenus = paginationMenus;
	}

	/**
	 * @return menus: List of application menus.
	 */
	public List<Menu> getMenus() {
		return menus;
	}

	/**
	 * @return menusSelected: List of selected menus in the application view.
	 */
	public List<Menu> getMenusSelected() {
		return menusSelected;
	}

	/**
	 * @return nameSearch: Get the name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Set the name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return searchMethods: method that consultation methods and load the
	 *         template with the information found.
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		return searchMethods();
	}

	/**
	 * Access to the existing methods in the database.
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * 
	 * @return gesMetodo: redirects to the Manage method
	 */
	public String searchMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String searchMessage = "";
		methods = new ArrayList<Metodo>();
		try {
			pagination.paginar(metodoDao.methodsAmount(this.nameSearch));
			this.methods = metodoDao.queryMethods(pagination.getInicio(),
					pagination.getRango(), this.nameSearch);
			if ((methods == null || methods.size() <= 0)
					&& (this.nameSearch != null && !"".equals(this.nameSearch))) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nameSearch + '"');
			} else if (methods == null || methods.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSecurity.getString("method_label_s"),
								bundle.getString("label_process") + ": " + '"'
										+ this.nameSearch + '"');
			}
			validations.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMetodo";
	}

	/**
	 * Method to load the template to add or edit a method.
	 * 
	 * @param method
	 *            : Object of method you want to register or edit.
	 * @return regMetodo: It redirects to register a method, there you can add
	 *         or edit a method.
	 */
	public String registerMethod(Metodo method) {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		try {
			this.menusSelected = new ArrayList<Menu>();
			if (method != null) {
				this.menusSelected = menuDao.consultMenusMethod(method.getId());
				for (Menu menu : this.menusSelected) {
					menuAction.convertNameMenuDescript(menu);
				}
				this.method = method;
			} else {
				this.method = new Metodo();
			}
			eraseAvailableMenus();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMetodo";
	}

	/**
	 * Save or edit a method.
	 * 
	 * @return: It redirects to a page to register or manage methods.
	 */
	public String saveMethods() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageKey = "message_registro_modificar";
		try {
			userTransaction.begin();
			method.setUserName(identity.getUserName());
			if (method.getId() != 0) {
				metodoDao.editMethod(method);
				addEditMethodMenu();
			} else {
				method.setFechaCreacion(new Date());
				metodoDao.saveMethod(method);
				for (Menu menu : this.menusSelected) {
					saveMethodMenu(menu);
				}
				messageKey = "message_registro_guardar";
			}
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageKey),
							method.getNombre()));
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
			return "regMetodo";
		}
		return searchMethods();
	}

	/**
	 * Allows save the method selected from the menus.
	 * 
	 * @throws Exception
	 */
	private void addEditMethodMenu() throws Exception {
		List<Menu> tempoSelectedMenu = menuDao.consultMenusMethod(method
				.getId());
		if (tempoSelectedMenu != null && this.menusSelected != null) {
			List<Integer> currentIds = new ArrayList<Integer>();
			List<Integer> newsIds = new ArrayList<Integer>();
			/* Lists are filled with only the ids */
			for (Menu menu : tempoSelectedMenu) {
				currentIds.add(menu.getId());
			}
			for (Menu menu : this.menusSelected) {
				newsIds.add(menu.getId());
			}
			/* Lists are validated */
			List<DatosGuardar> dataList = ValidacionesAction.validarListas(
					currentIds, newsIds);
			for (DatosGuardar saveData : dataList) {
				String action = saveData.getAccion();
				Menu menu = new Menu();
				menu.setId(saveData.getIdClase());
				if (Constantes.QUERY_DELETE.equals(action)) {
					MetodoMenu metodoMenu = metodoMenuDao.consultarMetodoMenu(
							this.method, menu);
					metodoMenuDao.eliminarMetodoMenu(metodoMenu);
				} else if (Constantes.QUERY_INSERT.equals(action)) {
					saveMethodMenu(menu);
				}
			}
		}
	}

	/**
	 * Save the menus related to the method.
	 * 
	 * @param menu
	 *            : Menu related to the method.
	 * @throws Exception
	 */
	private void saveMethodMenu(Menu menu) throws Exception {
		MetodoMenu menuMethod = new MetodoMenu();
		menuMethod.setMetodo(method);
		menuMethod.setMenu(menu);
		menuMethod.setFechaCreacion(new Date());
		menuMethod.setUserName(identity.getUserName());
		metodoMenuDao.guardarMetodoMenu(menuMethod);
	}

	/**
	 * Method that cleans the list of available menus.
	 * 
	 * @modify 20/05/2016 Gerardo.Herrera
	 */
	public void eraseAvailableMenus() {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		menuAction.setFromMethod(true);
		menuAction.setFromRol(false);
		menuAction.setNameSearch("");
		menuAction.consultMenus();
	}

	/**
	 * It Adds or removes from the list of selected menus.
	 * 
	 * @param flag
	 *            : Whether the item is being added or removed from the list of
	 *            menus.
	 */
	public void addRemoveAllMenus(String flag) {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		try {
			if (flag.equals(Constantes.ADD)) {
				List<Menu> tempSelectedMenu = menuDao
						.consultAllMenusAction(this.menusSelected);
				if (menuAction.getNameSearch() != null
						&& !"".equals(menuAction.getNameSearch())) {
					tempSelectedMenu = menuAction
							.filterMenusByName(tempSelectedMenu);
				}
				this.menusSelected.addAll(tempSelectedMenu);
				for (Menu menu : this.menusSelected) {
					menuAction.convertNameMenuDescript(menu);
				}
			} else {
				this.menusSelected = new ArrayList<Menu>();
			}
			menuAction.consultMenus();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It adds or removes a menu from the list of selected menus.
	 * 
	 * @param flag
	 *            : Whether the item is being added or removed from the list of
	 *            menus.
	 * @param menuAva
	 *            : Menu to add or remove from the list.
	 */
	public void addRemoveMenusListSelected(String flag, Menu menuAva) {
		if (flag.equals(Constantes.ADD)) {
			menusSelected.add(menuAva);
		} else {
			for (Menu selMenu : this.menusSelected) {
				if (menuAva.getId() == selMenu.getId()) {
					this.menusSelected.remove(selMenu);
					break;
				}
			}
		}
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		menuAction.consultMenus();
	}

	/**
	 * Valid if the method name exists in the database and if you have related
	 * menus.
	 * 
	 * @param context
	 *            : FacesContext variable for handling message type.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Validate component value.
	 */
	public void validateMethods(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		String clientId = toValidate.getClientId(context);
		String name = (String) value;
		try {
			Metodo auxMethod = metodoDao.methodNameExists(name,
					this.method.getId());
			if (auxMethod != null) {
				context.addMessage(
						toValidate.getClientId(context),
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString("message_ya_existe_verifique"), null));
			}

			if (this.menusSelected == null || this.menusSelected.size() <= 0) {
				context.addMessage(
						"metodoForm:tablaMenusDisponibles",
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								bundleSecurity
										.getString("menu_message_menu_required"),
								null));
			}

			if (!EncodeFilter.validarXSS(name, clientId, null)) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Valid if the name of action exists in the database.
	 * 
	 * @param context
	 *            : FacesContext variable for handling message type.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Validate component value.
	 */
	public void validateMethodName(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String clientId = toValidate.getClientId(context);
		String methodName = (String) value;
		try {
			Metodo auxMethod = metodoDao.actionNameExists(methodName,
					this.method.getId());
			if (auxMethod != null) {
				context.addMessage(
						toValidate.getClientId(context),
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString("message_ya_existe_verifique"), null));
			}

			if (!EncodeFilter.validarXSS(methodName, clientId, null)) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Deletes a method from the database that is not associated with a role.
	 * 
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @return searchMethods(): Consult the methods of the database and return
	 *         to the management methods.
	 */
	public String deleteMethod() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (this.method != null) {
				boolean result1 = rolMetodoDao.rolMethodRelation(this.method
						.getId());
				if (result1) {
					ControladorContexto
							.mensajeError(MessageFormat.format(
									bundleSecurity
											.getString("method_message_not_delete_rol"),
									this.method.getNombre()));
				} else {
					userTransaction.begin();
					List<MetodoMenu> menuMethods = metodoMenuDao
							.consultarTodosMetodoMenu(this.method);
					for (MetodoMenu menuMethod : menuMethods) {
						metodoMenuDao.eliminarMetodoMenu(menuMethod);
					}
					this.method.setFechaFinVigencia(new Date());
					metodoDao.editMethod(this.method);
					userTransaction.commit();
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle
									.getString("message_registro_eliminar"),
									method.getNombre()));
				}
			}
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return searchMethods();
	}
}