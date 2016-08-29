package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.MachineTypes;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the types of machines.
 * 
 * @author Dario.Lopez
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MachineTypesDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @param start
	 *            :where he started the consultation record
	 * @param range
	 *            : range of records
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<MachineTypes>: list of the types of machines.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MachineTypes> consultMachineTypes(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mt FROM  MachineTypes mt ");
		query.append(consult);
		query.append("ORDER BY mt.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<MachineTypes> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Saves a machine type BDs
	 * 
	 * @param machineTypes
	 *            : one machine to save.
	 * @throws Exception
	 */
	public void saveMachineTypes(MachineTypes machineTypes) throws Exception {
		em.persist(machineTypes);
	}

	/**
	 * Edits a machine type BD
	 * 
	 * @param machineTypes
	 *            : type of machine to edit.
	 * @throws Exception
	 */
	public void editMachineTypes(MachineTypes machineTypes) throws Exception {
		em.merge(machineTypes);
	}

	/**
	 * Deletes a machine type BD
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param machineTypes
	 *            : type of machine to remove
	 * @throws Exception
	 */
	public void removeMachineTypes(MachineTypes machineTypes) throws Exception {
		em.remove(em.merge(machineTypes));
	}

	/**
	 * Returns the number of types of existing machines in the database
	 * information by filtering the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found type of machine
	 * @throws Exception
	 */
	public Long quantityMachineTypes(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(mt) FROM MachineTypes mt ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consultation if one machine exists in the database when storing or
	 * editing.
	 * 
	 * @param name
	 *            : type of machine to verify
	 * @param id
	 *            : id the type of machine to verify
	 * @return MachineTypes: machineType object found with the search parameters
	 *         name and id.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public MachineTypes nameExists(String name, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT mt FROM MachineTypes mt ");
		query.append("WHERE UPPER(mt.name)=UPPER(:name) ");
		if (id != 0) {
			query.append("AND mt.idMachineType <>:idMachineType ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		if (id != 0) {
			q.setParameter("idMachineType", id);
		}
		List<MachineTypes> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Method that I see all kinds MachineTypes object and stores it in a list
	 * 
	 * @author Sergio.Ortiz
	 * @modify 15/03/2016 Wilhelm.Boada
	 * 
	 * @return List<MachineTypes>: MachineTypes List
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<MachineTypes> listMachineType() throws Exception {
		Query q = em
				.createQuery("SELECT mt FROM MachineTypes mt ORDER BY mt.name ");
		return q.getResultList();

	}

	/**
	 * Method consulting one machine associated with the machine
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param idMachine
	 *            : the machine identifier
	 * @return MachineTypes: Object of the machine types
	 * @throws Exception
	 */
	public MachineTypes machineTypeXMachine(int idMachine) throws Exception {
		Query q = em.createQuery(
				"SELECT m.machineTypes FROM Machines m  "
						+ " WHERE m.idMachine=:idMachine ").setParameter(
				"idMachine", idMachine);
		return (MachineTypes) q.getSingleResult();
	}

	/**
	 * Method that looks for the machineType that is associated with the
	 * idMachineType sent as parameter
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @param idMachineType
	 *            : the machine type identifier
	 * @return MachineTypes: machine type associated with the identifier
	 * @throws Exception
	 */
	public MachineTypes machineTypeXId(int idMachineType) throws Exception {
		Query q = em.createQuery(
				"SELECT mt FROM MachineTypes mt "
						+ " WHERE mt.idMachineType=:idMachineType ")
				.setParameter("idMachineType", idMachineType);
		return (MachineTypes) q.getSingleResult();
	}
}