package co.informatix.erp.costs.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.CycleStandardActivitiesDao;
import co.informatix.erp.costs.entities.CycleStandardActivities;
import co.informatix.erp.lifeCycle.dao.ActivityNamesDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all related to the creation and logic updating of the standard
 * cycle activities in the system
 * 
 * @author Mabell.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CycleStandardActivitiesAction implements Serializable {
	private List<CycleStandardActivities> listaCycleStandardActivities;
	private List<ActivityNames> listaActivityNames;
	private List<SelectItem> itemsCropName;
	private List<CycleStandardActivities> listaCycleStandard = new ArrayList<CycleStandardActivities>();

	private CycleStandardActivities cycleStandardActivities;
	private CycleStandardActivities eliminarLista;
	private CropNames cropNames;
	private ActivityNames activityNames;

	private int idCropNames;
	private String nombreBuscar;

	private Paginador paginador = new Paginador();
	private Paginador paginadorCycleActivities = new Paginador();

	@EJB
	private CropNamesDao cropNamesDao;

	@EJB
	private CycleStandardActivitiesDao cycleStandardActivitiesDao;

	@EJB
	private ActivityNamesDao activityNamesDao;

	/**
	 * @return listaCycleStandardActivities: List of standard cycle activities
	 */
	public List<CycleStandardActivities> getListaCycleStandardActivities() {
		return listaCycleStandardActivities;
	}

	/**
	 * @param listaCycleStandardActivities
	 *            : List of standard cycle activities
	 * 
	 */
	public void setListaCycleStandardActivities(
			List<CycleStandardActivities> listaCycleStandardActivities) {
		this.listaCycleStandardActivities = listaCycleStandardActivities;
	}

	/**
	 * @return listaActivityNames: List name of the activity
	 */
	public List<ActivityNames> getListaActivityNames() {
		return listaActivityNames;
	}

	/**
	 * @param listaActivityNames
	 *            : List name of the activity
	 * 
	 */
	public void setListaActivityNames(List<ActivityNames> listaActivityNames) {
		this.listaActivityNames = listaActivityNames;
	}

	/**
	 * @return itemsCropName: List crops associated activities standard cycle
	 */
	public List<SelectItem> getItemsCropName() {
		return itemsCropName;
	}

	/**
	 * @param itemsCropName
	 *            : List crops associated activities standard cycle
	 */
	public void setItemsCropName(List<SelectItem> itemsCropName) {
		this.itemsCropName = itemsCropName;
	}

	/**
	 * @return cycleStandardActivities: Object of standard class activities
	 *         cycle
	 */
	public CycleStandardActivities getCycleStandardActivities() {
		return cycleStandardActivities;
	}

	/**
	 * @param cycleStandardActivities
	 *            : Object of standard class activities cycle
	 * 
	 */
	public void setCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) {
		this.cycleStandardActivities = cycleStandardActivities;

	}

	/**
	 * @return eliminarLista: Object selected cycleStandardActivities to remove
	 *         from the list
	 */
	public CycleStandardActivities getEliminarLista() {
		return eliminarLista;
	}

	/**
	 * @param eliminarLista
	 *            : Object selected cycleStandardActivities to remove from the
	 *            list
	 * 
	 */
	public void setEliminarLista(CycleStandardActivities eliminarLista) {
		this.eliminarLista = eliminarLista;
	}

	/**
	 * @return cropNames: Object of class name crops
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            : Object of class name crops
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return activityNames: Object of name kind of activities
	 */
	public ActivityNames getActivityNames() {
		return activityNames;
	}

	/**
	 * @param activityNames
	 *            : Object of name kind of activities
	 */
	public void setActivityNames(ActivityNames activityNames) {
		this.activityNames = activityNames;
	}

	/**
	 * @return idCropNames: identifier crop Name
	 */
	public int getIdCropNames() {
		return idCropNames;
	}

	/**
	 * @param idCropNames
	 *            : identifier crop Name
	 * 
	 */
	public void setIdCropNames(int idCropNames) {
		this.idCropNames = idCropNames;
	}

	/**
	 * @return nombreBuscar: Name by which you want to query the name activity
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name by which you want to query the name activity
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * @return paginador: Paginated list of the names of the activities they may
	 *         have in the popup
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paginated list of the names of the activities they may have
	 *            in the popup
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paginadorCycleActivities: Management paged list of cycle
	 *         activities
	 */
	public Paginador getPaginadorCycleActivities() {
		return paginadorCycleActivities;
	}

	/**
	 * @param paginadorCycleActivities
	 *            : Management paged list of cycle activities
	 */
	public void setPaginadorCycleActivities(Paginador paginadorCycleActivities) {
		this.paginadorCycleActivities = paginadorCycleActivities;
	}

	/**
	 * @return listaCycleStandard: list of standard cycles that stores a sublist
	 *         for managing Pager
	 */
	public List<CycleStandardActivities> getListaCycleStandard() {
		return listaCycleStandard;
	}

	/**
	 * @param listaCycleStandard
	 *            : list of standard cycles that stores a sublist for managing
	 *            Pager
	 */
	public void setListaCycleStandard(
			List<CycleStandardActivities> listaCycleStandard) {
		this.listaCycleStandard = listaCycleStandard;
	}

	/**
	 * Method to create a new standard cycle activity
	 * 
	 * @param cycleStandardActivities
	 *            : Object standard cycle activities to be added
	 * 
	 * @return regCyclStandAct: Redirected to the template record Standard cycle
	 *         activities
	 * 
	 */
	public String agregarEditarCycleStandardActivities(
			CycleStandardActivities cycleStandardActivities) {
		itemsCropName = new ArrayList<SelectItem>();
		try {
			limpiarCycleStandardActivities();
			cargarCropNames();
			if (cycleStandardActivities != null) {
				this.cycleStandardActivities = cycleStandardActivities;
			} else {
				this.cycleStandardActivities = new CycleStandardActivities();
				cropNames = new CropNames();
				this.cycleStandardActivities
						.setActivityNames(new ActivityNames());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return "regCyclStandAct";
	}

	/**
	 * That loads method crops a list
	 * 
	 * @throws Exception
	 */
	private void cargarCropNames() throws Exception {
		itemsCropName = new ArrayList<SelectItem>();
		List<CropNames> listaCropName = cropNamesDao.listCropNames();
		if (listaCropName != null) {
			for (CropNames cropNames : listaCropName) {
				itemsCropName.add(new SelectItem(cropNames.getIdCropName(),
						cropNames.getCropName()));
			}
		}

	}

	/**
	 * Method used to save the cycle standard activities.
	 * 
	 * @return agregarEditarStandardActivities: Method That loads information
	 *         necessary and redirected to the template record Standard cycle
	 *         activities.
	 * 
	 * @throws Exception
	 */
	public String guardarCycleStandardActivities() throws Exception {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_guardar_actividades";
		boolean existe;
		try {
			Object nameCropNam = ValidacionesAction.getLabel(
					this.itemsCropName, this.cropNames.getIdCropName());
			for (CycleStandardActivities cycleStandar : this.listaCycleStandardActivities) {
				int idCropName = cycleStandar.getCropNames().getIdCropName();
				int idActivityName = cycleStandar.getActivityNames()
						.getIdActivityName();
				existe = cycleStandardActivitiesDao
						.relacionCropNamesActivityNames(idCropName,
								idActivityName);
				if (existe) {
					cycleStandardActivitiesDao
							.editarCycleStandardActivities(cycleStandar);
				} else {
					cycleStandardActivitiesDao
							.guardarCycleStandardActivities(cycleStandar);
				}
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), nameCropNam));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return agregarEditarStandardActivities();
	}

	/**
	 * Method to edit or create a new assignment of standard cycle activities
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return regCyclStandAct: Redirected to the template record Standard cycle
	 *         activities
	 * @throws Exception
	 * 
	 */
	public String agregarEditarStandardActivities() throws Exception {
		this.listaCycleStandardActivities = new ArrayList<CycleStandardActivities>();
		cargarCropNames();
		consultarCycleStandardActivities();
		return "regCyclStandAct";
	}

	/**
	 * Pager manages the cycle standard activities list
	 */
	public void initializeList() {
		Long cantidadPaginador = (long) listaCycleStandardActivities.size();
		try {
			this.paginadorCycleActivities.paginarRangoDefinido(
					cantidadPaginador, 10);
			int inicial = paginadorCycleActivities.getItemInicial() - 1;
			int fin = paginadorCycleActivities.getItemFinal();
			this.listaCycleStandard = listaCycleStandardActivities.subList(
					inicial, fin);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method passing objects list type activityNames type to a
	 * CycleStandardActivity
	 * 
	 * @modify 15/07/2015 Andres.Gomez
	 * 
	 * @param listaActivityNames
	 *            : Name list of activities
	 */
	public void generarListaCycleStandardActivities(
			List<ActivityNames> listaActivityNames) {
		for (ActivityNames activityNames : listaActivityNames) {
			if (activityNames.isSeleccionado()) {
				CycleStandardActivities cycleStandardActivities = new CycleStandardActivities();
				cycleStandardActivities.setActivityNames(activityNames);
				listaCycleStandardActivities.add(cycleStandardActivities);
				cycleStandardActivities.setCropNames(getCropNames());
			}
		}
		this.paginador = new Paginador();
		initializeList();
	}

	/**
	 * Method to remove from the list the names of the activities user selected
	 * in view RegistrarCycleStandardActivities
	 * 
	 */
	public void eliminarSeleccionados() {
		for (CycleStandardActivities cycleStandardActivities : this.listaCycleStandardActivities) {
			if (cycleStandardActivities.equals(eliminarLista)) {
				try {
					cycleStandardActivitiesDao
							.deleteCycleStandardActivities(eliminarLista);
					listaCycleStandardActivities.remove(eliminarLista);
					break;
				} catch (Exception e) {
					ControladorContexto.mensajeError(e);
				}

			}
		}
		initializeList();
	}

	/**
	 * Method that allows the list of standard cleaning cycle activities and
	 * Crop name
	 */
	private void limpiarCycleStandardActivities() {
		this.listaCycleStandardActivities = new ArrayList<CycleStandardActivities>();
		this.cropNames = new CropNames();
	}

	/**
	 * Consult the list of existing standard cycle activities
	 * 
	 */
	public void consultarCycleStandardActivities() {
		try {
			this.listaCycleStandardActivities = cycleStandardActivitiesDao
					.consultarCycleStandardActivities(this.cropNames
							.getIdCropName());
			this.paginadorCycleActivities = new Paginador();
			initializeList();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * 
	 Consult the list of the names of the activities to show the popup
	 * 
	 * @modify 29/09/2015 Andres.Gomez
	 * 
	 * @return regCyclStandAct: Redirects to the template to record Standard
	 *         cycle activities
	 */
	public String consultarActivityNames() {
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
			Long cantidad = activityNamesDao.amountActivityNames(consulta,
					parametros);
			if (cantidad != null) {
				paginador.paginarRangoDefinido(cantidad, 5);
			}
			listaActivityNames = activityNamesDao
					.queryActivityNamesXIdCropNames(paginador.getInicio(),
							paginador.getRango(), consulta, parametros);
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
		return "regCyclStandAct";
	}

	/**
	 * This method constructs the query to the advanced search also allows build
	 * messages to display depending on the search criteria selected by the user
	 * 
	 * @Mmodify 29/09/2015 Andres.Gomez
	 * 
	 * @param consulta
	 *            : Consult concatenate
	 * @param parametros
	 *            : List of search parameters
	 * @param bundle
	 *            : Access language tags
	 * 
	 * @param unionMensajesBusqueda
	 *            : Message search
	 */
	private void busquedaAvanzada(StringBuilder consulta,
			List<SelectItem> parametros, ResourceBundle bundle,
			StringBuilder unionMensajesBusqueda) {
		if (this.nombreBuscar != null && !"".equals(this.nombreBuscar)) {
			consulta.append("WHERE UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nombreBuscar + "%",
					"keyword");
			parametros.add(item);
			unionMensajesBusqueda.append(bundle.getString("label_nombre")
					+ ": " + '"' + this.nombreBuscar + '"');
		}
	}

}
