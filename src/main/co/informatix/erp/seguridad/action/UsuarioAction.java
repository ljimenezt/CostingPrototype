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
 * This class allows business logic of the application users as well as the
 * roles that are assigned to user user.
 * 
 * The logic is to see, edit, add or change the duration of a user and add the
 * user roles, terminate the life of the user role.
 * 
 * @author Gabriel.Moreno
 * 
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

	private List<RolUsuario> rolesUsuarioNuevos;
	private List<RolUsuario> rolesUsuarioCreados;
	private List<RolUsuario> rolesUsuario;
	private List<Usuario> usuarios;
	private List<Rol> roles;

	private Paginador paginador = new Paginador();
	private RolUsuario rolUsuario;
	private Usuario usuario;
	private Persona persona;
	private Rol rol;
	private CambioContrasena cambioContrasena;

	private Date selectFechaInicio;
	private Date selectFechaFin;
	private String vigencia = Constantes.SI;
	private String vigenciaRolUsuario = Constantes.SI;
	private String nombreBuscar;
	private String password2;
	private boolean renovarRol = false;
	private boolean renovarPassword = false;
	private boolean editar;

	/**
	 * 
	 * @return usuario: User that the implementation of registration or editing
	 *         is done.
	 */
	public Usuario getUsuario() {
		return usuario;
	}

	/**
	 * 
	 * @param usuario
	 *            : User that the implementation of registration or editing is
	 *            done.
	 */
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	/**
	 * 
	 * @return usuarios: User Lists that are loaded into the user interface.
	 */
	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	/**
	 * 
	 * @param usuarios
	 *            : User Lists that are loaded into the user interface.
	 */
	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	/**
	 * 
	 * @return paginador: management paged list of users in the view.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * 
	 * @param paginador
	 *            : management paged list of users in the view.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
	}

	/**
	 * 
	 * @return renovarRol: Monitors the status POPUP effective to renovate or
	 *         make a new user role.
	 */
	public boolean isRenovarRol() {
		return renovarRol;
	}

	/**
	 * 
	 * @param renovarRol
	 *            : Monitors the status POPUP effective to renovate or make a
	 *            new user role.
	 */
	public void setRenovarRol(boolean renovarRol) {
		this.renovarRol = renovarRol;
	}

	/**
	 * 
	 * @return renovarPassword: Controls whether to renew the password.
	 */
	public boolean isRenovarPassword() {
		return renovarPassword;
	}

	/**
	 * 
	 * @param renovarPassword
	 *            : Controls whether to renew the password.
	 */
	public void setRenovarPassword(boolean renovarPassword) {
		if (renovarPassword) {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			ControladorContexto.mensajeInformacion(
					"usuarioForm:outChangePassword",
					bundle.getString("message_advertencia_guardar_cambios"));
		}
		this.renovarPassword = renovarPassword;
	}

	/**
	 * 
	 * @return editar: Indicates if it is in edit or insert a user.
	 */
	public boolean isEditar() {
		return editar;
	}

	/**
	 * 
	 * @param editar
	 *            : Indicates if it is in edit or insert a user.
	 */
	public void setEditar(boolean editar) {
		this.editar = editar;
	}

	/**
	 * 
	 * @return vigencia: allows to obtain selected value 'yes' of existing and
	 *         'no' for not applicable.
	 */
	public String getVigencia() {
		return vigencia;
	}

	/**
	 * 
	 * @param vigencia
	 *            : allows to obtain selected value 'yes' of existing and 'no'
	 *            for not applicable.
	 */
	public void setVigencia(String vigencia) {
		this.vigencia = vigencia;
	}

	/**
	 * @return nombreBuscar: Name to search for the person who wants to
	 *         associate with the user.
	 */
	public String getNombreBuscar() {
		return nombreBuscar;
	}

	/**
	 * @param nombreBuscar
	 *            : Name to search for the person who wants to associate with
	 *            the user.
	 */
	public void setNombreBuscar(String nombreBuscar) {
		this.nombreBuscar = nombreBuscar;
	}

	/**
	 * 
	 * @return roles: List of roles that a user.
	 */
	public List<Rol> getRoles() {
		return roles;
	}

	/**
	 * 
	 * @param roles
	 *            : List of roles that a user.
	 */
	public void setRoles(List<Rol> roles) {
		this.roles = roles;
	}

	/**
	 * @return rolesUsuario: list of roles associated with the user shown on the
	 *         user interface, as required.
	 */
	public List<RolUsuario> getRolesUsuario() {
		return rolesUsuario;
	}

	/**
	 * @param rolesUsuario
	 *            : list of roles associated with the user shown on the user
	 *            interface, as required.
	 */
	public void setRolesUsuario(List<RolUsuario> rolesUsuario) {
		this.rolesUsuario = rolesUsuario;
	}

	/**
	 * @return rolUsuario: Object that gets the user to associate the user role.
	 */
	public RolUsuario getRolUsuario() {
		return rolUsuario;
	}

	/**
	 * @param rolUsuario
	 *            : Object that gets the user to associate the user role.
	 */
	public void setRolUsuario(RolUsuario rolUsuario) {
		this.rolUsuario = rolUsuario;
	}

	/**
	 * @return selectFechaInicio: start date associated with the user role.
	 */
	public Date getSelectFechaInicio() {
		return selectFechaInicio;
	}

	/**
	 * @param selectFechaInicio
	 *            : start date associated with the user role.
	 */
	public void setSelectFechaInicio(Date selectFechaInicio) {
		this.selectFechaInicio = selectFechaInicio;
	}

	/**
	 * @return selectFechaFin: end date associated with the user's role.
	 */
	public Date getSelectFechaFin() {
		return selectFechaFin;
	}

	/**
	 * @param selectFechaFin
	 *            : end date associated with the user's role.
	 */
	public void setSelectFechaFin(Date selectFechaFin) {
		this.selectFechaFin = selectFechaFin;
	}

	/**
	 * @return password2 : This field is used to confirm that the password has
	 *         been well written
	 */
	public String getPassword2() {
		return password2;
	}

	/**
	 * @param password2
	 *            : This field is used to confirm that the password has been
	 *            well written
	 */
	public void setPassword2(String password2) {
		this.password2 = password2;
	}

	/**
	 * @return cambioContrasena : object used to change the password
	 */
	public CambioContrasena getCambioContrasena() {
		return cambioContrasena;
	}

	/**
	 * @param cambioContrasena
	 *            : object used to change the password
	 */
	public void setCambioContrasena(CambioContrasena cambioContrasena) {
		this.cambioContrasena = cambioContrasena;
	}

	/**
	 * @return persona: gets obtain the person associated with the user.
	 */
	public Persona getPersona() {
		return persona;
	}

	/**
	 * @modify Cristhian.Pico 20/05/2015
	 * 
	 * @param persona
	 *            : sets obtain the person associated with the user.
	 */
	public void setPersona(Persona persona) {
		if (persona != null && persona.getId() != 0) {
			this.usuario.setNombre(persona.getNombres());
			this.usuario.setApellido(persona.getApellidos());
		}
		this.persona = persona;
	}

	/**
	 * 
	 * @return rol: Role to assign to the user.
	 */
	public Rol getRol() {
		return rol;
	}

	/**
	 * 
	 * @param rol
	 *            : Role to assign to the user.
	 */
	public void setRol(Rol rol) {
		this.rol = rol;
	}

	/**
	 * @return rolesUsuarioNuevos: List of new roles that are associated with a
	 *         user.
	 */
	public List<RolUsuario> getRolesUsuarioNuevos() {
		return rolesUsuarioNuevos;
	}

	/**
	 * @param rolesUsuarioNuevos
	 *            : List of new roles that are associated with a user.
	 */
	public void setRolesUsuarioNuevos(List<RolUsuario> rolesUsuarioNuevos) {
		this.rolesUsuarioNuevos = rolesUsuarioNuevos;
	}

	/**
	 * @return vigenciaRolUsuario: Gets the value of the life you want to see in
	 *         the user interface of the user's roles.
	 */
	public String getVigenciaRolUsuario() {
		return vigenciaRolUsuario;
	}

	/**
	 * @param vigenciaRolUsuario
	 *            : Sets the value of the life you want to see in the user
	 *            interface of the user's roles.
	 */
	public void setVigenciaRolUsuario(String vigenciaRolUsuario) {
		this.vigenciaRolUsuario = vigenciaRolUsuario;
	}

	/**
	 * @return rolesUsuarioCreados: List of roles that are already associated
	 *         with a user, are stored in the database.
	 */
	public List<RolUsuario> getRolesUsuarioCreados() {
		return rolesUsuarioCreados;
	}

	/**
	 * @param rolesUsuarioCreados
	 *            : List of roles that are already associated with a user, are
	 *            stored in the database.
	 */
	public void setRolesUsuarioCreados(List<RolUsuario> rolesUsuarioCreados) {
		this.rolesUsuarioCreados = rolesUsuarioCreados;
	}

	/**
	 * Initializes search parameters and load the initial list of users
	 * 
	 * @author Liseth.Jimenez
	 * 
	 * @return Navigation rule that redirects to the Manage user.
	 */
	public String inicializarBusqueda() {
		this.nombreBuscar = "";
		return this.consultarUsuarios();
	}

	/**
	 * Allows users to consult existing database as may be in force or not in
	 * force.
	 * 
	 * @return gesUsuario: Navigation rule that redirects to the Manage user.
	 */
	public String consultarUsuarios() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleSeg = ControladorContexto
				.getBundle("mensajeSeguridad");
		String mensajeBusqueda = "";
		ValidacionesAction validaciones = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		try {
			usuario = new Usuario();
			String condicionVigencia = Constantes.IS_NULL;
			if (Constantes.NOT.equals(vigencia)) {
				condicionVigencia = Constantes.IS_NOT_NULL;
			}
			Long cantidadUsuarios = usuarioDao.cantidadUsuarios(
					condicionVigencia, this.nombreBuscar);
			if (cantidadUsuarios != null) {
				paginador.paginar(cantidadUsuarios);
			}
			usuarios = usuarioDao.consultarUsuarios(paginador.getInicio(),
					paginador.getRango(), condicionVigencia, this.nombreBuscar);
			if ((this.usuarios == null || this.usuarios.size() <= 0)
					&& (this.nombreBuscar != null && !""
							.equals(this.nombreBuscar))) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			} else if (this.usuarios == null || this.usuarios.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (this.nombreBuscar != null
					&& !"".equals(this.nombreBuscar)) {
				mensajeBusqueda = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleSeg.getString("usuario_label_s"),
								bundle.getString("label_nombre") + ": " + '"'
										+ this.nombreBuscar + '"');
			}
			validaciones.setMensajeBusqueda(mensajeBusqueda);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesUsuario";
	}

	/**
	 * Allows to load the user interface for editing user roles or with partners
	 * to create a new user.
	 * 
	 * @param usuario
	 *            : User to register or edit.
	 * @return regUsuario: Rule navigation page that redirects the user to
	 *         register, which is loaded into editing or empty to add a new
	 *         user.
	 */
	public String registrarUsuario(Usuario usuario) {
		try {
			inicializarUsuario();
			if (usuario != null) {
				editar = true;
				this.usuario = usuario;
				consultarPersonaUsuario(usuario);
				consultarRolUsuario(usuario);
				controladorRolUsuario();
			} else {
				password2 = "";
				editar = false;
				this.usuario = new Usuario();
				vigenciaRolUsuario = Constantes.NUEVO;
			}
			consultarRolesNoAsignados(usuario);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "regUsuario";
	}

	/**
	 * Method for querying the person associated with the user.
	 * 
	 * @param usuario
	 *            : user to which the person is consulted.
	 */
	public void consultarPersonaUsuario(Usuario usuario) {
		try {
			persona = personaDao.consultarPersonaUsuario(usuario);
			if (persona == null) {
				persona = new Persona();
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Method for querying the roles are not assigned to the user.
	 * 
	 * @param usuario
	 *            : User who wants to see the roles.
	 * @throws Exception
	 */
	private void consultarRolesNoAsignados(Usuario usuario) throws Exception {
		roles = rolDao.consultarRolesNoAsignados(usuario);
	}

	/**
	 * Method to initialize the user.
	 */
	private void inicializarUsuario() {
		rolesUsuarioNuevos = new ArrayList<RolUsuario>();
		rolesUsuarioCreados = new ArrayList<RolUsuario>();
		rolesUsuario = new ArrayList<RolUsuario>();
		persona = new Persona();
		rol = new Rol();
		roles = new ArrayList<Rol>();
		vigenciaRolUsuario = Constantes.SI;
		renovarPassword = false;
	}

	/**
	 * Provides access user roles according to their effect, sent by a user
	 * parameter.
	 * 
	 */
	public void controladorRolUsuario() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			if (Constantes.NUEVO.equals(vigenciaRolUsuario)) {
				rolesUsuario = rolesUsuarioNuevos;
			} else {
				rolesUsuario = new ArrayList<RolUsuario>();
				Calendar hoy = Calendar.getInstance();
				Calendar ayer = Calendar.getInstance();
				ayer.add(Calendar.DATE, -1);
				if (rolesUsuarioCreados != null) {
					for (RolUsuario rolUsuario : rolesUsuarioCreados) {
						if (Constantes.NOT.equals(vigenciaRolUsuario)
								&& (rolUsuario.getFechaInicioVigencia().after(
										hoy.getTime()) || (rolUsuario
										.getFechaFinVigencia() != null && rolUsuario
										.getFechaFinVigencia().before(
												ayer.getTime())))) {
							rolesUsuario.add(rolUsuario);
						} else if (Constantes.SI.equals(vigenciaRolUsuario)
								&& !(rolUsuario.getFechaInicioVigencia().after(
										hoy.getTime()) || (rolUsuario
										.getFechaFinVigencia() != null && rolUsuario
										.getFechaFinVigencia().before(
												ayer.getTime())))) {
							rolesUsuario.add(rolUsuario);
						}
					}
				}
				if (rolesUsuario == null || rolesUsuario.size() <= 0) {
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
	 * Provides access user roles according to their effect, sent by a user
	 * parameter.
	 * 
	 * @param usuario
	 *            : User object to check.
	 */
	public void consultarRolUsuario(Usuario usuario) {
		try {
			rolesUsuarioCreados = rolUsuarioDao.consultarUsuarioRoles(usuario);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Allows to load detailed user information in the user interface.
	 * 
	 * @param usuario
	 *            : User object you want to see the information.
	 */
	public void verDetallesUsuario(Usuario usuario) {
		vigenciaRolUsuario = Constantes.SI;
		this.usuario = new Usuario();
		rolesUsuario = new ArrayList<RolUsuario>();
		try {
			this.usuario = usuario;
			consultarRolUsuario(usuario);
			controladorRolUsuario();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Allows you to save or edit a user, validating that the user name is not
	 * repeated in the database.
	 * 
	 * @modify Liseth.sJimenez 19/06/2012
	 * 
	 * @return inicializarBusqueda: Navigation rule that redirects the user
	 *         registration page for errors, otherwise it redirects the user
	 *         management.
	 */
	public String guardarUsuario() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = "message_registro_modificar";
		try {
			userTransaction.begin();
			validarObjetoEdoCivilPersona();
			if (editar) {
				if (renovarPassword) {
					usuario.setPassword(SecureIdentityLoginModule
							.doSign(usuario.getNombreUsuario()
									+ ControladorFechas.formatDate(
											usuario.getFechaCreacion(),
											Constantes.DATE_FORMAT_CREATION)));
				}
				usuario.setUserName(identity.getUserName());
				usuarioDao.editarUsuario(usuario);
				Persona ultimaPersona = personaDao
						.consultarPersonaUsuario(usuario);
				if (!persona.equals(ultimaPersona)) {
					if (ultimaPersona != null) {
						ultimaPersona.setUsuario(null);
						ultimaPersona.setUserName(identity.getUserName());
						personaDao.editarPersona(ultimaPersona);
					}
					persona.setUsuario(usuario);
					persona.setUserName(identity.getUserName());
					personaDao.editarPersona(persona);
				}
			} else {
				key = "message_registro_guardar";
				usuario.setFechaCreacion(new Date());
				usuario.setFechaInicioVigencia(new Date());
				usuario.setUserName(identity.getUserName());
				usuario.setPassword(SecureIdentityLoginModule.doSign(usuario
						.getNombreUsuario()
						+ ControladorFechas.formatDate(
								usuario.getFechaCreacion(),
								Constantes.DATE_FORMAT_CREATION)));
				usuarioDao.guardarUsuario(usuario);
				persona.setUsuario(usuario);
				persona.setUserName(identity.getUserName());
				personaDao.editarPersona(persona);
			}
			guardarRolUsuario();
			userTransaction.commit();
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(key),
							usuario.getNombreUsuario()));
		} catch (Exception e) {
			try {
				userTransaction.rollback();
			} catch (Exception e1) {
				ControladorContexto.mensajeError(e1);
			}
			ControladorContexto.quitarFacesMessages();
			ControladorContexto.mensajeError(e);
			return "regUsuario";
		}
		return inicializarBusqueda();
	}

	/**
	 * Method to validate the object of the civil status of the person before
	 * storing in the database, to which an instance of the class but I think
	 * there is no registration.
	 * 
	 * @author marisol.calderon
	 * @modify 17/03/2016 Wilhelm.Boada
	 */
	private void validarObjetoEdoCivilPersona() {
		if (this.persona != null) {
			CivilStatus civilStatus = this.persona.getCivilStatus();
			if (civilStatus != null && civilStatus.getId() == 0) {
				this.persona.setCivilStatus(null);
			}
		}
	}

	/**
	 * This method allows save and edit user roles in the database.
	 * 
	 * @throws Exception
	 */
	private void guardarRolUsuario() throws Exception {
		if (rolesUsuarioNuevos != null && rolesUsuarioNuevos.size() > 0) {
			for (RolUsuario rolUsuario : rolesUsuarioNuevos) {
				rolUsuario.setUserName(identity.getUserName());
				rolUsuarioDao.guardarRolUsuario(rolUsuario);
			}
		}
		if (rolesUsuarioCreados != null && rolesUsuarioCreados.size() > 0) {
			for (RolUsuario rolUsuario : rolesUsuarioCreados) {
				rolUsuario.setUserName(identity.getUserName());
				rolUsuarioDao.editarRolUsuario(rolUsuario);
			}
		}
	}

	/**
	 * To validate the user name so that it does not recur in the database.
	 * 
	 * @modify Liseth.Jimenez 19/06/2012
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 * 
	 * @throws Exception
	 */
	public void validarNombreUsuario(FacesContext context,
			UIComponent toValidate, Object value) throws Exception {
		String nombre = (String) value;
		String clientId = toValidate.getClientId(context);
		try {
			Usuario usuarioNombre = new Usuario();
			String resultado = "";

			if (editar) {
				usuarioNombre = usuarioDao.nombreUsuarioExiste(nombre,
						usuario.getId());
			} else {
				usuarioNombre = usuarioDao.nombreUsuarioExiste(nombre);
			}
			resultado = validarVigencia(usuarioNombre);
			if (Constantes.VIGENTE.equals(resultado)) {
				ControladorContexto.mensajeErrorEspecifico(clientId,
						"message_ya_existe_verifique", "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (Constantes.SIN_VIGENTE.equals(resultado)) {
				ControladorContexto.mensajeErrorEspecifico(clientId,
						"message_ya_existe_sin_vigencia", "mensaje");
				((UIInput) toValidate).setValid(false);
			}
			if (!EncodeFilter.validarXSS(nombre, clientId,
					"locate.regex.usuario")) {
				((UIInput) toValidate).setValid(false);
			}

		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

	}

	/**
	 * Validates that the password is valid
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 * @author Oscar.Amaya
	 */
	public void validarContrasena(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundle = ControladorContexto
				.getBundle("mensajeSeguridad");
		String txtPassword = (String) value;
		String idAsociado = (String) toValidate.getAttributes().get(
				"idAsociado");
		HtmlInputSecret txtPasswordConfirmar = (HtmlInputSecret) toValidate
				.findComponent(idAsociado);
		String clientId = toValidate.getClientId(context);
		if (!txtPasswordConfirmar.getSubmittedValue().equals(txtPassword)) {
			context.addMessage(
					clientId,
					new FacesMessage(
							FacesMessage.SEVERITY_ERROR,
							bundle.getString("usuario_message_contrasenas_diferentes"),
							bundle.getString("usuario_message_contrasenas_diferentes")));
		}
	}

	/**
	 * This method validates that the password change, the previous password is
	 * valid
	 * 
	 * @author Oscar.Amaya
	 * @modify 24/04/2013 marisol.calderon
	 * 
	 * @param context
	 *            : application context
	 * @param toValidate
	 *            : validate component
	 * @param value
	 *            : field value to be valid
	 */
	public void validarLogin(FacesContext context, UIComponent toValidate,
			Object value) {
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		try {
			String clientId = toValidate.getClientId(context);
			String contrasenaAnterior = (String) value;
			Usuario usuarioAutenticado = dao.consultarNombreUsuario(identity
					.getUserName());
			if (usuarioAutenticado != null) {
				String valPasw = contrasenaAnterior
						+ ControladorFechas.formatDate(
								usuarioAutenticado.getFechaCreacion(),
								Constantes.DATE_FORMAT_CREATION);
				contrasenaAnterior = SecureIdentityLoginModule.doSign(valPasw);
				usuarioAutenticado = dao.validarNombreUsuario(
						identity.getUserName(), contrasenaAnterior);
			}
			if (usuarioAutenticado == null) {
				context.addMessage(
						clientId,
						new FacesMessage(
								FacesMessage.SEVERITY_ERROR,
								bundleSeguridad
										.getString("usuario_message_contrasena_invalida"),
								bundleSeguridad
										.getString("usuario_message_contrasena_invalida")));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * Allows validate if the user is valid.
	 * 
	 * @param usuarioNombre
	 *            : User object to which the term is valid.
	 * @return result: 'empty' if the user name does not exist, 'current' if
	 *         there force and 'sinVigencia' if the user name exists no force.
	 */
	private String validarVigencia(Usuario usuarioNombre) {
		String result = "";
		if (usuarioNombre != null) {
			if (usuarioNombre.getFechaFinVigencia() != null) {
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
	 * @param vigente
	 *            : boolean that allows to know if the term is ended with 'true'
	 *            or start with 'false', the selected record in the user
	 *            interface.
	 * @return consultarUsuarios: Navigation rule that redirects users to the
	 *         Manage.
	 */
	public String vigenciaUsuarios(boolean vigente) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		StringBuilder regExito = new StringBuilder();
		String mensajeCambioVigencia = "message_inicio_vigencia_satisfactorio";
		try {
			if (vigente) {
				mensajeCambioVigencia = "message_fin_vigencia_satisfactorio";
				usuario.setFechaFinVigencia(new Date());
				usuario.setUserName(identity.getUserName());
				usuarioDao.editarUsuario(usuario);
				regExito.append(usuario.getNombreUsuario() + ", ");
			} else {
				usuario.setFechaFinVigencia(null);
				usuario.setUserName(identity.getUserName());
				usuarioDao.editarUsuario(usuario);
				regExito.append(usuario.getNombreUsuario() + ", ");
			}
			if (regExito.length() > 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString(mensajeCambioVigencia) + ": "
								+ regExito.substring(0, regExito.length() - 2));
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return consultarUsuarios();
	}

	/**
	 * Control the addition and subtraction of roles in rolesUsuarioNuevos list.
	 * 
	 * @param opcion
	 *            : Option to see taking place in the list.
	 * @param objRolUsuario
	 *            : Purpose of the role of the user you want to remove from the
	 *            list.
	 */
	public void controladorListaRoles(String opcion, RolUsuario objRolUsuario) {
		if (Constantes.NEW_ROL_USUARIO.equals(opcion)) {
			selectFechaInicio = null;
			selectFechaFin = null;
			renovarRol = false;
		} else if (Constantes.RE_NEW_ROL_USUARIO.equals(opcion)) {
			selectFechaInicio = objRolUsuario.getFechaInicioVigencia();
			selectFechaFin = objRolUsuario.getFechaFinVigencia();
			renovarRol = true;
		} else if (Constantes.ADD_ROL_USUARIO.equals(opcion)) {
			RolUsuario rolUsuario = new RolUsuario();
			rolUsuario.setRolUsuarioPK(new RolUsuarioPK(usuario, rol));
			rolUsuario.setFechaInicioVigencia(selectFechaInicio);
			rolUsuario.setFechaFinVigencia(selectFechaFin);
			rolUsuario.setFechaCreacion(new Date());
			rolesUsuarioNuevos.add(rolUsuario);
			rolesUsuario = rolesUsuarioNuevos;
			vigenciaRolUsuario = Constantes.NUEVO;
			roles.remove(rol);
		} else if (Constantes.CHANGE_ROL_USUARIO.equals(opcion)) {
			if (rolesUsuarioCreados != null) {
				for (int i = 0; i < rolesUsuarioCreados.size(); i++) {
					if (rolesUsuarioCreados.get(i).equals(rolUsuario)) {
						rolesUsuarioCreados.get(i).setFechaInicioVigencia(
								selectFechaInicio);
						rolesUsuarioCreados.get(i).setFechaFinVigencia(
								selectFechaFin);
						break;
					}
				}
			}
			controladorRolUsuario();
		} else if (Constantes.BORRAR_PERMISO.equals(opcion)) {
			roles.add(objRolUsuario.getRolUsuarioPK().getRol());
			rolesUsuarioNuevos.remove(objRolUsuario);
			rolesUsuario = rolesUsuarioNuevos;
		}
	}

	/**
	 * allows validate that the effective date to be not less than the start
	 * date of validity of the role to associate the user.
	 * 
	 * @param context
	 *            : application context
	 * @param component
	 *            : component to validate
	 * @param value
	 *            : field value to be valid
	 */
	public void validarFechas(FacesContext context, UIComponent component,
			Object value) {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		Date fechaFin = (Date) value;
		UIInput findComponent = (UIInput) component
				.findComponent("calFechaInicio");
		Date fechaInicio = (Date) findComponent.getValue();

		if (fechaInicio != null && fechaFin != null) {
			if (fechaFin.before(fechaInicio)) {
				FacesMessage message = new FacesMessage(
						FacesMessage.SEVERITY_ERROR,
						bundle.getString("message_validar_fechas_menor"), "");
				throw new ValidatorException(message);
			}
		}
	}

	/**
	 * Prepare the view so that the user can change their password
	 * 
	 * @author Oscar.Amaya
	 * 
	 * @return cambioContrasena: Navigation rule that targets the template user
	 *         password change.
	 * @throws Exception
	 */
	public String nuevaContrasena() throws Exception {
		usuario = usuarioDao.consultarUsuario(identity.getUserName());
		cambioContrasena = new CambioContrasena();
		return "cambioContrasena";
	}

	/**
	 * Performs Changing the password of a user in the database
	 * 
	 * @author Oscar.Amaya
	 * @modify 24/04/2013 marisol.calderon
	 * 
	 * @return gesLogin: navigation rule that addresses the login page to log on
	 *         to change the password.
	 */
	public String cambiarContrasena() {
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("mensajeSeguridad");
		try {
			String newPassword = SecureIdentityLoginModule
					.doSign(cambioContrasena.getContrasenaNueva()
							+ ControladorFechas.formatDate(
									usuario.getFechaCreacion(),
									Constantes.DATE_FORMAT_CREATION));
			usuario.setPassword(newPassword);
			usuario.setUserName(identity.getUserName());
			usuarioDao.editarUsuario(usuario);
			empresaHaciendaSesion.limpiarEmpresaSesion();
			identity.logout(true);
			String format = MessageFormat.format(bundleSeguridad
					.getString("usuario_message_exito_cambio_contrasena"),
					usuario.getNombreUsuario());
			ControladorContexto.mensajeInformacion(null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesLogin";
	}

	/**
	 * This class is used to change the password
	 * 
	 * @author Oscar.Amaya
	 * 
	 */
	public class CambioContrasena implements Serializable {

		private String contrasenaAnterior;
		private String contrasenaNueva;
		private String contrasenaConfirmada;

		/**
		 * @return contrasenaAnterior: It is the user's old password
		 */
		public String getContrasenaAnterior() {
			return contrasenaAnterior;
		}

		/**
		 * @param contrasenaAnterior
		 *            : It is the user's old password
		 */
		public void setContrasenaAnterior(String contrasenaAnterior) {
			this.contrasenaAnterior = contrasenaAnterior;
		}

		/**
		 * @return contrasenaNueva: It is the new user password
		 */
		public String getContrasenaNueva() {
			return contrasenaNueva;
		}

		/**
		 * @param contrasenaNueva
		 *            : It is the new user password
		 */
		public void setContrasenaNueva(String contrasenaNueva) {
			this.contrasenaNueva = contrasenaNueva;
		}

		/**
		 * @return contrasenaConfirmada : The new password is confirmed by the
		 *         user
		 */
		public String getContrasenaConfirmada() {
			return contrasenaConfirmada;
		}

		/**
		 * @param contrasenaConfirmada
		 *            : The new password is confirmed by the user
		 */
		public void setContrasenaConfirmada(String contrasenaConfirmada) {
			this.contrasenaConfirmada = contrasenaConfirmada;
		}
	}
}