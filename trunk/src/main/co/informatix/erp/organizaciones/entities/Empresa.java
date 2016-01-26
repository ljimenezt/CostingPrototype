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

import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;

/**
 * This class represents the business information system.
 * 
 * @author marisol.calderon
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "empresa", schema = "organizaciones")
public class Empresa implements Serializable {

	private int id;
	private String nit;
	private String nombre;
	private String logo;
	private String direccion;
	private String telefono;
	private String correo;
	private String fax;
	private String movil;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;

	private Organizacion organizacion;
	private Pais pais;
	private Departamento departamento;
	private Municipio municipio;

	/**
	 * Empty constructor to initialize the necessary variables.
	 */
	public Empresa() {
		this.organizacion = new Organizacion();
		this.pais = new Pais();
		this.departamento = new Departamento();
		this.municipio = new Municipio();
	}

	/**
	 * @return id: gets the identifier of the company.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : sets the identifier of the company.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return nit: tax identification number of a company.
	 */
	@Column(name = "nit", length = 25, nullable = false)
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit
	 *            : tax identification number of a company.
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return nombre: gets the name of the company.
	 */
	@Column(name = "nombre", length = 100, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : sets the name of the company.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return logo: company logo
	 */
	@Column(name = "logo", length = 200, nullable = true)
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            : company logo
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return direccion: gets the address of the company.
	 */
	@Column(name = "direccion", length = 50, nullable = false)
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            : sets the address of the company.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return telefono: gets the phone company.
	 */
	@Column(name = "telefono", length = 150, nullable = false)
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            : sets the phone company.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return correo: gets mail of a company.
	 */
	@Column(name = "correo", length = 50, nullable = true)
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo
	 *            : sets mail of a company.
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return fax: gets the fax number of a company
	 */
	@Column(name = "fax", length = 20)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            : gets the fax number of a company
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return movil: mobile phone company.
	 */
	@Column(name = "movil", length = 20, nullable = true)
	public String getMovil() {
		return movil;
	}

	/**
	 * @param movil
	 *            : mobile phone company.
	 */
	public void setMovil(String movil) {
		this.movil = movil;
	}

	/**
	 * @return fechaCreacion: get creation date of a company.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            : sets the date for registration of a company.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return fechaFinVigencia: gets the end validity date of the company
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaVigencia
	 *            : gets the end validity date of the company
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
	 * @return organizacion: organization to which a company belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_organizacion", referencedColumnName = "id")
	public Organizacion getOrganizacion() {
		return organizacion;
	}

	/**
	 * @param organizacion
	 *            : organization to which a company belongs.
	 */
	public void setOrganizacion(Organizacion organizacion) {
		this.organizacion = organizacion;
	}

	/**
	 * @return pais: Country to which the company belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais", referencedColumnName = "id", nullable = false)
	public Pais getPais() {
		return pais;
	}

	/**
	 * @param pais
	 *            : Country to which the company belongs
	 */
	public void setPais(Pais pais) {
		this.pais = pais;
	}

	/**
	 * @return departamento : Department to which the company belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = false)
	public Departamento getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento
	 *            : Department to which the company belongs
	 */
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return municipio: gets the municipalities of a business related.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio", referencedColumnName = "id", nullable = false)
	public Municipio getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio
	 *            : gets the municipalities of a business related.
	 */
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result + id;
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result + ((movil == null) ? 0 : movil.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Empresa other = (Empresa) obj;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
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
		if (movil == null) {
			if (other.movil != null)
				return false;
		} else if (!movil.equals(other.movil))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
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
