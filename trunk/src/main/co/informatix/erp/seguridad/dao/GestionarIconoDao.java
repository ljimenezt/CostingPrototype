package co.informatix.erp.seguridad.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.security.entities.Icono;

/**
 * DAO class that establishes the connection between business logic and
 * database. IconoAction used for managing menu icons, has the name 'Manage'
 * that the jar contains security and IconoDao
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class GestionarIconoDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Allows consult the icons in the database
	 * 
	 * @param inicio
	 *            : Start Registry
	 * @param rango
	 *            : end in the range of records to consult
	 * @return List<Icono>: list of icons found in the database.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Icono> consultarIconos(int inicio, int rango) throws Exception {
		return em.createQuery("SELECT i FROM Icono i ORDER BY i.nombre")
				.setFirstResult(inicio).setMaxResults(rango).getResultList();
	}

	/**
	 * Allows consult the amount of icons on the database
	 * 
	 * @return Long: number of icons found in the database.
	 * @throws Exception
	 */
	public Long cantidadIconos() throws Exception {
		return (Long) em.createQuery("SELECT COUNT(i) FROM Icono i ")
				.getSingleResult();
	}

	/**
	 * Save an icon in the database.
	 * 
	 * @param icono
	 *            : icon to save
	 * @throws Exception
	 */
	public void guardarIcono(Icono icono) throws Exception {
		em.persist(icono);
	}

	/**
	 * Modifies an icon in the database.
	 * 
	 * @param icono
	 *            : icon to modify
	 * @throws Exception
	 */
	public void editarIcono(Icono icono) throws Exception {
		em.merge(icono);
	}

	/**
	 * Consultation If the icon name exists in the database when you save
	 * 
	 * @param nombre
	 *            : name to verify
	 * @return Icono: Icon object found with the name or null but there
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Icono nombreExiste(String nombre) throws Exception {
		List<Icono> results = em
				.createQuery("FROM Icono i WHERE UPPER(i.nombre)=:nombre ")
				.setParameter("nombre", nombre).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Deletes an icon of the database.
	 * 
	 * @param icono
	 *            : Object icon to delete
	 * @throws Exception
	 */
	public void eliminarIcono(Icono icono) throws Exception {
		Icono e = em.find(Icono.class, icono.getId());
		em.remove(e);
	}

	/**
	 * Consultation icons that meet the search criteria name, considering the
	 * beginning and end of the list.
	 * 
	 * @param start
	 *            : where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param nombreBuscar
	 *            : name by which the icon is sought.
	 * @return List of icons found with that name.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Icono> buscarIconosXNombrePaginado(int start, int range,
			String nombreBuscar) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT i FROM Icono i ");
		query.append("WHERE i.fechaFinVigencia IS NULL ");
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append("AND UPPER(i.nombre) LIKE  UPPER(:keyword) ");
		}
		query.append("ORDER BY i.nombre ");
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Allows consult the amount of icons on the database, considering the name
	 * submitted as a parameter.
	 * 
	 * @param nombreBuscar
	 *            : name by which the icon is sought.
	 * 
	 * @return Long: number of icons found in the database.
	 * @throws Exception
	 */
	public Long cantidadIconosPorNombre(String nombreBuscar) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(i) FROM Icono i ");
		query.append("WHERE i.fechaFinVigencia IS NULL ");
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			query.append("AND UPPER(i.nombre) LIKE  UPPER(:keyword) ");
		}
		Query q = em.createQuery(query.toString());
		if (nombreBuscar != null && !"".equals(nombreBuscar)) {
			q.setParameter("keyword", "%" + nombreBuscar + "%");
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Allows consult the amount of icons by name in the existing database
	 * 
	 * @param nombreBuscar
	 *            : name by which the icon is sought
	 * @return Long: number of icons found
	 * @throws Exception
	 */
	public Long cantidadIconosXNombre(String nombreBuscar) throws Exception {
		return (Long) em
				.createQuery(
						"SELECT COUNT(i) FROM Icono i "
								+ "WHERE UPPER(i.nombre) LIKE :keyword ")
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.getSingleResult();
	}

	/**
	 * Method for querying name icons considering a beginning and a range
	 * 
	 * @param nombreBuscar
	 *            : Method that allows consulting name icons considering a
	 *            beginning and a range
	 * @param inicio
	 *            : Start the registration
	 * @param rango
	 *            : end in the range of records to consult
	 * @return List<Icono>: list of icons found.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Icono> consultarIconosXNombrePaginador(String nombreBuscar,
			int inicio, int rango) throws Exception {
		return em
				.createQuery(
						"SELECT i FROM Icono i "
								+ "WHERE UPPER(i.nombre) LIKE :keyword "
								+ "ORDER BY i.nombre")
				.setParameter("keyword", "%" + nombreBuscar + "%")
				.setFirstResult(inicio).setMaxResults(rango).getResultList();
	}

}
