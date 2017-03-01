package co.informatix.erp.diesel.action;

import java.io.File;
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
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import org.primefaces.event.FileUploadEvent;

import co.informatix.erp.diesel.dao.ConsumableResourcesDao;
import co.informatix.erp.diesel.dao.FuelPurchaseDao;
import co.informatix.erp.diesel.dao.FuelUsageLogDao;
import co.informatix.erp.diesel.entities.ConsumableResources;
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

	private String imageDocument;
	private String filesFolder;
	private String temporalFilesFolder;

	private boolean temporalImageDocument;

	private List<SelectItem> itemsSuppliers;
	private List<SelectItem> itemsFuelTypes;
	private List<SelectItem> itemsIvaRate;

	private FuelPurchase fuelPurchase;
	private Suppliers suppliers;
	private FuelTypes fuelTypes;
	private IvaRate ivaRate;
	private TransactionType transactionType;
	private FileUploadBean fileUploadBean;
	private FuelUsageLog fuelUsageLog;
	private ConsumableResources consumableResources;

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
	 * @return imageDocument: name of the image Document.
	 */
	public String getImageDocument() {
		return imageDocument;
	}

	/**
	 * @param imageDocument
	 *            : name of the image Document.
	 */
	public void setImageDocument(String imageDocument) {
		this.imageDocument = imageDocument;
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
	 * @return temporalFilesFolder:Path to the temporary folder where the fuel
	 *         documents are loaded.
	 */
	public String getTemporalFilesFolder() {
		this.temporalFilesFolder = Constantes.FOLDER_FILES
				+ Constantes.CARPETA_ARCHIVOS_TEMP;
		return temporalFilesFolder;
	}

	/**
	 * @return temporalImageDocument: Flag indicating whether the picture is
	 *         loaded from the temporary location or not.
	 */
	public boolean isTemporalImageDocument() {
		return temporalImageDocument;
	}

	/**
	 * @param temporalImageDocument
	 *            :Flag indicating whether the picture is loaded from the
	 *            temporary location or not.
	 */
	public void setTemporalImageDocument(boolean temporalImageDocument) {
		this.temporalImageDocument = temporalImageDocument;
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
	 * @return fuelPurchase: fuelPurchase stored in data base.
	 */
	public FuelPurchase getFuelPurchase() {
		return fuelPurchase;
	}

	/**
	 * @param fuelPurchase
	 *            : fuelPurchase stored in data base.
	 */
	public void setFuelPurchase(FuelPurchase fuelPurchase) {
		this.fuelPurchase = fuelPurchase;
	}

	/**
	 * Method that charge the cost unit of the diesel.
	 * 
	 * @throws Exception
	 */
	private void chargueCostUnit() throws Exception {
		this.consumableResources = consumableResourcesDao
				.consumableResourceById(Constantes.FUEL_PURCHASE_ID_COSTO);
		fuelPurchase.setUnitCost(consumableResources.getUnitCost());

	}

	/**
	 * This method is used to initialize the consultation of the fuel purchase
	 * registered in the information system.
	 * 
	 * @return gesFuelPurchase: Navigation rule that directs the fuelPurchase
	 *         form.
	 */
	public String initialize() {
		try {
			chargueCostUnit();
			loadComboSuppliers();
			loadComboFuelTypes();
			loadComboIvaRate();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesFuelPurchase";
	}

	/**
	 * Method to edit or create a new fuel purchase.
	 * 
	 * @return regFuelPurchase: redirected to the template record fuel purchase.
	 */
	public String addFuelPurchase() {
		try {
			this.fuelPurchase = new FuelPurchase();
			this.imageDocument = null;
			this.fileUploadBean = new FileUploadBean();
			this.temporalImageDocument = true;
			transactionType = new TransactionType();
			chargueCostUnit();
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
	 * Method that calculate the subtotal of a fuel purchase
	 */
	public void calculateSubtotal() {
		this.fuelPurchase.setSubTotal(ControllerAccounting.multiply(
				this.fuelPurchase.getQuantity(),
				this.fuelPurchase.getUnitCost()));
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
	 * Method that call to methods calculateTotal and calculateTaxes or
	 * CalculateSubtotal for do the twos things at the same time.
	 */
	public void calculateValors() {
		if (ivaRate.getIdIva() > 0) {
			calculateTaxes();
			calculateTotal();
		} else {
			calculateSubtotal();
			calculateTotal();
		}

	}

	/**
	 * Delete the file name.
	 */
	public void deleteFileName() {
		if (imageDocument != null && !"".equals(imageDocument)
				&& !imageDocument.equals(fuelPurchase.getInvoiceDocumentLink())
				&& this.temporalImageDocument) {
			deleteFile(imageDocument);
		}
		imageDocument = null;
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
	 * Method allows you to load the file system.
	 * 
	 * @param e
	 *            : Fileupload event for the file to be uploaded to the server.
	 */
	public void submit(FileUploadEvent e) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String allowedExt[] = Constantes.EXT_IMG.split(", ");
		String paths[] = { Constantes.RUTA_UPLOADFILE_GLASFISH
				+ getTemporalFilesFolder() };
		fileUploadBean.setUploadedFile(e.getFile());
		long maxFileSize = Constantes.TAMANYO_MAX_ARCHIVOS;
		String resultUpload = fileUploadBean.uploadValTamanyo(allowedExt,
				paths, maxFileSize);
		String message = "";
		if (Constantes.UPLOAD_EXT_INVALIDA.equals(resultUpload)) {
			message = "error_ext_invalida";
		} else if (Constantes.UPLOAD_TAMANO_INVALIDA.equals(resultUpload)) {
			String format = MessageFormat.format(
					bundle.getString("error_tamanyo_invalido"), maxFileSize,
					"MB");
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					format);
		} else if (Constantes.UPLOAD_NULL.equals(resultUpload)) {
			message = "error_carga_archivo";
		}
		if (!"".equals(message)) {
			ControladorContexto.mensajeError("formRegistrarEmpresa:uploadLogo",
					bundle.getString(message));
		}
		if (fuelPurchase.getIdFuelPurchase() != 0) {
			temporalImageDocument = true;
		}
		imageDocument = fileUploadBean.getFileName();
	}

	/**
	 * Method that save a fuel purchase in data base and save the fuel use log
	 */
	public void saveFuelPurchase() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String deletePicName = null;
		try {
			userTransaction.begin();
			if (this.imageDocument != null
					&& !"".equals(this.imageDocument.trim())) {
				deletePicName = this.imageDocument;
				uploadPicRealFolder();
			}
			this.fuelPurchase.setInvoiceDocumentLink(this.imageDocument);

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
					.transactionTypeById(Constantes.FUEL_PURCHASE_TYPE_TRANSACTION);

			this.fuelUsageLog = new FuelUsageLog();
			this.fuelUsageLog.setDate(new Date());
			this.fuelUsageLog.setFuelPurchase(fuelPurchase);
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

			if (deletePicName != null && !"".equals(deletePicName)) {
				this.deleteFile(deletePicName);
			}
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

	}

	/**
	 * Upload the logo image to the actual folder.
	 * 
	 * @throws Exception
	 */
	private void uploadPicRealFolder() throws Exception {
		String source = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getTemporalFilesFolder();
		String firstDestFolder = Constantes.RUTA_UPLOADFILE_GLASFISH
				+ this.getFilesFolder();
		String secondDestFolder = Constantes.RUTA_UPLOADFILE_WORKSPACE
				+ this.getFilesFolder();

		/* It checks whether the destinations are created there but */
		FileUploadBean.fileExist(firstDestFolder);
		FileUploadBean.fileExist(secondDestFolder);

		File sourceFile = new File(source, fileUploadBean.getFileName());
		File firstFolderFile = new File(firstDestFolder,
				fileUploadBean.getFileName());
		File secondFolderFile = new File(secondDestFolder,
				fileUploadBean.getFileName());

		/* Copies of temporal at 2 real destinations */
		FileUploadBean.copyFile(sourceFile, firstFolderFile);
		FileUploadBean.copyFile(sourceFile, secondFolderFile);
	}

}
