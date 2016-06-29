package co.informatix.erp.informacionBase.action;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.informacionBase.dao.SystemProfileDao;
import co.informatix.erp.informacionBase.entities.SystemProfile;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the system profile that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class SystemProfileAction implements Serializable {

	private SystemProfile systemProfile;

	@EJB
	private SystemProfileDao systemProfileDao;

	/**
	 * @return systemProfile: Object of class systemProfile.
	 */
	public SystemProfile getSystemProfile() {
		return systemProfile;
	}

	/**
	 * Method to edit or create a new profile system.
	 * 
	 * @param systemProfile
	 *            : systemProfile to be add or edit.
	 * @return "gesSystemProfile": redirected to the template to manage system
	 *         profile system.
	 */
	public String addEditSystemProfile(SystemProfile systemProfile) {
		try {
			systemProfile = systemProfileDao.findSystemProfile();
			if (systemProfile != null) {
				this.systemProfile = systemProfile;
			} else {
				this.systemProfile = new SystemProfile();
				this.systemProfile.setActivityDefaultStart(new Date());
				this.systemProfile.setActivityDefaultEnd(new Date());
				this.systemProfile.setBreakStart(new Date());
				this.systemProfile.setBreakEnd(new Date());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesSystemProfile";
	}

	/**
	 * Method used to save or edit system profile.
	 * 
	 * @return gesSystemProfile: System profile redirects to updated systems
	 *         profile.
	 */
	public String saveUpdateSystemProfile() {
		ResourceBundle bundle = ControladorContexto
				.getBundle("messageSecurity");
		String messageLog = "system_profile_message_schedule_modified";
		try {
			if (systemProfile.getId() != 0) {
				systemProfileDao.editSystemProfile(systemProfile);
			} else {
				messageLog = "system_profile_message_schedule_saved";
				systemProfileDao.saveSystemProfile(systemProfile);
			}
			ControladorContexto.mensajeInformacion(null,
					bundle.getString(messageLog));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesSystemProfile";
	}

	/**
	 * This method allows calculate the duration of the activity or lunch.
	 * 
	 * @param flagTime
	 *            : This flag is true for calculate activity duration or false
	 *            for calculate lunch duration.
	 */
	public void calculateTimeActivityAndLunch(boolean flagTime) {
		if (!flagTime) {
			Date startDateBreak = systemProfile.getBreakStart();
			Date endDateBreak = systemProfile.getBreakEnd();
			if (startDateBreak != null && endDateBreak != null) {
				systemProfile.setBreakDuration(ControladorFechas.restarFechas(
						startDateBreak, endDateBreak));
			}
		}
		Date startDate = systemProfile.getActivityDefaultStart();
		Date endDate = systemProfile.getActivityDefaultEnd();
		if (startDate != null && endDate != null) {
			double breakDuration = 0;
			if (systemProfile.getBreakStart().after(startDate)
					&& systemProfile.getBreakStart().before(endDate)) {
				breakDuration = systemProfile.getBreakDuration();
			}
			systemProfile.setActivityDefaultDuration(ControladorFechas
					.restarFechas(startDate, endDate) - breakDuration);
		}

	}

	/**
	 * This valid method that the dates are correct and are within range.
	 * 
	 */
	public void validateRequired() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		Date startActivity = this.systemProfile.getActivityDefaultStart();
		Date endActivity = this.systemProfile.getActivityDefaultEnd();
		Date startBreak = this.systemProfile.getBreakStart();
		Date endBreak = this.systemProfile.getBreakEnd();

		if (systemProfile.getActivityDefaultStart().after(
				systemProfile.getActivityDefaultEnd())
				|| systemProfile.getActivityDefaultStart().equals(
						systemProfile.getActivityDefaultEnd())) {
			ControladorContexto.mensajeErrorEspecifico(
					"formRegisterSystemProfile:activityEnd",
					"message_validar_rango_fecha", "mensaje");
		}
		if (systemProfile.getBreakStart().after(systemProfile.getBreakEnd())
				|| systemProfile.getBreakStart().equals(
						systemProfile.getBreakEnd())) {
			ControladorContexto.mensajeErrorEspecifico(
					"formRegisterSystemProfile:breakEnd",
					"message_validar_rango_fecha", "mensaje");
		}
		if ((startActivity.after(startBreak) || startActivity
				.equals(startBreak))
				&& (startActivity.before(endBreak) || startActivity
						.equals(endBreak))) {
			ControladorContexto
					.mensajeError(
							null,
							"formRegisterSystemProfile:activityStart",
							bundle.getString("message_validate_date_activity_lunch_range"));
		}
		if (endActivity.after(startBreak) && endActivity.before(endBreak)) {
			ControladorContexto
					.mensajeError(
							null,
							"formRegisterSystemProfile:activityEnd",
							bundle.getString("message_validate_date_activity_lunch_range"));
		}
	}
}
