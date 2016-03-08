package co.informatix.erp.kpi.action;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.utils.ControladorContexto;

/**
 * This class manages the business logic of the filters for the bean index
 * reports.
 * 
 * @author Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class BeanIndexFiltersAction implements Serializable {

	/**
	 * Allows to find out the filters on parameters table and load the list with
	 * this filters.
	 * 
	 * @modify 08/03/2016 Andres.Gomez
	 * 
	 * @return rptGeneralTrend : navigation rule to show the report of the
	 *         general trend
	 */
	public String initBeanIndex() {
		try {
			filtersChange(1);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e, null, null);
		}
		return "rptGeneralTrend";
	}

	/**
	 * Allows to find out the filters on parameters table and load the list with
	 * this filters.
	 * 
	 * @author Andres.Gomez
	 * 
	 * @return rptBySection: navigation rule to show the report of by section
	 */
	public String initBeanIndexBySection() {
		try {
			filtersChange(2);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e, null, null);
		}
		return "rptBySection";
	}

	/**
	 * This method allows to execute the consult to create the reports.
	 * 
	 * @modify 2016/01/19 Andres.Gomez
	 * @param type
	 *            : integer that indicate the chart type.
	 */
	public void filtersChange(int type) {
		try {
			GeneralTrendAction generalTrendAction = ControladorContexto
					.getContextBean(GeneralTrendAction.class);
			generalTrendAction.initializeCropDefault();
			if (type == 1) {
				generalTrendAction.consultGeneralTrend();
			}
			if (type == 2) {
				generalTrendAction.consultBySection();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e, null, null);
		}
	}
}
