package co.informatix.erp.costs.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.transaction.UserTransaction;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.util.CellRangeAddress;

import co.informatix.erp.costs.dao.ActivitiesAndHrDao;
import co.informatix.erp.costs.dao.ActivityPlotDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivitiesAndHr;
import co.informatix.erp.costs.entities.ActivityPlot;
import co.informatix.erp.costs.entities.ActivityPlotPK;
import co.informatix.erp.lifeCycle.action.PlotAction;
import co.informatix.erp.lifeCycle.dao.PlotDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.Plot;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorArchivos;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.ControllerReportPoi;
import co.informatix.erp.utils.FileUploadBean;
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
	@EJB
	private PlotDao plotDao;
	@EJB
	private ActivitiesAndHrDao activitiesAndHrDao;
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

	/**
	 * This method consult all the plots and set status into de cycle for each
	 * one.
	 * 
	 * @param idCycle
	 *            : Identifier of Cycle.
	 * @return HashMap<String, Plot>: Hashmap when the key is the concatenation
	 *         of map row and position into the this row (paint map).
	 */
	private HashMap<String, Plot> buildPlotStatus(int idCycle) throws Exception {
		String key = "";
		List<ActivityPlot> actualPlots = activityPlotDao
				.queryActivityPlot(activity.getIdActivity());
		List<Plot> plotsExecuted = plotDao.queryActivityPlotByCycle(idCycle);
		List<Plot> missingPlots = plotDao.queryPlotsMissing(plotsExecuted);
		HashMap<String, Plot> listPlotStatus = new HashMap<String, Plot>();

		if (plotsExecuted != null) {
			for (Plot plot : plotsExecuted) {
				plot.setStatusPlot((short) 1);
				if (actualPlots != null) {
					for (ActivityPlot ap : actualPlots) {
						if (ap.getActivityPlotPK().getPlot().getIdPlot() == plot
								.getIdPlot()) {
							plot.setStatusPlot((short) 2);
						}
					}
				}
				key = plot.getMapRow() + "" + plot.getPositionRow();
				listPlotStatus.put(key, plot);
			}
		}

		if (missingPlots != null) {
			for (Plot plot : missingPlots) {
				plot.setStatusPlot((short) 3);
				if (plot.getNumberOfTrees() == null
						|| plot.getNumberOfTrees() == 0) {
					plot.setStatusPlot((short) 0);
				}
				key = plot.getMapRow() + "" + plot.getPositionRow();
				listPlotStatus.put(key, plot);
			}
		}
		return listPlotStatus;
	}

	/**
	 * This method build hash map with number row and number de plots into the
	 * row.
	 * 
	 * @return HashMap<String, Integer>: String: Number row, Integer: number
	 *         plots into the row.
	 */
	private HashMap<String, Integer> createMapModel() {
		HashMap<String, Integer> buildMap = new HashMap<String, Integer>();
		buildMap.put("1", 5);
		buildMap.put("2", 5);
		buildMap.put("3", 6);
		buildMap.put("4", 6);
		buildMap.put("5", 6);
		buildMap.put("6", 7);
		buildMap.put("7", 7);
		buildMap.put("8", 8);
		buildMap.put("9", 8);
		buildMap.put("10", 10);
		buildMap.put("11", 9);
		buildMap.put("12", 9);
		buildMap.put("13", 10);
		buildMap.put("14", 11);
		buildMap.put("15", 11);
		buildMap.put("16", 12);
		buildMap.put("17", 12);
		buildMap.put("18", 6);
		buildMap.put("19", 5);
		buildMap.put("20", 5);
		buildMap.put("21", 5);
		buildMap.put("22", 5);
		buildMap.put("23", 5);
		buildMap.put("24", 5);
		buildMap.put("25", 5);
		buildMap.put("26", 5);
		buildMap.put("27", 5);
		buildMap.put("28", 5);
		buildMap.put("29", 5);
		buildMap.put("30", 5);
		buildMap.put("31", 5);
		buildMap.put("32", 4);
		buildMap.put("33", 2);
		buildMap.put("34", 3);
		return buildMap;
	}

	/**
	 * This method create Report on excel for the plots map.
	 * 
	 * @param cycle
	 *            : Cycle identifier.
	 */
	public void createExcelPlot(int cycle) {
		ResourceBundle bundleMessage = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMessageLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		String fileName = bundleMessageLifeCycle
				.getString("cycle_label_report");
		try {
			List<ActivitiesAndHr> listActivitiesHr = activitiesAndHrDao
					.activitiesAndHrByCycle(activity.getIdActivity());
			HashMap<String, Integer> buildMap = createMapModel();
			HashMap<String, Plot> listPlots = buildPlotStatus(cycle);
			Long tachos = activityPlotDao.queryTachosCollected(activity
					.getIdActivity());

			String formatName = fileName.toLowerCase() + ".xls";
			String urlServer = Constantes.RUTA_UPLOADFILE_GLASFISH;
			String pathname = urlServer + Constantes.CARPETA_ARCHIVOS_TEMP;

			FileUploadBean.fileExist(pathname);
			File file = new File(pathname, formatName);

			FileOutputStream outputFile = new FileOutputStream(file);

			ControllerReportPoi cReportPoi = new ControllerReportPoi();
			cReportPoi.loadStylesXls();

			/* Create all the styles for the report */
			CellStyle styleTitle1 = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleTitle1, "0,0,0",
					Constantes.FONT_COOPER_BLACK, 24, true, "226,107,10", null,
					"C");
			CellStyle styleTitle2 = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleTitle2, "0,0,0", null, 20, true,
					"255,204,153", null, "C");
			CellStyle styleTitle3 = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleTitle3, "0,0,0", null, 20, true,
					"226,107,10", null, "C");
			CellStyle stylePlotAccumalitive = cReportPoi
					.clonStylePoi(cReportPoi.getGeneralStyle());
			cReportPoi.changeCellStyle(stylePlotAccumalitive, "255,255,255",
					null, 8, true, "0,0,255", "LRTB", "C");
			CellStyle stylePlotActual = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(stylePlotActual, "0,0,0", null, 8, true,
					"255,255,0", "LRTB", "C");
			CellStyle stylePlotUnseeded = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(stylePlotUnseeded, "0,0,0", null, 8,
					true, "192,192,192", "LRTB", "C");
			CellStyle stylePlotPending = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(stylePlotPending, "0,0,0", null, 8,
					true, "255,0,0", "LRTB", "C");
			CellStyle styleBlue = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleBlue, "0,0,255", null, 10, true,
					null, null, "L");
			CellStyle styleBlack = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleBlack, "0,0,0", null, 10, true,
					null, null, "C");
			CellStyle styleBlackBorder = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleBlackBorder, "0,0,0", null, 10,
					true, null, "LRTB", "C");
			CellStyle styleWorkers = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleWorkers, "0,0,0", null, 10, false,
					null, "LRTB", "L");
			CellStyle styleWorkersTitles = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleWorkersTitles, "0,0,255", null, 10,
					true, null, "LRTB", "C");
			CellStyle styleBigText = cReportPoi.clonStylePoi(cReportPoi
					.getGeneralStyle());
			cReportPoi.changeCellStyle(styleBigText, "0,0,0", null, 24, true,
					null, "LRTB", "C");

			/* Create the header for report */
			HSSFRow fila = cReportPoi.getSheet().createRow(0);
			cReportPoi.getSheet().getRow(0).setHeight((short) 800);
			HSSFCell celda = fila.createCell(1);
			celda.setCellStyle(styleTitle1);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(0, 0, 1, 34));
			celda.setCellType(HSSFCell.CELL_TYPE_STRING);
			celda.setCellValue(new HSSFRichTextString(Constantes.COMPANY));

			fila = cReportPoi.getSheet().createRow(1);
			cReportPoi.getSheet().getRow(1).setHeight((short) 800);
			celda = fila.createCell(1);
			celda.setCellStyle(styleTitle2);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(1, 1, 1, 34));
			celda.setCellType(HSSFCell.CELL_TYPE_STRING);
			celda.setCellValue(new HSSFRichTextString(this.activity
					.getActivityName().getActivityName()));

			fila = cReportPoi.getSheet().createRow(2);
			cReportPoi.getSheet().getRow(2).setHeight((short) 800);
			celda = fila.createCell(1);
			celda.setCellStyle(styleTitle3);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(2, 2, 1, 34));
			celda.setCellType(HSSFCell.CELL_TYPE_STRING);
			celda.setCellValue(new HSSFRichTextString(bundleMessageLifeCycle
					.getString("cycle_label")
					+ ": "
					+ this.activity.getCycle().getCycleNumber()));

			fila = cReportPoi.getSheet().createRow(3);
			cReportPoi.getSheet().getRow(3).setHeight((short) 400);
			celda = fila.createCell(1);
			celda.setCellStyle(styleBlue);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(3, 3, 1, 9));
			celda.setCellType(HSSFCell.CELL_TYPE_STRING);
			celda.setCellValue(new HSSFRichTextString(bundleMessage.getString(
					"label_date").toUpperCase()
					+ ":"));
			celda = fila.createCell(10);
			celda.setCellStyle(styleBlack);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(3, 3, 10, 18));
			celda.setCellValue(new HSSFRichTextString(""
					+ ControladorFechas.formatDate(
							this.activity.getInitialDtBudget(), "dd-MM-yyyy")));

			fila = cReportPoi.getSheet().createRow(4);
			cReportPoi.getSheet().getRow(4).setHeight((short) 400);
			celda = fila.createCell(1);
			celda.setCellStyle(styleBlue);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(4, 4, 1, 9));
			celda.setCellType(HSSFCell.CELL_TYPE_STRING);
			celda.setCellValue(new HSSFRichTextString(bundleMessage.getString(
					"label_week").toUpperCase()
					+ ":"));
			celda = fila.createCell(10);
			celda.setCellStyle(styleBlack);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(4, 4, 10, 18));
			celda.setCellValue(new HSSFRichTextString(""
					+ ControladorFechas.getNumberWeek(this.activity
							.getInitialDtBudget())));
			fila = cReportPoi.getSheet().createRow(5);
			cReportPoi.getSheet().getRow(5).setHeight((short) 400);
			celda = fila.createCell(1);
			celda.setCellStyle(styleBlue);
			cReportPoi.getSheet().addMergedRegion(
					new CellRangeAddress(5, 5, 1, 9));
			celda.setCellType(HSSFCell.CELL_TYPE_STRING);
			celda.setCellValue(new HSSFRichTextString(bundleMessage.getString(
					"label_observations").toUpperCase()
					+ ":"));

			/* Create the plot map */
			int startNumberColumn = 9;
			int numberRow = 1;
			int startFile = 9;
			int plotActual = 0;
			int plotAccumulative = 0;
			int plotPending = 0;
			int plotUnseed = 0;

			if (listPlots.size() > 0) {
				for (int i = 1; i <= (buildMap.size() * 2); i = i + 2) {

					if (i > 1) {
						numberRow = ((i - 1) / 2) + 1;
					}

					fila = cReportPoi.getSheet().createRow(i + startFile);
					if (buildMap.get("" + (numberRow - 1)) != null
							&& buildMap.get("" + numberRow) != buildMap.get(""
									+ (numberRow - 1))) {

						switch (numberRow) {

						case 11:
							break;

						case 18:
							break;

						case 32:
							startNumberColumn++;
							break;

						case 33:
							startNumberColumn = startNumberColumn + 4;
							break;

						case 34:
							startNumberColumn = startNumberColumn - 2;
							break;

						default:
							if (numberRow - 1 != 18) {
								startNumberColumn = startNumberColumn - 1;
							}
						}
					}

					int limit = ((buildMap.get("" + numberRow) * 2));
					int contStyle = 0;
					int positionFile = 1;
					for (int j = 1; j <= limit; j = j + 2) {

						if (j > 1) {
							positionFile = ((j - 1) / 2) + 1;
						}

						Plot plot = listPlots
								.get(numberRow + "" + positionFile);

						celda = fila.createCell(j + startNumberColumn);

						String namePlot = "";
						celda.setCellStyle(stylePlotUnseeded);
						if (plot != null) {
							namePlot = plot.getName();

							if (plot.getStatusPlot() == 0) {
								celda.setCellStyle(stylePlotUnseeded);
								plotUnseed++;
							}

							if (plot.getStatusPlot() == 2) {
								celda.setCellStyle(stylePlotActual);
								plotActual++;
							}

							if (plot.getStatusPlot() == 1) {
								celda.setCellStyle(stylePlotAccumalitive);
								plotAccumulative++;
							}

							if (plot.getStatusPlot() == 3) {
								celda.setCellStyle(stylePlotPending);
								plotPending++;
							}
						}

						cReportPoi.getSheet().addMergedRegion(
								new CellRangeAddress(i + startFile, i
										+ startFile + 1, j + startNumberColumn,
										j + startNumberColumn + 1));
						celda.setCellType(HSSFCell.CELL_TYPE_STRING);
						celda.setCellValue(new HSSFRichTextString(namePlot));
						celda = fila.createCell(j + startNumberColumn + 1);
						celda.setCellStyle(stylePlotUnseeded);
						contStyle++;
					}

					fila = cReportPoi.getSheet().createRow(i + startFile + 1);
					for (int a = 1; a <= (contStyle * 2); a++) {
						celda = fila.createCell(startNumberColumn + a);
						celda.setCellStyle(stylePlotUnseeded);
					}
				}

				for (int i = 1; i < 30; i++) {
					cReportPoi.getSheet().setColumnWidth(i, 700);
				}

				/* Assign status of the activity (header) */
				fila = cReportPoi.getSheet().getRow(5);
				celda = fila.createCell(10);
				celda.setCellStyle(styleBlack);
				cReportPoi.getSheet().addMergedRegion(
						new CellRangeAddress(5, 5, 10, 18));
				String labor = bundleMessageLifeCycle
						.getString("activity_plot_label_labor_not_succesfull");
				if (plotActual > 0) {
					labor = bundleMessageLifeCycle
							.getString("activity_plot_label_labor_succesfull");
				}
				celda.setCellValue(new HSSFRichTextString(labor));

				/* Create the conventions and quantity of plots for status */
				fila = cReportPoi.getSheet().createRow(startFile);
				celda = fila.createCell(31);
				celda.setCellStyle(styleBlackBorder);
				celda = fila.createCell(32);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle
								.getString("activity_plot_label_valves_s")));
				celda = fila.createCell(33);
				celda.setCellStyle(styleBlackBorder);

				fila = cReportPoi.getSheet().getRow(startFile + 1);
				celda = fila.createCell(31);
				celda.setCellStyle(stylePlotUnseeded);
				celda = fila.createCell(32);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				celda.setCellValue(new HSSFRichTextString("" + plotUnseed));
				celda = fila.createCell(33);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle
								.getString("activity_plot_unseeded_area")));

				fila = cReportPoi.getSheet().getRow(startFile + 2);
				celda = fila.createCell(31);
				celda.setCellStyle(stylePlotAccumalitive);
				celda = fila.createCell(32);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				celda.setCellValue(new HSSFRichTextString("" + plotAccumulative));
				celda = fila.createCell(33);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle
								.getString("activity_plot_Accumulative_area")));

				fila = cReportPoi.getSheet().getRow(startFile + 3);
				celda = fila.createCell(31);
				celda.setCellStyle(stylePlotActual);
				celda = fila.createCell(32);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				celda.setCellValue(new HSSFRichTextString("" + plotActual));
				celda = fila.createCell(33);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle
								.getString("activity_plot_execute_area")));

				fila = cReportPoi.getSheet().getRow(startFile + 4);
				celda = fila.createCell(31);
				celda.setCellStyle(stylePlotPending);
				celda = fila.createCell(32);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				celda.setCellValue(new HSSFRichTextString("" + plotPending));
				celda = fila.createCell(33);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle
								.getString("activity_plot_pending_area")));

				fila = cReportPoi.getSheet().getRow(startFile + 5);
				celda = fila.createCell(31);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(bundleMessage
						.getString("label_total").toUpperCase()
						+ " "
						+ bundleMessageLifeCycle.getString(
								"activity_plot_label_valves_s").toUpperCase()));
				celda = fila.createCell(32);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_NUMERIC);
				celda.setCellValue(new HSSFRichTextString("" + listPlots.size()));
				celda = fila.createCell(33);
				celda.setCellStyle(styleBlackBorder);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(""));

				/* Create list of laborers into the activity */
				fila = cReportPoi.getSheet().getRow(startFile + 7);
				celda = fila.createCell(30);
				celda.setCellStyle(styleWorkersTitles);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString("No"));
				celda = fila.createCell(31);
				celda.setCellStyle(styleWorkersTitles);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle.getString(
								"activity_plot_label_laborers").toUpperCase()));
				celda = fila.createCell(32);
				celda.setCellStyle(styleWorkersTitles);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString("No "
						+ bundleMessageLifeCycle.getString(
								"activity_plot_label_valves_s").toUpperCase()));
				celda = fila.createCell(33);
				celda.setCellStyle(styleWorkersTitles);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle.getString(
								"activity_plot_label_bags_cocoa").toUpperCase()));
				celda = fila.createCell(34);
				celda.setCellStyle(styleWorkersTitles);
				celda.setCellType(HSSFCell.CELL_TYPE_STRING);
				celda.setCellValue(new HSSFRichTextString(
						bundleMessageLifeCycle.getString(
								"activity_plot_label_tachos_cocoa")
								.toUpperCase()));

				int contHr = 0;
				for (ActivitiesAndHr ahr : listActivitiesHr) {
					fila = cReportPoi.getSheet().getRow(startFile + 8 + contHr);
					contHr++;
					celda = fila.createCell(30);
					celda.setCellStyle(styleWorkers);
					celda.setCellValue(new HSSFRichTextString("" + contHr));
					celda = fila.createCell(31);
					celda.setCellStyle(styleWorkers);
					celda.setCellType(HSSFCell.CELL_TYPE_STRING);
					celda.setCellValue(new HSSFRichTextString(ahr
							.getActivitiesAndHrPK().getHr().getName()
							+ " "
							+ ahr.getActivitiesAndHrPK().getHr()
									.getFamilyName()));
					celda = fila.createCell(32);
					celda.setCellStyle(styleWorkers);
					celda = fila.createCell(33);
					celda.setCellStyle(styleWorkers);
					celda = fila.createCell(34);
					celda.setCellStyle(styleWorkers);
				}

				fila = cReportPoi.getSheet().getRow(startFile + 8);
				celda = fila.createCell(32);
				celda.setCellStyle(listActivitiesHr.size() < 2 ? styleBlackBorder
						: styleBigText);
				celda.setCellValue(new HSSFRichTextString("" + plotActual));
				cReportPoi.getSheet().addMergedRegion(
						new CellRangeAddress(startFile + 8, startFile + 7
								+ listActivitiesHr.size(), 32, 32));
				celda = fila.createCell(33);
				celda.setCellStyle(styleBigText);
				celda.setCellValue(new HSSFRichTextString(""));
				cReportPoi.getSheet().addMergedRegion(
						new CellRangeAddress(startFile + 8, startFile + 7
								+ listActivitiesHr.size(), 33, 33));
				celda = fila.createCell(34);
				celda.setCellStyle(listActivitiesHr.size() < 2 ? styleBlackBorder
						: styleBigText);
				tachos = (long) (tachos != null ? tachos.intValue() : 0);
				celda.setCellValue(new HSSFRichTextString(""
						+ tachos.intValue()));
				cReportPoi.getSheet().addMergedRegion(
						new CellRangeAddress(startFile + 8, startFile + 7
								+ listActivitiesHr.size(), 34, 34));

				cReportPoi.getSheet().setColumnWidth(30, 1000);
				cReportPoi.getSheet().setColumnWidth(31, 7000);
				cReportPoi.getSheet().setColumnWidth(32, 4000);
				cReportPoi.getSheet().setColumnWidth(33, 6000);
				cReportPoi.getSheet().setColumnWidth(34, 5000);
				cReportPoi.getSheet().setColumnWidth(35, 5000);

				cReportPoi.getWorkBook().write(outputFile);
				ControladorArchivos.downloadFile(pathname + "/" + formatName,
						"xls");
			} else {
				ControladorContexto.mensajeError(bundleMessageLifeCycle
						.getString("activity_plot_message_report_no_data"));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}
}
