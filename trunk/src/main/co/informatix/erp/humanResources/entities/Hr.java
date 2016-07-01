package co.informatix.erp.humanResources.entities;

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

import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;

/**
 * Class containing the record of human resources (HR) and the relationship with
 * the Hr rates and methods of payment
 * 
 * @author Dario.Lopez
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "hr", schema = "human_resources")
public class Hr implements Serializable {

	private int idHr;
	private double annualWage;
	private double hoursPerDay;
	private double hourCost;
	private Double hourCostOvertime;
	private Integer totalNumbersDays;

	private Date birthDate;
	private Date centralFacilitiesAccessStartDate;
	private Date centralFacilitiesAccessEndDate;
	private Boolean maternityBreastFeeding;
	private Boolean laborRights;
	private String name;
	private String familyName;
	private String genero;
	private String direccion;
	private String telefono;
	private String correo;
	private String fax;
	private String movil;
	private String foto;
	private String userName;
	private boolean seleccionado;
	private boolean certificado;

	private HrTypes hrTypes;
	private PaymentMethods paymentMethods;
	private CivilStatus civilStatus;
	private Pais paisNacimiento;
	private Departamento departamentoNacimiento;
	private Municipio municipioNacimiento;
	private Pais paisResidencia;
	private Departamento departamentoResidencia;
	private Municipio municipioResidencia;

	/**
	 * Constructor that initializes the foreign key
	 * 
	 * @modified 30/04/2015 Cristhian.Pico
	 * 
	 */
	public Hr() {
		this.hrTypes = new HrTypes();
		this.paymentMethods = new PaymentMethods();
		this.civilStatus = new CivilStatus();
		this.paisNacimiento = new Pais();
		this.departamentoNacimiento = new Departamento();
		this.municipioNacimiento = new Municipio();
		this.paisResidencia = new Pais();
		this.departamentoResidencia = new Departamento();
		this.municipioResidencia = new Municipio();
	}

	/**
	 * @return idHr: Human resource identifier
	 */
	@Id
	@Column(name = "idhr", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdHr() {
		return idHr;
	}

	/**
	 * @param idHr
	 *            : Human resource identifier
	 */
	public void setIdHr(int idHr) {
		this.idHr = idHr;
	}

	/**
	 * @return name: Human resource name.
	 */
	@Column(name = "name", length = 100, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            : Human resource name.
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return annualWage: Annual salary of human resources.
	 */
	@Column(name = "annual_wage")
	public double getAnnualWage() {
		return annualWage;
	}

	/**
	 * @param annualWage
	 *            : Annual salary of human resources.
	 */
	public void setAnnualWage(double annualWage) {
		this.annualWage = annualWage;
	}

	/**
	 * @return hoursPerDay: value indicating the number of hours per day of HR
	 */
	@Column(name = "hours_per_day")
	public double getHoursPerDay() {
		return hoursPerDay;
	}

	/**
	 * @param hoursPerDay
	 *            : value indicating the number of hours per day of HR
	 */
	public void setHoursPerDay(double hoursPerDay) {
		this.hoursPerDay = hoursPerDay;
	}

	/**
	 * @return totalNumbersDays: value indicating the total number of days of
	 *         human resource
	 */
	@Column(name = "total_number_days")
	public Integer getTotalNumbersDays() {
		return totalNumbersDays;
	}

	/**
	 * @param totalNumbersDays
	 *            : value indicating the total number of days of human resource
	 */
	public void setTotalNumbersDays(Integer totalNumbersDays) {
		this.totalNumbersDays = totalNumbersDays;
	}

	/**
	 * @return hourCost: value indicating the cost per hour of human resources
	 */
	@Column(name = "hour_cost")
	public double getHourCost() {
		return hourCost;
	}

	/**
	 * @param hourCost
	 *            : value indicating the cost per hour of human resources
	 */
	public void setHourCost(double hourCost) {
		this.hourCost = hourCost;
	}

	/**
	 * @return hourCostOvertime: Hour per cost time
	 */
	@Column(name = "hour_cost_overtime")
	public Double getHourCostOvertime() {
		return hourCostOvertime;
	}

	/**
	 * @param hourCostOvertime
	 *            : Hour per cost time
	 */
	public void setHourCostOvertime(Double hourCostOvertime) {
		this.hourCostOvertime = hourCostOvertime;
	}

	/**
	 * @return birthDate: date of birth of human resources
	 */
	@Column(name = "birth_date", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            : date of birth of human resources
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return centralFacilitiesAccessStartDate: date of access Central
	 *         facilities
	 */
	@Column(name = "central_facilities_access_start_date")
	@Temporal(TemporalType.DATE)
	public Date getCentralFacilitiesAccessStartDate() {
		return centralFacilitiesAccessStartDate;
	}

	/**
	 * @param centralFacilitiesAccessStartDate
	 *            : date of access to the central facility
	 */
	public void setCentralFacilitiesAccessStartDate(
			Date centralFacilitiesAccessStartDate) {
		this.centralFacilitiesAccessStartDate = centralFacilitiesAccessStartDate;
	}

	/**
	 * @return centralFacilitiesAccessEndDate: end date of access to the central
	 *         facility
	 */
	@Column(name = "central_facilities_access_end_date")
	@Temporal(TemporalType.DATE)
	public Date getCentralFacilitiesAccessEndDate() {
		return centralFacilitiesAccessEndDate;
	}

	/**
	 * @param centralFacilitiesAccessEndDate
	 *            : end date of access to the central facility
	 */
	public void setCentralFacilitiesAccessEndDate(
			Date centralFacilitiesAccessEndDate) {
		this.centralFacilitiesAccessEndDate = centralFacilitiesAccessEndDate;
	}

	/**
	 * @return maternityBreastFeeding: HR maternity leave
	 */
	@Column(name = "maternity_breast_feeding")
	public Boolean getMaternityBreastFeeding() {
		return maternityBreastFeeding;
	}

	/**
	 * @param maternityBreastFeeding
	 *            : HR maternity leave
	 */
	public void setMaternityBreastFeeding(Boolean maternityBreastFeeding) {
		this.maternityBreastFeeding = maternityBreastFeeding;
	}

	/**
	 * @return laborRights: HR labor rights
	 */
	@Column(name = "labor_rights")
	public Boolean getLaborRights() {
		return laborRights;
	}

	/**
	 * @param laborRights
	 *            : HR labor rights
	 */
	public void setLaborRights(Boolean laborRights) {
		this.laborRights = laborRights;
	}

	/**
	 * @return familyName: HR family name
	 */
	@Column(name = "family_name", length = 100, nullable = false)
	public String getFamilyName() {
		return familyName;
	}

	/**
	 * @param familyName
	 *            : HR family name
	 */
	public void setFamilyName(String familyName) {
		this.familyName = familyName;
	}

	/**
	 * 
	 * @return genero: Gender HR
	 */
	@Column(name = "genero", length = 1)
	public String getGenero() {
		return genero;
	}

	/**
	 * 
	 * @param genero
	 *            : Gender HR
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * @return direccion: You obtain the address of the HR.
	 */
	@Column(name = "direccion", length = 200)
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            : You obtain the address of the HR.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return telefono: Phone gets HR.
	 */
	@Column(name = "telefono", length = 150)
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            : Phone gets HR.
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return correo: HR gets the mail.
	 */
	@Column(name = "correo", length = 150)
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo
	 *            : HR gets the mail.
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return fax: get the fax HR.
	 */
	@Column(name = "fax", length = 30)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            : get the fax HR.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return movil: Mobile phone HR.
	 */
	@Column(name = "movil", length = 10)
	public String getMovil() {
		return movil;
	}

	/**
	 * @param movil
	 *            : Mobile phone HR.
	 */
	public void setMovil(String movil) {
		this.movil = movil;
	}

	/**
	 * @return foto: photo HR.
	 */
	@Column(name = "foto", length = 300, nullable = true)
	public String getFoto() {
		return foto;
	}

	/**
	 * @param foto
	 *            : photo HR.
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * @return userName: User name
	 */
	@Column(name = "user_name", length = 50)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return seleccionado: true if the object is selected and 'false' if it is
	 *         not selected
	 */
	@Transient
	public boolean isSeleccionado() {
		return seleccionado;
	}

	/**
	 * @param seleccionado
	 *            : true if the object is selected and 'false' if it is not
	 *            selected
	 */
	public void setSeleccionado(boolean seleccionado) {
		this.seleccionado = seleccionado;
	}

	/**
	 * @return certificado: if the hr is certified its 'true' 
	 */
	@Transient
	public boolean isCertificado() {
		return certificado;
	}

	/**
	 * @param certificado: if the hr is certified its 'true' 
	 */
	public void setCertificado(boolean certificado) {
		this.certificado = certificado;
	}

	/**
	 * @return hrTypes: Gets the type associated HR
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_hr_type", referencedColumnName = "idhrtype", nullable = false)
	public HrTypes getHrTypes() {
		return hrTypes;
	}

	/**
	 * @param hrTypes
	 *            : Gets the type associated HR
	 */
	public void setHrTypes(HrTypes hrTypes) {
		this.hrTypes = hrTypes;
	}

	/**
	 * @return paymentMethods: Gets the associated payment method
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_payment_method", referencedColumnName = "idpaymentmethod", nullable = false)
	public PaymentMethods getPaymentMethods() {
		return paymentMethods;
	}

	/**
	 * @param paymentMethods
	 *            : Gets the associated payment method
	 */
	public void setPaymentMethods(PaymentMethods paymentMethods) {
		this.paymentMethods = paymentMethods;
	}

	/**
	 * @return civilStatus: civil status of the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_civil_status", referencedColumnName = "id")
	public CivilStatus getCivilStatus() {
		return civilStatus;
	}

	/**
	 * @param civilStatus
	 *            : Civil status of the person
	 */
	public void setCivilStatus(CivilStatus civilStatus) {
		this.civilStatus = civilStatus;
	}

	/**
	 * @return paisNacimiento: Country in which the person was born
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais_nac", referencedColumnName = "id", nullable = false)
	public Pais getPaisNacimiento() {
		return paisNacimiento;
	}

	/**
	 * @param paisNacimiento
	 *            : Country in which the person was born
	 */
	public void setPaisNacimiento(Pais paisNacimiento) {
		this.paisNacimiento = paisNacimiento;
	}

	/**
	 * @return departamentoNacimiento : Department in which the person was born
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento_nac", referencedColumnName = "id")
	public Departamento getDepartamentoNacimiento() {
		return departamentoNacimiento;
	}

	/**
	 * @param departamentoNacimiento
	 *            : Department in which the person was born
	 */
	public void setDepartamentoNacimiento(Departamento departamentoNacimiento) {
		this.departamentoNacimiento = departamentoNacimiento;
	}

	/**
	 * @return municipioNacimiento: Municipality where the person was born.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio_nac", referencedColumnName = "id")
	public Municipio getMunicipioNacimiento() {
		return municipioNacimiento;
	}

	/**
	 * @param municipioNacimiento
	 *            : Municipality where the person was born.
	 */
	public void setMunicipioNacimiento(Municipio municipioNacimiento) {
		this.municipioNacimiento = municipioNacimiento;
	}

	/**
	 * @return paisResidencia: Country in which the person was born
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais_res", referencedColumnName = "id", nullable = false)
	public Pais getPaisResidencia() {
		return paisResidencia;
	}

	/**
	 * @param paisResidencia
	 *            : Country in which the person was born
	 */
	public void setPaisResidencia(Pais paisResidencia) {
		this.paisResidencia = paisResidencia;
	}

	/**
	 * @return departamentoResidencia : Department in which the person was born
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento_res", referencedColumnName = "id")
	public Departamento getDepartamentoResidencia() {
		return departamentoResidencia;
	}

	/**
	 * @param departamentoResidencia
	 *            : Department in which the person was born
	 */
	public void setDepartamentoResidencia(Departamento departamentoResidencia) {
		this.departamentoResidencia = departamentoResidencia;
	}

	/**
	 * @return municipioResidencia: Municipality where the person was born.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio_res", referencedColumnName = "id")
	public Municipio getMunicipioResidencia() {
		return municipioResidencia;
	}

	/**
	 * @param municipioResidencia
	 *            : Municipality where the person was born.
	 */
	public void setMunicipioResidencia(Municipio municipioResidencia) {
		this.municipioResidencia = municipioResidencia;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(annualWage);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result
				+ ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime
				* result
				+ ((centralFacilitiesAccessEndDate == null) ? 0
						: centralFacilitiesAccessEndDate.hashCode());
		result = prime
				* result
				+ ((centralFacilitiesAccessStartDate == null) ? 0
						: centralFacilitiesAccessStartDate.hashCode());
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result
				+ ((familyName == null) ? 0 : familyName.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((foto == null) ? 0 : foto.hashCode());
		result = prime * result + ((genero == null) ? 0 : genero.hashCode());
		temp = Double.doubleToLongBits(hourCost);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime
				* result
				+ ((hourCostOvertime == null) ? 0 : hourCostOvertime.hashCode());
		temp = Double.doubleToLongBits(hoursPerDay);
		result = prime * result + (int) (temp ^ (temp >>> 32));
		result = prime * result + idHr;
		result = prime * result
				+ ((laborRights == null) ? 0 : laborRights.hashCode());
		result = prime
				* result
				+ ((maternityBreastFeeding == null) ? 0
						: maternityBreastFeeding.hashCode());
		result = prime * result + ((movil == null) ? 0 : movil.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
		result = prime
				* result
				+ ((totalNumbersDays == null) ? 0 : totalNumbersDays.hashCode());
		result = prime * result
				+ ((userName == null) ? 0 : userName.hashCode());
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
		Hr other = (Hr) obj;
		if (Double.doubleToLongBits(annualWage) != Double
				.doubleToLongBits(other.annualWage))
			return false;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (centralFacilitiesAccessEndDate == null) {
			if (other.centralFacilitiesAccessEndDate != null)
				return false;
		} else if (!centralFacilitiesAccessEndDate
				.equals(other.centralFacilitiesAccessEndDate))
			return false;
		if (centralFacilitiesAccessStartDate == null) {
			if (other.centralFacilitiesAccessStartDate != null)
				return false;
		} else if (!centralFacilitiesAccessStartDate
				.equals(other.centralFacilitiesAccessStartDate))
			return false;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (familyName == null) {
			if (other.familyName != null)
				return false;
		} else if (!familyName.equals(other.familyName))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (foto == null) {
			if (other.foto != null)
				return false;
		} else if (!foto.equals(other.foto))
			return false;
		if (genero == null) {
			if (other.genero != null)
				return false;
		} else if (!genero.equals(other.genero))
			return false;
		if (Double.doubleToLongBits(hourCost) != Double
				.doubleToLongBits(other.hourCost))
			return false;
		if (hourCostOvertime == null) {
			if (other.hourCostOvertime != null)
				return false;
		} else if (!hourCostOvertime.equals(other.hourCostOvertime))
			return false;
		if (Double.doubleToLongBits(hoursPerDay) != Double
				.doubleToLongBits(other.hoursPerDay))
			return false;
		if (idHr != other.idHr)
			return false;
		if (laborRights == null) {
			if (other.laborRights != null)
				return false;
		} else if (!laborRights.equals(other.laborRights))
			return false;
		if (maternityBreastFeeding == null) {
			if (other.maternityBreastFeeding != null)
				return false;
		} else if (!maternityBreastFeeding.equals(other.maternityBreastFeeding))
			return false;
		if (movil == null) {
			if (other.movil != null)
				return false;
		} else if (!movil.equals(other.movil))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (totalNumbersDays == null) {
			if (other.totalNumbersDays != null)
				return false;
		} else if (!totalNumbersDays.equals(other.totalNumbersDays))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
