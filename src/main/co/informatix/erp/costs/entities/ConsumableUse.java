package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import co.informatix.erp.warehouse.entities.Transactions;

/**
 * This is the entity that is responsible for mapping the consumable_use table.
 * 
 * @author Andres.Gomez
 * @modify 30/09/2015 Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "consumable_use", schema = "costs")
public class ConsumableUse implements Serializable {

	private ConsumableUsePK consumableUsePK;

	private Double initialLevel;
	private Double finalLevel;
	private Double consumptionBudget;
	private Double consumptionActual;
	private Double unitCostBudget;
	private Double unitCostActual;
	private Double totalCostBudget;
	private Double totalCostActual;

	private Transactions transactions;

	/**
	 * @return consumableUsePK: the composite order table key Use consumable.
	 */
	@EmbeddedId
	public ConsumableUsePK getConsumableUsePK() {
		return consumableUsePK;
	}

	/**
	 * @param consumableUsePK
	 *            : the composite order table key Use consumable.
	 */
	public void setConsumableUsePK(ConsumableUsePK consumableUsePK) {
		this.consumableUsePK = consumableUsePK;
	}

	/**
	 * @return initialLevel: Initial level.
	 */
	@Column(name = "initial_level")
	public Double getInitialLevel() {
		return initialLevel;
	}

	/**
	 * @param initialLevel
	 *            : Initial level.
	 */
	public void setInitialLevel(Double initialLevel) {
		this.initialLevel = initialLevel;
	}

	/**
	 * @return finalLevel: Final level.
	 */
	@Column(name = "final_level")
	public Double getFinalLevel() {
		return finalLevel;
	}

	/**
	 * @param finalLevel
	 *            : Final level.
	 */
	public void setFinalLevel(Double finalLevel) {
		this.finalLevel = finalLevel;
	}

	/**
	 * @return consumptionBudget: Budgeted consumption.
	 */
	@Column(name = "consumption_budget")
	public Double getConsumptionBudget() {
		return consumptionBudget;
	}

	/**
	 * @param consumptionBudget
	 *            : Budgeted consumption.
	 */
	public void setConsumptionBudget(Double consumptionBudget) {
		this.consumptionBudget = consumptionBudget;
	}

	/**
	 * @return consumptionActual: Current consumption.
	 */
	@Column(name = "consumption_actual")
	public Double getConsumptionActual() {
		return consumptionActual;
	}

	/**
	 * @param consumptionActual
	 *            : Current consumption.
	 */
	public void setConsumptionActual(Double consumptionActual) {
		this.consumptionActual = consumptionActual;
	}

	/**
	 * @return unitCostBudget: Budget unit cost.
	 */
	@Column(name = "unit_cost_budget")
	public Double getUnitCostBudget() {
		return unitCostBudget;
	}

	/**
	 * @param unitCostBudget
	 *            : Budget unit cost.
	 */
	public void setUnitCostBudget(Double unitCostBudget) {
		this.unitCostBudget = unitCostBudget;
	}

	/**
	 * @return unitCostActual: Cost of current drive.
	 */
	@Column(name = "unit_cost_actual")
	public Double getUnitCostActual() {
		return unitCostActual;
	}

	/**
	 * @param unitCostActual
	 *            : Cost of current drive.
	 */
	public void setUnitCostActual(Double unitCostActual) {
		this.unitCostActual = unitCostActual;
	}

	/**
	 * @return totalCostBudget: Total budgeted cost.
	 */
	@Column(name = "total_cost_budget")
	public Double getTotalCostBudget() {
		return totalCostBudget;
	}

	/**
	 * @param totalCostBudget
	 *            : Total budgeted cost.
	 */
	public void setTotalCostBudget(Double totalCostBudget) {
		this.totalCostBudget = totalCostBudget;
	}

	/**
	 * @return totalCostActual: Current total cost.
	 */
	@Column(name = "total_cost_actual")
	public Double getTotalCostActual() {
		return totalCostActual;
	}

	/**
	 * @param totalCostActual
	 *            : Current total cost.
	 */
	public void setTotalCostActual(Double totalCostActual) {
		this.totalCostActual = totalCostActual;
	}

	/**
	 * @return transactions: relationship transactions
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_transaction", referencedColumnName = "idTransaction", nullable = false)
	public Transactions getTransactions() {
		return transactions;
	}

	/**
	 * @param transactions
	 *            : relationship Withdrawals
	 */
	public void setTransactions(Transactions transactions) {
		this.transactions = transactions;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((consumableUsePK == null) ? 0 : consumableUsePK.hashCode());
		result = prime
				* result
				+ ((consumptionActual == null) ? 0 : consumptionActual
						.hashCode());
		result = prime
				* result
				+ ((consumptionBudget == null) ? 0 : consumptionBudget
						.hashCode());
		result = prime * result
				+ ((finalLevel == null) ? 0 : finalLevel.hashCode());
		result = prime * result
				+ ((initialLevel == null) ? 0 : initialLevel.hashCode());
		result = prime * result
				+ ((totalCostActual == null) ? 0 : totalCostActual.hashCode());
		result = prime * result
				+ ((totalCostBudget == null) ? 0 : totalCostBudget.hashCode());
		result = prime * result
				+ ((unitCostActual == null) ? 0 : unitCostActual.hashCode());
		result = prime * result
				+ ((unitCostBudget == null) ? 0 : unitCostBudget.hashCode());
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
		ConsumableUse other = (ConsumableUse) obj;
		if (consumableUsePK == null) {
			if (other.consumableUsePK != null)
				return false;
		} else if (!consumableUsePK.equals(other.consumableUsePK))
			return false;
		if (consumptionActual == null) {
			if (other.consumptionActual != null)
				return false;
		} else if (!consumptionActual.equals(other.consumptionActual))
			return false;
		if (consumptionBudget == null) {
			if (other.consumptionBudget != null)
				return false;
		} else if (!consumptionBudget.equals(other.consumptionBudget))
			return false;
		if (finalLevel == null) {
			if (other.finalLevel != null)
				return false;
		} else if (!finalLevel.equals(other.finalLevel))
			return false;
		if (initialLevel == null) {
			if (other.initialLevel != null)
				return false;
		} else if (!initialLevel.equals(other.initialLevel))
			return false;
		if (totalCostActual == null) {
			if (other.totalCostActual != null)
				return false;
		} else if (!totalCostActual.equals(other.totalCostActual))
			return false;
		if (totalCostBudget == null) {
			if (other.totalCostBudget != null)
				return false;
		} else if (!totalCostBudget.equals(other.totalCostBudget))
			return false;
		if (unitCostActual == null) {
			if (other.unitCostActual != null)
				return false;
		} else if (!unitCostActual.equals(other.unitCostActual))
			return false;
		if (unitCostBudget == null) {
			if (other.unitCostBudget != null)
				return false;
		} else if (!unitCostBudget.equals(other.unitCostBudget))
			return false;
		return true;
	}
}
