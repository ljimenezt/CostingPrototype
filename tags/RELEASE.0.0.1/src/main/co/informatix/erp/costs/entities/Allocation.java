package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the allocation table, which contains the information of the
 * assignments.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "allocation", schema = "costs")
public class Allocation implements Serializable {

	private int idAllocation;
	private String name;

	/**
	 * @return idAllocation: Identifier allocation table
	 */
	@Id
	@Column(name = "idallocation", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdAllocation() {
		return idAllocation;
	}

	/**
	 * @param idAllocation
	 *            : Identifier allocation table
	 */
	public void setIdAllocation(int idAllocation) {
		this.idAllocation = idAllocation;
	}

	/**
	 * @return name: Name of the assignment recorded
	 */
	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name of the assignment recorded
	 */
	public void setName(String name) {
		this.name = name;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idAllocation;
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
		Allocation other = (Allocation) obj;
		if (idAllocation != other.idAllocation)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
