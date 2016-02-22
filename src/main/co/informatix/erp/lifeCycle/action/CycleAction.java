package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.CycleDao;
import co.informatix.erp.lifeCycle.entities.ActivityNames;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Cycle;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the cycles that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class CycleAction implements Serializable {

	private List<Cycle> listaCycles;
	private List<SelectItem> opcionesCropNames;
	private List<SelectItem> opcionesCrops;

	private Paginador paginador = new Paginador();

	private Cycle cycle;
	private Crops crops;
	private CropNames cropNames;

	private String nameSearch;
	private String messageCrumb;

	private Date initialDateSearch;
	private Date finalDateSearch;

	private int idCropNamesBuscar;
	private int idCropName;

	private boolean clean;

	@EJB
	private CycleDao cycleDao;
	@EJB
	private CropsDao cropsDao;
	@EJB
	private CropNamesDao cropNamesDao;

	/**
	 * @return listaCycles: List of cycles.
	 */
	public List<Cycle> getListaCycles() {
		return listaCycles;
	}

	/**
	 * @param listaCycles
	 *            : List of cycles.
	 */
	public void setListaCycles(List<Cycle> listaCycles) {
		this.listaCycles = listaCycles;
	}

	/**
	 * @return opcionesCropNames: crop name associated with an cycle.
	 */
	public List<SelectItem> getOpcionesCropNames() {
		return opcionesCropNames;
	}

	/**
	 * @param opcionesCropNames
	 *            :crop name associated with an cycle.
	 */
	public void setOpcionesCropNames(List<SelectItem> opcionesCropNames) {
		this.opcionesCropNames = opcionesCropNames;
	}

	/**
	 * @return opcionesCrops: crops associated with an cycle.
	 */
	public List<SelectItem> getOpcionesCrops() {
		return opcionesCrops;
	}

	/**
	 * @param opcionesCrops
	 *            :crops associated with an cycle.
	 */
	public void setOpcionesCrops(List<SelectItem> opcionesCrops) {
		this.opcionesCrops = opcionesCrops;
	}

	/**
	 * @return paginador: Management paged list of cycles.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list of cycles.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return cycle: Object of class cycle.
	 */
	public Cycle getCycle() {
		return cycle;
	}

	/**
	 * @param cycle
	 *            : Object of class cycle.
	 */
	public void setCycle(Cycle cycle) {
		this.cycle = cycle;
	}

	/**
	 * @return crops: Object of class crops.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            :Object of class crops.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return cropNames: Object of the class name crop.
	 */
	public CropNames getCropNames() {
		return cropNames;
	}

	/**
	 * @param cropNames
	 *            :Object of the class name crop.
	 */
	public void setCropNames(CropNames cropNames) {
		this.cropNames = cropNames;
	}

	/**
	 * @return nombreBuscar: Gets the search parameter in the system.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nombreBuscar
	 *            :Sets the search parameter in the system.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return messageCrumb: message crumb of bread in the record template.
	 */
	public String getMessageCrumb() {
		return messageCrumb;
	}

	/**
	 * @param messageCrumb
	 *            :message crumb of bread in the record template.
	 */
	public void setMessageCrumb(String messageCrumb) {
		this.messageCrumb = messageCrumb;
	}

	/**
	 * @return initialDateSearch: gets the initial date of the cycle to search
	 *         in a range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            :sets the initial date of the cycle to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the final date of the cycle to search in a
	 *         range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            :sets the final date of the cycle to search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return idCropNamesBuscar: id crop name by which you want to see the
	 *         crops.
	 */
	public int getIdCropNamesBuscar() {
		return idCropNamesBuscar;
	}

	/**
	 * @param idCropNamesBuscar
	 *            : id crop name by which you want to see the crops.
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
	 * @return clean: Cleans the standard list cycles if its value is 'true'.
	 */
	public boolean isClean() {
		return clean;
	}

	/**
	 * @param clean
	 *            :Cleans the standard list cycles if its value is 'true'.
	 */
	public void setClean(boolean clean) {
		this.clean = clean;
	}

	/**
	 * Method to edit or create a new assignment of cycle.
	 * 
	 * @param cycle
	 *            :Object of cycle are adding or editing.
	 * 
	 * @return gesCycle: Template redirects to management Cycle.
	 * 
	 */
	public String agregarEditarCycle(Cycle cycle) {
		try {
			if ((cycle != null)) {
				this.cycle = cycle;
				messageCrumb = "mensajeInformacionBase.municipio_label_modificar";
			} else {
				this.cycle = new Cycle();
				crops = new Crops();
				crops.setCropNames(new CropNames());
				cropNames = new CropNames();
				this.cycle.setActiviyNames(new ActivityNames());
				messageCrumb = "mensajeInformacionBase.municipio_label_registrar";
			}
			crops = cropsDao.descriptionSearch(Constantes.COSECHA);
			if (crops != null) {
				initializeCycles();
			}
			loadCropNames();
			setClean(false);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesCycle";
	}

	/**
	 * This method allows load of name crops list.
	 * 
	 */
	private void loadCropNames() {
		opcionesCropNames = new ArrayList<SelectItem>();
		List<CropNames> listCropNames;
		try {
			listCropNames = cropNamesDao.listaCropNames();
			if (listCropNames != null) {
				for (CropNames cropNames : listCropNames) {
					opcionesCropNames.add(new SelectItem(cropNames
							.getIdCropName(), cropNames.getCropName()));
				}
			}
			loadCropNamesCrop();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * Method allows complete the list of crops harvested after the name
	 * selected.
	 * 
	 */
	public void loadCropNamesCrop() {
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
	 * This method allows initialize all the cycle.
	 */
	public void initializeCycles() {
		this.nameSearch = "";
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		this.cycle = new Cycle();
		consultarCycles();
	}

	/**
	 * See the list of cycles depending on the crop.
	 */
	public void consultarCycles() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeLifeCycle");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaCycles = new ArrayList<Cycle>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			int idCrops = this.crops.getIdCrop();
			advanceSearchCycles(consult, parameters, bundle,
					unionMessagesSearch);
			Long amount = cycleDao
					.amountCycleCrop(consult, parameters, idCrops);
			if (amount != null) {
				paginador.paginar(amount);
			}
			if (amount != null && amount > 0) {
				listaCycles = cycleDao.consultCycleByCrop(
						paginador.getInicio(), paginador.getRango(), consult,
						parameters, idCrops);
			}
			if ((listaCycles == null || listaCycles.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listaCycles == null || listaCycles.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("cycle_label_s"),
								unionMessagesSearch);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method build the query to the advanced search also allows messages
	 * to display build depending on the search criteria selected by the user.
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
	private void advanceSearchCycles(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);

		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(an.activityName) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
		if (this.initialDateSearch != null && this.finalDateSearch != null) {
			consult.append("AND c.initialDateTime BETWEEN :initialDateSearch AND :finalDateSearch ");
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
	 * Method to remove a cycles of the database.
	 * 
	 * @return initializeCycles(): Consult the list of cycles and returns to
	 *         manage view.
	 */
	public void deleteCycle() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			cycleDao.deleteCycle(cycle);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), cycle
							.getActiviyNames().getActivityName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(bundle
					.getString("message_existe_relacion_eliminar"), cycle
					.getActiviyNames().getActivityName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		initializeCycles();
	}
}