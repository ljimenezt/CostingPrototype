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

/**
 * This is the entity that is responsible for mapping the purchase_invoices
 * table.
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "purchase_invoices", schema = "warehouse")
public class PurchaseInvoices implements Serializable {

	private int idPurchaseInvoice;
	private Date dateTime;
	private Suppliers suppliers;
	private Double totalValueActual;

	/**
	 * @return idpurchaseinvoice:purchase_invoices identifier
	 */
	@Id
	@Column(name = "idpurchaseinvoice", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdPurchaseInvoice() {
		return idPurchaseInvoice;
	}

	/**
	 * @param idpurchaseinvoice
	 *            :purchase_invoices identifier
	 */
	public void setIdPurchaseInvoice(int idpurchaseinvoice) {
		this.idPurchaseInvoice = idpurchaseinvoice;
	}

	/**
	 * @return dateTime: Date Time
	 */
	@Column(name = "date_time")
	@Temporal(TemporalType.DATE)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            :Date Time
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return suppliers: Suppliers
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_supplier", referencedColumnName = "idsupplier", nullable = false)
	public Suppliers getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            :Suppliers
	 */
	public void setSuppliers(Suppliers suppliers) {
		this.suppliers = suppliers;
	}

	/**
	 * @return totalValueActual: Total Value Actual
	 */
	@Column(name = "total_value_actual")
	public Double getTotalValueActual() {
		return totalValueActual;
	}

	/**
	 * @param totalValueActual
	 *            :Total Value Actual
	 */
	public void setTotalValueActual(Double totalValueActual) {
		this.totalValueActual = totalValueActual;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + idPurchaseInvoice;
		result = prime
				* result
				+ ((totalValueActual == null) ? 0 : totalValueActual.hashCode());
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
		PurchaseInvoices other = (PurchaseInvoices) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (idPurchaseInvoice != other.idPurchaseInvoice)
			return false;
		if (totalValueActual == null) {
			if (other.totalValueActual != null)
				return false;
		} else if (!totalValueActual.equals(other.totalValueActual))
			return false;
		return true;
	}
}