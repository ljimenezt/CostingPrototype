package co.informatix.erp.diesel.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.informatix.erp.warehouse.entities.TransactionType;

/**
 * This is the entity in charge of mapping the table fuel_usage_log table.
 * 
 * @author Luna.Granados
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "fuel_usage_log", schema = "diesel")
public class FuelUsageLog implements Serializable {

	private int idFuelUsage;
	private Double consumption;
	private Double finalLevel;
	private Double deposited;
	private String note;

	private EngineLog engineLog;
	private FuelPurchase fuelPurchase;
	private TransactionType transactionType;

	/**
	 * Constructor method.
	 */
	public FuelUsageLog() {
		this.engineLog = new EngineLog();
		this.fuelPurchase = new FuelPurchase();
		this.transactionType = new TransactionType();
	}

	/**
	 * @return idFuelUsage: FuelUsageLog table identifier.
	 */
	@Id
	@Column(name = "id_fuel_usage", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdFuelUsage() {
		return idFuelUsage;
	}

	/**
	 * @param idFuelUsage
	 *            : FuelUsageLog table identifier.
	 */
	public void setIdFuelUsage(int idFuelUsage) {
		this.idFuelUsage = idFuelUsage;
	}

	/**
	 * @return consumption
	 */
	@Column(name = "consumption")
	public Double getConsumption() {
		return consumption;
	}

	/**
	 * @param consumption
	 */
	public void setConsumption(Double consumption) {
		this.consumption = consumption;
	}

	/**
	 * @return finalLevel
	 */
	@Column(name = "final_level")
	public Double getFinalLevel() {
		return finalLevel;
	}

	/**
	 * @param finalLevel
	 */
	public void setFinalLevel(Double finalLevel) {
		this.finalLevel = finalLevel;
	}

	/**
	 * @return deposited
	 */
	@Column(name = "deposited")
	public Double getDeposited() {
		return deposited;
	}

	/**
	 * @param deposited
	 */
	public void setDeposited(Double deposited) {
		this.deposited = deposited;
	}

	/**
	 * @return note
	 */
	@Column(name = "note")
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return engineLog
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_engine_log", referencedColumnName = "id_engine_log")
	public EngineLog getEngineLog() {
		return engineLog;
	}

	/**
	 * @param engineLog
	 */
	public void setEngineLog(EngineLog engineLog) {
		this.engineLog = engineLog;
	}

	/**
	 * @return fuelPurchase
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fuel_purchase", referencedColumnName = "id_fuel_purchase")
	public FuelPurchase getFuelPurchase() {
		return fuelPurchase;
	}

	/**
	 * @param fuelPurchase
	 */
	public void setFuelPurchase(FuelPurchase fuelPurchase) {
		this.fuelPurchase = fuelPurchase;
	}

	/**
	 * @return transactionType
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_transaction_type", referencedColumnName = "idtransactiontype")
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((consumption == null) ? 0 : consumption.hashCode());
		result = prime * result
				+ ((deposited == null) ? 0 : deposited.hashCode());
		result = prime * result
				+ ((finalLevel == null) ? 0 : finalLevel.hashCode());
		result = prime * result + idFuelUsage;
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		FuelUsageLog other = (FuelUsageLog) obj;
		if (consumption == null) {
			if (other.consumption != null)
				return false;
		} else if (!consumption.equals(other.consumption))
			return false;
		if (deposited == null) {
			if (other.deposited != null)
				return false;
		} else if (!deposited.equals(other.deposited))
			return false;
		if (finalLevel == null) {
			if (other.finalLevel != null)
				return false;
		} else if (!finalLevel.equals(other.finalLevel))
			return false;
		if (idFuelUsage != other.idFuelUsage)
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		return true;
	}

}
