package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This entity represents the classification with the units of measure
 * 
 * @author Angelica Amaya
 * @modify 13/12/2011 marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tipo_unidad", schema = "general")
public class TipoUnidad implements Serializable {

	private Integer id;
	private String nombre;
	private String descripcion;
	private Date fechaInicioVigencia;
	private Date fechaFinVigencia;
	private Date fechaRegistro;
	private String userName;

	private List<UnidadMedida> unidadesMedida;

	/**
	 * @return id: Identifier type unit of measurement
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Identifier type unit of measurement
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return nombre: name the type of unit of measure
	 */
	@Column(name = "nombre", length = 50, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : name the type of unit of measure
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return descripcion: variable that stores the description or additional
	 *         information on the type of unit.
	 */
	@Column(name = "descripcion")
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : variable that stores the description or additional
	 *            information on the type of unit.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return fechaRegistro: creation date of unit type
	 */
	@Column(name = "fecha_registro", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro
	 *            : creation date of unit type
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return fechaInicioVigencia: start date duration unit type
	 */
	@Column(name = "fecha_inicio_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	/**
	 * @param fechaInicioVigencia
	 *            : start date duration unit type
	 */
	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	/**
	 * @return fechaFinVigencia: date it terminated effective drive type
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : date it terminated effective drive type
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
	 * @return unidadesMedida: Measurement units which they are associated with
	 *         the type of unit
	 */
	@OneToMany(mappedBy = "tipoUnidad", cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
	public List<UnidadMedida> getUnidadesMedida() {
		return unidadesMedida;
	}

	/**
	 * @param unidadesMedida
	 *            : Measurement units which they are associated with the type of
	 *            unit
	 */
	public void setUnidadesMedida(List<UnidadMedida> unidadesMedida) {
		this.unidadesMedida = unidadesMedida;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		TipoUnidad other = (TipoUnidad) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

}
