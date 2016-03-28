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
 * This class implements the logic related to create, update, and delete machine
 * usages that may exist.
 * 
 * @author Andres.Gomez
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
	private Paginador pagination = new Paginador();
	private Paginador paginationActivity = new Paginador();

	private String nameSearch;
	private int nameMachine;
	private int year;
	private boolean edit;
	private Double currentDuration;

	/**
	 * @return listMachineUsage: List of the machine usages shown in the user
	 *         interface.
	 */
	public List<MachineUsage> getListMachineUsage() {
		return listMachineUsage;
	}

	/**
	 * @param listMachineUsage
	 *            :List of the machine usages shown in the user interface.
	 */
	public void setListMachineUsage(List<MachineUsage> listMachineUsage) {
		this.listMachineUsage = listMachineUsage;
	}

	/**
	 * @return listActivityMachines: List of activity and machine.
	 */
	public List<ActivityMachine> getListActivityMachines() {
		return listActivityMachines;
	}

	/**
	 * @param listActivityMachines
	 *            : List of activity and machine.
	 */
	public void setListActivityMachines(
			List<ActivityMachine> listActivityMachines) {
		this.listActivityMachines = listActivityMachines;
	}

	/**
	 * @return subListActivityMachines: Sub list of activity and machine.
	 */
	public List<ActivityMachine> getSubListActivityMachines() {
		return subListActivityMachines;
	}

	/**
	 * @param subListActivityMachines
	 *            : Sub list of activity and machine.
	 */
	public void setSubListActivityMachines(
			List<ActivityMachine> subListActivityMachines) {
		this.subListActivityMachines = subListActivityMachines;
	}

	/**
	 * @return listActivityMachineUnique: List of activity and machine that has
	 *         unique records.
	 */
	public List<ActivityMachine> getListActivityMachineUnique() {
		return listActivityMachineUnique;
	}

	/**
	 * @param listActivityMachineUnique
	 *            : List of activity and machine that has unique records
	 */
	public void setListActivityMachineUnique(
			List<ActivityMachine> listActivityMachineUnique) {
		this.listActivityMachineUnique = listActivityMachineUnique;
	}

	/**
	 * @return itemsMachines: List of machine items to selected the machines
	 *         usage.
	 */
	public ArrayList<SelectItem> getItemsMachines() {
		return itemsMachines;
	}

	/**
	 * @param itemsMachines
	 *            : List of machine items to selected the machines usage.
	 */
	public void setItemsMachines(ArrayList<SelectItem> itemsMachines) {
		this.itemsMachines = itemsMachines;
	}

	/**
	 * @return itemsYears: List of year items to selected in the view.
	 */
	public List<Integer> getItemsYears() {
		return itemsYears;
	}

	/**
	 * @param itemsYears
	 *            : List of year items to selected in the view.
	 */
	public void setItemsYears(List<Integer> itemsYears) {
		this.itemsYears = itemsYears;
	}

	/**
	 * @return machineUnique: Hash map to get a unique machine of a list.
	 */
	public HashMap<Integer, Machines> getMachineUnique() {
		return machineUnique;
	}

	/**
	 * @param machineUnique
	 *            : Hash map to set a unique machine of a list.
	 */
	public void setMachineUnique(HashMap<Integer, Machines> machineUnique) {
		this.machineUnique = machineUnique;
	}

	/**
	 * @return machineUsage: Object that contains the machine usage data.
	 */
	public MachineUsage getMachineUsage() {
		return machineUsage;
	}

	/**
	 * @param machineUsage
	 *            : Object that contains the machine usage data.
	 */
	public void setMachineUsage(MachineUsage machineUsage) {
		this.machineUsage = machineUsage;
	}

	/**
	 * @return pagination: Paged list of the machine usage.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Paged list of the machine usage.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationActivity: Paged list of the activities with unreported
	 *         duration.
	 */
	public Paginador getPaginationActivity() {
		return paginationActivity;
	}

	/**
	 * @param paginationActivity
	 *            : Paged list of the activities with unreported duration.
	 */
	public void setPaginationActivity(Paginador paginationActivity) {
		this.paginationActivity = paginationActivity;
	}

	/**
	 * @return nameSearch: Machine usage to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Machine usage to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return nameMachine: Machine name to search.
	 */
	public int getNameMachine() {
		return nameMachine;
	}

	/**
	 * @param nameMachine
	 *            : Machine name to search.
	 */
	public void setNameMachine(int nameMachine) {
		this.nameMachine = nameMachine;
	}

	/**
	 * @return year: Year number to search.
	 */
	public int getYear() {
		return year;
	}

	/**
	 * @param year
	 *            : Year number to search.
	 */
	public void setYear(int year) {
		this.year = year;
	}

	/**
	 * @return edit: Boolean flag that is true if the edit view is active and
	 *         false otherwise.
	 */
	public boolean isEdit() {
		return edit;
	}

	/**
	 * @param edit
	 *            : Boolean flag that is true if the edit view is active and
	 *            false otherwise.
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/**
	 * @return currentDuration: Double number to get the current duration of the
	 *         machine usage.
	 */
	public Double getCurrentDuration() {
		return currentDuration;
	}

	/**
	 * @param currentDuration
	 *            :Double number to set the current duration of the machine
	 *            usage.
	 */
	public void setCurrentDuration(Double currentDuration) {
		this.currentDuration = currentDuration;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of the machine usage.
	 * 
	 * @return consultMachineUsage: method to query the machine usage< it
	 *         redirects to the management template.
	 */
	public String initializeSearch() {
		nameSearch = "";
		nameMachine = 0;
		year = 0;
		return consultMachineUsage();
	}

	/**
	 * Query the list of the machine usages to show in the view.
	 * 
	 * @return "gesMachineUsage": Redirects to the template to manage the
	 *         machine usage.
	 */
	public String consultMachineUsage() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.listMachineUsage = new ArrayList<MachineUsage>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(consult, parameters, bundle, jointSearchMessages);
			Long amount = machineUsageDao.machineUsageAmount(consult,
					parameters);
			if (amount > 0) {
				pagination.paginar(amount);
			}
			listMachineUsage = machineUsageDao.searchMachineUsage(
					pagination.getInicio(), pagination.getRango(), consult,
					parameters);
			if ((listMachineUsage == null || listMachineUsage.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (listMachineUsage == null || listMachineUsage.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("machine_usage_label_s"),
								jointSearchMessages);
			}
			if (amount > 0) {
				loadDetailsMachines();
			}
			loadComboMachine();
			loadComboYear();
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMachineUsage";
	}

	/**
	 * This method builds a query for an advanced search build, it also builds
	 * display messages depending on the search criteria selected by the user.
	 * 
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * @param jointSearchMessages
	 *            : Search message.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		if (this.nameMachine != 0 && !"".equals(this.nameMachine)) {
			consult.append("WHERE mu.machineUsagePK.machine.idMachine = :keyword ");
			SelectItem item = new SelectItem(this.nameMachine, "keyword");
			parameters.add(item);
			jointSearchMessages.append(bundle.getString("label_name") + ": "
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
				jointSearchMessages.append(bundle.getString("label_name")
						+ ": " + '"' + this.year + '"');
			}
		}
	}

	/**
	 * This method makes allocated depreciation insurance maintenance of the
	 * activity machine.
	 */
	public void calculateMachinesActivities() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensajeMachine");
		try {
			listActivityMachineUnique = new ArrayList<ActivityMachine>();
			if (machineActivityNonDuration()) {
				String auxYear = String.valueOf(this.year);
				listActivityMachineUnique = activityMachineDao
						.listActivitiesMachine(auxYear);
				if (listActivityMachineUnique != null) {
					fillUniqueMachine();
					Set<Entry<Integer, Machines>> set = machineUnique
							.entrySet();
					for (Entry<Integer, Machines> entry : set) {
						Integer auxMachine = entry.getKey();
						this.currentDuration = 0d;
						MachineUsage machineUsage = new MachineUsage();
						for (ActivityMachine activityM : listActivityMachineUnique) {
							Integer machineComp = activityM
									.getActivityMachinePK().getMachines()
									.getIdMachine();
							if (auxMachine == machineComp) {
								this.currentDuration += activityM
										.getDurationActual();
							}
						}
						machineUsage.getMachineUsagePK().getMachine()
								.setIdMachine(auxMachine);
						machineUsage.getMachineUsagePK().setYear(this.year);
						Integer usage = currentDuration.intValue();
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
	 * This method calculates the allocate fields when the usage field is
	 * changed.
	 * 
	 * @throws Exception
	 */
	private void calculateAllocate(MachineUsage machineUsage) throws Exception {
		if (machineUsage.getHourlyDepreciation() != null) {
			Integer machineID = machineUsage.getMachineUsagePK().getMachine()
					.getIdMachine();
			Machines machineAux = machinesDao.machinesXId(machineID);
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
		String auxYear = String.valueOf(this.year);
		int machineId = machineUsage.getMachineUsagePK().getMachine()
				.getIdMachine();
		if (machineUsage.getHourlyInsurance() != null) {
			Double insuranceAux = insuranceDao.calculateInsurance(machineId,
					auxYear);
			Integer usage = machineUsage.getUsage();
			machineUsage.setHourlyInsurance(insuranceAux / usage);
			machineUsageDao.editMachineUsage(machineUsage);
		}
		if (machineUsage.getHourlyMaintenance() != null) {
			Double maintenanceAux = maintenanceCalibrationDao
					.calculateMaintenance(machineId, auxYear);
			Integer usage = machineUsage.getUsage();
			machineUsage.setHourlyMaintenance(maintenanceAux / usage);
			machineUsageDao.editMachineUsage(machineUsage);
		}
	}

	/**
	 * Method that fills a hash map with the unique machines.
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
	 * This method calculates the depreciation of a machine in a certain year.
	 */
	public void saveAllocateDepretiation() {
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("mensajeMachine");
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		listMachineUsage = new ArrayList<MachineUsage>();
		String registerMessage = "message_registro_modificar";
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
							.format(bundle.getString(registerMessage),
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
	 * This method calculates the Insurance of a machine in a year.
	 */
	public void saveAllocateIsurance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("mensajeMachine");
		listMachineUsage = new ArrayList<MachineUsage>();
		String registerMessage = "message_registro_modificar";
		try {
			listMachineUsage = machineUsageDao.listMachineUsageXYear(year);
			if (listMachineUsage != null) {
				String auxYear = String.valueOf(this.year);
				for (MachineUsage machineUsage : listMachineUsage) {
					int machineId = machineUsage.getMachineUsagePK()
							.getMachine().getIdMachine();
					Double insuranceAux = insuranceDao.calculateInsurance(
							machineId, auxYear);
					Integer usage = machineUsage.getUsage();
					machineUsage.setHourlyInsurance(insuranceAux / usage);
					machineUsageDao.editMachineUsage(machineUsage);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(registerMessage),
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
	 * This method calculates the maintenance of a machine in a year selected by
	 * user.
	 * 
	 */
	public void saveAllocateMaintenance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("mensajeMachine");
		listMachineUsage = new ArrayList<MachineUsage>();
		String registerMessage = "message_registro_modificar";
		try {
			listMachineUsage = machineUsageDao.listMachineUsageXYear(year);
			if (listMachineUsage != null) {
				String auxYear = String.valueOf(this.year);
				for (MachineUsage machineUsage : listMachineUsage) {
					int idMachine = machineUsage.getMachineUsagePK()
							.getMachine().getIdMachine();
					Double maintenanceAux = maintenanceCalibrationDao
							.calculateMaintenance(idMachine, auxYear);
					Integer usage = machineUsage.getUsage();
					machineUsage.setHourlyMaintenance(maintenanceAux / usage);
					machineUsageDao.editMachineUsage(machineUsage);
					ControladorContexto.mensajeInformacion(null, MessageFormat
							.format(bundle.getString(registerMessage),
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
	 * Save or edit the machine usage.
	 * 
	 * @param machineUsage
	 *            : Machine usage object to save or edit.
	 */
	public void saveCalculateMachineUsage(MachineUsage machineUsage) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
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
				registerMessage = "message_registro_guardar";
				machineUsageDao.saveMachineUsage(machineUsage);

			}
			Machines machine = machinesDao.machinesXId(idMachine);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage), machine.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method checks if the current duration of an object activityMachine
	 * record is null.
	 * 
	 * @return nonDuration: Flag that indicates if one activityMachine have an
	 *         actual duration as null.
	 */
	public boolean machineActivityNonDuration() {
		this.listActivityMachines = new ArrayList<ActivityMachine>();
		this.subListActivityMachines = new ArrayList<ActivityMachine>();
		boolean nonDuration = true;
		try {
			String auxYear = String.valueOf(this.year);
			this.listActivityMachines = activityMachineDao
					.listActivitiesMachineXYear(auxYear);
			if (this.listActivityMachines != null) {
				nonDuration = false;
				managePagedList();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return nonDuration;
	}

	/**
	 * Paged List that manages the activities list.
	 * 
	 */
	public void managePagedList() {
		Long amountPagedList = (long) this.listActivityMachines.size();
		try {
			this.paginationActivity.paginarRangoDefinido(amountPagedList, 10);
			int start = paginationActivity.getItemInicial() - 1;
			int end = paginationActivity.getItemFinal();
			this.subListActivityMachines = this.listActivityMachines.subList(
					start, end);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method fills the various objects associated with a machinesUsage.
	 * 
	 * @throws Exception
	 */
	private void loadDetailsMachines() throws Exception {
		List<MachineUsage> machinesUsages = new ArrayList<MachineUsage>();
		machinesUsages.addAll(this.listMachineUsage);
		this.listMachineUsage = new ArrayList<MachineUsage>();
		for (MachineUsage machineUsage : machinesUsages) {
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
	 *            : Machine usage that you are adding or editing.
	 * 
	 * @return "regMachineUsage": Redirects to the record machine usage
	 *         template.
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
	 * Method used to save or edit the machine usage.
	 * 
	 * @return consultMachineUsage: Redirects to manage machine usages with the
	 *         list of names updated.
	 */
	public String saveMachineUsage() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String regiserMessage = "message_registro_modificar";
		try {

			if (machineUsage.getMachineUsagePK().getMachine() != null) {
				machineUsageDao.editMachineUsage(machineUsage);
			} else {
				regiserMessage = "message_registro_guardar";
				machineUsageDao.saveMachineUsage(machineUsage);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(regiserMessage), machineUsage
							.getMachineUsagePK().getMachine().getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMachineUsage();
	}

	/**
	 * Method to delete a type of fuel of the database.
	 * 
	 * 
	 * @return consultFuelTypes(): Consult the list of the types of fuel and
	 *         redirects to manages the fuels.
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
