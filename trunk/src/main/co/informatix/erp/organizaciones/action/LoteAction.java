package co.informatix.erp.organizaciones.action;

import java.awt.Color;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.informacionBase.dao.UnidadMedidaDao;
import co.informatix.erp.organizaciones.dao.LoteDao;
import co.informatix.erp.organizaciones.entities.Lote;
import co.informatix.erp.organizaciones.entities.Zona;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.security.action.IdentityAction;

/**
 * This class can handle business logic batch (valves) of an area that exist in
 * the system, which allows you to insert, modify, and query.
 * 
 * 
 * @author Gerson.Cespedes
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class LoteAction implements Serializable {

	@EJB
	private LoteDao loteDao;
	@EJB
	private UnidadMedidaDao unidadMedidaDao;
	@Inject
	private IdentityAction identity;

	private List<Lote> lotes;
	private List<SelectItem> itemsUnidadMedidaLongitud;
	private List<SelectItem> itemsUnidadMedidaDensidad;
	private Color colorControl = new Color(255, 255, 255);
	private Paginador pagination = new Paginador();
	private Lote lote;
	private Zona zona;
	private String vigencia = Constantes.SI;
	private String nombreBuscar = "";

	/**
	 * @return lote: Lot object to which you are making the implementation of
	 *         registration or edition.
	 */
	public Lote getLote() {
		return lote;
	}

	/**
	 * @param lote
	 *            : Lot object to which you are making the implementation of
	 *            registration or edition.
	 */
	public void setLote(Lote lote) {
		this.lote = lote;
	}

	/**
	 * @return vigencia: sets the value for the management of current records
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : gets the value for the management of current records
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return pagination: Object pager functions lots
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Object pager functions lots
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return zona: Purpose of the area to which lots are associated.
	 */
	public Zona getZona() {
		return zona;
	}

	/**
	 * @param zona
	 *            : Purpose of the area to which lots are associated.
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
	}

	/**
	 * @return lotes: list of lots that have an area
	 */
	public List<Lote> getLotes() {
		return lotes;
	}

	/**
	 * @param lotes
	 *            : list of lots that have an area
	 */
	public void setLotes(List<Lote> lotes) {
		this.lotes = lotes;
	}

	/**
	 * @return itemsUnidadMedidaLongitud: items of measurement units in length
	 *         that are loaded into combo in the user interface.
	 */
	public List<SelectItem> getItemsUnidadMedidaLongitud() {
		return itemsUnidadMedidaLongitud;
	}

	/**
	 * @return itemsUnidadMedidaDensidad: items of measurement units in length
	 *         that are loaded into combo in the user interface.
	 */
	public List<SelectItem> getItemsUnidadMedidaDensidad() {
		return itemsUnidadMedidaDensidad;
	}

	/**
	 * @return nombreBuscar: Lot name to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Lot name to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return colorControl: color used to assign the ColorPicker to store color
	 *         information Lot
	 */
	public Color getColorControl() {
		return colorControl;
	}

	/**
	 * @param colorControl
	 *            : color used to assign the ColorPicker to store color
	 *            information Lot
	 */
	public void setColorControl(Color colorControl) {
		this.colorControl = colorControl;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultarLotesZona: Method consulting lots and load the template
	 *         with the information found.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarLotesZona();
	}

	/**
	 * Method consulting lots and load the template with the information found.
	 * 
	 * @return gesLoteZona: Rule navigation load the template for managing lots.
	 */
	public String consultarLotesZona() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleOrg = ControladorContexto
				.getBundle("mensajeOrganizaciones");
		String enModal = ControladorContexto.getParam("param2");
		this.lotes = new ArrayList<Lote>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		boolean desdeModal = (enModal != null && Constantes.SI.equals(enModal)) ? true
				: false;
		String retorno = desdeModal ? "" : "gesLoteZona";
		String idMessage = desdeModal ? "formModalSeleccionarLote:tablaLotes"
				: "formLote:tablaLotes";
		try {
			if (zona != null) {
				busquedaAvanzada(consulta, parametros, bundle, bundleOrg,
						unionMensajesBusqueda, desdeModal);
				Long cantidadLotes = loteDao
						.cantidadLotes(consulta, parametros);
				if (cantidadLotes != null) {
					if (desdeModal)
						pagination.paginarRangoDefinido(cantidadLotes, 5);
					else
						pagination.paginar(cantidadLotes);
				}
				this.lotes = loteDao.consultarLotes(pagination.getInicio(),
						pagination.getRango(), consulta, parametros);
				if ((lotes == null || lotes.size() <= 0)
						&& !"".equals(unionMensajesBusqueda.toString())) {
					mensajeBusqueda = MessageFormat
							.format(bundle
									.getString("message_no_existen_registros_criterio_busqueda"),
									unionMensajesBusqueda);
				} else if (lotes == null || lotes.size() <= 0) {
					mensajeBusqueda = bundle
							.getString("message_no_existen_registros");
				} else if (!"".equals(unionMensajesBusqueda.toString())) {
					mensajeBusqueda = MessageFormat
							.format(bundle
									.getString("message_existen_registros_criterio_busqueda"),
									bundleOrg.getString("lote_label_s"),
									unionMensajesBusqueda);
				}
			} else {
				ControladorContexto.mensajeError(bundleOrg
						.getString("zona_message_validar"));
			}
			if (!"".equals(mensajeBusqueda)) {
				ControladorContexto.mensajeInformacion(idMessage,
						mensajeBusqueda);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return retorno;
	}

	/**
	 * This method allows to build the query to the advanced search and to
	 * display messages depending on the search criteria selected by the user.
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param bundleOrg
	 *            : Variable used to read the language module of organizations.s
	 * @param unionMessagesSearch
	 *            : message search
	 * @param desdeModal
	 *            : variable that shows whether lots are consulted from a modal.
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			ResourceBundle bundleOrg, StringBuilder unionMessagesSearch,
			boolean desdeModal) {
		boolean seAgregoMens = false;
		String comaEspacio = ", ";

		consult.append("WHERE l.fechaFinVigencia ");
		String condicionVigencia = Constantes.IS_NULL + " ";
		if (Constantes.NOT.equals(vigencia)) {
			condicionVigencia = Constantes.IS_NOT_NULL + " ";
		}
		consult.append(condicionVigencia);

		if (this.zona.getId() != 0) {
			consult.append("AND l.zona.id=:idZona ");
			SelectItem item = new SelectItem(this.zona.getId(), "idZona");
			parameters.add(item);
			if (desdeModal) {
				unionMessagesSearch.append(bundleOrg.getString("zona_label")
						+ ": " + '"' + this.zona.getNombre() + '"');
				seAgregoMens = true;
			}
		}

		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			SelectItem item = new SelectItem("%" + nombreBuscar + "%",
					"keyword");
			consult.append("AND UPPER(l.nombre) LIKE UPPER(:keyword) ");
			parameters.add(item);
			unionMessagesSearch.append((seAgregoMens ? comaEspacio : "")
					+ bundle.getString("label_nombre") + ": " + '"'
					+ this.nombreBuscar + '"');
		}
	}

	/**
	 * Method used to end or start the effect of lots of areas.
	 * 
	 * @param vigente
	 *            : Boolean that allows to know if the term with 'true' ends or
	 *            begins with 'false', the selected record in the user
	 *            interface.
	 * @return consultarLotesZona: Method consulting lots and load the template
	 *         with the information found.
	 */
	public String vigenciaLote(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";
		try {
			lote.setUserName(identity.getUserName());
			if (vigente) {
				lote.setFechaFinVigencia(new Date());
			} else {
				mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
				lote.setFechaFinVigencia(null);
			}
			loteDao.editarLote(lote);
			String mensajeMostrar = bundle.getString(mensajeCambioVigencia)
					+ ": " + lote.getNombre();
			ControladorContexto.mensajeInformacion("formLote:tablaLotes",
					mensajeMostrar);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarLotesZona();
	}

	/**
	 * Method that allows you to save or edit a batch associated with a zone.
	 * 
	 * @return consultarLotesZona: Method consulting lots and load the template
	 *         with the information found.
	 */
	public String guardarLote() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		String hex = Integer.toHexString(colorControl.getRGB());
		try {
			this.lote
					.setCodigo(WordUtils.capitalizeFully(this.lote.getCodigo()));
			this.lote.setUserName(identity.getUserName());
			lote.setColor(hex.substring(2, hex.length()));
			if (this.lote.getId() != 0) {
				loteDao.editarLote(this.lote);
			} else {
				key = "message_registro_guardar";
				this.lote.setZona(this.zona);
				this.lote.setFechaCreacion(new Date());
				loteDao.guardarLote(lote);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							lote.getNombre()));
			nombreBuscar = "";
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarLotesZona();
	}

	/**
	 * To validate the name of the lot, so it is not repeated in the database
	 * and validates against XSS.
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
			int id = lote.getId();
			String nombreCapitalize = WordUtils.capitalizeFully(nombre);
			Lote loteAux = new Lote();
			if (id != 0) {
				loteAux = loteDao.nombreExisteActualizar(nombreCapitalize, id,
						this.zona.getId());
			} else {
				loteAux = loteDao.nombreExiste(nombreCapitalize,
						this.zona.getId());
			}
			if (loteAux != null) {
				if (loteAux.getFechaFinVigencia() != null) {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_sin_vigencia"),
									null));
					((UIInput) toValidate).setValid(false);
				} else {
					context.addMessage(
							clientId,
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									bundle.getString("message_ya_existe_verifique"),
									null));
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(nombre, clientId, null)) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
