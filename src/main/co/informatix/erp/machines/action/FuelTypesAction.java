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

import co.informatix.erp.machines.dao.FuelTypesDao;
import co.informatix.erp.machines.entities.FuelTypes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the types of fuel that may exist.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class FuelTypesAction implements Serializable {

	@EJB
	private FuelTypesDao fuelTypesDao;

	private List<FuelTypes> listFuelTypes;
	private FuelTypes fuelTypes;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	/**
	 * @return listFuelTypes: list of the types of fuel shown in the user
	 *         interface.
	 */
	public List<FuelTypes> getListFuelTypes() {
		return listFuelTypes;
	}

	/**
	 * @param listFuelTypes
	 *            :list of the types of fuel shown in the user interface.
	 */
	public void setListFuelTypes(List<FuelTypes> listFuelTypes) {
		this.listFuelTypes = listFuelTypes;
	}

	/**
	 * @return fuelTypes: object containing data on the types of fuel.
	 */
	public FuelTypes getFuelTypes() {
		return fuelTypes;
	}

	/**
	 * @param fuelTypes
	 *            :object containing data on the types of fuel.
	 */
	public void setFuelTypes(FuelTypes fuelTypes) {
		this.fuelTypes = fuelTypes;
	}

	/**
	 * @return pagination: Management paginated list of the types of fuel.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paginated list of the types of fuel.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Type of fuel to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Type of fuel to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the types of fuel.
	 * 
	 * @return consultFuelTypes: method to query the types of fuel, returns to
	 *         the template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return consultFuelTypes();
	}

	/**
	 * Consult the list of the types of fuel.
	 * 
	 * @return "gesFuelTypes": redirects to the template to manage the types of
	 *         fuels.
	 */
	public String consultFuelTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("messageMachine");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listFuelTypes = new ArrayList<FuelTypes>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(consult, parameters, bundle, unionMessagesSearch);
			Long amount = fuelTypesDao.amountFuelTypes(consult, parameters);
			if (amount != null) {
				pagination.paginar(amount);
			}
			listFuelTypes = fuelTypesDao.consultFuelTypes(
					pagination.getInicio(), pagination.getRango(), consult,
					parameters);
			if ((listFuelTypes == null || listFuelTypes.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listFuelTypes == null || listFuelTypes.size() <= 0) {
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
		return "gesFuelTypes";
	}

	/**
	 * This method build consultation for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 * 
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(ft.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create new types of fuel.
	 * 
	 * @param fuelTypes
	 *            :types of fuel that you are adding or editing.
	 * 
	 * @return "regFuelTypes": redirected to the template record fuel types.
	 */
	public String addEditFuelTypes(FuelTypes fuelTypes) {
		if (fuelTypes != null) {
			this.fuelTypes = fuelTypes;
		} else {
			this.fuelTypes = new FuelTypes();
		}
		return "regFuelTypes";
	}

	/**
	 * It validates the types of the fuel, so it is not repeated in the database
	 * and validates against XSS.
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = fuelTypes.getIdFuelType();
			FuelTypes fuelTypesAux = new FuelTypes();
			fuelTypesAux = fuelTypesDao.nameTypeFuelExists(name, id);
			if (fuelTypesAux != null) {
				String messageExists = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExists), null));
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
	 * Method used to save or edit the types of fuel.
	 * 
	 * @return consultFuelTypes: Redirects to manage types of fuel with the list
	 *         of names updated.
	 */
	public String saveFuelTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {

			if (fuelTypes.getIdFuelType() != 0) {
				fuelTypesDao.editFuelTypes(fuelTypes);
			} else {
				messageLog = "message_registro_guardar";
				fuelTypesDao.saveFuelTypes(fuelTypes);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), fuelTypes.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultFuelTypes();
	}

	/**
	 * Method to delete a type of fuel database.
	 * 
	 * 
	 * @return consultFuelTypes(): Consult the list of the types of fuel and
	 *         returns to manages the fuels.
	 */
	public String deleteFuelTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			fuelTypesDao.deleteFuelTypes(fuelTypes);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					fuelTypes.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					fuelTypes.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultFuelTypes();
	}

}
