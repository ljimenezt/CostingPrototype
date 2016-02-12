package co.informatix.erp.costs.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.warehouse.entities.Materials;

/**
 * This class is created to form a compound key in the table Consumable Use.
 * 
 * @author Andres.Gomez
 * @modify 30/09/2015 Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class ConsumableUsePK implements Serializable {

	private Activities activities;
	private Machines machines;
	private Materials materials;

	/**
	 * @return activities: relationship activities.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_activity", referencedColumnName = "idactivity", nullable = false)
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : relationship activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return machines: machine relationship.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_machine", referencedColumnName = "idmachine", nullable = false)
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : machine relationship.
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	/**
	 * @return materials:materials relationship.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_material", referencedColumnName = "idmaterial", nullable = false)
	public Materials getMaterials() {
		return materials;
	}

	/**
	 * @param materials
	 *            :materials relationship.
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
				+ ((machines == null) ? 0 : machines.hashCode());
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
		ConsumableUsePK other = (ConsumableUsePK) obj;
		if (activities == null) {
			if (other.activities != null)
				return false;
		} else if (!activities.equals(other.activities))
			return false;
		if (machines == null) {
			if (other.machines != null)
				return false;
		} else if (!machines.equals(other.machines))
			return false;
		if (materials == null) {
			if (other.materials != null)
				return false;
		} else if (!materials.equals(other.materials))
			return false;
		return true;
	}

}
