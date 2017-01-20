package co.informatix.erp.humanResources.entities;

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

import co.informatix.erp.informacionBase.entities.TypeFood;

/**
 * This class maps the food control table, which contains the information of the
 * food control.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "food_control", schema = "human_resources")
public class FoodControl implements Serializable {

	private int id;
	private int quantity;
	private String other;
	private Date date;

	private Hr hr;
	private TypeFood typeFood;

	/**
	 * @return id: Food control identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            :Type food identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return quantity: Food quantity for worker.
	 */
	@Column(name = "quantity", nullable = false)
	public int getQuantity() {
		return quantity;
	}

	/**
	 * @param quantity
	 *            : Food quantity for worker.
	 */
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	/**
	 * @return date: Date of the food control.
	 */
	@Column(name = "date", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDate() {
		return date;
	}

	/**
	 * @param date
	 *            : Date of the food control.
	 */
	public void setDate(Date date) {
		this.date = date;
	}

	/**
	 * @return other: Description about the food control.
	 */
	@Column(name = "other")
	public String getOther() {
		return other;
	}

	/**
	 * @param other
	 *            : Description about the food control.
	 */
	public void setOther(String other) {
		this.other = other;
	}

	/**
	 * @return hr: Hr to which the foodControl belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hr", referencedColumnName = "idhr")
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Hr to which the foodControl belong.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return typeFood: TypeFood to which the foodControl belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_type_food", referencedColumnName = "id", nullable = false)
	public TypeFood getTypeFood() {
		return typeFood;
	}

	/**
	 * @param typeFood
	 *            : TypeFood to which the foodControl belong.
	 */
	public void setTypeFood(TypeFood typeFood) {
		this.typeFood = typeFood;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((date == null) ? 0 : date.hashCode());
		result = prime * result + id;
		result = prime * result + ((other == null) ? 0 : other.hashCode());
		result = prime * result + quantity;
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
		FoodControl other = (FoodControl) obj;
		if (date == null) {
			if (other.date != null)
				return false;
		} else if (!date.equals(other.date))
			return false;
		if (id != other.id)
			return false;
		if (this.other == null) {
			if (other.other != null)
				return false;
		} else if (!this.other.equals(other.other))
			return false;
		if (quantity != other.quantity)
			return false;
		return true;
	}
}