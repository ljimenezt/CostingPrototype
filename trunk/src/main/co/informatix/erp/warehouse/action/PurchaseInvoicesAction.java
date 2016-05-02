package co.informatix.erp.warehouse.action;

import java.io.File;
import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.InvoiceItemsDao;
import co.informatix.erp.warehouse.dao.PurchaseInvoicesDao;
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.entities.InvoiceItems;
import co.informatix.erp.warehouse.entities.PurchaseInvoices;
import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * This class is all related logic with creating, updating and removal of
 * purchase invoices.
 * 
 * @author Liseth.Jimenez
 * @modify 20/04/2016 Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PurchaseInvoicesAction implements Serializable {

	@EJB
	private PurchaseInvoicesDao purchaseInvoicesDao;
	@EJB
	private SuppliersDao suppliersDao;
	@EJB
	private InvoiceItemsDao invoiceItemsDao;
	@EJB
	private FileUploadBean fileUploadBean;
	@Resource
	private UserTransaction userTransaction;

	private String searchNumber;
	private String nameDocument;
	private String folderFileTemporal;
	private String locationServer;
	private String locationLocal;
	private String pathLocation;

	private boolean loadDocumentTemporal;
	private boolean iconPdf;
	private boolean iconImg;
	private boolean flag;

	private List<PurchaseInvoices> listInovoices;
	private List<SelectItem> itemsSupplier;
	private PurchaseInvoices invoices;
	private PurchaseInvoices invoicesActualSelected;
	private InvoiceItemsAction invoiceItemsAction;

	private Date initialDateSearch;
	private Date finalDateSearch;

	private Paginador pagerForm = new Paginador();
	private Paginador pagination = new Paginador();

	private int idSupplier;

	/**
	 * 
	 * @return fileUploadBean: Variable that gets the object for uploading
	 *         files.
	 */
	public FileUploadBean getFileUploadBean() {
		return fileUploadBean;
	}

	/**
	 * @param fileUploadBean
	 *            : field that gets the object for uploading files.
	 */
	public void setFileUploadBean(FileUploadBean fileUploadBean) {
		this.fileUploadBean = fileUploadBean;
	}

	/**
	 * @return searchNumber: gets the number the invoices by which you want to
	 *         consult purchases invoices
	 */
	public String getSearchNumber() {
		return searchNumber;
	}

	/**
	 * @param searchNumber
	 *            : sets the number the invoices by which you want to consult
	 *            purchases invoices
	 */
	public void setSearchNumber(String searchNumber) {
		this.searchNumber = searchNumber;
	}

	/**
	 * @return nameDocument: file name that has the information associated to
	 *         the invoice.
	 */
	public String getNameDocument() {
		return nameDocument;
	}

	/**
	 * @return pathLocation actual folder path where to save the file associated
	 *         with a invoice.
	 */
	public String getPathLocation() {
		pathLocation = Constantes.FOLDER_FILES + Constantes.FOLDER_INVOICES;
		return pathLocation;
	}

	/**
	 * @param nameDocument
	 *            : file name that has the information associated to the
	 *            invoice.
	 */
	public void setNameDocument(String nameDocument) {
		this.nameDocument = nameDocument;
	}

	/**
	 * @return loadDocumentTemporal: Flag indicating whether the picture is
	 *         loaded from the temporary location or not
	 */
	public boolean getLoadDocumentTemporal() {
		return loadDocumentTemporal;
	}

	/**
	 * @param loadDocumentTemporal
	 *            : Flag indicating whether the picture is loaded from the
	 *            temporary location or not
	 */
	public void setLoadDocumentTemporal(boolean loadDocumentTemporal) {
		this.loadDocumentTemporal = loadDocumentTemporal;
	}

	/**
	 * @return iconPdf: Flag indicating whether the file is loaded from the
	 *         temporary location or not
	 */
	public boolean getIconPdf() {
		return iconPdf;
	}

	/**
	 * @param iconPdf
	 *            : Flag indicating whether the file is loaded from the
	 *            temporary location or not
	 */
	public void setIconPdf(boolean iconPdf) {
		this.iconPdf = iconPdf;
	}

	/**
	 * @return iconImg: Flag indicating if the file is loaded from the temporary
	 *         location or not and is a image
	 */
	public boolean isIconImg() {
		return iconImg;
	}

	/**
	 * @param iconImg
	 *            :Flag indicating if the file is loaded from the temporary
	 *            location or not and is a image
	 */
	public void setIconImg(boolean iconImg) {
		this.iconImg = iconImg;
	}

	/**
	 * @return flag: This field allows to validate if added or modified a
	 *         invoice items.
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @return listInovoices: gets the list of purchases invoices
	 */
	public List<PurchaseInvoices> getListInovoices() {
		return listInovoices;
	}

	/**
	 * @param listInovoices
	 *            : sets the list of purchases invoices
	 */
	public void setListInovoices(List<PurchaseInvoices> listInovoices) {
		this.listInovoices = listInovoices;
	}

	/**
	 * @return itemsSupplier: List of supplier that are loaded into the user
	 *         interface.
	 */
	public List<SelectItem> getItemsSupplier() {
		return itemsSupplier;
	}

	/**
	 * @param itemsSupplier
	 *            :List of supplier that are loaded into the user interface.
	 */
	public void setItemsSupplier(List<SelectItem> itemsSupplier) {
		this.itemsSupplier = itemsSupplier;
	}

	/**
	 * @return invoice: sets object of purchases invoices
	 */
	public PurchaseInvoices getInvoices() {
		return invoices;
	}

	/**
	 * @param invoice
	 *            :gets object of purchases invoices
	 */
	public void setInvoices(PurchaseInvoices invoices) {
		this.invoices = invoices;
	}

	/**
	 * @return invoicesActualSelected: Deposit selected in the purchaseInvoices
	 *         table
	 */
	public PurchaseInvoices getInvoicesActualSelected() {
		return invoicesActualSelected;
	}

	/**
	 * @param invoicesActualSelected
	 *            : PurchaseInvoices selected in the purchaseInvoices table
	 */
	public void setInvoicesActualSelected(
			PurchaseInvoices invoicesActualSelected) {
		this.invoicesActualSelected = invoicesActualSelected;
	}

	/**
	 * @return invoiceItemsAction: Object of invoiceItems action
	 */
	public InvoiceItemsAction getInvoiceItemsAction() {
		return invoiceItemsAction;
	}

	/**
	 * @param invoiceItemsAction
	 *            : Object of invoiceItems action
	 */
	public void setInvoiceItemsAction(InvoiceItemsAction invoiceItemsAction) {
		this.invoiceItemsAction = invoiceItemsAction;
	}

	/**
	 * @return initialDateSearch: gets the initial date of the supplier to
	 *         search in a range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            :sets the initial date of the supplier to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the final date of the supplier to search in
	 *         a range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            :sets the final date of the supplier to search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return pagerForm: Management purchases invoices paged list from search
	 *         engine.
	 */
	public Paginador getPagerForm() {
		return pagerForm;
	}

	/**
	 * @param pagerForm
	 *            : Management purchases invoices paged list from search engine.
	 */
	public void setPagerForm(Paginador pagerForm) {
		this.pagerForm = pagerForm;
	}

	/**
	 * @return pagination : Paginated list of purchase invoices which can be in
	 *         view
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paginated list of purchase invoices which can be in view
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return idSupplier: Supplier identifier.
	 */
	public int getIdSupplier() {
		return idSupplier;
	}

	/**
	 * @param idSupplier
	 *            : Supplier identifier.
	 */
	public void setIdSupplier(int idSupplier) {
		this.idSupplier = idSupplier;
	}

	/**
	 * @return folderFileTemporal: path of the temporary folder where the
	 *         document of the cycle are loaded.
	 */
	public String getFolderFileTemporal() {
		this.folderFileTemporal = Constantes.CARPETA_ARCHIVOS_SUBIDOS
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return folderFileTemporal;
	}

	/**
	 * This Method assigned the values to the variables
	 */
	public void getLocations() {
		locationServer = Constantes.RUTA_UPLOADFILE_GLASFISH + pathLocation;
		locationServer = locationServer.replace("\\", "/");
		locationLocal = Constantes.RUTA_UPLOADFILE_WORKSPACE + pathLocation;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @modify 29/03/2016 Wilhelm.Boada
	 * 
	 * @return consultInvoices: Method that consultation purchase invoices and
	 *         load the template with the information found
	 */
	public String searchInitialize() {
		try {
			loadSuppliers();
			if (ControladorContexto.getFacesContext() != null) {
				this.invoiceItemsAction = ControladorContexto
						.getContextBean(InvoiceItemsAction.class);
			}
			this.searchNumber = "";
			this.initialDateSearch = null;
			this.finalDateSearch = null;
			this.invoicesActualSelected = null;
			this.invoices = new PurchaseInvoices();
			this.flag = false;
			this.idSupplier = 0;

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultInvoices();
	}

	/**
	 * This method allows consult a list of purchase invoices
	 * 
	 * @return giveBack: Navigation rule that redirects to manage purchase
	 *         invoices according condition
	 */
	public String consultInvoices() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		String giveBack = fromModal ? "" : "manInvoices";
		this.listInovoices = new ArrayList<PurchaseInvoices>();
		try {
			advancedSearch(consult, parameters, bundle, bundleWarehouse,
					unionSearchMessages);
			Long count = purchaseInvoicesDao.countPurchaseInvoices(consult,
					parameters);
			if (count != null) {
				if (fromModal) {
					pagerForm.paginarRangoDefinido(count, 5);
					this.listInovoices = purchaseInvoicesDao
							.listPurchaseInvoices(pagerForm.getInicio(),
									pagerForm.getRango(), consult, parameters);
				} else {
					pagination.paginar(count);
					this.listInovoices = purchaseInvoicesDao
							.listPurchaseInvoices(pagination.getInicio(),
									pagination.getRango(), consult, parameters);
				}
			}

			if ((listInovoices == null || listInovoices.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (listInovoices == null || listInovoices.size() <= 0) {
				searchMessages = bundle
						.getString("message_no_existen_registros");
			} else if (!"".equals(unionSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessages = MessageFormat.format(message,
						bundleWarehouse.getString("purchase_invoice_label"),
						unionSearchMessages);
			}

			if (fromModal) {
				validations.setMensajeBusquedaPopUp(searchMessages);
			} else {
				validations.setMensajeBusqueda(searchMessages);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return giveBack;
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @modify 29/03/2016 Wilhelm.Boada
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            : access language tags
	 * @param bundleWarehouse
	 *            : access language tags warehouse
	 * @param unionSearchMessages
	 *            : Message search
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			ResourceBundle bundleWarehouse, StringBuilder unionSearchMessages) {
		boolean flag = false;
		SimpleDateFormat formatDate = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);

		if ((this.searchNumber != null && !"".equals(this.searchNumber))) {
			consult.append(flag ? "AND " : "WHERE ");
			consult.append(" UPPER(pi.invoiceNumber) LIKE UPPER(:keywordNumber) ");
			SelectItem itemNombre = new SelectItem("%" + this.searchNumber
					+ "%", "keywordNumber");
			parameters.add(itemNombre);
			unionSearchMessages.append(bundleWarehouse
					.getString("purchase_invoice_label_number")
					+ ": "
					+ '"'
					+ this.searchNumber + '"' + " ");
			flag = true;
		}

		if (this.idSupplier != 0) {
			consult.append(flag ? "AND " : "WHERE ");
			consult.append("s.idSupplier = :keyword3 ");
			SelectItem item = new SelectItem(this.idSupplier, "keyword3");
			parameters.add(item);
			String supplierName = (String) ValidacionesAction.getLabel(
					itemsSupplier, this.idSupplier);
			unionSearchMessages.append(bundleWarehouse
					.getString("suppliers_label_name")
					+ ": "
					+ '"'
					+ supplierName + '"' + " ");
			flag = true;
		}

		if (this.initialDateSearch != null || this.finalDateSearch != null) {
			consult.append(flag ? "AND " : "WHERE ");
			if (this.initialDateSearch != null && this.finalDateSearch != null) {
				consult.append("pi.dateTime BETWEEN :initialDateSearch AND :finalDateSearch ");
			}
			if (this.initialDateSearch != null && this.finalDateSearch == null) {
				consult.append("pi.dateTime >= :initialDateSearch ");
			}
			if (this.initialDateSearch == null && this.finalDateSearch != null) {
				consult.append("pi.dateTime <= :finalDateSearch ");
			}
			if (this.initialDateSearch != null) {
				SelectItem item = new SelectItem(initialDateSearch,
						"initialDateSearch");
				parameters.add(item);
				String dateFrom = bundle.getString("label_start_date") + ": "
						+ '"' + formatDate.format(this.initialDateSearch) + '"'
						+ " ";
				unionSearchMessages.append(dateFrom);
			}
			if (this.finalDateSearch != null) {
				SelectItem item2 = new SelectItem(finalDateSearch,
						"finalDateSearch");
				parameters.add(item2);
				String dateTo = bundle.getString("label_end_date") + ": " + '"'
						+ formatDate.format(finalDateSearch) + '"' + " ";
				unionSearchMessages.append(dateTo);
			}
		}
	}

	/**
	 * Method to edit or create a new invoice.
	 * 
	 * @param invoices
	 *            :invoice are adding or editing
	 * 
	 * @return "regInvoice":redirected to the template record invoice.
	 */
	public String addEditInvoices(PurchaseInvoices invoices) {
		try {
			loadSuppliers();
			cleanItemList();
			if (invoices != null) {
				this.invoices = invoices;
				this.nameDocument = invoices.getInvoiceDocumentLink();
				if (!("").equals(nameDocument) && nameDocument != null) {
					relocateFileTemp();
				}
				selectInvoice(invoices);
				showInvoiceItems();
			} else {
				this.invoices = new PurchaseInvoices();
				this.invoices.setSuppliers(new Suppliers());
				this.nameDocument = null;
				invoiceItemsAction.cleanLists();
			}
			this.loadDocumentTemporal = true;
			this.flag = true;
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regInvoice";
	}

	/**
	 * This method allow clean the list of the invoiceItems list
	 * 
	 */
	private void cleanItemList() {
		this.invoiceItemsAction = ControladorContexto
				.getContextBean(InvoiceItemsAction.class);
		invoiceItemsAction.setInvoiceItemsList(new ArrayList<InvoiceItems>());
		invoiceItemsAction
				.setSubListInvoiceItems(new ArrayList<InvoiceItems>());
	}

	/**
	 * This method allows relocate file saving in the server to copy in the
	 * temporal folder when the invoice have a document
	 * 
	 * @throws Exception
	 * 
	 */
	private void relocateFileTemp() throws Exception {
		getPathLocation();
		getFolderFileTemporal();
		getLocations();
		String suffix = FilenameUtils.getExtension(nameDocument);
		if (suffix.equals(Constantes.FILE_EXT_PDF)) {
			iconPdf = true;
		} else if (suffix.equals(Constantes.FILE_EXT_DOCX)
				|| suffix.equals(Constantes.FILE_EXT_DOC)) {
			iconImg = false;
			iconPdf = false;
		} else {
			iconImg = true;
			iconPdf = false;
		}
		String location = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ folderFileTemporal;
		File fileTemp = new File(locationServer + "/" + nameDocument);
		FileUploadBean.fileCopyLocationReal(fileTemp, location);
	}

	/**
	 * This method allows you to load the suppliers in interface for registering
	 * a new invoice.
	 * 
	 * 
	 * @throws Exception
	 */
	private void loadSuppliers() throws Exception {
		itemsSupplier = new ArrayList<SelectItem>();
		List<Suppliers> supplierList = suppliersDao.querySuppliers();
		if (supplierList != null) {
			for (Suppliers supplier : supplierList) {
				itemsSupplier.add(new SelectItem(supplier.getIdSupplier(),
						supplier.getName()));
			}
		}
	}

	/**
	 * Selects a single purchase invoice for display the associated transaction
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param invoiceSelected
	 *            : invoice selected on the view
	 */
	public void selectInvoice(PurchaseInvoices invoiceSelected) {
		this.invoicesActualSelected = new PurchaseInvoices();
		this.flag = false;
		invoiceSelected.setSelected(true);
		for (PurchaseInvoices invoice : listInovoices) {
			if (invoice.isSelected()) {
				if (invoice.getIdPurchaseInvoice() == invoiceSelected
						.getIdPurchaseInvoice()) {
					this.invoicesActualSelected = invoice;
				} else {
					invoice.setSelected(false);
				}
			}
		}
	}

	/**
	 * Show the invoices item associated to purchase invoices.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void showInvoiceItems() {
		if (invoiceItemsAction != null) {
			invoiceItemsAction.setInvoicesSelected(invoicesActualSelected);
			invoiceItemsAction.consultInvoiceItems();
		}
	}

	/**
	 * Method allows you to load the file system.
	 * 
	 * @param e
	 *            : Fileupload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String extAccepted[] = Constantes.EXT_PDF_DOC_IMG.split(", ");
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFileTemporal() };
		fileUploadBean.setUploadedFile(e.getFile());
		long maximuSizeFile = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(extAccepted,
				locations, maximuSizeFile);
		String message = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			message = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"), maximuSizeFile,
					"MB");
			ControladorContexto.mensajeError("formInvoices:uploadDocument",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			message = "error_carga_archivo";
		}
		if (!"".equals(message)) {
			ControladorContexto.mensajeError("formInvoices:uploadDocument",
					bundle.getString(message));
		}
		this.nameDocument = fileUploadBean.getFileName();
		if (Constantes.OK.equals(resultUpload)) {
			String suffix = FilenameUtils.getExtension(this.nameDocument);
			if (suffix.equals(Constantes.FILE_EXT_PDF)) {
				iconPdf = true;
			} else if (suffix.equals(Constantes.FILE_EXT_DOCX)
					|| suffix.equals(Constantes.FILE_EXT_DOC)) {
				iconImg = false;
				iconPdf = false;
			} else {
				iconImg = true;
				iconPdf = false;
			}
		}
	}

	/**
	 * Delete the file name.
	 * 
	 */
	public void deleteFilename() {
		if (this.nameDocument != null && !"".equals(this.nameDocument)
				&& this.loadDocumentTemporal) {
			deleteFile(this.nameDocument);
		}
		this.nameDocument = null;
		fileUploadBean.setFileName(null);
	}

	/**
	 * Delete the files.
	 * 
	 * @param fileName
	 *            : Name of the file to delete.
	 * 
	 */
	public void deleteFile(String fileName) {
		String locations[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFileTemporal() };
		fileUploadBean.delete(locations, fileName);
	}

	/**
	 * Method used to save or edit the invoices
	 * 
	 * @modify 11/04/2016 Wilhelm.Boada
	 * 
	 * @return searchInitialize: Redirects to manage deposits with a list of
	 *         updated deposits
	 */
	public String saveInvoices() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_purchase_invoice_modify";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		InvoiceItemsAction invoiceItemsAction = ControladorContexto
				.getContextBean(InvoiceItemsAction.class);
		ControladorContexto.quitarFacesMessages();
		try {
			userTransaction.begin();
			if (!flag) {
				this.invoices = this.invoicesActualSelected;
			} else {
				getPathLocation();
				getLocations();
				getFolderFileTemporal();
				String nameActualDocument = this.invoices
						.getInvoiceDocumentLink();
				if (!("").equals(nameDocument) && nameDocument != null) {
					if (!("").equals(nameActualDocument)
							&& nameActualDocument != null) {
						if (nameDocument != nameActualDocument) {
							deleteOldFile(nameActualDocument);
						}
					}
					saveFiles();
				} else {
					if (nameActualDocument != null) {
						deleteOldFile(nameActualDocument);
					}
				}
				this.invoices.setInvoiceDocumentLink(nameDocument);
			}
			if (this.invoices.getIdPurchaseInvoice() != 0) {
				purchaseInvoicesDao.editInvoices(this.invoices);
			} else {
				mensajeRegistro = "message_purchase_invoice_save";
				purchaseInvoicesDao.saveInvoices(this.invoices);
			}
			invoiceItemsAction.setInvoicesSelected(this.invoices);
			invoiceItemsAction.saveUpdateInvoiceItem();
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					invoices.getInvoiceNumber()));
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception exception2) {
				ControladorContexto.mensajeError(exception2);
			}
			ControladorContexto.mensajeError(e);
		}
		if (fromModal) {
			return "";
		} else if (!flag) {
			return consultInvoices();
		} else {
			return searchInitialize();
		}
	}

	/**
	 * This method allows delete the old file saving preview to replace with the
	 * new file
	 * 
	 * @param nameActualDocument
	 *            :name of the actual document that invoice have
	 */
	private void deleteOldFile(String nameActualDocument) {
		String locationTemp[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getFolderFileTemporal() };
		String locServer[] = { locationServer };
		String locnLocal[] = { locationLocal };
		fileUploadBean.delete(locationTemp, nameActualDocument);
		fileUploadBean.delete(locServer, nameActualDocument);
		fileUploadBean.delete(locnLocal, nameActualDocument);
	}

	/**
	 * This method allow save the file in the server and the local path
	 * 
	 * @throws Exception
	 * 
	 */
	private void saveFiles() throws Exception {
		String location = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ folderFileTemporal + "/" + nameDocument;
		File fileTemp = new File(location);
		FileUploadBean.fileCopyLocationReal(fileTemp, locationServer);
		FileUploadBean.fileCopyLocationReal(fileTemp, locationLocal);
	}

	/**
	 * This method allow calculate the values according with the items invoice
	 * values
	 * 
	 * @throws Exception
	 */
	private void calculateValuesInvoices() throws Exception {
		int idPurchaseInvoice = this.invoices.getIdPurchaseInvoice();
		Object[] values = invoiceItemsDao.consultValuesItems(idPurchaseInvoice);
		if (values[0] != null) {
			double subtotal = (double) values[0];
			double shipping = (double) values[1];
			double packaging = (double) values[2];
			double taxes = (double) values[3];
			double discount = (double) values[4];
			double totalValueActual = (double) values[5];
			this.invoices.setSubtotal(subtotal);
			this.invoices.setShipping(shipping);
			this.invoices.setPackaging(packaging);
			this.invoices.setTaxes(taxes);
			this.invoices.setDiscount(discount);
			this.invoices.setTotalValueActual(totalValueActual);
		}
	}

	/**
	 * This method allows set the invoice selected in the invoices list
	 * 
	 * @modify 02/05/2016 Andres.Gomez
	 * @author Wilhelm.Boada
	 * 
	 */
	public void selectInvoiceCalculate() {
		try {
			calculateValuesInvoices();
			saveInvoices();
			for (PurchaseInvoices invoice : listInovoices) {
				int value = listInovoices.indexOf(invoice);
				if (invoice.getIdPurchaseInvoice() == invoicesActualSelected
						.getIdPurchaseInvoice()) {
					listInovoices.set(value, invoicesActualSelected);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * To validate the name of the allocation, to not repeat in the database and
	 * validates against XSS.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param context
	 *            : Application context.
	 * 
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Field value is validated.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int idSupplier = invoices.getSuppliers().getIdSupplier();
			PurchaseInvoices auxInvoice = purchaseInvoicesDao.nameExists(name,
					idSupplier);
			if (auxInvoice != null) {
				if (invoices.getIdPurchaseInvoice() != auxInvoice
						.getIdPurchaseInvoice()) {
					String messageExistence = "message_ya_existe_verifique";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							messageExistence, "mensaje");
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(name, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}

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
		InvoiceItemsAction invoiceItemsAction = ControladorContexto
				.getContextBean(InvoiceItemsAction.class);
		if (this.invoices.getDateTime() == null) {
			ControladorContexto.mensajeRequeridos("formInvoices:dateInvoice");
		}
		if (this.invoices.getSuppliers().getIdSupplier() == 0) {
			ControladorContexto.mensajeRequeridos("formInvoices:supplier");
		}
		if (("").equals(this.invoices.getInvoiceNumber())) {
			ControladorContexto.mensajeRequeridos("formInvoices:txtNumInv");
		}
		if (invoiceItemsAction.getInvoiceItemsList().size() <= 0) {
			ControladorContexto.mensajeError(null, null,
					bundle.getString("message_select_one_invoice_item"));
		}
		if (this.invoices.getSubtotal() == 0) {
			ControladorContexto.mensajeError(null,
					"formInvoices:subTotalValue",
					bundle.getString("message_campo_mayo_cero"));
		}
		if (this.invoices.getTotalValueActual() == 0) {
			ControladorContexto.mensajeError(null, "formInvoices:totalValue",
					bundle.getString("message_campo_mayo_cero"));
		}
		if (this.invoices.getTaxes() < 0) {
			ControladorContexto.mensajeError(null, "formInvoices:taxes",
					bundle.getString("message_campo_positivo"));
		}
		if (this.invoices.getShipping() < 0) {
			ControladorContexto.mensajeError(null, "formInvoices:shipping",
					bundle.getString("message_campo_positivo"));
		}
		if (this.invoices.getPackaging() < 0) {
			ControladorContexto.mensajeError(null, "formInvoices:packaging",
					bundle.getString("message_campo_positivo"));
		}
		if (this.invoices.getDiscount() < 0) {
			ControladorContexto.mensajeError(null, "formInvoices:discount",
					bundle.getString("message_campo_positivo"));
		}
	}
}
