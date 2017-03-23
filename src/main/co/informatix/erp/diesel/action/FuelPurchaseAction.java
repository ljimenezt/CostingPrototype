package co.informatix.erp.diesel.action;

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

import co.informatix.erp.diesel.dao.ConsumableResourcesDao;
import co.informatix.erp.diesel.dao.FuelPurchaseDao;
import co.informatix.erp.diesel.dao.FuelUsageLogDao;
import co.informatix.erp.diesel.entities.FuelPurchase;
import co.informatix.erp.diesel.entities.FuelUsageLog;
import co.informatix.erp.informacionBase.dao.IvaRateDao;
import co.informatix.erp.informacionBase.entities.IvaRate;
import co.informatix.erp.machines.dao.FuelTypesDao;
import co.informatix.erp.machines.entities.FuelTypes;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControllerAccounting;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.FileUploadBean;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.SuppliersDao;
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.entities.Suppliers;
import co.informatix.erp.warehouse.entities.TransactionType;

/**
 * This class is used to create, update, and query of a fuel purchase in the
 * information system.
 * 
 * @author Claudia.Rey
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class FuelPurchaseAction implements Serializable {

	private String nameDocument;
	private String filesFolder;
	private String temporalFilesFolder;

	private String folderFileTemporal;
	private String locationServer;
	private String locationLocal;
	private String pathLocation;

	private boolean loadDocumentTemporal;
	private boolean iconPdf;
	private boolean iconImg;
	private int idSupplier;

	private Date initialDateSearch;
	private Date finalDateSearch;

	private List<SelectItem> itemsSuppliers;
	private List<SelectItem> itemsFuelTypes;
	private List<SelectItem> itemsIvaRate;
	private List<FuelPurchase> listFuelPurchase;

	private FuelPurchase fuelPurchase;
	private Suppliers suppliers;
	private FuelTypes fuelTypes;
	private IvaRate ivaRate;
	private TransactionType transactionType;
	private FileUploadBean fileUploadBean;
	private FuelUsageLog fuelUsageLog;
	private Paginador pagination = new Paginador();

	@EJB
	private FuelPurchaseDao fuelPurchaseDao;
	@EJB
	private SuppliersDao suppliersDao;
	@EJB
	private FuelTypesDao fuelTypesDao;
	@EJB
	private IvaRateDao ivaRateDao;
	@EJB
	private TransactionTypeDao transactionTypeDao;
	@EJB
	private FuelUsageLogDao fuelUsageLogDao;
	@Resource
	private UserTransaction userTransaction;
	@EJB
	private ConsumableResourcesDao consumableResourcesDao;

	/**
	 * @return nameDocument: File name that has the information associated to
	 *         the fuel purchase.
	 */
	public String getNameDocument() {
		return nameDocument;
	}

	/**
	 * @return filesFolder: Route for the real folder where pictures of fuel
	 *         document are loaded.
	 */
	public String getFilesFolder() {
		this.filesFolder = Constantes.FOLDER_FILES
				+ Constantes.FOLDER_FILES_FUEL;
		return filesFolder;
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
	 * @return temporalFilesFolder: Path to the temporary folder where the fuel
	 *         documents are loaded.
	 */
	public String getTemporalFilesFolder() {
		this.temporalFilesFolder = Constantes.FOLDER_FILES
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return temporalFilesFolder;
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
	 * @return pathLocation actual folder path where to save the file associated
	 *         with a fuel purchase.
	 */
	public String getPathLocation() {
		this.pathLocation = Constantes.FOLDER_FILES
				+ Constantes.FOLDER_FILES_FUEL;
		return pathLocation;
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
	 * @return idSupplier: identifier of supplier selected.
	 */
	public int getIdSupplier() {
		return idSupplier;
	}

	/**
	 * @param idSupplier
	 *            : identifier of supplier selected.
	 */
	public void setIdSupplier(int idSupplier) {
		this.idSupplier = idSupplier;
	}

	/**
	 * @return initialDateSearch: gets the initial date of the search in a
	 *         range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            :sets the initial date of the search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the final date of the search in a range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            :sets the final date of the search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return itemsSuppliers: List of suppliers associated with fuel purchase.
	 */
	public List<SelectItem> getItemsSuppliers() {
		return itemsSuppliers;
	}

	/**
	 * @param itemsSuppliers
	 *            : List of suppliers associated with fuel purchase.
	 */
	public void setItemsSuppliers(List<SelectItem> itemsSuppliers) {
		this.itemsSuppliers = itemsSuppliers;
	}

	/**
	 * @return itemsFuelTypes: List of fuel types associated with fuel purchase.
	 */
	public List<SelectItem> getItemsFuelTypes() {
		return itemsFuelTypes;
	}

	/**
	 * @param itemsFuelTypes
	 *            : List of fuel types associated with fuel purchase.
	 */
	public void setItemsFuelTypes(List<SelectItem> itemsFuelTypes) {
		this.itemsFuelTypes = itemsFuelTypes;
	}

	/**
	 * @return itemsIvaRate: List of iva rate associated with fuel purchase.
	 */
	public List<SelectItem> getItemsIvaRate() {
		return itemsIvaRate;
	}

	/**
	 * @param itemsIvaRate
	 *            : List of iva rate associated with fuel purchase.
	 */
	public void setItemsIvaRate(List<SelectItem> itemsIvaRate) {
		this.itemsIvaRate = itemsIvaRate;
	}

	/**
	 * @return listFuelPurchase: list of objects of Fuel Purchase.
	 */
	public List<FuelPurchase> getListFuelPurchase() {
		return listFuelPurchase;
	}

	/**
	 * @param listFuelPurchase
	 *            : list of objects of Fuel Purchase.
	 */
	public void setListFuelPurchase(List<FuelPurchase> listFuelPurchase) {
		this.listFuelPurchase = listFuelPurchase;
	}

	/**
	 * @return suppliers: Object supplier
	 */
	public Suppliers getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            : Object supplier
	 */
	public void setSuppliers(Suppliers suppliers) {
		this.suppliers = suppliers;
	}

	/**
	 * @return fuelTypes: Object fuel type
	 */
	public FuelTypes getFuelTypes() {
		return fuelTypes;
	}

	/**
	 * @param fuelTypes
	 *            : Object fuel type
	 */
	public void setFuelTypes(FuelTypes fuelTypes) {
		this.fuelTypes = fuelTypes;
	}

	/**
	 * @return ivaRate: Object iva rate
	 */
	public IvaRate getIvaRate() {
		return ivaRate;
	}

	/**
	 * @param ivaRate
	 *            : Object iva rate
	 */
	public void setIvaRate(IvaRate ivaRate) {
		this.ivaRate = ivaRate;
	}

	/**
	 * @return fuelPurchase: Object fuelPurchase stored in data base.
	 */
	public FuelPurchase getFuelPurchase() {
		return fuelPurchase;
	}

	/**
	 * @param fuelPurchase
	 *            : Object fuelPurchase stored in data base.
	 */
	public void setFuelPurchase(FuelPurchase fuelPurchase) {
		this.fuelPurchase = fuelPurchase;
	}

	/**
	 * @return pagination: Management paged list of fuel purchase.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of fuel purchase.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of fuel purchase.
	 * 
	 * @author Luna.Granados
	 * 
	 * @return consultFuelPurchase: method that allows consulting the fuel
	 *         purchase, it redirects to the manage template.
	 */
	public String searchInitialization() {
		try {
			this.fuelPurchase = new FuelPurchase();
			this.idSupplier = 0;
			this.initialDateSearch = null;
			this.finalDateSearch = null;
			loadComboSuppliers();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultFuelPurchase();
	}

	/**
	 * Consult the list of the fuel purchase.
	 * 
	 * @author Luna.Granados
	 * 
	 * @return "gesFuelPurchase": It redirects to the template to manage the
	 *         fuel purchase.
	 */
	public String consultFuelPurchase() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleFuelPurchase = ControladorContexto
				.getBundle("messageDiesel");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listFuelPurchase = new ArrayList<FuelPurchase>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(consult, parameters, bundle, unionMessagesSearch);
			Long quantity = fuelPurchaseDao.quantityFuelPurchase(consult,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listFuelPurchase = fuelPurchaseDao.consultFuelPurchase(
					pagination.getInicio(), pagination.getRango(), consult,
					parameters);
			if ((listFuelPurchase == null || listFuelPurchase.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listFuelPurchase == null || listFuelPurchase.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleFuelPurchase
										.getString("fuel_purchase_label_name"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesFuelPurchase";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @author Luna.Granados
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		ResourceBundle bundleFuelPurchase = ControladorContexto
				.getBundle("messageDiesel");
		ResourceBundle bundleWarehouse = ControladorContexto
				.getBundle("mensajeWarehouse");
		boolean selection = false;

		if (!"".equals(this.fuelPurchase.getInvoiceNumber())
				&& this.fuelPurchase.getInvoiceNumber() != null) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("UPPER(fp.invoiceNumber)=UPPER(:invoiceNumber) ");
			SelectItem item = new SelectItem(
					this.fuelPurchase.getInvoiceNumber(), "invoiceNumber");
			parameters.add(item);
			unionMessagesSearch.append(bundleWarehouse
					.getString("purchase_invoice_label_number")
					+ ": "
					+ '"'
					+ this.fuelPurchase.getInvoiceNumber() + '"' + " ");
			selection = true;
		}

		if (this.idSupplier != 0) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("s.idSupplier = :idSupplier ");
			SelectItem item = new SelectItem(this.idSupplier, "idSupplier");
			parameters.add(item);
			String supplier = (String) ValidacionesAction.getLabel(
					itemsSuppliers, this.idSupplier);
			unionMessagesSearch.append(bundleFuelPurchase
					.getString("fuel_purchase_label_supplier")
					+ ": "
					+ '"'
					+ supplier + '"' + " ");
			selection = true;
		}

		if (this.initialDateSearch != null || this.finalDateSearch != null) {
			consult.append(selection ? "AND " : "WHERE ");

			if (this.initialDateSearch != null && this.finalDateSearch != null) {
				consult.append("fp.dateTime BETWEEN :initialDateSearch AND :finalDateSearch ");
			}

			if (this.initialDateSearch != null && this.finalDateSearch == null) {
				consult.append("fp.dateTime >= :initialDateSearch ");
			}
			if (this.initialDateSearch == null && this.finalDateSearch != null) {
				consult.append("fp.dateTime <= :finalDateSearch ");
			}

			if (this.initialDateSearch != null) {
				SelectItem item = new SelectItem(initialDateSearch,
						"initialDateSearch");
				parameters.add(item);
				String dateFrom = bundle.getString("label_start_date") + ": "
						+ '"' + formato.format(this.initialDateSearch) + '"'
						+ " ";
				unionMessagesSearch.append(dateFrom);
			}

			if (this.finalDateSearch != null) {
				SelectItem item2 = new SelectItem(finalDateSearch,
						"finalDateSearch");
				parameters.add(item2);
				String dateTo = bundle.getString("label_end_date") + ": " + '"'
						+ formato.format(finalDateSearch) + '"' + " ";
				unionMessagesSearch.append(dateTo);
			}
			selection = true;
		}

	}

	/**
	 * Method to edit or create a new fuel purchase.
	 * 
	 * @return regFuelPurchase: Redirected to the template record fuel purchase.
	 */
	public String addFuelPurchase() {
		try {
			this.nameDocument = null;
			this.fuelPurchase = new FuelPurchase();
			this.fileUploadBean = new FileUploadBean();
			this.loadDocumentTemporal = true;
			transactionType = new TransactionType();
			loadComboSuppliers();
			loadComboFuelTypes();
			loadComboIvaRate();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regFuelPurchase";
	}

	/**
	 * Method that allows load the suppliers in a list.
	 * 
	 * @throws Exception
	 */
	private void loadComboSuppliers() throws Exception {
		suppliers = new Suppliers();
		itemsSuppliers = new ArrayList<SelectItem>();
		List<Suppliers> listSuppliers = suppliersDao.querySuppliers();
		if (listSuppliers != null) {
			for (Suppliers supplier : listSuppliers) {
				itemsSuppliers.add(new SelectItem(supplier.getIdSupplier(),
						supplier.getName()));
			}
		}
	}

	/**
	 * Method that allows load the fuel types in a list.
	 * 
	 * @throws Exception
	 */
	private void loadComboFuelTypes() throws Exception {
		fuelTypes = new FuelTypes();
		itemsFuelTypes = new ArrayList<SelectItem>();
		List<FuelTypes> listFuelTypes = fuelTypesDao.listFuelType();
		if (listFuelTypes != null) {
			for (FuelTypes fuelType : listFuelTypes) {
				itemsFuelTypes.add(new SelectItem(fuelType.getIdFuelType(),
						fuelType.getName()));
			}
		}
	}

	/**
	 * Method that allows load the suppliers in a list.
	 * 
	 * @throws Exception
	 */
	private void loadComboIvaRate() throws Exception {
		ivaRate = new IvaRate();
		itemsIvaRate = new ArrayList<SelectItem>();
		List<IvaRate> listIvaRate = ivaRateDao.consultIvaRate();
		if (listIvaRate != null) {
			for (IvaRate ivaRate : listIvaRate) {
				itemsIvaRate.add(new SelectItem(ivaRate.getIdIva(), String
						.valueOf(ivaRate.getRate())));
			}
		}
	}

	/**
	 * To validate the number of the invoice, to not repeat in the database and
	 * validates against XSS.
	 * 
	 * @param context
	 *            : Application context.
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
			int idSupplier = this.suppliers.getIdSupplier();
			FuelPurchase auxFuelPurchase = fuelPurchaseDao.numberExists(name,
					idSupplier);
			if (auxFuelPurchase != null) {
				if (fuelPurchase.getIdFuelPurchase() != auxFuelPurchase
						.getIdFuelPurchase()) {
					String messageExistence = "message_ya_existe_verifique";
					ControladorContexto.mensajeErrorEspecifico(clientId,
							messageExistence, "mensaje");
					((UIInput) toValidate).setValid(false);
				}
			}
			if (!EncodeFilter.validarXSS(name, clientId,
					"locate.regex.letras.numeros.guion")) {
				((UIInput) toValidate).setValid(false);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that calculate the unit cost of a fuel purchase
	 */
	public void calculateUnitCost() {
		this.fuelPurchase.setUnitCost(ControllerAccounting.divide(
				this.fuelPurchase.getSubTotal(),
				this.fuelPurchase.getQuantity()));
	}

	/**
	 * Method that calculate the taxes of a fuel purchase
	 */
	public void calculateTaxes() {
		try {
			IvaRate ivaRateAux = ivaRateDao.ivaRateXId(this.ivaRate.getIdIva());
			this.fuelPurchase.setTaxes(ControllerAccounting.divide(
					ControllerAccounting.multiply(fuelPurchase.getSubTotal(),
							ivaRateAux.getRate()), 100.0));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that calculate the total of a fuel purchase.
	 */
	private void calculateTotal() {
		this.fuelPurchase.setTotal(ControllerAccounting.add(
				this.fuelPurchase.getSubTotal(), this.fuelPurchase.getTaxes()));
	}

	/**
	 * Method that call to methods calculateTotal, calculateTaxes and
	 * CalculateUnitCost at the same time.
	 */
	public void calculateValors() {
		calculateUnitCost();
		if (ivaRate.getIdIva() > 0) {
			calculateTaxes();
		}else{
			this.fuelPurchase.setTaxes(0);
		}
		calculateTotal();
	}

	/**
	 * Delete the file name.
	 */
	public void deleteFileName() {
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
	 */
	public void deleteFile(String fileName) {
		String paths[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getTemporalFilesFolder() };
		fileUploadBean.delete(paths, fileName);
	}

	/**
	 * Method that save a fuel purchase in data base and save the fuel use log
	 * 
	 * @modify 03/03/2017 Luna.Granados
	 * 
	 * @return searchInitialization(): It redirects to the template to manage
	 *         the fuel purchase.
	 */
	public String saveFuelPurchase() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			userTransaction.begin();
			getPathLocation();
			getLocations();
			getFolderFileTemporal();
			String nameActualDocument = this.fuelPurchase
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
			this.fuelPurchase.setInvoiceDocumentLink(this.nameDocument);

			fuelPurchase.setFuelType(new FuelTypes());
			fuelPurchase.setFuelType(this.fuelTypes);

			if (this.ivaRate.getIdIva() > 0) {
				fuelPurchase.setIvaRate(new IvaRate());
				fuelPurchase.setIvaRate(this.ivaRate);
			}
			fuelPurchase.setSupplier(new Suppliers());
			fuelPurchase.setSupplier(this.suppliers);
			fuelPurchaseDao.createFuelPurchase(fuelPurchase);

			transactionType = transactionTypeDao
					.transactionTypeById(Constantes.TRANSACTION_TYPE_FUEL_PURCHASE);

			this.fuelUsageLog = new FuelUsageLog();
			this.fuelUsageLog.setFuelPurchase(fuelPurchase);
			this.fuelUsageLog.setDate(fuelPurchase.getDateTime());
			this.fuelUsageLog.setEngineLog(null);
			this.fuelUsageLog.setTransactionType(transactionType);
			this.fuelUsageLog.setDeposited((Double) fuelPurchase.getQuantity());
			FuelUsageLog fuelUsageLogAux = fuelUsageLogDao
					.consultLastFuelUsage();
			if (fuelUsageLogAux != null) {
				this.fuelUsageLog.setFinalLevel(ControllerAccounting.add(
						(Double) fuelUsageLogAux.getFinalLevel(),
						(Double) fuelPurchase.getQuantity()));
			} else {
				this.fuelUsageLog.setFinalLevel((Double) fuelPurchase
						.getQuantity());
			}

			fuelUsageLogDao.saveFuelUsage(this.fuelUsageLog);

			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_guardar"),
					fuelPurchase.getInvoiceNumber()));
			userTransaction.commit();
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.printErrorLog(e1);
			}
			ControladorContexto.mensajeError(e);
		}
		return searchInitialization();
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
	 */
	private void saveFiles() throws Exception {
		String location = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ folderFileTemporal + "/" + nameDocument;
		File fileTemp = new File(location);
		if (fileTemp.exists()) {
			FileUploadBean.fileCopyLocationReal(fileTemp, locationServer);
			FileUploadBean.fileCopyLocationReal(fileTemp, locationLocal);
		}
	}

}
