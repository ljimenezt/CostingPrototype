package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.action.ActivitiesAction;
import co.informatix.erp.costs.action.ActivitiesAndHrAction;
import co.informatix.erp.costs.action.ActivitiesAndMachineAction;
import co.informatix.erp.costs.action.ActivityMaterialsAction;
import co.informatix.erp.costs.dao.ActivitiesAndMachineDao;
import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.CycleDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Cycle;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;

/**
 * This class implements the logic of activities that can be related to machines
 * and human resources in the database. The logic is: insert activities with one
 * particular machine and one human resource.
 * 
 * @author Gerardo.Herrera
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
	private ActivitiesDao activitiesDao;
	@EJB
	private ActivitiesAndMachineDao activitiesAndMachineDao;
	@EJB
	private MachineTypesDao machineTypesDao;
	@EJB
	private CycleDao cycleDao;

	private int idCrop;
	private int idCropName;
	private int idCycle;
	private boolean fromModal;
	private boolean fromActivity = false;
	private boolean flagStart;
	private boolean flagView = false;

	private List<Activities> listActivities;
	private List<SelectItem> optionsCropNames;
	private List<SelectItem> optionsCrops;
	private List<SelectItem> optionsActivityName;
	private List<SelectItem> optionsConsumables;

	private Activities activities;
	private Crops crops;
	private CropNames cropNames;
	private Activities selectedActivity;
	private ActivitiesAction activitiesAction;
	private ActivitiesAndMachineAction activitiesAndMachineAction;
	private ActivitiesAndHrAction activitiesAndHrAction;
	private ActivityMaterialsAction activityMaterialsAction;
	private Cycle cycle;

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
	 * @return idCycle : Cycle identifier to program
	 */
	public int getIdCycle() {
		return idCycle;
	}

	/**
	 * @param idCycle
	 *            : Cycle identifier to program
	 */
	public void setIdCycle(int idCycle) {
		this.idCycle = idCycle;
	}

	/**
	 * @return fromModal: this field is true if the query is made from
	 *         recordActivitiesActualsAction and is false in other case.
	 */
	public boolean isFromModal() {
		return fromModal;
	}

	/**
	 * @return fromActivity: this flag indicate if the view is actually in
	 *         record of search criteria
	 */
	public boolean isFromActivity() {
		return fromActivity;
	}

	/**
	 * @return flagView: Flag that indicate the view to show.
	 */
	public boolean isFlagView() {
		return flagView;
	}

	/**
	 * @param fromActivity
	 *            :this flag indicate if the view is actually in record of
	 *            search criteria
	 */
	public void setFromActivity(boolean fromActivity) {
		this.fromActivity = fromActivity;
	}

	/**
	 * @param fromModal
	 *            : this field is true if the query is made from
	 *            recordActivitiesActualsAction and is false in other case.
	 */
	public void setFromModal(boolean fromModal) {
		this.fromModal = fromModal;
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
	 * @return optionsCropNames: crop name associated with an activity.
	 */
	public List<SelectItem> getOptionsCropNames() {
		return optionsCropNames;
	}

	/**
	 * @param optionsCropNames
	 *            : crop name associated with an activity.
	 */
	public void setOptionsCropNames(List<SelectItem> optionsCropNames) {
		this.optionsCropNames = optionsCropNames;
	}

	/**
	 * @return optionsCrops: crop associated with an activity.
	 */
	public List<SelectItem> getOptionsCrops() {
		return optionsCrops;
	}

	/**
	 * @param optionsCrops
	 *            : crop associated with an activity.
	 */
	public void setOptionsCrops(List<SelectItem> optionsCrops) {
		this.optionsCrops = optionsCrops;
	}

	/**
	 * @return optionsActivityName: name of the activity associated with a crop.
	 */
	public List<SelectItem> getOptionsActivityName() {
		return optionsActivityName;
	}

	/**
	 * @param optionsActivityName
	 *            : name of the activity associated with a crop.
	 */
	public void setOptionsActivityName(List<SelectItem> optionsActivityName) {
		this.optionsActivityName = optionsActivityName;
	}

	/**
	 * @return optionsConsumables: Names of consumables.
	 */
	public List<SelectItem> getOptionsConsumables() {
		return optionsConsumables;
	}

	/**
	 * @param optionsConsumables
	 *            : Names of consumables.
	 */
	public void setOptionsConsumables(List<SelectItem> optionsConsumables) {
		this.optionsConsumables = optionsConsumables;
	}

	/**
	 * @return activities: Object of activities.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : Object of activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return crops: Object of crop.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : Object of crop.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return cropNames: Object of crop names.
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            : Object of crop names.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return selectedActivity: Object of activity that is selected.
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            : Object of activity that is selected.
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return activitiesAction: object of activitiesAction.
	 */
	public ActivitiesAction getActivitiesAction() {
		return activitiesAction;
	}

	/**
	 * @param activitiesAction
	 *            : object of activitiesAction.
	 */
	public void setActivitiesAction(ActivitiesAction activitiesAction) {
		this.activitiesAction = activitiesAction;
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
	 * @return cycle: Object of class cycle.
	 */
	public Cycle getCycle() {
		return cycle;
	}

	/**
	 * @param cycle
	 *            : Object of class cycle.
	 */
	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
		this.idCycle = this.cycle.getIdCycle();
	}

	/**
	 * Method to clean the object Cycle
	 * 
	 * @author Liseth.Jimenez
	 */
	public void cleanCycle() {
		this.cycle = new Cycle();
		this.idCycle = 0;
		ActivityNames activityNames = new ActivityNames();
		activityNames.setIdActivityName(0);
		this.cycle.setActiviyNames(activityNames);
	}

	/**
	 * Method to edit or create a new assignment of activities.
	 * 
	 * @modify 22/03/2016 Andres.Gomez
	 * @modify 20/06/2016 Liseth.Jimenez
	 * @modify 08/09/2016 Wilhelm.Boada
	 * @modify 28/11/2016 Claudia.Rey
	 * 
	 * @param flag
	 *            : indicates the view selected.
	 * @return scheduledActivities: Redirects to scheduled activities view.
	 */
	public String initializeActivities(boolean flag) {
		try {
			flagView = flag;
			this.activitiesAction = ControladorContexto
					.getContextBean(ActivitiesAction.class);
			eraseActivities();
			this.activities = new Activities();
			this.cropNames = new CropNames();
			if (this.activitiesAction != null) {
				this.activitiesAction.setListActivities(null);
				this.activitiesAction.setFlagCycle(false);
			}
			this.setSelectedActivity(null);
			this.crops = cropsDao.defaultSearchCrop(Constantes.ID_CROP_DEFAULT);
			if (crops != null) {
				idCropName = crops.getCropNames().getIdCropName();
				idCrop = crops.getIdCrop();
				showActivities();
			} else {
				idCrop = 0;
				idCropName = 0;
			}
			this.flagStart = false;
			loadCropNames();
			loadCropNamesCrop();
			cleanCycle();
			this.fromActivity = false;
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "scheduledActivities";
	}

	/**
	 * Method that loads a CropNames list.
	 * 
	 * @throws Exception
	 */
	private void loadCropNames() throws Exception {
		optionsCropNames = new ArrayList<SelectItem>();
		List<CropNames> listCropNames = cropNamesDao.listCropNames();
		if (listCropNames != null) {
			for (CropNames cropNames : listCropNames) {
				optionsCropNames.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}
	}

	/**
	 * Complete the list of crops harvested according to the selected name.
	 * 
	 * @modify 08/09/2016 Wilhelm.Boada
	 */
	public void loadCropNamesCrop() {
		try {
			if (this.flagStart) {
				idCrop = 0;
			}
			optionsCrops = new ArrayList<SelectItem>();
			List<Crops> listCropsVigentes = cropsDao
					.consultCropNamesCropsCurrent(idCropName);
			if (listCropsVigentes != null) {
				for (Crops crop : listCropsVigentes) {
					optionsCrops.add(new SelectItem(crop.getIdCrop(), crop
							.getDescription()));
				}
			}
			this.flagStart = true;
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to clean up the list of activities and the name of the crop.
	 */
	private void eraseActivities() {
		this.listActivities = new ArrayList<Activities>();
		this.cropNames = new CropNames();
	}

	/**
	 * Assigned selected activity.
	 * 
	 * @modify 06/30/2016 Wilhelm.Boada
	 */
	public void assignSelectedActivity() {
		this.activitiesAction = new ActivitiesAction();
		this.selectedActivity = new Activities();
		fromModal = false;
		if (ControladorContexto.getFacesContext() != null) {
			this.activitiesAction = ControladorContexto
					.getContextBean(ActivitiesAction.class);
			this.selectedActivity = activitiesAction.getSelectedActivities();
		}
	}

	/**
	 * To load the names of available activities associated with a crop in a
	 * list.
	 * 
	 * @modify 16/05/2017 Liseth.Jimenez
	 */
	public void showActivities() {
		try {
			this.activities = new Activities();
			optionsActivityName = new ArrayList<SelectItem>();
			List<ActivityNames> activityNameTypes = activityNamesDao
					.queryActivityNames();
			if (activityNameTypes != null) {
				for (ActivityNames activities : activityNameTypes) {
					optionsActivityName
							.add(new SelectItem(activities.getIdActivityName(),
									activities.getActivityName()));
				}
				if (ControladorContexto.getFacesContext() != null) {
					this.activitiesAction = ControladorContexto
							.getContextBean(ActivitiesAction.class);
					this.activitiesAction
							.setItemsActivities(optionsActivityName);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This Method allow set the activity selected by the user and show the
	 * resource associated to this activity.
	 * 
	 * @author Andres.Gomez
	 * @modify 13/07/2016 Gerardo.Herrera
	 * 
	 * @param activity
	 *            : Object Activities selected by the user
	 */
	public void selectedActivity(Activities activity) {
		this.activitiesAction = ControladorContexto
				.getContextBean(ActivitiesAction.class);
		activitiesAction.assignActivities(activity);
		assignSelectedActivity();
		RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
				.getContextBean(RecordActivitiesActualsAction.class);
		if (fromModal) {
			this.selectedActivity = recordActivitiesActualsAction
					.getSelectedActivity();
			recordActivitiesActualsAction.currentCost();
		}
		if (!fromModal) {
			initializeActivityResource();
		}
		setSelectedActivity(activity);
	}

	/**
	 * Initialize actions of the resources for the activty selected
	 */
	private void initializeActivityResource() {

		if (selectedActivity.getHrRequired() != null
				&& selectedActivity.getHrRequired()) {
			this.activitiesAndHrAction = ControladorContexto
					.getContextBean(ActivitiesAndHrAction.class);
			this.activitiesAndHrAction.setSelectedActivity(selectedActivity);
			this.activitiesAndHrAction.setFromModal(false);
			this.activitiesAndHrAction.consultActivitiesAndHrByActivity();
		}

		if (selectedActivity.getMachineRequired() != null
				&& selectedActivity.getMachineRequired()) {
			this.activitiesAndMachineAction = ControladorContexto
					.getContextBean(ActivitiesAndMachineAction.class);
			activitiesAndMachineAction.setFromModal(fromModal);
			activitiesAndMachineAction.setSelectedActivity(selectedActivity);
			activitiesAndMachineAction.showActivitiesAndMachineForActivity();
		}

		if (selectedActivity.getMaterialsRequired() != null
				&& selectedActivity.getMaterialsRequired()) {
			this.activityMaterialsAction = ControladorContexto
					.getContextBean(ActivityMaterialsAction.class);
			this.activityMaterialsAction.setSelectedActivity(selectedActivity);
			this.activityMaterialsAction.setFromModal(false);
			this.activityMaterialsAction.consultMaterialsByActivity();
		}
	}

}