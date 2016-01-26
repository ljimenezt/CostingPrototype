package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizaciones.entities.DepartamentoEmpresa;

/**
 * DAO establishing the connection between business logic and database
 * management departments in the company
 * 
 * @author Luis.Ruiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class DepartamentoEmpresaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Registers a company department
	 * 
	 * @param departamentoEmpresa
	 *            : Object department of the company you want to save.
	 * 
	 * @throws Exception
	 */
	public void registrarDepartamentoEmpresa(
			DepartamentoEmpresa departamentoEmpresa) throws Exception {
		this.em.persist(departamentoEmpresa);
	}

	/**
	 * Modify a company department
	 * 
	 * @param departamentoEmpresa
	 *            : Object department of the company you want to edit.
	 * 
	 * @throws Exception
	 */
	public void modificarDepartamentoEmpresa(
			DepartamentoEmpresa departamentoEmpresa) throws Exception {
		this.em.merge(departamentoEmpresa);
		this.em.flush();
	}

	/**
	 * Consultation if the department name already exists
	 * 
	 * @param nombre
	 *            : Department name you want to verify their existence.
	 * 
	 * @return boolean: value <b> true </ b> if exists, otherwise <b> false </
	 *         b>.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean nombreExiste(String nombre) throws Exception {
		boolean salida = false;
		List<DepartamentoEmpresa> departamentoEmpresas = this.em
				.createQuery(
						"FROM DepartamentoEmpresa "
								+ "WHERE UPPER(nombre) = UPPER(:nombre)")
				.setParameter("nombre", nombre).getResultList();

		salida = !departamentoEmpresas.isEmpty();
		return salida;
	}

	/**
	 * Consultation if the department name already exists, except for the id
	 * that is passed as parameter
	 * 
	 * @param name
	 *            : Department name you want to verify their existence.
	 * @param id
	 *            : Department ID when editing.
	 * 
	 * @return boolean: the value <b> true </ b> if exists, otherwise <b> false
	 *         </ b>.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean nombreExiste(String name, int id) throws Exception {
		boolean salida = false;
		List<DepartamentoEmpresa> departamentoEmpresas = this.em
				.createQuery(
						"FROM DepartamentoEmpresa "
								+ "WHERE UPPER(nombre) = UPPER(:nombre) "
								+ "AND id != :id").setParameter("nombre", name)
				.setParameter("id", id).getResultList();
		salida = !departamentoEmpresas.isEmpty();
		return salida;
	}

	/**
	 * Consult a department of the company name
	 * 
	 * @param name
	 *            : Department name you want to verify their existence.
	 * 
	 * @return DepartamentoEmpresa: Department of Enterprise object or null but
	 *         found there.
	 * 
	 * @throws Exception
	 */
	public DepartamentoEmpresa consultarDepartamentoEmpresaXNombre(String name)
			throws Exception {
		DepartamentoEmpresa departamentoEmpresa = (DepartamentoEmpresa) this.em
				.createQuery(
						"FROM DepartamentoEmpresa "
								+ "WHERE UPPER(nombre) = UPPER(:nombre)")
				.setParameter("nombre", name).getSingleResult();
		return departamentoEmpresa;
	}

	/**
	 * Consult the amount of departments in the company.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param condicionVigencia
	 *            : variable with the current and non current condition of the
	 *            query.
	 * @param nombreBuscar
	 *            : Parameter name by which departments are filtered.
	 * 
	 * @return long: Number of records in the database, or null if there are.
	 * 
	 * @throws Exception
	 */
	public long cantidadDepartamentosEmpresa(String condicionVigencia,
			String nombreBuscar) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(de) FROM DepartamentoEmpresa  de ");
		query.append("WHERE de.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(de.nombre) LIKE UPPER(:keyword) ");
		}
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method consulting company departments filtered by the parameters sent.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param start
	 *            : Number of first record where the query begins.
	 * @param range
	 *            : Number of the last record in the query.
	 * @param condicionVigencia
	 *            : condition to know whether existing or not existing are
	 *            queried.
	 * @param nombreBuscar
	 *            : Parameter name by which departments are filtered.
	 * @return List<DepartamentoEmpresa>: Departments list of objects of the
	 *         Company or null but records exist.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DepartamentoEmpresa> consultarDepartamentosEmpresa(int start,
			int range, String condicionVigencia, String nombreBuscar)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT de FROM DepartamentoEmpresa de ");
		query.append("WHERE de.fechaFinVigencia ");
		query.append(condicionVigencia);
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append(" AND UPPER(de.nombre) LIKE UPPER(:keyword) ");
		}
		query.append(" ORDER BY de.nombre ");
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Check out the departments of the company
	 * 
	 * @param vigentes
	 *            :Flag indicating whether or not existing consultation
	 * 
	 * @return listaDepartamentoEmpresas: List DepartamentoEmpresa objects
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DepartamentoEmpresa> consultarDepartamentosEmpresa(
			boolean vigentes) throws Exception {
		List<DepartamentoEmpresa> listaDepartamentoEmpresas = new ArrayList<DepartamentoEmpresa>();
		String consulta;
		if (vigentes) {
			consulta = "FROM DepartamentoEmpresa "
					+ "WHERE fechaFinVigencia IS NULL ORDER BY nombre";
		} else {
			consulta = "FROM DepartamentoEmpresa "
					+ "WHERE fechaFinVigencia IS NOT NULL ORDER BY nombre";
		}
		listaDepartamentoEmpresas = this.em.createQuery(consulta)
				.getResultList();
		return listaDepartamentoEmpresas;
	}

	/**
	 * Method for querying all departments.
	 * 
	 * @return List<DepartamentoEmpresa>: List of objects of all company
	 *         departments.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<DepartamentoEmpresa> consultarTodosDepartamentos()
			throws Exception {
		return this.em.createQuery("FROM DepartamentoEmpresa").getResultList();
	}

}
