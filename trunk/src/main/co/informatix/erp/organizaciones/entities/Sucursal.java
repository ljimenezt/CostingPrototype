package co.informatix.erp.organizaciones.entities;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;

/**
 * This class handles information of local or branches that a company with the
 * use of products and by-products leave the plant.
 * 
 * @author Pablo.Baron
 * @modify Javier Eduardo Gelvis Vega
 * @modify 21/03/2012 marisol.calderon
 * 
 */

@SuppressWarnings("serial")
@Entity
@Table(name = "sucursal", schema = "organizaciones")
public class Sucursal implements Serializable {

	private Integer id;
	private String nombre;
	private String descripcion;
	private String direccion;
	private String telefono;
	private Date fechaRegistro;
	private Date fechaInicioVigencia;
	private Date fechaFinVigencia;
	private String userName;
	private String tipoSucursal;

	private Empresa empresa;
	private Sucursal sucursalPadre;

	private List<Sucursal> sucursalesHijas;

	/**
	 * @return id: ID of the branch of the company
	 */
	@Id
	@Column(name = "id", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return id;
	}

	/**
	 * @param id
	 *            : ID of the branch of the company
	 */
	public void setId(Integer id) {
		this.id = id;
	}

	/**
	 * @return nombre: gets branch Name Company
	 */
	@Column(name = "nombre", length = 50, nullable = false)
	public String getNombre() {
		return nombre;
	}

	/**
	 * @param nombre
	 *            : sets branch Name Company
	 */
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}

	/**
	 * @return descripcion: description of the company branch
	 */
	@Column(name = "descripcion", length = 200, nullable = true)
	public String getDescripcion() {
		return descripcion;
	}

	/**
	 * @param descripcion
	 *            : description of the company branch
	 */
	public void setDescripcion(String descripcion) {
		this.descripcion = descripcion;
	}

	/**
	 * @return direccion: direction of the branch office
	 */
	@Column(name = "direccion", length = 50, nullable = true)
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            : sets direction of the branch office
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return telefono: the phone company branch
	 */
	@Column(name = "telefono", length = 20, nullable = true)
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            : sets the phone company branch
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return fechaRegistro: date on which the branch of the company
	 *         registration
	 */
	@Column(name = "fecha_creacion", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaRegistro() {
		return fechaRegistro;
	}

	/**
	 * @param fechaRegistro
	 *            : date on which the branch of the company registration
	 */
	public void setFechaRegistro(Date fechaRegistro) {
		this.fechaRegistro = fechaRegistro;
	}

	/**
	 * @return fechaInicioVigencia: start date of the validity of the company
	 *         branch
	 */
	@Column(name = "fecha_inicio_vigencia", nullable = false)
	@Temporal(TemporalType.DATE)
	public Date getFechaInicioVigencia() {
		return fechaInicioVigencia;
	}

	/**
	 * @param fechaInicioVigencia
	 *            : sets start date of the validity of the company branch
	 */
	public void setFechaInicioVigencia(Date fechaInicioVigencia) {
		this.fechaInicioVigencia = fechaInicioVigencia;
	}

	/**
	 * @return fechaFinVigencia: date of expire of the term of the branch of the
	 *         company
	 */
	@Column(name = "fecha_fin_vigencia", nullable = true)
	@Temporal(TemporalType.DATE)
	public Date getFechaFinVigencia() {
		return fechaFinVigencia;
	}

	/**
	 * @param fechaFinVigencia
	 *            : sets date of expire of the term of the branch of the company
	 */
	public void setFechaFinVigencia(Date fechaFinVigencia) {
		this.fechaFinVigencia = fechaFinVigencia;
	}

	/**
	 * @return userName: Returns the name of the logged-on user running the
	 *         action on registration
	 */
	@Column(name = "user_name", length = 50, nullable = false)
	public String getUserName() {
		return userName;
	}

	/**
	 * @param userName
	 *            : Returns the name of the logged-on user running the action on
	 *            registration
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * @return tipoSucursal: stores the type of branch: branch, local or point
	 *         of sale. Variable that is not saved in the database.
	 */
	@Transient
	public String getTipoSucursal() {
		return tipoSucursal;
	}

	/**
	 * @param tipoSucursal
	 *            : stores the type of branch: branch, local or point of sale.
	 *            Variable that is not saved in the database.
	 */
	public void setTipoSucursal(String tipoSucursal) {
		this.tipoSucursal = tipoSucursal;
	}

	/**
	 * @return empresa: Returns the company to which it is associated branch
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_empresa", referencedColumnName = "id")
	public Empresa getEmpresa() {
		return empresa;
	}

	/**
	 * @param empresa
	 *            : Returns the company to which it is associated branch
	 */
	public void setEmpresa(Empresa empresa) {
		this.empresa = empresa;
	}

	/**
	 * @return sucursalPadre: father of the branch office
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_sucursal_padre", referencedColumnName = "id", nullable = true)
	public Sucursal getSucursalPadre() {
		return sucursalPadre;
	}

	/**
	 * @param sucursalPadre
	 *            : father of the branch office
	 */
	public void setSucursalPadre(Sucursal sucursalPadre) {
		this.sucursalPadre = sucursalPadre;
	}

	/**
	 * @return sucursalesHijas: daughters list of branches of a branch.
	 */
	@OneToMany(mappedBy = "sucursalPadre", fetch = FetchType.LAZY)
	public List<Sucursal> getSucursalesHijas() {
		return sucursalesHijas;
	}

	/**
	 * @param sucursalesHijas
	 *            : daughters list of branches of a branch.
	 */
	public void setSucursalesHijas(List<Sucursal> sucursalesHijas) {
		this.sucursalesHijas = sucursalesHijas;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descripcion == null) ? 0 : descripcion.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime
				* result
				+ ((fechaFinVigencia == null) ? 0 : fechaFinVigencia.hashCode());
		result = prime
				* result
				+ ((fechaInicioVigencia == null) ? 0 : fechaInicioVigencia
						.hashCode());
		result = prime * result
				+ ((fechaRegistro == null) ? 0 : fechaRegistro.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nombre == null) ? 0 : nombre.hashCode());
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
		Sucursal other = (Sucursal) obj;
		if (descripcion == null) {
			if (other.descripcion != null)
				return false;
		} else if (!descripcion.equals(other.descripcion))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (fechaFinVigencia == null) {
			if (other.fechaFinVigencia != null)
				return false;
		} else if (!fechaFinVigencia.equals(other.fechaFinVigencia))
			return false;
		if (fechaInicioVigencia == null) {
			if (other.fechaInicioVigencia != null)
				return false;
		} else if (!fechaInicioVigencia.equals(other.fechaInicioVigencia))
			return false;
		if (fechaRegistro == null) {
			if (other.fechaRegistro != null)
				return false;
		} else if (!fechaRegistro.equals(other.fechaRegistro))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nombre == null) {
			if (other.nombre != null)
				return false;
		} else if (!nombre.equals(other.nombre))
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
