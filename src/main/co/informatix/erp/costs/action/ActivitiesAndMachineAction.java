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

import co.informatix.erp.costs.dao.ActivitiesAndMachineDao;
import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.costs.entities.ActivityMachinePK;
import co.informatix.erp.lifeCycle.action.RecordActivitiesActualsAction;
import co.informatix.erp.lifeCycle.dao.CycleDao;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.ControllerAccounting;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the relationship between the activities and
 * machines in the database. The logic is to record the relationship and
 * machines activities.
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAndMachineAction implements Serializable {

	@EJB
	private MachineTypesDao machineTypesDao;
	@EJB
	private ActivitiesAndMachineDao activitiesAndMachineDao;
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private CycleDao cycleDao;
	@Resource
	private UserTransaction userTransaction;

	private boolean fromModal = false;
	private boolean stateAddMachine = false;

	private Paginador pagination = new Paginador();
	private Activities selectedActivity;
	private Machines machine;
	private ActivityMachine activityMachine;

	private List<ActivityMachine> listActivityMachineTemp;
	private List<ActivityMachine> listActivityMachine;
	private List<SelectItem> itemsMachineTypes;

	/**
	 * @return fromModal: Flag from detected if this class is called from
	 *         differents views
	 */
	public boolean isFromModal() {
		return fromModal;
	}

	/**
	 * @param fromModal
	 *            :Flag from detected if this class is called from differents
	 *            views
	 */
	public void setFromModal(boolean fromModal) {
		this.fromModal = fromModal;
	}

	/**
	 * @return stateAddMachine: This state detected if the machine is going to
	 *         add or remove
	 */
	public boolean isStateAddMachine() {
		return stateAddMachine;
	}

	/**
	 * @param stateAddMachine
	 *            : This state detected if the machine is going to add or remove
	 */
	public void setStateAddMachine(boolean stateAddMachine) {
		this.stateAddMachine = stateAddMachine;
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
	 * @return selectedActivity: Activity selected for operations
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            : Activity selected for operations
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return machine: Machine object
	 */
	public Machines getMachine() {
		return machine;
	}

	/**
	 * @param machine
	 *            : Machine object
	 */
	public void setMachine(Machines machine) {
		this.machine = machine;
	}

	/**
	 * @return activityMachine: Its a relation between activities and machines
	 */
	public ActivityMachine getActivityMachine() {
		return activityMachine;
	}

	/**
	 * @param activityMachine
	 *            : Its a relation between activities and machines
	 */
	public void setActivityMachine(ActivityMachine activityMachine) {
		this.activityMachine = activityMachine;
	}

	/**
	 * @return listActivityMachineTemp: Relation list between activities and
	 *         machines temporal
	 */
	public List<ActivityMachine> getListActivityMachineTemp() {
		return listActivityMachineTemp;
	}

	/**
	 * @param listActivityMachineTemp
	 *            : Relation list between activities and machines temporal
	 */
	public void setListActivityMachineTemp(
			List<ActivityMachine> listActivityMachineTemp) {
		this.listActivityMachineTemp = listActivityMachineTemp;
	}

	/**
	 * @return listActivityMachine: Relation list between activities and
	 *         machines
	 */
	public List<ActivityMachine> getListActivityMachine() {
		return listActivityMachine;
	}

	/**
	 * @param listActivityMachine
	 *            : Relation list between activities and machines
	 */
	public void setListActivityMachine(List<ActivityMachine> listActivityMachine) {
		this.listActivityMachine = listActivityMachine;
	}

	/**
	 * @return itemsMachineTypes: Diferents machine types
	 */
	public List<SelectItem> getItemsMachineTypes() {
		return itemsMachineTypes;
	}

	/**
	 * @param itemsMachineTypes
	 *            : Diferents machine types
	 */
	public void setItemsMachineTypes(List<SelectItem> itemsMachineTypes) {
		this.itemsMachineTypes = itemsMachineTypes;
	}

	/**
	 * Method that loads a machinesType list.
	 */
	public void listMachineTypes() {
		try {
			this.listActivityMachineTemp = new ArrayList<ActivityMachine>();
			this.itemsMachineTypes = new ArrayList<SelectItem>();
			List<MachineTypes> listMachineType = machineTypesDao
					.listMachineType();
			if (listMachineType != null) {
				for (MachineTypes machineType : listMachineType) {
					itemsMachineTypes.add(new SelectItem(machineType
							.getIdMachineType(), machineType.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Check the relations between activities and human resources.
	 * 
	 */
	public void showActivitiesAndMachineForActivity() {
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		String SearchMessage = "";
		try {
			if (fromModal) {
				RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
						.getContextBean(RecordActivitiesActualsAction.class);
				this.selectedActivity = recordActivitiesActualsAction
						.getSelectedActivity();
			}
			advancedSearchActivitiesAndMachine(queryBuilder, parameters);
			Long amount = activitiesAndMachineDao.quantityActivitiesAndMachine(
					queryBuilder, parameters);
			if (amount != null) {
				if (amount > 5) {
					pagination.paginarRangoDefinido(amount, 5);
				} else {
					pagination.paginar(amount);
				}
				this.listActivityMachine = activitiesAndMachineDao
						.consultingActivitiesAndMachine(pagination.getInicio(),
								pagination.getRango(), queryBuilder, parameters);
			}
			validation.setMensajeBusqueda(SearchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method builds the query for an advanced search for relations between
	 * activities and machines, it also builds display messages depending on the
	 * search criteria selected by the user.
	 * 
	 * @param query
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of the search parameters.
	 */
	private void advancedSearchActivitiesAndMachine(StringBuilder query,
			List<SelectItem> parameters) {
		query.append("WHERE ac.idActivity = :id ");
		SelectItem item = new SelectItem(this.selectedActivity.getIdActivity(),
				"id");
		parameters.add(item);
	}

	/**
	 * Select and deselect the machines in a list.
	 * 
	 * @param machine
	 *            : Machine object.
	 */
	public void machineSelection(Machines machine) {
		try {
			this.stateAddMachine = false;
			this.machine = machine;
			if (!machine.isSelection()) {
				this.activityMachine = new ActivityMachine();
				if (this.selectedActivity.getDurationBudget() != null) {
					this.activityMachine
							.setDurationBudget(this.selectedActivity
									.getDurationBudget());
				} else {
					Date initialDate = this.selectedActivity
							.getInitialDtBudget();
					Date finalDate = this.selectedActivity.getFinalDtBudget();
					Double duration = ControladorFechas.restarFechas(
							initialDate, finalDate);
					this.activityMachine.setDurationBudget(duration);
				}
			} else {
				ActivityMachine activityMachine = new ActivityMachine();
				ActivityMachinePK activitiesMachinesPK = new ActivityMachinePK();
				activitiesMachinesPK.setMachines(machine);
				activitiesMachinesPK.setActivities(this.selectedActivity);
				for (ActivityMachine activityMachines : listActivityMachineTemp) {
					if (activityMachines.getActivityMachinePK().equals(
							activitiesMachinesPK)) {
						activityMachine = activityMachines;
					}
				}
				this.listActivityMachineTemp.remove(activityMachine);
				this.stateAddMachine = true;
				this.machine.setSelection(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Add machines and create activityMachine List.
	 */
	public void addMachines() {
		ActivityMachinePK activityMachinePK = new ActivityMachinePK();
		this.machine.setSelection(true);
		this.activityMachine.setInitialDateTime(selectedActivity
				.getInitialDtBudget());
		this.activityMachine.setFinalDateTime(selectedActivity
				.getFinalDtBudget());
		activityMachinePK.setActivities(selectedActivity);
		activityMachinePK.setMachines(this.machine);
		this.activityMachine.setActivityMachinePK(activityMachinePK);
		calculateConsumableCost(this.activityMachine);
		this.listActivityMachineTemp.add(this.activityMachine);
	}

	/**
	 * Calculate the cost of consumption of the machine.
	 * 
	 * @param activityMachine
	 *            : ActivityMachine object.
	 */
	public void calculateConsumableCost(ActivityMachine activityMachine) {
		Double fuelConsumption = activityMachine.getActivityMachinePK()
				.getMachines().getFuelConsumption();
		if (fuelConsumption > 0) {
			Double consumableCostBudget = ControllerAccounting.multiply(
					activityMachine.getDurationBudget(), fuelConsumption);
			this.activityMachine.setConsumablesCostBudget(consumableCostBudget);
		} else {
			this.activityMachine.setConsumablesCostBudget(0.0d);
		}
	}

	/**
	 * Save the relations between activities and machines.
	 */
	public void saveActivitiesAndMachine() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_guardar";
		Double costConsumableMachine = 0.0;
		try {
			userTransaction.begin();
			if (this.listActivityMachineTemp != null
					&& this.listActivityMachineTemp.size() > 0) {
				for (ActivityMachine activityMachine : listActivityMachineTemp) {
					activitiesAndMachineDao
							.saveActivitiesAndMachine(activityMachine);
					if (activityMachine.getConsumablesCostBudget() == null)
						activityMachine.setConsumablesCostBudget(0.0);
					costConsumableMachine += activityMachine
							.getConsumablesCostBudget();
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(registerMessage),
									activityMachine.getActivityMachinePK()
											.getMachines().getName()));
				}
				if (selectedActivity.getCostMachinesEqBudget() == null) {
					selectedActivity.setCostMachinesEqBudget(0.0);
				}
				costConsumableMachine += selectedActivity
						.getCostMachinesEqBudget();
				calculateCostBudgetActivity(costConsumableMachine);
				editCycleMachineBudget(costConsumableMachine);
				userTransaction.commit();
				setListActivityMachineTemp(null);
				showActivitiesAndMachineForActivity();
			}
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
	 * Calculate the general cost budget for the activity selected
	 * 
	 * @param costMachine
	 *            : New cost for cost budget of human resources
	 * 
	 * @throws Exception
	 */
	private void calculateCostBudgetActivity(double costMachine)
			throws Exception {
		double costActual = costMachine;
		if (selectedActivity.getGeneralCostBudget() != null
				&& selectedActivity.getGeneralCostBudget() > 0) {
			double costMachineActual = selectedActivity
					.getCostMachinesEqBudget();
			costActual = ControllerAccounting.subtract(
					selectedActivity.getGeneralCostBudget(), costMachineActual);
			costActual = ControllerAccounting.add(costActual, costMachine);
		}
		this.selectedActivity.setGeneralCostBudget(costActual);
		this.selectedActivity.setCostMachinesEqBudget(costMachine);
		this.activitiesDao.editActivities(this.selectedActivity);
	}

	/**
	 * Update the relations between activities and machines.
	 */
	public void updateActivityMachine() {
		try {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			String registerMessage = "message_registro_modificar";
			userTransaction.begin();
			ActivityMachine activityMachineTemp = activitiesAndMachineDao
					.activityMachineById(activityMachine.getActivityMachinePK());
			double costMachine = ControllerAccounting.subtract(
					selectedActivity.getCostMachinesEqBudget(),
					activityMachineTemp.getConsumablesCostBudget());
			costMachine = ControllerAccounting.add(costMachine,
					activityMachine.getConsumablesCostBudget());
			editCycleMachineBudget(costMachine);
			calculateCostBudgetActivity(costMachine);
			activitiesAndMachineDao
					.editActivitiesAndMachine(this.activityMachine);
			activitiesDao.editActivities(selectedActivity);
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), this.activityMachine
							.getActivityMachinePK().getMachines().getName()));
			showActivitiesAndMachineForActivity();
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
	 * Edit the budget cost for machines item into the cycle.
	 * 
	 * @param costConsumableMachine
	 *            : New budget cost for machines.
	 * @throws Exception
	 */
	private void editCycleMachineBudget(double costConsumableMachine)
			throws Exception {
		if (selectedActivity.getCycle() != null) {
			double costBudgetCycle = costConsumableMachine;
			if (selectedActivity.getCycle().getCostMachinesEqBudget() != null
					&& selectedActivity.getCycle().getCostMachinesEqBudget() > 0) {
				double lastCostMachineBudget = selectedActivity
						.getCostMachinesEqBudget();
				costBudgetCycle = ControllerAccounting.subtract(
						selectedActivity.getCycle().getCostMachinesEqBudget(),
						lastCostMachineBudget);
				costBudgetCycle = ControllerAccounting.add(costBudgetCycle,
						costConsumableMachine);
			}
			selectedActivity.getCycle()
					.setCostMachinesEqBudget(costBudgetCycle);
			cycleDao.editCycle(selectedActivity.getCycle());
		}
	}

	/**
	 * Delete the relations between activities and machines.
	 */
	public void deleteActivitiesAndMachine() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "message_registro_eliminar";
		try {
			userTransaction.begin();
			if (this.activityMachine.getConsumablesCostBudget() == null)
				this.activityMachine.setConsumablesCostBudget(0.0);
			double costMachineBudget = ControllerAccounting.subtract(
					this.selectedActivity.getCostMachinesEqBudget(),
					this.activityMachine.getConsumablesCostBudget());
			editCycleMachineBudget(costMachineBudget);
			calculateCostBudgetActivity(costMachineBudget);
			activitiesAndMachineDao
					.deleteActivitiesAndMachine(this.activityMachine);
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(message), activityMachine
							.getActivityMachinePK().getMachines().getName()));
			showActivitiesAndMachineForActivity();
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					this.activityMachine.getActivityMachinePK().getMachines()
							.getName());
			ControladorContexto.mensajeError(e, null, format);
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
	 * It is responsible for validating that the time duration entered does not
	 * exceed the total time of the activity.
	 * 
	 * @param context
	 *            : Context for the view.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 */
	public void validateDuration(FacesContext context, UIComponent toValidate,
			Object value) {
		String clientId = toValidate.getClientId(context);
		String flag = (String) toValidate.getAttributes().get("flag");
		boolean flagValue = (flag != null && "si".equals(flag)) ? true : false;
		Double duration = (Double) value;
		Double durationActivity = 0.0d;
		if (!flagValue) {
			durationActivity = (Double) ControladorFechas.restarFechas(
					selectedActivity.getInitialDtBudget(),
					selectedActivity.getFinalDtBudget());
		} else {
			durationActivity = (Double) ControladorFechas.restarFechas(
					activityMachine.getInitialDateTime(),
					activityMachine.getFinalDateTime());
		}
		if (duration != null && duration > 0) {
			if (duration.compareTo(durationActivity) > 0) {
				String message = "message_activity_duration";
				ControladorContexto.mensajeErrorEspecifico(clientId, message,
						"mensaje");
				((UIInput) toValidate).setValid(false);
			}
		} else {
			String message = "message_greater_zero";
			ControladorContexto.mensajeErrorEspecifico(clientId, message,
					"mensaje");
			((UIInput) toValidate).setValid(false);
		}
	}

	/**
	 * It will calculate the length considering the different two dates for an
	 * activity machine.
	 */
	public void calculateDuration() {
		try {
			Double durationBudget = ControladorFechas.restarFechas(
					activityMachine.getInitialDateTime(),
					activityMachine.getFinalDateTime());
			activityMachine.setDurationBudget(durationBudget);
			calculateConsumableCost(this.activityMachine);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}