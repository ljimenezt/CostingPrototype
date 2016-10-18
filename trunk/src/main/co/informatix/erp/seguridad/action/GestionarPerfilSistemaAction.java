package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.Date;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import co.informatix.erp.seguridad.dao.ManageProfileSystemDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.SecureIdentityLoginModule;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.entities.PerfilSistema;

/**
 * This class implements the business logic of the application for profile
 * system. The logic is to see, edit, add or change the duration of a profile
 * system.
 * 
 * @author Jhair.Leal
 */
@ManagedBean(name = "perfilSistemaAction")
@RequestScoped
@SuppressWarnings("serial")
public class GestionarPerfilSistemaAction implements Serializable {

	@Inject
	private IdentityAction identity;

	@EJB
	private ManageProfileSystemDao perfilSistemaDao;

	private PerfilSistema perfilSistema;

	/**
	 * @return perfilSistema: object that contains the data of profile system.
	 */
	public PerfilSistema getPerfilSistema() {
		return perfilSistema;
	}

	/**
	 * @param perfilSistema
	 *            : object that contains the data of profile system.
	 */
	public void setPerfilSistema(PerfilSistema perfilSistema) {
		this.perfilSistema = perfilSistema;
	}

	/**
	 * Method to edit or create a new profile system.
	 * 
	 * @param perfilSistema
	 *            : Profile system to be add or edit.
	 * @return "regProfileSystem":redirected to the template to register profile
	 *         system.
	 */
	public String addEditProfileSystem(PerfilSistema perfilSistema) {
		try {
			perfilSistema = perfilSistemaDao.findProfileSystem();
			if (perfilSistema != null) {
				this.perfilSistema = perfilSistema;
			} else {
				this.perfilSistema = new PerfilSistema();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regProfileSystem";
	}

	/**
	 * Method used to save or edit profile system.
	 * 
	 * @return regProfileSystem: Profile system redirects to updated profile
	 *         systems.
	 */
	public String saveProfileSystem() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String messageLog = "message_registro_modificar";
		try {
			perfilSistema.setUserName(identity.getUserName());
			if (perfilSistema.getId() != 0) {
				perfilSistemaDao.editProfileSystem(perfilSistema);
			} else {
				messageLog = "message_registro_guardar";
				perfilSistema.setFechaCreacion(new Date());
				perfilSistemaDao.saveProfileSystem(perfilSistema);
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString(messageLog),
					perfilSistema.getEmailServerUser()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regProfileSystem";
	}

	/**
	 * Validates multiple addresses separated by commas and it validates against
	 * XSS.
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateMultipleEmails(FacesContext context,
			UIComponent toValidate, Object value) {
		String clientId = toValidate.getClientId(context);
		String email = (String) value;
		try {
			if (!"".equals(email)) {
				String[] test = email.split(",");
				for (int i = 0; i < test.length; i++) {
					if (!EncodeFilter.validarXSS(test[i].trim(), clientId,
							"locate.regex.email")) {
						ControladorContexto.mensajeErrorEspecifico(
								"formProfileSystem:txaEmailReportarErrores",
								"message_add_email", "mensaje");
						((UIInput) toValidate).setValid(false);
						break;
					}
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validates the range of logical ports and it validates against XSS.
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNumberOfPort(FacesContext context,
			UIComponent toValidate, Object value) {
		Integer number = (Integer) value;
		String clientId = toValidate.getClientId(context);
		try {
			if (number != null) {
				if (number < Constantes.START_PORT
						|| number > Constantes.FINAL_PORT) {
					ControladorContexto.mensajeErrorArg1(
							"formProfileSystem:txtPort",
							"message_add_range_number", "mensaje",
							Constantes.START_PORT, Constantes.FINAL_PORT);
					((UIInput) toValidate).setValid(false);
				}
				if (!EncodeFilter.validarXSS(Integer.toString(number),
						clientId, "locate.regex.numeros")) {
					((UIInput) toValidate).setValid(false);
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Validate whether the current password is equal to the new one, if not so
	 * saves the new and encrypts.
	 */
	public void validatePassword() {
		try {
			PerfilSistema systemProf = perfilSistemaDao.findProfileSystem();
			String actualPassword = systemProf.getEmailServerPassword();
			String newPassword = this.perfilSistema.getEmailServerPassword();
			if (!actualPassword.equals(newPassword)) {
				newPassword = SecureIdentityLoginModule.doSign(newPassword);
				this.perfilSistema.setEmailServerPassword(newPassword);
			} else {
				this.perfilSistema.setEmailServerPassword(actualPassword);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

}
