package co.informatix.erp.warehouse.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is the entity that is responsible for mapping the transaction type
 * table.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "transaction_type", schema = "warehouse")
public class TransactionType implements Serializable {

	private int idTransactionType;
	private String transactionType;

	/**
	 * @return idTransactionType: transaction identify
	 */
	@Id
	@Column(name = "idtransactiontype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdTransactionType() {
		return idTransactionType;
	}

	/**
	 * @param idTransactionType
	 *            : transaction identify
	 */
	public void setIdTransactionType(int idTransactionType) {
		this.idTransactionType = idTransactionType;
	}

	/**
	 * @return transactionType: type of transaction
	 */
	@Column(name = "transactiontype", nullable = false)
	public String getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            : type of transaction
	 */
	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idTransactionType;
		result = prime * result
				+ ((transactionType == null) ? 0 : transactionType.hashCode());
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
		TransactionType other = (TransactionType) obj;
		if (idTransactionType != other.idTransactionType)
			return false;
		if (transactionType == null) {
			if (other.transactionType != null)
				return false;
		} else if (!transactionType.equals(other.transactionType))
			return false;
		return true;
	}
}