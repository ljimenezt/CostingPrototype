package co.informatix.erp.recursosHumanos.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.security.entities.Usuario;

/**
 * This method takes care of the administration in the database of people and
 * their associated roles.
 * 
 * @author Angelica.Amaya
 * @modify Luis.Ruiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class PersonaDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Method that allows people to consult existing database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List of people found null or otherwise.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Persona> consultPersons(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM Persona p ");
		query.append(consult);
		query.append("ORDER BY p.nombres, p.apellidos ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		return q.getResultList();
	}

	/**
	 * Method that allows to see the amount of people in the existing database.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return number of people found in the database.
	 * @throws Exception
	 */
	public Long quantityPersons(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(p) FROM Persona p ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method makes the registration of the person in the database
	 * information system.
	 * 
	 * @author Angelica.Amaya
	 * 
	 * @param person
	 *            : object recorded in the database.
	 * 
	 * @throws Exception
	 */
	public void createPerson(Persona person) throws Exception {
		em.persist(person);
	}

	/**
	 * This method is the one to make the update in the database for a given
	 * user.
	 * 
	 * @author Angelica.Amaya.
	 * 
	 * @param person
	 *            : subject to updating.
	 * 
	 * @throws Exception
	 */
	public void editPerson(Persona person) throws Exception {
		em.merge(person);
	}

	/**
	 * This method makes consulting a person considering his identity.
	 * 
	 * @param id
	 *            :ID of the person.
	 * @return person: Person object found by its id or null but there.
	 * @throws Exception
	 */
	public Persona consultPerson(Integer id) throws Exception {
		Persona person = em.find(Persona.class, id);
		return person;
	}

	/**
	 * Allows the user to consult related to user database.
	 * 
	 * @param user
	 *            : Login person related to the user or person.
	 * @return Persona: object founded or null but there person.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Persona consultPersonUser(Usuario user) throws Exception {
		List<Persona> results = em
				.createQuery("FROM Persona WHERE usuario=:user")
				.setParameter("user", user).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method for querying an Object depending on the condition sent as a
	 * parameter.
	 * 
	 * @author marisol.calderon
	 * 
	 * @param nomObject
	 *            : name of the object to be consulted on the person.
	 * @param idPerson
	 *            : id entity of the person to consult.
	 * @return Object with information related to the person or null but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectPerson(String nomObject, int idPerson)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p.");
		query.append(nomObject);
		query.append(" FROM Persona p WHERE p.id=:idPerson");
		Query q = em.createQuery(query.toString());
		q.setParameter("idPerson", idPerson);
		List<Object> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList.get(0);
		}
		return null;
	}

	/**
	 * This method validates whether currently existing document of a person
	 * when register or edit
	 * 
	 * @param document
	 *            : Document number of the person to be validated.
	 * @param idTypeDocument
	 *            : document type identifier of the person.
	 * @return Persona: Person object found by the number and type of document
	 *         or null but there.
	 * @throws Exception
	 */
	public Persona validatePersonByDocument(String document,
			short idTypeDocument) throws Exception {
		List<Persona> results = em
				.createQuery(
						"SELECT p FROM Persona p "
								+ "JOIN FETCH p.tipoDocumento td "
								+ "WHERE UPPER(documento)=:document "
								+ "AND td.id=:idTypeDocument ", Persona.class)
				.setParameter("document", document)
				.setParameter("idTypeDocument", idTypeDocument).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}