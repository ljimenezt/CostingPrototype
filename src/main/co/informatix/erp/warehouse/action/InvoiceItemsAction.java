package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.warehouse.dao.InvoiceItemsDao;
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
	@Resource
	private UserTransaction userTransaction;

	private List<InvoiceItems> invoiceItemsList;
	private Paginador pagination = new Paginador();
	private PurchaseInvoices invoicesSelected;
	private InvoiceItems invoiceItem;

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
}
