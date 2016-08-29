package co.informatix.erp.machines.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.machines.entities.Machines;

/**
 * Class DAO that establishes the connection between business logic and database
 * for handling the machines.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class MachinesDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method build consult the machines with determined range sent as a
	 * parameter and filtering the information by the values sent search.
	 * 
	 * @modify 28/05/2015 Mabell.Boada
	 * @modify 19/08/2015 Andres.Gomez
	 * 
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Machines>: list of machines.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Machines> consultMachines(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Machines m ");
		query.append("JOIN FETCH m.machineTypes mt ");
		query.append(consult);
		query.append("ORDER BY m.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Machines> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Save a machine in DB.
	 * 
	 * @param machines
	 *            : machine to save.
	 * @throws Exception
	 */
	public void saveMachines(Machines machines) throws Exception {
		em.persist(machines);
	}

	/**
	 * Edits a machine in DB.
	 * 
	 * @param machines
	 *            : editing machine.
	 * @throws Exception
	 */
	public void editMachines(Machines machines) throws Exception {
		em.merge(machines);
	}

	/**
	 * Machines removed from the database.
	 * 
	 * @param machines
	 *            : machines to removes.
	 * @throws Exception
	 */
	public void removeMachines(Machines machines) throws Exception {
		em.remove(em.merge(machines));
	}

	/**
	 * Returns the number of existing machines in the database information by
	 * filtering the search values sent.
	 * 
	 * @modify 28/05/2015 Mabell.Boada
	 * @modify 19/08/2015 Andres.Gomez
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount number of machines found.
	 * @throws Exception
	 */
	public Long quantityMachines(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(m) FROM Machines m ");
		query.append("JOIN m.machineTypes mt ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Machines method that queries the object and saves it to a lists.
	 * 
	 * @author Mabell.Boada
	 * @modify 15/03/2016 Wilhelm.Boada
	 * 
	 * @return List<Machines>: List of machines.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Machines> listMachines() throws Exception {
		Query q = em.createQuery("SELECT m FROM Machines m "
				+ " ORDER BY m.name");
		return q.getResultList();
	}

	/**
	 * Method that queries the machine that is associated with the idMachines.
	 * 
	 * @param idMachine
	 *            : the machine identifier.
	 * @return Machines: machine associated with insurances.
	 * @throws Exception
	 */
	public Machines machinesXId(int idMachine) throws Exception {
		Query q = em.createQuery(
				"SELECT m FROM Machines m WHERE m.idMachine=:idMachine ")
				.setParameter("idMachine", idMachine);
		return (Machines) q.getSingleResult();
	}

	/**
	 * Method to query the machines associated with a type of machines.
	 * 
	 * @param idMachineType
	 *            : identifier idMachineType.
	 * @return List<Machines>: list of machines associated with MachineType.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Machines> listMachinesByTypes(int idMachineType)
			throws Exception {

		return em
				.createQuery(
						"SELECT m FROM Machines m "
								+ " WHERE m.machineTypes.idMachineType=:idMachineType "
								+ " ORDER BY m.name ")
				.setParameter("idMachineType", idMachineType).getResultList();
	}

	/**
	 * Consult article assigned to a machine, considering that are only those
	 * that are not null in the table.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param nomObject
	 *            : object to refer to human resources.
	 * @param idMachine
	 *            : Machine id being queried.
	 * @return Object information associated with machine or null if not exists.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectMachines(String nomObject, int idMachine)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT m."
								+ nomObject
								+ " FROM Machines m WHERE m.idMachine=:idMachine")
				.setParameter("idMachine", idMachine).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * This method allows query whether the serial exists in the database when
	 * creating or editing.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param value
	 *            : string value for validate.
	 * @param id
	 *            : identifier of the machine.
	 * @return Machines: machine object found with the serial
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Machines machineExist(String value, int id) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m FROM Machines m ");
		query.append("WHERE UPPER(m.serialNumber)=UPPER(:value) ");
		if (id != 0) {
			query.append("AND m.idMachine<>:id ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("value", value);
		if (id != 0) {
			q.setParameter("id", id);
		}
		List<Machines> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}
}