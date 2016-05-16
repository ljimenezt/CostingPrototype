package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.component.UIComponent;
import javax.faces.component.UIInput;
import javax.faces.component.html.HtmlInputSecret;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import co.informatix.erp.informacionBase.entities.CivilStatus;
import co.informatix.erp.recursosHumanos.dao.PersonaDao;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.seguridad.dao.RolDao;
import co.informatix.erp.seguridad.dao.RolUsuarioDao;
import co.informatix.erp.seguridad.dao.UsuarioDao;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.ControladorFechas;
import co.informatix.erp.utils.EncodeFilter;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.SecureIdentityLoginModule;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.dao.IdentityDao;
import co.informatix.security.entities.Rol;
import co.informatix.security.entities.RolUsuario;
import co.informatix.security.entities.RolUsuarioPK;
import co.informatix.security.entities.Usuario;

/**
 * This class implements the business logic of the application for users as well
 * as the roles that are assigned to user.
 * 
 * The logic is to see, edit, add or change the duration of a user and add the
 * user roles, terminate the life of the user role.
 * 
 * @author Gabriel.Moreno
 */
@ManagedBean
@RequestScoped
@SuppressWarnings("serial")
public class UsuarioAction implements Serializable {

	@EJB
	private UsuarioDao usuarioDao;
	@EJB
	private RolUsuarioDao rolUsuarioDao;
	@EJB
	private RolDao rolDao;
	@EJB
	protected IdentityDao dao;
	@EJB
	private PersonaDao personaDao;
	@Inject
	private IdentityAction identity;
	@Inject
	private SesionEmpresaAction empresaHaciendaSesion;
	@Resource
	private UserTransaction userTransaction;

	private List<RolUsuario> newUserRoles;
	private List<RolUsuario> createdUserRoles;
	private List<RolUsuario> userRoles;
	private List<Usuario> users;
	private List<Rol> roles;

	private Paginador pagination = new Paginador();
	private RolUsuario userRole;
	private Usuario user;
	private Persona person;
	private Rol role;
	private ChangedPassword changePass;

	private Date selectStartDate;
	private Date selectEndDate;
	private String vigencia = Constantes.SI;
	private String userRoleValidity = Constantes.SI;
	private String nameSearch;
	private String password2;
	private boolean renovateRol = false;
	private boolean renovatePass = false;
	private boolean edited;

	/**
	 * 
	 * @return user: User that is used when a user register or modification.
	 */
	public Usuario getUser() {
		return user;
	}

	/**
	 * 
	 * @param user
	 *            : User that is used when a user register or modification.
	 */
	public void setUser(Usuario user) {
		this.user = user;
	}

	/**
	 * 
	 * @return users: User List that is loaded into the user interface.
	 */
	public List<Usuario> getUsers() {
		return users;
	}

	/**
	 * 
	 * @param users
	 *            : User List that is loaded into the user interface.
	 */
	public void setUsers(List<Usuario> users) {
		this.users = users;
	}

	/**
	 * 
	 * @return pagination: management paged list of users in the view.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * 
	 * @param pagination
	 *            : management paged list of users in the view.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * 
	 * @return renovateRol: Monitors the status POPUP effective to renovate or
	 *         make a new user role.
	 */
	public boolean isRenovateRol() {
		return renovateRol;
	}

	/**
	 * 
	 * @param renovateRol
	 *            : Monitors the status POPUP effective to renovate or make a
	 *            new user role.
	 */
	public void setRenovateRol(boolean renovateRol) {
		this.renovateRol = renovateRol;
	}

	/**
	 * @return renovatePass: Controls whether to renew the password or not.
	 */
	public boolean isRenovatePass() {
		return renovatePass;
	}

	/**
	 * 
	 * @param renovatePass
	 *            : Controls whether to renew the password or not.
	 */
	public void setRenovatePass(boolean renovatePass) {
		if (renovatePass) {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			ControladorContexto.mensajeInformacion(
					"usuarioForm:outChangePassword",
					bundle.getString("message_advertencia_guardar_cambios"));
		}
		this.renovatePass = renovatePass;
	}

	/**
	 * 
	 * @return edited: Indicates if it is a user edition or insertion.
	 */
	public boolean isEdited() {
		return edited;
	}

	/**
	 * 
	 * @param edited
	 *            : Indicates if it is a user edition or insertion.
	 */
	public void setEdited(boolean edited) {
		this.edited = edited;
	}

	/**
	 * @return vigencia: Obtain selected value 'yes' if existing and 'no' for
	 *         not applicable.
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * @param vigencia
	 *            : Obtain selected value 'yes' if existing and 'no' for not
	 *            applicable.
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return nameSearch: Name to search for the person to associate with the
	 *         user.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Name to search for the person to associate with the user.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * 
	 * @return roles: List of roles of a user.
	 */
	public List<Rol> getRoles() {
		return roles;
	}

	/**
	 * 
	 * @param roles
	 *            : List of roles of a user.
	 */
	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	/**
	 * @return userRoles: list of roles associated with the user shown on the
	 *         user interface, as required.
	 */
	public List<RolUsuario> getUserRoles() {
		return userRoles;
	}

	/**
	 * @param userRoles
	 *            : list of roles associated with the user shown on the user
	 *            interface, as required.
	 */
	public void setUserRoles(List<RolUsuario> userRoles) {
		this.userRoles = userRoles;
	}

	/**
	 * @return userRole: Object that gets the user to associate it with the user
	 *         role.
	 */
	public RolUsuario getUserRole() {
		return userRole;
	}

	/**
	 * @param userRole
	 *            : Object that gets the user to associate it with the user
	 *            role.
	 */
	public void setUserRole(RolUsuario userRole) {
		this.userRole = userRole;
	}

	/**
	 * @return selectStartDate: start date associated with the user role.
	 */
	public Date getSelectStartDate() {
		return selectStartDate;
	}

	/**
	 * @param selectStartDate
	 *            : start date associated with the user role.
	 */
	public void setSelectStartDate(Date selectStartDate) {
		this.selectStartDate = selectStartDate;
	}

	/**
	 * @return selectEndDate: end date associated with the user's role.
	 */
	public Date getSelectEndDate() {
		return selectEndDate;
	}

	/**
	 * @param selectEndDate
	 *            : end date associated with the user's role.
	 */
	public void setSelectEndDate(Date selectEndDate) {
		this.selectEndDate = selectEndDate;
	}

	/**
	 * @return password2 : This field is used to confirm that the password has
	 *         been well written.
	 */
	public String getPassword2() {
		return password2;
	}

	/**
	 * @param password2
	 *            : This field is used to confirm that the password has been
	 *            well written.
	 */
	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	/**
	 * @return changePass : object used to change the password.
	 */
	public ChangedPassword getChangePass() {
		return changePass;
	}

	/**
	 * @param changePass
	 *            : object used to change the password.
	 */
	public void setChangePass(ChangedPassword changePass) {
		this.changePass = changePass;
	}

	/**
	 * @return person: It obtains the person associated with the user.
	 */
	public Persona getPerson() {
		return person;
	}

	/**
	 * @modify Cristhian.Pico 20/05/2015
	 * 
	 * @param person
	 *            : It obtains the person associated with the user.
	 */
	public void setPerson(Persona person) {
		if (person != null && person.getId() != 0) {
			this.user.setNombre(person.getNombres());
			this.user.setApellido(person.getApellidos());
		}
		this.person = person;
	}

	/**
	 * @return role: Role to assign to the user.
	 */
	public Rol getRole() {
		return role;
	}

	/**
	 * @param role
	 *            : Role to assign to the user.
	 */
	public void setRole(Rol role) {
		this.role = role;
	}

	/**
	 * @return newUserRoles: List of new roles that are associated with a user.
	 */
	public List<RolUsuario> getNewUserRoles() {
		return newUserRoles;
	}

	/**
	 * @param newUserRoles
	 *            : List of new roles that are associated with a user.
	 */
	public void setNewUserRoles(List<RolUsuario> newUserRoles) {
		this.newUserRoles = newUserRoles;
	}

	/**
	 * @return userRoleValidity: Gets the value of the life you want to see in
	 *         the user interface of the user's roles.
	 */
	public String getUserRoleValidity() {
		return userRoleValidity;
	}

	/**
	 * @param userRoleValidity
	 *            : Sets the value of the life you want to see in the user
	 *            interface of the user's roles.
	 */
	public void setUserRoleValidity(String userRoleValidity) {
		this.userRoleValidity = userRoleValidity;
	}

	/**
	 * @return createdUserRoles: List of roles that are already associated with
	 *         a user, they are stored in the database.
	 */
	public List<RolUsuario> getCreatedUserRoles() {
		return createdUserRoles;
	}

	/**
	 * @param createdUserRoles
	 *            : List of roles that are already associated with a user, they
	 *            are stored in the database.
	 */
	public void setCreatedUserRoles(List<RolUsuario> createdUserRoles) {
		this.createdUserRoles = createdUserRoles;
	}

	/**
	 * It initializes search parameters and load the initial list of users.
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @return searchUsers(): Consult a list of users and redirects to the
	 *         Manage user template.
	 */
	public String initializeSearch() {
		this.nameSearch = "";
		return this.searchUsers();
	}

	/**
	 * Allows users to consult existing database as may be in force or not in
	 * force.
	 * 
	 * @return manUser: Navigation rule that redirects to the Manage user
	 *         template.
	 */
	public String searchUsers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		String searchMessage = "";
		ValidacionesAction validation = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			user = new Usuario();
			String validityCondition = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				validityCondition = Constantes.IS_NOT_NULL;
			}

			Long usersAmount = usuarioDao.usersAmount(validityCondition,
					this.nameSearch);
			if (usersAmount != null) {
				pagination.paginar(usersAmount);
			}

			users = usuarioDao.queryUsers(pagination.getInicio(),
					pagination.getRango(), validityCondition, this.nameSearch);
			if ((this.users == null || this.users.size() <= 0)
					&& (this.nameSearch != null && !"".equals(this.nameSearch))) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nameSearch + '"');
			} else if (this.users == null || this.users.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nameSearch != null && !"".equals(this.nameSearch)) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSecurity.getString("user_label_s"),
								bundle.getString("label_name") + ": " + '"'
										+ this.nameSearch + '"');
			}
			validation.setMensajeBusqueda(searchMessage);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "manUser";
	}

	/**
	 * Allows to load the user interface for editing user roles or with partners
	 * to create a new user.
	 * 
	 * @param user
	 *            : User to register or edit.
	 * @return regUser: Rule navigation page that redirects to the user
	 *         registering template, which is loaded into editing or empty to
	 *         add a new user.
	 */
	public String registerUser(Usuario user) {
		try {
			initializeUser();
			if (user != null) {
				edited = true;
				this.user = user;
				searchUserPerson(user);
				searchUserRole(user);
				userRoleControler();
			} else {
				password2 = "";
				edited = false;
				this.user = new Usuario();
				userRoleValidity = Constantes.NUEVO;
			}
			notAssignedUserRoles(user);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regUser";
	}

	/**
	 * Method for querying the person associated with the user.
	 * 
	 * @param user
	 *            : user which contains the person that is going to be queried.
	 */
	public void searchUserPerson(Usuario user) {
		try {
			person = personaDao.consultPersonUser(user);
			if (person == null) {
				person = new Persona();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method for querying the roles that are not assigned to the user.
	 * 
	 * @param user
	 *            : User who wants to see the roles.
	 * @throws Exception
	 */
	private void notAssignedUserRoles(Usuario user) throws Exception {
		roles = rolDao.queryNotAssignedRoles(user);
	}

	/**
	 * Method to initialize the user.
	 */
	private void initializeUser() {
		newUserRoles = new ArrayList<RolUsuario>();
		createdUserRoles = new ArrayList<RolUsuario>();
		userRoles = new ArrayList<RolUsuario>();
		person = new Persona();
		role = new Rol();
		roles = new ArrayList<Rol>();
		userRoleValidity = Constantes.SI;
		renovatePass = false;
	}

	/**
	 * It provides access to user roles according to their effect, sent by a
	 * user parameter.
	 */
	public void userRoleControler() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			if (Constantes.NUEVO.equals(userRoleValidity)) {
				userRoles = newUserRoles;
			} else {
				userRoles = new ArrayList<RolUsuario>();
				Calendar hoy = Calendar.getInstance();
				Calendar ayer = Calendar.getInstance();
				ayer.add(Calendar.DATE, -1);
				if (createdUserRoles != null) {
					for (RolUsuario rolUsuario : createdUserRoles) {
						if (Constantes.NOT.equals(userRoleValidity)
								&& (rolUsuario.getFechaInicioVigencia().after(
										hoy.getTime()) || (rolUsuario
										.getFechaFinVigencia() != null && rolUsuario
										.getFechaFinVigencia().before(
												ayer.getTime())))) {
							userRoles.add(rolUsuario);
						} else if (Constantes.SI.equals(userRoleValidity)
								&& !(rolUsuario.getFechaInicioVigencia().after(
										hoy.getTime()) || (rolUsuario
										.getFechaFinVigencia() != null && rolUsuario
										.getFechaFinVigencia().before(
												ayer.getTime())))) {
							userRoles.add(rolUsuario);
						}
					}
				}
				if (userRoles == null || userRoles.size() <= 0) {
					ControladorContexto.mensajeInformacion(
							"usuarioForm:selVigencia",
							bundle.getString("message_no_existen_registros"));
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It provides access to user roles according to their effect, sent by a
	 * user parameter.
	 * 
	 * @param user
	 *            : User object to check.
	 */
	public void searchUserRole(Usuario user) {
		try {
			createdUserRoles = rolUsuarioDao.consultarUsuarioRoles(user);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It allows to load detailed user information in the user interface.
	 * 
	 * @param user
	 *            : The user object information you want to see.
	 */
	public void seeUserDetails(Usuario user) {
		userRoleValidity = Constantes.SI;
		this.user = new Usuario();
		userRoles = new ArrayList<RolUsuario>();
		try {
			this.user = user;
			searchUserRole(user);
			userRoleControler();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It allows you to save or edit a user, validating that the user name is
	 * not repeated in the database.
	 * 
	 * @modify Liseth.sJimenez 19/06/2012
	 * 
	 * @return initializeSearch: Navigation rule that redirects to the user
	 *         registration page for errors, otherwise it redirects to the user
	 *         management.
	 */
	public String saveUser() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		try {
			userTransaction.begin();
			validateMaritalStatusObject();
			if (edited) {
				if (renovatePass) {
					user.setPassword(SecureIdentityLoginModule.doSign(user
							.getNombreUsuario()
							+ ControladorFechas.formatDate(
									user.getFechaCreacion(),
									Constantes.DATE_FORMAT_CREATION)));
				}
				user.setUserName(identity.getUserName());
				usuarioDao.editUser(user);
				Persona lastPerson = personaDao.consultPersonUser(user);
				if (!person.equals(lastPerson)) {
					if (lastPerson != null) {
						lastPerson.setUsuario(null);
						lastPerson.setUserName(identity.getUserName());
						personaDao.editPerson(lastPerson);
					}
					person.setUsuario(user);
					person.setUserName(identity.getUserName());
					personaDao.editPerson(person);
				}
			} else {
				key = "message_registro_guardar";
				user.setFechaCreacion(new Date());
				user.setFechaInicioVigencia(new Date());
				user.setUserName(identity.getUserName());
				user.setPassword(SecureIdentityLoginModule.doSign(user
						.getPassword()
						+ ControladorFechas.formatDate(user.getFechaCreacion(),
								Constantes.DATE_FORMAT_CREATION)));
				usuarioDao.saveUser(user);
				person.setUsuario(user);
				person.setUserName(identity.getUserName());
				personaDao.editPerson(person);
			}
			saveUserRole();
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							user.getNombreUsuario()));
		} catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.quitarFacesMessages();
			ControladorContexto.mensajeError(e);
			return "regUser";
		}
		return initializeSearch();
	}

	/**
	 * Method to validate the object of the civil status of the person before
	 * storing in the database, to which an instance of the class but it thinks
	 * there is no registration.
	 * 
	 * @author marisol.calderon
	 * @modify 17/03/2016 Wilhelm.Boada
	 */
	private void validateMaritalStatusObject() {
		if (this.person != null) {
			CivilStatus civilStatus = this.person.getCivilStatus();
			if (civilStatus != null && civilStatus.getId() == 0) {
				this.person.setCivilStatus(null);
			}
		}
	}

	/**
	 * This method allows save and edit user roles in the database.
	 * 
	 * @throws Exception
	 */
	private void saveUserRole() throws Exception {
		if (newUserRoles != null && newUserRoles.size() > 0) {
			for (RolUsuario rolUsuario : newUserRoles) {
				rolUsuario.setUserName(identity.getUserName());
				rolUsuarioDao.guardarRolUsuario(rolUsuario);
			}
		}
		if (createdUserRoles != null && createdUserRoles.size() > 0) {
			for (RolUsuario rolUsuario : createdUserRoles) {
				rolUsuario.setUserName(identity.getUserName());
				rolUsuarioDao.editarRolUsuario(rolUsuario);
			}
		}
	}

	/**
	 * To validate the user name so that it is not repeated in the database.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param context
	 *            : Application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : Field value to be valid.
	 * 
	 * @throws Exception
	 */
	public void validateUserName(FacesContext context, UIComponent toValidate,
			Object value) throws Exception {
		String name = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			Usuario userNAme = new Usuario();

			if (edited) {
				userNAme = usuarioDao.userNameExists(name, user.getId());
			} else {
				userNAme = usuarioDao.userNameExists(name);
			}
			String result = checkValidity(userNAme);
			if (Constantes.VIGENTE.equals(result)) {
				ControladorContexto.mensajeErrorEspecifico(clientId,
						"message_ya_existe_verifique", "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (Constantes.SIN_VIGENTE.equals(result)) {
				ControladorContexto.mensajeErrorEspecifico(clientId,
						"message_ya_existe_sin_vigencia", "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter
					.validarXSS(name, clientId, "locate.regex.usuario")) {
				((UIInput) toValidate).setValid(false);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * Check that the password is valid.
	 * 
	 * @author Oscar.Amaya
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validatePassword(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto
				.getBundle("messageSecurity");
		String txtPassword = (String) value;
		String idAsociado = (String) toValidate.getAttributes().get(
				"idAsociado");
		HtmlInputSecret confirmPassText = (HtmlInputSecret) toValidate
				.findComponent(idAsociado);
		String clientId = toValidate.getClientId(context);
		if (!confirmPassText.getSubmittedValue().equals(txtPassword)) {
			context.addMessage(
					clientId,
					new FacesMessage(FacesMessage.SEVERITY_ERROR, bundle
							.getString("user_message_password_diferents"),
							bundle.getString("user_message_password_diferents")));
		}
	}

	/**
	 * This method validates that the password changes, it checks that the
	 * previous password is valid.
	 * 
	 * @author Oscar.Amaya
	 * @modify 24/04/2013 marisol.calderon
	 * 
	 * @param context
	 *            : application context.
	 * @param toValidate
	 *            : validate component.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateLogin(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		try {
			String clientId = toValidate.getClientId(context);
			String previousPass = (String) value;
			Usuario autenticatedUser = dao.consultarNombreUsuario(identity
					.getUserName());
			if (autenticatedUser != null) {
				String passValue = previousPass
						+ ControladorFechas.formatDate(
								autenticatedUser.getFechaCreacion(),
								Constantes.DATE_FORMAT_CREATION);
				previousPass = SecureIdentityLoginModule.doSign(passValue);
				autenticatedUser = dao.validarNombreUsuario(
						identity.getUserName(), previousPass);
			}
			if (autenticatedUser == null) {
				context.addMessage(
						clientId,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								bundleSecurity
										.getString("user_message_password_invalid"),
								bundleSecurity
										.getString("user_message_password_invalid")));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * It checks that the user is valid.
	 * 
	 * @param userName
	 *            : User object to which the term is valid.
	 * @return result: 'empty' if the user name does not exist, 'current' if
	 *         there is force and 'sinVigencia' if the user name exists with no
	 *         force.
	 */
	private String checkValidity(Usuario userName) {
		String result = "";
		if (userName != null) {
			if (userName.getFechaFinVigencia() != null) {
				result = Constantes.SIN_VIGENTE;
			} else {
				result = Constantes.VIGENTE;
			}
		}
		return result;
	}

	/**
	 * Change the effect of users in the database.
	 * 
	 * @param valid
	 *            : boolean that allows to know if the term is ended with 'true'
	 *            or start with 'false', the selected record in the user
	 *            interface.
	 * @return searchUsers: Navigation rule that redirects to the manage users
	 *         template.
	 */
	public String usersValidity(boolean valid) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		StringBuilder successed = new StringBuilder();
		String validityChangeMessage = "message_inicio_vigencia_satisfactorio";
		try {
			if (valid) {
				validityChangeMessage = "message_fin_vigencia_satisfactorio";
				user.setFechaFinVigencia(new Date());
				user.setUserName(identity.getUserName());
				usuarioDao.editUser(user);
				successed.append(user.getNombreUsuario() + ", ");
			} else {
				user.setFechaFinVigencia(null);
				user.setUserName(identity.getUserName());
				usuarioDao.editUser(user);
				successed.append(user.getNombreUsuario() + ", ");
			}
			if (successed.length() > 0) {
				ControladorContexto
						.mensajeInformacion(
								null,
								bundle.getString(validityChangeMessage)
										+ ": "
										+ successed.substring(0,
												successed.length() - 2));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchUsers();
	}

	/**
	 * Control the addition and subtraction of roles in newUserRoles list.
	 * 
	 * @param option
	 *            : Option to see taking place in the list.
	 * @param userRoleObject
	 *            : Purpose of the role of the user you want to remove from the
	 *            list.
	 */
	public void rolesListControler(String option, RolUsuario userRoleObject) {
		if (Constantes.NEW_ROL_USUARIO.equals(option)) {
			selectStartDate = null;
			selectEndDate = null;
			renovateRol = false;
		} else if (Constantes.RE_NEW_ROL_USUARIO.equals(option)) {
			selectStartDate = userRoleObject.getFechaInicioVigencia();
			selectEndDate = userRoleObject.getFechaFinVigencia();
			renovateRol = true;
		} else if (Constantes.ADD_ROL_USUARIO.equals(option)) {
			RolUsuario rolUsuario = new RolUsuario();
			rolUsuario.setRolUsuarioPK(new RolUsuarioPK(user, role));
			rolUsuario.setFechaInicioVigencia(selectStartDate);
			rolUsuario.setFechaFinVigencia(selectEndDate);
			rolUsuario.setFechaCreacion(new Date());
			newUserRoles.add(rolUsuario);
			userRoles = newUserRoles;
			userRoleValidity = Constantes.NUEVO;
			roles.remove(role);
		} else if (Constantes.CHANGE_ROL_USUARIO.equals(option)) {
			if (createdUserRoles != null) {
				for (int i = 0; i < createdUserRoles.size(); i++) {
					if (createdUserRoles.get(i).equals(userRole)) {
						createdUserRoles.get(i).setFechaInicioVigencia(
								selectStartDate);
						createdUserRoles.get(i).setFechaFinVigencia(
								selectEndDate);
						break;
					}
				}
			}
			userRoleControler();
		} else if (Constantes.BORRAR_PERMISO.equals(option)) {
			roles.add(userRoleObject.getRolUsuarioPK().getRol());
			newUserRoles.remove(userRoleObject);
			userRoles = newUserRoles;
		}
	}

	/**
	 * It validates the effective date not to be less than the start date of
	 * validity of the role associated to the user.
	 * 
	 * @param context
	 *            : application context.
	 * @param component
	 *            : component to validate.
	 * @param value
	 *            : field value to be valid.
	 */
	public void validateDates(FacesContext context, UIComponent component,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		Date endDate = (Date) value;
		UIInput findComponent = (UIInput) component
				.findComponent("calFechaInicio");
		Date startDate = (Date) findComponent.getValue();

		if (startDate != null && endDate != null) {
			if (endDate.before(startDate)) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						bundle.getString("message_validar_fechas_menor"), "");
				throw new ValidatorException(message);
			}
		}
	}

	/**
	 * Prepare the view so that the user can change its password.
	 * 
	 * @author Oscar.Amaya
	 * 
	 * @return changePassword: Navigation rule that targets the template user
	 *         password change.
	 */
	public String newPassword() {
		try {
			user = usuarioDao.searchUsuario(identity.getUserName());
			changePass = new ChangedPassword();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "changePassword";
	}

	/**
	 * IT performs Changing the password of a user in the database.
	 * 
	 * @author Oscar.Amaya
	 * @modify 24/04/2013 marisol.calderon
	 * 
	 * @return gesLogin: navigation rule that addresses to the login page to log
	 *         on to change the password.
	 */
	public String changePassword() {
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		try {
			String newPassword = SecureIdentityLoginModule.doSign(changePass
					.getNewPassword()
					+ ControladorFechas.formatDate(user.getFechaCreacion(),
							Constantes.DATE_FORMAT_CREATION));
			user.setPassword(newPassword);
			user.setUserName(identity.getUserName());
			usuarioDao.editUser(user);
			empresaHaciendaSesion.cleanCompanySession();
			identity.logout(true);
			String format = MessageFormat.format(bundleSecurity
					.getString("user_message_password_change_successful"), user
					.getNombreUsuario());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesLogin";
	}

	/**
	 * This class is used to change the password.
	 * 
	 * @author Oscar.Amaya
	 * 
	 */
	public class ChangedPassword implements Serializable {

		private String previousPassword;
		private String newPassword;
		private String checkedPassword;

		/**
		 * @return previousPassword: It is the user's old password.
		 */
		public String getPreviousPassword() {
			return previousPassword;
		}

		/**
		 * @param previousPassword
		 *            : It is the user's old password.
		 */
		public void setPreviousPassword(String previousPassword) {
			this.previousPassword = previousPassword;
		}

		/**
		 * @return newPassword: It is the new user password.
		 */
		public String getNewPassword() {
			return newPassword;
		}

		/**
		 * @param newPassword
		 *            : It is the new user password.
		 */
		public void setNewPassword(String newPassword) {
			this.newPassword = newPassword;
		}

		/**
		 * @return checkedPassword : The new password that is confirmed by the
		 *         user.
		 */
		public String getCheckedPassword() {
			return checkedPassword;
		}

		/**
		 * @param checkedPassword
		 *            : The new password that is confirmed by the user.
		 */
		public void setCheckedPassword(String checkedPassword) {
			this.checkedPassword = checkedPassword;
		}
	}
}