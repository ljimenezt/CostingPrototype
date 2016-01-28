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

import co.informatix.erp.informacionBase.entities.TipoDocumento;
import co.informatix.erp.organizaciones.entities.Empresa;

/**
 * This class maps the third table which contains records of third parties who
 * manages the company.
 * 
 * @author Marcela.Chaparro
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tercero", schema = "recursos_humanos")
public class Tercero implements Serializable {

	private int id;
	private String documento;
	private String nombres;
	private String apellidos;
	private String razonSocial;
	private String direccion;
	private String telefono;
	private String correo;
	private String movil;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;

	private Empresa empresa;
	private TipoDocumento tipoDocumento;

	/**
	 * Class constructor.s
	 */
	public Tercero() {
		this.empresa = new Empresa();
		this.tipoDocumento = new TipoDocumento();
	}

	/**
	 * @return id: Table Identifier third person.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Table Identifier third person.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return documento : Third person identification number and number of
	 *         identity card, passport or NIT.
	 */
	@Column(name = "documento", length = 15, nullable = false)
	public String getDocumento() {
		return documento;
	}

	/**
	 * @param documento
	 *            : Third person identification number and number of identity
	 *            card, passport or NIT.
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * @return nombres: Names of the third person, for natural persons.
	 */
	@Column(name = "nombre", length = 100)
	public String getNombres() {
		return nombres;
	}

	/**
	 * @param nombres
	 *            : Names of the third person, for natural persons.
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	/**
	 * @return apellidos: Third person surname for natural persons.
	 */
	@Column(name = "apellido", length = 100)
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * @param apellidos
	 *            : Third person surname for natural persons.
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @return razonSocial: Name and address of the third, for legal persons.
	 */
	@Column(name = "razon_social", length = 100)
	public String getRazonSocial() {
		return razonSocial;
	}

	/**
	 * @param razonSocial
	 *            : Name and address of the third, for legal persons.
	 */
	public void setRazonSocial(String razonSocial) {
		this.razonSocial = razonSocial;
	}

	/**
	 * @return direccion: Address the third.
	 */
	@Column(name = "direccion", length = 200, nullable = false)
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            : Address the third.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return telefono: Phone number of the third.
	 */
	@Column(name = "telefono", length = 50, nullable = false)
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            : Phone number of the third.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return correo: E-mail of the third person.
	 */
	@Column(name = "correo", length = 150, nullable = false)
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo
	 *            : E-mail of the third person.
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return movil: Mobile number of the third person.
	 */
	@Column(name = "movil", length = 10)
	public String getMovil() {
		return movil;
	}

	/**
	 * @param movil
	 *            : Mobile number of the third persons.
	 */
	public void setMovil(String movil) {
		this.movil = movil;
	}

	/**
	 * @return fechaCreacion: Creation date of registration or third person in
	 *         the company.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            : Creation date of registration or third person in the
	 *            company.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return fechaFinVigencia: End date of the term of the Third person.
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : End date of the term of the Third person.
	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	/**
	 * @return userName: Create or modify user information.
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : Create or modify user information.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return empresa: Company to which the third part.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            : Company to which the third part.
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return tipoDocumento: Type of identification document of the third
	 *         person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            : Type of identification document of the third person.s
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result
				+ ((documento == null) ? 0 : documento.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result + id;
		result = prime * result + ((movil == null) ? 0 : movil.hashCode());
		result = prime * result + ((nombres == null) ? 0 : nombres.hashCode());
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
		Tercero other = (Tercero) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
			return false;
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
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
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
		if (movil == null) {
			if (other.movil != null)
				return false;
		} else if (!movil.equals(other.movil))
			return false;
		if (nombres == null) {
			if (other.nombres != null)
				return false;
		} else if (!nombres.equals(other.nombres))
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
