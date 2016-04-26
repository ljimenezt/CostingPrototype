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

	private List<ActivityNames> activityNamesList;

	private ActivityNames activityNames;
	private Paginador pagination = new Paginador();

	private String nameSearch;

	/**
	 * @return List<ActivityNames>: list of activity names displayed on the user
	 *         interface.
	 */
	public List<ActivityNames> getActivityNamesList() {
		return activityNamesList;
	}

	/**
	 * @param activityNamesList
	 *            :list of activities names displayed on the user interface.
	 */
	public void setActivityNamesList(List<ActivityNames> activityNamesList) {
		this.activityNamesList = activityNamesList;
	}

	/**
	 * Gets data from an activity name.
	 * 
	 * @return ActivityNames: An activity name object.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * Sets data from an activity name.
	 * 
	 * @param activityNames
	 *            : object containing activity name data.
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return pagination: Page list to manage the names of activities.
	 * 
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Page list to manage the names of activities.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Activity name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Activity name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of the names of activities
	 * 
	 * @return queryActivityNames: method to query the names of activities and
	 *         returns to the template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return queryActivityNames();
	}

	/**
	 * Consult the list of the names of the activities to show either a POPUP or
	 * in management
	 * 
	 * @modify Johnatan.Naranjo 22/04/2015
	 * 
	 * @return result: redirects to the template name to manage activity names or
	 *         POPUP
	 */
	public String queryActivityNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		activityNamesList = new ArrayList<ActivityNames>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder queryJointMessages = new StringBuilder();
		String queryMessage = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromWindow = (param2 != null && "si".equals(param2)) ? true
				: false;
		String result = fromWindow ? "" : "gesActivityNames";

		try {
			advancedQuery(query, parameters, bundle, queryJointMessages);
			Long amount = activityNamesDao.amountActivityNames(query,
					parameters);
			if (amount != null) {
				if (fromWindow) {
					pagination.paginarRangoDefinido(amount, 5);
				} else {
					pagination.paginar(amount);
				}

			}
			activityNamesList = activityNamesDao.queryActivityNames(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((activityNamesList == null || activityNamesList.size() <= 0)
					&& !"".equals(queryJointMessages.toString())) {
				queryMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								queryJointMessages);
			} else if (activityNamesList == null
					|| activityNamesList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(queryJointMessages.toString())) {
				queryMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								queryJointMessages);
			}
			validations.setMensajeBusqueda(queryMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return result;
	}

	/**
	 * This method builds the query to the advanced search; it also allows
	 * messages to display building depending on the search criteria selected by
	 * the user.
	 * 
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 * 
	 */
	private void advancedQuery(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new activity name.
	 * 
	 * @param activityNames
	 *            : Name of activity you are adding or editing.
	 * 
	 * @return "regActivityNames": redirects to the record template activity.
	 */
	public String addModifyActivityNames(ActivityNames activityNames) {
		if (activityNames != null) {
			this.activityNames = activityNames;
		} else {
			this.activityNames = new ActivityNames();
		}
		return "regActivityNames";
	}

	/**
	 * This method validate the name of the activity, so that it is not repeated
	 * in the database and validates against XSS.
	 * 
	 * @param context
	 *            : Application context.
	 * 
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value to be validated.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = activityNames.getIdActivityName();
			ActivityNames activityNamesAux = activityNamesDao.nameExists(nombre, id);
			if (activityNamesAux != null) {
				String existenceMessage = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(existenceMessage), null));
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
	 * Method to save or edit the names of the activities
	 * 
	 * @return queryActivityNames: Redirects manage activities with the list of
	 *         names updated
	 */
	public String saveActivityNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String recordMessage = "message_registro_modificar";
		try {

			if (activityNames.getIdActivityName() != 0) {
				activityNamesDao.modifyActivityNames(activityNames);
			} else {
				recordMessage = "message_registro_guardar";
				activityNamesDao.saveActivityNames(activityNames);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(recordMessage),
					activityNames.getActivityName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return queryActivityNames();
	}

	/**
	 * Method to remove a name from activity name table.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @return queryActivityNames: Redirects to manage the names of activities
	 *         with the updated list.
	 */
	public String deleteActivityNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			activityNamesDao.deleteActivityNames(activityNames);
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
		return queryActivityNames();
	}

}
