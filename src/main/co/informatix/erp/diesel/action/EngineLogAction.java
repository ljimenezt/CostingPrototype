package co.informatix.erp.diesel.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
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
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

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

	private List<ActivityMachine> activitiesMachineList;
	private List<SelectItem> itemsZone;

	private EngineLog engineLog;
	private FuelUsageLog fuelUsageLog;
	private IrrigationDetails irrigationDetails;
	private Zone zone;
	private Machines machineIrrigation;
	private Paginador paginationForm = new Paginador();

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
	 * @return addEditEngineLog: Redirects to register engineLog template.
	 * 
	 */
	public String saveEngineLog() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
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
				if (this.fuelUsageLog.getConsumption() != null) {
					FuelUsageLog fuelUsage = this.fuelUsageLogDao
							.consultLastFuelUsage();
					fuelUsage
							.setConsumption(this.fuelUsageLog.getConsumption());
					this.fuelUsageLogDao.saveFuelUsage(fuelUsage);
				}
				if (this.engineLog.isIrrigation()) {
					this.irrigationDetails.setZone(this.zone);
					this.irrigationDetails.setMachine(this.machineIrrigation);
					this.irrigationDetails.setEngineLog(engineLog);
					irrigationDetailsDao
							.saveIrrigationDetails(irrigationDetails);
				}
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage),
					engineLog.getIdEngineLog()));
			userTransaction.commit();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return addEditEngineLog(null);
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
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
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

		query.append(" WHERE ac.initialDtBudget >= current_date() ");

		query.append(" AND m.fuel = :diesel ");
		SelectItem dieselItem = new SelectItem(true, "diesel");
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
	public void validarRequeridos() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(
				context, "mensaje");
		try {
			if (this.engineLog.getDate() == null) {
				context.addMessage(
						"formEngineLog:txtDate",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								MessageFormat.format(bundle
										.getString("message_campo_requerido"),
										"date"), null));
			}
			if (this.engineLog.getHourmeterOn() == null) {
				context.addMessage(
						"formEngineLog:txtHourmeterOn",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								MessageFormat.format(bundle
										.getString("message_campo_requerido"),
										"hourmeterOn"), null));
			}
			if (this.engineLog.getHourmeterOff() == null) {
				context.addMessage(
						"formEngineLog:txtHourmeterOff",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								MessageFormat.format(bundle
										.getString("message_campo_requerido"),
										"hourmeterOff"), null));
			}
			if (this.engineLog.getHourOn() == null) {
				context.addMessage(
						"formEngineLog:txtHourOn",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								MessageFormat.format(bundle
										.getString("message_campo_requerido"),
										"hourOn"), null));
			}
			if (this.engineLog.getHourOff() == null) {
				context.addMessage(
						"formEngineLog:txtHourOff",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								MessageFormat.format(bundle
										.getString("message_campo_requerido"),
										"hourOff"), null));
			}
			if (this.engineLog.getDuration() == null) {
				context.addMessage(
						"formEngineLog:txtDuration",
						new FacesMessage(FacesMessage.SEVERITY_ERROR,
								MessageFormat.format(bundle
										.getString("message_campo_requerido"),
										"duration"), null));
			}
			if (this.engineLog.isIrrigation()) {
				if (this.zone.getId() == 0) {
					context.addMessage(
							"formEngineLog:txtZone",
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									MessageFormat.format(
											bundle.getString("message_campo_requerido"),
											"zone"), null));
				}
				if (this.machineIrrigation.getIdMachine() == 0) {
					context.addMessage(
							"formEngineLog:txtMachine",
							new FacesMessage(
									FacesMessage.SEVERITY_ERROR,
									MessageFormat.format(
											bundle.getString("message_campo_requerido"),
											"machine"), null));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * 
	 */
	public void calcularDuration() {
		FacesContext context = FacesContext.getCurrentInstance();
		ResourceBundle bundle = context.getApplication().getResourceBundle(
				context, "messageDiesel");
		if (this.engineLog.getHourmeterOn() < this.engineLog.getHourmeterOff()) {
			Double duration = this.engineLog.getHourmeterOff()
					- this.engineLog.getHourmeterOn();
			this.engineLog.setDuration(duration);
		} else {
			context.addMessage(
					"formEngineLog:txtHourmeterOff",
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							MessageFormat.format(
									bundle.getString("engine_log_message_hourmeter_off_higher"),
									""), null));
		}
	}
}
