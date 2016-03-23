package co.informatix.erp.warehouse.action;

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

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.TypeOfManagementDao;
import co.informatix.erp.warehouse.entities.TypeOfManagement;

/**
 * This class contains the logic related to create, update, and delete the names
 * of management types in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TypeOfManagementAction implements Serializable {
	private List<TypeOfManagement> typeOfManagementList;
	private Paginador pagination = new Paginador();
	private TypeOfManagement typeOfManagement;
	private String nameSearch;

	@EJB
	private TypeOfManagementDao typeOfManagementDao;

	/**
	 * @return typeOfManagementList: List of types of management.
	 */
	public List<TypeOfManagement> getTypeOfManagementList() {
		return typeOfManagementList;
	}

	/**
	 * @param typeOfManagementList
	 *            : List of types of management.
	 */
	public void setTypeOfManagementList(
			List<TypeOfManagement> typeOfManagementList) {
		this.typeOfManagementList = typeOfManagementList;
	}

	/**
	 * @return pagination: Page list of the types of management that a view may
	 *         have.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Page list of the types of management that a view may have.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return typeOfManagement: Object of management types.
	 */
	public TypeOfManagement getTypeOfManagement() {
		return typeOfManagement;
	}

	/**
	 * @param typeOfManagement
	 *            : Object of management types.
	 * 
	 */
	public void setTypeOfManagement(TypeOfManagement typeOfManagement) {
		this.typeOfManagement = typeOfManagement;
	}

	/**
	 * @return nameSearch: Name by which you want to consult the types of
	 *         management.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name by which you want to consult the types of management.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of types of management.
	 * 
	 * @return searchTypeOfManagement: Method consulting management types,
	 *         returns to the template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return searchTypeOfManagement();
	}

	/**
	 * Search a list with the types of management.
	 * 
	 * @return gesTypeManag: Navigation rule that redirects to manage management
	 *         types.
	 */
	public String searchTypeOfManagement() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		typeOfManagementList = new ArrayList<TypeOfManagement>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedQuery(queryBuilder, parameters, bundle, jointSearchMessages);
			Long amount = typeOfManagementDao.amountTypeOfManagement(
					queryBuilder, parameters);
			if (amount != null) {
				pagination.paginar(amount);
			}
			typeOfManagementList = typeOfManagementDao.searchTypeOfManagement(
					pagination.getInicio(), pagination.getRango(),
					queryBuilder, parameters);
			if ((typeOfManagementList == null || typeOfManagementList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (typeOfManagementList == null
					|| typeOfManagementList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse
										.getString("type_management_label"),
								jointSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesTypeManag";
	}

	/**
	 * This method allows to build the query with an advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access to the language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 * 
	 */
	private void advancedQuery(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(tm.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new type of management.
	 * 
	 * @param typeOfManagement
	 *            : Name the type of management that will add or edit.
	 * 
	 * @return regTypeManag: Redirected to the register management type
	 *         template.
	 */
	public String addEditTypeOfManagement(TypeOfManagement typeOfManagement) {
		if (typeOfManagement != null) {
			this.typeOfManagement = typeOfManagement;
		} else {
			this.typeOfManagement = new TypeOfManagement();
		}
		return "regTypeManag";
	}

	/**
	 * To validate the name of the types of management, so it is not repeated in
	 * the database and validates against XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * 
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is validated.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = typeOfManagement.getIdTypeOfManagement();
			TypeOfManagement auxTypeOfManagement = new TypeOfManagement();
			auxTypeOfManagement = typeOfManagementDao.nameExists(name, id);
			if (auxTypeOfManagement != null) {
				String existenceMessage = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(existenceMessage), null));
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
	 * Method used to save or edit the types of management.
	 * 
	 * @modify 13/05/2015 Cristhian.Pico
	 * 
	 * @return searchTypeOfManagement: Redirects to manage types of management
	 *         with the list of names updated.
	 */
	public String saveTypeOfManagement() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (typeOfManagement.getIdTypeOfManagement() != 0) {
				typeOfManagementDao.editTypeOfManagement(typeOfManagement);
			} else {
				mensajeRegistro = "message_registro_guardar";
				typeOfManagementDao.saveTypeOfManagement(typeOfManagement);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					typeOfManagement.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchTypeOfManagement();
	}

	/**
	 * Method to delete a type of management of the database.
	 * 
	 * 
	 * @return searchTypeOfManagement: Look for the list of the types of
	 *         management and returns to the template manage types of
	 *         management.
	 */
	public String deleteTypeOfManagement() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			typeOfManagementDao.deleteTypeOfManagement(typeOfManagement);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					typeOfManagement.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					typeOfManagement.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return searchTypeOfManagement();
	}

}
