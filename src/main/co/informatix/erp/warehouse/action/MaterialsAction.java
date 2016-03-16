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

import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.dao.TypeOfManagementDao;
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

	private String nameSearch;

	private Paginador paginador = new Paginador();

	private Materials materials;

	private List<Materials> materialsList;
	private List<SelectItem> materialTypeItems;
	private List<SelectItem> measureUnitItems;
	private List<SelectItem> managementTypeItems;

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
	 * @return paginador: The paging controller object.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :The paging controller object.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
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
	 * Method to initialize the fields in the search.
	 * 
	 * @return searchMaterials: Materials query method that redirects to the
	 *         template to manage materials.
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		this.materials = null;
		return searchMaterials();
	}

	/**
	 * Query the list of Materials.
	 * 
	 * @return gesMaterials: Navigation rule that redirects to manage materials.
	 */
	public String searchMaterials() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleRecursosHumanos = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		materialsList = new ArrayList<Materials>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(queryBuilder, parameters, bundle,
					jointSearchMessages);
			Long amount = materialsDao
					.materialsAmount(queryBuilder, parameters);
			if (amount != null) {
				paginador.paginar(amount);
			}
			if (amount != null && amount > 0) {
				materialsList = materialsDao.queryMaterials(
						paginador.getInicio(), paginador.getRango(),
						queryBuilder, parameters);
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
								bundleRecursosHumanos
										.getString("materials_label_s"),
								jointSearchMessages);
			}
			loadMaterialsDetails();
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMaterials";
	}

	/**
	 * This method builds the query to the advanced search and display messages
	 * depending on the search criteria selected by the user.
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
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			queryBuilder.append("WHERE UPPER(m.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to upload the details of a list of materials and their relations
	 * with other objects in the database.
	 * 
	 * @throws Exception
	 */
	private void loadMaterialsDetails() throws Exception {
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
	 * @throws Exception
	 */
	public void loadMaterialDetails(Materials materials) throws Exception {
		int materialId = materials.getIdMaterial();
		MaterialsType materialsType = (MaterialsType) materialsDao
				.queryMaterialObject("materialType", materialId);
		MeasurementUnits measurementUnits = (MeasurementUnits) materialsDao
				.queryMaterialObject("measurementUnits", materialId);
		TypeOfManagement typeOfManagement = (TypeOfManagement) materialsDao
				.queryMaterialObject("typeOfManagement", materialId);
		Hr responsable = (Hr) materialsDao.queryMaterialObject("responsable",
				materialId);

		materials.setMaterialType(materialsType);
		materials.setMeasurementUnits(measurementUnits);
		materials.setTypeOfManagement(typeOfManagement);
		materials.setResponsable(responsable);
	}

	/**
	 * Method to edit or create a new material.
	 * 
	 * @param materiales
	 *            : Material to modify.
	 * 
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
	 * @throws Exception
	 */
	private void loadComboBoxes() throws Exception {
		materialTypeItems = new ArrayList<SelectItem>();
		measureUnitItems = new ArrayList<SelectItem>();
		managementTypeItems = new ArrayList<SelectItem>();

		List<MaterialsType> materialTypes = materialsTypeDao
				.consultMaterialsTypes();
		if (materialTypes != null) {
			for (MaterialsType materialType : materialTypes) {
				materialTypeItems.add(new SelectItem(materialType
						.getIdMaterialsType(), materialType.getName()));
			}
		}
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
}
