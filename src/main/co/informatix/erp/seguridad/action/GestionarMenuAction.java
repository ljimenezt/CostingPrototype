package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import co.informatix.erp.seguridad.dao.GestionarIconoDao;
import co.informatix.erp.seguridad.dao.GestionarMenuDao;
import co.informatix.erp.seguridad.dao.RolMenuDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.dao.IconoDao;
import co.informatix.security.dao.MetodoMenuDao;
import co.informatix.security.entities.Icono;
import co.informatix.security.entities.Menu;
import co.informatix.security.utils.Parametros;

/**
 * This class allows business logic of the menu system.
 * 
 * The logic is to consult, edit or add a menu.
 * 
 * @author marisol.calderon
 * @modify 19/06/2014 Gabriel.Moreno
 * 
 */
@SuppressWarnings("serial")
@ManagedBean(name = "menuAction")
@RequestScoped
public class GestionarMenuAction implements Serializable {
	@Inject
	private IdentityAction identity;
	@EJB
	private GestionarMenuDao menuDao;
	@EJB
	private RolMenuDao rolMenuDao;
	@EJB
	private MetodoMenuDao metodoMenuDao;
	@EJB
	private IconoDao iconoDao;
	@EJB
	private GestionarIconoDao gesIconoDao;

	private List<Menu> listMenus;
	private List<Menu> listAllMenus;
	private List<Icono> listIcons;

	private Paginador pagination = new Paginador();
	private Menu menuAction;
	private Menu menuPadre;

	private String nameSearch;
	private String nameIconSearch;
	private String folderFilesIcons;

	private boolean fromRol = false;

	/**
	 * @return menuAction: Variable that gets the object menu of the user
	 *         interface.
	 */
	public Menu getMenuAction() {
		return menuAction;
	}

	/**
	 * @param menuAction
	 *            : Variable that gets the object menu of the user interface.
	 * 
	 */
	public void setMenuAction(Menu menuAction) {
		this.menuAction = menuAction;
	}

	/**
	 * @return listMenus: Variable that gets the list of menus that are loaded
	 *         into the user interface.
	 */
	public List<Menu> getListMenus() {
		return listMenus;
	}

	/**
	 * @param listMenus
	 *            : Variable that gets the list of menus that are loaded into
	 *            the user interface.
	 */
	public void setListMenus(List<Menu> listMenus) {
		this.listMenus = listMenus;
	}

	/**
	 * @return listAllMenus: Variable you set a list of all menus that are
	 *         loaded into the user interface.
	 */
	public List<Menu> getListAllMenus() {
		return listAllMenus;
	}

	/**
	 * 
	 * @return pagination: pagination controls menu list.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * 
	 * @param pagination
	 *            : pagination controls menu list.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * 
	 * @return menuPadre: Variable that gets the parent menu item.
	 */
	public Menu getMenuPadre() {
		return menuPadre;
	}

	/**
	 * 
	 * @param menuPadre
	 *            : Variable that gets the parent menu item.
	 */
	public void setMenuPadre(Menu menuPadre) {
		this.menuPadre = menuPadre;
	}

	/**
	 * 
	 * @return nameSearch: Variable that gets the name of the menu that search
	 *         the user interface.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * 
	 * @param nameSearch
	 *            : Variable sets the name of the menu that will search the user
	 *            interface.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listIcons: Gets the list of icons.
	 */
	public List<Icono> getListIcons() {
		return listIcons;
	}

	/**
	 * @param listIcons
	 *            : Returns the list of icons.
	 */
	public void setListIcons(List<Icono> listIcons) {
		this.listIcons = listIcons;
	}

	/**
	 * @return folderFilesIcons: The actual folder path where the menu icons.
	 */
	public String getFolderFilesIcons() {
		this.folderFilesIcons = Constantes.RUTA_IMG
				+ Constantes.CARPETA_ICONOS_MENU_CABECERA;
		return folderFilesIcons;
	}

	/**
	 * @return nameIconSearch: variable that gets the icon name is sought in the
	 *         user interface.
	 */
	public String getNameIconSearch() {
		return nameIconSearch;
	}

	/**
	 * 
	 * @param nameIconSearch
	 *            : variable that gets the icon name is sought in the user
	 *            interface.
	 */
	public void setNameIconSearch(String nameIconSearch) {
		this.nameIconSearch = nameIconSearch;
	}

	/**
	 * @return fromRol: Indicates whether the action is executed from RolAction,
	 *         to perform special actions.
	 */
	public boolean isFromRol() {
		return fromRol;
	}

	/**
	 * @param fromRol
	 *            : Indicates whether the action is executed from RolAction, to
	 *            perform special actions.
	 */
	public void setFromRol(boolean fromRol) {
		this.fromRol = fromRol;
	}

	/**
	 * Initializes the name in the search menu.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultMenus(): Consult the menus in the system and returns to
	 *         the management template with search results.
	 */
	public String searchInitialization() {
		try {
			initialData();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMenus();
	}

	/**
	 * Method that allows you to load the initial data.
	 * 
	 * @throws Exception
	 */
	public void initialData() throws Exception {
		this.nameSearch = "";
		this.fromRol = false;
		pagination = new Paginador();
	}

	/**
	 * ListaMenus provides access existing in the database
	 * 
	 * @return back: depending to a flag desdeModal redirects to the Manage
	 *         menus or not redirect.
	 */
	public String consultMenus() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String messageSearch = "";
		StringBuilder unionMessageSearch = new StringBuilder();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder order = new StringBuilder();
		listMenus = new ArrayList<Menu>();
		List<Menu> listMenusTemporal = new ArrayList<Menu>();
		String isPopup = ControladorContexto.getParam("param2");
		boolean fromModal = (isPopup != null && "si".equals(isPopup)) ? true
				: false;
		String back = fromModal ? "" : "gesMenus";
		try {
			advancedSearch(consult, order, parameters, bundle,
					unionMessageSearch);
			if (fromRol
					|| (this.nameSearch != null && !"".equals(this.nameSearch))) {
				this.listAllMenus = menuDao.consultMenus(null, null, consult,
						order, parameters);
				List<Menu> listMenusData = filterMenusByName(this.listAllMenus);
				int start = 0;
				int totalReg = pagination.getRango();
				long quantityMenus = (long) listMenusData.size();
				if (fromModal) {
					pagination.paginarRangoDefinido(quantityMenus, 5);
					totalReg = 5;
				} else {
					pagination.paginar(quantityMenus);
				}
				start = pagination.getInicio();
				int range = start + totalReg;
				if (listMenusData.size() < range) {
					range = listMenusData.size();
				}
				listMenusTemporal = listMenusData.subList(start, range);
			} else {
				Long quantityMenus = menuDao.quantityMenus(consult, parameters);
				if (fromModal) {
					pagination.paginarRangoDefinido(quantityMenus, 5);
				} else {
					pagination.paginar(quantityMenus);
				}
				listMenusTemporal = menuDao.consultMenus(
						pagination.getInicio(), pagination.getRango(), consult,
						order, parameters);
			}
			this.listMenus = loadInformationMenus(listMenusTemporal);
			if ((listMenus == null || listMenus.size() <= 0)
					&& !"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessageSearch);

			} else if (listMenus == null || listMenus.size() <= 0) {
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
								bundleSecurity.getString("menu_label_s"),
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
	 * Allows you to filter the list of menus for the selected name in the
	 * search criteria.
	 * 
	 * @param listTdosMenus
	 *            : List of all existing menus.
	 * 
	 * @return listMenusData: List of menus filtered by name.
	 */
	public List<Menu> filterMenusByName(List<Menu> listTdosMenus) {
		List<Menu> listMenusData = new ArrayList<Menu>();
		if (listTdosMenus != null) {
			for (Menu menu : listTdosMenus) {
				convertNameMenuDescript(menu);
				String nameMenu = menu.getNombre().toUpperCase();
				if (nameMenu.indexOf(this.nameSearch.toUpperCase()) != -1) {
					listMenusData.add(0, menu);
				}
			}
		}
		return listMenusData;
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param order
	 *            : order list that is loaded into the user interface.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void advancedSearch(StringBuilder consult, StringBuilder order,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		consult.append(" WHERE m.fechaFinVigencia IS NULL ");

		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
		if (fromRol) {
			RolAction rolAction = ControladorContexto
					.getContextBean(RolAction.class);
			consult.append("AND m IN(SELECT DISTINCT mm.menu FROM MetodoMenu mm ");
			consult.append("WHERE mm.metodo.id IN (:idsMetodo)) ");
			List<Integer> idsMetodo = new ArrayList<Integer>(rolAction
					.getMethodsPermissions().keySet());
			if (idsMetodo.size() <= 0) {
				idsMetodo.add(-1);
			}
			SelectItem item = new SelectItem(idsMetodo, "idsMetodo");
			parameters.add(item);
		}
		order.append(" ORDER BY m.nombre");
	}

	/**
	 * Allows you to load the list of menus with information from parents, this
	 * allows do Join in the main query.
	 * 
	 * @param listMenusInfo
	 *            : temporary list with the initial information to be loaded.
	 * @return List<Menu> : list of the information menus to load in the view.
	 * @throws Exception
	 */
	private List<Menu> loadInformationMenus(List<Menu> listMenusTemporal)
			throws Exception {
		List<Menu> listMenusInfo = new ArrayList<Menu>();
		if (listMenusTemporal != null && listMenusTemporal.size() > 0) {
			for (Menu menu : listMenusTemporal) {
				menu = loadDetailsMenu(menu);
				listMenusInfo.add(menu);
			}
		}
		return listMenusInfo;
	}

	/**
	 * Allows upload details sent as a parameter menu.
	 * 
	 * @param menu
	 *            : Menu sent to load the details.
	 * 
	 * @return Menu item loaded with details.
	 * @throws Exception
	 */
	public Menu loadDetailsMenu(Menu menu) throws Exception {
		convertNameMenuDescript(menu);
		Icono icon = menu.getIcono();
		if (icon != null) {
			icon = iconoDao.iconoXId(icon.getId());
			menu.setIcono(icon);
		}
		Menu menuFatherInfo = menuDao.consultMenuFather(menu.getId());
		if (menuFatherInfo != null) {
			convertNameMenuDescript(menuFatherInfo);
			menu.setMenuPadre(menuFatherInfo);
		}
		return menu;
	}

	/**
	 * Method for converting the name of the menu depending on the example
	 * internationalization: mensajeMenu.causa_label: Cause or Cause.
	 * 
	 * @param menu
	 *            : Menu to be converted.
	 */
	protected void convertNameMenuDescript(Menu menu) {
		if (menu.getNombre().contains("label_")
				|| menu.getNombre().contains("_label")) {
			String nameMenu = menu.getNombre();
			HtmlOutputText nameMenuShow = new HtmlOutputText();
			nameMenuShow.setValueExpression("value",
					Parametros.getValueExpression(nameMenu));
			if (nameMenuShow != null) {
				menu.setNombre((String) nameMenuShow.getValue());
			}
		}
		if (menu.getDescripcion().contains("label_")
				|| menu.getDescripcion().contains("_label")) {
			String descriptMenu = menu.getDescripcion();
			HtmlOutputText decriptMenuShow = new HtmlOutputText();
			decriptMenuShow.setValueExpression("value",
					Parametros.getValueExpression(descriptMenu));
			if (decriptMenuShow != null) {
				menu.setDescripcion((String) decriptMenuShow.getValue());
			}
		}
	}

	/**
	 * Method to record or edit a menu system.
	 * 
	 * @param menu
	 *            : Object menu to record or edit.
	 * @return regMenu: redirected to the registration page menu.
	 * 
	 */
	public String registerMenu(Menu menu) {
		listMenus = new ArrayList<Menu>();
		this.menuPadre = new Menu();
		nameSearch = "";
		try {
			if (menu != null) {
				Menu menuUpdated = menuDao.consultMenuXId(menu.getId());
				menu.setNombre(menuUpdated.getNombre());
				menu.setDescripcion(menuUpdated.getDescripcion());
				this.menuAction = menu;
				if (this.menuAction.getMenuPadre() != null) {
					this.menuPadre = this.menuAction.getMenuPadre();
				}
			} else {
				this.menuAction = new Menu();
				this.menuAction.setVisible(true);
				this.menuAction.setIcono(new Icono());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMenu";
	}

	/**
	 * Allows save or edit a menu, validating that the name is not repeated in
	 * the database.
	 * 
	 * @return consultMenus: returns to a page based on what happened.
	 */
	public String saveMenu() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		try {
			if (this.menuPadre != null && this.menuPadre.getId() != 0) {
				menuAction.setMenuPadre(this.menuPadre);
			} else {
				menuAction.setMenuPadre(null);
			}
			if (this.menuAction.getIcono() != null
					&& this.menuAction.getIcono().getId() == 0) {
				this.menuAction.setIcono(null);
			}
			if ("".equals(this.menuAction.getUrl())) {
				this.menuAction.setUrl(null);
			}
			menuAction.setUserName(identity.getUserName());
			if (menuAction.getId() != 0) {
				menuDao.editMenu(menuAction);
			} else {
				menuAction.setFechaCreacion(new Date());
				menuDao.saveMenu(menuAction);
				key = "message_registro_guardar";
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							menuAction.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
			return "regMenu";
		}
		return consultMenus();
	}

	/**
	 * Method to delete a menu of the database, which has no parent menu or be
	 * associated with a role or method.
	 * 
	 * @modify Cristhian.Pico 23/12/2014
	 * 
	 * @return consultMenus(): check out the menus of the database and returns
	 *         to manage menus.
	 */
	public String deleteMenu() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (this.menuAction != null) {
				boolean menuFather2 = menuDao.sonsMenu(this.menuAction.getId());
				String validateRelations = validateRelations(menuAction.getId());
				if (menuFather2) {
					ControladorContexto
							.mensajeError(MessageFormat.format(
									bundleSecurity
											.getString("menu_message_not_delete_parent"),
									menuAction.getNombre()));
				} else if (!"".equals(validateRelations)) {
					ControladorContexto.mensajeError(MessageFormat.format(
							bundleSecurity.getString(validateRelations),
							menuAction.getNombre()));
				} else {
					menuAction.setUserName(identity.getUserName());
					menuAction.setFechaFinVigencia(new Date());
					menuDao.editMenu(menuAction);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle
									.getString("message_registro_eliminar"),
									menuAction.getNombre()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMenus();
	}

	/**
	 * Method to validate the relationships you have the menu, the name of the
	 * entity or entities to which this partner is sent to DAO.
	 * 
	 * @param idMenu
	 *            : id of the menu that you want to validate.
	 * @return: String returns the string with the message of the entities which
	 *          have relation with the menu.
	 * @throws Exception
	 */
	private String validateRelations(int idMenu) throws Exception {
		String messageResult = "";
		boolean result1 = false;
		boolean result2 = false;
		result1 = rolMenuDao.rolMenuRelations(idMenu);
		result2 = metodoMenuDao.relacionesMetodoMenu(idMenu);
		if (result1 && result2) {
			messageResult = "menu_message_not_delete_rol_method";
		} else {
			if (result1) {
				messageResult = "menu_message_not_delete_rol";
			} else if (result2) {
				messageResult = "menu_message_not_delete_method";
			}
		}
		return messageResult;
	}

	/**
	 * Removes the menu icon.
	 */
	public void deleteIcon() {
		this.menuAction.setIcono(null);
	}

	/**
	 * Method for associating the icon menu.
	 * 
	 * @param icon
	 *            : associated icon.
	 */
	public void loadIconMenu(Icono icon) {
		this.menuAction.setIcono(icon);
	}

	/**
	 * Method to consult the list of all existing icons in the database to be
	 * assigned to the menu.
	 * 
	 * @modify Luz.Jaimes 24/02/2014
	 */
	public void consultIcons() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		String messageSearch = "";
		listIcons = new ArrayList<Icono>();
		try {
			IconoAction iconAction = ControladorContexto
					.getContextBean(IconoAction.class);
			iconAction.validateIconsFolder();
			Long quantityIconsByName = gesIconoDao
					.quantityIconsByName(nameIconSearch);
			if (quantityIconsByName != null) {
				pagination.paginarRangoDefinido(quantityIconsByName, 5);
			}
			listIcons = gesIconoDao.searchIconsXNamePaginated(
					pagination.getInicio(), pagination.getRango(),
					nameIconSearch);

			if ((this.listIcons == null || this.listIcons.size() <= 0)
					&& !"".equals(nameIconSearch)) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundleSecurity.getString("icon_label") + ": "
										+ '"' + this.nameIconSearch + '"');
			} else if (this.listIcons == null || this.listIcons.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(this.nameIconSearch)) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSecurity.getString("icon_label_s"),
								bundleSecurity.getString("icon_label") + ": "
										+ '"' + this.nameIconSearch + '"');

			}
			if (!"".equals(messageSearch)) {
				ControladorContexto.mensajeInformacion(
						"popupForm:mensajesPopupIconos", messageSearch);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Initializes the name on the search icon in the view popup register.
	 * 
	 * @author Luz.Jaimes
	 * 
	 */
	public void searchInitializationIcon() {
		this.nameIconSearch = "";
		consultIcons();
	}
}