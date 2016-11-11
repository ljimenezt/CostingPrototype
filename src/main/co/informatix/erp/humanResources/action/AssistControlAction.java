package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.AssistControlDao;
import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.NoveltyDao;
import co.informatix.erp.humanResources.dao.NoveltyTypeDao;
import co.informatix.erp.humanResources.entities.AssistControl;
import co.informatix.erp.humanResources.entities.AssistControlPojo;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.Novelty;
import co.informatix.erp.humanResources.entities.NoveltyType;
import co.informatix.erp.utils.Constantes;
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

	private Paginador pagination = new Paginador();
	private AssistControl assistControl;
	private AssistControlPojo assistControlPojo;
	private Date initialDateSearch;
	private Date finalDateSearch;
	private Hr hr;
	private String nameSearch;
	private Novelty novelty;
	private List<AssistControl> assistControlList;
	private List<AssistControlPojo> assistControlPojoList;
	private List<AssistControlPojo> assistControlPojoSubList;
	private List<SelectItem> itemsNoveltyType;
	private List<Hr> hrList;
	private List<Novelty> noveltyListRemove;
	private HashMap<Integer, Novelty> noveltyMap;

	@EJB
	private AssistControlDao assistControlDao;
	@EJB
	private HrDao hrDao;
	@EJB
	private NoveltyTypeDao noveltyTypeDao;
	@EJB
	private NoveltyDao noveltyDao;

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
	 * @return hr: Human Resource for the assist control.
	 */
	public Hr getHr() {
		return hr;
	}

	/**
	 * @param hr
	 *            : Human Resource for the assist control.
	 */
	public void setHr(Hr hr) {
		this.hr = hr;
	}

	/**
	 * @return nameSearch : Hr name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Hr name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return novelty: Reason for absence from work.
	 */
	public Novelty getNovelty() {
		return novelty;
	}

	/**
	 * @param novelty
	 *            : Reason for absence from work.
	 */
	public void setNovelty(Novelty novelty) {
		this.novelty = novelty;
	}

	/**
	 * @return assistControlList : AssistControl list.
	 */
	public List<AssistControl> getAssistControlList() {
		return assistControlList;
	}

	/**
	 * @param assistControlList
	 *            : AssistControl list.
	 */
	public void setAssistControlList(List<AssistControl> assistControlList) {
		this.assistControlList = assistControlList;
	}

	/**
	 * @return assistControlPojoList : AssistControlPojo list containing dates
	 *         introduced grouped by weeks.
	 */
	public List<AssistControlPojo> getAssistControlPojoList() {
		return assistControlPojoList;
	}

	/**
	 * @param assistControlPojoList
	 *            : AssistControlPojo list containing dates introduced grouped
	 *            by weeks.
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
	 * @return itemsNoveltyType: Name of the novelty type associated with a
	 *         novelty.
	 */
	public List<SelectItem> getItemsNoveltyType() {
		return itemsNoveltyType;
	}

	/**
	 * @param itemsNoveltyType
	 *            : Name of the novelty type associated with a novelty.
	 */
	public void setItemsNoveltyType(List<SelectItem> itemsNoveltyType) {
		this.itemsNoveltyType = itemsNoveltyType;
	}

	/**
	 * @return hrList: Human resources list
	 */
	public List<Hr> getHrList() {
		return hrList;
	}

	/**
	 * @param hrList
	 *            : Human resources list
	 */
	public void setHrList(List<Hr> hrList) {
		this.hrList = hrList;
	}

	/**
	 * @return noveltyMap: Saved the novelty of absence from work.
	 */
	public HashMap<Integer, Novelty> getNoveltyMap() {
		return noveltyMap;
	}

	/**
	 * @param noveltyMap
	 *            : Saved the novelty of absence from work.
	 */
	public void setNoveltyMap(HashMap<Integer, Novelty> noveltyMap) {
		this.noveltyMap = noveltyMap;
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
	 * @return loadAttendance(): Method allows load the novelties and
	 *         initialization values.
	 */
	public String addAttendance() {
		try {
			assistControlList = null;
			this.noveltyMap = null;
			this.noveltyListRemove = new ArrayList<Novelty>();
			loadNoveltyType();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return loadAttendance();
	}

	/**
	 * This method allows load the novelties and initialization values.
	 * 
	 * @return searchHr(): Method that consultation human resources list and
	 *         load the template with the information found.
	 */
	public String loadAttendance() {
		try {
			pagination = new Paginador();
			this.nameSearch = "";
			if (noveltyMap == null || noveltyMap.size() <= 0) {
				List<Novelty> noveltyList = noveltyDao
						.findNoveltyByDate(ControladorFechas
								.getFechaActual(Constantes.DATE_FORMAT_CONSULT));
				noveltyMap = new HashMap<Integer, Novelty>();
				if (noveltyList != null) {
					for (Novelty novelty : noveltyList) {
						if (!this.noveltyListRemove.contains(novelty)) {
							noveltyMap.put(novelty.getHr().getIdHr(), novelty);
						}
					}
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchHr();
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

	/**
	 * See the list of workers to attendance control.
	 * 
	 * @return regAssistControl: Navigation rule that redirects to register the
	 *         attendance control.
	 */
	public String searchHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHumanResources = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		this.hrList = new ArrayList<Hr>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		String searchMessage = "";
		try {
			advancedSearch(queryBuilder, parameters, bundle,
					jointSearchMessages);
			Long amount = hrDao.hrAmount(queryBuilder, parameters);
			if (amount != null) {
				pagination.paginar(amount);
				this.hrList = hrDao.queryHr(pagination.getInicio(),
						pagination.getRango(), queryBuilder, parameters);
			}
			if (this.hrList != null) {
				for (Hr hr : this.hrList) {
					if (noveltyMap.get(hr.getIdHr()) != null) {
						hr.setSeleccionado(true);
					}
				}
			}
			if ((hrList == null || hrList.size() <= 0)
					&& !"".equals(jointSearchMessages.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessages);
			} else if (hrList == null || hrList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessage = MessageFormat.format(message,
						bundleHumanResources.getString("human_resource_label"),
						jointSearchMessages);
			}
			validations.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regAssistControl";
	}

	/**
	 * This method builds a query with advanced search; it also render the
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param queryBuilder
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Access language tags.
	 * @param jointSearchMessages
	 *            : Message search.
	 */
	private void advancedSearch(StringBuilder queryBuilder,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder jointSearchMessages) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			queryBuilder.append("WHERE UPPER(h.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			jointSearchMessages.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * This method load all types of machines from a list.
	 * 
	 * @throws Exception
	 */
	private void loadNoveltyType() throws Exception {
		itemsNoveltyType = new ArrayList<SelectItem>();
		List<NoveltyType> listNoveltyType;
		listNoveltyType = noveltyTypeDao.listNoveltyType();
		if (listNoveltyType != null) {
			for (NoveltyType noveltyType : listNoveltyType) {
				itemsNoveltyType.add(new SelectItem(noveltyType.getId(),
						noveltyType.getName()));
			}
		}
	}

	/**
	 * This method allows create the novelty of absence of the worker.
	 * 
	 * @param hr
	 *            : For associate the human resource to the novelty.
	 */
	public void createNovelty(Hr hr) {
		try {
			this.hr = hr;
			if (!this.hr.isSeleccionado()) {
				this.novelty = new Novelty();
				this.novelty.setNoveltyType(new NoveltyType());
				this.novelty.setHr(this.hr);
			} else if (noveltyMap.get(this.hr.getIdHr()).getId() == 0) {
				this.noveltyMap.remove(this.hr.getIdHr());
				this.hr.setSeleccionado(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate the field and add a new novelty.
	 */
	public void addNoveltyAndValidateRequerid() {
		boolean flag = false;
		if (this.novelty.getNoveltyType().getId() == 0) {
			ControladorContexto.mensajeRequeridos("formNovelty:cmbNoveltyType");
			flag = true;
		}
		if (this.novelty.getInitialDateTime() == null
				|| this.novelty.getFinalDateTime() == null) {
			if (this.novelty.getInitialDateTime() == null) {
				ControladorContexto.mensajeRequeridos("formNovelty:startDate");
			}
			if (this.novelty.getFinalDateTime() == null) {
				ControladorContexto.mensajeRequeridos("formNovelty:endDate");
			}
			flag = true;
		} else if (this.novelty.getFinalDateTime().before(
				this.novelty.getInitialDateTime())) {
			ControladorContexto.mensajeErrorEspecifico("formNovelty:endDate",
					"attendance_message_end_date_not_earlier_start_date",
					"messageHumanResources");
			flag = true;
		}
		if (!flag) {
			this.novelty.getNoveltyType().setName(
					(String) ValidacionesAction.getLabel(itemsNoveltyType,
							novelty.getNoveltyType().getId()));
			noveltyMap.put(hr.getIdHr(), this.novelty);
			for (Hr hr : this.hrList) {
				if (this.hr.getIdHr() == hr.getIdHr()) {
					hr.setSeleccionado(true);
					break;
				}
			}
		}
	}

	/**
	 * Method used to save the assist control.
	 * 
	 * @return initializeAttendance(): Redirects to manage assist control with
	 *         assist control updated.
	 */
	public String saveAssistControl() {
		try {
			this.hrList = hrDao.queryHr();
			this.assistControlList = new ArrayList<AssistControl>();
			if (this.hrList != null) {
				for (Hr hr : this.hrList) {
					AssistControl assistControl = assistControlDao
							.consultAssistControlByHrAndDate(
									hr.getIdHr(),
									ControladorFechas
											.getFechaActual(Constantes.DATE_FORMAT_CONSULT));
					Novelty novelty = noveltyMap.get(hr.getIdHr());
					if (assistControl == null) {
						assistControl = new AssistControl();
						assistControl.setHr(hr);
						assistControl.setDate(new Date());
					}
					if (novelty != null) {
						assistControl.setAbsent(true);
						if (novelty.getId() == 0) {
							noveltyDao.saveNovelty(novelty);
						} else {
							noveltyDao.editNovelty(novelty);
						}
					} else {
						assistControl.setAbsent(false);
					}
					if (assistControl.getId() == 0) {
						assistControlDao.saveAssistControl(assistControl);
					} else {
						assistControlDao.editAssistControl(assistControl);
					}
				}
			}
			if (this.noveltyListRemove != null) {
				for (Novelty novelty : this.noveltyListRemove) {
					noveltyDao.removeNovelty(novelty);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeAttendance();
	}

	/**
	 * This method allows remove the novelty of absence of the worker.
	 */
	public void removeNovelty() {
		for (Hr hr : this.hrList) {
			if (hr.getIdHr() == this.hr.getIdHr()) {
				hr.setSeleccionado(false);
				if (noveltyMap.get(hr.getIdHr()).getId() != 0) {
					this.noveltyListRemove.add(noveltyMap.get(hr.getIdHr()));
				}
				noveltyMap.remove(hr.getIdHr());
				break;
			}
		}
	}

	// This section is to register logic
}