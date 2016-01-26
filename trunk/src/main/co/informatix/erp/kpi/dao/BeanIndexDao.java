package co.informatix.erp.kpi.dao;

import java.io.Serializable;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.kpi.entities.BeanIndex;

/**
 * Class DAO that establishes the connection between the business logic and the
 * database to manage the bean index records.
 * 
 * @author Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class BeanIndexDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Save a bean index in BD
	 * 
	 * @param beanIndex
	 *            : bean index saved.
	 * @throws Exception
	 */
	public void guardarBeanIndex(BeanIndex beanIndex) throws Exception {
		em.persist(beanIndex);
	}

	/**
	 * Edits a bean index in BD
	 * 
	 * @param beanIndex
	 *            : bean index edited.
	 * @throws Exception
	 */
	public void editarBeanIndex(BeanIndex beanIndex) throws Exception {
		em.merge(beanIndex);
	}

	/**
	 * Deletes the bean index of the database
	 * 
	 * @param beanIndex
	 *            : beanIndex to eliminate
	 * @throws Exception
	 */
	public void eliminarBeanIndex(BeanIndex beanIndex) throws Exception {
		em.remove(em.merge(beanIndex));
	}

	/**
	 * This methods return the average of the bean index on all the sections
	 * according to the filters sent as parameter.
	 * 
	 * @param filters
	 *            : Filters to generate the query.
	 * @param params
	 *            : Parameters required by the query for execution.
	 * @param reportQuery
	 *            : the query to build the report.
	 * @return List<Object[]>: Object List with information.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> consultBeanIndexAverage(StringBuilder filters,
			List<SelectItem> params, StringBuilder reportQuery)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append(reportQuery);
		query.append(filters);
		query.append("GROUP BY bi.cycle_number ");
		query.append("ORDER BY bi.cycle_number ");
		Query q = em.createNativeQuery(query.toString());
		for (SelectItem param : params) {
			q.setParameter(param.getLabel(), param.getValue());
		}
		return q.getResultList();
	}

	/**
	 * Returns the number of bean index in the database information by filtering
	 * the search values sent.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found machine usage
	 * @throws Exception
	 */
	public Long amountBeanIndex(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(bi) FROM BeanIndex bi ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

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
	 * @return List<BeanIndex>: list of the bean index.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<BeanIndex> consultBeanIndex(int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT bi FROM BeanIndex bi ");
		query.append(consult);
		query.append("ORDER BY bi.cycleNumber ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<BeanIndex> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * See also article assigned to bean index, considering that they are only
	 * those that are not null in the table
	 * 
	 * @param nomObject
	 *            : object found on the bean index
	 * @param idBeanIndex
	 *            : id bean index being queried
	 * @return Object information associated with the bean index or null but
	 *         there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectBeanIndex(String nomObject, int idBeanIndex)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT bi."
								+ nomObject
								+ " FROM BeanIndex bi WHERE bi.idBeanIndex=:idBeanIndex")
				.setParameter("idBeanIndex", idBeanIndex).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}