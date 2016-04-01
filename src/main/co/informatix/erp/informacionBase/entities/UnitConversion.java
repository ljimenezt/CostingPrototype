package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * A class to represent the different types of conversions that has a specific
 * unit.
 * 
 * @author Sergio.Gelves
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "unit_conversions", schema = "general")
public class UnitConversion implements Serializable {

	private UnitConversionPK unitConversionPk;
	private double conversionFactor;

	/**
	 * Default constructor that initializes the necessary variable as an empty
	 * primary key class.
	 */
	public UnitConversion() {
		this.unitConversionPk = new UnitConversionPK();
	}

	/**
	 * @return unitConversionPk: conversion of compound key unit containing the
	 *         initial and final units.
	 */
	@EmbeddedId
	public UnitConversionPK getUnitConversionPk() {
		return unitConversionPk;
	}

	/**
	 * @param unitConversionPk
	 *            : conversion of compound key unit containing the initial and
	 *            final units.
	 */
	public void setUnitConversionPk(UnitConversionPK unitConversionPk) {
		this.unitConversionPk = unitConversionPk;
	}

	/**
	 * @return conversionFactor: conversion factor between two units.
	 * 
	 */
	@Column(name = "conversion_factor", nullable = false)
	public double getConversionFactor() {
		return conversionFactor;
	}

	/**
	 * @param conversionFactor
	 *            : conversion factor between two units.
	 */
	public void setConversionFactor(double conversionFactor) {
		this.conversionFactor = conversionFactor;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(conversionFactor);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((unitConversionPk == null) ? 0 : unitConversionPk.hashCode());
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
		UnitConversion other = (UnitConversion) obj;
		if (Double.doubleToLongBits(conversionFactor) != Double
				.doubleToLongBits(other.conversionFactor))
			return false;
		if (unitConversionPk == null) {
			if (other.unitConversionPk != null)
				return false;
		} else if (!unitConversionPk.equals(other.unitConversionPk))
			return false;
		return true;
	}

}
