package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.MathContext;
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

import co.informatix.erp.informacionBase.dao.IvaRateDao;
import co.informatix.erp.informacionBase.entities.IvaRate;
import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.entities.Farm;
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
 * @modify 21/04/2016 Andres.Gomez
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
	@EJB
	private IvaRateDao ivaRateDao;
	@Resource
	private UserTransaction userTransaction;

	private List<InvoiceItems> invoiceItemsListEdit;
	private List<InvoiceItems> invoiceItemsListAdd;
	private List<InvoiceItems> invoiceItemsList;
	private List<InvoiceItems> invoiceItemsRemoves;
	private List<InvoiceItems> subListInvoiceItems;
	private List<SelectItem> itemsFarm;
	private List<SelectItem> itemsIva;

	private Paginador pagination = new Paginador();
	private PurchaseInvoices invoicesSelected;
	private InvoiceItems invoiceItem;
	private Date expirationDate;

	private int idFarm;
	private double unitCost;
	private boolean validateConvert;
	private boolean isEdit;
	private boolean editTemp = false;
	private boolean depositExist = false;
	private int index;
	private int indexTemp;

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
	 * @return invoiceItemsRemoves : invoiceItems list objects to remove
	 */
	public List<InvoiceItems> getInvoiceItemsRemoves() {
		return invoiceItemsRemoves;
	}

	/**
	 * @param invoiceItemsRemoves
	 *            :invoiceItems list objects to remove
	 */
	public void setInvoiceItemsRemoves(List<InvoiceItems> invoiceItemsRemoves) {
		this.invoiceItemsRemoves = invoiceItemsRemoves;
	}

	/**
	 * @return subListInvoiceItems: list of Invoice Items that stores a sublist
	 *         for managing Pager.
	 */
	public List<InvoiceItems> getSubListInvoiceItems() {
		return subListInvoiceItems;
	}

	/**
	 * @param subListInvoiceItems
	 *            :list of Invoice Items that stores a sublist for managing
	 *            Pager.
	 */
	public void setSubListInvoiceItems(List<InvoiceItems> subListInvoiceItems) {
		this.subListInvoiceItems = subListInvoiceItems;
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
	 * @return itemsIva: list of the IVA rate
	 */
	public List<SelectItem> getItemsIva() {
		return itemsIva;
	}

	/**
	 * @param itemsIva
	 *            : list of the IVA rate
	 */
	public void setItemsIva(List<SelectItem> itemsIva) {
		this.itemsIva = itemsIva;
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
	 * @return invoiceItemsListEdit :invoiceItems temporal list objects to edit
	 */
	public List<InvoiceItems> getInvoiceItemsListEdit() {
		return invoiceItemsListEdit;
	}

	/**
	 * @param invoiceItemsListEdit
	 *            :invoiceItems temporal list objects to edit
	 */
	public void setInvoiceItemsListEdit(List<InvoiceItems> invoiceItemsListEdit) {
		this.invoiceItemsListEdit = invoiceItemsListEdit;
	}

	/**
	 * @return invoiceItemsListAdd: invoiceItems temporal list objects to add
	 */
	public List<InvoiceItems> getInvoiceItemsListAdd() {
		return invoiceItemsListAdd;
	}

	/**
	 * @param invoiceItemsListAdd
	 *            : invoiceItems temporal list objects to add
	 */
	public void setInvoiceItemsListAdd(List<InvoiceItems> invoiceItemsListAdd) {
		this.invoiceItemsListAdd = invoiceItemsListAdd;
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
	 * @return validateConvert: Flag which displays the popup convert to deposit
	 *         if 'true'
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
	 * @return depositExist: Flag for exists deposits
	 */
	public boolean isDepositExist() {
		return depositExist;
	}

	/**
	 * @param depositExist
	 *            : Flag for exists deposits
	 */
	public void setDepositExist(boolean depositExist) {
		this.depositExist = depositExist;
	}

	/**
	 * Method to edit or create a new invoiceItem.
	 * 
	 * @param invoiceItem
	 *            :invoiceItem are adding or editing
	 */
	public void addEditInvoiceItems(InvoiceItems invoiceItem) {
		try {
			loadIvaRate();
			if (invoiceItem != null) {
				this.invoiceItem = invoiceItem.clone();
				this.isEdit = true;
				if (this.invoiceItem.getIvaRate() == null) {
					this.invoiceItem.setIvaRate(new IvaRate());
				}
				if (invoiceItem.getIdInvoiceItem() != 0) {
					index = this.invoiceItemsListEdit.indexOf(this.invoiceItem);
					editTemp = false;
				} else {
					index = this.invoiceItemsListAdd.indexOf(this.invoiceItem);
					editTemp = true;
				}
				indexTemp = this.invoiceItemsList.indexOf(this.invoiceItem);
			} else {
				this.invoiceItem = new InvoiceItems();
				this.invoiceItem.setMaterial(new Materials());
				this.invoiceItem.setIvaRate(new IvaRate());
				this.isEdit = false;
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows verify if the material of the invoice item is already
	 * set at the deposit
	 * 
	 * @param invoiceItems
	 *            : Invoice items to validate if is already created
	 */
	public void validateInvoiceItemsToEdit(InvoiceItems invoiceItems) {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		try {
			if (invoiceItems.getIdInvoiceItem() != 0) {
				if (depositDao.existsDeposit(invoiceItems.getMaterial(),
						invoiceItems.getPurchaseInvoice().getInvoiceNumber(),
						invoiceItems.getPurchaseInvoice().getSuppliers()
								.getIdSupplier())) {
					String format = MessageFormat.format(bundle
							.getString("deposits_message_convert_deposit"),
							invoiceItems.getMaterial().getName()
									+ " "
									+ invoiceItems.getMaterial()
											.getPresentation()
									+ " "
									+ invoiceItems.getMaterial()
											.getMeasurementUnits().getName());
					ControladorContexto.mensajeError(null,
							"formInvoices:pnlInvoice", format);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * This method allows to clean lists and initialize
	 */
	public void cleanLists() {
		invoiceItemsList = new ArrayList<InvoiceItems>();
		invoiceItemsListEdit = new ArrayList<InvoiceItems>();
		invoiceItemsListAdd = new ArrayList<InvoiceItems>();
		invoiceItemsRemoves = new ArrayList<InvoiceItems>();
		subListInvoiceItems = new ArrayList<InvoiceItems>();
	}

	/**
	 * Consult the list of invoice Items
	 * 
	 * @modify 06/04/2016 Andres.Gomez
	 */
	public void consultInvoiceItems() {
		cleanLists();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		try {
			advancedSearchInvoiceItems(consult, parameters);
			Long quantity = invoiceItemsDao.quantityInvoiceItems(consult,
					parameters);
			if (quantity != null && quantity > 0) {
				this.invoiceItemsList = invoiceItemsDao.consultInvoiceItems(
						consult, parameters);
				this.invoiceItemsListEdit.addAll(this.invoiceItemsList);
				initializeList();
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
	 */
	private void advancedSearchInvoiceItems(StringBuilder consult,
			List<SelectItem> parameters) {

		if (this.invoicesSelected != null) {
			consult.append("WHERE pi.idPurchaseInvoice = :purchaseInvoice ");
			SelectItem item = new SelectItem(
					this.invoicesSelected.getIdPurchaseInvoice(),
					"purchaseInvoice");
			parameters.add(item);
		}
	}

	/**
	 * Method used to save edit or remove the invoiceItem
	 * 
	 * @modify 06/04/2016 Andres.Gomez
	 */
	public void saveUpdateInvoiceItem() {
		try {
			if (this.invoiceItemsListAdd.size() > 0) {
				for (InvoiceItems invoiceItem : this.invoiceItemsListAdd) {
					if (invoiceItem.getIvaRate().getIdIva() == 0) {
						invoiceItem.setIvaRate(null);
					}
					invoiceItem.setPurchaseInvoice(invoicesSelected);
					invoiceItemsDao.saveInvoiceItem(invoiceItem);
				}
			}
			if (this.invoiceItemsListEdit.size() > 0) {
				for (InvoiceItems invoiceItem : this.invoiceItemsListEdit) {
					if (invoiceItem.getIvaRate() == null
							|| invoiceItem.getIvaRate().getIdIva() == 0) {
						invoiceItem.setIvaRate(null);
					}
					invoiceItemsDao.editInvoiceItem(invoiceItem);
				}
			}
			if (this.invoiceItemsRemoves.size() > 0) {
				for (InvoiceItems invoiceItem : this.invoiceItemsRemoves) {
					invoiceItemsDao.removeInvoiceItems(invoiceItem);
				}
			}
			consultInvoiceItems();
		} catch (Exception e) {
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
	 * @modify 19/04/2016 Wilhelm.Boada
	 * 
	 * @param invoiceItems
	 *            : Object invoice items
	 */
	public void loadConvertDeposit(InvoiceItems invoiceItems) {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeWarehouse");
		try {
			this.validateConvert = false;
			if (invoiceItems != null
					&& invoiceItems.getPurchaseInvoice() != null) {
				this.depositExist = this.depositDao.existsDeposit(invoiceItems
						.getMaterial(), invoiceItems.getPurchaseInvoice()
						.getInvoiceNumber(), invoiceItems.getPurchaseInvoice()
						.getSuppliers().getIdSupplier());
				if (!depositExist) {
					if (invoiceItems.getPurchaseInvoice().isReconcile()) {
						if (invoiceItems.getQuantity() > 0
								&& invoiceItems.getTotal() > 0) {
							this.invoiceItem = invoiceItems;
							double quantity = invoiceItem.getQuantity()
									* invoiceItem.getMaterial()
											.getPresentation();
							this.unitCost = invoiceItem.getTotal() / quantity;
							listFarms();
							this.validateConvert = true;
						} else {
							ControladorContexto
									.mensajeInformacion(
											"formPurchaseInvoices:invoiceItemsTable",
											bundle.getString("invoice_items_message_convert_deposit"));
						}
					} else {
						String format = MessageFormat
								.format(bundle
										.getString("invoice_items_message_invoice_not_reconcile"),
										invoiceItems.getPurchaseInvoice()
												.getInvoiceNumber());
						ControladorContexto.mensajeInformacion(
								"formPurchaseInvoices:invoiceItemsTable",
								format);
					}
				} else {
					String format = MessageFormat.format(bundle
							.getString("deposits_message_convert_deposit"),
							invoiceItems.getMaterial().getName()
									+ " "
									+ invoiceItems.getMaterial()
											.getPresentation()
									+ " "
									+ invoiceItems.getMaterial()
											.getMeasurementUnits().getName());
					ControladorContexto.mensajeInformacion(
							"formPurchaseInvoices:invoiceItemsTable", format);
				}
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
			String messageMaterial = material.getName() + " "
					+ material.getPresentation() + " "
					+ material.getMeasurementUnits().getName();
			String format = MessageFormat.format(bundle
					.getString("deposits_message_convert_deposit_successful"),
					invoiceItem.getPurchaseInvoice().getInvoiceNumber(),
					messageMaterial);
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow calculate the total according with the values that user
	 * enter in the view
	 */
	public void calculateTotal() {
		try {
			double quantity = this.invoiceItem.getQuantity();
			double costUnit = this.invoiceItem.getUnitCost();
			if (costUnit > 0 && quantity > 0) {
				double subTotal = quantity * costUnit;
				this.invoiceItem.setSubTotal(subTotal);
				double shipping = this.invoiceItem.getShipping();
				BigDecimal taxes = new BigDecimal(0);
				int idIvaRate = this.invoiceItem.getIvaRate().getIdIva();
				if (idIvaRate != 0) {
					IvaRate ivaRate = ivaRateDao.ivaRateXId(idIvaRate);
					this.invoiceItem.setIvaRate(ivaRate);
					double iva = ivaRate.getRate() / 100;
					taxes = new BigDecimal(subTotal * iva,
							MathContext.DECIMAL64);
					this.invoiceItem.setTaxes(taxes.doubleValue());
				}
				double packaging = this.invoiceItem.getPackaging();
				double handling = this.invoiceItem.getHandling();
				double discount = this.invoiceItem.getDiscount();
				double sum = (shipping + taxes.doubleValue() + packaging + handling)
						- (discount);
				double total = this.invoiceItem.getSubTotal() + sum;
				this.invoiceItem.setTotal(total);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow add the invoice item in a list to show and save if the
	 * purchase invoice is saved
	 */
	public void addTemporalList() {
		try {
			if (isEdit) {
				if (editTemp) {
					this.invoiceItemsListAdd.set(index, this.invoiceItem);
				} else {
					this.invoiceItemsListEdit.set(index, this.invoiceItem);
				}
				this.invoiceItemsList.set(indexTemp, this.invoiceItem);
			} else {
				this.invoiceItemsListAdd.add(this.invoiceItem);
				this.invoiceItemsList.add(this.invoiceItem);
			}
			this.invoiceItem = new InvoiceItems();
			initializeList();

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows remove the invoice item in the list of the purchase
	 * items.
	 */
	public void removeInvoiceItem() {
		int idInvoice = this.invoiceItem.getIdInvoiceItem();
		if (idInvoice != 0) {
			this.invoiceItemsRemoves.add(this.invoiceItem);
			this.invoiceItemsListEdit.remove(this.invoiceItem);
			this.invoiceItemsList.remove(this.invoiceItem);
		} else {
			this.invoiceItemsListAdd.remove(this.invoiceItem);
			this.invoiceItemsList.remove(this.invoiceItem);
		}
		this.invoiceItem = new InvoiceItems();
		initializeList();
	}

	/**
	 * It initializes the pager with a fixed range to manage the activities list
	 * and it gets a sublist of invoice items.
	 */
	public void initializeList() {
		subListInvoiceItems = new ArrayList<InvoiceItems>();
		subListInvoiceItems.addAll(this.invoiceItemsList);
		Long paginationAmount = (long) this.subListInvoiceItems.size();
		try {
			this.pagination.paginarRangoDefinido(paginationAmount, 5);
			int inicial = this.pagination.getItemInicial() - 1;
			int fin = this.pagination.getItemFinal();
			this.subListInvoiceItems = this.subListInvoiceItems.subList(
					inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates fields that are required in view of registering a new invoice
	 * Item.
	 */
	public void validateFields() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		if (this.invoiceItem.getMaterial().getIdMaterial() == 0) {
			ControladorContexto
					.mensajeRequeridos("formRegInvoiceItems:txtMaterial");
		}
		if (this.invoiceItem.getQuantity() <= 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:quantityItem",
					bundle.getString("message_campo_mayo_cero"));
		}
		if (this.invoiceItem.getUnitCost() <= 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:unitCostItem",
					bundle.getString("message_campo_mayo_cero"));
		}
		if (this.invoiceItem.getSubTotal() <= 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:subTotalItem",
					bundle.getString("message_campo_mayo_cero"));
		}
		if (this.invoiceItem.getTotal() <= 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:totalItem",
					bundle.getString("message_campo_mayo_cero"));
		}
		if (this.invoiceItem.getShipping() < 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:shippingItem",
					bundle.getString("message_campo_positivo"));
		}
		if (this.invoiceItem.getPackaging() < 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:packagingItem",
					bundle.getString("message_campo_positivo"));
		}
		if (this.invoiceItem.getHandling() < 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:handlingItem",
					bundle.getString("message_campo_positivo"));
		}
		if (this.invoiceItem.getDiscount() < 0) {
			ControladorContexto.mensajeError(null,
					"formRegInvoiceItems:discountItem",
					bundle.getString("message_campo_positivo"));
		}
	}

	/**
	 * This method allows you to load the IVA rate in interface for registering
	 * a new invoice items.
	 * 
	 * @throws Exception
	 */
	private void loadIvaRate() throws Exception {
		itemsIva = new ArrayList<SelectItem>();
		List<IvaRate> ivaRateList = ivaRateDao.consultIvaRate();
		if (ivaRateList != null) {
			for (IvaRate ivaRate : ivaRateList) {
				itemsIva.add(new SelectItem(ivaRate.getIdIva(), ivaRate
						.getName()));
			}
		}
	}
}