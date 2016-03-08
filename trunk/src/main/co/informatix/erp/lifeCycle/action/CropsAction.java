package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
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
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utils.ValidacionesAction.DatosGuardar;

/**
 * This class allows the logic of the crops that may be in the BD
 * 
 * The logic is to consult, edit or add crops
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
	private Plot plotEliminar;
	private Crops crops;
	private List<Crops> listaCrops;
	private List<Plot> listaPlotsAsociados;
	private List<SelectItem> opciones;
	private List<SelectItem> seleccionarAnyo;
	private Paginador paginador = new Paginador();
	private String nombreBuscar;
	private String anyo;
	private int nombreCrop;

	/**
	 * @return cropNames: crop names associated with the crop
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            crop names associated with the crop
	 */
	public void setCropNames(CropNames cropNames) {
		this.nombreCrop = cropNames.getIdCropName();
	}

	/**
	 * @return plotEliminar: plot to eliminate Plots Selected List
	 */
	public Plot getPlotEliminar() {
		return plotEliminar;
	}

	/**
	 * @param plotEliminar
	 *            : plot to eliminate Plots Selected List
	 */
	public void setPlotEliminar(Plot plotEliminar) {
		this.plotEliminar = plotEliminar;
	}

	/**
	 * @return crops: get the record crop
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            :set the record harvest
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return listaCrops: crops list
	 */
	public List<Crops> getListaCrops() {
		return listaCrops;
	}

	/**
	 * @param listaCrops
	 *            :crops list
	 */
	public void setListaCrops(List<Crops> listaCrops) {
		this.listaCrops = listaCrops;
	}

	/**
	 * @return listaPlotsAsociados: Plots list Associates Crops
	 */
	public List<Plot> getListaPlotsAsociados() {
		return listaPlotsAsociados;
	}

	/**
	 * @param listaPlotsAsociados
	 *            : Plots list Associates Crops
	 */
	public void setListaPlotsAsociados(List<Plot> listaPlotsAsociados) {
		this.listaPlotsAsociados = listaPlotsAsociados;
	}

	/**
	 * @return paginador: Management paginated list of the names of crops.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paginated list of the names of crops.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nombreBuscar: Name of the harvest to search
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name of the harvest to search
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return anyo: year to validate the crop records
	 */
	public String getAnyo() {
		return anyo;
	}

	/**
	 * @param anyo
	 *            : year to validate the crop records
	 * 
	 */
	public void setAnyo(String anyo) {
		this.anyo = anyo;
	}

	/**
	 * @return opciones: Items of values that can take list cropNames
	 * 
	 */
	public List<SelectItem> getOpciones() {
		return opciones;
	}

	/**
	 * @param opciones
	 *            : Items of values that can take list cropNames
	 */
	public void setOpciones(List<SelectItem> opciones) {
		this.opciones = opciones;
	}

	/**
	 * @return seleccionarAnyo: list where you load the years from 2000
	 */
	public List<SelectItem> getSeleccionarAnyo() {
		return seleccionarAnyo;
	}

	/**
	 * @param seleccionarAnyo
	 *            : list where you load the years from 2000
	 */
	public void setSeleccionarAnyo(List<SelectItem> seleccionarAnyo) {
		this.seleccionarAnyo = seleccionarAnyo;
	}

	/**
	 * @return nombreCrop: identifier crop name to search
	 */
	public int getNombreCrop() {
		return nombreCrop;
	}

	/**
	 * @param nombreCrop
	 *            : identifier crop name to search
	 */
	public void setNombreCrop(int nombreCrop) {
		this.nombreCrop = nombreCrop;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of crops
	 * 
	 * @modify 19/06/2015 Gerardo.Herrera
	 * 
	 * @return consultarCrops: method that consult crops, returns management
	 *         template.
	 */
	public String inicializarBusqueda() {
		nombreBuscar = "";
		nombreCrop = 0;
		return consultarCrops();
	}

	/**
	 * Consult the list of crops
	 * 
	 * @return "gesCrops": redirects to the template to manage crops
	 */
	public String consultarCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaCrops = new ArrayList<Crops>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = cropsDao.cantidadCrops(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			listaCrops = cropsDao.consultarCrops(paginador.getInicio(),
					paginador.getRango(), consulta, parametros);
			listaCropNames();
			this.nombreBuscar = "";
			if ((listaCrops == null || listaCrops.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaCrops == null || listaCrops.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("crops_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
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
			consult.append("WHERE UPPER(c.description) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_descripcion")
					+ ": " + '"' + this.nombreBuscar + '"');

			if (this.nombreCrop != 0) {
				consult.append("AND c.cropNames.idCropName = :keyword3 ");
				item = new SelectItem(this.nombreCrop, "keyword3");
				parameters.add(item);
			}
		} else {
			if (this.nombreCrop != 0) {
				consult.append("WHERE c.cropNames.idCropName = :keyword ");
				SelectItem item = new SelectItem(this.nombreCrop, "keyword");
				parameters.add(item);
			}

		}
	}

	/**
	 * Method to edit or create a new crop.
	 * 
	 * @modify 03/06/2015 Sergio.Ortiz
	 * 
	 * @param crops
	 *            :crop to be add or edit
	 * 
	 * @return "regCrops":redirected to the template record crop.
	 * 
	 */
	public String agregarEditarCrops(Crops crops) {
		try {
			listaCropNames();
			seleccionAnyo();
			if (crops != null) {
				this.crops = crops;
				plotsAsociados(this.crops);
				Date date = crops.getRegistrationYear();
				if (date != null) {
					Calendar cal = Calendar.getInstance();
					cal.setTime(date);
					int year = cal.get(Calendar.YEAR);
					setAnyo(String.valueOf(year));
				} else {
					setAnyo("");
				}
			} else {
				this.crops = new Crops();
				this.crops.setCropNames(new CropNames());
				this.listaPlotsAsociados = new ArrayList<Plot>();
				setAnyo("");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regCrops";
	}

	/**
	 * Method used to save or edit crops
	 * 
	 * @modify 25/05/2015 Sergio.Ortiz
	 * 
	 * @return consultarCrops: Redirects to manage crops with the list of crops
	 *         updated
	 */
	public String guardarCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		SimpleDateFormat formatter = new SimpleDateFormat("yyyy");
		String fechaString = getAnyo();
		Date dateConvertida = null;
		try {
			if ((fechaString != null) && (!"".equals(fechaString))) {
				try {
					dateConvertida = formatter.parse(fechaString);
				} catch (ParseException ex) {
					dateConvertida = null;
				}
				crops.setRegistrationYear(dateConvertida);
			}
			if (crops.getIdCrop() != 0) {
				cropsDao.editarCrops(crops);
			} else {
				mensajeRegistro = "message_registro_guardar";
				cropsDao.guardarCrops(crops);
			}
			guardarCropsPlots();
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), crops.getDescription()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarCrops();
	}

	/**
	 * Method to save the crop association to a list of selected plots
	 * 
	 * @author Sergio.Ortiz
	 * @throws Exception
	 */
	private void guardarCropsPlots() throws Exception {
		List<Plot> listaCropsPlotsAsociados = new ArrayList<Plot>();
		listaCropsPlotsAsociados = plotsDao.cropsPlotsAsociados(crops
				.getIdCrop());

		if (this.listaPlotsAsociados != null
				&& listaCropsPlotsAsociados != null) {
			List<Integer> currentIds = new ArrayList<Integer>();
			List<Integer> newsIds = new ArrayList<Integer>();
			/* Lists are filled with only the ids */
			for (Plot listaPlots : listaCropsPlotsAsociados) {
				currentIds.add(listaPlots.getIdPlot());
			}
			for (Plot listaPlotTem : this.listaPlotsAsociados) {
				newsIds.add(listaPlotTem.getIdPlot());
			}
			/* Lists are validated */
			List<DatosGuardar> dataList = ValidacionesAction.validarListas(
					currentIds, newsIds);
			for (DatosGuardar saveData : dataList) {
				String action = saveData.getAccion();
				Plot plotGuardar = new Plot();
				plotGuardar.setIdPlot(saveData.getIdClase());
				if (Constantes.QUERY_DELETE.equals(action)) {
					CropsPlots cropsPlotsGuardar = cropsPlotsDao
							.consultarCropsPlots(crops, plotGuardar);
					cropsPlotsDao.eliminarCropsPlots(cropsPlotsGuardar);
				} else {
					if (Constantes.QUERY_INSERT.equals(action)) {
						guardarCropsPlot(plotGuardar);
					}
				}
			}
		}
	}

	/**
	 * Saves the related crop Plots
	 * 
	 * @author Sergio.Ortiz
	 * @param plot
	 *            : Crop related plot
	 * @throws Exception
	 */
	private void guardarCropsPlot(Plot plot) throws Exception {
		CropsPlots cropsPlots = new CropsPlots();
		CropsPlotsPK cropsPlotsPK = new CropsPlotsPK();
		cropsPlotsPK.setCrops(crops);
		cropsPlotsPK.setPlot(plot);
		cropsPlots.setCropsPlotsPK(cropsPlotsPK);
		cropsPlotsDao.guardarCropsPlots(cropsPlots);

	}

	/**
	 * Method to remove a Crops of the database
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @return consultarCrops(): Consult the list of Crops and returns to manage
	 *         Crops
	 */
	public String eliminarCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			cropsDao.eliminarCrops(crops);
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
		return consultarCrops();
	}

	/**
	 * Method that loads a CropNames list
	 * 
	 * @throws Exception
	 */
	private void listaCropNames() throws Exception {
		opciones = new ArrayList<SelectItem>();
		List<CropNames> listCropsnames = cropNamesDao.listCropNames();
		if (listCropsnames != null) {
			for (CropNames cropNames : listCropsnames) {
				opciones.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}

	}

	/**
	 * Valid fields are required in the view so that you can load the button
	 * "add plots".
	 * 
	 * @author Sergio.Ortiz
	 * @modify 24/06/2015 Gerardo.Herrera
	 * 
	 */
	public void requeridosOk() {
		try {
			if (crops.getInitialDate() == null
					|| "".equals(crops.getInitialDate())) {
				ControladorContexto.mensajeRequeridos("formCrops:fechaInicio");
			}
			if (crops.getFinalDate() == null || "".equals(crops.getFinalDate())) {
				ControladorContexto.mensajeRequeridos("formCrops:fechFin");
			}
			if (crops.getCropNames().getIdCropName() == 0
					|| "".equals(crops.getCropNames().getIdCropName())) {
				ControladorContexto.mensajeRequeridos("formCrops:cropNames");
			}
			if (crops.getDescription() == null
					|| "".equals(crops.getDescription())) {
				ControladorContexto
						.mensajeRequeridos("formCrops:txtDescripcion");
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Plots query associated with the Crop
	 * 
	 * @author Sergio.Ortiz
	 * @param crops
	 *            : class object of the crops which you want to view the
	 *            associated plot
	 * @throws Exception
	 */
	private void plotsAsociados(Crops crops) throws Exception {
		if (crops != null) {
			this.listaPlotsAsociados = new ArrayList<Plot>();
			this.listaPlotsAsociados = plotsDao.cropsPlotsAsociados(crops
					.getIdCrop());
		}
	}

	/**
	 * Method which brings me selected plot and those found in the database in a
	 * single list
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @modify 17/11/2015 Cristhian.Pico
	 * 
	 */
	public void agruparListasPlot() {
		PlotAction plot = ControladorContexto.getContextBean(PlotAction.class);
		for (Plot plotAsociados : plot.getListaPlotsSeleccionado()) {
			this.listaPlotsAsociados.add(plotAsociados);
		}
	}

	/**
	 * method that eliminates the plot selected by the user from the list
	 * listaPlotsAsociados
	 * 
	 * @author Sergio.Ortiz
	 * 
	 * @modify 17/11/2015 Cristhian.Pico
	 * 
	 */
	public void eliminarPlotLista() {
		for (Plot listaPlot : this.listaPlotsAsociados) {
			if (listaPlot.equals(plotEliminar)) {
				this.listaPlotsAsociados.remove(plotEliminar);
				break;
			}
		}
	}

	/**
	 * Method to load the years from 2000 through the current year
	 * 
	 * @author Sergio.Ortiz
	 */
	private void seleccionAnyo() {
		Calendar anyo = new GregorianCalendar();
		seleccionarAnyo = new ArrayList<SelectItem>();
		int fechaAnyo = anyo.get(Calendar.YEAR);
		for (int i = 2000; i <= fechaAnyo; i++) {
			String fecha = "" + i;
			seleccionarAnyo.add(new SelectItem(i, fecha));
		}
	}

	/**
	 * Method verifies that the user selected crop the list of plots remain in
	 * sight, otherwise clean selected
	 * 
	 * @author Sergio.Ortiz
	 * @modify 10/12/2015 Andres.Gomez
	 */
	public void compararCrops() {
		if (!this.opciones.get(0).getValue().equals(this.crops)) {
			this.listaPlotsAsociados.removeAll(listaPlotsAsociados);
		}
	}
}
