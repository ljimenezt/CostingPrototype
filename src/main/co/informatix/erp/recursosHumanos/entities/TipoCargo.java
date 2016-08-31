package co.informatix.erp.recursosHumanos.entities;

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
 * 
 * This entity represents the types of functions that a user can have
 * 
 * @author Liseth.Jimenez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tipo_cargo", schema = "recursos_humanos")
public class TipoCargo implements Serializable, Comparable<TipoCargo> {

	private Integer id;
	private String nombre;
	private String funciones;
	private Date fechaRegistro;
	private Date fechaFinVigencia;
	private String userName;

	/**
	 * @return id: charge type identifier, primary key of the table.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            : charge type identifier, primary key of the table.
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return nombre: name of charge.
	 */
	@Column(name = "nombre", length = 50, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : name of charge.
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return funciones: functions that have the type of charge.
	 */
	@Column(name = "funciones")
	public String getFunciones() {
		return funciones;
	}

	/**
	 * @param funciones
	 *            : functions that have the type of charge.
	 */
	public void setFunciones(String funciones) {
		this.funciones = funciones;
	}

	/**
	 * @return fechaRegistro: date the record type charge was performed
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro
	 *            : date the record type charge was performed
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return fechaFinVigencia: Validity end date of the type of charge
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : Validity end date of the type of charge
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

	@Override
	public int compareTo(TipoCargo tipoCargo) {
		return nombre.compareTo(tipoCargo.getNombre());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((funciones == null) ? 0 : funciones.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result
				+ ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (!(obj instanceof TipoCargo)) {
			return false;
		}
		TipoCargo other = (TipoCargo) obj;
		if (funciones == null) {
			if (other.funciones != null) {
				return false;
			}
		} else if (!funciones.equals(other.funciones)) {
			return false;
		}
		if (fechaFinVigencia == null) {
			if (other.fechaFinVigencia != null) {
				return false;
			}
		} else if (!fechaFinVigencia.equals(other.fechaFinVigencia)) {
			return false;
		}
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null) {
				return false;
			}
		} else if (!fechaRegistro.equals(other.fechaRegistro)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (nombre == null) {
			if (other.nombre != null) {
				return false;
			}
		} else if (!nombre.equals(other.nombre)) {
			return false;
		}
		if (userName == null) {
			if (other.userName != null) {
				return false;
			}
		} else if (!userName.equals(other.userName)) {
			return false;
		}
		return true;
	}
}
