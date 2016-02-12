package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * This class maps the table crosp_plots database
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "crops_plots", schema = "life_cycle")
public class CropsPlots implements Serializable {

	private CropsPlotsPK cropsPlotsPK;

	/**
	 * @return cropsPlotsPK: get the primary key of the table crops_plots
	 */
	@EmbeddedId
	public CropsPlotsPK getCropsPlotsPK() {
		return cropsPlotsPK;
	}

	/**
	 * @param cropsPlotsPK
	 *            :sets the primary key of the table crops_plots
	 */
	public void setCropsPlotsPK(CropsPlotsPK cropsPlotsPK) {
		this.cropsPlotsPK = cropsPlotsPK;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((cropsPlotsPK == null) ? 0 : cropsPlotsPK.hashCode());
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
		CropsPlots other = (CropsPlots) obj;
		if (cropsPlotsPK == null) {
			if (other.cropsPlotsPK != null)
				return false;
		} else if (!cropsPlotsPK.equals(other.cropsPlotsPK))
			return false;
		return true;
	}

}
