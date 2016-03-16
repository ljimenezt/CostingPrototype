package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Section;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling section action..
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class SectionDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a section in the DB
	 * 
	 * @param section
	 *            : one section to save.
	 * @throws Exception
	 */
	public void saveSection(Section section) throws Exception {
		em.persist(section);
	}

	/**
	 * Edits a section in the BD
	 * 
	 * @param section
	 *            : section to edit.
	 * @throws Exception
	 */
	public void editSection(Section section) throws Exception {
		em.merge(section);
	}

	/**
	 * Removes the BD section
	 * 
	 * @param section
	 *            : section to eliminate
	 * @throws Exception
	 */
	public void deleteSection(Section section) throws Exception {
		em.remove(em.merge(section));
	}

	/**
	 * This method allow made the consult the section by identifier
	 * 
	 * @param id
	 *            : Identifier of the section.
	 * @return Section: Section found according to the identifier sent like
	 *         parameter
	 */
	public Section sectionXId(int id) throws Exception {
		return em.find(Section.class, id);
	}

	/**
	 * Method that consult all section objects and stores it in a list
	 * 
	 * @return List<Section>: Section list
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Section> listSection() throws Exception {
		Query q = em.createQuery("SELECT s FROM Section s "
				+ " ORDER BY s.name ");
		return q.getResultList();
	}

	/**
	 * Returns the number of existing section in the database by filtering
	 * information sent search values.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of section records found
	 * @throws Exception
	 */
	public Long amountSection(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(s) FROM Section s ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method consult a particular section sent as a parameter range and
	 * filtering the information sent search values.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<section>: section list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Section> consultSections(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT s FROM  Section s ");
		query.append(consult);
		query.append("ORDER BY s.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Section> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Consultation if the name of the section exists in the database when
	 * storing or editing.
	 * 
	 * @param name
	 *            : section name to verify
	 * @param id
	 *            : identifier to verify the section
	 * @return Section: section object found with the search parameters name and
	 *         identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Section nameExist(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT s FROM Section s ");
		query.append("WHERE UPPER(s.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND s.idSection <>:idSection ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idSection", id);
		}
		List<Section> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}