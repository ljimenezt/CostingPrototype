package co.informatix.erp.kpi.action;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import org.apache.commons.lang3.text.WordUtils;

import co.informatix.erp.kpi.dao.BeanIndexDao;
import co.informatix.erp.lifeCycle.dao.CropNamesDao;
import co.informatix.erp.lifeCycle.dao.CropsDao;
import co.informatix.erp.lifeCycle.dao.SectionDao;
import co.informatix.erp.lifeCycle.entities.CropNames;
import co.informatix.erp.lifeCycle.entities.Crops;
import co.informatix.erp.lifeCycle.entities.Section;
import co.informatix.erp.reports.json.CellJson;
import co.informatix.erp.reports.json.ColsJson;
import co.informatix.erp.reports.json.DataJson;
import co.informatix.erp.reports.json.RowsJson;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Serializer;

/**
 * This class allows to generate the consults for the general trend reports.
 * 
 * @author Cristhian.Pico
 * @modify 08/03/2016 Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class GeneralTrendAction implements Serializable {

	private String dataGeneralTrend;
	private String dataBySection;

	private int firstCycle = 100;
	private int lastCycle = 0;

	private List<SelectItem> itemsCropNames;
	private List<SelectItem> itemsCrops;

	private Crops crops;

	@EJB
	private BeanIndexDao beanIndexDao;
	@EJB
	private SectionDao sectionDao;
	@EJB
	private CropNamesDao cropNamesDao;
	@EJB
	private CropsDao cropsDao;

	/**
	 * @return firstCycle: first cycle that will be shown on the report.
	 * 
	 */
	public int getFirstCycle() {
		return firstCycle;
	}

	/**
	 * @param firstCycle
	 *            : first cycle that will be shown on the report.
	 */
	public void setFirstCycle(int firstCycle) {
		this.firstCycle = firstCycle;
	}

	/**
	 * @return lastCycle: last cycle that will be shown on the report.
	 * 
	 */
	public int getLastCycle() {
		return lastCycle;
	}

	/**
	 * @param lastCycle
	 *            : last cycle that will be shown on the report.
	 */
	public void setLastCycle(int lastCycle) {
		this.lastCycle = lastCycle;
	}

	/**
	 * @return itemsCropNames: gets the list of the crop name.
	 */
	public List<SelectItem> getItemsCropNames() {
		return itemsCropNames;
	}

	/**
	 * @param itemsCropNames
	 *            :sets the list of the crop name.
	 */
	public void setItemsCropNames(List<SelectItem> itemsCropNames) {
		this.itemsCropNames = itemsCropNames;
	}

	/**
	 * @return itemsCrops: gets the crop list
	 */
	public List<SelectItem> getItemsCrops() {
		return itemsCrops;
	}

	/**
	 * @param itemsCrops
	 *            :sets the crop list
	 */
	public void setItemsCrops(List<SelectItem> itemsCrops) {
		this.itemsCrops = itemsCrops;
	}

	/**
	 * @return crops: Object of class crops.
	 */
	public Crops getCrops() {
		return crops;
	}

	/**
	 * @param crops
	 *            :Object of class crops.
	 */
	public void setCrops(Crops crops) {
		this.crops = crops;
	}

	/**
	 * @return dataGeneralTrend: This field contains the json data for General
	 *         Trend.
	 */
	public String getDataGeneralTrend() {
		return dataGeneralTrend;
	}

	/**
	 * @param dataGeneralTrend
	 *            : This field contains the json data for General Trend.
	 */
	public void setDataGeneralTrend(String dataGeneralTrend) {
		this.dataGeneralTrend = dataGeneralTrend;
	}

	/**
	 * @return dataBySection: This field contains the JSON data for By Section.
	 */
	public String getDataBySection() {
		return dataBySection;
	}

	/**
	 * @param dataBySection
	 *            :This field contains the JSON data for By Section.
	 */
	public void setDataBySection(String dataBySection) {
		this.dataBySection = dataBySection;
	}

	/**
	 * This method allows load the crops list to generate report according to
	 * crop
	 * 
	 * @throws Exception
	 * 
	 */
	public void initializeCropDefault() throws Exception {
		crops = new Crops();
		crops.setCropNames(new CropNames());
		crops = cropsDao.descriptionSearch(Constantes.COSECHA);
		fillCropNames();
	}

	/**
	 * CropNames method that loads a list
	 * 
	 * @author Andres.Gomez
	 * 
	 */
	private void fillCropNames() {
		itemsCropNames = new ArrayList<SelectItem>();
		try {
			List<CropNames> listCropNames = cropNamesDao.listCropNames();
			if (listCropNames != null) {
				for (CropNames cropNames : listCropNames) {
					itemsCropNames
							.add(new SelectItem(cropNames.getIdCropName(),
									cropNames.getCropName()));
				}
			}
			fillCropNamesCrop();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method allows complete the list of crops harvested after the name
	 * selected.
	 * 
	 * @author Andres.Gomez
	 */
	public void fillCropNamesCrop() {
		int idCropsName = 0;
		itemsCrops = new ArrayList<SelectItem>();
		try {
			if (this.crops != null && this.crops.getCropNames() != null) {
				idCropsName = this.crops.getCropNames().getIdCropName();
			}
			List<Crops> listaCrops = cropsDao
					.consultCropNamesCropsCurrent(idCropsName);
			if (listaCrops != null) {
				for (Crops crops : listaCrops) {
					itemsCrops.add(new SelectItem(crops.getIdCrop(), crops
							.getDescription()));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This methods allows arm the json with data for the general trend report
	 * 
	 */
	public void consultGeneralTrend() {
		ResourceBundle bundleReports = ControladorContexto
				.getBundle("mensajeReports");

		StringBuilder reportQuery = new StringBuilder();
		StringBuilder filters = new StringBuilder();
		StringBuilder queryGroupBy = new StringBuilder();
		List<SelectItem> params = new ArrayList<SelectItem>();

		String jsonGeneralTrend = "";
		DataJson dataJsonGenTrend = new DataJson();
		List<ColsJson> colsGenTrend = new ArrayList<ColsJson>();
		List<RowsJson> rowsGenTrend = new ArrayList<RowsJson>();
		int count = 0;
		try {
			String cycleLabel = bundleReports
					.getString("reports_label_cycle_number");
			String beanIndexLabel = bundleReports
					.getString("reports_label_trend_improvement");
			DataJson.addCol(colsGenTrend, cycleLabel, "string");
			DataJson.addCol(colsGenTrend, beanIndexLabel, "number");

			buildConsult(filters, params, reportQuery, queryGroupBy, 1);

			List<Object[]> beanIndexList = beanIndexDao
					.consultBeanIndexAverage(filters, params, reportQuery,
							queryGroupBy);
			List<Object[]> resultConsultBeanIndex = getResultConsultBeanIndex(
					beanIndexList, 1);
			int sizeList = resultConsultBeanIndex.size();
			List<String> cycleNumberList = buildCyclesList(firstCycle,
					lastCycle);

			if (cycleNumberList != null && resultConsultBeanIndex != null
					&& sizeList > 0) {

				for (String cycle : cycleNumberList) {
					Object[] beanIndexAvg = resultConsultBeanIndex.get(count);
					String cycleStr = beanIndexAvg[1].toString();
					String cycleNumberCol = (String.valueOf(cycle.charAt(0)) + String
							.valueOf(cycle.charAt(1))).trim();
					RowsJson row = new RowsJson();
					List<CellJson> cl = new ArrayList<CellJson>();
					DataJson.addCel(cl, cycle);
					if (!cycleNumberCol.equals(cycleStr)) {
						DataJson.addCel(cl, 0);
						DataJson.addCel(cl, 0);
					} else {
						double sampleAverage = Double
								.parseDouble(beanIndexAvg[0].toString());
						double sampleAvgReport = Math
								.floor(sampleAverage * 100) / 100;
						DataJson.addCel(cl, sampleAvgReport);

						if (count < sizeList - 1) {
							count++;
						}
					}
					row.setC(cl);
					rowsGenTrend.add(row);
				}
			}
			dataJsonGenTrend.setCols(colsGenTrend);
			dataJsonGenTrend.setRows(rowsGenTrend);
			jsonGeneralTrend = Serializer.serialize(dataJsonGenTrend);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e, null, null);
		}
		this.dataGeneralTrend = jsonGeneralTrend;
	}

	/**
	 * This methods allows build the JSON with data for the report by section
	 * 
	 * @author Andres.Gomez
	 */
	public void consultBySection() {
		ResourceBundle bundleReports = ControladorContexto
				.getBundle("mensajeReports");
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeLifeCycle");

		StringBuilder reportQuery = new StringBuilder();
		StringBuilder filters = new StringBuilder();
		StringBuilder queryGroupBy = new StringBuilder();
		List<SelectItem> params = new ArrayList<SelectItem>();
		String jsonBySection = "";
		DataJson dataJsonBySection = new DataJson();
		List<ColsJson> colsBySection = new ArrayList<ColsJson>();
		List<RowsJson> rowsBySection = new ArrayList<RowsJson>();

		try {
			String cycleLabel = bundleReports
					.getString("reports_label_cycle_number");
			String lblCycle = bundle.getString("cycle_label");
			DataJson.addCol(colsBySection, cycleLabel, "string");
			buildConsult(filters, params, reportQuery, queryGroupBy, 2);
			List<Integer> sectionList = beanIndexDao.consultBySection(params);

			if (sectionList != null && sectionList.size() > 0) {
				for (Integer section : sectionList) {
					Section sectionAux = sectionDao.sectionXId(section);
					DataJson.addCol(colsBySection, sectionAux.getName(),
							"number");
				}
				List<Object[]> listCycles = beanIndexDao
						.consultCycleNumber(params);
				if (listCycles != null) {
					for (Object[] cycle : listCycles) {
						SimpleDateFormat ft = new SimpleDateFormat(
								Constantes.DATE_FORMAT_REPORT);
						String DateToStr = ft.format(cycle[1]);

						RowsJson row = new RowsJson();
						List<CellJson> cl = new ArrayList<CellJson>();
						DataJson.addCel(cl, lblCycle + cycle[0] + ", "
								+ WordUtils.capitalize(DateToStr));

						SelectItem item = new SelectItem(cycle[0], "idCycle");
						params.add(item);
						List<Object[]> listBySection = beanIndexDao
								.consultBySectionAverage(params);

						if (listBySection != null) {
							for (Integer idSection : sectionList) {
								for (Object[] bySection : listBySection) {
									if (idSection == bySection[1]) {
										double sampleAverage = Double
												.parseDouble(bySection[0]
														.toString());
										double sampleAvgReport = Math
												.floor(sampleAverage * 100) / 100;
										DataJson.addCel(cl, sampleAvgReport);
									}
								}
							}
							row.setC(cl);
							rowsBySection.add(row);
						}
					}
				}
			} else {
				DataJson.addCol(colsBySection, null, "number");
			}
			dataJsonBySection.setCols(colsBySection);
			dataJsonBySection.setRows(rowsBySection);
			jsonBySection = Serializer.serialize(dataJsonBySection);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e, null, null);
		}
		this.dataBySection = jsonBySection;
	}

	/**
	 * This method allows build a list of objects form the consult to the
	 * database.
	 * 
	 * @modify 2016/01/15 Andres.Gomez
	 * 
	 * @param weightAvgList
	 *            : list the weight averages, with the result of the Dao consult
	 * @param type
	 *            : integer that indicate the chart type.
	 * @return List<Object[]> list of objects.
	 */
	public List<Object[]> getResultConsultBeanIndex(
			List<Object[]> weightAvgList, int type) {
		List<Object[]> resultList = new ArrayList<Object[]>();
		if (weightAvgList != null && weightAvgList.size() > 0) {
			for (Object[] weightSampleAvg : weightAvgList) {

				Object[] resultObj = new Object[2];
				resultObj[0] = weightSampleAvg[0].toString();
				resultObj[1] = weightSampleAvg[1].toString();
				if (type == 2) {
					resultObj = new Object[3];
					resultObj[0] = weightSampleAvg[0].toString();
					resultObj[1] = weightSampleAvg[1].toString();
					resultObj[2] = weightSampleAvg[2].toString();
				}
				Integer cycleNum = Integer.parseInt((String) resultObj[1]);
				if (cycleNum > lastCycle) {
					lastCycle = cycleNum;
				}
				if (cycleNum < firstCycle) {
					firstCycle = cycleNum;
				}
				resultList.add(resultObj);
			}
		}
		return resultList;
	}

	/**
	 * This method allows to build the consult for the report.
	 * 
	 * @modify Andres.Gomez
	 * 
	 * @param filters
	 *            : List of filters to the query.
	 * @param params
	 *            : List of parameters to the query.
	 * @param reportQuery
	 *            : string with query to build the report.
	 * @param queryGroupBy
	 *            : string with query to group by.
	 * @param type
	 *            : integer that indicate the chart type.
	 */
	private void buildConsult(StringBuilder filters, List<SelectItem> params,
			StringBuilder reportQuery, StringBuilder queryGroupBy, int type) {
		reportQuery.append("SELECT AVG(bi.sample_weight), bi.cycle_number ");
		filters.append("WHERE bi.id_crop = :idCrop ");
		queryGroupBy.append("GROUP BY bi.cycle_number ");
		if (type == 2) {
			reportQuery.append(", bi.id_section ");
			queryGroupBy.append(", bi.id_section ");
		}
		reportQuery.append("FROM kpi.bean_index bi ");
		SelectItem item = new SelectItem(crops.getIdCrop(), "idCrop");
		params.add(item);
	}

	/**
	 * This method allows to build the list of cycle to include in the report,
	 * adding the month name to the cycle number. According to the business
	 * rules, there are two cycles for each month, if this changes, update this
	 * method
	 * 
	 * @param minCycle
	 *            : The first cycle that is going to be presented on the report.
	 * @param maxCycle
	 *            : The last cycle that is going to be presented on the report.
	 * @return List<String> list of cycles to be presented on the report.
	 * 
	 */
	private List<String> buildCyclesList(Integer minCycle, Integer maxCycle) {
		List<String> cyclesList = new ArrayList<String>();
		for (int cycleNum = minCycle; cycleNum <= maxCycle; cycleNum++) {
			String cycleLabel = Integer.toString(cycleNum);
			Integer monthNumber = ((cycleNum + 1) / 2);
			if (monthNumber > 0) {
				String monthName = ControladorFechas.retornarMes(monthNumber);
				cycleLabel = cycleLabel + " - " + monthName;
			}
			cyclesList.add(cycleLabel);
		}
		return cyclesList;
	}

}
