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
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import co.informatix.erp.organizaciones.entities.Sucursal;

/**
 * This class contains information from the history of changes made if the
 * contract or other person.
 * 
 * @author Marcela.Chaparro
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "historico_contrato", schema = "recursos_humanos")
public class HistoricoContrato implements Serializable {

	private int id;
	private Double remuneracion;
	private String objeto;
	private Date fechaInicio;
	private Date fechaFin;
	private boolean empleadoConfianza;
	private Date fechaCreacion;
	private String userName;
	private Short tipoSalario;
	private Integer documentoSoporte;

	private Contrato contrato;
	private TipoCargo cargo;
	private TipoContrato tipoContrato;
	private Sucursal sucursal;
	private FormaPago formaPago;

	/**
	 * @return id: Contract historical identifier.
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return id;
	}

	/**
	 * @param id
	 *            : Contract historical identifier.
	 */
	public void setId(int id) {
		this.id = id;
	}

	/**
	 * @return remuneracion: Base salary or remuneration of the person in the
	 *         contract.
	 */
	@Column(name = "remuneracion", nullable = false)
	public Double getRemuneracion() {
		return remuneracion;
	}

	/**
	 * @param remuneracion
	 *            : Base salary or remuneration of the person in the contract.
	 */
	public void setRemuneracion(Double remuneracion) {
		this.remuneracion = remuneracion;
	}

	/**
	 * @return objeto: Employee object in the contract.
	 */
	@Column(name = "objeto", length = 500)
	public String getObjeto() {
		return objeto;
	}

	/**
	 * @param objeto
	 *            : Employee object in the contract.
	 */
	public void setObjeto(String objeto) {
		this.objeto = objeto;
	}

	/**
	 * @return fechaInicio: Date on which the contract begins.
	 */
	@Column(name = "fecha_inicio", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaInicio() {
		return fechaInicio;
	}

	/**
	 * @param fechaInicio
	 *            : Date on which the contract begins.
	 */
	public void setFechaInicio(Date fechaInicio) {
		this.fechaInicio = fechaInicio;
	}

	/**
	 * @return fechaFin: Date of completion of the contract.
	 */
	@Column(name = "fecha_fin")
	@Temporal(TemporalType.DATE)
	public Date getFechaFin() {
		return fechaFin;
	}

	/**
	 * @param fechaFin
	 *            : Date of completion of the contract.
	 */
	public void setFechaFin(Date fechaFin) {
		this.fechaFin = fechaFin;
	}

	/**
	 * @return empleadoConfianza: Variable that identifies whether the person is
	 *         an employee of trust in the contract.
	 */
	@Column(name = "empleado_confianza", nullable = false)
	public boolean isEmpleadoConfianza() {
		return empleadoConfianza;
	}

	/**
	 * @param empleadoConfianza
	 *            : Variable that identifies whether the person is an employee
	 *            of trust in the contract.
	 */
	public void setEmpleadoConfianza(boolean empleadoConfianza) {
		this.empleadoConfianza = empleadoConfianza;
	}

	/**
	 * @return fechaCreacion: Creation date of registration.
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaCreacion() {
		return fechaCreacion;
	}

	/**
	 * @param fechaCreacion
	 *            : Creation date of registration.
	 */
	public void setFechaCreacion(Date fechaCreacion) {
		this.fechaCreacion = fechaCreacion;
	}

	/**
	 * @return userName: User name in session running the action on the record.
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : User name in session running the action on the record.
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return tipoSalario: type of salary which has the contract.
	 */
	@Column(name = "id_tipo_salario")
	public Short getTipoSalario() {
		return tipoSalario;
	}

	/**
	 * @param tipoSalario
	 *            : type of salary which has the contract.
	 */
	public void setTipoSalario(Short tipoSalario) {
		this.tipoSalario = tipoSalario;
	}

	/**
	 * @return documentoSoporte: Document support contract
	 */
	@Column(name = "id_documento_soporte")
	public Integer getDocumentoSoporte() {
		return documentoSoporte;
	}

	/**
	 * @param documentoSoporte
	 *            : Document support contract
	 */
	public void setDocumentoSoporte(Integer documentoSoporte) {
		this.documentoSoporte = documentoSoporte;
	}

	/**
	 * @return contrato: Reference to the historic agreement which is generated.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_contrato", referencedColumnName = "id", nullable = false)
	public Contrato getContrato() {
		return contrato;
	}

	/**
	 * @param contrato
	 *            : Reference to the historic agreement which is generated.s
	 */
	public void setContrato(Contrato contrato) {
		this.contrato = contrato;
	}

	/**
	 * @return cargo: Reference to the position for which a person is hired.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_cargo", referencedColumnName = "id", nullable = false)
	public TipoCargo getCargo() {
		return cargo;
	}

	/**
	 * @param cargo
	 *            : Reference to the position for which a person is hired.
	 */
	public void setCargo(TipoCargo cargo) {
		this.cargo = cargo;
	}

	/**
	 * @return tipoContrato: Reference to the type of contract that can be
	 *         applied in a labor agreement, example: providing services,
	 *         fixed-term contract, open-ended contracts, among others.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_tipo_contrato", referencedColumnName = "id", nullable = false)
	public TipoContrato getTipoContrato() {
		return tipoContrato;
	}

	/**
	 * @param tipoContrato
	 *            : Reference to the type of contract that can be applied in a
	 *            labor agreement, example: providing services, fixed-term
	 *            contract, open-ended contracts, among others.
	 */
	public void setTipoContrato(TipoContrato tipoContrato) {
		this.tipoContrato = tipoContrato;
	}

	/**
	 * @return sucursal: reference to where the person carry out the activities
	 *         to which the labor contract was made.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal", referencedColumnName = "id", nullable = false)
	public Sucursal getSucursal() {
		return sucursal;
	}

	/**
	 * @param sucursal
	 *            : reference to where the person carry out the activities to
	 *            which the labor contract was made.
	 */
	public void setSucursal(Sucursal sucursal) {
		this.sucursal = sucursal;
	}

	/**
	 * @return formaPago: reference to where the person carry out the activities
	 *         to which the labor contract was made.
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_forma_pago", referencedColumnName = "id", nullable = false)
	public FormaPago getFormaPago() {
		return formaPago;
	}

	/**
	 * @param formaPago
	 *            : reference to where the person carry out the activities to
	 *            which the labor contract was made.
	 */
	public void setFormaPago(FormaPago formaPago) {
		this.formaPago = formaPago;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime
				* result
				+ ((documentoSoporte == null) ? 0 : documentoSoporte.hashCode());
		result = prime * result + (empleadoConfianza ? 1231 : 1237);
		result = prime * result
				+ ((fechaCreacion == null) ? 0 : fechaCreacion.hashCode());
		result = prime * result
				+ ((fechaFin == null) ? 0 : fechaFin.hashCode());
		result = prime * result
				+ ((fechaInicio == null) ? 0 : fechaInicio.hashCode());
		result = prime * result + id;
		result = prime * result + ((objeto == null) ? 0 : objeto.hashCode());
		result = prime * result
				+ ((remuneracion == null) ? 0 : remuneracion.hashCode());
		result = prime * result
				+ ((tipoSalario == null) ? 0 : tipoSalario.hashCode());
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
		HistoricoContrato other = (HistoricoContrato) obj;
		if (documentoSoporte == null) {
			if (other.documentoSoporte != null)
				return false;
		} else if (!documentoSoporte.equals(other.documentoSoporte))
			return false;
		if (empleadoConfianza != other.empleadoConfianza)
			return false;
		if (fechaCreacion == null) {
			if (other.fechaCreacion != null)
				return false;
		} else if (!fechaCreacion.equals(other.fechaCreacion))
			return false;
		if (fechaFin == null) {
			if (other.fechaFin != null)
				return false;
		} else if (!fechaFin.equals(other.fechaFin))
			return false;
		if (fechaInicio == null) {
			if (other.fechaInicio != null)
				return false;
		} else if (!fechaInicio.equals(other.fechaInicio))
			return false;
		if (id != other.id)
			return false;
		if (objeto == null) {
			if (other.objeto != null)
				return false;
		} else if (!objeto.equals(other.objeto))
			return false;
		if (remuneracion == null) {
			if (other.remuneracion != null)
				return false;
		} else if (!remuneracion.equals(other.remuneracion))
			return false;
		if (tipoSalario == null) {
			if (other.tipoSalario != null)
				return false;
		} else if (!tipoSalario.equals(other.tipoSalario))
			return false;
		if (userName == null) {
			if (other.userName != null)
				return false;
		} else if (!userName.equals(other.userName))
			return false;
		return true;
	}

}
