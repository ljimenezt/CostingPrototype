package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.CropNames;

@SuppressWarnings("serial")
@Stateless
/**
 * class DAO that establishes the connection between the business logic and database for handling the CropNames entity.
 * 
 * @author Johnatan.Naranjo
 * 
 */
public class CropNamesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method allows consulting crop names with a certain range sent as a
	 * parameter and filtering the information sent search values.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<CropNames>: list of the names of crops.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CropNames> consultCropNames(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cn FROM  CropNames cn ");
		query.append(consult);
		query.append("ORDER BY cn.cropName ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<CropNames> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a name for harvest in BD
	 * 
	 * @param cropNames
	 *            : crop name to save.
	 * @throws Exception
	 */
	public void saveCropNames(CropNames cropNames) throws Exception {
		em.persist(cropNames);
	}

	/**
	 * Edit a crop name in BD
	 * 
	 * @param cropNames
	 *            : crop name to edit.
	 * @throws Exception
	 */
	public void editCropNames(CropNames cropNames) throws Exception {
		em.merge(cropNames);
	}

	/**
	 * Removes a name crop BD
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param cropNames
	 *            : Crop name eliminate
	 * @throws Exception
	 */
	public void removeCropNames(CropNames cropNames) throws Exception {
		em.remove(em.merge(cropNames));
	}

	/**
	 * Returns the number of existing crops names in the database by filtering
	 * information sent lookup values.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of crop name records found
	 * @throws Exception
	 */
	public Long quantityCropNames(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(cn) FROM CropNames cn ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consultation if the name of the crop there in the database when storing
	 * or editing.
	 * 
	 * @param name
	 *            : crop name to verify
	 * @param id
	 *            : identifier harvest to verify
	 * @return CropNames: cropName object found with the search parameters name
	 *         and identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public CropNames nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cn FROM CropNames cn ");
		query.append("WHERE UPPER(cn.cropName)=UPPER(:cropName) ");
		if (id != 0) {
			query.append("AND cn.idCropName <>:idCropName ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("cropName", name);
		if (id != 0) {
			q.setParameter("idCropName", id);
		}
		List<CropNames> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * CropNames method that queries the object and saves it to a list
	 * 
	 * @author Mabell.Boada
	 * @return List<CropNames>: List of crops
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<CropNames> listCropNames() throws Exception {
		Query q = em.createQuery("SELECT cn FROM CropNames cn ");
		return q.getResultList();

	}

	/**
	 * CropNames method that queries the object by its ID
	 * 
	 * @author Mabell.Boada
	 * @param id
	 *            : Identifier harvest to consult
	 * @return: CropName object found with the search parameter identifier
	 * @throws Exception
	 */
	public CropNames cropNamesXId(int id) throws Exception {
		return em.find(CropNames.class, id);
	}

}
