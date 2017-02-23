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

import co.informatix.erp.warehouse.entities.MeasurementUnits;

/**
 * This is the entity that is responsible for mapping the consumableResources.
 * 
 * @author Fabian.Diaz
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "consumable_resources", schema = "diesel")
public class ConsumableResources implements Serializable {

	private int id;
	private String name;
	private double unitCost;

	private MeasurementUnits measurementUnits;

	/**
	 * Constructor that initializes the foreign key
	 */
	public ConsumableResources() {
		this.measurementUnits = new MeasurementUnits();
	}

	/**
	 * @return id: consumable resource identify
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : consumable resource identify
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: name of consumableResource
	 */
	@Column(name = "name", nullable = false, length = 50)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : name of consumableResource
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return unitCost: unitCost of consumableResource
	 */
	@Column(name = "unit_cost", nullable = false)
	public double getUnitCost() {
		return unitCost;
	}

	/**
	 * @param unitCost
	 *            : unitCost of consumableResource
	 */
	public void setUnitCost(double unitCost) {
		this.unitCost = unitCost;
	}

	/**
	 * @return measurementUnits: measurementUnits related to the
	 *         consumableResources.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmeasurementunits", referencedColumnName = "idmeasurementunits", nullable = false)
	public MeasurementUnits getMeasurementUnits() {
		return measurementUnits;
	}

	/**
	 * @param measurementUnits
	 *            : measurementUnits related to the consumableResources.
	 */
	public void setMeasurementUnits(MeasurementUnits measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
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
		ConsumableResources other = (ConsumableResources) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(unitCost) != Double
				.doubleToLongBits(other.unitCost))
			return false;
		return true;
	}

}
