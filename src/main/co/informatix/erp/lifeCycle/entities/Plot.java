package co.informatix.erp.lifeCycle.entities;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * This class maps the table plot database
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "plot", schema = "life_cycle")
public class Plot implements Serializable, Comparable<Plot> {
	private int idPlot;
	private short positionRow;
	private short mapRow;
	private short statusPlot;
	private String name;
	private String description;
	private Double locationLinkToMap;
	private Double size;
	private Integer numberOfTrees;
	private boolean selected;
	private Section section;
	private CropNames cropNames;

	/**
	 * @return idPlot: plot identifier
	 */
	@Id
	@Column(name = "idplot", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdPlot() {
		return idPlot;
	}

	/**
	 * @param idPlot
	 *            :plot identifier
	 */
	public void setIdPlot(int idPlot) {
		this.idPlot = idPlot;
	}

	/**
	 * @return positionRow: Plot position into the row for paint the plot map.
	 */
	@Column(name = "position_row")
	public short getPositionRow() {
		return positionRow;
	}

	/**
	 * @param positionRow
	 *            : Plot position into the row for paint the plot map.
	 */
	public void setPositionRow(short positionRow) {
		this.positionRow = positionRow;
	}

	/**
	 * @return mapRow: Number of row into the map for plot.
	 */
	@Column(name = "map_row")
	public short getMapRow() {
		return mapRow;
	}

	/**
	 * @param mapRow
	 *            : Number of row into the map for plot.
	 */
	public void setMapRow(short mapRow) {
		this.mapRow = mapRow;
	}

	/**
	 * @return statusPlot: Status of plot into the cycle.
	 */
	@Transient
	public short getStatusPlot() {
		return statusPlot;
	}

	/**
	 * @param statusPlot
	 *            : Status of plot into the cycle.
	 */
	public void setStatusPlot(short statusPlot) {
		this.statusPlot = statusPlot;
	}

	/**
	 * @return name: plot name
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :plot name
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return description: That is the plot description.
	 */
	@Column(name = "description")
	public String getDescription() {
		return description;
	}

	/**
	 * @param description
	 *            :That is the plot description.
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/**
	 * @return locationLinkToMap: link location plot
	 */
	@Column(name = "location_link_to_map")
	public Double getLocationLinkToMap() {
		return locationLinkToMap;
	}

	/**
	 * @param locationLinkToMap
	 *            :link location plot
	 */
	public void setLocationLinkToMap(Double locationLinkToMap) {
		this.locationLinkToMap = locationLinkToMap;
	}

	/**
	 * @return size: plot size
	 */
	@Column(name = "size")
	public Double getSize() {
		return size;
	}

	/**
	 * @param size
	 *            :plot size
	 */
	public void setSize(Double size) {
		this.size = size;
	}

	/**
	 * @return numberOfTrees: Number of trees that have the plot.
	 */
	@Column(name = "number_of_trees")
	public Integer getNumberOfTrees() {
		return numberOfTrees;
	}

	/**
	 * @param numberOfTrees
	 *            :Number of trees that have the plot.
	 */
	public void setNumberOfTrees(Integer numberOfTrees) {
		this.numberOfTrees = numberOfTrees;
	}

	/**
	 * @return selected: object selected from a list of plot
	 */
	@Transient
	public boolean isSelected() {
		return selected;
	}

	/**
	 * @param selected
	 *            : object selected from a list of plot
	 */
	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	/**
	 * @return section: Section to which the plot belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idsection", referencedColumnName = "idsection", nullable = false)
	public Section getSection() {
		return section;
	}

	/**
	 * @param section
	 *            : Section to which the plot belong.
	 */
	public void setSection(Section section) {
		this.section = section;
	}

	/**
	 * @return cropNames: CropNames to which the plot belong.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idcropname", referencedColumnName = "idcropname", nullable = false)
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            : CropNames to which the plot belong.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	@Override
	public int compareTo(Plot o) {
		return this.getName().toUpperCase()
				.compareTo(o.getName().toUpperCase());
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((description == null) ? 0 : description.hashCode());
		result = prime * result + idPlot;
		result = prime
				* result
				+ ((locationLinkToMap == null) ? 0 : locationLinkToMap
						.hashCode());
		result = prime * result + mapRow;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((numberOfTrees == null) ? 0 : numberOfTrees.hashCode());
		result = prime * result + positionRow;
		result = prime * result + ((size == null) ? 0 : size.hashCode());
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
		Plot other = (Plot) obj;
		if (description == null) {
			if (other.description != null)
				return false;
		} else if (!description.equals(other.description))
			return false;
		if (idPlot != other.idPlot)
			return false;
		if (locationLinkToMap == null) {
			if (other.locationLinkToMap != null)
				return false;
		} else if (!locationLinkToMap.equals(other.locationLinkToMap))
			return false;
		if (mapRow != other.mapRow)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (numberOfTrees == null) {
			if (other.numberOfTrees != null)
				return false;
		} else if (!numberOfTrees.equals(other.numberOfTrees))
			return false;
		if (positionRow != other.positionRow)
			return false;
		if (size == null) {
			if (other.size != null)
				return false;
		} else if (!size.equals(other.size))
			return false;
		return true;
	}

}
