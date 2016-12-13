package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.el.ValueExpression;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.html.HtmlOutputText;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.text.WordUtils;
import org.richfaces.component.UIColumn;
import org.richfaces.component.UIDataTable;

import co.informatix.erp.humanResources.dao.AssistControlDao;
import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.NoveltyDao;
import co.informatix.erp.humanResources.dao.NoveltyTypeDao;
import co.informatix.erp.humanResources.entities.AssistControl;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.Novelty;
import co.informatix.erp.humanResources.entities.NoveltyType;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.ControladorGenerico;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ReportsController;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the attendance that may exist.
 * 
 * @author Wilhelm.Boada
 * @modify Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class AssistControlAction implements Serializable {

	private Paginador pagination = new Paginador();
	private AssistControl assistControl;
	private Date initialDateSearch;
	private Date finalDateSearch;
	private Hr hr;
	private String nameSearch;
	private Novelty novelty;
	private List<SelectItem> itemsNoveltyType;
	private List<Hr> hrList;
	private List<Novelty> noveltyListRemove;
	private HashMap<Integer, Novelty> noveltyMap;

	private List<Date> listDateTable;
	private List<Hr> listHrAssistControl;
	private UIDataTable dataTable;

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
	 * @return listDateTable: List Date to show the dynamically columns in the
	 *         view
	 */
	public List<Date> getListDateTable() {
		return listDateTable;
	}

	/**
	 * @param listDateTable
	 *            :List Date to show the dynamically columns in the view
	 */
	public void setListDateTable(List<Date> listDateTable) {
		this.listDateTable = listDateTable;
	}

	/**
	 * @return listHrAssistControl :List HR assist control associate in the
	 *         table
	 */
	public List<Hr> getListHrAssistControl() {
		return listHrAssistControl;
	}

	/**
	 * @param listHrAssistControl
	 *            :List HR assist control associate in the table
	 */
	public void setListHrAssistControl(List<Hr> listHrAssistControl) {
		this.listHrAssistControl = listHrAssistControl;
	}

	/**
	 * @return dataTable: object builds datatable
	 */
	public UIDataTable getDataTable() {
		return dataTable;
	}

	/**
	 * @param dataTable
	 *            : object builds datatable
	 */
	public void setDataTable(UIDataTable dataTable) {
		this.dataTable = dataTable;
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
		this.initialDateSearch = null;
		this.finalDateSearch = null;
		pagination = new Paginador();
		return consultAssistControl();
	}

	/**
	 * This Method build dataTable for assist control, Add columns from date of
	 * assist
	 * 
	 */
	public void buildDataTable() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		dataTable = new UIDataTable();
		HtmlOutputText headerText1 = new HtmlOutputText();
		headerText1.setValue(WordUtils.capitalize(bundle
				.getString("label_name")));
		UIColumn column1 = (UIColumn) ControladorContexto.getApplication()
				.createComponent(UIColumn.COMPONENT_TYPE);
		HtmlOutputText out1 = new HtmlOutputText();
		column1.setStyleClass("colTextoMediano");
		String mMergeName = "hr.name";
		String nMergeFamilyName = "hr.familyName";
		ValueExpression value1 = ControladorGenerico.getValueExpression(
				mMergeName, nMergeFamilyName);
		out1.setValueExpression("value", value1);
		column1.getChildren().add(out1);
		column1.setHeader(headerText1);
		dataTable.getChildren().add(column1);

		if (this.listDateTable != null) {
			int countId = 0;
			for (Date date : this.listDateTable) {
				countId++;
				HtmlOutputText headerText = new HtmlOutputText();
				String d = ControladorFechas.formatDate(date,
						Constantes.DATE_FORMAT_COLUMN_TABLE);
				Date dateL = ControladorFechas.formatearFecha(date,
						Constantes.DATE_FORMAT_CONSULT);
				Integer i = (int) (dateL.getTime() / 1000);
				headerText.setValue(WordUtils.capitalize(d));
				UIColumn column = (UIColumn) ControladorContexto
						.getApplication().createComponent(
								UIColumn.COMPONENT_TYPE);
				column.setId("column" + countId);
				column.setStyleClass("center");
				HtmlPanelGroup panel = new HtmlPanelGroup();
				panel.setLayout("block");
				String valueStyle = "background-color: #{hr.styleControl[(" + i
						+ ").intValue()]};";
				ValueExpression valueEStyle = ControladorContexto
						.getApplication()
						.getExpressionFactory()
						.createValueExpression(
								ControladorContexto.getELContext(), valueStyle,
								String.class);
				panel.setValueExpression("style", valueEStyle);
				HtmlOutputText out = new HtmlOutputText();
				String mMergeList = "hr.assistControl[(" + i + ").intValue()] ";
				ValueExpression value = ControladorGenerico.getValueExpression(
						mMergeList, null);
				out.setEscape(false);
				out.setValueExpression("value", value);
				String image = "hr.styleControl[(" + i + ").intValue()]";
				ValueExpression valueImage = ControladorGenerico
						.getValueExpression(image, null);
				out.setValueExpression("styleClass", valueImage);
				panel.getChildren().add(out);
				column.getChildren().add(panel);
				column.setHeader(headerText);
				dataTable.getChildren().add(column);
			}
		}
	}

	/**
	 * This method allows validate the range of the search filter
	 */
	public void validateRangeFortNight() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		if (this.initialDateSearch != null && this.finalDateSearch != null
				&& initialDateSearch != finalDateSearch) {
			Double valueFornight = ControladorFechas.restarFechas(
					initialDateSearch, finalDateSearch);
			Double fornight = valueFornight / 24;
			if (fornight > Constantes.VALUE_FORNIGHT) {
				ControladorContexto.mensajeError("formBuscar:message",
						bundle.getString("message_validar_rango_fecha"));
			}
		}
	}

	/**
	 * Method to edit or create a new attendance.
	 * 
	 * @return loadAttendance(): Method allows load the novelties and
	 *         initialization values.
	 */
	public String addAttendance() {
		try {
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
		ResourceBundle bundleHr = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validate = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		this.listHrAssistControl = new ArrayList<Hr>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		String whiteSpace = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
		try {
			advanceSearch(consult, parameters, bundle, unionSearchMessages,
					true);
			Long quantity = hrDao.hrAssistControlAmount(consult, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			this.listHrAssistControl = hrDao.listHrOfAssistControl(
					pagination.getInicio(), pagination.getRango(), consult,
					parameters);
			if (listHrAssistControl != null) {
				for (Hr hr : listHrAssistControl) {
					int idHr = hr.getIdHr();
					List<AssistControl> listAssistControlAux = assistControlDao
							.listHrOfAssistControl(idHr, consult, parameters);
					if (listAssistControlAux != null) {
						HashMap<Integer, String> hasmapAux = new HashMap<Integer, String>();
						HashMap<Integer, String> hasmapStyle = new HashMap<Integer, String>();
						for (AssistControl ac : listAssistControlAux) {
							Date dateAssist = ControladorFechas.formatearFecha(
									ac.getDate(),
									Constantes.DATE_FORMAT_CONSULT);
							Integer i = (int) (dateAssist.getTime() / 1000);
							String value = "";
							String styleAssist = "";
							if (ac.isAbsent()) {
								Novelty novelty = noveltyDao
										.noveltyByHrAndDate(idHr, dateAssist);
								if (novelty != null) {
									value = novelty.getNoveltyType().getName();
									styleAssist = novelty.getNoveltyType()
											.getColor().getCode();
								} else {
									value = whiteSpace;
									styleAssist = Constantes.STYLE_ASSIST_NOT;
								}
							} else {
								value = whiteSpace;
								styleAssist = Constantes.STYLE_ASSIST_OK;
							}
							hasmapAux.put(i, value);
							hasmapStyle.put(i, styleAssist);
						}
						hr.setAssistControl(hasmapAux);
						hr.setStyleControl(hasmapStyle);
					}
				}
			}
			this.listDateTable = assistControlDao.consultAssistControlDates(
					consult, parameters);
			if ((listHrAssistControl == null || listHrAssistControl.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (listHrAssistControl == null
					|| listHrAssistControl.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleHr.getString("attendance_label_attendance_s"),
								unionSearchMessages);
			}
			buildDataTable();
			validate.setMensajeBusqueda(searchMessages);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesAssistControl";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @modify 01/03/2016 Gerardo.Herrera
	 * @modify 14/04/2016 Wilhelm.Boada
	 * @modify 15/07/2016 Andres.Gomez
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 * @param flag
	 *            :boolean indicate if the message is to show
	 */
	private void advanceSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch, boolean flag) {
		SimpleDateFormat format = new SimpleDateFormat(
				Constantes.DATE_FORMAT_MESSAGE_SIMPLE);
		if (this.initialDateSearch != null && this.finalDateSearch != null) {
			consult.append("WHERE ac.date >= :initialDateSearch AND ac.date <= :finalDateSearch ");
			SelectItem item = new SelectItem(initialDateSearch,
					"initialDateSearch");
			parameters.add(item);
			Date finalDateSearchAux = ControladorFechas
					.finDeDia(finalDateSearch);
			SelectItem item2 = new SelectItem(finalDateSearchAux,
					"finalDateSearch");
			parameters.add(item2);
			if (flag) {
				String dateFrom = bundle.getString("label_start_date") + ": "
						+ '"' + format.format(initialDateSearch) + '"' + " ";
				unionMessagesSearch.append(dateFrom);
				String dateTo = bundle.getString("label_end_date") + ": " + '"'
						+ format.format(finalDateSearch) + '"';
				unionMessagesSearch.append(dateTo);
			}
		} else {
			Date actualDate = new Date();
			Date initialDateDefault = ControladorFechas
					.diaInicialSemana(actualDate);
			Date fianlDateDefault = ControladorFechas
					.diaFinalSemana(actualDate);
			consult.append("WHERE ac.date >= :initialDateDefault AND ac.date <= :fianlDateDefault ");
			SelectItem item = new SelectItem(initialDateDefault,
					"initialDateDefault");
			parameters.add(item);
			fianlDateDefault = ControladorFechas.finDeDia(fianlDateDefault);
			SelectItem item2 = new SelectItem(fianlDateDefault,
					"fianlDateDefault");
			parameters.add(item2);
		}
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
						hr.setSeleccionado(false);
					} else {
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
				this.hr.setSeleccionado(true);
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
					hr.setSeleccionado(false);
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
				hr.setSeleccionado(true);
				if (noveltyMap.get(hr.getIdHr()).getId() != 0) {
					this.noveltyListRemove.add(noveltyMap.get(hr.getIdHr()));
				}
				noveltyMap.remove(hr.getIdHr());
				break;
			}
		}
	}

	/**
	 * This method allows cancel the novelty.
	 */
	public void cancelNovelty() {
		for (Hr hr : this.hrList) {
			if (hr.getIdHr() == this.hr.getIdHr()) {
				hr.setSeleccionado(true);
			}
		}
	}

	/**
	 * This method allow consult the assist control information and generate the
	 * report
	 */
	public void generateReportAssitControl() {
		ReportsController reportsController = ControladorContexto
				.getContextBean(ReportsController.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		try {
			advanceSearch(consult, parameters, null, null, false);
			List<AssistControl> listAssistControl = assistControlDao
					.listHrOfAssistControl(0, consult, parameters);
			for (AssistControl ac : listAssistControl) {
				String fullName = ac.getHr().getName() + " "
						+ ac.getHr().getFamilyName();
				ac.getHr().setFullName(fullName);
				if (ac.isAbsent()) {
					int idHr = ac.getHr().getIdHr();
					Date date = ControladorFechas.formatearFecha(ac.getDate(),
							Constantes.DATE_FORMAT_CONSULT);
					Novelty novelty = noveltyDao.noveltyByHrAndDate(idHr, date);
					ac.setNovelty(novelty);
				}
			}
			Date maxDate = assistControlDao.consultMaxDate(consult, parameters);
			maxDate = ControladorFechas.formatearFecha(maxDate,
					Constantes.DATE_FORMAT_CONSULT);
			reportsController.generateReportAttendance(listAssistControl,
					maxDate);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

}