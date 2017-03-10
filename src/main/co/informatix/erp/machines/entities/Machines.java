package co.informatix.erp.machines.entities;

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
import javax.persistence.Transient;

/**
 * Class that contains the records of machines
 * 
 * @author Sergio.Ortiz
 * @modify 16/10/2015 Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "machines", schema = "machines")
public class Machines implements Serializable {

	private double investment;
	private double lifeYears;
	private double residualValue;
	private double depreciation;
	private double horsepower;
	private double kilowatts;
	private double fuelConsumption;
	private Double hydrometer;
	private Double hourmeter;
	private int idMachine;

	private boolean fuel;
	private boolean selection;

	private String name;
	private String serialNumber;

	private Date purchaseDate;
	private Date lastMaintenance;

	private MachineTypes machineTypes;
	private FuelTypes fuelTypes;

	/**
	 * Constructor that initializes the foreign key
	 */
	public Machines() {
		this.machineTypes = new MachineTypes();
		this.fuelTypes = new FuelTypes();
	}

	/**
	 * @return idMachine: machine identifier
	 */
	@Id
	@Column(name = "idmachine", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdMachine() {
		return idMachine;
	}

	/**
	 * @param idMachine
	 *            : machine identifier
	 */
	public void setIdMachine(int idMachine) {
		this.idMachine = idMachine;
	}

	/**
	 * @return machineTypes: gets the foreign key of machines and type of
	 *         machines
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_machine_type", referencedColumnName = "idmachinetype", nullable = false)
	public MachineTypes getMachineTypes() {
		return machineTypes;
	}

	/**
	 * @param machineTypes
	 *            : sets the foreign key of machines and type of machines
	 */
	public void setMachineTypes(MachineTypes machineTypes) {
		this.machineTypes = machineTypes;
	}

	/**
	 * @return fuel: gets fuel of the machine
	 */
	@Column(name = "fuel")
	public boolean isFuel() {
		return fuel;
	}

	/**
	 * @param fuel
	 *            :sets fuel of the machine
	 */
	public void setFuel(boolean fuel) {
		this.fuel = fuel;
	}

	/**
	 * @return selection: true if the object is selected and 'false' if it is
	 *         not selected
	 */
	@Transient
	public boolean isSelection() {
		return selection;
	}

	/**
	 * @param selection
	 *            : true if the object is selected and 'false' if it is not
	 *            selected
	 */
	public void setSelection(boolean selection) {
		this.selection = selection;
	}

	/**
	 * @return name: name of the machine
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :name of the machine
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return serialNumber: gets the serial number of the machine
	 */
	@Column(name = "serial_number", length = 20)
	public String getSerialNumber() {
		return serialNumber;
	}

	/**
	 * @param serialNumber
	 *            : sets the serial number of the machine
	 */
	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	/**
	 * @return purchaseDate: gets the date of purchase
	 */
	@Column(name = "purchase_date")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getPurchaseDate() {
		return purchaseDate;
	}

	/**
	 * @param purchaseDate
	 *            : sets the date of purchase
	 */
	public void setPurchaseDate(Date purchaseDate) {
		this.purchaseDate = purchaseDate;
	}

	/**
	 * @return investment: gets the investment
	 */
	@Column(name = "investment")
	public double getInvestment() {
		return investment;
	}

	/**
	 * @param investment
	 *            : sets the investment
	 */
	public void setInvestment(double investment) {
		this.investment = investment;
	}

	/**
	 * @return lifeYears: gets the lifetime of machine
	 */
	@Column(name = "life_years")
	public double getLifeYears() {
		return lifeYears;
	}

	/**
	 * @param lifeYears
	 *            : sets the lifetime of machine
	 */
	public void setLifeYears(double lifeYears) {
		this.lifeYears = lifeYears;
	}

	/**
	 * @return residualValue: gets the waste value
	 */
	@Column(name = "residual_value")
	public double getResidualValue() {
		return residualValue;
	}

	/**
	 * @param residualValue
	 *            : sets the waste value
	 */
	public void setResidualValue(double residualValue) {
		this.residualValue = residualValue;
	}

	/**
	 * @return depreciation: gets depreciation
	 */
	@Column(name = "depreciation")
	public double getDepreciation() {
		return depreciation;
	}

	/**
	 * @param depreciation
	 *            :sets depreciation
	 */
	public void setDepreciation(double depreciation) {
		this.depreciation = depreciation;
	}

	/**
	 * @return horsepower: get the horsepower
	 */
	@Column(name = "horsepower")
	public double getHorsepower() {
		return horsepower;
	}

	/**
	 * @param horsepower
	 *            :set the horsepower
	 */
	public void setHorsepower(double horsepower) {
		this.horsepower = horsepower;
	}

	/**
	 * @return kilowatts: get the kilowatts
	 */
	@Column(name = "kilowatts")
	public double getKilowatts() {
		return kilowatts;
	}

	/**
	 * @param kilowatts
	 *            : set the kilowatts
	 */
	public void setKilowatts(double kilowatts) {
		this.kilowatts = kilowatts;
	}

	/**
	 * @return fuelConsumption: gets the fuel consumption of the machine
	 */
	@Column(name = "fuel_consumption")
	public double getFuelConsumption() {
		return fuelConsumption;
	}

	/**
	 * @param fuelConsumption
	 *            :sets the fuel consumption of the machine
	 */
	public void setFuelConsumption(double fuelConsumption) {
		this.fuelConsumption = fuelConsumption;
	}

	/**
	 * @return hydrometer: gets the hydrometer of the machine
	 */
	@Column(name = "hydrometer")
	public Double getHydrometer() {
		return hydrometer;
	}

	/**
	 * @param hydrometer
	 *            :sets the hydrometer of the machine
	 */
	public void setHydrometer(Double hydrometer) {
		this.hydrometer = hydrometer;
	}

	/**
	 * @return hourmeter: gets the hourmeter of the machine
	 */
	@Column(name = "hourmeter")
	public Double getHourmeter() {
		return hourmeter;
	}

	/**
	 * @param hourmeter
	 *            :sets the hourmeter of the machine
	 */
	public void setHourmeter(Double hourmeter) {
		this.hourmeter = hourmeter;
	}

	/**
	 * @return lastMaintenance: get the last maintenance
	 */
	@Column(name = "last_maintenance")
	@Temporal(TemporalType.TIMESTAMP)
	public Date getLastMaintenance() {
		return lastMaintenance;
	}

	/**
	 * @param lastMaintenance
	 *            : set the last maintenance
	 */
	public void setLastMaintenance(Date lastMaintenance) {
		this.lastMaintenance = lastMaintenance;
	}

	/**
	 * @return fuelTypes: gets the object fuel types associate a machine
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_fuel_type", referencedColumnName = "idfueltype")
	public FuelTypes getFuelTypes() {
		return fuelTypes;
	}

	/**
	 * @param fuelTypes
	 *            :sets the object fuel types associate a machine
	 */
	public void setFuelTypes(FuelTypes fuelTypes) {
		this.fuelTypes = fuelTypes;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(depreciation);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (fuel ? 1231 : 1237);
		temp = Double.doubleToLongBits(fuelConsumption);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(horsepower);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(hourmeter);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(hydrometer);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + idMachine;
		temp = Double.doubleToLongBits(investment);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		temp = Double.doubleToLongBits(kilowatts);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((lastMaintenance == null) ? 0 : lastMaintenance.hashCode());
		temp = Double.doubleToLongBits(lifeYears);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((purchaseDate == null) ? 0 : purchaseDate.hashCode());
		temp = Double.doubleToLongBits(residualValue);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + (selection ? 1231 : 1237);
		result = prime * result
				+ ((serialNumber == null) ? 0 : serialNumber.hashCode());
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
		Machines other = (Machines) obj;
		if (Double.doubleToLongBits(depreciation) != Double
				.doubleToLongBits(other.depreciation))
			return false;
		if (fuel != other.fuel)
			return false;
		if (Double.doubleToLongBits(fuelConsumption) != Double
				.doubleToLongBits(other.fuelConsumption))
			return false;
		if (Double.doubleToLongBits(horsepower) != Double
				.doubleToLongBits(other.horsepower))
			return false;
		if (Double.doubleToLongBits(hourmeter) != Double
				.doubleToLongBits(other.hourmeter))
			return false;
		if (Double.doubleToLongBits(hydrometer) != Double
				.doubleToLongBits(other.hydrometer))
			return false;
		if (idMachine != other.idMachine)
			return false;
		if (Double.doubleToLongBits(investment) != Double
				.doubleToLongBits(other.investment))
			return false;
		if (Double.doubleToLongBits(kilowatts) != Double
				.doubleToLongBits(other.kilowatts))
			return false;
		if (lastMaintenance == null) {
			if (other.lastMaintenance != null)
				return false;
		} else if (!lastMaintenance.equals(other.lastMaintenance))
			return false;
		if (Double.doubleToLongBits(lifeYears) != Double
				.doubleToLongBits(other.lifeYears))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (purchaseDate == null) {
			if (other.purchaseDate != null)
				return false;
		} else if (!purchaseDate.equals(other.purchaseDate))
			return false;
		if (Double.doubleToLongBits(residualValue) != Double
				.doubleToLongBits(other.residualValue))
			return false;
		if (selection != other.selection)
			return false;
		if (serialNumber == null) {
			if (other.serialNumber != null)
				return false;
		} else if (!serialNumber.equals(other.serialNumber))
			return false;
		return true;
	}

}
