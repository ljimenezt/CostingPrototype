package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * This class maps the iva_rate table, which contains the information of the tax
 * rate of the materials.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "iva_rate", schema = "general")
public class IvaRate implements Serializable {

	private int idIva;
	private String name;
	private double rate;

	/**
	 * @return idIva: iva rate Identifier.
	 */
	@Id
	@Column(name = "id_iva", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdIva() {
		return idIva;
	}

	/**
	 * @param idIva
	 *            :iva rate Identifier.
	 */
	public void setIdIva(int idIva) {
		this.idIva = idIva;
	}

	/**
	 * @return name : iva rate name.
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :iva rate name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return rate : iva rate.
	 */
	@Column(name = "rate", nullable = false)
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            :iva rate.
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idIva;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		long temp;
		temp = Double.doubleToLongBits(rate);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		IvaRate other = (IvaRate) obj;
		if (idIva != other.idIva)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (Double.doubleToLongBits(rate) != Double
				.doubleToLongBits(other.rate))
			return false;
		return true;
	}

}
