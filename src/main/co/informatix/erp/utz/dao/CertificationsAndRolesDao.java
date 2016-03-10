package co.informatix.erp.utz.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.utz.entities.CertificationsAndRoles;

/**
 * DAO class that establishes the connection between business logic and database
 * for the management of the CertificationsAndRoles entity.
 * 
 * @author Johnatan.Naranjo
 * @modify Sergio.Ortiz
 */
@SuppressWarnings("serial")
@Stateless
public class CertificationsAndRolesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a type of certification and role in BD.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param certificationsAndRoles
	 *            : Certification type and role to save.
	 * @throws Exception
	 */
	public void saveCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) throws Exception {
		em.persist(certificationsAndRoles);
	}

	/**
	 * Removes a type of certification and the role of BD.
	 * 
	 * @author Andres.Gomez.
	 * 
	 * @param certificationsAndRoles
	 *            : certification and role to eliminate.
	 * @throws Exception
	 */
	public void removeCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) throws Exception {
		em.remove(em.merge(certificationsAndRoles));
	}

	/**
	 * Edit a type of certification and role in BD.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param certificationsAndRoles
	 *            : type Certification and editing role.
	 * @throws Exception
	 */
	public void editCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) throws Exception {
		em.merge(certificationsAndRoles);
	}

	/**
	 * Method that returns the list of certificates and roles.
	 * 
	 * @return List<CertificationsAndRoles>: List all certificates and roles.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CertificationsAndRoles> consultCertificationsAndRoles()
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cr FROM CertificationsAndRoles cr ");
		Query q = em.createQuery(query.toString());
		List<CertificationsAndRoles> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method consult the types with a certain range Certifications sent as
	 * a parameter and filtering the information by the values sent search.
	 * 
	 * @author Sergio.Ortiz
	 * @modify 05/08/2015 Mabell.Boada
	 * 
	 * @param start
	 *            : records where start the consult.
	 * @param range
	 *            : ranges to records.
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @return List<CertificationsAndRoles> list of the types of
	 *         CertificationsAndRoles.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CertificationsAndRoles> consultCertificationsAndRolesAction(
			int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM  CertificationsAndRoles ct ");
		query.append(consult);
		query.append("ORDER BY ct.idCertificactionsAndRoles ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<CertificationsAndRoles> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the number of types of certifications and roles in the database
	 * information by filtering the search values sent.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param consult
	 *            : String containing the query why certifications and filtered
	 *            roles.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records founds and roles type of Certification.
	 * @throws Exception
	 */
	public Long quantityCertificationsAndRoles(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ct) FROM CertificationsAndRoles ct ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult if the name of the certification and roles exist in the database
	 * when saving or editing.
	 * 
	 * @author Sergio.Ortiz
	 * @modify 10/03/2016 Jhair.Leal
	 * 
	 * @param nombre
	 *            : name of certification to verify.
	 * 
	 * @return CertificationsAndRoles: certificationsAndRoles object found with
	 *         the search parameters name and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CertificationsAndRoles nameExists(String name,
			int idCertificactionsAndRoles) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cr FROM CertificationsAndRoles cr ");
		query.append("WHERE UPPER(cr.name)=UPPER(:name) ");
		if (idCertificactionsAndRoles != 0) {
			query.append("AND cr.idCertificactionsAndRoles <>:idCertificactionsAndRoles ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (idCertificactionsAndRoles != 0) {
			q.setParameter("idCertificactionsAndRoles",
					idCertificactionsAndRoles);
		}
		List<CertificationsAndRoles> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
