package co.informatix.erp.recursosHumanos.entities;

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

import co.informatix.erp.organizaciones.entities.Empresa;

/**
 * This class maps the contract table which contains a record of information
 * relating to the contracts that are handled in the payroll of the company.
 * 
 * @author Marcela.Chaparro
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "contrato", schema = "recursos_humanos")
public class Contrato implements Serializable {

	private int id;
	private String observaciones;
	private Boolean esPeriodoPrueba;
	private Date fechaFinContrato;
	private Date fechaCreacion;
	private String userName;

	private Empresa empresa;
	private Persona persona;

	/**
	 * @return id: Contract identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Contract identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return observaciones: Additional observations of the contract.
	 */
	@Column(name = "observaciones", length = 300)
	public String getObservaciones() {
		return observaciones;
	}

	/**
	 * @param observaciones
	 *            : Additional observations of the contract.
	 */
	public void setObservaciones(String observaciones) {
		this.observaciones = observaciones;
	}

	/**
	 * @return esPeriodoPrueba: Variable that identifies if the contract is on
	 *         probation.
	 */
	@Column(name = "es_periodo_prueba")
	public Boolean getEsPeriodoPrueba() {
		return esPeriodoPrueba;
	}

	/**
	 * @param esPeriodoPrueba
	 *            : Variable that identifies if the contract is on probation.
	 */
	public void setEsPeriodoPrueba(Boolean esPeriodoPrueba) {
		this.esPeriodoPrueba = esPeriodoPrueba;
	}

	/**
	 * @return fechaFinContrato: End date of the trial period, if the contract
	 *         is of this type.
	 */
	@Column(name = "fecha_fin_contrato")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinContrato() {
		return fechaFinContrato;
	}

	/**
	 * @param fechaFinContrato
	 *            : End date of the trial period, if the contract is of this
	 *            type.
	 */
	public void setFechaFinContrato(Date fechaFinContrato) {
		this.fechaFinContrato = fechaFinContrato;
	}

	/**
	 * @return fechaCreacion: Creation date of registration.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            : Creation date of registration.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return userName: User name in session running the action on the record.
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name in session running the action on the record.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return empresa: Reference of the company with which the contract is made
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id")
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            : Reference of the company with which the contract is made
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return persona: Reference person who has been associated with an
	 *         employment contract
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona", referencedColumnName = "id", nullable = false)
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @param persona
	 *            : Reference person who has been associated with an employment
	 *            contract
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((esPeriodoPrueba == null) ? 0 : esPeriodoPrueba.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinContrato == null) ? 0 : fechaFinContrato.hashCode());
		result = prime * result + id;
		result = prime * result
				+ ((observaciones == null) ? 0 : observaciones.hashCode());
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
		Contrato other = (Contrato) obj;
		if (esPeriodoPrueba == null) {
			if (other.esPeriodoPrueba != null)
				return false;
		} else if (!esPeriodoPrueba.equals(other.esPeriodoPrueba))
			return false;
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null)
				return false;
		} else if (!fechaCreacion.equals(other.fechaCreacion))
			return false;
		if (fechaFinContrato == null) {
			if (other.fechaFinContrato != null)
				return false;
		} else if (!fechaFinContrato.equals(other.fechaFinContrato))
			return false;
		if (id != other.id)
			return false;
		if (observaciones == null) {
			if (other.observaciones != null)
				return false;
		} else if (!observaciones.equals(other.observaciones))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}
}
