package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.action.ActivityPlotAction;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.dao.PlotDao;
import co.informatix.erp.lifeCycle.dao.SectionDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.lifeCycle.entities.Plot;
import co.informatix.erp.lifeCycle.entities.Section;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the plots that may be in the BD
 * 
 * The logic is to consult, edit or add plots
 * 
 * @author Johnatan.Naranjo
 * 
 * @modify 21/07/2015 Andres.Gomez
 * @modify 19/05/2015 Sergio.Ortiz
 * @modify 25/06/2015 Gerardo.Herrera
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PlotAction implements Serializable {

	@EJB
	private PlotDao plotDao;
	@EJB
	private FarmDao farmDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private SectionDao sectionDao;

	private boolean pagerState;
	private boolean flagPlotActivity = false;
	private boolean flagSection = false;
	private boolean flagPlotSection = false;
	private int idSection;
	private int idCropNames;

	private List<SelectItem> optionsFarm;
	private List<Plot> listPlots;
	private List<Plot> listPlotsSelected;
	private List<Plot> listPlotDate;
	private List<SelectItem> itemsCropNames;
	private List<SelectItem> itemsSection;
	private Plot plot;
	private Farm farm;
	private Activities activity;
	private Paginador pagination = new Paginador();
	private String nameSearch;

	private String flagAction;

	/**
	 * @return flagAction: flag that indicate that the action is of section
	 */
	public String getFlagAction() {
		return flagAction;
	}

	/**
	 * @param flagAction
	 *            : flag that indicate that the action is of section
	 */
	public void setFlagAction(String flagAction) {
		this.flagAction = flagAction;
	}

	/**
	 * @return farm: farm related a plot.
	 */
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            : farm related a plot.
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	/**
	 * @return listPlots: list of plots.
	 */
	public List<Plot> getListPlots() {
		return listPlots;
	}

	/**
	 * @param listPlots
	 *            :list of plots.
	 */
	public void setListPlots(List<Plot> listPlots) {
		this.listPlots = listPlots;
	}

	/**
	 * @return listPlotsSelected: list of selected plots.
	 */
	public List<Plot> getListPlotsSelected() {
		return listPlotsSelected;
	}

	/**
	 * @param listPlotsSelected
	 *            : list of selected plots.
	 */
	public void setListPlotsSelected(List<Plot> listPlotsSelected) {
		this.listPlotsSelected = listPlotsSelected;
	}

	/**
	 * @return plot: gets the registration of plot.
	 */
	public Plot getPlot() {
		return plot;
	}

	/**
	 * @param plot
	 *            :sets the registration of plot.
	 */
	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	/**
	 * @return pagination: Management paged list estates.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :Management paged list estates.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Name plot to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name plot to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listPlotDate: plot list found on a certain date.
	 */
	public List<Plot> getListPlotDate() {
		return listPlotDate;
	}

	/**
	 * @param listPlotDate
	 *            : plot list found on a certain date.
	 */
	public void setListPlotDate(List<Plot> listPlotDate) {
		this.listPlotDate = listPlotDate;
	}

	/**
	 * @return itemsCropNames: cropName associated with a plot.
	 */
	public List<SelectItem> getItemsCropNames() {
		return itemsCropNames;
	}

	/**
	 * @param itemsCropNames
	 *            :cropName associated with a plot.
	 */
	public void setItemsCropNames(List<SelectItem> itemsCropNames) {
		this.itemsCropNames = itemsCropNames;
	}

	/**
	 * @return itemsSection: section associated with a plot.
	 */
	public List<SelectItem> getItemsSection() {
		return itemsSection;
	}

	/**
	 * @param itemsSection
	 *            :section associated with a plot.
	 */
	public void setItemsSection(List<SelectItem> itemsSection) {
		this.itemsSection = itemsSection;
	}

	/**
	 * 
	 * @return pagerState: Pager state, "true" if first initialized, "false" If
	 *         you are browsing pager.
	 */
	public boolean isPagerState() {
		return pagerState;
	}

	/**
	 * 
	 * @param pagerState
	 *            : Pager state, "true" if first initialized, "false" If you are
	 *            browsing pager.
	 */
	public void setPagerState(boolean pagerState) {
		this.pagerState = pagerState;
	}

	/**
	 * @return flagPlotActivity: Flag from activityPlot for modify query
	 */
	public boolean isFlagPlotActivity() {
		return flagPlotActivity;
	}

	/**
	 * @param flagPlotActivity
	 *            : Flag from activityPlot for modify query
	 */
	public void setFlagPlotActivity(boolean flagPlotActivity) {
		this.flagPlotActivity = flagPlotActivity;
	}

	/**
	 * @return flagPlotSection: Flag from plotSection for modify query
	 */
	public boolean isFlagPlotSection() {
		return flagPlotSection;
	}

	/**
	 * @param flagPlotSection
	 *            : Flag from plotSection for modify query
	 */
	public void setFlagPlotSection(boolean flagPlotSection) {
		this.flagPlotSection = flagPlotSection;
	}

	/**
	 * @return idSection: identifier section to look for.
	 */
	public int getIdSection() {
		return idSection;
	}

	/**
	 * @param idSection
	 *            :identifier section to look for.
	 */
	public void setIdSection(int idSection) {
		this.idSection = idSection;
	}

	/**
	 * @return idCropNames: CropNames identifier.
	 */
	public int getIdCropNames() {
		return idCropNames;
	}

	/**
	 * @param idCropNames
	 *            : CropNames identifier.
	 */
	public void setIdCropNames(int idCropNames) {
		this.idCropNames = idCropNames;
	}

	/**
	 * @return activity: Activity associated to plot
	 */
	public Activities getActivity() {
		return activity;
	}

	/**
	 * @param activity
	 *            : Activity associated to plot
	 */
	public void setActivity(Activities activity) {
		this.activity = activity;
	}

	/**
	 * @return optionsFarm: Items of values that can take the list of farms.
	 */
	public List<SelectItem> getOptionsFarm() {
		return optionsFarm;
	}

	/**
	 * @param optionsFarm
	 *            :Items of values that can take the list of farms.
	 */
	public void setOptionsFarm(List<SelectItem> optionsFarm) {
		this.optionsFarm = optionsFarm;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the plots.
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 01/10/2015 Gerardo.Herrera
	 * @modify 22/08/2016 Wilhelm.Boada
	 * @modify 06/10/2016 Claudia.Rey
	 * 
	 * @return consultPlots: plots query method returns to the template
	 *         management.
	 */
	public String searchInitialization() {
		String flag = "";
		try {
			nameSearch = "";
			if (flagAction != "" && flagAction != null) {
				if ((flagAction.equals(Constantes.FLAG_ACTION_SECTION))
						&& !this.flagSection && !this.flagPlotSection) {
					this.idCropNames = 0;
					this.idSection = 0;
				}
				this.flagSection = false;
				loadCropNames();
				pagination = new Paginador();
				if (flagAction.equals(Constantes.FLAG_ACTION_CROPS)) {
					consultPlotForDate();
				} else {
					flag = consultPlots();
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return flag;
	}

	/**
	 * It is responsible for initializing the changing state flag
	 * flagPlotSection.
	 * 
	 * @author Gerardo.Herrera
	 * @modify 01/09/2016 Wilhelm.Boada
	 * @modify 11/10/2016 Claudia.Rey
	 * 
	 * @return searchInitialization(): method to initialize the parameters of
	 *         the search and load the initial listing of the plots.
	 */
	public String initializeSearchPlot() {
		this.flagPlotSection = false;
		this.flagAction = Constantes.FLAG_ACTION_SECTION;
		return searchInitialization();
	}

	/**
	 * Consult the list of the plots.
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 15/05/2015 Sergio.Ortiz
	 * @modify 05/08/2016 Gerardo.Herrera
	 * @modify 29/08/2016 Wilhelm.Boada
	 * 
	 * @return back: redirects to the template to manage parcels or register
	 *         popups Crops.
	 */
	public String consultPlots() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listPlots = new ArrayList<Plot>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";

		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		String back = fromModal ? "" : "gesPlot";
		try {
			advancedSearch(consult, parameters, bundle, unionMessagesSearch);
			Long quantity = plotDao.quantityPlots(consult, parameters);
			if (quantity != null) {
				if (fromModal) {
					pagination.paginarRangoDefinido(quantity, 5);
				} else {
					pagination.paginar(quantity);
				}
			}
			listPlots = plotDao.consultPlots(pagination.getInicio(),
					pagination.getRango(), consult, parameters);
			if ((listPlots == null || listPlots.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listPlots == null || listPlots.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("plot_label_s"),
								unionMessagesSearch);
			}
			if (listPlotsSelected != null) {
				maintainPlotsSelected(listPlots, listPlotsSelected);
			}
			maintainPlots(fromModal);
			validations.setMensajeBusquedaPopUp(messageSearch);
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return back;
	}

	/**
	 * This method maintain the plots selected when there are pagination into
	 * the activity plot management.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param fromModal
	 *            : flag for identifier the call.
	 */
	private void maintainPlots(boolean fromModal) {
		if (fromModal) {
			ActivityPlotAction activityPlotAction = ControladorContexto
					.getContextBean(ActivityPlotAction.class);
			if (activityPlotAction.getListActivityPlot() != null
					&& activityPlotAction.getListActivityPlot().size() > 0)
				activityPlotAction.maintainHrSelected(listPlots,
						activityPlotAction.getListActivityPlot());
		}
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @modify 17/07/2015 Andres.Gomez
	 * @modify 08/07/2016 Gerardo.Herrera
	 * @modify 22/08/2016 Wilhelm.Boada
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		boolean selection = false;

		if (this.idCropNames != 0) {
			consult.append("WHERE cn.idCropName=:keyword2 ");
			SelectItem item = new SelectItem(this.idCropNames, "keyword2");
			parameters.add(item);
			selection = true;
		}

		if (this.idSection != 0 && !this.flagPlotSection) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append(idSection < 0 ? "p.section.idSection IS NULL "
					: "p.section.idSection = :keyword4 ");
			if (this.idSection > 0) {
				SelectItem item = new SelectItem(this.idSection, "keyword4");
				parameters.add(item);
			}
			selection = true;
		}

		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("UPPER(p.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
			selection = true;
		}

		if (flagPlotSection) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("(p.section.idSection = :idSection ");
			consult.append("OR p.section.idSection IS NULL) ");
			SelectItem item = new SelectItem(this.idSection, "idSection");
			parameters.add(item);
			selection = true;
		}

		if (flagPlotActivity && this.activity != null) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("p NOT IN ");
			consult.append("(SELECT p FROM ActivityPlot ap ");
			consult.append("JOIN ap.activityPlotPK.plot p ");
			consult.append("JOIN ap.activityPlotPK.activity a ");
			consult.append(activity.getCycle() == null ? "WHERE a.idActivity = :idActivity ) "
					: "WHERE a.cycle.idCycle = :idCycle) ");
			consult.append("AND p.numberOfTrees > 0 ");
			if (activity.getCycle() == null) {
				SelectItem item = new SelectItem(this.activity.getIdActivity(),
						"idActivity");
				parameters.add(item);
			} else {
				SelectItem item = new SelectItem(this.activity.getCycle()
						.getIdCycle(), "idCycle");
				parameters.add(item);
			}
			selection = true;
		}

	}

	/**
	 * Method to edit or create a new plot.
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 01/10/2015 Gerardo.Herrera
	 * @modify 29/08/2016 Wilhelm.Boada
	 * 
	 * @param plot
	 *            :plot to be add or edit.
	 * @return "regPlot":redirects to register plot template.
	 */
	public String addEditPlot(Plot plot) {
		try {
			Section section = new Section();
			if (plot != null) {
				this.plot = plot;
				section = sectionDao.consultSectionByPlot(plot.getIdPlot());
				this.idCropNames = this.plot.getCropNames().getIdCropName();
			} else {
				this.plot = new Plot();
				this.plot.setCropNames(new CropNames());
				this.plot.getCropNames().setIdCropName(this.idCropNames);
			}

			if (section == null || this.idSection < 0) {
				this.idSection = 0;
			} else {
				this.idSection = section.getIdSection();
			}
			this.flagSection = false;
			loadCropNames();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPlot";
	}

	/**
	 * To validate the name of the plot, so it is not repeated in the farm and
	 * in the database and valid against XSS.
	 * 
	 * @modify 14/03/2016 Jhair.Leal
	 * @modify 29/08/2016 Wilhelm.Boada
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = plot.getIdPlot();
			Plot plotAux = plotDao.nameExist(name, id, plot.getCropNames()
					.getIdCropName());
			if (plotAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExistence), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(name, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the plots.
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 29/08/2016 Wilhelm.Boada
	 * 
	 * @return searchInitialization: Redirects to manage the list of sites with
	 *         plots updated
	 */
	public String savePlot() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			plot.getCropNames().setIdCropName(idCropNames);
			if (this.idSection != 0) {
				plot.setSection(new Section());
				plot.getSection().setIdSection(this.idSection);
			} else {
				plot.setSection(null);
			}
			if (plot.getIdPlot() != 0) {
				plotDao.editPlot(plot);
			} else {
				messageLog = "message_registro_guardar";
				plotDao.savePlot(plot);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							plot.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		this.flagSection = true;
		return searchInitialization();
	}

	/**
	 * Method to remove a plot of the database.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultPlots(): Consult the list of parcels and returns to manage
	 *         plot.
	 */
	public String removePlot() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			plotDao.removePlot(plot);
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(
							bundle.getString("message_registro_eliminar"),
							plot.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					plot.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultPlots();
	}

	/**
	 * Method to validate that dishes are available depending on the date range
	 * entered in the view of crops.
	 * 
	 * @author Sergio.Ortiz
	 * @modify Gerardo.Herrera
	 * @modify 11/03/2016 Mabell.Boada
	 * @modify 05/10/2016 Claudia.Rey
	 */
	public void consultPlotForDate() {
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessageSearch = new StringBuilder();
		String messageSearch = "";
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		CropsAction crops = ControladorContexto
				.getContextBean(CropsAction.class);
		listPlotDate = new ArrayList<Plot>();
		try {
			searchAdvancedPopup(consult, parameters, bundle, unionMessageSearch);
			Long amount = plotDao.quantityPlotsFiltered(crops
					.getListPlotsAsocciates(), crops.getCrops()
					.getInitialDate(), crops.getCrops().getFinalDate(),
					consult, parameters, crops.isGuarded());
			if (pagerState) {
				pagination = new Paginador();
				pagerState = false;
			}
			if (amount != null) {
				pagination.paginarRangoDefinido(amount, 5);
			}
			listPlotDate = plotDao.searchCopsPlotsDate(crops.getCrops()
					.getInitialDate(), crops.getCrops().getFinalDate(), crops
					.getListPlotsAsocciates(), pagination.getInicio(),
					pagination.getRango(), consult, parameters, crops
							.isGuarded());
			if ((listPlotDate == null || listPlotDate.size() <= 0)
					&& !"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessageSearch);
			} else if (listPlotDate == null || listPlotDate.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessageSearch.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				messageSearch = MessageFormat.format(message,
						bundleLifeCycle.getString("plot_label_s"),
						unionMessageSearch);
			}
			if (listPlotsSelected != null) {
				maintainPlotsSelected(listPlotDate, listPlotsSelected);
			}
			maintainPlotsCrops();
			validations.setMensajeBusquedaPopUp(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method weapon search query to pop into the user selected POPUP.
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
	private void searchAdvancedPopup(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(p.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * This method allows load of cropNames list.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @throws Exception
	 */
	private void loadCropNames() throws Exception {
		itemsCropNames = new ArrayList<SelectItem>();
		List<CropNames> listCropNames = cropNamesDao.listCropNames();
		if (listCropNames != null) {
			for (CropNames cropNames : listCropNames) {
				itemsCropNames.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}
		loadSection();
	}

	/**
	 * This method allows load of sections list.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void loadSection() {
		try {
			itemsSection = new ArrayList<SelectItem>();
			List<Section> listSection = sectionDao
					.listSection(this.idCropNames);
			if (listSection != null) {
				for (Section section : listSection) {
					itemsSection.add(new SelectItem(section.getIdSection(),
							section.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows load the drop down and the list.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void loadComboAndList() {
		this.idSection = 0;
		loadSection();
		consultPlots();
	}

	/**
	 * This method allows maintaining list of selected plot whether the search
	 * of plot is run again.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param plotsList
	 *            : plotsList consult of the database.
	 * @param plotsListSelected
	 *            : plotsListSelected for compare to the hrList.
	 */
	private void maintainPlotsSelected(List<Plot> plotsList,
			List<Plot> plotsListSelected) {
		for (Plot plot : plotsList) {
			for (Plot selectedPlot : plotsListSelected) {
				if (plot.getIdPlot() == selectedPlot.getIdPlot()) {
					plot.setSelected(true);
				}
			}
		}
	}

	/**
	 * Selects a single plot for display the associated transaction.
	 * 
	 * @author Wilhelm.Boada
	 * 
	 * @param flagSelected
	 *            : plot selected on the view.
	 */
	public void selectPlot(boolean flagSelected) {
		this.plot.setSelected(flagSelected);
		if (this.plot.isSelected()) {
			this.listPlotsSelected.add(plot);
		} else {
			this.listPlotsSelected.remove(plot);
		}
	}

	/**
	 * This method allows initialize the values on the plotAction for display in
	 * the view.
	 * 
	 * @author Wilhelm.Boada
	 * @modify 06/10/2016 Claudia.Rey
	 * 
	 * @param plotsList
	 *            : plotsList consult of the database associated to the section.
	 */
	public void initializePlotsBySection(List<Plot> plotList) {
		listPlotsSelected = new ArrayList<Plot>();
		if (plotList != null) {
			listPlotsSelected.addAll(plotList);
		}
		flagPlotSection = true;
		searchInitialization();
	}

	/**
	 * New This method maintain the plots selected when there are pagination
	 * into the crop plot management.
	 * 
	 * @author Claudia.Rey
	 * @modify 02/03/2017 Claudia.Rey
	 */
	private void maintainPlotsCrops() {
		CropsAction cropAction = ControladorContexto
				.getContextBean(CropsAction.class);
		if (cropAction.getListPlotsAsocciates() != null
				&& cropAction.getListPlotsAsocciates().size() > 0)
			cropAction.maintainPlotSelect(listPlotDate,
					cropAction.getListPlotsAsocciates());
	}

}