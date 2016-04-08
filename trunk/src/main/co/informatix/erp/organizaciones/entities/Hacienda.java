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
import javax.persistence.Transient;

import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.informacionBase.entities.UnidadMedida;
import co.informatix.erp.recursosHumanos.entities.Persona;

/**
 * This class maps the farm table, which contains information on all the
 * properties associated with the system
 * 
 * @author marisol.calderon
 * @modify 03/02/2015 Jonathan.Arias
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "hacienda", schema = "organizaciones")
public class Hacienda implements Serializable, Comparable<Hacienda>, Cloneable {

	private int id;
	private String codigoRegistro;
	private String nombre;
	private String ubicacion;
	private String foto;
	private Integer area;
	private String vereda;
	private Double longitud;
	private Double latitud;
	private Double altitud;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;
	private String mapa;

	private Empresa empresa;
	private Persona contacto;
	private Pais pais;
	private Departamento departamento;
	private Municipio municipio;
	private UnidadMedida unidadMedida;

	/**
	 * Constructor method.
	 */
	public Hacienda() {
		this.empresa = new Empresa();
		this.contacto = new Persona();
		this.pais = new Pais();
		this.departamento = new Departamento();
		this.municipio = new Municipio();
		this.unidadMedida = new UnidadMedida();
	}

	/**
	 * 
	 * @return id: Identification of the farm.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * 
	 * @param id
	 *            : Identification of the farm.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return codigoRegistro: registration code of the farm.
	 */
	@Column(name = "codigo_registro", length = 50, nullable = true)
	public String getCodigoRegistro() {
		return codigoRegistro;
	}

	/**
	 * @param codigoRegistro
	 *            : registration code of the farm.
	 */
	public void setCodigoRegistro(String codigoRegistro) {
		this.codigoRegistro = codigoRegistro;
	}

	/**
	 * 
	 * @return nombre: Name of the farm
	 */
	@Column(name = "nombre", length = 70, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * 
	 * @param nombre
	 *            : Name of the farm
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return ubicacion: Location of the farm
	 */
	@Column(name = "ubicacion", length = 200, nullable = false)
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * 
	 * @param ubicacion
	 *            : Location of the farm
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**
	 * 
	 * @return foto: Photo of the farm
	 */
	@Column(name = "foto", length = 200, nullable = true)
	public String getFoto() {
		return foto;
	}

	/**
	 * 
	 * @param foto
	 *            : Photo of the farm
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * 
	 * @return area: Area of the farm
	 */
	@Column(name = "area", nullable = true)
	public Integer getArea() {
		return area;
	}

	/**
	 * 
	 * @param area
	 *            : Area of the farm
	 */
	public void setArea(Integer area) {
		this.area = area;
	}

	/**
	 * 
	 * @return vereda: Path to which the farm belongs
	 */
	@Column(name = "vereda", length = 100, nullable = true)
	public String getVereda() {
		return vereda;
	}

	/**
	 * 
	 * @param vereda
	 *            : Path to which the farm belongs
	 */
	public void setVereda(String vereda) {
		this.vereda = vereda;
	}

	/**
	 * 
	 * @return longitud: Length of the farm
	 */
	@Column(name = "longitud", nullable = true)
	public Double getLongitud() {
		return longitud;
	}

	/**
	 * 
	 * @param longitud
	 *            : Length of the farm
	 */
	public void setLongitud(Double longitud) {
		this.longitud = longitud;
	}

	/**
	 * 
	 * @return latitud: Latitude of farm
	 */
	@Column(name = "latitud", nullable = true)
	public Double getLatitud() {
		return latitud;
	}

	/**
	 * 
	 * @param latitud
	 *            : Latitude of farm
	 */
	public void setLatitud(Double latitud) {
		this.latitud = latitud;
	}

	/**
	 * 
	 * @return altitud: Altitude of the farm
	 */
	@Column(name = "altitud", nullable = true)
	public Double getAltitud() {
		return altitud;
	}

	/**
	 * 
	 * @param altitud
	 *            : Altitude of the farms
	 */
	public void setAltitud(Double altitud) {
		this.altitud = altitud;
	}

	/**
	 * 
	 * @return fechaCreacion: Record Creation Date
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * 
	 * @param fechaCreacion
	 *            : Record Creation Date
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * 
	 * @return fechaFinVigencia: Date on which the registration validity ends
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * 
	 * @param fechaFinVigencia
	 *            : Date on which the registration validity ends
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
	 * @return contacto: gets the contact associated with the farm.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_contacto", referencedColumnName = "id", nullable = false)
	public Persona getContacto() {
		return contacto;
	}

	/**
	 * @param contacto
	 *            : sets the contact associated with the farm.
	 */
	public void setContacto(Persona contacto) {
		this.contacto = contacto;
	}

	/**
	 * @return empresa: company to which the farm belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id", nullable = false)
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            : company to which the farm belongs
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return departamento: gets the department associated with the farm.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id", nullable = true)
	public Departamento getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento
	 *            : sets the department associated with the farm.
	 */
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return municipio: gets the municipality associated with the farm.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio", referencedColumnName = "id", nullable = true)
	public Municipio getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio
	 *            : sets the municipality associated with the farm.
	 */
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	/**
	 * @return pais: gets the associated country estate.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais", referencedColumnName = "id", nullable = false)
	public Pais getPais() {
		return pais;
	}

	/**
	 * @param pais
	 *            : sets the associated country estate.
	 */
	public void setPais(Pais pais) {
		this.pais = pais;
	}

	/**
	 * @return unidadMedida: gets the unit of measurement associated with the
	 *         farm.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmeasurementunits", referencedColumnName = "id", nullable = true)
	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * @param unidadMedida
	 *            : sets the unit of measurement associated with the farm.
	 */
	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	/**
	 * @return mapa: variable that gets the file name of the farm map. Variable
	 *         used for business logic, it is not stored in the database.
	 */
	@Transient
	public String getMapa() {
		return mapa;
	}

	/**
	 * @param mapa
	 *            : variable that sets the file name of the farm map. Variable
	 *            used for business logic, it is not stored in the database.
	 */
	public void setMapa(String mapa) {
		this.mapa = mapa;
	}

	@Override
	public int compareTo(Hacienda hacienda) {
		return nombre.compareTo(hacienda.getNombre());
	}

	public Hacienda clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (Hacienda) clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((altitud == null) ? 0 : altitud.hashCode());
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result
				+ ((codigoRegistro == null) ? 0 : codigoRegistro.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result + ((foto == null) ? 0 : foto.hashCode());
		result = prime * result + id;
		result = prime * result + ((latitud == null) ? 0 : latitud.hashCode());
		result = prime * result
				+ ((longitud == null) ? 0 : longitud.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((ubicacion == null) ? 0 : ubicacion.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		result = prime * result + ((vereda == null) ? 0 : vereda.hashCode());
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
		Hacienda other = (Hacienda) obj;
		if (altitud == null) {
			if (other.altitud != null)
				return false;
		} else if (!altitud.equals(other.altitud))
			return false;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (codigoRegistro == null) {
			if (other.codigoRegistro != null)
				return false;
		} else if (!codigoRegistro.equals(other.codigoRegistro))
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
		if (foto == null) {
			if (other.foto != null)
				return false;
		} else if (!foto.equals(other.foto))
			return false;
		if (id != other.id)
			return false;
		if (latitud == null) {
			if (other.latitud != null)
				return false;
		} else if (!latitud.equals(other.latitud))
			return false;
		if (longitud == null) {
			if (other.longitud != null)
				return false;
		} else if (!longitud.equals(other.longitud))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (ubicacion == null) {
			if (other.ubicacion != null)
				return false;
		} else if (!ubicacion.equals(other.ubicacion))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		if (vereda == null) {
			if (other.vereda != null)
				return false;
		} else if (!vereda.equals(other.vereda))
			return false;
		return true;
	}

}
