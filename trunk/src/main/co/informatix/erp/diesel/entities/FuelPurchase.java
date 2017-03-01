package co.informatix.erp.diesel.entities;

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

import co.informatix.erp.informacionBase.entities.IvaRate;
import co.informatix.erp.machines.entities.FuelTypes;
import co.informatix.erp.warehouse.entities.Suppliers;

/**
 * This class containing the record of fuel purchase and the relationship with
 * supplier, fuel type and iva rate.
 * 
 * @author Claudia.Rey
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "fuel_purchase", schema = "diesel")
public class FuelPurchase implements Serializable {

	private int idFuelPurchase;
	private double quantity;
	private double unitCost;
	private double subTotal;
	private double taxes;
	private double total;
	private String invoiceNumber;
	private String note;
	private String invoiceDocumentLink;
	private Date dateTime;

	private Suppliers supplier;
	private FuelTypes fuelType;
	private IvaRate ivaRate;

	/**
	 * Construct of class.
	 */
	public FuelPurchase() {
		this.supplier = new Suppliers();
		this.fuelType = new FuelTypes();

	}

	/**
	 * @return idFuelPurchase: Identifier of fuel purchase
	 */
	@Id
	@Column(name = "id_fuel_purchase", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdFuelPurchase() {
		return idFuelPurchase;
	}

	/**
	 * @param idFuelPurchase
	 *            : Identifier of fuel purchase
	 */
	public void setIdFuelPurchase(int idFuelPurchase) {
		this.idFuelPurchase = idFuelPurchase;
	}

	/**
	 * @return quantity: Quantity of fuel purchased
	 */
	@Column(name = "quantity", nullable = false)
	public double getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            : Quantity of fuel purchased
	 */
	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return unitCost: Cost unit of fuel purchased
	 */
	@Column(name = "unit_cost", nullable = false)
	public double getUnitCost() {
		return unitCost;
	}

	/**
	 * @param unitCost
	 *            : Cost unit of fuel purchased
	 */
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return subTotal: SubTotal of the fuel purchased
	 */
	@Column(name = "subTotal", nullable = false)
	public double getSubTotal() {
		return subTotal;
	}

	/**
	 * @param subTotal
	 *            : SubTotal of the fuel purchased
	 */
	public void setSubTotal(double subTotal) {
		this.subTotal = subTotal;
	}

	/**
	 * @return taxes: Taxes applied to the purchase of fuel
	 */
	@Column(name = "taxes")
	public double getTaxes() {
		return taxes;
	}

	/**
	 * @param taxes
	 *            : Taxes applied to the purchase of fuel
	 */
	public void setTaxes(double taxes) {
		this.taxes = taxes;
	}

	/**
	 * @return total: Value total of the fuel purchase
	 */
	@Column(name = "total", nullable = false)
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            : Value total of the fuel purchase
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return invoiceNumber: Invoice number to relate
	 */
	@Column(name = "invoice_number", length = 50, nullable = false)
	public String getInvoiceNumber() {
		return invoiceNumber;
	}

	/**
	 * @param invoiceNumber
	 *            : Invoice number to relate
	 */
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}

	/**
	 * @return note: Note of the fuel purchase
	 */
	@Column(name = "note")
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            : Note of the fuel purchase
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return invoiceDocumentLink: Link of the invoice document
	 */
	@Column(name = "invoice_document_link")
	public String getInvoiceDocumentLink() {
		return invoiceDocumentLink;
	}

	/**
	 * @param invoiceDocumentLink
	 *            : Link of the invoice document
	 */
	public void setInvoiceDocumentLink(String invoiceDocumentLink) {
		this.invoiceDocumentLink = invoiceDocumentLink;
	}

	/**
	 * @return dateTime: Date of the register of fuel invoice
	 */
	@Column(name = "date_time", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            : Date of the register of fuel invoice
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return supplier: Supplier of the fuel
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_supplier", referencedColumnName = "idsupplier", nullable = false)
	public Suppliers getSupplier() {
		return supplier;
	}

	/**
	 * @param supplier
	 *            : Supplier of the fuel
	 */
	public void setSupplier(Suppliers supplier) {
		this.supplier = supplier;
	}

	/**
	 * @return fuelType: Type of fuel purchased
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fuel_type", referencedColumnName = "idfueltype", nullable = false)
	public FuelTypes getFuelType() {
		return fuelType;
	}

	/**
	 * @param fuelType
	 *            : Type of fuel purchased
	 */
	public void setFuelType(FuelTypes fuelType) {
		this.fuelType = fuelType;
	}

	/**
	 * @return iva_rate: Iva rate applied to the fuel purchased
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_iva_rate", referencedColumnName = "id_iva")
	public IvaRate getIvaRate() {
		return ivaRate;
	}

	/**
	 * @param ivaRate
	 *            : Iva rate applied to the fuel purchased
	 */
	public void setIvaRate(IvaRate ivaRate) {
		this.ivaRate = ivaRate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + idFuelPurchase;
		result = prime
				* result
				+ ((invoiceDocumentLink == null) ? 0 : invoiceDocumentLink
						.hashCode());
		result = prime * result
				+ ((invoiceNumber == null) ? 0 : invoiceNumber.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		long temp;
		temp = Double.doubleToLongBits(quantity);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(subTotal);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(taxes);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(total);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(unitCost);
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
		FuelPurchase other = (FuelPurchase) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (idFuelPurchase != other.idFuelPurchase)
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
		if (Double.doubleToLongBits(quantity) != Double
				.doubleToLongBits(other.quantity))
			return false;
		if (Double.doubleToLongBits(subTotal) != Double
				.doubleToLongBits(other.subTotal))
			return false;
		if (Double.doubleToLongBits(taxes) != Double
				.doubleToLongBits(other.taxes))
			return false;
		if (Double.doubleToLongBits(total) != Double
				.doubleToLongBits(other.total))
			return false;
		if (Double.doubleToLongBits(unitCost) != Double
				.doubleToLongBits(other.unitCost))
			return false;
		return true;
	}

}
