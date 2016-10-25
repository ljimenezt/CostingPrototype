package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.AssistControlDao;
import co.informatix.erp.humanResources.entities.AssistControl;
import co.informatix.erp.humanResources.entities.AssistControlPojo;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the attendance that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class AssistControlAction implements Serializable {

	private Paginador pagination;
	private AssistControl assistControl;
	private AssistControlPojo assistControlPojo;
	private Date initialDateSearch;
	private Date finalDateSearch;
	private List<AssistControl> assistControlList;
	private List<AssistControlPojo> assistControlPojoList;
	private List<AssistControlPojo> assistControlPojoSubList;

	@EJB
	private AssistControlDao assistControlDao;

	/**
	 * @return pagination: Management paged list of attendance.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of attendance.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return assistControl: Object of class AssistControl for register o edit.
	 */
	public AssistControl getAssistControl() {
		return assistControl;
	}

	/**
	 * @param assistControl
	 *            : Object of class AssistControl for register o edit.
	 */
	public void setAssistControl(AssistControl assistControl) {
		this.assistControl = assistControl;
	}

	/**
	 * @return assistControlPojo: Object having information of attendance taken
	 *         in a range.
	 */
	public AssistControlPojo getAssistControlPojo() {
		return assistControlPojo;
	}

	/**
	 * @param assistControlPojo
	 *            : Object having information of attendance taken in a range.
	 */
	public void setAssistControlPojo(AssistControlPojo assistControlPojo) {
		this.assistControlPojo = assistControlPojo;
	}

	/**
	 * @return initialDateSearch: gets the date of the attendance to search in a
	 *         range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            : gets the date of the attendance to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return finalDateSearch: gets the date of the attendance to search in a
	 *         range.
	 */
	public Date getFinalDateSearch() {
		return finalDateSearch;
	}

	/**
	 * @param finalDateSearch
	 *            : gets the date of the attendance to search in a range.
	 */
	public void setFinalDateSearch(Date finalDateSearch) {
		this.finalDateSearch = finalDateSearch;
	}

	/**
	 * @return assistControlPojoList : pluviometerPojo list containing dates
	 *         introduced grouped by weeks.
	 */
	public List<AssistControlPojo> getAssistControlPojoList() {
		return assistControlPojoList;
	}

	/**
	 * @param assistControlPojoList
	 *            : pluviometerPojo list containing dates introduced grouped by
	 *            weeks.
	 */
	public void setAssistControlPojoList(
			List<AssistControlPojo> assistControlPojoList) {
		this.assistControlPojoList = assistControlPojoList;
	}

	/**
	 * @return assistControlPojoSubList: AssistControlPojo list containing dates
	 *         introduced grouped by weeks to show in the view.
	 */
	public List<AssistControlPojo> getAssistControlPojoSubList() {
		return assistControlPojoSubList;
	}

	/**
	 * @param assistControlPojoSubList
	 *            : AssistControlPojo list containing dates introduced grouped
	 *            by weeks to show in the view.
	 */
	public void setAssistControlPojoSubList(
			List<AssistControlPojo> assistControlPojoSubList) {
		this.assistControlPojoSubList = assistControlPojoSubList;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultAssistControl: Method that consultation attendance list
	 *         and load the template with the information found.
	 */
	public String initializeAttendance() {
		assistControlPojoList = new ArrayList<AssistControlPojo>();
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		pagination = new Paginador();
		assistControlPojo = null;
		return consultAssistControl();
	}

	/**
	 * Method to edit or create a new attendance.
	 * 
	 * @param assistControl
	 *            :Object of AssistControl are adding or editing.
	 * @return regAssistControl: Template redirects to register attendance.
	 */
	public String addAttendance(AssistControl assistControl) {
		if (assistControl != null) {
			this.assistControl = assistControl;
		} else {
			this.assistControl = new AssistControl();
		}
		return "regAssistControl";
	}

	/**
	 * See the list of attendance control.
	 * 
	 * @return gesAssistControl: Navigation rule that redirects to manage the
	 *         attendance control.
	 */
	public String consultAssistControl() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		ValidacionesAction validate = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		assistControlPojoSubList = new ArrayList<AssistControlPojo>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		try {
			assistControlList = assistControlDao.consultAssistControl(consult,
					parameters);
			assistControlPojoList = new ArrayList<AssistControlPojo>();
			if (assistControlList != null) {
				for (AssistControl assistControl : assistControlList) {
					Date date = assistControl.getDate();
					if (assistControlPojo == null
							|| !((date.after(assistControlPojo.getStartWeek()) && date
									.before(assistControlPojo.getEndWeek())))) {
						assistControlPojo = new AssistControlPojo();
						assistControlPojo.setName(assistControl.getHr()
								.getName()
								+ " "
								+ assistControl.getHr().getFamilyName());
						assistControlPojo.setStartWeek(ControladorFechas
								.diaInicialSemana(assistControl.getDate()));
						Date endDate = ControladorFechas
								.diaFinalSemana(assistControl.getDate());
						assistControlPojo.setEndWeek(ControladorFechas
								.diaFinalSemana(ControladorFechas.sumarDias(
										endDate, 1)));
						assistControlPojo.setVector(new int[14]);
						assistControlPojoList.add(assistControlPojo);
					}
					int numberDay = ControladorFechas.getNumberDay(date);
					if (numberDay != 1) {
						numberDay = numberDay - 1;
					} else {
						numberDay = 7;
					}
					if (!assistControl.isAbsent()) {
						assistControlPojo.setVectorPos(numberDay - 1, 1);
					}
				}
			}
			long amount = (long) assistControlPojoList.size();
			pagination.paginar(amount);
			int totalReg = pagination.getRango();
			int start = pagination.getInicio();
			int rank = start + totalReg;
			if (assistControlPojoList.size() < rank) {
				rank = assistControlPojoList.size();
			}
			this.assistControlPojoSubList = assistControlPojoList.subList(
					start, rank);
			if ((assistControlPojoList == null || assistControlPojoList.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (assistControlPojoList == null
					|| assistControlPojoList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("rain_gauge_label_s"),
								unionSearchMessages);
			}
			validate.setMensajeBusqueda(searchMessages);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesAssistControl";
	}

	// This section is to register logic

}