package co.informatix.erp.organizaciones.entities;

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

import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.recursosHumanos.entities.TipoCargo;

/**
 * This class maps the empresa_persona table, which contains the information of
 * persons belonging to a company.
 * 
 * @author Luz.Jaimes
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "empresa_persona", schema = "organizaciones")
public class EmpresaPersona implements Serializable {

	private int id;
	private boolean contacto;
	private Date fechaCreacion;
	private Date fechaInicioVigencia;
	private Date fechaFinVigencia;
	private String userName;

	private Persona persona;
	private Empresa empresa;
	private Hacienda hacienda;
	private TipoCargo tipoCargo;

	/**
	 * 
	 * @return id: Unique identifier of the table or entity.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :Unique identifier of the table or entity.s
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return contacto: Attribute that indicates whether the person is business
	 *         contact
	 */
	@Column(name = "contacto", nullable = false)
	public boolean isContacto() {
		return contacto;
	}

	/**
	 * @param contacto
	 *            :Attribute that indicates whether the person is business
	 *            contact
	 */
	public void setContacto(boolean contacto) {
		this.contacto = contacto;
	}

	/**
	 * @return fechaCreacion: Creation date of registration of the person
	 *         associated with the company
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            :Creation date of registration of the person associated with
	 *            the company
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return fechaInicioVigencia: Start date of validity of the company for
	 *         the person.
	 */
	@Column(name = "fecha_inicio_vigencia", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	/**
	 * @param fechaInicioVigencia
	 *            :Start date of validity of the company for the person.
	 */
	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	/**
	 * @return fechaFinVigencia: End date validity the company to the person.
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : End date validity the company to the person.
	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	/**
	 * 
	 * @return userName: User name session running in action on recording
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            :User name session running in action on recording
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return persona: Reference to the person who is related to the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_persona", referencedColumnName = "id", nullable = false)
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @param persona
	 *            :Reference to the person who is related to the company.
	 */
	public void setPersona(Persona persona) {
		this.persona = persona;
	}

	/**
	 * @return empresa: Reference to the company to which the person belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            : Reference to the company to which the person belongs.
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return hacienda: ID of the farm if you work exclusively on a farm,
	 *         whether it is null is because not directly working on the farm
	 *         but for the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_farm", referencedColumnName = "id", nullable = true)
	public Hacienda getHacienda() {
		return hacienda;
	}

	/**
	 * @param hacienda
	 *            :ID of the farm if you work exclusively on a farm, whether it
	 *            is null is because not directly working on the farm but for
	 *            the company.
	 */
	public void setHacienda(Hacienda hacienda) {
		this.hacienda = hacienda;
	}

	/**
	 * @return tipoCargo: Reference to the type of jobs held by the person in
	 *         the company.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_cargo", referencedColumnName = "id", nullable = false)
	public TipoCargo getTipoCargo() {
		return tipoCargo;
	}

	/**
	 * @param tipoCargo
	 *            :Reference to the type of jobs held by the person in the
	 *            company.
	 */
	public void setTipoCargo(TipoCargo tipoCargo) {
		this.tipoCargo = tipoCargo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (contacto ? 1231 : 1237);
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
		EmpresaPersona other = (EmpresaPersona) obj;
		if (contacto != other.contacto)
			return false;
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
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
