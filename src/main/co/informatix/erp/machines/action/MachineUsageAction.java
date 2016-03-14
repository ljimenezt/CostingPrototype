package co.informatix.erp.machines.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.ResourceBundle;
import java.util.Set;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.dao.ActivitiesAndMachineDao;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.machines.dao.InsuranceDao;
import co.informatix.erp.machines.dao.MachineUsageDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.dao.MaintenanceAndCalibrationDao;
import co.informatix.erp.machines.entities.MachineUsage;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the manage usage that may exist.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MachineUsageAction implements Serializable {

	@EJB
	private MachineUsageDao machineUsageDao;
	@EJB
	private MachinesDao machinesDao;
	@EJB
	private ActivitiesAndMachineDao activityMachineDao;
	@EJB
	private InsuranceDao insuranceDao;
	@EJB
	private MaintenanceAndCalibrationDao maintenanceCalibrationDao;

	private List<MachineUsage> listMachineUsage;
	private List<ActivityMachine> listActivityMachines;
	private List<ActivityMachine> subListActivityMachines;
	private List<ActivityMachine> listActivityMachineUnique;
	private ArrayList<SelectItem> itemsMachines;
	private List<Integer> itemsYears;
	private HashMap<Integer, Machines> machineUnique;

	private MachineUsage machineUsage;
	private Paginador paginador = new Paginador();
	private Paginador paginadorActivity = new Paginador();

	private String nameSearch;
	private int nameMachine;
	private int year;
	private boolean edit;
	private Double durationActual;

	/**
	 * @return listMachineUsage: list of the machine usages shown in the user
	 *         interface
	 */
	public List<MachineUsage> getListMachineUsage() {
		return listMachineUsage;
	}

	/**
	 * @param listMachineUsage
	 *            :list of the machine usages shown in the user interface
	 */
	public void setListMachineUsage(List<MachineUsage> listMachineUsage) {
		this.listMachineUsage = listMachineUsage;
	}

	/**
	 * @return listActivityMachines: list of activity and machine in use
	 */
	public List<ActivityMachine> getListActivityMachines() {
		return listActivityMachines;
	}

	/**
	 * @param listActivityMachines
	 *            :list of activity and machine in use
	 */
	public void setListActivityMachines(
			List<ActivityMachine> listActivityMachines) {
		this.listActivityMachines = listActivityMachines;
	}

	/**
	 * @return subListActivityMachines: sub list of activity and machine
	 */
	public List<ActivityMachine> getSubListActivityMachines() {
		return subListActivityMachines;
	}

	/**
	 * @param subListActivityMachines
	 *            :sub list of activity and machine
	 */
	public void setSubListActivityMachines(
			List<ActivityMachine> subListActivityMachines) {
		this.subListActivityMachines = subListActivityMachines;
	}

	/**
	 * @return listActivityMachineUnique: list of activity and machine what have
	 *         a unique records
	 */
	public List<ActivityMachine> getListActivityMachineUnique() {
		return listActivityMachineUnique;
	}

	/**
	 * @param listActivityMachineUnique
	 *            :list of activity and machine what have a unique records
	 */
	public void setListActivityMachineUnique(
			List<ActivityMachine> listActivityMachineUnique) {
		this.listActivityMachineUnique = listActivityMachineUnique;
	}

	/**
	 * @return itemsMachines: list items of machines to selected the machines
	 *         usage
	 */
	public ArrayList<SelectItem> getItemsMachines() {
		return itemsMachines;
	}

	/**
	 * @param itemsMachines
	 *            :list items of machines to selected the machines usage
	 */
	public void setItemsMachines(ArrayList<SelectItem> itemsMachines) {
		this.itemsMachines = itemsMachines;
	}

	/**
	 * @return itemsYears: list items of year to selected in the view
	 */
	public List<Integer> getItemsYears() {
		return itemsYears;
	}

	/**
	 * @param itemsYears
	 *            :list items of year to selected in the view
	 */
	public void setItemsYears(List<Integer> itemsYears) {
		this.itemsYears = itemsYears;
	}

	/**
	 * @return machineUnique: hash map to get unique machine of a list
	 */
	public HashMap<Integer, Machines> getMachineUnique() {
		return machineUnique;
	}

	/**
	 * @param machineUnique
	 *            :hash map to set unique machine of a list
	 */
	public void setMachineUnique(HashMap<Integer, Machines> machineUnique) {
		this.machineUnique = machineUnique;
	}

	/**
	 * @return machineUsage: object containing data on the machine usage
	 */
	public MachineUsage getMachineUsage() {
		return machineUsage;
	}

	/**
	 * @param machineUsage
	 *            :object containing data on the machine usage
	 */
	public void setMachineUsage(MachineUsage machineUsage) {
		this.machineUsage = machineUsage;
	}

	/**
	 * @return paginador: Management paginated list of the machine usage.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paginated list of the machine usage.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return paginadorActivity: Management paginated list of the activities
	 *         with unreported duration
	 */
	public Paginador getPaginadorActivity() {
		return paginadorActivity;
	}

	/**
	 * @param paginadorActivity
	 *            :Management paginated list of the activities with unreported
	 *            duration
	 */
	public void setPaginadorActivity(Paginador paginadorActivity) {
		this.paginadorActivity = paginadorActivity;
	}

	/**
	 * @return nameSearch: machine usage to search
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            :machine usage to search
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return nameMachine: machine name to search in the manage
	 */
	public int getNameMachine() {
		return nameMachine;
	}

	/**
	 * @param nameMachine
	 *            :machine name to search in the manage
	 */
	public void setNameMachine(int nameMachine) {
		this.nameMachine = nameMachine;
	}

	/**
	 * @return year: number of the year to search
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            :number of the year to search
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return edit: boolean flag indicate if true is edit view and false if the
	 *         view is register
	 */
	public boolean isEdit() {
		return edit;
	}

	/**
	 * @param edit
	 *            :boolean flag indicate if true is edit view and false if the
	 *            view is register
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/**
	 * @return durationActual: Double number to get the duration actual of the
	 *         machine usage
	 */
	public Double getDurationActual() {
		return durationActual;
	}

	/**
	 * @param durationActual
	 *            :Double number to set the duration actual of the machine usage
	 */
	public void setDurationActual(Double durationActual) {
		this.durationActual = durationActual;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * listing of the machine usage
	 * 
	 * @return consultMachineUsage: method to query the machine usage, returns
	 *         to the template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		nameMachine = 0;
		year = 0;
		return consultMachineUsage();
	}

	/**
	 * Consult the list of the machine usages to show in the view
	 * 
	 * @return "gesMachineUsage": redirects to the template to manage the
	 *         machine usage
	 */
	public String consultMachineUsage() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listMachineUsage = new ArrayList<MachineUsage>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String mensajeBusqueda = "";
		try {
			advancedSearch(consult, parameters, bundle, unionMessagesSearch);
			Long amount = machineUsageDao.amountMachineUsage(consult,
					parameters);
			if (amount > 0) {
				paginador.paginar(amount);
			}
			listMachineUsage = machineUsageDao.consultMachineUsage(
					paginador.getInicio(), paginador.getRango(), consult,
					parameters);
			if ((listMachineUsage == null || listMachineUsage.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listMachineUsage == null || listMachineUsage.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("machine_usage_label_s"),
								unionMessagesSearch);
			}
			if (amount > 0) {
				loadDetailsMachines();
			}
			loadComboMachine();
			loadComboYear();
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMachineUsage";
	}

	/**
	 * This method build consultation for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
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
		if (this.nameMachine != 0 && !"".equals(this.nameMachine)) {
			consult.append("WHERE mu.machineUsagePK.machine.idMachine = :keyword ");
			SelectItem item = new SelectItem(this.nameMachine, "keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameMachine + '"');
			if (this.year != 0 && !"".equals(this.year)) {
				consult.append("AND mu.machineUsagePK.year = :keyword1 ");
				item = new SelectItem(this.year, "keyword1");
				parameters.add(item);
			}
		} else {
			if (this.year != 0 && !"".equals(this.year)) {
				consult.append("WHERE mu.machineUsagePK.year = :keyword1 ");
				SelectItem item = new SelectItem(this.year, "keyword1");
				parameters.add(item);
				unionMessagesSearch.append(bundle.getString("label_nombre")
						+ ": " + '"' + this.year + '"');
			}
		}
	}

	/**
	 * This method allows allocated depreciation insurance maintenance of the
	 * activity machine
	 */
	public void calculateMachinesActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensajeMachine");
		try {
			listActivityMachineUnique = new ArrayList<ActivityMachine>();
			if (machineActivityNonDuration()) {
				String anio = String.valueOf(this.year);
				listActivityMachineUnique = activityMachineDao
						.listActivitiesMachine(anio);
				if (listActivityMachineUnique != null) {
					fillUniqueMachine();
					Set<Entry<Integer, Machines>> set = machineUnique
							.entrySet();
					for (Entry<Integer, Machines> entry : set) {
						Integer machinAux = entry.getKey();
						this.durationActual = 0d;
						MachineUsage machineUsage = new MachineUsage();
						for (ActivityMachine activityM : listActivityMachineUnique) {
							Integer machineComp = activityM
									.getActivityMachinePK().getMachines()
									.getIdMachine();
							if (machinAux == machineComp) {
								this.durationActual += activityM
										.getDurationActual();
							}
						}
						machineUsage.getMachineUsagePK().getMachine()
								.setIdMachine(machinAux);
						machineUsage.getMachineUsagePK().setYear(this.year);
						Integer usage = durationActual.intValue();
						machineUsage.setUsage(usage);
						saveCalculateMachineUsage(machineUsage);
					}
				}
				if (listActivityMachineUnique == null
						|| listActivityMachineUnique.size() <= 0) {
					String value = Integer.toString(year);
					String format = MessageFormat.format(bundle
							.getString("machine_usage_message_non_activity"),
							value);
					ControladorContexto.mensajeInformacion(null, format);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows calculate the allocate fields when the usage field is
	 * changed
	 * 
	 * @throws Exception
	 * 
	 */
	private void calculateAllocate(MachineUsage machineUsage) throws Exception {
		if (machineUsage.getHourlyDepreciation() != null) {
			Integer idMachine = machineUsage.getMachineUsagePK().getMachine()
					.getIdMachine();
			Machines machineAux = machinesDao.machinesXId(idMachine);
			Calendar cal = Calendar.getInstance();
			cal.setTime(machineAux.getPurchaseDate());
			int currentYear = (year - cal.get(Calendar.YEAR));
			if (currentYear > machineAux.getLifeYears()) {
				machineUsage.setHourlyDepreciation(0d);
			} else {
				Integer usage = machineUsage.getUsage();
				Double depre = machineAux.getDepreciation();
				machineUsage.setHourlyDepreciation(depre / usage);
			}
			machineUsageDao.editMachineUsage(machineUsage);
		}
		String anio = String.valueOf(this.year);
		int idMachine = machineUsage.getMachineUsagePK().getMachine()
				.getIdMachine();
		if (machineUsage.getHourlyInsurance() != null) {
			Double insuranceAux = insuranceDao.calculateInsurance(idMachine,
					anio);
			Integer usage = machineUsage.getUsage();
			machineUsage.setHourlyInsurance(insuranceAux / usage);
			machineUsageDao.editMachineUsage(machineUsage);
		}
		if (machineUsage.getHourlyMaintenance() != null) {
			Double maintenanceAux = maintenanceCalibrationDao
					.calculateMaintenance(idMachine, anio);
			Integer usage = machineUsage.getUsage();
			machineUsage.setHourlyMaintenance(maintenanceAux / usage);
			machineUsageDao.editMachineUsage(machineUsage);
		}
	}

	/**
	 * Method allows fill a hash map of the unique machines
	 * 
	 */
	public void fillUniqueMachine() {
		machineUnique = new HashMap<Integer, Machines>();
		for (ActivityMachine activityM : listActivityMachineUnique) {
			Integer idMachine = activityM.getActivityMachinePK().getMachines()
					.getIdMachine();
			Machines machine = activityM.getActivityMachinePK().getMachines();
			machineUnique.put(idMachine, machine);
		}
	}

	/**
	 * This method allows calculate the depreciation of a machine in a year
	 * selected by user
	 */
	public void saveAllocateDepretiation() {
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("mensajeMachine");
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		listMachineUsage = new ArrayList<MachineUsage>();
		String mensajeRegistro = "message_registro_modificar";
		try {
			listMachineUsage = machineUsageDao.listMachineUsageXYear(year);
			if (listMachineUsage != null) {
				int year = Calendar.getInstance().get(Calendar.YEAR);
				for (MachineUsage machineUsage : listMachineUsage) {
					int idMachine = machineUsage.getMachineUsagePK()
							.getMachine().getIdMachine();
					Machines machineAux = machinesDao.machinesXId(idMachine);
					Calendar cal = Calendar.getInstance();
					cal.setTime(machineAux.getPurchaseDate());
					int currentYear = (year - cal.get(Calendar.YEAR));
					if (currentYear > machineAux.getLifeYears()) {
						machineUsage.setHourlyDepreciation(0d);
					} else {
						Integer usage = machineUsage.getUsage();
						Double depre = machineAux.getDepreciation();
						machineUsage.setHourlyDepreciation(depre / usage);
					}
					machineUsageDao.editMachineUsage(machineUsage);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(mensajeRegistro),
									machineAux.getName()));
				}
			}
			if (listMachineUsage == null || listMachineUsage.size() <= 0) {
				String value = Integer.toString(year);
				String format = MessageFormat.format(bundleMachine
						.getString("machine_usage_message_non_usage"), value);
				ControladorContexto.mensajeInformacion(null, format);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows calculate the Insurance of a machine in a year
	 * selected by user
	 * 
	 */
	public void saveAllocateIsurance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("mensajeMachine");
		listMachineUsage = new ArrayList<MachineUsage>();
		String mensajeRegistro = "message_registro_modificar";
		try {
			listMachineUsage = machineUsageDao.listMachineUsageXYear(year);
			if (listMachineUsage != null) {
				String anio = String.valueOf(this.year);
				for (MachineUsage machineUsage : listMachineUsage) {
					int idMachine = machineUsage.getMachineUsagePK()
							.getMachine().getIdMachine();
					Double insuranceAux = insuranceDao.calculateInsurance(
							idMachine, anio);
					Integer usage = machineUsage.getUsage();
					machineUsage.setHourlyInsurance(insuranceAux / usage);
					machineUsageDao.editMachineUsage(machineUsage);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(mensajeRegistro),
									machineUsage.getMachineUsagePK()
											.getMachine().getName()));
				}
			}
			if (listMachineUsage == null || listMachineUsage.size() <= 0) {
				String value = Integer.toString(year);
				String format = MessageFormat.format(bundleMachine
						.getString("machine_usage_message_non_usage"), value);
				ControladorContexto.mensajeInformacion(null, format);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows calculate the maintenance of a machine in a year
	 * selected by user
	 * 
	 */
	public void saveAllocateMaintenance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("mensajeMachine");
		listMachineUsage = new ArrayList<MachineUsage>();
		String mensajeRegistro = "message_registro_modificar";
		try {
			listMachineUsage = machineUsageDao.listMachineUsageXYear(year);
			if (listMachineUsage != null) {
				String anio = String.valueOf(this.year);
				for (MachineUsage machineUsage : listMachineUsage) {
					int idMachine = machineUsage.getMachineUsagePK()
							.getMachine().getIdMachine();
					Double maintenanceAux = maintenanceCalibrationDao
							.calculateMaintenance(idMachine, anio);
					Integer usage = machineUsage.getUsage();
					machineUsage.setHourlyMaintenance(maintenanceAux / usage);
					machineUsageDao.editMachineUsage(machineUsage);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(mensajeRegistro),
									machineUsage.getMachineUsagePK()
											.getMachine().getName()));
				}
			}
			if (listMachineUsage == null || listMachineUsage.size() <= 0) {
				String value = Integer.toString(year);
				String format = MessageFormat.format(bundleMachine
						.getString("machine_usage_message_non_usage"), value);
				ControladorContexto.mensajeInformacion(null, format);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method used to save or edit the machine usage
	 * 
	 * @param machineUsage
	 *            : Object machine usage to save or edit
	 */
	public void saveCalculateMachineUsage(MachineUsage machineUsage) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {
			int idMachine = machineUsage.getMachineUsagePK().getMachine()
					.getIdMachine();
			int year = machineUsage.getMachineUsagePK().getYear();
			MachineUsage machineUsageAux = machineUsageDao.machineUsageExists(
					idMachine, year);
			if (machineUsageAux != null) {
				machineUsageDao.editMachineUsage(machineUsage);
				machineUsageAux.setUsage(machineUsage.getUsage());
				calculateAllocate(machineUsageAux);
			} else {
				mensajeRegistro = "message_registro_guardar";
				machineUsageDao.saveMachineUsage(machineUsage);

			}
			Machines machine = machinesDao.machinesXId(idMachine);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), machine.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate if the field duration actual the one record
	 * of the object activityMachine is null
	 * 
	 * @return nonDuration: Flag indicate if one activityMachine have a actual
	 *         duration as null
	 */
	public boolean machineActivityNonDuration() {
		this.listActivityMachines = new ArrayList<ActivityMachine>();
		this.subListActivityMachines = new ArrayList<ActivityMachine>();
		boolean nonDuration = true;
		try {
			String anio = String.valueOf(this.year);
			this.listActivityMachines = activityMachineDao
					.listActivitiesMachineXYear(anio);
			if (this.listActivityMachines != null) {
				nonDuration = false;
				managePager();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return nonDuration;
	}

	/**
	 * Pager manages the activities list
	 * 
	 */
	public void managePager() {
		Long amountPager = (long) this.listActivityMachines.size();
		try {
			this.paginadorActivity.paginarRangoDefinido(amountPager, 10);
			int start = paginadorActivity.getItemInicial() - 1;
			int end = paginadorActivity.getItemFinal();
			this.subListActivityMachines = this.listActivityMachines.subList(
					start, end);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method fills the various objects associated with a machinesUsage
	 * 
	 * @throws Exception
	 */
	private void loadDetailsMachines() throws Exception {
		List<MachineUsage> machinesU = new ArrayList<MachineUsage>();
		machinesU.addAll(this.listMachineUsage);
		this.listMachineUsage = new ArrayList<MachineUsage>();
		for (MachineUsage machineUsage : machinesU) {
			int idMachine = machineUsage.getMachineUsagePK().getMachine()
					.getIdMachine();
			Machines machine = machinesDao.machinesXId(idMachine);
			machineUsage.getMachineUsagePK().setMachine(machine);
			this.listMachineUsage.add(machineUsage);
		}
	}

	/**
	 * Method to edit or create new machine usage.
	 * 
	 * @param machineUsage
	 *            :machine usage that you are adding or editing
	 * 
	 * @return "regMachineUsage": redirected to the template record machine
	 *         usage.
	 */
	public String addEditMachineUsage(MachineUsage machineUsage) {
		try {
			this.subListActivityMachines = new ArrayList<ActivityMachine>();
			if (machineUsage != null) {
				this.machineUsage = machineUsage;
			} else {
				this.machineUsage = new MachineUsage();
			}
			loadComboMachine();
			loadComboYear();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regMachineUsage";
	}

	/**
	 * Method that allows check the year to fill the combo of the user
	 * interface.
	 * 
	 * @throws Exception
	 */
	private void loadComboYear() throws Exception {
		itemsYears = new ArrayList<Integer>();
		int i;
		int yearCurrent = Calendar.getInstance().get(Calendar.YEAR);
		for (i = 2000; i <= yearCurrent; i++) {
			itemsYears.add(i);
		}
	}

	/**
	 * Method that allows check the machines to fill the combo of the user
	 * interface.
	 * 
	 * @throws Exception
	 */
	private void loadComboMachine() throws Exception {
		itemsMachines = new ArrayList<SelectItem>();
		List<Machines> machines = machinesDao.listMachines();
		if (machines != null) {
			for (Machines machine : machines) {
				itemsMachines.add(new SelectItem(machine.getIdMachine(),
						machine.getName()));
			}
		}
	}

	/**
	 * Method used to save or edit the machine usage
	 * 
	 * @return consultMachineUsage: Redirects to manage manage usages with the
	 *         list of names updated
	 */
	public String saveMachineUsage() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String mensajeRegistro = "message_registro_modificar";
		try {

			if (machineUsage.getMachineUsagePK().getMachine() != null) {
				machineUsageDao.editMachineUsage(machineUsage);
			} else {
				mensajeRegistro = "message_registro_guardar";
				machineUsageDao.saveMachineUsage(machineUsage);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(mensajeRegistro), machineUsage
							.getMachineUsagePK().getMachine().getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMachineUsage();
	}

	/**
	 * Method to delete a type of fuel database
	 * 
	 * 
	 * @return consultFuelTypes(): Consult the list of the types of fuel and
	 *         returns to manages the fuels
	 */
	public String deleteManageUsage() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			machineUsageDao.deleteMachineUsage(machineUsage);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), machineUsage
							.getMachineUsagePK().getMachine().getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					machineUsage.getMachineUsagePK().getMachine().getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMachineUsage();
	}

}
