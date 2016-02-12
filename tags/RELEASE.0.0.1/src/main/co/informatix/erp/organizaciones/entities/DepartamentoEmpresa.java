package co.informatix.erp.organizaciones.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import co.informatix.erp.recursosHumanos.entities.TipoCargo;

/**
 * This class maps the departamento_empresa table, which contains information on
 * the types of departments that may have a business, example: administrative,
 * human resources, systems ... etc.
 * 
 * @author Gerson.Cespedes
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "departamento_empresa", schema = "organizaciones")
public class DepartamentoEmpresa implements Serializable {

	private int id;
	private String nombre;
	private String descripcion;
	private Boolean predeterminadoCalidad;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;
	private String activo;

	private List<TipoCargo> tipoCargos;

	/**
	 * 
	 * @return id: Unique identifier of the department of the company.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Unique identifier of the department of the company.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return nombre: department name of the company.
	 */
	@Column(name = "nombre", length = 100, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * 
	 * @param nombre
	 *            : department name of the company.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return descripcion: description of the business department.
	 */
	@Column(name = "descripcion", length = 200, nullable = true)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * 
	 * @param descripcion
	 *            : description of the business department.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * 
	 * @return predeterminadoCalidad: flag indicates whether the department is
	 *         quality or not
	 */
	@Column(name = "predeterminado_calidad", nullable = false)
	public Boolean getPredeterminadoCalidad() {
		return predeterminadoCalidad;
	}

	/**
	 * 
	 * @param predeterminadoCalidad
	 *            : flag indicates whether the department is quality or not
	 */
	public void setPredeterminadoCalidad(Boolean predeterminadoCalidad) {
		this.predeterminadoCalidad = predeterminadoCalidad;
	}

	/**
	 * 
	 * @return fechaCreacion: Date of creation of the department.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * 
	 * @param fechaCreacion
	 *            : Date of creation of the department.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * 
	 * @return fechaFinVigencia: End date of the term of the department.
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * 
	 * @param fechaFinVigencia
	 *            : End date of the term of the department.
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
	 * @return The types of functions that have the department of the company
	 *         (tipoCargos)
	 */
	@Transient
	public List<TipoCargo> getTipoCargos() {
		return tipoCargos;
	}

	/**
	 * @param tipoCargos
	 *            The types of functions that have the department of the company
	 *            (tipoCargos)
	 */
	public void setTipoCargos(List<TipoCargo> tipoCargos) {
		this.tipoCargos = tipoCargos;
	}

	/**
	 * @return activo: variable that allows the department to know if the
	 *         company is active in PQRC. Variable used only for business logic.
	 */
	@Transient
	public String getActivo() {
		return activo;
	}

	/**
	 * @param activo
	 *            : variable that allows the department to know if the company
	 *            is active in PQRC. Variable used only for business logic.
	 */
	public void setActivo(String activo) {
		this.activo = activo;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result + id;
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime
				* result
				+ ((predeterminadoCalidad == null) ? 0 : predeterminadoCalidad
						.hashCode());
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
		DepartamentoEmpresa other = (DepartamentoEmpresa) obj;
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
		if (predeterminadoCalidad == null) {
			if (other.predeterminadoCalidad != null)
				return false;
		} else if (!predeterminadoCalidad.equals(other.predeterminadoCalidad))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
