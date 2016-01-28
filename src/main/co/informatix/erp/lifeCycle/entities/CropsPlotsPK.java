package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * That represents the primary key table product movement
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Embeddable
public class CropsPlotsPK implements Serializable {
	private Crops crops;
	private Plot plot;

	/**
	 * @return Crops: associated with the crop plot
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_crop", referencedColumnName = "idcrop", nullable = false)
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : associated with the crop plot
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return Plot: plot associated with the harvest
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_plot", referencedColumnName = "idplot", nullable = false)
	public Plot getPlot() {
		return plot;
	}

	/**
	 * @param plot
	 *            :plot associated with the harvest
	 */
	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((crops == null) ? 0 : crops.hashCode());
		result = prime * result + ((plot == null) ? 0 : plot.hashCode());
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
		CropsPlotsPK other = (CropsPlotsPK) obj;
		if (crops == null) {
			if (other.crops != null)
				return false;
		} else if (!crops.equals(other.crops))
			return false;
		if (plot == null) {
			if (other.plot != null)
				return false;
		} else if (!plot.equals(other.plot))
			return false;
		return true;
	}

}
