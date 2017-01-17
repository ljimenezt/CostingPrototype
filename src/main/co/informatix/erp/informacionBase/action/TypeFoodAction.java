package co.informatix.erp.informacionBase.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.action.DayTypeFoodAction;
import co.informatix.erp.humanResources.dao.MealControlDao;
import co.informatix.erp.humanResources.entities.DayTypeFood;
import co.informatix.erp.informacionBase.dao.TypeFoodDao;
import co.informatix.erp.informacionBase.entities.Day;
import co.informatix.erp.informacionBase.entities.TypeFood;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the typeFood that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TypeFoodAction implements Serializable {

	private Paginador pagination = new Paginador();
	private TypeFood typeFood;
	private TypeFood typeFoodActualSelected;
	private DayTypeFood dayTypeFood;
	private DayTypeFoodAction dayTypeFoodAction;
	private DayAction dayAction;
	private String nameSearch;
	private List<TypeFood> typeFoodList;
	private List<DayTypeFood> dayTypeFoodList;
	private boolean start;

	@EJB
	private TypeFoodDao typeFoodDao;
	@EJB
	private MealControlDao mealControlDao;

	/**
	 * @return pagination: Management paged typeFood list.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged typeFood list.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return typeFood: Object of class typeFood for register o edit.
	 */
	public TypeFood getTypeFood() {
		return typeFood;
	}

	/**
	 * @param typeFood
	 *            : Object of class typeFood for register o edit.
	 */
	public void setTypeFood(TypeFood typeFood) {
		this.typeFood = typeFood;
	}

	/**
	 * @return typeFoodSelected: typeFood selected in the typeFood table.
	 */
	public TypeFood getTypeFoodActualSelected() {
		return typeFoodActualSelected;
	}

	/**
	 * @param typeFoodActualSelected
	 *            : typeFood selected in the typeFood table.
	 */
	public void setTypeFoodActualSelected(TypeFood typeFoodActualSelected) {
		this.typeFoodActualSelected = typeFoodActualSelected;
	}

	/**
	 * @return dayTypeFood: Object of class dayTypeFood for register or delete.
	 */
	public DayTypeFood getDayTypeFood() {
		return dayTypeFood;
	}

	/**
	 * @param dayTypeFood
	 *            : Object of class dayTypeFood for register or delete.
	 */
	public void setDayTypeFood(DayTypeFood dayTypeFood) {
		this.dayTypeFood = dayTypeFood;
	}

	/**
	 * @return nameSearch: TypeFood name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : TypeFood name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return typeFoodList: typeFood list query the database.
	 */
	public List<TypeFood> getTypeFoodList() {
		return typeFoodList;
	}

	/**
	 * @param typeFoodList
	 *            : typeFood list query the database.
	 */
	public void setTypeFoodList(List<TypeFood> typeFoodList) {
		this.typeFoodList = typeFoodList;
	}

	/**
	 * @return dayTypeFoodList: dayTypeFood List query the database.
	 */
	public List<DayTypeFood> getDayTypeFoodList() {
		return dayTypeFoodList;
	}

	/**
	 * @return start: This field allows to validate if added or modified a
	 *         typeFood.
	 */
	public boolean isStart() {
		return start;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultTypeFood(): Method that consultation typeFood and load the
	 *         template with the information found.
	 */
	public String initializeTypeFood() {
		this.nameSearch = "";
		this.start = false;
		this.typeFoodActualSelected = null;
		if (ControladorContexto.getFacesContext() != null) {
			this.dayTypeFoodAction = ControladorContexto
					.getContextBean(DayTypeFoodAction.class);
			this.dayTypeFoodAction.setFlag(false);
		}
		return consultTypeFood();
	}

	/**
	 * Method to edit or create a new assignment of typeFood.
	 * 
	 * @param typeFood
	 *            :Object of typeFood are adding or editing.
	 * @return regTypeFood: Template redirects to register typeFood.
	 */
	public String addEditTypeFood(TypeFood typeFood) {
		if (ControladorContexto.getFacesContext() != null) {
			this.dayTypeFoodAction = ControladorContexto
					.getContextBean(DayTypeFoodAction.class);
			this.dayAction = ControladorContexto
					.getContextBean(DayAction.class);
			this.dayAction.setDayListSelected(new ArrayList<Day>());
			this.dayTypeFoodAction.setFlag(false);
		}
		if (typeFood != null) {
			this.typeFood = typeFood;
			selectTypeFood(typeFood);
			showDayTypeFood();
		} else {
			this.typeFood = new TypeFood();
			this.dayTypeFoodAction.setTypeFoodSelected(this.typeFood);
			this.dayTypeFoodAction.setDayTypeFoodList(null);
			this.dayTypeFoodAction.setSubDayTypeFoodList(null);
		}
		this.start = true;
		return "regTypeFood";
	}

	/**
	 * See the existing typeFood list.
	 * 
	 * @return "gesTypeFood": It redirects to the template to manage the type
	 *         food.
	 */
	public String consultTypeFood() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleBaseInformation = ControladorContexto
				.getBundle("messageBaseInformation");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		typeFoodList = new ArrayList<TypeFood>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = typeFoodDao.quantityTypeFood(query, parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
				typeFoodList = typeFoodDao.consultTypeFood(
						pagination.getInicio(), pagination.getRango(), query,
						parameters);
			}
			if ((typeFoodList == null || typeFoodList.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (typeFoodList == null || typeFoodList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleBaseInformation
										.getString("type_food_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesTypeFood";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
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
			consult.append("WHERE UPPER(t.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to delete a typeFood of the database.
	 * 
	 * @return initializeTypeFood(): Redirects to manage the typeFood list with
	 *         typeFood updated.
	 */
	public String deleteTypeFood() {
		try {
			if (!mealControlDao
					.consultMealControlByIdTypeFood(typeFood.getId())) {
				dayTypeFoodAction.deleteAllDayTypeFood(typeFood);
				typeFoodDao.removeTypeFood(typeFood);
				ControladorContexto.mensajeInfoArg2(
						"message_registro_eliminar", "mensaje",
						typeFood.getName());
			} else {
				ControladorContexto.mensajeInfoArg2(
						"type_food_message_can_not_eliminate_type_food",
						"messageBaseInformation", typeFood.getName());
			}

		} catch (EJBException e) {
			ControladorContexto.mensajeErrorArg2(
					"message_existe_relacion_eliminar", "mensaje",
					typeFood.getName());
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeTypeFood();
	}

	/**
	 * Selects a single typeFood for display the associated transaction.
	 * 
	 * @param typeFoodSelected
	 *            : typeFood selected on the view.
	 */
	public void selectTypeFood(TypeFood typeFoodSelected) {
		this.typeFoodActualSelected = new TypeFood();
		typeFoodSelected.setSelected(true);
		for (TypeFood typeFood : typeFoodList) {
			if (typeFood.isSelected()) {
				if (typeFood.getId() == typeFoodSelected.getId()) {
					this.typeFoodActualSelected = typeFood;
				} else {
					typeFood.setSelected(false);
				}
			}
		}
	}

	/**
	 * Show the dayTypeFood associated to typeFood.
	 */
	public void showDayTypeFood() {
		if (this.dayTypeFoodAction != null) {
			dayTypeFoodAction.setTypeFoodSelected(typeFoodActualSelected);
			dayTypeFoodAction.initializeDayTypeFood();
		}
	}

	/**
	 * Method used to save or edit typeFood
	 * 
	 * @return initializeTypeFood(): Redirects to manage the typeFood list with
	 *         typeFood updated.
	 */
	public String saveUpdateTypeFood() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "message_registro_modificar";
		try {

			if (typeFood.getId() != 0) {
				typeFoodDao.editTypeFood(typeFood);
			} else {
				typeFoodDao.saveTypeFood(typeFood);
				message = "message_registro_guardar";
			}
			dayTypeFoodAction.saveEditDayTypeFood();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(message),
							typeFood.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeTypeFood();
	}

	/**
	 * To validate the name of the typeFood, so it is not repeated in the
	 * database and it validates against XSS.
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNameXSS(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			int id = typeFood.getId();
			TypeFood typeFoodAux = typeFoodDao.nameExists(name, id);
			if (typeFoodAux != null) {
				String messageExistence = "message_ya_existe_verifique";
				context.addMessage(
						clientId,
						new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
								.getString(messageExistence), null));
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(name, clientId,
					"locate.regex.letras.numeros")) {
				((UIInput) toValidate).setValid(false);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows validate the required fields.
	 */
	public void validateRequired() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("messageBaseInformation");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		if (this.typeFood.getName() == null
				|| ("").equals(this.typeFood.getName())) {
			ControladorContexto
					.mensajeRequeridos("formRegisterTypeFood:txtName");
		}
		if (dayTypeFoodAction.getDayTypeFoodList() == null
				|| dayTypeFoodAction.getDayTypeFoodList().size() == 0) {
			validations.setMensajeBusquedaPopUp(bundle
					.getString("type_food_message_must_add_days"));
		}
	}

	/**
	 * This method allows consult the dayTypeFood list associated to a typeFood
	 * and save in a day list.
	 * 
	 * @return dayAction.initializeDay(): Redirects to initialize variables and
	 *         load day list.
	 */
	public String initializeDay() {
		try {
			if (dayTypeFoodAction.getDayTypeFoodList() != null
					&& dayTypeFoodAction.getDayTypeFoodList().size() > 0) {
				dayAction.setDayListSelected(new ArrayList<Day>());
				for (DayTypeFood dayTypeFood : dayTypeFoodAction
						.getDayTypeFoodList()) {
					if (!dayTypeFood.isAfterHoliday()) {
						dayAction.getDayListSelected()
								.add(dayTypeFood.getDay());
					} else {
						dayAction.setFlagHoliday(true);
					}
				}
			} else {
				dayAction.setDayListSelected(new ArrayList<Day>());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		dayAction.setTypeFoodActualSelected(this.typeFood);
		return dayAction.initializeDay();
	}

	/**
	 * Selects a single day for display the associated transaction.
	 * 
	 * @param day
	 *            : Day selected on the view.
	 */
	public void selectDay(Day day) {
		ResourceBundle bundleBaseInformation = ControladorContexto
				.getBundle("messageBaseInformation");
		String nameDay = bundleBaseInformation
				.getString("type_food_label_after_holiday");
		if (!day.isSelected()) {
			this.dayAction.getDayListSelected().add(day);
			day.setSelected(true);
		} else {
			this.dayAction.getDayListSelected().remove(day);
			day.setSelected(false);
		}
		if (day.getName().contains(nameDay)) {
			dayAction.setFlagHoliday(day.isSelected());
		}
	}

	/**
	 * This method allows add days to a typeFood.
	 */
	public void addDaysToTypeFood() {
		dayTypeFoodAction.setDayTypeFoodList(new ArrayList<DayTypeFood>());
		ResourceBundle bundleBaseInformation = ControladorContexto
				.getBundle("messageBaseInformation");
		String nameDay = bundleBaseInformation
				.getString("type_food_label_after_holiday");
		if (this.dayAction.getDayListSelected() != null
				&& this.dayAction.getDayListSelected().size() > 0) {
			for (Day day : this.dayAction.getDayListSelected()) {
				DayTypeFood dayTypeFood = new DayTypeFood();
				dayTypeFood.setDay(day);
				if (day.getName().contains(nameDay)) {
					dayTypeFood.setAfterHoliday(dayAction.isFlagHoliday());
				}
				dayTypeFoodAction.getDayTypeFoodList().add(dayTypeFood);
			}
		}
		dayTypeFoodAction.setFlag(true);
		dayTypeFoodAction.initializeDayTypeFood();
	}

	/**
	 * Method to delete a dayTypeFood associate to a typeFood.
	 */
	public void deleteDayTypeFood() {
		dayTypeFoodAction.getDayTypeFoodList().remove(dayTypeFood);
		dayTypeFoodAction.setFlag(true);
		dayTypeFoodAction.consultDayTypeFood();
	}

}