package co.informatix.erp.humanResources.action;

import java.io.Serializable;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.utils.Paginador;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the meal control that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MealControlAction implements Serializable {

	private Paginador pagination = new Paginador();
	private String nameSearch;

	@EJB
	private HrDao hrDao;

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

}