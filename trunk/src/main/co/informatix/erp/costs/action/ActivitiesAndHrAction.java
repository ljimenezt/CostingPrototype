package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import co.informatix.erp.costs.dao.ActivitiesAndHrDao;
import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivitiesAndHr;
import co.informatix.erp.costs.entities.ActivitiesAndHrPK;
import co.informatix.erp.humanResources.action.HrAction;
import co.informatix.erp.humanResources.action.TeamAction;
import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.HrTypesDao;
import co.informatix.erp.humanResources.dao.OvertimePaymentRateDao;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.HrTypes;
import co.informatix.erp.humanResources.entities.OvertimePaymentRate;
import co.informatix.erp.humanResources.entities.Team;
import co.informatix.erp.informacionBase.dao.SystemProfileDao;
import co.informatix.erp.lifeCycle.action.RecordActivitiesActualsAction;
import co.informatix.erp.lifeCycle.dao.CycleDao;
import co.informatix.erp.lifeCycle.entities.Cycle;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utz.dao.CertificationsAndRolesDao;
import co.informatix.erp.utz.dao.HrCertificationsAndRolesDao;
import co.informatix.erp.utz.entities.CertificationsAndRoles;

/**
 * This class allows the logic of the relationship between the activities and
 * human resources in the database. The logic is to record the relationship and
 * human resources activities.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAndHrAction implements Serializable {

	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private HrDao hrDao;
	@EJB
	private HrTypesDao hrTypeDao;
	@EJB
	private ActivitiesAndHrDao activitiesAndHrDao;
	@EJB
	private OvertimePaymentRateDao overtimePaymentRateDao;
	@EJB
	private SystemProfileDao systemProfileDao;
	@EJB
	private CertificationsAndRolesDao certificationsAndRolesDao;
	@EJB
	private HrCertificationsAndRolesDao hrCertificationsAndRolesDao;
	@EJB
	private CycleDao cycleDao;
	@Resource
	private UserTransaction userTransaction;

	private int idWorker;
	private int idOvertimeRate;
	private boolean validateWorker;
	private boolean workHoursValid;
	private boolean statusMessage = false;
	private boolean certifiedActivity = false;
	private boolean reportingActuals = false;
	private boolean fromModal;

	private List<SelectItem> listTypeWorker;
	private List<SelectItem> listOvertimePaymentRate;
	private List<Hr> workers;
	private List<Hr> selectedWorkers;
	private List<ActivitiesAndHr> listActivitiesAndHrTemp;
	private List<ActivitiesAndHr> listActivitiesAndHr;
	private List<ActivitiesAndHr> listActivitiesAndHrNotAvailable;

	private ActivitiesAndHrPK activitiesAndHrPK;
	private ActivitiesAndHr activitiesAndHr;
	private Activities activities;
	private Activities selectedActivity;
	private Hr worker;
	private Paginador pagination = new Paginador();
	private Paginador paginationWorker = new Paginador();
	private Paginador paginationActivitiesAndHr = new Paginador();
	private String message;
	private String messageWorkersAvailability;
	private String nameSearch;

	/**
	 * @return idWorker: id for idWorker.
	 */
	public int getIdWorker() {
		return idWorker;
	}

	/**
	 * @param idWorker
	 *            : id for idWorker.
	 */
	public void setIdWorker(int idWorker) {
		this.idWorker = idWorker;
	}

	/**
	 * @return idOvertimeRate: id for OvertimeRate
	 */
	public int getIdOvertimeRate() {
		return idOvertimeRate;
	}

	/**
	 * @param idOvertimeRate
	 *            : id for OvertimeRate
	 */
	public void setIdOvertimeRate(int idOvertimeRate) {
		this.idOvertimeRate = idOvertimeRate;
	}

	/**
	 * @return listTypeWorker: List of type worker.
	 */
	public List<SelectItem> getListTypeWorker() {
		return listTypeWorker;
	}

	/**
	 * @param listTypeWorker
	 *            : List of type worker.
	 */
	public void setListTypeWorker(List<SelectItem> listTypeWorker) {
		this.listTypeWorker = listTypeWorker;
	}

	/**
	 * @return listOvertimePaymentRate: list of over time payments rate
	 */
	public List<SelectItem> getListOvertimePaymentRate() {
		return listOvertimePaymentRate;
	}

	/**
	 * @param listOvertimePaymentRate
	 *            : list of over time payments rate
	 */
	public void setListOvertimePaymentRate(
			List<SelectItem> listOvertimePaymentRate) {
		this.listOvertimePaymentRate = listOvertimePaymentRate;
	}

	/**
	 * @return workers: List type worker.
	 */
	public List<Hr> getWorkers() {
		return workers;
	}

	/**
	 * @param workers
	 *            : List type worker.
	 */
	public void setWorkers(List<Hr> workers) {
		this.workers = workers;
	}

	/**
	 * @return selectedWorkers: list of selected workers
	 */
	public List<Hr> getSelectedWorkers() {
		return selectedWorkers;
	}

	/**
	 * @param selectedWorkers
	 *            : list of selected workers
	 */
	public void setSelectedWorkers(List<Hr> selectedWorkers) {
		this.selectedWorkers = selectedWorkers;
	}

	/**
	 * @return listActivitiesAndHrTemp: list of the relationship between
	 *         activity and human resources
	 */
	public List<ActivitiesAndHr> getListActivitiesAndHrTemp() {
		return listActivitiesAndHrTemp;
	}

	/**
	 * @param listActivitiesAndHrTemp
	 *            : list of the relationship between activity and human
	 *            resources
	 */
	public void setListActivitiesAndHrTemp(
			List<ActivitiesAndHr> listActivitiesAndHrTemp) {
		this.listActivitiesAndHrTemp = listActivitiesAndHrTemp;
	}

	/**
	 * @return listActivitiesAndHr: list of the relationship between activity
	 *         and human resources
	 */
	public List<ActivitiesAndHr> getListActivitiesAndHr() {
		return listActivitiesAndHr;
	}

	/**
	 * @param listActivitiesAndHr
	 *            : list of the relationship between activity and human
	 *            resources
	 */
	public void setListActivitiesAndHr(List<ActivitiesAndHr> listActivitiesAndHr) {
		this.listActivitiesAndHr = listActivitiesAndHr;
	}

	/**
	 * @return listActivitiesAndHrNotAvailable: List of workers and activities
	 *         are occupied.
	 */
	public List<ActivitiesAndHr> getListActivitiesAndHrNotAvailable() {
		return listActivitiesAndHrNotAvailable;
	}

	/**
	 * @param listActivitiesAndHrNotAvailable
	 *            : List of workers and activities are occupied.
	 */
	public void setListActivitiesAndHrNotAvailable(
			List<ActivitiesAndHr> listActivitiesAndHrNotAvailable) {
		this.listActivitiesAndHrNotAvailable = listActivitiesAndHrNotAvailable;
	}

	/**
	 * @return activitiesAndHrPK: primary key activitiesAndHr
	 */
	public ActivitiesAndHrPK getActivitiesAndHrPK() {
		return activitiesAndHrPK;
	}

	/**
	 * @param activitiesAndHrPK
	 *            : primary key activitiesAndHr
	 */
	public void setActivitiesAndHrPK(ActivitiesAndHrPK activitiesAndHrPK) {
		this.activitiesAndHrPK = activitiesAndHrPK;
	}

	/**
	 * @return worker: Object type of worker.
	 */
	public Hr getWorker() {
		return worker;
	}

	/**
	 * @param worker
	 *            : Object type of worker.
	 */
	public void setWorker(Hr worker) {
		this.worker = worker;
	}

	/**
	 * @return activities: activity performed.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : activity performed.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return selectedActivity: activities object.
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            : activities object.
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return activitiesAndHr: Object of the relationship between the
	 *         activities and human resources.
	 */
	public ActivitiesAndHr getActivitiesAndHr() {
		return activitiesAndHr;
	}

	/**
	 * @param activitiesAndHr
	 *            : Object of the relationship between the activities and human
	 *            resources.
	 */
	public void setActivitiesAndHr(ActivitiesAndHr activitiesAndHr) {
		this.activitiesAndHr = activitiesAndHr;
	}

	/**
	 * @return pagination: Management paged list of activities.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of activities.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return message: validation message.
	 */
	public String getMessage() {
		return message;
	}

	/**
	 * @param message
	 *            : validation message.
	 */
	public void setMessage(String message) {
		this.message = message;
	}

	/**
	 * @return messageWorkersAvailability: state responsible for displaying
	 *         message availability of workers
	 */
	public String getMessageWorkersAvailability() {
		return messageWorkersAvailability;
	}

	/**
	 * @param messageWorkersAvailability
	 *            : state responsible for displaying message availability of
	 *            workers
	 */
	public void setMessageWorkersAvailability(String messageWorkersAvailability) {
		this.messageWorkersAvailability = messageWorkersAvailability;
	}

	/**
	 * @return nameSearch: Name of workers to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Name of workers to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return paginationWorker: Management paged list of workerd.
	 */
	public Paginador getPaginationWorker() {
		return paginationWorker;
	}

	/**
	 * @param paginationWorker
	 *            : Management paged list of workerd.
	 */
	public void setPaginationWorker(Paginador paginationWorker) {
		this.paginationWorker = paginationWorker;
	}

	/**
	 * @return paginationActivitiesAndHr: Pager relationship and human resources
	 *         activities.
	 */
	public Paginador getPaginationActivitiesAndHr() {
		return paginationActivitiesAndHr;
	}

	/**
	 * @param paginationActivitiesAndHr
	 *            : Pager relationship and human resources activities.
	 */
	public void setPaginationActivitiesAndHr(Paginador paginationActivitiesAndHr) {
		this.paginationActivitiesAndHr = paginationActivitiesAndHr;
	}

	/**
	 * @return validateWorker: handles the validation status of workers.
	 */
	public boolean isValidateWorker() {
		return validateWorker;
	}

	/**
	 * @param validateWorker
	 *            : handles the validation status of workers.
	 */
	public void setValidateWorker(boolean validateWorker) {
		this.validateWorker = validateWorker;
	}

	/**
	 * @return statusMessage: status to check if the message is displayed
	 *         certificated staff
	 */
	public boolean isStatusMessage() {
		return statusMessage;
	}

	/**
	 * @param statusMessage
	 *            : status to check if the message is displayed certificated
	 *            staff
	 */
	public void setStatusMessage(boolean statusMessage) {
		this.statusMessage = statusMessage;
	}

	/**
	 * @return certifiedActivity: status to check if the message is displayed
	 *         certified activity
	 */
	public boolean isCertifiedActivity() {
		return certifiedActivity;
	}

	/**
	 * @param certifiedActivity
	 *            : status to check if the message is displayed certified
	 *            activity
	 */
	public void setCertifiedActivity(boolean certifiedActivity) {
		this.certifiedActivity = certifiedActivity;
	}

	/**
	 * @return reportingActuals: status to check if this interface is the
	 *         actuals reporting
	 */
	public boolean isReportingActuals() {
		return reportingActuals;
	}

	/**
	 * @return fromModal: this field is true if the query is made from
	 *         recordActivitiesActualsAction and is false in other case.
	 */
	public boolean isFromModal() {
		return fromModal;
	}

	/**
	 * @param fromModal
	 *            this field is true if the query is made from
	 *            recordActivitiesActualsAction and is false in other case.
	 */
	public void setFromModal(boolean fromModal) {
		this.fromModal = fromModal;
	}

	/**
	 * @param reportingActuals
	 *            : status to check if this interface is the actuals reporting
	 */
	public void setReportingActuals(boolean reportingActuals) {
		this.reportingActuals = reportingActuals;
	}

	/**
	 * @return workHoursValid: determines whether the human resource hours
	 *         allocated to meet the validation or not.
	 */
	public boolean isWorkHoursValid() {
		return workHoursValid;
	}

	/**
	 * @param workHoursValid
	 *            : determines whether the human resource hours allocated to
	 *            meet the validation or not..
	 */
	public void setWorkHoursValid(boolean workHoursValid) {
		this.workHoursValid = workHoursValid;
	}

	/**
	 * It is responsible for initializing the parameters for the search of
	 * workers.
	 */
	public void initializeWorkers() {
		paginationWorker = new Paginador();
		nameSearch = "";
		consultWorkers();
	}

	/**
	 * Consult the list of workers required.
	 */
	public void consultWorkers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHumanResources = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		HrAction hrAction = ControladorContexto.getContextBean(HrAction.class);
		this.statusMessage = false;
		this.certifiedActivity = false;
		this.workers = new ArrayList<Hr>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder uniteMessageSearch = new StringBuilder();
		String menssageSearch = "";
		try {
			Long certifiedActivities = activitiesDao
					.queryCertifiedActivities(selectedActivity.getIdActivity());
			Long certifiedHr = hrDao.queryHrCertifications(
					selectedActivity.getIdActivity(), this.idWorker);
			Long searchHrCertifiedAndMaternity = hrDao
					.hrCertifiedAndMaternityAmount(
							selectedActivity.getIdActivity(), this.idWorker);
			advancedSearchWorker(consult, parameters, certifiedActivities,
					certifiedHr, searchHrCertifiedAndMaternity,
					uniteMessageSearch);
			Long quantity = hrDao.hrAmount(consult, parameters);
			if (quantity != null) {
				if (quantity > 5) {
					paginationWorker.paginarRangoDefinido(quantity, 5);
				} else {
					paginationWorker.paginar(quantity);
				}
				this.workers = hrDao.queryHr(paginationWorker.getInicio(),
						paginationWorker.getRango(), consult, parameters);
			}
			if ((workers == null || workers.size() <= 0)
					&& !"".equals(uniteMessageSearch.toString())) {
				menssageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								uniteMessageSearch);
			} else if (workers == null || workers.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(uniteMessageSearch.toString())) {
				menssageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleHumanResources
										.getString("human_resource_label"),
								uniteMessageSearch);
			}
			if (workers != null) {
				hrAction.maintainHrSelected(workers, selectedWorkers);
			}
			validations.setMensajeBusqueda(menssageSearch);
			validations.setMensajeBusquedaPopUp(menssageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search for workers assembling
	 * also allows messages to be displayed depending on the search criteria
	 * selected by the user.
	 * 
	 * @modify Cristhian.Pico
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param certifiedActivities
	 *            : list of the certifications required to perform the selected
	 *            activity
	 * @param certifiedHr
	 *            : list of the human resources who have the certifications
	 *            required to perform the selected activity
	 * @param searchHrCertifiedAndMaternity
	 *            : list of the human resources who have the certifications
	 *            required to perform the selected activity but are on maternity
	 *            or breast feeding period
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearchWorker(StringBuilder consult,
			List<SelectItem> parameters, Long certifiedActivities,
			Long certifiedHr, Long searchHrCertifiedAndMaternity,
			StringBuilder uniteMessageSearch) {
		String nameSearchTrim = nameSearch;
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		this.messageWorkersAvailability = null;
		Date minimumDate = ControladorFechas.restarAnyos(
				ControladorFechas.fechaActual(),
				Constantes.EDAD_MINIMA_ACTIVIDAD_PELIGROSA);
		boolean selection = false;
		consult.append("JOIN h.hrTypes ht ");
		if (this.idWorker != 0) {
			consult.append("WHERE ht.idHrType = :idWorker ");
			SelectItem itemHrType = new SelectItem(this.idWorker, "idWorker");
			parameters.add(itemHrType);
			selection = true;
		}
		consult.append(selection ? "AND " : "WHERE ");
		consult.append("h NOT IN ");
		consult.append("(SELECT h FROM ActivitiesAndHr ah ");
		consult.append("JOIN ah.activitiesAndHrPK ahp ");
		consult.append("JOIN ahp.hr h ");
		consult.append("WHERE ah.initialDateTimeBudget BETWEEN :fechaInicial AND :fechaFinal ");
		consult.append("OR ah.initialDateTimeActual BETWEEN :fechaInicial AND :fechaFinal ");
		consult.append("OR ah.finalDateTimeBudget BETWEEN :fechaInicial AND :fechaFinal ");
		consult.append("OR ah.finalDateTimeActual BETWEEN :fechaInicial AND :fechaFinal) ");
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {

			consult.append("AND UPPER(h.name || h.familyName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%"
					+ nameSearchTrim.replace(" ", "") + "%", "keyword");
			parameters.add(item);
			uniteMessageSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
		if (certifiedActivities != null && certifiedActivities > 0) {
			consult.append("AND h IN ");
			consult.append("(SELECT h FROM HrCertificationsAndRoles hrc ");
			consult.append("JOIN hrc.hrCertificationsAndRolesPK.certificationsAndRoles ca ");
			consult.append("JOIN hrc.hrCertificationsAndRolesPK.hr h ");
			consult.append("WHERE ca IN ");
			consult.append("(SELECT acr FROM ActivitiesAndCertifications ac ");
			consult.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles acr ");
			consult.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
			consult.append("WHERE aca.idActivity = :actividadSeleccion )) ");
			SelectItem itemActividadSeleccion = new SelectItem(
					this.selectedActivity.getIdActivity(), "actividadSeleccion");
			parameters.add(itemActividadSeleccion);
			this.messageWorkersAvailability = Constantes.STATE_CERTIFIED;
			this.certifiedActivity = true;
			if (certifiedHr > 0) {
				this.statusMessage = true;
				this.messageWorkersAvailability = Constantes.STATE_HR_CERTIFIED;
			}
		}
		if (selectedActivity.getDangerous() != null
				&& selectedActivity.getDangerous()) {
			consult.append("AND h.birthDate < :fechaMinima ");
			consult.append("AND h.maternityBreastFeeding = false  ");
			SelectItem itemMinimumDate = new SelectItem(minimumDate,
					"fechaMinima");
			parameters.add(itemMinimumDate);
			if (searchHrCertifiedAndMaternity > 0) {
				this.messageWorkersAvailability = Constantes.STATE_CERTIFIED_MATERNITY;
			}
		}
		SelectItem initialItem = new SelectItem(
				this.selectedActivity.getInitialDtBudget(), "fechaInicial");
		SelectItem finalItem = new SelectItem(
				this.selectedActivity.getFinalDtBudget(), "fechaFinal");
		parameters.add(initialItem);
		parameters.add(finalItem);
	}

	/**
	 * load the OvertimePaymentsRate list.
	 * 
	 * @throws Exception
	 */
	private void loadOvertimePaymentRate() throws Exception {
		this.listOvertimePaymentRate = new ArrayList<SelectItem>();
		List<OvertimePaymentRate> overtimePaymentsRate = overtimePaymentRateDao
				.listOvertimePaymentRate();
		if (overtimePaymentsRate != null) {
			for (OvertimePaymentRate overtimePaymentRate : overtimePaymentsRate) {
				listOvertimePaymentRate.add(new SelectItem(overtimePaymentRate
						.getOvertimepaymentid(), overtimePaymentRate
						.getOvertimeRateType()));
			}
		}
	}

	/**
	 * It allows charging workers available on a list of SelectItem type.
	 * 
	 */
	public void showTypeWorker() {
		try {
			idWorker = 0;
			nameSearch = "";
			listActivitiesAndHr = new ArrayList<ActivitiesAndHr>();
			selectedWorkers = new ArrayList<Hr>();
			consultWorkers();
			selectedWorkers = new ArrayList<Hr>();
			listTypeWorker = new ArrayList<SelectItem>();
			List<HrTypes> workerType = hrTypeDao.queryHrTypes();
			if (workerType != null) {
				for (HrTypes type : workerType) {
					listTypeWorker.add(new SelectItem(type.getIdHrType(), type
							.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Adds a list of workers who are selected validating age and time of
	 * motherhood.
	 * 
	 * @modify 21/04/2016 Wilhelm.Boada
	 * 
	 * @param selectedWorker
	 *            : Human Resource object type.
	 */
	public void selectedWorker(Hr selectedWorker) {
		this.message = "";
		this.worker = new Hr();
		activitiesAndHrPK = new ActivitiesAndHrPK();
		activitiesAndHr = new ActivitiesAndHr();
		validateWorker = false;
		try {
			worker = selectedWorker;
			if (!selectedWorker.isSeleccionado()) {
				if (selectedActivity.getDurationBudget() != null) {
					activitiesAndHr.setDurationBudget(selectedActivity
							.getDurationBudget());
				} else {
					activitiesAndHr.setDurationBudget(ControladorFechas
							.restarFechas(
									selectedActivity.getInitialDtBudget(),
									selectedActivity.getFinalDtBudget()));
				}
			} else {
				ActivitiesAndHr actividadesHr = new ActivitiesAndHr();
				for (ActivitiesAndHr actividadHr : listActivitiesAndHr) {
					int actividadIdHr = actividadHr.getActivitiesAndHrPK()
							.getHr().getIdHr();
					int idActivity = actividadHr.getActivitiesAndHrPK()
							.getActivities().getIdActivity();
					if (actividadIdHr == worker.getIdHr()
							&& selectedActivity.getIdActivity() == idActivity) {
						actividadesHr = actividadHr;
					}
				}
				listActivitiesAndHr.remove(actividadesHr);
				worker.setSeleccionado(false);
				validateWorker = true;
				selectedWorkers.remove(worker);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for adding workers are selected to list workers and
	 * selected the list of activities and resources humans.
	 */
	public void addEditWorker() {
		try {
			OvertimePaymentRate overTimePaymentRate = overtimePaymentRateDao
					.overtimePaymentRateXDefaultRate(true);
			worker.setSeleccionado(true);
			Double totalCostBudget = calculateTotalCostBudget(worker,
					activitiesAndHr, overTimePaymentRate);
			activitiesAndHrPK.setHr(worker);
			activitiesAndHrPK.setActivities(selectedActivity);
			activitiesAndHr.setActivitiesAndHrPK(activitiesAndHrPK);
			activitiesAndHr.setInitialDateTimeBudget(selectedActivity
					.getInitialDtBudget());
			activitiesAndHr.setFinalDateTimeBudget(selectedActivity
					.getFinalDtBudget());
			activitiesAndHr.setTotalCostBudget(totalCostBudget);
			activitiesAndHr.setOvertimePaymentRate(overTimePaymentRate);
			listActivitiesAndHr.add(activitiesAndHr);
			selectedWorkers.add(worker);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Calculate the total cost for the human resources.
	 * 
	 * @param worker
	 *            : Human resource
	 * @param ahr
	 *            : Object activitiesAndHr
	 * @param opr
	 *            : Object OvertimePaymentRate
	 * @return totalCostBudget: total cost budget value
	 */
	private Double calculateTotalCostBudget(Hr worker, ActivitiesAndHr ahr,
			OvertimePaymentRate opr) {
		Double costNormalHours = worker.getHourCost() * ahr.getNormalHours();
		Double costOvertimeHours = worker.getHourCostOvertime()
				* ahr.getOvertimeHours() * opr.getOvertimeRateRatio();
		Double totalCostBudget = Math
				.round((costNormalHours + costOvertimeHours) * 10.0) / 10.0;
		return totalCostBudget;
	}

	/**
	 * This method save activities and hr for the teams selected and validate if
	 * each human resource is available
	 * 
	 * @param activities
	 *            : Object Activities selected
	 */
	public void saveActivitiesHrTeam(Activities activities) {
		if (ControladorContexto.getFacesContext() != null) {
			TeamAction teamAction = ControladorContexto
					.getContextBean(TeamAction.class);
			HrAction hrAction = ControladorContexto
					.getContextBean(HrAction.class);
			try {
				StringBuilder message = new StringBuilder();
				selectedWorkers = new ArrayList<Hr>();
				listActivitiesAndHrNotAvailable = new ArrayList<ActivitiesAndHr>();
				listActivitiesAndHr = new ArrayList<ActivitiesAndHr>();
				List<Team> teamList = teamAction.getTeamSelected();
				List<CertificationsAndRoles> certificationsRoles = certificationsAndRolesDao
						.consultCertificationsAndRolesByActivity(activities
								.getIdActivity());
				for (Team team : teamList) {
					List<Hr> hrList = hrDao.hrByGroup(team.getIdTeam());
					if (hrList != null) {
						for (Hr hr : hrList) {
							boolean saveHr = true;
							List<ActivitiesAndHr> activitiesAndHr = activitiesAndHrDao
									.hrOccupied(hr.getIdHr(), activities);
							if (activitiesAndHr != null) {
								saveHr = false;
								boolean addFlag = compareHr(
										listActivitiesAndHrNotAvailable, hr);
								if (addFlag) {
									listActivitiesAndHrNotAvailable
											.add(activitiesAndHr.get(0));
								}
							}
							if (hr.getMaternityBreastFeeding() != null
									&& hr.getMaternityBreastFeeding()
									&& activities.getDangerous()) {
								saveHr = false;
								hrAction.compareHr(selectedWorkers, hr);
							}
							if (activities.getDangerous()) {
								Date minimumBirthDate = ControladorFechas
										.restarAnyos(
												ControladorFechas.fechaActual(),
												Constantes.EDAD_MINIMA_ACTIVIDAD_PELIGROSA);
								if (hr.getBirthDate().after(minimumBirthDate)) {
									saveHr = false;
									hrAction.compareHr(selectedWorkers, hr);
								}
							}
							if (certificationsRoles != null) {
								boolean certifications = hrCertificationsAndRolesDao
										.consultCertificationExists(
												hr.getIdHr(),
												certificationsRoles);
								if (!certifications) {
									saveHr = false;
									hr.setCertificate(false);
									hrAction.compareHr(selectedWorkers, hr);
								} else {
									hr.setCertificate(true);
								}
							}
							if (saveHr) {
								boolean addFlag = compareHr(
										listActivitiesAndHr, hr);
								if (addFlag) {
									Double duracion = activities
											.getDurationBudget();
									OvertimePaymentRate opr = overtimePaymentRateDao
											.overtimePaymentRateXDefaultRate(true);
									ActivitiesAndHrPK ahrPK = new ActivitiesAndHrPK();
									ActivitiesAndHr ahr = new ActivitiesAndHr();
									ahrPK.setActivities(activities);
									ahrPK.setHr(hr);
									ahr.setActivitiesAndHrPK(ahrPK);
									ahr.setInitialDateTimeBudget(activities
											.getInitialDtBudget());
									ahr.setFinalDateTimeBudget(activities
											.getFinalDtBudget());
									ahr.setDurationBudget(activities
											.getDurationBudget());
									ahr.setOvertimePaymentRate(opr);
									validateWorkLoad(duracion, hr.getIdHr(),
											false, ahr, activities);
									Double totalCostBudget = calculateTotalCostBudget(
											hr, ahr, opr);
									ahr.setTotalCostBudget(totalCostBudget);
									listActivitiesAndHr.add(ahr);
								}
							}
						}
					}
					message.append(team.getName() + ", ");
				}
				this.message = message.toString();
			} catch (Exception e) {
				this.listActivitiesAndHr.clear();
				ControladorContexto.mensajeError(e);
			}
		}
	}

	/**
	 * This method return 'true' if the ahr isnt into the relation between
	 * activities and human resources
	 * 
	 * @param ahrList
	 *            : List of relation between activities and human resources
	 * @param hr
	 *            : human resource
	 * @return boolean
	 */
	private boolean compareHr(List<ActivitiesAndHr> ahrList, Hr hr) {
		boolean addFlag = true;
		for (ActivitiesAndHr ahr : ahrList) {
			if (ahr.getActivitiesAndHrPK().getHr().getIdHr() == hr.getIdHr()) {
				addFlag = false;
			}
		}
		return addFlag;
	}

	/**
	 * Save the relationship between activities and human resources.
	 * 
	 * @param flagTeam
	 *            : It indicate if the message is for the teams.
	 */
	public void createListActivitiesAndHr(boolean flagTeam) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageRegister = "message_registro_modificar";
		double costHrBudget = 0;
		try {
			if (selectedActivity.getCostHrBudget() == null)
				selectedActivity.setCostHrBudget(new Double(0));
			userTransaction.begin();
			if (this.listActivitiesAndHr != null
					&& this.listActivitiesAndHr.size() > 0) {
				for (ActivitiesAndHr actividadAndHr : listActivitiesAndHr) {
					activitiesAndHrDao.saveActivitiesAndHr(actividadAndHr);
					costHrBudget = costHrBudget
							+ actividadAndHr.getTotalCostBudget();
				}
				costHrBudget = selectedActivity.getCostHrBudget()
						+ costHrBudget;
				editCycleHrBudget(costHrBudget);
				selectedActivity.setCostHrBudget(costHrBudget);
				activitiesDao.editActivities(this.selectedActivity);
				setListActivitiesAndHr(null);
				consultActivitiesAndHrByActivity();
				if (flagTeam) {
					bundle = ControladorContexto
							.getBundle("messageHumanResources");
					messageRegister = "team_message_added";
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(messageRegister), message,
									selectedActivity.getActivityName()
											.getActivityName()));
				}
			} else if (!flagTeam) {

				OvertimePaymentRate overtimePaymentRate = overtimePaymentRateDao
						.overtimePaymentRateXId(idOvertimeRate);
				Double costNormalHours = activitiesAndHr.getNormalHours()
						* activitiesAndHr.getActivitiesAndHrPK().getHr()
								.getHourCost();
				Double costOvertimeHours = activitiesAndHr.getOvertimeHours()
						* activitiesAndHr.getActivitiesAndHrPK().getHr()
								.getHourCostOvertime()
						* overtimePaymentRate.getOvertimeRateRatio();
				Double totalCostBudget = costNormalHours + costOvertimeHours;
				ActivitiesAndHr activitiesAndHrAnterior = activitiesAndHrDao
						.activitiesAndHrById(activitiesAndHr
								.getActivitiesAndHrPK());
				activitiesAndHr.setTotalCostBudget(Math
						.round(totalCostBudget * 10.0) / 10.0);
				activitiesAndHr.setOvertimePaymentRate(overtimePaymentRate);
				Double costHr = (selectedActivity.getCostHrBudget() - activitiesAndHrAnterior
						.getTotalCostBudget())
						+ activitiesAndHr.getTotalCostBudget();
				editCycleHrBudget(costHr);
				selectedActivity.setCostHrBudget(costHr);
				activitiesAndHrDao.editActivitiesAndHr(activitiesAndHr);
				activitiesDao.editActivities(selectedActivity);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(messageRegister),
								activitiesAndHr.getActivitiesAndHrPK().getHr()
										.getName()));
				consultActivitiesAndHrByActivity();
			}
			userTransaction.commit();
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.printErrorLog(e1);
			}
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Edit the budget cost for human resorces item into the cycle.
	 * 
	 * @param costHrBudget
	 *            : New budget cost for human resources.
	 * @throws Exception
	 */
	private void editCycleHrBudget(double costHrBudget) throws Exception {
		Cycle cycle = cycleDao.cycleById(selectedActivity.getCycle()
				.getIdCycle());
		double costBudgetCycle = costHrBudget;
		if (cycle.getCostHrBudget() != null && cycle.getCostHrBudget() > 0) {
			double lastCostBudgetHrActivity = selectedActivity
					.getCostHrBudget();
			costBudgetCycle = (cycle.getCostHrBudget() - lastCostBudgetHrActivity)
					+ costHrBudget;
		}
		cycle.setCostHrBudget(costBudgetCycle);
		cycleDao.editCycle(cycle);
	}

	/**
	 * Check the relationship between activities and human resources
	 * 
	 * @modify 12/01/2016 Wilhelm.Boada
	 * 
	 */
	public void consultActivitiesAndHrByActivity() {
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listActivitiesAndHrTemp = new ArrayList<ActivitiesAndHr>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		String messageSearch = "";
		String param2 = ControladorContexto.getParam("param2");
		try {
			RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
					.getContextBean(RecordActivitiesActualsAction.class);
			if (fromModal) {
				this.selectedActivity = recordActivitiesActualsAction
						.getSelectedActivity();
			}
			advancedSearchActivitiesAndHr(consult, parameters);
			Long quantity = activitiesAndHrDao.amountActivitiesAndHr(consult,
					parameters);
			advancedSearchActivitiesAndHr(consult, parameters);
			if (quantity != null) {
				if (quantity > 5) {
					paginationActivitiesAndHr.paginarRangoDefinido(quantity, 5);
				} else {
					paginationActivitiesAndHr.paginar(quantity);
				}
				this.listActivitiesAndHrTemp = activitiesAndHrDao
						.queryActivitiesAndHr(
								paginationActivitiesAndHr.getInicio(),
								paginationActivitiesAndHr.getRango(), consult,
								parameters);
				if (param2 != null && (fromModal || param2.equals("mostrar"))) {
					recordActivitiesActualsAction
							.setListActivitiesAndHr(listActivitiesAndHrTemp);
					recordActivitiesActualsAction
							.setCalculateCostsButtonActivated(false);
				}
			}
			loadOvertimePaymentRate();
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search for relationship
	 * between activities and human resources, also it allows messages to build
	 * show depending on the search criteria selected by the user.
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of the search parameters.
	 */
	private void advancedSearchActivitiesAndHr(StringBuilder consult,
			List<SelectItem> parameters) {
		boolean seleccion = false;
		if (consult.length() > 0) {
			consult.setLength(0);
			seleccion = true;
		}
		consult.append(seleccion ? "JOIN FETCH " : "JOIN ");
		consult.append("ah.activitiesAndHrPK.hr h ");
		consult.append(seleccion ? "JOIN FETCH " : "JOIN ");
		consult.append("ah.activitiesAndHrPK.activities ac ");
		consult.append(seleccion ? "JOIN FETCH " : "JOIN ");
		consult.append("ah.overtimePaymentRate op ");
		consult.append("WHERE ac.idActivity = :id ");
		SelectItem item = new SelectItem(this.selectedActivity.getIdActivity(),
				"id");
		parameters.add(item);
	}

	/**
	 * It is responsible for validating that the time duration entered does not
	 * exceed the total time of the activity and that the guidelines are met
	 * workload
	 * 
	 * @modify 28/08/2015 Cristhian.Pico
	 * @modify 27/06/2016 Gerardo.Herrera
	 * 
	 * @param context
	 *            : Context of sight.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 */
	public void validate(FacesContext context, UIComponent toValidate,
			Object value) {
		int idHr;
		String clientId = toValidate.getClientId(context);
		String modal = (String) toValidate.getAttributes().get("var");
		boolean fromModal = (modal != null && Constantes.SI.equals(modal)) ? true
				: false;
		this.workHoursValid = true;
		Double duration = (Double) value;
		Double durationActivity;
		if (!fromModal) {
			durationActivity = (Double) ControladorFechas.restarFechas(
					selectedActivity.getInitialDtBudget(),
					selectedActivity.getFinalDtBudget());
		} else {
			durationActivity = (Double) ControladorFechas.restarFechas(
					activitiesAndHr.getInitialDateTimeBudget(),
					activitiesAndHr.getFinalDateTimeBudget());
		}
		try {
			if (duration > 0) {
				if (duration.compareTo(durationActivity) > 0) {
					String message = "message_activity_duration";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							message, "mensaje");
					((UIInput) toValidate).setValid(false);
				} else {
					if (activitiesAndHr.getActivitiesAndHrPK() != null) {
						idHr = this.activitiesAndHr.getActivitiesAndHrPK()
								.getHr().getIdHr();
					} else {
						idHr = this.worker.getIdHr();
					}
					validateWorkLoad(duration, idHr, false,
							this.activitiesAndHr, this.selectedActivity);
					if (!this.workHoursValid) {
						String message = "message_overtime_week";
						ControladorContexto.mensajeErrorEspecifico(clientId,
								message, "mensaje");
						((UIInput) toValidate).setValid(false);
					}
				}
			} else {
				String message = "message_greater_zero";
				ControladorContexto.mensajeErrorEspecifico(clientId, message,
						"mensaje");
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method is responsible for validating the hours assigned to the
	 * worker This activity does not exceed the hours of weekly overtime
	 * 
	 * @author Cristhian.Pico
	 * @modify 29/06/2016 Gerardo.Herrera
	 * 
	 * @param durationHrActivity
	 *            : Value entered by the user as long as the human resource work
	 *            on that activity.
	 * @param humanReosurceId
	 *            : human resource id assigned to the activity.
	 * @param var
	 *            : variable that indicates whether the user is editing a record
	 *            or not.
	 * @param activitiesAndHr
	 *            : Object ActivitiesAndHr
	 * @param selectedActivity
	 *            : Activity selected
	 */
	public void validateWorkLoad(Double durationHrActivity,
			int humanReosurceId, boolean var, ActivitiesAndHr activitiesAndHr,
			Activities selectedActivity) {
		Date activityDate = selectedActivity.getInitialDtBudget();
		Date mindDateTime = ControladorFechas.diaInicialSemana(activityDate);
		Date maxDateTime = ControladorFechas.diaFinalSemana(activityDate);
		try {
			Double overtimeWeek = activitiesAndHrDao.calculateOverTimeHours(
					humanReosurceId, mindDateTime, maxDateTime,
					selectedActivity.getIdActivity());
			Double workedHoursDay = activitiesAndHrDao.calculateNormalHours(
					humanReosurceId, activityDate,
					selectedActivity.getIdActivity());
			if (durationHrActivity <= (8 - workedHoursDay)) {
				activitiesAndHr.setNormalHours(durationHrActivity);
				activitiesAndHr.setOvertimeHours(0.0);
			} else {
				Double activityNormalHours = (8 - workedHoursDay);
				activitiesAndHr.setNormalHours(activityNormalHours);
				if ((overtimeWeek + durationHrActivity - activityNormalHours) <= 12) {
					activitiesAndHr
							.setOvertimeHours(Math
									.round((durationHrActivity - activityNormalHours) * 10.0) / 10.0);
				} else {
					Double overtimeActivity = 12 - overtimeWeek;
					activitiesAndHr.setOvertimeHours(Math
							.round(overtimeActivity * 10.0) / 10.0);
					if (!var) {
						activitiesAndHr.setDurationBudget(activityNormalHours
								+ overtimeActivity);
					} else {
						activitiesAndHr.setDurationActual(activityNormalHours
								+ overtimeActivity);
					}
					this.setWorkHoursValid(false);
				}
			}
			activitiesAndHr.setTotalHours(activitiesAndHr.getNormalHours()
					+ activitiesAndHr.getOvertimeHours());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Deletes a relationship of activity and human resource data base.
	 * 
	 */
	public void removeActivitiesAndHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "message_registro_eliminar";
		try {
			activitiesAndHrDao.deleteActivitiesAndHr(activitiesAndHr);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(message), activitiesAndHr
							.getActivitiesAndHrPK().getHr().getName()));
			double costHrBudget=this.selectedActivity
					.getCostHrBudget() - activitiesAndHr.getTotalCostBudget();
			editCycleHrBudget(costHrBudget);
			this.selectedActivity.setCostHrBudget(costHrBudget);
			activitiesDao.editActivities(this.selectedActivity);
			consultActivitiesAndHrByActivity();
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					activitiesAndHr.getActivitiesAndHrPK().getHr().getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It will calculate the length considering the difference two dates
	 * 
	 * @modify 27/06/2016 Andres.Gomez
	 * @modify 28/06/2016 Gerardo.Herrera
	 */
	public void calculateDuration() {
		try {
			RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
					.getContextBean(RecordActivitiesActualsAction.class);
			double durationBudget = recordActivitiesActualsAction
					.subtractDuration(activitiesAndHr, null, false);
			int idHr = activitiesAndHr.getActivitiesAndHrPK().getHr().getIdHr();
			activitiesAndHr.setDurationBudget(durationBudget);
			validateWorkLoad(durationBudget, idHr, false, this.activitiesAndHr,
					this.selectedActivity);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}
