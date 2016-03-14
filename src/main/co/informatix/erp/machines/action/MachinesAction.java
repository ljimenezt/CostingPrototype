package co.informatix.erp.machines.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.lifeCycle.action.ScheduledActivitiesAction;
import co.informatix.erp.machines.dao.FuelTypesDao;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.entities.FuelTypes;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the machines may be in the DB The logic is to
 * consult, edit or add machines.
 * 
 * @author Sergio.Ortiz
 * @modify 16/10/2015 Andres.Gomez
 **/
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MachinesAction implements Serializable {

	@EJB
	private MachinesDao machinesDao;
	@EJB
	private MachineTypesDao machineTypesDao;
	@EJB
	private FuelTypesDao fuelTypesDao;

	private Machines machines;
	private ScheduledActivitiesAction scheduledActivitiesAction;

	private Paginador paginador = new Paginador();
	private String nameSearch;
	private String serialSearch;

	private int nameMachines;
	private boolean state;

	private List<Machines> listMachines;
	private ArrayList<SelectItem> itemsMachinesType;
	private ArrayList<SelectItem> itemsFuelTypes;

	/**
	 * @return machines: object containing data machine.
	 */
	public Machines getMachines() {
		return machines;
	}

	/**
	 * @param machines
	 *            : object containing data machine.
	 */
	public void setMachines(Machines machines) {
		this.machines = machines;
	}

	/**
	 * @return scheduledActivitiesAction: scheduledActivitiesAction object.
	 */
	public ScheduledActivitiesAction getScheduledActivitiesAction() {
		return scheduledActivitiesAction;
	}

	/**
	 * @param scheduledActivitiesAction
	 *            : scheduledActivitiesAction object.
	 */
	public void setScheduledActivitiesAction(
			ScheduledActivitiesAction scheduledActivitiesAction) {
		this.scheduledActivitiesAction = scheduledActivitiesAction;
	}

	/**
	 * @return listMachines: list of objects of type machine.
	 */
	public List<Machines> getListMachines() {
		return listMachines;
	}

	/**
	 * @param listMachines
	 *            : list of objects of type machine.
	 */
	public void setListMachines(List<Machines> listMachines) {
		this.listMachines = listMachines;
	}

	/**
	 * @return paginador: Management paged list of names of machines.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            : Management paged list of names of machines.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * @return nameSearch: machine name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : machine name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return serialSearch: serial number of the machine to search.
	 */
	public String getSerialSearch() {
		return serialSearch;
	}

	/**
	 * @param serialSearch
	 *            :serial number of the machine to search.
	 */
	public void setSerialSearch(String serialSearch) {
		this.serialSearch = serialSearch;
	}

	/**
	 * @return nameMachines: id name of the machine to look for.
	 */
	public int getNameMachines() {
		return nameMachines;
	}

	/**
	 * @param nameMachines
	 *            :id name of the machine to look for.
	 */
	public void setNameMachines(int nameMachines) {
		this.nameMachines = nameMachines;
	}

	/**
	 * @return state: modifies the logic of the method consult machines.
	 */
	public boolean isState() {
		return state;
	}

	/**
	 * @param state
	 *            : modifies the logic of the method consult machines.
	 */
	public void setState(boolean state) {
		this.state = state;
	}

	/**
	 * @return itemsMachinesType: list of the types of machine.
	 */
	public ArrayList<SelectItem> getItemsMachinesType() {
		return itemsMachinesType;
	}

	/**
	 * @param itemsMachinesType
	 *            : list of the types of machine.
	 */
	public void setItemsMachinesType(ArrayList<SelectItem> itemsMachinesType) {
		this.itemsMachinesType = itemsMachinesType;
	}

	/**
	 * @return itemsFuelTypes :list of the types of fuel.
	 */
	public ArrayList<SelectItem> getItemsFuelTypes() {
		return itemsFuelTypes;
	}

	/**
	 * @param itemsFuelTypes
	 *            :list of types of fuel.
	 */
	public void setItemsFuelTypes(ArrayList<SelectItem> itemsFuelTypes) {
		this.itemsFuelTypes = itemsFuelTypes;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of machines.
	 * 
	 * @modify 28/05/2015 Mabell.Boada
	 * @modify 19/08/2015 Andres.Gomez
	 * 
	 * @return gesMachines: Returns to the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		serialSearch = "";
		setState(false);
		this.paginador = new Paginador();
		this.nameMachines = 0;
		return consultMachines();

	}

	/**
	 * This method load all types of machines from a list.
	 * 
	 * @throws Exception
	 */
	private void loadMachineTypes() throws Exception {
		itemsMachinesType = new ArrayList<SelectItem>();
		List<MachineTypes> listMachinetypes;
		listMachinetypes = machineTypesDao.listMachineType();
		if (listMachinetypes != null) {
			for (MachineTypes machineTypes : listMachinetypes) {
				itemsMachinesType.add(new SelectItem(machineTypes
						.getIdMachineType(), machineTypes.getName()));
			}
		}

	}

	/**
	 * This method allow load all type of fuel from a list.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @throws Exception
	 */
	private void fillFuelTypes() throws Exception {
		itemsFuelTypes = new ArrayList<SelectItem>();
		List<FuelTypes> listFuelTypes = fuelTypesDao.listFuelType();
		if (listFuelTypes != null) {
			for (FuelTypes fueltypes : listFuelTypes) {
				itemsFuelTypes.add(new SelectItem(fueltypes.getIdFuelType(),
						fueltypes.getName()));
			}
		}
	}

	/**
	 * Consult initialized machines considering a state that allows changing the
	 * logic to check.
	 * 
	 * @author Gerardo.Herrera
	 */
	public void initializeMachines() {
		setState(true);
		this.paginador.setOpcion('f');
		consultMachines();
	}

	/**
	 * Consult the list of the machines.
	 * 
	 * @modify 28/05/2015 Mabell.Boada
	 * @modify 19/08/2015 Andres.Gomez
	 * @modify 11/11/2015 Gerardo.Herrera
	 * 
	 * @return "gesMachines": Redirects to the template to manage machines.
	 */
	public String consultMachines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("mensajeMachine");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listMachines = new ArrayList<Machines>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		String result = this.state ? "" : "gesMachines";
		try {
			if (this.state) {
				advancedSearchActivityMachine(query, parameters);
			} else {
				advancedSearch(query, parameters, bundle, unionMessagesSearch);
			}
			Long quantity = machinesDao.quantityMachines(query, parameters);
			if (quantity != null) {
				if (this.state) {
					paginador.paginarRangoDefinido(quantity, 5);
				} else {
					paginador.paginar(quantity);
				}
			}
			this.listMachines = machinesDao.consultMachines(
					paginador.getInicio(), paginador.getRango(), query,
					parameters);
			if (!this.state) {
				loadMachineTypes();
				fillFuelTypes();
			}
			if ((listMachines == null || listMachines.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listMachines == null || listMachines.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleMachineType
										.getString("machines_label_names_s"),
								unionMessagesSearch);
			}
			if (quantity != 0 && !this.state) {
				loadDetailsMachines();
			}
			if (this.state) {
				persistMachines();
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return result;
	}

	/**
	 * This method build consultation for advanced search build also allows
	 * messages to be displayed depending on the search criteria selected by the
	 * user (search by name).
	 * 
	 * @modify 19/08/2015 Andres.Gomez
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
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(m.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_modelo") + ": "
					+ '"' + this.nameSearch + '"');
			if (this.nameMachines != 0) {
				consult.append("AND mt.idMachineType = :keyword1 ");
				item = new SelectItem(this.nameMachines, "keyword1");
				parameters.add(item);
			}
			if (this.serialSearch != null && !"".equals(this.serialSearch)) {
				consult.append("AND UPPER(m.serialNumber) LIKE UPPER (:keyword2) ");
				item = new SelectItem(this.serialSearch, "keyword2");
				parameters.add(item);
			}
		} else {
			if (this.nameMachines != 0) {
				consult.append("WHERE mt.idMachineType = :keyword1 ");
				SelectItem item = new SelectItem(this.nameMachines, "keyword1");
				parameters.add(item);
				if (this.serialSearch != null && !"".equals(this.serialSearch)) {
					consult.append("AND UPPER(m.serialNumber) LIKE UPPER (:keyword2) ");
					item = new SelectItem(this.serialSearch, "keyword2");
					parameters.add(item);
				}
			} else {
				if (this.serialSearch != null && !"".equals(this.serialSearch)) {
					consult.append("WHERE UPPER(m.serialNumber) LIKE UPPER (:keyword2) ");
					SelectItem item = new SelectItem(this.serialSearch,
							"keyword2");
					parameters.add(item);
				}
			}
		}
	}

	/**
	 * This method build consultation for advanced search of machine for
	 * activity - machine relation build also allows messages to be displayed
	 * depending on the search criteria selected by the user (search by name).
	 * 
	 * @author Gerardo.Herrera
	 * 
	 * @param query
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 */
	private void advancedSearchActivityMachine(StringBuilder query,
			List<SelectItem> parameters) {
		Activities selectedActivity = new Activities();
		boolean seleccion = false;
		if (ControladorContexto.getFacesContext() != null) {
			this.scheduledActivitiesAction = ControladorContexto
					.getContextBean(ScheduledActivitiesAction.class);
			selectedActivity = scheduledActivitiesAction.getSelectedActivity();
		}
		if (this.nameMachines != 0) {
			query.append("WHERE mt.idMachineType = :idMachineType ");
			seleccion = true;
		}
		query.append(seleccion ? "AND " : "WHERE ");
		query.append("m NOT IN ");
		query.append("(SELECT ma FROM ActivityMachine am ");
		query.append("JOIN am.activityMachinePK.machines ma ");
		query.append("WHERE am.initialDateTime BETWEEN :itemInitialdate AND :itemFinalDate ");
		query.append("OR am.finalDateTime BETWEEN :itemInitialdate AND :itemFinalDate) ");
		if (this.nameMachines != 0) {
			SelectItem itemMachineType = new SelectItem(this.nameMachines,
					"idMachineType");
			parameters.add(itemMachineType);
		}
		SelectItem itemInitialDate = new SelectItem(
				selectedActivity.getInitialDtBudget(), "itemInitialdate");
		SelectItem itemFinalDate = new SelectItem(
				selectedActivity.getFinalDtBudget(), "itemFinalDate");
		parameters.add(itemInitialDate);
		parameters.add(itemFinalDate);
	}

	/**
	 * This method fills the various objects associated with a machines.
	 * 
	 * @throws Exception
	 */
	private void loadDetailsMachines() throws Exception {
		List<Machines> machines = new ArrayList<Machines>();
		machines.addAll(this.listMachines);
		this.listMachines = new ArrayList<Machines>();
		for (Machines machine : machines) {
			int idMachine = machine.getIdMachine();
			FuelTypes fuelTypes = (FuelTypes) this.machinesDao
					.consultObjectMachines("fuelTypes", idMachine);
			machine.setFuelTypes(fuelTypes);
			this.listMachines.add(machine);
		}
	}

	/**
	 * Method to edit or create a new machine.
	 * 
	 * @param machines
	 *            :machine that you are adding or editing.
	 * 
	 * @return "regMachines":redirected to the template record machine.
	 */
	public String addEditMachines(Machines machines) throws Exception {
		loadMachineTypes();
		fillFuelTypes();
		if (machines != null) {
			this.machines = machines;
			if (machines.getFuelTypes() == null) {
				this.machines.setFuelTypes(new FuelTypes());
			}
		} else {
			this.machines = new Machines();
			this.machines.setMachineTypes(new MachineTypes());
			this.machines.setFuelTypes(new FuelTypes());
		}
		return "regMachines";
	}

	/**
	 * Method used to save or edit machines.
	 * 
	 * @return consultMachines: Redirects to manage the list of machines with
	 *         machines updated.
	 */
	public String saveMachines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";

		try {
			if (machines.getFuelTypes().getIdFuelType() == 0) {
				machines.setFuelTypes(null);
			}
			if (machines.getSerialNumber().isEmpty()) {
				machines.setSerialNumber(null);
			}
			if (machines.getIdMachine() != 0) {
				machinesDao.editMachines(machines);
			} else {
				messageLog = "message_registro_guardar";
				machinesDao.saveMachines(machines);
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(messageLog),
							machines.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMachines();
	}

	/**
	 * Delete method that allows a machine to database.
	 * 
	 * 
	 * @return consultMachines: Consult the list of machines and returns to
	 *         manage machines.
	 */
	public String removeMachines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			machinesDao.removeMachines(machines);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					machines.getIdMachine()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					machines.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultMachines();
	}

	/**
	 * This method allows calculate the depreciation of the machine and
	 * validates required fields are greater than zero.
	 * 
	 * @author Andres.Gomez
	 */
	public void calculateDepreciation() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		double yearLife = machines.getLifeYears();
		double residual = machines.getResidualValue();
		double investment = machines.getInvestment();
		try {
			validateFields();
			if (yearLife <= 0) {
				ControladorContexto.mensajeError(null,
						"formMachines:txtTiemVida",
						bundle.getString("message_campo_mayo_cero"));
			}
			if (investment <= 0) {
				ControladorContexto.mensajeError(null,
						"formMachines:txtinversion",
						bundle.getString("message_campo_mayo_cero"));
			}
			if (investment <= residual && investment > 0) {
				ControladorContexto.mensajeError(null,
						"formMachines:txtResVal",
						bundle.getString("message_field_lower_to") + " "
								+ investment);
			}
			if (investment > 0 && yearLife > 0) {
				double depreciation = ((investment - residual) / yearLife);
				machines.setDepreciation(depreciation);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates fields that are required in view of registering a new machine.
	 */
	public void validateFields() {
		if (this.machines.getMachineTypes() == null
				|| this.machines.getMachineTypes().getIdMachineType() == 0) {
			ControladorContexto.mensajeRequeridos("formMachines:tipoMaquina");
		}
		if (this.machines.getName() == null
				|| "".equals(this.machines.getName())) {
			ControladorContexto.mensajeRequeridos("formMachines:txtName");
		}
		if (this.machines.getPurchaseDate() == null) {
			ControladorContexto.mensajeRequeridos("formMachines:fechcompra");
		}
		String clientId = "formMachines:txtSerialNumber";
		if (this.machines.getSerialNumber() == null
				|| "".equals(this.machines.getSerialNumber())) {
			ControladorContexto.mensajeRequeridos(clientId);
		} else {
			validateNameXSS(clientId, this.machines.getSerialNumber());
		}
		if (this.machines.isFuel()) {
			if (this.machines.getFuelTypes().getIdFuelType() == 0) {
				ControladorContexto.mensajeRequeridos("formMachines:fuelTypes");
			}
			if (this.machines.getFuelConsumption() <= 0) {
				ControladorContexto
						.mensajeRequeridos("formMachines:txtFuelConsumption");
			}
		}
	}

	/**
	 * method to validate the concept and name of the partner company that is
	 * not repeated in the database and validates against XSS.
	 * 
	 * @param clientId
	 *            : context application.
	 * @param value
	 *            : value to validate.
	 */
	public void validateNameXSS(String clientId, String value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			int id = machines.getIdMachine();
			Machines machinesAux = machinesDao.machineExist(value, id);
			if (machinesAux != null) {
				ControladorContexto.mensajeError(null, clientId,
						bundle.getString("message_ya_existe_verifique"));
			}
			if (!EncodeFilter.validarXSS(value, clientId,
					"locate.regex.letras.numeros")) {
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It is responsible for maintaining selected machines regardless of whether
	 * this machines run the search again.
	 * 
	 * @author Cristhian.Pico
	 */
	private void persistMachines() {
		if (ControladorContexto.getFacesContext() != null) {
			this.scheduledActivitiesAction = ControladorContexto
					.getContextBean(ScheduledActivitiesAction.class);
		}
		if (this.listMachines != null) {
			for (Machines machine : this.listMachines) {
				for (ActivityMachine machineSelected : scheduledActivitiesAction
						.getListActivityMachineTemp()) {
					int idMachineSelected = machineSelected
							.getActivityMachinePK().getMachines()
							.getIdMachine();
					if (machine.getIdMachine() == idMachineSelected)
						machine.setSelection(true);
				}
			}
		}
	}
}