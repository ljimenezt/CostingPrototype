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
 * This class maps the lot table, which allows the system to record the
 * different lots (valves) used in areas of a farm.
 * 
 * @author Gerson.Cespedes
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "lote", schema = "organizaciones")
public class Lote implements Serializable {

	private int id;
	private String codigo;
	private String nombre;
	private String descripcion;
	private String color;
	private double area;
	private double densidad;
	private int nroPlantas;
	private int tamXPlanta;
	private int tamYPlanta;
	private int pendiente;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;

	private Zona zona;
	private Hacienda hacienda;
	private UnidadMedida unidadMedidaArea;
	private UnidadMedida unidadMedidaDensidad;
	private UnidadMedida unidadMedidaTamX;
	private UnidadMedida unidadMedidaTamY;

	private List<Integer[]> listCoordLimites;

	/**
	 * Class constructor
	 */
	public Lote() {
		this.unidadMedidaArea = new UnidadMedida();
		this.unidadMedidaDensidad = new UnidadMedida();
		this.unidadMedidaTamX = new UnidadMedida();
		this.unidadMedidaTamY = new UnidadMedida();
	}

	/**
	 * 
	 * @return id: Unique identifier for the lot table.
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
	 *            :Unique identifier for the lot table.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * 
	 * @return codigo: Lot identification code
	 */
	@Column(name = "codigo", length = 20, nullable = false)
	public String getCodigo() {
		return codigo;
	}

	/**
	 * 
	 * @param codigo
	 *            : Lot identification code
	 */
	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}

	/**
	 * 
	 * @return nombre: Lot name or valve.
	 */
	@Column(name = "nombre", length = 100, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * 
	 * @param nombre
	 *            : Lot name or valve.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return descripcion: Description that can be done about the lot or valve.
	 */
	@Column(name = "descripcion", length = 200, nullable = true)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * 
	 * @param descripcion
	 *            : Description that can be done about the lot or valve.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @return color: color identifying the batch or valve.
	 */
	@Column(name = "color", length = 20, nullable = false)
	public String getColor() {
		return color;
	}

	/**
	 * 
	 * @param color
	 *            : color identifying the batch or valve.
	 */
	public void setColor(String color) {
		this.color = color;
	}

	/**
	 * 
	 * @return area: area covering a lot or a valve.
	 */
	@Column(name = "area", nullable = false)
	public double getArea() {
		return area;
	}

	/**
	 * 
	 * @param area
	 *            : area covering a lot or a valve.
	 */
	public void setArea(double area) {
		this.area = area;
	}

	/**
	 * 
	 * @return densidad: Density in the lot or valve.
	 */
	@Column(name = "densidad", nullable = false)
	public double getDensidad() {
		return densidad;
	}

	/**
	 * 
	 * @param densidad
	 *            : Density in the lot or valve.
	 */
	public void setDensidad(double densidad) {
		this.densidad = densidad;
	}

	/**
	 * 
	 * @return nroPlantas: Number of plants covering the lot or the valve.
	 */
	@Column(name = "nro_plantas", nullable = false)
	public int getNroPlantas() {
		return nroPlantas;
	}

	/**
	 * 
	 * @param nroPlantas
	 *            : Number of plants covering the lot or the valve.
	 */
	public void setNroPlantas(int nroPlantas) {
		this.nroPlantas = nroPlantas;
	}

	/**
	 * 
	 * @return tamXPlanta: plant size in relation to the x axis.
	 */
	@Column(name = "tam_x_planta", nullable = false)
	public int getTamXPlanta() {
		return tamXPlanta;
	}

	/**
	 * 
	 * @param tamXPlanta
	 *            : plant size in relation to the x axis.
	 */
	public void setTamXPlanta(int tamXPlanta) {
		this.tamXPlanta = tamXPlanta;
	}

	/**
	 * 
	 * @return tamYPlanta: plant size in relation to the y axis.
	 */
	@Column(name = "tam_y_planta", nullable = false)
	public int getTamYPlanta() {
		return tamYPlanta;
	}

	/**
	 * 
	 * @param tamYPlanta
	 *            : plant size in relation to the y axis.
	 */
	public void setTamYPlanta(int tamYPlanta) {
		this.tamYPlanta = tamYPlanta;
	}

	/**
	 * 
	 * @return pendiente: Value of the slope of the lot or valve.
	 */
	@Column(name = "pendiente", nullable = false)
	public int getPendiente() {
		return pendiente;
	}

	/**
	 * 
	 * @param pendiente
	 *            : Value of the slope of the lot or valve.
	 */
	public void setPendiente(int pendiente) {
		this.pendiente = pendiente;
	}

	/**
	 * 
	 * @return fechaCreacion: Creation date of registration.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * 
	 * @param fechaCreacion
	 *            :Creation date of registration.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * 
	 * @return fechaFinVigencia: End date of the term of the lot.
	 * 
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * 
	 * @param fechaFinVigencia
	 *            : End date of the term of the lot.
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
	 * 
	 * @param userName
	 *            : User name session running in action on recording
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * 
	 * @return zona: Zone object.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_zona", referencedColumnName = "id", nullable = false)
	public Zona getZona() {
		return zona;
	}

	/**
	 * 
	 * @param zona
	 *            : Zone object.
	 */
	public void setZona(Zona zona) {
		this.zona = zona;
	}

	/**
	 * 
	 * @return hacienda: Farm object.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hacienda", referencedColumnName = "id", nullable = false)
	public Hacienda getHacienda() {
		return hacienda;
	}

	/**
	 * 
	 * @param hacienda
	 *            : Farm object.
	 */
	public void setHacienda(Hacienda hacienda) {
		this.hacienda = hacienda;
	}

	/**
	 * 
	 * @return unidadMedidaArea: Unit of measure for the area.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad_medida_area", referencedColumnName = "id", nullable = true)
	public UnidadMedida getUnidadMedidaArea() {
		return unidadMedidaArea;
	}

	/**
	 * 
	 * @param unidadMedidaArea
	 *            : Unit of measure for the area.
	 */
	public void setUnidadMedidaArea(UnidadMedida unidadMedidaArea) {
		this.unidadMedidaArea = unidadMedidaArea;
	}

	/**
	 * 
	 * @return unidadMedidaDensidad: Unit of measure for the density.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad_medida_densidad", referencedColumnName = "id", nullable = true)
	public UnidadMedida getUnidadMedidaDensidad() {
		return unidadMedidaDensidad;
	}

	/**
	 * 
	 * @param unidadMedidaDensidad
	 *            : Unit of measure for the density.
	 */
	public void setUnidadMedidaDensidad(UnidadMedida unidadMedidaDensidad) {
		this.unidadMedidaDensidad = unidadMedidaDensidad;
	}

	/**
	 * 
	 * @return unidadMedidaTamX: Unit of measure for the size x of the plant.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad_medida_tam_x", referencedColumnName = "id", nullable = true)
	public UnidadMedida getUnidadMedidaTamX() {
		return unidadMedidaTamX;
	}

	/**
	 * 
	 * @param unidadMedidaTamX
	 *            : Unit of measure for the size x of the plant.
	 */
	public void setUnidadMedidaTamX(UnidadMedida unidadMedidaTamX) {
		this.unidadMedidaTamX = unidadMedidaTamX;
	}

	/**
	 * 
	 * @return unidadMedidaTamY: Unit of measure for the size of the plant.
	 * 
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad_medida_tam_y", referencedColumnName = "id", nullable = true)
	public UnidadMedida getUnidadMedidaTamY() {
		return unidadMedidaTamY;
	}

	/**
	 * 
	 * @param unidadMedidaTamY
	 *            : Unit of measure for the size of the plant.
	 */
	public void setUnidadMedidaTamY(UnidadMedida unidadMedidaTamY) {
		this.unidadMedidaTamY = unidadMedidaTamY;
	}

	/**
	 * @return listCoordLimites: list of coordinates x, y which has the lot on
	 *         the map.
	 */
	@Transient
	public List<Integer[]> getListCoordLimites() {
		return listCoordLimites;
	}

	/**
	 * @param listCoordLimites
	 *            : list of coordinates x, y which has the lot on the map.
	 */
	public void setListCoordLimites(List<Integer[]> listCoordLimites) {
		this.listCoordLimites = listCoordLimites;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(area);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((codigo == null) ? 0 : codigo.hashCode());
		result = prime * result + ((color == null) ? 0 : color.hashCode());
		temp = Double.doubleToLongBits(densidad);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result + id;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result + nroPlantas;
		result = prime * result + pendiente;
		result = prime * result + tamXPlanta;
		result = prime * result + tamYPlanta;
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
		Lote other = (Lote) obj;
		if (Double.doubleToLongBits(area) != Double
				.doubleToLongBits(other.area))
			return false;
		if (codigo == null) {
			if (other.codigo != null)
				return false;
		} else if (!codigo.equals(other.codigo))
			return false;
		if (color == null) {
			if (other.color != null)
				return false;
		} else if (!color.equals(other.color))
			return false;
		if (Double.doubleToLongBits(densidad) != Double
				.doubleToLongBits(other.densidad))
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
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (nroPlantas != other.nroPlantas)
			return false;
		if (pendiente != other.pendiente)
			return false;
		if (tamXPlanta != other.tamXPlanta)
			return false;
		if (tamYPlanta != other.tamYPlanta)
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
