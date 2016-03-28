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
 * @modify 23/03/2016 Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CycleStandardActivitiesAction implements Serializable {

	private List<CycleStandardActivities> cycleStandardActivitiesList;
	private List<CycleStandardActivities> cycleStandardsList;
	private List<ActivityNames> activityNamesList;
	private List<SelectItem> itemsCropName;

	private CycleStandardActivities cycleStandardActivities;
	private CycleStandardActivities deleteList;
	private CropNames cropNames;

	private Paginador pagination = new Paginador();
	private Paginador paginationCycleActivities = new Paginador();

	private String nameSearch;

	private int idCropNames;

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
	 * Method to create a new standard cycle activity.
	 * 
	 * @modify 18/03/2016 Gerardo.Herrera
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
		this.cycleStandardsList = new ArrayList<CycleStandardActivities>();
		this.cycleStandardActivitiesList = new ArrayList<CycleStandardActivities>();
		this.cropNames = new CropNames();
		try {
			loadCropNames();
			if (cycleStandardActivities != null) {
				this.cycleStandardActivities = cycleStandardActivities;
			} else {
				this.cycleStandardActivities = new CycleStandardActivities();
				cropNames = new CropNames();
				this.cycleStandardActivities
						.setActivityNames(new ActivityNames());
				consultCycleStandarActivities();
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
	 * @modify 18/03/2016 Gerardo.Herrera
	 * 
	 * @return consultCycleStandarActivities: Method That loads information
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
			if (this.cycleStandardActivities == null
					|| this.cycleStandardActivities.getIdCycleActivity() == 0) {
				for (CycleStandardActivities cycleStandar : this.cycleStandardActivitiesList) {
					int idCropName = cycleStandar.getCropNames()
							.getIdCropName();
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
			} else {
				cycleStandardActivitiesDao
						.editCycleStandardActivities(this.cycleStandardActivities);
				this.cycleStandardActivities = null;
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), cropNameString));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCycleStandarActivities();
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
		this.cycleStandardActivities = null;
		try {
			cycleStandardActivitiesList.clear();
			this.cropNames = cropNamesDao.cropNamesXId(idCropNames);
			for (ActivityNames activityName : activityNamesList) {
				if (activityName.isSeleccionado()) {
					CycleStandardActivities cycleStandardActivities = new CycleStandardActivities();
					cycleStandardActivities.setActivityNames(activityName);
					cycleStandardActivitiesList.add(cycleStandardActivities);
					cycleStandardActivities.setCropNames(this.cropNames);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to remove from the list the names of the activities that an user
	 * selected in the register view.
	 * 
	 */
	public void deleteSelectedOnes() {
		try {
			cycleStandardActivitiesDao
					.deleteCycleStandardActivities(deleteList);
			cycleStandardActivitiesList.remove(deleteList);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		consultCycleStandarActivities();
	}

	/**
	 * It is responsible for consulting all cyclestandardactivities
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @return regCyclStandAct: Redirects to the register Standard cycle
	 *         activities template.
	 */
	public String consultCycleStandarActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		cycleStandardsList = new ArrayList<CycleStandardActivities>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder joinSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearchCycleStandar(queryBuilder, parameters, bundle,
					joinSearchMessages);
			Long amount = cycleStandardActivitiesDao.quantityCycleStandard(
					queryBuilder, parameters);
			if (amount != null) {
				paginationCycleActivities.paginar(amount);
			}
			cycleStandardsList = cycleStandardActivitiesDao
					.queryCycleStandarActivities(
							paginationCycleActivities.getInicio(),
							paginationCycleActivities.getRango(), queryBuilder,
							parameters);
			if ((cycleStandardsList == null || cycleStandardsList.size() <= 0)
					&& !"".equals(joinSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								joinSearchMessages);
			} else if (cycleStandardsList == null
					|| cycleStandardsList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(joinSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								joinSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regCyclStandAct";
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param query
	 *            : query to concatenate.
	 * @param parameter
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags.
	 * @param joinSearchMessages
	 *            : Message search.
	 */
	private void advancedSearchCycleStandar(StringBuilder query,
			List<SelectItem> parameter, ResourceBundle bundle,
			StringBuilder joinSearchMessages) {

		if (this.idCropNames != 0) {
			query.append("WHERE cn.idCropName=:idCropName ");
			SelectItem item = new SelectItem(this.idCropNames, "idCropName");
			parameter.add(item);
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
	 * @modify 29/09/2015 Andres.Gomez
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
			jointSearchMessages.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

}
