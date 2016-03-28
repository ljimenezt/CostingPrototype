package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
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
 * This class implements the logic of the activities that can be stored in the
 * database.
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
	 * @return reportingActuals: Flag to modify the activities query.
	 */
	public boolean isReportingActuals() {
		return reportingActuals;
	}

	/**
	 * @param reportingActuals
	 *            : Flag to modify the activities query.
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
	 * @return activities: object containing activity data.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : object containing activity data
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return crops: object containing crop data.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : object containing crop data.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return selectedActivities: Activity object that is selected.
	 */
	public Activities getSelectedActivities() {
		return selectedActivities;
	}

	/**
	 * @param selectedActivities
	 *            : activity object that is selected.
	 */
	public void setSelectedActivities(Activities selectedActivities) {
		this.selectedActivities = selectedActivities;
	}

	/**
	 * @return pager: Paged list of activities.
	 */
	public Paginador getPager() {
		return pager;
	}

	/**
	 * @param pager
	 *            : Paged list of activities.
	 */
	public void setPager(Paginador pager) {
		this.pager = pager;
	}

	/**
	 * Method that gets the activitiesAndCertifications for activities.
	 * 
	 * @return activitiesAndCertifications:activitiesAndCertifications you want
	 *         to get.
	 */
	public ActivitiesAndCertifications getActivitiesAndCertifications() {
		return activitiesAndCertifications;
	}

	/**
	 * Method that sets the activitiesAndCertifications for activities.
	 * 
	 * @param activitiesAndCertifications
	 *            :activitiesAndCertifications you want to set.
	 * 
	 */
	public void setActivitiesAndCertifications(
			ActivitiesAndCertifications activitiesAndCertifications) {
		this.activitiesAndCertifications = activitiesAndCertifications;
	}

	/**
	 * @return itemsCertificationsAndRoles: Obtain the items for certifications.
	 */
	public List<SelectItem> getItemsCertificationsAndRoles() {
		return itemsCertificationsAndRoles;
	}

	/**
	 * @param itemsCertificationsAndRoles
	 *            : Set the items for certifications
	 */
	public void setItemsCertificationsAndRoles(
			List<SelectItem> itemsCertificationsAndRoles) {
		this.itemsCertificationsAndRoles = itemsCertificationsAndRoles;
	}

	/**
	 * @return itemsActivities: list of activities.
	 */
	public List<SelectItem> getItemsActivities() {
		return itemsActivities;
	}

	/**
	 * @param itemsActivities
	 *            : list of activities.
	 */
	public void setItemsActivities(List<SelectItem> itemsActivities) {
		this.itemsActivities = itemsActivities;
	}

	/**
	 * @return listActivities: list of activity objects.
	 */
	public List<Activities> getListActivities() {
		return listActivities;
	}

	/**
	 * @param listActivities
	 *            : list of activity objects.
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
		searchActivities();
	}

	/**
	 * It queries activities.
	 * 
	 * @author Gerardo.Herrera
	 */
	public void searchActivities() {
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
			Long quantity = activitiesDao.amountActivities(query, parameters);
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
				this.listActivities = activitiesDao.queryActivities(
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
	 * This method builds the query with an advanced search; it also builds
	 * display messages depending on the criteria search selected by the user.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @modify 14/01/2016 Wilhelm.Boada
	 * 
	 * @param queryBuilder
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * @param bundleCosts
	 *            : Contexts to access costs language labels.
	 * @param jointSearchMessages
	 *            : Search message.
	 * @param fromModal
	 *            : boolean that indicates whether this method is called from a
	 *            modal window or not.
	 * @param order
	 *            : Flag that indicates if the query have an order to list
	 */
	private void advancedSearch(StringBuilder queryBuilder,
			List<SelectItem> parameters, ResourceBundle bundle,
			ResourceBundle bundleCosts, StringBuilder jointSearchMessages,
			boolean fromModal, StringBuilder order) {
		boolean selection = false;
		boolean showSearchMessage = false;
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE);
		if (queryBuilder.length() > 0) {
			queryBuilder.setLength(0);
			queryBuilder.append("JOIN FETCH ");
		} else {
			queryBuilder.append("JOIN ");
			showSearchMessage = true;
		}
		queryBuilder.append("a.activityName an ");
		if (this.activities.getIdActivity() != 0 && this.activities != null) {
			queryBuilder.append(selection ? "AND " : "WHERE ");
			queryBuilder.append("an.idActivityName = :keywordActivity ");
			SelectItem item = new SelectItem(this.activities.getIdActivity(),
					"keywordActivity");
			parameters.add(item);
			if (!showSearchMessage)
				jointSearchMessages.append(bundleCosts
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
			queryBuilder.append(selection ? "AND " : "WHERE ");
			queryBuilder
					.append("UPPER(a.description) LIKE UPPER(:keywordDescription) ");
			SelectItem item = new SelectItem("%"
					+ this.activities.getDescription() + "%",
					"keywordDescription");
			parameters.add(item);
			if (!showSearchMessage)
				jointSearchMessages.append(bundle
						.getString("label_description")
						+ ": "
						+ '"'
						+ this.activities.getDescription() + '"' + " ");
			selection = true;
		}
		if (this.activities.getInitialDtBudget() != null
				&& this.activities.getFinalDtBudget() != null) {
			Calendar cal = Calendar.getInstance();
			cal.setTime(this.activities.getFinalDtBudget());
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			this.activities.setFinalDtBudget(cal.getTime());
			queryBuilder.append(selection ? "AND " : "WHERE ");
			queryBuilder
					.append("a.initialDtBudget BETWEEN :keywordDateInitial AND :keywordDateFinal ");
			queryBuilder
					.append("AND a.finalDtBudget BETWEEN :keywordDateInitial AND :keywordDateFinal ");
			SelectItem itemInitial = new SelectItem(
					this.activities.getInitialDtBudget(), "keywordDateInitial");
			SelectItem itemFinal = new SelectItem(
					this.activities.getFinalDtBudget(), "keywordDateFinal");
			parameters.add(itemInitial);
			parameters.add(itemFinal);
			if (!showSearchMessage) {
				jointSearchMessages.append(bundle
						.getString("label_fecha_inicio")
						+ ": "
						+ '"'
						+ dateFormat.format(this.activities
								.getInitialDtBudget()) + '"' + " ");
				jointSearchMessages.append(bundle.getString("label_fecha_fin")
						+ ": " + '"'
						+ dateFormat.format(this.activities.getFinalDtBudget())
						+ '"' + " ");
			}
			selection = true;
		}
		if (this.idCrop != 0) {
			queryBuilder.append(selection ? "AND " : "WHERE ");
			queryBuilder.append("a.crop.idCrop = :keywordIdCrop ");
			SelectItem item = new SelectItem(this.idCrop, "keywordIdCrop");
			parameters.add(item);
			selection = true;
		}
		if (fromModal) {
			queryBuilder.append(selection ? "AND " : "WHERE ");
			queryBuilder.append("(a IN ");
			queryBuilder.append("(SELECT a FROM ActivitiesAndHr ah ");
			queryBuilder.append("JOIN ah.activitiesAndHrPK.activities a) ");
			queryBuilder.append("OR a IN ");
			queryBuilder.append("(SELECT ac FROM ActivityMachine am ");
			queryBuilder.append("JOIN am.activityMachinePK.activities ac)) ");
			selection = true;
		}
		if (!fromModal) {
			queryBuilder.append("AND a.generalCostActual IS NULL ");
		}
		queryBuilder.append(selection ? "AND " : "WHERE ");
		queryBuilder.append("(a.machineRequired = true ");
		queryBuilder.append("OR a.hrRequired = true ) ");

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
	 * Assign the selected activity.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param actualActivity
	 *            : Activities object.
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
	 * Method to set the selected activity name in the popup.
	 * 
	 * @param activityNames
	 *            : Name of activity that is load from popup.
	 */
	public void loadActivityNames(ActivityNames activityNames) {
		this.activities.setActivityName(activityNames);
	}

	/**
	 * Method to clear the selected activity name of activity
	 */
	public void clearActivityNames() {
		this.activities.setActivityName(new ActivityNames());
	}

	/**
	 * Method to add or edit activities.
	 * 
	 * @param activities
	 *            : activity to add or edit.
	 * @return "regActivities": register activities template.
	 */
	public String addEditActivities(Activities activities) {
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
			loadComboCertificationsAndRoles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regActivities";
	}

	/**
	 * Method to save or edit activities.
	 * 
	 * @return addEditActivities: Redirects to register activities template.
	 * 
	 */
	public String saveActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		try {

			if (activities.getIdActivity() != 0) {
				activitiesDao.editActivities(activities);
			} else {
				registerMessage = "message_registro_guardar";
				activitiesDao.saveActivities(activities);
				activitiesAndCertificationsDao
						.saveActivitiesAndCertifications(activitiesAndCertifications);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage),
					activities.getIdActivity()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return addEditActivities(null);
	}

	/**
	 * Validates fields that are required when saving occurs.
	 */
	public void verifyRequirements() {
		if (activities.getActivityName() == null
				|| activities.getActivityName().getActivityName() == null) {
			ControladorContexto
					.mensajeRequeridos("formActivities:txtActivityNames");
		}
	}

	/**
	 * Method to load the select item types CertificationsAndRoles.
	 * 
	 * @throws Exception
	 */
	private void loadComboCertificationsAndRoles() throws Exception {
		List<CertificationsAndRoles> certificationsAndRolesList = certificationsAndRolesDao
				.consultCertificationsAndRoles();
		this.itemsCertificationsAndRoles = new ArrayList<SelectItem>();
		for (CertificationsAndRoles certificationsAndRoles : certificationsAndRolesList) {
			this.itemsCertificationsAndRoles.add(new SelectItem(
					certificationsAndRoles.getIdCertificactionsAndRoles(),
					certificationsAndRoles.getName()));
		}
	}

}
