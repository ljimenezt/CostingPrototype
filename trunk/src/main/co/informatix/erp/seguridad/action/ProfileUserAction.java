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

import co.informatix.erp.organizations.action.BusinessAction;
import co.informatix.erp.organizations.entities.Business;
import co.informatix.erp.recursosHumanos.action.PersonaAction;
import co.informatix.erp.recursosHumanos.dao.PersonaDao;
import co.informatix.erp.recursosHumanos.entities.Persona;
import co.informatix.erp.seguridad.dao.PermissionPersonBusinessDao;
import co.informatix.erp.seguridad.dao.UserDao;
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
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ProfileUserAction implements Serializable {

	@EJB
	private PersonaDao personDao;
	@EJB
	private UserDao userDao;
	@EJB
	private PermissionPersonBusinessDao permissionPersonBusinessDao;

	@Inject
	private IdentityAction identity;
	@Resource
	private UserTransaction userTransaction;

	private List<PermisoPersonaEmpresa> listBusinessWithPermissionAcces;
	private List<SelectItem> itemsValues;
	private Paginador pagination = new Paginador();
	private PermisoPersonaEmpresa permissionPersonBusiness;
	private Persona personSesion;
	private String tabSelect = Constantes.N_TAB;
	private boolean savePersonFromProfile;

	/**
	 * @return pagination: Object pager functions from the list of companies
	 *         with permissions to the person.
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
	 * @return listBusinessWithPermissionAcces: list of companies to which the
	 *         user has access.
	 */
	public List<PermisoPersonaEmpresa> getListBusinessWithPermissionAcces() {
		return listBusinessWithPermissionAcces;
	}

	/**
	 * @param listBusinessWithPermissionAcces
	 *            : list of companies to which the user has access.
	 */
	public void setListBusinessWithPermissionAcces(
			List<PermisoPersonaEmpresa> listBusinessWithPermissionAcces) {
		this.listBusinessWithPermissionAcces = listBusinessWithPermissionAcces;
	}

	/**
	 * @return permissionPersonBusiness: Business person covered by the permit.
	 */
	public PermisoPersonaEmpresa getPermissionPersonBusiness() {
		return permissionPersonBusiness;
	}

	/**
	 * @param permissionPersonBusiness
	 *            : Business person covered by the permit.
	 */
	public void setPermissionPersonBusiness(
			PermisoPersonaEmpresa permissionPersonBusiness) {
		this.permissionPersonBusiness = permissionPersonBusiness;
	}

	/**
	 * @return List<SelectItem>: The list of items of the values that can take
	 *         the parameter.
	 */
	public List<SelectItem> getItemsValues() {
		return itemsValues;
	}

	/**
	 * @return savePersonFromProfile: variable that shows whether a person
	 *         is saved from the user profile.
	 */
	public boolean isSavePersonFromProfile() {
		return savePersonFromProfile;
	}

	/**
	 * @param savePersonFromProfile
	 *            : variable that shows whether a person is saved from the user
	 *            profile.
	 */
	public void setSavePersonFromProfile(boolean savePersonFromProfile) {
		this.savePersonFromProfile = savePersonFromProfile;
	}

	/**
	 * Method to load the user profile of the person in session.
	 * 
	 * @param pestana
	 *            : variable that lets you know which tab is loaded and
	 *            information being queried.
	 * @return gesProfileUser: navigation rule that loads the template with user profile
	 *         information.
	 */
	public String loadProfileOfUser(String pestana) {
		try {
			Usuario userSession = userDao.searchUsuario(identity
					.getUserName());
			Integer idUsuario = userSession.getId();
			Persona personaSesionTemp = personDao.consultPerson(idUsuario);
			personSesion = personaSesionTemp;
			if (Constantes.N_TAB.equals(pestana)) {
				loadPersonalData();
			} else if (Constantes.F_TAB.equals(pestana)) {
				loadBusinessWithPermissionAcces();
			}
			setTabSelect(pestana);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesProfileUser";
	}

	/**
	 * Method of uploading personal data of the person in session.
	 * 
	 * @throws Exception
	 */
	private void loadPersonalData() throws Exception {
		PersonaAction personAction = ControladorContexto
				.getContextBean(PersonaAction.class);
		if (personSesion != null) {
			personAction.loadDetailsOnePerson(personSesion);
			personAction.registerPerson(personSesion);
		}
	}

	/**
	 * Method of uploading information to the companies that have access
	 * permissions on the system.
	 * 
	 * @throws Exception
	 */
	private void loadBusinessWithPermissionAcces() throws Exception {
		BusinessAction businessAction = ControladorContexto
				.getContextBean(BusinessAction.class);
		listBusinessWithPermissionAcces = new ArrayList<PermisoPersonaEmpresa>();
		if (personSesion != null) {
			/* Companies with access permission of the person */
			listBusinessWithPermissionAcces = permissionPersonBusinessDao
					.consultPermissionsPersonEBusinessAccessUser(personSesion
							.getDocumento());
			if (listBusinessWithPermissionAcces != null) {
				for (PermisoPersonaEmpresa permissionPersonBusiness : listBusinessWithPermissionAcces) {
					permissionPersonBusinessDao
							.consultDetailsPermissionPersonBusiness(permissionPersonBusiness);
					/* Company details are loaded */
					Business empresaPermiso = permissionPersonBusiness.getEmpresa();
					businessAction.loadDetailsOneBusiness(empresaPermiso);
					permissionPersonBusiness.setEmpresa(empresaPermiso);
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
	public void changedTab(ItemChangeEvent event) {
		try {
			String idTab = event.getNewItemName();
			if (idTab != null && !"".equals(idTab)) {
				if (Constantes.N_TAB.equals(idTab)) {
					loadPersonalData();
				} else if (Constantes.F_TAB.equals(idTab)) {
					loadBusinessWithPermissionAcces();
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
	 * @return loadProfileOfUser: method to load the user profile in the
	 *         required Tab.
	 */
	public String predetermineBusiness() {
		ResourceBundle bundleSecurity = ControladorContexto
				.getBundle("messageSecurity");
		PermissionPersonBusinessAction permissionPersonBusinessAction = ControladorContexto
				.getContextBean(PermissionPersonBusinessAction.class);
		try {
			if (permissionPersonBusiness != null && personSesion != null) {
				this.userTransaction.begin();
				PermisoPersonaEmpresa permissionPersonBusinessPre = permissionPersonBusinessDao
						.consultExistPredetermined(personSesion.getDocumento());
				if (permissionPersonBusinessPre != null) {
					permissionPersonBusinessPre.setPredeterminado(false);
					permissionPersonBusinessDao
							.editPermissionPersonCompany(permissionPersonBusinessPre);
				}
				permissionPersonBusiness.setUserName(identity.getUserName());
				permissionPersonBusiness.setPredeterminado(true);
				permissionPersonBusinessAction.nullValidate(permissionPersonBusiness);
				permissionPersonBusinessDao
						.editPermissionPersonCompany(permissionPersonBusiness);
				this.userTransaction.commit();
				String message = bundleSecurity
						.getString("user_profile_message_modify_default");
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(message, permissionPersonBusiness.getEmpresa()
								.getName()));
			}
		} catch (Exception e) {
			try {
				this.userTransaction.rollback();

			} catch (Exception e2) {
				ControladorContexto.mensajeError(e2);
			}
			ControladorContexto.mensajeError(e);
		}
		return loadProfileOfUser(Constantes.F_TAB);
	}

}
