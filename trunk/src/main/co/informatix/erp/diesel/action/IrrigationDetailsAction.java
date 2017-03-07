package co.informatix.erp.diesel.action;

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

import co.informatix.erp.diesel.dao.IrrigationDetailsDao;
import co.informatix.erp.diesel.entities.IrrigationDetails;
import co.informatix.erp.machines.dao.MachinesDao;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is used to create, update, and query of an irrigation details in
 * the information system.
 * 
 * @author Liseth.Jimenez
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class IrrigationDetailsAction implements Serializable {

	private List<IrrigationDetails> irrigationDetailsList;
	private List<Machines> machineList;

	private String nameMachineSearch;
	private Date startDateSearch;
	private Date endDateSearch;

	private Paginador pagination = new Paginador();
	private Paginador paginationForm = new Paginador();

	@EJB
	private IrrigationDetailsDao irrigationDetailsDao;
	@EJB
	private MachinesDao machinesDao;

	/**
	 * @return irrigationDetailsList: list of irrigation details stored in data
	 *         base.
	 */
	public List<IrrigationDetails> getIrrigationDetailsList() {
		return irrigationDetailsList;
	}

	/**
	 * @param irrigationDetailsList
	 *            : list of irrigation details stored in data base.
	 */
	public void setIrrigationDetailsList(
			List<IrrigationDetails> irrigationDetailsList) {
		this.irrigationDetailsList = irrigationDetailsList;
	}

	/**
	 * @return machineList: machine list for associated a machine to an
	 *         irrigation details.
	 */
	public List<Machines> getMachineList() {
		return machineList;
	}

	/**
	 * @param machineList
	 *            : machine list for associated a machine to an irrigation
	 *            details.
	 */
	public void setMachineList(List<Machines> machineList) {
		this.machineList = machineList;
	}

	/**
	 * @return nameMachineSearch: machine name to search.
	 */
	public String getNameMachineSearch() {
		return nameMachineSearch;
	}

	/**
	 * @param nameMachineSearch
	 *            : machine name to search.
	 */
	public void setNameMachineSearch(String nameMachineSearch) {
		this.nameMachineSearch = nameMachineSearch;
	}

	/**
	 * @return startDateSearch: gets the initial search range for the irrigation
	 *         details in the system.
	 */
	public Date getStartDateSearch() {
		return startDateSearch;
	}

	/**
	 * @param startDateSearch
	 *            : sets the initial search range for the irrigation details in
	 *            the system.
	 */
	public void setStartDateSearch(Date startDateSearch) {
		this.startDateSearch = startDateSearch;
	}

	/**
	 * @return endDateSearch: gets the end range to search the irrigation
	 *         details in the system.
	 */
	public Date getEndDateSearch() {
		return endDateSearch;
	}

	/**
	 * @param endDateSearch
	 *            :sets the end range to search the irrigation details in the
	 *            system.
	 */
	public void setEndDateSearch(Date endDateSearch) {
		this.endDateSearch = endDateSearch;
	}

	/**
	 * @return pagination: the paging controller object.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : the paging controller object.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return paginationForm: the paging controller object for machine list.
	 */
	public Paginador getPaginationForm() {
		return paginationForm;
	}

	/**
	 * @param paginationForm
	 *            : the paging controller object for machine list.
	 */
	public void setPaginationForm(Paginador paginationForm) {
		this.paginationForm = paginationForm;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultFuelUsage: consultFuelUsage consulting method and
	 *         redirects to the template to manage fuel usage.
	 */
	public String searchInitialization() {
		this.startDateSearch = null;
		this.endDateSearch = null;
		return consultIrrigationDetails();
	}

	/**
	 * Method to initialize the fields in the search on machine popup.
	 */
	public void initializeSearchMachine() {
		this.nameMachineSearch = "";
		searchMachines();
	}

	/**
	 * Consult the list of fuel usage
	 * 
	 * @return gesIrrigationDetails: Navigation rule that redirects to manage
	 *         Irrigation Details usage
	 */
	public String consultIrrigationDetails() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleDiesel = ControladorContexto
				.getBundle("messageDiesel");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long amount = irrigationDetailsDao.quantityIrrigationDetails(query,
					parameters);
			if (amount != null) {
				pagination.paginar(amount);
			}

			irrigationDetailsList = irrigationDetailsDao
					.consultIrrigationDetails(pagination.getInicio(),
							pagination.getRango(), query, parameters);

			if ((irrigationDetailsList == null || irrigationDetailsList.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (irrigationDetailsList == null
					|| irrigationDetailsList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleDiesel
										.getString("irrigation_details_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return "gesIrrigationDetails";
	}

	/**
	 * This method builds a query with advanced search; it also render the
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param unionMessagesSearch
	 *            : Message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) throws Exception {
		SimpleDateFormat formats = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		boolean addFilter = false;
		if (this.startDateSearch != null && this.endDateSearch != null) {
			consult.append(addFilter ? "AND " : "WHERE ");
			addFilter = true;
			consult.append("id.engineLog.date BETWEEN :startDateSearch AND :endDateSearch ");
			SelectItem item = new SelectItem(startDateSearch, "startDateSearch");
			parameters.add(item);
			SelectItem item2 = new SelectItem(endDateSearch, "endDateSearch");
			parameters.add(item2);
			String dateFrom = bundle.getString("label_start_date") + ": " + '"'
					+ formats.format(this.startDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateFrom);
			String dateTo = bundle.getString("label_end_date") + ": " + '"'
					+ formats.format(endDateSearch) + '"' + " ";
			unionMessagesSearch.append(dateTo);
		}
	}

	/**
	 * Method for get a machine list for user associate a machine object to
	 * irrigation details.
	 * 
	 * @author Patricia.Patinio
	 */
	public void searchMachines() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleMachine = ControladorContexto
				.getBundle("messageMachine");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.setMachineList(new ArrayList<Machines>());
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder joinSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearchMachine(queryBuilder, parameters, bundle,
					bundleMachine, joinSearchMessages);
			Long amount = machinesDao
					.quantityMachines(queryBuilder, parameters);
			if (amount != null) {
				paginationForm.paginarRangoDefinido(amount, 5);
				this.machineList = machinesDao.consultMachines(
						paginationForm.getInicio(), paginationForm.getRango(),
						queryBuilder, parameters);
			}
			if ((machineList == null || machineList.size() <= 0)
					&& !"".equals(joinSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								joinSearchMessages);
			} else if (machineList == null || machineList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(joinSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessage = MessageFormat.format(message,
						bundleMachine.getString("machines_label_names"),
						joinSearchMessages);
			}
			validations.setMensajeBusquedaPopUp(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method builds a query with advanced search of machines; it also
	 * render the messages to be displayed depending on the search criteria
	 * selected by the user.
	 * 
	 * @author Patricia.Patinio
	 * 
	 * @param consult
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param bundleMachine
	 *            : Access machine language tags.
	 * @param joinSearchMessages
	 *            : Message search.
	 */
	private void advancedSearchMachine(StringBuilder query,
			List<SelectItem> parameter, ResourceBundle bundle,
			ResourceBundle bundleMachine, StringBuilder joinSearchMessages) {

		query.append(" WHERE m.fuel = :diesel ");
		SelectItem dieselItem = new SelectItem(true, "diesel");
		parameter.add(dieselItem);

		if ((this.nameMachineSearch != null && !""
				.equals(this.nameMachineSearch))) {
			query.append(" AND UPPER(m.name) LIKE UPPER(:keywordNombre) ");
			SelectItem nameItem = new SelectItem("%" + this.nameMachineSearch
					+ "%", "keywordNombre");
			parameter.add(nameItem);
			joinSearchMessages.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameMachineSearch + '"' + " ");
		}
	}

}