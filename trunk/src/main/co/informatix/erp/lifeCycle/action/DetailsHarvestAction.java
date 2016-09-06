package co.informatix.erp.lifeCycle.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.costs.entities.Activities;
import co.informatix.erp.lifeCycle.dao.DetailsHarvestDao;
import co.informatix.erp.lifeCycle.entities.DetailsHarvest;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControllerAccounting;

/**
 * This class is all the logic related to the creation, updating, and deleting
 * the harvest details that may exist.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class DetailsHarvestAction implements Serializable {

	private Activities selectedActivity;
	private DetailsHarvest detailsHarvest;

	@EJB
	private DetailsHarvestDao detailsHarvestDao;

	/**
	 * @return selectedActivity :Object type selected activity.
	 */
	public Activities getSelectedActivity() {
		return selectedActivity;
	}

	/**
	 * @param selectedActivity
	 *            :Object type selected activity.
	 */
	public void setSelectedActivity(Activities selectedActivity) {
		this.selectedActivity = selectedActivity;
	}

	/**
	 * @return detailsHarvest :Object of the class detailsHarvest
	 */
	public DetailsHarvest getDetailsHarvest() {
		return detailsHarvest;
	}

	/**
	 * @param detailsHarvest
	 *            :Object of the class detailsHarvest
	 */
	public void setDetailsHarvest(DetailsHarvest detailsHarvest) {
		this.detailsHarvest = detailsHarvest;
	}

	/**
	 * This method allows consult the details harvest of a activity.
	 * 
	 * @param activity
	 *            : Object activities to consult the harvest details.
	 */
	public void consultDetailsHarvest(Activities activity) {
		try {
			RecordActivitiesActualsAction recordActivitiesActualsAction = ControladorContexto
					.getContextBean(RecordActivitiesActualsAction.class);
			int idActivityName = activity.getActivityName().getIdActivityName();
			int idCycle = activity.getCycle() != null ? activity.getCycle()
					.getIdCycle() : 0;
			int idActivity = activity.getIdActivity();
			this.selectedActivity = activity;
			DetailsHarvest harvestDetails = detailsHarvestDao
					.detailsHarvestXActivity(idActivity);
			if (harvestDetails != null) {
				this.detailsHarvest = harvestDetails;
			} else {
				this.detailsHarvest = new DetailsHarvest();
				Double previousSacks = detailsHarvestDao
						.consultPreviousSacksHarvestActivity(idActivityName,
								idCycle);
				this.detailsHarvest.setPreviousSacks(previousSacks);
			}
			recordActivitiesActualsAction.assignActivity(selectedActivity);
			recordActivitiesActualsAction.consultResourcesByActivity();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows save or update the details harvest object.
	 */
	public void saveOrUpdateDetailsHarvest() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			this.detailsHarvest.setActivities(this.selectedActivity);
			if (this.detailsHarvest.getId() != 0) {
				detailsHarvestDao.editDetailsHarvest(this.detailsHarvest);
			} else {
				messageLog = "message_registro_guardar";
				detailsHarvestDao.saveDetailsHarvest(this.detailsHarvest);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog), this.detailsHarvest
							.getActivities().getActivityName()
							.getActivityName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allow calculated the sack of the harvest
	 * 
	 */
	public void calculatedCurrentSacks() {
		Double previousSack = this.detailsHarvest.getPreviousSacks();
		Double sacksDay = this.detailsHarvest.getSacksDay();
		Double currentSacks = ControllerAccounting.add(previousSack, sacksDay);
		this.detailsHarvest.setCurrentSacks(currentSacks);
		Double dispatch = this.detailsHarvest.getDispatch();
		Double totalSacks = ControllerAccounting.subtract(currentSacks,
				dispatch);
		this.detailsHarvest.setTotalSacks(totalSacks);
	}

	/**
	 * This method allows validate the field of the object details harvest
	 * 
	 */
	public void validateFields() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageLifeCycle");
		Double currentSacks = this.detailsHarvest.getCurrentSacks();
		Double dispatch = this.detailsHarvest.getDispatch();
		if (this.detailsHarvest.getCurrentSacks() <= 0) {
			ControladorContexto.mensajeError(null,
					"formHarvest:txtCurrentSacks",
					bundle.getString("message_campo_mayo_cero"));
		}
		if (this.detailsHarvest.getId() == 0) {
			if (this.detailsHarvest.getTotalSacks() <= 0) {
				ControladorContexto.mensajeError(null,
						"formHarvest:txtTotalSacks",
						bundle.getString("message_campo_mayo_cero"));
			}
		}
		if (dispatch.compareTo(currentSacks) > 0) {
			ControladorContexto
					.mensajeError(
							null,
							"formHarvest:txtDispatch",
							bundleLifeCycle
									.getString("harvest_details_message_dispath_validate"));
		}
	}
}