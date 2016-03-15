package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Crops;

/**
 * Class DAO that establishes the connection between the business logic and the
 * database to manage crops.
 * 
 * @author Johnatan.Naranjo
 * @modify Dario.Lopez 16/04/2015
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class CropsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This query crops a certain range method as a parameter sent and filtering
	 * the information sent search values.
	 * 
	 * @modify 14/05/2015 Sergio.Ortiz
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Crops>: list of crops.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Crops> consultCrops(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM  Crops c ");
		query.append("JOIN FETCH c.cropNames ");
		query.append(consult);
		query.append("ORDER BY c.idCrop ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Crops> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a crop in BD.
	 * 
	 * @param crops
	 *            : crop saved.
	 * @throws Exception
	 */
	public void saveCrops(Crops crops) throws Exception {
		em.persist(crops);
	}

	/**
	 * Edits a crop in BD.
	 * 
	 * @param crops
	 *            : crop edited.
	 * @throws Exception
	 */
	public void editCrops(Crops crops) throws Exception {
		em.merge(crops);
	}

	/**
	 * Deletes the crops of the database.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param crops
	 *            : crops to eliminate.
	 * @throws Exception
	 */
	public void removeCrops(Crops crops) throws Exception {
		em.remove(em.merge(crops));
	}

	/**
	 * Crops method that queries the object by its ID.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param id
	 *            : Harvest identifier to consult.
	 * 
	 * @return: Crops object found with the search parameter identifier.
	 * @throws Exception
	 */
	public Crops cropsById(int id) throws Exception {
		return em.find(Crops.class, id);
	}

	/**
	 * Returns the number of existing crops in the database by filtering
	 * information sent lookup values.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of crop records found.
	 * @throws Exception
	 */
	public Long quantityCrops(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(c) FROM Crops c ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Method allows us to obtain the identifier given harvest.
	 * 
	 * @param id
	 *            : identifier crop we want to find.
	 * @return Crops: crop we found.
	 * @throws Exception
	 */
	public Crops cropsXID(int id) throws Exception {
		return em.find(Crops.class, id);
	}

	/**
	 * This method returns a list of all harvests name crops that are in place.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idCropsName
	 *            : valid name identifier crop.
	 * @return List<Crops>: List of crops.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Crops> consultCropNamesCropsCurrent(int idCropsName)
			throws Exception {
		return em
				.createQuery(
						"SELECT c FROM Crops c "
								+ "WHERE c.cropNames.idCropName=:idCropsName "
								+ "ORDER BY c.description ASC")
				.setParameter("idCropsName", idCropsName).getResultList();
	}

	/**
	 * See also article assigned to crops, considering that they are only those
	 * that are not null in the table.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param nomObject
	 *            : object found on the farm.
	 * @param idCrop
	 *            : id crop being queried.
	 * @return Object information associated with the crop or null but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectCrop(String nomObject, int idCrop)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT c." + nomObject
								+ " FROM Crops c WHERE c.id=:idCrop")
				.setParameter("idCrop", idCrop).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}

	/**
	 * Consult a specific crops by passing the description of this as a
	 * parameter.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param crop
	 *            : description of the crop to search.
	 * @return Crops: Crops object type.
	 * @throws Exception
	 */
	public Crops descriptionSearch(String crop) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM Crops c ");
		query.append("JOIN FETCH c.cropNames cn ");
		query.append("WHERE c.description LIKE :crop ");
		Query q = em.createQuery(query.toString());
		q.setParameter("crop", crop);
		return (Crops) q.getSingleResult();
	}

	/**
	 * This query crops a certain range method as a parameter sent and filtering
	 * the information sent search values.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return List<Crops>: list of crops.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Crops> listCrops() throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT c FROM  Crops c ");
		query.append("JOIN FETCH c.cropNames cn ");
		query.append("ORDER BY c.description ");
		Query q = em.createQuery(query.toString());
		List<Crops> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}
}
