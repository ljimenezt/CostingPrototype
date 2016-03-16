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
 * DAO Class that establishes the connection between the business logic and the
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
	 * Save a bean index in the database.
	 * 
	 * @param beanIndex
	 *            : bean index saved.
	 * @throws Exception
	 */
	public void saveBeanIndex(BeanIndex beanIndex) throws Exception {
		em.persist(beanIndex);
	}

	/**
	 * Edits a bean index in the database.
	 * 
	 * @param beanIndex
	 *            : bean index edited.
	 * @throws Exception
	 */
	public void editBeanIndex(BeanIndex beanIndex) throws Exception {
		em.merge(beanIndex);
	}

	/**
	 * Deletes the bean index of the database.
	 * 
	 * @param beanIndex
	 *            : beanIndex to eliminate.
	 * @throws Exception
	 */
	public void deleteBeanIndex(BeanIndex beanIndex) throws Exception {
		em.remove(em.merge(beanIndex));
	}

	/**
	 * This method returns the average of the bean index on all the sections
	 * according to the filters sent as parameters.
	 * 
	 * @modify 2016/01/15 Andres.Gomez
	 * 
	 * @param filters
	 *            : Filters to generate the query.
	 * @param params
	 *            : Parameters required by the query for execution.
	 * @param reportQuery
	 *            : The query to build the report.
	 * @param queryGroupBy
	 *            : The query to group by the report.
	 * @return List<Object[]>: Objects List with information.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryBeanIndexAverage(StringBuilder filters,
			List<SelectItem> params, StringBuilder reportQuery,
			StringBuilder queryGroupBy) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append(reportQuery);
		queryBuilder.append(filters);
		queryBuilder.append(queryGroupBy);
		queryBuilder.append("ORDER BY bi.cycle_number ");
		Query queryResult = em.createNativeQuery(queryBuilder.toString());
		for (SelectItem param : params) {
			queryResult.setParameter(param.getLabel(), param.getValue());
		}
		return queryResult.getResultList();
	}

	/**
	 * This method returns the average of the bean index section according to
	 * the filters sent as parameters.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param params
	 *            : Parameters required by the query for execution.
	 * @return List<Object[]>: Object List with information.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryBySectionAverage(List<SelectItem> params)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT AVG(bi.sample_weight), bi.id_section ");
		query.append("FROM kpi.bean_index bi ");
		query.append("WHERE bi.id_crop = :idCrop ");
		query.append("AND bi.cycle_number = :idCycle ");
		query.append("GROUP BY bi.cycle_number, bi.id_section ");
		query.append("ORDER BY bi.cycle_number ASC ");
		Query q = em.createNativeQuery(query.toString());
		for (SelectItem param : params) {
			q.setParameter(param.getLabel(), param.getValue());
		}
		return q.getResultList();
	}

	/**
	 * This methods return the number of cycle of the bean index according to
	 * the crop sent as parameter.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param params
	 *            : Parameters required by the query for execution.
	 * @return List<Object[]>: Object List with information.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Object[]> queryCycleNumber(List<SelectItem> params)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT bi.cycle_number, MAX(bi.date_bean_index) ");
		query.append("FROM kpi.bean_index bi ");
		query.append("WHERE bi.id_crop = :idCrop ");
		query.append("GROUP BY bi.cycle_number  ");
		query.append("ORDER BY bi.cycle_number ");
		Query q = em.createNativeQuery(query.toString());
		for (SelectItem param : params) {
			q.setParameter(param.getLabel(), param.getValue());
		}
		return q.getResultList();
	}

	/**
	 * This method returns the section of the bean index according to the crop
	 * sent as a parameter.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param params
	 *            : Parameters required by the query for execution.
	 * @return List<Integer>: Object List with information.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Integer> queryBySection(List<SelectItem> params)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT bi.id_section ");
		queryBuilder.append("FROM kpi.bean_index bi ");
		queryBuilder.append("WHERE bi.id_crop = :idCrop ");
		queryBuilder.append("GROUP BY bi.id_section ");
		queryBuilder.append("ORDER BY bi.id_section ");
		Query query = em.createNativeQuery(queryBuilder.toString());
		for (SelectItem param : params) {
			query.setParameter(param.getLabel(), param.getValue());
		}
		return query.getResultList();
	}

	/**
	 * Returns the number of bean index in the database information and it
	 * filters with search values.
	 * 
	 * @param query
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: number of records found machine usage
	 * @throws Exception
	 */
	public Long amountBeanIndex(StringBuilder query, List<SelectItem> parameters)
			throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT COUNT(bi) FROM BeanIndex bi ");
		queryBuilder.append(query);
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * This method builds the query with an advanced search; it also builds
	 * display messages depending on the search criteria selected by the user.
	 * 
	 * @param start
	 *            : First record from the query result.
	 * @param range
	 *            : Range of records to retrieve.
	 * @param query
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<BeanIndex>: list of the bean index.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<BeanIndex> queryBeanIndex(int start, int range,
			StringBuilder query, List<SelectItem> parameters) throws Exception {
		StringBuilder queryBuilder = new StringBuilder();
		queryBuilder.append("SELECT bi FROM BeanIndex bi ");
		queryBuilder.append(query);
		queryBuilder.append("ORDER BY bi.cycleNumber ");
		Query q = em.createQuery(queryBuilder.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<BeanIndex> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Returns an article assigned to a bean index, it retrieves only those that
	 * are not null in the table.
	 * 
	 * @param objectName
	 *            : Object found in the bean index.
	 * @param idBeanIndex
	 *            : Bean index identifier that is being queried.
	 * @return Object information associated with the bean index or null.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object queryObjectBeanIndex(String objectName, int idBeanIndex)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT bi."
								+ objectName
								+ " FROM BeanIndex bi WHERE bi.idBeanIndex=:idBeanIndex")
				.setParameter("idBeanIndex", idBeanIndex).getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

}