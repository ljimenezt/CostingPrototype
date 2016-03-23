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

import co.informatix.erp.lifeCycle.entities.Farm;

/**
 * This is the entity that is responsible for mapping the deposits table.
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "deposits", schema = "warehouse")
public class Deposits implements Serializable {

	private int idDeposit;
	private boolean selected;

	private Date dateTime;
	private Date expireDate;
	private Double initialQuantity;
	private Double actualQuantity;
	private Double totalCost;
	private String location;
	private String qualityCertificateLocationLink;

	private Materials materials;
	private PurchaseInvoices purchaseInvoices;
	private Farm farm;
	private MeasurementUnits measurementUnits;

	/**
	 * Constructor
	 */
	public Deposits() {
		this.materials = new Materials();
		this.purchaseInvoices = new PurchaseInvoices();
		this.farm = new Farm();
		this.measurementUnits = new MeasurementUnits();
	}

	/**
	 * @return idDeposit:Deposit identifier
	 */
	@Id
	@Column(name = "iddeposit", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdDeposit() {
		return idDeposit;
	}

	/**
	 * @param idDeposit
	 *            :Deposit identifier
	 */
	public void setIdDeposit(int idDeposit) {
		this.idDeposit = idDeposit;
	}

	/**
	 * @return dateTime:Date Time
	 */
	@Column(name = "date_time", nullable = false)
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
	 * @return expireDate: Expire Date
	 */
	@Column(name = "expire_date")
	@Temporal(TemporalType.DATE)
	public Date getExpireDate() {
		return expireDate;
	}

	/**
	 * @param expireDate
	 *            Expire Date
	 */
	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}

	/**
	 * @return initialQuantity: initial Quantity
	 */
	@Column(name = "initial_quantity", nullable = false)
	public Double getInitialQuantity() {
		return initialQuantity;
	}

	/**
	 * @param initialQuantity
	 *            : initial Quantity
	 */
	public void setInitialQuantity(Double initialQuantity) {
		this.initialQuantity = initialQuantity;
	}

	/**
	 * @return realQuantityActual: Real Quantity Actual
	 */
	@Column(name = "actual_quantity", nullable = false)
	public Double getActualQuantity() {
		return this.actualQuantity;
	}

	/**
	 * @param actualQuantity
	 *            actual Quantity
	 */
	public void setActualQuantity(Double actualQuantity) {
		this.actualQuantity = actualQuantity;
	}

	/**
	 * @return location: Location
	 */
	@Column(name = "location")
	public String getLocation() {
		return location;
	}

	/**
	 * @param location
	 *            :Location
	 */
	public void setLocation(String location) {
		this.location = location;
	}

	/**
	 * @return qualityCertificateLocationLink:Quality Certificate Location Link
	 */
	@Column(name = "quality_certificate_location_link")
	public String getQualityCertificateLocationLink() {
		return qualityCertificateLocationLink;
	}

	/**
	 * @param qualityCertificateLocationLink
	 *            :Quality Certificate Location Link
	 */
	public void setQualityCertificateLocationLink(
			String qualityCertificateLocationLink) {
		this.qualityCertificateLocationLink = qualityCertificateLocationLink;
	}

	/**
	 * @return totalCost: total cost
	 */
	@Column(name = "total_cost", nullable = false)
	public Double getTotalCost() {
		return totalCost;
	}

	/**
	 * @param totalCost
	 *            : total cost
	 */
	public void setTotalCost(Double totalCost) {
		this.totalCost = totalCost;
	}

	/**
	 * @return materials:Materials
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_material", referencedColumnName = "idmaterial", nullable = false)
	public Materials getMaterials() {
		return materials;
	}

	/**
	 * @param materials
	 *            :Materials
	 */
	public void setMaterials(Materials materials) {
		this.materials = materials;
	}

	/**
	 * @return purchaseInvoices: Purchase Invoices
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_purchase_invoice", referencedColumnName = "idpurchaseinvoice", nullable = false)
	public PurchaseInvoices getPurchaseInvoices() {
		return purchaseInvoices;
	}

	/**
	 * @param purchaseInvoices
	 *            :Purchase Invoices
	 */
	public void setPurchaseInvoices(PurchaseInvoices purchaseInvoices) {
		this.purchaseInvoices = purchaseInvoices;
	}

	/**
	 * @return farm: Farm
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_farm", referencedColumnName = "idfarm", nullable = false)
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            :Farm
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	/**
	 * @return measurementUnits: gets Object measurement units related a deposit
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_measurement_unit", referencedColumnName = "idmeasurementunits", nullable = false)
	public MeasurementUnits getMeasurementUnits() {
		return measurementUnits;
	}

	/**
	 * @param measurementUnits
	 *            : sets Object measurement units related a deposit
	 */
	public void setMeasurementUnits(MeasurementUnits measurementUnits) {
		this.measurementUnits = measurementUnits;
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
	 *            : Flag to see if it is selected deposit, true is selected and
	 *            false is not selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((actualQuantity == null) ? 0 : actualQuantity.hashCode());
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result
				+ ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result + idDeposit;
		result = prime * result
				+ ((initialQuantity == null) ? 0 : initialQuantity.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime
				* result
				+ ((qualityCertificateLocationLink == null) ? 0
						: qualityCertificateLocationLink.hashCode());
		result = prime * result
				+ ((totalCost == null) ? 0 : totalCost.hashCode());
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
		Deposits other = (Deposits) obj;
		if (actualQuantity == null) {
			if (other.actualQuantity != null)
				return false;
		} else if (!actualQuantity.equals(other.actualQuantity))
			return false;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (expireDate == null) {
			if (other.expireDate != null)
				return false;
		} else if (!expireDate.equals(other.expireDate))
			return false;
		if (idDeposit != other.idDeposit)
			return false;
		if (initialQuantity == null) {
			if (other.initialQuantity != null)
				return false;
		} else if (!initialQuantity.equals(other.initialQuantity))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (qualityCertificateLocationLink == null) {
			if (other.qualityCertificateLocationLink != null)
				return false;
		} else if (!qualityCertificateLocationLink
				.equals(other.qualityCertificateLocationLink))
			return false;
		if (totalCost == null) {
			if (other.totalCost != null)
				return false;
		} else if (!totalCost.equals(other.totalCost))
			return false;
		return true;
	}

}
