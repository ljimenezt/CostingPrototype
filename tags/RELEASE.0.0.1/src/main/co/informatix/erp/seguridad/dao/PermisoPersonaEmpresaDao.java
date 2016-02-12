package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.organizaciones.entities.Hacienda;
import co.informatix.erp.organizaciones.entities.Sucursal;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.seguridad.entities.PermisoPersonaEmpresa;
import co.informatix.erp.utils.Constantes;

/**
 * DAO establishing the connection between business logic and database for
 * handling the relationship between people with business, branches and
 * properties to control access from the action PermisoPersonaEmpresaAction.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class PermisoPersonaEmpresaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save a person who is assigned access rights to a company.
	 * 
	 * @param permisoPersonaEmpresa
	 *            : Company relationship with the person you want to save.
	 * @throws Exception
	 */
	public void guardarPermisoPersonaEmpresa(
			PermisoPersonaEmpresa permisoPersonaEmpresa) throws Exception {
		em.persist(permisoPersonaEmpresa);
	}

	/**
	 * Modifies a person who is assigned access rights to a company.
	 * 
	 * @param permisoPersonaEmpresa
	 *            : Company relationship with the person you want to modify.
	 * @throws Exception
	 */
	public void modificarPermisoPersonaEmpresa(
			PermisoPersonaEmpresa permisoPersonaEmpresa) throws Exception {
		em.merge(permisoPersonaEmpresa);
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
	 * @return List of companies associated with access permissions to the
	 *         person.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PermisoPersonaEmpresa> consultarEmpresasAccesoPersona(
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
	 * @param consulta
	 *            : Consultation records depending on the parameters selected by
	 *            the user.
	 * @param parametros
	 *            : query parameters.
	 * @return Long: Number of records found.
	 * @throws Exception
	 */
	public Long cantidadEmpresasAccesoPersona(StringBuilder consulta,
			List<SelectItem> parametros) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(ppe) FROM PermisoPersonaEmpresa ppe ");
		query.append(consulta);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parametros) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method that allows consulting information in the relationships you have
	 * the permiso_persona_empresa the table.
	 * 
	 * @param permisoPersonaEmpresa
	 *            : covered by the permit of the person in the company.
	 * @return permisoPersonaEmpresa: The found object information detail to
	 *         permit a person in business.
	 * @throws Exception
	 */
	public PermisoPersonaEmpresa consultarDetallesPermisoPersonaEmpresa(
			PermisoPersonaEmpresa permisoPersonaEmpresa) throws Exception {
		Integer id = permisoPersonaEmpresa.getId();
		Empresa empresa = (Empresa) consultarObjetoPermisoPersonaEmpresa(
				Constantes.EMPRESA, id);
		Persona persona = (Persona) consultarObjetoPermisoPersonaEmpresa(
				Constantes.PERSONA, id);
		Sucursal sucursal = (Sucursal) consultarObjetoPermisoPersonaEmpresa(
				Constantes.SUCURSAL, id);
		Hacienda hacienda = (Hacienda) consultarObjetoPermisoPersonaEmpresa(
				Constantes.HACIENDA, id);
		permisoPersonaEmpresa.setEmpresa(empresa);
		permisoPersonaEmpresa.setPersona(persona);
		if (sucursal != null) {
			permisoPersonaEmpresa.setSucursal(sucursal);
		} else {
			permisoPersonaEmpresa.setSucursal(new Sucursal());
		}
		if (hacienda != null) {
			permisoPersonaEmpresa.setHacienda(hacienda);
		} else {
			permisoPersonaEmpresa.setHacienda(new Hacienda());
		}
		return permisoPersonaEmpresa;
	}

	/**
	 * See also article assigned to a permisoPersonaEmpresa depending on the
	 * parameters sent.
	 * 
	 * @param nomObject
	 *            : in order to consult permisoPersonaEmpresa.
	 * @param idPermisoPersonaEmpresa
	 *            : permisoPersonaEmpresa ID is consulted.
	 * @return The found object information that is associated with the business
	 *         person permission.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultarObjetoPermisoPersonaEmpresa(String nomObject,
			int idPermisoPersonaEmpresa) throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT ppe." + nomObject
								+ " FROM PermisoPersonaEmpresa ppe "
								+ "WHERE ppe.id=:idPermisoPersonaEmpresa")
				.setParameter("idPermisoPersonaEmpresa",
						idPermisoPersonaEmpresa).getResultList();
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
	 * @param documentoPersona
	 *            : person document in session.
	 * @return List<PermisoPersonaEmpresa>: List of permits to which the person
	 *         has access.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<PermisoPersonaEmpresa> consultarPermisosPersonaEmpresaAccesoUsuario(
			String documentoPersona) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.persona.documento=:documentoPersona ");
		query.append("AND (ppe.fechaFinVigencia IS NULL ");
		query.append("OR ppe.fechaFinVigencia >= :fechaActual) ");
		query.append("ORDER BY ppe.empresa.nombre ");
		Query q = em.createQuery(query.toString());
		q.setParameter("documentoPersona", documentoPersona);
		q.setParameter("fechaActual", new Date());
		return q.getResultList();
	}

	/**
	 * Method to consult if there is a company with permits
	 * (PermisoPersonaEmpresa) default user or person.
	 * 
	 * @param documentoPersona
	 *            : document of the person to whom permits the company is
	 *            consulted.
	 * @return PermisoPersonaEmpresa object that is predetermined or null but
	 *         there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public PermisoPersonaEmpresa consultarExistePredeterminado(
			String documentoPersona) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.predeterminado = TRUE ");
		query.append("AND ppe.persona.documento=:documentoPersona ");
		Query q = em.createQuery(query.toString());
		q.setParameter("documentoPersona", documentoPersona);
		List<PermisoPersonaEmpresa> listaPerPerEmp = q.getResultList();
		if (listaPerPerEmp.size() > 0) {
			return listaPerPerEmp.get(0);
		}
		return null;
	}

	/**
	 * Method that checks whether a farm is the default in the company to the
	 * permissions of a user.
	 * 
	 * @param idPersona
	 *            : ID of the person to verify
	 * @param idEmpresa
	 *            : Company ID to verify
	 * @param idHacienda
	 *            : ID to verify the farm
	 * @return True if the property is predetermined, False otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean haciendaPredeterminada(int idPersona, int idEmpresa,
			int idHacienda) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe.empresa FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.persona.id=:idPersona ");
		query.append("AND  ppe.empresa.id = :idEmpresa ");
		query.append("AND  ppe.hacienda.id = :idHacienda ");
		query.append("AND  ppe.predeterminado IS TRUE ");
		query.append("AND (ppe.fechaFinVigencia IS NULL ");
		query.append("OR ppe.fechaFinVigencia >= :fechaActual) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idPersona", idPersona);
		q.setParameter("idHacienda", idHacienda);
		q.setParameter("idEmpresa", idEmpresa);
		q.setParameter("fechaActual", new Date());
		List<Empresa> empresas = q.getResultList();
		if (empresas != null && empresas.size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Method that checks whether a business is the default for a user
	 * 
	 * @author Fredy.Vera
	 * @param documentoPersona
	 *            : User documentation to verify
	 * @param empresa
	 *            : Company to verify
	 * @return True if the default company, False otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public boolean empresaPredeterminada(String documentoPersona,
			Empresa empresa) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT ppe.empresa FROM PermisoPersonaEmpresa ppe ");
		query.append("WHERE ppe.persona.documento=:documentoPersona ");
		query.append("AND  ppe.empresa.id = :idEmpresa ");
		query.append("AND  ppe.predeterminado IS TRUE ");
		query.append("AND (ppe.fechaFinVigencia IS NULL ");
		query.append("OR ppe.fechaFinVigencia >= :fechaActual) ");
		Query q = em.createQuery(query.toString());
		q.setParameter("documentoPersona", documentoPersona);
		q.setParameter("idEmpresa", empresa.getId());
		q.setParameter("fechaActual", new Date());
		List<Empresa> empresas = q.getResultList();

		if (empresas != null && empresas.size() > 0) {
			return true;
		}
		return false;
	}
}
