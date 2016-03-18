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

import co.informatix.erp.seguridad.dao.GestionarMenuDao;
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
 * This class allows business logic methods that apply specific permissions on
 * the system
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
	private GestionarMenuDao menuDao;
	@EJB
	private MetodoMenuDao metodoMenuDao;
	@Resource
	private UserTransaction userTransaction;

	private List<Metodo> metodos;
	private List<Menu> menus;
	private List<Menu> menusSelected = new ArrayList<Menu>();

	private Paginador paginador = new Paginador();
	private Paginador paginadorMenus = new Paginador();
	private Metodo metodo;
	private String nombreBuscar;

	/**
	 * 
	 * @return metodo: Method object that apply to permits
	 */
	public Metodo getMetodo() {
		return metodo;
	}

	/**
	 * 
	 * @param metodo
	 *            : Method object that apply to permits
	 */
	public void setMetodo(Metodo metodo) {
		this.metodo = metodo;
	}

	/**
	 * 
	 * @return metodos: List of methods that are loaded into the user interface.
	 */
	public List<Metodo> getMetodos() {
		return metodos;
	}

	/**
	 * 
	 * @param metodos
	 *            : List of methods that are loaded into the user interface.
	 */
	public void setMetodos(List<Metodo> metodos) {
		this.metodos = metodos;
	}

	/**
	 * 
	 * @return paginador: Allows management of the pagination of the list of
	 *         menus in record method.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * 
	 * @param paginador
	 *            : Allows management of the pagination of the list of menus in
	 *            record method.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * 
	 * @return paginadorMenus: Allows management of the pagination of the list
	 *         of menus in record method.
	 */
	public Paginador getPaginadorMenus() {
		return paginadorMenus;
	}

	/**
	 * 
	 * @param paginadorMenus
	 *            : Allows management of the pagination of the list of menus in
	 *            record method.
	 */
	public void setPaginadorMenus(Paginador paginadorMenus) {
		this.paginadorMenus = paginadorMenus;
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
	 * @return nombreBuscar: method name to search.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : method name to search.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultarMetodos: method that consultation methods and load the
	 *         template with the information found.
	 * 
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarMetodos();
	}

	/**
	 * Provides access existing methods in the database.
	 * 
	 * @modify 10/10/2012 Adonay.Mantilla
	 * 
	 * @return gesMetodo: redirects to the Manage method
	 */
	public String consultarMetodos() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String mensajeBusqueda = "";
		metodos = new ArrayList<Metodo>();
		try {
			paginador.paginar(metodoDao.cantidadMetodos(this.nombreBuscar));
			this.metodos = metodoDao.consultarMetodos(paginador.getInicio(),
					paginador.getRango(), this.nombreBuscar);
			if ((metodos == null || metodos.size() <= 0)
					&& (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			} else if (metodos == null || metodos.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("method_label_s"),
								bundle.getString("label_proceso") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMetodo";
	}

	/**
	 * Method to load the template to add or edit a method
	 * 
	 * @param metodo
	 *            : Object of method you want to register or edit
	 * @return regMetodo: page redirects to record the method, in which you can
	 *         add or edit a method
	 */
	public String registrarMetodo(Metodo metodo) {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		try {
			this.menusSelected = new ArrayList<Menu>();
			if (metodo != null) {
				this.menusSelected = menuDao.consultarMenusMetodo(metodo
						.getId());
				for (Menu menu : this.menusSelected) {
					menuAction.convertirNombreMenuDescript(menu);
				}
				this.metodo = metodo;
			} else {
				this.metodo = new Metodo();
			}
			limpiarMenusDisponibles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMetodo";
	}

	/**
	 * Allows save or edit a method.
	 * 
	 * @return: return to a page to register or manage methods, as it happened.
	 */
	public String guardarMetodo() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageKey = "message_registro_modificar";
		try {
			userTransaction.begin();
			metodo.setUserName(identity.getUserName());
			if (metodo.getId() != 0) {
				metodoDao.editarMetodo(metodo);
				guardarEditarMetodoMenu();
			} else {
				metodo.setFechaCreacion(new Date());
				metodoDao.guardarMetodo(metodo);
				for (Menu menu : this.menusSelected) {
					guardarMetodoMenu(menu);
				}
				messageKey = "message_registro_guardar";
			}
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageKey),
							metodo.getNombre()));
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
			return "regMetodo";
		}
		return consultarMetodos();
	}

	/**
	 * Allows save the method selected from the menus.
	 * 
	 * @throws Exception
	 */
	private void guardarEditarMetodoMenu() throws Exception {
		List<Menu> menusSelectedTemp = menuDao.consultarMenusMetodo(metodo
				.getId());
		if (menusSelectedTemp != null && this.menusSelected != null) {
			List<Integer> currentIds = new ArrayList<Integer>();
			List<Integer> newsIds = new ArrayList<Integer>();
			/* Lists are filled with only the ids */
			for (Menu menu : menusSelectedTemp) {
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
							this.metodo, menu);
					metodoMenuDao.eliminarMetodoMenu(metodoMenu);
				} else if (Constantes.QUERY_INSERT.equals(action)) {
					guardarMetodoMenu(menu);
				}
			}
		}
	}

	/**
	 * Allows save the menus related to the method.
	 * 
	 * @param menu
	 *            : Menu related to the method.
	 * 
	 * @throws Exception
	 */
	private void guardarMetodoMenu(Menu menu) throws Exception {
		MetodoMenu metodoMenu = new MetodoMenu();
		metodoMenu.setMetodo(metodo);
		metodoMenu.setMenu(menu);
		metodoMenu.setFechaCreacion(new Date());
		metodoMenu.setUserName(identity.getUserName());
		metodoMenuDao.guardarMetodoMenu(metodoMenu);
	}

	/**
	 * Method that cleans the list of available menus.
	 */
	public void limpiarMenusDisponibles() {
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		paginadorMenus = new Paginador();
		menuAction.setNombreBuscar(null);
		consultarMenusDisponibles();
	}

	/**
	 * Provides access menus available.
	 */
	public void consultarMenusDisponibles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		GestionarMenuAction menuAction = ControladorContexto
				.getContextBean(GestionarMenuAction.class);
		String mensajeBusqueda = "";
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		this.menus = new ArrayList<Menu>();
		try {
			if (menuAction.getNombreBuscar() != null
					&& !"".equals(menuAction.getNombreBuscar())) {
				unionMensajesBusqueda.append(bundle.getString("label_nombre")
						+ ": " + '"' + menuAction.getNombreBuscar() + '"');
				List<Menu> listaTdosMenus = menuDao
						.consultarTodosMenusAction(this.menusSelected);
				List<Menu> listaMenusDatos = menuAction
						.filtrarMenusPorNombre(listaTdosMenus);
				long cantidadMenus = (long) listaMenusDatos.size();
				paginadorMenus.paginarRangoDefinido(cantidadMenus, 5);
				int totalReg = paginadorMenus.getRango();
				int inicio = paginadorMenus.getInicio();
				int rango = inicio + totalReg;
				if (listaMenusDatos.size() < rango) {
					rango = listaMenusDatos.size();
				}
				this.menus = listaMenusDatos.subList(inicio, rango);
			} else {
				Long cantidadMenus = menuDao
						.cantidadMenusAction(this.menusSelected);
				paginadorMenus.paginarRangoDefinido(cantidadMenus, 5);
				this.menus = menuDao.consultarMenusAction(
						paginadorMenus.getInicio(), paginadorMenus.getRango(),
						this.menusSelected);
			}
			for (Menu menu : this.menus) {
				menuAction.convertirNombreMenuDescript(menu);
			}
			if ((this.menus == null || this.menus.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);

			} else if (this.menus == null || this.menus.size() <= 0) {
				mensajeBusqueda = bundle
						.getString("message_no_existen_registros");
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeguridad.getString("menu_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusquedaPopUp(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Added or removed from the list of selected menus.
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
				List<Menu> menusSelectedTemp = menuDao
						.consultarTodosMenusAction(this.menusSelected);
				if (menuAction.getNombreBuscar() != null
						&& !"".equals(menuAction.getNombreBuscar())) {
					menusSelectedTemp = menuAction
							.filtrarMenusPorNombre(menusSelectedTemp);
				}
				this.menusSelected.addAll(menusSelectedTemp);
				for (Menu menu : this.menusSelected) {
					menuAction.convertirNombreMenuDescript(menu);
				}
			} else {
				this.menusSelected = new ArrayList<Menu>();
			}
			consultarMenusDisponibles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Adds or removes a menu from the list of selected menus.
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
			for (Menu menuSel : this.menusSelected) {
				if (menuAva.getId() == menuSel.getId()) {
					this.menusSelected.remove(menuSel);
					break;
				}
			}
		}
		consultarMenusDisponibles();
	}

	/**
	 * Valid if the method name exists in the database and if you have related
	 * menus.
	 * 
	 * @param context
	 *            : FacesContext variable type for message handling.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Validate component value.
	 */
	public void validarMetodo(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		String clientId = toValidate.getClientId(context);
		String name = (String) value;
		try {
			Metodo metodoAux = metodoDao.nombreMetodoExiste(name,
					this.metodo.getId());
			if (metodoAux != null) {
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
	 *            : FacesContext variable type for message handling.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Validate component value.
	 */
	public void validarNombreMetodo(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String clientId = toValidate.getClientId(context);
		String nombreMetodo = (String) value;
		try {
			Metodo metodoAux = metodoDao.nombreActionExiste(nombreMetodo,
					this.metodo.getId());
			if (metodoAux != null) {
				context.addMessage(
						toValidate.getClientId(context),
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString("message_ya_existe_verifique"), null));
			}

			if (!EncodeFilter.validarXSS(nombreMetodo, clientId, null)) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Deletes a method from the database, which is not associated with a role.
	 * 
	 * @modify 22/12/2014 Cristhian.Pico
	 * 
	 * @return consultarMetodos(): Consult the methods of the database and
	 *         return to the management methods.
	 */
	public String eliminarMetodo() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (this.metodo != null) {
				boolean result1 = rolMetodoDao.relacionRolMetodo(this.metodo
						.getId());
				if (result1) {
					ControladorContexto
							.mensajeError(MessageFormat.format(
									bundleSeguridad
											.getString("method_message_not_delete_rol"),
									this.metodo.getNombre()));
				} else {
					userTransaction.begin();
					List<MetodoMenu> metodosMenu = metodoMenuDao
							.consultarTodosMetodoMenu(this.metodo);
					for (MetodoMenu metodoMenu : metodosMenu) {
						metodoMenuDao.eliminarMetodoMenu(metodoMenu);
					}
					this.metodo.setFechaFinVigencia(new Date());
					metodoDao.editarMetodo(this.metodo);
					userTransaction.commit();
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle
									.getString("message_registro_eliminar"),
									metodo.getNombre()));
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
		return consultarMetodos();
	}
}