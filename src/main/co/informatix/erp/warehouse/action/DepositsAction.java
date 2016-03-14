package co.informatix.erp.warehouse.action;

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

import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContable;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.dao.MeasurementUnitsDao;
import co.informatix.erp.warehouse.dao.PurchaseInvoicesDao;
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.dao.TransactionsDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.MaterialsType;
import co.informatix.erp.warehouse.entities.MeasurementUnits;
import co.informatix.erp.warehouse.entities.PurchaseInvoices;
import co.informatix.erp.warehouse.entities.Suppliers;

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

	private Paginador paginador = new Paginador();

	private Deposits deposits;
	private Deposits depositActualSelected;
	private Deposits depositDetails;

	private PurchaseInvoices purchaseInvoice;
	private TransactionsAction transactionsAction;

	private Date dateStartSearch;
	private Date dateEndSearch;

	private Double unitCost;

	private List<Deposits> listDeposits;
	private List<SelectItem> itemsMaterial;
	private List<SelectItem> itemsFarm;
	private List<SelectItem> itemsMeasurementUnits;
	private List<SelectItem> itemsMaterialType;

	private int idMaterialType;
	private int idMaterial;

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
	 * Method to initialize the fields in the search.
	 * 
	 * @modify 08/03/2016 Gerardo.Herrera
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
				paginador.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				listDeposits = depositsDao.consultDeposits(
						paginador.getInicio(), paginador.getRango(), consult,
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
		boolean queryAdded = false;

		if (this.idMaterialType > 0) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append("d.materials.materialType.idMaterialsType = :idMaterialType ");
			SelectItem item = new SelectItem(this.idMaterialType,
					"idMaterialType");
			parameters.add(item);
			queryAdded = true;
		}

		if (this.idMaterial > 0) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append("d.materials.id = :idMaterial ");
			SelectItem item = new SelectItem(this.idMaterial, "idMaterial");
			parameters.add(item);
			queryAdded = true;
		}

		if (this.dateStartSearch != null && this.dateEndSearch != null) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append("d.dateTime BETWEEN :dateStartSearch AND :dateEndSearch ");
			SelectItem item = new SelectItem(dateStartSearch, "dateStartSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(dateEndSearch, "dateEndSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + format.format(this.dateStartSearch) + '"' + ", ";
			unionMessagesSearch.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + format.format(dateEndSearch) + '"';
			unionMessagesSearch.append(dateTo);
			parameters.add(item2);
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
		List<Farm> farmsList = farmDao.queryFarms();
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
				.consultarMeasurementsUnits();
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
							.getIdMaterial(), material.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the deposits
	 * 
	 * @return consultDeposits: Redirects to manage deposits with a list of
	 *         updated deposits
	 */
	public String saveDeposits() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			deposits.setActualQuantity(deposits.getInitialQuantity());
			if (deposits.getIdDeposit() != 0) {
				depositsDao.editDeposits(deposits);
			} else {
				mensajeRegistro = "message_registro_guardar";
				depositsDao.saveDeposits(deposits);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), deposits.getDateTime()));
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
	 * This method allows to set a value of purchase invoice for deposits
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @param purchaseInvoices
	 *            : Purchase Invoice for a deposits
	 */
	public void loadInvoice(PurchaseInvoices purchaseInvoices) {
		this.deposits.setPurchaseInvoices(purchaseInvoices);
	}

	/**
	 * This method clean the value of purchase invoice for deposits
	 * 
	 * @author Liseth.Jimenez
	 */
	public void cleanInvoice() {
		this.deposits.setPurchaseInvoices(new PurchaseInvoices());
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
	 * 
	 * @param invoices
	 *            : Purchase invoice for consult supplier
	 */
	public void setInoviceSupplier(PurchaseInvoices invoices) {
		try {
			this.purchaseInvoice = invoices;
			Suppliers suppliers = suppliersDao
					.suppliersById(this.purchaseInvoice.getSuppliers()
							.getIdSupplier());
			this.purchaseInvoice.setSuppliers(suppliers);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate the required fields.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void validateRequired() {

		if (this.deposits.getPurchaseInvoices().getInvoiceNumber() == null
				|| "".equals(this.deposits.getPurchaseInvoices()
						.getInvoiceNumber())) {
			ControladorContexto
					.mensajeRequeridos("formDeposits:txtInvoiceNumber");
		}
		if (this.deposits.getMaterials().getIdMaterial() == 0) {
			ControladorContexto.mensajeRequeridos("formDeposits:materials");
		}
		if (this.deposits.getMeasurementUnits().getIdMeasurementUnits() == 0) {
			ControladorContexto
					.mensajeRequeridos("formDeposits:measurementUnits");
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