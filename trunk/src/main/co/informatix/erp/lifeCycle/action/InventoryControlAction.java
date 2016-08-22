package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ReportsController;
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

	private int idMaterialType;
	private boolean range;

	private List<Materials> listInventory;
	private List<Integer> listDepositsIds;
	private Paginador pagination = new Paginador();
	private Materials materialSelected;

	private String nameSearch;

	private Date initialDate;
	private Date finalDate;

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
	 * @return materialSelected: Materials Object selected by the user
	 */
	public Materials getMaterialSelected() {
		return materialSelected;
	}

	/**
	 * @param materialSelected
	 *            :Materials Object selected by the user
	 */
	public void setMaterialSelected(Materials materialSelected) {
		this.materialSelected = materialSelected;
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
	 * @return range: This flag indicate if the query to list the material have
	 *         a date of range
	 */
	public boolean isRange() {
		return range;
	}

	/**
	 * @param range
	 */
	public void setRange(boolean range) {
		this.range = range;
	}

	/**
	 * @return initialDate: Gets the initial date to consult the inventory
	 */
	public Date getInitialDate() {
		return initialDate;
	}

	/**
	 * @param initialDate
	 *            :Sets the initial date to consult the inventory
	 */
	public void setInitialDate(Date initialDate) {
		this.initialDate = initialDate;
	}

	/**
	 * @return finalDate : Gets final date to consult the inventory
	 */
	public Date getFinalDate() {
		return finalDate;
	}

	/**
	 * @param finalDate
	 *            :Sets final date to consult the inventory
	 */
	public void setFinalDate(Date finalDate) {
		this.finalDate = finalDate;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of inventory.
	 * 
	 * @return consultInventory: Method to initialize the parameters of the
	 *         search and load the initial list of deposit of the inventory.
	 */
	public String searchInitialization() {
		this.idMaterialType = 0;
		this.nameSearch = "";
		this.initialDate = null;
		this.finalDate = null;
		this.range = false;
		this.materialSelected = null;
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
	 * This flag indicate if the query to list the material has a date range
	 */
	public void consultInventoryByRange() {
		this.range = this.initialDate != null && this.finalDate != null ? true
				: false;
		if (this.range) {
			this.finalDate = ControladorFechas.finDeDia(finalDate);
			this.listDepositsIds = depositsDao
					.consultIdsMaterialXDepositsByRange(ControladorFechas
							.finDeDia(finalDate));
		}
		consultInventory();
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
			flag = true;
		}
		if (range) {
			this.finalDate = ControladorFechas.finDeDia(finalDate);
			String formatDate = Constantes.DATE_FORMAT_MESSAGE_SIMPLE;
			consult.append(flag ? "AND " : "WHERE ");
			consult.append("m.idMaterial IN :keyword3 ");
			SelectItem item = new SelectItem(this.listDepositsIds, "keyword3");
			parameters.add(item);

			unionMessagesSearch.append(bundle.getString("label_start_date")
					+ ": "
					+ '"'
					+ ControladorFechas
							.formatDate(this.initialDate, formatDate) + '"'
					+ " ");
			unionMessagesSearch.append(bundle.getString("label_end_date")
					+ ": " + '"'
					+ ControladorFechas.formatDate(this.finalDate, formatDate)
					+ '"' + " ");
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
		Double actualQuantity = depositsDao.quantityMaterialsById(idMaterial,
				this.finalDate);
		Double totalCost = depositsDao.calculateTotalCost(idMaterial,
				this.finalDate);
		actualQuantity = (actualQuantity != null ? actualQuantity : 0d);
		totalCost = (totalCost != null ? totalCost : 0d);
		material.setActualQuantity(actualQuantity);
		material.setTotalCost(totalCost);
	}

	/**
	 * Selects a single material of the inventory for display the deposits
	 * associated
	 * 
	 * @param materialSelected
	 *            : materials selected on the view
	 */
	public void selectMaterialByInventory(Materials materialSelected) {
		this.materialSelected = new Materials();
		for (Materials material : this.listInventory) {
			material.setSelected(false);
			if (material.getIdMaterial() == materialSelected.getIdMaterial()) {
				material.setSelected(true);
				this.materialSelected = material;
			}
		}
	}

	/**
	 * This method allow consult the inventories information and generate the
	 * report
	 */
	public void generateReportInventory() {
		ReportsController reportsController = ControladorContexto
				.getContextBean(ReportsController.class);
		StringBuilder query = new StringBuilder();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		reportAdvanceSearch(query, parameters);
		try {
			List<Date> listMonths = new ArrayList<>();
			if (this.initialDate != null && this.finalDate != null) {
				listMonths = ControladorFechas.getDatesBetweenTwoDates(
						this.initialDate, this.finalDate);
			} else {
				listMonths = depositsDao.consultMonths(query, parameters);
			}
			List<Object[]> listInventory = depositsDao
					.consultoInventoryByDepositReport(query, parameters);
			int count = 0;
			Double actualQuantity = 0d;
			int idMaterialAux = 0;
			int month = 0;
			int year = 0;
			Double totalInitialQuantity = 0d;
			for (Object[] object : listInventory) {
				int idMaterial = Integer.parseInt(object[7].toString());
				Date dateT = (Date) object[3];
				object[2] = totalInitialQuantity;
				int actualMonth = ControladorFechas.getMonth(dateT);
				int actualYear = ControladorFechas.getYear(dateT);
				boolean editQ = month != actualMonth ? true
						: year != actualYear ? true : false;
				count = idMaterialAux == idMaterial ? count : 0;
				if (count == 0) {
					actualQuantity = 0d;
					idMaterialAux = idMaterial;
					totalInitialQuantity = 0d;
					object[2] = 0d;
				}
				if (editQ) {
					if (count == 0) {
						totalInitialQuantity = 0d;
						object[2] = 0d;
					} else {
						totalInitialQuantity = actualQuantity;
						object[2] = totalInitialQuantity;
					}
				}
				Double income = (Double) object[8];
				Double outcome = (Double) object[9];
				actualQuantity -= income;
				actualQuantity += outcome;
				object[6] = actualQuantity;

				month = actualMonth;
				year = actualYear;
				count++;
				if (this.initialDate != null) {
					if (dateT.compareTo(this.initialDate) < 0) {
						totalInitialQuantity = actualQuantity;
						object[2] = totalInitialQuantity;
					}
				}
			}
			reportsController.generateReportInventoryControl(listInventory,
					listMonths, this.initialDate);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow build the query to consult the information for the
	 * report
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 */
	private void reportAdvanceSearch(StringBuilder consult,
			List<SelectItem> parameters) {
		if (this.idMaterialType != 0) {
			consult.append("AND m.id_material_type = :keyword1 ");
			SelectItem item = new SelectItem(this.idMaterialType, "keyword1");
			parameters.add(item);
		}
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(m.name) LIKE UPPER(:keyword2) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword2");
			parameters.add(item);
		}
		if (this.finalDate != null) {
			consult.append("AND t.date_time <= :keyword3 ");
			SelectItem item = new SelectItem(this.finalDate, "keyword3");
			parameters.add(item);
		}
	}

}