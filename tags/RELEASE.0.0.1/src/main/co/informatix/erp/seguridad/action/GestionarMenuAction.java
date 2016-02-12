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
 * This class allows business logic of the menu system
 * 
 * The logic is to consult, edit or add a menu
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

	private List<Menu> listaMenus;
	private List<Menu> listaTodosMenus;
	private List<Icono> listaIconos;

	private Paginador paginador = new Paginador();
	private Menu menuAction;
	private Menu menuPadre;

	private String nombreBuscar;
	private String nombreIconoBuscar;
	private String carpetaArchivosIconos;

	private boolean desdeRol = false;

	/**
	 * @return menuAction: Variable that gets the object menu of the user interface.
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
	 * @return listaMenus: Variable that gets the list of menus that are loaded
	 *         into the user interface.
	 */
	public List<Menu> getListaMenus() {
		return listaMenus;
	}

	/**
	 * @param listaMenus
	 *            : Variable that gets the list of menus that are loaded into
	 *            the user interface.
	 */
	public void setListaMenus(List<Menu> listaMenus) {
		this.listaMenus = listaMenus;
	}

	/**
	 * @return listaTodosMenus: Variable you set a list of all menus that are
	 *         loaded into the user interface.
	 */
	public List<Menu> getListaTodosMenus() {
		return listaTodosMenus;
	}

	/**
	 * 
	 * @return paginador: pagination controls menu list.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * 
	 * @param paginador
	 *            : pagination controls menu list.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * 
	 * @return menuPadre: Variable that gets the parent menu item
	 */
	public Menu getMenuPadre() {
		return menuPadre;
	}

	/**
	 * 
	 * @param menuPadre
	 *            : Variable that gets the parent menu item
	 */
	public void setMenuPadre(Menu menuPadre) {
		this.menuPadre = menuPadre;
	}

	/**
	 * 
	 * @return nombreBuscar: Variable that gets the name of the menu that search
	 *         the user interface
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * 
	 * @param nombreBuscar
	 *            : Variable sets the name of the menu that will search the user
	 *            interface
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return listaIconos: Gets the list of icons.
	 */
	public List<Icono> getListaIconos() {
		return listaIconos;
	}

	/**
	 * @param listaIconos
	 *            : Returns the list of icons.
	 */
	public void setListaIconos(List<Icono> listaIconos) {
		this.listaIconos = listaIconos;
	}

	/**
	 * @return carpetaArchivosIconos: The actual folder path where the menu
	 *         icons
	 */
	public String getCarpetaArchivosIconos() {
		this.carpetaArchivosIconos = Constantes.RUTA_IMG
				+ Constantes.CARPETA_ICONOS_MENU_CABECERA;
		return carpetaArchivosIconos;
	}

	/**
	 * @return nombreIconoBuscar: variable that gets the icon name is sought in
	 *         the user interface
	 */
	public String getNombreIconoBuscar() {
		return nombreIconoBuscar;
	}

	/**
	 * 
	 * @param nombreIconoBuscar
	 *            : variable that gets the icon name is sought in the user
	 *            interface
	 */
	public void setNombreIconoBuscar(String nombreIconoBuscar) {
		this.nombreIconoBuscar = nombreIconoBuscar;
	}

	/**
	 * @return desdeRol: Indicates whether the action is executed from
	 *         RolAction, to perform special actions.
	 */
	public boolean isDesdeRol() {
		return desdeRol;
	}

	/**
	 * @param desdeRol
	 *            : Indicates whether the action is executed from RolAction, to
	 *            perform special actions.
	 */
	public void setDesdeRol(boolean desdeRol) {
		this.desdeRol = desdeRol;
	}

	/**
	 * Initializes the name in the search menu.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultarMenus(): Consult the menus in the system and returns to
	 *         the management template with search results.
	 */
	public String inicializarBusqueda() {
		try {
			datosIniciales();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMenus();
	}

	/**
	 * Method that allows you to load the initial data.
	 * 
	 * @throws Exception
	 */
	public void datosIniciales() throws Exception {
		this.nombreBuscar = "";
		this.desdeRol = false;
		paginador = new Paginador();
	}

	/**
	 * ListaMenus provides access existing in the database
	 * 
	 * @return retorno: depending to a flag desdeModal redirects to the Manage
	 *         menus or not redirect.
	 */
	public String consultarMenus() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String mensajeBusqueda = "";
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder order = new StringBuilder();
		listaMenus = new ArrayList<Menu>();
		List<Menu> listaMenusTemporal = new ArrayList<Menu>();
		String esPopup = ControladorContexto.getParam("param2");
		boolean desdeModal = (esPopup != null && "si".equals(esPopup)) ? true
				: false;
		String retorno = desdeModal ? "" : "gesMenus";
		try {
			busquedaAvanzada(consulta, order, parametros, bundle,
					unionMensajesBusqueda);
			if (desdeRol
					|| (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				this.listaTodosMenus = menuDao.consultarMenus(null, null,
						consulta, order, parametros);
				List<Menu> listaMenusDatos = filtrarMenusPorNombre(this.listaTodosMenus);
				int inicio = 0;
				int totalReg = paginador.getRango();
				long cantidadMenus = (long) listaMenusDatos.size();
				if (desdeModal) {
					paginador.paginarRangoDefinido(cantidadMenus, 5);
					totalReg = 5;
				} else {
					paginador.paginar(cantidadMenus);
				}
				inicio = paginador.getInicio();
				int rango = inicio + totalReg;
				if (listaMenusDatos.size() < rango) {
					rango = listaMenusDatos.size();
				}
				listaMenusTemporal = listaMenusDatos.subList(inicio, rango);
			} else {
				Long cantidadMenus = menuDao
						.cantidadMenus(consulta, parametros);
				if (desdeModal) {
					paginador.paginarRangoDefinido(cantidadMenus, 5);
				} else {
					paginador.paginar(cantidadMenus);
				}
				listaMenusTemporal = menuDao.consultarMenus(
						paginador.getInicio(), paginador.getRango(), consulta,
						order, parametros);
			}
			this.listaMenus = cargarInformacionMenus(listaMenusTemporal);
			if ((listaMenus == null || listaMenus.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);

			} else if (listaMenus == null || listaMenus.size() <= 0) {
				mensajeBusqueda = bundle
						.getString("message_no_existen_registros");
				if (!desdeModal) {
					ControladorContexto.mensajeInformacion(null,
							mensajeBusqueda);
					mensajeBusqueda = "";
				}
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("menu_label_s"),
								unionMensajesBusqueda);
			}
			if (!"".equals(mensajeBusqueda) && desdeModal) {
				validaciones.setMensajeBusquedaPopUp(mensajeBusqueda);
			} else {
				validaciones.setMensajeBusqueda(mensajeBusqueda);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return retorno;
	}

	/**
	 * Allows you to filter the list of menus for the selected name in the
	 * search criteria.
	 * 
	 * @param listaTdosMenus
	 *            : List of all existing menus.
	 * 
	 * @return listaMenusDatos: List of menus filtered by name.
	 */
	public List<Menu> filtrarMenusPorNombre(List<Menu> listaTdosMenus) {
		List<Menu> listaMenusDatos = new ArrayList<Menu>();
		if (listaTdosMenus != null) {
			for (Menu menu : listaTdosMenus) {
				convertirNombreMenuDescript(menu);
				String nombreMenu = menu.getNombre().toUpperCase();
				if (nombreMenu.indexOf(this.nombreBuscar.toUpperCase()) != -1) {
					listaMenusDatos.add(0, menu);
				}
			}
		}
		return listaMenusDatos;
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
	private void busquedaAvanzada(StringBuilder consult, StringBuilder order,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		consult.append(" WHERE m.fechaFinVigencia IS NULL ");

		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
		if (desdeRol) {
			RolAction rolAction = ControladorContexto
					.getContextBean(RolAction.class);
			consult.append("AND m IN(SELECT DISTINCT mm.menu FROM MetodoMenu mm ");
			consult.append("WHERE mm.metodo.id IN (:idsMetodo)) ");
			List<Integer> idsMetodo = new ArrayList<Integer>(rolAction
					.getSelChecksPermisos().keySet());
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
	 * allows do Join in the main query
	 * 
	 * @param listaMenusInfo
	 *            : temporary list with the initial information to be loaded
	 * @return List<Menu> : list of the information menus to load in the view.
	 * @throws Exception
	 */
	private List<Menu> cargarInformacionMenus(List<Menu> listaMenusTemporal)
			throws Exception {
		List<Menu> listaMenusInfo = new ArrayList<Menu>();
		if (listaMenusTemporal != null && listaMenusTemporal.size() > 0) {
			for (Menu menu : listaMenusTemporal) {
				menu = cargarDetallesMenu(menu);
				listaMenusInfo.add(menu);
			}
		}
		return listaMenusInfo;
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
	public Menu cargarDetallesMenu(Menu menu) throws Exception {
		convertirNombreMenuDescript(menu);
		Icono icono = menu.getIcono();
		if (icono != null) {
			icono = iconoDao.iconoXId(icono.getId());
			menu.setIcono(icono);
		}
		Menu menuPadreInfo = menuDao.consultarMenuPadre(menu.getId());
		if (menuPadreInfo != null) {
			convertirNombreMenuDescript(menuPadreInfo);
			menu.setMenuPadre(menuPadreInfo);
		}
		return menu;
	}

	/**
	 * Method for converting the name of the menu depending on the example
	 * internationalization: mensajeMenu.causa_label: Cause or Cause
	 * 
	 * @param menu
	 *            : Menu to be converted
	 */
	protected void convertirNombreMenuDescript(Menu menu) {
		if (menu.getNombre().contains("label_")
				|| menu.getNombre().contains("_label")) {
			String nombreMenu = menu.getNombre();
			HtmlOutputText nombreMenuMostrar = new HtmlOutputText();
			nombreMenuMostrar.setValueExpression("value",
					Parametros.getValueExpression(nombreMenu));
			if (nombreMenuMostrar != null) {
				menu.setNombre((String) nombreMenuMostrar.getValue());
			}
		}
		if (menu.getDescripcion().contains("label_")
				|| menu.getDescripcion().contains("_label")) {
			String descriptMenu = menu.getDescripcion();
			HtmlOutputText decriptMenuMostrar = new HtmlOutputText();
			decriptMenuMostrar.setValueExpression("value",
					Parametros.getValueExpression(descriptMenu));
			if (decriptMenuMostrar != null) {
				menu.setDescripcion((String) decriptMenuMostrar.getValue());
			}
		}
	}

	/**
	 * Method to record or edit a menu system
	 * 
	 * @param menu
	 *            : Object menu to record or edit
	 * @return regMenu: redirected to the registration page menu
	 * 
	 */
	public String registrarMenu(Menu menu) {
		listaMenus = new ArrayList<Menu>();
		this.menuPadre = new Menu();
		nombreBuscar = "";
		try {
			if (menu != null) {
				Menu menuActualizado = menuDao.consultarMenuXId(menu.getId());
				menu.setNombre(menuActualizado.getNombre());
				menu.setDescripcion(menuActualizado.getDescripcion());
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
	 * @return consultarMenus: returns to a page based on what happened.
	 */
	public String guardarMenu() {
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
				menuDao.editarMenu(menuAction);
			} else {
				menuAction.setFechaCreacion(new Date());
				menuDao.guardarMenu(menuAction);
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
		return consultarMenus();
	}

	/**
	 * Method to delete a menu of the database, which has no parent menu or be
	 * associated with a role or method
	 * 
	 * @modify Cristhian.Pico 23/12/2014
	 * 
	 * @return consultarMenus(): check out the menus of the database and returns
	 *         to manage menus
	 */
	public String eliminarMenu() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		try {
			if (this.menuAction != null) {
				boolean menuPadre2 = menuDao.hijosMenu(this.menuAction.getId());
				String validarRelaciones = validarRelaciones(menuAction.getId());
				if (menuPadre2) {
					ControladorContexto
							.mensajeError(MessageFormat.format(
									bundleSeguridad
											.getString("menu_message_no_eliminar_padre"),
									menuAction.getNombre()));
				} else if (!"".equals(validarRelaciones)) {
					ControladorContexto.mensajeError(MessageFormat.format(
							bundleSeguridad.getString(validarRelaciones),
							menuAction.getNombre()));
				} else {
					menuAction.setUserName(identity.getUserName());
					menuAction.setFechaFinVigencia(new Date());
					menuDao.editarMenu(menuAction);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle
									.getString("message_registro_eliminar"),
									menuAction.getNombre()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMenus();
	}

	/**
	 * Method to validate the relationships you have the menu, the name of the
	 * entity or entities to which this partner is sent to DAO
	 * 
	 * @param idMenu
	 *            : id of the menu that you want to validate
	 * @return: String returns the string with the message of the entities which
	 *          have relation with the menu
	 * @throws Exception
	 */
	private String validarRelaciones(int idMenu) throws Exception {
		String mensajeResult = "";
		boolean result1 = false;
		boolean result2 = false;
		result1 = rolMenuDao.relacionesRolMenu(idMenu);
		result2 = metodoMenuDao.relacionesMetodoMenu(idMenu);
		if (result1 && result2) {
			mensajeResult = "menu_message_no_eliminar_rol_metodo";
		} else {
			if (result1) {
				mensajeResult = "menu_message_no_eliminar_rol";
			} else if (result2) {
				mensajeResult = "menu_message_no_eliminar_metodo";
			}
		}
		return mensajeResult;
	}

	/**
	 * Removes the menu icon
	 */
	public void quitarIcono() {
		this.menuAction.setIcono(null);
	}

	/**
	 * Method for associating the icon menu
	 * 
	 * @param icono
	 *            : associated icon
	 */
	public void cargarIconoMenu(Icono icono) {
		this.menuAction.setIcono(icono);
	}

	/**
	 * Method to consult the list of all existing icons in the database to be
	 * assigned to the menu
	 * 
	 * @modify Luz.Jaimes 24/02/2014
	 */
	public void consultarIconos() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		String mensajeBusqueda = "";
		listaIconos = new ArrayList<Icono>();
		try {
			IconoAction iconoAction = ControladorContexto
					.getContextBean(IconoAction.class);
			iconoAction.validarIconosCarpeta();
			Long cantidadIconosPorNombre = gesIconoDao
					.cantidadIconosPorNombre(nombreIconoBuscar);
			if (cantidadIconosPorNombre != null) {
				paginador.paginarRangoDefinido(cantidadIconosPorNombre, 5);
			}
			listaIconos = gesIconoDao.buscarIconosXNombrePaginado(
					paginador.getInicio(), paginador.getRango(),
					nombreIconoBuscar);

			if ((this.listaIconos == null || this.listaIconos.size() <= 0)
					&& !"".equals(nombreIconoBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("icono_label") + ": "
										+ '"' + this.nombreIconoBuscar + '"');
			} else if (this.listaIconos == null || this.listaIconos.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(this.nombreIconoBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("icono_label_s"),
								bundleSeguridad.getString("icono_label") + ": "
										+ '"' + this.nombreIconoBuscar + '"');

			}
			if (!"".equals(mensajeBusqueda)) {
				ControladorContexto.mensajeInformacion(
						"popupForm:mensajesPopupIconos", mensajeBusqueda);
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
	public void inicializarBusquedaIcono() {
		this.nombreIconoBuscar = "";
		consultarIconos();
	}
}