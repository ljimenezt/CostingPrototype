package co.informatix.erp.informacionBase.entities;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * That represents the primary key of the class Conversion Unit which is
 * embedded therein.
 * 
 * @author Angelica Amaya
 * @modify 13/12/2011 marisol.calderon
 */
@SuppressWarnings("serial")
@Embeddable
public class ConversionUnidadPK implements Serializable {

	private UnidadMedida unidadInicial;
	private UnidadMedida unidadFinal;

	/**
	 * Empty class constructor that initializes the necessary variables.
	 */
	public ConversionUnidadPK() {
		this.unidadInicial = new UnidadMedida();
		this.unidadFinal = new UnidadMedida();
	}

	/**
	 * @return unidadInicial: unit to which you are going to take the
	 *         equivalence
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad_inicial", referencedColumnName = "id", nullable = false)
	public UnidadMedida getUnidadInicial() {
		return unidadInicial;
	}

	/**
	 * @param unidadInicial
	 *            : unit to which you are going to take the equivalence
	 */
	public void setUnidadInicial(UnidadMedida unidadInicial) {
		this.unidadInicial = unidadInicial;
	}

	/**
	 * @return unidadFinal: unit that is then converted to take the equivalence
	 */
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_unidad_final", referencedColumnName = "id", nullable = false)
	public UnidadMedida getUnidadFinal() {
		return unidadFinal;
	}

	/**
	 * @param unidadFinal
	 *            : unit that is then converted to take the equivalence
	 */
	public void setUnidadFinal(UnidadMedida unidadFinal) {
		this.unidadFinal = unidadFinal;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((unidadFinal == null) ? 0 : unidadFinal.hashCode());
		result = prime * result
				+ ((unidadInicial == null) ? 0 : unidadInicial.hashCode());
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
		ConversionUnidadPK other = (ConversionUnidadPK) obj;
		if (unidadFinal == null) {
			if (other.unidadFinal != null)
				return false;
		} else if (!unidadFinal.equals(other.unidadFinal))
			return false;
		if (unidadInicial == null) {
			if (other.unidadInicial != null)
				return false;
		} else if (!unidadInicial.equals(other.unidadInicial))
			return false;
		return true;
	}

}
