package co.informatix.erp.organizaciones.action;

import java.io.File;
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
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.primefaces.event.FileUploadEvent;
import org.richfaces.event.ItemChangeEvent;

import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.organizaciones.dao.EmpresaDao;
import co.informatix.erp.organizaciones.dao.OrganizacionDao;
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.organizaciones.entities.Organizacion;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class lets you handle the business logic of the companies that exist in
 * the system, which allows you to insert, modify, and query.
 * 
 * 
 * @author Oscar.Amaya
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class EmpresaAction implements Serializable {

	@EJB
	private EmpresaDao empresaDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private OrganizacionDao organizacionDao;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private Paginador pagination = new Paginador();
	private Empresa empresaVigencia;
	private Empresa empresa;
	private List<Empresa> listaEmpresas;
	private List<SelectItem> itemsOrganizaciones;
	private List<SelectItem> itemsPaises;
	private List<SelectItem> itemDepartamentos;
	private List<SelectItem> itemsMunicipios;
	private String vigencia = Constantes.SI;
	private String tabSelect = Constantes.N_TAB;
	private String opcion;
	private String carpetaArchivos;
	private String carpetaArchivosTemporal;
	private String nombreFotoLogo;
	private String labelCrear;
	private String mensajeMiga;
	private String nombreBuscar;
	private boolean cargarFotoTemporal;

	/**
	 * @return Label to show when a company is created
	 */
	public String getLabelCrear() {
		return labelCrear;
	}

	/**
	 * @return nombreFotoLogo: filename of the logo associated with the company
	 */
	public String getNombreFotoLogo() {
		return nombreFotoLogo;
	}

	/**
	 * @param nombreFotoLogo
	 *            : filename of the logo associated with the company
	 */
	public void setNombreFotoLogo(String nombreFotoLogo) {
		this.nombreFotoLogo = nombreFotoLogo;
	}

	/**
	 * @return itemsOrganizaciones: Items organizations that are uploaded the
	 *         combo of the user interface
	 */
	public List<SelectItem> getItemsOrganizaciones() {
		return itemsOrganizaciones;
	}

	/**
	 * @return carpetaArchivos: the actual folder path where you load the photos
	 *         of company logos
	 */
	public String getCarpetaArchivos() {
		this.carpetaArchivos = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_LOGOS_EMPRESAS;
		return carpetaArchivos;
	}

	/**
	 * @return carpetaArchivosTemporal: temporary folder path where carry the
	 *         logos of companies
	 */
	public String getCarpetaArchivosTemporal() {
		this.carpetaArchivosTemporal = Constantes.CARPETA_ARCHIVOS_ORGANIZACIONES
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return carpetaArchivosTemporal;
	}

	/**
	 * 
	 * @return fileUploadBean: Variable that gets the object for loading files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : Variable that gets the object for loading files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return vigencia: gets the value for the management of the current of the
	 *         registers
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : sets the value for the management of the current of the
	 *            registers
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return empresaVigencia: company to modify the current
	 */
	public Empresa getEmpresaVigencia() {
		return empresaVigencia;
	}

	/**
	 * @param empresaVigencia
	 *            : company to modify the current
	 */
	public void setEmpresaVigencia(Empresa empresaVigencia) {
		this.empresaVigencia = empresaVigencia;
	}

	/**
	 * @return nombreBuscar: Variable that gets the name of the company that is
	 *         searches the user interface.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Variable that sets the name of the company that is searches
	 *            the user interface.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return opcion: allows to add or edit a new company existing
	 */
	public String getOpcion() {
		return opcion;
	}

	/**
	 * @param opcion
	 *            : allows to add or edit a new company existing
	 */
	public void setOpcion(String opcion) {
		this.opcion = opcion;
	}

	/**
	 * @return tabSelect: Variable that gets the name of the tab you want to
	 *         load as selected.
	 */
	public String getTabSelect() {
		return tabSelect;
	}

	/**
	 * @param tabSelect
	 *            : Variable that gets the name of the tab you want to load as
	 *            selected.
	 */
	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}

	/**
	 * @return pagination: Object to the functions of the pager companies
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Object to the functions of the pager companies
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return empresa: object of the company to which the record or editing is
	 *         done.
	 */
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            : object of the company to which the record or editing is
	 *            done.
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return itemsPaises: Items of the countries that are displayed in a combo
	 *         in the user interface
	 */
	public List<SelectItem> getItemsPaises() {
		return itemsPaises;
	}

	/**
	 * @return itemDepartamentos: Items of the countries that are displayed in a
	 *         combo in the user interface
	 */
	public List<SelectItem> getItemDepartamentos() {
		return itemDepartamentos;
	}

	/**
	 * @return itemsMunicipios: Items of the municipalities that are displayed
	 *         in a combo in the user interface
	 */
	public List<SelectItem> getItemsMunicipios() {
		return itemsMunicipios;
	}

	/**
	 * @return listaEmpresas: list of companies that are loaded into the user
	 *         interface.
	 */
	public List<Empresa> getListaEmpresas() {
		return listaEmpresas;
	}

	/**
	 * @param listaEmpresas
	 *            : list of companies that are loaded into the user interface.
	 */
	public void setListaEmpresas(List<Empresa> listaEmpresas) {
		this.listaEmpresas = listaEmpresas;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on the role from which the company releases
	 * 
	 * @return mensajeMiga: message crumb of bread in the template
	 */
	public String getMensajeMiga() {
		return mensajeMiga;
	}

	/**
	 * Variable that gets the value of the message to display in the bread
	 * crumbs, depending on the role from which the company releases
	 * 
	 * @param mensajeMiga
	 *            : message crumb of bread in the template
	 */
	public void setMensajeMiga(String mensajeMiga) {
		this.mensajeMiga = mensajeMiga;
	}

	/**
	 * @return cargarFotoTemporal: Flag indicating whether the picture is loaded
	 *         from the temporary location or not
	 */
	public boolean isCargarFotoTemporal() {
		return cargarFotoTemporal;
	}

	/**
	 * @param cargarFotoTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not
	 */
	public void setCargarFotoTemporal(boolean cargarFotoTemporal) {
		this.cargarFotoTemporal = cargarFotoTemporal;
	}

	/**
	 * This method establishes a joint company instance, also validates the
	 * selection of at least one role and required fields Company
	 * 
	 * @param empresa
	 *            : company establish
	 * @throws Exception
	 */
	public void establecerEmpresaValidarRolYRequeridos(Empresa empresa)
			throws Exception {
		if (requeridosOk("formRegistrarEmpresa")) {
			this.empresa = empresa;
		}
	}

	/**
	 * This method creates a new object class Business and load combo. This
	 * method is used when loading a page to register a new company.
	 * 
	 * @modify 26/01/2012 marisol.calderon
	 * @modify 16/02/2012 marisol.calderon
	 * 
	 * @param rol
	 *            : This role is to create the company, these can be ... Normal
	 *            (n), customer (c), insurance (s), transportation (t), provider
	 *            (p)
	 * @return regEmpresa: View to record the company
	 */
	public String nuevaEmpresa(char rol) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		String pagina = "regEmpresa";
		try {
			this.opcion = Constantes.NUEVO;
			this.empresa = new Empresa();
			this.cargarCombos();
			this.nombreFotoLogo = null;
			this.fileUploadBean = new FileUploadBean();
			this.cargarFotoTemporal = true;
			if (Constantes.N_TAB.equals(rol)) {
				this.labelCrear = bundleOrg
						.getString("empresa_label_registrar");
				this.mensajeMiga = "mensajeOrganizaciones.empresa_label_crear";
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return pagina;
	}

	/**
	 * This method is used when loading a page to edit a business. Edits always
	 * be loaded in the same registrarEmpresa template to associate a new role
	 * at the company to edit
	 * 
	 * @modify Luis.Ruiz
	 * @modify 25/01/2012 marisol.calderon
	 * @modify 16/02/2012 marisol.calderon
	 * 
	 * @param empresaEditar
	 *            : The company that publishes
	 * @param rol
	 *            : role or type of company that is selected for editing, in the
	 *            case 'f' the same procedure is performed 'n'
	 * @return "regEmpresa": returns to the template registrarEmpresa
	 */
	public String editarEmpresa(Empresa empresaEditar, char rol) {
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		try {
			opcion = Constantes.EDITAR;
			this.empresa = empresaEditar;
			this.nombreFotoLogo = this.empresa.getLogo();
			this.cargarFotoTemporal = false;
			cargarCombos();
			/*
			 * Message displayed depending on the role in registrarEmpresa
			 * template.
			 */
			if (Constantes.N_TAB.equals(rol)) {
				this.labelCrear = bundleOrg
						.getString("empresa_label_registrar");
				mensajeMiga = "mensajeOrganizaciones.empresa_label_modificar";
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regEmpresa";
	}

	/**
	 * Method for consulting companies from the pager, the method takes no
	 * parameters for the template used Pager
	 */
	public void consultarEmpresas() {
		String rolEmpresa = ControladorContexto.getParam("param2");
		consultarEmpresas(rolEmpresa);
	}

	/**
	 * Initializes the name in enterprise search.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @return consultarEmpresas(): Method consulting firms present in the
	 *         system and returns to the management staff with the search
	 *         results.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarEmpresas(Constantes.N_TAB);
	}

	/**
	 * This method consulting firms depending on the type of company that has
	 * each.
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 * @modify 11/10/2012 Adonay.Mantilla
	 * 
	 * @param rol
	 *            : establishes the role of business for companies on request.
	 * @return gesEmpresas: Navigation rule that routes to the template list
	 *         management companies.
	 */
	public String consultarEmpresas(String rol) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleOrganizaciones = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		this.empresa = new Empresa();
		try {
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			setTabSelect(rol);
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			if (Constantes.N_TAB.equals(rol)) {
				/* All companies are queried */
				this.pagination.paginar(this.empresaDao.cantidadEmpresas(
						condicionVigencia, consulta, parametros));
				this.listaEmpresas = this.empresaDao.consultarEmpresas(
						this.pagination.getInicio(),
						this.pagination.getRango(), condicionVigencia,
						consulta, parametros);
			} else if (Constantes.F_TAB.equals(rol)) {
				/* Businesses are consulted with estates */
				this.pagination.paginar(this.empresaDao
						.cantidadEmpresasConHaciendas(condicionVigencia));
				this.listaEmpresas = this.empresaDao
						.consultarEmpresasConHaciendas(
								this.pagination.getInicio(),
								this.pagination.getRango(), condicionVigencia);
			}
			this.empresaVigencia = new Empresa();
			cargarDetallesEmpresas();
			/* Search posts are build */
			if ((this.listaEmpresas == null || this.listaEmpresas.size() <= 0)
					&& (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (this.listaEmpresas == null
					|| this.listaEmpresas.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleOrganizaciones
										.getString("empresa_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesEmpresas";
	}

	/**
	 * This method build the consult for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @author Adonay.Mantilla
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
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			SelectItem item = new SelectItem("%" + nombreBuscar + "%",
					"keyword");
			consult.append(" AND UPPER(e.nombre) LIKE UPPER(:keyword) ");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * This method fills the various objects associated with a business
	 * 
	 * @modify 01/02/2012 marisol.calderon
	 * 
	 * @throws Exception
	 */
	public void cargarDetallesEmpresas() throws Exception {
		List<Empresa> empresas = new ArrayList<Empresa>();
		empresas.addAll(this.listaEmpresas);
		this.listaEmpresas = new ArrayList<Empresa>();
		for (Empresa empresa : empresas) {
			cargarDetallesUnaEmpresa(empresa);
			this.listaEmpresas.add(empresa);
		}
	}

	/**
	 * Method of uploading the details of a company.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param empresa
	 *            : company which will carry the details.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public void cargarDetallesUnaEmpresa(Empresa empresa) throws Exception {
		int idEmpresa = empresa.getId();
		/* Objects are consulted */
		Pais pais = (Pais) this.empresaDao.consultarObjetoEmpresa("pais",
				idEmpresa);
		Departamento departamento = (Departamento) this.empresaDao
				.consultarObjetoEmpresa("departamento", idEmpresa);
		Municipio municipio = (Municipio) this.empresaDao
				.consultarObjetoEmpresa("municipio", idEmpresa);
		List<Organizacion> organizacion = (List<Organizacion>) this.empresaDao
				.consultarListaObjetosDeEmpresa("organizacion", idEmpresa);
		/* They are assigned to the company */
		empresa.setPais(pais);
		empresa.setDepartamento(departamento);
		empresa.setMunicipio(municipio);
		if (organizacion.size() > 0) {
			empresa.setOrganizacion(organizacion.get(0));
		} else {
			empresa.setOrganizacion(new Organizacion());
		}
	}

	/**
	 * Method to execute the action of the tabs to be selected by the user
	 * 
	 * @param event
	 *            : event that runs when a tab is selected
	 */
	public void cambioPestana(ItemChangeEvent event) {
		String idPestana = event.getNewItemName();
		if (idPestana != null && !"".equals(idPestana)) {
			consultarEmpresas(idPestana);
		}
	}

	/**
	 * This method modifies the current of a business
	 * 
	 * @param vigente
	 *            : boolean that allows to know if the current ends with 'true'
	 *            or INICA with 'false', the selected record in the user
	 *            interface.
	 * @param rol
	 *            : establishes the role of business for companies on request.
	 * @return consultarEmpresas(): Consulting companies present in the system
	 *         according to the selected role and returns to the management
	 *         staff.
	 */
	public String vigenciaEmpresa(boolean vigente, String rol) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_fin_vigencia_satisfactorio";
		try {
			if (Constantes.N_TAB.equals(rol)) {
				this.empresaVigencia.setUserName(this.identity.getUserName());
				/* The organization is not required field */
				if (this.empresaVigencia.getOrganizacion() != null
						&& this.empresaVigencia.getOrganizacion().getId() == 0) {
					this.empresaVigencia.setOrganizacion(null);
				}
				if (vigente) {
					this.empresaVigencia.setFechaFinVigencia(new Date());
					this.empresaDao.modificarEmpresa(this.empresaVigencia);
				} else {
					key = "message_inicio_vigencia_satisfactorio";
					this.empresaVigencia.setFechaFinVigencia(null);
					this.empresaDao.modificarEmpresa(this.empresaVigencia);
				}
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(key) + ": {0}",
								this.empresaVigencia.getNombre()));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarEmpresas(rol);
	}

	/**
	 * This method allows you to register a new company, obtains the data stored
	 * in the form and stored in the database
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 * @modify 20/09/2012 Gerson.Cespedes
	 * 
	 * @param rol
	 *            : Type of business that the company you select to save, this
	 *            value is used so that the tab for the type of company could be
	 *            loading the list.
	 * @return String: Navigation rule that returns to the template record or
	 *         check depending on the case company.
	 */
	public String agregarEditarEmpresa(String rol) {
		ResourceBundle bundle2 = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		String mensaje = "";
		String nombreFotoBorrar = null;
		String salida = "regEmpresa";
		boolean edicion = false;
		boolean seCambioLogo = false;
		boolean error = false;
		try {
			this.userTransaction.begin();
			/* The organization is not required field */
			if (this.empresa.getOrganizacion().getId() == 0) {
				this.empresa.setOrganizacion(null);
			}
			this.empresa.setUserName(this.identity.getUserName());
			if (this.empresa.getId() != 0) {
				edicion = true;
				if (this.empresa.getLogo() != null
						&& !"".equals(this.empresa.getLogo())
						&& !this.empresa.getLogo().equals(this.nombreFotoLogo)) {
					this.borrarArchivoReal(this.empresa.getLogo());
					seCambioLogo = true;
				} else if (empresa.getLogo() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					seCambioLogo = true;
				}
				this.empresa.setLogo(this.nombreFotoLogo);
				if (empresa.getLogo() != null && seCambioLogo) {
					nombreFotoBorrar = this.nombreFotoLogo;
					/* The image is loaded into the actual location */
					subirImagenUbicacionReal();
				}
				this.empresaDao.modificarEmpresa(this.empresa);

				mensaje = MessageFormat
						.format(bundle2
								.getString("empresa_message_exito_empresa_modificacion"),
								this.empresa.getNombre(), this.empresa.getNit());
			} else {
				if (this.nombreFotoLogo != null
						&& !"".equals(this.nombreFotoLogo.trim())) {
					nombreFotoBorrar = this.nombreFotoLogo;
					subirImagenUbicacionReal();
				}
				this.empresa.setFechaCreacion(new Date());
				this.empresa.setLogo(this.nombreFotoLogo);
				this.empresaDao.crearEmpresa(this.empresa);
				mensaje = MessageFormat.format(bundle2
						.getString("empresa_message_exito_empresa_registro"),
						empresa.getNombre(), empresa.getNit());
			}
			this.userTransaction.commit();
			/* The temporary file is deleted */
			if (nombreFotoBorrar != null && !"".equals(nombreFotoBorrar)) {
				this.borrarArchivo(nombreFotoBorrar);
			}
			ControladorContexto.mensajeInformacion(null, mensaje);
			salida = consultarEmpresas(rol);
		} catch (Exception e) {
			error = true;
			if (this.nombreFotoLogo != null && !"".equals(this.nombreFotoLogo)
					&& !edicion) {
				this.borrarArchivoReal(this.nombreFotoLogo);
			}
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		if (!error) {
			this.empresa = new Empresa();
		}
		return salida;
	}

	/**
	 * This method allows you to load combo country, department, municipality
	 * company, nature of business, organization and unit of measure
	 * 
	 * @throws Exception
	 * 
	 * @modify Luis.Ruiz
	 * @modify 01/02/2012 marisol.calderon
	 * @modify 21/01/2013 Adonay.Mantilla
	 * 
	 */
	private void cargarCombos() throws Exception {
		itemsPaises = new ArrayList<SelectItem>();
		List<Pais> paises = paisDao.consultarPaisesVigentes();
		if (paises != null) {
			for (Pais pais : paises) {
				itemsPaises.add(new SelectItem(pais.getId(), pais.getNombre()));
			}
		}
		cargarDepartamentos();
		cargarMunicipios();
		itemsOrganizaciones = new ArrayList<SelectItem>();
		List<Organizacion> listaOrganizacionesVigentes = organizacionDao
				.consultarOrganizacionesVigentes();
		if (listaOrganizacionesVigentes != null) {
			for (Organizacion organizacion : listaOrganizacionesVigentes) {
				itemsOrganizaciones.add(new SelectItem(organizacion.getId(),
						organizacion.getRazonSocial()));
			}
		}
	}

	/**
	 * This method makes the request of the departments registered in the
	 * database, associated with a selected country.
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 * 
	 */
	public void cargarDepartamentos() {
		itemDepartamentos = new ArrayList<SelectItem>();
		itemsMunicipios = new ArrayList<SelectItem>();
		try {
			Pais pais = empresa.getPais();
			if (pais != null && pais.getId() > 0) {
				short idPais = pais.getId();
				List<Departamento> departamentos = departamentoDao
						.consultarDepartamentosPaisVigentes(idPais);
				if (departamentos != null) {
					for (Departamento d : departamentos) {
						itemDepartamentos.add(new SelectItem(d.getId(), d
								.getNombre()));
					}
				}
				cargarMunicipios();
			} else {
				empresa.setDepartamento(new Departamento());
				empresa.setMunicipio(new Municipio());
				empresa.getDepartamento().setId(0);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered in the
	 * database, associated with a department selected a partner country.
	 * 
	 * @modify 16/02/2012 marisol.calderon
	 * @modify 30/03/2012 angelica.amaya
	 * 
	 */
	public void cargarMunicipios() {
		itemsMunicipios = new ArrayList<SelectItem>();
		try {
			Departamento departamento = empresa.getDepartamento();
			if (departamento != null && departamento.getId() > 0
					&& this.itemDepartamentos.size() > 0) {
				int idDepartamento = departamento.getId();
				List<Municipio> municipios = municipioDao
						.consultarMunicipiosVigentes(idDepartamento);
				if (municipios != null) {
					for (Municipio m : municipios) {
						itemsMunicipios.add(new SelectItem(m.getId(), m
								.getNombre()));
					}
				}
			} else {
				itemsMunicipios = new ArrayList<SelectItem>();
				empresa.setDepartamento(new Departamento());
				empresa.setMunicipio(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method validates the NIT of a company
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNitEmpresaXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle2 = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		String nit = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			Integer id = 0;
			if (this.empresa.getId() != 0) {
				id = this.empresa.getId();
			}
			boolean validarNit = empresaDao.nitExiste(nit, id);
			if (validarNit) {
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle2
								.getString("empresa_message_validar_nit"), null));
			}
			if (!EncodeFilter.validarXSS(nit, clientId, "locate.regex.nit")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allow delete the file name.
	 * 
	 * @author Luis.Ruiz
	 */
	public void borrarFilename() {
		if (nombreFotoLogo != null && !"".equals(nombreFotoLogo)
				&& !nombreFotoLogo.equals(empresa.getLogo())
				&& this.cargarFotoTemporal) {
			borrarArchivo(nombreFotoLogo);
		}
		nombreFotoLogo = null;
		fileUploadBean.setFileName(null);
	}

	/**
	 * method that allows delete files.
	 * 
	 * @author Luis.Ruiz
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void borrarArchivo(String fileName) {
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Delete files from the actual location
	 * 
	 * @author marisol.calderon
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void borrarArchivoReal(String fileName) {
		String ubicaciones[] = {
				Constantes.RUTA_UPLOADFILE_GLASFISH + this.getCarpetaArchivos(),
				Constantes.RUTA_UPLOADFILE_WORKSPACE
						+ this.getCarpetaArchivos() };
		fileUploadBean.delete(ubicaciones, fileName);
	}

	/**
	 * Allows you to load the file system
	 * 
	 * @author Luis.Ruiz
	 * @modify 02/02/2015 Jonathan.Arias
	 * 
	 * @param e
	 *            : File upload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAceptadas[] = Constantes.EXT_IMG.split(", ");
		String ubicaciones[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getCarpetaArchivosTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		long tamanyoMaxArchivos = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(extAceptadas,
				ubicaciones, tamanyoMaxArchivos);
		String mensaje = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			mensaje = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"),
					tamanyoMaxArchivos, "MB");
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			mensaje = "error_carga_archivo";
		}
		if (!"".equals(mensaje)) {
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					bundle.getString(mensaje));
		}
		if (empresa.getId() != 0) {
			cargarFotoTemporal = true;
		}
		nombreFotoLogo = fileUploadBean.getFileName();
	}

	/**
	 * Add the logo image to the actual folder
	 * 
	 * @author marisol.calderon
	 * 
	 * @throws Exception
	 */
	private void subirImagenUbicacionReal() throws Exception {
		String origen = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getCarpetaArchivosTemporal();
		String destino1 = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getCarpetaArchivos();
		String destino2 = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getCarpetaArchivos();

		/* It checks whether the destinations are created there but */
		FileUploadBean.fileExist(destino1);
		FileUploadBean.fileExist(destino2);

		File fileOrigen = new File(origen, fileUploadBean.getFileName());
		File fileDestino1 = new File(destino1, fileUploadBean.getFileName());
		File fileDestino2 = new File(destino2, fileUploadBean.getFileName());

		/* Copies of temporal at 2 real destinations */
		FileUploadBean.copyFile(fileOrigen, fileDestino1);
		FileUploadBean.copyFile(fileOrigen, fileDestino2);
	}

	/**
	 * Validates fields that are required in the view so that you can load
	 * regardless logo that are not filled out these fields
	 * 
	 * @author marisol.calderon
	 * 
	 * @param nombreForm
	 *            : name or id form in which the message is displayed.
	 * @return result: boolean to true if all fields are good or false
	 *         otherwise.
	 * @throws Exception
	 */
	public boolean requeridosOk(String nombreForm) throws Exception {
		boolean result = true;
		if (empresa.getNit() == null || "".equals(empresa.getNit())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":nit");
			result = false;
		}
		if (empresa.getNombre() == null || "".equals(empresa.getNombre())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":nombre");
			result = false;
		}
		if (empresa.getDireccion() == null || "".equals(empresa.getDireccion())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":direccion");
			result = false;
		}
		if (empresa.getTelefono() == null || "".equals(empresa.getTelefono())) {
			ControladorContexto.mensajeRequeridos(nombreForm + ":telefono");
			result = false;
		}
		if (empresa.getPais().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":comboPaisEmpresa");
			result = false;
		}
		if (empresa.getDepartamento().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":comboDepartamentoEmpresa");
			result = false;
		}
		if (empresa.getMunicipio().getId() == 0) {
			ControladorContexto.mensajeRequeridos(nombreForm
					+ ":comboMunicipioEmpresa");
			result = false;
		}
		if (!EncodeFilter.validarXSS(nombreFotoLogo,
				nombreForm + ":uploadLogo", null)) {
			result = false;
		}

		return result;
	}

	/**
	 * This method makes the registration of a company from a modal
	 * 
	 * @author marisol.calderon
	 */
	public void guardarEmpresaDesdeDialogo() {
		try {
			if (this.requeridosOk("formRegistrarEmpresa")
					&& ControladorContexto.getMaxSeverity() == null) {
				agregarEditarEmpresa(Constantes.N_TAB);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
