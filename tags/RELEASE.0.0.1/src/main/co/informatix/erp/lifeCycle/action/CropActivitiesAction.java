package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
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

import co.informatix.erp.costs.dao.ActivitiesDao;
import co.informatix.erp.costs.dao.CycleStandardActivitiesDao;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.CycleStandardActivities;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows you to assign the logic of activities that may be related
 * to crops in the database. The logic is: insert activities to a particular
 * crop.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CropActivitiesAction implements Serializable {
	@EJB
	private ActivitiesDao activitiesDao;
	@EJB
	private CropsDao cropsDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private ActivityNamesDao activityNamesDao;
	@EJB
	private CycleStandardActivitiesDao cycleStandardActivitiesDao;

	private List<Activities> listaActivities;
	private List<ActivityNames> listaActivityNames;
	private List<CycleStandardActivities> listaCycleStandardActivities;
	private List<SelectItem> opcionesCropNames;
	private List<SelectItem> opcionesCrops;
	private List<Activities> subListActivities;
	private List<CycleStandardActivities> subListCycleStandardActivities;

	private Paginador paginador = new Paginador();
	private Paginador paginadorActivities = new Paginador();
	private Paginador paginadorActivitiesStandardCycle = new Paginador();
	private Paginador paginadorListActivities = new Paginador();

	private Activities activities;
	private Activities eliminarLista;
	private ActivityNames activityNames;
	private Crops crops;
	private CropNames cropNames;
	private CycleStandardActivities cycleStandardActivities;
	private CycleStandardActivities eliminarStandard;

	private String nombreBuscar;
	private String mensajeMiga;

	private Date maxDate;
	private Date initialDateSearch;
	private Date finalDateSearch;

	private int idCropNamesBuscar;
	private int idCropName;

	private boolean limpiar;

	/**
	 * @return listaActivities: List of activities.
	 */
	public List<Activities> getListaActivities() {
		return listaActivities;
	}

	/**
	 * @param listaActivities
	 *            : List of activities.
	 */
	public void setListaActivities(List<Activities> listaActivities) {
		this.listaActivities = listaActivities;
	}

	/**
	 * @return listaActivityNames: List name of the activity.
	 */
	public List<ActivityNames> getListaActivityNames() {
		return listaActivityNames;
	}

	/**
	 * @param listaActivityNames
	 *            :List name of the activity.
	 */
	public void setListaActivityNames(List<ActivityNames> listaActivityNames) {
		this.listaActivityNames = listaActivityNames;
	}

	/**
	 * @return listaCycleStandardActivities: List of standard cycle activities.
	 */
	public List<CycleStandardActivities> getListaCycleStandardActivities() {
		return listaCycleStandardActivities;
	}

	/**
	 * @param listaCycleStandardActivities
	 *            : List of standard cycle activities.
	 */
	public void setListaCycleStandardActivities(
			List<CycleStandardActivities> listaCycleStandardActivities) {
		this.listaCycleStandardActivities = listaCycleStandardActivities;
	}

	/**
	 * @return opcionesCropNames: crop name associated with an activity.
	 */
	public List<SelectItem> getOpcionesCropNames() {
		return opcionesCropNames;
	}

	/**
	 * @param opcionesCropNames
	 *            :crop name associated with an activity.
	 */
	public void setOpcionesCropNames(List<SelectItem> opcionesCropNames) {
		this.opcionesCropNames = opcionesCropNames;
	}

	/**
	 * @return opcionesCrops: cultivation associated with an activity.
	 */
	public List<SelectItem> getOpcionesCrops() {
		return opcionesCrops;
	}

	/**
	 * @param opcionesCrops
	 *            :cultivation associated with an activity.
	 */
	public void setOpcionesCrops(List<SelectItem> opcionesCrops) {
		this.opcionesCrops = opcionesCrops;
	}

	/**
	 * @return subListActivities: list of activities that stores a sublist for
	 *         managing Pager
	 */
	public List<Activities> getSubListActivities() {
		return subListActivities;
	}

	/**
	 * @param subListActivities
	 *            : list of activities that stores a sublist for managing Pager
	 */
	public void setSubListActivities(List<Activities> subListActivities) {
		this.subListActivities = subListActivities;
	}

	/**
	 * @return subListCycleStandardActivities: list of cycle standard activities
	 *         that stores a sublist for managing Pager.
	 */
	public List<CycleStandardActivities> getSubListCycleStandardActivities() {
		return subListCycleStandardActivities;
	}

	/**
	 * @param subListCycleStandardActivities
	 *            : list of cycle standard activities that stores a sublist for
	 *            managing Pager.
	 */
	public void setSubListCycleStandardActivities(
			List<CycleStandardActivities> subListCycleStandardActivities) {
		this.subListCycleStandardActivities = subListCycleStandardActivities;
	}

	/**
	 * @return paginador: Management paged list of activity names.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list of activity names.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paginadorActivities: Management paged list of activities
	 */
	public Paginador getPaginadorActivities() {
		return paginadorActivities;
	}

	/**
	 * @param paginadorActivities
	 *            : Management paged list of activities
	 */
	public void setPaginadorActivities(Paginador paginadorActivities) {
		this.paginadorActivities = paginadorActivities;
	}

	/**
	 * @return paginadorActivitiesStandardCycle: Management paged list of
	 *         activities standard cycle
	 */
	public Paginador getPaginadorActivitiesStandardCycle() {
		return paginadorActivitiesStandardCycle;
	}

	/**
	 * @param paginadorActivitiesStandardCycle
	 *            : Management paged list of activities standard cycle
	 */
	public void setPaginadorActivitiesStandardCycle(
			Paginador paginadorActivitiesStandardCycle) {
		this.paginadorActivitiesStandardCycle = paginadorActivitiesStandardCycle;
	}

	/**
	 * @return paginadorListActivities: Management paged list of activities
	 * 
	 */
	public Paginador getPaginadorListActivities() {
		return paginadorListActivities;
	}

	/**
	 * @param paginadorListActivities
	 *            :Management paged list of activities
	 */
	public void setPaginadorListActivities(Paginador paginadorListActivities) {
		this.paginadorListActivities = paginadorListActivities;
	}

	/**
	 * @return activities: Object of class activities.
	 */
	public Activities getActivities() {
		return activities;
	}

	/**
	 * @param activities
	 *            : Object of class activities.
	 */
	public void setActivities(Activities activities) {
		this.activities = activities;
	}

	/**
	 * @return eliminarLista: Object selected from the list to eliminate
	 *         activities.
	 */
	public Activities getEliminarLista() {
		return eliminarLista;
	}

	/**
	 * @param eliminarLista
	 *            :Object selected from the list to eliminate activities.
	 */
	public void setEliminarLista(Activities eliminarLista) {
		this.eliminarLista = eliminarLista;
	}

	/**
	 * @return activityNames: Object of the class name activities.
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Object of the class name activities.
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return crops: Object of class culture.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            :Object of class culture.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return cropNames: Object of the class name of the crop.
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            :Object of the class name of the crop.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return cycleStandardActivities: Object class standard cycle activities.
	 */
	public CycleStandardActivities getCycleStandardActivities() {
		return cycleStandardActivities;
	}

	/**
	 * @param cycleStandardActivities
	 *            :Object class standard cycle activities.
	 */
	public void setCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) {
		this.cycleStandardActivities = cycleStandardActivities;
	}

	/**
	 * @return eliminarStandard: Standard Cycle Activities object selected to
	 *         remove from the list.
	 */
	public CycleStandardActivities getEliminarStandard() {
		return eliminarStandard;
	}

	/**
	 * @param eliminarStandard
	 *            :Standard Cycle Activities object selected to remove from the
	 *            list.
	 */
	public void setEliminarStandard(CycleStandardActivities eliminarStandard) {
		this.eliminarStandard = eliminarStandard;
	}

	/**
	 * @return nombreBuscar: Gets the search parameter in the system.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            :Sets the search parameter in the system
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return mensajeMiga: message crumb of bread in the record template
	 */
	public String getMensajeMiga() {
		return mensajeMiga;
	}

	/**
	 * @param mensajeMiga
	 *            :message crumb of bread in the record template
	 */
	public void setMensajeMiga(String mensajeMiga) {
		this.mensajeMiga = mensajeMiga;
	}

	/**
	 * @return maxDate: max date to validate date range of activity
	 */
	public Date getMaxDate() {
		return maxDate;
	}

	/**
	 * @param maxDate
	 *            :max date to validate date range of activity
	 */
	public void setMaxDate(Date maxDate) {
		this.maxDate = maxDate;
	}

	/**
	 * @return initialDateSearch: gets the initial date of the activity to
	 *         search in a range
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            :sets the initial date of the activity to search in a range
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the final date of the activity to search in
	 *         a range
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            :sets the final date of the activity to search in a range
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return idCropNamesBuscar: id crop name by which you want to see the
	 *         culture.
	 */
	public int getIdCropNamesBuscar() {
		return idCropNamesBuscar;
	}

	/**
	 * @param idCropNamesBuscar
	 *            : id crop name by which you want to see the culture.
	 */
	public void setIdCropNamesBuscar(int idCropNamesBuscar) {
		this.idCropNamesBuscar = idCropNamesBuscar;
	}

	/**
	 * @return idCropName: Name identifier harvest.
	 */
	public int getIdCropName() {
		return idCropName;
	}

	/**
	 * @param idCropName
	 *            :Name identifier harvest.
	 */
	public void setIdCropName(int idCropName) {
		this.idCropName = idCropName;
	}

	/**
	 * 
	 * @return limpiar: Cleans the standard list activities if its value is
	 *         'true'.
	 */
	public boolean isLimpiar() {
		return limpiar;
	}

	/**
	 * @param limpiar
	 *            :Cleans the standard list activities if its value is 'true'.
	 */
	public void setLimpiar(boolean limpiar) {
		this.limpiar = limpiar;
	}

	/**
	 * Method to edit or create a new assignment of activities
	 * 
	 * @param activities
	 *            :Object of activities are adding or editing
	 * 
	 * @return regCropActivities: Template redirects to assign farming
	 *         activities
	 * 
	 */
	public String agregarEditarCropActivities(Activities activities) {
		try {
			limpiarActivities();
			if (activities == null) {
				this.activities = new Activities();
				crops = new Crops();
				crops.setCropNames(new CropNames());
				cropNames = new CropNames();
				this.activities.setActivityName(new ActivityNames());
				mensajeMiga = "mensajeInformacionBase.municipio_label_registrar";
			} else {
				this.activities = activities;
				mensajeMiga = "mensajeInformacionBase.municipio_label_modificar";
			}
			crops = cropsDao.descriptionSearch(Constantes.COSECHA);
			if (crops != null) {
				initializeActivities();
			}
			llenarCropNames();
			setLimpiar(false);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regCropActivities";
	}

	/**
	 * CropNames method that loads a list
	 * 
	 * @throws Exception
	 */
	private void llenarCropNames() throws Exception {
		opcionesCropNames = new ArrayList<SelectItem>();
		List<CropNames> listCropNames = cropNamesDao.listaCropNames();
		if (listCropNames != null) {
			for (CropNames cropNames : listCropNames) {
				opcionesCropNames.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}
		llenarCropNamesCrop();
	}

	/**
	 * Method allows complete the list of crops harvested after the name
	 * selected.
	 * 
	 */
	public void llenarCropNamesCrop() {
		try {
			int idCropsName = 0;
			opcionesCrops = new ArrayList<SelectItem>();
			if (this.crops != null && this.crops.getCropNames() != null) {
				idCropsName = this.crops.getCropNames().getIdCropName();
			} else {
				idCropsName = idCropNamesBuscar;
			}
			List<Crops> listaCropsVigentes;
			listaCropsVigentes = cropsDao
					.consultarCropNamesCropsVigentes(idCropsName);
			if (listaCropsVigentes != null) {
				for (Crops crops : listaCropsVigentes) {
					opcionesCrops.add(new SelectItem(crops.getIdCrop(), crops
							.getDescription()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allows you to save or edit an activity assigned to a crop.
	 * 
	 * @return agregarEditarActivities: Method to edit or create a new
	 *         assignment of activities
	 * @throws Exception
	 */
	public String guardarCropActivities() throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			String outTxtEdit = "";
			String outTxtSave = "";
			String outStandardSave = "";
			for (Activities activities : this.listaActivities) {
				if (activities.getIdActivity() != 0) {
					outTxtEdit += activities.getActivityName()
							.getActivityName() + ", ";
					activitiesDao.editarActivities(activities);
				} else {
					outTxtSave += activities.getActivityName()
							.getActivityName() + ", ";
					activitiesDao.guardarActivities(activities);
				}
			}
			if (this.listaCycleStandardActivities.size() != 0) {
				for (CycleStandardActivities cycleStandardActivities : this.listaCycleStandardActivities) {
					Activities activities = new Activities();
					activities.setCrop(crops);
					activities.setActivityName(cycleStandardActivities
							.getActivityNames());
					activities.setHrRequired(cycleStandardActivities
							.getHrRequired());
					activities.setMachineRequired(cycleStandardActivities
							.getMachineRequired());
					activities.setServiceRequired(cycleStandardActivities
							.getServiceRequired());
					activities.setMaterialsRequired(cycleStandardActivities
							.getMaterialsRequired());
					activities.setSequenceNumber(cycleStandardActivities
							.getSequenceNumber());
					activities.setDurationBudget(cycleStandardActivities
							.getDuration());
					activities.setCostHrBudget(cycleStandardActivities
							.getCostHrHa());
					activities.setCostMachinesEqBudget(cycleStandardActivities
							.getCostMachinesEqHa());
					activities.setCostServicesBudget(cycleStandardActivities
							.getCostServicesHa());
					activities.setCostMaterialsBudget(cycleStandardActivities
							.getCostMaterialsHa());
					activities.setGeneralCostBudget(cycleStandardActivities
							.getGeneralCostHa());
					activities.setDangerous(cycleStandardActivities
							.getDangerous());
					activitiesDao.guardarActivities(activities);
					outStandardSave += cycleStandardActivities
							.getActivityNames().getActivityName() + ", ";
				}
			}
			if (outTxtEdit.length() > 0) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_modificar"),
								outTxtEdit));
			}
			if (outTxtSave.length() > 0) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_guardar"),
								outTxtSave));
			}
			if (outStandardSave.length() > 0) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_guardar"),
								outStandardSave));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return agregarEditarActivities();
	}

	/**
	 * Method to edit or create a new assignment of activities
	 * 
	 * @return regCropActivities: Template redirects to assign farming
	 *         activities
	 * @throws Exception
	 * 
	 */
	public String agregarEditarActivities() throws Exception {
		limpiarActivities();
		llenarCropNames();
		initializeActivities();
		return "regCropActivities";
	}

	/**
	 * Consult the list of activities crops
	 * 
	 */
	public void consultarActivityNamesXCrops() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaActivityNames = new ArrayList<ActivityNames>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";

		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = activityNamesDao.cantidadActivityNamesCrops(
					consulta, parametros);
			if (cantidad != null) {
				paginador.paginarRangoDefinido(cantidad, 5);
			}
			listaActivityNames = activityNamesDao
					.consultarActivityNamesXIdCrop(paginador.getInicio(),
							paginador.getRango(), consulta, parametros);
			for (ActivityNames activityNames : this.listaActivityNames) {
				if (activityNames.isSeleccionado()) {
					listaActivityNames.remove(activityNames);
					break;
				}

			}
			if ((listaActivityNames == null || listaActivityNames.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaActivityNames == null
					|| listaActivityNames.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
			if (!"".equals(nombreBuscar)) {
				ControladorContexto.mensajeInformacion(
						"popupForm:tActivityNames", mensajeBusqueda);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user
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
			consult.append("WHERE UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
	}

	/**
	 * Method passing objects of type Names activity to a list of type
	 * Activities
	 * 
	 * @modify 14/01/2016 Wilhelm.Boada
	 * 
	 * @param listActivityNames
	 *            : Name list of activities
	 * @throws Exception
	 */
	public void generarListaActivities(List<ActivityNames> listActivityNames)
			throws Exception {
		String param2 = ControladorContexto.getParam("param2");
		boolean desdeModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		setLimpiar(desdeModal);
		this.activities = new Activities();
		this.activities.setActivityName(this.activityNames);
		this.activities.setCrop(getCrops());
		if (this.listaActivities == null) {
			this.listaActivities = new ArrayList<Activities>();
		}
		this.listaActivities.add(this.activities);
		this.paginador = new Paginador();
		initializeList();
	}

	/**
	 * Method to remove from the list the names of the activities selected by
	 * the user in the eye.
	 * 
	 */
	public void eliminarSeleccionados() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			for (CycleStandardActivities cycleStandardActivities : this.listaCycleStandardActivities) {
				if (cycleStandardActivities.equals(eliminarStandard)) {
					listaCycleStandardActivities.remove(eliminarStandard);
					break;
				}
			}
			for (Activities activities : this.listaActivities) {
				if (activities.equals(eliminarLista)) {
					activitiesDao.eliminarActivities(eliminarLista);
					listaActivities.remove(eliminarLista);
					break;
				}
			}
			if (eliminarLista != null) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_eliminar"),
								eliminarLista.getActivityName()
										.getActivityName()));
				initializeList();
			}
			if (eliminarStandard != null) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_eliminar"),
								eliminarStandard.getActivityNames()
										.getActivityName()));
				initializeListStandardCycle();
			}
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					activities.getActivityName().getActivityName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method to clean up the list of activities
	 */
	private void limpiarActivities() {
		this.listaActivities = new ArrayList<Activities>();
		this.listaCycleStandardActivities = new ArrayList<CycleStandardActivities>();
	}

	/**
	 * This method allows initialize all the activities
	 */
	public void initializeActivities() {
		this.nombreBuscar = "";
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		consultarActivities();
	}

	/**
	 * See the list of activities depending on the crop.
	 */
	public void consultarActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			int idCrops = this.crops.getIdCrop();
			advanceSearchActivities(consult, parameters, bundle,
					unionMessagesSearch);
			Long amount = activitiesDao.amountActivitiesCrop(consult,
					parameters, idCrops);
			if (amount != null) {
				paginadorListActivities.paginar(amount);
			}
			this.listaActivities = activitiesDao.consultarActivitiesXCrop(
					paginadorListActivities.getInicio(),
					paginadorListActivities.getRango(), consult, parameters,
					idCrops);
			if (!limpiar) {
				this.listaCycleStandardActivities = new ArrayList<CycleStandardActivities>();
			}
			setLimpiar(false);
			if ((listaActivities == null || listaActivities.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existe_actividades_relacionadas"),
								unionMessagesSearch);
				this.listaActivities = new ArrayList<Activities>();
			} else if (listaActivities == null || listaActivities.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle
										.getString("activity_names_label_s"),
								unionMessagesSearch);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user
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
	private void advanceSearchActivities(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);

		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consult.append("AND UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nombreBuscar + '"');
		}
		if (this.initialDateSearch != null && this.finalDateSearch != null) {
			consult.append("AND a.initialDtBudget BETWEEN :initialDateSearch AND :finalDateSearch ");
			SelectItem item = new SelectItem(initialDateSearch,
					"initialDateSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(finalDateSearch,
					"finalDateSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + formato.format(this.initialDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + formato.format(finalDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateTo);
		}

	}

	/**
	 * Consult the list of existing standard cycle activities
	 * 
	 */
	public void consultarStandardActivities() {
		try {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			this.listaCycleStandardActivities = cycleStandardActivitiesDao
					.consultarCycleStandardActivities(idCropName);
			if (listaCycleStandardActivities == null
					|| listaCycleStandardActivities.size() <= 0) {
				ControladorContexto
						.mensajeInformacion(
								null,
								bundle.getString("message_no_existe_actividades_standard_relacionadas"));
			}
			initializeListStandardCycle();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Valid dates view, when the start date is entered you must enter the end
	 * date.
	 * 
	 * @author Mabell.Boada
	 * 
	 */
	public void validarFechas() {
		if (activities.getInitialDtBudget() != null
				&& activities.getFinalDtBudget() == null) {
			ControladorContexto.mensajeRequeridos("popupFormReg:fechaFin");
		}
		if (activities.getFinalDtBudget() != null
				&& activities.getInitialDtBudget() == null) {
			ControladorContexto.mensajeRequeridos("popupFormReg:fechaInicio");
		}
	}

	/**
	 * Validate the date to add the max hour in the end date
	 * 
	 */
	public void validateMaxDate() {
		if (this.activities.getInitialDtBudget() != null) {
			this.maxDate = new Date();
			maxDate = activities.getInitialDtBudget();
			Calendar cal = Calendar.getInstance();
			cal.setTime(maxDate);
			cal.set(Calendar.HOUR_OF_DAY, 23);
			cal.set(Calendar.MINUTE, 59);
			maxDate = cal.getTime();
		}
	}

	/**
	 * This method validates the entered duration time does not exceed the total
	 * time of the activity and workload guidelines are met
	 * 
	 * @param context
	 *            : Context of view.
	 * @param toValidate
	 *            : Validate component.
	 * @param value
	 *            : Component value.
	 * @throws Exception
	 */
	public void validateDate(FacesContext context, UIComponent toValidate,
			Object value) throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String clientId = toValidate.getClientId(context);

		Double duration = (Double) value;
		Double durationActivity;
		Date initialDate = this.activities.getInitialDtBudget();
		Date finalDate = this.activities.getFinalDtBudget();
		try {
			if (duration != null) {
				if (duration > 0) {
					durationActivity = (Double) ControladorFechas.restarFechas(
							initialDate, finalDate);
					if (duration.compareTo(durationActivity) > 0) {
						String mensaje = "message_duracion_actividad";
						context.addMessage(clientId,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										bundle.getString(mensaje), null));
						((UIInput) toValidate).setValid(false);
					}
				} else {
					String mensaje = "message_duration_mayor_cero";
					context.addMessage(clientId,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									bundle.getString(mensaje), null));
					((UIInput) toValidate).setValid(false);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Pager manages the activities list
	 * 
	 * @author Gerardo.Herrera
	 */
	public void initializeList() {
		Long cantidadPaginador = (long) listaActivities.size();
		try {
			this.paginadorActivities.paginarRangoDefinido(cantidadPaginador, 5);
			int inicial = paginadorActivities.getItemInicial() - 1;
			int fin = paginadorActivities.getItemFinal();
			this.subListActivities = listaActivities.subList(inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Pager manages the standard cycle activities list
	 * 
	 * @author Gerardo.Herrera
	 */
	public void initializeListStandardCycle() {
		Long cantidadPaginador = (long) listaCycleStandardActivities.size();
		try {
			this.paginadorActivitiesStandardCycle.paginarRangoDefinido(
					cantidadPaginador, 5);
			int inicial = paginadorActivitiesStandardCycle.getItemInicial() - 1;
			int fin = paginadorActivitiesStandardCycle.getItemFinal();
			this.subListCycleStandardActivities = listaCycleStandardActivities
					.subList(inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
