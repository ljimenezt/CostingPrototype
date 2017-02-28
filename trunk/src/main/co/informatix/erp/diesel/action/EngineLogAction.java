package co.informatix.erp.diesel.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.diesel.dao.EngineLogDao;
import co.informatix.erp.diesel.entities.EngineLog;
import co.informatix.erp.diesel.entities.FuelUsageLog;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.machines.entities.Machines;
import co.informatix.erp.utils.ControladorContexto;

@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class EngineLogAction implements Serializable {

	private String nameActivitySearch;
	private String nameMachineSearch;
	private EngineLog engineLog;
	private FuelUsageLog fuelUsageLog;

	@EJB
	private EngineLogDao engineLogDao;

	/**
	 * @return nameActivitySearch: return the activity name that user want
	 *         searching.
	 */
	public String getNameActivitySearch() {
		return nameActivitySearch;
	}

	/**
	 * @param nameActivitySearch
	 *            : set the activity name that user want searching.
	 */
	public void setNameActivitySearch(String nameActivitySearch) {
		this.nameActivitySearch = nameActivitySearch;
	}

	/**
	 * @return nameMachineSearch: return machine name that user want searching.
	 */
	public String getNameMachineSearch() {
		return nameMachineSearch;
	}

	/**
	 * @param nameMachineSearch
	 *            : set machine name that user want searching.
	 */
	public void setNameMachineSearch(String nameMachineSearch) {
		this.nameMachineSearch = nameMachineSearch;
	}

	/**
	 * @return engineLog: return engine log object with the information
	 *         required.
	 */
	public EngineLog getEngineLog() {
		return engineLog;
	}

	/**
	 * @param engineLog
	 *            : add the information required to engine log object.
	 */
	public void setEngineLog(EngineLog engineLog) {
		this.engineLog = engineLog;
	}

	/**
	 * @return fuelUsageLog: FuelUsageLog object related with the engineLog
	 *         object.
	 */
	public FuelUsageLog getFuelUsageLog() {
		return fuelUsageLog;
	}

	/**
	 * @param fuelUsageLog
	 *            : FuelUsageLog object related with the engineLog object.
	 */
	public void setFuelUsageLog(FuelUsageLog fuelUsageLog) {
		this.fuelUsageLog = fuelUsageLog;
	}

	/**
	 * Cleans the person field associated with the engine log.
	 */
	public void deleteHr1() {
		this.engineLog.setDeliveredBy(new Hr());
	}

	/**
	 * Cleans the person field associated with the engine log.
	 */
	public void deleteHr2() {
		this.engineLog.setReceivedBy(new Hr());
	}

	/**
	 * Cleans the activity field associated with the engine log.
	 */
	public void deleteActivity() {
		engineLog.getActivityMachine().getActivityMachinePK()
				.setActivities(new Activities());
	}

	/**
	 * Cleans the machine field associated with the engine log.
	 */
	public void deleteMachine() {
		engineLog.getActivityMachine().getActivityMachinePK()
				.setMachines(new Machines());
	}

	/**
	 * Method to load the selected deliver person.
	 * 
	 * @param person
	 *            : Object of the selected deliver person.
	 */
	public void loadHr1(Hr hr) {
		this.engineLog.setDeliveredBy(hr);
	}

	/**
	 * Method to load the selected receiver person.
	 * 
	 * @param person
	 *            : Object of the selected receiver person.
	 */
	public void loadHr2(Hr hr) {
		this.engineLog.setReceivedBy(hr);
	}

	/**
	 * @param engineLog
	 *            :
	 * @return "regEngineLog": return the string for redirect to register view.
	 */
	public String addEditEngineLog(EngineLog engineLog) {
		if (engineLog != null) {
			this.engineLog = engineLog;
		} else {
			this.engineLog = new EngineLog();
			this.engineLog.setReceivedBy(new Hr());
			this.engineLog.setDeliveredBy(new Hr());
		}
		return "regEngineLog";
	}

	/**
	 * Method to save or edit engine logs.
	 * 
	 * @return addEditEngineLog: Redirects to register engineLog template.
	 * 
	 */
	public String saveEngineLog() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String registerMessage = "message_registro_modificar";
		try {

			if (engineLog.getIdEngineLog() != 0) {
				engineLogDao.editEngineLog(engineLog);
			} else {
				registerMessage = "message_registro_guardar";
				engineLogDao.saveEngineLog(engineLog);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(registerMessage),
					engineLog.getIdEngineLog()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return addEditEngineLog(null);
	}
}
