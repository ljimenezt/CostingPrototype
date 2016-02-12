package co.informatix.erp.utils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.event.ValueChangeEvent;

import org.richfaces.component.SortOrder;

/**
 * Class that allows business logic to sort by field Selected by the user in the
 * rich: extendedDataTable. By default this Without multiple system..
 * 
 * For use from the presentation layer you should:
 * 
 * Add property sortPriority = "# {} controladorOrdenarCampo.sortPriorities" In
 * extendedDataTable.
 * 
 * In rich: column to order must: add the property SortBy =
 * "# {} varExtendedDataTable.nombreCampo" SortOrder =
 * "# {controladorOrdenarCampo.sortsOrders ['fieldName']}".
 * 
 * In the action where it is called the order Action =
 * "# {controladorOrdenarCampo.sort}" must send the parameter: <F: param name =
 * "sortProperty" value = "fieldName" />
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ControllerSortField implements Serializable {

	private List<String> sortPriorities;

	private Map<String, SortOrder> sortsOrders;

	private String property;
	private String order;

	private boolean multipleSorting = false;

	private static final String SORT_PROPERTY_PARAMETER = Constantes.SORT_PROPERTY;

	/**
	 * Class constructor.
	 */
	public ControllerSortField() {
		sortsOrders = new HashMap<String, SortOrder>();
		sortPriorities = new ArrayList<String>();
	}

	/**
	 * @return multipleSorting: variable that lets you know if multiple System
	 *         with true or false otherwise.
	 */
	public boolean isMultipleSorting() {
		return multipleSorting;
	}

	/**
	 * @param multipleSorting
	 *            : variable that lets you know if multiple System with true or
	 *            false otherwise.
	 */
	public void setMultipleSorting(boolean multipleSorting) {
		this.multipleSorting = multipleSorting;
	}

	/**
	 * @return sortPriorities: variable that stores the properties or name
	 *         Fields in the event that multiple system.
	 */
	public List<String> getSortPriorities() {
		return sortPriorities;
	}

	/**
	 * @return sortsOrders: Map containing the field and property Descending or
	 *         ascending when ordering.
	 */
	public Map<String, SortOrder> getSortsOrders() {
		return sortsOrders;
	}

	/**
	 * @return property: Property system.
	 */
	public String getProperty() {
		return property;
	}

	/**
	 * @param property
	 *            : Property system.
	 */
	public void setProperty(String property) {
		this.property = property;
	}

	/**
	 * @return order: Indicates the type of system.
	 */
	public String getOrder() {
		return order;
	}

	/**
	 * Method to sort the selected field.
	 */
	public void sort() {
		String propertyParameter = ControladorContexto
				.getParam(SORT_PROPERTY_PARAMETER);
		if (propertyParameter != null) {
			property = propertyParameter;
			SortOrder currentPropertySortOrder = sortsOrders.get(property);
			if (multipleSorting) {
				if (!sortPriorities.contains(property)) {
					sortPriorities.add(property);
				}
			} else {
				sortsOrders.clear();
			}
			if (currentPropertySortOrder == null
					|| currentPropertySortOrder.equals(SortOrder.descending)) {
				order = Constantes.DESCENDING;
				sortsOrders.put(property, SortOrder.ascending);
			} else {
				order = Constantes.ASCENDING;
				sortsOrders.put(property, SortOrder.descending);
			}
		}
	}

	/**
	 * Cleaning method that allows the values when the change is made Multiple
	 * system.
	 * 
	 * @param event
	 *            : running event.
	 */
	public void modeChanged(ValueChangeEvent event) {
		reset();
	}

	/**
	 * Cleaning method that allows system variables.
	 */
	public void reset() {
		sortPriorities.clear();
		sortsOrders.clear();
		this.order = null;
		this.property = null;
	}
}