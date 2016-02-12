package co.informatix.erp.utz.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.utz.entities.HrCertificationsAndRoles;

/**
 * DAO class that establishes the connection between business logic and
 * database. HrCertificationsAndRolesAction used for managing
 * HrCertificationsAndRoles.
 * 
 * @author Mabell.Boada
 * 
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class HrCertificationsAndRolesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a record of human resources and certificates in the database.
	 * 
	 * @param hrCertificationsAndRoles
	 *            : Human resources and certified to save.
	 * @throws Exception
	 */
	public void guardarHrCertRoles(
			HrCertificationsAndRoles hrCertificationsAndRoles) throws Exception {
		em.persist(hrCertificationsAndRoles);
	}

	/**
	 * Edit a record of certified human resources and BD.
	 * 
	 * @param hrCertificationsAndRoles
	 *            : Human resources and certified to edit.
	 * @throws Exception
	 */
	public void editarHrCertRoles(
			HrCertificationsAndRoles hrCertificationsAndRoles) throws Exception {
		em.merge(hrCertificationsAndRoles);
	}

	/**
	 * Deletes a record of human resources and certificates the database.
	 * 
	 * @param hrCertificationsAndRoles
	 *            : Human resources and eliminate certificate
	 * @throws Exception
	 */
	public void eliminarHrCertRoles(
			HrCertificationsAndRoles hrCertificationsAndRoles) throws Exception {
		em.remove(em.merge(hrCertificationsAndRoles));
	}

	/**
	 * Method that returns the list of human resources and related certificates
	 * and certificates roles.
	 * 
	 * @param idCertAndRoles
	 *            : ID certificates and roles.
	 * @param start
	 *            : Registry where consultation begins
	 * @param range
	 *            : Range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : Parameters of the query.
	 * 
	 * @return List <HrCertificationsAndRoles> : List of human resources and
	 *         certified according to the search criteria.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<HrCertificationsAndRoles> consultarXIdCertRol(
			int idCertAndRoles, int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT hcr FROM HrCertificationsAndRoles hcr ");
		query.append("JOIN FETCH hcr.hrCertificationsAndRolesPK.hr hr ");
		query.append("JOIN FETCH hcr.hrCertificationsAndRolesPK.certificationsAndRoles cr ");
		query.append("WHERE cr.idCertificactionsAndRoles=:idCertAndRoles ");
		query.append(consult);
		query.append("ORDER BY hr.idHr ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCertAndRoles", idCertAndRoles);
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<HrCertificationsAndRoles> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Method that returns the number of certified human resources and related
	 * certificates and roles.
	 * 
	 * @param idCertAndRoles
	 *            : ID certificates and roles.
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : Parameters of the query.
	 * 
	 * @return Long : Number of human resources and certifications according to
	 *         the search criteria.
	 * 
	 * @throws Exception
	 */
	public Long cantXIdCertRol(int idCertAndRoles, StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(hcr) FROM HrCertificationsAndRoles hcr ");
		query.append("JOIN hcr.hrCertificationsAndRolesPK.hr hr ");
		query.append("JOIN hcr.hrCertificationsAndRolesPK.certificationsAndRoles cr ");
		query.append("WHERE cr.idCertificactionsAndRoles=:idCertAndRoles ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		q.setParameter("idCertAndRoles", idCertAndRoles);
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method to check whether a record already exists with the two identifiers
	 * that form the table composite key.
	 * 
	 * @param idCertAndRoles
	 *            : ID certificates and roles.
	 * @param idHr
	 *            : Human Resource Identifier
	 * @return Boolean: Returns true if the relationship exists otherwise false.
	 * @throws Exception
	 */
	public Boolean relacionExiste(int idCertAndRoles, int idHr)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT hcr FROM HrCertificationsAndRoles hcr ");
		query.append("JOIN hcr.hrCertificationsAndRolesPK.hr hr ");
		query.append("JOIN hcr.hrCertificationsAndRolesPK.certificationsAndRoles cr ");
		query.append("WHERE cr.idCertificactionsAndRoles=:idCertAndRoles ");
		query.append("AND hr.idHr=:idHr ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCertAndRoles", idCertAndRoles).setParameter("idHr",
				idHr);
		if (q.getResultList().size() > 0) {
			return true;
		}
		return false;
	}
}
