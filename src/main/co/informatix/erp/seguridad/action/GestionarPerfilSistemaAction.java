package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import co.informatix.erp.seguridad.dao.GestionarPerfilSistemaDao;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.SecureIdentityLoginModule;
import co.informatix.erp.utils.ValidacionesAction;
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
	private GestionarPerfilSistemaDao perfilSistemaDao;

	private PerfilSistema perfilSistema;
	private Paginador pagination = new Paginador();

	private String nameSearch;
	private boolean edit = false;

	private List<PerfilSistema> listProfileSystem;

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
	 * @return pagination: Management paged list names profile system.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list names profile system.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Profile system name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Profile system name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return listProfileSystem: list of objects of profile system.
	 */
	public List<PerfilSistema> getListProfileSystem() {
		return listProfileSystem;
	}

	/**
	 * @param listProfileSystem
	 *            : list of objects of profile system.
	 */
	public void setListProfileSystem(List<PerfilSistema> listProfileSystem) {
		this.listProfileSystem = listProfileSystem;
	}

	/**
	 * @return edit: Boolean variable that allows to verify if the user is
	 *         editing or saving.
	 */
	public boolean isEdit() {
		return edit;
	}

	/**
	 * @param edit
	 *            : Boolean variable that allows to verify if the user is
	 *            editing or saving.
	 */
	public void setEdit(boolean edit) {
		this.edit = edit;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of profile system.
	 * 
	 * @return consultProfileSystem: method that allows consulting the profile
	 *         system, it redirects to the manage profile system template.
	 */
	public String searchInitialization() {
		nameSearch = "";
		return consultProfileSystem();
	}

	/**
	 * Consult the list of the profile system.
	 * 
	 * @return "manProfileSystem": It redirects to the template to manage the
	 *         profile system.
	 */
	public String consultProfileSystem() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		listProfileSystem = new ArrayList<PerfilSistema>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder query = new StringBuilder();
		StringBuilder unionMessagesSearch = new StringBuilder();
		String messageSearch = "";
		try {
			advancedSearch(query, parameters, bundle, unionMessagesSearch);
			Long quantity = perfilSistemaDao.quantityProfileSystem(query,
					parameters);
			if (quantity != null) {
				pagination.paginar(quantity);
			}
			listProfileSystem = perfilSistemaDao.consultProfileSystem(
					pagination.getInicio(), pagination.getRango(), query,
					parameters);
			if ((listProfileSystem == null || listProfileSystem.size() <= 0)
					&& !"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionMessagesSearch);
			} else if (listProfileSystem == null
					|| listProfileSystem.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(unionMessagesSearch.toString())) {
				messageSearch = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSecurity
										.getString("profile_system_label"),
								unionMessagesSearch);
			}
			validations.setMensajeBusqueda(messageSearch);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manProfileSystem";
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(ps.emailServerUser) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}

	}

	/**
	 * Method to edit or create a new profile system.
	 * 
	 * @param perfilSistema
	 *            : Profile system to be add or edit.
	 * 
	 * @return "regProfileSystem":redirected to the template to manage profile
	 *         system.
	 */
	public String addEditProfileSystem(PerfilSistema perfilSistema)
			throws Exception {
		edit = false;
		if (perfilSistema != null) {
			edit = true;
			this.perfilSistema = perfilSistema;

		} else {
			this.perfilSistema = new PerfilSistema();

		}
		return "regProfileSystem";
	}

	/**
	 * Method used to save or edit profile system.
	 * 
	 * @return consultProfileSystem: Profile system redirects to manage the list
	 *         of updated profile systems.
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
		return consultProfileSystem();
	}

	/**
	 * Profile system redirects to manage the list of updated profile system.
	 * 
	 * @return consultProfileSystem(): Consult the list of profile system and
	 *         returns to manage profile system.
	 */
	public String removeProfileSystem() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			perfilSistemaDao.removeProfileSystem(perfilSistema);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"),
					perfilSistema.getEmailServerUser()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					perfilSistema.getEmailServerUser());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return consultProfileSystem();
	}

	/**
	 * Validates multiple addresses separated by commas and it validates against
	 * XSS.
	 * 
	 * @param context
	 *            : application context.
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateMultipleEmails(FacesContext context,
			UIComponent toValidate, Object value) {
		String clientId = toValidate.getClientId(context);
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String email = (String) value;
		try {
			if (email != "") {
				String[] test = email.split(",");
				for (int i = 0; i < test.length; i++) {
					if (!EncodeFilter.validarXSS(test[i].trim(), clientId,
							"locate.regex.email")) {
						((UIInput) toValidate).setValid(false);
						String message = "message_add_email";
						context.addMessage(clientId,
								new FacesMessage(FacesMessage.SEVERITY_ERROR,
										bundle.getString(message), null));
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
	 * 
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateNumberOfPort(FacesContext context,
			UIComponent toValidate, Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		Integer number = (Integer) value;
		String clientId = toValidate.getClientId(context);
		try {
			if (number != null) {
				if (number < 1 || number > 65535) {
					String message = "message_add_range_invalid_port";
					context.addMessage(clientId,
							new FacesMessage(FacesMessage.SEVERITY_ERROR,
									bundle.getString(message), null));
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
