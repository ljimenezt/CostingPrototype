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
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ReportsController;
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

	private Date startDateSearch;
	private Date endDateSearch;

	private Paginador pagination = new Paginador();

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
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultIrrigationDetails: Consulting a list of irrigarion details
	 *         and redirects to the template to manage irrigarion details.
	 */
	public String searchInitialization() {
		this.startDateSearch = null;
		this.endDateSearch = null;
		return consultIrrigationDetails();
	}

	/**
	 * Consult the list of Irrigation Details
	 * 
	 * @return gesIrrigationDetails: Navigation rule that redirects to manage
	 *         Irrigation Details
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
	 * This method allow consult the irrigation engine information and generate
	 * the report.
	 * 
	 * @author Luna.Granados
	 */
	public void generateReportIrrigationEngine() {
		ReportsController reportsController = ControladorContexto
				.getContextBean(ReportsController.class);
		StringBuilder consult = new StringBuilder();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		try {
			reportAdvanceSearch(consult, parameters);

			List<Object[]> listControl = irrigationDetailsDao
					.consultIrrigationEngineReport(consult, parameters);

			reportsController.generateReportIrrigationEngine(listControl);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow build the query by date to consult the information for
	 * the report.
	 * 
	 * @author Luna.Granados
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 */
	private void reportAdvanceSearch(StringBuilder consult,
			List<SelectItem> parameters) {
		if (this.startDateSearch != null && this.endDateSearch != null) {
			consult.append("AND el.date BETWEEN :startDateSearch AND :endDateSearch ");

			SelectItem item = new SelectItem(startDateSearch, "startDateSearch");
			parameters.add(item);

			SelectItem item2 = new SelectItem(endDateSearch, "endDateSearch");
			parameters.add(item2);
		}
	}

}