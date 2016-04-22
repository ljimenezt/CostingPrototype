package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.dao.CycleStandardActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.CycleStandardActivities;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows you to assign the logic of activities that may be related
 * to crops in the database. The logic is: insert activities to a particular
 * crop.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CropActivitiesAction implements Serializable {
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private CropsDao cropsDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private ActivityNamesDao activityNamesDao;
	@EJB
	private CycleStandardActivitiesDao cycleStandardActivitiesDao;

	private List<Activities> listActivities;
	private List<ActivityNames> listActivityNames;
	private List<CycleStandardActivities> listCycleStandardActivities;
	private List<SelectItem> optionsCropNames;
	private List<SelectItem> optionsCrops;
	private List<Activities> subListActivities;
	private List<CycleStandardActivities> subListCycleStandardActivities;

	private Paginador pagination = new Paginador();
	private Paginador paginationActivities = new Paginador();
	private Paginador paginationActivitiesStandardCycle = new Paginador();
	private Paginador paginationListActivities = new Paginador();

	private Activities activities;
	private Activities removeList;
	private ActivityNames activityNames;
	private Crops crops;
	private CropNames cropNames;
	private CycleStandardActivities cycleStandardActivities;
	private CycleStandardActivities removeStandard;

	private String nameSearch;
	private String messageCrumb;

	private Date maxDate;
	private Date initialDateSearch;
	private Date finalDateSearch;

	private int idCropNamesSearch;
	private int idCropName;

	private boolean clean;

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
	 * @return listActivityNames: List name of the activity.
	 */
	public List<ActivityNames> getListActivityNames() {
		return listActivityNames;
	}

	/**
	 * @param listActivityNames
	 *            :List name of the activity.
	 */
	public void setListActivityNames(List<ActivityNames> listActivityNames) {
		this.listActivityNames = listActivityNames;
	}

	/**
	 * @return listCycleStandardActivities: List of standard cycle activities.
	 */
	public List<CycleStandardActivities> getListCycleStandardActivities() {
		return listCycleStandardActivities;
	}

	/**
	 * @param listCycleStandardActivities
	 *            : List of standard cycle activities.
	 */
	public void setListCycleStandardActivities(
			List<CycleStandardActivities> listCycleStandardActivities) {
		this.listCycleStandardActivities = listCycleStandardActivities;
	}

	/**
	 * @return optionsCropNames: crop name associated with an activity.
	 */
	public List<SelectItem> getOptionsCropNames() {
		return optionsCropNames;
	}

	/**
	 * @param optionsCropNames
	 *            :crop name associated with an activity.
	 */
	public void setOptionsCropNames(List<SelectItem> optionsCropNames) {
		this.optionsCropNames = optionsCropNames;
	}

	/**
	 * @return optionsCrops: cultivation associated with an activity.
	 */
	public List<SelectItem> getOptionsCrops() {
		return optionsCrops;
	}

	/**
	 * @param optionsCrops
	 *            :cultivation associated with an activity.
	 */
	public void setOptionsCrops(List<SelectItem> optionsCrops) {
		this.optionsCrops = optionsCrops;
	}

	/**
	 * @return subListActivities: list of activities that stores a sublist for
	 *         managing Pager
	 */
	public List<Activities> getSubListActivities() {
		return subListActivities;
	}

	/**
	 * @param subListActivities
	 *            : list of activities that stores a sublist for managing Pager.
	 */
	public void setSubListActivities(List<Activities> subListActivities) {
		this.subListActivities = subListActivities;
	}

	/**
	 * @return subListCycleStandardActivities: list of cycle standard activities
	 *         that stores a sublist for managing Pager.
	 */
	public List<CycleStandardActivities> getSubListCycleStandardActivities() {
		return subListCycleStandardActivities;
	}

	/**
	 * @param subListCycleStandardActivities
	 *            : list of cycle standard activities that stores a sublist for
	 *            managing Pager.
	 */
	public void setSubListCycleStandardActivities(
			List<CycleStandardActivities> subListCycleStandardActivities) {
		this.subListCycleStandardActivities = subListCycleStandardActivities;
	}

	/**
	 * @return pagination: Management paged list of activity names.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of activity names.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationActivities: Management paged list of activities.
	 */
	public Paginador getPaginationActivities() {
		return paginationActivities;
	}

	/**
	 * @param paginationActivities
	 *            : Management paged list of activities.
	 */
	public void setPaginationActivities(Paginador paginationActivities) {
		this.paginationActivities = paginationActivities;
	}

	/**
	 * @return paginationActivitiesStandardCycle: Management paged list of
	 *         activities standard cycle.
	 */
	public Paginador getpaginationActivitiesStandardCycle() {
		return paginationActivitiesStandardCycle;
	}

	/**
	 * @param paginationActivitiesStandardCycle
	 *            : Management paged list of activities standard cycle.
	 */
	public void setpaginationActivitiesStandardCycle(
			Paginador paginationActivitiesStandardCycle) {
		this.paginationActivitiesStandardCycle = paginationActivitiesStandardCycle;
	}

	/**
	 * @return paginationListActivities: Management paged list of activities.
	 * 
	 */
	public Paginador getPaginationListActivities() {
		return paginationListActivities;
	}

	/**
	 * @param paginationListActivities
	 *            :Management paged list of activities.
	 */
	public void setPaginationListActivities(Paginador paginationListActivities) {
		this.paginationListActivities = paginationListActivities;
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
	 * @return removeList: Object selected from the list to eliminate
	 *         activities.
	 */
	public Activities getRemoveList() {
		return removeList;
	}

	/**
	 * @param removeList
	 *            :Object selected from the list to eliminate activities.
	 */
	public void setRemoveList(Activities removeList) {
		this.removeList = removeList;
	}

	/**
	 * @return activityNames: Object of the class name activities.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Object of the class name activities.
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return crops: Object of class culture.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            :Object of class culture.
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
	 *            :Object of the class name of the crop.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return cycleStandardActivities: Object class standard cycle activities.
	 */
	public CycleStandardActivities getCycleStandardActivities() {
		return cycleStandardActivities;
	}

	/**
	 * @param cycleStandardActivities
	 *            :Object class standard cycle activities.
	 */
	public void setCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) {
		this.cycleStandardActivities = cycleStandardActivities;
	}

	/**
	 * @return removeStandard: Standard Cycle Activities object selected to
	 *         remove from the list.
	 */
	public CycleStandardActivities getRemoveStandard() {
		return removeStandard;
	}

	/**
	 * @param removeStandard
	 *            :Standard Cycle Activities object selected to remove from the
	 *            list.
	 */
	public void setRemoveStandard(CycleStandardActivities removeStandard) {
		this.removeStandard = removeStandard;
	}

	/**
	 * @return nameSearch: Gets the search parameter in the system.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :Sets the search parameter in the system.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return messageCrumb: message crumb of bread in the record template.
	 */
	public String getMessageCrumb() {
		return messageCrumb;
	}

	/**
	 * @param messageCrumb
	 *            :message crumb of bread in the record template.
	 */
	public void setMessageCrumb(String messageCrumb) {
		this.messageCrumb = messageCrumb;
	}

	/**
	 * @return maxDate: max date to validate date range of activity.
	 */
	public Date getMaxDate() {
		return maxDate;
	}

	/**
	 * @param maxDate
	 *            :max date to validate date range of activity.
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * @return initialDateSearch: gets the initial date of the activity to
	 *         search in a range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            :sets the initial date of the activity to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the final date of the activity to search in
	 *         a range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            :sets the final date of the activity to search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return idCropNamesSearch: id crop name by which you want to see the
	 *         culture.
	 */
	public int getIdCropNamesSearch() {
		return idCropNamesSearch;
	}

	/**
	 * @param idCropNamesSearch
	 *            : id crop name by which you want to see the culture.
	 */
	public void setIdCropNamesSearch(int idCropNamesSearch) {
		this.idCropNamesSearch = idCropNamesSearch;
	}

	/**
	 * @return idCropName: Name identifier harvest.
	 */
	public int getIdCropName() {
		return idCropName;
	}

	/**
	 * @param idCropName
	 *            :Name identifier harvest.
	 */
	public void setIdCropName(int idCropName) {
		this.idCropName = idCropName;
	}

	/**
	 * 
	 * @return clean: Cleans the standard list activities if its value is
	 *         'true'.
	 */
	public boolean isClean() {
		return clean;
	}

	/**
	 * @param clean
	 *            :Cleans the standard list activities if its value is 'true'.
	 */
	public void setClean(boolean clean) {
		this.clean = clean;
	}

	/**
	 * Method to edit or create a new assignment of activities.
	 * 
	 * @param activities
	 *            :Object of activities are adding or editing.
	 * 
	 * @return regCropActivities: Template redirects to assign farming
	 *         activities.
	 * 
	 */
	public String addEditCropActivities(Activities activities) {
		try {
			cleanActivities();
			if (activities == null) {
				this.activities = new Activities();
				crops = new Crops();
				crops.setCropNames(new CropNames());
				cropNames = new CropNames();
				this.activities.setActivityName(new ActivityNames());
				messageCrumb = "messageBaseInformation.municipio_label_registrar";
			} else {
				this.activities = activities;
				messageCrumb = "messageBaseInformation.municipio_label_modificar";
			}
			crops = cropsDao.defaultSearchCrop(Constantes.ID_CROP_DEFAULT);
			if (crops != null) {
				initializeActivities();
			}
			fillCropNames();
			setClean(false);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regCropActivities";
	}

	/**
	 * CropNames method that loads a list.
	 * 
	 * @throws Exception
	 */
	private void fillCropNames() throws Exception {
		optionsCropNames = new ArrayList<SelectItem>();
		List<CropNames> listCropNames = cropNamesDao.listCropNames();
		if (listCropNames != null) {
			for (CropNames cropNames : listCropNames) {
				optionsCropNames.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}
		fillCropNamesCrop();
	}

	/**
	 * Method allows complete the list of crops harvested after the name
	 * selected.
	 * 
	 */
	public void fillCropNamesCrop() {
		try {
			int idCropsName = 0;
			optionsCrops = new ArrayList<SelectItem>();
			if (this.crops != null && this.crops.getCropNames() != null) {
				idCropsName = this.crops.getCropNames().getIdCropName();
			} else {
				idCropsName = idCropNamesSearch;
			}
			List<Crops> listCropsCurrent;
			listCropsCurrent = cropsDao
					.consultCropNamesCropsCurrent(idCropsName);
			if (listCropsCurrent != null) {
				for (Crops crops : listCropsCurrent) {
					optionsCrops.add(new SelectItem(crops.getIdCrop(), crops
							.getDescription()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allows you to save or edit an activity assigned to a crop.
	 * 
	 * @return addEditActivities: Method to edit or create a new assignment of
	 *         activities.
	 * @throws Exception
	 */
	public String saveCropActivities() throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			String outTxtEdit = "";
			String outTxtSave = "";
			String outStandardSave = "";
			for (Activities activities : this.listActivities) {
				if (activities.getIdActivity() != 0) {
					outTxtEdit += activities.getActivityName()
							.getActivityName() + ", ";
					activitiesDao.editActivities(activities);
				} else {
					outTxtSave += activities.getActivityName()
							.getActivityName() + ", ";
					activitiesDao.saveActivities(activities);
				}
			}
			if (this.listCycleStandardActivities.size() != 0) {
				for (CycleStandardActivities cycleStandardActivities : this.listCycleStandardActivities) {
					Activities activities = new Activities();
					activities.setCrop(crops);
					activities.setActivityName(cycleStandardActivities
							.getActivityNames());
					activities.setHrRequired(cycleStandardActivities
							.getHrRequired());
					activities.setMachineRequired(cycleStandardActivities
							.getMachineRequired());
					activities.setServiceRequired(cycleStandardActivities
							.getServiceRequired());
					activities.setMaterialsRequired(cycleStandardActivities
							.getMaterialsRequired());
					activities.setSequenceNumber(cycleStandardActivities
							.getSequenceNumber());
					activities.setDurationBudget(cycleStandardActivities
							.getDuration());
					activities.setCostHrBudget(cycleStandardActivities
							.getCostHrHa());
					activities.setCostMachinesEqBudget(cycleStandardActivities
							.getCostMachinesEqHa());
					activities.setCostServicesBudget(cycleStandardActivities
							.getCostServicesHa());
					activities.setCostMaterialsBudget(cycleStandardActivities
							.getCostMaterialsHa());
					activities.setGeneralCostBudget(cycleStandardActivities
							.getGeneralCostHa());
					activities.setDangerous(cycleStandardActivities
							.getDangerous());
					activitiesDao.saveActivities(activities);
					outStandardSave += cycleStandardActivities
							.getActivityNames().getActivityName() + ", ";
				}
			}
			if (outTxtEdit.length() > 0) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_modificar"),
								outTxtEdit));
			}
			if (outTxtSave.length() > 0) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_guardar"),
								outTxtSave));
			}
			if (outStandardSave.length() > 0) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_guardar"),
								outStandardSave));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return addEditActivities();
	}

	/**
	 * Method to edit or create a new assignment of activities.
	 * 
	 * @return regCropActivities: Template redirects to assign farming
	 *         activities.
	 * @throws Exception
	 * 
	 */
	public String addEditActivities() throws Exception {
		cleanActivities();
		fillCropNames();
		initializeActivities();
		return "regCropActivities";
	}

	/**
	 * Consult the list of activities crops
	 * 
	 * @modify 09/03/2016 Mabell.Boada
	 * 
	 */
	public void consultActivityNamesXCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listActivityNames = new ArrayList<ActivityNames>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessageSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(consult, parameters, bundle, unionMessageSearch);
			Long amount = activityNamesDao.amountActivityNameCrops(consult,
					parameters);
			if (amount != null) {
				pagination.paginarRangoDefinido(amount, 5);
			}
			listActivityNames = activityNamesDao.queryActivityNamesXIdCrop(
					pagination.getInicio(), pagination.getRango(), consult,
					parameters);
			if (this.listActivityNames != null
					&& this.listActivityNames.size() > 0) {
				for (ActivityNames activityNames : this.listActivityNames) {
					if (activityNames.isSeleccionado()) {
						listActivityNames.remove(activityNames);
						break;
					}

				}
			}
			if ((listActivityNames == null || listActivityNames.size() <= 0)
					&& !"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessageSearch);
			} else if (listActivityNames == null
					|| listActivityNames.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								unionMessageSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
			if (!"".equals(nameSearch)) {
				ControladorContexto.mensajeInformacion(
						"popupForm:tActivityNames", messageSearch);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 * 
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method passing objects of type Names activity to a list of type
	 * Activities.
	 * 
	 * @modify 14/01/2016 Wilhelm.Boada
	 * 
	 * @param listActivityNames
	 *            : Name list of activities.
	 * @throws Exception
	 */
	public void generateListActivities(List<ActivityNames> listActivityNames)
			throws Exception {
		String param2 = ControladorContexto.getParam("param2");
		boolean desdeModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		setClean(desdeModal);
		this.activities = new Activities();
		this.activities.setActivityName(this.activityNames);
		this.activities.setCrop(getCrops());
		if (this.listActivities == null) {
			this.listActivities = new ArrayList<Activities>();
		}
		this.listActivities.add(this.activities);
		this.pagination = new Paginador();
		initializeList();
	}

	/**
	 * Method to remove from the list the names of the activities selected by
	 * the user in the eye.
	 * 
	 */
	public void removeSelected() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			for (CycleStandardActivities cycleStandardActivities : this.listCycleStandardActivities) {
				if (cycleStandardActivities.equals(removeStandard)) {
					listCycleStandardActivities.remove(removeStandard);
					break;
				}
			}
			for (Activities activities : this.listActivities) {
				if (activities.equals(removeList)) {
					activitiesDao.deleteActivities(removeList);
					listActivities.remove(removeList);
					break;
				}
			}
			if (removeList != null) {
				ControladorContexto
						.mensajeInformacion(null, MessageFormat.format(
								bundle.getString("message_registro_eliminar"),
								removeList.getActivityName().getActivityName()));
				initializeList();
			}
			if (removeStandard != null) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_eliminar"),
								removeStandard.getActivityNames()
										.getActivityName()));
				initializeListStandardCycle();
			}
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					activities.getActivityName().getActivityName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to clean up the list of activities.
	 */
	private void cleanActivities() {
		this.listActivities = new ArrayList<Activities>();
		this.listCycleStandardActivities = new ArrayList<CycleStandardActivities>();
	}

	/**
	 * This method allows initialize all the activities.
	 */
	public void initializeActivities() {
		this.nameSearch = "";
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		consultActivities();
	}

	/**
	 * See the list of activities depending on the crop.
	 */
	public void consultActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			int idCrops = this.crops.getIdCrop();
			advanceSearchActivities(consult, parameters, bundle,
					unionMessagesSearch);
			Long amount = activitiesDao.amountActivitiesCrop(consult,
					parameters, idCrops);
			if (amount != null) {
				paginationListActivities.paginar(amount);
			}
			this.listActivities = activitiesDao.queryActivitiesByCrop(
					paginationListActivities.getInicio(),
					paginationListActivities.getRango(), consult, parameters,
					idCrops);
			if (!clean) {
				this.listCycleStandardActivities = new ArrayList<CycleStandardActivities>();
			}
			setClean(false);
			if ((listActivities == null || listActivities.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat.format(
						bundle.getString("message_no_related_activity"),
						unionMessagesSearch);
				this.listActivities = new ArrayList<Activities>();
			} else if (listActivities == null || listActivities.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 * 
	 */
	private void advanceSearchActivities(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);

		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
		if (this.initialDateSearch != null && this.finalDateSearch != null) {
			consult.append("AND a.initialDtBudget BETWEEN :initialDateSearch AND :finalDateSearch ");
			SelectItem item = new SelectItem(initialDateSearch,
					"initialDateSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(finalDateSearch,
					"finalDateSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_start_date") + ": " + '"'
					+ formato.format(this.initialDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateFrom);

			String dateTo = bundle.getString("label_end_date") + ": " + '"'
					+ formato.format(finalDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateTo);
		}

	}

	/**
	 * Consult the list of existing standard cycle activities
	 * 
	 */
	public void consultStandardActivities() {
		try {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			this.listCycleStandardActivities = cycleStandardActivitiesDao
					.queryCycleStandardActivities(idCropName);
			if (listCycleStandardActivities == null
					|| listCycleStandardActivities.size() <= 0) {
				ControladorContexto.mensajeInformacion(null, bundle
						.getString("message_no_related_standard_activities"));
			}
			initializeListStandardCycle();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate that the activity dates are in the range of
	 * dates crop.
	 * 
	 * @author Mabell.Boada
	 * @modify 20/04/2016 Wilhelm.Boada
	 * 
	 */
	public void validateDates() {
		try {
			Crops crop = cropsDao.cropsXID(activities.getCrop().getIdCrop());
			Date date = ControladorFechas.formatearFecha(
					activities.getInitialDtBudget(),
					Constantes.DATE_FORMAT_MESSAGE_WITHOUT_TIME);

			if (date.before(crop.getInitialDate())
					|| date.after(crop.getFinalDate())) {
				ControladorContexto.mensajeErrorArg1(
						"popupFormReg:fechaInicio",
						"message_validate_dates_range", "mensaje",
						ControladorFechas.formatDate(crop.getInitialDate(),
								Constantes.DATE_FORMAT_MESSAGE_WITHOUT_TIME),
						ControladorFechas.formatDate(crop.getFinalDate(),
								Constantes.DATE_FORMAT_MESSAGE_WITHOUT_TIME));
			}
			date = ControladorFechas.formatearFecha(
					activities.getFinalDtBudget(),
					Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
			if (date.before(crop.getInitialDate())
					|| date.after(crop.getFinalDate())) {
				ControladorContexto.mensajeErrorArg1("popupFormReg:fechaFin",
						"message_validate_dates_range", "mensaje",
						ControladorFechas.formatDate(crop.getInitialDate(),
								Constantes.DATE_FORMAT_MESSAGE_WITHOUT_TIME),
						ControladorFechas.formatDate(crop.getFinalDate(),
								Constantes.DATE_FORMAT_MESSAGE_WITHOUT_TIME));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validate the date to add the max hour in the end date
	 * 
	 * @modify 21/04/2016 Wilhelm.Boada
	 * 
	 */
	public void validateMaxDate() {
		if (this.activities.getInitialDtBudget() != null) {
			this.maxDate = new Date();
			maxDate = activities.getInitialDtBudget();
			Calendar cal = Calendar.getInstance();
			cal.setTime(maxDate);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			maxDate = cal.getTime();
			this.activities.setFinalDtBudget(null);
		}
	}

	/**
	 * This method validates the entered duration time does not exceed the total
	 * time of the activity and workload guidelines are met.
	 * 
	 * @param context
	 *            : Context of view.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 * @throws Exception
	 */
	public void validateDate(FacesContext context, UIComponent toValidate,
			Object value) throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String clientId = toValidate.getClientId(context);

		Double duration = (Double) value;
		Double durationActivity;
		Date initialDate = this.activities.getInitialDtBudget();
		Date finalDate = this.activities.getFinalDtBudget();
		try {
			if (duration != null) {
				if (duration > 0) {
					durationActivity = (Double) ControladorFechas.restarFechas(
							initialDate, finalDate);
					if (duration.compareTo(durationActivity) > 0) {
						String mensaje = "message_activity_duration";
						context.addMessage(clientId,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										bundle.getString(mensaje), null));
						((UIInput) toValidate).setValid(false);
					}
				} else {
					String mensaje = "message_greater_zero";
					context.addMessage(clientId,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									bundle.getString(mensaje), null));
					((UIInput) toValidate).setValid(false);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Pager manages the activities list.
	 * 
	 * @author Gerardo.Herrera
	 */
	public void initializeList() {
		Long cantidadPaginador = (long) listActivities.size();
		try {
			this.paginationActivities
					.paginarRangoDefinido(cantidadPaginador, 5);
			int inicial = paginationActivities.getItemInicial() - 1;
			int fin = paginationActivities.getItemFinal();
			this.subListActivities = listActivities.subList(inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Pager manages the standard cycle activities list.
	 * 
	 * @author Gerardo.Herrera
	 */
	public void initializeListStandardCycle() {
		Long cantidadPaginador = (long) listCycleStandardActivities.size();
		try {
			this.paginationActivitiesStandardCycle.paginarRangoDefinido(
					cantidadPaginador, 5);
			int inicial = paginationActivitiesStandardCycle.getItemInicial() - 1;
			int fin = paginationActivitiesStandardCycle.getItemFinal();
			this.subListCycleStandardActivities = listCycleStandardActivities
					.subList(inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}