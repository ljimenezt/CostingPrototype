package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.CropsPlotsDao;
import co.informatix.erp.lifeCycle.dao.PlotDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.CropsPlots;
import co.informatix.erp.lifeCycle.entities.CropsPlotsPK;
import co.informatix.erp.lifeCycle.entities.Plot;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utils.ValidacionesAction.DatosGuardar;

import com.google.gson.JsonObject;

/**
 * This class allows the logic of the crops that may be in the BD.
 * 
 * The logic is to consult, edit or add crops.
 * 
 * @author Johnatan.Naranjo
 * @modify 16/04/2015 Dario.Lopez
 * @modify 14/05/2015 Sergio.Ortiz
 * @modify 19/06/2015 Gerardo.Herrera
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CropsAction implements Serializable {
	@EJB
	private CropsDao cropsDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private CropsPlotsDao cropsPlotsDao;
	@EJB
	private PlotDao plotsDao;

	private CropNames cropNames;
	private Crops crops;
	private Plot plot;
	private PlotAction plotAction;
	private List<Crops> listCrops;
	private List<Plot> listPlotsAsocciates;
	private List<Plot> subListPlotsAsocciates;
	private List<SelectItem> options;
	private List<Integer> selectYear;
	private Paginador pagination = new Paginador();
	private Paginador paginationPlotsAsocciates;
	private String nameSearch;
	private int year;
	private int nameCrop;
	private boolean flagButton;
	private boolean guarded;

	private JsonObject obj;
	private JsonObject constructionParametersMap;

	/**
	 * @return cropNames: crop names associated with the crop.
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            crop names associated with the crop.
	 */
	public void setCropNames(CropNames cropNames) {
		this.nameCrop = cropNames.getIdCropName();
	}

	/**
	 * @return crops: get the record crop.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            :set the record harvest.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return plot: Plot selected in the plot table.
	 */
	public Plot getPlot() {
		return plot;
	}

	/**
	 * @param plot
	 *            :Plot selected in the plot table.
	 */
	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	/**
	 * @return listCrops: crops list.
	 */
	public List<Crops> getListCrops() {
		return listCrops;
	}

	/**
	 * @param listCrops
	 *            :crops list.
	 */
	public void setListCrops(List<Crops> listCrops) {
		this.listCrops = listCrops;
	}

	/**
	 * @return listPlotsAsocciates: Plots list Associates Crops.
	 */
	public List<Plot> getListPlotsAsocciates() {
		return listPlotsAsocciates;
	}

	/**
	 * @param listPlotsAsocciates
	 *            : Plots list Associates Crops.
	 */
	public void setListPlotsAsocciates(List<Plot> listPlotsAsocciates) {
		this.listPlotsAsocciates = listPlotsAsocciates;
	}

	/**
	 * @return subListPlotsAsocciates: Plots list Associates Crops.
	 */
	public List<Plot> getSubListPlotsAsocciates() {
		return subListPlotsAsocciates;
	}

	/**
	 * @param subListPlotsAsocciates
	 *            : Plots list Associates Crops.
	 */
	public void setSubListPlotsAsocciates(List<Plot> subListPlotsAsocciates) {
		this.subListPlotsAsocciates = subListPlotsAsocciates;
	}

	/**
	 * @return pagination: Management paginated list of the names of crops.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :Management paginated list of the names of crops.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationPlotsAsocciates: Management paginated plots list.
	 */
	public Paginador getPaginationPlotsAsocciates() {
		return paginationPlotsAsocciates;
	}

	/**
	 * @param paginationPlotsAsocciates
	 *            :Management paginated plots list.
	 */
	public void setPaginationPlotsAsocciates(Paginador paginationPlotsAsocciates) {
		this.paginationPlotsAsocciates = paginationPlotsAsocciates;
	}

	/**
	 * @return nameSearch: Name of the harvest to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name of the harvest to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return year: year to validate the crop records.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            : year to validate the crop records.
	 * 
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return options: Items of values that can take list cropNames.
	 * 
	 */
	public List<SelectItem> getOptions() {
		return options;
	}

	/**
	 * @param options
	 *            : Items of values that can take list cropNames.
	 */
	public void setOptions(List<SelectItem> options) {
		this.options = options;
	}

	/**
	 * @return selectYear: list where you load the years from 2000.
	 */
	public List<Integer> getSelectYear() {
		return selectYear;
	}

	/**
	 * @param selectYear
	 *            : list where you load the years from 2000.
	 */
	public void setSelectYear(List<Integer> selectYear) {
		this.selectYear = selectYear;
	}

	/**
	 * @return nameCrop: identifier crop name to search.
	 */
	public int getNameCrop() {
		return nameCrop;
	}

	/**
	 * @param nameCrop
	 *            : identifier crop name to search.
	 */
	public void setNameCrop(int nameCrop) {
		this.nameCrop = nameCrop;
	}

	/**
	 * @return flagButton: this flag is true if it is loaded from the register.
	 */
	public boolean isFlagButton() {
		return flagButton;
	}

	/**
	 * @param flagButton
	 *            : this flag is true if it is loaded from the register.
	 */
	public void setFlagButton(boolean flagButton) {
		this.flagButton = flagButton;
	}

	/**
	 * @return guarded: flag that indicate when a plot is selected.
	 */
	public boolean isGuarded() {
		return guarded;
	}

	/**
	 * @param guarded
	 *            : flag that indicate when a plot is selected.
	 */
	public void setGuarded(boolean guarded) {
		this.guarded = guarded;
	}

	/**
	 * @return obj: Object with the information of the plots
	 */
	public JsonObject getObj() {
		return obj;
	}

	/**
	 * @param obj
	 *            Object with the information of the plots
	 */
	public void setObj(JsonObject obj) {
		this.obj = obj;
	}

	/**
	 * @return constructionParametersMap : JSON Object with the information of
	 *         the location of the every plot to draw
	 */
	public JsonObject getConstructionParametersMap() {
		return constructionParametersMap;
	}

	/**
	 * @param constructionParametersMap
	 *            :JSON Object with the information of the location of the every
	 *            plot to draw
	 */
	public void setConstructionParametersMap(
			JsonObject constructionParametersMap) {
		this.constructionParametersMap = constructionParametersMap;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial.
	 * list of crops.
	 * 
	 * @modify 19/06/2015 Gerardo.Herrera
	 * 
	 * @return consultCrops: method that consult crops, returns management
	 *         template.
	 */
	public String searchInitialization() {
		nameSearch = "";
		nameCrop = 0;
		return consultCrops();
	}

	/**
	 * Consult the list of crops.
	 * 
	 * @return "gesCrops": redirects to the template to manage crops.
	 */
	public String consultCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		guarded = false;
		listCrops = new ArrayList<Crops>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = cropsDao.quantityCrops(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listCrops = cropsDao.consultCrops(pagination.getInicio(),
					pagination.getRango(), query, parameters);
			listCropNames();
			this.nameSearch = "";
			if ((listCrops == null || listCrops.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listCrops == null || listCrops.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("crops_label_s"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCrops";
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @modify 19/06/2015 Gerardo.Herrera
	 * @modify 08/03/2016 Mabell.Boada
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(c.description) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_description")
					+ ": " + '"' + this.nameSearch + '"');

			if (this.nameCrop != 0) {
				consult.append("AND c.cropNames.idCropName = :keyword3 ");
				item = new SelectItem(this.nameCrop, "keyword3");
				parameters.add(item);
			}
		} else {
			if (this.nameCrop != 0) {
				consult.append("WHERE c.cropNames.idCropName = :keyword ");
				SelectItem item = new SelectItem(this.nameCrop, "keyword");
				parameters.add(item);
			}

		}
	}

	/**
	 * Method to edit or create a new crop.
	 * 
	 * @modify 03/06/2015 Sergio.Ortiz
	 * @modify 22/08/2016 Wilhelm.Boada
	 * @modify 11/10/2016 Claudia.Rey
	 * 
	 * @param crops
	 *            :crop to be add or edit.
	 * @return "regCrops":redirected to the template record crop.
	 */
	public String addEditCrops(Crops crops) {
		try {
			listCropNames();
			selectYear = ControladorFechas.loadYears();
			this.plotAction = ControladorContexto
					.getContextBean(PlotAction.class);
			this.plotAction.setFlagAction(Constantes.FLAG_ACTION_CROPS);
			paginationPlotsAsocciates = new Paginador();
			this.flagButton = true;
			if (crops != null) {
				this.crops = crops;
				this.listPlotsAsocciates = plotsDao
						.cropsPlotsAssociated(this.crops.getIdCrop());
				updateSubList();
				Date date = crops.getRegistrationYear();
				if (date != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					year = cal.get(Calendar.YEAR);
				} else {
					year = 0;
				}
			} else {
				this.crops = new Crops();
				this.crops.setCropNames(new CropNames());
				this.listPlotsAsocciates = new ArrayList<Plot>();
				this.subListPlotsAsocciates = new ArrayList<Plot>();
				year = 0;
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regCrops";
	}

	/**
	 * Method used to save or edit crops.
	 * 
	 * @modify 25/05/2015 Sergio.Ortiz
	 * @modify 06/05/2016 Wilhelm.Boada
	 * 
	 * @return consultCrops: Redirects to manage crops with the list of crops
	 *         updated.
	 */
	public String saveCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		Date dateConverted = null;
		try {
			if (year != 0) {
				try {
					dateConverted = formatter.parse(String.valueOf(year));
				} catch (ParseException ex) {
					dateConverted = null;
				}
				crops.setRegistrationYear(dateConverted);
			}
			if (crops.getIdCrop() != 0) {
				cropsDao.editCrops(crops);
			} else {
				messageLog = "message_registro_guardar";
				cropsDao.saveCrops(crops);
			}
			saveCropsPlots();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							crops.getDescription()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCrops();
	}

	/**
	 * Method to save the crop association to a list of selected plots.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @throws Exception
	 */
	private void saveCropsPlots() throws Exception {
		List<Plot> listCropsPlotsAsocciates = new ArrayList<Plot>();
		listCropsPlotsAsocciates = plotsDao.cropsPlotsAssociated(crops
				.getIdCrop());

		if (this.listPlotsAsocciates != null
				&& listCropsPlotsAsocciates != null) {
			List<Integer> currentIds = new ArrayList<Integer>();
			List<Integer> newsIds = new ArrayList<Integer>();
			/* Lists are filled with only the ids */
			for (Plot listPlots : listCropsPlotsAsocciates) {
				currentIds.add(listPlots.getIdPlot());
			}
			for (Plot listPlotTem : this.listPlotsAsocciates) {
				newsIds.add(listPlotTem.getIdPlot());
			}
			/* Lists are validated */
			List<DatosGuardar> dataList = ValidacionesAction.validarListas(
					currentIds, newsIds);
			for (DatosGuardar saveData : dataList) {
				String action = saveData.getAccion();
				Plot plotSave = new Plot();
				plotSave.setIdPlot(saveData.getIdClase());
				if (Constantes.QUERY_DELETE.equals(action)) {
					CropsPlots cropsPlotsSave = cropsPlotsDao
							.consultCropsPlots(crops, plotSave);
					cropsPlotsDao.deleteCropsPlots(cropsPlotsSave);
				} else {
					if (Constantes.QUERY_INSERT.equals(action)) {
						saveCropsPlot(plotSave);
					}
				}
			}
		}
	}

	/**
	 * Saves the related crop Plots.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @param plot
	 *            : Crop related plot.
	 * @throws Exception
	 */
	private void saveCropsPlot(Plot plot) throws Exception {
		CropsPlots cropsPlots = new CropsPlots();
		CropsPlotsPK cropsPlotsPK = new CropsPlotsPK();
		cropsPlotsPK.setCrops(crops);
		cropsPlotsPK.setPlot(plot);
		cropsPlots.setCropsPlotsPK(cropsPlotsPK);
		cropsPlotsDao.saveCropsPlots(cropsPlots);

	}

	/**
	 * Method to remove a Crops of the database.
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return consultCrops(): Consult the list of Crops and returns to manage
	 *         Crops.
	 */
	public String removeCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			cropsDao.removeCrops(crops);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					crops.getDescription()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					crops.getDescription());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultCrops();
	}

	/**
	 * Method that loads a CropNames list.
	 * 
	 * @throws Exception
	 */
	private void listCropNames() throws Exception {
		options = new ArrayList<SelectItem>();
		List<CropNames> listCropsnames = cropNamesDao.listCropNames();
		if (listCropsnames != null) {
			for (CropNames cropNames : listCropsnames) {
				options.add(new SelectItem(cropNames.getIdCropName(), cropNames
						.getCropName()));
			}
		}

	}

	/**
	 * Valid fields are required in the view so that you can load the button
	 * "add plots".
	 * 
	 * @author Sergio.Ortiz
	 * @modify 24/06/2015 Gerardo.Herrera
	 * @modify 06/05/2016 Wilhelm.Boada
	 */
	public void requiredOk() {
		if (crops.getCropNames().getIdCropName() == 0
				|| "".equals(crops.getCropNames().getIdCropName())) {
			ControladorContexto.mensajeRequeridos("formCrops:cropNames");
		}
		if (crops.getInitialDate() == null || "".equals(crops.getInitialDate())) {
			ControladorContexto.mensajeRequeridos("formCrops:fechaInicio");
		}
		if (crops.getFinalDate() == null || "".equals(crops.getFinalDate())) {
			ControladorContexto.mensajeRequeridos("formCrops:fechFin");
		}
		if (this.year == 0) {
			ControladorContexto.mensajeRequeridos("formCrops:cropAnyo");
		}
		if (crops.getDescription() == null || "".equals(crops.getDescription())) {
			ControladorContexto.mensajeRequeridos("formCrops:txtDescripcion");
		}
	}

	/**
	 * Method to validate that the start date and end date are selected.
	 */
	public void requiredDates() {
		FacesContext contexto = FacesContext.getCurrentInstance();
		ResourceBundle bundleCrop = contexto.getApplication()
				.getResourceBundle(contexto, "messageLifeCycle");
		try {
			if ((crops.getInitialDate() == null || "".equals(crops
					.getInitialDate()))
					|| (crops.getFinalDate() == null || "".equals(crops
							.getFinalDate()))) {
				ControladorContexto.mensajeError(null,
						"formCrops:mensajeBusquedaMenu",
						bundleCrop.getString("crops_label_date_required"));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows update the crops list that be shown in the view.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void updateSubList() {
		try {
			long amount = (long) listPlotsAsocciates.size();
			paginationPlotsAsocciates.paginarRangoDefinido(amount, 5);
			int totalReg = paginationPlotsAsocciates.getRango();
			int start = paginationPlotsAsocciates.getInicio();
			int rank = start + totalReg;
			if (listPlotsAsocciates.size() < rank) {
				rank = listPlotsAsocciates.size();
			}
			this.subListPlotsAsocciates = listPlotsAsocciates.subList(start,
					rank);

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that eliminates the plot selected by the user from the list
	 * listPlotsAsocciates.
	 * 
	 * @author Sergio.Ortiz
	 * @modify 17/11/2015 Cristhian.Pico
	 * @modify 17/08/2016 Wilhelm.Boada
	 * 
	 * @param plot
	 *            : plot to remove of the list.
	 */
	public void removePlotList(Plot plot) {
		for (Plot listPlot : this.listPlotsAsocciates) {
			if (listPlot.equals(plot)) {
				this.listPlotsAsocciates.remove(plot);
				break;
			}
		}
		updateSubList();
	}

	/**
	 * This method maintain the plots selected.
	 * 
	 * @author Claudia.Rey
	 * 
	 * @param plots
	 *            : List of plots.
	 * @param listPlotCrops
	 *            : Relation between plots and crops.
	 */
	public void maintainPlotSelect(List<Plot> plots, List<Plot> listPlotCrops) {
		for (Plot plot : plots) {
			for (Plot plot2 : listPlotCrops) {
				if (plot.getIdPlot() == plot2.getIdPlot()) {
					plot.setSelected(true);
				}
			}
		}
	}

	/**
	 * This method initializes the pager with a fixed range to manage the plot
	 * list and it gets a sublist of plots.
	 * 
	 * @author Claudia.Rey
	 */
	public void initializeList() {
		try {
			guarded = false;
			subListPlotsAsocciates = new ArrayList<Plot>();
			if (this.listPlotsAsocciates != null) {
				Collections.sort(this.listPlotsAsocciates);
				subListPlotsAsocciates.addAll(this.listPlotsAsocciates);
			}
			Long paginationAmount = (long) this.subListPlotsAsocciates.size();

			this.paginationPlotsAsocciates.paginarRangoDefinido(
					paginationAmount, 5);
			int inicial = this.paginationPlotsAsocciates.getItemInicial() - 1;
			int fin = this.paginationPlotsAsocciates.getItemFinal();
			this.subListPlotsAsocciates = this.subListPlotsAsocciates.subList(
					inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow load parameter in JSON to load the form of the
	 * plantation
	 * 
	 * @author Andres.Gomez
	 * 
	 */
	public void loadMapPlantation() {

		constructionParametersMap = new JsonObject();
		constructionParametersMap.addProperty("1", 5);
		constructionParametersMap.addProperty("2", 5);
		constructionParametersMap.addProperty("3", 6);
		constructionParametersMap.addProperty("4", 6);
		constructionParametersMap.addProperty("5", 6);
		constructionParametersMap.addProperty("6", 7);
		constructionParametersMap.addProperty("7", 7);
		constructionParametersMap.addProperty("8", 8);
		constructionParametersMap.addProperty("9", 8);
		constructionParametersMap.addProperty("10", 10);
		constructionParametersMap.addProperty("11", 9);
		constructionParametersMap.addProperty("12", 9);
		constructionParametersMap.addProperty("13", 10);
		constructionParametersMap.addProperty("14", 11);
		constructionParametersMap.addProperty("15", 11);
		constructionParametersMap.addProperty("16", 12);
		constructionParametersMap.addProperty("17", 12);
		constructionParametersMap.addProperty("18", 6);
		constructionParametersMap.addProperty("19", 5);
		constructionParametersMap.addProperty("20", 5);
		constructionParametersMap.addProperty("21", 5);
		constructionParametersMap.addProperty("22", 5);
		constructionParametersMap.addProperty("23", 5);
		constructionParametersMap.addProperty("24", 5);
		constructionParametersMap.addProperty("25", 5);
		constructionParametersMap.addProperty("26", 5);
		constructionParametersMap.addProperty("27", 5);
		constructionParametersMap.addProperty("28", 5);
		constructionParametersMap.addProperty("29", 5);
		constructionParametersMap.addProperty("30", 5);
		constructionParametersMap.addProperty("31", 5);
		constructionParametersMap.addProperty("32", 4);
		constructionParametersMap.addProperty("33", 2);
		constructionParametersMap.addProperty("34", 3);

		obj = new JsonObject();
		int count = 1;
		for (int i = 0; i <= 221; i++) {
			String property = String.valueOf(count);
			obj.addProperty(property, i);
			count++;
		}
	}

}
