package co.informatix.erp.humanResources.entities;

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
 * This entity represents the types of functions that a user can have
 * 
 * @author Liseth.Jimenez
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "tipo_cargo", schema = "recursos_humanos")
public class ChargeType implements Serializable, Comparable<ChargeType> {

	private Integer id;
	private String name;
	private String functions;
	private Date dateCreation;
	private Date dateEndValidity;
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
	 * @return name: name of charge.
	 */
	@Column(name = "nombre", length = 50, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : name of charge.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return functions: functions that have the type of charge.
	 */
	@Column(name = "funciones")
	public String getFunctions() {
		return functions;
	}

	/**
	 * @param functions
	 *            : functions that have the type of charge.
	 */
	public void setFunctions(String functions) {
		this.functions = functions;
	}

	/**
	 * @return dateCreation: date the record type charge was performed
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param dateCreation
	 *            : date the record type charge was performed
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return dateEndValidity: Validity end date of the type of charge
	 */
	@Column(name = "fecha_fin_vigencia")
	@Temporal(TemporalType.DATE)
	public Date getDateEndValidity() {
		return dateEndValidity;
	}

	/**
	 * @param dateEndValidity
	 *            : Validity end date of the type of charge
	 */
	public void setDateEndValidity(Date dateEndValidity) {
		this.dateEndValidity = dateEndValidity;
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
	public int compareTo(ChargeType chargeType) {
		return name.compareTo(chargeType.getName());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((functions == null) ? 0 : functions.hashCode());
		result = prime * result
				+ ((dateEndValidity == null) ? 0 : dateEndValidity.hashCode());
		result = prime * result
				+ ((dateCreation == null) ? 0 : dateCreation.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
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
		if (!(obj instanceof ChargeType)) {
			return false;
		}
		ChargeType other = (ChargeType) obj;
		if (functions == null) {
			if (other.functions != null) {
				return false;
			}
		} else if (!functions.equals(other.functions)) {
			return false;
		}
		if (dateEndValidity == null) {
			if (other.dateEndValidity != null) {
				return false;
			}
		} else if (!dateEndValidity.equals(other.dateEndValidity)) {
			return false;
		}
		if (dateCreation == null) {
			if (other.dateCreation != null) {
				return false;
			}
		} else if (!dateCreation.equals(other.dateCreation)) {
			return false;
		}
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
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