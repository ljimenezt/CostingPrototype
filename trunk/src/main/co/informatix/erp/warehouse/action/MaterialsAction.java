package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.action.ActivityMaterialsAction;
import co.informatix.erp.costs.dao.ActivityMaterialsDao;
import co.informatix.erp.costs.entities.ActivityMaterials;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControllerAccounting;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.dao.TypeOfManagementDao;
import co.informatix.erp.warehouse.entities.InvoiceItems;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.MaterialsType;
import co.informatix.erp.warehouse.entities.MeasurementUnits;
import co.informatix.erp.warehouse.entities.TypeOfManagement;

/**
 * This class implements the business logic for creating, updating and removing
 * materials.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MaterialsAction implements Serializable {

	@EJB
	private MaterialsDao materialsDao;
	@EJB
	private MaterialsTypeDao materialsTypeDao;
	@EJB
	private MeasurementUnitsDao measurementUnitsDao;
	@EJB
	private TypeOfManagementDao typeOfManagementDao;
	@EJB
	private ActivityMaterialsDao activityMaterialsDao;
	@EJB
	private DepositsDao depositsDao;

	private String nameSearch;

	private Paginador pagination = new Paginador();
	private Paginador pagerForm = new Paginador();
	private ActivityMaterialsAction activityMaterialsAction;

	private Materials materials;

	private List<Materials> materialsList;
	private List<Materials> materialSelected;
	private List<SelectItem> materialTypeItems;
	private List<SelectItem> measureUnitItems;
	private List<SelectItem> managementTypeItems;

	private int idMaterialType;
	private boolean fromModal;
	private boolean state;

	/**
	 * @return nameSearch : The material name that is going to be queried.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : The material name that is going to be queried.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return pagination: The paging controller object.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :The paging controller object.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return pagerForm: The paging controller object.
	 */
	public Paginador getPagerForm() {
		return pagerForm;
	}

	/**
	 * @param pagerForm
	 *            :The paging controller object.
	 */
	public void setPagerForm(Paginador pagerForm) {
		this.pagerForm = pagerForm;
	}

	/**
	 * @return materials: Material object that is going to be managed.
	 */
	public Materials getMaterials() {
		return materials;
	}

	/**
	 * @param materials
	 *            : Material object that is going to be managed.
	 */
	public void setMaterials(Materials materials) {
		this.materials = materials;
	}

	/**
	 * @return materialsList: List of materials that is loaded into the user
	 *         interface.
	 */
	public List<Materials> getMaterialsList() {
		return materialsList;
	}

	/**
	 * @param materialsList
	 *            : List of materials that is loaded into the user interface.
	 */
	public void setMaterialsList(List<Materials> materialsList) {
		this.materialsList = materialsList;
	}

	/**
	 * @return materialSelected: List of materials that is selected by user.
	 */
	public List<Materials> getMaterialSelected() {
		return materialSelected;
	}

	/**
	 * @param materialSelected
	 *            :List of materials that is selected by user.
	 */
	public void setMaterialSelected(List<Materials> materialSelected) {
		this.materialSelected = materialSelected;
	}

	/**
	 * @return materialTypeItems: List of items of the types of materials to be
	 *         loaded into the combo in the user interface.
	 */
	public List<SelectItem> getMaterialTypeItems() {
		return materialTypeItems;
	}

	/**
	 * @param materialTypeItems
	 *            :List of items of the types of materials to be loaded into the
	 *            combo in the user interface.
	 */
	public void setMaterialTypeItems(List<SelectItem> materialTypeItems) {
		this.materialTypeItems = materialTypeItems;
	}

	/**
	 * @return measureUnitItems: List of items from the units of measure of
	 *         materials are loaded into the combo in the user interface.
	 */
	public List<SelectItem> getMeasureUnitItems() {
		return measureUnitItems;
	}

	/**
	 * @param measureUnitItems
	 *            :List of items from the units of measure of materials are
	 *            loaded into the combo in the user interface.
	 */
	public void setMeasureUnitItems(List<SelectItem> measureUnitItems) {
		this.measureUnitItems = measureUnitItems;
	}

	/**
	 * @return managementTypeItems :List of items of materials management types
	 *         that are loaded into the combo in the user interface.
	 */
	public List<SelectItem> getManagementTypeItems() {
		return managementTypeItems;
	}

	/**
	 * @param managementTypeItems
	 *            :List of items of materials management types that are loaded
	 *            into the combo in the user interface.
	 */
	public void setManagementTypeItems(List<SelectItem> managementTypeItems) {
		this.managementTypeItems = managementTypeItems;
	}

	/**
	 * @return idMaterialType : Material type identifier associated with the
	 *         materials to consult
	 */
	public int getIdMaterialType() {
		return idMaterialType;
	}

	/**
	 * @param idMaterialType
	 *            : Material type identifier associated with the materials to
	 *            consult
	 */
	public void setIdMaterialType(int idMaterialType) {
		this.idMaterialType = idMaterialType;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @modify 01/04/2016 Wilhelm.Boada
	 * 
	 * @return searchMaterials: Materials query method that redirects to the
	 *         template to manage materials.
	 */
	public String initializeSearch() {
		String param2 = ControladorContexto.getParam("param2");
		fromModal = (param2 != null && "si".equals(param2)) ? true : false;
		this.idMaterialType = 0;
		this.nameSearch = "";
		if (fromModal) {
			loadListMaterialSelected();
			pagerForm = new Paginador();
		} else {
			this.materials = null;
		}
		return searchMaterials();
	}

	/**
	 * Consult initialized materials considering a state that allows changing
	 * the logic to check.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void initializeMaterials() {
		try {
			this.state = true;
			fromModal = true;
			this.nameSearch = "";
			pagerForm = new Paginador();
			listMaterialsType();
			searchMaterials();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the list that user already selected
	 */
	private void loadListMaterialSelected() {
		InvoiceItemsAction invoiceItemsAction = ControladorContexto
				.getContextBean(InvoiceItemsAction.class);
		List<InvoiceItems> invoiceItemsList = new ArrayList<InvoiceItems>();
		materialSelected = new ArrayList<Materials>();
		invoiceItemsList = invoiceItemsAction.getInvoiceItemsList();
		if (invoiceItemsList != null && invoiceItemsList.size() > 0) {
			for (InvoiceItems invoiceItem : invoiceItemsList) {
				Materials material = invoiceItem.getMaterial();
				materialSelected.add(material);
			}
		}
	}

	/**
	 * Query the list of Materials.
	 * 
	 * @modify 30/03/2016 Liseth.Jimenez
	 * @modify 01/04/2016 Wilhelm.Boada
	 * 
	 * @return gesMaterials: Navigation rule that redirects to manage materials.
	 */
	public String searchMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		materialsList = new ArrayList<Materials>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		String giveBack = fromModal || state ? "" : "gesMaterials";
		try {
			advancedSearch(queryBuilder, parameters, bundle,
					jointSearchMessages);
			Long amount = materialsDao
					.materialsAmount(queryBuilder, parameters);
			if (amount != null) {
				if (fromModal) {
					pagerForm.paginarRangoDefinido(amount, 5);
					pagerForm.setOpcion('f');
					materialsList = materialsDao.queryMaterials(
							pagerForm.getInicio(), pagerForm.getRango(),
							queryBuilder, parameters);
				} else {
					pagination.paginar(amount);
					pagination.setOpcion('f');
					materialsList = materialsDao.queryMaterials(
							pagination.getInicio(), pagination.getRango(),
							queryBuilder, parameters);
				}
			}
			if ((materialsList == null || materialsList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (materialsList == null || materialsList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("materials_label_s"),
								jointSearchMessages);
			}
			if (state) {
				persistMaterials();
			}
			listMaterialsType();
			loadMaterialsDetails();
			if (fromModal) {
				validation.setMensajeBusquedaPopUp(searchMessage);
			} else {
				validation.setMensajeBusqueda(searchMessage);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return giveBack;
	}

	/**
	 * This method builds the query to the advanced search and display messages
	 * depending on the search criteria selected by the user.
	 * 
	 * @modify 30/03/2016 Liseth.Jimenez
	 * @modify 30/03/2016 Wilhelm.Boada
	 * 
	 * @param queryBuilder
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * @param unionMessagesSearch
	 *            : Search message.
	 */
	private void advancedSearch(StringBuilder queryBuilder,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		boolean flag = false;
		if (this.idMaterialType != 0) {
			queryBuilder
					.append("WHERE m.materialType.idMaterialsType = :keyword3 ");
			SelectItem item = new SelectItem(this.idMaterialType, "keyword3");
			parameters.add(item);
			String materialTypeName = (String) ValidacionesAction.getLabel(
					materialTypeItems, this.idMaterialType);
			unionMessagesSearch.append(bundleWarehouse
					.getString("materials_type_label")
					+ ": "
					+ '"'
					+ materialTypeName + '"' + " ");
			flag = true;
		}
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			if (flag) {
				queryBuilder.append("AND ");
			} else {
				queryBuilder.append("WHERE ");
				flag = true;
			}
			queryBuilder.append("UPPER(m.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
		if (fromModal && !state) {
			if (materialSelected != null && materialSelected.size() > 0) {
				queryBuilder.append(flag ? "AND " : "WHERE ");
				queryBuilder.append("m NOT IN (:materialSelected) ");
				SelectItem item = new SelectItem(this.materialSelected,
						"materialSelected");
				parameters.add(item);
			}
		}
		if (state) {
			if (ControladorContexto.getFacesContext() != null) {
				activityMaterialsAction = ControladorContexto
						.getContextBean(ActivityMaterialsAction.class);
			}
			queryBuilder.append(flag ? "AND " : "WHERE ");
			queryBuilder.append("m NOT IN ");
			queryBuilder.append("(SELECT ma FROM ActivityMaterials am ");
			queryBuilder.append("JOIN am.activityMaterialsPK.materials ma ");
			queryBuilder.append("JOIN am.activityMaterialsPK.activities ac ");
			queryBuilder.append("WHERE ac.idActivity=:idActivity) ");
			SelectItem item = new SelectItem(this.activityMaterialsAction
					.getSelectedActivity().getIdActivity(), "idActivity");
			parameters.add(item);
		}
		flag = false;
	}

	/**
	 * Method to upload the details of a list of materials and their relations
	 * with other objects in the database.
	 */
	private void loadMaterialsDetails() {
		if (this.materialsList != null) {
			for (Materials material : this.materialsList) {
				loadMaterialDetails(material);
			}
		}
	}

	/**
	 * Method of uploading the details of a material or relationships with other
	 * objects in the database.
	 * 
	 * @param materials
	 *            : Materials to load its details.
	 */
	public void loadMaterialDetails(Materials materials) {
		int materialId = materials.getIdMaterial();
		try {
			MaterialsType materialsType = (MaterialsType) materialsDao
					.queryMaterialObject("materialType", materialId);
			MeasurementUnits measurementUnits = (MeasurementUnits) materialsDao
					.queryMaterialObject("measurementUnits", materialId);
			TypeOfManagement typeOfManagement = (TypeOfManagement) materialsDao
					.queryMaterialObject("typeOfManagement", materialId);
			Hr responsable = (Hr) materialsDao.queryMaterialObject(
					"responsable", materialId);
			materials.setMaterialType(materialsType);
			materials.setMeasurementUnits(measurementUnits);
			materials.setTypeOfManagement(typeOfManagement);
			materials.setResponsable(responsable);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to edit or create a new material.
	 * 
	 * @param materiales
	 *            : Material to modify.
	 * @return "regMaterials": Redirects to the register materials template.
	 */
	public String addEditMaterials(Materials materiales) {
		try {
			if (materiales != null) {
				this.materials = materiales;
				Hr personInCharge = (Hr) materialsDao.queryMaterialObject(
						"responsable", materiales.getIdMaterial());
				if (personInCharge != null) {
					this.materials.setResponsable(personInCharge);
				}
			} else {
				this.materials = new Materials();
				this.materials.setResponsable(new Hr());
			}
			loadComboBoxes();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMaterials";
	}

	/**
	 * This method loads combo in the register a new material template.
	 * 
	 * @modify 30/03/2016 Liseth.Jimenez
	 * 
	 * @throws Exception
	 */
	private void loadComboBoxes() throws Exception {
		measureUnitItems = new ArrayList<SelectItem>();
		managementTypeItems = new ArrayList<SelectItem>();
		listMaterialsType();

		List<MeasurementUnits> measureUnits = measurementUnitsDao
				.consultMeasurementsUnits();
		if (measureUnits != null) {
			for (MeasurementUnits mUnit : measureUnits) {
				measureUnitItems.add(new SelectItem(mUnit
						.getIdMeasurementUnits(), mUnit.getName()));
			}
		}
		List<TypeOfManagement> managementType = typeOfManagementDao
				.queryTypesOfManagements();
		if (managementType != null) {
			for (TypeOfManagement mType : managementType) {
				managementTypeItems.add(new SelectItem(mType
						.getIdTypeOfManagement(), mType.getName()));
			}
		}
	}

	/**
	 * Method to erase the person in charge.
	 */
	public void erasePersonInCharge() {
		this.materials.setResponsable(new Hr());
	}

	/**
	 * Method to load the person in charge.
	 * 
	 * @param personInCharge
	 *            : The selected person in charge.
	 */
	public void loadPersonInCharge(Hr personInCharge) {
		this.materials.setResponsable(personInCharge);
	}

	/**
	 * Method that deletes materials of the database.
	 * 
	 * @return searchMaterials: Query the list of materials and redirects to
	 *         manage materials template.
	 */
	public String deleteMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			materialsDao.deleteMateriales(materials);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					materials.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					materials.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchMaterials();
	}

	/**
	 * Save or edit the materials.
	 * 
	 * @return searchMaterials: Redirects to manage materials with a list of
	 *         updated materials.
	 */
	public String saveMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		try {
			if (materials.getIdMaterial() != 0) {
				materialsDao.editMaterials(materials);
			} else {
				registerMessage = "message_registro_guardar";
				materialsDao.saveMaterials(materials);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), materials.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchMaterials();
	}

	/**
	 * Method that loads a materials type list.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @throws Exception
	 */
	public void listMaterialsType() throws Exception {
		this.materialTypeItems = new ArrayList<SelectItem>();
		List<MaterialsType> materialsTypeList = materialsTypeDao
				.consultMaterialsTypes();
		if (materialsTypeList != null) {
			for (MaterialsType materialType : materialsTypeList) {
				this.materialTypeItems.add(new SelectItem(materialType
						.getIdMaterialsType(), materialType.getName()));
			}
		}
	}

	/**
	 * Method that validates the material name and presentation to check that
	 * there is no other record with the same attributes in the database.
	 * 
	 * @author Sergio.Gelves
	 * @modify 11/04/2016 Liseth.Jimenez
	 */
	public void validateMaterialPresentation() {
		try {
			Materials materialAux = materialsDao.materialByNamePresentation(
					this.materials.getName(), this.materials.getPresentation(),
					this.materials.getMeasurementUnits()
							.getIdMeasurementUnits(), this.materials
							.getIdMaterial());
			if (materialAux != null) {
				ControladorContexto.mensajeErrorEspecifico(
						"formMaterials:txtNombre",
						"materials_label_repeated_name_presentation",
						"mensajeWarehouse");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for maintaining selected materials regardless of
	 * whether this materials run the search again.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void persistMaterials() throws Exception {
		if (this.materialsList != null) {
			for (Materials material : this.materialsList) {
				double quantityBudget = 0;
				for (ActivityMaterials activityMaterialsSelected : activityMaterialsAction
						.getListActivityMaterials()) {
					int idMaterialsSelected = activityMaterialsSelected
							.getActivityMaterialsPK().getMaterials()
							.getIdMaterial();
					if (material.getIdMaterial() == idMaterialsSelected) {
						material.setSelected(true);
						quantityBudget = activityMaterialsSelected
								.getQuantityBudget();
					}
				}
				Double total = activityMaterialsDao
						.calculateTotalQuantityBudgetByMaterial(material
								.getIdMaterial());
				if (total != null) {
					total = ControllerAccounting.add(total, quantityBudget);
				}
				material.setTotalMaterialsBudget(total);
				material.setTotalMaterialsDeposits(depositsDao
						.quantityMaterialsById(material.getIdMaterial(), null));
			}
		}
	}
}