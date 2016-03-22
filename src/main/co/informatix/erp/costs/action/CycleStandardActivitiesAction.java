package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.CycleStandardActivitiesDao;
import co.informatix.erp.costs.entities.CycleStandardActivities;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the creating and updating logic of the cycle standard
 * activities in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CycleStandardActivitiesAction implements Serializable {
	private List<CycleStandardActivities> cycleStandardActivitiesList;
	private List<ActivityNames> activityNamesList;
	private List<SelectItem> itemsCropName;
	private List<CycleStandardActivities> cycleStandardsList = new ArrayList<CycleStandardActivities>();

	private CycleStandardActivities cycleStandardActivities;
	private CycleStandardActivities deleteList;
	private CropNames cropNames;
	private ActivityNames activityNames;

	private int idCropNames;
	private String nameSearch;

	private Paginador pagination = new Paginador();
	private Paginador paginationCycleActivities = new Paginador();

	@EJB
	private CropNamesDao cropNamesDao;

	@EJB
	private CycleStandardActivitiesDao cycleStandardActivitiesDao;

	@EJB
	private ActivityNamesDao activityNamesDao;

	/**
	 * @return cycleStandardActivitiesList: List of cycle standard activities.
	 */
	public List<CycleStandardActivities> getCycleStandardActivitiesList() {
		return cycleStandardActivitiesList;
	}

	/**
	 * @param cycleStandardActivitiesList
	 *            : List of cycle standard activities.
	 * 
	 */
	public void setCycleStandardActivitiesList(
			List<CycleStandardActivities> cycleStandardActivitiesList) {
		this.cycleStandardActivitiesList = cycleStandardActivitiesList;
	}

	/**
	 * @return activityNamesList: List of activity names.
	 */
	public List<ActivityNames> getActivityNamesList() {
		return activityNamesList;
	}

	/**
	 * @param activityNamesList
	 *            : List of activity names.
	 * 
	 */
	public void setActivityNamesList(List<ActivityNames> activityNamesList) {
		this.activityNamesList = activityNamesList;
	}

	/**
	 * @return itemsCropName: List of crops associated to cycle standard
	 *         activities.
	 */
	public List<SelectItem> getItemsCropName() {
		return itemsCropName;
	}

	/**
	 * @param itemsCropName
	 *            : List of crops associated to cycle standard activities.
	 */
	public void setItemsCropName(List<SelectItem> itemsCropName) {
		this.itemsCropName = itemsCropName;
	}

	/**
	 * @return cycleStandardActivities: Object of cycle standard activities.
	 */
	public CycleStandardActivities getCycleStandardActivities() {
		return cycleStandardActivities;
	}

	/**
	 * @param cycleStandardActivities
	 *            : Object of cycle standard activities.
	 * 
	 */
	public void setCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) {
		this.cycleStandardActivities = cycleStandardActivities;

	}

	/**
	 * @return deleteList: Object selected cycleStandardActivities to remove
	 *         from the list
	 */
	public CycleStandardActivities getDeleteList() {
		return deleteList;
	}

	/**
	 * @param deleteList
	 *            : Object selected cycleStandardActivities to remove from the
	 *            list
	 * 
	 */
	public void setDeleteList(CycleStandardActivities deleteList) {
		this.deleteList = deleteList;
	}

	/**
	 * @return cropNames: Object of the class named crops.
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            : Object of the class named crops.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return activityNames: Object of activity name.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Object of activity name.
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return idCropNames: Crop Name identifier.
	 */
	public int getIdCropNames() {
		return idCropNames;
	}

	/**
	 * @param idCropNames
	 *            : Crop Name identifier.
	 * 
	 */
	public void setIdCropNames(int idCropNames) {
		this.idCropNames = idCropNames;
	}

	/**
	 * @return nameSearch: Name of ActivityName you want to query.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name of ActivityName you want to query.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return pagination: Paged list of the names of the activities they may
	 *         have in the popup.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paged list of the names of the activities they may have in
	 *            the popup.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationCycleActivities: Paged list of cycle activities.
	 */
	public Paginador getPaginationCycleActivities() {
		return paginationCycleActivities;
	}

	/**
	 * @param paginationCycleActivities
	 *            : Paged list of cycle activities.
	 */
	public void setPaginationCycleActivities(Paginador paginationCycleActivities) {
		this.paginationCycleActivities = paginationCycleActivities;
	}

	/**
	 * @return cycleStandardsList: List of standard cycles that stores a sublist
	 *         for managing paging.
	 */
	public List<CycleStandardActivities> getCycleStandardsList() {
		return cycleStandardsList;
	}

	/**
	 * @param cycleStandardsList
	 *            : List of CycleStandardActivities that stores a sublist for
	 *            managing paging.
	 */
	public void setCycleStandardsList(
			List<CycleStandardActivities> cycleStandardsList) {
		this.cycleStandardsList = cycleStandardsList;
	}

	/**
	 * Method to create a new standard cycle activity.
	 * 
	 * @param cycleStandardActivities
	 *            : Object standard cycle activities to be added.
	 * 
	 * @return regCyclStandAct: Redirects to the register standard cycle
	 *         activities template.
	 * 
	 */
	public String addEditCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) {
		itemsCropName = new ArrayList<SelectItem>();
		try {
			eraseCycleStandardActivities();
			loadCropNames();
			if (cycleStandardActivities != null) {
				this.cycleStandardActivities = cycleStandardActivities;
			} else {
				this.cycleStandardActivities = new CycleStandardActivities();
				cropNames = new CropNames();
				this.cycleStandardActivities
						.setActivityNames(new ActivityNames());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return "regCyclStandAct";
	}

	/**
	 * It loads crop names in a list.
	 * 
	 * @throws Exception
	 */
	private void loadCropNames() throws Exception {
		itemsCropName = new ArrayList<SelectItem>();
		List<CropNames> cropNamesList = cropNamesDao.listCropNames();
		if (cropNamesList != null) {
			for (CropNames cropName : cropNamesList) {
				itemsCropName.add(new SelectItem(cropName.getIdCropName(),
						cropName.getCropName()));
			}
		}

	}

	/**
	 * Save the cycle standard activities.
	 * 
	 * @return addEditStandardActivities: Method That loads information
	 *         necessary and redirected to the template record Standard cycle
	 *         activities.
	 * 
	 * @throws Exception
	 */
	public String saveCycleStandardActivities() throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_guardar_actividades";
		boolean exists;
		try {
			Object cropNameString = ValidacionesAction.getLabel(
					this.itemsCropName, this.cropNames.getIdCropName());
			for (CycleStandardActivities cycleStandar : this.cycleStandardActivitiesList) {
				int idCropName = cycleStandar.getCropNames().getIdCropName();
				int idActivityName = cycleStandar.getActivityNames()
						.getIdActivityName();
				exists = cycleStandardActivitiesDao
						.relateCropNamesActivityNames(idCropName,
								idActivityName);
				if (exists) {
					cycleStandardActivitiesDao
							.editCycleStandardActivities(cycleStandar);
				} else {
					cycleStandardActivitiesDao
							.saveCycleStandardActivities(cycleStandar);
				}
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), cropNameString));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return addEditStandardActivities();
	}

	/**
	 * Method to edit or create a new assignment of standard cycle activities.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return regCyclStandAct: Redirects to the register standard cycle
	 *         activities template.
	 * @throws Exception
	 * 
	 */
	public String addEditStandardActivities() throws Exception {
		this.cycleStandardActivitiesList = new ArrayList<CycleStandardActivities>();
		loadCropNames();
		searchCycleStandardActivities();
		return "regCyclStandAct";
	}

	/**
	 * Load the paged list to manages the cycle standard activities list.
	 */
	public void initializeList() {
		Long paginadorAmount = (long) cycleStandardActivitiesList.size();
		try {
			this.paginationCycleActivities.paginarRangoDefinido(paginadorAmount,
					10);
			int first = paginationCycleActivities.getItemInicial() - 1;
			int last = paginationCycleActivities.getItemFinal();
			this.cycleStandardsList = cycleStandardActivitiesList.subList(
					first, last);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It loads a list of activityNames to a CycleStandardActivity.
	 * 
	 * @modify 15/07/2015 Andres.Gomez
	 * 
	 * @param activityNamesList
	 *            : List of activities names.
	 */
	public void generateCycleStandardActivitiesList(
			List<ActivityNames> activityNamesList) {
		for (ActivityNames activityName : activityNamesList) {
			if (activityName.isSeleccionado()) {
				CycleStandardActivities cycleStandardActivities = new CycleStandardActivities();
				cycleStandardActivities.setActivityNames(activityName);
				cycleStandardActivitiesList.add(cycleStandardActivities);
				cycleStandardActivities.setCropNames(getCropNames());
			}
		}
		this.pagination = new Paginador();
		initializeList();
	}

	/**
	 * Method to remove from the list the names of the activities that an user
	 * selected in the register view.
	 * 
	 */
	public void deleteSelectedOnes() {
		for (CycleStandardActivities cycleStandardActivities : this.cycleStandardActivitiesList) {
			if (cycleStandardActivities.equals(deleteList)) {
				try {
					cycleStandardActivitiesDao
							.deleteCycleStandardActivities(deleteList);
					cycleStandardActivitiesList.remove(deleteList);
					break;
				} catch (Exception e) {
					ControladorContexto.mensajeError(e);
				}

			}
		}
		initializeList();
	}

	/**
	 * Method that cleans the list of standard cycle activities and Crop names.
	 */
	private void eraseCycleStandardActivities() {
		this.cycleStandardActivitiesList = new ArrayList<CycleStandardActivities>();
		this.cropNames = new CropNames();
	}

	/**
	 * Look for the list of existing standard cycle activities.
	 * 
	 */
	public void searchCycleStandardActivities() {
		try {
			this.cycleStandardActivitiesList = cycleStandardActivitiesDao
					.queryCycleStandardActivities(this.cropNames
							.getIdCropName());
			this.paginationCycleActivities = new Paginador();
			initializeList();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Consult the list of the activity names to show in the pop-up.
	 * 
	 * @modify 29/09/2015 Andres.Gomez
	 * 
	 * @return regCyclStandAct: Redirects to the register Standard cycle
	 *         activities template.
	 */
	public String searchActivityNames() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		activityNamesList = new ArrayList<ActivityNames>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(queryBuilder, parameters, bundle,
					jointSearchMessages);
			Long amount = activityNamesDao.amountActivityNames(queryBuilder,
					parameters);
			if (amount != null) {
				pagination.paginarRangoDefinido(amount, 5);
			}
			activityNamesList = activityNamesDao
					.queryActivityNamesXIdCropNames(pagination.getInicio(),
							pagination.getRango(), queryBuilder, parameters);
			if ((activityNamesList == null || activityNamesList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (activityNamesList == null
					|| activityNamesList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								jointSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
			if (!"".equals(nameSearch)) {
				ControladorContexto.mensajeInformacion(
						"popupForm:tActivityNames", searchMessage);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regCyclStandAct";
	}

	/**
	 * This method builds the query with an advanced search; it also builds
	 * display messages depending on the search criteria selected by the user.
	 * 
	 * @Mmodify 29/09/2015 Andres.Gomez
	 * 
	 * @param query
	 *            : Query to concatenate.
	 * @param parameter
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * 
	 * @param jointSearchMessages
	 *            : Search message.
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameter, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append("WHERE UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameter.add(item);
			jointSearchMessages.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

}
