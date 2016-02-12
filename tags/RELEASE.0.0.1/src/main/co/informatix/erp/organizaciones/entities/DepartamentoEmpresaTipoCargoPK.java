package co.informatix.erp.organizaciones.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.recursosHumanos.entities.TipoCargo;

/**
 * This class represents the primary key of the table
 * departamento_empresa_tipo_cargo, which is formed by a department of the
 * company and type of jobs
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class DepartamentoEmpresaTipoCargoPK implements Serializable {
	private DepartamentoEmpresa departamentoEmpresa;
	private TipoCargo tipoCargo;

	/**
	 * Empty constructor
	 */
	public DepartamentoEmpresaTipoCargoPK() {

	}

	/**
	 * Builder with the parameters of the class
	 * 
	 * @param departamentoEmpresa
	 *            : Object type department of the company.
	 * @param tipoCargo
	 *            : object type by the department of the company.
	 */
	public DepartamentoEmpresaTipoCargoPK(
			DepartamentoEmpresa departamentoEmpresa, TipoCargo tipoCargo) {
		this.departamentoEmpresa = departamentoEmpresa;
		this.tipoCargo = tipoCargo;
	}

	/**
	 * Returns the object of the department of the company
	 * 
	 * @return departamentoEmpresa: reference departments associated with the
	 *         company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
	public DepartamentoEmpresa getDepartamentoEmpresa() {
		return departamentoEmpresa;
	}

	/**
	 * Sets the object of the department of the company
	 * 
	 * @param departamentoEmpresa
	 *            : reference departments associated with the company.
	 */
	public void setDepartamentoEmpresa(DepartamentoEmpresa departamentoEmpresa) {
		this.departamentoEmpresa = departamentoEmpresa;
	}

	/**
	 * Returns the type to jobs
	 * 
	 * @return tipoCargo: type reference by the department of the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_cargo", referencedColumnName = "id", nullable = false)
	public TipoCargo getTipoCargo() {
		return tipoCargo;
	}

	/**
	 * Sets the object of charge
	 * 
	 * @param tipoCargo
	 *            : type reference by the department of the company.
	 */
	public void setTipoCargo(TipoCargo tipoCargo) {
		this.tipoCargo = tipoCargo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((departamentoEmpresa == null) ? 0 : departamentoEmpresa
						.hashCode());
		result = prime * result
				+ ((tipoCargo == null) ? 0 : tipoCargo.hashCode());
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
		DepartamentoEmpresaTipoCargoPK other = (DepartamentoEmpresaTipoCargoPK) obj;
		if (departamentoEmpresa == null) {
			if (other.departamentoEmpresa != null)
				return false;
		} else if (!departamentoEmpresa.equals(other.departamentoEmpresa))
			return false;
		if (tipoCargo == null) {
			if (other.tipoCargo != null)
				return false;
		} else if (!tipoCargo.equals(other.tipoCargo))
			return false;
		return true;
	}

}
