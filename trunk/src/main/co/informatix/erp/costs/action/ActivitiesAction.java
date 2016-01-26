package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.action.RecordActivitiesActualsAction;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControllerSortField;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utz.dao.ActivitiesAndCertificationsDao;
import co.informatix.erp.utz.dao.CertificationsAndRolesDao;
import co.informatix.erp.utz.entities.ActivitiesAndCertifications;
import co.informatix.erp.utz.entities.CertificationsAndRoles;

/**
 * This class allows the logic of the activities that can be BD. The logic is:
 * insert activities
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAction implements Serializable {
	@EJB
	private ActivitiesDao activitiesDao;

	@EJB
	private CertificationsAndRolesDao certificationsAndRolesDao;

	@EJB
	private ActivitiesAndCertificationsDao activitiesAndCertificationsDao;

	private int idCrop;
	private boolean reportingActuals = false;
	private boolean sort;

	private ActivitiesAndCertifications activitiesAndCertifications;
	private Activities activities;
	private Crops crops;
	private Activities selectedActivities;
	private Paginador pager = new Paginador();

	private List<SelectItem> itemsCertificationsAndRoles;
	private List<SelectItem> itemsActivities;
	private List<Activities> listActivities;

	/**
	 * @return idCrop: crop identifier
	 */
	public int getIdCrop() {
		return idCrop;
	}

	/**
	 * @param idCrop
	 *            : crop identifier
	 */
	public void setIdCrop(int idCrop) {
		this.idCrop = idCrop;
	}

	/**
	 * @return reportingActuals: Bandera que permite modificar la consulta de
	 *         las actividades.
	 */
	public boolean isReportingActuals() {
		return reportingActuals;
	}

	/**
	 * @param reportingActuals
	 *            : Bandera que permite modificar la consulta de las
	 *            actividades.
	 */
	public void setReportingActuals(boolean reportingActuals) {
		this.reportingActuals = reportingActuals;
	}

	/**
	 * @return sort: Flag indicate if the consult builder put the sort
	 */
	public boolean isSort() {
		return sort;
	}

	/**
	 * @param sort
	 *            : Flag indicate if the consult builder put the sort
	 */
	public void setSort(boolean sort) {
		this.sort = sort;
	}

	/**
	 * @return activities: object containing activities data
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : object containing activities data
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return crops: object containing crops data
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : object containing crops data
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return selectedActivities: activity object selected
	 */
	public Activities getSelectedActivities() {
		return selectedActivities;
	}

	/**
	 * @param selectedActivities
	 *            : activity object selected
	 */
	public void setSelectedActivities(Activities selectedActivities) {
		this.selectedActivities = selectedActivities;
	}

	/**
	 * @return pager: Management paged list of activities
	 */
	public Paginador getPager() {
		return pager;
	}

	/**
	 * @param pager
	 *            : Management paged list of activities
	 */
	public void setPager(Paginador pager) {
		this.pager = pager;
	}

	/**
	 * Method that gets the activitiesAndCertifications whose foreign to
	 * activities
	 * 
	 * @return activitiesAndCertifications:activitiesAndCertifications obtenida
	 */
	public ActivitiesAndCertifications getActivitiesAndCertifications() {
		return activitiesAndCertifications;
	}

	/**
	 * Method that set the activitiesAndCertifications whose foreign to
	 * activities
	 * 
	 * @param activitiesAndCertifications
	 *            :activitiesAndCertifications a setear
	 * 
	 */
	public void setActivitiesAndCertifications(
			ActivitiesAndCertifications activitiesAndCertifications) {
		this.activitiesAndCertifications = activitiesAndCertifications;
	}

	/**
	 * @return itemsCertificationsAndRoles: We obtain the item of the
	 *         certificates
	 */
	public List<SelectItem> getItemsCertificationsAndRoles() {
		return itemsCertificationsAndRoles;
	}

	/**
	 * @param itemsCertificationsAndRoles
	 *            : We obtain the item of the certificates
	 */
	public void setItemsCertificationsAndRoles(
			List<SelectItem> itemsCertificationsAndRoles) {
		this.itemsCertificationsAndRoles = itemsCertificationsAndRoles;
	}

	/**
	 * @return itemsActivities: list of activities
	 */
	public List<SelectItem> getItemsActivities() {
		return itemsActivities;
	}

	/**
	 * @param itemsActivities
	 *            : list of activities
	 */
	public void setItemsActivities(List<SelectItem> itemsActivities) {
		this.itemsActivities = itemsActivities;
	}

	/**
	 * @return listActivities: list of activities object
	 */
	public List<Activities> getListActivities() {
		return listActivities;
	}

	/**
	 * @param listActivities
	 *            : list of activities object
	 */
	public void setListActivities(List<Activities> listActivities) {
		this.listActivities = listActivities;
	}

	/**
	 * Initialize the activities
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param activity
	 *            : activities object.
	 * @param idCrop
	 *            : crop identifier.
	 */
	public void initializeActivities(Activities activity, int idCrop) {
		this.activities = activity;
		this.idCrop = idCrop;
		this.pager = new Paginador();
		consultingActivities();
	}

	/**
	 * It allows consulting activities.
	 * 
	 * @author Gerardo.Herrera
	 */
	public void consultingActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleCostos = ControladorContexto
				.getBundle("mensajeCosts");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listActivities = new ArrayList<Activities>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		StringBuilder order = new StringBuilder();
		String searchMessage = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		sort = false;
		try {
			advancedSearch(query, parameters, bundle, bundleCostos,
					unionMessagesSearch, fromModal, order);
			Long quantity = activitiesDao.cantidadActivities(query, parameters);
			order.setLength(0);
			sort = true;
			advancedSearch(query, parameters, bundle, bundleCostos,
					unionMessagesSearch, fromModal, order);
			query.append(order);
			if (quantity != null) {
				if (quantity > 5) {
					pager.paginarRangoDefinido(quantity, 5);
				} else {
					pager.paginar(quantity);
				}
				this.listActivities = activitiesDao.consultarActivities(
						pager.getInicio(), pager.getRango(), query, parameters);

				if (fromModal) {
					RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
							.getContextBean(RecordActivitiesActualsAction.class);
					recordActivitiesActualsAction
							.setListActivities(listActivities);
				}

			}
			if ((this.listActivities == null || listActivities.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listActivities == null || listActivities.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_not_exists_activities_hr"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleCostos.getString("activities_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search for activities
	 * building also allows messages to be displayed depending on the criteria
	 * search selected by the user.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param query
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * @param bundleCosts
	 *            : access language labels cost
	 * @param unionMessagesSearch
	 *            : Message search
	 * @param fromModal
	 *            : boolean that indicates whether this method is called from a
	 *            modal window or not
	 * @param order
	 *            : flag indicates if the query have a order to list
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			ResourceBundle bundleCosts, StringBuilder unionMessagesSearch,
			boolean fromModal, StringBuilder order) {
		boolean selection = false;
		boolean showSearchMessage = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE);
		if (query.length() > 0) {
			query.setLength(0);
			query.append("JOIN FETCH ");
		} else {
			query.append("JOIN ");
			showSearchMessage = true;
		}
		query.append("a.activityName an ");
		if (this.activities.getIdActivity() != 0 && this.activities != null) {
			query.append(selection ? "AND " : "WHERE ");
			query.append("an.idActivityName = :keywordActivity ");
			SelectItem item = new SelectItem(this.activities.getIdActivity(),
					"keywordActivity");
			parameters.add(item);
			if (!showSearchMessage)
				unionMessagesSearch.append(bundleCosts
						.getString("activities_label")
						+ ": "
						+ '"'
						+ ValidacionesAction.getLabel(itemsActivities,
								this.activities.getIdActivity())
						+ " "
						+ '"'
						+ " ");
			selection = true;
		}
		if (this.activities.getDescription() != null
				&& !"".equals(this.activities.getDescription())) {
			query.append(selection ? "AND " : "WHERE ");
			query.append("UPPER(a.description) LIKE UPPER(:keywordDescription) ");
			SelectItem item = new SelectItem("%"
					+ this.activities.getDescription() + "%",
					"keywordDescription");
			parameters.add(item);
			if (!showSearchMessage)
				unionMessagesSearch.append(bundle
						.getString("label_descripcion")
						+ ": "
						+ '"'
						+ this.activities.getDescription() + '"' + " ");
			selection = true;
		}
		if (this.activities.getInitialDtBudget() != null
				&& this.activities.getFinalDtBudget() != null) {
			query.append(selection ? "AND " : "WHERE ");
			query.append("a.initialDtBudget BETWEEN :keywordDateInitial AND :keywordDateFinal ");
			query.append("AND a.finalDtBudget BETWEEN :keywordDateInitial AND :keywordDateFinal ");
			SelectItem itemInitial = new SelectItem(
					this.activities.getInitialDtBudget(), "keywordDateInitial");
			SelectItem itemFinal = new SelectItem(
					this.activities.getFinalDtBudget(), "keywordDateFinal");
			parameters.add(itemInitial);
			parameters.add(itemFinal);
			if (!showSearchMessage) {
				unionMessagesSearch.append(bundle
						.getString("label_fecha_inicio")
						+ ": "
						+ '"'
						+ dateFormat.format(this.activities
								.getInitialDtBudget()) + '"' + " ");
				unionMessagesSearch.append(bundle.getString("label_fecha_fin")
						+ ": " + '"'
						+ dateFormat.format(this.activities.getFinalDtBudget())
						+ '"' + " ");
			}
			selection = true;
		}
		if (this.idCrop != 0) {
			query.append(selection ? "AND " : "WHERE ");
			query.append("a.crop.idCrop = :keywordIdCrop ");
			SelectItem item = new SelectItem(this.idCrop, "keywordIdCrop");
			parameters.add(item);
			selection = true;
		}
		if (fromModal) {
			query.append(selection ? "AND " : "WHERE ");
			query.append("(a IN ");
			query.append("(SELECT a FROM ActivitiesAndHr ah ");
			query.append("JOIN ah.activitiesAndHrPK.activities a) ");
			query.append("OR a IN ");
			query.append("(SELECT ac FROM ActivityMachine am ");
			query.append("JOIN am.activityMachinePK.activities ac)) ");
			selection = true;
		}
		if (!fromModal) {
			query.append("AND a.costHrActual IS NULL ");
		}
		query.append(selection ? "AND " : "WHERE ");
		query.append("(a.machineRequired = true ");
		query.append("OR a.hrRequired = true ) ");

		if (sort) {
			ControllerSortField controllerSortField = ControladorContexto
					.getContextBean(ControllerSortField.class);
			controllerSortField.sort();
			if (controllerSortField.getProperty() != null
					&& controllerSortField.getOrder() != null) {
				order.append(" ORDER BY " + controllerSortField.getProperty()
						+ " " + controllerSortField.getOrder());
			} else {
				order.append("ORDER BY a.initialDtBudget DESC ");
			}
		}
	}

	/**
	 * Assign the selected activity
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param actualActivity
	 *            : activities object.
	 */
	public void assignActivities(Activities actualActivity) {
		this.selectedActivities = new Activities();
		actualActivity.setSeleccionado(true);
		for (Activities activity : listActivities) {
			if (activity.isSeleccionado()) {
				if (activity.getIdActivity() == actualActivity.getIdActivity()) {
					this.selectedActivities = activity;
				} else {
					activity.setSeleccionado(false);
				}
			}
		}
	}

	/**
	 * Method of setting name selected activity in the popup
	 * 
	 * @param activityNames
	 *            : Name of activity that is load from popup.
	 */
	public void cargarActivityNames(ActivityNames activityNames) {
		this.activities.setActivityName(activityNames);
	}

	/**
	 * Method to clear the name of activity
	 */
	public void limpiarActivityNames() {
		this.activities.setActivityName(new ActivityNames());
	}

	/**
	 * Charge method to add or edit activities
	 * 
	 * @param activities
	 *            : activity to add or edit
	 * @return "regActivities": log view activities
	 */
	public String agregarEditarActivities(Activities activities) {
		try {
			if (activities != null) {
				this.activities = activities;
			} else {
				this.activities = new Activities();
				this.activities.setActivityName(new ActivityNames());
				this.activities.setCrop(this.crops);

				this.activitiesAndCertifications = new ActivitiesAndCertifications();
				this.activitiesAndCertifications
						.getActivitiesAndCertificationsPK().setActivities(
								this.activities);
			}
			cargarComboCertificationsAndRoles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regActivities";
	}

	/**
	 * Method used to save or edit activities
	 * 
	 * @return agregarEditarActivities: Redirects to record activities
	 * 
	 */
	public String guardarActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (activities.getIdActivity() != 0) {
				activitiesDao.editarActivities(activities);
			} else {
				mensajeRegistro = "message_registro_guardar";
				activitiesDao.guardarActivities(activities);
				activitiesAndCertificationsDao
						.guardarActivitiesAndCertifications(activitiesAndCertifications);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					activities.getIdActivity()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return agregarEditarActivities(null);
	}

	/**
	 * Validates fields that are required at the hearing at saving time.
	 */
	public void requeridosOk() {
		if (activities.getActivityName() == null
				|| activities.getActivityName().getActivityName() == null) {
			ControladorContexto
					.mensajeRequeridos("formActivities:txtActivityNames");
		}
	}

	/**
	 * Charge method to load the select item types CertificationsAndRoles
	 * 
	 * @throws Exception
	 */
	private void cargarComboCertificationsAndRoles() throws Exception {
		List<CertificationsAndRoles> listaCertificationsAndRoles = certificationsAndRolesDao
				.consultarCertificationsAndRoles();
		this.itemsCertificationsAndRoles = new ArrayList<SelectItem>();
		for (CertificationsAndRoles certificationsAndRoles : listaCertificationsAndRoles) {
			this.itemsCertificationsAndRoles.add(new SelectItem(
					certificationsAndRoles.getIdCertificactionsAndRoles(),
					certificationsAndRoles.getName()));
		}
	}

}
