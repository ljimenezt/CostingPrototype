package co.informatix.erp.warehouse.action;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.utils.Paginador;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the movements that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class MovementsAction implements Serializable {

	private List<Activities> activitiesList;
	private Paginador pagination = new Paginador();

	/**
	 * @return activitiesList: list of activity objects.
	 */
	public List<Activities> getActivitiesList() {
		return activitiesList;
	}

	/**
	 * @param activitiesList
	 *            : list of activity objects.
	 */
	public void setActivitiesList(List<Activities> activitiesList) {
		this.activitiesList = activitiesList;
	}

	/**
	 * @return pagination: Management paged list of activities.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of activities.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	// Field To Logic Expiration

}
