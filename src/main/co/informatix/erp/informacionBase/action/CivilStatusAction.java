package co.informatix.erp.informacionBase.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.informacionBase.dao.CivilStatusDao;
import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating or removal of
 * civil States in the system
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CivilStatusAction implements Serializable {
	private List<CivilStatus> listaEstadoCivil;
	private Paginador paginador = new Paginador();
	private CivilStatus civilStatus;
	private String nombreBuscar;

	@EJB
	private CivilStatusDao civilStatusDao;

	/**
	 * @return listaEstadoCivil: List of civil status
	 */
	public List<CivilStatus> getListaEstadoCivil() {
		return listaEstadoCivil;
	}

	/**
	 * @param listaEstadoCivil
	 *            : List of civil status
	 */
	public void setListaEstadoCivil(List<CivilStatus> listaEstadoCivil) {
		this.listaEstadoCivil = listaEstadoCivil;
	}

	/**
	 * @return paginador: Paginated list of civil states can be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paginated list of civil states can be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return civilStatus: Object civil status
	 */
	public CivilStatus getCivilStatus() {
		return civilStatus;
	}

	/**
	 * @param civilStatus
	 *            : Object civil status
	 */
	public void setCivilStatus(CivilStatus civilStatus) {
		this.civilStatus = civilStatus;
	}

	/**
	 * @return nombreBuscar: Name by which you want to view the status civil
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name by which you want to view the status civil
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of civil states
	 * 
	 * @return consultarEstadoCivil: Method consulting civil states, returns to
	 *         the template management
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarEstadoCivil();
	}

	/**
	 * Consult the list of existing civil status
	 * 
	 * @return gesEstCivil: Navigation rule that redirects manage civil status
	 */
	public String consultarEstadoCivil() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleGeneral = ControladorContexto
				.getBundle("mensajeGeneral");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaEstadoCivil = new ArrayList<CivilStatus>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = civilStatusDao.cantidadEstadoCivil(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaEstadoCivil = civilStatusDao.consultarEstadoCivil(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaEstadoCivil == null || listaEstadoCivil.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaEstadoCivil == null || listaEstadoCivil.size() <= 0) {
				ControladorContexto
						.mensajeInformacion(null, MessageFormat.format(bundle
								.getString("message_no_existen_registros"), ""));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleGeneral.getString("estado_civil_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesEstCivil";
	}

	/**
	 * This method constructs the query to the advanced search also allows build
	 * messages to display depending on the search criteria selected by the user
	 * 
	 * @param consulta
	 *            : Consult concatenate
	 * @param parametros
	 *            : List of search parameters
	 * @param bundle
	 *            : Access language tags
	 * 
	 * @param unionMensajesBusqueda
	 *            : Message Word Search
	 * 
	 */
	private void busquedaAvanzada(StringBuilder consulta,
			List<SelectItem> parametros, ResourceBundle bundle,
			StringBuilder unionMensajesBusqueda) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consulta.append("WHERE UPPER(cs.nombre) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new civil status
	 * 
	 * @param civilStatus
	 *            : Marital status to be add or edit
	 * 
	 * @return regEstCivil: Redirects the state record template civil
	 */
	public String agregarEditarEstadoCivil(CivilStatus civilStatus) {
		if (civilStatus != null) {
			this.civilStatus = civilStatus;
		} else {
			this.civilStatus = new CivilStatus();
		}
		return "regEstCivil";
	}

	/**
	 * To validate the name of civil states, so it is not repeated in the
	 * database and validates against XSS.
	 * 
	 * @param contexto
	 *            : Application context
	 * 
	 * @param toValidate
	 *            : Validate component
	 * @param value
	 *            : Field value is validated
	 */
	public void validarNombreXSS(FacesContext contexto, UIComponent toValidate,
			Object value) {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(contexto);
		try {
			int id = civilStatus.getId();
			CivilStatus civilStatusAux = new CivilStatus();
			civilStatusAux = civilStatusDao.nombreExiste(nombre, id);
			if (civilStatusAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						mensajeExistencia, "mensaje");
				((UIInput) toValidate).setValid(false);
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
	 * Method used to save or edit the civil status
	 * 
	 * @return consultarEstadoCivil: Redirects to manage civil states with the
	 *         list of names updated
	 */
	public String guardarEstadoCivil() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			if (civilStatus.getId() != 0) {
				civilStatusDao.editarEstadoCivil(civilStatus);
			} else {
				mensajeRegistro = "message_registro_guardar";
				civilStatusDao.guardarEstadoCivil(civilStatus);
			}
			ControladorContexto
					.mensajeInformacion(null, MessageFormat.format(
							bundle.getString(mensajeRegistro),
							civilStatus.getNombre()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarEstadoCivil();
	}

	/**
	 * Method to delete a civil state of the database
	 * 
	 * @return consultarEstadoCivil: Redirects to manage the states civilians
	 *         with the list of names updated
	 */
	public String eliminarEstadoCivil() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			civilStatusDao.eliminarEstadoCivil(civilStatus);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					civilStatus.getNombre()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					civilStatus.getNombre());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarEstadoCivil();
	}
}
