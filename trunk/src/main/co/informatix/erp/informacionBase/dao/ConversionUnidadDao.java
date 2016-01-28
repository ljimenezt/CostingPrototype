package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.informacionBase.entities.ConversionUnidad;

/**
 * This class in charge of the administration in the database conversion of
 * measurement units.
 * 
 * @author marisol.calderon
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class ConversionUnidadDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method makes the record in the database a new conversion units in
	 * the database information system.
	 * 
	 * @param conversionUnidad
	 *            : Conversion unit to record in the database.
	 * 
	 * @throws Exception
	 */
	public void crearConversionUnidad(ConversionUnidad conversionUnidad)
			throws Exception {
		em.persist(conversionUnidad);
	}

	/**
	 * This method makes updating a conversion of the base unit system data.
	 * 
	 * @param conversionUnidad
	 *            : conversion unit to update.
	 * 
	 * @throws Exception
	 */
	public void modificarConversionUnidad(ConversionUnidad conversionUnidad)
			throws Exception {
		em.merge(conversionUnidad);
	}

	/**
	 * This method is used to calculate the number of conversions registered in
	 * the information system
	 * 
	 * @param parametroConsulta
	 *            : query parameter.
	 * @return Long: number of existing records gives conversions measurement
	 *         units.
	 * @throws Exception
	 */
	public Long cantidadConversionUnidades(String parametroConsulta)
			throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(c) FROM  ConversionUnidad c WHERE "
								+ "UPPER(c.llavePrimaria.unidadInicial.nombre) "
								+ "LIKE UPPER(:parametro) "
								+ "OR  UPPER(c.llavePrimaria.unidadFinal.nombre) "
								+ "LIKE UPPER(:parametro) ")
				.setParameter("parametro", "%" + parametroConsulta + "%")
				.getSingleResult();
	}

	/**
	 * This method makes reference to all units conversions as available in the
	 * information system.
	 * 
	 * @param inicio
	 *            : record where the query begins.
	 * @param rango
	 *            : range of the query.
	 * @param parametroConsulta
	 *            : parameter for which the query is filtered.
	 * @return List<ConversionUnidad>: List of unit of measure conversions.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<ConversionUnidad> consultarConversionUnidades(int inicio,
			int rango, String parametroConsulta) throws Exception {
		return em
				.createQuery(
						"FROM ConversionUnidad c WHERE "
								+ "UPPER(c.llavePrimaria.unidadInicial.nombre) "
								+ "LIKE UPPER(:parametro) "
								+ "OR  UPPER(c.llavePrimaria.unidadFinal.nombre) "
								+ "LIKE UPPER(:parametro) "
								+ "ORDER BY c.llavePrimaria.unidadInicial.nombre ASC, "
								+ "c.llavePrimaria.unidadFinal.nombre ASC")
				.setFirstResult(inicio)
				.setParameter("parametro", "%" + parametroConsulta + "%")
				.setMaxResults(rango).getResultList();
	}

}