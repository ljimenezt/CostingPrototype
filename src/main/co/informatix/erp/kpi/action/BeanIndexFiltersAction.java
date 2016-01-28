package co.informatix.erp.kpi.action;

import java.io.Serializable;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.utils.Constantes;
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

	private List<SelectItem> itemsCropNames;

	private Date startDateFilter;
	private Date endDateFilter;

	private String cropNameSelected;

	private Crops selectedCrop;

	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private CropsDao cropsDao;

	/**
	 * @return startDateFilter: Start date selected in the user interface.
	 */
	public Date getStartDateFilter() {
		return startDateFilter;
	}

	/**
	 * @param startDateFilter
	 *            : Start date selected in the user interface.
	 */
	public void setStartDateFilter(Date startDateFilter) {
		this.startDateFilter = startDateFilter;
	}

	/**
	 * @return endDateFilter: End date selected in the user interface.
	 */
	public Date getEndDateFilter() {
		return endDateFilter;
	}

	/**
	 * @param endDateFilter
	 *            : End date selected in the user interface.
	 */
	public void setEndDateFilter(Date endDateFilter) {
		this.endDateFilter = endDateFilter;
	}

	/**
	 * @return itemsCropNames: Items of crop names to load in the combo to
	 *         filter the charts on the reports.
	 */
	public List<SelectItem> getItemsCropNames() {
		return itemsCropNames;
	}

	/**
	 * @return cropNameSelected: Selected crop name in the user interface.
	 */
	public String getCropNameSelected() {
		return cropNameSelected;
	}

	/**
	 * @param cropNameSelected
	 *            : Selected crop name in the user interface.
	 */
	public void setCropNameSelected(String cropNameSelected) {
		this.cropNameSelected = cropNameSelected;
	}

	/**
	 * @return selectedCrop: Selected crop in the user interface.
	 */
	public Crops getSelectedCrop() {
		return selectedCrop;
	}

	/**
	 * @param selectedCrop
	 *            : Selected crop in the user interface.
	 */
	public void setSelectedCrop(Crops selectedCrop) {
		this.selectedCrop = selectedCrop;
	}

	/**
	 * Allows to find out the filters on parameters table and load the list with
	 * this filters.
	 */
	public void initBeanIndex() {
		try {
			loadFilterItems();
			filtersChange(1);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e, null, null);
		}
	}

	/**
	 * Allows to find out the filters on parameters table and load the list with
	 * this filters.
	 * 
	 * @author Andres.Gomez
	 */
	public void initBeanIndexBySection() {
		try {
			loadFilterItems();
			filtersChange(2);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e, null, null);
		}
	}

	/**
	 * This method allows to load the dropdowns lists for the filters on the
	 * bean index reports page.
	 * 
	 * @throws Exception
	 */
	private void loadFilterItems() throws Exception {

		this.itemsCropNames = new ArrayList<SelectItem>();
		List<CropNames> cropNames = cropNamesDao.listaCropNames();
		if (cropNames != null) {
			for (CropNames cropName : cropNames) {
				itemsCropNames.add(new SelectItem(cropName.getIdCropName(),
						cropName.getCropName()));
			}
			selectDefaultCrop();
		}

	}

	/**
	 * This method allows to select the CCN-51 cocoa crop as default. If any
	 * other crop is needed as the default, update this method.
	 * 
	 * @throws Exception
	 */
	private void selectDefaultCrop() throws Exception {
		this.selectedCrop = cropsDao.descriptionSearch(Constantes.COSECHA);
		if (selectedCrop != null) {
			this.cropNameSelected = Integer.toString(selectedCrop.getIdCrop());
		} else {
			this.cropNameSelected = (String) this.itemsCropNames.get(0)
					.getValue();
		}
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
