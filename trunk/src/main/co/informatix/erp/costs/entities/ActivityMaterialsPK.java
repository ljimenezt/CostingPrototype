package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.warehouse.entities.Materials;

/**
 * This class is created to form a compound key in the table activity_materials.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ActivityMaterialsPK implements Serializable {

	private Activities activities;
	private Materials materials;

	/**
	 * @return activities: relationship to form primary key activities.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idactivity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : relationship to form primary key activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return materials: relationship materials to form primary key.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idmaterial", referencedColumnName = "idmaterial", nullable = false)
	public Materials getMaterials() {
		return materials;
	}

	/**
	 * @param materials
	 *            : materials relationship.
	 */
	public void setMaterials(Materials materials) {
		this.materials = materials;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((activities == null) ? 0 : activities.hashCode());
		result = prime * result
				+ ((materials == null) ? 0 : materials.hashCode());
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
		ActivityMaterialsPK other = (ActivityMaterialsPK) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (materials == null) {
			if (other.materials != null)
				return false;
		} else if (!materials.equals(other.materials))
			return false;
		return true;
	}
}
