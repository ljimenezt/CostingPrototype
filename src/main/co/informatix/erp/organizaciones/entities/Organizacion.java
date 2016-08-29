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

import co.informatix.erp.informacionBase.entities.TipoDocumento;

/**
 * 
 * This class maps the organization table
 * 
 * @author Oscar.Amaya
 * @modify Luis.Ruiz
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "organizacion", schema = "organizaciones")
public class Organizacion implements Serializable, Comparable<Organizacion> {

	private int id;
	private String nit;
	private String razonSocial;
	private String direccion;
	private String telefono;
	private String logo;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;

	private TipoDocumento tipoDocumento;

	/**
	 * @return id: ID (primary key) of the table
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :ID (primary key) of the table
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return nit: NIT of the organization
	 */
	@Column(name = "nit", length = 100, nullable = false)
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit
	 *            : NIT of the organization
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return razonSocial: Business name of the organization
	 */
	@Column(name = "razon_social", length = 150, nullable = false)
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial
	 *            : Business name of the organization
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return direccion: direction of the organization
	 */
	@Column(name = "direccion", length = 200, nullable = true)
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            : direction of the organizations
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return telefono: Organization Phone
	 */
	@Column(name = "telefono", length = 100, nullable = true)
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            : Organization Phone
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return logo: Image name of the logo of the organization.
	 */
	@Column(name = "logo", length = 200)
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            : Image name of the logo of the organization.
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return fechaCreacion: creation date of record
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            : creation date of record
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return fechaFinVigencia: date of currency of the registration order
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : date of currency of the registration order
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
	 *            : User name session running in action on recording
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return tipoDocumento: gets the type of document of the organization.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            : sets the type of document of the organization.
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	public int compareTo(Organizacion organizacion) {
		return razonSocial.compareTo(organizacion.getRazonSocial());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result + id;
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result
				+ ((razonSocial == null) ? 0 : razonSocial.hashCode());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
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
		Organizacion other = (Organizacion) obj;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
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
		if (id != other.id)
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (razonSocial == null) {
			if (other.razonSocial != null)
				return false;
		} else if (!razonSocial.equals(other.razonSocial))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
