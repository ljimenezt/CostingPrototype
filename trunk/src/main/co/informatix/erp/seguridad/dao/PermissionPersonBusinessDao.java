package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.organizations.entities.Business;
import co.informatix.erp.humanResources.entities.Person;
import co.informatix.erp.seguridad.entities.PermisoPersonaEmpresa;
import co.informatix.erp.utils.Constantes;

/**
 * DAO establishing the connection between business logic and database for
 * handling the relationship between people with business, branches and
 * properties to control access from the action PermisoPersonaEmpresaAction.
 * 
 * @author marisol.calderon
 */
@SuppressWarnings("serial")
@Stateless
public class PermissionPersonBusinessDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save a person who is assigned access rights to a company.
	 * 
	 * @param permissionPersonCompany
	 *            : Company relationship with the person you want to save.
	 * @throws Exception
	 */
	public void savePermissionPersonCompany(
			PermisoPersonaEmpresa permissionPersonCompany) throws Exception {
		em.persist(permissionPersonCompany);
	}

	/**
	 * Modifies a person who is assigned access rights to a company.
	 * 
	 * @param permissionPersonCompany
	 *            : Company relationship with the person you want to modify.
	 * @throws Exception
	 */
	public void editPermissionPersonCompany(
			PermisoPersonaEmpresa permissionPersonCompany) throws Exception {
		em.merge(permissionPersonCompany);
	}

	/**
	 * Returns a list of companies that have an individual access permissions
	 * associated with existing or not existing.
	 * 
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @param start
	 *            : value in initiating the inquiry.
	 * @param range
	 *            : amount or range of records to consult.
	 * @return List<PermissionPersonBusiness>: List of companies associated with
	 *         access permissions to the person.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PermisoPersonaEmpresa> consultBusinessAccessPerson(
			StringBuilder consult, List<SelectItem> parameters, int start,
			int range) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe FROM PermisoPersonaEmpresa ppe ");
		query.append(consult);
		query.append("ORDER BY ppe.id DESC ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Check the number of companies that have access permissions in place or
	 * not in place and that have been associated with a particular person.
	 * 
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: Number of records found.
	 * @throws Exception
	 */
	public Long amountBusinessAccessPerson(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ppe) FROM PermisoPersonaEmpresa ppe ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method that allows consulting information in the relationships you have
	 * the permiso_persona_empresa the table.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @param permissionPersonBusiness
	 *            : covered by the permit of the person in the company.
	 * @return permissionPersonBusiness: The found object information detail to
	 *         permit a person in business.
	 * @throws Exception
	 */
	public PermisoPersonaEmpresa consultDetailsPermissionPersonBusiness(
			PermisoPersonaEmpresa permissionPersonBusiness) throws Exception {
		Integer id = permissionPersonBusiness.getId();
		Business business = (Business) consultObjectPermissionPersonBusiness(
				Constantes.EMPRESA, id);
		Person person = (Person) consultObjectPermissionPersonBusiness(
				Constantes.PERSONA, id);
		Farm farm = (Farm) consultObjectPermissionPersonBusiness(
				Constantes.Farm, id);
		permissionPersonBusiness.setEmpresa(business);
		permissionPersonBusiness.setPersona(person);
		if (farm != null) {
			permissionPersonBusiness.setFarm(farm);
		} else {
			permissionPersonBusiness.setFarm(new Farm());
		}
		return permissionPersonBusiness;
	}

	/**
	 * See also article assigned to a permisoPersonaEmpresa depending on the
	 * parameters sent.
	 * 
	 * @param nomObject
	 *            : in order to consult permisoPersonaEmpresa.
	 * @param idPermissionPersonBusiness
	 *            : permisoPersonaEmpresa ID is consulted.
	 * @return The found object information that is associated with the business
	 *         person permission.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectPermissionPersonBusiness(String nomObject,
			int idPermissionPersonBusiness) throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT ppe." + nomObject
								+ " FROM PermisoPersonaEmpresa ppe "
								+ "WHERE ppe.id=:idPermisoPersonaEmpresa")
				.setParameter("idPermisoPersonaEmpresa",
						idPermissionPersonBusiness).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that allows consulting companies with access permissions a person
	 * has on the system.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param documentPerson
	 *            : person document in session.
	 * @return List<PermissionPersonBusiness>: List of permits to which the
	 *         person has access.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PermisoPersonaEmpresa> consultPermissionsPersonEBusinessAccessUser(
			String documentPerson) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.persona.documento=:documentPerson ");
		query.append("AND (ppe.fechaFinVigencia IS NULL ");
		query.append("OR ppe.fechaFinVigencia >= :currentDate) ");
		query.append("ORDER BY ppe.empresa.name ");
		Query q = em.createQuery(query.toString());
		q.setParameter("documentPerson", documentPerson);
		q.setParameter("currentDate", new Date());
		return q.getResultList();
	}

	/**
	 * Method to consult if there is a company with permits
	 * (PermisoPersonaEmpresa) default user or person.
	 * 
	 * @param documentPerson
	 *            : document of the person to whom permits the company is
	 *            consulted.
	 * @return PermissionPersonBusiness: object that is predetermined or null
	 *         but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PermisoPersonaEmpresa consultExistPredetermined(String documentPerson)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.predeterminado = TRUE ");
		query.append("AND ppe.persona.documento=:documentPerson ");
		Query q = em.createQuery(query.toString());
		q.setParameter("documentPerson", documentPerson);
		List<PermisoPersonaEmpresa> listPermissionPersonBusiness = q
				.getResultList();
		if (listPermissionPersonBusiness.size() > 0) {
			return listPermissionPersonBusiness.get(0);
		}
		return null;
	}

	/**
	 * Method that checks whether a farm is the default in the company to the
	 * permissions of a user.
	 * 
	 * @modify 04/05/2016 Wilhelm.Boada
	 * 
	 * @param idPerson
	 *            : ID of the person to verify
	 * @param idCompany
	 *            : Company ID to verify
	 * @param idFarm
	 *            : ID to verify the farm
	 * @return True if the property is predetermined, False otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean farmPredetermined(int idPerson, int idCompany, int idFarm)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe.empresa FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.persona.id=:idPerson ");
		query.append("AND  ppe.empresa.id = :idCompany ");
		query.append("AND  ppe.farm.idFarm = :idFarm ");
		query.append("AND  ppe.predeterminado IS TRUE ");
		query.append("AND (ppe.fechaFinVigencia IS NULL ");
		query.append("OR ppe.fechaFinVigencia >= :currentDate) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idPerson", idPerson);
		q.setParameter("idFarm", idFarm);
		q.setParameter("idCompany", idCompany);
		q.setParameter("currentDate", new Date());
		List<Business> business = q.getResultList();
		if (business != null && business.size() > 0) {
			return true;
		}
		return false;
	}

}
