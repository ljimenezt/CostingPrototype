package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * A class to represent the different types of conversions that has a specific
 * unit.
 * 
 * @author Angelica Amaya
 * @modify 13/12/2011 marisol.calderon
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "conversion_unidad", schema = "general")
public class ConversionUnidad implements Serializable {

	private ConversionUnidadPK llavePrimaria;
	private Float factorConversion;
	private String userName;
	private Date fechaCreacion;

	/**
	 * Empty class constructor that initializes the necessary variables.
	 */
	public ConversionUnidad() {
		this.llavePrimaria = new ConversionUnidadPK();
	}

	/**
	 * @return llavePrimaria: conversion of compound key unit containing the
	 *         initial and final conversion.
	 */
	@EmbeddedId
	public ConversionUnidadPK getLlavePrimaria() {
		return llavePrimaria;
	}

	/**
	 * @param llavePrimaria
	 *            : conversion of compound key unit containing the initial and
	 *            final conversion.
	 */
	public void setLlavePrimaria(ConversionUnidadPK llavePrimaria) {
		this.llavePrimaria = llavePrimaria;
	}

	/**
	 * @return factorConversion: conversion factor between the two units in
	 *         which you want to change
	 * 
	 */
	@Column(name = "factor_conversion", nullable = false)
	public Float getFactorConversion() {
		return factorConversion;
	}

	/**
	 * @param factorConversion
	 *            : conversion factor between the two units in which you want to
	 *            change
	 */
	public void setFactorConversion(Float factorConversion) {
		this.factorConversion = factorConversion;
	}

	/**
	 * @return userName: User name session running in action on recording
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name session running in action on recording
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return fechaCreacion: Creation date of registration in the database.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * 
	 * @param fechaCreacion
	 *            : Creation date of registration in the database.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((factorConversion == null) ? 0 : factorConversion.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime * result
				+ ((llavePrimaria == null) ? 0 : llavePrimaria.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		ConversionUnidad other = (ConversionUnidad) obj;
		if (factorConversion == null) {
			if (other.factorConversion != null)
				return false;
		} else if (!factorConversion.equals(other.factorConversion))
			return false;
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null)
				return false;
		} else if (!fechaCreacion.equals(other.fechaCreacion))
			return false;
		if (llavePrimaria == null) {
			if (other.llavePrimaria != null)
				return false;
		} else if (!llavePrimaria.equals(other.llavePrimaria))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
