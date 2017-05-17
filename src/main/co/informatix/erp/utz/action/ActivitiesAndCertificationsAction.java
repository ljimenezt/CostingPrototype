package co.informatix.erp.utz.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utz.dao.ActivitiesAndCertificationsDao;
import co.informatix.erp.utz.dao.CertificationsAndRolesDao;
import co.informatix.erp.utz.entities.ActivitiesAndCertifications;
import co.informatix.erp.utz.entities.ActivitiesAndCertificationsPK;
import co.informatix.erp.utz.entities.CertificationsAndRoles;

/**
 * This class is all related logic with creating activities and certifications
 * in the system.
 * 
 * @author Mabell.Boada
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAndCertificationsAction implements Serializable {
	private List<ActivityNames> listActivitiesNames;
	private List<ActivitiesAndCertifications> listActivities;
	private List<SelectItem> itemsCertificationsAndRoles;
	private List<SelectItem> itemsActivities;

	private CertificationsAndRoles certificationsAndRoles;
	private Activities activities;
	private ActivityNames activityNames;
	private ActivitiesAndCertifications activitiesAndCertifications;
	private ActivitiesAndCertificationsPK activitiesAndCertificationsPK;
	private Paginador pagination = new Paginador();
	private Paginador activitiesPagination = new Paginador();

	private int idCertAndRoles;

	private String nameSearch;

	@EJB
	private CertificationsAndRolesDao certificationsAndRolesDao;
	@EJB
	private ActivitiesAndCertificationsDao activitiesAndCertificationsDao;
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private ActivityNamesDao activityNamesDao;

	/**
	 * @return listActivitiesNames: List of activities names.
	 */
	public List<ActivityNames> getListActivitiesNames() {
		return listActivitiesNames;
	}

	/**
	 * @param listActivitiesNames
	 *            : List of activities names.
	 */
	public void setListActivitiesNames(List<ActivityNames> listActivitiesNames) {
		this.listActivitiesNames = listActivitiesNames;
	}

	/**
	 * @return listActivities: List of activitiesAndCertifications.
	 */
	public List<ActivitiesAndCertifications> getListActivities() {
		return listActivities;
	}

	/**
	 * @param listActivities
	 *            : List of activitiesAndCertifications.
	 */
	public void setListActivities(
			List<ActivitiesAndCertifications> listActivities) {
		this.listActivities = listActivities;
	}

	/**
	 * @return itemsCertificationsAndRoles: List items certifications and roles.
	 */
	public List<SelectItem> getItemsCertificationsAndRoles() {
		return itemsCertificationsAndRoles;
	}

	/**
	 * @param itemsCertificationsAndRoles
	 *            : List items certifications and roles.
	 */
	public void setItemsCertificationsAndRoles(
			List<SelectItem> itemsCertificationsAndRoles) {
		this.itemsCertificationsAndRoles = itemsCertificationsAndRoles;
	}

	/**
	 * @return itemsActivities: List of items of activities.
	 */
	public List<SelectItem> getItemsActivities() {
		return itemsActivities;
	}

	/**
	 * @param itemsActivities
	 *            : List of items of activities.
	 */
	public void setItemsActivities(List<SelectItem> itemsActivities) {
		this.itemsActivities = itemsActivities;
	}

	/**
	 * @return certificationsAndRoles: Object to certification and associated
	 *         roles.
	 */
	public CertificationsAndRoles getCertificationsAndRoles() {
		return certificationsAndRoles;
	}

	/**
	 * @param certificationsAndRoles
	 *            : Object to certification and associated roles.
	 */
	public void setCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) {
		this.certificationsAndRoles = certificationsAndRoles;
	}

	/**
	 * @return activities: Object associated activities.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : Object associated activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return activityNames: Name Object associated activity.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Name Object associated activity.
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return activitiesAndCertifications: Object and certification activities.
	 */
	public ActivitiesAndCertifications getActivitiesAndCertifications() {
		return activitiesAndCertifications;
	}

	/**
	 * @param activitiesAndCertifications
	 *            : Object and certification activities.
	 */
	public void setActivitiesAndCertifications(
			ActivitiesAndCertifications activitiesAndCertifications) {
		this.activitiesAndCertifications = activitiesAndCertifications;
	}

	/**
	 * @return activitiesAndCertificationsPK: Object the composite key
	 *         activities and certifications.
	 */
	public ActivitiesAndCertificationsPK getActivitiesAndCertificationsPK() {
		return activitiesAndCertificationsPK;
	}

	/**
	 * @param activitiesAndCertificationsPK
	 *            : Object the composite key activities and certifications.
	 */
	public void setActivitiesAndCertificationsPK(
			ActivitiesAndCertificationsPK activitiesAndCertificationsPK) {
		this.activitiesAndCertificationsPK = activitiesAndCertificationsPK;
	}

	/**
	 * @return idCertAndRoles: Identifier of certifications and roles.
	 */
	public int getIdCertAndRoles() {
		return idCertAndRoles;
	}

	/**
	 * @param idCertAndRoles
	 *            : Identifier of certifications and roles.
	 */
	public void setIdCertAndRoles(int idCertAndRoles) {
		this.idCertAndRoles = idCertAndRoles;
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
	 * @return pagination: Paginated list of human resources and certifications
	 *         may be in the view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paginated list of human resources and certifications may be
	 *            in the view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return activitiesPagination: Paginated list of activities names may be
	 *         in the view.
	 */
	public Paginador getActivitiesPagination() {
		return activitiesPagination;
	}

	/**
	 * @param activitiesPagination
	 *            : Paginated list of activities names may be in the view.
	 */
	public void setActivitiesPagination(Paginador activitiesPagination) {
		this.activitiesPagination = activitiesPagination;
	}

	/**
	 * Method to initialize the search parameters and load the template to
	 * manage activities and certifications.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 * 
	 * @return searchActivities: Returns to the template management and
	 *         certification activities.
	 */
	public String searchInitialization() {
		try {
			this.idCertAndRoles = 0;
			nameSearch = "";
			certificationsAndRoles = new CertificationsAndRoles();
			activityNames = new ActivityNames();
			loadComboCertAndRoles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchActivities();
	}

	/**
	 * Method to edit or create a certification activity.
	 * 
	 * @param activitiesAndCertifications
	 *            :Activity and certification are adding or editing.
	 * @return regActivAndCert: Template redirects to record activities and
	 *         certifications.
	 */
	public String addEditActiAndCert(
			ActivitiesAndCertifications activitiesAndCertifications) {
		try {
			loadComboCertAndRoles();
			loadComboActivityNames();
			if (activitiesAndCertifications != null) {
				this.activitiesAndCertifications = activitiesAndCertifications;

			} else {
				this.certificationsAndRoles = new CertificationsAndRoles();
				this.activityNames = new ActivityNames();
				this.activitiesAndCertifications = new ActivitiesAndCertifications();
				this.activitiesAndCertificationsPK = new ActivitiesAndCertificationsPK();
				this.activitiesAndCertificationsPK
						.setActivities(new Activities());
				this.activitiesAndCertificationsPK
						.setCertificationsAndRoles(new CertificationsAndRoles());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return "regActivAndCert";
	}

	/**
	 * Method to load certifications and roles in a list.
	 * 
	 * @throws Exception
	 */
	private void loadComboCertAndRoles() throws Exception {
		itemsCertificationsAndRoles = new ArrayList<SelectItem>();
		List<CertificationsAndRoles> listCertificationsAndRoles = certificationsAndRolesDao
				.consultCertificationsAndRoles();
		if (listCertificationsAndRoles != null) {
			for (CertificationsAndRoles certificationsAndRoles : listCertificationsAndRoles) {
				itemsCertificationsAndRoles.add(new SelectItem(
						certificationsAndRoles.getIdCertificactionsAndRoles(),
						certificationsAndRoles.getName()));
			}
		}

	}

	/**
	 * Method that allows load the certificates and roles in a list.
	 * 
	 * @throws Exception
	 */
	private void loadComboActivityNames() throws Exception {
		itemsActivities = new ArrayList<SelectItem>();
		List<Activities> listActivities = activitiesDao.queryAllActivities();
		if (listActivities != null) {
			for (Activities activities : listActivities) {
				itemsActivities.add(new SelectItem(activities.getActivityName()
						.getIdActivityName(), activities.getActivityName()
						.getActivityName()));
			}
		}
	}

	/**
	 * This method allows initialize and search the values of the activities
	 * names.
	 * 
	 * @author Luna.Granados
	 */
	public void initializeSearch() {
		try {
			this.activitiesPagination.setOpcion('f');
			this.activityNames = new ActivityNames();
			this.activities = new Activities();
			nameSearch = "";

			this.listActivitiesNames = activityNamesDao.queryActivityNames();
			this.itemsActivities = new ArrayList<SelectItem>();
			if (this.listActivitiesNames != null) {
				for (ActivityNames names : this.listActivitiesNames) {
					this.itemsActivities.add(new SelectItem(names
							.getIdActivityName(), names.getActivityName()));
				}
			}
			consultActivities();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Consult the list of activities names.
	 * 
	 * @author Luna.Granados
	 * 
	 * @return "gesActivAndCert": Redirects to the template to manage activities
	 *         and certifications.
	 */
	public String searchActivities() {
		consultActivities();
		return "gesActivAndCert";
	}

	/**
	 * Consult the list of activities and associated certifications
	 * certifications.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 * @modify 12/10/2016 Luna.Granados
	 * @modify 17/05/2017 Claudia.Rey
	 */
	public void consultActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listActivities = new ArrayList<ActivitiesAndCertifications>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch,
					fromModal);
			Long quantity = activitiesAndCertificationsDao
					.queryActivitiesByIdCert(query, parameters);

			if (quantity != null && quantity > 0) {
				if (fromModal) {
					activitiesPagination.paginarRangoDefinido(quantity, 5);
					listActivities = activitiesAndCertificationsDao
							.queryActivityNamesByIdCert(
									activitiesPagination.getInicio(),
									activitiesPagination.getRango(), query,
									parameters);
				} else {
					pagination.paginar(quantity);
					listActivities = activitiesAndCertificationsDao
							.queryActivityNamesByIdCert(pagination.getInicio(),
									pagination.getRango(), query, parameters);
				}
			}

			if ((listActivities == null || listActivities.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listActivities == null || listActivities.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString()) && !fromModal) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activities_certifications_label_s"),
								unionMessagesSearch);
			} else if (!"".equals(unionMessagesSearch.toString()) && fromModal) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								unionMessagesSearch);
			}

			if (fromModal) {
				validations.setMensajeBusquedaPopUp(messageSearch);
			} else {
				validations.setMensajeBusqueda(messageSearch);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit activities and certifications.
	 * 
	 * @return searchInitialization: Method to initialize the search parameters
	 *         and load the template to manage activities and certifications.
	 */
	public String saveActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		int idActName = this.activityNames.getIdActivityName();
		Object nameCert = new Object();
		try {
			nameCert = ValidacionesAction.getLabel(itemsCertificationsAndRoles,
					this.certificationsAndRoles.getIdCertificactionsAndRoles());
			activities = activitiesDao.activityByActNameId(idActName);
			String messageLog = "message_registro_guardar";
			activitiesAndCertificationsPK.setActivities(activities);
			activitiesAndCertificationsPK
					.setCertificationsAndRoles(certificationsAndRoles);
			activitiesAndCertifications
					.setActivitiesAndCertificationsPK(activitiesAndCertificationsPK);
			activitiesAndCertificationsDao
					.saveActivitiesAndCertifications(activitiesAndCertifications);

			String format = MessageFormat.format(bundle.getString(messageLog),
					nameCert);
			ControladorContexto.mensajeInformacion(null, format);
			activityNames = new ActivityNames();
			certificationsAndRoles = new CertificationsAndRoles();
		} catch (EJBException e2) {
			String errorLog = e2.getCause().getCause().getCause().toString();
			String error = "ConstraintViolationException";
			if (errorLog.contains(error)) {
				String format = MessageFormat.format(bundle
						.getString("message_relationship_exist"), nameCert,
						activities.getActivityName().getActivityName());
				ControladorContexto.mensajeError(e2, null, format);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchInitialization();
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @modify 17/03/2016 Wilhelm.Boada
	 * @modify 12/10/2016 Luna.Granados
	 * @modify 17/05/2017 Claudia.Rey
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 * @param fromModal
	 *            :Flag indicating that the method is called from the pop-up
	 *            search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch, boolean fromModal) {
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		SelectItem item = new SelectItem();

		if (!fromModal) {
			if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				if (this.idCertAndRoles != 0) {
					consult.append("WHERE cr.idCertificactionsAndRoles = :keyword3 )");
					item = new SelectItem(this.idCertAndRoles, "keyword3");
					parameters.add(item);
				}
				consult.append("WHERE UPPER(an.activityName) LIKE UPPER(:keyword) ");
				item = new SelectItem("%" + this.nameSearch + "%", "keyword");
				parameters.add(item);
				unionMessagesSearch.append(bundle.getString("label_name")
						+ ": " + '"' + this.nameSearch + '"');
			} else if (this.idCertAndRoles != 0) {
				consult.append("WHERE cr.idCertificactionsAndRoles = :keyword ");
				item = new SelectItem(this.idCertAndRoles, "keyword");
				parameters.add(item);
			}
		} else {
			if (this.activityNames.getIdActivityName() != 0) {
				consult.append("WHERE an.idActivityName = :keyword ");
				item = new SelectItem(this.activityNames.getIdActivityName(),
						"keyword");
				parameters.add(item);
				String activityName = (String) ValidacionesAction
						.getLabel(itemsActivities,
								this.activityNames.getIdActivityName());
				unionMessagesSearch.append(bundle.getString("label_name")
						+ ": " + '"' + activityName + '"' + " ");
			}

			String state = (this.activityNames.getIdActivityName() != 0) ? "AND "
					: "WHERE ";

			if (this.activities.getInitialDtBudget() != null
					|| this.activities.getFinalDtBudget() != null) {

				if (this.activities.getInitialDtBudget() != null
						&& this.activities.getFinalDtBudget() != null) {
					consult.append(state
							+ "a.initialDtBudget BETWEEN :initialDtBudget AND :finalDtBudget ");
				}

				if (this.activities.getInitialDtBudget() != null
						&& this.activities.getFinalDtBudget() == null) {
					consult.append(state
							+ "a.initialDtBudget >= :initialDtBudget ");
				}
				if (this.activities.getInitialDtBudget() == null
						&& this.activities.getFinalDtBudget() != null) {
					consult.append(state
							+ "a.initialDtBudget <= :finalDtBudget ");
				}

				if (this.activities.getInitialDtBudget() != null) {
					item = new SelectItem(this.activities.getInitialDtBudget(),
							"initialDtBudget");
					parameters.add(item);
					String dateFrom = bundle.getString("label_start_date")
							+ ": "
							+ '"'
							+ formato.format(this.activities
									.getInitialDtBudget()) + '"' + " ";
					unionMessagesSearch.append(dateFrom);
				}

				if (this.activities.getFinalDtBudget() != null) {
					item = new SelectItem(this.activities.getFinalDtBudget(),
							"finalDtBudget");
					parameters.add(item);
					String dateTo = bundle.getString("label_end_date")
							+ ": "
							+ '"'
							+ formato
									.format(this.activities.getFinalDtBudget())
							+ '"';
					unionMessagesSearch.append(dateTo);
				}
			}
		}
	}

	/**
	 * Method to delete an activity, certification of the database.
	 * 
	 * @return searchInitialization: Method to initialize the search parameters
	 *         and load the template to manage activities and certifications.
	 */
	public String removeActivitiesAndCert() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			int idActivities = this.activities.getIdActivity();
			ActivitiesAndCertifications activitiesAndCertifications = activitiesAndCertificationsDao
					.activAndCertifXIdActiviAndIdCertAndRol(idActivities,
							this.idCertAndRoles);
			activitiesAndCertificationsDao
					.removeActivitiesAndCertifications(activitiesAndCertifications);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					this.activities.getActivityName().getActivityName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					this.activities.getActivityName().getActivityName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchInitialization();
	}

	/**
	 * Validates interface required fields.
	 * 
	 * @author Luna.Granados
	 */
	public void validateRequired() {
		try {
			if (this.certificationsAndRoles.getIdCertificactionsAndRoles() == 0) {
				ControladorContexto
						.mensajeRequeridos("formActCert:certificacion");
			}
			if (this.activityNames.getActivityName() == null) {
				ControladorContexto
						.mensajeRequeridos("formActCert:txtActivity");
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to set the selected activity in the popup.
	 * 
	 * @author Luna.Granados
	 * @modify 17/05/2017 Claudia.Rey
	 * 
	 * @param activity
	 *            : Activity that is load from popup.
	 */
	public void loadActivity(
			ActivitiesAndCertifications activityAndCertifications) {
		setActivityNames(activityAndCertifications
				.getActivitiesAndCertificationsPK().getActivities()
				.getActivityName());
	}

	/**
	 * Method to clear the selected activity name.
	 * 
	 * @author Luna.Granados
	 */
	public void clearActivity() {
		setActivityNames(new ActivityNames());
	}
}