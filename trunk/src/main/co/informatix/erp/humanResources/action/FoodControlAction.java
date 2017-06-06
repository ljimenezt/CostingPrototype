package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
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
import org.richfaces.component.UIColumnGroup;
import org.richfaces.component.UIDataTable;

import co.informatix.erp.humanResources.dao.AssistControlDao;
import co.informatix.erp.humanResources.dao.DayTypeFoodDao;
import co.informatix.erp.humanResources.dao.FoodControlDao;
import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.entities.AssistControl;
import co.informatix.erp.humanResources.entities.DayTypeFood;
import co.informatix.erp.humanResources.entities.FoodControl;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.informacionBase.dao.HolidayDao;
import co.informatix.erp.informacionBase.dao.TypeFoodDao;
import co.informatix.erp.informacionBase.entities.TypeFood;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ConstantesErp;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.ControladorGenerico;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ReportsController;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the food control that may exist.
 * 
 * @author Wilhelm.Boada
 * @modify Andrex.Gomez
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class FoodControlAction implements Serializable {

	private Paginador pagination = new Paginador();
	private Date initialDateSearch;
	private Hr hrOther;
	private String nameSearch;
	private StringBuilder unionSearchMessages;
	private StringBuilder consult;
	private UIDataTable dataTable;
	private UIColumnGroup dataHeader;
	private List<Hr> hrList;
	private List<Hr> subHrList;
	private List<Hr> otherHrList;
	private List<TypeFood> typeFoodList;
	private List<Integer> otherQuantity;
	private List<DayTypeFood> dayTypeFoodList;
	private List<Hr> listHrFoodControl;
	private List<Date> listDateTable;
	private List<SelectItem> parameters;
	private HashMap<Integer, Integer> foodControlHashmap;
	boolean flagStart;
	private int contNextColumn;
	private int columnsCont;

	@EJB
	private HrDao hrDao;
	@EJB
	private TypeFoodDao typeFoodDao;
	@EJB
	private DayTypeFoodDao dayTypeFoodDao;
	@EJB
	private FoodControlDao foodControlDao;
	@EJB
	private HolidayDao holidayDao;
	@EJB
	AssistControlDao assistControlDao;

	/**
	 * @return pagination: Management paged food control.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged food control.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return initialDateSearch: gets the date of the food control to search in
	 *         a range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            : gets the date of the food control to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return hrOther: Human Resource for the food control.
	 */
	public Hr getHrOther() {
		return hrOther;
	}

	/**
	 * @param hrOther
	 *            : Human Resource for the food control.
	 */
	public void setHrOther(Hr hrOther) {
		this.hrOther = hrOther;
	}

	/**
	 * @return nameSearch : Name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return unionSearchMessages: message search in the advance consult
	 */
	public StringBuilder getUnionSearchMessages() {
		return unionSearchMessages;
	}

	/**
	 * @param unionSearchMessages
	 *            :message search in the advance consult
	 */
	public void setUnionSearchMessages(StringBuilder unionSearchMessages) {
		this.unionSearchMessages = unionSearchMessages;
	}

	/**
	 * @return hrList: Human resources list.
	 */
	public List<Hr> getHrList() {
		return hrList;
	}

	/**
	 * @param hrList
	 *            : Human resources list.
	 */
	public void setHrList(List<Hr> hrList) {
		this.hrList = hrList;
	}

	/**
	 * @return subHrList: Human resources list.
	 */
	public List<Hr> getSubHrList() {
		return subHrList;
	}

	/**
	 * @param subHrList
	 *            : Human resources list.
	 */
	public void setSubHrList(List<Hr> subHrList) {
		this.subHrList = subHrList;
	}

	/**
	 * @return typeFoodList: Type food list.
	 */
	public List<TypeFood> getTypeFoodList() {
		return typeFoodList;
	}

	/**
	 * @param typeFoodList
	 *            : Type food list.
	 */
	public void setTypeFoodList(List<TypeFood> typeFoodList) {
		this.typeFoodList = typeFoodList;
	}

	/**
	 * @return otherQuantity : Quantity list of food.
	 */
	public List<Integer> getOtherQuantity() {
		return otherQuantity;
	}

	/**
	 * @param otherQuantity
	 *            : Quantity list of food.
	 */
	public void setOtherQuantity(List<Integer> otherQuantity) {
		this.otherQuantity = otherQuantity;
	}

	/**
	 * @param flagStart
	 *            : This flag Initializes the consult.
	 */
	public void setFlagStart(boolean flagStart) {
		this.flagStart = flagStart;
	}

	/**
	 * @return listHrFoodControl: List of human resources of food control
	 */
	public List<Hr> getListHrFoodControl() {
		return listHrFoodControl;
	}

	/**
	 * @param listHrFoodControl
	 *            :List of human resources of food control
	 */
	public void setListHrFoodControl(List<Hr> listHrFoodControl) {
		this.listHrFoodControl = listHrFoodControl;
	}

	/**
	 * @return dayTypeFoodList: List containing the days associated with the
	 *         types of food.
	 */
	public List<DayTypeFood> getDayTypeFoodList() {
		return dayTypeFoodList;
	}

	/**
	 * @param dayTypeFoodList
	 *            : List containing the days associated with the types of food
	 */
	public void setDayTypeFoodList(List<DayTypeFood> dayTypeFoodList) {
		this.dayTypeFoodList = dayTypeFoodList;
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
	 * @return dataHeader: object builds header datatable
	 */
	public UIColumnGroup getDataHeader() {
		return dataHeader;
	}

	/**
	 * @param dataHeader
	 *            :object builds header datatable
	 */
	public void setDataHeader(UIColumnGroup dataHeader) {
		this.dataHeader = dataHeader;
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
	 * @return consult: query to concatenate
	 */
	public StringBuilder getConsult() {
		return consult;
	}

	/**
	 * @param consult
	 *            : query to concatenate
	 */
	public void setConsult(StringBuilder consult) {
		this.consult = consult;
	}

	/**
	 * @return parameters: list of search parameters.
	 */
	public List<SelectItem> getParameters() {
		return parameters;
	}

	/**
	 * @param parameters
	 *            : list of search parameters.
	 */
	public void setParameters(List<SelectItem> parameters) {
		this.parameters = parameters;
	}

	/**
	 * @return contNextColumn: variable to know the next column
	 */
	public int getContNextColumn() {
		return contNextColumn;
	}

	/**
	 * @param contNextColumn
	 *            : variable to know the next column
	 */
	public void setContNextColumn(int contNextColumn) {
		this.contNextColumn = contNextColumn;
	}

	/**
	 * @return columnsCont: variable to get the quantity of columns and it can
	 *         use in the view
	 */
	public int getColumnsCont() {
		return columnsCont;
	}

	/**
	 * @param contLogs
	 *            : variable to get the quantity of columns and it can use in
	 *            the view
	 */
	public void setColumnsCont(int columnsCont) {
		this.columnsCont = columnsCont;
	}

	/**
	 * Method to edit or create a new food control.
	 * 
	 * @return loadFoodControl(): Method allows load and initializes values.
	 */
	public String addEditFoodControl() {
		try {
			if (initialDateSearch == null) {
				initialDateSearch = new Date();
			}
			hrOther = new Hr();
			otherQuantity = new ArrayList<Integer>();
			otherHrList = new ArrayList<Hr>();
			flagStart = false;
			loadTypeFoodInHr();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return loadFoodControl();
	}

	/**
	 * This method allows load in the map the type food consulted in the
	 * database.
	 * 
	 * @throws Exception
	 */
	private void loadTypeFoodInHr() throws Exception {
		typeFoodList = typeFoodDao.consultTypeFood();
		dayTypeFoodList = dayTypeFoodDao.consultDayTypeFood(ControladorFechas
				.formatDate(this.initialDateSearch,
						Constantes.DATE_FORMAT_DAY_OF_WEEK));
		foodControlHashmap = new HashMap<Integer, Integer>();
		if (typeFoodList != null && typeFoodList.size() > 0) {
			if (dayTypeFoodList != null && dayTypeFoodList.size() > 0) {
				for (TypeFood typeFood : typeFoodList) {
					DayTypeFood dayTypeFoodHoliday = dayTypeFoodDao
							.consultAfterHoliday(typeFood.getId());
					for (DayTypeFood daytypeFood : dayTypeFoodList) {
						if (daytypeFood.getTypeFood().getId() == typeFood
								.getId()) {
							Date date = ControladorFechas
									.getYesterdayDate(initialDateSearch);
							boolean afterHoliday = holidayDao
									.consultHolidayByDate(ControladorFechas
											.formatDate(
													date,
													Constantes.DATE_FORMAT_CONSULT));
							if (!afterHoliday
									|| (afterHoliday && dayTypeFoodHoliday != null)) {
								foodControlHashmap.put(typeFood.getId(), 1);
								otherQuantity.add(1);
								break;
							}
						}
					}
					if (!foodControlHashmap.containsKey(typeFood.getId())) {
						foodControlHashmap.put(typeFood.getId(), 0);
						otherQuantity.add(0);
					}
				}
			}
		}
	}

	/**
	 * This method allows load and initializes values.
	 * 
	 * @return searchHr(): Method that consultation human resources list and
	 *         load the template with the information found.
	 */
	public String loadFoodControl() {
		pagination = new Paginador();
		this.nameSearch = "";
		return searchHr();
	}

	/**
	 * See the list of workers to food control.
	 * 
	 * @modify 14/02/2017 Claudia.Rey
	 * 
	 * @return regFoodControl: Navigation rule that redirects to register the
	 *         food control.
	 */
	public String searchHr() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHumanResources = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessages = new StringBuilder();
		HashMap<Integer, Integer> foodControlAsentHashmap = new HashMap<Integer, Integer>();
		String searchMessage = "";
		try {
			if (!flagStart) {
				advancedSearch(queryBuilder, parameters, bundle,
						jointSearchMessages);
				String date = ControladorFechas.formatDate(initialDateSearch,
						Constantes.DATE_FORMAT_CONSULT);
				hrList = hrDao.consultHrListByAssistControl(date, queryBuilder,
						parameters);
				if (hrList != null && hrList.size() > 0) {
					for (Hr hr : hrList) {
						boolean absent = hrDao.consultHrAssistControlAsent(
								date, hr.getIdHr());
						if (absent) {
							for (TypeFood typeFood : this.typeFoodList) {
								foodControlAsentHashmap
										.put(typeFood.getId(), 0);
							}
							hr.setMealControl(foodControlAsentHashmap);
						} else {
							hr.setMealControl(foodControlHashmap);
						}
					}
				}
				loadOtherHr();
				flagStart = true;
			}
			subHrList = new ArrayList<Hr>();
			if (hrList != null && hrList.size() > 0) {
				long amount = (long) this.hrList.size();
				this.pagination.paginar(amount);
				int start = this.pagination.getItemInicial() - 1;
				int end = this.pagination.getItemFinal();
				this.subHrList = this.hrList.subList(start, end);
			}
			buildDataTableRegister();
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
		return "regFoodControl";
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
			queryBuilder.append("AND UPPER(h.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			jointSearchMessages.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * This method allow search and add HR in the HR list.
	 */
	private void loadOtherHr() {
		for (Hr hr : this.otherHrList) {
			if (hr.getName().toLowerCase().contains(nameSearch.toLowerCase())) {
				hrList.add(hr);
			}
		}
	}

	/**
	 * This Method build dataTable for food control, add food type columns.
	 */
	public void buildDataTableRegister() {
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
		if (this.typeFoodList != null) {
			int countId = 0;
			for (TypeFood typeFood : this.typeFoodList) {
				countId++;
				HtmlOutputText headerText = new HtmlOutputText();
				String typeFoodName = typeFood.getName();
				headerText.setValue(WordUtils.capitalize(typeFoodName));
				UIColumn column = (UIColumn) ControladorContexto
						.getApplication().createComponent(
								UIColumn.COMPONENT_TYPE);
				column.setId("column" + countId);
				column.setStyleClass("center");
				HtmlPanelGroup panel = new HtmlPanelGroup();
				panel.setLayout("block");
				HtmlOutputText out = new HtmlOutputText();
				String mMergeList = "hr.mealControl[(" + typeFood.getId()
						+ ").intValue()] ";
				ValueExpression value = ControladorGenerico.getValueExpression(
						mMergeList, null);
				out.setEscape(false);
				out.setValueExpression(ConstantesErp.VALUE, value);
				panel.getChildren().add(out);
				column.getChildren().add(panel);
				column.setHeader(headerText);
				dataTable.getChildren().add(column);
			}
		}
	}

	/**
	 * This Method allows create and add other HR to the HR list.
	 * 
	 * @return loadFoodControl(): Method allows load and initializes values.
	 */
	public String createOtherHr() {
		HashMap<Integer, Integer> otherHashmap = new HashMap<Integer, Integer>();
		for (TypeFood typeFood : typeFoodList) {
			otherHashmap.put(typeFood.getId(),
					otherQuantity.get(typeFoodList.indexOf(typeFood)));
		}
		hrOther.setMealControl(otherHashmap);
		hrOther.setSeleccionado(true);
		otherHrList.add(hrOther);
		hrList.add(hrOther);
		hrOther = new Hr();
		return loadFoodControl();
	}

	/**
	 * Method used to save the food control.
	 * 
	 * @modify 17/02/2017 Patricia.Patinio
	 * @modify 28/02/2017 Claudia.Rey
	 * 
	 * @return initializeFoodControl(): Redirects to manage food control with
	 *         food control updated.
	 */
	public String saveFoodControl() {
		try {
			hrList = hrDao.consultHrListByAssistControl(ControladorFechas
					.formatDate(initialDateSearch,
							Constantes.DATE_FORMAT_CONSULT));
			if (hrList != null && hrList.size() > 0) {
				for (Hr hr : hrList) {
					String date = ControladorFechas.formatDate(
							initialDateSearch, Constantes.DATE_FORMAT_CONSULT);
					AssistControl assistControlAux = assistControlDao
							.consultAssistControlByHrAndDate(hr.getIdHr(), date);
					boolean flagAbsent = assistControlAux.isAbsent();
					for (TypeFood typeFood : typeFoodList) {
						FoodControl foodControl = foodControlDao
								.consultFoodControlByIdTypeIdHrAndDate(
										typeFood.getId(), hr.getIdHr(), date);
						foodControl.setHr(hr);
						foodControl.setTypeFood(typeFood);
						boolean flag = false;

						for (DayTypeFood dayTypeFood : dayTypeFoodList) {
							if (dayTypeFood.getTypeFood().getId() == typeFood
									.getId()) {
								flag = true;
							}
						}
						if (flag && !flagAbsent) {
							foodControl.setQuantity(1);
						} else {
							foodControl.setQuantity(0);
						}
						foodControl.setDate(initialDateSearch);
						if (foodControl.getId() > 0) {
							foodControlDao.editFoodControl(foodControl);
						} else {
							foodControlDao.saveFoodControl(foodControl);
						}
					}
				}
			}
			if (otherHrList != null && otherHrList.size() > 0) {
				for (Hr hr : otherHrList) {
					for (TypeFood typeFood : typeFoodList) {
						FoodControl foodControl = new FoodControl();
						foodControl.setOther(hr.getName());
						foodControl.setTypeFood(typeFood);
						foodControl
								.setQuantity(Integer.parseInt((String) String
										.valueOf(hr.getMealControl().get(
												typeFood.getId()))));
						foodControl.setDate(initialDateSearch);
						foodControlDao.saveFoodControl(foodControl);
					}
				}
			}
			ControladorContexto.mensajeInfoEspecifico(
					"meal_control_message_meal_control_saved",
					"messageHumanResources");
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeFoodControl();
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @modify 05/04/2017 Luna.Granados
	 * 
	 * @return consultFoodControl: Method that consultation food control list
	 *         and load the template with the information found.
	 */
	public String initializeFoodControl() {
		pagination = new Paginador();
		contNextColumn = 6;
		AssistControlAction assistControlAction = ControladorContexto
				.getContextBean(AssistControlAction.class);
		assistControlAction.setInitialDateSearch(null);
		assistControlAction.setFinalDateSearch(null);
		return consultFoodControl();
	}

	/**
	 * Consult the the list food control to show in the view and return
	 * navigation rule.
	 * 
	 * @modify 07/04/2017 Claudia.Rey
	 * 
	 * @return gesFoodControl: Navigation rule that redirects to manage the food
	 *         control.
	 */
	public String consultFoodControl() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHr = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validate = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		AssistControlAction assistControlAction = ControladorContexto
				.getContextBean(AssistControlAction.class);
		String searchMessages = "";
		StringBuilder unionSearchMessages = new StringBuilder();
		try {
			boolean flagValidationDates = true;
			if (assistControlAction.getInitialDateSearch() != null
					&& assistControlAction.getFinalDateSearch() != null) {
				flagValidationDates = assistControlAction
						.validateRangeFortNight();
			}
			if (flagValidationDates) {
				this.listHrFoodControl = new ArrayList<Hr>();
				assistControlAction.setSource(false);
				assistControlAction.consultAssistControl();
				pagination = assistControlAction.getPagination();
				unionSearchMessages = assistControlAction
						.getUnionSearchMessages();
				this.listHrFoodControl = assistControlAction
						.getListHrAssistControl();
				if (listHrFoodControl != null && listHrFoodControl.size() > 0) {
					this.listDateTable = assistControlAction.getListDateTable();
					associateTypeFood(this.consult, this.parameters);
					String param3move = ControladorContexto
							.getParam("param3move");
					if (param3move == null) {
						param3move = "";
					}
					buildDataTable(param3move);
				} else {
					columnsCont = 0;
					contNextColumn = 6;
				}
				if ((listHrFoodControl == null || listHrFoodControl.size() <= 0)
						&& !"".equals(unionSearchMessages.toString())) {
					searchMessages = MessageFormat
							.format(bundle
									.getString("message_no_existen_registros_criterio_busqueda"),
									unionSearchMessages);
				} else if (listHrFoodControl == null
						|| listHrFoodControl.size() <= 0) {
					ControladorContexto.mensajeInformacion(null,
							bundle.getString("message_no_existen_registros"));
				} else if (!"".equals(unionSearchMessages.toString())) {
					searchMessages = MessageFormat
							.format(bundle
									.getString("message_existen_registros_criterio_busqueda"),
									bundleHr.getString("meal_control_label_s"),
									unionSearchMessages);
				}
				validate.setMensajeBusqueda(searchMessages);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesFoodControl";
	}

	/**
	 * This method allows add the novelty of the assist control according with
	 * the human resource
	 * 
	 * @modify 15/03/2017 Claudia.Rey
	 * @modify 24/03/2017 Claudia.Rey
	 * @modify 28/03/2017 Fabian.Diaz
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @throws Exception
	 */
	private void associateTypeFood(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		boolean flagOther = false;
		String nameOther = "";
		List<Date> listDate = assistControlDao.consultAssistControlDates(
				consult, parameters, "FoodControl");
		for (Hr hr : listHrFoodControl) {
			int idHr = 0;
			List<FoodControl> listFoodControlAux = new ArrayList<FoodControl>();
			if (hr.getIdHr() > 0) {
				idHr = hr.getIdHr();
				listFoodControlAux = foodControlDao.listHrOfFoodControl(idHr,
						consult, parameters);
			} else {
				String name = hr.getFullName();
				listFoodControlAux = foodControlDao.listHrOfFoodControlXOther(
						name, consult, parameters);
			}
			if (listFoodControlAux != null) {
				HashMap<Integer, Integer> hasmapAux = new HashMap<Integer, Integer>();
				for (FoodControl fc : listFoodControlAux) {
					Date dateAssist = ControladorFechas.formatearFecha(
							fc.getDate(), Constantes.DATE_FORMAT_CONSULT);
					int idFoodType = fc.getTypeFood().getId();
					Integer i = (int) (dateAssist.getTime() / 1000)
							+ idFoodType;
					String date = fc.getDate().toString();
					int val;
					if (idHr > 0) {
						AssistControl assistControlAux = assistControlDao
								.consultAssistControlByHrAndDate(idHr, date);
						if (assistControlAux != null
								&& assistControlAux.isAbsent()) {
							val = 0;
						} else {
							val = fc.getQuantity();
						}
					} else {
						flagOther = true;
						nameOther = fc.getOther();
						val = fc.getQuantity();
					}
					hasmapAux.put(i, val);
				}
				if (flagOther) {
					for (Date date : listDate) {
						FoodControl foodControlAux = foodControlDao
								.consultFoodControlXDate(nameOther,
										date.toString());
						if (foodControlAux.getTypeFood() == null) {
							Date dateAssist = ControladorFechas.formatearFecha(
									date, Constantes.DATE_FORMAT_CONSULT);
							typeFoodList = typeFoodDao.consultTypeFood();
							for (TypeFood tf : typeFoodList) {
								Integer i = (int) (dateAssist.getTime() / 1000)
										+ tf.getId();
								hasmapAux.put(i, 0);
							}
						}
					}
					flagOther = false;
				}
				hr.setAssistFoodControl(hasmapAux);
			}
		}
	}

	/**
	 * This method allow show a maximum of only six columns
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param columns
	 *            : total quantity of columns
	 * @param column
	 *            : get an object of type column to render the columns
	 */
	private void maxColumns(int columns, UIColumn column) {
		if (columns <= 6) {
			column.setRendered(true);
		} else {
			column.setRendered(false);
		}
	}

	/**
	 * This method allow to go forward to next column
	 * 
	 * @author Fabian.Diaz
	 * 
	 * @param columns
	 *            : get total quantity of columns
	 * @param nextColumn
	 *            : get next column to show
	 * @param column
	 *            : get an object of type column to render the columns
	 * @param param
	 *            : get a string to know if it should show the next or the
	 *            previous column
	 */
	private void nextColumns(int columns, int nextColumn, UIColumn column,
			String param) {
		if ((param.equals("next") || param.equals(""))
				|| (param.equals("back") || param.equals(""))
				&& contNextColumn > 6) {
			for (int i = 7; i <= 15; i++) {
				if (columns <= i && nextColumn == i) {
					int initialColumn = i - 6;
					if (columns > initialColumn && columns <= i) {
						column.setRendered(true);
					} else {
						column.setRendered(false);
					}
				}
			}
		}
	}

	/**
	 * This Method build dataTable for assist control, Add columns from date of
	 * assist
	 * 
	 * @modify 28/03/2017 Fabian.Diaz
	 * 
	 * @param param3move
	 *            : get a string that proceed of the view manage meal control
	 * @throws Exception
	 */
	private void buildDataTable(String param3move) throws Exception {
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
		column1.setRowspan(1);
		dataTable.getChildren().add(column1);

		dataHeader = new UIColumnGroup();
		UIColumn columnName = (UIColumn) ControladorContexto.getApplication()
				.createComponent(UIColumn.COMPONENT_TYPE);
		dataHeader.getChildren().add(columnName);

		if (param3move.equals("next")) {
			contNextColumn += 1;
		}
		if (param3move.equals("back")) {
			contNextColumn -= 1;
		}
		int contColumns = 0;

		if (this.listDateTable != null) {
			List<TypeFood> listTypeFood = typeFoodDao.consultTypeFood();
			int colspan = listTypeFood.size();
			for (Date date : this.listDateTable) {
				contColumns++;
				Date dateL = ControladorFechas.formatearFecha(date,
						Constantes.DATE_FORMAT_CONSULT);
				Integer d = (int) (dateL.getTime() / 1000);

				String dayOfWeek = ControladorFechas.formatDate(date,
						Constantes.DATE_FORMAT_DAY_WEEK_DAY);
				UIColumn columnH = (UIColumn) ControladorContexto
						.getApplication().createComponent(
								UIColumn.COMPONENT_TYPE);
				columnH.setColspan(colspan);
				HtmlOutputText headerTextH = new HtmlOutputText();
				headerTextH.setValue(WordUtils.capitalize(dayOfWeek));

				maxColumns(contColumns, columnH);
				nextColumns(contColumns, contNextColumn, columnH, param3move);

				columnH.getChildren().add(headerTextH);
				dataHeader.getChildren().add(columnH);
				for (TypeFood typeF : listTypeFood) {
					int val = d + typeF.getId();
					HtmlOutputText headerText = new HtmlOutputText();
					String typeFoodName = typeF.getAbbreviation();
					headerText.setValue(WordUtils.capitalize(typeFoodName));
					UIColumn column = (UIColumn) ControladorContexto
							.getApplication().createComponent(
									UIColumn.COMPONENT_TYPE);
					column.setStyleClass("center");
					HtmlPanelGroup panel = new HtmlPanelGroup();
					panel.setLayout("block");
					HtmlOutputText out = new HtmlOutputText();
					String mMergeList = "hr.assistFoodControl[(" + val
							+ ").intValue()] ";
					ValueExpression value = ControladorGenerico
							.getValueExpression(mMergeList, null);
					out.setEscape(false);
					out.setValueExpression(ConstantesErp.VALUE, value);

					maxColumns(contColumns, column);
					nextColumns(contColumns, contNextColumn, column, param3move);

					panel.getChildren().add(out);
					column.getChildren().add(panel);
					column.setHeader(headerText);
					dataTable.getChildren().add(column);
				}
			}
			columnsCont = contColumns;
		}
	}

	/**
	 * This method allow consult the food control information and generate the
	 * report
	 * 
	 * @modify 27/03/2017 Claudia.Rey
	 * @modify 05/06/2017 Fabian.Diaz
	 */
	public void generateReportAssitControl() {
		ReportsController reportsController = ControladorContexto
				.getContextBean(ReportsController.class);
		AssistControlAction assistControlAction = ControladorContexto
				.getContextBean(AssistControlAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHr = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validate = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		try {
			assistControlAction.setInitialDateSearch(null);
			assistControlAction.setFinalDateSearch(null);
			assistControlAction.advanceSearch(consult, parameters, bundle,
					unionSearchMessages, false);
			List<FoodControl> listFoodControl = foodControlDao
					.listHrOfFoodControl(0, consult, parameters);
			if (listFoodControl != null && listFoodControl.size() > 0) {
				for (FoodControl food : listFoodControl) {
					if (food.getHr() == null) {
						Hr hr = new Hr();
						String name = food.getOther();
						hr.setFullName(name);
						String delimiter = " ";
						String[] temp = name.split(delimiter);
						hr.setName(temp[0]);
						if (temp.length >= 2) {
							hr.setFamilyName(temp[1]);
						} else {
							hr.setName("");
						}
						food.setHr(hr);
					}
				}
			}
			List<Date> listDate = assistControlDao.consultAssistControlDates(
					consult, parameters, "FoodControl");
			if ((listFoodControl == null || listFoodControl.size() <= 0)
					|| (listDate == null || listDate.size() <= 0)) {
				searchMessages = MessageFormat.format(
						bundle.getString("message_no_existen_registros"),
						bundleHr.getString("meal_control_label_s"),
						unionSearchMessages);
				validate.setMensajeBusqueda(searchMessages);
			} else {
				Long countD = 0L;
				for (Date d : listDate) {
					Date initialD = ControladorFechas.finDeDia(d);
					Date finalD = ControladorFechas.inicioDeDia(d);
					Long countW = foodControlDao.countDateFoodControl(initialD,
							finalD);
					countD += countW;
				}
				reportsController.generateReportFoodControl(listFoodControl,
						countD);
			}
			assistControlAction.setStartDateReport(null);
			assistControlAction.setEndDateReport(null);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}