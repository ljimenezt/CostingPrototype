package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContable;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.InvoiceItemsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.dao.PurchaseInvoicesDao;
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.dao.TransactionsDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.InvoiceItems;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.MaterialsType;
import co.informatix.erp.warehouse.entities.MeasurementUnits;
import co.informatix.erp.warehouse.entities.PurchaseInvoices;
import co.informatix.erp.warehouse.entities.Suppliers;
import co.informatix.erp.warehouse.entities.TransactionType;
import co.informatix.erp.warehouse.entities.Transactions;
import co.informatix.security.action.IdentityAction;

/**
 * This class is all related logic with creating, updating and removal of
 * deposits.
 * 
 * @author Sergio.Ortiz
 * @modify 08/03/2016 Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DepositsAction implements Serializable {

	@EJB
	private DepositsDao depositsDao;
	@EJB
	private MaterialsDao materialsDao;
	@EJB
	private FarmDao farmDao;
	@EJB
	private MeasurementUnitsDao measurementUnitsDao;
	@EJB
	private PurchaseInvoicesDao purchaseInvoicesDao;
	@EJB
	private MaterialsTypeDao materialsTypeDao;
	@EJB
	private SuppliersDao suppliersDao;
	@EJB
	private TransactionsDao transactionsDao;
	@EJB
	private TransactionTypeDao transactionTypeDao;
	@EJB
	private InvoiceItemsDao invoiceItemsDao;
	@Resource
	private UserTransaction userTransaction;
	@Inject
	private IdentityAction identity;

	private Paginador pagination = new Paginador();

	private Deposits deposits;
	private Deposits depositActualSelected;
	private Deposits depositDetails;

	private PurchaseInvoices purchaseInvoice;
	private TransactionsAction transactionsAction;

	private Date dateStartSearch;
	private Date dateEndSearch;

	private Double unitCost;
	private Double newQuantity;

	private String justificationTransaction;
	private String nameSearch;
	private String invoiceSearch;

	private List<Deposits> listDeposits;
	private List<SelectItem> itemsMaterial;
	private List<SelectItem> itemsFarm;
	private List<SelectItem> itemsMeasurementUnits;
	private List<SelectItem> itemsMaterialType;

	private int idMaterialType;
	private int idMaterial;
	private boolean existsDeposit;

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
	 * @return deposits: deposits stored in data base
	 */
	public Deposits getDeposits() {
		return deposits;
	}

	/**
	 * @param deposits
	 *            : deposits stored in data base
	 */
	public void setDeposits(Deposits deposits) {
		this.deposits = deposits;
	}

	/**
	 * @return depositActualSelected: Deposit selected in the deposits table
	 */
	public Deposits getDepositActualSelected() {
		return depositActualSelected;
	}

	/**
	 * @param depositActualSelected
	 *            : Deposit selected in the deposits table
	 */
	public void setDepositActualSelected(Deposits depositActualSelected) {
		this.depositActualSelected = depositActualSelected;
	}

	/**
	 * @return depositDetails: Object deposit for details
	 */
	public Deposits getDepositDetails() {
		return depositDetails;
	}

	/**
	 * @param depositDetails
	 *            : Object deposit for details
	 */
	public void setDepositDetails(Deposits depositDetails) {
		this.depositDetails = depositDetails;
	}

	/**
	 * @return purchaseInvoice: Object purchase for deposit
	 */
	public PurchaseInvoices getPurchaseInvoice() {
		return purchaseInvoice;
	}

	/**
	 * @param purchaseInvoice
	 *            : Object purchase for deposit
	 */
	public void setPurchaseInvoice(PurchaseInvoices purchaseInvoice) {
		this.purchaseInvoice = purchaseInvoice;
	}

	/**
	 * @return transactionsAction: Object of transaction action
	 */
	public TransactionsAction getTransactionsAction() {
		return transactionsAction;
	}

	/**
	 * @param transactionsAction
	 *            : Object of transaction action
	 */
	public void setTransactionsAction(TransactionsAction transactionsAction) {
		this.transactionsAction = transactionsAction;
	}

	/**
	 * @return listDeposits: list with all deposits stored in data base
	 */
	public List<Deposits> getListDeposits() {
		return listDeposits;
	}

	/**
	 * @param listDeposits
	 *            : list with all deposits stored in data base
	 */
	public void setListDeposits(List<Deposits> listDeposits) {
		this.listDeposits = listDeposits;
	}

	/**
	 * @return itemsMaterial: List of materials that are loaded into the user
	 *         interface.
	 */
	public List<SelectItem> getItemsMaterial() {
		return itemsMaterial;
	}

	/**
	 * @param itemsMaterial
	 *            : List of materials that are loaded into the user interface.
	 */
	public void setItemsMaterial(List<SelectItem> itemsMaterial) {
		this.itemsMaterial = itemsMaterial;
	}

	/**
	 * @return itemsFarm: List of farms that are loaded into the user interface.
	 */
	public List<SelectItem> getItemsFarm() {
		return itemsFarm;
	}

	/**
	 * @param itemsFarm
	 *            : List of farms that are loaded into the user interface.
	 */
	public void setItemsFarm(List<SelectItem> itemsFarm) {
		this.itemsFarm = itemsFarm;
	}

	/**
	 * @return itemsMeasurementUnits: List of MeasurementUnits that are loaded
	 *         into the user interface.
	 */
	public List<SelectItem> getItemsMeasurementUnits() {
		return itemsMeasurementUnits;
	}

	/**
	 * @param itemsMeasurementUnits
	 *            : List of MeasurementUnits that are loaded into the user
	 *            interface.
	 */
	public void setItemsMeasurementUnits(List<SelectItem> itemsMeasurementUnits) {
		this.itemsMeasurementUnits = itemsMeasurementUnits;
	}

	/**
	 * @return itemsMaterialType : List of materials type that are loaded into
	 *         the user interface.
	 */
	public List<SelectItem> getItemsMaterialType() {
		return itemsMaterialType;
	}

	/**
	 * @param itemsMaterialType
	 *            : List of materials type that are loaded into the user
	 *            interface.
	 */
	public void setItemsMaterialType(List<SelectItem> itemsMaterialType) {
		this.itemsMaterialType = itemsMaterialType;
	}

	/**
	 * @return dateStartSearch: Determines the initial range to search for
	 *         deposits in the system
	 */
	public Date getDateStartSearch() {
		return dateStartSearch;
	}

	/**
	 * @param dateStartSearch
	 *            : Determines the initial range to search for deposits in the
	 *            system
	 */
	public void setDateStartSearch(Date dateStartSearch) {
		this.dateStartSearch = dateStartSearch;
	}

	/**
	 * @return dateEndSearch: Determines the final range to search for deposits
	 *         in the system
	 */
	public Date getDateEndSearch() {
		return dateEndSearch;
	}

	/**
	 * @param dateEndSearch
	 *            : Determines the final range to search for deposits in the
	 *            system
	 */
	public void setDateEndSearch(Date dateEndSearch) {
		this.dateEndSearch = dateEndSearch;
	}

	/**
	 * @return unitCost : Cost unit of the material in the deposit
	 */
	public Double getUnitCost() {
		return unitCost;
	}

	/**
	 * @param unitCost
	 *            : Cost unit of the material in the deposit
	 */
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return newQuantity: Quantity for make the adjust
	 */
	public Double getNewQuantity() {
		return newQuantity;
	}

	/**
	 * @param newQuantity
	 *            : Quantity for make the adjust
	 */
	public void setNewQuantity(Double newQuantity) {
		this.newQuantity = newQuantity;
	}

	/**
	 * @return justificationTransaction: Justification for make the transaction
	 */
	public String getJustificationTransaction() {
		return justificationTransaction;
	}

	/**
	 * @param justificationTransaction
	 *            : Justification for make the transaction
	 */
	public void setJustificationTransaction(String justificationTransaction) {
		this.justificationTransaction = justificationTransaction;
	}

	/**
	 * @return nameSearch: Material name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Material name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return invoiceSearch: Invoice name to search.
	 */
	public String getInvoiceSearch() {
		return invoiceSearch;
	}

	/**
	 * @param invoiceSearch
	 *            : Invoice name to search.
	 */
	public void setInvoiceSearch(String invoiceSearch) {
		this.invoiceSearch = invoiceSearch;
	}

	/**
	 * @return idMaterialType : Material type identifier selected in the UI
	 */
	public int getIdMaterialType() {
		return idMaterialType;
	}

	/**
	 * @param idMaterialType
	 *            : Material type identifier selected in the UI
	 */
	public void setIdMaterialType(int idMaterialType) {
		this.idMaterialType = idMaterialType;
	}

	/**
	 * @return idMaterial: Identifier of material
	 */
	public int getIdMaterial() {
		return idMaterial;
	}

	/**
	 * @param idMaterial
	 *            : Identifier of material
	 */
	public void setIdMaterial(int idMaterial) {
		this.idMaterial = idMaterial;
	}

	/**
	 * @return existsDeposit: Flag for deposits exists
	 */
	public boolean isExistsDeposit() {
		return existsDeposit;
	}

	/**
	 * @param existsDeposit
	 *            : Flag for deposits exists
	 */
	public void setExistsDeposit(boolean existsDeposit) {
		this.existsDeposit = existsDeposit;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @modify 08/03/2016 Gerardo.Herrera
	 * @modify 14/04/2016 Wilhelm.Boada
	 * 
	 * @return consultDeposits: Deposits consulting method and redirects to the
	 *         template to manage deposits.
	 */
	public String initializeSearch() {
		if (ControladorContexto.getFacesContext() != null) {
			this.transactionsAction = ControladorContexto
					.getContextBean(TransactionsAction.class);
		}
		this.idMaterial = 0;
		this.idMaterialType = 0;
		this.depositActualSelected = null;
		this.dateStartSearch = null;
		this.dateEndSearch = null;
		this.deposits = new Deposits();
		this.nameSearch = "";
		this.invoiceSearch = "";
		try {
			this.loadMaterialsType();
			this.loadMaterials();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultDeposits();
	}

	/**
	 * Consult the list of Deposits
	 * 
	 * @modify 07/03/2016 Gerardo.Herrera
	 * 
	 * @return gesDeposits: Navigation rule that redirects to manage deposits
	 */
	public String consultDeposits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listDeposits = new ArrayList<Deposits>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder allMessageSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advanceSearch(consult, parameters, bundle, allMessageSearch);
			Long quantity = depositsDao.amountDeposits(consult, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				listDeposits = depositsDao.consultDeposits(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters);
			}
			if ((listDeposits == null || listDeposits.size() <= 0)
					&& !"".equals(allMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								allMessageSearch);
			} else if (listDeposits == null || listDeposits.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(allMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleWarehouse.getString("deposits_label"),
								allMessageSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesDeposits";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @modify 01/03/2016 Gerardo.Herrera
	 * @modify 14/04/2016 Wilhelm.Boada
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void advanceSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat format = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		boolean queryAdded = false;

		if (this.idMaterialType > 0) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append("d.materials.materialType.idMaterialsType = :idMaterialType ");
			SelectItem item = new SelectItem(this.idMaterialType,
					"idMaterialType");
			parameters.add(item);
			queryAdded = true;
		}

		if ((this.nameSearch != null && !"".equals(this.nameSearch))) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append(" UPPER(m.name) LIKE UPPER(:keywordName) ");
			SelectItem itemNombre = new SelectItem("%" + this.nameSearch + "%",
					"keywordName");
			parameters.add(itemNombre);
			unionMessagesSearch.append(bundleWarehouse
					.getString("materials_label")
					+ ": "
					+ '"'
					+ this.nameSearch + '"' + " ");
			queryAdded = true;
		}

		if ((this.invoiceSearch != null && !"".equals(this.invoiceSearch))) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append(" UPPER(p.invoiceNumber) LIKE UPPER(:keywordInovice) ");
			SelectItem itemNombre = new SelectItem("%" + this.invoiceSearch
					+ "%", "keywordInovice");
			parameters.add(itemNombre);
			unionMessagesSearch.append(bundleWarehouse
					.getString("purchase_invoice_label")
					+ ": "
					+ '"'
					+ this.invoiceSearch + '"' + " ");
			queryAdded = true;
		}
		if (this.dateStartSearch != null || this.dateEndSearch != null) {
			consult.append(queryAdded ? "AND " : "WHERE ");

			if (this.dateStartSearch != null && this.dateEndSearch != null) {
				consult.append("d.dateTime BETWEEN :dateStartSearch AND :dateEndSearch ");
			}
			if (this.dateStartSearch != null && this.dateEndSearch == null) {
				consult.append("d.dateTime >= :dateStartSearch ");
			}
			if (this.dateStartSearch == null && this.dateEndSearch != null) {
				consult.append("d.dateTime <= :dateEndSearch ");
			}

			if (this.dateStartSearch != null) {
				SelectItem item = new SelectItem(dateStartSearch,
						"dateStartSearch");
				parameters.add(item);
				parameters.add(item);
				String dateFrom = bundle.getString("label_start_date") + ": "
						+ '"' + format.format(this.dateStartSearch) + '"' + " ";
				unionMessagesSearch.append(dateFrom);
			}
			if (this.dateEndSearch != null) {
				SelectItem item2 = new SelectItem(dateEndSearch,
						"dateEndSearch");
				parameters.add(item2);
				String dateTo = bundle.getString("label_end_date") + ": " + '"'
						+ format.format(dateEndSearch) + '"';
				unionMessagesSearch.append(dateTo);
				parameters.add(item2);
			}
		}
	}

	/**
	 * Method to edit or create a new deposits.
	 * 
	 * @modify 29/02/2016 Liseth.Jimenez
	 * 
	 * @param deposits
	 *            :deposit are adding or editing
	 * 
	 * @return "regDeposits":redirected to the template record deposits.
	 */
	public String addEditDeposits(Deposits deposits) {
		this.unitCost = 0d;
		this.idMaterialType = 0;
		try {
			loadMaterialsType();
			loadFarms();
			loadMeasurementUnits();
			if (deposits != null) {
				this.deposits = deposits;
				Materials material = this.deposits.getMaterials();
				this.idMaterialType = material.getMaterialType()
						.getIdMaterialsType();
				calculateUnitCost();
				loadMaterials();
			} else {
				this.deposits = new Deposits();
				this.deposits.setMaterials(new Materials());
				this.deposits.setFarm(new Farm());
				this.deposits.setMeasurementUnits(new MeasurementUnits());
				this.deposits.setPurchaseInvoices(new PurchaseInvoices());
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regDeposits";
	}

	/**
	 * This method allows you to load farms interface for registering a new
	 * deposits.
	 * 
	 * @throws Exception
	 */
	private void loadFarms() throws Exception {
		itemsFarm = new ArrayList<SelectItem>();
		List<Farm> farmsList = farmDao.farmsList();
		if (farmsList != null) {
			for (Farm farms : farmsList) {
				itemsFarm
						.add(new SelectItem(farms.getIdFarm(), farms.getName()));
			}
		}
	}

	/**
	 * This method allows you to load the measurement units types in interface
	 * for registering a new deposits.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @throws Exception
	 */
	private void loadMeasurementUnits() throws Exception {
		itemsMeasurementUnits = new ArrayList<SelectItem>();
		List<MeasurementUnits> measurementUnitsList = measurementUnitsDao
				.consultMeasurementsUnits();
		if (measurementUnitsList != null) {
			for (MeasurementUnits measurement : measurementUnitsList) {
				itemsMeasurementUnits.add(new SelectItem(measurement
						.getIdMeasurementUnits(), measurement.getName()));
			}
		}
	}

	/**
	 * This method allows you to load the materials types in interface for
	 * registering a new deposits.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @throws Exception
	 */
	private void loadMaterialsType() throws Exception {
		itemsMaterialType = new ArrayList<SelectItem>();
		List<MaterialsType> materialsTypes = materialsTypeDao
				.consultMaterialsTypes();
		if (materialsTypes != null) {
			for (MaterialsType materialType : materialsTypes) {
				this.itemsMaterialType.add(new SelectItem(materialType
						.getIdMaterialsType(), materialType.getName()));
			}
		}
	}

	/**
	 * This method allows you to load the materials in interface for registering
	 * a new deposits.
	 * 
	 * @author Liseth.Jimenez
	 * @modify 14/04/2016 Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	public void loadMaterials() {
		try {
			itemsMaterial = new ArrayList<SelectItem>();
			List<Materials> materials = materialsDao
					.queryMaterialsByType(idMaterialType);
			if (materials != null) {
				for (Materials material : materials) {
					this.itemsMaterial.add(new SelectItem(material
							.getIdMaterial(), material.getName() + " "
							+ material.getPresentation() + " "
							+ material.getMeasurementUnits().getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the deposits
	 * 
	 * @modify 15/04/2016 Gerardo.Herrera
	 * 
	 * @return consultDeposits: Redirects to manage deposits with a list of
	 *         updated deposits
	 */
	public String saveDeposits() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		String mensajeRegistro = "deposits_message_save_succesful";
		try {
			Materials material = materialsDao
					.consultMaterialsById(this.deposits.getMaterials()
							.getIdMaterial());
			deposits.getMeasurementUnits().setIdMeasurementUnits(
					material.getMeasurementUnits().getIdMeasurementUnits());
			if (deposits.getIdDeposit() != 0) {
				depositsDao.editDeposits(deposits);
			} else {
				mensajeRegistro = "deposits_message_save_succesful";
				depositsDao.saveDeposits(deposits);
			}
			String messageMaterial = material.getName() + " "
					+ material.getPresentation() + " "
					+ material.getMeasurementUnits().getName();
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), messageMaterial,
					deposits.getPurchaseInvoices().getInvoiceNumber()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeSearch();
	}

	/**
	 * Method that allows deposits to delete one database
	 * 
	 * @return consultDeposits: Consult the list of deposits and returns to
	 *         manage deposits
	 */
	public String deleteDeposits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			depositsDao.deleteDeposits(deposits);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					deposits.getDateTime()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					deposits.getDateTime());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultDeposits();
	}

	/**
	 * Calculate the unit cost of the deposits
	 * 
	 * @author Liseth.Jimenez
	 */
	public void calculateUnitCost() {
		if (deposits.getInitialQuantity() != null
				&& deposits.getTotalCost() != null) {
			if (deposits.getInitialQuantity() > 0) {
				unitCost = ControladorContable.dividir(deposits.getTotalCost(),
						deposits.getInitialQuantity());
			}
		} else {
			unitCost = 0d;
		}
	}

	/**
	 * This method allows to set a value of purchase invoice for deposits and
	 * load all the materials associated
	 * 
	 * @author Liseth.Jimenez
	 * @modify 15/04/2016 Gerardo.Herrera
	 * 
	 * @param purchaseInvoices
	 *            : Purchase Invoice for a deposits
	 */
	public void loadInvoice(PurchaseInvoices purchaseInvoices) {
		this.deposits = new Deposits();
		this.deposits.setPurchaseInvoices(purchaseInvoices);
		try {
			itemsMaterial = new ArrayList<SelectItem>();
			List<Materials> materials = materialsDao
					.materialsXInvoicePurchase(purchaseInvoices);
			if (materials != null) {
				for (Materials material : materials) {
					this.itemsMaterial.add(new SelectItem(material
							.getIdMaterial(), material.getName() + " "
							+ material.getPresentation() + " "
							+ material.getMeasurementUnits().getName()));
				}
			}
			cleanDeposits();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method load the information into deposit associated to item invoice
	 * 
	 * @author Gerardo.Herrera
	 * @modify 19/04/2016 Wilhelm.Boada
	 */
	public void loadDataMaterial() {
		try {
			this.existsDeposit = depositsDao.existsDeposit(this.deposits
					.getMaterials(), this.deposits.getPurchaseInvoices()
					.getInvoiceNumber(), this.deposits.getPurchaseInvoices()
					.getSuppliers().getIdSupplier());
			if (this.deposits.getMaterials().getIdMaterial() != 0
					&& !existsDeposit) {
				InvoiceItems invoiceItem = invoiceItemsDao
						.invoiceItemByMaterial(this.deposits
								.getPurchaseInvoices().getInvoiceNumber(),
								this.deposits.getMaterials().getIdMaterial());
				if (invoiceItem != null) {
					Materials material = materialsDao
							.consultMaterialsById(this.deposits.getMaterials()
									.getIdMaterial());
					double quantity = material.getPresentation()
							* invoiceItem.getQuantity();
					this.deposits.setActualQuantity(quantity);
					this.deposits.setInitialQuantity(quantity);
					this.deposits
							.setUnitCost(invoiceItem.getTotal() / quantity);
					this.deposits.setTotalCost(invoiceItem.getTotal());
					this.deposits.setDateTime(this.deposits
							.getPurchaseInvoices().getDateTime());
				}
			} else {
				cleanDeposits();
				if (existsDeposit
						&& this.deposits.getMaterials().getIdMaterial() != 0) {
					ResourceBundle bundle = ControladorContexto
							.getBundle("mensajeWarehouse");
					ControladorContexto.mensajeError("formDeposits:materials",
							bundle.getString("deposits_message_exists"));
				}
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Clean deposit fields
	 * 
	 * @author Gerardo.Herrera
	 */
	private void cleanDeposits() {
		this.deposits.setActualQuantity(null);
		this.deposits.setInitialQuantity(null);
		this.deposits.setUnitCost(null);
		this.deposits.setTotalCost(null);
		this.deposits.setDateTime(null);
	}

	/**
	 * This method clean the value of purchase invoice for deposits
	 * 
	 * @author Liseth.Jimenez
	 */
	public void cleanInvoice() {
		this.deposits.setPurchaseInvoices(new PurchaseInvoices());
		cleanDeposits();
	}

	/**
	 * Initialize the dropdown filter for materialType
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param flag
	 *            : True if this method is called from dropdown materialType and
	 *            false if it is called from dropdown material
	 */
	public void initializeDropDownTypeMaterial(boolean flag) {
		try {
			if (flag) {
				this.idMaterial = 0;
				this.loadMaterials();
			}
			this.depositActualSelected = null;
			this.consultDeposits();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Selects a single deposit for display the associated transaction
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param depositSelected
	 *            : deposit selected on the view
	 */
	public void selectDeposit(Deposits depositSelected) {
		this.depositActualSelected = new Deposits();
		depositSelected.setSelected(true);
		for (Deposits deposit : listDeposits) {
			if (deposit.isSelected()) {
				if (deposit.getIdDeposit() == depositSelected.getIdDeposit()) {
					this.depositActualSelected = deposit;
				} else {
					deposit.setSelected(false);
				}
			}
		}
	}

	/**
	 * Show the transactions associated to deposit.
	 * 
	 * @author Gerardo.Herrera
	 */
	public void showTransaction() {
		if (transactionsAction != null) {
			transactionsAction.setDepositSelected(depositActualSelected);
			transactionsAction.consultTransaction();
		}
	}

	/**
	 * This method allows to consult the supplier associated to an invoice
	 * 
	 * @author Liseth.Jimenez
	 * @modify 05/04/2016 Wilhelm.Boada
	 * 
	 * @param deposit
	 *            : To view details of deposits and purchase invoices
	 */
	public void setInoviceSupplier(Deposits deposit) {
		try {
			this.depositDetails = deposit;
			this.purchaseInvoice = deposit.getPurchaseInvoices();
			Suppliers suppliers = suppliersDao
					.suppliersById(this.purchaseInvoice.getSuppliers()
							.getIdSupplier());
			this.purchaseInvoice.setSuppliers(suppliers);
			selectDeposit(deposit);
			showTransaction();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for validating the number entered to make an adjustment
	 * does not exceed the current registered
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param context
	 *            : Context for the view.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 */
	public void validateDuration(FacesContext context, UIComponent toValidate,
			Object value) {
		String clientId = toValidate.getClientId(context);
		Double quantity = (Double) value;
		if (quantity.compareTo(this.depositDetails.getActualQuantity()) >= 0) {
			String message = "deposits_message_adjust_quantity";
			ControladorContexto.mensajeErrorEspecifico(clientId, message,
					"mensajeWarehouse");
			((UIInput) toValidate).setValid(false);
		}
	}

	/**
	 * It is responsible of save a transaction and edit the deposit when it make
	 * a adjust
	 * 
	 * @author Gerardo.Herrera
	 */
	public void depositAdjust() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		if (this.depositDetails != null) {
			try {
				this.userTransaction.begin();
				Transactions transactions = new Transactions();
				Double quantity = this.depositDetails.getActualQuantity()
						- this.newQuantity;
				this.depositDetails.setActualQuantity(this.newQuantity);
				TransactionType transactionType = this.transactionTypeDao
						.transactionTypeById(Constantes.IDENTIFIER_ADJUSTEMENT_ADJUST_TYPE);
				this.depositsDao.editDeposits(this.depositDetails);
				transactions.setJustification(this.justificationTransaction);
				transactions.setTransactionType(transactionType);
				transactions.setUserName(identity.getUserName());
				transactions.setDeposits(this.depositDetails);
				transactions.setDateTime(new Date());
				transactions.setQuantity(quantity);
				this.transactionsDao.saveTransaction(transactions);
				userTransaction.commit();
				showTransaction();
				String format = MessageFormat
						.format(bundle
								.getString("deposits_message_adjust_deposit"),
								depositDetails.getPurchaseInvoices()
										.getInvoiceNumber(), depositDetails
										.getMaterials().getName());
				ControladorContexto.mensajeInformacion(null, format);
			} catch (Exception e) {
				try {
					this.userTransaction.rollback();
				} catch (Exception e1) {
					ControladorContexto.printErrorLog(e1);
				}
				ControladorContexto.mensajeError(e);
			}
		}
	}

	/**
	 * Clear fields for adjustement deposit
	 * 
	 * @author Gerardo.Herrera
	 */
	public void clearFieldAdjust() {
		setNewQuantity(null);
		setJustificationTransaction(null);
	}

	/**
	 * This method allows validate the required fields.
	 * 
	 * @author Wilhelm.Boada
	 * @modify 19/04/2016 Gerardo.Herrera
	 */
	public void validateRequired() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		if (this.deposits.getPurchaseInvoices().getInvoiceNumber() == null
				|| "".equals(this.deposits.getPurchaseInvoices()
						.getInvoiceNumber())) {
			ControladorContexto
					.mensajeRequeridos("formDeposits:txtInvoiceNumber");
		}
		if (this.deposits.getMaterials().getIdMaterial() == 0) {
			ControladorContexto.mensajeRequeridos("formDeposits:materials");
		}
		if (existsDeposit && this.deposits.getMaterials().getIdMaterial() != 0) {
			ControladorContexto.mensajeError("formDeposits:materials",
					bundle.getString("deposits_message_exists"));
		}
		if (this.deposits.getInitialQuantity() == null) {
			ControladorContexto
					.mensajeRequeridos("formDeposits:initialQuantity");
		}
		if (this.deposits.getTotalCost() == null) {
			ControladorContexto.mensajeRequeridos("formDeposits:totalCost");
		}
		if (this.deposits.getDateTime() == null
				|| "".equals(this.deposits.getDateTime())) {
			ControladorContexto.mensajeRequeridos("formDeposits:datePurchase");
		}
		if (this.deposits.getFarm().getIdFarm() == 0) {
			ControladorContexto.mensajeRequeridos("formDeposits:farm");
		}
	}
}