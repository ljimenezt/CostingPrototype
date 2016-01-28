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
	private Date dateTime;
	private Date expireDate;
	private Double initialQuantityActual;
	private Double realQuantityActual;
	private Double initialCost;
	private Double unitCost;
	private Boolean depleted;
	private String location;
	private String qualityCertificateLocationLink;

	private Materials materials;
	private PurchaseInvoices purchaseInvoices;
	private Farm farm;
	private MeasurementUnits measurementUnits;

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
	 * @return initialQuantityActual: initial Quantity Actual
	 */
	@Column(name = "initial_quantity_actual")
	public Double getInitialQuantityActual() {
		return initialQuantityActual;
	}

	/**
	 * @param initialQuantityActual
	 *            : initial Quantity Actual
	 */
	public void setInitialQuantityActual(Double initialQuantityActual) {
		this.initialQuantityActual = initialQuantityActual;
	}

	/**
	 * @return realQuantityActual: Real Quantity Actual
	 */
	@Column(name = "real_quantity_actual")
	public Double getRealQuantityActual() {
		return realQuantityActual;
	}

	/**
	 * @param realQuantityActual
	 *            Real Quantity Actual
	 */
	public void setRealQuantityActual(Double realQuantityActual) {
		this.realQuantityActual = realQuantityActual;
	}

	/**
	 * @return initialCost: gets initial cost of the deposit
	 */
	@Column(name = "initial_cost")
	public Double getInitialCost() {
		return initialCost;
	}

	/**
	 * @param initialCost
	 *            : sets initial cost of the deposit
	 */
	public void setInitialCost(Double initialCost) {
		this.initialCost = initialCost;
	}

	/**
	 * @return unitCost: gets unit cost of the deposit
	 */
	@Column(name = "unit_cost", nullable = false)
	public Double getUnitCost() {
		return unitCost;
	}

	/**
	 * @param unitCost
	 *            : sets unit cost of the deposit
	 */
	public void setUnitCost(Double unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return depleted: gets depleted of deposit
	 */
	public Boolean getDepleted() {
		return depleted;
	}

	/**
	 * @param depleted
	 *            : sets depleted of deposit
	 */
	public void setDepleted(Boolean depleted) {
		this.depleted = depleted;
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
	@JoinColumn(name = "id_farm", referencedColumnName = "idfarm")
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

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result
				+ ((depleted == null) ? 0 : depleted.hashCode());
		result = prime * result
				+ ((expireDate == null) ? 0 : expireDate.hashCode());
		result = prime * result + ((farm == null) ? 0 : farm.hashCode());
		result = prime * result + idDeposit;
		result = prime * result
				+ ((initialCost == null) ? 0 : initialCost.hashCode());
		result = prime
				* result
				+ ((initialQuantityActual == null) ? 0 : initialQuantityActual
						.hashCode());
		result = prime * result
				+ ((location == null) ? 0 : location.hashCode());
		result = prime * result
				+ ((materials == null) ? 0 : materials.hashCode());
		result = prime
				* result
				+ ((measurementUnits == null) ? 0 : measurementUnits.hashCode());
		result = prime
				* result
				+ ((purchaseInvoices == null) ? 0 : purchaseInvoices.hashCode());
		result = prime
				* result
				+ ((qualityCertificateLocationLink == null) ? 0
						: qualityCertificateLocationLink.hashCode());
		result = prime
				* result
				+ ((realQuantityActual == null) ? 0 : realQuantityActual
						.hashCode());
		result = prime * result
				+ ((unitCost == null) ? 0 : unitCost.hashCode());
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
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (depleted == null) {
			if (other.depleted != null)
				return false;
		} else if (!depleted.equals(other.depleted))
			return false;
		if (expireDate == null) {
			if (other.expireDate != null)
				return false;
		} else if (!expireDate.equals(other.expireDate))
			return false;
		if (farm == null) {
			if (other.farm != null)
				return false;
		} else if (!farm.equals(other.farm))
			return false;
		if (idDeposit != other.idDeposit)
			return false;
		if (initialCost == null) {
			if (other.initialCost != null)
				return false;
		} else if (!initialCost.equals(other.initialCost))
			return false;
		if (initialQuantityActual == null) {
			if (other.initialQuantityActual != null)
				return false;
		} else if (!initialQuantityActual.equals(other.initialQuantityActual))
			return false;
		if (location == null) {
			if (other.location != null)
				return false;
		} else if (!location.equals(other.location))
			return false;
		if (materials == null) {
			if (other.materials != null)
				return false;
		} else if (!materials.equals(other.materials))
			return false;
		if (measurementUnits == null) {
			if (other.measurementUnits != null)
				return false;
		} else if (!measurementUnits.equals(other.measurementUnits))
			return false;
		if (purchaseInvoices == null) {
			if (other.purchaseInvoices != null)
				return false;
		} else if (!purchaseInvoices.equals(other.purchaseInvoices))
			return false;
		if (qualityCertificateLocationLink == null) {
			if (other.qualityCertificateLocationLink != null)
				return false;
		} else if (!qualityCertificateLocationLink
				.equals(other.qualityCertificateLocationLink))
			return false;
		if (realQuantityActual == null) {
			if (other.realQuantityActual != null)
				return false;
		} else if (!realQuantityActual.equals(other.realQuantityActual))
			return false;
		if (unitCost == null) {
			if (other.unitCost != null)
				return false;
		} else if (!unitCost.equals(other.unitCost))
			return false;
		return true;
	}

}
