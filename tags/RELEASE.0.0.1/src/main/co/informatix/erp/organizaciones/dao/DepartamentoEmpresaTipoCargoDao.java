package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.organizaciones.entities.DepartamentoEmpresaTipoCargo;
import co.informatix.erp.organizaciones.entities.DepartamentoEmpresaTipoCargoPK;

/**
 * 
 * Class DAO establishing the connection between business logic and database
 * management departments in the company related to the types of cargo
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class DepartamentoEmpresaTipoCargoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Consult a DepartamentoEmpresaTipoCargo receiving object as the primary
	 * key parameter
	 * 
	 * @author marisol.calderon
	 * 
	 * @param primaryKey
	 *            : DepartamentoEmpresaTipoCargo object identifier.
	 * @return DepartamentoEmpresaTipoCargo: department business office object
	 *         type or null but found there.
	 * @throws Exception
	 */
	public DepartamentoEmpresaTipoCargo departamentoEmpresaTipoCargoXId(
			DepartamentoEmpresaTipoCargoPK primaryKey) throws Exception {
		return em.find(DepartamentoEmpresaTipoCargo.class, primaryKey);
	}

	/**
	 * Save an object DepartamentoEmpresaTipoCargo
	 * 
	 * @param depEmpresaTipoCargo
	 *            : object to save
	 * @throws Exception
	 */
	public void guardarDepartamentoEmpresaTipoCargo(
			DepartamentoEmpresaTipoCargo depEmpresaTipoCargo) throws Exception {
		em.persist(depEmpresaTipoCargo);
	}

	/**
	 * Edit object information from a DepartamentoEmpresaTipoCargo
	 * 
	 * @param depEmpresaTipoCargo
	 *            : edit object
	 * @throws Exception
	 */
	public void editarDepartamentoEmpresaTipoCargo(
			DepartamentoEmpresaTipoCargo depEmpresaTipoCargo) throws Exception {
		em.merge(depEmpresaTipoCargo);
	}

}
