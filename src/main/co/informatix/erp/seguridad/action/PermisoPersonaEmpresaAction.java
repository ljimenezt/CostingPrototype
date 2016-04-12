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

import co.informatix.erp.organizaciones.action.EmpresaAction;
import co.informatix.erp.organizaciones.dao.EmpresaDao;
import co.informatix.erp.organizaciones.dao.HaciendaDao;
import co.informatix.erp.organizaciones.dao.SucursalDao;
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.organizaciones.entities.Hacienda;
import co.informatix.erp.organizaciones.entities.Sucursal;
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
	private SucursalDao sucursalDao;
	@EJB
	private HaciendaDao haciendaDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private Paginador pagination = new Paginador();
	private PermisoPersonaEmpresa permisoPersonaEmpresa;
	private Persona persona;
	private Empresa empresaSeleccionada;

	private List<PermisoPersonaEmpresa> listaPermisoPersonaEmpresa;
	private List<PermisoPersonaEmpresa> listaPermisoPersonaEmpresaTemp;
	private List<Empresa> empresas;

	private List<SelectItem> itemsSucursalEmpresa;
	private List<SelectItem> itemsHaciendasEmpresa;
	private List<SelectItem> itemsSucursales;
	private List<SelectItem> itemsHaciendas;

	private String empresaBuscar;
	private String empresaBuscarGestion;
	private String vigencia = Constantes.SI;
	private int idSucursal;
	private int idHacienda;
	private int idSucursalBuscar;
	private int idHaciendaBuscar;

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
	 * @return listaPermisoPersonaEmpresa: list of companies to which the person
	 *         has access.
	 */
	public List<PermisoPersonaEmpresa> getListaPermisoPersonaEmpresa() {
		return listaPermisoPersonaEmpresa;
	}

	/**
	 * @param listaPermisoPersonaEmpresa
	 *            : list of companies to which the person has access.
	 */
	public void setListaPermisoPersonaEmpresa(
			List<PermisoPersonaEmpresa> listaPermisoPersonaEmpresa) {
		this.listaPermisoPersonaEmpresa = listaPermisoPersonaEmpresa;
	}

	/**
	 * @return empresaBuscar: word by which companies want to search.
	 */
	public String getEmpresaBuscar() {
		return empresaBuscar;
	}

	/**
	 * @param empresaBuscar
	 *            : word by which companies want to search.
	 */
	public void setEmpresaBuscar(String empresaBuscar) {
		this.empresaBuscar = empresaBuscar;
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
	 * @return listaPermisoPersonaEmpresaTemp: Temporary list of the selected to
	 *         give businesses access to a person.
	 */
	public List<PermisoPersonaEmpresa> getListaPermisoPersonaEmpresaTemp() {
		return listaPermisoPersonaEmpresaTemp;
	}

	/**
	 * @param listaPermisoPersonaEmpresaTemp
	 *            : Temporary list of the selected to give businesses access to
	 *            a person.
	 */
	public void setListaPermisoPersonaEmpresaTemp(
			List<PermisoPersonaEmpresa> listaPermisoPersonaEmpresaTemp) {
		this.listaPermisoPersonaEmpresaTemp = listaPermisoPersonaEmpresaTemp;
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
	 * @return itemsSucursalEmpresa: Items branch of the company that are shown
	 *         in the combo of the user interface.
	 */
	public List<SelectItem> getItemsSucursalEmpresa() {
		return itemsSucursalEmpresa;
	}

	/**
	 * @return itemsHaciendasEmpresa: items from the farm of the company is
	 *         selected to associate permissions to the person.
	 */
	public List<SelectItem> getItemsHaciendasEmpresa() {
		return itemsHaciendasEmpresa;
	}

	/**
	 * @return itemsHaciendas: farms items shown in the search combo in the user
	 *         interface.
	 */
	public List<SelectItem> getItemsHaciendas() {
		return itemsHaciendas;
	}

	/**
	 * @return itemsSucursales: branch items shown in the search combo in the
	 *         user interface.
	 */
	public List<SelectItem> getItemsSucursales() {
		return itemsSucursales;
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
	 * @return empresaBuscarGestion: variable which the company seeks from
	 *         management of permits.
	 */
	public String getEmpresaBuscarGestion() {
		return empresaBuscarGestion;
	}

	/**
	 * @param empresaBuscarGestion
	 *            : variable which the company seeks from management of permits.
	 */
	public void setEmpresaBuscarGestion(String empresaBuscarGestion) {
		this.empresaBuscarGestion = empresaBuscarGestion;
	}

	/**
	 * @return idSucursal: id of the branch that selects the user permissions
	 *         associated with the company.
	 */
	public int getIdSucursal() {
		return idSucursal;
	}

	/**
	 * @param idSucursal
	 *            : id of the branch that selects the user permissions
	 *            associated with the company.
	 */
	public void setIdSucursal(int idSucursal) {
		this.idSucursal = idSucursal;
	}

	/**
	 * @return idHacienda: id of the farm that selects the user permissions to
	 *         associate with the company.
	 */
	public int getIdHacienda() {
		return idHacienda;
	}

	/**
	 * @param idHacienda
	 *            : id of the farm that selects the user permissions to
	 *            associate with the company.
	 */
	public void setIdHacienda(int idHacienda) {
		this.idHacienda = idHacienda;
	}

	/**
	 * @return idSucursalBuscar: id of the branch in which the company wants to
	 *         consult partnered with permissions to the person.
	 */
	public int getIdSucursalBuscar() {
		return idSucursalBuscar;
	}

	/**
	 * @param idSucursalBuscar
	 *            : id of the branch in which the company wants to consult
	 *            partnered with permissions to the person.
	 */
	public void setIdSucursalBuscar(int idSucursalBuscar) {
		this.idSucursalBuscar = idSucursalBuscar;
	}

	/**
	 * @return idHaciendaBuscar: id of the farm for which you want to consult
	 *         the company that was associated with the individual permissions.
	 */
	public int getIdHaciendaBuscar() {
		return idHaciendaBuscar;
	}

	/**
	 * @param idHaciendaBuscar
	 *            : id of the farm for which you want to consult the company
	 *            that was associated with the individual permissions.
	 */
	public void setIdHaciendaBuscar(int idHaciendaBuscar) {
		this.idHaciendaBuscar = idHaciendaBuscar;
	}

	/**
	 * @return empresaSeleccionada: company is selected to associate
	 *         permissions.
	 */
	public Empresa getEmpresaSeleccionada() {
		return empresaSeleccionada;
	}

	/**
	 * @param empresaSeleccionada
	 *            : company is selected to associate permissions.
	 */
	public void setEmpresaSeleccionada(Empresa empresaSeleccionada) {
		this.empresaSeleccionada = empresaSeleccionada;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of permissions person company.
	 * 
	 * @return consultarPermisoPersonaEmpresa: method consulting firms with
	 *         access rights of the person and load management template.
	 */
	public String inicializarBusqueda() {
		this.empresaBuscarGestion = "";
		this.idSucursalBuscar = 0;
		this.idHaciendaBuscar = 0;
		return consultarPermisoPersonaEmpresa();
	}

	/**
	 * Browse companies that have access to the person.
	 * 
	 * @return gesPermisoPersonaEmpresa: navigation rule load the template for
	 *         managing access permissions per person.
	 */
	public String consultarPermisoPersonaEmpresa() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		listaPermisoPersonaEmpresa = new ArrayList<PermisoPersonaEmpresa>();
		List<PermisoPersonaEmpresa> listPermisoPersonaEmpresaTemp = new ArrayList<PermisoPersonaEmpresa>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		String panelId = "frmGestionarPermisosEmpresa:panelEmpresaPermiso";
		try {
			if (persona != null) {
				busquedaAvanzada(consulta, parametros, bundle,
						unionMensajesBusqueda);
				pagination.paginar(permisoPersonaEmpresaDao
						.cantidadEmpresasAccesoPersona(consulta, parametros));
				listPermisoPersonaEmpresaTemp = permisoPersonaEmpresaDao
						.consultarEmpresasAccesoPersona(consulta, parametros,
								pagination.getInicio(), pagination.getRango());
				if ((listPermisoPersonaEmpresaTemp == null || listPermisoPersonaEmpresaTemp
						.size() <= 0)
						&& !"".equals(unionMensajesBusqueda.toString())) {
					mensajeBusqueda = MessageFormat
							.format(bundle
									.getString("message_no_existen_registros_criterio_busqueda"),
									unionMensajesBusqueda);
				} else if (listPermisoPersonaEmpresaTemp == null
						|| listPermisoPersonaEmpresaTemp.size() <= 0) {
					ControladorContexto.mensajeInformacion(panelId,
							bundle.getString("message_no_existen_registros"));
				} else if (!"".equals(unionMensajesBusqueda.toString())) {
					mensajeBusqueda = MessageFormat
							.format(bundle
									.getString("message_existen_registros_criterio_busqueda"),
									bundleSeguridad
											.getString("person_permission_company_label"),
									unionMensajesBusqueda);
				}
				if (!"".equals(mensajeBusqueda)) {
					ControladorContexto.mensajeInformacion(panelId,
							mensajeBusqueda);
				}
				detallesPermisoPersonaEmpresa(listPermisoPersonaEmpresaTemp);
				cargarCombos();
			} else {
				ControladorContexto
						.mensajeError(bundleSeguridad
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
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("messageOrganizations");
		boolean seAgregoMens = false;
		String comaEspacio = ", ";

		consult.append("WHERE ppe.persona.id=:idPersona ");
		SelectItem itemPer = new SelectItem(persona.getId(), "idPersona");
		parameters.add(itemPer);

		if (Constantes.NOT.equals(vigencia)) {
			consult.append("AND (ppe.fechaFinVigencia IS NOT NULL ");
			consult.append("AND ppe.fechaFinVigencia <= :fechaActual) ");
		} else if (Constantes.SI.equals(vigencia)) {
			consult.append("AND (ppe.fechaFinVigencia IS NULL ");
			consult.append("OR ppe.fechaFinVigencia > :fechaActual) ");
		}
		SelectItem itemVig = new SelectItem(new Date(), "fechaActual");
		parameters.add(itemVig);

		if (idSucursalBuscar != 0) {
			SelectItem item = new SelectItem(idSucursalBuscar, "idSucursal");
			consult.append("AND ppe.sucursal.id=:idSucursal ");
			parameters.add(item);
			String nombreSucursal = (String) ValidacionesAction.getLabel(
					itemsSucursales, idSucursalBuscar);
			unionMessagesSearch.append(bundleOrg.getString("branch_label")
					+ ": " + '"' + nombreSucursal + '"');
			seAgregoMens = true;
		}
		if (idHaciendaBuscar != 0) {
			SelectItem item = new SelectItem(idHaciendaBuscar, "idHacienda");
			consult.append("AND ppe.hacienda.id=:idHacienda ");
			parameters.add(item);
			String nombreHacienda = (String) ValidacionesAction.getLabel(
					itemsHaciendas, idHaciendaBuscar);
			unionMessagesSearch.append((seAgregoMens ? comaEspacio : "")
					+ bundleOrg.getString("farm_label") + ": " + '"'
					+ nombreHacienda + '"');
			seAgregoMens = true;
		}
		if (this.empresaBuscarGestion != null
				&& !"".equals(this.empresaBuscarGestion)) {
			SelectItem item = new SelectItem("%" + empresaBuscarGestion + "%",
					"keyword");
			consult.append("AND UPPER(ppe.empresa.nombre) LIKE UPPER(:keyword) ");
			parameters.add(item);
			unionMessagesSearch.append((seAgregoMens ? comaEspacio : "")
					+ bundle.getString("label_name") + ": " + '"'
					+ this.empresaBuscarGestion + '"');
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
	private void detallesPermisoPersonaEmpresa(
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
				listaPermisoPersonaEmpresa.add(permisoPersonaEmpresa);
			}
		}
	}

	/**
	 * A new object instance to associate the individual companies.
	 * 
	 * @return navigation rule that loads the template to permit registration of
	 *         the individual companies.
	 */
	public String nuevoPermisoPersonaEmpresa() {
		try {
			empresas = new ArrayList<Empresa>();
			listaPermisoPersonaEmpresaTemp = new ArrayList<PermisoPersonaEmpresa>();
			cargarCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPermisoPersonaEmpresa";
	}

	/**
	 * This method looks for a list of companies search parameter system (ILS /
	 * name).
	 */
	public void buscarEmpresas() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			empresas = new ArrayList<Empresa>();
			empresas = empresaDao.buscarEmpresaXNombreONit(empresaBuscar);
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
	 * @param vigente
	 *            : value that indicates whether the term begins or ends
	 * @return consultation existing records and returns to the management of
	 *         the company permits.
	 */
	public String cambiarVigenciaPermisosEmpresa(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";
		try {
			if (permisoPersonaEmpresa != null) {
				validarNulos(permisoPersonaEmpresa);
				permisoPersonaEmpresa.setUserName(identity.getUserName());
				if (vigente) {
					permisoPersonaEmpresa.setPredeterminado(false);
					permisoPersonaEmpresa.setFechaFinVigencia(new Date());
				} else {
					mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
					permisoPersonaEmpresa.setFechaFinVigencia(null);
				}
				permisoPersonaEmpresaDao
						.modificarPermisoPersonaEmpresa(permisoPersonaEmpresa);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(mensajeCambioVigencia)
								+ ": {0}", this.permisoPersonaEmpresa
								.getEmpresa().getNombre()));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarPermisoPersonaEmpresa();
	}

	/**
	 * Allows to validate the records that the object instance is beginning but
	 * his id is 0 or null, if null if so assigned to permisoPersonaEmpresa.
	 * 
	 * @param permisoPersonaEmpresaVal
	 *            : null object to validate.
	 */
	public void validarNulos(PermisoPersonaEmpresa permisoPersonaEmpresaVal) {
		if (permisoPersonaEmpresaVal != null) {
			Sucursal sucursal = permisoPersonaEmpresaVal.getSucursal();
			Hacienda hacienda = permisoPersonaEmpresaVal.getHacienda();
			if (sucursal != null
					&& (sucursal.getId() == null || (sucursal.getId() != null && sucursal
							.getId() == 0))) {
				permisoPersonaEmpresaVal.setSucursal(null);
			}
			if (hacienda != null && hacienda.getId() == 0) {
				permisoPersonaEmpresaVal.setHacienda(null);
			}
		}
	}

	/**
	 * This method saves the information of the companies with the person and
	 * the assigned permissions.
	 * 
	 * 
	 * @return consultarPermisoPersonaEmpresa: method consulting firms with
	 *         access rights of the person and load management template.
	 */
	public String guardarPermisoPersonaEmpresa() {
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (listaPermisoPersonaEmpresaTemp != null) {
				this.userTransaction.begin();
				String nombrePersona = persona.getNombres() + " "
						+ persona.getApellidos();
				StringBuilder msgEmpresasHaciendas = new StringBuilder();
				String mensajeHaciendaEmpresa = "person_permission_company_message_associate_company_farm";
				for (PermisoPersonaEmpresa permisoPersonaEmpresaAdd : listaPermisoPersonaEmpresaTemp) {
					boolean predeterminado = permisoPersonaEmpresaAdd
							.isPredeterminado();
					PermisoPersonaEmpresa permisoPredeterminado = permisoPersonaEmpresaDao
							.consultarExistePredeterminado(persona
									.getDocumento());
					if (predeterminado && permisoPredeterminado != null) {
						permisoPredeterminado.setPredeterminado(false);
						permisoPersonaEmpresaDao
								.modificarPermisoPersonaEmpresa(permisoPredeterminado);
					}
					validarNulos(permisoPersonaEmpresaAdd);
					permisoPersonaEmpresaAdd.setFechaCreacion(new Date());
					permisoPersonaEmpresaAdd
							.setUserName(identity.getUserName());
					permisoPersonaEmpresaDao
							.guardarPermisoPersonaEmpresa(permisoPersonaEmpresaAdd);
					if (msgEmpresasHaciendas.length() > 1) {
						msgEmpresasHaciendas.append(", ");
					}
					String nitEmpresa = permisoPersonaEmpresaAdd.getEmpresa()
							.getNit();
					String nombreHacienda = permisoPersonaEmpresaAdd
							.getHacienda().getNombre();
					String msg = MessageFormat.format(
							bundleSeguridad.getString(mensajeHaciendaEmpresa),
							nitEmpresa, nombreHacienda);
					msgEmpresasHaciendas.append(msg);
				}
				this.userTransaction.commit();
				String format = MessageFormat
						.format(bundleSeguridad
								.getString("person_permission_company_message_associate_company"),
								nombrePersona, msgEmpresasHaciendas);
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
		return consultarPermisoPersonaEmpresa();
	}

	/**
	 * Loads charges combo items to relate to a person in a company
	 * 
	 * @throws Exception
	 */
	private void cargarCombos() throws Exception {
		itemsSucursales = new ArrayList<SelectItem>();
		List<Sucursal> sucursalesVigentes = sucursalDao
				.consultarSucursalesVigentes();
		if (sucursalesVigentes != null) {
			for (Sucursal sucursal : sucursalesVigentes) {
				itemsSucursales.add(new SelectItem(sucursal.getId(), sucursal
						.getNombre()));
			}
		}
		itemsHaciendas = new ArrayList<SelectItem>();
		List<Hacienda> haciendasVigentes = haciendaDao
				.consultarHaciendasVigentes();
		if (haciendasVigentes != null) {
			for (Hacienda hacienda : haciendasVigentes) {
				itemsHaciendas.add(new SelectItem(hacienda.getId(), hacienda
						.getNombre()));
			}
		}
	}

	/**
	 * Allows to control the addition and subtraction of companies in the list
	 * of companies with access rights of the individual.
	 * 
	 * @param opcion
	 *            : Option to know that is made in the list: add, delete, or
	 *            create a new one.
	 * @param objPermisoPersonaEmpresa
	 *            : Object of company permission person you want to remove from
	 *            the list.
	 */
	public void controladorListaPermisos(String opcion,
			PermisoPersonaEmpresa objPermisoPersonaEmpresa) {
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		try {
			if (Constantes.NEW_PERMISO.equals(opcion)) {
				this.permisoPersonaEmpresa = new PermisoPersonaEmpresa();
				this.permisoPersonaEmpresa.setSucursal(new Sucursal());
				this.permisoPersonaEmpresa.setHacienda(new Hacienda());
				this.permisoPersonaEmpresa.setPersona(persona);
				this.permisoPersonaEmpresa.setEmpresa(empresaSeleccionada);
				cargarCombosEmpresa(empresaSeleccionada);
				idSucursal = 0;
				idHacienda = 0;
			} else if (Constantes.ADD_PERMISO.equals(opcion)) {
				String mensaje = "person_permission_company_label_associated";
				if (idSucursal != 0) {
					mensaje = "person_permission_company_label_associated_branch";
					Sucursal sucursal = sucursalDao
							.consultarSucursal(idSucursal);
					permisoPersonaEmpresa.setSucursal(sucursal);
				} else if (idHacienda != 0) {
					mensaje = "person_permission_company_label_associated_farm";
					Hacienda hacienda = haciendaDao
							.consultarHaciendaPorId(idHacienda);
					permisoPersonaEmpresa.setHacienda(hacienda);
				}
				if (!validarExistePermisoAsociado(this.empresaSeleccionada,
						permisoPersonaEmpresa.getHacienda(),
						permisoPersonaEmpresa.getSucursal())) {
					listaPermisoPersonaEmpresaTemp.add(permisoPersonaEmpresa);
				} else {
					ControladorContexto.mensajeError("popupForm:mensajesPopup",
							bundleSeguridad.getString(mensaje));
				}
			} else if (Constantes.BORRAR_PERMISO.equals(opcion)) {
				listaPermisoPersonaEmpresaTemp.remove(objPermisoPersonaEmpresa);
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
	 * @param empresaSel
	 *            : company selected to associate permissions.
	 * @param hacienda
	 *            : Selected finance company.
	 * @param sucursal
	 *            : Selected branch of the company.
	 * @return boolean to true if it is already associated or false otherwise.
	 */
	private boolean validarExistePermisoAsociado(Empresa empresaSel,
			Hacienda hacienda, Sucursal sucursal) {
		if (listaPermisoPersonaEmpresaTemp != null) {
			for (PermisoPersonaEmpresa permPersonaEmp : listaPermisoPersonaEmpresaTemp) {
				Empresa empresaList = permPersonaEmp.getEmpresa();
				Hacienda haciendaList = permPersonaEmp.getHacienda();
				Sucursal sucursalList = permPersonaEmp.getSucursal();
				if (sucursal != null && sucursal.getId() != null
						&& sucursalList != null
						&& sucursalList.equals(sucursal)
						&& empresaList.equals(empresaSel)) {
					return true;

				}
				if (hacienda != null && hacienda.getId() != 0
						&& haciendaList != null
						&& haciendaList.equals(hacienda)
						&& empresaList.equals(empresaSel)) {
					return true;
				}
				if ((hacienda == null || hacienda.getId() == 0)
						&& (sucursal == null || sucursal.getId() == null || sucursal
								.getId() == 0)
						&& empresaList.equals(empresaSel)) {
					return true;
				}
			}
		}
		if (listaPermisoPersonaEmpresa != null) {
			for (PermisoPersonaEmpresa permisoPerEmp : listaPermisoPersonaEmpresa) {
				Date fechaFinVigencia = permisoPerEmp.getFechaFinVigencia();
				Empresa empresaList = permisoPerEmp.getEmpresa();
				Sucursal sucursalList = permisoPerEmp.getSucursal();
				Hacienda haciendaList = permisoPerEmp.getHacienda();
				if (sucursal != null
						&& sucursal.getId() != null
						&& sucursalList != null
						&& sucursalList.equals(sucursal)
						&& empresaList.equals(empresaSel)
						&& (fechaFinVigencia == null || fechaFinVigencia
								.after(new Date()))) {
					return true;
				}
				if (hacienda != null
						&& hacienda.getId() != 0
						&& haciendaList != null
						&& haciendaList.equals(hacienda)
						&& empresaList.equals(empresaSel)
						&& (fechaFinVigencia == null || fechaFinVigencia
								.after(new Date()))) {
					return true;
				}
				if ((hacienda == null || hacienda.getId() == 0)
						&& (sucursal == null || sucursal.getId() == null || sucursal
								.getId() == 0)
						&& empresaList.equals(empresaSel)
						&& (fechaFinVigencia == null || fechaFinVigencia
								.after(new Date()))) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Method of uploading the information combo branches and farm combo in the
	 * user interface.
	 * 
	 * @param empresaSelect
	 *            : Selected business listing to which you are charged combo
	 *            branches and farms.
	 * @throws Exception
	 */
	private void cargarCombosEmpresa(Empresa empresaSelect) throws Exception {
		itemsSucursalEmpresa = new ArrayList<SelectItem>();
		itemsHaciendasEmpresa = new ArrayList<SelectItem>();
		if (empresaSelect != null) {
			List<Sucursal> sucursalesEmpresa = sucursalDao
					.consultarSucursalesXEmpresa(empresaSelect.getId());
			if (sucursalesEmpresa != null) {
				for (Sucursal sucursal : sucursalesEmpresa) {
					itemsSucursalEmpresa.add(new SelectItem(sucursal.getId(),
							sucursal.getNombre()));
				}
			}
			List<Hacienda> haciendasPorIdEmpresa = haciendaDao
					.buscarHaciendasPorIdEmpresa(empresaSelect.getId());
			if (haciendasPorIdEmpresa != null) {
				for (Hacienda hacienda : haciendasPorIdEmpresa) {
					itemsHaciendasEmpresa.add(new SelectItem(hacienda.getId(),
							hacienda.getNombre()));
				}
			}
		}
	}

	/**
	 * Method for validating the selection of at least one company, to be
	 * associate access permissions to the person.
	 */
	public void validarPermisosEmpresasSeleccionado() {
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		if (listaPermisoPersonaEmpresaTemp == null
				|| listaPermisoPersonaEmpresaTemp.size() <= 0) {
			ControladorContexto
					.mensajeError(
							"frmAsociarPermisos:extDTPermisoPersonaEmpresa",
							bundleSeguridad
									.getString("person_permission_company_message_validate_select_company"));
		}
	}
}
