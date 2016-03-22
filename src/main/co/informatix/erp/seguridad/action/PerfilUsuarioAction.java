package co.informatix.erp.seguridad.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;
import javax.transaction.UserTransaction;

import org.richfaces.event.ItemChangeEvent;

import co.informatix.erp.organizaciones.action.EmpresaAction;
import co.informatix.erp.organizaciones.entities.Empresa;
import co.informatix.erp.recursosHumanos.action.PersonaAction;
import co.informatix.erp.recursosHumanos.dao.PersonaDao;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.seguridad.dao.PermisoPersonaEmpresaDao;
import co.informatix.erp.seguridad.dao.UsuarioDao;
import co.informatix.erp.seguridad.entities.PermisoPersonaEmpresa;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.security.action.IdentityAction;
import co.informatix.security.entities.Usuario;

/**
 * This class allows business logic User Profile System session.
 * 
 * The logic is to check and modify personal data, list the companies they have
 * permission to access and modify the information of the parameters of each.
 * 
 * 
 * @author marisol.calderon
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class PerfilUsuarioAction implements Serializable {

	@EJB
	private PersonaDao personaDao;
	@EJB
	private UsuarioDao usuarioDao;
	@EJB
	private PermisoPersonaEmpresaDao permisoPersonaEmpresaDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private List<PermisoPersonaEmpresa> listaEmpresasConPermisoAcceso;
	private List<SelectItem> itemsValores;
	private Paginador pagination = new Paginador();
	private PermisoPersonaEmpresa permisoPersonaEmpresa;
	private Persona personaSesion;
	private String tabSelect = Constantes.N_TAB;
	private boolean guardarPersonaDesdePerfil;

	/**
	 * @return pagination: Object pager functions from the list of companies with
	 *         permissions to the person.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :Object pager functions from the list of companies with
	 *            permissions to the person.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * 
	 * @return tabSelect: Variable that gets the name of the tab you want to
	 *         load as selected.
	 */
	public String getTabSelect() {
		return tabSelect;
	}

	/**
	 * @param tabSelect
	 *            : Variable that gets the name of the tab you want to load as
	 *            selected.
	 */
	public void setTabSelect(String tabSelect) {
		this.tabSelect = tabSelect;
	}

	/**
	 * @return listaEmpresasConPermisoAcceso: list of companies to which the
	 *         user has access.
	 */
	public List<PermisoPersonaEmpresa> getListaEmpresasConPermisoAcceso() {
		return listaEmpresasConPermisoAcceso;
	}

	/**
	 * @param listaEmpresasConPermisoAcceso
	 *            : list of companies to which the user has access.
	 */
	public void setListaEmpresasConPermisoAcceso(
			List<PermisoPersonaEmpresa> listaEmpresasConPermisoAcceso) {
		this.listaEmpresasConPermisoAcceso = listaEmpresasConPermisoAcceso;
	}

	/**
	 * @return permisoPersonaEmpresa: Business person covered by the permit.
	 */
	public PermisoPersonaEmpresa getPermisoPersonaEmpresa() {
		return permisoPersonaEmpresa;
	}

	/**
	 * @param permisoPersonaEmpresa
	 *            : Business person covered by the permit.
	 */
	public void setPermisoPersonaEmpresa(
			PermisoPersonaEmpresa permisoPersonaEmpresa) {
		this.permisoPersonaEmpresa = permisoPersonaEmpresa;
	}

	/**
	 * @return List<SelectItem>: The list of items of the values that can take
	 *         the parameter.
	 */
	public List<SelectItem> getItemsValores() {
		return itemsValores;
	}

	/**
	 * @return guardarPersonaDesdePerfil: variable that shows whether a person
	 *         is saved from the user profile.
	 */
	public boolean isGuardarPersonaDesdePerfil() {
		return guardarPersonaDesdePerfil;
	}

	/**
	 * @param guardarPersonaDesdePerfil
	 *            : variable that shows whether a person is saved from the user
	 *            profile.
	 */
	public void setGuardarPersonaDesdePerfil(boolean guardarPersonaDesdePerfil) {
		this.guardarPersonaDesdePerfil = guardarPersonaDesdePerfil;
	}

	/**
	 * Method to load the user profile of the person in session.
	 * 
	 * @param pestana
	 *            : variable that lets you know which tab is loaded and
	 *            information being queried.
	 * @return navigation rule that loads the template with user profile
	 *         information.
	 */
	public String cargarPerfilDeUsuario(String pestana) {
		try {
			Usuario usuarioSesion = usuarioDao.searchUsuario(identity
					.getUserName());
			Integer idUsuario = usuarioSesion.getId();
			Persona personaSesionTemp = personaDao.consultarPersona(idUsuario);
			personaSesion = personaSesionTemp;
			if (Constantes.N_TAB.equals(pestana)) {
				cargarDatosPersonales();
			} else if (Constantes.F_TAB.equals(pestana)) {
				cargarEmpresasConPermisosAcceso();
			}
			setTabSelect(pestana);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesPerfilUsuario";
	}

	/**
	 * Method of uploading personal data of the person in session.
	 * 
	 * @throws Exception
	 */
	private void cargarDatosPersonales() throws Exception {
		PersonaAction personaAction = ControladorContexto
				.getContextBean(PersonaAction.class);
		if (personaSesion != null) {
			personaAction.cargarDetallesUnaPersona(personaSesion);
			personaAction.registrarPersona(personaSesion);
		}
	}

	/**
	 * Method of uploading information to the companies that have access
	 * permissions on the system.
	 * 
	 * @throws Exception
	 */
	private void cargarEmpresasConPermisosAcceso() throws Exception {
		EmpresaAction empresaAction = ControladorContexto
				.getContextBean(EmpresaAction.class);
		listaEmpresasConPermisoAcceso = new ArrayList<PermisoPersonaEmpresa>();
		if (personaSesion != null) {
			/* Companies with access permission of the person */
			listaEmpresasConPermisoAcceso = permisoPersonaEmpresaDao
					.consultarPermisosPersonaEmpresaAccesoUsuario(personaSesion
							.getDocumento());
			if (listaEmpresasConPermisoAcceso != null) {
				for (PermisoPersonaEmpresa permisoPerEmp : listaEmpresasConPermisoAcceso) {
					permisoPersonaEmpresaDao
							.consultarDetallesPermisoPersonaEmpresa(permisoPerEmp);
					/* Company details are loaded */
					Empresa empresaPermiso = permisoPerEmp.getEmpresa();
					empresaAction.cargarDetallesUnaEmpresa(empresaPermiso);
					permisoPerEmp.setEmpresa(empresaPermiso);
				}
			}
		}

	}

	/**
	 * Method to execute the action of the tabs to be selected by the user
	 * 
	 * @param event
	 *            : event that runs when a tab is selected
	 */
	public void cambioPestana(ItemChangeEvent event) {
		try {
			String idPestana = event.getNewItemName();
			if (idPestana != null && !"".equals(idPestana)) {
				if (Constantes.N_TAB.equals(idPestana)) {
					cargarDatosPersonales();
				} else if (Constantes.F_TAB.equals(idPestana)) {
					cargarEmpresasConPermisosAcceso();
				}
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows change the company selects a default when you log into
	 * the system.
	 * 
	 * @return cargarPerfilDeUsuario: method to load the user profile in the
	 *         required Tab.
	 */
	public String predeterminarEmpresa() {
		ResourceBundle bundleSeguridad = ControladorContexto
				.getBundle("messageSecurity");
		PermisoPersonaEmpresaAction permisoPersonaEmpresaAction = ControladorContexto
				.getContextBean(PermisoPersonaEmpresaAction.class);
		try {
			if (permisoPersonaEmpresa != null && personaSesion != null) {
				this.userTransaction.begin();
				PermisoPersonaEmpresa permPersonaEmpresaPre = permisoPersonaEmpresaDao
						.consultarExistePredeterminado(personaSesion
								.getDocumento());
				if (permPersonaEmpresaPre != null) {
					permPersonaEmpresaPre.setPredeterminado(false);
					permisoPersonaEmpresaDao
							.modificarPermisoPersonaEmpresa(permPersonaEmpresaPre);
				}
				permisoPersonaEmpresa.setUserName(identity.getUserName());
				permisoPersonaEmpresa.setPredeterminado(true);
				permisoPersonaEmpresaAction.validarNulos(permisoPersonaEmpresa);
				permisoPersonaEmpresaDao
						.modificarPermisoPersonaEmpresa(permisoPersonaEmpresa);
				this.userTransaction.commit();
				String message = bundleSeguridad
						.getString("user_profile_message_modify_default");
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(message, permisoPersonaEmpresa.getEmpresa()
								.getNombre()));
			}
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();

			} catch (Exception e2) {
				ControladorContexto.mensajeError(e2);
			}
			ControladorContexto.mensajeError(e);
		}
		return cargarPerfilDeUsuario(Constantes.F_TAB);
	}

}
