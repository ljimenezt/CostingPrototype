package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.action.ActivitiesAction;
import co.informatix.erp.costs.action.ActivitiesAndHrAction;
import co.informatix.erp.costs.dao.ActivitiesAndMachineDao;
import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.costs.entities.ActivityMachinePK;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.machines.action.MachinesAction;
import co.informatix.erp.machines.dao.ConsumableTypesDao;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows you assign the logic of activities that can be related
 * machines and human resources in the database. The logic is: insert activities
 * one particular machine and one human resource.
 * 
 * @author Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ScheduledActivitiesAction implements Serializable {

	@EJB
	private CropsDao cropsDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private ActivityNamesDao activityNamesDao;
	@EJB
	private MachinesDao machinesDao;
	@EJB
	private ConsumableTypesDao consumableTypesDao;
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private ActivitiesAndMachineDao activitiesAndMachineDao;
	@EJB
	private MachineTypesDao machineTypesDao;

	private int idCrop;
	private int idCropName;
	private boolean stateAddMachine;

	private List<Activities> listActivities;
	private List<ActivityMachine> listActivityMachine;
	private List<ActivityMachine> listActivityMachineTemp;
	private List<Machines> listMachine;
	private List<SelectItem> itemsMachineTypes;
	private List<SelectItem> opcionesCropNames;
	private List<SelectItem> opcionesCrops;
	private List<SelectItem> opcionesActivityName;
	private List<SelectItem> opcionesConsumables;

	private Activities activities;
	private Crops crops;
	private CropNames cropNames;
	private ActivityMachine activityMachine;
	private Machines machine;
	private Activities selectedActivity;
	private ActivitiesAction activitiesAction;
	private MachinesAction machinesAction;
	private Paginador paginadorActivitiesMachines = new Paginador();
	private ActivitiesAndHrAction activitiesAndHrAction;

	/**
	 * @return idCrop: Crop identifier.
	 */
	public int getIdCrop() {
		return idCrop;
	}

	/**
	 * @param idCrop
	 *            : Crop identifier.
	 */
	public void setIdCrop(int idCrop) {
		this.idCrop = idCrop;
	}

	/**
	 * @return idCropName: Crop name identifier.
	 */
	public int getIdCropName() {
		return idCropName;
	}

	/**
	 * @param idCropName
	 *            : Crop name identifier.
	 */
	public void setIdCropName(int idCropName) {
		this.idCropName = idCropName;
	}

	/**
	 * @return stateAddMachine: 'true' show register duration popup, 'false'
	 *         delete machine of list.
	 */
	public boolean isStateAddMachine() {
		return stateAddMachine;
	}

	/**
	 * @param stateAddMachine
	 *            : 'true' show register duration popup, 'false' delete machine
	 *            of list.
	 */
	public void setStateAddMachine(boolean stateAddMachine) {
		this.stateAddMachine = stateAddMachine;
	}

	/**
	 * @return listActivities: List of activities.
	 */
	public List<Activities> getListActivities() {
		return listActivities;
	}

	/**
	 * @param listActivities
	 *            : List of activities.
	 */
	public void setListActivities(List<Activities> listActivities) {
		this.listActivities = listActivities;
	}

	/**
	 * @return listActivityMachine: list object of ActivityMachine
	 */
	public List<ActivityMachine> getListActivityMachine() {
		return listActivityMachine;
	}

	/**
	 * @param listActivityMachine
	 *            : list object of ActivityMachine
	 */
	public void setListActivityMachine(List<ActivityMachine> listActivityMachine) {
		this.listActivityMachine = listActivityMachine;
	}

	/**
	 * @return listActivityMachineTemp: list object of ActivityMachine
	 */
	public List<ActivityMachine> getListActivityMachineTemp() {
		return listActivityMachineTemp;
	}

	/**
	 * @param listActivityMachineTemp
	 *            : list object of ActivityMachine
	 */
	public void setListActivityMachineTemp(
			List<ActivityMachine> listActivityMachineTemp) {
		this.listActivityMachineTemp = listActivityMachineTemp;
	}

	/**
	 * @return listMachine: object machine list
	 */
	public List<Machines> getListMachine() {
		return listMachine;
	}

	/**
	 * @param listMachine
	 *            : object machine list
	 */
	public void setListMachine(List<Machines> listMachine) {
		this.listMachine = listMachine;
	}

	/**
	 * @return itemsMachineTypes: selectedItem list type machine types
	 */
	public List<SelectItem> getItemsMachineTypes() {
		return itemsMachineTypes;
	}

	/**
	 * @param itemsMachineTypes
	 *            : selectedItem list type machine types
	 */
	public void setMachineTypes(List<SelectItem> itemsMachineTypes) {
		this.itemsMachineTypes = itemsMachineTypes;
	}

	/**
	 * @return opcionesCropNames: crop name associated with an activity.
	 */
	public List<SelectItem> getOpcionesCropNames() {
		return opcionesCropNames;
	}

	/**
	 * @param opcionesCropNames
	 *            : crop name associated with an activity.
	 */
	public void setOpcionesCropNames(List<SelectItem> opcionesCropNames) {
		this.opcionesCropNames = opcionesCropNames;
	}

	/**
	 * @return opcionesCrops: crop associated with an activity.
	 */
	public List<SelectItem> getOpcionesCrops() {
		return opcionesCrops;
	}

	/**
	 * @param opcionesCrops
	 *            : crop associated with an activity.
	 */
	public void setOpcionesCrops(List<SelectItem> opcionesCrops) {
		this.opcionesCrops = opcionesCrops;
	}

	/**
	 * @return opcionesActivityName: name of the activity associated with a
	 *         crop.
	 */
	public List<SelectItem> getOpcionesActivityName() {
		return opcionesActivityName;
	}

	/**
	 * @param opcionesActivityName
	 *            : name of the activity associated with a crop.
	 */
	public void setOpcionesActivityName(List<SelectItem> opcionesActivityName) {
		this.opcionesActivityName = opcionesActivityName;
	}

	/**
	 * @return opcionesConsumables: name consumable.
	 */
	public List<SelectItem> getOpcionesConsumables() {
		return opcionesConsumables;
	}

	/**
	 * @param opcionesConsumables
	 *            : name consumable.
	 */
	public void setOpcionesConsumables(List<SelectItem> opcionesConsumables) {
		this.opcionesConsumables = opcionesConsumables;
	}

	/**
	 * @return activities: Object of class activities.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : Object of class activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return crops: Object of class crop.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : Object of class crop.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return cropNames: Object of the class name of the crop.
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            : Object of the class name of the crop.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return activityMachine: Object class machine activity.
	 */
	public ActivityMachine getActivityMachine() {
		return activityMachine;
	}

	/**
	 * @param activityMachine
	 *            : Object class machine activity.
	 */
	public void setActivityMachine(ActivityMachine activityMachine) {
		this.activityMachine = activityMachine;
	}

	/**
	 * @return machine: Object of machine class.
	 */
	public Machines getMachine() {
		return machine;
	}

	/**
	 * @param machine
	 *            : Object of machine class.
	 */
	public void setMachine(Machines machine) {
		this.machine = machine;
	}

	/**
	 * @return selectedActivity: object activity selected
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            : object activity selected
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return activitiesAction: object activitiesAction
	 */
	public ActivitiesAction getActivitiesAction() {
		return activitiesAction;
	}

	/**
	 * @param activitiesAction
	 *            : object activitiesAction
	 */
	public void setActivitiesAction(ActivitiesAction activitiesAction) {
		this.activitiesAction = activitiesAction;
	}

	/**
	 * @return machinesAction: object machinesAction
	 */
	public MachinesAction getMachinesAction() {
		return machinesAction;
	}

	/**
	 * @param machinesAction
	 *            : object machinesAction
	 */
	public void setMachinesAction(MachinesAction machinesAction) {
		this.machinesAction = machinesAction;
	}

	/**
	 * @return paginadorActivitiesMachines: Management paged list of
	 *         activityMachines.
	 */
	public Paginador getPaginadorActivitiesMachines() {
		return paginadorActivitiesMachines;
	}

	/**
	 * @param paginadorActivitiesMachines
	 *            : Management paged list of activityMachines.
	 */
	public void setPaginadorActivitiesMachines(
			Paginador paginadorActivitiesMachines) {
		this.paginadorActivitiesMachines = paginadorActivitiesMachines;
	}

	/**
	 * @return activitiesAndHrAction: ActivitiesAndHrAction object
	 */
	public ActivitiesAndHrAction getActivitiesAndHrAction() {
		return activitiesAndHrAction;
	}

	/**
	 * @param activitiesAndHrAction
	 *            : ActivitiesAndHrAction object
	 */
	public void setActivitiesAndHrAction(
			ActivitiesAndHrAction activitiesAndHrAction) {
		this.activitiesAndHrAction = activitiesAndHrAction;
	}

	/**
	 * Method to edit or create a new assignment of activities
	 * 
	 * @return activitiesAndMachine: Redirects view of machine activities
	 */
	public String initializeActivities() {
		try {
			if (ControladorContexto.getFacesContext() != null) {
				this.activitiesAction = ControladorContexto
						.getContextBean(ActivitiesAction.class);
			}
			limpiarActivities();
			this.crops = new Crops();
			this.activities = new Activities();
			this.cropNames = new CropNames();
			this.machine = new Machines();
			if (this.activitiesAction != null)
				this.activitiesAction.setListActivities(null);
			this.machinesAction = new MachinesAction();
			this.setSelectedActivity(null);
			crops = cropsDao.descriptionSearch(Constantes.COSECHA);
			if (crops != null) {
				idCropName = crops.getCropNames().getIdCropName();
				idCrop = crops.getIdCrop();
				showActivities();
			} else {
				idCrop = 0;
				idCropName = 0;
			}
			llenarCropNames();
			llenarCropNamesCrop();

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "scheduledActivities";
	}

	/**
	 * Method that loads a CropNames list
	 * 
	 * @throws Exception
	 */
	private void llenarCropNames() throws Exception {
		opcionesCropNames = new ArrayList<SelectItem>();
		List<CropNames> listCropNames = cropNamesDao.listCropNames();
		if (listCropNames != null) {
			for (CropNames cropNames : listCropNames) {
				opcionesCropNames.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}
	}

	/**
	 * It allows complete the list of crops harvested according to the selected
	 * name.
	 */
	public void llenarCropNamesCrop() {
		try {
			opcionesCrops = new ArrayList<SelectItem>();
			List<Crops> listaCropsVigentes = cropsDao
					.consultarCropNamesCropsVigentes(idCropName);
			if (listaCropsVigentes != null) {
				for (Crops crops : listaCropsVigentes) {
					opcionesCrops.add(new SelectItem(crops.getIdCrop(), crops
							.getDescription()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that loads a machinesType list
	 * 
	 */
	public void listMachineTypes() {
		try {
			this.listActivityMachineTemp = new ArrayList<ActivityMachine>();
			this.itemsMachineTypes = new ArrayList<SelectItem>();
			List<MachineTypes> listMachineType = machineTypesDao
					.listaMachineType();
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
	 * Method to clean up the list of activities and the name of the crop
	 */
	private void limpiarActivities() {
		this.listActivities = new ArrayList<Activities>();
		this.cropNames = new CropNames();
	}

	/**
	 * Assign selected activity
	 * 
	 */
	public void assignSelectedActivity() {
		this.activitiesAction = new ActivitiesAction();
		this.selectedActivity = new Activities();
		if (ControladorContexto.getFacesContext() != null) {
			this.activitiesAction = ControladorContexto
					.getContextBean(ActivitiesAction.class);
			this.selectedActivity = activitiesAction.getSelectedActivities();
		}
	}

	/**
	 * gets the list of machines machineAction
	 * 
	 */
	public void initializeMachine() {
		this.listActivities = new ArrayList<Activities>();
		if (ControladorContexto.getFacesContext() != null) {
			machinesAction = ControladorContexto
					.getContextBean(MachinesAction.class);
			this.listMachine = machinesAction.getListaMachines();
		}
	}

	/**
	 * To load the names of available activities associated with a crop in a
	 * list.
	 * 
	 */
	public void showActivities() {
		try {
			this.activities = new Activities();
			opcionesActivityName = new ArrayList<SelectItem>();
			List<ActivityNames> tiposActivityNames = activityNamesDao
					.consultarActivityNamesXCrop(idCrop);
			if (tiposActivityNames != null) {
				for (ActivityNames activities : tiposActivityNames) {
					opcionesActivityName
							.add(new SelectItem(activities.getIdActivityName(),
									activities.getActivityName()));
				}
				if (ControladorContexto.getFacesContext() != null) {
					this.activitiesAction = ControladorContexto
							.getContextBean(ActivitiesAction.class);
					this.activitiesAction
							.setItemsActivities(opcionesActivityName);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Check the relationship between activities and human resources
	 * 
	 */
	public void showActivitiesAndMachineForActivity() {
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listActivityMachine = new ArrayList<ActivityMachine>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		String SearchMessage = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		try {
			RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
					.getContextBean(RecordActivitiesActualsAction.class);
			if (fromModal) {
				this.selectedActivity = recordActivitiesActualsAction
						.getSelectedActivity();
			}
			busquedaAvanzadaActivitiesAndMachine(query, parameters);
			Long cantidad = activitiesAndMachineDao
					.quantityActivitiesAndMachine(query, parameters);
			busquedaAvanzadaActivitiesAndMachine(query, parameters);
			if (cantidad != null) {
				if (cantidad > 5) {
					paginadorActivitiesMachines.paginarRangoDefinido(cantidad,
							5);
				} else {
					paginadorActivitiesMachines.paginar(cantidad);
				}
				this.listActivityMachine = activitiesAndMachineDao
						.consultingActivitiesAndMachine(
								paginadorActivitiesMachines.getInicio(),
								paginadorActivitiesMachines.getRango(), query,
								parameters);
			}
			if (!fromModal) {
				if (ControladorContexto.getFacesContext() != null) {
					this.activitiesAndHrAction = ControladorContexto
							.getContextBean(ActivitiesAndHrAction.class);
					this.activitiesAndHrAction
							.setActividadSeleccionada(selectedActivity);
					this.activitiesAndHrAction
							.consultarActivitiesAndHrXActividad();
				}
			} else {
				recordActivitiesActualsAction.actualCost();
			}
			validaciones.setMensajeBusqueda(SearchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search for relationship
	 * between activities and machines, also it allows messages to build show
	 * depending on the search criteria selected by the user.
	 * 
	 * 
	 * @param query
	 *            : query to concatenate
	 * @param parameters
	 *            : list of the search parameters.
	 */
	private void busquedaAvanzadaActivitiesAndMachine(StringBuilder query,
			List<SelectItem> parameters) {
		boolean selection = false;
		if (query.length() > 0) {
			query.setLength(0);
			selection = true;
		}
		query.append(selection ? "JOIN FETCH " : "JOIN ");
		query.append("am.activityMachinePK.machines m ");
		query.append(selection ? "JOIN FETCH " : "JOIN ");
		query.append("am.activityMachinePK.activities ac ");
		query.append("WHERE ac.idActivity = :id ");
		SelectItem item = new SelectItem(this.selectedActivity.getIdActivity(),
				"id");
		parameters.add(item);
	}

	/**
	 * Select and deselect the machines in a list
	 * 
	 * @param machine
	 *            : Machine object.
	 */
	public void machineSelection(Machines machine) {
		try {
			this.stateAddMachine = false;
			this.machine = machine;
			this.activityMachine = new ActivityMachine();
			if (!machine.isSelection()) {
				this.activityMachine.setDurationBudget(this.activitiesAction
						.getSelectedActivities().getDurationBudget());
			} else {
				ActivityMachine activityMachine = new ActivityMachine();
				ActivityMachinePK activitiesMachinesPK = new ActivityMachinePK();
				activitiesMachinesPK.setMachines(machine);
				activitiesMachinesPK.setActivities(this.activitiesAction
						.getSelectedActivities());
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
		this.activityMachine.setInitialDateTime(activitiesAction
				.getSelectedActivities().getInitialDtBudget());
		this.activityMachine.setFinalDateTime(activitiesAction
				.getSelectedActivities().getFinalDtBudget());
		activityMachinePK.setActivities(activitiesAction
				.getSelectedActivities());
		activityMachinePK.setMachines(this.machine);
		this.activityMachine.setActivityMachinePK(activityMachinePK);
		calculateConsumableCost(this.activityMachine);
		this.listActivityMachineTemp.add(this.activityMachine);
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
			Double consumableCostBudget = activityMachine.getDurationBudget()
					* fuelConsumption;
			this.activityMachine.setConsumablesCostBudget(consumableCostBudget);
		} else {
			this.activityMachine.setConsumablesCostBudget(0.0d);
		}
	}

	/**
	 * Save the relationship between activities and machines.
	 * 
	 */
	public void saveActivitiesAndMachine() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_guardar";
		Double costConsumableMachine = 0.0;
		try {
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
							.format(bundle.getString(mensajeRegistro),
									activityMachine.getActivityMachinePK()
											.getMachines().getName()));
				}
				if (selectedActivity.getCostMachinesEqBudget() == null) {
					selectedActivity.setCostMachinesEqBudget(0.0);
				}
				costConsumableMachine += selectedActivity
						.getCostMachinesEqBudget();
				selectedActivity.setCostMachinesEqBudget(costConsumableMachine);
				activitiesDao.editarActivities(this.selectedActivity);
				setListActivityMachineTemp(null);
				showActivitiesAndMachineForActivity();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Update the relationship between activities and machines.
	 */
	public void updateActivityMachine() {
		try {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			String mensajeRegistro = "message_registro_modificar";
			activitiesAndMachineDao
					.editActivitiesAndMachine(this.activityMachine);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), this.activityMachine
							.getActivityMachinePK().getMachines().getName()));
			showActivitiesAndMachineForActivity();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Delete the relationship between activities and machines.
	 * 
	 */
	public void deleteActivitiesAndMachine() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "message_registro_eliminar";
		try {
			activitiesAndMachineDao
					.deleteActivitiesAndMachine(this.activityMachine);
			if (this.activityMachine.getConsumablesCostBudget() == null)
				this.activityMachine.setConsumablesCostBudget(0.0);
			Double costMachineBudget = this.selectedActivity
					.getCostMachinesEqBudget()
					- this.activityMachine.getConsumablesCostBudget();
			this.selectedActivity.setCostMachinesEqBudget(costMachineBudget);
			this.activitiesDao.editarActivities(this.selectedActivity);
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
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for validating that the time duration entered does not
	 * exceed the total time of the activity.
	 * 
	 * @param context
	 *            : Context of sight.
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
					activitiesAction.getSelectedActivities()
							.getInitialDtBudget(), activitiesAction
							.getSelectedActivities().getFinalDtBudget());
		} else {
			durationActivity = (Double) ControladorFechas.restarFechas(
					activityMachine.getInitialDateTime(),
					activityMachine.getFinalDateTime());
		}
		if (duration > 0 && duration != null) {
			if (duration.compareTo(durationActivity) > 0) {
				String message = "message_duracion_actividad";
				ControladorContexto.mensajeErrorEspecifico(clientId, message,
						"mensaje");
				((UIInput) toValidate).setValid(false);
			}
		} else {
			String mensaje = "message_duration_mayor_cero";
			ControladorContexto.mensajeErrorEspecifico(clientId, mensaje,
					"mensaje");
			((UIInput) toValidate).setValid(false);
		}
	}

	/**
	 * It will calculate the length considering the difference two dates for
	 * activity machine
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