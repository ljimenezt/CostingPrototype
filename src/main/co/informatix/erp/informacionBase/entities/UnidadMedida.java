package co.informatix.erp.informacionBase.entities;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class that references the available units of measure to reference amounts
 * 
 * @author Angelica Amaya
 * @modify 18/01/2012 marisol.calderon
 * @modify 21/12/2011 Liseth Jimenez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "unidad_medida", schema = "general")
public class UnidadMedida implements Serializable, Cloneable {

	private int id;
	private String nombre;
	private String abreviatura;
	private TipoUnidad tipoUnidad;
	private Date fechaRegistro;
	private Date fechaInicioVigencia;
	private Date fechaFinVigencia;
	private String userName;

	private List<ConversionUnidad> conversionesUnidadesIniciales;
	private List<ConversionUnidad> conversionesUnidadesFinales;

	/**
	 * Empty class constructor that initializes the necessary variables.
	 */
	public UnidadMedida() {
		this.tipoUnidad = new TipoUnidad();
	}

	/**
	 * @return id: ID unit of measure
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : ID unit of measure
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return nombre: name of the unit of measure
	 */
	@Column(name = "nombre", length = 50, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : name of the unit of measure
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return abreviatura: abbreviation for the unit of measurement
	 */
	@Column(name = "abreviatura", length = 10, nullable = false)
	public String getAbreviatura() {
		return abreviatura;
	}

	/**
	 * @param abreviatura
	 *            : abbreviation for the unit of measurement
	 */
	public void setAbreviatura(String abreviatura) {
		this.abreviatura = abreviatura;
	}

	/**
	 * @return tipoUnidad: type information unit as the unit of measurement
	 *         belongs.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_unidad", referencedColumnName = "id", nullable = false)
	public TipoUnidad getTipoUnidad() {
		return tipoUnidad;
	}

	/**
	 * @param tipoUnidad
	 *            : type information unit as the unit of measurement belongs.
	 */
	public void setTipoUnidad(TipoUnidad tipoUnidad) {
		this.tipoUnidad = tipoUnidad;
	}

	/**
	 * @return fechaRegistro: date of creation of the unit of measure
	 */
	@Column(name = "fecha_registro", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro
	 *            : date of creation of the unit of measure
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return fechaInicioVigencia: start date of validity of the measurement
	 *         unit
	 */
	@Column(name = "fecha_inicio_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	/**
	 * @param fechaInicioVigencia
	 *            : start date of validity of the measurement unit
	 */
	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	/**
	 * @return fechaFinVigencia: date of current of the term of the measure unit
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : date of current of the term of the measure unit
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
	 * @return conversionesUnidadesIniciales: list with the information of the
	 *         conversion of the initial measurement units, which has the unit
	 *         of measurement.
	 */
	@OneToMany(mappedBy = "llavePrimaria.unidadInicial", fetch = FetchType.LAZY)
	public List<ConversionUnidad> getConversionesUnidadesIniciales() {
		return conversionesUnidadesIniciales;
	}

	/**
	 * @param conversionesUnidadesIniciales
	 *            : list with the information of the conversion of the initial
	 *            measurement units, which has the unit of measurement.
	 */
	public void setConversionesUnidadesIniciales(
			List<ConversionUnidad> conversionesUnidadesIniciales) {
		this.conversionesUnidadesIniciales = conversionesUnidadesIniciales;
	}

	/**
	 * 
	 * @return conversionesUnidadesFinales: list with the information of the
	 *         conversion of units of the end measure, which has the unit of
	 *         measurement.
	 */
	@OneToMany(mappedBy = "llavePrimaria.unidadFinal", fetch = FetchType.LAZY)
	public List<ConversionUnidad> getConversionesUnidadesFinales() {
		return conversionesUnidadesFinales;
	}

	/**
	 * @param conversionesUnidadesFinales
	 *            : list with the information of the conversion of units of the
	 *            end measure, which has the unit of measurement.
	 */
	public void setConversionesUnidadesFinales(
			List<ConversionUnidad> conversionesUnidadesFinales) {
		this.conversionesUnidadesFinales = conversionesUnidadesFinales;
	}

	public UnidadMedida clone() {
		Object clone = null;
		try {
			clone = super.clone();
		} catch (CloneNotSupportedException e) {
		}
		return (UnidadMedida) clone;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((abreviatura == null) ? 0 : abreviatura.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime
				* result
				+ ((fechaInicioVigencia == null) ? 0 : fechaInicioVigencia
						.hashCode());
		result = prime * result
				+ ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + id;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		UnidadMedida other = (UnidadMedida) obj;
		if (abreviatura == null) {
			if (other.abreviatura != null)
				return false;
		} else if (!abreviatura.equals(other.abreviatura))
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
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null)
				return false;
		} else if (!fechaRegistro.equals(other.fechaRegistro))
			return false;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
