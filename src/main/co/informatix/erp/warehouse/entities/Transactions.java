package co.informatix.erp.warehouse.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.humanResources.entities.Hr;

/**
 * Class that contains the records of transactions.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "transactions", schema = "warehouse")
public class Transactions implements Serializable {

	private int idTransaction;
	private Date dateTime;
	private double quantity;
	private String justification;

	private TransactionType transactionType;
	private Activities activities;
	private Hr hr;
	private Deposits deposits;

	/**
	 * @return idTransaction: Identifier for transactions.
	 */
	@Id
	@Column(name = "idtransaction", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdTransaction() {
		return idTransaction;
	}

	/**
	 * @param idTransaction
	 *            : Identifier for transactions.
	 */
	public void setIdTransaction(int idTransaction) {
		this.idTransaction = idTransaction;
	}

	/**
	 * @return dateTime: dateTime for transactions
	 */
	@Column(name = "date_time", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            : dateTime for transactions
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return quantity: : quantity for transactions
	 */
	@Column(name = "quantity", nullable = false)
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            : quantity for transactions
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return justification: justification for transactions.
	 */
	@Column(name = "justification", nullable = false)
	public String getJustification() {
		return justification;
	}

	/**
	 * @param justification
	 *            : justification for transactions.
	 */
	public void setJustification(String justification) {
		this.justification = justification;
	}

	/**
	 * @return transactionType: gets the foreign key of machines type.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_transaction_type", referencedColumnName = "idtransactiontype", nullable = false)
	public TransactionType getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            : gets the foreign key of machines type.
	 */
	public void setTransactionType(TransactionType transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return activities: activities to which belong the transactions.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_activity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : activities to which belong the transactions.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return hr: human resource to which belong the transactions.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hr", referencedColumnName = "idhr", nullable = false)
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : human resource to which belong the transactions.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return deposits : deposits to which belong the transactions.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_deposit", referencedColumnName = "iddeposit", nullable = false)
	public Deposits getDeposits() {
		return deposits;
	}

	/**
	 * @param deposits
	 *            : deposits to which belong the transactions.
	 */
	public void setDeposits(Deposits deposits) {
		this.deposits = deposits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + idTransaction;
		result = prime * result
				+ ((justification == null) ? 0 : justification.hashCode());
		long temp;
		temp = Double.doubleToLongBits(quantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		Transactions other = (Transactions) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (idTransaction != other.idTransaction)
			return false;
		if (justification == null) {
			if (other.justification != null)
				return false;
		} else if (!justification.equals(other.justification))
			return false;
		if (Double.doubleToLongBits(quantity) != Double
				.doubleToLongBits(other.quantity))
			return false;
		return true;
	}
}
