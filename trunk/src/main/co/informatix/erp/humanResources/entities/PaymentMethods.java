package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Class containing the record of payment methods
 * 
 * @author Dario.Lopez
 * @modify Sergio.Ortiz
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "payment_methods", schema = "human_resources")
public class PaymentMethods implements Serializable {
	private int idPaymentMethod;
	private String name;
	private String descripcion;

	/**
	 * @return idPaymentMethod: Payment method identifier
	 */
	@Id
	@Column(name = "idpaymentmethod", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdPaymentMethod() {
		return idPaymentMethod;
	}

	/**
	 * @param idPaymentMethod
	 *            : Payment method identifier
	 */
	public void setIdPaymentMethod(int idPaymentMethod) {
		this.idPaymentMethod = idPaymentMethod;
	}

	/**
	 * @return name: Name payment method.
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Name payment method.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return descripcion: description of methods of payment
	 */
	@Column(name = "description", length = 250)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : description of methods of payment
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
		result = prime * result + idPaymentMethod;
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
		PaymentMethods other = (PaymentMethods) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (idPaymentMethod != other.idPaymentMethod)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		return true;
	}

}
