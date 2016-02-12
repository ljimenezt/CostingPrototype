package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 * This class maps the cost_types table, which contains the information of the
 * types of costs of system costs.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "cost_types", schema = "costs")
public class CostTypes implements Serializable {

	private int idCostType;
	private Allocation allocation;
	private String name;
	private boolean hour;

	/**
	 * @return idCostType: Cost_types identifier of the table
	 */
	@Id
	@Column(name = "idcosttype", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdCostType() {
		return idCostType;
	}

	/**
	 * @param idCostType
	 *            : Cost_types identifier of the table
	 */
	public void setIdCostType(int idCostType) {
		this.idCostType = idCostType;
	}

	/**
	 * @return allocation: Type object allocation
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idallocation", referencedColumnName = "idallocation")
	public Allocation getAllocation() {
		return allocation;
	}

	/**
	 * @param allocation
	 *            : Type object allocation
	 */
	public void setAllocation(Allocation allocation) {
		this.allocation = allocation;
	}

	/**
	 * @return name: Name the type of cost
	 */
	@Column(name = "name", nullable = false, length = 100)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name the type of cost
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return hour: time cost type
	 */
	@Column(name = "hour")
	public boolean getHour() {
		return hour;
	}

	/**
	 * @param hour
	 *            : time cost type
	 */
	public void setHour(boolean hour) {
		this.hour = hour;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (hour ? 1231 : 1237);
		result = prime * result + idCostType;
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
		CostTypes other = (CostTypes) obj;
		if (hour != other.hour)
			return false;
		if (idCostType != other.idCostType)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}