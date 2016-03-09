package co.informatix.erp.humanResources.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.humanResources.entities.Hr;

/**
 * DAO class that establishes the connection between business logic and base
 * data. HrAction manages Human Resources.
 * 
 * @author Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class HrDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Edit the track information for human resource.
	 * 
	 * @param hr
	 *            : human resource editing.
	 * @throws Exception
	 */
	public void editarHr(Hr hr) throws Exception {
		em.merge(hr);
	}

	/**
	 * Save the human resource in the database
	 * 
	 * @param hr
	 *            : human resources to save
	 * @throws Exception
	 */
	public void guardarHr(Hr hr) throws Exception {
		em.persist(hr);
	}

	/**
	 * Eliminates the human resource of the BD
	 * 
	 * @param hr
	 *            : human resources to eliminate
	 * @throws Exception
	 */
	public void eliminarHr(Hr hr) throws Exception {
		Hr humanResource = em.find(Hr.class, hr.getIdHr());
		em.remove(em.merge(humanResource));
	}

	/**
	 * Returns the number of existing human resources in the database filtering
	 * information search by the values sent.
	 * 
	 * @param consulta
	 *            : String containing the query why the filter human Resources.
	 * @param parametros
	 *            : query parameters.
	 * @return Long: number of HR records found.
	 * @throws Exception
	 */
	public Long cantidadHr(StringBuilder consulta, List<SelectItem> parametros)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(h) FROM Hr h ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method human resources consulting with a certain range sent as a
	 * parameter and filtering the information by the values of sent search.
	 * 
	 * @param inicio
	 *            : where he started the consultation record
	 * @param rango
	 *            : range of records
	 * @param consulta
	 *            : Consult records the parameters depending selected by the
	 *            user.
	 * @param parametros
	 *            : query parameters.
	 * @return List
	 *         <Hr>
	 *         : List of human resources.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hr> consultarHr(int inicio, int rango, StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT h FROM Hr h ");
		query.append(consulta);
		query.append("ORDER BY h.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(inicio).setMaxResults(rango);
		List<Hr> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult if the full name exists in human resource base data when storing
	 * or editing.
	 * 
	 * @modify 11/09/2015 Dario.Lopez
	 * 
	 * @param nombre
	 *            : human resource name to verify
	 * @param apellido
	 *            : human resource last Name to verify
	 * @param id
	 *            : human resource id to verify
	 * @return Hr: hr object found with the search parameters surname and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Hr nombreCompletoExiste(String nombre, String apellido, int id)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT h FROM Hr h ");
		query.append("WHERE UPPER(h.familyName)=UPPER(:apellido) ");
		query.append("AND  UPPER(h.name)=UPPER(:nombre)   ");
		if (id != 0) {
			query.append("AND h.idHr <>:idHr ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("apellido", apellido);
		q.setParameter("nombre", nombre);
		if (id != 0) {
			q.setParameter("idHr", id);
		}
		List<Hr> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult article assigned to a human resource, considering that are only
	 * those that are not null in the table
	 * 
	 * @param nomObject
	 *            : object to refer to human resources
	 * @param idHr
	 *            : Human Resource id being queried
	 * @return Object information associated with human resource or null if not
	 *         exists.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoHr(String nomObject, int idHr)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT h." + nomObject
								+ " FROM Hr h WHERE h.idHr=:idHr")
				.setParameter("idHr", idHr).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that returns the list of human resources.
	 * 
	 * @author Mabell.Boada
	 * 
	 * @return List
	 *         <Hr>
	 *         : List of all human resources.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Hr> consultarHr() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT hr FROM Hr hr ");
		Query q = em.createQuery(query.toString());
		List<Hr> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consult if certified human resources that can be assigned to a specific
	 * activity to have certain certifications.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param idActivity
	 *            : activity identifier.
	 * @param idHrTypes
	 *            : human resource type identifier
	 * @return Long: amounts of activities with at least one certified.
	 * @throws Exception
	 */
	public Long consultarHrCertificados(int idActivity, int idHrTypes)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(h) FROM Hr h ");
		query.append("JOIN h.hrTypes ht ");
		query.append("WHERE h IN ");
		query.append("(SELECT h FROM HrCertificationsAndRoles hrc ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.certificationsAndRoles ca ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.hr h ");
		query.append("WHERE ca IN ");
		query.append("(SELECT acr FROM ActivitiesAndCertifications ac ");
		query.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles acr ");
		query.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
		query.append("WHERE aca.idActivity = :idActividad)) ");
		if (idHrTypes > 0)
			query.append("AND ht.idHrType = :idHrTypes ");
		Query q = em.createQuery(query.toString());
		if (idHrTypes > 0)
			q.setParameter("idHrTypes", idHrTypes);
		q.setParameter("idActividad", idActivity);
		return (Long) q.getSingleResult();
	}

	/**
	 * Consult if certified human resources and state of mother hood that can be
	 * assigned to a specific activity.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param idActivity
	 *            : activity identifier.
	 * @param idHrTypes
	 *            : human resource type identifier
	 * @return Long: list of activities with at least one certified.
	 * @throws Exception
	 */
	public Long amountHrCertifiedAndMaternity(int idActivity, int idHrTypes)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(h) FROM Hr h ");
		query.append("JOIN h.hrTypes ht ");
		query.append("WHERE h IN ");
		query.append("(SELECT h FROM HrCertificationsAndRoles hrc ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.certificationsAndRoles ca ");
		query.append("JOIN hrc.hrCertificationsAndRolesPK.hr h ");
		query.append("WHERE ca IN ");
		query.append("(SELECT acr FROM ActivitiesAndCertifications ac ");
		query.append("JOIN ac.activitiesAndCertificationsPK.certificationsAndRoles acr ");
		query.append("JOIN ac.activitiesAndCertificationsPK.activities aca ");
		query.append("WHERE aca.idActivity = :idActividad)) ");
		query.append("AND h.maternityBreastFeeding = true ");
		if (idHrTypes > 0)
			query.append("AND ht.idHrType = :idHrTypes ");
		Query q = em.createQuery(query.toString());
		if (idHrTypes > 0)
			q.setParameter("idHrTypes", idHrTypes);
		q.setParameter("idActividad", idActivity);
		return (Long) q.getSingleResult();
	}

}
