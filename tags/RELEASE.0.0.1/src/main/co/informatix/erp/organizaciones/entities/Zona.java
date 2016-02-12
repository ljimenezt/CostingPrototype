package co.informatix.erp.organizaciones.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

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

import co.informatix.erp.informacionBase.entities.UnidadMedida;

/**
 * This class maps the zone table, which contains information on all areas of
 * the properties associated with the system.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "zona", schema = "organizaciones")
public class Zona implements Serializable, Comparable<Zona>, Cloneable {

	private int id;
	private String nombre;
	private String ubicacion;
	private String descripcion;
	private String foto;
	private Double area;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;
	private String colorBordeMapa;

	private Hacienda hacienda;
	private UnidadMedida unidadMedida;

	private List<Lote> listaLotes;

	/**
	 * Constructor that initializes the variables required for the class.
	 */
	public Zona() {
		this.hacienda = new Hacienda();
		this.unidadMedida = new UnidadMedida();
	}

	/**
	 * 
	 * @return id: Identification of the area.
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
	 *            : Identification of the area.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return nombre: Name of the zone
	 */
	@Column(name = "nombre", length = 100, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * 
	 * @param nombre
	 *            : Name of the zone
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return ubicacion: Location Area
	 */
	@Column(name = "ubicacion", length = 200, nullable = false)
	public String getUbicacion() {
		return ubicacion;
	}

	/**
	 * 
	 * @param ubicacion
	 *            : Location Area
	 */
	public void setUbicacion(String ubicacion) {
		this.ubicacion = ubicacion;
	}

	/**
	 * @return descripcion: description of the area.
	 */
	@Column(name = "descripcion", length = 250, nullable = true)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : description of the area.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @return foto: Photo of the area
	 */
	@Column(name = "foto", length = 200, nullable = true)
	public String getFoto() {
		return foto;
	}

	/**
	 * 
	 * @param foto
	 *            : Photo of the area
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * 
	 * @return area: zone area
	 */
	@Column(name = "area", nullable = true)
	public Double getArea() {
		return area;
	}

	/**
	 * 
	 * @param area
	 *            : zone area
	 */
	public void setArea(Double area) {
		this.area = area;
	}

	/**
	 * 
	 * @return colorBordeMapa: variable that contains the color information that
	 *         is painted on the edge of the map.
	 */
	@Column(name = "color_borde_mapa", length = 20)
	public String getColorBordeMapa() {
		return colorBordeMapa;
	}

	/**
	 * 
	 * @param colorBordeMapa
	 *            : variable that contains the color information that is painted
	 *            on the edge of the map.
	 */
	public void setColorBordeMapa(String colorBordeMapa) {
		this.colorBordeMapa = colorBordeMapa;
	}

	/**
	 * 
	 * @return fechaCreacion: creation date of a record
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * 
	 * @param fechaCreacion
	 *            : creation date of a record
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
	 * @return hacienda: farm to which the zone belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hacienda", referencedColumnName = "id", nullable = false)
	public Hacienda getHacienda() {
		return hacienda;
	}

	/**
	 * @param hacienda
	 *            : farm to which the zone belongs.
	 */
	public void setHacienda(Hacienda hacienda) {
		this.hacienda = hacienda;
	}

	/**
	 * @return unidadMedida: gets the unit of measurement associated with the
	 *         area.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad_medida", referencedColumnName = "id", nullable = true)
	public UnidadMedida getUnidadMedida() {
		return unidadMedida;
	}

	/**
	 * @param unidadMedida
	 *            : sets the unit of measurement associated with the area.
	 */
	public void setUnidadMedida(UnidadMedida unidadMedida) {
		this.unidadMedida = unidadMedida;
	}

	/**
	 * @return listaLotes: list of lots that have the area.
	 */
	@Transient
	public List<Lote> getListaLotes() {
		return listaLotes;
	}

	/**
	 * @param listaLotes
	 *            : list of lots that have the area.
	 */
	public void setListaLotes(List<Lote> listaLotes) {
		this.listaLotes = listaLotes;
	}

	@Override
	public int compareTo(Zona zona) {
		return nombre.compareTo(zona.getNombre());
	}

	public Zona clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (Zona) clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((area == null) ? 0 : area.hashCode());
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result + ((foto == null) ? 0 : foto.hashCode());
		result = prime * result + id;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((ubicacion == null) ? 0 : ubicacion.hashCode());
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
		Zona other = (Zona) obj;
		if (area == null) {
			if (other.area != null)
				return false;
		} else if (!area.equals(other.area))
			return false;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
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
		return true;
	}

}
