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

	private List<MachineTypes> listMachineTypes;

	private MachineTypes machineTypes;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	/**
	 * @return List<MachineTypes>: list of the types of machine shown in the
	 *         user interface
	 */
	public List<MachineTypes> getListMachineTypes() {
		return listMachineTypes;
	}

	/**
	 * @param listMachineTypes
	 *            : list of the types of machine shown in the user interface
	 */
	public void setListMachineTypes(List<MachineTypes> listMachineTypes) {
		this.listMachineTypes = listMachineTypes;
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
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paginated list of the types of machines.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Type of machine to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Type of machine to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the types of machines
	 * 
	 * @return consultMachineTypes: method to query the types of machines,
	 *         returns to the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultMachineTypes();
	}

	/**
	 * Consult the list of the types of machines
	 * 
	 * @return "gesMachineTypes": redirects to the template to manage the types
	 *         of machines
	 */
	public String consultMachineTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listMachineTypes = new ArrayList<MachineTypes>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = machineTypesDao.quantityMachineTypes(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listMachineTypes = machineTypesDao.consultMachineTypes(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listMachineTypes == null || listMachineTypes.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listMachineTypes == null || listMachineTypes.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("machine_types_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
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
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(mt.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
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
	public String addEditMachineTypes(MachineTypes machineTypes) {
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
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = machineTypes.getIdMachineType();
			MachineTypes machineTypesAux = new MachineTypes();
			machineTypesAux = machineTypesDao.nameExists(name, id);
			if (machineTypesAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExistence), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(name, clientId,
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
	 * @return consultMachineTypes: Redirects to manage types of machines with
	 *         the list of names updated
	 */
	public String saveMachineTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {

			if (machineTypes.getIdMachineType() != 0) {
				machineTypesDao.editMachineTypes(machineTypes);
			} else {
				messageLog = "message_registro_guardar";
				machineTypesDao.saveMachineTypes(machineTypes);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), machineTypes.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMachineTypes();
	}

	/**
	 * Method to delete a type of machine database
	 * 
	 * 
	 * @return consultMachineTypes(): Consult the list of the types of machine
	 *         and returns to manage one machine
	 */
	public String removeMachineTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			machineTypesDao.removeMachineTypes(machineTypes);
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
		return consultMachineTypes();
	}
}
