package co.informatix.erp.informacionBase.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.informacionBase.entities.MedioPago;

/**
 * DAO class that establishes the connection between business logic and
 * database. FormaPagoAction used to manage different forms of payment in a
 * sale.
 * 
 * @author Marcela.Chaparro
 * 
 */

@SuppressWarnings("serial")
@Stateless
public class MedioPagoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Consult the payment methods that are in force.
	 * 
	 * @return List<MedioPago>: List of payment methods found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MedioPago> consultarFormasPagoVigentes() throws Exception {
		return em.createQuery(
				"SELECT fp FROM FormaPago fp "
						+ "WHERE fp.fechaFinVigencia IS NULL "
						+ "ORDER BY fp.nombre").getResultList();
	}

}
