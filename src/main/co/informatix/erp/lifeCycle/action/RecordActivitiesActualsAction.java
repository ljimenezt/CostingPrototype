package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.action.ActivitiesAction;
import co.informatix.erp.costs.action.ActivitiesAndHrAction;
import co.informatix.erp.costs.dao.ActivitiesAndHrDao;
import co.informatix.erp.costs.dao.ActivitiesAndMachineDao;
import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivitiesAndHr;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.humanResources.dao.OvertimePaymentRateDao;
import co.informatix.erp.humanResources.entities.OvertimePaymentRate;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.CycleDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Cycle;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;

/**
 * This class implements the logic of the relations between the activities and
 * human resources of the database. The logic is to update "CURRENT" in the
 * relations and human resources activities.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class RecordActivitiesActualsAction implements Serializable {

	@EJB
	private CropsDao cropDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private ActivityNamesDao activityNamesDao;
	@EJB
	private ActivitiesAndHrDao activitiesAndHrDao;
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private OvertimePaymentRateDao overtimePaymentRateDao;
	@EJB
	private ActivitiesAndMachineDao activitiesAndMachineDao;
	@EJB
	private CycleDao cycleDao;

	private int idCrop;
	private int idCropName;
	private int idOvertimePaymentsRate;
	private int idCycle;
	private boolean calculateCostsButtonActivated;

	private List<SelectItem> listCropNames;
	private List<SelectItem> listCrops;
	private List<SelectItem> listActivityNames;
	private List<Activities> listActivities;
	private List<ActivitiesAndHr> listActivitiesAndHr;
	private List<SelectItem> optionsCycles;

	private Activities activities;
	private Activities selectedActivity;
	private Crops crops;
	private ActivitiesAndHr activitiesAndHr;
	private ActivitiesAndHrAction activitiesAndHrAction;
	private ScheduledActivitiesAction scheduledActivitiesAction;
	private ActivitiesAction activitiesAction;
	private OvertimePaymentRate overtimePaymentRate;
	private Date maxDate;
	private Date minDate;
	private ActivityMachine activityMachine;

	/**
	 * @return idCrop: crop identifier.
	 */
	public int getIdCrop() {
		return idCrop;
	}

	/**
	 * @param idCrop
	 *            : crop identifier.
	 */
	public void setIdCrop(int idCrop) {
		this.idCrop = idCrop;
	}

	/**
	 * @return idCropName: CropName identifier.
	 */
	public int getIdCropName() {
		return idCropName;
	}

	/**
	 * @param idCropName
	 *            : CropName identifier.
	 */
	public void setIdCropName(int idCropName) {
		this.idCropName = idCropName;
	}

	/**
	 * @return idOvertimePaymentsRate: OvertimePaymentsRate identifier.
	 */
	public int getIdOvertimePaymentsRate() {
		return idOvertimePaymentsRate;
	}

	/**
	 * @return idCycle : Cycle identifier
	 */
	public int getIdCycle() {
		return idCycle;
	}

	/**
	 * @param idCycle
	 *            : Cycle identifier
	 */
	public void setIdCycle(int idCycle) {
		this.idCycle = idCycle;
	}

	/**
	 * @param idOvertimePaymentsRate
	 *            : OvertimePaymentsRate identifier.
	 */
	public void setIdOvertimePaymentsRate(int idOvertimePaymentsRate) {
		this.idOvertimePaymentsRate = idOvertimePaymentsRate;
	}

	/**
	 * @return calculateCostsButtonActivated: It sets a value for determining
	 *         whether a button is shown or not. setEstadoBotonCalcularCosto
	 */
	public boolean isCalculateCostsButtonActivated() {
		return calculateCostsButtonActivated;
	}

	/**
	 * @param calculateCostsButtonActivated
	 *            : It sets a value for determining whether a button is shown or
	 *            not.
	 */
	public void setCalculateCostsButtonActivated(
			boolean calculateCostsButtonActivated) {
		this.calculateCostsButtonActivated = calculateCostsButtonActivated;
	}

	/**
	 * @return listCropNames: List SelectItem cropNames kind.
	 */
	public List<SelectItem> getListCropNames() {
		return listCropNames;
	}

	/**
	 * @param listCropNames
	 *            : List SelectItem cropNames kind.
	 */
	public void setListCropNames(List<SelectItem> listCropNames) {
		this.listCropNames = listCropNames;
	}

	/**
	 * @return listCrops: Crops type list.
	 * 
	 */
	public List<SelectItem> getListCrops() {
		return listCrops;
	}

	/**
	 * @param listCrops
	 *            : Crops type list.
	 */
	public void setListCrops(List<SelectItem> listCrops) {
		this.listCrops = listCrops;
	}

	/**
	 * @return listActivityNames: List of SelectItem activityNames type.
	 */
	public List<SelectItem> getListActivityNames() {
		return listActivityNames;
	}

	/**
	 * @param listActivityNames
	 *            : List of SelectItem activityNames type.
	 */
	public void setListActivityNames(List<SelectItem> listActivityNames) {
		this.listActivityNames = listActivityNames;
	}

	/**
	 * @return listActivities: list of objects of type activities.
	 */
	public List<Activities> getListActivities() {
		return listActivities;
	}

	/**
	 * @param listActivities
	 *            : list of objects of type activities.
	 */
	public void setListActivities(List<Activities> listActivities) {
		this.listActivities = listActivities;
	}

	/**
	 * @return listActivitiesAndHr: list of the activities and resources humans
	 *         relationship.
	 */
	public List<ActivitiesAndHr> getListActivitiesAndHr() {
		return listActivitiesAndHr;
	}

	/**
	 * @param listActivitiesAndHr
	 *            : list of the activities and resources humans relationship.
	 */
	public void setListActivitiesAndHr(List<ActivitiesAndHr> listActivitiesAndHr) {
		this.listActivitiesAndHr = listActivitiesAndHr;
	}

	/**
	 * @return optionsCycles : Cycles list
	 */
	public List<SelectItem> getOptionsCycles() {
		return optionsCycles;
	}

	/**
	 * @param optionsCycles
	 *            : Cycles list
	 */
	public void setOptionsCycles(List<SelectItem> optionsCycles) {
		this.optionsCycles = optionsCycles;
	}

	/**
	 * @return activities: object type activities.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : object type activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return selectedActivity: Object type selected activity.
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            : Object type selected activity.
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return Crops: Crops object
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : Crops object
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return activitiesAndHr: Object type ActivitiesAndHr
	 */
	public ActivitiesAndHr getActivitiesAndHr() {
		return activitiesAndHr;
	}

	/**
	 * @param activitiesAndHr
	 *            : Object type ActivitiesAndHr
	 */
	public void setActivitiesAndHr(ActivitiesAndHr activitiesAndHr) {
		this.activitiesAndHr = activitiesAndHr;
	}

	/**
	 * @return activitiesAndHrAction: Object of Activities and Human Resources
	 */
	public ActivitiesAndHrAction getActivitiesAndHrAction() {
		return activitiesAndHrAction;
	}

	/**
	 * @param activitiesAndHrAction
	 *            : Object of Activities and Human Resources
	 */
	public void setActivitiesAndHrAction(
			ActivitiesAndHrAction activitiesAndHrAction) {
		this.activitiesAndHrAction = activitiesAndHrAction;
	}

	/**
	 * @return scheduledActivitiesAction: scheduledActivitiesAction object.
	 */
	public ScheduledActivitiesAction getScheduledActivitiesAction() {
		return scheduledActivitiesAction;
	}

	/**
	 * @param scheduledActivitiesAction
	 *            : scheduledActivitiesAction object.
	 */
	public void setScheduledActivitiesAction(
			ScheduledActivitiesAction scheduledActivitiesAction) {
		this.scheduledActivitiesAction = scheduledActivitiesAction;
	}

	/**
	 * @return activitiesAction: ActivitiesAction object
	 */
	public ActivitiesAction getActivitiesAction() {
		return activitiesAction;
	}

	/**
	 * @param activitiesAction
	 *            : ActivitiesAction object
	 */
	public void setActivitiesAction(ActivitiesAction activitiesAction) {
		this.activitiesAction = activitiesAction;
	}

	/**
	 * @return overtimePaymentRate: overtimePaymentRate object.
	 */
	public OvertimePaymentRate getOvertimePaymentRate() {
		return overtimePaymentRate;
	}

	/**
	 * @param overtimePaymentRate
	 *            : overtimePaymentRate object.
	 */
	public void setOvertimePaymentRate(OvertimePaymentRate overtimePaymentRate) {
		this.overtimePaymentRate = overtimePaymentRate;
	}

	/**
	 * @return maxDate: Maximum Date
	 */
	public Date getMaxDate() {
		return maxDate;
	}

	/**
	 * @param maxDate
	 *            : Maximum Date
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * @return minDate: Minimum date
	 */
	public Date getMinDate() {
		return minDate;
	}

	/**
	 * @param minDate
	 *            : Minimum date
	 */
	public void setMinDate(Date minDate) {
		this.minDate = minDate;
	}

	/**
	 * @return activityMachine: object that represents the ratio of a machine
	 *         and an activity
	 */
	public ActivityMachine getActivityMachine() {
		return activityMachine;
	}

	/**
	 * @param activityMachine
	 *            : object that represents the ratio of a machine and an
	 *            activity
	 */
	public void setActivityMachine(ActivityMachine activityMachine) {
		this.activityMachine = activityMachine;
	}

	/**
	 * Initializes the necessary variables for the management actual activities
	 * 
	 * @modify 22/03/2016 Andres.Gomez
	 * @modify 20/06/2016 Liseth.Jimenez
	 * 
	 * @return recordActivitiesActuals: redirected to the management to keep
	 *         actual activities
	 */
	public String initializeRecordActual() {
		try {
			if (ControladorContexto.getFacesContext() != null) {
				this.activitiesAction = ControladorContexto
						.getContextBean(ActivitiesAction.class);
			}
			this.idCrop = 0;
			this.idCropName = 0;
			this.activities = new Activities();
			this.listActivities = new ArrayList<Activities>();
			this.selectedActivity = new Activities();
			this.activitiesAndHrAction = new ActivitiesAndHrAction();
			if (this.activitiesAction != null
					&& this.activitiesAction.getListActivities() != null)
				this.activitiesAction.getListActivities().clear();
			crops = cropDao.defaultSearchCrop(Constantes.ID_CROP_DEFAULT);
			if (crops != null) {
				idCropName = crops.getCropNames().getIdCropName();
				idCrop = crops.getIdCrop();
				showActivities();
			} else {
				idCrop = 0;
				idCropName = 0;
			}
			loadComboCrops();
			loadComboCropName();
			loadCycles();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "recordActivitiesActuals";
	}

	/**
	 * Load the names of the crops that are listed.
	 * 
	 * @throws Exception
	 */
	private void loadComboCropName() throws Exception {
		this.listCropNames = new ArrayList<SelectItem>();
		List<CropNames> cropNamesTypes = cropNamesDao.listCropNames();
		if (cropNamesTypes != null) {
			for (CropNames cropName : cropNamesTypes) {
				listCropNames.add(new SelectItem(cropName.getIdCropName(),
						cropName.getCropName()));
			}
		}
	}

	/**
	 * To load the associated crops to a CropName.
	 */
	public void loadComboCrops() {
		try {
			listCrops = new ArrayList<SelectItem>();
			List<Crops> cropTypes = cropDao
					.consultCropNamesCropsCurrent(idCropName);
			if (cropTypes != null) {
				for (Crops crop : cropTypes) {
					listCrops.add(new SelectItem(crop.getIdCrop(), crop
							.getDescription()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Complete the list of cycles according to the crop selected name.
	 * 
	 * @author Liseth.Jimenez
	 */
	public void loadCycles() {
		try {
			optionsCycles = new ArrayList<SelectItem>();
			List<Cycle> listCycles = cycleDao.consultCycleByCrop(idCrop);
			if (listCycles != null) {
				for (Cycle cycle : listCycles) {
					optionsCycles.add(new SelectItem(cycle.getIdCycle(), cycle
							.getCycleNumber()
							+ " - "
							+ cycle.getActiviyNames().getActivityName()));
				}
				idCycle = listCycles.get(0).getIdCycle();
			}
			showActivities();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Load the names of available activities associated with a crop listed.
	 */
	public void showActivities() {
		try {
			listActivityNames = new ArrayList<SelectItem>();
			List<ActivityNames> activityNameTypes = activityNamesDao
					.queryActivityNamesXCrop(idCrop);
			if (activityNameTypes != null) {
				for (ActivityNames activity : activityNameTypes) {
					listActivityNames.add(new SelectItem(activity
							.getIdActivityName(), activity.getActivityName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Query activities that were already assigned to a human resource.
	 */
	public void assignActivities() {
		this.listActivities = new ArrayList<Activities>();
		if (ControladorContexto.getFacesContext() != null) {
			this.activitiesAction = ControladorContexto
					.getContextBean(ActivitiesAction.class);
			this.listActivities = activitiesAction.getListActivities();
		}
	}

	/**
	 * Select one activity from a list of activities.
	 * 
	 * @param activity
	 *            : Object type activities
	 */
	public void assignActivity(Activities activity) {
		if (ControladorContexto.getFacesContext() != null) {
			this.activitiesAndHrAction = ControladorContexto
					.getContextBean(ActivitiesAndHrAction.class);
		}
		this.selectedActivity = new Activities();
		this.listActivitiesAndHr = new ArrayList<ActivitiesAndHr>();
		activity.setSeleccionado(true);
		if (this.listActivities != null) {
			for (Activities activityTemp : this.listActivities) {
				if (activityTemp.isSeleccionado()) {
					if (activityTemp.getIdActivity() == activity
							.getIdActivity()) {
						this.selectedActivity = activity;
						activitiesAndHrAction
								.setSelectedActivity(selectedActivity);
					} else {
						activityTemp.setSeleccionado(false);
					}
				}
			}
		}
	}

	/**
	 * It verifies that all records from the list of activities and human
	 * resources have a value in the field of current total cost.
	 * 
	 * @modify 12/01/2016 Wilhelm.Boada
	 */
	public void currentCost() {
		if (ControladorContexto.getFacesContext() != null) {
			this.scheduledActivitiesAction = ControladorContexto
					.getContextBean(ScheduledActivitiesAction.class);
		}
		this.calculateCostsButtonActivated = false;
		if (this.listActivitiesAndHr != null) {
			for (ActivitiesAndHr activitiesAndHr : this.listActivitiesAndHr) {
				if (activitiesAndHr.getTotalCostActual() == null) {
					this.calculateCostsButtonActivated = true;
				}
			}
		}
		if (this.scheduledActivitiesAction.getListActivityMachine() != null) {
			for (ActivityMachine activityMachine : this.scheduledActivitiesAction
					.getListActivityMachine()) {
				if (activityMachine.getConsumablesCostActual() == null) {
					this.calculateCostsButtonActivated = true;
				}
			}
		}
		if (this.listActivitiesAndHr == null
				&& this.scheduledActivitiesAction.getListActivityMachine() == null) {
			this.calculateCostsButtonActivated = true;
		}
	}

	/**
	 * It copies the fields budget to current fields.
	 */
	public void budgetCopy() {
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		if (!fromModal) {
			activitiesAndHr.setInitialDateTimeActual(activitiesAndHr
					.getInitialDateTimeBudget());
			activitiesAndHr.setFinalDateTimeActual(activitiesAndHr
					.getFinalDateTimeBudget());
			activitiesAndHr.setDurationActual(activitiesAndHr
					.getDurationBudget());
			setIdOvertimePaymentsRate(activitiesAndHr.getOvertimePaymentRate()
					.getOvertimepaymentid());
			double hourCost = activitiesAndHr.getTotalCostBudget();
			if (activitiesAndHr.getDurationActual() > 8) {
				hourCost = calculateCostOvertime(activitiesAndHr
						.getDurationActual());
			}
			activitiesAndHr.setTotalCostActual(hourCost);
		} else {
			this.activityMachine.setDurationActual(this.activityMachine
					.getDurationBudget());
			this.activityMachine.setConsumablesCostActual(this.activityMachine
					.getConsumablesCostBudget());

		}
	}

	/**
	 * It updates the human resources activities and its relations.
	 * 
	 * @modify 12/01/2016 Wilhelm.Boada
	 */
	public void updateActivitiesAndHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		try {
			if (!fromModal) {
				OvertimePaymentRate overtimePaymentRate = overtimePaymentRateDao
						.overtimePaymentRateXId(idOvertimePaymentsRate);
				activitiesAndHr.setOvertimePaymentRate(overtimePaymentRate);
				activitiesAndHrDao.editActivitiesAndHr(activitiesAndHr);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(registerMessage),
								activitiesAndHr.getActivitiesAndHrPK().getHr()
										.getName()));
				activitiesAndHrAction.consultActivitiesAndHrByActivity();
			} else {
				ScheduledActivitiesAction scheduledActivitiesAction = ControladorContexto
						.getContextBean(ScheduledActivitiesAction.class);
				activitiesAndMachineDao
						.editActivitiesAndMachine(this.activityMachine);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(registerMessage),
								activityMachine.getActivityMachinePK()
										.getMachines().getName()));
				scheduledActivitiesAction.showActivitiesAndMachineForActivity();
			}
			currentCost();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Register the total cost of an activity.
	 * 
	 * @modify 25/01/2016 Wilhelm.Boada
	 * 
	 */
	public void endActivity() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifecycle = ControladorContexto
				.getBundle("messageLifeCycle");
		String registerMessage = "message_calculate_labor_cost";
		boolean flag = false;
		try {
			if (ControladorContexto.getFacesContext() != null) {
				this.scheduledActivitiesAction = ControladorContexto
						.getContextBean(ScheduledActivitiesAction.class);
			}
			if (this.selectedActivity != null
					&& (this.listActivitiesAndHr != null || this.scheduledActivitiesAction
							.getListActivityMachine() != null)) {
				if (this.listActivitiesAndHr != null
						&& selectedActivity.getHrRequired() == true
						&& selectedActivity.getMachineRequired() == false) {
					Double totalCostHr = activitiesAndHrDao
							.totalCost(selectedActivity.getIdActivity());
					this.selectedActivity.setCostHrActual(totalCostHr);
					flag = true;
				}
				if (this.scheduledActivitiesAction.getListActivityMachine() != null
						&& selectedActivity.getMachineRequired() == true
						&& selectedActivity.getHrRequired() == false) {
					Double totalCostMachine = activitiesAndMachineDao
							.calculateTotalCostMachine(selectedActivity
									.getIdActivity());
					this.selectedActivity
							.setCostMachinesEqActual(totalCostMachine);
					flag = true;
				}
				if (this.listActivitiesAndHr != null
						&& this.scheduledActivitiesAction
								.getListActivityMachine() != null) {
					Double totalCostHr = activitiesAndHrDao
							.totalCost(selectedActivity.getIdActivity());
					this.selectedActivity.setCostHrActual(totalCostHr);
					Double totalCostMachine = activitiesAndMachineDao
							.calculateTotalCostMachine(selectedActivity
									.getIdActivity());
					this.selectedActivity
							.setCostMachinesEqActual(totalCostMachine);
					flag = true;
				}
				if (flag == true) {
					activitiesDao.editActivities(this.selectedActivity);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(registerMessage),
									selectedActivity.getActivityName()));
					initializeRecordActual();
				} else {
					ControladorContexto
							.mensajeInformacion(
									null,
									bundleLifecycle
											.getString("scheduled_activities_message_recursos"));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates that the length does not exceed the difference in dates.
	 * 
	 * @param context
	 *            : context of view.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : component value.
	 */
	public void validateCurrentDuration(FacesContext context,
			UIComponent toValidate, Object value) {
		String clientId = toValidate.getClientId(context);
		String modal = (String) toValidate.getAttributes().get("temp");
		boolean fromModal = (modal != null && "si".equals(modal)) ? true
				: false;
		Double duration = (Double) value;
		try {
			if (this.activitiesAndHr.getInitialDateTimeActual() != null
					&& this.activitiesAndHr.getFinalDateTimeActual() != null) {
				Double activityDuration = (Double) ControladorFechas
						.restarFechas(
								this.activitiesAndHr.getInitialDateTimeActual(),
								this.activitiesAndHr.getFinalDateTimeActual());
				if (duration >= 0) {
					if (duration.compareTo(activityDuration) > 0) {
						String message = "message_activity_duration";
						ControladorContexto.mensajeErrorEspecifico(clientId,
								message, "mensaje");
						((UIInput) toValidate).setValid(false);
					} else {
						int idHr = this.activitiesAndHr.getActivitiesAndHrPK()
								.getHr().getIdHr();
						activitiesAndHrAction
								.setActivitiesAndHr(activitiesAndHr);
						activitiesAndHrAction.setWorkHoursValid(true);
						activitiesAndHrAction.validateWorkLoad(duration, idHr,
								fromModal);
						if (!activitiesAndHrAction.isWorkHoursValid()) {
							String message = "message_overtime_week";
							ControladorContexto.mensajeErrorEspecifico(
									clientId, message, "mensaje");
							((UIInput) toValidate).setValid(false);
						}
					}
				} else {
					String message = "message_greater_equal_cero";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							message, "mensaje");
					((UIInput) toValidate).setValid(false);
				}
			} else {
				String message = "message_duration_date";
				ControladorContexto.mensajeErrorEspecifico(clientId, message,
						"mensaje");
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates that the length does not exceed the difference in dates.
	 * 
	 * @param context
	 *            : context of view.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : component value.
	 */
	public void validateCurrentActivityAndMachine(FacesContext context,
			UIComponent toValidate, Object value) {
		String clientId = toValidate.getClientId(context);
		Double duration = (Double) value;
		Date startDate = this.activityMachine.getInitialDateTime();
		Date endFate = this.activityMachine.getFinalDateTime();
		try {
			if (startDate != null && endFate != null) {
				Double durationActivity = (Double) ControladorFechas
						.restarFechas(startDate, endFate);
				if (durationActivity >= 0) {
					if (duration.compareTo(durationActivity) > 0) {
						String message = "message_activity_duration";
						ControladorContexto.mensajeErrorEspecifico(clientId,
								message, "mensaje");
						((UIInput) toValidate).setValid(false);
					}
				} else {
					String message = "message_greater_equal_cero";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							message, "mensaje");
					((UIInput) toValidate).setValid(false);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It will calculate the length according to the two different dates.
	 */
	public void calculateCurrentDuration() {
		Date inicial = activitiesAndHr.getInitialDateTimeActual();
		Date fin = activitiesAndHr.getFinalDateTimeActual();

		if (inicial != null && fin != null) {
			double durationActual = ControladorFechas
					.restarFechas(inicial, fin);
			double hourCost = activitiesAndHr.getActivitiesAndHrPK().getHr()
					.getHourCost();
			double totalCost = durationActual * hourCost;
			if (durationActual > 8) {
				totalCost = calculateCostOvertime(durationActual);
			}
			activitiesAndHr.setDurationActual(durationActual);
			activitiesAndHr
					.setTotalCostActual(Math.round(totalCost * 10.0) / 10.0);
		}
	}

	/**
	 * Calculates the cost of HR given the overtimePaymentRate.
	 */
	public void updateTotalCostByOvertimePaymentRate() {
		try {
			this.overtimePaymentRate = overtimePaymentRateDao
					.overtimePaymentRateXId(idOvertimePaymentsRate);
			double currentDuration = activitiesAndHr.getDurationActual();
			double hourCost = activitiesAndHr.getActivitiesAndHrPK().getHr()
					.getHourCost();
			double totalCost = currentDuration * hourCost;
			if (currentDuration > 8) {
				totalCost = calculateCostOvertime(currentDuration);
			}
			activitiesAndHr
					.setTotalCostActual(Math.round(totalCost * 10.0) / 10.0);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * the Hr cost is calculated taking into account overtime.
	 * 
	 * @param currentDuration
	 *            : Activity duration.
	 * @return total: Hr total cost.
	 */
	private double calculateCostOvertime(double currentDuration) {
		double normalHours = activitiesAndHr.getActivitiesAndHrPK().getHr()
				.getHourCost();
		double costNormal = activitiesAndHr.getNormalHours() * normalHours;
		double overtimeHours = activitiesAndHr.getActivitiesAndHrPK().getHr()
				.getHourCostOvertime();
		double costOvertime = activitiesAndHr.getOvertimeHours()
				* overtimeHours * overtimePaymentRate.getOvertimeRateRatio();
		double totalCost = Math.round((costNormal + costOvertime) * 10.0) / 10.0;
		return totalCost;
	}

	/**
	 * Add the max hour and min hour in the end date and initial date.
	 */
	public void validateMaxDate() {
		this.maxDate = activitiesAndHr.getFinalDateTimeBudget();
		this.minDate = activitiesAndHr.getInitialDateTimeBudget();
		maxDate = ControladorFechas.finDeDia(maxDate);
		minDate = ControladorFechas.inicioDeDia(minDate);
		this.overtimePaymentRate = activitiesAndHr.getOvertimePaymentRate();
		setIdOvertimePaymentsRate(overtimePaymentRate.getOvertimepaymentid());
	}

	/**
	 * Calculate the cost of consumption of the machine.
	 * 
	 * @param activityMachine
	 *            : activityMachine object.
	 */
	public void calculateConsumableCost(ActivityMachine activityMachine) {
		Double fuelConsumption = activityMachine.getActivityMachinePK()
				.getMachines().getFuelConsumption();
		if (fuelConsumption > 0) {
			Double consumableCostActual = activityMachine.getDurationActual()
					* fuelConsumption;
			this.activityMachine.setConsumablesCostActual(consumableCostActual);
		}
	}

}
