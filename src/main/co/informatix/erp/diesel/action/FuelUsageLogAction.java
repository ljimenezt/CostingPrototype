package co.informatix.erp.diesel.action;

import java.io.Serializable;
import java.text.MessageFormat;
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

	private FuelUsageLog fuelUsageLog;
	private TransactionType transactionType;

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
	 * Method to create a new fuel usage.
	 * 
	 * @param fuelUsage
	 *            : fuel usage to adding.
	 * @return "regFuelUsageLog": redirected to the template record fuel usage.
	 */
	public String registerFuelUsage(FuelUsageLog fuelUsageLog) {
		try {
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

			if (fuelUsageList.size() > 0) {
				FuelUsageLog LastfuelUsage = fuelUsageLogDao
						.consultLastFuelUsage();

				Object transactionType = (String) ValidacionesAction.getLabel(
						itemsTransactionTypes, this.fuelUsageLog
								.getTransactionType().getIdTransactionType());

				if (transactionType.equals(Constantes.GAUGE_ADJUSTMENT_DOWN)) {
					finalLevel = LastfuelUsage.getFinalLevel()
							- this.fuelUsageLog.getDeposited();
				} else if (transactionType
						.equals(Constantes.GAUGE_ADJUSTMENT_UP)) {
					finalLevel = LastfuelUsage.getFinalLevel()
							+ this.fuelUsageLog.getDeposited();
				}
			} else {
				finalLevel = this.fuelUsageLog.getDeposited();
			}

			if (finalLevel >= 0) {
				this.fuelUsageLog.setFinalLevel(finalLevel);
				fuelUsageLogDao.saveFuelUsage(this.fuelUsageLog);

				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_guardar"),
								this.fuelUsageLog.getIdFuelUsage()));
			} else {
				ControladorContexto.mensajeErrorEspecifico(
						"fuel_usage_log_final_level_message", "messageDiesel");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultFuelUsage();
	}

	private String consultFuelUsage() {
		return "";
	}
}
