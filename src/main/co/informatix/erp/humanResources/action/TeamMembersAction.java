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

import co.informatix.erp.humanResources.dao.TeamMembersDao;
import co.informatix.erp.humanResources.entities.Team;
import co.informatix.erp.humanResources.entities.TeamMembers;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;

/**
 * 
 * This class is all the logic related to the creation, updating, and deleting
 * the teamMember that may exist.
 * 
 * @author Wilhelm.Boada
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class TeamMembersAction implements Serializable {

	@EJB
	private TeamMembersDao teamMembersDao;

	private TeamMembers teamMembers;
	private Team teamSelected;
	private Paginador pagination = new Paginador();
	private List<TeamMembers> teamMembersList;
	private String nameSearch;
	private boolean flagDelete;

	/**
	 * @return teamMembers: Object of class TeamMembers for register or delete.
	 */
	public TeamMembers getTeamMembers() {
		return teamMembers;
	}

	/**
	 * @param teamMembers
	 *            : Object of class TeamMembers for register or delete.
	 */
	public void setTeamMembers(TeamMembers teamMembers) {
		this.teamMembers = teamMembers;
	}

	/**
	 * @return teamSelected: Object of class Team selected.
	 */
	public Team getTeamSelected() {
		return teamSelected;
	}

	/**
	 * @param teamSelected
	 *            : Object of class Team selected.
	 */
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}

	/**
	 * @return pagination: Management paged list of teamMembers.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            : Management paged list of teamMembers.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return teamMembersList: teamMembersList query the database.
	 */
	public List<TeamMembers> getTeamMembersList() {
		return teamMembersList;
	}

	/**
	 * @param teamMembersList
	 *            : teamMembersList query the database.
	 */
	public void setTeamMembersList(List<TeamMembers> teamMembersList) {
		this.teamMembersList = teamMembersList;
	}

	/**
	 * @return nameSearch: TeamMembers name to search.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : TeamMembers name to search.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return flagDelete: TeamMembers name to search.
	 */
	public boolean getFlagDelete() {
		return flagDelete;
	}

	/**
	 * @param flagDelete
	 */
	public void setFlagDelete(boolean flagDelete) {
		this.flagDelete = flagDelete;
	}

	/**
	 * Method to initialize the fields in the search.
	 * 
	 */
	public void initializeTeamMembers() {
		this.nameSearch = "";
		consultTeamMembers();
	}

	/**
	 * Consult the teamMembers list.
	 * 
	 */
	public void consultTeamMembers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleCosts = ControladorContexto
				.getBundle("messageCosts");
		ValidacionesAction validations = (ValidacionesAction) ControladorContexto
				.getContextBean(ValidacionesAction.class);
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		String searchMessages = "";
		StringBuilder unionSearchMessages = new StringBuilder();
		this.teamMembersList = new ArrayList<TeamMembers>();
		try {
			advancedSearch(consult, parameters, bundle, unionSearchMessages);
			Long amount = teamMembersDao.amountTeamMembers(consult, parameters);
			if (amount != null && amount > 0) {
				pagination.paginarRangoDefinido(amount, 5);
				this.teamMembersList = teamMembersDao.consultTeamsMembers(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters);
			}
			if ((teamMembersList == null || teamMembersList.size() <= 0)
					&& !"".equals(unionSearchMessages.toString())) {
				searchMessages = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								unionSearchMessages);
			} else if (teamMembersList == null || teamMembersList.size() <= 0) {
				searchMessages = bundle
						.getString("message_no_existen_registros");
			} else if (!"".equals(unionSearchMessages.toString())) {
				String message = bundle
						.getString("message_existen_registros_criterio_busqueda");
				searchMessages = MessageFormat.format(message, bundleCosts
						.getString("activities_and_hr_label_workers"),
						unionSearchMessages);
			}
			validations.setMensajeBusquedaPopUp(searchMessages);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
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
		boolean queryAdded = false;
		if (this.teamSelected != null) {
			consult.append("WHERE t.idTeam = :idTeam ");
			SelectItem item = new SelectItem(this.teamSelected.getIdTeam(),
					"idTeam");
			parameters.add(item);
			queryAdded = true;
		}
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			consult.append(queryAdded ? "AND " : "WHERE ");
			consult.append("UPPER(h.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method that allows delete a teamMembers of database.
	 * 
	 */
	public void deleteTeamMembers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			teamMembersDao.deleteTeamMembers(teamMembers);
			if (!flagDelete) {
				ControladorContexto.mensajeInformacion(null, MessageFormat
						.format(bundle.getString("message_registro_eliminar"),
								teamMembers.getTeamMembersPK().getHr()
										.getName()
										+ " "
										+ teamMembers.getTeamMembersPK()
												.getHr().getFamilyName()));
			}
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					teamMembers.getTeamMembersPK().getHr().getName()
							+ " "
							+ teamMembers.getTeamMembersPK().getHr()
									.getFamilyName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		flagDelete = false;
		consultTeamMembers();
	}

	/**
	 * This method allows the team member who will be the leader and
	 * statistician.
	 * 
	 * @param teamMembers
	 *            : teamMembers selected.
	 * @param flag
	 *            : flag is true if will save the leader and is false is will
	 *            save the statistician.
	 * 
	 */
	public void saveLeadStatistician(TeamMembers teamMembers, boolean flag) {
		try {
			ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
			if (flag) {
				teamMembersDao.editTeamMembers(teamMembers);
			} else {
				for (TeamMembers teamMember : teamMembersList) {
					int idHr = teamMember.getTeamMembersPK().getHr().getIdHr();
					if (idHr == teamMembers.getTeamMembersPK().getHr()
							.getIdHr()
							&& teamMembers.isStatistician()) {
						teamMember.setStatistician(true);
					} else {
						teamMember.setStatistician(false);
					}
					teamMembersDao.editTeamMembers(teamMember);
				}
			}
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_modificar"), teamMembers
							.getTeamMembersPK().getHr().getName()
							+ " "
							+ teamMembers.getTeamMembersPK().getHr()
									.getFamilyName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		initializeTeamMembers();
	}
}