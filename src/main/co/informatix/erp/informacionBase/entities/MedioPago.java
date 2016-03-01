package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * This class maps the forma_pago table, which stores information of the
 * payment. Which can be: Cash, check, transfer Electronia, etc.
 * 
 * @author Marcela.Chaparro
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "medio_pago", schema = "general")
public class MedioPago implements Serializable {

	private short id;
	private String nombre;
	private String descripcion;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;

	/**
	 * 
	 * @return id: Forma_pago unique identifier of the table.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public short getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Forma_pago unique identifier of the table.
	 */
	public void setId(short id) {
		this.id = id;
	}

	/**
	 * 
	 * @return nombre: Name payment.
	 */
	@Column(name = "nombre", length = 100, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : Name payment.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * 
	 * @return descripcion: Description of the method of payment.
	 */
	@Column(name = "descripcion", length = 200)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : Description of the method of payment.
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
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
	 * @param fechaCreacion
	 *            : Creation date of registration.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * 
	 * @return fechaFinVigencia: End date of the term of the payment.
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : End date of the term of the payment.
	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	/**
	 * 
	 * @return userName: User name in session running the action on the record.
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name in session running the action on the record.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
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
		MedioPago other = (MedioPago) obj;
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
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
