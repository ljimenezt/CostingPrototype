package co.informatix.erp.organizaciones.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.organizaciones.entities.Lote;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the batch entity.
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class LoteDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save the information for a batch record.
	 * 
	 * @param lote
	 *            : lot object to add.
	 * @throws Exception
	 */
	public void guardarLote(Lote lote) throws Exception {
		em.persist(lote);
	}

	/**
	 * Edit the record information for a lot.
	 * 
	 * @param lote
	 *            : Object Lot editing.
	 * @throws Exception
	 */
	public void editarLote(Lote lote) throws Exception {
		em.merge(lote);
	}

	/**
	 * Returns the number of existing lots in the database that are in force.
	 * 
	 * @param consult
	 *            : Consultation records depending on user-selected parameters.
	 * @param parameters
	 *            : parameters for the query.
	 * @return Long:number of lots found in the database.
	 * @throws Exception
	 */
	public Long cantidadLotes(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(l) FROM Lote l ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * 
	 * Allows to consult existing lots in the BD, in relation to the condition
	 * of force sent.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Lote>: List of lots that meet the condition of validity
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Lote> consultarLotes(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT l FROM Lote l ");
		query.append("JOIN FETCH l.unidadMedidaArea ");
		query.append("JOIN FETCH l.unidadMedidaDensidad ");
		query.append("JOIN FETCH l.unidadMedidaTamX ");
		query.append("JOIN FETCH l.unidadMedidaTamY ");
		query.append(consult);
		query.append("ORDER BY l.nombre ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Queries if the batch name exists in the database at the time of editing.
	 * 
	 * @param name
	 *            : name check.
	 * @param id
	 *            : Lot identifier.
	 * @param idZona
	 *            : the area identifier.
	 * @return Lote: found bundle object named.
	 * 
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Lote nombreExisteActualizar(String name, int id, int idZona)
			throws Exception {
		List<Lote> results = em
				.createQuery(
						"FROM Lote l WHERE codigo=:nombre AND id<>:id "
								+ "AND l.zona.id=:idZona")
				.setParameter("nombre", name).setParameter("idZona", idZona)
				.setParameter("id", id).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Queries if the batch name exists in the database when you save it.
	 * 
	 * @param name
	 *            : name to verify
	 * @param idZona
	 *            : the area identifier.
	 * @return Lote: bundle object found with the name
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Lote nombreExiste(String name, int idZona) throws Exception {
		List<Lote> results = em
				.createQuery(
						"FROM Lote l WHERE codigo=:nombre "
								+ "AND l.zona.id=:idZona")
				.setParameter("nombre", name).setParameter("idZona", idZona)
				.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}
