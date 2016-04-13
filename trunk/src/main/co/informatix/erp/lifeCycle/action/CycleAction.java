package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.CycleDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Cycle;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.services.dao.ServiceTypeDao;
import co.informatix.erp.services.entities.ServiceType;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.MaterialsType;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the cycles that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CycleAction implements Serializable {

	private List<Cycle> listCycles;
	private List<SelectItem> optionsCropNames;
	private List<SelectItem> optionsCrops;
	private List<SelectItem> itemsActivityName;
	private List<SelectItem> itemsMaterialsType;
	private List<SelectItem> itemsMaterials;
	private List<SelectItem> itemsMachinesType;
	private List<SelectItem> itemsServicesType;
	private Paginador pagination = new Paginador();
	private Cycle cycle;
	private Crops crops;
	private String nameSearch;
	private String nameDocument;
	private String folderFile;
	private String folderFileTemporal;
	private String units;
	private Date initialDateSearch;
	private Date finalDateSearch;
	private boolean loadDocumentTemporal;
	private boolean iconPdf;
	private boolean flagCrops;
	private boolean flagCycle;
	private boolean flagDate;
	private int idCrops;
	private int idCropsName;
	private int idMaterialsType;
	private int idMaterials;
	private int idMachinesType;
	private int idServicesType;
	private int idActivitiesName;
	private int quantity;
	private double quote;

	@EJB
	private FileUploadBean fileUploadBean;
	@EJB
	private CycleDao cycleDao;
	@EJB
	private CropsDao cropsDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private ActivityNamesDao activityNamesDao;
	@EJB
	private MaterialsTypeDao materialsTypeDao;
	@EJB
	private MaterialsDao materialsDao;
	@EJB
	private MachineTypesDao machineTypesDao;
	@EJB
	private ServiceTypeDao serviceTypeDao;
	@EJB
	private DepositsDao depositsDao;

	/**
	 * @return listCycles: cycles list.
	 */
	public List<Cycle> getListCycles() {
		return listCycles;
	}

	/**
	 * @param listCycles
	 *            : cycles list.
	 */
	public void setListCycles(List<Cycle> listCycles) {
		this.listCycles = listCycles;
	}

	/**
	 * @return optionsCropNames: crop name associated with a cycle.
	 */
	public List<SelectItem> getOptionsCropNames() {
		return optionsCropNames;
	}

	/**
	 * @param optionsCropNames
	 *            :crop name associated with a cycle.
	 */
	public void setptionsCropNames(List<SelectItem> optionsCropNames) {
		this.optionsCropNames = optionsCropNames;
	}

	/**
	 * @return optionsCrops: crops associated with a cycle.
	 */
	public List<SelectItem> getOptionsCrops() {
		return optionsCrops;
	}

	/**
	 * @param optionsCrops
	 *            :crops associated with a cycle.
	 */
	public void setOptionsCrops(List<SelectItem> optionsCrops) {
		this.optionsCrops = optionsCrops;
	}

	/**
	 * @return itemsActivityName: name of the activity associated with a crop.
	 */
	public List<SelectItem> getItemsActivityName() {
		return itemsActivityName;
	}

	/**
	 * @param itemsActivityName
	 *            : name of the activity associated with a crop.
	 */
	public void setItemsActivityName(List<SelectItem> itemsActivityName) {
		this.itemsActivityName = itemsActivityName;
	}

	/**
	 * @return itemsMaterialsType: List of items of the types of materials to be
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsMaterialsType() {
		return itemsMaterialsType;
	}

	/**
	 * @param itemsMaterialsType
	 *            :List of items of the types of materials to be loaded into the
	 *            combo in the user interface.
	 */
	public void setItemsMaterialsType(List<SelectItem> itemsMaterialsType) {
		this.itemsMaterialsType = itemsMaterialsType;
	}

	/**
	 * @return itemsMaterials: List of items of the materials to be loaded into
	 *         the combo in the user interface.
	 */
	public List<SelectItem> getItemsMaterials() {
		return itemsMaterials;
	}

	/**
	 * @param itemsMaterials
	 *            :List of items of the materials to be loaded into the combo in
	 *            the user interface.
	 */
	public void setItemsMaterials(List<SelectItem> itemsMaterials) {
		this.itemsMaterials = itemsMaterials;
	}

	/**
	 * @return itemsMachinesType: List of items of the materials to be loaded
	 *         into the combo in the user interface.
	 */
	public List<SelectItem> getItemsMachinesType() {
		return itemsMachinesType;
	}

	/**
	 * @param itemsMachinesType
	 *            :List of items of the types of materials to be loaded into the
	 *            combo in the user interface.
	 */
	public void setItemsMachinesType(List<SelectItem> itemsMachinesType) {
		this.itemsMachinesType = itemsMachinesType;
	}

	/**
	 * @return itemsServicesType: List of items of the types of services to be
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getItemsServicesType() {
		return itemsServicesType;
	}

	/**
	 * @param itemsServicesType
	 *            :List of items of the types of services to be loaded into the
	 *            combo in the user interface.
	 */
	public void setItemsServicesType(List<SelectItem> itemsServicesType) {
		this.itemsServicesType = itemsServicesType;
	}

	/**
	 * @return pagination: Management paged list of cycles.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of cycles.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
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
	}

	/**
	 * @return crops: Object of class crops.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            :Object of class crops.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
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
	 * @return nameDocument: file name that has the information associated to
	 *         the services.
	 */
	public String getNameDocument() {
		return nameDocument;
	}

	/**
	 * @param nameDocument
	 *            : file name that has the information associated to the
	 *            services.
	 */
	public void setNameDocument(String nameDocument) {
		this.nameDocument = nameDocument;
	}

	/**
	 * @return folderFile: route real folder where the pictures of the document
	 *         of the cycle are loaded.
	 */
	public String getFolderFile() {
		this.folderFile = Constantes.CARPETA_ARCHIVOS_SUBIDOS;
		return folderFile;
	}

	/**
	 * @return folderFileTemporal: path of the temporary folder where the
	 *         document of the cycle are loaded.
	 */
	public String getFolderFileTemporal() {
		this.folderFileTemporal = Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return folderFileTemporal;
	}

	/**
	 * @return units: materials units associated to the cycle.
	 */
	public String getUnits() {
		return units;
	}

	/**
	 * @param units
	 *            : materials units associated to the cycle.
	 */
	public void setUnits(String units) {
		this.units = units;
	}

	/**
	 * @return initialDateSearch: gets the initial date of the cycle to search
	 *         in a range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            :sets the initial date of the cycle to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the final date of the cycle to search in a
	 *         range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            :sets the final date of the cycle to search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * 
	 * @return fileUploadBean: Variable that gets the object for uploading
	 *         files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : field that gets the object for uploading files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return loadDocumentTemporal: Flag indicating whether the picture is
	 *         loaded from the temporary location or not
	 */
	public boolean getLoadDocumentTemporal() {
		return loadDocumentTemporal;
	}

	/**
	 * @param loadDocumentTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not
	 */
	public void setLoadDocumentTemporal(boolean loadDocumentTemporal) {
		this.loadDocumentTemporal = loadDocumentTemporal;
	}

	/**
	 * @return iconPdf: Flag indicating whether the file is loaded from the
	 *         temporary location or not
	 */
	public boolean getIconPdf() {
		return iconPdf;
	}

	/**
	 * @param iconPdf
	 *            : Flag indicating whether the file is loaded from the
	 *            temporary location or not
	 */
	public void setIconPdf(boolean iconPdf) {
		this.iconPdf = iconPdf;
	}

	/**
	 * @return flagCycle: Flag indicating whether a cycle will be edited or
	 *         added.
	 */
	public boolean getFlagCycle() {
		return flagCycle;
	}

	/**
	 * @return flagDate: Flag indicating whether there are cycles registered
	 *         later date.
	 */
	public boolean getFlagDate() {
		return flagDate;
	}

	/**
	 * @return idCrops: Crop identifier.
	 */
	public int getIdCrops() {
		return idCrops;
	}

	/**
	 * @param idCrops
	 *            : Crops identifier.
	 */
	public void setIdCrops(int idCrops) {
		this.idCrops = idCrops;
	}

	/**
	 * @return idCropsName: Crops name identifier.
	 */
	public int getIdCropsName() {
		return idCropsName;
	}

	/**
	 * @param idCropsName
	 *            : Crops name identifier.
	 */
	public void setIdCropsName(int idCropsName) {
		this.idCropsName = idCropsName;
	}

	/**
	 * @return idMaterialsType: materials type identifier.
	 */
	public int getIdMaterialsType() {
		return idMaterialsType;
	}

	/**
	 * @param idMaterialsType
	 *            : materials type identifier.
	 */
	public void setIdMaterialsType(int idMaterialsType) {
		this.idMaterialsType = idMaterialsType;
	}

	/**
	 * @return idMaterials: materials identifier.
	 */
	public int getIdMaterials() {
		return idMaterials;
	}

	/**
	 * @param idMaterials
	 *            : materials identifier.
	 */
	public void setIdMaterials(int idMaterials) {
		this.idMaterials = idMaterials;
	}

	/**
	 * @return idMachinesType: machines type identifier.
	 */
	public int getIdMachinesType() {
		return idMachinesType;
	}

	/**
	 * @param idMachinesType
	 *            : machines type identifier.
	 */
	public void setIdMachinesType(int idMachinesType) {
		this.idMachinesType = idMachinesType;
	}

	/**
	 * @return idServicesType: services type identifier.
	 */
	public int getIdServicesType() {
		return idServicesType;
	}

	/**
	 * @param idServicesType
	 *            : services type identifier.
	 */
	public void setIdServicesType(int idServicesType) {
		this.idServicesType = idServicesType;
	}

	/**
	 * @return idActivitiesName: activitiesName identifier.
	 */
	public int getIdActivitiesName() {
		return idActivitiesName;
	}

	/**
	 * @param idActivitiesName
	 *            : activitiesName identifier.
	 */
	public void setIdActivitiesName(int idActivitiesName) {
		this.idActivitiesName = idActivitiesName;
	}

	/**
	 * @return quantity: materials quantity associated to the cycle.
	 */
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            : materials quantity associated to the cycle.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return quote: services quote associated to the cycle.
	 */
	public double getQuote() {
		return quote;
	}

	/**
	 * @param quote
	 *            : services quote associated to the cycle.
	 */
	public void setQuote(double quote) {
		this.quote = quote;
	}

	/**
	 * This method allows initialize all the cycle.
	 */
	public void initializeSearch() {
		this.nameSearch = "";
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		this.cycle = new Cycle();
		consultCycles();
	}

	/**
	 * Method to initialize cycles.
	 * 
	 * @modify 22/03/2016 Andres.Gomez
	 * 
	 * @param cycle
	 *            :Object of cycle are adding or editing.
	 * 
	 * @return gesCycle: Template redirects to management Cycle.
	 * 
	 */
	public String initializeCycle(Cycle cycle) {
		try {
			if (cycle != null) {
				crops = cropsDao.cropsById(cycle.getCrops().getIdCrop());
			} else {
				crops = cropsDao.defaultSearchCrop(Constantes.ID_CROP_DEFAULT);
			}
			this.idCrops = crops.getIdCrop();
			this.idCropsName = crops.getCropNames().getIdCropName();
			if (crops != null) {
				initializeSearch();
			}
			this.flagCrops = false;
			loadCropNames();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCycle";
	}

	/**
	 * Method to edit or create a new assignment of cycle.
	 * 
	 * @modify 22/03/2016 Andres.Gomez
	 * 
	 * @param cycle
	 *            :Object of cycle are adding or editing.
	 * 
	 * @return regCycle: Template redirects to register Cycle.
	 * 
	 */
	public String addEditCycles(Cycle cycle) {
		try {
			this.crops = new Crops();
			this.crops.setCropNames(new CropNames());
			if (cycle != null) {
				this.cycle = cycle;
				this.idCrops = this.cycle.getCrops().getIdCrop();
				this.crops = cropsDao.cropsById(idCrops);
				this.idCropsName = this.crops.getCropNames().getIdCropName();
				loadCombos();
				this.flagCycle = true;
				this.flagDate = cycleDao.consultCycleByIdCropsAndIdActivity(
						this.idCrops, this.cycle.getActiviyNames()
								.getIdActivityName(), this.cycle
								.getInitialDateTime());
			} else {
				if (this.idCrops == 0 && this.idCropsName == 0) {
					this.crops = cropsDao
							.defaultSearchCrop(Constantes.ID_CROP_DEFAULT);
					this.idCrops = this.getCrops().getIdCrop();
					this.idCropsName = this.getCrops().getCropNames()
							.getIdCropName();
				}
				this.cycle = new Cycle();
				this.cycle.setActiviyNames(new ActivityNames());
				this.flagCycle = false;
				this.flagDate = false;
			}
			this.flagCrops = false;
			clearFields();
			loadActivities();
			loadCropNames();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regCycle";
	}

	/**
	 * This method allows clean the fields.
	 * 
	 */
	private void clearFields() {
		this.idMachinesType = 0;
		this.idMaterials = 0;
		this.idMaterialsType = 0;
		this.idServicesType = 0;
		this.quantity = 0;
		this.units = "";
		this.quote = 0;
		this.nameDocument = null;
		this.loadDocumentTemporal = true;
	}

	/**
	 * This method allows load of name crops list.
	 * 
	 * @throws Exception
	 * 
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
		loadCropNamesCrop();
	}

	/**
	 * This method allows load the name cropName list.
	 * 
	 */
	public void loadCropNamesCrop() {
		try {
			if (this.flagCrops) {
				this.idCrops = 0;
			}
			optionsCrops = new ArrayList<SelectItem>();
			List<Crops> listCropsActive = cropsDao
					.consultCropNamesCropsCurrent(this.idCropsName);
			if (listCropsActive != null) {
				for (Crops crops : listCropsActive) {
					optionsCrops.add(new SelectItem(crops.getIdCrop(), crops
							.getDescription()));
				}
			}
			this.flagCrops = true;
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the machines type list.
	 * 
	 */
	public void loadMaterialsType() {
		try {
			itemsMaterialsType = new ArrayList<SelectItem>();
			List<MaterialsType> materialsType = materialsTypeDao
					.consultMaterialsTypes();
			if (materialsType != null) {
				for (MaterialsType materialsTypes : materialsType) {
					itemsMaterialsType.add(new SelectItem(materialsTypes
							.getIdMaterialsType(), materialsTypes.getName()));
				}
				loadMaterials();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the machines list.
	 */
	public void loadMaterials() {
		try {
			itemsMaterials = new ArrayList<SelectItem>();
			List<Materials> materialsList = materialsDao
					.queryMaterialsByType(idMaterialsType);
			if (materialsList != null) {
				for (Materials materials : materialsList) {
					itemsMaterials.add(new SelectItem(
							materials.getIdMaterial(), materials.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the machines list.
	 * 
	 */
	public void loadMachines() {
		try {
			itemsMachinesType = new ArrayList<SelectItem>();
			List<MachineTypes> listMachinetypes = machineTypesDao
					.listMachineType();
			if (listMachinetypes != null) {
				for (MachineTypes machineTypes : listMachinetypes) {
					itemsMachinesType.add(new SelectItem(machineTypes
							.getIdMachineType(), machineTypes.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the machines type list.
	 * 
	 */
	public void loadServices() {
		try {
			itemsServicesType = new ArrayList<SelectItem>();
			List<ServiceType> listServiceType = serviceTypeDao
					.consultServicesTypes();
			if (listServiceType != null) {
				for (ServiceType serviceType : listServiceType) {
					itemsServicesType.add(new SelectItem(serviceType
							.getIdServiceType(), serviceType.getDescription()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the activities list.
	 * 
	 */
	public void loadActivities() {
		try {
			itemsActivityName = new ArrayList<SelectItem>();
			List<ActivityNames> tiposActivityNames = activityNamesDao
					.queryActivityNamesXCrop(this.idCrops);
			if (tiposActivityNames != null) {
				for (ActivityNames activitiesName : tiposActivityNames) {
					itemsActivityName.add(new SelectItem(activitiesName
							.getIdActivityName(), activitiesName
							.getActivityName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method validates the burden of combos.
	 * 
	 */
	private void loadCombos() {
		if (this.cycle.getMaterialsRequired()) {
			loadMaterialsType();
			loadMaterials();
		}
		if (this.cycle.getMachineRequired()) {
			loadMachines();
		}
		if (this.cycle.getServiceRequired()) {
			loadServices();
		}
	}

	/**
	 * See the list of cycles depending on the crop.
	 */
	public void consultCycles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listCycles = new ArrayList<Cycle>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advanceSearchCycles(consult, parameters, bundle,
					unionMessagesSearch);
			Long amount = cycleDao.amountCycleCrop(consult, parameters,
					this.idCrops);
			if (amount != null) {
				pagination.paginar(amount);
			}
			if (amount != null && amount > 0) {
				listCycles = cycleDao.consultCycleByCrop(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters, this.idCrops);
			}
			if ((listCycles == null || listCycles.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listCycles == null || listCycles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("cycle_label_s"),
								unionMessagesSearch);
			}
			validaciones.setMensajeBusqueda(messageSearch);
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
	private void advanceSearchCycles(StringBuilder consult,
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
			consult.append("AND c.initialDateTime BETWEEN :initialDateSearch AND :finalDateSearch ");
			SelectItem item = new SelectItem(initialDateSearch,
					"initialDateSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(finalDateSearch,
					"finalDateSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + formato.format(this.initialDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + formato.format(finalDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateTo);
		}

	}

	/**
	 * Method to remove a cycles of the database.
	 * 
	 * @return initializeSearch(): Consult the list of cycles and returns to
	 *         manage view.
	 */
	public void deleteCycle() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			cycleDao.deleteCycle(cycle);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), cycle
							.getActiviyNames().getActivityName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(bundle
					.getString("message_existe_relacion_eliminar"), cycle
					.getActiviyNames().getActivityName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		initializeSearch();
	}

	/**
	 * This method allows update the budget cost for a cycle.
	 * 
	 * @return initializeCycle(): Consult the list of cycles and returns to
	 *         manage view.
	 */
	public String updateCycleBudget() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		try {
			if (fromModal) {
				cycleDao.editCycle(this.cycle);
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString(mensajeRegistro), this.cycle
								.getActiviyNames().getActivityName()));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeCycle(this.cycle);
	}

	/**
	 * Method used to save or edit cycles
	 * 
	 * @return initializeCycle(): Redirects to manage the list of cycles with
	 *         cycles updated.
	 */
	public String saveUpdateCycle() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";

		try {
			this.crops.setIdCrop(this.idCrops);
			this.cycle.setCrops(this.crops);
			if (cycle.getIdCycle() != 0) {
				cycleDao.editCycle(cycle);
			} else {
				mensajeRegistro = "message_registro_guardar";
				cycleDao.saveCycle(cycle);
			}
			String activitisName = (String) ValidacionesAction.getLabel(
					itemsActivityName, cycle.getActiviyNames()
							.getIdActivityName());
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), activitisName));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeCycle(cycle);
	}

	/**
	 * Method allows you to load the file system.
	 * 
	 * @param e
	 *            : Fileupload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAccepted[] = Constantes.EXT_DOC_PDF.split(", ");
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFileTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		long maximuSizeFile = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(extAccepted,
				locations, maximuSizeFile);
		String message = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			message = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"), maximuSizeFile,
					"MB");
			ControladorContexto.mensajeError(
					"formRegisterCycle:uploadDocument", format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			message = "error_carga_archivo";
		}
		if (!"".equals(message)) {
			ControladorContexto.mensajeError(
					"formRegisterCycle:uploadDocument",
					bundle.getString(message));
		}
		this.nameDocument = fileUploadBean.getFileName();
		if (Constantes.OK.equals(resultUpload)) {
			String suffix = FilenameUtils.getExtension(this.nameDocument);
			if (suffix.equals("pdf")) {
				iconPdf = true;
			} else {
				iconPdf = false;
			}
		}

	}

	/**
	 * Delete the file name.
	 * 
	 */
	public void deleteFilename() {
		if (this.nameDocument != null && !"".equals(this.nameDocument)
				&& this.loadDocumentTemporal) {
			deleteFile(this.nameDocument);
		}
		this.nameDocument = null;
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void deleteFile(String fileName) {
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFileTemporal() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * This method allows validate the materials quantity in the deposit.
	 * 
	 */
	public void validateQuantityMaterials() {
		try {
			ResourceBundle bundle = ControladorContexto
					.getBundle("mensajeWarehouse");
			StringBuilder consult = new StringBuilder();
			List<SelectItem> parameters = new ArrayList<SelectItem>();
			if (this.cycle.getMaterialsRequired()) {
				boolean materialFlag = depositsDao
						.associatedMaterialsDeposits(idMaterials);
				String materialName = (String) ValidacionesAction.getLabel(
						this.itemsMaterials, idMaterials);
				if (materialFlag) {
					Double quantityActual = depositsDao
							.quantityMaterialsById(idMaterials);
					if (this.quantity > quantityActual) {
						ControladorContexto
								.mensajeError(
										"formRegisterCycle:quantity",
										MessageFormat.format(
												bundle.getString("deposits_message_not_enough_materials"),
												materialName));
					}
				} else {
					ControladorContexto
							.mensajeError(
									"formRegisterCycle:quantity",
									MessageFormat.format(
											bundle.getString("deposits_message_no_materials"),
											materialName));
				}
			}

			if (flagCycle) {
				consult.append("AND c.idCycle <>:idCycle ");
				SelectItem item = new SelectItem(cycle.getIdCycle(), "idCycle");
				parameters.add(item);
			}

			if (!flagDate) {
				Date dateInitial = cycleDao.consultDateCycle(this.idCrops,
						this.cycle.getActiviyNames().getIdActivityName(),
						this.cycle.getInitialDateTime(), consult, parameters);
				if (dateInitial != null) {
					ControladorContexto.mensajeErrorArg1(
							"formRegisterCycle:fechaInicio",
							"cycle_message_must_enter_late_date",
							"messageLifeCycle", dateInitial);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow set the measurement unit and update the material type.
	 * 
	 * @author Andres.Gomez
	 * @modify 07/04/2016 Wilhelm.Boada
	 */
	public void loadUnits() {
		try {
			Materials material = materialsDao.consultMaterialsById(idMaterials);
			units = material.getMeasurementUnits().getName();
			if (this.idMaterialsType == 0
					|| this.idMaterialsType != material.getMaterialType()
							.getIdMaterialsType()) {
				this.idMaterialsType = material.getMaterialType()
						.getIdMaterialsType();
				loadMaterials();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow set the cycle number.
	 * 
	 */
	public void calculateCycleNumber() {
		try {
			if (!flagCycle) {
				Cycle cycleNumber = cycleDao.consultCycleNumber(this.idCrops,
						cycle.getActiviyNames().getIdActivityName());
				if (cycleNumber != null) {
					cycle.setCycleNumber(cycleNumber.getCycleNumber() + 1);
				} else {
					cycle.setCycleNumber(1);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}