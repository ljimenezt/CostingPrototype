package co.informatix.erp.informacionBase.action;

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
import javax.inject.Inject;

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.informacionBase.dao.EstadoDao;
import co.informatix.erp.informacionBase.entities.Estado;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class can handle the business logic states that exist in system, which
 * allows you to insert, modify, and query.
 * 
 * @author Gerson.Cespedes
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class EstadoAction implements Serializable {

	private List<Estado> listaEstados;
	private Paginador pagination = new Paginador();
	private Estado estado;
	private String vigencia = "si";
	private String nombreBuscar;

	@EJB
	private EstadoDao estadoDao;
	@Inject
	private IdentityAction identity;

	/**
	 * 
	 * @return listaEstados: get a list of states.
	 */
	public List<Estado> getListaEstados() {
		return listaEstados;
	}

	/**
	 * 
	 * @param listaEstados
	 *            : get a list of states.
	 */
	public void setListaEstados(List<Estado> listaEstados) {
		this.listaEstados = listaEstados;
	}

	/**
	 * 
	 * @return estado: gets an variable object to which state performs the
	 *         management.
	 */
	public Estado getEstado() {
		return estado;
	}

	/**
	 * 
	 * @param estado
	 *            : gets an variable object to which state performs the
	 *            management
	 */
	public void setEstado(Estado estado) {
		this.estado = estado;
	}

	/**
	 * 
	 * @return vigencia: It is giving the selected value 'yes' of the current
	 *         and 'no' for not applicable.
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : It is giving the selected value 'yes' of the current and
	 *            'no' for not applicable.
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return nombreBuscar: name by which you want to view the status.
	 * 
	 * 
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : name by which you want to view the status.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * 
	 * @return pagination: paging management from the list of states that may
	 *         have in view
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * 
	 * @param pagination
	 *            : paging management from the list of states that may have in
	 *            view
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * A new object instance of the state, if different from the null load for
	 * editing.
	 * 
	 * @param estado
	 *            : State object variable you want to upload edition.
	 * 
	 * @return regEstado: Navigation rule that redirects to register been.
	 */
	public String nuevoEstado(Estado estado) {
		if (estado != null) {
			this.estado = estado;
		} else {
			this.estado = new Estado();
		}
		return "regEstado";

	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultarEstados: method to query and load states template with
	 *         the information found.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return consultarEstados();
	}

	/**
	 * 
	 * Consult the list of states.
	 * 
	 * @return gesEstado: Navigation rule that redirects to manage States.
	 */
	public String consultarEstados() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleInfoBase = ControladorContexto
				.getBundle("mensajeInformacionBase");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listaEstados = new ArrayList<Estado>();
		String mensajeBusqueda = "";

		try {
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			Long cantidadEstados = estadoDao.cantidadEstados(condicionVigencia,
					this.nombreBuscar);
			if (cantidadEstados != null) {
				pagination.paginar(cantidadEstados);
			}
			this.listaEstados = estadoDao.consultarEstados(
					pagination.getInicio(), pagination.getRango(),
					condicionVigencia, this.nombreBuscar);

			if (listaEstados == null || listaEstados.size() <= 0
					&& this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			} else if (listaEstados == null || listaEstados.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleInfoBase
										.getString("estado_civil_label_s"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesEstado";
	}

	/**
	 * Method used to complete the term of the records states.
	 * 
	 * @param vigente
	 *            : boolean to find out the validity ends with 'true' or INICA
	 *            with 'false', the selected record in the user interface.
	 * @return consultarEstados: Redirects states manage the list the updated
	 *         states.
	 */
	public String vigenciaEstado(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			estado.setUserName(identity.getUserName());
			String mensaje = "";
			if (vigente) {
				estado.setFechaFinVigencia(new Date());
				estadoDao.editarEstado(estado);
				mensaje = bundle
						.getString("message_fin_vigencia_satisfactorio") + ": ";
			} else {
				estado.setFechaFinVigencia(null);
				estadoDao.editarEstado(estado);
				mensaje = bundle
						.getString("message_inicio_vigencia_satisfactorio")
						+ ": ";
			}
			ControladorContexto.mensajeInformacion(null,
					mensaje + estado.getNombre());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarEstados();
	}

	/**
	 * To validate the name of the state, so it is not repeated in the base and
	 * validates data against XSS.
	 * 
	 * @param contexto
	 *            : JSF context of view.
	 * @param toValidate
	 *            : View component to validate.
	 * @param value
	 *            : Value of the object in view.
	 */
	public void validarNombreXSS(FacesContext contexto, UIComponent toValidate,
			Object value) {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(contexto);
		String mensajeVigencia = "message_ya_existe_sin_vigencia";
		try {
			int id = estado.getId();
			String nombreCapitalize = WordUtils.capitalizeFully(nombre);
			Estado estadoAux = new Estado();
			if (id != 0) {
				estadoAux = estadoDao.nombreExisteActualizar(nombreCapitalize,
						id);
			} else {
				estadoAux = estadoDao.nombreExiste(nombreCapitalize);
			}
			if (estadoAux != null) {
				if (estadoAux.getFechaFinVigencia() != null) {
					ControladorContexto.mensajeErrorEspecifico(clientId,
							mensajeVigencia, "mensaje");
					((UIInput) toValidate).setValid(false);
				} else {
					mensajeVigencia = "message_ya_existe_verifique";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							mensajeVigencia, "mensaje");
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

	/**
	 * Method used to save or edit the states.
	 * 
	 * @return consultarEstados: allows consult the states and redirect template
	 *         management.
	 */
	public String guardarEstado() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		try {
			estado.setNombre(WordUtils.capitalizeFully(estado.getNombre()));
			estado.setUserName(identity.getUserName());
			if (estado.getId() != 0) {
				estadoDao.editarEstado(estado);
			} else {
				key = "message_registro_guardar";
				estado.setFechaCreacion(new Date());
				estadoDao.guardarEstado(estado);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							estado.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarEstados();
	}

}
