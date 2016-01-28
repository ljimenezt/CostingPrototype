package co.informatix.erp.machines.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * This class is created by the need to form a compound key in the table
 * machine_usage
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class MachineUsagePK implements Serializable {

	private Machines machine;
	private Integer year;

	/**
	 * Empty constructor to initialize the primary key of the entity.
	 */
	public MachineUsagePK() {
		this.machine = new Machines();
	}

	/**
	 * @return machine: relationship to form primary key machine usage
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmachine", referencedColumnName = "idmachine", nullable = false)
	public Machines getMachine() {
		return machine;
	}

	/**
	 * @param machine
	 *            :relationship to form primary key machine usage
	 */
	public void setMachine(Machines machine) {
		this.machine = machine;
	}

	/**
	 * @return year: year of the relationship to form primary key machine usage
	 */
	@Column(name = "year", nullable = false)
	public Integer getYear() {
		return year;
	}

	/**
	 * @param year
	 *            :year of the relationship to form primary key machine usage
	 */
	public void setYear(Integer year) {
		this.year = year;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((machine == null) ? 0 : machine.hashCode());
		result = prime * result + ((year == null) ? 0 : year.hashCode());
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
		MachineUsagePK other = (MachineUsagePK) obj;
		if (machine == null) {
			if (other.machine != null)
				return false;
		} else if (!machine.equals(other.machine))
			return false;
		if (year == null) {
			if (other.year != null)
				return false;
		} else if (!year.equals(other.year))
			return false;
		return true;
	}

}
