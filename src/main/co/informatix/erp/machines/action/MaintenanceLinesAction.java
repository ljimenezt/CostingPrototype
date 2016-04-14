package co.informatix.erp.machines.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.dao.MaintenanceAndCalibrationDao;
import co.informatix.erp.machines.dao.MaintenanceLinesDao;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.machines.entities.MaintenanceAndCalibration;
import co.informatix.erp.machines.entities.MaintenanceLines;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements the business logic: creating and updating maintenance
 * lines in the system.
 * 
 * @author Mabell.Boada
 * 
 */
@ManagedBean
@SuppressWarnings("serial")
@RequestScoped
public class MaintenanceLinesAction implements Serializable {
	private List<MaintenanceLines> maintenanceLinesList;
	private List<SelectItem> machineOptions;
	private List<SelectItem> maintenanceOptions;

	private Paginador pagination = new Paginador();

	private MaintenanceLines maintenanceLines;

	private String machineNameSearch;

	@EJB
	private MaintenanceLinesDao maintenanceLinesDao;

	@EJB
	private MaintenanceAndCalibrationDao maintenanceAndCalibrationDao;

	@EJB
	private MachinesDao machinesDao;

	/**
	 * @return maintenanceLinesList: Gets the list of maintenance lines.
	 */
	public List<MaintenanceLines> getMaintenanceLinesList() {
		return maintenanceLinesList;
	}

	/**
	 * @param maintenanceLinesList
	 *            : Sets the list of maintenance lines.
	 */
	public void setMaintenanceLinesList(
			List<MaintenanceLines> maintenanceLinesList) {
		this.maintenanceLinesList = maintenanceLinesList;
	}

	/**
	 * @return machineOptions: Gets the list of the machines attached to the
	 *         maintenance lines.
	 */
	public List<SelectItem> getMachineOptions() {
		return machineOptions;
	}

	/**
	 * @param machineOptions
	 *            : Sets the list of the machines attached to the maintenance
	 *            lines.
	 */
	public void setMachineOptions(List<SelectItem> machineOptions) {
		this.machineOptions = machineOptions;
	}

	/**
	 * @return maintenanceOptions: Gets the list of the maintenance and
	 *         calibration lines associated with a maintenance.
	 */
	public List<SelectItem> getMaintenanceOptions() {
		return maintenanceOptions;
	}

	/**
	 * @param maintenanceOptions
	 *            : Sets the list of the maintenance and calibration lines
	 *            associated a maintenance.
	 */
	public void setMaintenanceOptions(List<SelectItem> maintenanceOptions) {
		this.maintenanceOptions = maintenanceOptions;
	}

	/**
	 * @return pagination: Gets the paged list of maintenance lines that may be
	 *         in the view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Sets the paged list of maintenance lines that may be in the
	 *            view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return maintenanceLines: Gets the maintenance lines object .
	 */
	public MaintenanceLines getMaintenanceLines() {
		return maintenanceLines;
	}

	/**
	 * @param maintenanceLines
	 *            : Sets the maintenance lines object.
	 */
	public void setMaintenanceLines(MaintenanceLines maintenanceLines) {
		this.maintenanceLines = maintenanceLines;
	}

	/**
	 * @return machineNameSearch: Gets the search parameter.
	 */
	public String getMachineNameSearch() {
		return machineNameSearch;
	}

	/**
	 * @param machineNameSearch
	 *            : Sets the search parameter.
	 */
	public void setMachineNameSearch(String machineNameSearch) {
		this.machineNameSearch = machineNameSearch;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of maintenance lines.
	 * 
	 * @return searchMaintenanceLines: Look for maintenance lines, and it
	 *         redirects to the template management
	 */
	public String initializeSearch() {
		machineNameSearch = "";
		return searchMaintenanceLines();
	}

	/**
	 * Look for the list of existing maintenance lines.
	 * 
	 * @return gesMaintLin: Navigation rule that redirects to manage maintenance
	 *         lines.
	 */
	public String searchMaintenanceLines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("messageMachine");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		maintenanceLinesList = new ArrayList<MaintenanceLines>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			loadMachineCombos();
			advancedSearch(queryBuilder, parameters, bundle,
					jointSearchMessages);
			Long amount = maintenanceLinesDao.maintenanceLinesAmount(
					queryBuilder, parameters);
			if (amount != null) {
				pagination.paginar(amount);
			}
			maintenanceLinesList = maintenanceLinesDao.queryMaintenanceLines(
					pagination.getInicio(), pagination.getRango(),
					queryBuilder, parameters);

			if ((maintenanceLinesList == null || maintenanceLinesList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (maintenanceLinesList == null
					|| maintenanceLinesList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("maintenance_lines_label"),
								jointSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMaintLin";
	}

	/**
	 * Method to edit or create a new maintenance line.
	 * 
	 * @param maintenanceLines
	 *            : Object of maintenance lines you want to add or edit.
	 * 
	 * @return regMaintLin: it redirects to the register a maintenance line
	 *         template.
	 * 
	 */
	public String addEditMaintenanceLines(MaintenanceLines maintenanceLines) {
		try {
			loadMachineCombos();
			if (maintenanceLines != null) {
				this.maintenanceLines = maintenanceLines;
			} else {
				this.maintenanceLines = new MaintenanceLines();
				this.maintenanceLines.setMachines(new Machines());
				this.maintenanceLines
						.setMaintenanceAndCalibration(new MaintenanceAndCalibration());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMaintLin";
	}

	/**
	 * Method to load the machines on a list.
	 * 
	 * @throws Exception
	 */
	private void loadMachineCombos() throws Exception {
		machineOptions = new ArrayList<SelectItem>();
		List<Machines> machinesList = machinesDao.listMachines();
		if (machinesList != null) {
			for (Machines machine : machinesList) {
				machineOptions.add(new SelectItem(machine.getIdMachine(),
						machine.getName()));

			}
		}

	}

	/**
	 * Method to load MaintenanceAndCalibration objects in a list.
	 */
	public void loadMaintenance() {
		maintenanceOptions = new ArrayList<SelectItem>();
		SimpleDateFormat date = new SimpleDateFormat("yyyy/MM/dd");
		try {
			List<MaintenanceAndCalibration> maintenanceAndCalibrationList = maintenanceAndCalibrationDao
					.maintenanceCalibrationXId(this.maintenanceLines
							.getMachines().getIdMachine());
			if (maintenanceAndCalibrationList != null) {
				for (MaintenanceAndCalibration maintenanceAndCalibration : maintenanceAndCalibrationList) {
					maintenanceOptions.add(new SelectItem(
							maintenanceAndCalibration.getIdMaintenance(), date
									.format(maintenanceAndCalibration
											.getDateTime())));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method builds queries with advanced search, it also builds display
	 * messages depending on the search criteria selected by the user, a machine
	 * name.
	 * 
	 * @param queryBuilder
	 *            : Query to concatenate
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context of access language tags
	 * @param jointSearchMessages
	 *            : Search message.
	 */
	private void advancedSearch(StringBuilder queryBuilder,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		if (this.machineNameSearch != null
				&& !"".equals(this.machineNameSearch)) {
			queryBuilder.append("JOIN  ml.machines m ");
			queryBuilder.append("WHERE UPPER(m.name)=UPPER(:nombreMaquina )");
			SelectItem item = new SelectItem(this.machineNameSearch,
					"nombreMaquina");
			parameters.add(item);
			jointSearchMessages.append(bundle.getString("label_name") + ": "
					+ '"' + this.machineNameSearch + '"');
		}
	}

	/**
	 * Method used to save or edit maintenance lines.
	 * 
	 * @modify 28/03/2016 Jhair.Leal
	 * 
	 * @return searchMaintenanceLines: Redirects to manage maintenance lines
	 *         with the list of names updated.
	 */
	public String saveMaintenanceLines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("messageMachine");
		String registerMessage = "message_registro_modificar";

		try {
			Machines machine = machinesDao.machinesXId(maintenanceLines
					.getMachines().getIdMachine());
			StringBuilder details = new StringBuilder();
			details.append(bundleMachineType.getString("machines_label_names"));
			details.append(": ");
			details.append(machine.getName());
			if (maintenanceLines.getIdMaintenanceline() != 0) {
				maintenanceLinesDao.editMaintenanceLines(maintenanceLines);
			} else {
				registerMessage = "message_registro_guardar";
				maintenanceLinesDao.saveMaintenanceLines(maintenanceLines);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), details));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchMaintenanceLines();
	}

}