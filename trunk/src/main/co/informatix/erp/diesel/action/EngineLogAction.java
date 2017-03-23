package co.informatix.erp.diesel.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import co.informatix.erp.costs.dao.ActivitiesAndMachineDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.costs.entities.ActivityMachinePK;
import co.informatix.erp.diesel.dao.EngineLogDao;
import co.informatix.erp.diesel.dao.FuelUsageLogDao;
import co.informatix.erp.diesel.dao.IrrigationDetailsDao;
import co.informatix.erp.diesel.dao.ZoneDao;
import co.informatix.erp.diesel.entities.EngineLog;
import co.informatix.erp.diesel.entities.FuelUsageLog;
import co.informatix.erp.diesel.entities.IrrigationDetails;
import co.informatix.erp.diesel.entities.Zone;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.ControllerAccounting;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.entities.TransactionType;

/**
 * This class is used to create, update, and query of an engine log object in
 * the information system.
 * 
 * @author Patricia.Patinio
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class EngineLogAction implements Serializable {

	private String nameActivitySearch;
	private Double finalLevel;

	private List<ActivityMachine> activitiesMachineList;
	private List<SelectItem> itemsZone;
	private List<FuelUsageLog> engineLogList;
	private List<IrrigationDetails> irrigationDetailsList;

	private EngineLog engineLog;
	private FuelUsageLog fuelUsageLog;
	private IrrigationDetails irrigationDetails;
	private Zone zone;
	private Machines machineIrrigation;
	private Paginador pagination = new Paginador();
	private Paginador paginationForm = new Paginador();
	private Date startDateSearch;
	private Date endDateSearch;

	@EJB
	private EngineLogDao engineLogDao;
	@EJB
	private FuelUsageLogDao fuelUsageLogDao;
	@EJB
	private ZoneDao zoneDao;
	@EJB
	private IrrigationDetailsDao irrigationDetailsDao;
	@EJB
	private ActivitiesAndMachineDao activitiesAndMachineDao;
	@EJB
	private TransactionTypeDao transactionTypeDao;
	@Resource
	private UserTransaction userTransaction;

	/**
	 * @return nameActivitySearch: return the activity name that user want
	 *         searching.
	 */
	public String getNameActivitySearch() {
		return nameActivitySearch;
	}

	/**
	 * @param nameActivitySearch
	 *            : set the activity name that user want searching.
	 */
	public void setNameActivitySearch(String nameActivitySearch) {
		this.nameActivitySearch = nameActivitySearch;
	}

	/**
	 * @return finalLevel: it is the last final level of fuelUsageLog minus the
	 *         consumption on engineLog.
	 */
	public Double getFinalLevel() {
		return finalLevel;
	}

	/**
	 * @param finalLevel
	 *            : it is the last final level of fuelUsageLog minus the
	 *            consumption on engineLog.
	 */
	public void setFinalLevel(Double finalLevel) {
		this.finalLevel = finalLevel;
	}

	/**
	 * @return activitiesMachineList: return an activityMachine objects list.
	 */
	public List<ActivityMachine> getActivitiesMachineList() {
		return activitiesMachineList;
	}

	/**
	 * @param activitiesMachineList
	 *            : set an activityMachine objects list.
	 */
	public void setActivitiesMachineList(
			List<ActivityMachine> activitiesMachineList) {
		this.activitiesMachineList = activitiesMachineList;
	}

	/**
	 * @return itemsZone: items zone list.
	 */
	public List<SelectItem> getItemsZone() {
		return itemsZone;
	}

	/**
	 * @param itemsZone
	 *            : items zone list.
	 */
	public void setItemsZone(List<SelectItem> itemsZone) {
		this.itemsZone = itemsZone;
	}

	/**
	 * @return engineLog: return engine log object with the information
	 *         required.
	 */
	public EngineLog getEngineLog() {
		return engineLog;
	}

	/**
	 * @param engineLog
	 *            : add the information required to engine log object.
	 */
	public void setEngineLog(EngineLog engineLog) {
		this.engineLog = engineLog;
	}

	/**
	 * @return fuelUsageLog: FuelUsageLog object related with the engineLog
	 *         object.
	 */
	public FuelUsageLog getFuelUsageLog() {
		return fuelUsageLog;
	}

	/**
	 * @param fuelUsageLog
	 *            : FuelUsageLog object related with the engineLog object.
	 */
	public void setFuelUsageLog(FuelUsageLog fuelUsageLog) {
		this.fuelUsageLog = fuelUsageLog;
	}

	/**
	 * @return irrigationDetails: IrrigationDetails object related with the
	 *         engineLog object.
	 */
	public IrrigationDetails getIrrigationDetails() {
		return irrigationDetails;
	}

	/**
	 * @param irrigationDetails
	 *            : IrrigationDetails object related with the engineLog object.
	 */
	public void setIrrigationDetails(IrrigationDetails irrigationDetails) {
		this.irrigationDetails = irrigationDetails;
	}

	/**
	 * @return zone: Zone object related with the engineLog object.
	 */
	public Zone getZone() {
		return zone;
	}

	/**
	 * @param zone
	 *            : Zone object related with the engineLog object.
	 */
	public void setZone(Zone zone) {
		this.zone = zone;
	}

	/**
	 * @return machineIrrigation: Machines object related with the engineLog
	 *         object.
	 */
	public Machines getMachineIrrigation() {
		return machineIrrigation;
	}

	/**
	 * @param machineIrrigation
	 *            : Machines object related with the engineLog object.
	 */
	public void setMachineIrrigation(Machines machineIrrigation) {
		this.machineIrrigation = machineIrrigation;
	}

	/**
	 * @return pagination : management responsible paged list from search engine
	 *         log.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : management responsible paged list from search engine log.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationForm : management responsible paged list from search
	 *         activityMachine.
	 */
	public Paginador getPaginationForm() {
		return paginationForm;
	}

	/**
	 * @param paginationForm
	 *            : management responsible paged list from search
	 *            activityMachine.
	 */
	public void setPaginationForm(Paginador paginationForm) {
		this.paginationForm = paginationForm;
	}

	/**
	 * @return startDateSearch: gets the initial search range for the fuel usage
	 *         in the system.
	 */
	public Date getStartDateSearch() {
		return startDateSearch;
	}

	/**
	 * @param startDateSearch
	 *            : sets the initial search range for the fuel usage in the
	 *            system.
	 */
	public void setStartDateSearch(Date startDateSearch) {
		this.startDateSearch = startDateSearch;
	}

	/**
	 * @return endDateSearch: gets the end range to search the fuel usage in the
	 *         system.
	 */
	public Date getEndDateSearch() {
		return endDateSearch;
	}

	/**
	 * @param endDateSearch
	 *            :sets the end range to search the fuel usage in the system.
	 */
	public void setEndDateSearch(Date endDateSearch) {
		this.endDateSearch = endDateSearch;
	}

	/**
	 * @return engineLogList: list of engine log stored in data base.
	 */
	public List<FuelUsageLog> getEngineLogList() {
		return engineLogList;
	}

	/**
	 * @param engineLogList
	 *            : list of engine log stored in data base.
	 */
	public void setEngineLogList(List<FuelUsageLog> engineLogList) {
		this.engineLogList = engineLogList;
	}

	/**
	 * @return irrigationDetailsList: list of irrigation details stored in data
	 *         base.
	 */
	public List<IrrigationDetails> getIrrigationDetailsList() {
		return irrigationDetailsList;
	}

	/**
	 * @param irrigationDetailsList
	 *            : list of irrigation details stored in data base.
	 */
	public void setIrrigationDetailsList(
			List<IrrigationDetails> irrigationDetailsList) {
		this.irrigationDetailsList = irrigationDetailsList;
	}

	/**
	 * Cleans the person field associated with the engine log.
	 */
	public void deleteHr1() {
		this.engineLog.setDeliveredBy(new Hr());
	}

	/**
	 * Cleans the person field associated with the engine log.
	 */
	public void deleteHr2() {
		this.engineLog.setReceivedBy(new Hr());
	}

	/**
	 * Cleans the activityMachine field associated with the engine log.
	 */
	public void deleteActivity() {
		engineLog.getActivityMachine().getActivityMachinePK()
				.setActivities(new Activities());
		engineLog.getActivityMachine().getActivityMachinePK()
				.setMachines(new Machines());
	}

	/**
	 * Cleans the person field associated with the engine log.
	 */
	public void deleteMachine() {
		this.machineIrrigation = new Machines();
	}

	/**
	 * Method to load the selected deliver person.
	 * 
	 * @param person
	 *            : Object of the selected deliver person.
	 */
	public void loadHr1(Hr hr) {
		this.engineLog.setDeliveredBy(hr);
	}

	/**
	 * Method to load the selected receiver person.
	 * 
	 * @param person
	 *            : Object of the selected receiver person.
	 */
	public void loadHr2(Hr hr) {
		this.engineLog.setReceivedBy(hr);
	}

	/**
	 * Method to load the selected machine.
	 * 
	 * @param person
	 *            : Object of the selected machine.
	 */
	public void loadMachine(Machines machine) {
		this.machineIrrigation = machine;
	}

	/**
	 * Method to load the selected activityMachine;
	 * 
	 * @param person
	 *            : Object of the selected activityMachine.
	 */
	public void loadActivityMachine(ActivityMachine activityMachine) {
		this.engineLog.setActivityMachine(activityMachine);
	}

	/**
	 * Method to initialize the parameters of the search and load initial list
	 * of ActivityMachines.
	 */
	public void initializeSearchActivityMachine() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundleDiesel = context.getApplication()
				.getResourceBundle(context, "messageDiesel");
		this.nameActivitySearch = "";
		if (this.engineLog.getDate() != null) {
			searchActivitiesAndMachine();
		} else {
			ControladorContexto.mensajeError(null, "formEngineLog:txtActivity",
					bundleDiesel.getString("engine_log_message_select_date"));
		}
	}

	/**
	 * Method for initializer engineLog register view, set a engineLog if user
	 * start edit function or load a new object for register.
	 * 
	 * @param engineLog
	 *            : object that user need modify.
	 * @return "regEngineLog": return the string for redirect to register view.
	 */
	public String addEditEngineLog(EngineLog engineLog) {
		if (engineLog != null) {
			this.engineLog = engineLog;
		} else {
			this.zone = new Zone();
			this.machineIrrigation = new Machines();
			this.engineLog = new EngineLog();
			this.irrigationDetails = new IrrigationDetails();
			this.fuelUsageLog = new FuelUsageLog();
			this.engineLog.setReceivedBy(new Hr());
			this.engineLog.setDeliveredBy(new Hr());
			this.engineLog.setIrrigation(false);
			this.engineLog.setActivityMachine(new ActivityMachine());
			this.engineLog.getActivityMachine().setActivityMachinePK(
					new ActivityMachinePK());
			this.engineLog.getActivityMachine().getActivityMachinePK()
					.setActivities(new Activities());
			this.engineLog.getActivityMachine().getActivityMachinePK()
					.getActivities().setActivityName(new ActivityNames());
			this.engineLog.getActivityMachine().getActivityMachinePK()
					.setMachines(new Machines());
		}
		return "regEngineLog";
	}

	/**
	 * Method to save or edit engine logs.
	 * 
	 * @return searchInitialization(): Method that return the navigation rule
	 *         for redirects to management engineLog template.
	 */
	public String saveEngineLog() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		SimpleDateFormat dateFormat = new SimpleDateFormat(
				Constantes.DATE_FORMAT_TABLE);
		String messageSave = dateFormat.format(this.engineLog.getDate());
		try {
			userTransaction.begin();
			if (engineLog.getIdEngineLog() != 0) {
				engineLogDao.editEngineLog(engineLog);
			} else {
				registerMessage = "message_registro_guardar";
				if (this.engineLog.isIrrigation()) {
					this.engineLog.setActivityMachine(null);
				}
				if (this.engineLog.getDeliveredBy().getIdHr() == 0) {
					this.engineLog.setDeliveredBy(null);
				}
				if (this.engineLog.getReceivedBy().getIdHr() == 0) {
					this.engineLog.setReceivedBy(null);
				}
				engineLogDao.saveEngineLog(this.engineLog);

				this.fuelUsageLog.setEngineLog(this.engineLog);
				this.fuelUsageLog.setDate(new Date());
				this.fuelUsageLog.setFuelPurchase(null);
				this.fuelUsageLog.setFinalLevel(this.finalLevel);

				TransactionType transactionType = transactionTypeDao
						.transactionTypeById(Constantes.TRANSACTION_TYPE_WITHDRAWAL);
				this.fuelUsageLog.setTransactionType(transactionType);
				this.fuelUsageLogDao.saveFuelUsage(this.fuelUsageLog);

				if (this.engineLog.isIrrigation()) {
					this.irrigationDetails.setZone(this.zone);
					this.irrigationDetails.setMachine(this.machineIrrigation);
					this.irrigationDetails.setEngineLog(engineLog);
					irrigationDetailsDao
							.saveIrrigationDetails(irrigationDetails);
					Zone zone = zoneDao.zoneById(this.zone.getId());
					messageSave = messageSave + " - " + zone.getName() + " - "
							+ this.irrigationDetails.getMachine().getName();
				} else {
					messageSave = messageSave
							+ " - "
							+ this.engineLog.getActivityMachine()
									.getActivityMachinePK().getActivities()
									.getActivityName().getActivityName()
							+ " - "
							+ this.engineLog.getActivityMachine()
									.getActivityMachinePK().getMachines()
									.getName();
				}
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), messageSave));
			userTransaction.commit();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchInitialization();
	}

	/**
	 * Method for get a ActivityMachine list for user associate a object to
	 * engine log.
	 */
	public void searchActivitiesAndMachine() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleDiesel = ControladorContexto
				.getBundle("messageDiesel");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.activitiesMachineList = new ArrayList<ActivityMachine>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder joinSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearchActivities(queryBuilder, parameters, bundle,
					bundleDiesel, joinSearchMessages);
			Long amount = activitiesAndMachineDao.quantityActivitiesAndMachine(
					queryBuilder, parameters);
			if (amount != null) {
				paginationForm.paginarRangoDefinido(amount, 5);
				this.activitiesMachineList = activitiesAndMachineDao
						.consultingActivitiesAndMachine(
								paginationForm.getInicio(),
								paginationForm.getRango(), queryBuilder,
								parameters);
			}
			if ((activitiesMachineList == null || activitiesMachineList.size() <= 0)
					&& !"".equals(joinSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								joinSearchMessages);
			} else if (activitiesMachineList == null
					|| activitiesMachineList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(joinSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessage = MessageFormat.format(message,
						bundleDiesel.getString("engine_Log_label_activity"),
						joinSearchMessages);
			}
			validations.setMensajeBusquedaPopUp(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method builds a query with advanced search of ActivityMachine; it
	 * also render the messages to be displayed depending on the search criteria
	 * selected by the user.
	 * 
	 * @param query
	 *            : Query to concatenate.
	 * @param parameter
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param bundleDiesel
	 *            : Access diesel language tags.
	 * @param joinSearchMessages
	 *            : Message search.
	 */
	private void advancedSearchActivities(StringBuilder query,
			List<SelectItem> parameter, ResourceBundle bundle,
			ResourceBundle bundleDiesel, StringBuilder joinSearchMessages) {
		query.append(" WHERE am.initialDateTime BETWEEN :initialDate AND :finalDate ");
		SelectItem dieselItem = new SelectItem(
				ControladorFechas.inicioDeDia(this.engineLog.getDate()),
				"initialDate");
		parameter.add(dieselItem);
		dieselItem = new SelectItem(ControladorFechas.finDeDia(this.engineLog
				.getDate()), "finalDate");
		parameter.add(dieselItem);
		query.append(" AND m.fuel = :diesel ");
		dieselItem = new SelectItem(true, "diesel");
		parameter.add(dieselItem);
		if ((this.nameActivitySearch != null && !""
				.equals(this.nameActivitySearch))) {
			query.append(" AND UPPER(ac.activityName.activityName) LIKE UPPER(:keywordNombre) ");
			SelectItem nameItem = new SelectItem("%" + this.nameActivitySearch
					+ "%", "keywordNombre");
			parameter.add(nameItem);
			joinSearchMessages.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameActivitySearch + '"' + " ");
		}
	}

	/**
	 * This method allows load the zone list.
	 */
	public void loadZones() {
		try {
			this.irrigationDetails = new IrrigationDetails();
			itemsZone = new ArrayList<SelectItem>();
			List<Zone> zones = zoneDao.consultZonesList();
			if (zones != null) {
				for (Zone zone : zones) {
					itemsZone.add(new SelectItem(zone.getId(), zone.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates interface required fields.
	 */
	public void validateRequired() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(
				context, "mensaje");
		ResourceBundle bundleDiesel = context.getApplication()
				.getResourceBundle(context, "messageDiesel");
		try {
			if (this.engineLog.getDate() == null) {
				ControladorContexto.mensajeRequeridos("formEngineLog:txtDate");
			}
			if (this.engineLog.getHourOn() == null) {
				ControladorContexto
						.mensajeRequeridos("formEngineLog:txtHourOn");
			}
			if (this.engineLog.getHourOff() == null) {
				ControladorContexto
						.mensajeRequeridos("formEngineLog:txtHourOff");
			}
			if (this.engineLog.getHourOff() != null
					&& this.engineLog.getHourOn() != null) {
				if (this.engineLog.getHourOff().compareTo(
						this.engineLog.getHourOn()) < 0) {
					ControladorContexto
							.mensajeError(
									null,
									"formEngineLog:txtHourOff",
									bundleDiesel
											.getString("engine_log_message_hour_off_higher"));
				}
				if ((this.engineLog.getHourmeterOn() != null && this.engineLog
						.getHourmeterOff() != null)
						&& this.engineLog.getHourOff().compareTo(
								this.engineLog.getHourOn()) == 0) {
					if (!this.engineLog.getHourmeterOn().equals(
							this.engineLog.getHourmeterOff())) {
						ControladorContexto
								.mensajeError(
										null,
										"formEngineLog:txtHourmeterOff",
										bundleDiesel
												.getString("engine_log_message_no_duration"));
					}
				}
			}
			if (this.engineLog.getHourmeterOn() == null) {
				ControladorContexto
						.mensajeRequeridos("formEngineLog:txtHourmeterOn");
			}
			if (this.engineLog.getHourmeterOff() == null) {
				ControladorContexto
						.mensajeRequeridos("formEngineLog:txtHourmeterOff");
			}
			if (this.engineLog.getDuration() == null) {
				ControladorContexto
						.mensajeRequeridos("formEngineLog:txtDuration");
			}
			if (this.fuelUsageLog.getConsumption() == null) {
				ControladorContexto
						.mensajeRequeridos("formEngineLog:txtConsumption");
			}
			if (this.fuelUsageLog.getConsumption() != null) {
				if (this.engineLog.getDuration() != null
						&& (this.engineLog.getDuration() > 0 && this.fuelUsageLog
								.getConsumption() == 0)) {
					ControladorContexto.mensajeError(null,
							"formEngineLog:txtConsumption",
							bundle.getString("message_campo_mayo_cero"));
				}
				if (this.engineLog.getDuration() != null
						&& (this.engineLog.getDuration() == 0 && this.fuelUsageLog
								.getConsumption() > 0)) {
					ControladorContexto
							.mensajeError(
									null,
									"formEngineLog:txtConsumption",
									bundleDiesel
											.getString("engine_log_message_no_consumption"));
				}
				FuelUsageLog fuelUsage = this.fuelUsageLogDao
						.consultLastFuelUsage();
				this.finalLevel = ControllerAccounting.subtract(
						fuelUsage.getFinalLevel(),
						this.fuelUsageLog.getConsumption());
				if (this.finalLevel < 0) {
					ControladorContexto.mensajeError(null,
							"formEngineLog:txtConsumption", bundleDiesel
									.getString("engine_log_message_no_diesel"));
				}
			}
			if (this.engineLog.isIrrigation()) {
				if (this.zone.getId() == 0) {
					ControladorContexto
							.mensajeRequeridos("formEngineLog:txtZone");
				}
				if (this.machineIrrigation.getIdMachine() == 0) {
					ControladorContexto
							.mensajeRequeridos("formEngineLog:txtMachine");
				}
				if (this.irrigationDetails.getHidrometerOn() == null) {
					ControladorContexto
							.mensajeRequeridos("formEngineLog:txtHidrometerOn");
				}
				if (this.irrigationDetails.getHidrometerOff() == null) {
					ControladorContexto
							.mensajeRequeridos("formEngineLog:txtHidrometerOff");
				}
				if (this.irrigationDetails.getHidrometerOn() != null
						&& this.irrigationDetails.getHidrometerOff() != null) {
					if ((this.engineLog.getHourOff() != null && this.engineLog
							.getHourOn() != null)
							&& this.engineLog.getHourOff().compareTo(
									this.engineLog.getHourOn()) == 0
							&& !this.irrigationDetails.getHidrometerOn()
									.equals(this.irrigationDetails
											.getHidrometerOff())) {
						ControladorContexto
								.mensajeError(
										null,
										"formEngineLog:txtHidrometerOff",
										bundleDiesel
												.getString("irrigation_details_message_no_water_usage"));
					}
				}
				if (this.irrigationDetails.getWaterUsage() == null) {
					ControladorContexto
							.mensajeRequeridos("formEngineLog:txtWaterUsage");
				}
			} else {
				if (this.engineLog.getActivityMachine().getActivityMachinePK()
						.getActivities().getIdActivity() == 0
						|| this.engineLog.getActivityMachine()
								.getActivityMachinePK().getMachines()
								.getIdMachine() == 0) {
					ControladorContexto
							.mensajeError(
									null,
									"formEngineLog:txtActivity",
									bundleDiesel
											.getString("engine_log_message_select_activity"));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows calculate the difference between hourmeterOn and
	 * hourmeterOff or difference between hidrometerOn and hidrometerOff.
	 */
	public void calculateDifference() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundleDiesel = context.getApplication()
				.getResourceBundle(context, "messageDiesel");
		if ((this.engineLog.getHourmeterOn() != null && this.engineLog
				.getHourmeterOff() != null)
				&& this.engineLog.getHourmeterOff() >= this.engineLog
						.getHourmeterOn()) {
			Double duration = ControllerAccounting.subtract(
					this.engineLog.getHourmeterOff(),
					this.engineLog.getHourmeterOn());
			this.engineLog.setDuration(duration);
		} else {
			ControladorContexto
					.mensajeError(
							null,
							"formEngineLog:txtHourmeterOff",
							bundleDiesel
									.getString("engine_log_message_hourmeter_off_higher"));
		}
		if (this.engineLog.isIrrigation()
				&& (this.irrigationDetails.getHidrometerOn() != null && this.irrigationDetails
						.getHidrometerOff() != null)) {
			if (this.irrigationDetails.getHidrometerOff() >= this.irrigationDetails
					.getHidrometerOn()) {
				Double waterUsage = ControllerAccounting.subtract(
						this.irrigationDetails.getHidrometerOff(),
						this.irrigationDetails.getHidrometerOn());
				this.irrigationDetails.setWaterUsage(waterUsage);
			} else {
				ControladorContexto
						.mensajeError(
								null,
								"formEngineLog:txtHidrometerOff",
								bundleDiesel
										.getString("irrigation_details_message_hidrometer_off_higher"));
			}
		}
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @return consultEngineLog: engineLog consulting method and redirects to
	 *         the template to manage engine log.
	 */
	public String searchInitialization() {
		this.engineLog = new EngineLog();
		this.fuelUsageLog = new FuelUsageLog();
		this.startDateSearch = null;
		this.endDateSearch = null;
		return consultEngineLog();
	}

	/**
	 * Consult the list of consumable resources
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @return gesEngineLog: Navigation rule that redirects to manage engine log
	 */
	public String consultEngineLog() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleConsumableResources = ControladorContexto
				.getBundle("messageDiesel");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		engineLogList = new ArrayList<FuelUsageLog>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = fuelUsageLogDao
					.quantityEngineLog(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				engineLogList = fuelUsageLogDao.consultEngineLog(
						pagination.getInicio(), pagination.getRango(), query,
						parameters);
			}
			if ((engineLogList == null || engineLogList.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (engineLogList == null || engineLogList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleConsumableResources
										.getString("engine_log_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesEngineLog";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat formats = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		if (this.startDateSearch != null && this.endDateSearch != null) {
			consult.append("AND el.date BETWEEN :startDateSearch AND :endDateSearch ");
			SelectItem item = new SelectItem(startDateSearch, "startDateSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(endDateSearch, "endDateSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_start_date") + ": " + '"'
					+ formats.format(this.startDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateFrom);
			String dateTo = bundle.getString("label_end_date") + ": " + '"'
					+ formats.format(endDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateTo);
		}
	}

	/**
	 * This method allows show the information of table irrigation details
	 * depending of an identifier
	 * 
	 * @author Fabian.Diaz
	 */
	public void showIrrigationDetails() {
		try {
			if (this.fuelUsageLog.getEngineLog().getIdEngineLog() != 0
					&& this.fuelUsageLog != null
					&& this.fuelUsageLog.getEngineLog().isIrrigation()) {
				this.irrigationDetails = new IrrigationDetails();
				this.irrigationDetails = irrigationDetailsDao
						.consultIrrigationDetails(this.fuelUsageLog
								.getEngineLog().getIdEngineLog());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
