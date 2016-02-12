package co.informatix.erp.machines.entities;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * Class containing records of insurance
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "insurance", schema = "machines")
public class Insurance implements Serializable {

	private int idInsurance;

	private Double totalCostBudget;
	private Double totalCostActual;

	private String descripcion;

	private Date dateTime;

	private Machines machines;

	/**
	 * Constructor method.
	 */
	public Insurance() {
		this.machines = new Machines();
	}

	/**
	 * @return idInsurance: Identifier insurance
	 */
	@Id
	@Column(name = "idinsurance", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdInsurance() {
		return idInsurance;
	}

	/**
	 * @param idInsurance
	 *            : Identifier insurance
	 */
	public void setIdInsurance(int idInsurance) {
		this.idInsurance = idInsurance;
	}

	/**
	 * @return dateTime: date and time of insurance
	 */
	@Column(name = "date_time")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getDateTime() {
		return dateTime;
	}

	/**
	 * @param dateTime
	 *            : date and time of insurance
	 * 
	 */
	public void setDateTime(Date dateTime) {
		this.dateTime = dateTime;
	}

	/**
	 * @return totalCostBudget: total cost budget
	 */
	@Column(name = "total_cost_budget")
	public Double getTotalCostBudget() {
		return totalCostBudget;
	}

	/**
	 * @param totalCostBudget
	 *            : total cost budget
	 * 
	 */
	public void setTotalCostBudget(Double totalCostBudget) {
		this.totalCostBudget = totalCostBudget;
	}

	/**
	 * @return totalCostActual: actual total cost of the budget
	 */
	@Column(name = "total_cost_actual")
	public Double getTotalCostActual() {
		return totalCostActual;
	}

	/**
	 * @param totalCostActual
	 *            : actual total cost of the budget
	 * */
	public void setTotalCostActual(Double totalCostActual) {
		this.totalCostActual = totalCostActual;
	}

	/**
	 * @return descripcion: description of Insurance
	 */
	@Column(name = "description", length = 250, nullable = false)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : description of Insurance
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return machines: gets the foreign key of insurance and machine
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_machine", referencedColumnName = "idmachine", nullable = false)
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : gets the foreign key of insurance and machine
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((dateTime == null) ? 0 : dateTime.hashCode());
		result = prime * result + idInsurance;
		result = prime * result
				+ ((totalCostBudget == null) ? 0 : totalCostBudget.hashCode());
		result = prime * result
				+ ((totalCostActual == null) ? 0 : totalCostActual.hashCode());
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
		Insurance other = (Insurance) obj;
		if (dateTime == null) {
			if (other.dateTime != null)
				return false;
		} else if (!dateTime.equals(other.dateTime))
			return false;
		if (idInsurance != other.idInsurance)
			return false;
		if (totalCostBudget == null) {
			if (other.totalCostBudget != null)
				return false;
		} else if (!totalCostBudget.equals(other.totalCostBudget))
			return false;
		if (totalCostActual == null) {
			if (other.totalCostActual != null)
				return false;
		} else if (!totalCostActual.equals(other.totalCostActual))
			return false;
		return true;
	}

}
