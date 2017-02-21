package co.informatix.erp.diesel.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This is the entity that is responsible for mapping the zone.
 * 
 * @author Fabian.Diaz
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "zone", schema = "diesel")
public class Zone implements Serializable {

	private int id;
	private String name;

	/**
	 * @return id: zone identify
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : zone identify
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return name: name of zone
	 */
	@Column(name = "name", nullable = false, length=100)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : name of zone
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + id;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		Zone other = (Zone) obj;
		if (id != other.id)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
