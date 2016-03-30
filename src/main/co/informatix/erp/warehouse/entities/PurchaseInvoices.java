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
	private Double totalValueActual;
	private String invoiceNumber;
	private Double subtotal;
	private Double shipping;
	private Double packaging;
	private Double taxes;
	private Double discount;
	private String note;
	private String invoiceDocumentLink;
	private boolean selected;

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
	public Double getSubtotal() {
		return subtotal;
	}

	/**
	 * @param subtotal
	 *            :Sets subtotal of the purshace invoices
	 */
	public void setSubtotal(Double subtotal) {
		this.subtotal = subtotal;
	}

	/**
	 * @return shipping :Gets shipping of the purchase invoices
	 */
	@Column(name = "shipping", nullable = false)
	public Double getShipping() {
		return shipping;
	}

	/**
	 * @param shipping
	 *            :Sets shipping of the purchase invoices
	 */
	public void setShipping(Double shipping) {
		this.shipping = shipping;
	}

	/**
	 * @return packaging :Gets packaging of he purchase invoices
	 */
	@Column(name = "packaging", nullable = false)
	public Double getPackaging() {
		return packaging;
	}

	/**
	 * @param packaging
	 *            :Sets packaging of he purchase invoices
	 */
	public void setPackaging(Double packaging) {
		this.packaging = packaging;
	}

	/**
	 * @return taxes: Gets taxes of the purchase invoices
	 */
	@Column(name = "taxes", nullable = false)
	public Double getTaxes() {
		return taxes;
	}

	/**
	 * @param taxes
	 *            :Sets taxes of the purchase invoices
	 */
	public void setTaxes(Double taxes) {
		this.taxes = taxes;
	}

	/**
	 * @return discount: Gets discount of the purchase invoices
	 */
	@Column(name = "discount", nullable = false)
	public Double getDiscount() {
		return discount;
	}

	/**
	 * @param discount
	 *            :Sets discount of the purchase invoices
	 */
	public void setDiscount(Double discount) {
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
	 * @return selected: Flag to see if it is selected deposit, true is selected
	 *         and false is not selected
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result
				+ ((discount == null) ? 0 : discount.hashCode());
		result = prime * result + idPurchaseInvoice;
		result = prime
				* result
				+ ((invoiceDocumentLink == null) ? 0 : invoiceDocumentLink
						.hashCode());
		result = prime * result
				+ ((invoiceNumber == null) ? 0 : invoiceNumber.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result
				+ ((packaging == null) ? 0 : packaging.hashCode());
		result = prime * result
				+ ((shipping == null) ? 0 : shipping.hashCode());
		result = prime * result
				+ ((subtotal == null) ? 0 : subtotal.hashCode());
		result = prime * result + ((taxes == null) ? 0 : taxes.hashCode());
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
		if (discount == null) {
			if (other.discount != null)
				return false;
		} else if (!discount.equals(other.discount))
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
		if (packaging == null) {
			if (other.packaging != null)
				return false;
		} else if (!packaging.equals(other.packaging))
			return false;
		if (shipping == null) {
			if (other.shipping != null)
				return false;
		} else if (!shipping.equals(other.shipping))
			return false;
		if (subtotal == null) {
			if (other.subtotal != null)
				return false;
		} else if (!subtotal.equals(other.subtotal))
			return false;
		if (taxes == null) {
			if (other.taxes != null)
				return false;
		} else if (!taxes.equals(other.taxes))
			return false;
		if (totalValueActual == null) {
			if (other.totalValueActual != null)
				return false;
		} else if (!totalValueActual.equals(other.totalValueActual))
			return false;
		return true;
	}
}
