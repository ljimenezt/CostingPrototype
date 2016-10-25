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
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;
import co.informatix.erp.informacionBase.entities.TipoDocumento;
import co.informatix.security.entities.Usuario;

/**
 * This class represents identification data, name, location, etc., of those the
 * system.
 * 
 * @author marisol.calderon
 * @modify Luz.Jaimes 09/08/2013
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "persona", schema = "recursos_humanos")
public class Person implements Serializable {

	private int id;
	private String document;
	private String names;
	private String surnames;
	private String gender;
	private String photo;
	private Date birthDate;
	private Integer childrenNumber;
	private String telephone;
	private String address;
	private String email;
	private String fax;
	private String mobile;
	private Date dateCreation;
	private Date dateEndValidity;
	private String userName;
	private String fullName;
	private String responsible;

	private TipoDocumento documentType;
	private Municipio municipalityBirth;
	private Departamento departmentBirth;
	private Pais countryBirth;
	private CivilStatus civilStatus;
	private Municipio municipalityHome;
	private Departamento departmentHome;
	private Pais countryHome;
	private Usuario user;

	/**
	 * Class constructor
	 */
	public Person() {
		this.documentType = new TipoDocumento();
		this.municipalityBirth = new Municipio();
		this.departmentBirth = new Departamento();
		this.countryBirth = new Pais();
		this.municipalityHome = new Municipio();
		this.departmentHome = new Departamento();
		this.countryHome = new Pais();
		this.civilStatus = new CivilStatus();
	}

	/**
	 * @return id: identifier of the person table
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : identifier of the person table
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return document: Document Person
	 */
	@Column(name = "documento", length = 20, nullable = false)
	public String getDocument() {
		return document;
	}

	/**
	 * @param document
	 *            : Document Person
	 */
	public void setDocument(String document) {
		this.document = document;
	}

	/**
	 * @return names: Name of Person
	 */
	@Column(name = "nombres", length = 100, nullable = false)
	public String getNames() {
		return names;
	}

	/**
	 * @param names
	 *            : Name of Person
	 */
	public void setNames(String names) {
		this.names = names;
	}

	/**
	 * @return surnames: The person's surname
	 */
	@Column(name = "apellidos", length = 100, nullable = true)
	public String getSurnames() {
		return surnames;
	}

	/**
	 * @param surnames
	 *            : The person's surname
	 */
	public void setSurnames(String surnames) {
		this.surnames = surnames;
	}

	/**
	 * @return gender: gender of the person.
	 */
	@Column(name = "genero", length = 1, nullable = false)
	public String getGender() {
		return gender;
	}

	/**
	 * @param gender
	 *            : gender of the person.
	 */
	public void setGender(String gender) {
		this.gender = gender;
	}

	/**
	 * @return photo: Picture name of the person
	 */
	@Column(name = "foto", length = 300, nullable = true)
	public String getPhoto() {
		return photo;
	}

	/**
	 * @param photo
	 *            : sets picture name of the person
	 */
	public void setPhoto(String photo) {
		this.photo = photo;
	}

	/**
	 * @return birthDate: date of birth of the person.
	 */
	@Column(name = "fecha_nacimiento", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getBirthDate() {
		return birthDate;
	}

	/**
	 * @param birthDate
	 *            : date of birth of the person.
	 */
	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	/**
	 * @return childrenNumber: number of children a person has.
	 */
	@Column(name = "nro_hijos", nullable = true)
	public Integer getChildrenNumber() {
		return childrenNumber;
	}

	/**
	 * @param childrenNumber
	 *            : number of children a person has.
	 */
	public void setChildrenNumber(Integer childrenNumber) {
		this.childrenNumber = childrenNumber;
	}

	/**
	 * @return telephone: Person Phone
	 */
	@Column(name = "telefono", length = 150, nullable = false)
	public String getTelephone() {
		return telephone;
	}

	/**
	 * @param telephone
	 *            : Person Phone
	 */
	public void setTelephone(String telephone) {
		this.telephone = telephone;
	}

	/**
	 * @return address: Address of the person
	 */
	@Column(name = "direccion", length = 200, nullable = true)
	public String getAddress() {
		return address;
	}

	/**
	 * @param address
	 *            : Address of the person
	 */
	public void setAddress(String address) {
		this.address = address;
	}

	/**
	 * @return email: mail of the person
	 */
	@Column(name = "correo", length = 150, nullable = true)
	public String getEmail() {
		return email;
	}

	/**
	 * @param email
	 *            : mail of the person
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * @return fax: the FAX number of the person
	 */
	@Column(name = "fax", length = 30, nullable = true)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            : the FAX number of the person
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return mobile: mobile phone of the person
	 */
	@Column(name = "movil", length = 10, nullable = true)
	public String getMobile() {
		return mobile;
	}

	/**
	 * @param mobile
	 *            : mobile phone of the person
	 */
	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	/**
	 * @return dateCreation: creation date of the person
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getDateCreation() {
		return dateCreation;
	}

	/**
	 * @param dateCreation
	 *            : creation date of the person
	 */
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}

	/**
	 * @return dateEndValidity: End date of validity of the person in the system
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getDateEndValidity() {
		return dateEndValidity;
	}

	/**
	 * @param dateEndValidity
	 *            : End date of validity of the person in the system
	 */
	public void setDateEndValidity(Date dateEndValidity) {
		this.dateEndValidity = dateEndValidity;
	}

	/**
	 * @return userName: User name session running in action on recording
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name session running in action on recording
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return documentType: document type person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
	public TipoDocumento getDocumentType() {
		return documentType;
	}

	/**
	 * @param documentType
	 *            : document type person.
	 */
	public void setDocumentType(TipoDocumento documentType) {
		this.documentType = documentType;
	}

	/**
	 * @return municipalityBirth: Municipality of birth of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio_nac", referencedColumnName = "id", nullable = false)
	public Municipio getMunicipalityBirth() {
		return municipalityBirth;
	}

	/**
	 * @param municipalityBirth
	 *            : Municipality of birth of the person.
	 */
	public void setMunicipalityBirth(Municipio municipalityBirth) {
		this.municipalityBirth = municipalityBirth;
	}

	/**
	 * @return departmentBirth: department of birth of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento_nac", referencedColumnName = "id", nullable = false)
	public Departamento getDepartmentBirth() {
		return departmentBirth;
	}

	/**
	 * @param departmentBirth
	 *            : department of birth of the person.
	 */
	public void setDepartmentBirth(Departamento departmentBirth) {
		this.departmentBirth = departmentBirth;
	}

	/**
	 * @return countryBirth: country of birth of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais_nac", referencedColumnName = "id", nullable = false)
	public Pais getCountryBirth() {
		return countryBirth;
	}

	/**
	 * @param countryBirth
	 *            : country of birth of the person.
	 */
	public void setCountryBirth(Pais countryBirth) {
		this.countryBirth = countryBirth;
	}

	/**
	 * @return civilStatus: civil status of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_civil_status", referencedColumnName = "id")
	public CivilStatus getCivilStatus() {
		return civilStatus;
	}

	/**
	 * @param civilStatus
	 *            : civil status of the person.
	 */
	public void setCivilStatus(CivilStatus civilStatus) {
		this.civilStatus = civilStatus;
	}

	/**
	 * @return municipalityHome: municipality of residence of the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio_res", referencedColumnName = "id", nullable = false)
	public Municipio getMunicipalityHome() {
		return municipalityHome;
	}

	/**
	 * @param municipalityHome
	 *            : municipality of residence of the person
	 */
	public void setMunicipalityHome(Municipio municipalityHome) {
		this.municipalityHome = municipalityHome;
	}

	/**
	 * @return departmentHome: department of residence of the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento_res", referencedColumnName = "id", nullable = false)
	public Departamento getDepartmentHome() {
		return departmentHome;
	}

	/**
	 * @param departmentHome
	 *            : department of residence of the person
	 */
	public void setDepartmentHome(Departamento departmentHome) {
		this.departmentHome = departmentHome;
	}

	/**
	 * @return countryHome: country of residence of the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais_res", referencedColumnName = "id", nullable = false)
	public Pais getCountryHome() {
		return countryHome;
	}

	/**
	 * @param countryHome
	 *            : country of residence of the person
	 */
	public void setCountryHome(Pais countryHome) {
		this.countryHome = countryHome;
	}

	/**
	 * @return user: user system with the person.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	public Usuario getUser() {
		return user;
	}

	/**
	 * @param user
	 *            : user system with the person.
	 */
	public void setUser(Usuario user) {
		this.user = user;
	}

	/**
	 * @return fullName: full name of the person consists of the names fields.
	 */
	@Transient
	public String getFullName() {
		this.fullName = null;
		StringBuilder fullName = new StringBuilder(this.names == null ? ""
				: this.names);
		if (fullName.length() > 1)
			fullName.append(" ");
		fullName.append(this.surnames == null ? "" : this.surnames);
		if (fullName.length() > 1) {
			this.fullName = fullName.toString();
		}
		return this.fullName;
	}

	/**
	 * @param fullName
	 *            : full name of the person consists of the names fields.
	 */
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}

	/**
	 * @return responsible: Field representing whether or not the person
	 *         selected for the activity is responsible, is used for the
	 *         management of the activities temporarily while it is stored in
	 *         the persona_actividad table.
	 */
	@Transient
	public String getResponsible() {
		return responsible;
	}

	/**
	 * @param responsible
	 *            : Field representing whether or not the person selected for
	 *            the activity is responsible, is used for the management of the
	 *            activities temporarily while it is stored in the
	 *            persona_actividad table.
	 */
	public void setResponsible(String responsible) {
		this.responsible = responsible;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((surnames == null) ? 0 : surnames.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((address == null) ? 0 : address.hashCode());
		result = prime * result
				+ ((document == null) ? 0 : document.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result
				+ ((dateCreation == null) ? 0 : dateCreation.hashCode());
		result = prime * result
				+ ((dateEndValidity == null) ? 0 : dateEndValidity.hashCode());
		result = prime * result
				+ ((birthDate == null) ? 0 : birthDate.hashCode());
		result = prime * result + ((photo == null) ? 0 : photo.hashCode());
		result = prime * result + ((gender == null) ? 0 : gender.hashCode());
		result = prime * result + id;
		result = prime * result + ((mobile == null) ? 0 : mobile.hashCode());
		result = prime * result + ((names == null) ? 0 : names.hashCode());
		result = prime * result
				+ ((childrenNumber == null) ? 0 : childrenNumber.hashCode());
		result = prime * result
				+ ((telephone == null) ? 0 : telephone.hashCode());
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
		Person other = (Person) obj;
		if (surnames == null) {
			if (other.surnames != null)
				return false;
		} else if (!surnames.equals(other.surnames))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (address == null) {
			if (other.address != null)
				return false;
		} else if (!address.equals(other.address))
			return false;
		if (document == null) {
			if (other.document != null)
				return false;
		} else if (!document.equals(other.document))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (dateCreation == null) {
			if (other.dateCreation != null)
				return false;
		} else if (!dateCreation.equals(other.dateCreation))
			return false;
		if (dateEndValidity == null) {
			if (other.dateEndValidity != null)
				return false;
		} else if (!dateEndValidity.equals(other.dateEndValidity))
			return false;
		if (birthDate == null) {
			if (other.birthDate != null)
				return false;
		} else if (!birthDate.equals(other.birthDate))
			return false;
		if (photo == null) {
			if (other.photo != null)
				return false;
		} else if (!photo.equals(other.photo))
			return false;
		if (gender == null) {
			if (other.gender != null)
				return false;
		} else if (!gender.equals(other.gender))
			return false;
		if (id != other.id)
			return false;
		if (mobile == null) {
			if (other.mobile != null)
				return false;
		} else if (!mobile.equals(other.mobile))
			return false;
		if (names == null) {
			if (other.names != null)
				return false;
		} else if (!names.equals(other.names))
			return false;
		if (childrenNumber == null) {
			if (other.childrenNumber != null)
				return false;
		} else if (!childrenNumber.equals(other.childrenNumber))
			return false;
		if (telephone == null) {
			if (other.telephone != null)
				return false;
		} else if (!telephone.equals(other.telephone))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}