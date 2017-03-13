package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.DayTypeFoodDao;
import co.informatix.erp.humanResources.entities.DayTypeFood;
import co.informatix.erp.informacionBase.entities.TypeFood;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utils.ValidacionesAction.DatosGuardar;

/**
 * This class allows the logic of dayTypeFood which may be in the database. The
 * logic is to consult, edit or add dayTypeFood.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DayTypeFoodAction implements Serializable {

	private TypeFood typeFoodSelected;
	private Paginador pagination = new Paginador();
	private List<DayTypeFood> dayTypeFoodList;
	private List<DayTypeFood> subDayTypeFoodList;
	private String nameSearch;
	private boolean flag;

	@EJB
	private DayTypeFoodDao dayTypeFoodDao;

	/**
	 * @return typeFoodSelected: Object of class typeFood selected.
	 */
	public TypeFood getTypeFoodSelected() {
		return typeFoodSelected;
	}

	/**
	 * @param typeFoodSelected
	 *            : Object of class typeFood selected.
	 */
	public void setTypeFoodSelected(TypeFood typeFoodSelected) {
		this.typeFoodSelected = typeFoodSelected;
	}

	/**
	 * @return pagination: Management paged list of dayTypeFood.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of dayTypeFood.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return dayTypeFoodList: dayTypeFood List query the database.
	 */
	public List<DayTypeFood> getDayTypeFoodList() {
		return dayTypeFoodList;
	}

	/**
	 * @param dayTypeFoodList
	 *            : dayTypeFoodList query the database.
	 */
	public void setDayTypeFoodList(List<DayTypeFood> dayTypeFoodList) {
		this.dayTypeFoodList = dayTypeFoodList;
	}

	/**
	 * @return subDayTypeFoodList: dayTypeFood List query the database.
	 */
	public List<DayTypeFood> getSubDayTypeFoodList() {
		return subDayTypeFoodList;
	}

	/**
	 * @param subDayTypeFoodList
	 *            : dayTypeFoodList query the database.
	 */
	public void setSubDayTypeFoodList(List<DayTypeFood> subDayTypeFoodList) {
		this.subDayTypeFoodList = subDayTypeFoodList;
	}

	/**
	 * @return nameSearch: DayTypeFood name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : DayTypeFood name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return flag: This field allows to validate if added or modified a
	 *         typeFood.
	 */
	public boolean isFlag() {
		return flag;
	}

	/**
	 * @param flag
	 *            : This field allows to validate if added or modified a
	 *            typeFood.
	 */
	public void setFlag(boolean flag) {
		this.flag = flag;
	}

	/**
	 * Method to initialize the fields in the search.
	 */
	public void initializeDayTypeFood() {
		this.nameSearch = "";
		pagination = new Paginador();
		if (!flag) {
			dayTypeFoodList = new ArrayList<DayTypeFood>();
		}
		consultDayTypeFood();
	}

	/**
	 * Consult the dayTypeFood list.
	 */
	public void consultDayTypeFood() {
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		try {
			advancedSearch(consult, parameters);
			if (!flag) {
				this.dayTypeFoodList = dayTypeFoodDao.consultDayTypeFood(
						consult, parameters);
				DayTypeFood dayTypeFood = dayTypeFoodDao
						.consultAfterHoliday(typeFoodSelected.getId());
				if (dayTypeFood != null) {
					dayTypeFoodList.add(dayTypeFood);
				}
			}
			subDayTypeFoodList = new ArrayList<DayTypeFood>();
			if (dayTypeFoodList != null && dayTypeFoodList.size() > 0) {
				long amount = (long) this.dayTypeFoodList.size();
				this.pagination.paginarRangoDefinido(amount, 5);
				int start = this.pagination.getItemInicial() - 1;
				int end = this.pagination.getItemFinal();
				this.subDayTypeFoodList = this.dayTypeFoodList.subList(start,
						end);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
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
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters) {
		boolean queryAdded = false;
		if (this.typeFoodSelected != null) {
			consult.append("WHERE t.id = :id ");
			SelectItem item = new SelectItem(this.typeFoodSelected.getId(),
					"id");
			parameters.add(item);
			queryAdded = true;
		}
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append("UPPER(da.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
		}
	}

	/**
	 * This method identifies the dayTypeFood to delete and save.
	 * 
	 * @throws Exception
	 */
	public void saveEditDayTypeFood() {
		List<Integer> currentIds = new ArrayList<Integer>();
		List<Integer> newsIds = new ArrayList<Integer>();
		try {
			List<DayTypeFood> dayTypeFoodActualList = dayTypeFoodDao
					.consultDayTypeFoodByIdTypeFood(typeFoodSelected.getId(),
							true);
			boolean flagHoliday = false;
			if (this.dayTypeFoodList != null) {
				if (dayTypeFoodActualList != null) {
					for (DayTypeFood dayTypeFood : dayTypeFoodActualList) {
						currentIds.add(dayTypeFood.getDay().getId());
					}
				} else {
					dayTypeFoodActualList = new ArrayList<DayTypeFood>();
				}
				for (DayTypeFood dayTypeFood : dayTypeFoodList) {
					if (!dayTypeFood.isAfterHoliday()) {
						newsIds.add(dayTypeFood.getDay().getId());
					} else {
						flagHoliday = true;
					}
				}
				List<DatosGuardar> dataList = ValidacionesAction.validarListas(
						currentIds, newsIds);
				for (DatosGuardar saveData : dataList) {
					String action = saveData.getAccion();
					DayTypeFood dayTypeFood = new DayTypeFood();
					for (DayTypeFood dayTypeFoodSeleted : dayTypeFoodList) {
						if (dayTypeFoodSeleted.getDay() != null) {
							if (dayTypeFoodSeleted.getDay().getId() == saveData
									.getIdClase()) {
								dayTypeFood = dayTypeFoodSeleted;
								break;
							}
						}
					}
					dayTypeFood.setTypeFood(typeFoodSelected);
					if (Constantes.QUERY_DELETE.equals(action)) {
						dayTypeFood = dayTypeFoodDao
								.findDayTypeFoodByIds(typeFoodSelected.getId(),
										saveData.getIdClase());
						dayTypeFoodDao.deleteDayTypeFood(dayTypeFood);
					} else if (Constantes.QUERY_INSERT.equals(action)) {
						dayTypeFoodDao.saveDayTypeFood(dayTypeFood);
					} else {
						dayTypeFoodDao.editDayTypeFood(dayTypeFood);
					}
				}
				DayTypeFood dayTypeFood = dayTypeFoodDao
						.consultAfterHoliday(typeFoodSelected.getId());
				saveOrDeleteHoliday(dayTypeFood, flagHoliday);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * This method allows saved or delete the field with the after holiday true.
	 * 
	 * @param dayTypeFood
	 *            : dayTypeFood to save or delete in the database.
	 * @param flagHoliday
	 *            : If this field is true save the field in the database and if
	 *            false delete the field.
	 * @throws Exception
	 */
	private void saveOrDeleteHoliday(DayTypeFood dayTypeFood,
			boolean flagHoliday) throws Exception {
		if (flagHoliday && dayTypeFood == null) {
			dayTypeFood = new DayTypeFood();
			dayTypeFood.setTypeFood(typeFoodSelected);
			dayTypeFood.setAfterHoliday(flagHoliday);
			dayTypeFoodDao.saveDayTypeFood(dayTypeFood);
		} else if (!flagHoliday && dayTypeFood != null) {
			dayTypeFoodDao.deleteDayTypeFood(dayTypeFood);
		}
	}

	/**
	 * This method allows delete all dayTypeFood associates to a typeFood.
	 * 
	 * @param typeFood
	 *            : TypeFood to delete all dayTypeFood associates to this.
	 * @throws Exception
	 */
	public void deleteAllDayTypeFood(TypeFood typeFood) {
		try {
			List<DayTypeFood> dayTypeFoodActualList = dayTypeFoodDao
					.consultDayTypeFoodByIdTypeFood(typeFood.getId(), false);
			if (dayTypeFoodActualList != null) {
				for (DayTypeFood dayTypeFood : dayTypeFoodActualList) {
					dayTypeFoodDao.deleteDayTypeFood(dayTypeFood);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}
}
