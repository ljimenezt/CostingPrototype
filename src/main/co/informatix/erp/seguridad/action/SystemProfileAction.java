package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.util.Date;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

import co.informatix.erp.seguridad.dao.SystemProfileDao;
import co.informatix.erp.seguridad.entities.SystemProfile;
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
			systemProfile.setActivityDefaultDuration(ControladorFechas
					.restarFechas(startDate, endDate)
					- systemProfile.getBreakDuration());
		}

	}

	/**
	 * This valid method that the dates are correct and are within range.
	 * 
	 */
	public void validateRequired() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		int startTime = ControladorFechas.getHours(this.systemProfile
				.getActivityDefaultStart());
		int endTime = ControladorFechas.getHours(this.systemProfile
				.getActivityDefaultEnd());
		int startLunch = ControladorFechas.getHours(this.systemProfile
				.getBreakStart());
		int endLunch = ControladorFechas.getHours(this.systemProfile
				.getBreakEnd());

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
		if (startTime >= startLunch && startTime <= endLunch) {
			ControladorContexto
					.mensajeError(
							null,
							"formRegisterSystemProfile:activityStart",
							bundle.getString("message_validate_date_activity_lunch_range"));
		}
		if (endTime >= startLunch && endTime <= endLunch) {
			ControladorContexto
					.mensajeError(
							null,
							"formRegisterSystemProfile:activityEnd",
							bundle.getString("message_validate_date_activity_lunch_range"));
		}
	}
}
