package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.TeamDao;
import co.informatix.erp.humanResources.entities.Team;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

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

	private Paginador pagination = new Paginador();
	private Team team;
	private Team teamActualSelected;
	private TeamMembersAction teamMembersAction;
	private List<Team> teamList;
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
		}
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
	 * Show the invoices item associated to purchase invoices.
	 * 
	 */
	public void showTeamMembers() {
		if (teamMembersAction != null) {
			teamMembersAction.setTeamSelected(teamActualSelected);
			teamMembersAction.consultTeamMembers();
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
}
