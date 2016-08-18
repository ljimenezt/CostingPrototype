package co.informatix.erp.humanResources.action;

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

import co.informatix.erp.humanResources.dao.HrTypesDao;
import co.informatix.erp.humanResources.entities.HrTypes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the logic related to create, update, and delete types
 * of human resources in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class HrTypesAction implements Serializable {

	private List<HrTypes> hrTypesList;
	private Paginador pagination = new Paginador();
	private HrTypes hrTypes;
	private String nameSearch;

	@EJB
	private HrTypesDao hrTypesDao;

	/**
	 * @return hrTypesList: Gets the list of types of human resources.
	 */
	public List<HrTypes> getHrTypesList() {
		return hrTypesList;
	}

	/**
	 * @param hrTypesList
	 *            : Gets the list of types of human resources.
	 */
	public void setHrTypesList(List<HrTypes> hrTypesList) {
		this.hrTypesList = hrTypesList;
	}

	/**
	 * @return pagination: Paged list of human resources types which may be in
	 *         the view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paged list of human resources types which may be in the
	 *            view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return hrTypes: Object of human resources.
	 */
	public HrTypes getHrTypes() {
		return hrTypes;
	}

	/**
	 * @param hrTypes
	 *            : Object of human resources.
	 */
	public void setHrTypes(HrTypes hrTypes) {
		this.hrTypes = hrTypes;
	}

	/**
	 * @return nameSearch: Gets the name you want to find in the types of human
	 *         resources.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Sets the name you want to find in the types of human
	 *            resources.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load an initial
	 * list of human resources types.
	 * 
	 * @return searchHrTypes: method that queries resource types human, returns
	 *         to the template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return searchHrTypes();
	}

	/**
	 * Search the list of hrTypes.
	 * 
	 * @return gesHrTypes: Navigation rule that redirects to manageHrtypes
	 *         template.
	 */
	public String searchHrTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle hrBundle = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		hrTypesList = new ArrayList<HrTypes>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedQuery(queryBuilder, parameters, bundle, jointSearchMessages);
			Long amount = hrTypesDao.amountHrTypes(queryBuilder, parameters);
			if (amount != null) {
				pagination.paginar(amount);
			}
			hrTypesList = hrTypesDao.searchHrTypes(pagination.getInicio(),
					pagination.getRango(), queryBuilder, parameters);
			if ((hrTypesList == null || hrTypesList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (hrTypesList == null || hrTypesList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								hrBundle.getString("human_resource_type_label"),
								jointSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesHrTypes";
	}

	/**
	 * This method builds the query to the advanced search, and it also creates
	 * messages to display depending on the search criteria selected by the
	 * user.
	 * 
	 * @param query
	 *            : Query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : Variable to access message tags for software
	 *            internationalization.
	 * @param searchMessage
	 *            : Message displayed.
	 */
	private void advancedQuery(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder searchMessage) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append("WHERE UPPER(ht.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			searchMessage.append(bundle.getString("label_name") + ": " + '"'
					+ this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new type of human resources.
	 * 
	 * @param hrTypes
	 *            : Human resources type name to be added or edited.
	 * @return "regHrTypes": Redirects to register in the human resources type
	 *         template.
	 */
	public String addEditHrTypes(HrTypes hrTypes) {
		if (hrTypes != null) {
			this.hrTypes = hrTypes;
		} else {
			this.hrTypes = new HrTypes();
		}
		return "regHrTypes";
	}

	/**
	 * To validate the name of the kinds of human resources, so that it is not
	 * repeated in the database and it is valid compared with XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = hrTypes.getIdHrType();
			HrTypes auxHrTypes = hrTypesDao.nameExists(name, id);
			if (auxHrTypes != null) {
				String existenceMessage = "message_ya_existe_verifique";
				ControladorContexto.mensajeErrorEspecifico(clientId,
						existenceMessage, "mensaje");
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
	 * Method to save or edit the types of human resources.
	 * 
	 * @modify 30/07/2015 Gerardo.Herrera
	 * 
	 * @return searchHrTypes: Redirects to manage human resources types with the
	 *         list of names updated.
	 */
	public String saveHrTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		try {

			if (hrTypes.getIdHrType() != 0) {
				hrTypesDao.editHrTypes(hrTypes);
			} else {
				registerMessage = "message_registro_guardar";
				hrTypesDao.saveHrTypes(hrTypes);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), hrTypes.getName()));

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchHrTypes();
	}

	/**
	 * Method to delete a type of human resources database.
	 * 
	 * @return searchHrTypes: Query the list of human resources types and
	 *         returns to manage the types of human resources template.
	 */
	public String deleteHrTypes() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			hrTypesDao.deleteHrTypes(hrTypes);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					hrTypes.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					hrTypes.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return searchHrTypes();
	}
}