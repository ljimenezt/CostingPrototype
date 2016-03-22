package co.informatix.erp.informacionBase.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.informacionBase.dao.ConversionUnidadDao;
import co.informatix.erp.informacionBase.dao.TipoUnidadDao;
import co.informatix.erp.informacionBase.dao.UnidadMedidaDao;
import co.informatix.erp.informacionBase.entities.ConversionUnidad;
import co.informatix.erp.informacionBase.entities.ConversionUnidadPK;
import co.informatix.erp.informacionBase.entities.TipoUnidad;
import co.informatix.erp.informacionBase.entities.UnidadMedida;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class is all the logic related to the creation, update and delete
 * measurement units, unit types and conversions that can occur between them
 * 
 * @author Angelica.Amaya
 * @modify 01/10/2015 Johnatan.Naranjo
 * 
 */

@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class UnidadMedidaAction implements Serializable {

	@EJB
	private UnidadMedidaDao unidadMedidaDao;
	@EJB
	private ConversionUnidadDao conversionUnidadMedidaDao;
	@EJB
	private TipoUnidadDao tipoUnidadDao;
	@Inject
	private IdentityAction identity;

	private HashMap<String, Integer> itemsUnidadInicial;
	private HashMap<String, Integer> itemsUnidadFinal;
	private List<UnidadMedida> listaUnidadesMedida;
	private List<ConversionUnidad> listaConversionUnidades;
	private List<TipoUnidad> tiposUnidades;
	private List<SelectItem> itemsTipoUnidades;
	private Paginador pagination = new Paginador();
	private UnidadMedida unidadMedida;
	private TipoUnidad tipoUnidad;
	private ConversionUnidad conversionUnidad;
	private String filtroBusqueda;
	private String nombreBuscar;
	private Integer idTipoUnidad = 0;
	private int tipoVigenteSelect = 1;
	private boolean esEdicion;

	/**
	 * @return nombreBuscar: name by which you want to query unit measure.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * 
	 * @param nombreBuscar
	 *            : name by which you want to query unit measure.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * 
	 * @return idTipoUnidad: id the type of the unit of measurement for the
	 *         which you want to consult.
	 */
	public Integer getIdTipoUnidad() {
		return idTipoUnidad;
	}

	/**
	 * @param idTipoUnidad
	 *            : id the type of the unit of measurement for the which you
	 *            want to consult.
	 */
	public void setIdTipoUnidad(Integer idTipoUnidad) {
		this.idTipoUnidad = idTipoUnidad;
	}

	/**
	 * @return filtroBusqueda: Search filter that the user selects in the
	 *         parameters the user interface.
	 */
	public String getFiltroBusqueda() {
		return filtroBusqueda;
	}

	/**
	 * @param filtroBusqueda
	 *            : Search filter that the user selects in the parameters the
	 *            user interface.
	 */
	public void setFiltroBusqueda(String filtroBusqueda) {
		this.filtroBusqueda = filtroBusqueda;
	}

	/**
	 * 
	 * @return unidadMedida:unit of measure to which the insertion is performed
	 *         or edition.
	 */
	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * 
	 * @param unidadMedida
	 *            : unit of measure to which the insertion is made or edition.
	 */
	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	/**
	 * 
	 * @return esEdicion: true if editing or false otherwise
	 */
	public boolean isEsEdicion() {
		return esEdicion;
	}

	/**
	 * @param esEdicion
	 *            : true if editing or false otherwise
	 */
	public void setEsEdicion(boolean esEdicion) {
		this.esEdicion = esEdicion;
	}

	/**
	 * @return tipoUnidad: Gets the object variable of the type of unit measure.
	 */
	public TipoUnidad getTipoUnidad() {
		return tipoUnidad;
	}

	/**
	 * @param tipoUnidad
	 *            : Gets the object variable of the type of unit measure.
	 */
	public void setTipoUnidad(TipoUnidad tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	/**
	 * @return listaUnidadesMedida:List of units as they are loaded in User
	 *         Interface.
	 */
	public List<UnidadMedida> getListaUnidadesMedida() {
		return listaUnidadesMedida;
	}

	/**
	 * @param listaUnidadesMedida
	 *            : List of units as they are loaded in User Interface.
	 */
	public void setListaUnidadesMedida(List<UnidadMedida> listaUnidadesMedida) {
		this.listaUnidadesMedida = listaUnidadesMedida;
	}

	/**
	 * @return conversionUnidad: gets the object variable conversion of unit of
	 *         measure
	 */
	public ConversionUnidad getConversionUnidad() {
		return conversionUnidad;
	}

	/**
	 * @param conversionUnidad
	 *            : gets the object variable conversion of unit of measure
	 */
	public void setConversionUnidad(ConversionUnidad conversionUnidad) {
		this.conversionUnidad = conversionUnidad;
	}

	/**
	 * @return listaConversionUnidades: list of conversions measurement units.
	 */
	public List<ConversionUnidad> getListaConversionUnidades() {
		return listaConversionUnidades;
	}

	/**
	 * @param listaConversionUnidades
	 *            : list of conversions measurement units.
	 */
	public void setListaConversionUnidades(
			List<ConversionUnidad> listaConversionUnidades) {
		this.listaConversionUnidades = listaConversionUnidades;
	}

	/**
	 * @return tiposUnidades: list of the types of measurement units
	 */
	public List<TipoUnidad> getTiposUnidades() {
		return tiposUnidades;
	}

	/**
	 * @param tiposUnidades
	 *            : list of the types of measurement units
	 */
	public void setTiposUnidades(List<TipoUnidad> tiposUnidades) {
		this.tiposUnidades = tiposUnidades;
	}

	/**
	 * @return tipoVigenteSelect: variable gets the value 1 to select the list
	 *         of existing or non-existing 2
	 */
	public int getTipoVigenteSelect() {
		return tipoVigenteSelect;
	}

	/**
	 * @param tipoVigenteSelect
	 *            : variable gets the value 1 to select the list of existing or
	 *            non-existing 2
	 */
	public void setTipoVigenteSelect(int tipoVigenteSelect) {
		this.tipoVigenteSelect = tipoVigenteSelect;
	}

	/**
	 * @return pagination: paging management from the list of units of measure
	 *         they may be in view
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : paging management from the list of units of measure they may
	 *            be in view
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return itemsUnidadInicial: List of items of the initial units to convert
	 */
	public HashMap<String, Integer> getItemsUnidadInicial() {
		return itemsUnidadInicial;
	}

	/**
	 * @param itemsUnidadInicial
	 *            : List of items of the initial units to convert
	 */
	public void setItemsUnidadInicial(
			HashMap<String, Integer> itemsUnidadInicial) {
		this.itemsUnidadInicial = itemsUnidadInicial;
	}

	/**
	 * 
	 * @return itemsUnidadFinal: List of items of the end units convert
	 */
	public HashMap<String, Integer> getItemsUnidadFinal() {
		return itemsUnidadFinal;
	}

	/**
	 * @param itemsUnidadFinal
	 *            : List of items of the end units convert
	 */
	public void setItemsUnidadFinal(HashMap<String, Integer> itemsUnidadFinal) {
		this.itemsUnidadFinal = itemsUnidadFinal;
	}

	/**
	 * @return itemsTipoUnidades: List of items of measurement units.
	 */
	public List<SelectItem> getItemsTipoUnidades() {
		return itemsTipoUnidades;
	}

	/**
	 * @param itemsTipoUnidades
	 *            : List of items of measurement units.
	 * 
	 */
	public void setItemsTipoUnidades(List<SelectItem> itemsTipoUnidades) {
		this.itemsTipoUnidades = itemsTipoUnidades;
	}

	/**
	 * It allows charging interface to record or edit a unit of measure.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param unidadMedida
	 *            : Unit of measurement that is published in edit mode.
	 * @return regUnidadMedida: Navigation rule that goes to form unit of
	 *         measure.
	 */
	public String registrarUnidadMedida(UnidadMedida unidadMedida) {
		try {
			cargarComboTipoUnidad();
			if (unidadMedida == null) {
				this.unidadMedida = new UnidadMedida();
			} else {
				this.unidadMedida = unidadMedida;
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regUnidadMedida";
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @param casoUnidadMedida
	 *            : variable which provides information about the consultation
	 *            or case Unit of measure: 'true' queries and measurement units
	 *            'false' drive types.
	 * @return consultarUnidadesMedida o consultarTipoUnidades: If according to
	 *         parameter measurement unit, returning to the template management.
	 */
	public String inicializarBusqueda(boolean casoUnidadMedida) {
		this.nombreBuscar = "";
		if (casoUnidadMedida) {
			this.idTipoUnidad = 0;
			return consultarUnidadesMedida();
		} else {
			return consultarTipoUnidades();
		}
	}

	/**
	 * Change the validity of the measurement units that do not have associated
	 * records.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param vigente
	 *            : boolean to find out the validity ends with 'true' or INICA
	 *            with 'false', the selected record in the user interface.
	 * @return String: Navigation rule that directs me to the management of the
	 *         measurement units.
	 */
	public String cambiarVigenciaUnidadMedida(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";
		boolean filtro = false;
		try {
			if (this.tipoUnidad != null) {
				filtro = true;
			}
			if (vigente) {
				unidadMedida.setFechaFinVigencia(new Date());
				unidadMedidaDao.editarUnidadMedida(unidadMedida);
			} else {
				mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
				unidadMedida.setFechaFinVigencia(null);
				unidadMedida.setUserName(identity.getUserName());
				unidadMedidaDao.editarUnidadMedida(unidadMedida);
			}
			ControladorContexto.mensajeInformacion(null,
					bundle.getString(mensajeCambioVigencia) + ": "
							+ unidadMedida.getNombre());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarUnidadesMedida(filtro);
	}

	/**
	 * Method to load the list of units of measure without receiving parameters
	 * as the pager is called from a template, also tipoUnidad initializes the
	 * object to not load the list this filter
	 * 
	 * @author marisol.calderon
	 * 
	 * @return consultarUnidadesMedida: the method for querying measurement
	 *         units and redirect management template.
	 */
	public String consultarUnidadesMedida() {
		this.tipoUnidad = null;
		return consultarUnidadesMedida(false);
	}

	/**
	 * This method redirects me to the page of the list of units of measure
	 * classified as existing or not existing.
	 * 
	 * @author Angelica.Amaya
	 * @modify 09/12/2011 marisol.calderon
	 * @modify 04/10/2012 Adonay.Mantilla
	 * 
	 * @param filtro
	 *            : variable that shows whether the list of units As is filtered
	 *            or not tipoUnidad
	 * 
	 * @return gesUnidadMedida: lets you load User Interface for management of
	 *         measurement units
	 */
	public String consultarUnidadesMedida(boolean filtro) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleInformacionGeneral = ControladorContexto
				.getBundle("mensajeInformacionBase");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String mensajeBusqueda = "";
		StringBuilder unionMensajesBusqueda = new StringBuilder();

		StringBuilder condicionVigencia = new StringBuilder();
		try {
			cargarComboTipoUnidad();
			if (tipoVigenteSelect == 1) {
				condicionVigencia.append(Constantes.IS_NULL + " ");
			}
			if (tipoVigenteSelect == 2) {
				condicionVigencia.append(Constantes.IS_NOT_NULL + " ");
			}
			if (this.tipoUnidad != null && filtro) {
				condicionVigencia.append(" AND um.tipoUnidad = :tipoUnidad");
			}
			busquedaAvanzadaUnidadMedida(bundle, unionMensajesBusqueda);
			Long cantidadUnidadesMedida = unidadMedidaDao
					.cantidadUnidadesMedida(condicionVigencia, this.tipoUnidad,
							this.nombreBuscar, this.idTipoUnidad);

			if (cantidadUnidadesMedida != null) {
				pagination.paginar(cantidadUnidadesMedida);
			}
			this.listaUnidadesMedida = unidadMedidaDao.consultarUnidadesMedida(
					pagination.getInicio(), pagination.getRango(),
					condicionVigencia, this.tipoUnidad, this.nombreBuscar,
					this.idTipoUnidad);
			if ((listaUnidadesMedida == null || listaUnidadesMedida.size() <= 0)
					&& (this.nombreBuscar != null
							&& !"".equals(this.nombreBuscar) || idTipoUnidad != 0)) {

				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);

			} else if (listaUnidadesMedida == null
					|| listaUnidadesMedida.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));

			} else if ((this.nombreBuscar != null && !""
					.equals(this.nombreBuscar)) || idTipoUnidad != 0) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleInformacionGeneral
										.getString("unidad_medida_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesUnidadMedida";
	}

	/**
	 * This method constructs the messages to be displayed according to criteria
	 * user-selected search.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @param bundle
	 *            : variable that allows to know the file messages Generic
	 *            languages.
	 * @param unionMensajesBusqueda
	 *            : variable that stores messages depending on the selected
	 *            parameters.
	 */
	private void busquedaAvanzadaUnidadMedida(ResourceBundle bundle,
			StringBuilder unionMensajesBusqueda) {
		ResourceBundle bundleInformacionGeneral = ControladorContexto
				.getBundle("mensajeInformacionBase");
		boolean seAgregoMens = false;
		String nombreTipoUnidad = (String) ValidacionesAction.getLabel(
				itemsTipoUnidades, idTipoUnidad);
		String comaEspacio = ", ";
		if (idTipoUnidad != 0) {
			unionMensajesBusqueda.append(bundleInformacionGeneral
					.getString("tipo_unidad_medida_label_s")
					+ ": "
					+ '"'
					+ nombreTipoUnidad + '"');
			seAgregoMens = true;
		}
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			unionMensajesBusqueda.append((seAgregoMens ? comaEspacio : "")
					+ bundle.getString("label_nombre") + ": " + '"'
					+ this.nombreBuscar + '"');
		}
	}

	/**
	 * This method is responsible for the validation of the name of the unit of
	 * measure who do not register for two with the same value, also not valid
	 * register malicious code
	 * 
	 * @author marisol.calderon
	 * 
	 * @param contexto
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value is validated
	 */
	public void validarNombreUnidadMedidaXSS(FacesContext contexto,
			UIComponent toValidate, Object value) {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(contexto);
		UIInput findComponent = (UIInput) toValidate
				.findComponent("cmbTipoUnidad");
		Integer tipoUnidad = (Integer) findComponent.getValue();
		String mensajeVigencia = "label_el,label_nombre,message_ya_existe_sin_vigencia";
		try {
			if (tipoUnidad != null) {
				Integer id = this.unidadMedida.getId();
				String nombreCapitalize = WordUtils.capitalizeFully(nombre);
				UnidadMedida unidadMedidaTemp = new UnidadMedida();
				if (id != null) {
					unidadMedidaTemp = unidadMedidaDao
							.nombreExisteUnidadActualizar(nombreCapitalize, id,
									tipoUnidad);
				} else {
					unidadMedidaTemp = unidadMedidaDao.nombreUnidadExiste(
							nombreCapitalize, tipoUnidad);
				}
				if (unidadMedidaTemp != null) {
					if (unidadMedidaTemp.getFechaFinVigencia() != null) {
						ControladorContexto.mensajeErrorEspecifico(clientId,
								mensajeVigencia, "mensaje");
						((UIInput) toValidate).setValid(false);
					} else {
						mensajeVigencia = "label_el,label_nombre,message_ya_existe_verifique";
						ControladorContexto.mensajeErrorEspecifico(clientId,
								mensajeVigencia, "mensaje");
						((UIInput) toValidate).setValid(false);
					}
				}
			}
			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.letras.numeros.caracteres")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * This method is responsible for the validation of the abbreviation of the
	 * unit As for two same can not register, also not valid register malicious
	 * code
	 * 
	 * @author marisol.calderon
	 * @modify 21/12/2011 Gabriel.Moreno
	 * 
	 * @param contexto
	 *            : Context of view.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 */
	public void validarAbreviaturaUnidadXSS(FacesContext contexto,
			UIComponent toValidate, Object value) {
		String abreviatura = (String) value;
		String clientId = toValidate.getClientId(contexto);
		UIInput findComponent = (UIInput) toValidate
				.findComponent("cmbTipoUnidad");
		Integer tipoUnidad = (Integer) findComponent.getValue();
		String mensajeExistencia = "message_ya_existe_sin_vigencia";
		try {
			if (tipoUnidad != null) {
				Integer id = this.unidadMedida.getId();
				UnidadMedida unidadMedidaTemp = new UnidadMedida();
				if (id != null) {
					unidadMedidaTemp = unidadMedidaDao
							.nombreExisteAbreviaturaActualizar(abreviatura, id,
									tipoUnidad);
				} else {
					unidadMedidaTemp = unidadMedidaDao.nombreAbreviaturaExiste(
							abreviatura, tipoUnidad);
				}
				if (unidadMedidaTemp != null) {
					if (unidadMedidaTemp.getFechaFinVigencia() != null) {
						ControladorContexto.mensajeErrorEspecifico(clientId,
								mensajeExistencia, "mensaje");
						((UIInput) toValidate).setValid(false);
					} else {
						mensajeExistencia = "message_ya_existe_verifique";
						ControladorContexto.mensajeErrorEspecifico(clientId,
								mensajeExistencia, "mensaje");
						((UIInput) toValidate).setValid(false);
					}
				}
			}
			if (!EncodeFilter.validarXSS(abreviatura, clientId,
					"locate.regex.letras.numeros.caracteres")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the creation or modification of a unit of measurement
	 * and recorded in the database.
	 * 
	 * @author Angelica.Amaya
	 * @modify 09/12/2011 marisol.calderon
	 * @modify 21/12/2011 Gabriel.Moreno
	 * 
	 * @return Navigation rule that addresses the management units measure.
	 */
	public String guardarUnidadMedida() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		boolean filtro = this.tipoUnidad != null ? true : false;
		String key = "message_registro_modificar";
		try {
			this.unidadMedida.setUserName(identity.getUserName());
			unidadMedida.setNombre(WordUtils.capitalizeFully(unidadMedida
					.getNombre()));
			if (unidadMedida.getId() != 0) {
				unidadMedidaDao.editarUnidadMedida(this.unidadMedida);

			} else {
				key = "message_registro_guardar";
				unidadMedida.setFechaRegistro(new Date());
				unidadMedidaDao.agregarUnidadMedida(unidadMedida);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							unidadMedida.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarUnidadesMedida(filtro);
	}

	/**
	 * This method consultation units associated with the type of unit of
	 * measure, allowing perform management
	 * 
	 * @author Angelica.Amaya
	 * @modify 05/12/2011 marisol.calderon
	 * 
	 * @param tipoUnidad
	 *            : type of unit to which the measurement units are loaded
	 *            associated
	 * @return: consulting method of measurement units and redirects the
	 *          template management.
	 */
	public String modificarUnidadesAsociadas(TipoUnidad tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
		this.unidadMedida = new UnidadMedida();
		return consultarUnidadesMedida(true);
	}

	/**
	 * This method modifies the validity of one type of unit of measure
	 * 
	 * @author Angelica.Amaya
	 * @modify 02/12/2011 marisol.calderon
	 * 
	 * @param vigente
	 *            : boolean value that shows whether they started or ended the
	 *            term
	 * @return: consultarTipoUnidades, method to query the types of units
	 *          measurement
	 */
	public String cambiarVigenciaTipoUnidadMedida(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensaje = "message_fin_vigencia_satisfactorio";
		try {
			if (vigente) {
				this.tipoUnidad.setFechaFinVigencia(new Date());
			} else {
				this.tipoUnidad.setFechaInicioVigencia(new Date());
				this.tipoUnidad.setFechaFinVigencia(null);
				mensaje = "message_inicio_vigencia_satisfactorio";
			}
			this.tipoUnidad.setUserName(identity.getUserName());
			tipoUnidadDao.modificarTipoUnidad(this.tipoUnidad);
			ControladorContexto.mensajeInformacion(null,
					bundle.getString(mensaje) + ": " + tipoUnidad.getNombre());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarTipoUnidades();
	}

	/**
	 * This method makes the query for existing units types shown in a combo of
	 * selection.
	 * 
	 * @author Angelica.Amaya
	 * @modify 09/12/2011 marisol.calderon
	 * @modify 04/10/2012 Adonay.Mantilla
	 * 
	 * @throws Exception
	 */
	private void cargarComboTipoUnidad() throws Exception {
		itemsTipoUnidades = new ArrayList<SelectItem>();
		List<TipoUnidad> resTiposUnidades = tipoUnidadDao
				.consultarTipoUnidadesVigentes();
		if (resTiposUnidades != null) {
			for (TipoUnidad tu : resTiposUnidades) {
				itemsTipoUnidades
						.add(new SelectItem(tu.getId(), tu.getNombre()));
			}
		}
	}

	/**
	 * This method makes the query types of units registered in the information
	 * system
	 * 
	 * @author Angelica.Amaya
	 * @modify 09/12/2011 marisol.calderon
	 * @modify 04/10/2012 Adonay.Mantilla
	 * 
	 * @return gesTipoUnidadMedida: redirected to the page type listing Unit of
	 *         measure
	 */
	public String consultarTipoUnidades() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleInformacionGeneral = ControladorContexto
				.getBundle("mensajeInformacionBase");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";

		tiposUnidades = new ArrayList<TipoUnidad>();
		try {
			busquedaAvanzadaTipoUnidad(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidadtipoUnidad = tipoUnidadDao.cantidadTipoUnidad(
					this.tipoVigenteSelect, consulta, parametros);
			if (cantidadtipoUnidad != null) {
				pagination.paginar(cantidadtipoUnidad);
			}
			tiposUnidades = tipoUnidadDao.consultarTipoUnidades(
					pagination.getInicio(), pagination.getRango(),
					this.tipoVigenteSelect, consulta, parametros);
			if ((tiposUnidades == null || tiposUnidades.size() <= 0)
					&& (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (tiposUnidades == null || tiposUnidades.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleInformacionGeneral
										.getString("tipo_unidad_medida_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesTipoUnidadMedida";
	}

	/**
	 * This method constructs the messages to be displayed according to criteria
	 * user-selected search.
	 * 
	 * @author Adonay.Mantilla
	 * 
	 * @param consulta
	 *            : string that stores the consultation carried out on the
	 *            damage.
	 * @param parametros
	 *            : parameters selected by the user.
	 * @param bundle
	 *            : variable that allows to know the generic languages system.
	 * @param unionMensajesBusqueda
	 *            : variable that stores messages displayed to the user
	 *            according to the selected parameters.
	 */
	private void busquedaAvanzadaTipoUnidad(StringBuilder consulta,
			List<SelectItem> parametros, ResourceBundle bundle,
			StringBuilder unionMensajesBusqueda) {

		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			SelectItem item = new SelectItem("%" + nombreBuscar + "%",
					"keyword");
			consulta.append("AND UPPER(tu.nombre) LIKE UPPER(:keyword) ");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * This method is used to redirect page creation or editing the type of unit
	 * of measure.
	 * 
	 * @modify 02/12/2011 marisol.calderon
	 * 
	 * @param tipoUnidad
	 *            : object variable of the type of unit of measure edited.
	 * @return regTipoUnidadMedida: redirected to the page creation or edition
	 *         of a unit type.
	 * 
	 * 
	 */
	public String registrarTipoUnidad(TipoUnidad tipoUnidad) {
		if (tipoUnidad != null) {
			this.tipoUnidad = tipoUnidad;
		} else {
			this.tipoUnidad = new TipoUnidad();
		}
		return "regTipoUnidadMedida";

	}

	/**
	 * This method makes the update of a type of measurement unit determined.
	 * Given name availability and if this Currently the system
	 * 
	 * @author Angelica.Amaya
	 * @modify 05/12/2011 marisol.calderon
	 * 
	 * @return consultarTipoUnidades(): see the types of units and returns to
	 *         its existing management
	 */
	public String guardarTipoUnidadMedida() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		try {
			tipoUnidad.setNombre(WordUtils.capitalizeFully(tipoUnidad
					.getNombre()));
			tipoUnidad.setUserName(identity.getUserName());

			if (tipoUnidad.getId() != null) {
				tipoUnidadDao.modificarTipoUnidad(this.tipoUnidad);
			} else {
				key = "message_registro_guardar";
				this.tipoUnidad.setFechaRegistro(new Date());
				tipoUnidadDao.crearTipoUnidad(this.tipoUnidad);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							tipoUnidad.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarTipoUnidades();
	}

	/**
	 * 
	 This method is responsible for the validation of the unit type name who
	 * do not register for two with the same value, also not valid register
	 * malicious code
	 * 
	 * @author angelica.amaya
	 * @modify 05/12/2011 marisol.calderon
	 * 
	 * @param contexto
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value is validated
	 */
	public void validarNombreTipoUnidadXSS(FacesContext contexto,
			UIComponent toValidate, Object value) {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(contexto);
		String mensajeExistencia = "label_el,label_nombre,message_ya_existe_sin_vigencia";
		try {
			Integer id = this.tipoUnidad.getId();
			String nombreCapitalize = WordUtils.capitalizeFully(nombre);
			TipoUnidad tipoUnidadTemp = new TipoUnidad();
			if (id != null) {
				tipoUnidadTemp = tipoUnidadDao
						.nombreExisteTipoUnidadActualizar(nombreCapitalize, id);
			} else {
				tipoUnidadTemp = tipoUnidadDao
						.nombreTipoUnidadExiste(nombreCapitalize);
			}
			if (tipoUnidadTemp != null) {
				if (tipoUnidadTemp.getFechaFinVigencia() != null) {
					ControladorContexto.mensajeErrorEspecifico(clientId,
							mensajeExistencia, "mensaje");
					((UIInput) toValidate).setValid(false);
				} else {
					mensajeExistencia = "label_el,label_nombre,message_ya_existe_verifique";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							mensajeExistencia, "mensaje");
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.letras.numeros.caracteres")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * This method makes reference to all units conversions As recorded in the
	 * system.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @return gesConversionUnidadMedida: redirected to the page listing
	 *         conversion of measurement units.
	 */
	public String consultarConversionUnidades() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleInformacionBase = ControladorContexto
				.getBundle("mensajeInformacionBase");

		this.listaConversionUnidades = new ArrayList<ConversionUnidad>();
		try {
			String mensajeBusqueda = "";
			ValidacionesAction validaciones = ControladorContexto
					.getContextBean(ValidacionesAction.class);
			pagination.paginar(conversionUnidadMedidaDao
					.cantidadConversionUnidades(this.filtroBusqueda));
			this.listaConversionUnidades = conversionUnidadMedidaDao
					.consultarConversionUnidades(pagination.getInicio(),
							pagination.getRango(), this.filtroBusqueda);
			for (ConversionUnidad conversion : listaConversionUnidades) {
				conversion.getLlavePrimaria()
						.setUnidadInicial(
								unidadMedidaDao
										.consultarUnidadMedida(conversion
												.getLlavePrimaria()
												.getUnidadInicial().getId()));

				conversion.getLlavePrimaria().setUnidadFinal(
						unidadMedidaDao.consultarUnidadMedida(conversion
								.getLlavePrimaria().getUnidadFinal().getId()));
			}
			if ((this.listaConversionUnidades.size() == 0)
					&& (this.filtroBusqueda != null && !""
							.equals(this.filtroBusqueda))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.filtroBusqueda + '"');
			} else if (this.listaConversionUnidades == null
					|| this.listaConversionUnidades.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.filtroBusqueda != null
					&& !"".equals(this.filtroBusqueda)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleInformacionBase
										.getString("conversion_unidad_medida_label"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.filtroBusqueda + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesConversionUnidadMedida";
	}

	/**
	 * This method makes the opening of the page that allows you to register
	 * (add or edit) the conversion of measurement.
	 * 
	 * @modify 09/12/2011 marisol.calderon
	 * 
	 * @param conversionUnidad
	 *            : object variable conversion unit that edited.
	 * @return regConversionUnidadMedida: redirected to the registration page
	 *         conversion unit of measure.
	 * 
	 */
	public String registrarConversionUnidades(ConversionUnidad conversionUnidad) {
		try {
			if (conversionUnidad != null) {
				esEdicion = true;
				this.conversionUnidad = conversionUnidad;
			} else {
				esEdicion = false;
				this.conversionUnidad = new ConversionUnidad();
			}
			cargarComboUnidadMedida();
			inicializarConsulta();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regConversionUnidadMedida";
	}

	/**
	 * Method for querying measurement units to fill combo user interface
	 * conversion
	 * 
	 * @modify 13/12/2011 marisol.calderon
	 * 
	 */
	public void cargarComboUnidadMedida() {
		itemsUnidadInicial = new HashMap<String, Integer>();
		itemsUnidadFinal = new HashMap<String, Integer>();
		try {
			List<UnidadMedida> unidadesMedidas = unidadMedidaDao
					.consultarUnidadesMedidas();
			if (unidadesMedidas != null) {
				for (UnidadMedida unidadMedida : unidadesMedidas) {
					itemsUnidadInicial.put(unidadMedida.getNombre(),
							unidadMedida.getId());
					if (esEdicion) {
						itemsUnidadFinal.put(unidadMedida.getNombre(),
								unidadMedida.getId());
					}
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the query of the units associated with the same group
	 * of a given unit
	 * 
	 * @modify 13/12/2011 marisol.calderon
	 * 
	 */
	public void cargarComboUnidadesFinales() {
		itemsUnidadFinal = new HashMap<String, Integer>();
		try {
			if (this.conversionUnidad != null) {
				UnidadMedida unidadInicial = this.conversionUnidad
						.getLlavePrimaria().getUnidadInicial();
				List<UnidadMedida> unidadesMedidas = unidadMedidaDao
						.consultarUnidadesSimilares(unidadInicial.getId());
				unidadInicial = unidadMedidaDao
						.consultarUnidadMedida(unidadInicial.getId());
				if (unidadesMedidas != null) {
					unidadesMedidas.remove(unidadInicial);
					for (UnidadMedida um : unidadesMedidas) {
						itemsUnidadFinal.put(um.getNombre(), um.getId());
					}
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method makes the registration (new or editing) an equivalence Unit
	 * of measure in the information system
	 * 
	 * @author Angelica Amaya
	 * @modify 13/02/2014 marisol.calderon
	 * 
	 * @return consultarConversionUnidades: consulting method conversions and
	 *         returns to the list in the user interface
	 */
	public String guardarConversionUnidad() {
		ResourceBundle bundleInfoBase = ControladorContexto
				.getBundle("mensajeInformacionBase");
		try {
			ConversionUnidadPK llavePrimaria = conversionUnidad
					.getLlavePrimaria();
			int idUnidadInicial = llavePrimaria.getUnidadInicial().getId();
			String unidadInicial = (String) EncodeFilter.getKey(
					itemsUnidadInicial, idUnidadInicial);
			int idUnidadFinal = llavePrimaria.getUnidadFinal().getId();
			String unidadFinal = (String) EncodeFilter.getKey(itemsUnidadFinal,
					idUnidadFinal);
			conversionUnidad.setUserName(identity.getUserName());
			if (esEdicion) {
				conversionUnidadMedidaDao
						.modificarConversionUnidad(conversionUnidad);
			} else {
				conversionUnidad.setFechaCreacion(new Date());
				conversionUnidadMedidaDao
						.crearConversionUnidad(conversionUnidad);
			}
			String format = MessageFormat.format(bundleInfoBase
					.getString("conversion_unidad_medida_message_registro"),
					unidadInicial, unidadFinal, this.conversionUnidad
							.getFactorConversion());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarConversionUnidades();
	}

	/**
	 * This method initializes consulting unit conversions measure
	 * 
	 * @return consulting existing method and load conversions template
	 *         management.
	 */
	public String inicializarConsulta() {
		this.filtroBusqueda = "";
		return consultarConversionUnidades();
	}

}