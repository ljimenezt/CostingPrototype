package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
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
import co.informatix.erp.humanResources.dao.DayTypeFoodDao;
import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.MealControlDao;
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
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the meal control that may exist.
 * 
 * @author Wilhelm.Boada
 * @author Andrex.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MealControlAction implements Serializable {

	private Paginador pagination = new Paginador();
	private Date initialDateSearch;
	private Hr hrOther;
	private String nameSearch;
	private StringBuilder consult;
	private UIDataTable dataTable;
	private List<Hr> hrList;
	private List<Hr> subHrList;
	private List<Hr> otherHrList;
	private List<TypeFood> typeFoodList;
	private List<Integer> otherQuantity;
	private List<FoodControl> listFoodControl;
	private List<Hr> listHrMealControl;
	private List<Date> listDateTable;
	private List<SelectItem> parameters;
	private HashMap<Integer, Integer> mealControlHashmap;
	boolean flagStart;

	@EJB
	private HrDao hrDao;
	@EJB
	private TypeFoodDao typeFoodDao;
	@EJB
	private DayTypeFoodDao dayTypeFoodDao;
	@EJB
	private MealControlDao mealControlDao;
	@EJB
	private HolidayDao holidayDao;
	@EJB
	AssistControlDao assistControlDao;

	/**
	 * @return pagination: Management paged meal control.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged meal control.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return initialDateSearch: gets the date of the meal control to search in
	 *         a range.
	 */
	public Date getInitialDateSearch() {
		return initialDateSearch;
	}

	/**
	 * @param initialDateSearch
	 *            : gets the date of the meal control to search in a range.
	 */
	public void setInitialDateSearch(Date initialDateSearch) {
		this.initialDateSearch = initialDateSearch;
	}

	/**
	 * @return hrOther: Human Resource for the meal control.
	 */
	public Hr getHrOther() {
		return hrOther;
	}

	/**
	 * @param hrOther
	 *            : Human Resource for the meal control.
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
	 * @return listFoodControl: List Of Food Control Object
	 */
	public List<FoodControl> getListFoodControl() {
		return listFoodControl;
	}

	/**
	 * @param listFoodControl
	 *            :List Of Food Control Object
	 */
	public void setListFoodControl(List<FoodControl> listFoodControl) {
		this.listFoodControl = listFoodControl;
	}

	/**
	 * @return listHrMealControl: List of human resources of meal control
	 */
	public List<Hr> getListHrMealControl() {
		return listHrMealControl;
	}

	/**
	 * @param listHrMealControl
	 */
	public void setListHrMealControl(List<Hr> listHrMealControl) {
		this.listHrMealControl = listHrMealControl;
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
	 * 
	 * @param parameters
	 *            : list of search parameters.
	 */
	public void setParameters(List<SelectItem> parameters) {
		this.parameters = parameters;
	}

	/**
	 * Method to edit or create a new meal control.
	 * 
	 * @return loadMealControl(): Method allows load and initializes values.
	 */
	public String addEditMealControl() {
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
		return loadMealControl();
	}

	/**
	 * This method allows load in the map the type food consulted in the
	 * database.
	 */
	private void loadTypeFoodInHr() throws Exception {
		typeFoodList = typeFoodDao.consultTypeFood();
		List<DayTypeFood> dayTypeFoodList = dayTypeFoodDao
				.consultDayTypeFood(ControladorFechas.formatDate(
						this.initialDateSearch,
						Constantes.DATE_FORMAT_DAY_OF_WEEK, Locale.US));
		mealControlHashmap = new HashMap<Integer, Integer>();
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
								mealControlHashmap.put(typeFood.getId(), 1);
								otherQuantity.add(1);
								break;
							}
						}
					}
					if (!mealControlHashmap.containsKey(typeFood.getId())) {
						mealControlHashmap.put(typeFood.getId(), 0);
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
	public String loadMealControl() {
		try {
			pagination = new Paginador();
			this.nameSearch = "";
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchHr();
	}

	/**
	 * See the list of workers to meal control.
	 * 
	 * @return regMealControl: Navigation rule that redirects to register the
	 *         meal control.
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
						hr.setMealControl(mealControlHashmap);
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
		return "regMealControl";
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
	 * This Method build dataTable for meal control, add food type columns.
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
	 * @return loadMealControl(): Method allows load and initializes values.
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
		return loadMealControl();
	}

	/**
	 * Method used to save the meal control.
	 * 
	 * @return initializeMealControl(): Redirects to manage meal control with
	 *         meal control updated.
	 */
	public String saveMealControl() {
		try {
			hrList = hrDao.consultHrListByAssistControl(ControladorFechas
					.formatDate(initialDateSearch,
							Constantes.DATE_FORMAT_CONSULT));
			if (hrList != null && hrList.size() > 0) {
				for (Hr hr : hrList) {
					for (TypeFood typeFood : typeFoodList) {
						String date = ControladorFechas.formatDate(
								initialDateSearch,
								Constantes.DATE_FORMAT_CONSULT);
						FoodControl foodControl = mealControlDao
								.consultMealControlByIdTypeIdHrAndDate(
										typeFood.getId(), hr.getIdHr(), date);
						foodControl.setHr(hr);
						foodControl.setTypeFood(typeFood);
						foodControl.setQuantity(typeFood.getId());
						foodControl.setDate(initialDateSearch);
						if (foodControl.getId() > 0) {
							mealControlDao.editFoodControl(foodControl);
						} else {
							mealControlDao.saveFoodControl(foodControl);
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
						foodControl.setQuantity(typeFood.getId());
						foodControl.setDate(initialDateSearch);
						mealControlDao.saveFoodControl(foodControl);
					}
				}
			}
			ControladorContexto.mensajeInfoEspecifico(
					"meal_control_message_meal_control_saved",
					"messageHumanResources");
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeMealControl();
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultMealControl: Method that consultation meal control list
	 *         and load the template with the information found.
	 */
	public String initializeMealControl() {
		pagination = new Paginador();
		return consultMealControl();
	}

	/**
	 * Consult the the list meal control to show in the view and return
	 * navigation rule.
	 * 
	 * @return gesMealControl: Navigation rule that redirects to manage the meal
	 *         control.
	 */
	public String consultMealControl() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHr = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validate = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		AssistControlAction assistControlAction = ControladorContexto
				.getContextBean(AssistControlAction.class);
		this.listHrMealControl = new ArrayList<Hr>();
		String searchMessages = "";
		StringBuilder unionSearchMessages = new StringBuilder();
		try {
			assistControlAction.setSource(false);
			assistControlAction.consultAssistControl();
			pagination = assistControlAction.getPagination();
			unionSearchMessages = assistControlAction.getUnionSearchMessages();
			this.listHrMealControl = assistControlAction
					.getListHrAssistControl();
			if (listHrMealControl != null) {
				this.listDateTable = assistControlAction.getListDateTable();
				associateTypeFood(this.consult, this.parameters);
				buildDataTable();
			}
			if ((listHrMealControl == null || listHrMealControl.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (listHrMealControl == null
					|| listHrMealControl.size() <= 0) {
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
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesMealControl";
	}

	/**
	 * This method allows add the novelty of the assist control according with
	 * the human resource
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @throws Exception
	 */
	private void associateTypeFood(StringBuilder consult,
			List<SelectItem> parameters) throws Exception {
		for (Hr hr : listHrMealControl) {
			int idHr = hr.getIdHr();
			List<FoodControl> listMealControlAux = mealControlDao
					.listHrOfMealControl(idHr, consult, parameters);
			if (listMealControlAux != null) {
				HashMap<Integer, Integer> hasmapAux = new HashMap<Integer, Integer>();
				for (FoodControl fc : listMealControlAux) {
					Date dateAssist = ControladorFechas.formatearFecha(
							fc.getDate(), Constantes.DATE_FORMAT_CONSULT);
					int idFoodType = fc.getTypeFood().getId();
					Integer i = (int) (dateAssist.getTime() / 1000)
							+ idFoodType;
					int val = fc.getQuantity();
					hasmapAux.put(i, val);
				}
				hr.setAssistFoodControl(hasmapAux);
			}
		}
	}

	/**
	 * This Method build dataTable for assist control, Add columns from date of
	 * assist
	 * 
	 * @throws Exception
	 * 
	 */
	public void buildDataTable() throws Exception {
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
			for (Date date : this.listDateTable) {
				Date dateL = ControladorFechas.formatearFecha(date,
						Constantes.DATE_FORMAT_CONSULT);
				Integer d = (int) (dateL.getTime() / 1000);
				TypeFood typeFoodBreak = typeFoodDao.nameExists(
						Constantes.TYPE_FOOD_BREAKFAST, 0);
				int valBreak = d + typeFoodBreak.getId();
				UIColumn columnBreak = (UIColumn) ControladorContexto
						.getApplication().createComponent(
								UIColumn.COMPONENT_TYPE);
				columnBreak.setStyleClass("center colFecha");
				HtmlOutputText valueBreak = new HtmlOutputText();
				valueBreak.setValue(bundle.getString("label_breakfast"));
				columnBreak.setHeader(valueBreak);
				HtmlOutputText outBreak = new HtmlOutputText();
				String mergeBreak = "hr.assistFoodControl[(" + valBreak
						+ ").intValue()] ";
				ValueExpression valExpBreak = ControladorGenerico
						.getValueExpression(mergeBreak, null);
				outBreak.setEscape(false);
				outBreak.setValueExpression(ConstantesErp.VALUE, valExpBreak);
				columnBreak.getChildren().add(outBreak);
				dataTable.getChildren().add(columnBreak);

				TypeFood typeFoodLunch = typeFoodDao.nameExists(
						Constantes.TYPE_FOOD_LUNCH, 0);
				int valLunch = d + typeFoodLunch.getId();
				UIColumn columnLunch = (UIColumn) ControladorContexto
						.getApplication().createComponent(
								UIColumn.COMPONENT_TYPE);
				columnLunch.setStyleClass("center colFecha");
				HtmlOutputText valueLunch = new HtmlOutputText();
				valueLunch.setValue(bundle.getString("label_lunch"));
				columnLunch.setHeader(valueLunch);
				HtmlOutputText outLunch = new HtmlOutputText();
				String mergeLunch = "hr.assistFoodControl[(" + valLunch
						+ ").intValue()] ";
				ValueExpression valExpLunch = ControladorGenerico
						.getValueExpression(mergeLunch, null);
				outLunch.setEscape(false);
				outLunch.setValueExpression(ConstantesErp.VALUE, valExpLunch);
				columnLunch.getChildren().add(outLunch);
				dataTable.getChildren().add(columnLunch);

				TypeFood typeFoodDinner = typeFoodDao.nameExists(
						Constantes.TYPE_FOOD_DINNER, 0);
				int valDinner = d + typeFoodDinner.getId();
				UIColumn columnDinner = (UIColumn) ControladorContexto
						.getApplication().createComponent(
								UIColumn.COMPONENT_TYPE);
				columnDinner.setStyleClass("center colFecha");
				HtmlOutputText valueDinner = new HtmlOutputText();
				valueDinner.setValue(bundle.getString("label_dinner"));
				columnDinner.setHeader(valueDinner);
				HtmlOutputText outDinner = new HtmlOutputText();
				String mergeDinner = "hr.assistFoodControl[(" + valDinner
						+ ").intValue()] ";
				ValueExpression valExpDinner = ControladorGenerico
						.getValueExpression(mergeDinner, null);
				outDinner.setEscape(false);
				outDinner.setValueExpression(ConstantesErp.VALUE, valExpDinner);
				columnDinner.getChildren().add(outDinner);
				dataTable.getChildren().add(columnDinner);
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
		AssistControlAction assistControlAction = ControladorContexto
				.getContextBean(AssistControlAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		try {
			assistControlAction.advanceSearch(consult, parameters, null, null,
					false);
			List<FoodControl> listFoodControl = mealControlDao
					.listHrOfMealControl(0, consult, parameters);

			List<Date> listDate = assistControlDao.consultAssistControlDates(
					consult, parameters, "FoodControl");
			Long countD = 0L;
			for (Date d : listDate) {
				Date initialD = ControladorFechas.finDeDia(d);
				Date finalD = ControladorFechas.inicioDeDia(d);
				Long countW = mealControlDao.countDateMealControl(initialD,
						finalD);
				countD += countW;
			}
			reportsController
					.generateReportFoodControl(listFoodControl, countD);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}
}