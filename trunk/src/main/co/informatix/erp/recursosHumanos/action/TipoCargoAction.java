package co.informatix.erp.recursosHumanos.action;

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
import javax.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.recursosHumanos.dao.TipoCargoDao;
import co.informatix.erp.recursosHumanos.entities.TipoCargo;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class lets you handle the business logic of the type of charge that
 * exist in the system, which allows you to insert, modify, and query.
 * 
 * @author Oscar.Amaya
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TipoCargoAction implements Serializable {

	@EJB
	private TipoCargoDao tipoCargoDao;
	@Inject
	private IdentityAction identity;

	private List<TipoCargo> listaTiposCargo;
	private Paginador pagination = new Paginador();
	private TipoCargo tipoCargoVigencia;
	private TipoCargo tipoCargo;
	private String vigencia = Constantes.SI;
	private String nombreBuscar = "";

	/**
	 * @return vigencia: gets the value for the management of currency records
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : sets the value for the management of currency records
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return pagination: management paginated list of types of jobs that may
	 *         be in view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : management paginated list of types of jobs that may be in
	 *            view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return listaTiposCargo: list of types of jobs as shown in the table the
	 *         user interface.
	 */
	public List<TipoCargo> getListaTiposCargo() {
		return listaTiposCargo;
	}

	/**
	 * @param listaTiposCargo
	 *            : list of types of jobs as shown in the table the user
	 *            interface.
	 */
	public void setListaTiposCargo(List<TipoCargo> listaTiposCargo) {
		this.listaTiposCargo = listaTiposCargo;
	}

	/**
	 * @return tipoCargoVigencia: type job object to manage of the currency
	 */
	public TipoCargo getTipoCargoVigencia() {
		return tipoCargoVigencia;
	}

	/**
	 * @param tipoCargoVigencia
	 *            : type job object to manage of the currency
	 */
	public void setTipoCargoVigencia(TipoCargo tipoCargoVigencia) {
		this.tipoCargoVigencia = tipoCargoVigencia;
	}

	/**
	 * @return tipoCargo: type job object to which the implementation of
	 *         registration or editing is done.
	 */
	public TipoCargo getTipoCargo() {
		return tipoCargo;
	}

	/**
	 * @param tipoCargo
	 *            : type job object to which the implementation of registration
	 *            or editing is done.
	 */
	public void setTipoCargo(TipoCargo tipoCargo) {
		this.tipoCargo = tipoCargo;
	}

	/**
	 * @return nombreBuscar: Name to find the type of charge
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Name to find the type of charge
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * This method prepares the view to add a new type of charge
	 * 
	 * @return navigation rule that loads register a template of charge.
	 */
	public String nuevoTipoCargo() {
		tipoCargo = new TipoCargo();
		return "regTipoCargo";
	}

	/**
	 * This method prepares the view to edit one type of charge
	 * 
	 * @param tipoCargoEditar
	 *            : the type of object you want to edit position.
	 * @return regTipoCargo: navigation rule that loads register a template of
	 *         charge.
	 */
	public String editarTipoCargo(TipoCargo tipoCargoEditar) {
		tipoCargo = tipoCargoEditar;
		return "regTipoCargo";
	}

	/**
	 * Cleans the variable used in the search for types of cargo
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @return consultarTiposCargo: Navigation rule shows the view
	 *         gestionarTipoCargo
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarTiposCargo();
	}

	/**
	 * Consult the list of types of cargo according to the term submitted
	 * 
	 * @modify Luis.Ruiz
	 * @modify Liseth Jimenez 21/03/2012
	 * 
	 * @return gesTipoCargo: load navigation rule template management fee rates.
	 */
	public String consultarTiposCargo() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRH = ControladorContexto
				.getBundle("messageHumanResources");
		String mensajeBusqueda = "";
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			tipoCargoVigencia = new TipoCargo();
			this.listaTiposCargo = new ArrayList<TipoCargo>();
			long cantidadRegistros = this.tipoCargoDao.contarTiposCargo(
					condicionVigencia, this.nombreBuscar);
			pagination.paginar(cantidadRegistros);
			this.listaTiposCargo = tipoCargoDao.buscarTiposCargo(
					pagination.getInicio(), pagination.getRango(),
					condicionVigencia, this.nombreBuscar);
			if ((this.listaTiposCargo == null || this.listaTiposCargo.size() <= 0)
					&& (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nombreBuscar + '"');
			} else if (this.listaTiposCargo == null
					|| this.listaTiposCargo.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleRH.getString("tipo_cargo_label_s"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesTipoCargo";
	}

	/**
	 * Modifies the validity of one type of charge
	 * 
	 * @param vigente
	 *            : boolean that allows to know if the term ends with 'true' or
	 *            start with 'false', the selected record in the user interface.
	 * @return consultarTiposCargo: consulting method types of cargo and freight
	 *         management template.
	 */
	public String vigenciaTipoCargo(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			tipoCargoVigencia.setUserName(identity.getUserName());
			String mensaje = "";
			if (vigente) {
				tipoCargoVigencia.setFechaFinVigencia(new Date());
				tipoCargoDao.modificarTipoCargo(tipoCargoVigencia);
				mensaje = bundle
						.getString("message_fin_vigencia_satisfactorio") + ": ";
			} else {
				tipoCargoVigencia.setFechaFinVigencia(null);
				tipoCargoDao.modificarTipoCargo(tipoCargoVigencia);
				mensaje = bundle
						.getString("message_inicio_vigencia_satisfactorio")
						+ ": ";
			}
			ControladorContexto.mensajeInformacion(null, mensaje
					+ tipoCargoVigencia.getNombre());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarTiposCargo();
	}

	/**
	 * This method allows you to edit register a new type of charge, you gets
	 * the data stored in the form and stored in the database
	 * 
	 * @return navigation rule for querying the types of charges and load
	 *         management template.
	 */
	public String agregarEditarTipoCargo() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensaje = "";
		try {
			tipoCargo
					.setNombre(WordUtils.capitalizeFully(tipoCargo.getNombre()));
			tipoCargo.setUserName(identity.getUserName());
			if (tipoCargo.getId() != null && tipoCargo.getId().intValue() != 0) {
				tipoCargoDao.modificarTipoCargo(tipoCargo);
				mensaje = MessageFormat.format(
						bundle.getString("message_registro_modificar"),
						this.tipoCargo.getNombre());
			} else {
				tipoCargo.setFechaRegistro(new Date());
				tipoCargoDao.crearTipoCargo(tipoCargo);
				mensaje = MessageFormat.format(
						bundle.getString("message_registro_guardar"),
						this.tipoCargo.getNombre());
			}
			ControladorContexto.mensajeInformacion(null, mensaje);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return inicializarBusqueda();
	}

	/**
	 * This method applies the current existence of the name of a type of charge
	 * 
	 * @modify 15/03/2012 marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNombreCargoXSS(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String nombreCapitalize = WordUtils.capitalizeFully(nombre);
		String clientId = toValidate.getClientId(context);
		try {
			Integer id = this.tipoCargo.getId();
			TipoCargo tipoCargoAux = new TipoCargo();
			if (id == null) {
				tipoCargoAux = tipoCargoDao.nombreExiste(nombreCapitalize);
			} else {
				tipoCargoAux = tipoCargoDao.nombreExiste(nombreCapitalize, id);
			}
			if (tipoCargoAux != null) {
				if (tipoCargoAux.getFechaFinVigencia() != null) {
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

			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}