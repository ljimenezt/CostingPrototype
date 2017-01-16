package co.informatix.erp.informacionBase.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.informacionBase.dao.DayDao;
import co.informatix.erp.informacionBase.entities.Day;
import co.informatix.erp.informacionBase.entities.TypeFood;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the day that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DayAction implements Serializable {

	private Paginador pagination = new Paginador();
	private TypeFood typeFoodActualSelected;
	private String nameSearch;
	private List<Day> dayList;
	private List<Day> subDayList;
	private List<Day> dayListSelected;
	private boolean flagHoliday;
	private boolean flagStart;

	@EJB
	private DayDao dayDao;

	/**
	 * @return pagination: Management paged day list.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged day list.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
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
	 * @return nameSearch: Day name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Day name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return dayList: day list query the database.
	 */
	public List<Day> getDayList() {
		return dayList;
	}

	/**
	 * @param dayList
	 *            : day list query the database.
	 */
	public void setDayList(List<Day> dayList) {
		this.dayList = dayList;
	}

	/**
	 * @return subDayList: day list query the database.
	 */
	public List<Day> getSubDayList() {
		return subDayList;
	}

	/**
	 * @param subDayList
	 *            : day list query the database.
	 */
	public void setSubDayList(List<Day> subDayList) {
		this.subDayList = subDayList;
	}

	/**
	 * @return dayListSelected: gets the list of selected days.
	 */
	public List<Day> getDayListSelected() {
		return dayListSelected;
	}

	/**
	 * @param dayListSelected
	 *            : gets the list of selected days.
	 */
	public void setDayListSelected(List<Day> dayListSelected) {
		this.dayListSelected = dayListSelected;
	}

	/**
	 * @return flagHoliday: This field is valid if you have selected the holiday
	 *         in the popup.
	 */
	public boolean isFlagHoliday() {
		return flagHoliday;
	}

	/**
	 * @param flagHoliday
	 *            : This field is valid if you have selected the holiday in the
	 *            popup.
	 */
	public void setFlagHoliday(boolean flagHoliday) {
		this.flagHoliday = flagHoliday;
	}

	/**
	 * @return flagStart: This field allows validate the initialize the consult
	 *         by popup.
	 */
	public boolean isFlagStart() {
		return flagStart;
	}

	/**
	 * @param flagStart
	 *            : This field allows validate the initialize the consult by
	 *            popup.
	 */
	public void setFlagStart(boolean flagStart) {
		this.flagStart = flagStart;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultDay(): Method that consultation day and load the template
	 *         with the information found.
	 */
	public String initializeDay() {
		this.nameSearch = "";
		this.flagStart = false;
		return consultDay();
	}

	/**
	 * See the existing day list.
	 * 
	 * @return returns: It redirects to the template to manage the type food.
	 */
	public String consultDay() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		String param2 = ControladorContexto.getParam("param2");
		boolean fromModal = (param2 != null && Constantes.SI.equals(param2)) ? true
				: false;
		String returns = fromModal ? "" : "gesDay";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			if (!fromModal) {
				dayList = new ArrayList<Day>();
				Long quantity = dayDao.quantityDay(query, parameters);
				if (quantity != null) {
					pagination.paginar(quantity);
					dayList = dayDao.consultDay(pagination.getInicio(),
							pagination.getRango(), query, parameters);
				}
			} else {
				if (!flagStart) {
					consultDaysAndAddHoliday(query, parameters);
				}
				maintainDaySelected();
				subDayList = new ArrayList<Day>();
				if (dayList != null && dayList.size() > 0) {
					long amount = (long) this.dayList.size();
					this.pagination.paginarRangoDefinido(amount, 5);
					int start = this.pagination.getItemInicial() - 1;
					int end = this.pagination.getItemFinal();
					this.subDayList = this.dayList.subList(start, end);
				}
				flagStart = true;
			}
			if ((dayList == null || dayList.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (dayList == null || dayList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundle.getString("label_day"),
								unionMessagesSearch);
			}
			if (fromModal) {
				validations.setMensajeBusquedaPopUp(messageSearch);
			} else {
				validations.setMensajeBusqueda(messageSearch);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return returns;
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
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
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append("WHERE UPPER(d.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * This method allows maintaining list of selected day whether the search of
	 * day is run again.
	 */
	private void maintainDaySelected() {
		for (Day day : dayList) {
			for (Day daySeleceted : dayListSelected) {
				if (day.getId() == daySeleceted.getId()) {
					day.setSelected(true);
				}
			}
		}
	}

	/**
	 * This method allows consult days and add holiday to the list.
	 * 
	 * @param query
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @throws Exception
	 */
	private void consultDaysAndAddHoliday(StringBuilder query,
			List<SelectItem> parameters) throws Exception {
		dayList = dayDao.consultDay(query, parameters);
		ResourceBundle bundleBaseInformation = ControladorContexto
				.getBundle("messageBaseInformation");
		String nameDay = bundleBaseInformation
				.getString("type_food_label_after_holiday");
		if (this.nameSearch.equals("")
				|| nameDay.toUpperCase()
						.contains(this.nameSearch.toUpperCase())) {
			Day dayHoliday = new Day();
			dayHoliday.setName(nameDay);
			dayHoliday.setSelected(flagHoliday);
			if (!flagStart) {
				dayList.add(dayHoliday);
				if (flagHoliday && !dayListSelected.contains(dayHoliday)) {
					dayListSelected.add(dayHoliday);
				}
			}
		}
	}
}
