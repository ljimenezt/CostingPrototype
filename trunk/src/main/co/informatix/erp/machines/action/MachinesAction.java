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

import co.informatix.erp.costs.action.ActivitiesAndMachineAction;
import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.costs.entities.ActivityMachine;
import co.informatix.erp.machines.dao.FuelTypesDao;
import co.informatix.erp.machines.dao.MachineTypesDao;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.entities.FuelTypes;
import co.informatix.erp.machines.entities.MachineTypes;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControllerAccounting;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class allows the logic of the machines may be in the DB The logic is to
 * consult, edit or add machines.
 * 
 * @author Sergio.Ortiz
 * @modify 16/10/2015 Andres.Gomez
 * @modify 13/03/2017 Patricia.Patinio
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
	private ActivitiesAndMachineAction activitiesAndMachineAction;
	private Paginador pagination = new Paginador();

	private String nameSearch;
	private String serialSearch;

	private int nameMachines;
	private boolean state;
	private boolean stateActiviy;
	private boolean stateDiesel;

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
	public ActivitiesAndMachineAction getActivitiesAndMachineAction() {
		return activitiesAndMachineAction;
	}

	/**
	 * @param scheduledActivitiesAction
	 *            : scheduledActivitiesAction object.
	 */
	public void setActivitiesAndMachineAction(
			ActivitiesAndMachineAction activitiesAndMachineAction) {
		this.activitiesAndMachineAction = activitiesAndMachineAction;
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
	 * @return pagination: Management paged list of names of machines.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of names of machines.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
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
	 * @return stateDiesel: modifies the logic of the method consult machines
	 *         for consult diesel machines.
	 */
	public boolean isStateDiesel() {
		return stateDiesel;
	}

	/**
	 * @param stateDiesel
	 *            : modifies the logic of the method consult machines for
	 *            consult diesel machines.
	 */
	public void setStateDiesel(boolean stateDiesel) {
		this.stateDiesel = stateDiesel;
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
	 * @return consultMachines(): Returns to the template management.
	 */
	public String searchInitialization() {
		nameSearch = "";
		serialSearch = "";
		setState(false);
		this.pagination = new Paginador();
		this.nameMachines = 0;
		return consultMachines();
	}

	/**
	 * This method load all types of machines from a list.
	 * 
	 * @modify 15/11/2016 Wilhelm.Boada
	 */
	public void loadMachineTypes() {
		try {
			itemsMachinesType = new ArrayList<SelectItem>();
			List<MachineTypes> listMachinetypes;
			listMachinetypes = machineTypesDao.listMachineType();
			if (listMachinetypes != null) {
				for (MachineTypes machineTypes : listMachinetypes) {
					itemsMachinesType.add(new SelectItem(machineTypes
							.getIdMachineType(), machineTypes.getName()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
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
	 * @modify 15/11/2016 Wilhelm.Boada
	 */
	public void initializeMachines() {
		setState(true);
		stateActiviy = true;
		stateDiesel = false;
		this.nameSearch = "";
		this.pagination.setOpcion('f');
		consultMachines();
	}

	/**
	 * Consult initialized machines considering a state that allows changing the
	 * logic to check.
	 * 
	 * @author Wilhelm.Boada
	 */
	public void initializeMachinesInMaintenance() {
		state = true;
		stateActiviy = false;
		stateDiesel = false;
		this.nameMachines = 0;
		this.nameSearch = "";
		consultMachines();
	}

	/**
	 * Consult initialized machines considering a state that allows changing the
	 * logic to check.
	 * 
	 * @author Patricia.Patinio
	 */
	public void initializeMachinesDiesel() {
		state = true;
		stateActiviy = false;
		stateDiesel = true;
		this.nameMachines = 0;
		this.nameSearch = "";
		consultMachines();
	}

	/**
	 * Consult the list of the machines.
	 * 
	 * @modify 28/05/2015 Mabell.Boada
	 * @modify 19/08/2015 Andres.Gomez
	 * @modify 11/11/2015 Gerardo.Herrera
	 * @modify 15/11/2016 Wilhelm.Boada
	 * 
	 * @return "gesMachines": Redirects to the template to manage machines.
	 */
	public String consultMachines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachineType = ControladorContexto
				.getBundle("messageMachine");
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
				advancedSearchActivityMachine(query, parameters, bundle,
						unionMessagesSearch);
			} else {
				advancedSearch(query, parameters, bundle, unionMessagesSearch);
			}
			Long quantity = machinesDao.quantityMachines(query, parameters);
			if (quantity != null) {
				if (this.state) {
					pagination.paginarRangoDefinido(quantity, 5);
					pagination.setOpcion('f');
				} else {
					pagination.paginar(quantity);
					pagination.setOpcion('f');
				}
			}
			this.listMachines = machinesDao.consultMachines(
					pagination.getInicio(), pagination.getRango(), query,
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
			if (this.state && this.stateActiviy) {
				persistMachines();
			}
			validations.setMensajeBusqueda(messageSearch);
			validations.setMensajeBusquedaPopUp(messageSearch);
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
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(m.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_model") + ": "
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
	 * @modify 15/11/2016 Wilhelm.Boada
	 * @modify 13/03/2017 Patricia.Patinio
	 * 
	 * @param query
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearchActivityMachine(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		Activities selectedActivity = new Activities();
		boolean seleccion = false;
		if (ControladorContexto.getFacesContext() != null && this.stateActiviy) {
			this.activitiesAndMachineAction = ControladorContexto
					.getContextBean(ActivitiesAndMachineAction.class);
			selectedActivity = activitiesAndMachineAction.getSelectedActivity();
		}
		if (this.nameMachines != 0) {
			query.append("WHERE mt.idMachineType = :idMachineType ");
			SelectItem itemMachineType = new SelectItem(this.nameMachines,
					"idMachineType");
			parameters.add(itemMachineType);
			seleccion = true;
		}
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append(seleccion ? "AND " : "WHERE ");
			query.append("UPPER(m.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_model") + ": "
					+ '"' + this.nameSearch + '"');
			seleccion = true;
		}
		if (this.stateActiviy) {
			query.append(seleccion ? "AND " : "WHERE ");
			query.append("m NOT IN ");
			query.append("(SELECT ma FROM ActivityMachine am ");
			query.append("JOIN am.activityMachinePK.machines ma ");
			query.append("WHERE am.initialDateTime BETWEEN :itemInitialdate AND :itemFinalDate ");
			query.append("OR am.finalDateTime BETWEEN :itemInitialdate AND :itemFinalDate) ");
			SelectItem itemInitialDate = new SelectItem(
					selectedActivity.getInitialDtBudget(), "itemInitialdate");
			SelectItem itemFinalDate = new SelectItem(
					selectedActivity.getFinalDtBudget(), "itemFinalDate");
			parameters.add(itemInitialDate);
			parameters.add(itemFinalDate);
		}
		if (this.stateDiesel) {
			query.append(seleccion ? "AND " : "WHERE ");
			query.append(" m.fuel = :diesel ");
			SelectItem dieselItem = new SelectItem(true, "diesel");
			parameters.add(dieselItem);
		}
	}

	/**
	 * This method fills the various objects associated with a machines.
	 * 
	 * @throws Exception
	 */
	private void loadDetailsMachines() throws Exception {
		if (this.listMachines != null) {
			for (Machines machine : this.listMachines) {
				int idMachine = machine.getIdMachine();
				FuelTypes fuelTypes = (FuelTypes) this.machinesDao
						.consultObjectMachines("fuelTypes", idMachine);
				machine.setFuelTypes(fuelTypes);
			}
		}
	}

	/**
	 * Method to edit or create a new machine.
	 * 
	 * @param machines
	 *            :machine that you are adding or editing.
	 * @return "regMachines":redirected to the template record machine.
	 */
	public String addEditMachines(Machines machines) {
		try {
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
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
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
	 * This method allows calculate the depreciation of the machine.
	 * 
	 * @author Andres.Gomez
	 * @modify 15/03/2017 Fabian.Diaz
	 */
	public void calculateDepreciation() {
		double yearLife = machines.getLifeYears();
		double residual = machines.getResidualValue();
		double investment = machines.getInvestment();

		if (investment > 0 && yearLife > 0) {
			double depreciation = ControllerAccounting.divide(
					ControllerAccounting.subtract(investment, residual),
					yearLife);
			machines.setDepreciation(depreciation);
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
			this.activitiesAndMachineAction = ControladorContexto
					.getContextBean(ActivitiesAndMachineAction.class);
		}
		if (this.listMachines != null) {
			for (Machines machine : this.listMachines) {
				for (ActivityMachine machineSelected : activitiesAndMachineAction
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