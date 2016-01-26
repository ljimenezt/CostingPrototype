package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizaciones.entities.EmpresaPersona;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling people associated with a company.
 * 
 * @author Luz.Jaimes
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class EmpresaPersonaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Method consulting people who are related to the company in session and
	 * which are in force.
	 * 
	 * @param idEmpresa
	 *            :Company identifier in session to consult people
	 * @param idHacienda
	 *            :Farm identifier in session to consult people
	 * @param fechaHoy
	 *            : date of doing the query
	 * @return List<EmpresaPersona>: List of relations between the company and
	 *         those that meet the parameters sent in the query.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<EmpresaPersona> consultarEmpresaPersonas(int idEmpresa,
			int idHacienda, Date fechaHoy) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ep FROM EmpresaPersona ep ");
		query.append("WHERE ep.fechaInicioVigencia<=:fechaHoy ");
		query.append("AND ep.fechaFinVigencia>=:fechaHoy ");
		query.append("AND ep.empresa.id=:idEmpresa ");
		query.append("AND (ep.hacienda.id=:idHacienda ");
		query.append("OR ep.hacienda IS NULL) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idEmpresa", idEmpresa);
		q.setParameter("idHacienda", idHacienda);
		q.setParameter("fechaHoy", fechaHoy);

		return q.getResultList();
	}

	/**
	 * Method that queries the number of people who are related to the company
	 * in session and which are in force.
	 * 
	 * @param idEmpresa
	 *            :Company identifier in session to consult people
	 * @param idHacienda
	 *            :Farm identifier in session to consult people
	 * @param fechaHoy
	 *            : date of doing the query
	 * @return Long: number of relationships between the company and those that
	 *         meet the parameters sent in the query.
	 * @throws Exception
	 */
	public Long cantidadPersonaActividad(int idEmpresa, int idHacienda,
			Date fechaHoy) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ep) FROM EmpresaPersona ep ");
		query.append("WHERE ep.fechaInicioVigencia<=:fechaHoy ");
		query.append("AND ep.fechaFinVigencia>=:fechaHoy ");
		query.append("AND ep.empresa.id=:idEmpresa ");
		query.append("AND (ep.hacienda.id=:idHacienda ");
		query.append("OR ep.hacienda IS NULL) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idEmpresa", idEmpresa);
		q.setParameter("idhacienda", idHacienda);
		q.setParameter("fechaHoy", fechaHoy);
		return (Long) q.getSingleResult();
	}

}
