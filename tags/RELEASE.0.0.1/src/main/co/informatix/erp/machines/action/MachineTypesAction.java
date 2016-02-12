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

import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the types of machines that may exist.
 * 
 * @author Dario.Lopez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MachineTypesAction implements Serializable {

	@EJB
	private MachineTypesDao machineTypesDao;

	private List<MachineTypes> listaMachineTypes;

	private MachineTypes machineTypes;
	private Paginador paginador = new Paginador();

	private String nombreBuscar;

	/**
	 * @return List<MachineTypes>: list of the types of machine shown in the
	 *         user interface
	 */
	public List<MachineTypes> getListaMachineTypes() {
		return listaMachineTypes;
	}

	/**
	 * @param listaMachineTypes
	 *            : list of the types of machine shown in the user interface
	 */
	public void setListaMachineTypes(List<MachineTypes> listaMachineTypes) {
		this.listaMachineTypes = listaMachineTypes;
	}

	/**
	 * Gets data from one type of machine
	 * 
	 * @return MachineTypes: object containing data on the types of machine
	 */
	public MachineTypes getMachineTypes() {
		return machineTypes;
	}

	/**
	 * Sets data from one type of machine
	 * 
	 * @param machineTypes
	 *            : object containing data on the types of machine
	 */
	public void setMachineTypes(MachineTypes machineTypes) {
		this.machineTypes = machineTypes;
	}

	/**
	 * @return Paginador: Management paginated list of the types of machines.
	 * 
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paginated list of the types of machines.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Type of machine to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Type of machine to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the types of machines
	 * 
	 * @return consultarMachineTypes: method to query the types of machines,
	 *         returns to the template management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		return consultarMachineTypes();
	}

	/**
	 * Consult the list of the types of machines
	 * 
	 * @return "gesMachineTypes": redirects to the template to manage the types
	 *         of machines
	 */
	public String consultarMachineTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaMachineTypes = new ArrayList<MachineTypes>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = machineTypesDao.cantidadMachineTypes(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaMachineTypes = machineTypesDao.consultarMachineTypes(
					paginador.getInicio(), paginador.getRango(), consulta,
					parametros);
			if ((listaMachineTypes == null || listaMachineTypes.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaMachineTypes == null
					|| listaMachineTypes.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("machine_types_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMachineTypes";
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
			consult.append("WHERE UPPER(mt.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method to edit or create new types of machine.
	 * 
	 * @param machineTypes
	 *            :types of machine that you are adding or editing
	 * 
	 * @return "regMachineTypes": redirected to the template record machine
	 *         types.
	 */
	public String agregarEditarMachineTypes(MachineTypes machineTypes) {
		if (machineTypes != null) {
			this.machineTypes = machineTypes;
		} else {
			this.machineTypes = new MachineTypes();
		}
		return "regMachineTypes";
	}

	/**
	 * It validates the types of the machine, so it is not repeated in the
	 * database and validates against XSS.
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
			int id = machineTypes.getIdMachineType();
			MachineTypes machineTypesAux = new MachineTypes();
			machineTypesAux = machineTypesDao.nombreExiste(nombre, id);
			if (machineTypesAux != null) {
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
	 * Method used to save or edit the types of machines
	 * 
	 * @return consultarMachineTypes: Redirects to manage types of machines with
	 *         the list of names updated
	 */
	public String guardarMachineTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (machineTypes.getIdMachineType() != 0) {
				machineTypesDao.editarMachineTypes(machineTypes);
			} else {
				mensajeRegistro = "message_registro_guardar";
				machineTypesDao.guardarMachineTypes(machineTypes);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), machineTypes.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMachineTypes();
	}

	/**
	 * Method to delete a type of machine database
	 * 
	 * 
	 * @return consultarMachineTypess(): Consult the list of the types of
	 *         machine and returns to manage one machine
	 */
	public String eliminarMachineTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			machineTypesDao.eliminarMachineTypes(machineTypes);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					machineTypes.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					machineTypes.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMachineTypes();
	}
}
