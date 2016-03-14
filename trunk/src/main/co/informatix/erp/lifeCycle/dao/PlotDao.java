package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.model.SelectItem;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Plot;

@SuppressWarnings("serial")
@Stateless
/**
 * Class DAO that establishes the connection between business logic and database for handling the Plot entity.
 * 
 * @author Johnatan.Naranjo
 * 
 */
public class PlotDao implements Serializable {
	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * This method consult a particular parcel sent as a parameter range and
	 * filtering the information sent search values.
	 * 
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Plot>: plot list.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Plot> consultPlots(int start, int range, StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM  Plot p ");
		query.append("JOIN FETCH p.farm ");
		query.append(consult);
		query.append("ORDER BY p.name ");
		Query q = em.createQuery(query.toString());
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}
		q.setFirstResult(start).setMaxResults(range);
		List<Plot> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}
		return null;
	}

	/**
	 * Saves a plot in BD.
	 * 
	 * @param plot
	 *            : plot to save.
	 * @throws Exception
	 */
	public void savePlot(Plot plot) throws Exception {
		em.persist(plot);
	}

	/**
	 * Edits a plot in BD.
	 * 
	 * @param plot
	 *            : plot editing.
	 * @throws Exception
	 */
	public void editPlot(Plot plot) throws Exception {
		em.merge(plot);
	}

	/**
	 * Removes the BD plot.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param plot
	 *            : plot to eliminate.
	 * @throws Exception
	 */
	public void removePlot(Plot plot) throws Exception {
		em.remove(em.merge(plot));
	}

	/**
	 * Returns the number of existing parcels in the database by filtering
	 * information sent search values.
	 * 
	 * @param consult
	 *            : String containing the query for which the properties are
	 *            filtered.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: amount of plots records found
	 * @throws Exception
	 */
	public Long quantityPlots(StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT COUNT(p) FROM Plot p ");
		query.append(consult);
		Query q = em.createQuery(query.toString());
		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}
		return (Long) q.getSingleResult();
	}

	/**
	 * Consultation if the name of the plot exist in the farm and in the
	 * database when storing or editing.
	 * 
	 * @modify 14/03/2016 Jhair.Leal
	 * 
	 * @param name
	 *            : plot name to verify
	 * @param id
	 *            : identifier to verify the plot
	 * @return Plot: plot object found with the search parameters name and
	 *         identifier.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Plot nameExist(String name, int id, int idFarm) throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT p FROM Plot p JOIN p.farm f ");
		query.append("WHERE UPPER(p.name)=UPPER(:name) ");
		query.append("AND f.idFarm = :idFarm ");
		if (id != 0) {
			query.append("AND p.idPlot <>:idPlot ");
		}
		Query q = em.createQuery(query.toString());
		q.setParameter("name", name);
		q.setParameter("idFarm", idFarm);
		if (id != 0) {
			q.setParameter("idPlot", id);
		}
		List<Plot> results = q.getResultList();
		if (results.size() > 0) {
			return results.get(0);
		}
		return null;
	}

	/**
	 * Consult also article assigned to a plot, considering only those that are
	 * not null in the table.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param nomObject
	 *            : object found on the plot.
	 * @param idPlot
	 *            : plot ID being queried.
	 * @return Object: Information associated with the plot or null but there.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public Object consultObjectPlot(String nomObject, int idPlot)
			throws Exception {
		List<Object> results = em
				.createQuery(
						"SELECT p." + nomObject
								+ " FROM Plot p WHERE p.id=:idPlot")
				.setParameter("idPlot", idPlot).getResultList();

		if (results.size() > 0) {
			return results.get(0);
		}

		return null;
	}

	/**
	 * Method associates a crop plots are consulted on a particular date range.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param dateStart
	 *            : start date which is associated with a plot to a crop.
	 * @param dateEnd
	 *            : end date of a crop plot association.
	 * @param plotSelected
	 *            : a list of associated crop plots a certain date range.
	 * @param start
	 *            :where he started the consultation record.
	 * @param range
	 *            : range of records.
	 * @param consult
	 *            : Query records depending on the user selected parameter.
	 * @param parameters
	 *            : consult parameters.
	 * @return List<Plot>: Plot list that are available for the date range
	 *         selected by the user
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Plot> searchCopsPlotsDate(Date dateStart, Date dateEnd,
			List<Plot> plotSelected, int start, int range,
			StringBuilder consult, List<SelectItem> parameters)
			throws Exception {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT p FROM Plot p");
		query.append(" WHERE p.idPlot NOT IN ");
		query.append(" (SELECT cp.cropsPlotsPK.plot.idPlot FROM CropsPlots cp WHERE ");
		query.append(" cp.cropsPlotsPK.crops.idCrop IN ");
		query.append(" (SELECT cr.idCrop FROM Crops cr WHERE ");
		query.append(" ((:fechaInicio  >= cr.initialDate AND :fechaInicio <= cr.finalDate)");
		query.append(" OR (:fechaInicio <= cr.initialDate AND :fechaFin >= cr.initialDate)))) ");
		if (plotSelected != null && plotSelected.size() > 0) {
			query.append(" AND p NOT IN (:plotSeleccionado) ");
		}
		query.append(consult);
		query.append("ORDER BY p.name ");
		Query q = em.createQuery(query.toString());
		q.setParameter("fechaInicio", dateStart).setParameter("fechaFin",
				dateEnd);

		if (plotSelected != null && plotSelected.size() > 0) {
			q.setParameter("plotSeleccionado", plotSelected);
		}
		for (SelectItem parameter : parameters) {
			q.setParameter(parameter.getLabel(), parameter.getValue());
		}

		q.setFirstResult(start).setMaxResults(range);
		List<Plot> resultList = q.getResultList();
		if (resultList.size() > 0) {
			return resultList;
		}

		return null;
	}

	/**
	 * method consulting in the plots that are associated with a Crop.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param idCrop
	 *            : Crops identifier to which you want to know which is.
	 *            associated Plots.
	 * @return list<Plots>: Plots list associated Crops.
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	public List<Plot> cropsPlotsAssociated(int idCrop) throws Exception {

		StringBuilder query = new StringBuilder();
		query.append(" SELECT cp.cropsPlotsPK.plot FROM CropsPlots cp ");
		query.append(" WHERE cp.cropsPlotsPK.crops.idCrop  = :idCrop  ");
		Query q = em.createQuery(query.toString());
		q.setParameter("idCrop", idCrop);
		return q.getResultList();
	}

	/**
	 * Returns the number of existing filtering Plot in the database search by
	 * the values sent.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param plotSelected
	 *            : plot to which you want to view the number of associated
	 *            plot.
	 * @param dateStart
	 *            : where he started the consultation record.
	 * @param dateEnd
	 *            : end date of the plot to crop association.
	 * @param consult
	 *            : String containing the query why the names are filtered plot.
	 * @param parameters
	 *            : query parameters.
	 * @return Long: plot number of records found
	 * @throws Exception
	 */
	public Long quantityPlotsFiltered(List<Plot> plotSelected, Date dateStart,
			Date dateEnd, StringBuilder consult, List<SelectItem> parameters)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append(" SELECT COUNT(p) FROM Plot p");
		query.append(" WHERE p.idPlot NOT IN ");
		query.append(" (SELECT cp.cropsPlotsPK.plot.idPlot FROM CropsPlots cp WHERE ");
		query.append(" cp.cropsPlotsPK.crops.idCrop IN ");
		query.append(" (SELECT cr.idCrop FROM Crops cr WHERE ");
		query.append(" ((:fechaInicio  >= cr.initialDate AND :fechaInicio <= cr.finalDate)");
		query.append(" OR (:fechaInicio <= cr.initialDate AND :fechaFin >= cr.initialDate)))) ");
		if (plotSelected != null && plotSelected.size() > 0) {
			query.append(" AND p NOT IN (:plotSeleccionado) ");
		}
		query.append(consult);
		Query q = em.createQuery(query.toString());
		q.setParameter("fechaInicio", dateStart).setParameter("fechaFin",
				dateEnd);

		if (plotSelected != null && plotSelected.size() > 0) {
			q.setParameter("plotSeleccionado", plotSelected);
		}

		for (SelectItem parametro : parameters) {
			q.setParameter(parametro.getLabel(), parametro.getValue());
		}

		return (Long) q.getSingleResult();
	}
}
