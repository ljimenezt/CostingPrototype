package co.informatix.erp.reports;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

/**
 * This class manage the reports of the bean index general trend.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class BeanIndexGeneralTrend implements Serializable {

	/**
	 * This method allow initialize the needles variables to create a report
	 * 
	 * @return rptGeneralTrend : display the view to manage the report
	 */
	public String initializeReport() {
		return "rptGeneralTrend";
	}
}
