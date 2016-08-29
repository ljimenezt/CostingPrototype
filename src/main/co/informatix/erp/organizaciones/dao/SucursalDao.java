package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import co.informatix.erp.organizaciones.entities.Sucursal;

/**
 * Class DAO that establishes the connection between the business logic and the
 * database for managing the Branch entity.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class SucursalDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows to consult branches belonging to a company in effect
	 * 
	 * @param idEmpresa
	 *            : id of the company to which branches belong to consult
	 * @return List<Sucursal>: list of branches of the company or null if not
	 *         present.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Sucursal> consultarSucursalesXEmpresa(int idEmpresa)
			throws Exception {
		List<Sucursal> sucursales = em
				.createQuery(
						"SELECT s FROM Sucursal s "
								+ "WHERE s.empresa.id=:idEmp "
								+ "AND s.fechaFinVigencia IS NULL "
								+ "ORDER BY s.nombre")
				.setParameter("idEmp", idEmpresa).getResultList();

		return sucursales;

	}

	/**
	 * This method makes the query given branch identifier thereof.
	 * 
	 * @param identificadorSucursal
	 *            : ID of the branch you want to consult.
	 * @return Sucursal: object of the branch whether or null otherwise.
	 * @throws Exception
	 */
	public Sucursal consultarSucursal(int identificadorSucursal)
			throws Exception {
		return em.find(Sucursal.class, identificadorSucursal);
	}

	/**
	 * Method that allows consulting current branch system.
	 * 
	 * @return list of existing branches.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Sucursal> consultarSucursalesVigentes() throws Exception {
		List<Sucursal> sucursales = em.createQuery(
				"SELECT s FROM Sucursal s "
						+ "WHERE s.fechaFinVigencia IS NULL "
						+ "ORDER BY s.nombre").getResultList();
		return sucursales;
	}
}