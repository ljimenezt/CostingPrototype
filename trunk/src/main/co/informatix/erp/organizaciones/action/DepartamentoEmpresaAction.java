package co.informatix.erp.organizaciones.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.organizaciones.dao.DepartamentoEmpresaDao;
import co.informatix.erp.organizaciones.dao.DepartamentoEmpresaTipoCargoDao;
import co.informatix.erp.organizaciones.entities.DepartamentoEmpresa;
import co.informatix.erp.organizaciones.entities.DepartamentoEmpresaTipoCargo;
import co.informatix.erp.organizaciones.entities.DepartamentoEmpresaTipoCargoPK;
import co.informatix.erp.recursosHumanos.dao.TipoCargoDao;
import co.informatix.erp.recursosHumanos.entities.TipoCargo;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utils.ValidacionesAction.DatosGuardar;
import co.informatix.security.action.IdentityAction;

/**
 * Class that performs business logic of Enterprise Department and screen events
 * 
 * @author Luis.Ruiz
 * @modify 12/03/2012 marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DepartamentoEmpresaAction implements Serializable {

	@EJB
	private DepartamentoEmpresaDao departamentoEmpresaDao;
	@EJB
	private DepartamentoEmpresaTipoCargoDao depEmpresaTipoCargoDao;
	@EJB
	private TipoCargoDao tipoCargoDao;
	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private Paginador pagination = new Paginador();
	private DepartamentoEmpresa departamentoEmpresa;

	private List<DepartamentoEmpresa> listaDepartamentoEmpresas;
	private List<TipoCargo> listaTipoCargos;
	private List<TipoCargo> listaTipoCargosSeleccionados;
	private List<TipoCargo> listaTipoCargosIniciales;
	private List<SelectItem> opcionesVigencia;
	private List<SelectItem> opcionesDepartamentos;
	private Map<Integer, DepartamentoEmpresa> departamentos;

	private String consultarVigentes = Constantes.SI;
	private String mensajeMiga;
	private String nombreBuscar = "";
	private int idDepartamentoPredeterminado;

	/**
	 * @return List options screen departments to predetermine department
	 */
	public List<SelectItem> getOpcionesDepartamentos() {
		return opcionesDepartamentos;
	}

	/**
	 * @return Object department of the company that is saved
	 */
	public DepartamentoEmpresa getDepartamentoEmpresa() {
		return departamentoEmpresa;
	}

	/**
	 * @param departamentoEmpresa
	 *            Object department of the company that is saved
	 */
	public void setDepartamentoEmpresa(DepartamentoEmpresa departamentoEmpresa) {
		this.departamentoEmpresa = departamentoEmpresa;
	}

	/**
	 * @return pagination: gets the instance of the pager to the list of
	 *         departments within the company on the interface.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : sets the instance of the pager to the list of departments
	 *            within the company on the interface.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return List of departments of the company management screen
	 */
	public List<DepartamentoEmpresa> getListaDepartamentoEmpresas() {
		return listaDepartamentoEmpresas;
	}

	/**
	 * @param listaDepartamentoEmpresas
	 *            List of departments of the company management screen
	 */
	public void setListaDepartamentoEmpresas(
			List<DepartamentoEmpresa> listaDepartamentoEmpresas) {
		this.listaDepartamentoEmpresas = listaDepartamentoEmpresas;
	}

	/**
	 * @return consultarVigentes: gets the value for the management of current
	 *         records
	 */
	public String getConsultarVigentes() {
		return consultarVigentes;
	}

	/**
	 * @param consultarVigentes
	 *            :gets the value for the management of current records
	 */
	public void setConsultarVigentes(String consultarVigentes) {
		this.consultarVigentes = consultarVigentes;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on whether you create or modify a record
	 * 
	 * @return message crumb of bread in the template
	 */
	public String getMensajeMiga() {
		return mensajeMiga;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on whether you create or modify a record
	 * 
	 * @param mensajeMiga
	 *            message crumb of bread in the template
	 */
	public void setMensajeMiga(String mensajeMiga) {
		this.mensajeMiga = mensajeMiga;
	}

	/**
	 * @return opcionesVigencia: gets the list of valid options that can force
	 *         or not in force.
	 */
	public List<SelectItem> getOpcionesVigencia() {
		return opcionesVigencia;
	}

	/**
	 * @return listaTipoCargos: list of the types of positions that exist in the
	 *         system
	 */
	public List<TipoCargo> getListaTipoCargos() {
		return listaTipoCargos;
	}

	/**
	 * @param listaTipoCargos
	 *            :list of the types of positions that exist in the system
	 */
	public void setListaTipoCargos(List<TipoCargo> listaTipoCargos) {
		this.listaTipoCargos = listaTipoCargos;
	}

	/**
	 * @return listaTipoCargosSeleccionados: list of selected types of charges
	 *         for a kind of department of the company
	 */
	public List<TipoCargo> getListaTipoCargosSeleccionados() {
		return listaTipoCargosSeleccionados;
	}

	/**
	 * @param listaTipoCargosSeleccionados
	 *            : list of selected types of charges for a kind of department
	 *            of the company
	 */
	public void setListaTipoCargosSeleccionados(
			List<TipoCargo> listaTipoCargosSeleccionados) {
		this.listaTipoCargosSeleccionados = listaTipoCargosSeleccionados;
	}

	/**
	 * @return idDepartamentoPredeterminado: default department identifier
	 *         currently
	 */
	public int getIdDepartamentoPredeterminado() {
		return idDepartamentoPredeterminado;
	}

	/**
	 * @param idDepartamentoPredeterminado
	 *            :default department identifier currently
	 */
	public void setIdDepartamentoPredeterminado(int idDepartamentoPredeterminado) {
		this.idDepartamentoPredeterminado = idDepartamentoPredeterminado;
	}

	/**
	 * @return nombreBuscar: name to search the company department type
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Name to search the company department type
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the object departamentoEmpresa to create or edit an
	 * existing record, charging turn the record template.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param departamentoEmpresa
	 *            : object department of the company that is loaded into the
	 *            template editing in case.
	 * @return regDepartamentoEmpresa: navigation rule that addresses the
	 *         department registration screen Business.
	 */
	public String registrarDepartamentoEmpresa(
			DepartamentoEmpresa departamentoEmpresa) {
		try {
			this.listaTipoCargos = new ArrayList<TipoCargo>();
			this.listaTipoCargosIniciales = new ArrayList<TipoCargo>();
			this.listaTipoCargosSeleccionados = new ArrayList<TipoCargo>();
			/* Load the list of types of cargo */
			this.listaTipoCargos = tipoCargoDao.consultarTiposCargo();
			if (departamentoEmpresa != null) {
				this.departamentoEmpresa = departamentoEmpresa;
				/* tiposCargo the type department */
				List<TipoCargo> tiposCargoTipoDep = this.departamentoEmpresa
						.getTipoCargos();
				if (tiposCargoTipoDep != null && tiposCargoTipoDep.size() > 0) {
					this.listaTipoCargosSeleccionados = tiposCargoTipoDep;
					this.listaTipoCargosIniciales
							.addAll(listaTipoCargosSeleccionados);
					/* Selected from the original list are removed */
					if (listaTipoCargos != null) {
						for (TipoCargo tipoCargoSeleccionado : listaTipoCargosSeleccionados) {
							for (TipoCargo tipoCargo : listaTipoCargos) {
								if (tipoCargoSeleccionado.getId() == tipoCargo
										.getId()) {
									listaTipoCargos.remove(tipoCargo);
									break;
								}
							}
						}
					}
				}
				this.mensajeMiga = "messageOrganizations.departamento_company_label_modify";
			} else {
				this.departamentoEmpresa = new DepartamentoEmpresa();
				this.mensajeMiga = "messageOrganizations.department_company_label_register";
			}
			Collections.sort(listaTipoCargos);
			Collections.sort(listaTipoCargosSeleccionados);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regDepartamentoEmpresa";
	}

	/**
	 * Save / Modify a company department
	 * 
	 * @modify 12/03/2012 marisol.calderon
	 * 
	 * @return salida: Rule navigation screen management departments
	 */
	public String guardarDepartamentoEmpresa() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String salida = "regDepartamentoEmpresa";
		String key = "message_registro_modificar";
		try {
			this.departamentoEmpresa.setUserName(this.identity.getUserName());
			departamentoEmpresa.setNombre(WordUtils
					.capitalizeFully(departamentoEmpresa.getNombre()));
			this.userTransaction.begin();
			if (this.departamentoEmpresa.getId() != 0) {
				// modify
				this.departamentoEmpresaDao
						.modificarDepartamentoEmpresa(this.departamentoEmpresa);
			} else {
				// save
				key = "message_registro_guardar";
				Date fechaActual = new Date();
				this.departamentoEmpresa.setFechaCreacion(fechaActual);
				List<DepartamentoEmpresa> listaDepartamentos = this.departamentoEmpresaDao
						.consultarTodosDepartamentos();
				if (listaDepartamentos != null && !listaDepartamentos.isEmpty()) {
					this.departamentoEmpresa.setPredeterminadoCalidad(false);
				} else {
					this.departamentoEmpresa.setPredeterminadoCalidad(true);
				}

				this.departamentoEmpresaDao
						.registrarDepartamentoEmpresa(this.departamentoEmpresa);
			}
			guardarDepartamentoTipoCargo();
			this.userTransaction.commit();
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(bundle.getString(key),
							this.departamentoEmpresa.getNombre()));
			salida = this.inicializarBusqueda();
		} catch (Exception exception) {
			try {
				this.userTransaction.rollback();

			} catch (Exception exception2) {
				ControladorContexto.mensajeError(exception2);
			}
			ControladorContexto.mensajeError(exception);
		}
		return salida;
	}

	/**
	 * Method to save records of the types of departments selected types of
	 * charge
	 * 
	 * @author marisol.calderon
	 * 
	 * @throws Exception
	 */
	private void guardarDepartamentoTipoCargo() throws Exception {
		if (listaTipoCargosIniciales != null
				&& listaTipoCargosSeleccionados != null) {
			List<Integer> actualesIds = new ArrayList<Integer>();
			List<Integer> nuevosIds = new ArrayList<Integer>();
			/* The lists are filled with only the ids */
			for (TipoCargo tipoCargoNuevo : listaTipoCargosIniciales) {
				actualesIds.add(tipoCargoNuevo.getId());
			}
			for (TipoCargo tipoCargoSel : listaTipoCargosSeleccionados) {
				nuevosIds.add(tipoCargoSel.getId());
			}
			/* Validated lists */
			List<DatosGuardar> listaDatos = ValidacionesAction.validarListas(
					actualesIds, nuevosIds);
			for (DatosGuardar datosGuardar : listaDatos) {
				boolean existe = true;
				String accion = datosGuardar.getAccion();
				TipoCargo tipoCargo = tipoCargoDao
						.consultarTipoCargo(datosGuardar.getIdClase());
				DepartamentoEmpresaTipoCargoPK llavePrimaria = new DepartamentoEmpresaTipoCargoPK();
				llavePrimaria.setDepartamentoEmpresa(departamentoEmpresa);
				llavePrimaria.setTipoCargo(tipoCargo);
				/* The object is queried for the primary key */
				DepartamentoEmpresaTipoCargo depEmpresaTipoCargo = depEmpresaTipoCargoDao
						.departamentoEmpresaTipoCargoXId(llavePrimaria);
				Date fechaActual = new Date();
				if (depEmpresaTipoCargo == null) {
					existe = false;
					depEmpresaTipoCargo = new DepartamentoEmpresaTipoCargo();
					depEmpresaTipoCargo.setLlavePrimaria(llavePrimaria);
					depEmpresaTipoCargo.setFechaCreacion(fechaActual);
				}
				if (Constantes.QUERY_INSERT.equals(accion)) {
					fechaActual = null;
				}
				depEmpresaTipoCargo.setUserName(identity.getUserName());
				depEmpresaTipoCargo.setFechaFinVigencia(fechaActual);
				if (existe) {
					depEmpresaTipoCargoDao
							.editarDepartamentoEmpresaTipoCargo(depEmpresaTipoCargo);
				} else {
					depEmpresaTipoCargoDao
							.guardarDepartamentoEmpresaTipoCargo(depEmpresaTipoCargo);
				}
			}
		}

	}

	/**
	 * To validate the department name so that it does not recur in the database
	 * and validates against XSS.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNombreXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			boolean existe = true;
			if (departamentoEmpresa.getId() != 0) {
				existe = this.departamentoEmpresaDao.nombreExiste(nombre,
						this.departamentoEmpresa.getId());
			} else {
				existe = this.departamentoEmpresaDao.nombreExiste(nombre);
			}
			if (existe) {
				boolean esVigente = validarVigenciaDepartamentoEmpresaXNombre(nombre);
				if (esVigente) {
					// Informs the user that the name exists and is in force
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);

				} else {
					// Informs the user that the name exists and is not valid
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				}
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
	 * Validates the effectiveness of the department name, to tell the client
	 * what to do
	 * 
	 * @param name
	 *            : name of the department to be validated if it exists.
	 * 
	 * @return <b>true</b> if applicable, otherwise <b>false</b>
	 * 
	 * @throws Exception
	 */
	private boolean validarVigenciaDepartamentoEmpresaXNombre(String name)
			throws Exception {
		boolean salida = true;
		DepartamentoEmpresa departamentoEmpresa = this.departamentoEmpresaDao
				.consultarDepartamentoEmpresaXNombre(name);
		if (departamentoEmpresa.getFechaFinVigencia() != null
				&& departamentoEmpresa.getFechaFinVigencia().before(new Date())) {
			salida = false;
		}
		return salida;
	}

	/**
	 * Initializes search parameters and load the departments list
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @return gestionarDepartamentoEmpresa: Rule navigation screen management
	 *         departments
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return this.gestionarDepartamentoEmpresa();
	}

	/**
	 * Initializes the variables of action for screen management departments
	 * 
	 * @modify 12/03/2012 marisol.calderon
	 * @modify 19/06/2012 Liseth.Jimenez
	 * 
	 * @return salida: Rule navigation screen management departments
	 */
	public String gestionarDepartamentoEmpresa() {
		String salida = null;
		String mensajeBusqueda = "";
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("messageOrganizations");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			this.opcionesVigencia = new ArrayList<SelectItem>();
			// Enter valid options
			this.opcionesVigencia.add(new SelectItem(true, bundle
					.getString("label_current")));
			this.opcionesVigencia.add(new SelectItem(false, bundle
					.getString("label_not_current")));

			this.listaDepartamentoEmpresas = new ArrayList<DepartamentoEmpresa>();
			long cantidadRegistros = 0;

			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(this.consultarVigentes)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			cantidadRegistros = this.departamentoEmpresaDao
					.cantidadDepartamentosEmpresa(condicionVigencia,
							this.nombreBuscar);
			this.pagination.paginar(cantidadRegistros);
			this.listaDepartamentoEmpresas = this.departamentoEmpresaDao
					.consultarDepartamentosEmpresa(this.pagination.getInicio(),
							this.pagination.getRango(), condicionVigencia,
							this.nombreBuscar);
			for (DepartamentoEmpresa departamentoEmpresa : listaDepartamentoEmpresas) {
				List<TipoCargo> tipoCargos = tipoCargoDao
						.tipoCargoXDepartamentoEmpresa(departamentoEmpresa);
				departamentoEmpresa.setTipoCargos(tipoCargos);
			}
			if ((this.listaDepartamentoEmpresas == null || this.listaDepartamentoEmpresas
					.size() <= 0)
					&& (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nombreBuscar + '"');
			} else if (this.listaDepartamentoEmpresas == null
					|| this.listaDepartamentoEmpresas.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleOrg
										.getString("department_company_label_s"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
			salida = "gesDepartamentoEmpresa";
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return salida;
	}

	/**
	 * Assign an object variable to modify business department, it is used when
	 * it will change the life
	 * 
	 * @param departamentoEmpresa
	 *            Department to be changed
	 */
	public void asignarDepartamentoEmpresaModificar(
			DepartamentoEmpresa departamentoEmpresa) {
		if (departamentoEmpresa instanceof DepartamentoEmpresa) {
			this.departamentoEmpresa = departamentoEmpresa;
		}
	}

	/**
	 * Modifies the life of a company department
	 * 
	 * @param vigencia
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            INICA with 'false', the selected record in the user interface.
	 * 
	 * 
	 * @return salida: Rule navigation screen management departments
	 */
	public String modificarVigenciaDepartamentoEmpresa(boolean vigencia) {
		String salida = null;
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_fin_vigencia_satisfactorio";
		try {
			Date fechaActual;
			if (vigencia) {
				fechaActual = new Date();
			} else {
				key = "message_inicio_vigencia_satisfactorio";
				fechaActual = null;
			}
			this.departamentoEmpresa.setFechaFinVigencia(fechaActual);
			this.departamentoEmpresa.setUserName(this.identity.getUserName());
			this.departamentoEmpresaDao
					.modificarDepartamentoEmpresa(this.departamentoEmpresa);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(key) + ": {0}",
					this.departamentoEmpresa.getNombre()));
			salida = this.gestionarDepartamentoEmpresa();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return salida;

	}

	/**
	 * Method to predetermine the main department of the company.
	 * 
	 * @return salida: Rule screen navigation to predetermine Department
	 */
	public String predeterminarDepartamento() {
		String salida = null;
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			List<DepartamentoEmpresa> listaDptos = this.departamentoEmpresaDao
					.consultarDepartamentosEmpresa(true);
			this.departamentoEmpresa = new DepartamentoEmpresa();
			this.departamentos = new HashMap<Integer, DepartamentoEmpresa>();
			this.opcionesDepartamentos = new ArrayList<SelectItem>();
			if (listaDptos != null & !listaDptos.isEmpty()) {
				for (DepartamentoEmpresa de : listaDptos) {
					this.departamentos.put(de.getId(), de);
					this.opcionesDepartamentos.add(new SelectItem(de.getId(),
							de.getNombre()));
					if (de.getPredeterminadoCalidad().booleanValue()) {
						this.idDepartamentoPredeterminado = de.getId();
					}
				}
			} else {
				FacesContext
						.getCurrentInstance()
						.addMessage(
								null,
								new FacesMessage(
										FacesMessage.SEVERITY_INFO,
										bundle.getString("message_no_existen_registros"),
										""));

			}
			salida = "predDepartamentoEmpresa";
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return salida;
	}

}
