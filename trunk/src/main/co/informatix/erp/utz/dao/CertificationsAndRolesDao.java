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
	 * Saves a type of certification and role in BD
	 * 
	 * @author Sergio.Ortiz
	 * @param certificationsAndRoles
	 *            : Certification type and role to save.
	 * @throws Exception
	 */
	public void guardaCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) throws Exception {
		em.persist(certificationsAndRoles);
	}

	/**
	 * Removes a type of certification and the role of BD
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param certificationsAndRoles
	 *            : certification and role to eliminate
	 * @throws Exception
	 */
	public void eliminarCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) throws Exception {
		em.remove(em.merge(certificationsAndRoles));
	}

	/**
	 * Edit a type of certification and role in BD
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param certificationsAndRoles
	 *            : type Certification and editing role.
	 * @throws Exception
	 */
	public void editarCertificationsAndRoles(
			CertificationsAndRoles certificationsAndRoles) throws Exception {
		em.merge(certificationsAndRoles);
	}

	/**
	 * Method that returns the list of certificates and roles.
	 * 
	 * @return List<CertificationsAndRoles>: List all certificates and roles
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CertificationsAndRoles> consultarCertificationsAndRoles()
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
	 *            : records where start the consult
	 * @param range
	 *            : ranges to records
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
	public List<CertificationsAndRoles> consultarCertificationsAndRolesAction(
			int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ct FROM  CertificationsAndRoles ct ");
		query.append(consult);
		query.append("ORDER BY ct.idCertificactionsAndRoles ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
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
	 * @param consult
	 *            : String containing the query why certifications and filtered
	 *            roles
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found sAndRoles type of Certification
	 * @throws Exception
	 */
	public Long cantidadCertificationsAndRoles(StringBuilder consult,
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
	 * Consult If the certificationsAndRoles name exists in the database when
	 * storing or editing.
	 * 
	 * @author Sergio.Ortiz
	 * @param nombre
	 *            : name of certification to verify
	 * 
	 * @return CertificationsAndRoles: certificationsAndRoles object found with
	 *         the search parameters name and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CertificationsAndRoles nombreExiste(String nombre) throws Exception {
		List<CertificationsAndRoles> results = em
				.createQuery(
						"FROM CertificationsAndRoles WHERE UPPER(name)=UPPER(:nombre)")
				.setParameter("nombre", nombre).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
