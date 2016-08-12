package co.informatix.erp.warehouse.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.utils.Constantes;
import co.informatix.erp.warehouse.entities.Deposits;
import co.informatix.erp.warehouse.entities.Materials;

/**
 * DAO class that establishes the connection between business logic and
 * database. DepositsAction used for managing Deposits.
 * 
 * @author Sergio.Ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class DepositsDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a Deposits in the database.
	 * 
	 * @param deposits
	 *            : Save Deposits.
	 * @throws Exception
	 */
	public void saveDeposits(Deposits deposits) throws Exception {
		em.persist(deposits);
	}

	/**
	 * Modify a Deposits in the database.
	 * 
	 * @param deposits
	 *            : Deposits to edit.
	 * @throws Exception
	 */
	public void editDeposits(Deposits deposits) throws Exception {
		em.merge(deposits);
	}

	/**
	 * Removes the BD Deposits.
	 * 
	 * @param deposits
	 *            : Deposits to be removed
	 * @throws Exception
	 */
	public void deleteDeposits(Deposits deposits) throws Exception {
		em.remove(em.merge(deposits));
	}

	/**
	 * Consult the list of Deposits that comply with the option of force.
	 * 
	 * @modify 07/03/2016 Gerardo.Herrera
	 * @modify 14/04/2016 Wilhelm.Boada
	 * 
	 * @param start
	 *            : Registry where consultation begins
	 * @param range
	 *            : Range of records
	 * @param consult
	 *            : Consultation records depending on the parameters selected by
	 *            the user
	 * @param parameters
	 *            : Query parameters
	 * @return List<Deposits>:List of Deposits that comply with the condition of
	 *         validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Deposits> consultDeposits(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Deposits d ");
		query.append("JOIN FETCH d.materials m ");
		query.append("JOIN FETCH m.materialType ");
		query.append("JOIN FETCH m.measurementUnits ");
		query.append("JOIN FETCH d.purchaseInvoices p ");
		query.append(consult);
		query.append(" ORDER BY d.dateTime");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Deposits> deposits = q.getResultList();
		return deposits;
	}

	/**
	 * Returns the number of existing Deposits in the database that are existing
	 * or not existing.
	 * 
	 * @modify 14/04/2016 Wilhelm.Boada
	 * 
	 * @param queryBuilder
	 *            : Query running on SQL.
	 * @param parameters
	 *            :Parameters of the query.
	 * @return Number of records found.
	 * @throws Exception
	 */
	public Long amountDeposits(StringBuilder queryBuilder,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(d) FROM Deposits d ");
		query.append("JOIN d.materials m ");
		query.append("JOIN d.purchaseInvoices p ");
		query.append(queryBuilder);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method allows consult a material that are associated with a deposit.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param idMaterial
	 *            :material identifier to find in the deposit.
	 * @return boolean: true if find deposits associated.
	 * @throws Exception
	 */
	public boolean associatedMaterialsDeposits(int idMaterial) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Deposits d ");
		query.append("WHERE d.materials.idMaterial=:idMaterial ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", idMaterial);
		if (q.getResultList().size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * This method allows to consult if there is a deposit with a material and a
	 * specific invoice.
	 * 
	 * @author Gerardo.Herrera
	 * @modify 19/04/2016 Wilhelm.Boada
	 * 
	 * @param material
	 *            : Material identifier to find in the deposit.
	 * @param numberInvoice
	 *            : Invoice number.
	 * @param idSupplier
	 *            : Supplier identifier belonging to a purchase invoice.
	 * @return boolean: true if the deposit exists, and false otherwise.
	 * @throws Exception
	 */
	public boolean existsDeposit(Materials material, String numberInvoice,
			int idSupplier) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Deposits d ");
		query.append("JOIN FETCH d.purchaseInvoices pi ");
		query.append("WHERE d.materials.idMaterial=:idMaterial ");
		query.append("AND pi.suppliers.idSupplier=:idSupplier ");
		query.append("AND pi.invoiceNumber LIKE :numberInvoice ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", material.getIdMaterial());
		q.setParameter("idSupplier", idSupplier);
		q.setParameter("numberInvoice", numberInvoice);
		if (q.getResultList().size() > 0) {
			return true;
		}
		return false;
	}

	/**
	 * Returns the sum of material quantity register in the deposit.
	 * 
	 * @author Wilhelm.Boada
	 * @modify 14/07/2016 Andres.Gomez
	 * 
	 * @param idMaterial
	 *            : Material identifier.
	 * @param finalDate
	 *            : Date object to filter the deposits
	 * @return Double: Number of records found.
	 * @throws Exception
	 */
	public Double quantityMaterialsById(int idMaterial, Date finalDate)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(d.actualQuantity) FROM Deposits d ");
		query.append("WHERE d.materials.idMaterial=:idMaterial ");
		if (finalDate != null) {
			query.append("AND d.dateTime <= :finalDate ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", idMaterial);
		if (finalDate != null) {
			q.setParameter("finalDate", finalDate);
		}
		return (Double) q.getSingleResult();
	}

	/**
	 * This method allows consult the deposits in the database filtering
	 * information search by the values sent.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param idMaterial
	 *            : Material identifier.
	 * @return List<Deposits>:List of Deposits that comply with the condition of
	 *         validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Deposits> consultDepositsActualQuantityById(int idMaterial)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Deposits d ");
		query.append("WHERE d.materials.idMaterial=:idMaterial ");
		query.append("AND d.actualQuantity>0 ");
		query.append("ORDER BY d.dateTime, d.idDeposit ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", idMaterial);
		List<Deposits> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult the deposits in the database filtering
	 * information search by the values sent.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param idMaterial
	 *            : Material identifier.
	 * @param idActivity
	 *            : Activity identifier.
	 * @param idTransactionType
	 *            : TransactionType identifier.
	 * @return List<Deposits>:List of Deposits that comply with the condition of
	 *         validity.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Deposits> consultDepositsByTransactions(int idMaterial,
			int idActivity, int idTransactionType) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT d FROM Transactions t ");
		query.append("JOIN t.deposits d ");
		query.append("WHERE d.materials.idMaterial=:idMaterial ");
		query.append("AND t.activities.idActivity=:idActivity ");
		query.append("AND t.transactionType.idTransactionType=:idTransactionType ");
		query.append("ORDER BY d.dateTime DESC, d.idDeposit DESC ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", idMaterial);
		q.setParameter("idActivity", idActivity);
		q.setParameter("idTransactionType", idTransactionType);
		List<Deposits> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns the sum of material quantity multiplied by the value of the unit
	 * cost register in the deposit.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param idMaterial
	 *            : Material identifier.
	 * @param finalDate
	 *            : Date object to filter the deposits
	 * @return Double: Number of records found.
	 * @throws Exception
	 */
	public Double calculateTotalCost(int idMaterial, Date finalDate)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT SUM(d.actualQuantity * d.unitCost) ");
		query.append("FROM Deposits d ");
		query.append("WHERE d.materials.idMaterial=:idMaterial ");
		if (finalDate != null) {
			query.append("AND d.dateTime <= :finalDate ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("idMaterial", idMaterial);
		if (finalDate != null) {
			q.setParameter("finalDate", finalDate);
		}
		return (Double) q.getSingleResult();
	}

	/**
	 * This method allow consult the deposit according to range selected by the
	 * user
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param finalDate
	 *            : Date object to filter the deposits
	 * @return List<Integer>:List of id's of the material that comply with the
	 *         condition of validity.
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> consultIdsMaterialXDepositsByRange(Date finalDate) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m.idMaterial FROM Deposits d ");
		query.append("JOIN d.materials m ");
		query.append("WHERE d.dateTime <= :finalDate ");
		Query q = em.createQuery(query.toString());
		q.setParameter("finalDate", finalDate);
		List<Integer> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * This method allows consult of the inventory of the materials in deposit.
	 * This native query, because is information of the report.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param finalDate
	 *            : Date object to filter the information of the inventory
	 * @return list of the object with the deposit, material and transaction
	 *         information.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> consultoInventoryByDepositReport(Date finalDate)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT m.name as material, mt.name as materialtype, ");
		query.append("		 d.initial_quantity, t.date_time as datetransaction, tt.transactiontype, ");
		query.append("		 t.quantity, d.actual_quantity, m.idmaterial, ");
		query.append("CASE WHEN idtransactiontype != :transactiontype THEN t.quantity ELSE 0.0 END as Salida, ");
		query.append("CASE WHEN idtransactiontype = :transactiontype THEN t.quantity ELSE 0.0 END as Entrada ");
		query.append("FROM warehouse.materials_types mt, warehouse.materials m, ");
		query.append("	   warehouse.deposits d, warehouse.transactions t,  ");
		query.append("	   warehouse.transaction_type tt ");
		query.append("WHERE m.id_material_type = mt.idmaterialtype ");
		query.append("AND m.idmaterial = d.id_material ");
		query.append("AND d.iddeposit = t.id_deposit ");
		query.append("AND t.id_transaction_type = tt.idtransactiontype ");
		if (finalDate != null) {
			query.append("AND t.date_time <= :finalDate ");
		}
		query.append("UNION ALL SELECT m.name as material,  mt.name as materialtype, ");
		query.append("	d.initial_quantity, d.date_time as datetransaction, ");
		query.append("	varchar(7) 'Deposit' as transactiontype, float8(0) as quantity, ");
		query.append("	float8(0) as actualq, m.idmaterial, float8(0) as salida, ");
		query.append("	d.initial_quantity as entrada ");
		query.append("FROM warehouse.materials m, warehouse.deposits d, ");
		query.append("	   warehouse.materials_types mt ");
		query.append("WHERE m.idmaterial = d.id_material ");
		query.append("AND m.id_material_type = mt.idmaterialtype  ");
		if (finalDate != null) {
			query.append("AND d.date_time <= :finalDate ");
		}
		query.append("ORDER BY 1,4 ");
		Query q = em.createNativeQuery(query.toString());
		q.setParameter("transactiontype", Constantes.TRANSACTION_TYPE_RETURN);
		if (finalDate != null) {
			q.setParameter("finalDate", finalDate);
		}
		return q.getResultList();
	}

	/**
	 * This method allow consult the months of the deposits according to range
	 * selected by the user
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param finalDate
	 *            : Date object to filter the deposits
	 * @return List<String>:List of id's of the material that comply with the
	 *         condition of validity.
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> consultMonths(Date finalDate) {
		StringBuilder query = new StringBuilder();
		query.append("SELECT DISTINCT CAST(date_part('month',t.date_time) as int) ");
		query.append("FROM warehouse.materials m, ");
		query.append("	   warehouse.deposits d, ");
		query.append("	   warehouse.transactions t, ");
		query.append("	   warehouse.transaction_type tt ");
		query.append("WHERE m.idmaterial = d.id_material ");
		query.append("AND d.iddeposit = t.id_deposit ");
		query.append("AND t.id_transaction_type = tt.idtransactiontype ");
		if (finalDate != null) {
			query.append("AND t.date_time <= :finalDate ");
		}
		query.append("UNION SELECT DISTINCT CAST(date_part('month',d.date_time) as int) ");
		query.append("FROM warehouse.materials m, warehouse.deposits d, ");
		query.append("     warehouse.materials_types mt ");
		query.append("WHERE m.idmaterial = d.id_material ");
		query.append("AND m.id_material_type = mt.idmaterialtype ");
		if (finalDate != null) {
			query.append("AND d.date_time <= :finalDate ");
		}
		query.append("ORDER BY 1 DESC");
		Query q = em.createNativeQuery(query.toString());
		if (finalDate != null) {
			q.setParameter("finalDate", finalDate);
		}
		List<Integer> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

}
