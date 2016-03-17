package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of Civil Status
 * 
 * @author Mabell.Boada
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "civil_status", schema = "general")
public class CivilStatus implements Serializable {
	private int id;
	private String nombre;
	private String descripcion;

	/**
	 * @return id: Civil status identifier
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Civil status identifier
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return nombre: Name civil status
	 */
	@Column(name = "nombre", length = 100, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : Name civil status
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return descripcion: Description of civil status
	 */
	@Column(name = "descripcion", length = 200)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : Description of civil status
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result + id;
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
		CivilStatus other = (CivilStatus) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (id != other.id)
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
			return false;
		return true;
	}

}
