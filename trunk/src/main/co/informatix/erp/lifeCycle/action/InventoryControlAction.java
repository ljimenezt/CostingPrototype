package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.action.MaterialsAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.entities.Materials;

/**
 * This class implements the logic of the relations between the material,
 * deposit and transactions of the database. The logic is calculate the
 * inventories of the material.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class InventoryControlAction implements Serializable {

	@EJB
	private MaterialsDao materialsDao;
	@EJB
	private DepositsDao depositsDao;

	private List<Materials> listInventory;
	private Paginador pagination = new Paginador();
	private String nameSearch;
	private int idMaterialType;

	/**
	 * @return listInventory: list of the inventory according with the deposit
	 */
	public List<Materials> getListInventory() {
		return listInventory;
	}

	/**
	 * @param listInventory
	 *            :list of the inventory according with the deposit
	 */
	public void setListInventory(List<Materials> listInventory) {
		this.listInventory = listInventory;
	}

	/**
	 * @return pagination: Management paged list of material inventory.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of material inventory.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Name of the assignment to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name of the assignment to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return idMaterialType : Material type identifier associated with the
	 *         materials of the inventory to consult
	 */
	public int getIdMaterialType() {
		return idMaterialType;
	}

	/**
	 * @param idMaterialType
	 *            : Material type identifier associated with the materials of
	 *            the inventory to consult to consult
	 */
	public void setIdMaterialType(int idMaterialType) {
		this.idMaterialType = idMaterialType;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of inventory.
	 * 
	 * @return consultInventory: Method to initialize the parameters of the
	 *         search and load the initial list of deposit of the inventory.
	 */
	public String searchInitialization() {
		idMaterialType = 0;
		nameSearch = "";
		return consultInventory();
	}

	/**
	 * Consult the list of the inventories control.
	 * 
	 * @return "gesInventory": navigation rule to manage inventories control
	 */
	public String consultInventory() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		MaterialsAction materialsAction = ControladorContexto
				.getContextBean(MaterialsAction.class);
		listInventory = new ArrayList<Materials>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch,
					materialsAction);
			Long quantity = materialsDao.materialsAmount(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listInventory = materialsDao.queryMaterials(pagination.getInicio(),
					pagination.getRango(), query, parameters);
			if ((listInventory == null || listInventory.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listInventory == null || listInventory.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("materials_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
			materialsAction.listMaterialsType();
			loadMaterialsDetails(materialsAction);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesInventory";
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 * @param materialsAction
	 *            : Class to material to get the items of the type of material
	 *            of the inventory
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch, MaterialsAction materialsAction) {
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		List<SelectItem> materialTypeItems = materialsAction
				.getMaterialTypeItems();
		boolean flag = false;
		if (this.idMaterialType != 0) {
			consult.append("WHERE m.materialType.idMaterialsType = :keyword1 ");
			SelectItem item = new SelectItem(this.idMaterialType, "keyword1");
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
			consult.append(flag ? "AND " : "WHERE ");
			consult.append("UPPER(m.name) LIKE UPPER(:keyword2) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword2");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to upload the details of a list of materials of the inventory and
	 * their relations with other objects in the database.
	 * 
	 * @param materialsAction
	 *            :Class to material to get the items of the type of material of
	 *            the inventory
	 * @throws Exception
	 */
	private void loadMaterialsDetails(MaterialsAction materialsAction)
			throws Exception {
		if (this.listInventory != null) {
			for (Materials material : this.listInventory) {
				materialsAction.loadMaterialDetails(material);
				calculateActualQuantity(material);
			}
		}
	}

	/**
	 * This method allow calculate the actual quantity of the material in the
	 * inventory
	 * 
	 * @param material
	 *            : Material Object to calculate the quantity
	 * @throws Exception
	 */
	private void calculateActualQuantity(Materials material) throws Exception {
		int idMaterial = material.getIdMaterial();
		Double actualQuantity = depositsDao.quantityMaterialsById(idMaterial);
		Double totalCost = depositsDao.calculateTotalCost(idMaterial);
		actualQuantity = (actualQuantity != null ? actualQuantity : 0d);
		totalCost = (totalCost != null ? totalCost : 0d);
		material.setActualQuantity(actualQuantity);
		material.setTotalCost(totalCost);
	}

}
