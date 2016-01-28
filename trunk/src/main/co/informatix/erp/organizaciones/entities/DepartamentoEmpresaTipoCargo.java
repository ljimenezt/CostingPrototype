package co.informatix.erp.organizaciones.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class represents the relation between the departments of a company with
 * the type of jobs they can have.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "departamento_empresa_tipo_cargo", schema = "organizaciones")
public class DepartamentoEmpresaTipoCargo implements Serializable {

	private DepartamentoEmpresaTipoCargoPK llavePrimaria;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;

	/**
	 * @return llavePrimaria: gets llavePrimaria of the entity.
	 */
	@EmbeddedId
	public DepartamentoEmpresaTipoCargoPK getLlavePrimaria() {
		return llavePrimaria;
	}

	/**
	 * @param llavePrimaria
	 *            : sets llavePrimaria of the entity.
	 */
	public void setLlavePrimaria(DepartamentoEmpresaTipoCargoPK llavePrimaria) {
		this.llavePrimaria = llavePrimaria;
	}

	/**
	 * 
	 * @return fechaCreacion: Date of creation of types of jobs with the
	 *         department of the company.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * 
	 * @param fechaCreacion
	 *            : Date of creation of types of jobs with the department of the
	 *            company.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * 
	 * @return fechaFinVigencia: End date of the term of types of jobs with the
	 *         department of the company.
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * 
	 * @param fechaFinVigencia
	 *            : End date of the term of types of jobs with the department of
	 *            the company.
	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
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
	 *            : User name session running in action on recordings
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
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
		DepartamentoEmpresaTipoCargo other = (DepartamentoEmpresaTipoCargo) obj;
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null)
				return false;
		} else if (!fechaCreacion.equals(other.fechaCreacion))
			return false;
		if (fechaFinVigencia == null) {
			if (other.fechaFinVigencia != null)
				return false;
		} else if (!fechaFinVigencia.equals(other.fechaFinVigencia))
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
