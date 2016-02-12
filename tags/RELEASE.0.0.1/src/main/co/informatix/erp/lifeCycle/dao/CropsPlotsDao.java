package co.informatix.erp.lifeCycle.dao;

import java.io.Serializable;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.CropsPlots;
import co.informatix.erp.lifeCycle.entities.Plot;

/**
 * Class DAO that establishes the connection between the business logic and
 * database management for Crops associated to the plot.
 * 
 * @author Sergio.ortiz
 * 
 */
@SuppressWarnings("serial")
@Stateless
public class CropsPlotsDao implements Serializable {

	@PersistenceContext(unitName = "ERPImp")
	private EntityManager em;

	/**
	 * Saves a relationship CropsPlots BD
	 * 
	 * @param cropsPlots
	 *            : relationship to save crops and plots.
	 * @throws Exception
	 */
	public void guardarCropsPlots(CropsPlots cropsPlots) throws Exception {
		em.persist(cropsPlots);
	}

	/**
	 * Removes BD CropsPlots
	 * 
	 * @param cropsPlots
	 *            : plot associated crop eliminate
	 * @throws Exception
	 */
	public void eliminarCropsPlots(CropsPlots cropsPlots) throws Exception {
		em.remove(em.merge(cropsPlots));
	}

	/**
	 * Method to query a CropsPlots having a relationship with an Crops and Plot
	 * 
	 * @param crops
	 *            : crops to find the plot
	 * @param plot
	 *            : plot to find the crops.
	 * 
	 * @return CropsPlots and plot found by crops.
	 * @throws Exception
	 */
	public CropsPlots consultarCropsPlots(Crops crops, Plot plot)
			throws Exception {
		StringBuilder query = new StringBuilder();
		query.append("SELECT cp FROM CropsPlots cp ");
		query.append("WHERE cp.cropsPlotsPK.crops=:crops ");
		query.append("AND cp.cropsPlotsPK.plot=:plot ");
		Query q = em.createQuery(query.toString());
		q.setParameter("crops", crops);
		q.setParameter("plot", plot);
		if (q.getResultList().size() > 0) {
			return (CropsPlots) q.getResultList().get(0);
		}
		return null;
	}

}
