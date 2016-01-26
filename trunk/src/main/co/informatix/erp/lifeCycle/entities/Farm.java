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

import co.informatix.erp.informacionBase.entities.Departamento;
import co.informatix.erp.informacionBase.entities.Municipio;
import co.informatix.erp.informacionBase.entities.Pais;

/**
 * This class maps the farm table in the database
 * 
 * @author Johnatan.Naranjo
 * 
 */
@SuppressWarnings("serial")
@Entity
@Table(name = "farm", schema = "life_cycle")
public class Farm implements Serializable {
	private int idFarm;
	private String name;
	private Double locationLinkToMap;
	private String otherFieldAddress;
	private String nit;
	private String nombreComercial;
	private String logo;
	private String direccion;
	private String telefono;
	private String correo;
	private String fax;
	private String movil;

	private Pais pais;
	private Departamento departamento;
	private Municipio municipio;

	/**
	 * Empty constructor to initialize the necessary variables.
	 */
	public Farm() {
		this.pais = new Pais();
		this.departamento = new Departamento();
		this.municipio = new Municipio();
	}

	/**
	 * @return idFarm: Identifier farm.
	 */
	@Id
	@Column(name = "idfarm", nullable = false)
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getIdFarm() {
		return idFarm;
	}

	/**
	 * @param idFarm
	 *            : Identifier farm.
	 */
	public void setIdFarm(int idFarm) {
		this.idFarm = idFarm;
	}

	/**
	 * @return name: farm name
	 */
	@Column(name = "name", length = 70, nullable = false)
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            :farm name
	 * 
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return locationLinkToMap: location of the farm
	 */
	@Column(name = "location_link_to_map")
	public Double getLocationLinkToMap() {
		return locationLinkToMap;
	}

	/**
	 * @param locationLinkToMap
	 *            :location of the farm
	 * 
	 */
	public void setLocationLinkToMap(Double locationLinkToMap) {
		this.locationLinkToMap = locationLinkToMap;
	}

	/**
	 * @return otherFieldAddress: direction of the farm
	 */
	@Column(name = "other_field_address", length = 100)
	public String getOtherFieldAddress() {
		return otherFieldAddress;
	}

	/**
	 * @param otherFieldAddress
	 *            :direction of the farm
	 */
	public void setOtherFieldAddress(String otherFieldAddress) {
		this.otherFieldAddress = otherFieldAddress;
	}

	/**
	 * @return nit: tax identification number of a farm
	 */
	@Column(name = "nit", length = 25)
	public String getNit() {
		return nit;
	}

	/**
	 * @param nit
	 *            : tax identification number of a farm
	 */
	public void setNit(String nit) {
		this.nit = nit;
	}

	/**
	 * @return nombreComercial: Get the trade name of the estate.
	 */
	@Column(name = "nombre_comercial", length = 100)
	public String getNombreComercial() {
		return nombreComercial;
	}

	/**
	 * @param nombre
	 *            : Sets the trade name of the estate.
	 */
	public void setNombreComercial(String nombreComercial) {
		this.nombreComercial = nombreComercial;
	}

	/**
	 * @return logo: logo of the farm
	 */
	@Column(name = "logo", length = 200)
	public String getLogo() {
		return logo;
	}

	/**
	 * @param logo
	 *            : logo of the farm
	 */
	public void setLogo(String logo) {
		this.logo = logo;
	}

	/**
	 * @return direccion: gets the address of the farm.
	 */
	@Column(name = "direccion", length = 50)
	public String getDireccion() {
		return direccion;
	}

	/**
	 * @param direccion
	 *            : sets the address of the farm.
	 */
	public void setDireccion(String direccion) {
		this.direccion = direccion;
	}

	/**
	 * @return telefono: get the phone of a farm
	 */
	@Column(name = "telefono", length = 150)
	public String getTelefono() {
		return telefono;
	}

	/**
	 * @param telefono
	 *            : set the phone of a farm
	 */
	public void setTelefono(String telefono) {
		this.telefono = telefono;
	}

	/**
	 * @return correo: getting mail from a farm.
	 */
	@Column(name = "correo", length = 50)
	public String getCorreo() {
		return correo;
	}

	/**
	 * @param correo
	 *            : setting mail from a farm.
	 */
	public void setCorreo(String correo) {
		this.correo = correo;
	}

	/**
	 * @return fax: get the FAX of a farm.
	 */
	@Column(name = "fax", length = 20)
	public String getFax() {
		return fax;
	}

	/**
	 * @param fax
	 *            : set the FAX of a farm.
	 */
	public void setFax(String fax) {
		this.fax = fax;
	}

	/**
	 * @return movil: mobile phone farm.
	 */
	@Column(name = "movil", length = 20)
	public String getMovil() {
		return movil;
	}

	/**
	 * @param movil
	 *            : mobile phone farm.
	 */
	public void setMovil(String movil) {
		this.movil = movil;
	}

	/**
	 * @return pais: Country to which the farm belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_pais", referencedColumnName = "id")
	public Pais getPais() {
		return pais;
	}

	/**
	 * @param pais
	 *            :Country to which the farm belongs
	 */
	public void setPais(Pais pais) {
		this.pais = pais;
	}

	/**
	 * @return departamento : Department to which the farm belongs
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_departamento", referencedColumnName = "id")
	public Departamento getDepartamento() {
		return departamento;
	}

	/**
	 * @param departamento
	 *            : Department to which the farm belongs
	 */
	public void setDepartamento(Departamento departamento) {
		this.departamento = departamento;
	}

	/**
	 * @return municipio: get the town relate of a farm
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_municipio", referencedColumnName = "id")
	public Municipio getMunicipio() {
		return municipio;
	}

	/**
	 * @param municipio
	 *            : set the town relate of a farm
	 */
	public void setMunicipio(Municipio municipio) {
		this.municipio = municipio;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + idFarm;
		result = prime
				* result
				+ ((locationLinkToMap == null) ? 0 : locationLinkToMap
						.hashCode());
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime
				* result
				+ ((otherFieldAddress == null) ? 0 : otherFieldAddress
						.hashCode());
		result = prime * result + ((nit == null) ? 0 : nit.hashCode());
		result = prime * result
				+ ((nombreComercial == null) ? 0 : nombreComercial.hashCode());
		result = prime * result + ((logo == null) ? 0 : logo.hashCode());
		result = prime * result
				+ ((direccion == null) ? 0 : direccion.hashCode());
		result = prime * result
				+ ((telefono == null) ? 0 : telefono.hashCode());
		result = prime * result + ((correo == null) ? 0 : correo.hashCode());
		result = prime * result + ((fax == null) ? 0 : fax.hashCode());
		result = prime * result + ((movil == null) ? 0 : movil.hashCode());
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
		Farm other = (Farm) obj;
		if (idFarm != other.idFarm)
			return false;
		if (locationLinkToMap == null) {
			if (other.locationLinkToMap != null)
				return false;
		} else if (!locationLinkToMap.equals(other.locationLinkToMap))
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (otherFieldAddress == null) {
			if (other.otherFieldAddress != null)
				return false;
		} else if (!otherFieldAddress.equals(other.otherFieldAddress))
			return false;
		if (nit == null) {
			if (other.nit != null)
				return false;
		} else if (!nit.equals(other.nit))
			return false;
		if (nombreComercial == null) {
			if (other.nombreComercial != null)
				return false;
		} else if (!nombreComercial.equals(other.nombreComercial))
			return false;
		if (logo == null) {
			if (other.logo != null)
				return false;
		} else if (!logo.equals(other.logo))
			return false;
		if (direccion == null) {
			if (other.direccion != null)
				return false;
		} else if (!direccion.equals(other.direccion))
			return false;
		if (telefono == null) {
			if (other.telefono != null)
				return false;
		} else if (!telefono.equals(other.telefono))
			return false;
		if (correo == null) {
			if (other.correo != null)
				return false;
		} else if (!correo.equals(other.correo))
			return false;
		if (fax == null) {
			if (other.fax != null)
				return false;
		} else if (!fax.equals(other.fax))
			return false;
		if (movil == null) {
			if (other.movil != null)
				return false;
		} else if (!movil.equals(other.movil))
			return false;
		return true;
	}

}
