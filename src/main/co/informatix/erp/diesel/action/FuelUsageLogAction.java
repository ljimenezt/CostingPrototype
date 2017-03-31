package co.informatix.erp.diesel.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.diesel.dao.FuelUsageLogDao;
import co.informatix.erp.diesel.entities.FuelUsageLog;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControllerAccounting;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.warehouse.dao.TransactionTypeDao;
import co.informatix.erp.warehouse.entities.TransactionType;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * of the Fuel Usage.
 * 
 * @author Luna.Granados
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class FuelUsageLogAction implements Serializable {

	private List<SelectItem> itemsTransactionTypes;
	private List<FuelUsageLog> fuelUsageLogList;

	private FuelUsageLog fuelUsageLog;
	private TransactionType transactionType;

	private Date startDateSearch;
	private Date endDateSearch;

	private Paginador pagination = new Paginador();

	private Double motion;

	@EJB
	private FuelUsageLogDao fuelUsageLogDao;
	@EJB
	private TransactionTypeDao transactionTypeDao;

	/**
	 * @return itemsTransactionTypes: obtain the items for transaction types.
	 */
	public List<SelectItem> getItemsTransactionTypes() {
		return itemsTransactionTypes;
	}

	/**
	 * @param itemsTransactionTypes
	 *            : set the items for transaction types.
	 */
	public void setItemsTransactionTypes(List<SelectItem> itemsTransactionTypes) {
		this.itemsTransactionTypes = itemsTransactionTypes;
	}

	/**
	 * @return fuelUsageLog: object that contains the data of fuel usage log.
	 */
	public FuelUsageLog getFuelUsageLog() {
		return fuelUsageLog;
	}

	/**
	 * @param fuelUsageLog
	 *            : object that contains the data of fuel usage log.
	 */
	public void setFuelUsageLog(FuelUsageLog fuelUsageLog) {
		this.fuelUsageLog = fuelUsageLog;
	}

	/**
	 * @return transactionType: object that contains the data of transaction
	 *         type.
	 */
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            : object that contains the data of transaction type.
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return fuelUsageLogList: list of fuel usage stored in data base.
	 */
	public List<FuelUsageLog> getFuelUsageLogList() {
		return fuelUsageLogList;
	}

	/**
	 * @param fuelUsageLogList
	 *            : list of fuel usage stored in data base.
	 */
	public void setFuelUsageLogList(List<FuelUsageLog> fuelUsageLogList) {
		this.fuelUsageLogList = fuelUsageLogList;
	}

	/**
	 * @return pagination: The paging controller object.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : The paging controller object.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return motion: Indicates if diesel input or output was performed.
	 */
	public Double getMotion() {
		return motion;
	}

	/**
	 * @param motion
	 *            : Indicates if diesel input or output was performed.
	 */
	public void setMotion(Double motion) {
		this.motion = motion;
	}

	/**
	 * @return startDateSearch: gets the initial search range for the fuel usage
	 *         in the system.
	 */
	public Date getStartDateSearch() {
		return startDateSearch;
	}

	/**
	 * @param startDateSearch
	 *            : sets the initial search range for the fuel usage in the
	 *            system.
	 */
	public void setStartDateSearch(Date startDateSearch) {
		this.startDateSearch = startDateSearch;
	}

	/**
	 * @return endDateSearch: gets the end range to search the fuel usage in the
	 *         system.
	 */
	public Date getEndDateSearch() {
		return endDateSearch;
	}

	/**
	 * @param endDateSearch
	 *            :sets the end range to search the fuel usage in the system.
	 */
	public void setEndDateSearch(Date endDateSearch) {
		this.endDateSearch = endDateSearch;
	}

	/**
	 * Method to create a new fuel usage.
	 * 
	 * @return "regFuelUsageLog": redirected to the template record fuel usage.
	 */
	public String registerFuelUsage() {
		try {
			this.motion = 0d;
			this.fuelUsageLog = new FuelUsageLog();
			this.transactionType = new TransactionType();
			this.fuelUsageLog.setTransactionType(this.transactionType);
			loadComboTransactionTypes();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regFuelUsageLog";
	}

	/**
	 * Method to load the transaction types related to gauge.
	 * 
	 * @throws Exception
	 */
	private void loadComboTransactionTypes() throws Exception {
		List<TransactionType> transactionTypesList = transactionTypeDao
				.consultTransactionTypeGauges();
		this.itemsTransactionTypes = new ArrayList<SelectItem>();
		for (TransactionType transactionTypes : transactionTypesList) {
			this.itemsTransactionTypes.add(new SelectItem(transactionTypes
					.getIdTransactionType(), transactionTypes
					.getTransactionType()));
		}
	}

	/**
	 * Method used to save or edit Fuel Usage.
	 * 
	 * @return consultFuelUsage(): method that redirects to manage the list of
	 *         Fuel Usage.
	 */
	public String saveFuelUsage() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		Double finalLevel = 0d;
		try {
			this.fuelUsageLog.setEngineLog(null);
			this.fuelUsageLog.setFuelPurchase(null);
			this.fuelUsageLog.setDate(new Date());

			List<FuelUsageLog> fuelUsageList = fuelUsageLogDao
					.consultFuelUsage();

			if (fuelUsageList != null && fuelUsageList.size() > 0) {
				FuelUsageLog LastfuelUsage = fuelUsageLogDao
						.consultLastFuelUsage();

				if (this.fuelUsageLog.getTransactionType()
						.getIdTransactionType() == Constantes.TRANSACTION_TYPE_ADJUSTMENT_DOWN) {
					finalLevel = ControllerAccounting.subtract(
							LastfuelUsage.getFinalLevel(), this.motion);
					this.fuelUsageLog.setConsumption(this.motion);

				} else if (this.fuelUsageLog.getTransactionType()
						.getIdTransactionType() == Constantes.TRANSACTION_TYPE_ADJUSTMENT_UP) {
					finalLevel = ControllerAccounting.add(
							LastfuelUsage.getFinalLevel(), this.motion);
					this.fuelUsageLog.setDeposited(this.motion);
				}
			} else {
				if (this.fuelUsageLog.getTransactionType()
						.getIdTransactionType() == Constantes.TRANSACTION_TYPE_ADJUSTMENT_DOWN) {
					finalLevel = -1d;
				} else if (this.fuelUsageLog.getTransactionType()
						.getIdTransactionType() == Constantes.TRANSACTION_TYPE_ADJUSTMENT_UP) {
					this.fuelUsageLog.setDeposited(this.motion);
				}
			}

			if (finalLevel >= 0) {
				this.fuelUsageLog.setFinalLevel(finalLevel);
				fuelUsageLogDao.saveFuelUsage(this.fuelUsageLog);

				SimpleDateFormat dateFormat = new SimpleDateFormat(
						Constantes.DATE_FORMAT_TABLE);
				Object transactionType = (String) ValidacionesAction.getLabel(
						itemsTransactionTypes, this.fuelUsageLog
								.getTransactionType().getIdTransactionType());

				String messageSave = dateFormat.format(new Date()) + " - "
						+ transactionType;

				ControladorContexto.mensajeInformacion(MessageFormat.format(
						bundle.getString("message_registro_guardar"),
						messageSave));
			} else {
				ControladorContexto.mensajeErrorEspecifico(
						"fuel_usage_log_message_final_level", "messageDiesel");
				return "";
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultFuelUsageLog();
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @return consultFuelUsage: consultFuelUsage consulting method and
	 *         redirects to the template to manage fuel usage.
	 */
	public String searchInitialization() {
		this.startDateSearch = null;
		this.endDateSearch = null;
		this.fuelUsageLog = new FuelUsageLog();
		return consultFuelUsageLog();
	}

	/**
	 * Consult the list of fuel usage
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @return gesFuelUsageLog: Navigation rule that redirects to manage fuel
	 *         usage
	 */
	public String consultFuelUsageLog() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleFuelUsageLog = ControladorContexto
				.getBundle("messageDiesel");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		fuelUsageLogList = new ArrayList<FuelUsageLog>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = fuelUsageLogDao.quantityFuelUsageLog(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			if (quantity != null && quantity > 0) {
				fuelUsageLogList = fuelUsageLogDao.consultFuelUsageLog(
						pagination.getInicio(), pagination.getRango(), query,
						parameters);

			}
			if ((fuelUsageLogList == null || fuelUsageLogList.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (fuelUsageLogList == null || fuelUsageLogList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleFuelUsageLog
										.getString("fuel_usage_log_label_title"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesFuelUsageLog";
	}

	/**
	 * This method builds a query with advanced search; it also render the
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) throws Exception {
		SimpleDateFormat formats = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		boolean addFilter = false;
		if (this.startDateSearch != null && this.endDateSearch != null) {
			consult.append(addFilter ? "AND " : "WHERE ");
			addFilter = true;
			consult.append("ful.date BETWEEN :startDateSearch AND :endDateSearch ");
			SelectItem item = new SelectItem(startDateSearch, "startDateSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(endDateSearch, "endDateSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_start_date") + ": " + '"'
					+ formats.format(this.startDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateFrom);
			String dateTo = bundle.getString("label_end_date") + ": " + '"'
					+ formats.format(endDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateTo);
		}
	}
}
