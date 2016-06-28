package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.HrDao;
import co.informatix.erp.humanResources.dao.TeamDao;
import co.informatix.erp.humanResources.dao.TeamMembersDao;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.humanResources.entities.Team;
import co.informatix.erp.humanResources.entities.TeamMembers;
import co.informatix.erp.humanResources.entities.TeamMembersPK;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.erp.utils.ValidacionesAction.DatosGuardar;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the teams that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TeamAction implements Serializable {

	@EJB
	private TeamDao teamDao;
	@EJB
	private HrDao hrDao;
	@EJB
	private TeamMembersDao teamMembersDao;

	private Paginador pagination = new Paginador();
	private Team team;
	private Team teamActualSelected;
	private Hr hrActualSelected;
	private TeamMembersAction teamMembersAction;
	private HrAction hrAction;
	private List<Team> teamList;
	private List<Hr> hrActualList;
	private String nameSearch;

	/**
	 * @return pagination: Management paged list of teams.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of teams.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return team: Object of class team.
	 */
	public Team getTeam() {
		return team;
	}

	/**
	 * @param team
	 *            : Object of class team.
	 */
	public void setTeam(Team team) {
		this.team = team;
	}

	/**
	 * @return teamActualSelected: team selected in the team table.
	 */
	public Team getTeamActualSelected() {
		return teamActualSelected;
	}

	/**
	 * @param teamActualSelected
	 *            : team selected in the team table.
	 */
	public void setTeamActualSelected(Team teamActualSelected) {
		this.teamActualSelected = teamActualSelected;
	}

	/**
	 * @return hrActualSelected: hr selected in the hr table.
	 */
	public Hr getHrActualSelected() {
		return hrActualSelected;
	}

	/**
	 * @param hrActualSelected
	 *            : hr selected in the hr table.
	 */
	public void setHrActualSelected(Hr hrActualSelected) {
		this.hrActualSelected = hrActualSelected;
	}

	/**
	 * @return teamMembersAction: Object of teamMembersAction.
	 */
	public TeamMembersAction getTeamMembersAction() {
		return teamMembersAction;
	}

	/**
	 * @param teamMembersAction
	 *            : Object of teamMembersAction.
	 */
	public void setTeamMembersAction(TeamMembersAction teamMembersAction) {
		this.teamMembersAction = teamMembersAction;
	}

	/**
	 * @return hrAction: Object of HrAction.
	 */
	public HrAction getHrAction() {
		return hrAction;
	}

	/**
	 * @param hrAction
	 *            : Object of HrAction.
	 */
	public void setHrAction(HrAction hrAction) {
		this.hrAction = hrAction;
	}

	/**
	 * @return teamList: gets the teams list
	 */
	public List<Team> getTeamList() {
		return teamList;
	}

	/**
	 * @param teamList
	 *            : sets the teams list
	 */
	public void setTeamList(List<Team> teamList) {
		this.teamList = teamList;
	}

	/**
	 * @return nameSearch: Team name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Team name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 * @return consultTeam: Method that consultation team and load the template
	 *         with the information found.
	 */
	public String initializeTeam() {
		if (ControladorContexto.getFacesContext() != null) {
			this.teamMembersAction = ControladorContexto
					.getContextBean(TeamMembersAction.class);
			this.hrAction = ControladorContexto.getContextBean(HrAction.class);
			this.hrAction.setHrListSelected(new ArrayList<Hr>());
		}
		hrAction.setFlagButton(true);
		this.teamActualSelected = null;
		this.nameSearch = "";
		return consultTeam();
	}

	/**
	 * Method to edit or create a new assignment of team.
	 * 
	 * @param team
	 *            :Object of team are adding or editing.
	 * 
	 * @return regTeam: Template redirects to register team.
	 * 
	 */
	public String addEditTeams(Team team) {
		if (team != null) {
			this.team = team;
		} else {
			this.team = new Team();
		}
		return "regTeam";
	}

	/**
	 * See the existing team list.
	 * 
	 * @return gesTeam: Navigation rule that redirects to manage the team.
	 */
	public String consultTeam() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleHumanResources = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder unionSearchMessages = new StringBuilder();
		String searchMessages = "";
		this.teamList = new ArrayList<Team>();
		try {
			advancedSearch(consult, parameters, bundle, unionSearchMessages);
			Long amount = teamDao.amountTeam(consult, parameters);
			if (amount != null && amount > 0) {
				pagination.paginar(amount);
				this.teamList = teamDao.consultTeams(pagination.getInicio(),
						pagination.getRango(), consult, parameters);
			}
			if ((teamList == null || teamList.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (teamList == null || teamList.size() <= 0) {
				searchMessages = bundle
						.getString("message_no_existen_registros");
			} else if (!"".equals(unionSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessages = MessageFormat.format(message,
						bundleHumanResources.getString("team_label"),
						unionSearchMessages);
			}
			validations.setMensajeBusqueda(searchMessages);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return "gesTeam";
	}

	/**
	 * This method builds a query with advanced search; it also render the
	 * messages to be displayed depending on the search criteria selected by the
	 * user.
	 * 
	 * @param consult
	 *            : query to concatenate.
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags.
	 * @param unionMessagesSearch
	 *            : message search.
	 * 
	 */
	private void advancedSearch(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append("WHERE UPPER(t.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Selects a single team for display the associated transaction.
	 * 
	 * @param teamSelected
	 *            : team selected on the view.
	 */
	public void selectTeam(Team teamSelected) {
		this.teamActualSelected = new Team();
		teamSelected.setSelected(true);
		for (Team team : teamList) {
			if (team.isSelected()) {
				if (team.getIdTeam() == teamSelected.getIdTeam()) {
					this.teamActualSelected = team;
				} else {
					team.setSelected(false);
				}
			}
		}
	}

	/**
	 * Selects a single hr for display the associated transaction.
	 * 
	 * @param flagSelected
	 *            : team selected on the view.
	 */
	public void selectHr(boolean flagSelected) {
		this.hrActualSelected.setSeleccionado(flagSelected);
		if (this.hrActualSelected.isSeleccionado()) {
			this.hrAction.getHrListSelected().add(hrActualSelected);
		} else {
			this.hrAction.getHrListSelected().remove(hrActualSelected);
		}

	}

	/**
	 * this method allows validate if be can add more workers to the team.
	 * 
	 */
	public void validateSizeTeam() {
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		ResourceBundle bundle = ControladorContexto
				.getBundle("messageHumanResources");
		String searchMessage = "";
		if (this.hrAction.getHrListSelected().size() >= teamActualSelected
				.getSize()) {
			searchMessage = bundle
					.getString("team_messeage_can_not_add_more_workers");
			validations.setMensajeBusquedaPopUp(searchMessage);
		} else {
			selectHr(true);
		}
	}

	/**
	 * Show the teamMembers associated to team.
	 * 
	 */
	public void showTeamMembers() {
		if (teamMembersAction != null) {
			teamMembersAction.setTeamSelected(teamActualSelected);
			teamMembersAction.initializeTeamMembers();
		}
	}

	/**
	 * Method used to save or edit teams
	 * 
	 * @return initializeTeam(): Redirects to manage the team list with teams
	 *         updated.
	 */
	public String saveUpdateTeam() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String message = "message_registro_modificar";
		try {

			if (team.getIdTeam() != 0) {
				teamDao.editTeam(team);
			} else {
				teamDao.saveTeam(team);
				message = "message_registro_guardar";
			}
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(bundle.getString(message),
							team.getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeTeam();
	}

	/**
	 * Method that allows delete a team of database.
	 * 
	 * @return initializeTeam: Redirects to initialize variables and load the
	 *         template team management.
	 */
	public String deleteTeam() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			if (teamMembersAction.getTeamMembersList() != null) {
				for (TeamMembers teamMembers : teamMembersAction
						.getTeamMembersList()) {
					teamMembersAction.setFlagDelete(true);
					teamMembersAction.setTeamMembers(teamMembers);
					teamMembersAction.deleteTeamMembers();
				}
			}
			teamDao.deleteTeam(team);
			ControladorContexto.mensajeInformacion(
					null,
					MessageFormat.format(
							bundle.getString("message_registro_eliminar"),
							team.getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					team.getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}

		return initializeTeam();
	}

	/**
	 * this method allows consult the teammembers list associated to a team and
	 * save in a hr list.
	 * 
	 * @return hrAction.initializeSearch: Redirects to initialize variables and
	 *         load hr list.
	 * 
	 */
	public String initializeHr() {
		hrActualList = new ArrayList<Hr>();
		try {
			if (teamMembersAction.getTeamMembersList() != null
					&& teamMembersAction.getTeamMembersList().size() > 0) {
				hrAction.setHrListSelected(new ArrayList<Hr>());
				for (TeamMembers teammebers : teamMembersAction
						.getTeamMembersList()) {
					hrAction.getHrListSelected().add(
							hrDao.hrById(teammebers.getTeamMembersPK().getHr()
									.getIdHr()));
				}
				hrActualList.addAll(hrAction.getHrListSelected());
			} else {
				hrAction.setHrListSelected(new ArrayList<Hr>());
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return hrAction.initializeSearch();
	}

	/**
	 * This method identifies the teammember to delete and save.
	 * 
	 */
	public void saveEditTeamMembers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			List<Integer> currentIds = new ArrayList<Integer>();
			List<Integer> newsIds = new ArrayList<Integer>();
			if (this.hrAction.getHrListSelected() != null) {
				if (this.hrActualList != null) {
					for (Hr hr : hrActualList) {
						currentIds.add(hr.getIdHr());
					}
					for (Hr hr : this.hrAction.getHrListSelected()) {
						newsIds.add(hr.getIdHr());
					}
					List<DatosGuardar> dataList = ValidacionesAction
							.validarListas(currentIds, newsIds);

					for (DatosGuardar saveData : dataList) {
						String action = saveData.getAccion();
						Hr hr = new Hr();
						hr.setIdHr(saveData.getIdClase());
						if (Constantes.QUERY_DELETE.equals(action)) {
							TeamMembers teamMembers = teamMembersDao
									.associatedTeamMembersByIds(
											this.teamActualSelected.getIdTeam(),
											hr.getIdHr());
							teamMembersDao.deleteTeamMembers(teamMembers);
						} else if (Constantes.QUERY_INSERT.equals(action)) {
							saveTeamMembers(hr);
						}
					}
				}
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_modificar"),
					teamActualSelected.getName()));
			showTeamMembers();
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows save the teamMembers related to the team.
	 * 
	 * @param hr
	 *            : hr related to a team.
	 * @throws Exception
	 * 
	 */
	private void saveTeamMembers(Hr hr) throws Exception {
		TeamMembers teamMembers = new TeamMembers();
		teamMembers.setTeamMembersPK(new TeamMembersPK());
		teamMembers.getTeamMembersPK().setHr(hr);
		teamMembers.getTeamMembersPK().setTeam(teamActualSelected);
		teamMembers.setLead(false);
		teamMembers.setStatistician(false);
		teamMembersDao.saveTeamMembers(teamMembers);
	}
}