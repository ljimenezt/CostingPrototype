package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.warehouse.entities.MeasurementUnits;

/**
 * It represents the embeddable primary key of the class Conversion Unit.
 * 
 * @author Angelica Amaya
 * @modify 13/12/2011 marisol.calderon
 */
@SuppressWarnings("serial")
@Embeddable
public class UnitConversionPK implements Serializable {

	private MeasurementUnits originalUnit;
	private MeasurementUnits finalUnit;

	/**
	 * Default constructor that initializes the necessary variables.
	 */
	public UnitConversionPK() {
		this.originalUnit = new MeasurementUnits();
		this.finalUnit = new MeasurementUnits();
	}

	/**
	 * @return originalUnit: Original unit to calculate an equivalent amount.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_original_unit", referencedColumnName = "idmeasurementunits", nullable = false)
	public MeasurementUnits getOriginalUnit() {
		return originalUnit;
	}

	/**
	 * @param originalUnit
	 *            : Original unit to calculate an equivalent amount.
	 */
	public void setOriginalUnit(MeasurementUnits originalUnit) {
		this.originalUnit = originalUnit;
	}

	/**
	 * @return unidadFinal: Final unit for a conversion.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_final_unit", referencedColumnName = "idmeasurementunits", nullable = false)
	public MeasurementUnits getFinalUnit() {
		return finalUnit;
	}

	/**
	 * @param finalUnit
	 *            : Final unit for a conversion.
	 */
	public void setFinalUnit(MeasurementUnits finalUnit) {
		this.finalUnit = finalUnit;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((finalUnit == null) ? 0 : finalUnit.hashCode());
		result = prime * result
				+ ((originalUnit == null) ? 0 : originalUnit.hashCode());
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
		UnitConversionPK other = (UnitConversionPK) obj;
		if (finalUnit == null) {
			if (other.finalUnit != null)
				return false;
		} else if (!finalUnit.equals(other.finalUnit))
			return false;
		if (originalUnit == null) {
			if (other.originalUnit != null)
				return false;
		} else if (!originalUnit.equals(other.originalUnit))
			return false;
		return true;
	}

}
