package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class maps the activity_names table, which contains information on the
 * names of the activities.
 * 
 * @author Marcela.Chaparro
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "activity_names", schema = "life_cycle")
public class ActivityNames implements Serializable {

	private int idActivityName;
	private String activityName;
	private String description;
	private Boolean cycle;
	private Boolean harvest;
	private Boolean replanted;
	private boolean selected = false;

	/**
	 * @return idActivityName: Activity names identifier table.
	 */
	@Id
	@Column(name = "idactivityname", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdActivityName() {
		return idActivityName;
	}

	/**
	 * @param idActivityName
	 *            : Activity names identifier table.
	 */
	public void setIdActivityName(int idActivityName) {
		this.idActivityName = idActivityName;
	}

	/**
	 * @return activityName: Name of activity.
	 */
	@Column(name = "activity_name", length = 100, nullable = false)
	public String getActivityName() {
		return activityName;
	}

	/**
	 * @param activityName
	 *            : Name of activity.
	 */
	public void setActivityName(String activityName) {
		this.activityName = activityName;
	}

	/**
	 * @return description: Description of the activity name
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            : Description of the activity name
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return cycle: Its true if the activity name belongs to a cycle
	 */
	@Column(name = "cycle")
	public Boolean getCycle() {
		return cycle;
	}

	/**
	 * @param cycle
	 *            : Its true if the activity name belongs to a cycle
	 */
	public void setCycle(Boolean cycle) {
		this.cycle = cycle;
	}

	/**
	 * @return harvest: Its true if the activity name is used for harvest
	 */
	@Column(name = "harvest")
	public Boolean getHarvest() {
		return harvest;
	}

	/**
	 * @param harvest
	 *            : Its true if the activity name is used for harvest
	 */
	public void setHarvest(Boolean harvest) {
		this.harvest = harvest;
	}

	/**
	 * @return replanted: Its true if the activity name is used for replanted
	 */
	@Column(name = "replanted")
	public Boolean getReplanted() {
		return replanted;
	}

	/**
	 * @param replanted
	 *            : Its true if the activity name is used for replanted
	 */
	public void setReplanted(Boolean replanted) {
		this.replanted = replanted;
	}

	/**
	 * @return selected: To validate whether the selected name on a list of
	 *         activities, used by the management CycleStandardActivities
	 */
	@Transient
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            : To validate whether the selected name on a list of
	 *            activities, used by the management CycleStandardActivities
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activityName == null) ? 0 : activityName.hashCode());
		result = prime * result + ((cycle == null) ? 0 : cycle.hashCode());
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + ((harvest == null) ? 0 : harvest.hashCode());
		result = prime * result + idActivityName;
		result = prime * result
				+ ((replanted == null) ? 0 : replanted.hashCode());
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
		ActivityNames other = (ActivityNames) obj;
		if (activityName == null) {
			if (other.activityName != null)
				return false;
		} else if (!activityName.equals(other.activityName))
			return false;
		if (cycle == null) {
			if (other.cycle != null)
				return false;
		} else if (!cycle.equals(other.cycle))
			return false;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (harvest == null) {
			if (other.harvest != null)
				return false;
		} else if (!harvest.equals(other.harvest))
			return false;
		if (idActivityName != other.idActivityName)
			return false;
		if (replanted == null) {
			if (other.replanted != null)
				return false;
		} else if (!replanted.equals(other.replanted))
			return false;
		return true;
	}
}