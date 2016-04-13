package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.warehouse.dao.DepositsDao;
import co.informatix.erp.warehouse.dao.InvoiceItemsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.InvoiceItems;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.PurchaseInvoices;

/**
 * This class is all related logic with the management of invoice items
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class InvoiceItemsAction implements Serializable {

	@EJB
	private InvoiceItemsDao invoiceItemsDao;
	@EJB
	private FarmDao farmDao;
	@EJB
	private DepositsDao depositDao;
	@EJB
	private MaterialsDao materialsDao;
	@Resource
	private UserTransaction userTransaction;

	private List<InvoiceItems> invoiceItemsList;
	private List<SelectItem> itemsFarm;

	private Paginador pagination = new Paginador();
	private PurchaseInvoices invoicesSelected;
	private InvoiceItems invoiceItem;
	private Date expirationDate;

	private int idFarm;
	private double unitCost;
	private boolean validateConvert;

	/**
	 * @return invoiceItemsList: invoiceItems list objects
	 */
	public List<InvoiceItems> getInvoiceItemsList() {
		return invoiceItemsList;
	}

	/**
	 * @param invoiceItemsList
	 *            : invoiceItems list objects
	 */
	public void setInvoiceItemsList(List<InvoiceItems> invoiceItemsList) {
		this.invoiceItemsList = invoiceItemsList;
	}

	/**
	 * @return itemsFarm: List of items farm
	 */
	public List<SelectItem> getItemsFarm() {
		return itemsFarm;
	}

	/**
	 * @param itemsFarm
	 *            : List of items farm
	 */
	public void setItemsFarm(List<SelectItem> itemsFarm) {
		this.itemsFarm = itemsFarm;
	}

	/**
	 * @return pagination: Pager for the invoice items list
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Pager for the invoice items list
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return invoicesSelected: purchaseInvoices object
	 */
	public PurchaseInvoices getInvoicesSelected() {
		return invoicesSelected;
	}

	/**
	 * @param invoicesSelected
	 *            : purchaseInvoices object
	 */
	public void setInvoicesSelected(PurchaseInvoices invoicesSelected) {
		this.invoicesSelected = invoicesSelected;
	}

	/**
	 * @return invoiceItem: invoiceItem object
	 */
	public InvoiceItems getInvoiceItems() {
		return invoiceItem;
	}

	/**
	 * @param invoiceItem
	 *            : invoiceItem object
	 */
	public void setInvoiceItems(InvoiceItems invoiceItem) {
		this.invoiceItem = invoiceItem;
	}

	/**
	 * @return expirationDate: Expiration date for deposit
	 */
	public Date getExpirationDate() {
		return expirationDate;
	}

	/**
	 * @param expirationDate
	 *            : Expiration date for deposit
	 */
	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}

	/**
	 * @return idFarm: Identifier farm
	 */
	public int getIdFarm() {
		return idFarm;
	}

	/**
	 * @param idFarm
	 *            : Identifier farm
	 */
	public void setIdFarm(int idFarm) {
		this.idFarm = idFarm;
	}

	/**
	 * @return unitCost: Unit cost for deposit
	 */
	public double getUnitCost() {
		return unitCost;
	}

	/**
	 * @param unitCost
	 *            : Unit cost for deposit
	 */
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return validateConvert: Flag which displays the popup convert to deposit if 'true'
	 */
	public boolean isValidateConvert() {
		return validateConvert;
	}

	/**
	 * @param validateConvert
	 *            : Flag which displays the popup convert to deposit if 'true'
	 */
	public void setValidateConvert(boolean validateConvert) {
		this.validateConvert = validateConvert;
	}

	/**
	 * Method to edit or create a new invoiceItem.
	 * 
	 * @param invoiceItem
	 *            :invoiceItem are adding or editing
	 */
	public void addEditInvoiceItems(InvoiceItems invoiceItem) {

		if (invoiceItem != null) {
			this.invoiceItem = invoiceItem.clone();
		} else {
			this.invoiceItem = new InvoiceItems();
			this.invoiceItem.setMaterial(new Materials());
		}
	}

	/**
	 * Consult the list of invoice Items
	 * 
	 * @modify 06/04/2016 Andres.Gomez
	 */
	public void consultInvoiceItems() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		invoiceItemsList = new ArrayList<InvoiceItems>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder allMessageSearch = new StringBuilder();
		try {
			advancedSearchInvoiceItems(consult, parameters, bundle,
					allMessageSearch);
			Long quantity = invoiceItemsDao.quantityInvoiceItems(consult,
					parameters);
			if (quantity != null) {
				pagination.paginarRangoDefinido(quantity, 5);
			}
			if (quantity != null && quantity > 0) {
				this.invoiceItemsList = invoiceItemsDao.consultInvoiceItems(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @modify 06/04/2016 Andres.Gomez
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
	private void advancedSearchInvoiceItems(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {

		if (this.invoicesSelected != null) {
			consult.append("WHERE pi.idPurchaseInvoice = :purchaseInvoice ");
			SelectItem item = new SelectItem(
					this.invoicesSelected.getIdPurchaseInvoice(),
					"purchaseInvoice");
			parameters.add(item);
		}
	}

	/**
	 * Method used to save or edit the invoiceItem
	 * 
	 * @modify 06/04/2016 Andres.Gomez
	 * 
	 */
	public void saveUpdateInvoiceItem() {
		String param2 = ControladorContexto.getParam("param2");
		boolean desdeModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		PurchaseInvoicesAction purchaseInvoicesAction = ControladorContexto
				.getContextBean(PurchaseInvoicesAction.class);
		try {
			if (desdeModal) {
				purchaseInvoicesAction.saveInvoices();
				invoicesSelected = purchaseInvoicesAction.getInvoices();
			}
			userTransaction.begin();
			if (invoiceItem.getIdInvoiceItem() != 0) {
				invoiceItemsDao.editInvoiceItem(invoiceItem);
			} else {
				invoiceItem.setPurchaseInvoice(invoicesSelected);
				invoiceItemsDao.saveInvoiceItem(invoiceItem);
			}
			consultInvoiceItems();
			userTransaction.commit();
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception exception2) {
				ControladorContexto.mensajeError(exception2);
			}
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows to set a value of material for invoice item
	 * 
	 * @param material
	 *            : material for a invoice item
	 */
	public void loadMaterial(Materials material) {
		this.invoiceItem.setMaterial(material);
	}

	/**
	 * This method clean the value of material for invoice item
	 * 
	 */
	public void cleanMaterial() {
		this.invoiceItem.setMaterial(new Materials());
	}

	/**
	 * Method that loads a farms list.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @throws Exception
	 */
	private void listFarms() throws Exception {
		this.itemsFarm = new ArrayList<SelectItem>();
		List<Farm> listFarms = farmDao.farmsList();
		if (listFarms != null) {
			for (Farm farms : listFarms) {
				this.itemsFarm.add(new SelectItem(farms.getIdFarm(), farms
						.getName()));
			}
		}
	}

	/**
	 * Load all the data for convert invoice item to deposit
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param invoiceItems
	 *            : Object invoice items
	 */
	public void loadConvertDeposit(InvoiceItems invoiceItems) {
		try {
			this.validateConvert = false;
			boolean depositExist = this.depositDao.existsDeposit(invoiceItems
					.getMaterial(), invoiceItems.getPurchaseInvoice()
					.getInvoiceNumber());
			if (!depositExist) {
				if (invoiceItems.getQuantity() > 0
						&& invoiceItems.getTotal() > 0) {
					this.invoiceItem = invoiceItems;
					double quantity = invoiceItem.getQuantity()
							* invoiceItem.getMaterial().getPresentation();
					this.unitCost = invoiceItem.getTotal() / quantity;
					listFarms();
					this.validateConvert = true;
				} else {
					ControladorContexto.mensajeInfoEspecifico(
							"invoice_items_message_convert_deposit",
							"mensajeWarehouse");
				}
			} else {
				ControladorContexto.mensajeInfoEspecifico(
						"deposits_message_convert_deposit", "mensajeWarehouse");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Save deposit from a invoice item.
	 * 
	 * @author Gerardo.Herrera
	 */
	public void saveConvertToDeposit() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		try {
			Farm farm = farmDao.farmXId(this.idFarm);
			Materials material = invoiceItem.getMaterial();
			double quantity = material.getPresentation()
					* invoiceItem.getQuantity();
			Deposits deposit = new Deposits();
			deposit.setDateTime(new Date());
			deposit.setFarm(farm);
			deposit.setMaterials(material);
			deposit.setPurchaseInvoices(invoiceItem.getPurchaseInvoice());
			deposit.setMeasurementUnits(invoiceItem.getMaterial()
					.getMeasurementUnits());
			deposit.setInitialQuantity(quantity);
			deposit.setActualQuantity(quantity);
			deposit.setExpireDate(expirationDate);
			deposit.setTotalCost(invoiceItem.getTotal());
			deposit.setUnitCost(unitCost);
			depositDao.saveDeposits(deposit);
			String format = MessageFormat.format(bundle
					.getString("deposits_message_convert_deposit_successful"),
					invoiceItem.getPurchaseInvoice().getInvoiceNumber(),
					material.getName());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}
