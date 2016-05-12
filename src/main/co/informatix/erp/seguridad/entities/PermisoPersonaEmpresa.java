package co.informatix.erp.seguridad.entities;

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
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.organizaciones.entities.Sucursal;
import co.informatix.erp.recursosHumanos.entities.Persona;

/**
 * This class maps the table permiso_empresa_persona_sucursal_hacienda, which
 * can store the information of the access rights of individuals to a company or
 * companies and their subsidiaries and / or farms.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "permiso_persona_empresa", schema = "seguridad")
public class PermisoPersonaEmpresa implements Serializable {

	private int id;
	private Date fechaCreacion;
	private Date fechaInicioVigencia;
	private Date fechaFinVigencia;
	private boolean predeterminado;
	private String userName;

	private Empresa empresa;
	private Persona persona;
	private Sucursal sucursal;
	private Farm farm;

	/**
	 * @return id: identifier permit for the person in the company.
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id", nullable = false)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : identifier permit for the person in the company.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return fechaCreacion: Date of creating access permission for the person.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * 
	 * @param fechaCreacion
	 *            : Date of creating access permission for the person.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return fechaInicioVigencia: current start date of the permit for access
	 *         to the person.
	 */
	@Column(name = "fecha_inicio_vigencia", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	/**
	 * @param fechaInicioVigencia
	 *            : current start date of the permit for access to the person.
	 */
	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	/**
	 * @return fechaFinVigencia: current ending date of the permit for access to
	 *         the person.
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : current ending date of the permit for access to the person.
	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	/**
	 * @return predeterminado: Indicates the default company to be charged to
	 *         log into the system.
	 */
	@Column(name = "predeterminada")
	public boolean isPredeterminado() {
		return predeterminado;
	}

	/**
	 * @param predeterminado
	 *            : Indicates the default company to be charged to log into the
	 *            system.
	 */
	public void setPredeterminado(boolean predeterminado) {
		this.predeterminado = predeterminado;
	}

	/**
	 * @return userName: User name in session running the action on the
	 *         registration of the transfer movement son.
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name in session running the action on the registration
	 *            of the transfer movement son.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return empresa: Company which the person has access permissions.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            : Company which the person has access permissions.
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return persona: person who is assigned access permissions
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona", referencedColumnName = "id", nullable = false)
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @param persona
	 *            : person who is assigned access permissions
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	/**
	 * @return sucursal: branch in which the authorized person in the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal", referencedColumnName = "id", nullable = true)
	public Sucursal getSucursal() {
		return sucursal;
	}

	/**
	 * @param sucursal
	 *            : branch in which the authorized person in the company.
	 */
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	/**
	 * @return farm: farm to which the person has permission to view the
	 *         information.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_farm", referencedColumnName = "idfarm", nullable = false)
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            : farm to which the person has permission to view the
	 *            information.
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
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
		result = prime
				* result
				+ ((fechaInicioVigencia == null) ? 0 : fechaInicioVigencia
						.hashCode());
		result = prime * result + id;
		result = prime * result + (predeterminado ? 1231 : 1237);
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
		PermisoPersonaEmpresa other = (PermisoPersonaEmpresa) obj;
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
		if (fechaInicioVigencia == null) {
			if (other.fechaInicioVigencia != null)
				return false;
		} else if (!fechaInicioVigencia.equals(other.fechaInicioVigencia))
			return false;
		if (id != other.id)
			return false;
		if (predeterminado != other.predeterminado)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}