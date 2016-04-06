package co.informatix.erp.warehouse.entities;

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

import co.informatix.erp.humanResources.entities.Hr;

/**
 * This class maps the materials table, which contains materials information.
 * 
 * @author Andres.Gomez
 * 
 */
@Entity
@Table(name = "materials", schema = "warehouse")
@SuppressWarnings("serial")
public class Materials implements Serializable {

	private int idMaterial;

	private short presentation;

	private Integer waitingTimeHarvest;
	private Integer waitingTimePeople;

	private String name;
	private String productTradeBrandName;
	private String applicationMethod;
	private String maxResidueLevels;
	private String firstAidInfo;
	private String activeIngredients;
	private String mineralChemicalContent;

	private double quantityHectarPlot;

	private boolean dangerous;
	private boolean euApproved;
	private boolean usaApproved;
	private boolean japanApproved;
	private boolean classified1a;
	private boolean classified1b;
	private boolean pops;
	private boolean includedUnep;
	private boolean includedPan;
	private boolean obsolete;
	private boolean fao;
	private boolean organic;

	private MaterialsType materialType;
	private MeasurementUnits measurementUnits;
	private TypeOfManagement typeOfManagement;
	private Hr responsable;

	/**
	 * Constructor that initializes the foreign key
	 * 
	 */
	public Materials() {
		this.materialType = new MaterialsType();
		this.measurementUnits = new MeasurementUnits();
		this.typeOfManagement = new TypeOfManagement();
		this.responsable = new Hr();

	}

	/**
	 * @return idMaterial: Identifier material.
	 */
	@Id
	@Column(name = "idmaterial", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdMaterial() {
		return idMaterial;
	}

	/**
	 * @param idMaterial
	 *            :Identifier material.
	 */
	public void setIdMaterial(int idMaterial) {
		this.idMaterial = idMaterial;
	}

	/**
	 * Get the small integer for a presentation.
	 * 
	 * @return A small integer.
	 */
	@Column(name = "presentation", nullable = false)
	public short getPresentation() {
		return presentation;
	}

	/**
	 * Set the small integer for a presentation.
	 * 
	 * @param presentation
	 *            : A small integer.
	 */
	public void setPresentation(short presentation) {
		this.presentation = presentation;
	}

	/**
	 * @return waitingTimeHarvest: Sets the timeout harvest.
	 */
	@Column(name = "waiting_time_harvest")
	public Integer getWaitingTimeHarvest() {
		return waitingTimeHarvest;
	}

	/**
	 * @param waitingTimeHarvest
	 *            :Gets the timeout harvest.
	 */
	public void setWaitingTimeHarvest(Integer waitingTimeHarvest) {
		this.waitingTimeHarvest = waitingTimeHarvest;
	}

	/**
	 * @return waitingTimePeople : Set the timeout of the person.
	 */
	@Column(name = "waiting_time_people")
	public Integer getWaitingTimePeople() {
		return waitingTimePeople;
	}

	/**
	 * @param waitingTimePeople
	 *            :Gets the timeout person.
	 */
	public void setWaitingTimePeople(Integer waitingTimePeople) {
		this.waitingTimePeople = waitingTimePeople;
	}

	/**
	 * @return name : Gets the name of the material.
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :Sets the name of the material.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return productTradeBrandName: Gets the trade of branded products.
	 */
	@Column(name = "product_trade_brand_name")
	public String getProductTradeBrandName() {
		return productTradeBrandName;
	}

	/**
	 * @param productTradeBrandName
	 *            :Sets the trade of branded products.
	 */
	public void setProductTradeBrandName(String productTradeBrandName) {
		this.productTradeBrandName = productTradeBrandName;
	}

	/**
	 * @return applicationMethod: Gets the method of application.
	 */
	@Column(name = "application_method")
	public String getApplicationMethod() {
		return applicationMethod;
	}

	/**
	 * @param applicationMethod
	 *            :Sets the method of application.
	 */
	public void setApplicationMethod(String applicationMethod) {
		this.applicationMethod = applicationMethod;
	}

	/**
	 * @return maxResidueLevels: Gets the maximum residue levels.
	 */
	@Column(name = "max_residue_levels")
	public String getMaxResidueLevels() {
		return maxResidueLevels;
	}

	/**
	 * @param maxResidueLevels
	 *            :Set maximum residue levels.
	 */
	public void setMaxResidueLevels(String maxResidueLevels) {
		this.maxResidueLevels = maxResidueLevels;
	}

	/**
	 * @return firstAidInfo: Gets the first aid information.
	 */
	@Column(name = "first_aid_info")
	public String getFirstAidInfo() {
		return firstAidInfo;
	}

	/**
	 * @param firstAidInfo
	 *            : Sets the first aid information.
	 */
	public void setFirstAidInfo(String firstAidInfo) {
		this.firstAidInfo = firstAidInfo;
	}

	/**
	 * @return activeIngredients: Gets the information of the active
	 *         ingredients.
	 */
	@Column(name = "active_ingredients", length = 100)
	public String getActiveIngredients() {
		return activeIngredients;
	}

	/**
	 * @param activeIngredients
	 *            : Sets the information of the active ingredients.
	 */
	public void setActiveIngredients(String activeIngredients) {
		this.activeIngredients = activeIngredients;
	}

	/**
	 * @return mineralChemicalContent: Gets the information from the mineral
	 *         chemical content.
	 */
	@Column(name = "mineral_chemical_content", length = 100)
	public String getMineralChemicalContent() {
		return mineralChemicalContent;
	}

	/**
	 * @param mineralChemicalContent
	 *            : Sets mineral chemical information content.
	 */
	public void setMineralChemicalContent(String mineralChemicalContent) {
		this.mineralChemicalContent = mineralChemicalContent;
	}

	/**
	 * @return quantityHectarPlot: Value of the amount of a plot hectare.
	 */
	@Column(name = "quantity_hectar_plot")
	public double getQuantityHectarPlot() {
		return quantityHectarPlot;
	}

	/**
	 * @param quantityHectarPlot
	 *            :Value of the amount of a plot hectare.
	 */
	public void setQuantityHectarPlot(double quantityHectarPlot) {
		this.quantityHectarPlot = quantityHectarPlot;
	}

	/**
	 * @return dangerous : Value indicating whether the material is dangerous.
	 */
	@Column(name = "dangerous")
	public boolean isDangerous() {
		return dangerous;
	}

	/**
	 * @param dangerous
	 *            :Value indicating whether the material is dangerous.
	 */
	public void setDangerous(boolean dangerous) {
		this.dangerous = dangerous;
	}

	/**
	 * @return euApproved : Value indicating approval in Europe.
	 */
	@Column(name = "eu_approved")
	public boolean isEuApproved() {
		return euApproved;
	}

	/**
	 * @param euApproved
	 *            :Value indicating approval in Europe.
	 */
	public void setEuApproved(boolean euApproved) {
		this.euApproved = euApproved;
	}

	/**
	 * @return usaApproved :Value indicating approval in USA.
	 */
	@Column(name = "usa_approved")
	public boolean isUsaApproved() {
		return usaApproved;
	}

	/**
	 * @param usaApproved
	 *            :Value indicating approval in USA.
	 */
	public void setUsaApproved(boolean usaApproved) {
		this.usaApproved = usaApproved;
	}

	/**
	 * @return japanApproved: Value indicating approval in Japan.
	 */
	@Column(name = "japan_approved")
	public boolean isJapanApproved() {
		return japanApproved;
	}

	/**
	 * @param japanApproved
	 *            :Value indicating approval in Japan.
	 */
	public void setJapanApproved(boolean japanApproved) {
		this.japanApproved = japanApproved;
	}

	/**
	 * @return classified1a: Value indicating that the classification has 1a.
	 */
	@Column(name = "classified_1a")
	public boolean isClassified1a() {
		return classified1a;
	}

	/**
	 * @param classified1a
	 *            :Value indicating that the classification has 1a.
	 */
	public void setClassified1a(boolean classified1a) {
		this.classified1a = classified1a;
	}

	/**
	 * @return classified1b: Value indicating that the classification has 1b.
	 */
	@Column(name = "classified_1b")
	public boolean isClassified1b() {
		return classified1b;
	}

	/**
	 * @param classified1b
	 *            :Value indicating that the classification has 1b.
	 */
	public void setClassified1b(boolean classified1b) {
		this.classified1b = classified1b;
	}

	/**
	 * @return pops: Value indicating whether has emerged.
	 */
	@Column(name = "pops")
	public boolean isPops() {
		return pops;
	}

	/**
	 * @param pops
	 *            :Value indicating whether has emerged.
	 */
	public void setPops(boolean pops) {
		this.pops = pops;
	}

	/**
	 * @return includedUnep: Value indicating whether includes Unep.
	 */
	@Column(name = "included_unep")
	public boolean isIncludedUnep() {
		return includedUnep;
	}

	/**
	 * @param includedUnep
	 *            : Value indicating whether includes Unep.
	 */
	public void setIncludedUnep(boolean includedUnep) {
		this.includedUnep = includedUnep;
	}

	/**
	 * @return includedPan: Value indicating whether includes Pan.
	 */
	@Column(name = "included_pan")
	public boolean isIncludedPan() {
		return includedPan;
	}

	/**
	 * @param includedPan
	 *            :Value indicating whether includes Pan.
	 */
	public void setIncludedPan(boolean includedPan) {
		this.includedPan = includedPan;
	}

	/**
	 * @return obsolete: Value indicating whether the material is obsolete.
	 */
	@Column(name = "obsolete")
	public boolean isObsolete() {
		return obsolete;
	}

	/**
	 * @param obsolete
	 *            :Value indicating whether the material is obsolete.
	 */
	public void setObsolete(boolean obsolete) {
		this.obsolete = obsolete;
	}

	/**
	 * @return fao: Value indicating whether the material has FAO.
	 */
	@Column(name = "fao")
	public boolean isFao() {
		return fao;
	}

	/**
	 * @param fao
	 *            :Value indicating whether the material has FAO.
	 */
	public void setFao(boolean fao) {
		this.fao = fao;
	}

	/**
	 * @return organic: Value indicating whether the material is organic.
	 */
	@Column(name = "organic")
	public boolean isOrganic() {
		return organic;
	}

	/**
	 * @param organic
	 *            :Value indicating whether the material is organic.
	 */
	public void setOrganic(boolean organic) {
		this.organic = organic;
	}

	/**
	 * @return materialType :Gets the type of material for the material.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_material_type", referencedColumnName = "idmaterialtype", nullable = false)
	public MaterialsType getMaterialType() {
		return materialType;
	}

	/**
	 * @param materialType
	 *            :Set the type of material for the material.
	 */
	public void setMaterialType(MaterialsType materialType) {
		this.materialType = materialType;
	}

	/**
	 * @return measurementUnits: Gets the type of unit of measure of the
	 *         material.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_measurement_units", referencedColumnName = "idmeasurementunits", nullable = false)
	public MeasurementUnits getMeasurementUnits() {
		return measurementUnits;
	}

	/**
	 * @param measurementUnits
	 *            :Set the type of unit of measure of the material.
	 */
	public void setMeasurementUnits(MeasurementUnits measurementUnits) {
		this.measurementUnits = measurementUnits;
	}

	/**
	 * @return typeOfManagement: Gets the type of material management.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_type_of_management", referencedColumnName = "idtypeofmanagement", nullable = false)
	public TypeOfManagement getTypeOfManagement() {
		return typeOfManagement;
	}

	/**
	 * @param typeOfManagement
	 *            :Sets the type of material management.
	 */
	public void setTypeOfManagement(TypeOfManagement typeOfManagement) {
		this.typeOfManagement = typeOfManagement;
	}

	/**
	 * @return responsable: Gets responsible.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_responsible", referencedColumnName = "idhr", nullable = false)
	public Hr getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable
	 *            : Sets responsible.
	 */
	public void setResponsable(Hr responsable) {
		this.responsable = responsable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((activeIngredients == null) ? 0 : activeIngredients
						.hashCode());
		result = prime
				* result
				+ ((applicationMethod == null) ? 0 : applicationMethod
						.hashCode());
		result = prime * result + (classified1a ? 1231 : 1237);
		result = prime * result + (classified1b ? 1231 : 1237);
		result = prime * result + (dangerous ? 1231 : 1237);
		result = prime * result + (euApproved ? 1231 : 1237);
		result = prime * result + (fao ? 1231 : 1237);
		result = prime * result
				+ ((firstAidInfo == null) ? 0 : firstAidInfo.hashCode());
		result = prime * result + idMaterial;
		result = prime * result + (includedPan ? 1231 : 1237);
		result = prime * result + (includedUnep ? 1231 : 1237);
		result = prime * result + (japanApproved ? 1231 : 1237);
		result = prime
				* result
				+ ((maxResidueLevels == null) ? 0 : maxResidueLevels.hashCode());
		result = prime
				* result
				+ ((mineralChemicalContent == null) ? 0
						: mineralChemicalContent.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + (obsolete ? 1231 : 1237);
		result = prime * result + (organic ? 1231 : 1237);
		result = prime * result + (pops ? 1231 : 1237);
		result = prime * result + presentation;
		result = prime
				* result
				+ ((productTradeBrandName == null) ? 0 : productTradeBrandName
						.hashCode());
		long temp;
		temp = Double.doubleToLongBits(quantityHectarPlot);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (usaApproved ? 1231 : 1237);
		result = prime
				* result
				+ ((waitingTimeHarvest == null) ? 0 : waitingTimeHarvest
						.hashCode());
		result = prime
				* result
				+ ((waitingTimePeople == null) ? 0 : waitingTimePeople
						.hashCode());
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
		Materials other = (Materials) obj;
		if (activeIngredients == null) {
			if (other.activeIngredients != null)
				return false;
		} else if (!activeIngredients.equals(other.activeIngredients))
			return false;
		if (applicationMethod == null) {
			if (other.applicationMethod != null)
				return false;
		} else if (!applicationMethod.equals(other.applicationMethod))
			return false;
		if (classified1a != other.classified1a)
			return false;
		if (classified1b != other.classified1b)
			return false;
		if (dangerous != other.dangerous)
			return false;
		if (euApproved != other.euApproved)
			return false;
		if (fao != other.fao)
			return false;
		if (firstAidInfo == null) {
			if (other.firstAidInfo != null)
				return false;
		} else if (!firstAidInfo.equals(other.firstAidInfo))
			return false;
		if (idMaterial != other.idMaterial)
			return false;
		if (includedPan != other.includedPan)
			return false;
		if (includedUnep != other.includedUnep)
			return false;
		if (japanApproved != other.japanApproved)
			return false;
		if (maxResidueLevels == null) {
			if (other.maxResidueLevels != null)
				return false;
		} else if (!maxResidueLevels.equals(other.maxResidueLevels))
			return false;
		if (mineralChemicalContent == null) {
			if (other.mineralChemicalContent != null)
				return false;
		} else if (!mineralChemicalContent.equals(other.mineralChemicalContent))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (obsolete != other.obsolete)
			return false;
		if (organic != other.organic)
			return false;
		if (pops != other.pops)
			return false;
		if (presentation != other.presentation)
			return false;
		if (productTradeBrandName == null) {
			if (other.productTradeBrandName != null)
				return false;
		} else if (!productTradeBrandName.equals(other.productTradeBrandName))
			return false;
		if (Double.doubleToLongBits(quantityHectarPlot) != Double
				.doubleToLongBits(other.quantityHectarPlot))
			return false;
		if (usaApproved != other.usaApproved)
			return false;
		if (waitingTimeHarvest == null) {
			if (other.waitingTimeHarvest != null)
				return false;
		} else if (!waitingTimeHarvest.equals(other.waitingTimeHarvest))
			return false;
		if (waitingTimePeople == null) {
			if (other.waitingTimePeople != null)
				return false;
		} else if (!waitingTimePeople.equals(other.waitingTimePeople))
			return false;
		return true;
	}

}