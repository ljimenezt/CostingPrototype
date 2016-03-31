package co.informatix.erp.warehouse.action;

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
import co.informatix.erp.warehouse.dao.InvoiceItemsDao;
import co.informatix.erp.warehouse.dao.MaterialsDao;
import co.informatix.erp.warehouse.dao.MaterialsTypeDao;
import co.informatix.erp.warehouse.entities.InvoiceItems;
import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.MaterialsType;
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
	private MaterialsTypeDao materialsTypeDao;
	@EJB
	private MaterialsDao materialsDao;

	private List<InvoiceItems> invoiceItemsList;
	private List<SelectItem> itemsMaterialsType;
	private List<SelectItem> itemsMaterials;
	private Paginador pagination = new Paginador();
	private PurchaseInvoices invoicesSelected;
	private InvoiceItems invoiceItem;

	private int idMaterialsType;

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
	 * Method to edit or create a new invoiceItem.
	 * 
	 * @param invoiceItem
	 *            :invoiceItem are adding or editing
	 */
	public void addEditInvoiceItems(InvoiceItems invoiceItem) {
		try {
			if (invoiceItem != null) {
				this.invoiceItem = invoiceItem;
				this.idMaterialsType = this.invoiceItem.getMaterial()
						.getMaterialType().getIdMaterialsType();
			} else {
				this.invoiceItem = new InvoiceItems();
				this.invoiceItem.setMaterial(new Materials());
				this.idMaterialsType = 0;
			}
			loadMaterialsType();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Consult the list of invoice Items
	 */
	public void consultInvoiceItems() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		invoiceItemsList = new ArrayList<InvoiceItems>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder allMessageSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearchInvoiceItems(consult, parameters, bundle,
					allMessageSearch);
			Long quantity = invoiceItemsDao.quantityInvoiceItems(consult,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				this.invoiceItemsList = invoiceItemsDao.consultInvoiceItems(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters);
			}
			if ((invoiceItemsList == null || invoiceItemsList.size() <= 0)
					&& !"".equals(allMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								allMessageSearch);
			} else if (invoiceItemsList == null || invoiceItemsList.size() <= 0) {
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

	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
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
			consult.append("WHERE it.purchaseInvoice = :purchaseInvoice ");
			SelectItem item = new SelectItem(this.invoicesSelected,
					"purchaseInvoice");
			parameters.add(item);
		}
	}

	/**
	 * This method allows load the materials type list.
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
	 * This method allows load the materials list.
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
}
