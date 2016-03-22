package co.informatix.erp.machines.action;

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

import co.informatix.erp.machines.dao.ConsumableTypesDao;
import co.informatix.erp.machines.entities.ConsumableTypes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * types of consumables that can exist.
 * 
 * @author Dario.Lopez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ConsumableTypesAction implements Serializable {
	@EJB
	private ConsumableTypesDao consumableTypesDao;

	private List<ConsumableTypes> listaConsumableTypes;

	private ConsumableTypes consumableTypes;
	private Paginador pagination = new Paginador();

	private String nombreBuscar;

	/**
	 * @return List<ConsumableTypes>: list of the types of consumable displayed
	 *         in the user interface
	 */
	public List<ConsumableTypes> getListaConsumableTypes() {
		return listaConsumableTypes;
	}

	/**
	 * @param listaConsumableTypes
	 *            : list of the types of consumable displayed in the user
	 *            interface
	 */
	public void setListaConsumableTypes(
			List<ConsumableTypes> listaConsumableTypes) {
		this.listaConsumableTypes = listaConsumableTypes;
	}

	/**
	 * Gets data from one type of consumable
	 * 
	 * @return ConsumableTypes: object that contains the data from one type of
	 *         consumable
	 */
	public ConsumableTypes getConsumableTypes() {
		return consumableTypes;
	}

	/**
	 * Sets data from one type of consumable
	 * 
	 * @param consumableTypes
	 *            : object that contains the data from one type of consumable
	 */
	public void setConsumableTypes(ConsumableTypes consumableTypes) {
		this.consumableTypes = consumableTypes;
	}

	/**
	 * @return Paginador: Management paginated list of the types of consumables.
	 * 
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paginated list of the types of consumables.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nombreBuscar: Consumable Type name to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Consumable Type name to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the types of consumables
	 * 
	 * @return consultarConsumableTypes: method to query the types of
	 *         consumables, returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarConsumableTypes();
	}

	/**
	 * Consult the list of the types of consumables
	 * 
	 * @return "gesConsumableTypes": redirects to the template to manage the
	 *         types of consumables
	 */
	public String consultarConsumableTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleConsumableType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaConsumableTypes = new ArrayList<ConsumableTypes>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = consumableTypesDao.cantidadConsumableTypes(
					consulta, parametros);
			if (cantidad != null) {
				pagination.paginar(cantidad);
			}
			listaConsumableTypes = consumableTypesDao.consultarConsumableTypes(
					pagination.getInicio(), pagination.getRango(), consulta,
					parametros);
			if ((listaConsumableTypes == null || listaConsumableTypes.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaConsumableTypes == null
					|| listaConsumableTypes.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleConsumableType
										.getString("consumable_types_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesConsumableTypes";
	}

	/**
	 * This method build consultation for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
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
			consult.append("WHERE UPPER(ct.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create new types of consumable.
	 * 
	 * @param consumableTypes
	 *            :Consumable types that are adding or editing
	 * 
	 * @return "regConsumableTypes":redirected to the template record consumable
	 *         types
	 */
	public String agregarEditarConsumableTypes(ConsumableTypes consumableTypes) {
		if (consumableTypes != null) {
			this.consumableTypes = consumableTypes;
		} else {
			this.consumableTypes = new ConsumableTypes();
		}
		return "regConsumableTypes";
	}

	/**
	 * method allow validates the types of consumable, so it is not repeated in
	 * the database and validates against XSS.
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
			int id = consumableTypes.getIdConsumableType();
			ConsumableTypes consumableTypesAux = new ConsumableTypes();
			consumableTypesAux = consumableTypesDao.nombreExiste(nombre, id);
			if (consumableTypesAux != null) {
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
	 * Method used to save or edit the types of consumables
	 * 
	 * @return consultarConsumableTypes: Redirects to manage types of
	 *         consumables with a list of names updated
	 */
	public String guardarConsumableTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (consumableTypes.getIdConsumableType() != 0) {
				consumableTypesDao.editarConsumableTypes(consumableTypes);
			} else {
				mensajeRegistro = "message_registro_guardar";
				consumableTypesDao.guardarConsumableTypes(consumableTypes);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					consumableTypes.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarConsumableTypes();
	}

	/**
	 * Method that eliminates one type of consumable database
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultarConsumableTypes(): Consult the list of types of
	 *         consumable and returns to manage media types.
	 */
	public String eliminarConsumableTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			consumableTypesDao.eliminarConsumableTypes(consumableTypes);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					consumableTypes.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					consumableTypes.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarConsumableTypes();
	}

}
