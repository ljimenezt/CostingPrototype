package co.informatix.erp.machines.action;

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

import co.informatix.erp.machines.dao.InsuranceDao;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.entities.Insurance;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class implements logic of insurance that may be in the database. The
 * logic is to consult, edit or add insurances.
 * 
 * @author Sergio.Ortiz
 **/
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class InsuranceAction implements Serializable {

	@EJB
	private MachinesDao machinesDao;

	@EJB
	private InsuranceDao insuranceDao;

	@EJB
	private MachineTypesDao machineTypesDao;

	private ArrayList<SelectItem> machinesList;
	private ArrayList<SelectItem> machineTypeOption;
	private List<Insurance> insurancesList;

	private Date initDaySearch;
	private Date lastDaySearch;

	private Paginador paginador = new Paginador();
	private Insurance insurance;
	private Machines machines;
	private MachineTypes machineTypes;
	private MachineTypes machineTypesResult;

	/**
	 * @return machinesList: List of machines associated to an insurance.
	 */
	public ArrayList<SelectItem> getMachinesList() {
		return machinesList;
	}

	/**
	 * @param machinesList
	 *            : List of machines associated to an insurance.
	 */
	public void setMachinesList(ArrayList<SelectItem> machinesList) {
		this.machinesList = machinesList;
	}

	/**
	 * @return machineTypeOption: MachinesType list.
	 */
	public ArrayList<SelectItem> getMachineTypeOption() {
		return machineTypeOption;
	}

	/**
	 * @param machineTypeOption
	 *            : MachinesType list.
	 */
	public void setMachineTypeOption(ArrayList<SelectItem> machineTypeOption) {
		this.machineTypeOption = machineTypeOption;
	}

	/**
	 * @return insurancesList: Insurance list.
	 */
	public List<Insurance> getInsurancesList() {
		return insurancesList;
	}

	/**
	 * @param insurancesList
	 *            : Insurance list.
	 */
	public void setInsurancesList(List<Insurance> insurancesList) {
		this.insurancesList = insurancesList;
	}

	/**
	 * @return insurance: Object of the insurance class.
	 */
	public Insurance getInsurance() {
		return insurance;
	}

	/**
	 * @param insurance
	 *            : Object of the insurance class.
	 * 
	 */
	public void setInsurance(Insurance insurance) {
		this.insurance = insurance;
	}

	/**
	 * @return machines: Machines object.
	 */
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : Machines object.
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	/**
	 * @return machineTypes: Machine type object that is associated with the
	 *         insurance.
	 */
	public MachineTypes getMachineTypes() {
		return machineTypes;
	}

	/**
	 * @param machineTypes
	 *            : Machine type object that is associated with the insurance.
	 */
	public void setMachineTypes(MachineTypes machineTypes) {
		this.machineTypes = machineTypes;
	}

	/**
	 * @return initDaySearch: Determines the beginning of the range to search
	 *         for insurances in the system.
	 */
	public Date getInitDaySearch() {
		return initDaySearch;
	}

	/**
	 * @param initDaySearch
	 *            : Determines the beginning of the range to search for
	 *            insurances in the system.
	 */
	public void setInitDaySearch(Date initDaySearch) {
		this.initDaySearch = initDaySearch;
	}

	/**
	 * @return lastDaySearch: Determines the end of the range to search for
	 *         insurances in the system.
	 */
	public Date getLastDaySearch() {
		return lastDaySearch;
	}

	/**
	 * @param lastDaySearch
	 *            : Determines the end of the range to search for insurance in
	 *            the system.
	 */
	public void setLastDaySearch(Date lastDaySearch) {
		this.lastDaySearch = lastDaySearch;
	}

	/**
	 * @return paginador: Paged list of insurance that may be in view.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Paged list of insurance that may be in view
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return machineTypesResult: Type of machine to compare with one
	 *         associated to the database and selected from the view.
	 */
	public MachineTypes getMachineTypesResult() {
		return machineTypesResult;
	}

	/**
	 * @param machineTypesResult
	 *            : Type of machine to compare with one associated to the
	 *            database and selected from the view.
	 */
	public void setMachineTypesResult(MachineTypes machineTypesResult) {
		this.machineTypesResult = machineTypesResult;
	}

	/**
	 * Method to initialize the search parameters and load the initial list of
	 * insurances.
	 * 
	 * @return gesInsurance: Navigation rule that redirects to manage insurance
	 *         template.
	 */
	public String initializeSearch() {
		try {
			loadMachineTypeCombos();
			initDaySearch = null;
			lastDaySearch = null;
			this.insurancesList = new ArrayList<Insurance>();
			this.insurance = new Insurance();
			this.insurance.setMachines(new Machines());
			this.machines = new Machines();
			this.machines.setMachineTypes(new MachineTypes());
			this.machineTypes = new MachineTypes();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesInsurance";
	}

	/**
	 * Fills the insurancesList with the existing insurances.
	 * 
	 */
	public void searchInsurances() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle insuranceBundle = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		insurancesList = new ArrayList<Insurance>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(queryBuilder, parameters, bundle,
					jointSearchMessages);
			Long amount = insuranceDao.insurancesAmount(queryBuilder,
					parameters);
			if (amount != null) {
				paginador.paginar(amount);
			}
			insurancesList = insuranceDao.searchInsurances(
					paginador.getInicio(), paginador.getRango(), queryBuilder,
					parameters);
			if ((insurancesList == null || insurancesList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (insurancesList == null || insurancesList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								insuranceBundle.getString("insurance_label"),
								jointSearchMessages);
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method builds an advanced search for a query and it also displays
	 * messages depending on the search criteria selected by the user.
	 * 
	 * @param query
	 *            : Query to concatenate
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * @param jointSearchMessages
	 *            : Search message.
	 * 
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		SimpleDateFormat format = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		int machineId = machines.getIdMachine();
		if (this.initDaySearch != null && this.lastDaySearch != null) {
			query.append("WHERE ins.dateTime BETWEEN :fechaInicioBuscar AND :fechaFinBuscar ");
			query.append("AND m.idMachine =:idMachine ");
			SelectItem item = new SelectItem(initDaySearch, "fechaInicioBuscar");
			parameters.add(item);
			SelectItem item2 = new SelectItem(lastDaySearch, "fechaFinBuscar");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_fecha_inicio") + ": "
					+ '"' + format.format(this.initDaySearch) + '"' + ", ";
			jointSearchMessages.append(dateFrom);

			String dateTo = bundle.getString("label_fecha_finalizacion") + ": "
					+ '"' + format.format(lastDaySearch) + '"';
			jointSearchMessages.append(dateTo);
			parameters.add(item2);

		} else {
			query.append("WHERE m.idMachine =:idMachine ");
		}
		SelectItem item3 = new SelectItem(machineId, "idMachine");
		parameters.add(item3);
	}

	/**
	 * Method to edit or create an insurance.
	 * 
	 * @modify 11/03/2016 Sergio.Gelves
	 * 
	 * @param insurance
	 *            : Object of insurance to add or edit.
	 * 
	 * @return regInsurance: Redirects to the register insurance template.
	 * 
	 */
	public String addEditInsurance(Insurance insurance) {
		try {
			loadMachineTypeCombos();
			if (insurance != null) {
				this.insurance = insurance;
				this.insurance.setMachines(machines);
			} else {
				this.insurance = new Insurance();
				this.insurance.setMachines(new Machines());
				this.insurance.setDateTime(new Date());
				this.machines = new Machines();
				this.machines.setMachineTypes(new MachineTypes());
				this.machineTypes = new MachineTypes();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regInsurance";
	}

	/**
	 * Method that loads a MachineTypes list.
	 * 
	 * @throws Exception
	 */
	private void loadMachineTypeCombos() throws Exception {
		machineTypeOption = new ArrayList<SelectItem>();
		List<MachineTypes> result = machineTypesDao.listMachineType();
		if (result != null) {
			for (MachineTypes machinesType : result) {
				machineTypeOption.add(new SelectItem(machinesType
						.getIdMachineType(), machinesType.getName()));
			}
		}
		loadMachines();
	}

	/**
	 * Method used to save or edit insurance.
	 * 
	 * @return initializeSearch: Redirects to manage the insurance with an
	 *         updated list of insurances.
	 */
	public String saveInsurance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		try {
			this.insurance.setMachines(machines);
			this.insurance.getMachines().setMachineTypes(machineTypes);
			if (this.insurance.getIdInsurance() != 0) {
				insuranceDao.editInsurance(this.insurance);
			} else {
				registerMessage = "message_registro_guardar";
				insuranceDao.saveInsurance(this.insurance);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage),
					this.insurance.getDescripcion()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeSearch();
	}

	/**
	 * Redirects to manage the insurance with an updated list of insurances.
	 * 
	 **/
	public void loadMachines() {
		int machineId = 0;
		try {

			machinesList = new ArrayList<SelectItem>();
			if (this.insurance != null && machineTypes == null) {
				machineId = this.insurance.getMachines().getIdMachine();
				this.machines = machinesDao.machinesXId(machineId);
				machinesList.add(new SelectItem(machines.getIdMachine(),
						machines.getName()));

				machineTypes = machineTypesDao.machineTypeXMachine(machineId);
				machines.setMachineTypes(machineTypesResult);
			} else {
				if (machineTypes != null) {
					machineId = machineTypes.getIdMachineType();
				}
				List<Machines> result = machinesDao
						.listMachinesByTypes(machineId);
				if (result != null) {
					for (Machines machine : result) {
						machinesList.add(new SelectItem(machine.getIdMachine(),
								machine.getName()));
					}
				}
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method that deletes an insurance of the database.
	 * 
	 * 
	 * @return initializeSearch: Redirects to manage the insurance with an
	 *         updated list of insurances.
	 */
	public String deleteInsurance() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			insuranceDao.deleteInsurance(insurance);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					this.insurance.getDescripcion()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					insurance.getDateTime());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return initializeSearch();
	}
}
