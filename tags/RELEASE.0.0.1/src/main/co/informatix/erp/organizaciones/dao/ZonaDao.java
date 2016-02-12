package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizaciones.entities.Zona;

/**
 * Class DAO that establishes the connection between business logic and
 * database. ZonaAction used for the management of the areas that have the farm.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class ZonaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consultation areas with a range determinate sent as a
	 * parameter and filtering the information by the values sent search.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Zona>: List of zones in the database, or null otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Zona> consultarZonas(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT z FROM Zona z ");
		query.append(consult);
		query.append("ORDER BY z.nombre ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		if (range != 0) {
			q.setFirstResult(start).setMaxResults(range);
		}
		return q.getResultList();
	}

	/**
	 * Returns the number of existing or not existing areas existing in the
	 * database information by filtering the search values sent.
	 * 
	 * @param consult
	 *            : Consultation depend records the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : parameters for the query.
	 * @return Long: number of records in the database, or null otherwise.
	 * @throws Exception
	 */
	public Long cantidadZonas(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(z) FROM Zona z ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Save a zone in BD.
	 * 
	 * @param zona
	 *            : save area.
	 * @throws Exception
	 */
	public void guardarZona(Zona zona) throws Exception {
		em.persist(zona);
	}

	/**
	 * Edit the information area.
	 * 
	 * @param zona
	 *            : edit area.
	 * @throws Exception
	 */
	public void editarZona(Zona zona) throws Exception {
		em.merge(zona);
	}

	/**
	 * Query whether the Farm zone name exists in the database when you save it.
	 * 
	 * @param name
	 *            : name to search
	 * @param id
	 *            : ID in the editing area.
	 * @param idHacienda
	 *            : id of the farm to which the zone belongs.
	 * @return Zona: area found with the name or null if not found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Zona nombreZonaExiste(String name, int id, int idHacienda)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT z FROM Zona z ");
		query.append("WHERE UPPER(z.nombre)=UPPER(:nombre) ");
		query.append("AND z.hacienda.id=:idHacienda ");
		if (id != 0) {
			query.append("AND z.id <>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("nombre", name);
		q.setParameter("idHacienda", idHacienda);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<Zona> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
