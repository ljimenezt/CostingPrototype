package co.informatix.erp.recursosHumanos.entities;

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
public class Persona implements Serializable {

	private int id;
	private String documento;
	private String nombres;
	private String apellidos;
	private String genero;
	private String foto;
	private Date fechaNacimiento;
	private Integer nroHijos;
	private String telefono;
	private String direccion;
	private String correo;
	private String fax;
	private String movil;
	private Date fechaCreacion;
	private Date fechaFinVigencia;
	private String userName;
	private String nombreCompleto;
	private String responsable;

	private TipoDocumento tipoDocumento;
	private Municipio municipioNac;
	private Departamento departamentoNac;
	private Pais paisNac;
	private CivilStatus civilStatus;
	private Municipio municipioRes;
	private Departamento departamentoRes;
	private Pais paisRes;
	private Usuario usuario;

	/**
	 * Class constructor
	 */
	public Persona() {
		this.tipoDocumento = new TipoDocumento();
		this.municipioNac = new Municipio();
		this.departamentoNac = new Departamento();
		this.paisNac = new Pais();
		this.municipioRes = new Municipio();
		this.departamentoRes = new Departamento();
		this.paisRes = new Pais();
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
	 * 
	 * @return documento: Document Person
	 */
	@Column(name = "documento", length = 20, nullable = false)
	public String getDocumento() {
		return documento;
	}

	/**
	 * 
	 * @param documento
	 *            : Document Person
	 */
	public void setDocumento(String documento) {
		this.documento = documento;
	}

	/**
	 * 
	 * @return nombre: Name of Person
	 */
	@Column(name = "nombres", length = 100, nullable = false)
	public String getNombres() {
		return nombres;
	}

	/**
	 * 
	 * @param nombre
	 *            : Name of Person
	 */
	public void setNombres(String nombres) {
		this.nombres = nombres;
	}

	/**
	 * 
	 * @return apellido: The person's surname
	 */
	@Column(name = "apellidos", length = 100, nullable = true)
	public String getApellidos() {
		return apellidos;
	}

	/**
	 * 
	 * @param apellido
	 *            : The person's surname
	 */
	public void setApellidos(String apellidos) {
		this.apellidos = apellidos;
	}

	/**
	 * @return genero: gender of the person.
	 */
	@Column(name = "genero", length = 1, nullable = false)
	public String getGenero() {
		return genero;
	}

	/**
	 * @param genero
	 *            : gender of the person.
	 */
	public void setGenero(String genero) {
		this.genero = genero;
	}

	/**
	 * @return foto: Picture name of the person
	 */
	@Column(name = "foto", length = 300, nullable = true)
	public String getFoto() {
		return foto;
	}

	/**
	 * @param foto
	 *            : sets picture name of the person
	 */
	public void setFoto(String foto) {
		this.foto = foto;
	}

	/**
	 * @return fechaNacimiento: date of birth of the person.
	 */
	@Column(name = "fecha_nacimiento", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaNacimiento() {
		return fechaNacimiento;
	}

	/**
	 * @param fechaNacimiento
	 *            : date of birth of the person.
	 */
	public void setFechaNacimiento(Date fechaNacimiento) {
		this.fechaNacimiento = fechaNacimiento;
	}

	/**
	 * @return nroHijos: number of children a person has.
	 */
	@Column(name = "nro_hijos", nullable = true)
	public Integer getNroHijos() {
		return nroHijos;
	}

	/**
	 * @param nroHijos
	 *            : number of children a person has.
	 */
	public void setNroHijos(Integer nroHijos) {
		this.nroHijos = nroHijos;
	}

	/**
	 * 
	 * @return telefono: Person Phone
	 */
	@Column(name = "telefono", length = 150, nullable = false)
	public String getTelefono() {
		return telefono;
	}

	/**
	 * 
	 * @param telefono
	 *            : Person Phone
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * 
	 * @return direccion: Address of the person
	 */
	@Column(name = "direccion", length = 200, nullable = true)
	public String getDireccion() {
		return direccion;
	}

	/**
	 * 
	 * @param direccion
	 *            : Address of the person
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * 
	 * @return correo: mail of the person
	 */
	@Column(name = "correo", length = 150, nullable = true)
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo
	 *            : mail of the person
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
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
	 * @return movil: mobile phone of the person
	 */
	@Column(name = "movil", length = 10, nullable = true)
	public String getMovil() {
		return movil;
	}

	/**
	 * @param movil
	 *            : mobile phone of the person
	 */
	public void setMovil(String movil) {
		this.movil = movil;
	}

	/**
	 * @return fechaRegistro: creation date of the person
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaRegistro
	 *            : creation date of the person
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return fechaFinVigencia: End date of validity of the person in the
	 *         system
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : End date of validity of the person in the system
	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
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
	 * @return tipoDocumento: document type person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_documento", referencedColumnName = "id", nullable = false)
	public TipoDocumento getTipoDocumento() {
		return tipoDocumento;
	}

	/**
	 * @param tipoDocumento
	 *            : document type person.
	 */
	public void setTipoDocumento(TipoDocumento tipoDocumento) {
		this.tipoDocumento = tipoDocumento;
	}

	/**
	 * @return municipioNac: Municipality of birth of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio_nac", referencedColumnName = "id", nullable = false)
	public Municipio getMunicipioNac() {
		return municipioNac;
	}

	/**
	 * @param municipioNac
	 *            : Municipality of birth of the person.
	 */
	public void setMunicipioNac(Municipio municipioNac) {
		this.municipioNac = municipioNac;
	}

	/**
	 * @return departamentoNac: department of birth of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento_nac", referencedColumnName = "id", nullable = false)
	public Departamento getDepartamentoNac() {
		return departamentoNac;
	}

	/**
	 * @param departamentoNac
	 *            : department of birth of the person.
	 */
	public void setDepartamentoNac(Departamento departamentoNac) {
		this.departamentoNac = departamentoNac;
	}

	/**
	 * @return paisNac: country of birth of the person.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais_nac", referencedColumnName = "id", nullable = false)
	public Pais getPaisNac() {
		return paisNac;
	}

	/**
	 * @param paisNac
	 *            : country of birth of the person.
	 */
	public void setPaisNac(Pais paisNac) {
		this.paisNac = paisNac;
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
	 * @return municipioRes: municipality of residence of the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio_res", referencedColumnName = "id", nullable = false)
	public Municipio getMunicipioRes() {
		return municipioRes;
	}

	/**
	 * @param municipioRes
	 *            : municipality of residence of the person
	 */
	public void setMunicipioRes(Municipio municipioRes) {
		this.municipioRes = municipioRes;
	}

	/**
	 * @return departamentoRes: department of residence of the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento_res", referencedColumnName = "id", nullable = false)
	public Departamento getDepartamentoRes() {
		return departamentoRes;
	}

	/**
	 * @param departamentoRes
	 *            : department of residence of the person
	 */
	public void setDepartamentoRes(Departamento departamentoRes) {
		this.departamentoRes = departamentoRes;
	}

	/**
	 * @return paisRes: country of residence of the person
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais_res", referencedColumnName = "id", nullable = false)
	public Pais getPaisRes() {
		return paisRes;
	}

	/**
	 * @param paisRes
	 *            : country of residence of the person
	 */
	public void setPaisRes(Pais paisRes) {
		this.paisRes = paisRes;
	}

	/**
	 * @return usuario: user system with the person.
	 */
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_usuario", referencedColumnName = "id")
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * @param usuario
	 *            : user system with the person.
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * @return nombreCompleto: full name of the person consists of the names
	 *         fields.
	 */
	@Transient
	public String getNombreCompleto() {
		this.nombreCompleto = null;
		StringBuilder nombreCompleto = new StringBuilder(
				this.nombres == null ? "" : this.nombres);
		if (nombreCompleto.length() > 1)
			nombreCompleto.append(" ");
		nombreCompleto.append(this.apellidos == null ? "" : this.apellidos);
		if (nombreCompleto.length() > 1) {
			this.nombreCompleto = nombreCompleto.toString();
		}
		return this.nombreCompleto;
	}

	/**
	 * @param nombreCompleto
	 *            : full name of the person consists of the names fields.
	 */
	public void setNombreCompleto(String nombreCompleto) {
		this.nombreCompleto = nombreCompleto;
	}

	/**
	 * @return responsable:Field representing whether or not the person selected
	 *         for the activity is responsible, is used for the management of
	 *         the activities temporarily while it is stored in the
	 *         persona_actividad table.
	 */
	@Transient
	public String getResponsable() {
		return responsable;
	}

	/**
	 * @param responsable
	 *            :Field representing whether or not the person selected for the
	 *            activity is responsible, is used for the management of the
	 *            activities temporarily while it is stored in the
	 *            persona_actividad table.
	 */
	public void setResponsable(String responsable) {
		this.responsable = responsable;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((apellidos == null) ? 0 : apellidos.hashCode());
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result
				+ ((documento == null) ? 0 : documento.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime * result
				+ ((fechaNacimiento == null) ? 0 : fechaNacimiento.hashCode());
		result = prime * result + ((foto == null) ? 0 : foto.hashCode());
		result = prime * result + ((genero == null) ? 0 : genero.hashCode());
		result = prime * result + id;
		result = prime * result + ((movil == null) ? 0 : movil.hashCode());
		result = prime * result + ((nombres == null) ? 0 : nombres.hashCode());
		result = prime * result
				+ ((nroHijos == null) ? 0 : nroHijos.hashCode());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
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
		Persona other = (Persona) obj;
		if (apellidos == null) {
			if (other.apellidos != null)
				return false;
		} else if (!apellidos.equals(other.apellidos))
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
		if (documento == null) {
			if (other.documento != null)
				return false;
		} else if (!documento.equals(other.documento))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null)
				return false;
		} else if (!fechaCreacion.equals(other.fechaCreacion))
			return false;
		if (fechaFinVigencia == null) {
			if (other.fechaFinVigencia != null)
				return false;
		} else if (!fechaFinVigencia.equals(other.fechaFinVigencia))
			return false;
		if (fechaNacimiento == null) {
			if (other.fechaNacimiento != null)
				return false;
		} else if (!fechaNacimiento.equals(other.fechaNacimiento))
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
		if (id != other.id)
			return false;
		if (movil == null) {
			if (other.movil != null)
				return false;
		} else if (!movil.equals(other.movil))
			return false;
		if (nombres == null) {
			if (other.nombres != null)
				return false;
		} else if (!nombres.equals(other.nombres))
			return false;
		if (nroHijos == null) {
			if (other.nroHijos != null)
				return false;
		} else if (!nroHijos.equals(other.nroHijos))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
