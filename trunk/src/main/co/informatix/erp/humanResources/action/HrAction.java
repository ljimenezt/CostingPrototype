package co.informatix.erp.humanResources.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.HrTypesDao;
import co.informatix.erp.humanResources.dao.PaymentMethodsDao;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.HrTypes;
import co.informatix.erp.humanResources.entities.PaymentMethods;
import co.informatix.erp.informacionBase.dao.DepartamentoDao;
import co.informatix.erp.informacionBase.dao.EstadoCivilDao;
import co.informatix.erp.informacionBase.dao.MunicipioDao;
import co.informatix.erp.informacionBase.dao.PaisDao;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.EstadoCivil;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating or removal of
 * human resources in the system.
 * 
 * @author Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class HrAction implements Serializable {
	@EJB
	private HrDao hrDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private PaisDao paisDao;
	@EJB
	private DepartamentoDao departamentoDao;
	@EJB
	private MunicipioDao municipioDao;
	@EJB
	private EstadoCivilDao estadoCivilDao;
	@EJB
	private PaymentMethodsDao paymentMethodsDao;
	@EJB
	private HrTypesDao hrTypesDao;

	private Hr hr;
	private Paginador paginador = new Paginador();
	private Paginador paginadorForm = new Paginador();
	private String nombreBuscar;
	private String apellidoBuscar;
	private String carpetaArchivos;
	private String carpetaArchivosTemporal;
	private String nombreFotoHr;
	private Date fechaActual = new Date();

	private List<Hr> listaHr;
	private List<SelectItem> itemsPaisesNacimiento;
	private List<SelectItem> itemDepartamentosNacimiento;
	private List<SelectItem> itemsMunicipiosNacimiento;
	private List<SelectItem> itemsPaisesResidencia;
	private List<SelectItem> itemDepartamentosResidencia;
	private List<SelectItem> itemsMunicipiosResidencia;
	private List<SelectItem> itemsEstadosCivil;
	private List<SelectItem> itemsHrTypes;
	private List<SelectItem> itemsPaymentMethods;

	private int hrTypeBuscar;
	private int paymentTypeBuscar;

	private boolean cargarFotoTemporal;

	/**
	 * @return listaHr: gets the list of human resources
	 */
	public List<Hr> getListaHr() {
		return listaHr;
	}

	/**
	 * @param listaHr
	 *            : gets the list of human resources
	 */
	public void setListaHr(List<Hr> listaHr) {
		this.listaHr = listaHr;
	}

	/**
	 * @return paginador: paginated list of human resources which can be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : paginated list of human resources which can be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paginadorForm : management responsible paged list from search
	 *         engine.
	 */
	public Paginador getPaginadorForm() {
		return paginadorForm;
	}

	/**
	 * @param paginadorForm
	 *            : management responsible paged list from search engine.
	 */
	public void setPaginadorForm(Paginador paginadorForm) {
		this.paginadorForm = paginadorForm;
	}

	/**
	 * @return hr: HR object
	 */
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : HR object
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return nombreBuscar: gets the name by which you want to consult Human
	 *         resources
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : gets the name by which you want to consult Human resources
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return apellidoBuscar: Name by which you want to consult human Resources
	 */
	public String getApellidoBuscar() {
		return apellidoBuscar;
	}

	/**
	 * @param apellidoBuscar
	 *            : Name by which you want to consult human Resources
	 */
	public void setApellidoBuscar(String apellidoBuscar) {
		this.apellidoBuscar = apellidoBuscar;
	}

	/**
	 * @return hrTypeBuscar: Type of human resources for which they want search
	 *         for human resources.
	 */
	public int getHrTypeBuscar() {
		return hrTypeBuscar;
	}

	/**
	 * @param hrTypeBuscar
	 *            : Type of human resources for which they want search for human
	 *            resources.
	 */
	public void setHrTypeBuscar(int hrTypeBuscar) {
		this.hrTypeBuscar = hrTypeBuscar;
	}

	/**
	 * @return paymentTypeBuscar: Payment type for which you want to search for
	 *         human Resources.
	 */
	public int getPaymentTypeBuscar() {
		return paymentTypeBuscar;
	}

	/**
	 * @param paymentTypeBuscar
	 *            : Payment type for which you want to search for human
	 *            Resources.
	 */
	public void setPaymentTypeBuscar(int paymentTypeBuscar) {
		this.paymentTypeBuscar = paymentTypeBuscar;
	}

	/**
	 * @return itemsPaisesNacimiento: Items of the countries shown in the combo
	 *         of country of birth in the user interface
	 */
	public List<SelectItem> getItemsPaisesNacimiento() {
		return itemsPaisesNacimiento;
	}

	/**
	 * @return itemDepartamentosNacimiento: Items Items of the countries shown
	 *         in the combo of country of birth in the user interface
	 */
	public List<SelectItem> getItemDepartamentosNacimiento() {
		return itemDepartamentosNacimiento;
	}

	/**
	 * @return itemsMunicipiosNacimiento: Items of the municipalities that are
	 *         shown in the combo municipality of birth interface user
	 */
	public List<SelectItem> getItemsMunicipiosNacimiento() {
		return itemsMunicipiosNacimiento;
	}

	/**
	 * @return itemsPaisesResidencia: Items of the municipalities that are shown
	 *         in the combo municipality of birth interface user
	 */
	public List<SelectItem> getItemsPaisesResidencia() {
		return itemsPaisesResidencia;
	}

	/**
	 * @return itemDepartamentosResidencia: Items of the departments shown in
	 *         the department of residence combobox interface user
	 */
	public List<SelectItem> getItemDepartamentosResidencia() {
		return itemDepartamentosResidencia;
	}

	/**
	 * @return itemsMunicipiosResidencia: Items of the departments shown in the
	 *         department of residence combobox interface user
	 */
	public List<SelectItem> getItemsMunicipiosResidencia() {
		return itemsMunicipiosResidencia;
	}

	/**
	 * @return itemsEstadosCivil: civil status items that are loaded into the
	 *         combo in the user interface.
	 */
	public List<SelectItem> getItemsEstadosCivil() {
		return itemsEstadosCivil;
	}

	/**
	 * @return itemsHrTypes: civil status items that are loaded into the combo
	 *         in the user interface.
	 */
	public List<SelectItem> getItemsHrTypes() {
		return itemsHrTypes;
	}

	/**
	 * @return itemsPaymentMethods: the method of payment items that are loaded
	 *         into the combo in the user interface.
	 */
	public List<SelectItem> getItemsPaymentMethods() {
		return itemsPaymentMethods;
	}

	/**
	 * @return nombreFotoHr: filename of the picture that is associated with
	 *         human resource
	 */
	public String getNombreFotoHr() {
		return nombreFotoHr;
	}

	/**
	 * @param nombreFotoHr
	 *            : filename of the picture that is associated with human
	 *            resource
	 */
	public void setNombreFotoHr(String nombreFotoHr) {
		this.nombreFotoHr = nombreFotoHr;
	}

	/**
	 * @return carpetaArchivos: the actual folder path where you load the Photos
	 *         of human resources
	 */
	public String getCarpetaArchivos() {
		this.carpetaArchivos = Constantes.CARPETA_ARCHIVOS_RECURSOS_HUMANOS
				+ Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_PERSONAS;
		return carpetaArchivos;
	}

	/**
	 * @return carpetaArchivosTemporal: temporary folder path where Photo charge
	 *         of human resources
	 */
	public String getCarpetaArchivosTemporal() {
		this.carpetaArchivosTemporal = Constantes.CARPETA_ARCHIVOS_RECURSOS_HUMANOS
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
	 * @return cargarFotoTemporal: Flag indicating whether the picture is loaded
	 *         from temporary location or not
	 */
	public boolean isCargarFotoTemporal() {
		return cargarFotoTemporal;
	}

	/**
	 * @param cargarFotoTemporal
	 *            : Flag indicating whether the picture is loaded from temporary
	 *            location or not
	 */
	public void setCargarFotoTemporal(boolean cargarFotoTemporal) {
		this.cargarFotoTemporal = cargarFotoTemporal;
	}

	/**
	 * @return fechaActual: variable that gets the current system date.
	 */
	public Date getFechaActual() {
		return fechaActual;
	}

	/**
	 * @param fechaActual
	 *            : variable that gets the current system date.
	 */
	public void setFechaActual(Date fechaActual) {
		this.fechaActual = fechaActual;
	}

	/**
	 * Initializes the search parameters to filter by hrTypes
	 * 
	 * @return consultarHrs(): method that queries resource types human, returns
	 *         to the template management.
	 */
	public String inicializarFiltro() {
		nombreBuscar = "";
		apellidoBuscar = "";
		paymentTypeBuscar = 0;
		return consultarHrs();
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of types of human resources
	 * 
	 * @modify 15/07/2015 Gerardo.Herrera
	 * 
	 * @return consultarHrs: method to query the types of human resources,
	 *         returns to the template management.
	 */
	public String inicializarBusqueda() {
		hrTypeBuscar = 0;
		nombreBuscar = "";
		apellidoBuscar = "";
		paymentTypeBuscar = 0;
		return consultarHrs();
	}

	/**
	 * Consult the list of hr
	 * 
	 * @modify 13/05/2015 Andres.Gomez
	 * @modify 14/07/2015 Gerardo.Herrera
	 * 
	 * @return retorno: Navigation rule that redirects to manage Human Resources
	 *         According condition
	 */
	public String consultarHrs() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validaciones = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listaHr = new ArrayList<Hr>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean desdeModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		String retorno = desdeModal ? "" : "gesHumanResources";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					bundleRecursosHumanos, unionMensajesBusqueda);
			Long cantidad = hrDao.cantidadHr(consulta, parametros);
			if (cantidad != null) {
				if (desdeModal) {
					paginadorForm.paginarRangoDefinido(cantidad, 5);
					this.listaHr = hrDao.consultarHr(paginadorForm.getInicio(),
							paginadorForm.getRango(), consulta, parametros);

				} else {
					paginador.paginar(cantidad);
					this.listaHr = hrDao.consultarHr(paginador.getInicio(),
							paginador.getRango(), consulta, parametros);
				}
			}
			if ((listaHr == null || listaHr.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaHr == null || listaHr.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				mensajeBusqueda = MessageFormat
						.format(message, bundleRecursosHumanos
								.getString("recurso_humano_label"),
								unionMensajesBusqueda);
			}
			if (cantidad != 0) {
				cargarDetallesHr();
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
			cargarComboHrTypes();
			cargarComboPaymentMethods();
			if (!"".equals(nombreBuscar)) {
				ControladorContexto.mensajeInformacion(
						"popupForm:tResponsable", mensajeBusqueda);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return retorno;
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @modify 14/07/2015 Gerardo.Herrera
	 * 
	 * @param consulta
	 *            : query to concatenate
	 * @param parametros
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * 
	 * @param bundleRecursosHumanos
	 *            : access language tags HR
	 * 
	 * @param unionMensajesBusqueda
	 *            : Message search
	 */
	private void busquedaAvanzada(StringBuilder consulta,
			List<SelectItem> parametros, ResourceBundle bundle,
			ResourceBundle bundleRecursosHumanos,
			StringBuilder unionMensajesBusqueda) {
		boolean agregarFiltro = false;
		String tipoRecursoHumano = "";
		String tipoPago = "";
		if ((this.nombreBuscar != null && !"".equals(this.nombreBuscar))) {
			consulta.append(agregarFiltro ? "AND " : "WHERE ");
			consulta.append(" UPPER(h.name) LIKE UPPER(:keywordNombre) ");
			SelectItem itemNombre = new SelectItem("%" + this.nombreBuscar
					+ "%", "keywordNombre");
			parametros.add(itemNombre);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"' + " ");
			agregarFiltro = true;
		}
		if (this.apellidoBuscar != null && !"".equals(this.apellidoBuscar)) {
			consulta.append(agregarFiltro ? "AND " : "WHERE ");
			consulta.append(" UPPER(h.familyName) LIKE UPPER(:keywordApellido) ");
			SelectItem itemApellido = new SelectItem("%" + this.apellidoBuscar
					+ "%", "keywordApellido");
			parametros.add(itemApellido);
			unionMensajesBusqueda.append(bundle.getString("label_apellido")
					+ ": " + '"' + this.apellidoBuscar + '"' + " ");
			agregarFiltro = true;
		}
		if (this.hrTypeBuscar > 0) {
			consulta.append(agregarFiltro ? "AND " : "WHERE ");
			consulta.append(" h.hrTypes.idHrType = :keywordHrType ");
			SelectItem itemHrType = new SelectItem(this.hrTypeBuscar,
					"keywordHrType");
			parametros.add(itemHrType);
			tipoRecursoHumano = nombreTipoBusqueda(itemsHrTypes, hrTypeBuscar);
			unionMensajesBusqueda.append(bundleRecursosHumanos
					.getString("tipo_recurso_humano_label")
					+ ": "
					+ '"'
					+ tipoRecursoHumano + '"' + " ");
			agregarFiltro = true;
		}
		if (this.paymentTypeBuscar > 0) {
			consulta.append(agregarFiltro ? "AND " : "WHERE ");
			consulta.append(" h.paymentMethods.idPaymentMethod = :keywordPaymentType ");
			SelectItem itemPaymentType = new SelectItem(this.paymentTypeBuscar,
					"keywordPaymentType");
			parametros.add(itemPaymentType);
			tipoPago = nombreTipoBusqueda(itemsPaymentMethods,
					paymentTypeBuscar);
			unionMensajesBusqueda.append(bundleRecursosHumanos
					.getString("recurso_humano_label_tipo_pago")
					+ ": "
					+ '"'
					+ tipoPago + '"' + " ");
			agregarFiltro = true;
		}
	}

	/**
	 * Lets get the value of an object in a List <SelectItem> passing as a
	 * search id
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param parametro
	 *            : List of objects type SelectItem
	 * @param id
	 *            : Object Identifier
	 * @return resultado: returns the value of the object sought
	 */
	private String nombreTipoBusqueda(List<SelectItem> parametro, int id) {
		String resultado = "";
		for (SelectItem tipos : parametro) {
			if (tipos.getValue() == (Integer) id) {
				resultado = tipos.getLabel();
			}
		}
		return resultado;
	}

	/**
	 * Method to edit or create a new human resource.
	 * 
	 * @param hr
	 *            : human resource that will add or edit
	 * 
	 * @return "regHumanResources": redirected to the template record human
	 *         Resources.
	 */
	public String agregarEditarHr(Hr hr) {
		try {
			if (hr != null) {
				this.hr = hr;
				this.nombreFotoHr = this.hr.getFoto();
				this.cargarFotoTemporal = false;
			} else {
				this.hr = new Hr();
				this.nombreFotoHr = null;
				this.fileUploadBean = new FileUploadBean();
				this.cargarFotoTemporal = true;
			}
			cargarCombos();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regHumanResources";
	}

	/**
	 * Delete method that allows a human resource database
	 * 
	 * @return consultarHrs(): human resources consulting the database and
	 *         returns to manage hr
	 */
	public String eliminarHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			hrDao.eliminarHr(hr);
			String format = MessageFormat
					.format(bundle.getString("message_registro_eliminar"),
							hr.getName());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarHrs();
	}

	/**
	 * Assigns the name of human resources to process validation, not to be
	 * repeated in the database.
	 * 
	 * @author Dario.Lopez
	 * 
	 * @param value
	 *            : field value is validated
	 */
	public void agregarNombreValidacionXSS(Object value) {
		String nombre = (String) value;
		this.hr.setName(nombre);
	}

	/**
	 * Validates the full name of human resources, so that no is repeated in the
	 * database and validates against XSS.
	 * 
	 * @modify 11/09/2015 Dario.Lopez
	 * 
	 * @param contexto
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value is validated
	 */
	public void validarNombreCompletoXSS(FacesContext contexto,
			UIComponent toValidate, Object value) {
		String apellido = (String) value;
		String nombre = (String) toValidate.getAttributes().get("nombre");
		String clientId = toValidate.getClientId(contexto);
		try {
			int id = hr.getIdHr();
			Hr hrAux = new Hr();
			hrAux = hrDao.nombreCompletoExiste(nombre, apellido, id);
			if (hrAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						mensajeExistencia, "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(apellido, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit human resources
	 * 
	 * @return consultarHrs: Redirects to manage human resources in updated list
	 */
	public String guardarHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		boolean seCambioLogo = false;
		String nombreFotoBorrar = null;
		try {
			/* No son campos requeridos */
			if (this.hr.getDepartamentoNacimiento().getId() == 0) {
				this.hr.setDepartamentoNacimiento(null);
			}
			if (this.hr.getMunicipioNacimiento().getId() == 0) {
				this.hr.setMunicipioNacimiento(null);
			}
			if (this.hr.getDepartamentoResidencia().getId() == 0) {
				this.hr.setDepartamentoResidencia(null);
			}
			if (this.hr.getMunicipioResidencia().getId() == 0) {
				this.hr.setMunicipioResidencia(null);
			}
			if (this.hr.getEstadoCivil().getId() == 0) {
				this.hr.setEstadoCivil(null);
			}

			if (hr.getIdHr() != 0) {
				if (this.hr.getFoto() != null && !"".equals(this.hr.getFoto())
						&& !this.hr.getFoto().equals(this.nombreFotoHr)) {
					this.borrarArchivoReal(this.hr.getFoto());
					seCambioLogo = true;
				} else if (hr.getFoto() == null
						&& fileUploadBean.getFileName() != null
						&& !"".equals(fileUploadBean.getFileName())) {
					seCambioLogo = true;
				}
				this.hr.setFoto(this.nombreFotoHr);
				if (hr.getFoto() != null && seCambioLogo) {
					nombreFotoBorrar = this.nombreFotoHr;
					/* Se carga la imagen en la ubicacion real */
					subirImagenUbicacionReal();
				}
				hrDao.editarHr(hr);
			} else {
				if (this.nombreFotoHr != null
						&& !"".equals(this.nombreFotoHr.trim())) {
					nombreFotoBorrar = this.nombreFotoHr;
					subirImagenUbicacionReal();
				}
				this.hr.setFoto(this.nombreFotoHr);
				mensajeRegistro = "message_registro_guardar";
				hrDao.guardarHr(hr);
			}
			/* Se borra el archivo temporal */
			if (nombreFotoBorrar != null && !"".equals(nombreFotoBorrar)) {
				this.borrarArchivo(nombreFotoBorrar);
			}
			String format = MessageFormat.format(
					bundle.getString(mensajeRegistro), hr.getName());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarHrs();
	}

	/**
	 * This method allows you to load combos country, department and
	 * municipality
	 * 
	 * @modify 14/07/2015 Gerardo.Herrera
	 * 
	 * @throws Exception
	 * 
	 */
	private void cargarCombos() throws Exception {
		/* Pais */
		itemsPaisesNacimiento = new ArrayList<SelectItem>();
		itemsPaisesResidencia = new ArrayList<SelectItem>();
		List<Pais> paises = paisDao.consultarPaisesVigentes();
		if (paises != null) {
			for (Pais pais : paises) {
				itemsPaisesNacimiento.add(new SelectItem(pais.getId(), pais
						.getNombre()));
				itemsPaisesResidencia.add(new SelectItem(pais.getId(), pais
						.getNombre()));
			}
		}
		/* Departamentos */
		cargarDepartamentosNacimiento();
		cargarDepartamentosResidencia();
		/* Municipios */
		cargarMunicipiosNacimiento();
		cargarMunicipiosResidencia();
		/* Estado Civil */
		itemsEstadosCivil = new ArrayList<SelectItem>();
		List<EstadoCivil> estadosCivilesVigentes = estadoCivilDao
				.consultarEstadosCivilesVigentes();
		if (estadosCivilesVigentes != null) {
			for (EstadoCivil estadoCivil : estadosCivilesVigentes) {
				itemsEstadosCivil.add(new SelectItem(estadoCivil.getId(),
						estadoCivil.getNombre()));
			}
		}
		cargarComboPaymentMethods();
		cargarComboHrTypes();
	}

	/**
	 * It allows charging combo types of human resources
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @throws Exception
	 */
	private void cargarComboHrTypes() throws Exception {
		itemsHrTypes = new ArrayList<SelectItem>();
		List<HrTypes> tiposHrVigentes = hrTypesDao.consultarHrTypesVigentes();
		if (tiposHrVigentes != null) {
			for (HrTypes hrType : tiposHrVigentes) {
				itemsHrTypes.add(new SelectItem(hrType.getIdHrType(), hrType
						.getName()));
			}
		}
	}

	/**
	 * It allows loading the item select the types of payment
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @throws Exception
	 */
	private void cargarComboPaymentMethods() throws Exception {
		itemsPaymentMethods = new ArrayList<SelectItem>();
		List<PaymentMethods> tiposPagoVigentes = paymentMethodsDao
				.consultarPaymentMethodsVigentes();
		if (tiposPagoVigentes != null) {
			for (PaymentMethods paymentMethod : tiposPagoVigentes) {
				itemsPaymentMethods.add(new SelectItem(paymentMethod
						.getIdPaymentMethod(), paymentMethod.getName()));
			}
		}
	}

	/**
	 * This method makes the request of the departments registered in the
	 * database, a country associated with selected birth
	 * 
	 */
	public void cargarDepartamentosNacimiento() {
		itemDepartamentosNacimiento = new ArrayList<SelectItem>();
		itemsMunicipiosNacimiento = new ArrayList<SelectItem>();
		try {
			Pais paisDeNacimiento = hr.getPaisNacimiento();
			if (paisDeNacimiento != null && paisDeNacimiento.getId() > 0) {
				short idPaisNacimiento = paisDeNacimiento.getId();
				List<Departamento> departamentosNacimiento = departamentoDao
						.consultarDepartamentosPaisVigentes(idPaisNacimiento);
				if (departamentosNacimiento != null) {
					for (Departamento depNacimiento : departamentosNacimiento) {
						itemDepartamentosNacimiento.add(new SelectItem(
								depNacimiento.getId(), depNacimiento
										.getNombre()));
					}
				}
				cargarMunicipiosNacimiento();
			} else {
				hr.setDepartamentoNacimiento(new Departamento());
				hr.setMunicipioNacimiento(new Municipio());
				hr.getDepartamentoNacimiento().setId(0);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the departments registered in the
	 * database, associated with a selected country residence.
	 */
	public void cargarDepartamentosResidencia() {
		itemDepartamentosResidencia = new ArrayList<SelectItem>();
		itemsMunicipiosResidencia = new ArrayList<SelectItem>();
		try {
			Pais paisDeResidencia = hr.getPaisResidencia();
			if (paisDeResidencia != null && paisDeResidencia.getId() > 0) {
				short idPaisResidencia = paisDeResidencia.getId();
				List<Departamento> departamentosResidencia = departamentoDao
						.consultarDepartamentosPaisVigentes(idPaisResidencia);
				if (departamentosResidencia != null) {
					for (Departamento depResidencia : departamentosResidencia) {
						itemDepartamentosResidencia.add(new SelectItem(
								depResidencia.getId(), depResidencia
										.getNombre()));
					}
				}
				cargarMunicipiosResidencia();
			} else {
				hr.setDepartamentoResidencia(new Departamento());
				hr.setMunicipioResidencia(new Municipio());
				hr.getDepartamentoResidencia().setId(0);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered to the
	 * base data associated with the department of birth.
	 */
	public void cargarMunicipiosNacimiento() {
		itemsMunicipiosNacimiento = new ArrayList<SelectItem>();
		try {
			Departamento departamentoNacimiento = hr
					.getDepartamentoNacimiento();
			if (departamentoNacimiento != null
					&& departamentoNacimiento.getId() > 0
					&& this.itemDepartamentosNacimiento.size() > 0) {
				int idDepartamentoNacimiento = departamentoNacimiento.getId();
				List<Municipio> municipiosNacimiento = municipioDao
						.consultarMunicipiosVigentes(idDepartamentoNacimiento);
				if (municipiosNacimiento != null) {
					for (Municipio munNacimiento : municipiosNacimiento) {
						itemsMunicipiosNacimiento.add(new SelectItem(
								munNacimiento.getId(), munNacimiento
										.getNombre()));
					}
				}
			} else {
				itemsMunicipiosNacimiento = new ArrayList<SelectItem>();
				hr.setDepartamentoNacimiento(new Departamento());
				hr.setMunicipioNacimiento(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the request of the municipalities registered to the
	 * base data associated with the department of residence.
	 */
	public void cargarMunicipiosResidencia() {
		itemsMunicipiosResidencia = new ArrayList<SelectItem>();
		try {
			Departamento departamentoResidencia = hr
					.getDepartamentoResidencia();
			if (departamentoResidencia != null
					&& departamentoResidencia.getId() > 0
					&& this.itemDepartamentosResidencia.size() > 0) {
				int idDepartamentoResidencia = departamentoResidencia.getId();
				List<Municipio> municipiosResidencia = municipioDao
						.consultarMunicipiosVigentes(idDepartamentoResidencia);
				if (municipiosResidencia != null) {
					for (Municipio munResidencia : municipiosResidencia) {
						itemsMunicipiosResidencia.add(new SelectItem(
								munResidencia.getId(), munResidencia
										.getNombre()));
					}
				}
			} else {
				itemsMunicipiosResidencia = new ArrayList<SelectItem>();
				hr.setDepartamentoResidencia(new Departamento());
				hr.setMunicipioResidencia(new Municipio());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method fills the various objects associated with a human resource
	 * 
	 * @throws Exception
	 */
	private void cargarDetallesHr() throws Exception {
		List<Hr> humanResources = new ArrayList<Hr>();
		humanResources.addAll(this.listaHr);
		this.listaHr = new ArrayList<Hr>();
		for (Hr hr : humanResources) {
			cargarDetallesUnHumanResource(hr);
			this.listaHr.add(hr);
		}
	}

	/**
	 * Method of uploading the details of a human resource.
	 * 
	 * @param hr
	 *            : human resources which will carry the details.
	 * @throws Exception
	 */
	private void cargarDetallesUnHumanResource(Hr hr) throws Exception {
		int idHr = hr.getIdHr();
		/* Se consultan los objetos */
		HrTypes hrTypes = (HrTypes) this.hrDao.consultarObjetoHr("hrTypes",
				idHr);
		PaymentMethods paymentMethods = (PaymentMethods) this.hrDao
				.consultarObjetoHr("paymentMethods", idHr);
		EstadoCivil estadoCivil = (EstadoCivil) this.hrDao.consultarObjetoHr(
				"estadoCivil", idHr);
		Pais paisNacimiento = (Pais) this.hrDao.consultarObjetoHr(
				"paisNacimiento", idHr);
		Departamento departamentoNacimiento = (Departamento) this.hrDao
				.consultarObjetoHr("departamentoNacimiento", idHr);
		Municipio municipioNacimiento = (Municipio) this.hrDao
				.consultarObjetoHr("municipioNacimiento", idHr);
		Pais paisResidencia = (Pais) this.hrDao.consultarObjetoHr(
				"paisResidencia", idHr);
		Departamento departamentoResidencia = (Departamento) this.hrDao
				.consultarObjetoHr("departamentoResidencia", idHr);
		Municipio municipioResidencia = (Municipio) this.hrDao
				.consultarObjetoHr("municipioResidencia", idHr);
		/* Se asignan al human resource */
		hr.setHrTypes(hrTypes);
		hr.setPaymentMethods(paymentMethods);
		hr.setEstadoCivil(estadoCivil);
		hr.setPaisNacimiento(paisNacimiento);
		hr.setDepartamentoNacimiento(departamentoNacimiento);
		hr.setMunicipioNacimiento(municipioNacimiento);
		hr.setPaisResidencia(paisResidencia);
		hr.setDepartamentoResidencia(departamentoResidencia);
		hr.setMunicipioResidencia(municipioResidencia);
	}

	/**
	 * Delete the file name.
	 */
	public void borrarFilename() {
		if (nombreFotoHr != null && !"".equals(nombreFotoHr)
				&& !nombreFotoHr.equals(hr.getFoto())
				&& this.cargarFotoTemporal) {
			borrarArchivo(nombreFotoHr);
		}
		nombreFotoHr = null;
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
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
	 * It allows you to load the file system
	 * 
	 * @param e
	 *            : Fileupload event for the file to be up server.
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
			ControladorContexto.mensajeError("formRegistrarHr:uploadFoto",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			mensaje = "error_carga_archivo";
		}
		if (!"".equals(mensaje)) {
			ControladorContexto.mensajeError("formRegistrarHr:uploadFoto",
					bundle.getString(mensaje));
		}
		if (hr.getIdHr() != 0) {
			cargarFotoTemporal = true;
		}
		nombreFotoHr = fileUploadBean.getFileName();
	}

	/**
	 * Add photo image to the actual folder
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

		FileUploadBean.fileExist(destino1);
		FileUploadBean.fileExist(destino2);

		File fileOrigen = new File(origen, fileUploadBean.getFileName());
		File fileDestino1 = new File(destino1, fileUploadBean.getFileName());
		File fileDestino2 = new File(destino2, fileUploadBean.getFileName());

		FileUploadBean.copyFile(fileOrigen, fileDestino1);
		FileUploadBean.copyFile(fileOrigen, fileDestino2);
	}

	/**
	 * This method allows dynamically calculate the cost per hour from the
	 * information of the payment information in the form registrarHR view
	 * 
	 * @author Dario.Lopez
	 * 
	 * @return hourCost: cost per hour
	 */
	public double calcularCostoPorHora() {
		double hourCost = 0;
		double annualWage = 0;
		double hoursPerDay = 0;
		double totalNumbersDays = 0;
		if (hr.getTotalNumbersDays() != null && hr.getTotalNumbersDays() != 0
				&& hr.getHoursPerDay() != 0) {
			annualWage = this.hr.getAnnualWage();
			hoursPerDay = this.hr.getHoursPerDay();
			totalNumbersDays = this.hr.getTotalNumbersDays();
			hourCost = (annualWage / (hoursPerDay * totalNumbersDays));
			hr.setHourCost(hourCost);
		} else {
			hourCost = 0;
			return hourCost;
		}
		return hourCost;
	}

	/**
	 * Assigns a state maternityBreastFeeding considering gender HR
	 * 
	 * @author Gerardo.Herrera
	 */
	public void assignMaternityBreastFeeding() {
		if (hr.getGenero().equals(Constantes.GENERO) && hr.getGenero() != null) {
			hr.setMaternityBreastFeeding(false);
		}
	}
}
