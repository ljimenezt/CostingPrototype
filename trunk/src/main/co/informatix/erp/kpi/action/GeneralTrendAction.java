package co.informatix.erp.kpi.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.kpi.dao.BeanIndexDao;
import co.informatix.erp.reports.json.CellJson;
import co.informatix.erp.reports.json.ColsJson;
import co.informatix.erp.reports.json.DataJson;
import co.informatix.erp.reports.json.RowsJson;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.Serializer;

/**
 * This class allows to generate the consults for the general trend reports.
 * 
 * @author Cristhian.Pico
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class GeneralTrendAction implements Serializable {

	private String dataGeneralTrend;

	private int firstCycle = 100;
	private int lastCycle = 0;

	@EJB
	private BeanIndexDao beanIndexDao;

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
	 * This methods allows arm the json with data for the general trend report
	 * 
	 */
	public void consultGeneralTrend() {
		ResourceBundle bundleReports = ControladorContexto
				.getBundle("mensajeReports");

		StringBuilder reportQuery = new StringBuilder();
		StringBuilder filters = new StringBuilder();
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

			buildConsult(filters, params, reportQuery);

			List<Object[]> beanIndexList = beanIndexDao
					.consultBeanIndexAverage(filters, params, reportQuery);
			List<Object[]> resultConsultBeanIndex = getResultConsultBeanIndex(beanIndexList);
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
	 * This method allows build a list of objects form the consult to the
	 * database.
	 * 
	 * @param weightAvgList
	 *            : list the weight averages, with the result of the Dao consult
	 * @return List<Object[]> list of objects.
	 */
	public List<Object[]> getResultConsultBeanIndex(List<Object[]> weightAvgList) {
		List<Object[]> resultList = new ArrayList<Object[]>();
		if (weightAvgList != null && weightAvgList.size() > 0) {
			for (Object[] weightSampleAvg : weightAvgList) {
				Object[] resultObj = new Object[2];
				resultObj[0] = weightSampleAvg[0].toString();
				resultObj[1] = weightSampleAvg[1].toString();
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
	 * @param filters
	 *            : List of filters to the query.
	 * @param params
	 *            : List of parameters to the query.
	 * @param reportQuery
	 *            : string with query to build the report.
	 */
	private void buildConsult(StringBuilder filters, List<SelectItem> params,
			StringBuilder reportQuery) {

		BeanIndexFiltersAction filtersAction = ControladorContexto
				.getContextBean(BeanIndexFiltersAction.class);

		reportQuery.append("SELECT AVG(bi.sample_weight), bi.cycle_number ");
		reportQuery.append("FROM kpi.bean_index bi ");
		filters.append("WHERE bi.id_crop = :idCrop ");
		SelectItem item = new SelectItem(filtersAction.getSelectedCrop()
				.getIdCrop(), "idCrop");
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
