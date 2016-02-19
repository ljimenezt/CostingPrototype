package co.informatix.erp.kpi.entities;

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

import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Section;

/**
 * This class maps the bean_index table, which contains bean_index information.
 * 
 * @author Mabell.Boada
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "bean_index", schema = "kpi")
public class BeanIndex implements Serializable {
	private int idBeanIndex;
	private int cycleNumber;
	private double sampleWeight;
	private Section section;
	private Crops crops;
	private Date dateBean;

	/**
	 * @return idBeanIndex: Identifier the beanIndex.
	 */
	@Id
	@Column(name = "idbeanindex", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdBeanIndex() {
		return idBeanIndex;
	}

	/**
	 * @param idBeanIndex
	 *            : Identifier the beanIndex.
	 */
	public void setIdBeanIndex(int idBeanIndex) {
		this.idBeanIndex = idBeanIndex;
	}

	/**
	 * @return cycleNumber: Number of cycle.
	 */
	@Column(name = "cycle_number", nullable = false)
	public int getCycleNumber() {
		return cycleNumber;
	}

	/**
	 * @param cycleNumber
	 *            : Number of cycle.
	 */
	public void setCycleNumber(int cycleNumber) {
		this.cycleNumber = cycleNumber;
	}

	/**
	 * @return sampleWeight: Sample weight.
	 */
	@Column(name = "sample_weight", nullable = false)
	public double getSampleWeight() {
		return sampleWeight;
	}

	/**
	 * @param sampleWeight
	 *            : Sample weight.
	 */
	public void setSampleWeight(double sampleWeight) {
		this.sampleWeight = sampleWeight;
	}

	/**
	 * @return section: Object the section
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_section", referencedColumnName = "idsection", nullable = false)
	public Section getSection() {
		return section;
	}

	/**
	 * @param section
	 *            : Object the section.
	 */
	public void setSection(Section section) {
		this.section = section;
	}

	/**
	 * @return crops: Object of Crops.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_crop", referencedColumnName = "idcrop", nullable = false)
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            : Object of Crops.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return dateBean : date for the bean index
	 */
	@Temporal(TemporalType.DATE)
	@Column(name = "date_bean_index", nullable = false)
	public Date getDateBean() {
		return dateBean;
	}

	/**
	 * @param dateBean
	 *            : date for the bean index
	 */
	public void setDateBean(Date dateBean) {
		this.dateBean = dateBean;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + cycleNumber;
		result = prime * result
				+ ((dateBean == null) ? 0 : dateBean.hashCode());
		result = prime * result + idBeanIndex;
		long temp;
		temp = Double.doubleToLongBits(sampleWeight);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		BeanIndex other = (BeanIndex) obj;
		if (cycleNumber != other.cycleNumber)
			return false;
		if (dateBean == null) {
			if (other.dateBean != null)
				return false;
		} else if (!dateBean.equals(other.dateBean))
			return false;
		if (idBeanIndex != other.idBeanIndex)
			return false;
		if (Double.doubleToLongBits(sampleWeight) != Double
				.doubleToLongBits(other.sampleWeight))
			return false;
		return true;
	}
}
