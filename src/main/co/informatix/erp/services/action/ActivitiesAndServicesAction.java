package co.informatix.erp.services.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.services.dao.ActivitiesAndServicesDao;
import co.informatix.erp.services.dao.ServiceTypeDao;
import co.informatix.erp.services.entities.ActivitiesAndServices;
import co.informatix.erp.services.entities.ServiceType;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * This class allows the logic to allocate services to activities that may be
 * related to crops in the database. The logic is: insert services activities to
 * a particular crop.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivitiesAndServicesAction implements Serializable {

	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private CropsDao cropsDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private ActivitiesAndServicesDao activitiesAndServicesDao;
	@EJB
	private ServiceTypeDao serviceTypeDao;
	@EJB
	private SuppliersDao supplierDao;

	private List<Activities> listActivities;
	private List<ActivitiesAndServices> listActivitiesServices;
	private List<SelectItem> optionsCropNames;
	private List<SelectItem> optionsCrops;
	private List<SelectItem> itemsServiceType;
	private List<SelectItem> itemsSupplier;

	private ActivitiesAndServices activitiesAndServices;
	private Activities activities;
	private ActivityNames activityNames;
	private Crops crops;
	private CropNames cropNames;
	private Paginador paginador = new Paginador();

	private String crumbMessage;
	private String nameSearch;

	private int idCropNamesSearch;
	private int idCropName;

	/**
	 * @return listActivities: Activities list.
	 */
	public List<Activities> getListActivities() {
		return listActivities;
	}

	/**
	 * @param listActivities
	 *            :Activities list.
	 */
	public void setListActivities(List<Activities> listActivities) {
		this.listActivities = listActivities;
	}

	/**
	 * @return listActivitiesServices: Activities and services list.
	 */
	public List<ActivitiesAndServices> getListActivitiesServices() {
		return listActivitiesServices;
	}

	/**
	 * @param listActivitiesServices
	 *            :Activities and services list.
	 */
	public void setListActivitiesServices(
			List<ActivitiesAndServices> listActivitiesServices) {
		this.listActivitiesServices = listActivitiesServices;
	}

	/**
	 * @return optionsCropNames: Crop name associated with an activity.
	 */
	public List<SelectItem> getOptionsCropNames() {
		return optionsCropNames;
	}

	/**
	 * @param optionsCropNames
	 *            :Crop name associated with an activity.
	 */
	public void setOptionsCropNames(List<SelectItem> optionsCropNames) {
		this.optionsCropNames = optionsCropNames;
	}

	/**
	 * @return optionsCrops :Crop associated with an activity.
	 */
	public List<SelectItem> getOptionsCrops() {
		return optionsCrops;
	}

	/**
	 * @param optionsCrops
	 *            :Crop associated with an activity.
	 */
	public void setOptionsCrops(List<SelectItem> optionsCrops) {
		this.optionsCrops = optionsCrops;
	}

	/**
	 * @return itemsServiceType: Type of service associated with a service.
	 */
	public List<SelectItem> getItemsServiceType() {
		return itemsServiceType;
	}

	/**
	 * @param itemsServiceType
	 *            : Type of service associated with a service.
	 */
	public void setItemsServiceType(List<SelectItem> itemsServiceType) {
		this.itemsServiceType = itemsServiceType;
	}

	/**
	 * @return itemsSupplier: Provider associated to a service.
	 */
	public List<SelectItem> getItemsSupplier() {
		return itemsSupplier;
	}

	/**
	 * @param itemsSupplier
	 *            : Provider associated to a service.
	 */
	public void setItemsSupplier(List<SelectItem> itemsSupplier) {
		this.itemsSupplier = itemsSupplier;
	}

	/**
	 * @return activitiesAndServices: Object of class activities and services.
	 */
	public ActivitiesAndServices getActivitiesAndServices() {
		return activitiesAndServices;
	}

	/**
	 * @param activitiesAndServices
	 *            :Object of class activities and services.
	 */
	public void setActivitiesAndServices(
			ActivitiesAndServices activitiesAndServices) {
		this.activitiesAndServices = activitiesAndServices;
	}

	/**
	 * @return activities: Object of class activities.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            :Object of class activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return activityNames: Object of the class name activities.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            :Object of the class name activities.
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
	 * @return paginador: Management paged list of activities.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list of activities.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return crumbMessage: Message crumb of bread in the record template.
	 */
	public String getCrumbMessage() {
		return crumbMessage;
	}

	/**
	 * @param crumbMessage
	 *            :Message crumb of bread in the record template.
	 */
	public void setCrumbMessage(String crumbMessage) {
		this.crumbMessage = crumbMessage;
	}

	/**
	 * @return nameSearch: Activity name to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Activity name to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
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
	 *            :id crop name by which you want to see the culture.
	 */
	public void setIdCropNamesSearch(int idCropNamesSearch) {
		this.idCropNamesSearch = idCropNamesSearch;
	}

	/**
	 * @return idCropName: Identifier crop name.
	 */
	public int getIdCropName() {
		return idCropName;
	}

	/**
	 * @param idCropName
	 *            :Identifier crop name.
	 */
	public void setIdCropName(int idCropName) {
		this.idCropName = idCropName;
	}

	/**
	 * Method to assign a new service to an activity
	 * 
	 * @param activitiesAndServices
	 *            :Object of activities and services that are adding or editing
	 * 
	 * @return regActivitiesAndServices: Template redirects to assign activities
	 *         and services.
	 * 
	 */
	public String addEditActivitiesAndServices(
			ActivitiesAndServices activitiesAndServices) {
		try {
			cleanActivities();
			llenarCropNames();
			if (activitiesAndServices == null) {
				this.activitiesAndServices = new ActivitiesAndServices();
				activities = new Activities();
				crops = new Crops();
				crops.setCropNames(new CropNames());
				cropNames = new CropNames();
				activities.setActivityName(new ActivityNames());
				crumbMessage = "mensajeInformacionBase.municipio_label_registrar";
			} else {
				this.activitiesAndServices = activitiesAndServices;
				crumbMessage = "mensajeInformacionBase.municipio_label_modificar";
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regActivitiesAndServices";
	}

	/**
	 * Method to clean up the list of activities and the name of the crop
	 */
	public void cleanActivities() {
		this.listActivities = new ArrayList<Activities>();
		this.listActivitiesServices = new ArrayList<ActivitiesAndServices>();
		this.cropNames = new CropNames();
	}

	/**
	 * That method loads CropNames in a list.
	 * 
	 * @throws Exception
	 */
	private void llenarCropNames() throws Exception {
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
	 * Method allows filling the list of crops according to the selected name.
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
			List<Crops> listCropsVigentes;
			listCropsVigentes = cropsDao
					.consultarCropNamesCropsVigentes(idCropsName);
			if (listCropsVigentes != null) {
				for (Crops crops : listCropsVigentes) {
					optionsCrops.add(new SelectItem(crops.getIdCrop(), crops
							.getDescription()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of activities
	 * 
	 * @return consultActivities: method consulting activities depending on the
	 *         crop, returns to the template management.
	 */
	public void initializingSearch() {
		nameSearch = "";
		consultActivities();
	}

	/**
	 * See the list of activities depending on the crop.
	 * 
	 */
	public void consultActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleAcivities = ControladorContexto
				.getBundle("mensajeCosts");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String menssageSearch = "";
		try {
			advancedSearch(consult, parameters, bundle, unionMessagesSearch);
			int idCrops = this.crops.getIdCrop();
			Long amount = activitiesDao.amountActivities(consult, parameters,
					idCrops);
			if (amount != null) {
				paginador.paginar(amount);
			}
			this.listActivities = activitiesDao.consultActivities(
					paginador.getInicio(), paginador.getRango(), consult,
					parameters, idCrops);
			if (listActivities == null || listActivities.size() <= 0) {
				ControladorContexto
						.mensajeInformacion(
								null,
								bundle.getString("message_no_existe_actividades_relacionadas"));
				this.listActivities = new ArrayList<Activities>();
			} else if (listActivities == null || listActivities.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				menssageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleAcivities.getString("activities_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(menssageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * messages to display build depending on the search criteria selected by
	 * the user.
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
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new service to an activity
	 * 
	 * @param activities
	 *            :Object of activities to be r to add or edit a service
	 * 
	 * @return regServices: Template redirects to assign a service to the
	 *         selected activity.
	 * 
	 */
	public String addEditServices(Activities activities) {
		try {
			loadCombos();
			if (activities != null) {
				this.setActivities(activities);
				this.setCrops(activities.getCrop());
				loadDetailsCrop(this.crops);
				this.setActivityNames(activities.getActivityName());

				this.listActivitiesServices = activitiesAndServicesDao
						.consultarXActivities(activities);
				if (this.listActivitiesServices == null) {
					this.listActivitiesServices = new ArrayList<ActivitiesAndServices>();
					this.activitiesAndServices = new ActivitiesAndServices();
				} else {
					loadDetilsServices();
				}
			} else {
				this.activities = new Activities();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regServices";
	}

	/**
	 * This method fills the various objects associated with a service
	 * 
	 * @throws Exception
	 */
	private void loadDetilsServices() throws Exception {
		List<ActivitiesAndServices> services = new ArrayList<ActivitiesAndServices>();
		services.addAll(this.listActivitiesServices);
		this.listActivitiesServices = new ArrayList<ActivitiesAndServices>();
		for (ActivitiesAndServices ActivitiesServices : services) {
			loadDetailActivitiesServices(ActivitiesServices);
			this.listActivitiesServices.add(ActivitiesServices);
		}
	}

	/**
	 * Method of uploading the details of a human resource.
	 * 
	 * @param activitiesAndServices
	 *            : activities and services which will carry the details.
	 * @throws Exception
	 */
	private void loadDetailActivitiesServices(
			ActivitiesAndServices activitiesAndServices) throws Exception {
		int idServices = activitiesAndServices.getIdService();
		Activities activities = (Activities) this.activitiesAndServicesDao
				.consultarObjetoServices("activities", idServices);
		ServiceType serviceType = (ServiceType) this.activitiesAndServicesDao
				.consultarObjetoServices("serviceType", idServices);
		Suppliers suppliers = (Suppliers) this.activitiesAndServicesDao
				.consultarObjetoServices("suppliers", idServices);
		activitiesAndServices.setActivities(activities);
		activitiesAndServices.setServiceType(serviceType);
		activitiesAndServices.setSuppliers(suppliers);
	}

	/**
	 * Method of uploading the details of a crops.
	 * 
	 * @param crops
	 *            : crops to which the details will be loaded.
	 * @throws Exception
	 */
	public void loadDetailsCrop(Crops crops) throws Exception {
		int idCrop = crops.getIdCrop();
		CropNames cropName = (CropNames) this.cropsDao.consultarObjetoCrop(
				"cropNames", idCrop);
		crops.setCropNames(cropName);
	}

	/**
	 * Adds a service in the list from the user interface.
	 * 
	 */
	public void addListServices() {
		this.activitiesAndServices = new ActivitiesAndServices();
		this.listActivitiesServices.add(activitiesAndServices);
	}

	/**
	 * This method allows you to load the combos to register a new interface
	 * services.
	 * 
	 * @throws Exception
	 */
	private void loadCombos() throws Exception {
		itemsServiceType = new ArrayList<SelectItem>();
		itemsSupplier = new ArrayList<SelectItem>();
		List<ServiceType> servicesType = serviceTypeDao
				.consultarServicesTypes();
		if (servicesType != null) {
			for (ServiceType serviceType : servicesType) {
				itemsServiceType.add(new SelectItem(serviceType
						.getIdServiceType(), serviceType.getDescripcion()));
			}
		}
		List<Suppliers> suppliers = supplierDao.consultarComboSuppliers();
		if (suppliers != null) {
			for (Suppliers supplier : suppliers) {
				itemsSupplier.add(new SelectItem(supplier.getIdSupplier(),
						supplier.getName()));
			}
		}
	}

	/**
	 * Method allows you to save or edit a service assigned to an activity
	 * 
	 * @return addEditActivitiesAndServices: Method to assign a new service for
	 *         an activity has wanted a navigation rule
	 */
	public String saveActivitiesAndServices() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_guardar";
		String outTxtSave = "";
		try {
			for (ActivitiesAndServices services : this.listActivitiesServices) {
				if (services.getIdService() != 0) {
					key = "message_registro_modificar";
					outTxtSave += services.getDescription() + ", ";
					activitiesAndServicesDao
							.editarActivitiesAndServices(services);
				} else {
					outTxtSave += services.getDescription() + ", ";
					Activities activity = activitiesDao
							.obtenerActivity(this.activities.getIdActivity());
					services.setActivities(activity);
					activitiesAndServicesDao
							.guardarActivitiesAndServices(services);
				}
			}
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(bundle.getString(key), outTxtSave));

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return addEditActivitiesAndServices(null);
	}
}
