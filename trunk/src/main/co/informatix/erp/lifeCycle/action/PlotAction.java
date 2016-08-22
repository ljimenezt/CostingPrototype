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
import co.informatix.erp.lifeCycle.dao.FarmDao;
import co.informatix.erp.lifeCycle.dao.PlotDao;
import co.informatix.erp.lifeCycle.entities.Farm;
import co.informatix.erp.lifeCycle.entities.Plot;
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

	private boolean pagerState;
	private boolean farmParameter = false;
	private boolean flagPlotActivity = false;
	private int nameFarm;
	private int idSection;
	private Activities activity;

	private List<SelectItem> optionsFarm;
	private List<Plot> listPlots;
	private List<Plot> listPlotsSelected;
	private List<Plot> listPlotDate;
	private Plot plot;
	private Farm farm;
	private Paginador pagination = new Paginador();
	private String nameSearch;

	/**
	 * @return farm: farm related a plot.
	 */
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            farm related a plot.
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
	 * @return farmParameter: parameter that takes the 'true' value if a farm or
	 *         'false' if there is none.
	 */
	public boolean isFarmParameter() {
		return farmParameter;
	}

	/**
	 * @param farmParameter
	 *            : parameter that takes the 'true' value if a farm or 'false'
	 *            if there is none.
	 */
	public void setFarmParameter(boolean farmParameter) {
		this.farmParameter = farmParameter;
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
	 * @return nameFarm: identifier name of the farm to look for.
	 */
	public int getNameFarm() {
		return nameFarm;
	}

	/**
	 * @param nameFarm
	 *            :identifier name of the farm to look for.
	 */
	public void setNameFarm(int nameFarm) {
		this.nameFarm = nameFarm;
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
	 * 
	 * @return consultPlots: plots query method returns to the template
	 *         management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		if (!farmParameter) {
			this.farm = null;
			this.nameFarm = 0;
			this.idSection = 0;
		}
		return consultPlots();
	}

	/**
	 * It is responsible for initializing the changing state flag farmParameter.
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @return searchInitialization(): method to initialize the parameters of
	 *         the search and load the initial listing of the plots.
	 */
	public String initializeSearchPlot() {
		this.farmParameter = false;
		return searchInitialization();
	}

	/**
	 * Consult the list of the plots.
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 15/05/2015 Sergio.Ortiz
	 * @modify 05/08/2016 Gerardo.Herrera
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
			listFarms();
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
	 * 
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		boolean selection = false;

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

		if (this.farm != null) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("p.farm.idFarm=:keyword2 ");
			SelectItem item = new SelectItem(farm.getIdFarm(), "keyword2");
			parameters.add(item);
			selection = true;
		}

		if (this.nameFarm != 0) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("p.farm.idFarm = :keyword3 ");
			SelectItem item = new SelectItem(this.nameFarm, "keyword3");
			parameters.add(item);
			selection = true;
		}

		if (this.idSection != 0) {
			consult.append(selection ? "AND " : "WHERE ");
			consult.append("p.section.idSection = :keyword4 ");
			SelectItem item = new SelectItem(this.idSection, "keyword4");
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
	 * This method fills the various objects associated with a plot.
	 * 
	 * @author Andres.Gomez
	 * @modify 27/04/2016 Gerardo.Herrera
	 * 
	 * @throws Exception
	 */
	private void loadDetailsPlot() throws Exception {
		if (this.listPlots != null) {
			for (Plot plot : this.listPlots) {
				Farm farm = (Farm) this.plotDao.consultObjectPlot("farm",
						plot.getIdPlot());
				plot.setFarm(farm);
			}
		}
	}

	/**
	 * Method that loads a farms list.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @throws Exception
	 */
	private void listFarms() throws Exception {
		optionsFarm = new ArrayList<SelectItem>();
		List<Farm> listFarms = farmDao.farmsList();
		if (listFarms != null) {
			for (Farm farms : listFarms) {
				optionsFarm.add(new SelectItem(farms.getIdFarm(), farms
						.getName()));
			}
		}
	}

	/**
	 * Method to edit or create a new plot.
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 01/10/2015 Gerardo.Herrera
	 * 
	 * @param plot
	 *            :plot to be add or edit.
	 * 
	 * @return "regPlot":redirects to register plot template.
	 */
	public String addEditPlot(Plot plot) {
		try {
			if (plot != null) {
				this.plot = plot;
				this.farm = this.plot.getFarm();
			} else {
				this.plot = new Plot();
				if (!farmParameter)
					this.farm = new Farm();
			}
			listFarms();
			loadDetailsPlot();
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
	 * 
	 * @param context
	 *            : application context.
	 * 
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
			int idFarm = farm.getIdFarm();
			Plot plotAux = plotDao.nameExist(name, id, idFarm);
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
	 * 
	 * @return searchInitialization: Redirects to manage the list of sites with
	 *         plots updated
	 */
	public String savePlot() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			plot.setFarm(farm);
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
	 * 
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
					consult, parameters);
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
					pagination.getRango(), consult, parameters);
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
	 * 
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

}
