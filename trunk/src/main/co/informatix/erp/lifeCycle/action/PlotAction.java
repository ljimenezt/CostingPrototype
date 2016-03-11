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

	private boolean estadoPaginador;
	private boolean farmParameter = false;
	private int nombreFarm;

	private List<SelectItem> opcionesFarm;
	private List<Plot> listaPlots;
	private List<Plot> listaPlotsSeleccionado;
	private List<Plot> listaPlotFecha;
	private Plot plot;
	private Farm farm;
	private Paginador paginador = new Paginador();
	private String nombreBuscar;

	/**
	 * @return farm: farm related a plot
	 */
	public Farm getFarm() {
		return farm;
	}

	/**
	 * @param farm
	 *            farm related a plot
	 */
	public void setFarm(Farm farm) {
		this.farm = farm;
	}

	/**
	 * @return listaPlots: list of plots
	 */
	public List<Plot> getListaPlots() {
		return listaPlots;
	}

	/**
	 * @param listaPlots
	 *            :list of plots
	 */
	public void setListaPlots(List<Plot> listaPlots) {
		this.listaPlots = listaPlots;
	}

	/**
	 * @return listaPlotsSeleccionado: list of selected plots
	 */
	public List<Plot> getListaPlotsSeleccionado() {
		return listaPlotsSeleccionado;
	}

	/**
	 * @param listaPlotsSeleccionado
	 *            : list of selected plots
	 */
	public void setListaPlotsSeleccionado(List<Plot> listaPlotsSeleccionado) {
		this.listaPlotsSeleccionado = listaPlotsSeleccionado;
	}

	/**
	 * @return plot: gets the registration of plot
	 */
	public Plot getPlot() {
		return plot;
	}

	/**
	 * @param plot
	 *            :sets the registration of plot
	 */
	public void setPlot(Plot plot) {
		this.plot = plot;
	}

	/**
	 * @return paginador: Management paged list estates.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paged list estates.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Name plot to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name plot to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return listaPlotFecha: plot list found on a certain date
	 */
	public List<Plot> getListaPlotFecha() {
		return listaPlotFecha;
	}

	/**
	 * @param listaPlotFecha
	 *            : plot list found on a certain date
	 */
	public void setListaPlotFecha(List<Plot> listaPlotFecha) {
		this.listaPlotFecha = listaPlotFecha;
	}

	/**
	 * 
	 * @return estadoPaginador: Pager state, "true" if first initialized,
	 *         "false" If you are browsing pager.
	 */
	public boolean isEstadoPaginador() {
		return estadoPaginador;
	}

	/**
	 * 
	 * @param estadoPaginador
	 *            : Pager state, "true" if first initialized, "false" If you are
	 *            browsing pager.
	 */
	public void setEstadoPaginador(boolean estadoPaginador) {
		this.estadoPaginador = estadoPaginador;
	}

	/**
	 * @return farmParameter: parameter that takes the 'true' value if a farm or
	 *         'false' if there is none
	 */
	public boolean isFarmParameter() {
		return farmParameter;
	}

	/**
	 * @param farmParameter
	 *            : parameter that takes the 'true' value if a farm or 'false'
	 *            if there is none
	 */
	public void setFarmParameter(boolean farmParameter) {
		this.farmParameter = farmParameter;
	}

	/**
	 * @return nombreFarm: identifier name of the farm to look for.
	 */
	public int getNombreFarm() {
		return nombreFarm;
	}

	/**
	 * @param nombreFarm
	 *            :identifier name of the farm to look for.
	 */
	public void setNombreFarm(int nombreFarm) {
		this.nombreFarm = nombreFarm;
	}

	/**
	 * @return opcionesFarm: Items of values that can take the list of farms
	 */
	public List<SelectItem> getOpcionesFarm() {
		return opcionesFarm;
	}

	/**
	 * @param opcionesFarm
	 *            :Items of values that can take the list of farms
	 */
	public void setOpcionesFarm(List<SelectItem> opcionesFarm) {
		this.opcionesFarm = opcionesFarm;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the plots
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 01/10/2015 Gerardo.Herrera
	 * 
	 * @return consultarPlots: plots query method returns to the template
	 *         management.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		if (!farmParameter) {
			this.farm = null;
			this.nombreFarm = 0;
		}
		return consultarPlots();
	}

	/**
	 * It is responsible for initializing the changing state flag farmParameter
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @return inicializarBusqueda(): method to initialize the parameters of the
	 *         search and load the initial listing of the plots
	 */
	public String initializeSearchPlot() {
		this.farmParameter = false;
		return inicializarBusqueda();
	}

	/**
	 * Consult the list of the plots
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * @modify 15/05/2015 Sergio.Ortiz
	 * 
	 * @return retorno: redirects to the template to manage parcels or register
	 *         popup Crops
	 */
	public String consultarPlots() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaPlots = new ArrayList<Plot>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";

		String param2 = ControladorContexto.getParam("param2");
		boolean desdeModal = (param2 != null && "si".equals(param2)) ? true
				: false;
		String retorno = desdeModal ? "" : "gesPlot";

		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = plotDao.cantidadPlots(consulta, parametros);
			if (cantidad != null) {
				if (desdeModal) {
					paginador.paginarRangoDefinido(cantidad, 5);
				} else {
					paginador.paginar(cantidad);
				}
			}
			listaPlots = plotDao.consultarPlots(paginador.getInicio(),
					paginador.getRango(), consulta, parametros);
			listaFarms();
			if ((listaPlots == null || listaPlots.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaPlots == null || listaPlots.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("plot_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return retorno;
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
	 * 
	 * @modify 17/07/2015 Andres.Gomez
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
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {

		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("WHERE UPPER(p.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
			if (this.farm != null) {
				consult.append("AND p.farm.idFarm=:keyword2 ");
				item = new SelectItem(farm.getIdFarm(), "keyword2");
				parameters.add(item);
			}

			if (this.nombreFarm != 0) {
				consult.append("AND p.farm.idFarm = :keyword3 ");
				item = new SelectItem(this.nombreFarm, "keyword3");
				parameters.add(item);
			}

		} else {
			if (this.farm != null) {
				consult.append("WHERE p.farm.idFarm=:keyword2 ");
				SelectItem item = new SelectItem(farm.getIdFarm(), "keyword2");
				parameters.add(item);
			}

			if (this.nombreFarm != 0) {
				consult.append("WHERE p.farm.idFarm = :keyword ");
				SelectItem item = new SelectItem(this.nombreFarm, "keyword");
				parameters.add(item);
			}

		}

	}

	/**
	 * This method fills the various objects associated with a plot
	 * 
	 * @author Andres.Gomez
	 * 
	 * @throws Exception
	 */
	public void cargarDetallesPlot() throws Exception {
		List<Plot> plots = new ArrayList<Plot>();
		if (this.listaPlots != null) {
			plots.addAll(this.listaPlots);
			this.listaPlots = new ArrayList<Plot>();
			for (Plot plot : plots) {
				Farm farm = (Farm) this.plotDao.consultarObjetoPlot("farm",
						plot.getIdPlot());
				plot.setFarm(farm);
				this.listaPlots.add(plot);
			}
		}
	}

	/**
	 * Method that loads a farms list
	 * 
	 * @author Andres.Gomez
	 * 
	 * @throws Exception
	 */
	private void listaFarms() throws Exception {
		opcionesFarm = new ArrayList<SelectItem>();
		List<Farm> listFarms = farmDao.farmsList();
		if (listFarms != null) {
			for (Farm farms : listFarms) {
				opcionesFarm.add(new SelectItem(farms.getIdFarm(), farms
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
	 *            :plot to be add or edit
	 * 
	 * @return "regPlot":redirects to register plot template.
	 */
	public String agregarEditarPlot(Plot plot) {
		try {
			if (plot != null) {
				this.plot = plot;
				this.farm = this.plot.getFarm();
			} else {
				this.plot = new Plot();
				if (!farmParameter)
					this.farm = new Farm();
			}
			listaFarms();
			cargarDetallesPlot();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regPlot";
	}

	/**
	 * To validate the name of the plot, so it is not repeated in the database
	 * and valid against XSS.
	 * 
	 * @param context
	 *            : application context
	 * 
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarNombreXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = plot.getIdPlot();
			Plot plotAux = new Plot();
			plotAux = plotDao.nombreExiste(nombre, id);
			if (plotAux != null) {
				String mensajeExistencia = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(mensajeExistencia), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the plots
	 * 
	 * @modify 21/07/2015 Andres.Gomez
	 * 
	 * @return inicializarBusqueda: Redirects to manage the list of sites with
	 *         plots updated
	 */
	public String guardarPlot() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			plot.setFarm(farm);
			if (plot.getIdPlot() != 0) {
				plotDao.editarPlot(plot);
			} else {
				mensajeRegistro = "message_registro_guardar";
				plotDao.guardarPlot(plot);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), plot.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return inicializarBusqueda();
	}

	/**
	 * Method to remove a plot of the database
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return consultarPlots(): Consult the list of parcels and returns to
	 *         manage plot
	 */
	public String eliminarPlot() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			plotDao.eliminarPlot(plot);
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
		return consultarPlots();
	}

	/**
	 * Method a list is saved with the selected plots in the harvest POPUP
	 * 
	 * @author Sergio.Ortiz
	 */
	public void agregarSeleccionados() {
		listaPlotsSeleccionado = new ArrayList<Plot>();
		try {
			for (Plot plot : listaPlotFecha) {
				if (plot.isSeleccionado()) {
					this.listaPlotsSeleccionado.add(plot);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to validate that dishes are available depending on the date range
	 * entered in the view of crops
	 * 
	 * @author Sergio.Ortiz
	 * @modify Gerardo.Herrera
	 * @modify 11/03/2016 Mabell.Boada
	 * 
	 */
	public void consultarPlotPorFecha() {
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessageSearch = new StringBuilder();
		String messageSearch = "";
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		CropsAction crops = ControladorContexto
				.getContextBean(CropsAction.class);
		listaPlotFecha = new ArrayList<Plot>();
		try {
			busquedaAvanzadaPopup(consult, parameters, bundle,
					unionMessageSearch);
			Long amount = plotDao.cantidadPlotsFiltrados(crops
					.getListaPlotsAsociados(), crops.getCrops()
					.getInitialDate(), crops.getCrops().getFinalDate(),
					consult, parameters);
			if (estadoPaginador) {
				paginador = new Paginador();
				estadoPaginador = false;
			}
			if (amount != null) {
				paginador.paginarRangoDefinido(amount, 5);
			}
			listaPlotFecha = plotDao.buscarCopsPlotsFecha(crops.getCrops()
					.getInitialDate(), crops.getCrops().getFinalDate(), crops
					.getListaPlotsAsociados(), paginador.getInicio(), paginador
					.getRango(), consult, parameters);
			if ((listaPlotFecha == null || listaPlotFecha.size() <= 0)
					&& !"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessageSearch);
			} else if (listaPlotFecha == null || listaPlotFecha.size() <= 0) {
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
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 * 
	 */
	private void busquedaAvanzadaPopup(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("AND UPPER(p.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

}
