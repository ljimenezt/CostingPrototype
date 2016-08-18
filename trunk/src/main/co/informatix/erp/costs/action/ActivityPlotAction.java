package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import co.informatix.erp.costs.dao.ActivityPlotDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityPlot;
import co.informatix.erp.costs.entities.ActivityPlotPK;
import co.informatix.erp.lifeCycle.action.PlotAction;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.Plot;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;

/**
 * This class implements the logic of plots that can be related to activity in
 * the database. The logic is: insert, consult, update and delete the relation
 * between activities and plots.
 * 
 * @author Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ActivityPlotAction implements Serializable {

	@EJB
	private ActivityPlotDao activityPlotDao;
	@Resource
	private UserTransaction userTransaction;

	private Integer tachosCollected;
	private Integer replants;
	private boolean flagPlot;

	private Plot plot;
	private Paginador pagination = new Paginador();
	private Activities activity;
	private ActivityPlot activityPlot;
	private ActivityNames activityNames;

	private List<ActivityPlot> listActivityPlot;
	private List<ActivityPlot> listActivityPlotTemp;

	/**
	 * @return tachosCollected: Number of tachos collected.
	 */
	public Integer getTachosCollected() {
		return tachosCollected;
	}

	/**
	 * @param tachosCollected
	 *            : Number of tachos collected.
	 */
	public void setTachosCollected(Integer tachosCollected) {
		this.tachosCollected = tachosCollected;
	}

	/**
	 * @return replants: Number of replants done.
	 */
	public Integer getReplants() {
		return replants;
	}

	/**
	 * @param replants
	 *            : Number of replants done.
	 */
	public void setReplants(Integer replants) {
		this.replants = replants;
	}

	/**
	 * @return validatePlot: Flag for identifier if the relation between
	 *         activity and plot need number of tachos or replants or the plot
	 *         doesnt need.
	 */
	public boolean isFlagPlot() {
		return flagPlot;
	}

	/**
	 * @param validatePlot
	 *            : Flag for identifier if the relation between activity and
	 *            plot need number of tachos or replants or the plot doesnt
	 *            need.
	 */
	public void setFlagPlot(boolean flagPlot) {
		this.flagPlot = flagPlot;
	}

	/**
	 * @return plot: Plot object.
	 */
	public Plot getPlot() {
		return plot;
	}

	/**
	 * @param plot
	 *            : Plot object.
	 */
	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	/**
	 * @return pagination: Paginador object for pager the relations between
	 *         activities and plots.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paginador object for pager the relations between activities
	 *            and plots.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return activity: Activity object.
	 */
	public Activities getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            : Activity object.
	 */
	public void setActivity(Activities activity) {
		this.activity = activity;
	}

	/**
	 * @return activityPlot: Object for the relation between activities and
	 *         plots.
	 */
	public ActivityPlot getActivityPlot() {
		return activityPlot;
	}

	/**
	 * @param activityPlot
	 *            : Object for the relation between activities and plots.
	 */
	public void setActivityPlot(ActivityPlot activityPlot) {
		this.activityPlot = activityPlot;
	}

	/**
	 * @return activityNames: Activity name.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Activity name.
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return listActivityPlot: List of the relation between activities and
	 *         plots.
	 */
	public List<ActivityPlot> getListActivityPlot() {
		return listActivityPlot;
	}

	/**
	 * @param listActivityPlot
	 *            :List of the relation between activities and plots.
	 */
	public void setListActivityPlot(List<ActivityPlot> listActivityPlot) {
		this.listActivityPlot = listActivityPlot;
	}

	/**
	 * @return listActivityPlotTemp: List of the relation between activities and
	 *         plots (for management).
	 */
	public List<ActivityPlot> getListActivityPlotTemp() {
		return listActivityPlotTemp;
	}

	/**
	 * @param listActivityPlotTemp
	 *            : List of the relation between activities and plots (for
	 *            management).
	 */
	public void setListActivityPlotTemp(List<ActivityPlot> listActivityPlotTemp) {
		this.listActivityPlotTemp = listActivityPlotTemp;
	}

	/**
	 * Initialize the resources for add the relations between activities and
	 * plots.
	 */
	public void initializePlots() {
		listActivityPlot = new ArrayList<ActivityPlot>();
		PlotAction plotAction = ControladorContexto
				.getContextBean(PlotAction.class);
		plotAction.setFlagPlotActivity(true);
		plotAction.setNameSearch("");
		plotAction.setActivity(this.activity);
		plotAction.consultPlots();
	}

	/**
	 * Add plot to list of plots and select this plot.
	 */
	public void addPlots() {
		ActivityPlot activityPlot = new ActivityPlot();
		ActivityPlotPK activityPlotPK = new ActivityPlotPK();
		activityPlotPK.setActivity(activity);
		activityPlotPK.setPlot(plot);
		activityPlot.setActivityPlotPK(activityPlotPK);
		activityPlot.setReplatedPlants(replants);
		activityPlot.setTachosCollected(tachosCollected);
		listActivityPlot.add(activityPlot);
		plot.setSelected(true);
	}

	/**
	 * Remove plot from list of plots and deselect this plot.
	 * 
	 * @param plot
	 *            : Object plot.
	 */
	private void removePlot(Plot plot) {
		List<ActivityPlot> listActivityPlot = new ArrayList<ActivityPlot>();
		listActivityPlot.addAll(this.listActivityPlot);
		for (ActivityPlot activityPlot : listActivityPlot) {
			if (plot.getIdPlot() == activityPlot.getActivityPlotPK().getPlot()
					.getIdPlot()) {
				this.listActivityPlot.remove(activityPlot);
			}
		}
		plot.setSelected(false);
	}

	/**
	 * Validate if the plot selected need information about the harvest or
	 * replanted and add or remove plot to list selected.
	 * 
	 * @param plot
	 *            : Object plot.
	 */
	public void validatePlot(Plot plot) {
		if (plot != null && !plot.isSelected()) {
			Boolean harvest = activity.getActivityName().getHarvest();
			Boolean replanted = activity.getActivityName().getReplanted();
			this.plot = plot;
			if ((harvest != null && harvest)
					|| (replanted != null && replanted)) {
				setReplants(null);
				setTachosCollected(null);
				flagPlot = false;
			} else {
				setReplants(null);
				setTachosCollected(null);
				addPlots();
				flagPlot = true;
			}
		} else {
			removePlot(plot);
			flagPlot = true;
		}
	}

	/**
	 * Delete a relation between activity and plot.
	 */
	public void deleteActivityPlot() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("messageLifeCycle");
		try {
			activityPlotDao.deleteActivityPlot(this.activityPlot);
			String mensaje = MessageFormat.format(
					bundle.getString("activity_plot_message_delete"),
					activityPlot.getActivityPlotPK().getPlot().getName(),
					activityPlot.getActivityPlotPK().getActivity()
							.getActivityName().getActivityName());
			ControladorContexto.mensajeInformacion(mensaje);
			consultActivityPlot();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Thgis method save or edit the relation between activity and plots that
	 * the user selected.
	 * 
	 * @param flagSaveEdit
	 *            : Flag for save or edit activityPlot.
	 */
	public void saveEditPlotActivities(boolean flagSaveEdit) {
		ResourceBundle bundle = ControladorContexto
				.getBundle("messageLifeCycle");
		String mensaje = "";
		try {
			userTransaction.begin();
			if (flagSaveEdit) {
				for (ActivityPlot activityPlot : listActivityPlot) {
					activityPlotDao.saveActivityPlot(activityPlot);
				}
				mensaje = MessageFormat.format(
						bundle.getString("activity_plot_message_register"),
						this.activityNames.getActivityName());
			} else {
				activityPlotDao.editActivityPlot(this.activityPlot);
				mensaje = MessageFormat.format(
						bundle.getString("activity_plot_message_update"),
						activityPlot.getActivityPlotPK().getPlot().getName(),
						activityPlot.getActivityPlotPK().getActivity()
								.getActivityName().getActivityName());
			}
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(mensaje);
			consultActivityPlot();
		} catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.printErrorLog(e1);
			}
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Consult all the relations between activity and plot.
	 */
	public void consultActivityPlot() {
		this.listActivityPlotTemp = new ArrayList<ActivityPlot>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		try {
			advancedSearchPlotActivity(consult, parameters);
			Long quantity = activityPlotDao.amountPlotActivity(consult,
					parameters);
			if (quantity != null) {
				if (quantity > 5) {
					pagination.paginarRangoDefinido(quantity, 5);
				} else {
					pagination.paginar(quantity);
				}
				this.listActivityPlotTemp = activityPlotDao.queryActivityPlot(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method constructs the query to the advanced search also allows
	 * assemble messages to display depending on the search criteria selected by
	 * the user.
	 * 
	 * @param consult
	 *            : Query to concanate.
	 * @param parameters
	 *            : List of parameters.
	 */
	private void advancedSearchPlotActivity(StringBuilder consult,
			List<SelectItem> parameters) {
		consult.append("WHERE a.idActivity = :idActivity");
		SelectItem item = new SelectItem(this.activity.getIdActivity(),
				"idActivity");
		parameters.add(item);
	}

	/**
	 * This method maintain the plots selected.
	 * 
	 * @param plots
	 *            : List of plots.
	 * @param activityPlots
	 *            : Relation between plots and activity.
	 */
	public void maintainHrSelected(List<Plot> plots,
			List<ActivityPlot> activityPlots) {
		for (Plot plot : plots) {
			for (ActivityPlot activityPlot : activityPlots) {
				if (plot.getIdPlot() == activityPlot.getActivityPlotPK()
						.getPlot().getIdPlot()) {
					plot.setSelected(true);
				}
			}
		}
	}
}
