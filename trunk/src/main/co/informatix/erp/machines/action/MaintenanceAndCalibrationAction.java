package co.informatix.erp.machines.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.dao.MaintenanceAndCalibrationDao;
import co.informatix.erp.machines.dao.MaintenanceLinesDao;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.machines.entities.MaintenanceAndCalibration;
import co.informatix.erp.machines.entities.MaintenanceLines;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all related logic with the creation and updating of the
 * maintenance and calibration of the machines in the system.
 * 
 * @author Mabell.Boada
 * @modify 26/11/2015 Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MaintenanceAndCalibrationAction implements Serializable {

	private List<MaintenanceAndCalibration> listaMaintenanceAndCalibrations;
	private List<SelectItem> machinesItems;
	private List<SelectItem> machinesTypeItems;
	private List<MaintenanceLines> listMaintenanceLines;

	private Paginador paginador = new Paginador();
	private Paginador paginadorMaintenance = new Paginador();

	private MaintenanceAndCalibration maintenanceAndCalibration;
	private Machines machines;
	private MachineTypes machineTypes;
	private MachineTypes machineTypesConsulta;
	private MaintenanceLines maintenanceLines;

	private Date fechaInicioBuscar;
	private Date fechaFinBuscar;
	private String serialNumberSearch;
	private String nameSearch;

	@EJB
	private MaintenanceAndCalibrationDao maintenanceAndCalibrationDao;
	@EJB
	private MachinesDao machinesDao;
	@EJB
	private MachineTypesDao machineTypesDao;
	@EJB
	private MaintenanceLinesDao maintenanceLinesDao;

	/**
	 * @return listaMaintenanceAndCalibrations: gets the list maintenance and
	 *         calibration
	 */
	public List<MaintenanceAndCalibration> getListaMaintenanceAndCalibrations() {
		return listaMaintenanceAndCalibrations;
	}

	/**
	 * @param listaMaintenanceAndCalibrations
	 *            : sets the list maintenance and calibration
	 */
	public void setListaMaintenanceAndCalibrations(
			List<MaintenanceAndCalibration> listaMaintenanceAndCalibrations) {
		this.listaMaintenanceAndCalibrations = listaMaintenanceAndCalibrations;
	}

	/**
	 * @return machinesItems: gets the list of the machines associated with the
	 *         maintenance and calibration
	 */
	public List<SelectItem> getMachinesItems() {
		return machinesItems;
	}

	/**
	 * @param machinesItems
	 *            : sets the list of the machines associated with the
	 *            maintenance and calibration
	 * 
	 */
	public void setMachinesItems(List<SelectItem> machinesItems) {
		this.machinesItems = machinesItems;
	}

	/**
	 * @return machinesTypeItems: List machinesType
	 */
	public List<SelectItem> getMachinesTypeItems() {
		return machinesTypeItems;
	}

	/**
	 * @param machinesTypeItems
	 *            :List machinesType
	 */
	public void setMachinesTypeItems(List<SelectItem> machinesTypeItems) {
		this.machinesTypeItems = machinesTypeItems;
	}

	/**
	 * @return listMaintenanceLines: List of maintenance lines associated to one
	 *         maintenance and calibration
	 */
	public List<MaintenanceLines> getListMaintenanceLines() {
		return listMaintenanceLines;
	}

	/**
	 * @param listMaintenanceLines
	 *            :List of maintenance lines associated to one maintenance and
	 *            calibration
	 */
	public void setListMaintenanceLines(
			List<MaintenanceLines> listMaintenanceLines) {
		this.listMaintenanceLines = listMaintenanceLines;
	}

	/**
	 * @return paginador: gets the paged list of maintenance and calibration
	 *         that may be in view
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : sets the paged list of maintenance and calibration that may
	 *            be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paginadorMaintenance: gets the paged list of maintenance lines
	 */
	public Paginador getPaginadorMaintenance() {
		return paginadorMaintenance;
	}

	/**
	 * @param paginadorMaintenance
	 *            :sets the paged list of maintenance lines
	 */
	public void setPaginadorMaintenance(Paginador paginadorMaintenance) {
		this.paginadorMaintenance = paginadorMaintenance;
	}

	/**
	 * @return maintenanceAndCalibration: gets the object of the maintenance and
	 *         calibration
	 */
	public MaintenanceAndCalibration getMaintenanceAndCalibration() {
		return maintenanceAndCalibration;
	}

	/**
	 * @param maintenanceAndCalibration
	 *            : sets the object of the maintenance and calibration
	 * 
	 */
	public void setMaintenanceAndCalibration(
			MaintenanceAndCalibration maintenanceAndCalibration) {
		this.maintenanceAndCalibration = maintenanceAndCalibration;
	}

	/**
	 * @return machines: gets object class of machines
	 */
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : sets object class of machines
	 * 
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	/**
	 * @return machineTypes: Machine type object that is associated maintenance
	 *         and calibration
	 */
	public MachineTypes getMachineTypes() {
		return machineTypes;
	}

	/**
	 * @param machineTypes
	 *            :Machine type object that is associated maintenance and
	 *            calibration
	 */
	public void setMachineTypes(MachineTypes machineTypes) {
		this.machineTypes = machineTypes;
	}

	/**
	 * @return machineTypesConsulta: machine type to compare the associated DB
	 *         and selected from the view
	 */
	public MachineTypes getMachineTypesConsulta() {
		return machineTypesConsulta;
	}

	/**
	 * @param machineTypesConsulta
	 *            :machine type to compare the associated DB and selected from
	 *            the view
	 */
	public void setMachineTypesConsulta(MachineTypes machineTypesConsulta) {
		this.machineTypesConsulta = machineTypesConsulta;
	}

	/**
	 * @return fechaInicioBuscar: gets the initial search range for the
	 *         maintenance and calibration in the system
	 */
	public Date getFechaInicioBuscar() {
		return fechaInicioBuscar;
	}

	/**
	 * @param fechaInicioBuscar
	 *            : sets the initial search range for the maintenance and
	 *            calibration in the system
	 */
	public void setFechaInicioBuscar(Date fechaInicioBuscar) {
		this.fechaInicioBuscar = fechaInicioBuscar;
	}

	/**
	 * @return maintenanceLines: Object maintenance lines
	 */
	public MaintenanceLines getMaintenanceLines() {
		return maintenanceLines;
	}

	/**
	 * @param maintenanceLines
	 *            :Object maintenance lines
	 */
	public void setMaintenanceLines(MaintenanceLines maintenanceLines) {
		this.maintenanceLines = maintenanceLines;
	}

	/**
	 * @return fechaFinBuscar: gets the end range to search the maintenance and
	 *         calibration system
	 */
	public Date getFechaFinBuscar() {
		return fechaFinBuscar;
	}

	/**
	 * @param fechaFinBuscar
	 *            :sets the end range to search the maintenance and calibration
	 *            system
	 */
	public void setFechaFinBuscar(Date fechaFinBuscar) {
		this.fechaFinBuscar = fechaFinBuscar;
	}

	/**
	 * @return serialNumberSearch: gets the serial number to search the machine
	 *         where the maintenance and calibration was applied
	 */
	public String getSerialNumberSearch() {
		return serialNumberSearch;
	}

	/**
	 * @param serialNumberSearch
	 *            :sets the serial number to search the machine where the
	 *            maintenance and calibration was applied
	 */
	public void setSerialNumberSearch(String serialNumberSearch) {
		this.serialNumberSearch = serialNumberSearch;
	}

	/**
	 * @return nameSearch: name of to search a maintenance line
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :name of to search a maintenance line
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of the maintenance and calibration and the items of machines and
	 * machine types
	 * 
	 * @modify 13/11/2015 cristhian.pico
	 * @return consultarMaintenanceAndCalibration: Method consulting the
	 *         maintenance and calibration, returns to the template management
	 */
	public String inicializarBusqueda() {
		try {
			llenarMachinesType();
			fechaInicioBuscar = null;
			fechaFinBuscar = null;
			serialNumberSearch = null;
			this.maintenanceAndCalibration = new MaintenanceAndCalibration();
			this.maintenanceAndCalibration.setMachines(new Machines());
			this.machines = new Machines();
			this.machines.setMachineTypes(new MachineTypes());
			this.machineTypes = new MachineTypes();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMaintenanceAndCalibration();
	}

	/**
	 * Method to clear the maintenance date parameters of the search and load
	 * the list of the maintenance and calibration according to the machine
	 * type, model or serial number selected
	 * 
	 * @author Cristhian.Pico
	 * 
	 * @return consultMaintenanceCalibration: Method consulting the maintenance
	 *         and calibration, returns to the template management
	 */
	public String searchByMachine() {
		fechaInicioBuscar = null;
		fechaFinBuscar = null;
		return consultMaintenanceCalibration();
	}

	/**
	 * The method allows consult the maintenance and calibration according with
	 * search parameters and clean the pager
	 * 
	 * @return consultarMaintenanceAndCalibration :Consult the list of existing
	 *         maintenance and calibration
	 */
	public String consultMaintenanceCalibration() {
		paginador = new Paginador();
		return consultarMaintenanceAndCalibration();
	}

	/**
	 * Consult the list of existing maintenance and calibration
	 * 
	 * @modify 13/11/2015 cristhian.pico
	 * @return gesMaintAndCali: Navigation rule that redirects to manage
	 *         maintenance and calibration
	 */
	public String consultarMaintenanceAndCalibration() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listaMaintenanceAndCalibrations = new ArrayList<MaintenanceAndCalibration>();
		List<MaintenanceAndCalibration> maintenanceAndCalibrationsAuxList = new ArrayList<MaintenanceAndCalibration>();
		List<SelectItem> parametros = new ArrayList<SelectItem>();
		StringBuilder consulta = new StringBuilder();
		StringBuilder unionMensajesBusqueda = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			busquedaAvanzada(consulta, parametros, bundle,
					unionMensajesBusqueda);
			Long cantidad = maintenanceAndCalibrationDao
					.cantidadMaintenanceAndCalibration(consulta, parametros);
			if (cantidad != null) {
				paginador.paginar(cantidad);
			}
			maintenanceAndCalibrationsAuxList = maintenanceAndCalibrationDao
					.consultarMaintenanceAndCalibration(paginador.getInicio(),
							paginador.getRango(), consulta, parametros);

			if (maintenanceAndCalibrationsAuxList != null) {
				for (MaintenanceAndCalibration maintenanceItemAux : maintenanceAndCalibrationsAuxList) {
					Machines machineAux = machinesDao
							.machinesXId(maintenanceItemAux.getMachines()
									.getIdMachine());
					MachineTypes machineTypeAux = machineTypesDao
							.machineTypeXId(machineAux.getMachineTypes()
									.getIdMachineType());
					machineAux.setMachineTypes(machineTypeAux);
					maintenanceItemAux.setMachines(machineAux);
					listaMaintenanceAndCalibrations.add(maintenanceItemAux);
				}
			}

			llenarMachinesType();
			if ((listaMaintenanceAndCalibrations == null || listaMaintenanceAndCalibrations
					.size() <= 0)
					&& !"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMensajesBusqueda);
			} else if (listaMaintenanceAndCalibrations == null
					|| listaMaintenanceAndCalibrations.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMensajesBusqueda.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("mantenimiento_calibracion_label"),
								unionMensajesBusqueda);
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMaintAndCali";
	}

	/**
	 * This method builds the query for advanced search also displayed messages
	 * depending on the search criteria selected by the user.
	 * 
	 * @modify 23/09/2015 marisol.calderon
	 * @modify 13/11/2015 cristhian.pico
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
	private void busquedaAvanzada(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		boolean addFilter = false;
		if (this.fechaInicioBuscar != null && this.fechaFinBuscar != null) {
			consult.append(addFilter ? "AND " : "WHERE ");
			addFilter = true;
			consult.append("mc.dateTime BETWEEN :fechaInicioBuscar AND :fechaFinBuscar ");
			SelectItem item = new SelectItem(fechaInicioBuscar,
					"fechaInicioBuscar");
			parameters.add(item);
			SelectItem item2 = new SelectItem(fechaFinBuscar, "fechaFinBuscar");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + formato.format(this.fechaInicioBuscar) + '"' + " ";
			unionMessagesSearch.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + formato.format(fechaFinBuscar) + '"' + " ";
			unionMessagesSearch.append(dateTo);
		}

		if (this.machineTypes != null
				&& this.machineTypes.getIdMachineType() > 0) {
			try {
				int idMachineType = this.machineTypes.getIdMachineType();
				MachineTypes machineTypeSearchMachine = machineTypesDao
						.machineTypeXId(idMachineType);
				consult.append(addFilter ? "AND " : "WHERE ");
				addFilter = true;
				consult.append("m.machineTypes.idMachineType = :idMachineType ");
				SelectItem itemMachineType = new SelectItem(idMachineType,
						"idMachineType");
				parameters.add(itemMachineType);
				String machineTypeName = bundleMachineType
						.getString("machines_label_types")
						+ ": "
						+ '"'
						+ machineTypeSearchMachine.getName() + '"' + " ";
				unionMessagesSearch.append(machineTypeName);
			} catch (Exception e) {
				ControladorContexto.mensajeError(e);
			}
		}

		if (this.machines != null && this.machines.getIdMachine() > 0) {
			try {
				int idMachineSearch = this.machines.getIdMachine();
				Machines modelSearchMachine = machinesDao
						.machinesXId(idMachineSearch);
				consult.append(addFilter ? "AND " : "WHERE ");
				addFilter = true;
				consult.append("m.idMachine = :idMachineSearch  ");
				SelectItem itemMachine = new SelectItem(idMachineSearch,
						"idMachineSearch");
				parameters.add(itemMachine);
				String machineName = bundle.getString("label_modelo") + ": "
						+ '"' + modelSearchMachine.getName() + '"' + " ";
				unionMessagesSearch.append(machineName);
			} catch (Exception e) {
				ControladorContexto.mensajeError(e);
			}
		}

		if (this.serialNumberSearch != null
				&& !"".equals(this.serialNumberSearch)) {
			consult.append(addFilter ? "AND " : "WHERE ");
			addFilter = true;
			consult.append(" UPPER(m.serialNumber) LIKE UPPER(:keywordSerialNumber) ");
			SelectItem itemSerialNumber = new SelectItem("%"
					+ this.serialNumberSearch + "%", "keywordSerialNumber");
			parameters.add(itemSerialNumber);
			String serialNumberMessage = bundleMachineType
					.getString("machines_label_serial_number")
					+ ": "
					+ '"'
					+ this.serialNumberSearch + '"' + " ";
			unionMessagesSearch.append(serialNumberMessage);
		}
	}

	/**
	 * Method to edit or create a new maintenance and calibration
	 * 
	 * @param maintenanceAndCalibration
	 *            :Maintenance and calibration object that you are adding or
	 *            editing
	 * 
	 * @return regMaintAndCal: Redirected to the template record maintenance and
	 *         calibration
	 * 
	 */
	public String agregarEditarMaintenanceAndCalibration(
			MaintenanceAndCalibration maintenanceAndCalibration) {
		try {
			llenarMachinesType();
			if (maintenanceAndCalibration != null) {
				this.maintenanceAndCalibration = maintenanceAndCalibration;
				Machines machineToEdit = machinesDao
						.machinesXId(maintenanceAndCalibration.getMachines()
								.getIdMachine());
				MachineTypes machineTypeEdit = machineTypesDao
						.machineTypeXId(machineToEdit.getMachineTypes()
								.getIdMachineType());
				this.setMachineTypes(machineTypeEdit);
				llenarMachine();
				this.setMachines(machineToEdit);
			} else {
				this.maintenanceAndCalibration = new MaintenanceAndCalibration();
				this.maintenanceAndCalibration.setMachines(new Machines());
				this.machines = new Machines();
				this.machines.setMachineTypes(new MachineTypes());
				this.machineTypes = new MachineTypes();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMaintAndCal";
	}

	/**
	 * Method used to save or edit the maintenance and calibration
	 * 
	 * @modify 13/11/2015 cristhian.pico
	 * @modify 09/12/2015 Andres.Gomez
	 * 
	 * @return consultarMaintenanceAndCalibration: Redirects to manage
	 *         maintenance and calibration with a list of updated dates
	 */
	public String guardarMaintenanceAndCalibration() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);

		try {
			this.machines = machinesDao.machinesXId(machines.getIdMachine());
			this.maintenanceAndCalibration.setMachines(machines);
			this.maintenanceAndCalibration.getMachines().setMachineTypes(
					machineTypes);
			if (maintenanceAndCalibration.getIdMaintenance() != 0) {
				maintenanceAndCalibrationDao
						.editarMaintenanceAndCalibration(maintenanceAndCalibration);
			} else {
				mensajeRegistro = "message_registro_guardar";
				maintenanceAndCalibrationDao
						.guardarMaintenanceAndCalibration(maintenanceAndCalibration);
			}
			Date dateComnpareTo = maintenanceAndCalibrationDao
					.lastMaintenance(machines.getIdMachine());
			Date maintenanceDate = maintenanceAndCalibration.getDateTime();
			machines.setLastMaintenance(maintenanceDate);
			if (dateComnpareTo != null
					&& maintenanceDate.before(dateComnpareTo)) {
				machines.setLastMaintenance(dateComnpareTo);
			}
			machinesDao.editMachines(machines);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					formato.format(maintenanceAndCalibration.getDateTime())));
			this.serialNumberSearch = new String();
			this.machines = new Machines();
			this.machines.setMachineTypes(new MachineTypes());
			this.machineTypes = new MachineTypes();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarMaintenanceAndCalibration();
	}

	/**
	 * Method charging machine types in a list
	 * 
	 * @modify 06/16/2015 Andres.Gomez
	 * 
	 * @throws Exception
	 */
	private void llenarMachinesType() throws Exception {
		machinesTypeItems = new ArrayList<SelectItem>();
		List<MachineTypes> listMachineType = machineTypesDao.listMachineType();
		if (listMachineType != null) {
			for (MachineTypes machineTypes : listMachineType) {
				machinesTypeItems.add(new SelectItem(machineTypes
						.getIdMachineType(), machineTypes.getName()));
			}
		}
		llenarMachine();
	}

	/**
	 * Method to load the machines on a list
	 * 
	 * @author Andres.Gomez
	 * 
	 **/
	public void llenarMachine() {
		int idMachine = 0;
		try {
			machinesItems = new ArrayList<SelectItem>();
			if (this.maintenanceAndCalibration != null && machineTypes == null) {
				idMachine = this.maintenanceAndCalibration.getMachines()
						.getIdMachine();
				this.machines = machinesDao.machinesXId(idMachine);
				machinesItems.add(new SelectItem(machines.getIdMachine(),
						machines.getName()));

				machineTypes = machineTypesDao.machineTypeXMachine(idMachine);
				machines.setMachineTypes(machineTypesConsulta);
			} else {
				if (machineTypes != null) {
					idMachine = machineTypes.getIdMachineType();
				}
				List<Machines> listMachines = machinesDao
						.listMachinesByTypes(idMachine);
				if (listMachines != null) {
					for (Machines machines : listMachines) {
						machinesItems.add(new SelectItem(machines
								.getIdMachine(), machines.getName()));
					}
				}
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows initialize the class variables and consult the
	 * maintenance line
	 * 
	 * @author Andres.Gomez
	 */
	public void initializeSearchMaintenanceLine() {
		this.nameSearch = "";
		consultMaintenanceLines();
	}

	/**
	 * This method allows consult the maintenance line referenced to maintenance
	 * and calibration
	 * 
	 * @author Andres.Gomez
	 * 
	 */
	public void consultMaintenanceLines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessageSearch = new StringBuilder();
		String messageSearch = "";
		this.listMaintenanceLines = new ArrayList<MaintenanceLines>();
		try {
			searchAvance(consult, parameters, bundle, unionMessageSearch);
			int idMaintenance = this.maintenanceAndCalibration
					.getIdMaintenance();
			Long amount = maintenanceLinesDao.amountMaintenanceLines(consult,
					parameters, idMaintenance);
			if (amount != null) {
				paginadorMaintenance.paginarRangoDefinido(amount, 5);
			}
			listMaintenanceLines = maintenanceLinesDao
					.listMaintenanceLinesXCalibration(
							paginadorMaintenance.getInicio(),
							paginadorMaintenance.getRango(), consult,
							parameters, idMaintenance);
			if ((listMaintenanceLines == null || listMaintenanceLines.size() <= 0)
					&& !"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessageSearch);
			} else if (listMaintenanceLines == null
					|| listMaintenanceLines.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessageSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachine
										.getString("lineas_mantenimiento_label"),
								unionMessageSearch);
			}
			validaciones.setMensajeBusqueda(messageSearch);
			if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				ControladorContexto.mensajeInformacion(
						"popupForm:tMaintenance", messageSearch);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows build a query depending if the nameSearch is not null
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param consult
	 *            :query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessageSearch
	 *            : message search
	 * 
	 */
	private void searchAvance(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessageSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("AND UPPER(ml.description) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessageSearch.append(bundle.getString("label_descripcion")
					+ ": " + '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new line maintenance
	 * 
	 * @author Andres.Gomez
	 * 
	 * @param maintenanceLines
	 *            :Object maintenance lines are adding or editing
	 * 
	 */
	public void addEditMaintenanceLines(MaintenanceLines maintenanceLines) {
		if (maintenanceLines != null) {
			this.maintenanceLines = maintenanceLines;
		} else {
			this.maintenanceLines = new MaintenanceLines();
			this.maintenanceLines.setMachines(this.maintenanceAndCalibration
					.getMachines());
			this.maintenanceLines
					.setMaintenanceAndCalibration(this.maintenanceAndCalibration);
		}
	}

	/**
	 * Method used to save or edit lines maintenance
	 * 
	 * @author Andres.Gomez
	 * 
	 */
	public void saveMaintenanceLines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		SimpleDateFormat formato = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		try {
			if (maintenanceLines.getIdMaintenanceline() != 0) {
				maintenanceLinesDao.editarMaintenanceLines(maintenanceLines);
			} else {
				mensajeRegistro = "message_registro_guardar";
				maintenanceLinesDao.guardarMaintenanceLines(maintenanceLines);
			}
			Double costBudget = 0d;
			Double costActual = 0d;
			if (listMaintenanceLines != null) {
				for (MaintenanceLines mLines : listMaintenanceLines) {
					if (mLines.getCostBudget() != null) {
						costBudget += mLines.getCostBudget();
					}
					if (mLines.getCostActual() != null) {
						costActual += mLines.getCostActual();
					}
				}
			} else {
				costBudget = maintenanceLines.getCostBudget();
				costActual = maintenanceLines.getCostActual();
			}
			this.maintenanceAndCalibration.setTotalCostBudget(costBudget);
			this.maintenanceAndCalibration.setTotalCostActual(costActual);
			maintenanceAndCalibrationDao
					.editarMaintenanceAndCalibration(maintenanceAndCalibration);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro),
					formato.format(maintenanceAndCalibration.getDateTime())));
			ControladorContexto.mensajeInformacion("popupForm:tMaintenance",
					MessageFormat.format(bundle.getString(mensajeRegistro),
							maintenanceLines.getDescription()));
			consultMaintenanceLines();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}
