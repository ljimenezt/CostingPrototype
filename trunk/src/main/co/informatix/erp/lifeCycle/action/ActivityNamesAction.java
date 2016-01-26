package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the names of activities that may exist.
 * 
 * @author Dario.Lopez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivityNamesAction implements Serializable {

	@EJB
	private ActivityNamesDao activityNamesDao;

	private List<ActivityNames> listaActivityNames;

	private ActivityNames activityNames;
	private Paginador paginador = new Paginador();

	private String nombreBuscar;

	/**
	 * @return List<ActivityNames>: list of activities names displayed on the
	 *         user interface
	 */
	public List<ActivityNames> getListaActivityNames() {
		return listaActivityNames;
	}

	/**
	 * @param listaActivityNames
	 *            :list of activities names displayed on the user interface
	 */
	public void setListaActivityNames(List<ActivityNames> listaActivityNames) {
		this.listaActivityNames = listaActivityNames;
	}

	/**
	 * Gets data from a name Activity
	 * 
	 * @return ActivityNames: object containing data Activity name
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * Data set name Activity
	 * 
	 * @param activityNames
	 *            : object containing data Activity name
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return Paginador: Management paginated list of the names of activities.
	 * 
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paginated list of the names of activities.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Activity name to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Activity name to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of the names of activities
	 * 
	 * @return consultarActivityNames: method to query the names of activities,
	 *         returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarActivityNames();
	}

	/**
	 * Consult the list of the names of the activities to show either a POPUP or
	 * in management
	 * 
	 * @modify Johnatan.Naranjo 22/04/2015
	 * 
	 * @return retorno: redirects to the template name to manage activities or
	 *         POPUP
	 */
	public String consultarActivityNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaActivityNames = new ArrayList<ActivityNames>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";

		String param2 = ControladorContexto.getParam("param2");
		boolean desdeModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		String retorno = desdeModal ? "" : "gesActivityNames";

		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = activityNamesDao.cantidadActivityNames(consulta,
					parametros);
			if (cantidad != null) {
				if (desdeModal) {
					paginador.paginarRangoDefinido(cantidad, 5);
				} else {
					paginador.paginar(cantidad);
				}

			}
			listaActivityNames = activityNamesDao.consultarActivityNames(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaActivityNames == null || listaActivityNames.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaActivityNames == null
					|| listaActivityNames.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return retorno;

	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 * 
	 */
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create a new activity name.
	 * 
	 * @param activityNames
	 *            :name of activity you are adding or editing
	 * 
	 * @return "regActivityNames":redirects to the record template activity
	 */
	public String agregarEditarActivityNames(ActivityNames activityNames) {
		if (activityNames != null) {
			this.activityNames = activityNames;
		} else {
			this.activityNames = new ActivityNames();
		}
		return "regActivityNames";
	}

	/**
	 * This method allow validate the name of the activity, so that it is not
	 * repeated in the database and validates against XSS.
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
			int id = activityNames.getIdActivityName();
			ActivityNames activityNamesAux = new ActivityNames();
			activityNamesAux = activityNamesDao.nombreExiste(nombre, id);
			if (activityNamesAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(mensajeExistencia), null));
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
	 * Method used to save or edit the names of the activities
	 * 
	 * @return consultarActivityNames: Names redirects manage activities with
	 *         the list of names updated
	 */
	public String guardarActivityNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (activityNames.getIdActivityName() != 0) {
				activityNamesDao.editarActivityNames(activityNames);
			} else {
				mensajeRegistro = "message_registro_guardar";
				activityNamesDao.guardarActivityNames(activityNames);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					activityNames.getActivityName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarActivityNames();
	}

	/**
	 * Method to remove a name from activity database
	 * 
	 * @author Mabell.Boada
	 * 
	 * @return consultarActivityNames: Redirects to manage the names of
	 *         activities with the updated list
	 */
	public String eliminarActivityNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			activityNamesDao.eliminarActivityNames(activityNames);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					activityNames.getActivityName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					activityNames.getActivityName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarActivityNames();
	}

}
