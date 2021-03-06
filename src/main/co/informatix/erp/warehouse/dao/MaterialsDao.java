package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.warehouse.entities.Materials;
import co.informatix.erp.warehouse.entities.PurchaseInvoices;

/**
 * DAO class that establishes the connection between business logic and
 * database. MaterialsAction manages Materials.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MaterialsDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Look for a list of materials according to the parameters.
	 * 
	 * @param start
	 *            : First record that is retrieved from the query result.
	 * @param range
	 *            : Range of records.
	 * @param query
	 *            : Query records depending on the parameters selected by the
	 *            user.
	 * @param parameters
	 *            : Query parameters.
	 * @return List<Materials>: List of materials that comply with the
	 *         condition. of validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Materials> queryMaterials(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT m FROM Materials m ");
		queryBuilder.append(query);
		queryBuilder.append(" ORDER BY m.name");
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing materials in the database.
	 * 
	 * @param query
	 *            : SQL query.
	 * @param parameters
	 *            : Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long materialsAmount(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(m) FROM Materials m ");
		queryBuilder.append(query);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * It saves a material in the database.
	 * 
	 * @param materials
	 *            : Material to save.
	 * @throws Exception
	 */
	public void saveMaterials(Materials materials) throws Exception {
		em.persist(materials);
	}

	/**
	 * Modify a Material in the database.
	 * 
	 * @param materials
	 *            : Material to edit.
	 * @throws Exception
	 */
	public void editMaterials(Materials materials) throws Exception {
		em.merge(materials);
	}

	/**
	 * Removes a material of the database.
	 * 
	 * @param materials
	 *            : Material to be removed
	 * @throws Exception
	 */
	public void deleteMateriales(Materials materials) throws Exception {
		em.remove(em.merge(materials));
	}

	/**
	 * Consult a material object according to an identifier.
	 * 
	 * @param objectName
	 *            : Object that is contained in materials.
	 * @param materialsId
	 *            : Material Id.
	 * @return Object related to a material.
	 */
	@SuppressWarnings("unchecked")
	public Object queryMaterialObject(String objectName, int materialsId)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT m." + objectName + " FROM Materials m "
								+ "WHERE m.id=:idMaterials")
				.setParameter("idMaterials", materialsId).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Query materials associated with a material type.
	 * 
	 * @author Liseth.Jimenez
	 * @modify Gerardo.Herrera
	 * @modify 14/04/2016 Wilhelm.Boada
	 * 
	 * @param idMaterialType
	 *            : Materials type identifier to look for in materials.
	 * @return List<Materials>: Materials stored in data base for material type.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Materials> queryMaterialsByType(int idMaterialType)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT m FROM Materials m ");
		queryBuilder.append("JOIN FETCH m.measurementUnits ");
		queryBuilder
				.append(idMaterialType != 0 ? "WHERE m.materialType.idMaterialsType=:idMaterialType "
						: "");
		queryBuilder.append("ORDER BY m.name ");
		Query query = em.createQuery(queryBuilder.toString());
		if (idMaterialType != 0)
			query.setParameter("idMaterialType", idMaterialType);
		return query.getResultList();
	}

	/**
	 * It retrieves a Material from the database according to it name and
	 * presentation, because there should not be any two materials with the same
	 * name and presentation.
	 * 
	 * @author Sergio.Gelves
	 * @modify 11/04/2016 Liseth.Jimenez
	 * 
	 * @param name
	 *            : Material's name.
	 * @param presentation
	 *            : Material's presentation.
	 * @param idMeasurementUnits
	 *            : Identifier measurement unit
	 * @param idMaterial
	 *            : Identifier materials
	 * @return The matched material or null if there is none.
	 * @throws Exception
	 */
	public Materials materialByNamePresentation(String name,
			short presentation, int idMeasurementUnits, int idMaterial)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT m FROM Materials m ");
		queryBuilder
				.append("WHERE m.name=:name AND m.presentation=:presentation ");
		queryBuilder
				.append("AND m.measurementUnits.idMeasurementUnits=:idMeasurementUnits ");
		if (idMaterial != 0) {
			queryBuilder.append("AND m.idMaterial <>:idMaterial ");
		}
		Query query = em.createQuery(queryBuilder.toString());
		query.setParameter("name", name);
		query.setParameter("presentation", presentation);
		query.setParameter("idMeasurementUnits", idMeasurementUnits);
		if (idMaterial != 0) {
			query.setParameter("idMaterial", idMaterial);
		}
		if (query.getResultList().size() > 0) {
			return (Materials) query.getResultList().get(0);
		}
		return null;
	}

	/**
	 * This method queries all materials stored in the database and its
	 * information from measurement unit.
	 * 
	 * @author Andres.Gomez
	 * @modify 07/04/2016 Wilhelm.Boada
	 * 
	 * @param idMaterial
	 *            :identifier using like search criteria to consult
	 * @return Materials: material stored in the database according with the
	 *         identifier.
	 * @throws Exception
	 */
	public Materials consultMaterialsById(int idMaterial) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Materials m ");
		query.append("JOIN FETCH m.measurementUnits ");
		query.append("JOIN FETCH m.materialType mt ");
		query.append("WHERE m.idMaterial =:idMaterial ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", idMaterial);
		if (q.getResultList().size() > 0) {
			return (Materials) q.getResultList().get(0);
		}
		return null;
	}

	/**
	 * This method consult the material for invoice purchase.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param purchaseInvoice
	 *            : Purchase invoice object
	 * @return List<Materials>: List of materials.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Materials> materialsXInvoicePurchase(
			PurchaseInvoices purchaseInvoice) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Materials m ");
		query.append("JOIN FETCH m.measurementUnits ");
		query.append("WHERE m IN ");
		query.append("(SELECT m FROM InvoiceItems ii ");
		query.append("JOIN ii.material m ");
		query.append("WHERE ii.purchaseInvoice = :purchaseInvoice) ");
		query.append("ORDER BY m.name");
		Query q = em.createQuery(query.toString());
		q.setParameter("purchaseInvoice", purchaseInvoice);
		return q.getResultList();
	}
}