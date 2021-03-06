package co.informatix.erp.humanResources.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class maps the team table, which contains the information of the teams
 * in the plantations
 * 
 * @author Liseth.Jimenez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "team", schema = "human_resources")
public class Team implements Serializable {

	private short idTeam;
	private short size;
	private String name;
	private String note;
	private boolean selected;
	private int workersAssociated;

	/**
	 * @return idTeam: Team identifier
	 */
	@Id
	@Column(name = "idteam", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public short getIdTeam() {
		return idTeam;
	}

	/**
	 * @param idTeam
	 *            : Team identifier
	 */
	public void setIdTeam(short idTeam) {
		this.idTeam = idTeam;
	}

	/**
	 * @return size: Team size
	 */
	@Column(name = "size", nullable = false)
	public short getSize() {
		return size;
	}

	/**
	 * @param size
	 *            : Team size
	 */
	public void setSize(short size) {
		this.size = size;
	}

	/**
	 * @return name: Team name
	 */
	@Column(name = "name", length = 200, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Team name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return note: Description about team
	 */
	@Column(name = "note", nullable = true)
	public String getNote() {
		return note;
	}

	/**
	 * @param note
	 *            : Description about team
	 */
	public void setNote(String note) {
		this.note = note;
	}

	/**
	 * @return selected: Flag to see if it is selected team, true is selected
	 *         and false is not selected
	 */
	@Transient
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            : Flag to see if it is selected team, true is selected and
	 *            false is not selected
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return workersAssociated: Quantity of workers associated
	 */
	@Transient
	public int getWorkersAssociated() {
		return workersAssociated;
	}

	/**
	 * @param workersAssociated
	 *            : Quantity of workers associated
	 */
	public void setWorkersAssociated(int workersAssociated) {
		this.workersAssociated = workersAssociated;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idTeam;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + ((note == null) ? 0 : note.hashCode());
		result = prime * result + size;
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
		Team other = (Team) obj;
		if (idTeam != other.idTeam)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (note == null) {
			if (other.note != null)
				return false;
		} else if (!note.equals(other.note))
			return false;
		if (size != other.size)
			return false;
		return true;
	}
}