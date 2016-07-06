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
import javax.persistence.Transient;

/**
 * This is the entity that is responsible for mapping the purchase_invoices
 * table.
 * 
 * @author Johnatan.Naranjo
 * @modify 04/02/2016 Liseth.Jimenez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "purchase_invoices", schema = "warehouse")
public class PurchaseInvoices implements Serializable {

	private int idPurchaseInvoice;
	private Date dateTime;
	private Suppliers suppliers;
	private double totalValueActual;
	private String invoiceNumber;
	private double subtotal;
	private double shipping;
	private double packaging;
	private double taxes;
	private double discount;
	private String note;
	private String invoiceDocumentLink;
	private boolean selected;
	private boolean reconcile;

	/**
	 * @return idpurchaseinvoice: Purchase invoices identifier
	 */
	@Id
	@Column(name = "idpurchaseinvoice", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdPurchaseInvoice() {
		return idPurchaseInvoice;
	}

	/**
	 * @param idpurchaseinvoice
	 *            : Purchase invoices identifier
	 */
	public void setIdPurchaseInvoice(int idpurchaseinvoice) {
		this.idPurchaseInvoice = idpurchaseinvoice;
	}

	/**
	 * @return dateTime: Date of Purchase invoices
	 */
	@Column(name = "date_time", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            : Date of Purchase invoices
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return suppliers: Supplier associated with the purchase
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_supplier", referencedColumnName = "idsupplier", nullable = false)
	public Suppliers getSuppliers() {
		return suppliers;
	}

	/**
	 * @param suppliers
	 *            : Supplier associated with the purchase
	 */
	public void setSuppliers(Suppliers suppliers) {
		this.suppliers = suppliers;
	}

	/**
	 * @return totalValueActual: Total Value Actual
	 */
	@Column(name = "total_value_actual", nullable = false)
	public double getTotalValueActual() {
		return totalValueActual;
	}

	/**
	 * @param totalValueActual
	 *            :Total Value Actual
	 */
	public void setTotalValueActual(double totalValueActual) {
		this.totalValueActual = totalValueActual;
	}

	/**
	 * @return invoiceNumber: Number of the invoice
	 */
	@Column(name = "invoice_number", length = 50, nullable = false)
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	/**
	 * @param invoiceNumber
	 *            : Number of the invoice
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * @return subtotal: Gets subtotal of the purshace invoices
	 */
	@Column(name = "subtotal", nullable = false)
	public double getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal
	 *            :Sets subtotal of the purshace invoices
	 */
	public void setSubtotal(double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @return shipping :Gets shipping of the purchase invoices
	 */
	@Column(name = "shipping", nullable = false)
	public double getShipping() {
		return shipping;
	}

	/**
	 * @param shipping
	 *            :Sets shipping of the purchase invoices
	 */
	public void setShipping(double shipping) {
		this.shipping = shipping;
	}

	/**
	 * @return packaging :Gets packaging of he purchase invoices
	 */
	@Column(name = "packaging", nullable = false)
	public double getPackaging() {
		return packaging;
	}

	/**
	 * @param packaging
	 *            :Sets packaging of he purchase invoices
	 */
	public void setPackaging(double packaging) {
		this.packaging = packaging;
	}

	/**
	 * @return taxes: Gets taxes of the purchase invoices
	 */
	@Column(name = "taxes", nullable = false)
	public double getTaxes() {
		return taxes;
	}

	/**
	 * @param taxes
	 *            :Sets taxes of the purchase invoices
	 */
	public void setTaxes(double taxes) {
		this.taxes = taxes;
	}

	/**
	 * @return discount: Gets discount of the purchase invoices
	 */
	@Column(name = "discount", nullable = false)
	public double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            :Sets discount of the purchase invoices
	 */
	public void setDiscount(double discount) {
		this.discount = discount;
	}

	/**
	 * @return note: Gets note of the purchase invoices
	 */
	@Column(name = "note")
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            :Sets note of the purchase invoices
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return invoiceDocumentLink: Gets invoice document link of the purchase
	 *         invoices
	 */
	@Column(name = "invoice_document_link", length = 100)
	public String getInvoiceDocumentLink() {
		return invoiceDocumentLink;
	}

	/**
	 * @param invoiceDocumentLink
	 *            : Sets invoice document link of the purchase invoices
	 */
	public void setInvoiceDocumentLink(String invoiceDocumentLink) {
		this.invoiceDocumentLink = invoiceDocumentLink;
	}

	/**
	 * @return selected: Flag to see if it is selected purchase invoices, true
	 *         is selected and false is not selected
	 */
	@Transient
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            : Flag to see if it is selected purchase invoices, true is
	 *            selected and false is not selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return reconcile: Flag to see if the invoice was reconciled.
	 */
	@Column(name = "reconcile")
	public boolean isReconcile() {
		return reconcile;
	}

	/**
	 * @param reconcile
	 *            : Flag to see if the invoice was reconciled.
	 */
	public void setReconcile(boolean reconcile) {
		this.reconcile = reconcile;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		long temp;
		temp = Double.doubleToLongBits(discount);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + idPurchaseInvoice;
		result = prime
				* result
				+ ((invoiceDocumentLink == null) ? 0 : invoiceDocumentLink
						.hashCode());
		result = prime * result
				+ ((invoiceNumber == null) ? 0 : invoiceNumber.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		temp = Double.doubleToLongBits(packaging);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (reconcile ? 1231 : 1237);
		result = prime * result + (selected ? 1231 : 1237);
		temp = Double.doubleToLongBits(shipping);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(subtotal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(taxes);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(totalValueActual);
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
		PurchaseInvoices other = (PurchaseInvoices) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (Double.doubleToLongBits(discount) != Double
				.doubleToLongBits(other.discount))
			return false;
		if (idPurchaseInvoice != other.idPurchaseInvoice)
			return false;
		if (invoiceDocumentLink == null) {
			if (other.invoiceDocumentLink != null)
				return false;
		} else if (!invoiceDocumentLink.equals(other.invoiceDocumentLink))
			return false;
		if (invoiceNumber == null) {
			if (other.invoiceNumber != null)
				return false;
		} else if (!invoiceNumber.equals(other.invoiceNumber))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (Double.doubleToLongBits(packaging) != Double
				.doubleToLongBits(other.packaging))
			return false;
		if (reconcile != other.reconcile)
			return false;
		if (selected != other.selected)
			return false;
		if (Double.doubleToLongBits(shipping) != Double
				.doubleToLongBits(other.shipping))
			return false;
		if (Double.doubleToLongBits(subtotal) != Double
				.doubleToLongBits(other.subtotal))
			return false;
		if (Double.doubleToLongBits(taxes) != Double
				.doubleToLongBits(other.taxes))
			return false;
		if (Double.doubleToLongBits(totalValueActual) != Double
				.doubleToLongBits(other.totalValueActual))
			return false;
		return true;
	}
}
