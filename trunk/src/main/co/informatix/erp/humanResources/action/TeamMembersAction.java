package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;

import co.informatix.erp.humanResources.dao.TeamMembersDao;
import co.informatix.erp.humanResources.entities.Team;
import co.informatix.erp.humanResources.entities.TeamMembers;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;

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
public class TeamMembersAction implements Serializable {

	@EJB
	private TeamMembersDao teamMembersDao;

	private TeamMembers teamMembers;
	private Team teamSelected;
	private List<TeamMembers> teamMembersList;
	private Paginador pagination = new Paginador();

	/**
	 * @return teamMembers: Object of class TeamMembers.
	 */
	public TeamMembers getTeamMembers() {
		return teamMembers;
	}

	/**
	 * @param teamMembers
	 *            : Object of class TeamMembers.
	 */
	public void setTeamMembers(TeamMembers teamMembers) {
		this.teamMembers = teamMembers;
	}

	/**
	 * @return teamSelected: Object of class Team.
	 */
	public Team getTeamSelected() {
		return teamSelected;
	}

	/**
	 * @param teamSelected
	 *            : Object of class Team.
	 */
	public void setTeamSelected(Team teamSelected) {
		this.teamSelected = teamSelected;
	}

	/**
	 * @return teamMembersList: gets the teams list
	 */
	public List<TeamMembers> getTeamMembersList() {
		return teamMembersList;
	}

	/**
	 * @param teamMembersList
	 *            : sets the teams list
	 */
	public void setTeamMembersList(List<TeamMembers> teamMembersList) {
		this.teamMembersList = teamMembersList;
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
	 * Consult the list of invoice Items
	 * 
	 */
	public void consultTeamMembers() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder consult = new StringBuilder();
		StringBuilder allMessageSearch = new StringBuilder();
		this.teamMembersList = new ArrayList<TeamMembers>();
		try {
			advancedSearchInvoiceItems(consult, parameters, bundle,
					allMessageSearch);
			Long amount = teamMembersDao.amountTeamMembers(consult, parameters);
			if (amount != null && amount > 0) {
				pagination.paginarRangoDefinido(amount, 5);
				this.teamMembersList = teamMembersDao.consultTeamsMembers(
						pagination.getInicio(), pagination.getRango(), consult,
						parameters);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
	}

	/**
	 * This method allows to build the query to the advanced search and allows
	 * to construct messages displayed depending on the search criteria selected
	 * by the user.
	 * 
	 * 
	 * @param consult
	 *            : query to concatenate
	 * @param parameters
	 *            : list of search parameters.
	 * @param bundle
	 *            :access language tags
	 * @param unionMessagesSearch
	 *            : message search
	 */
	private void advancedSearchInvoiceItems(StringBuilder consult,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {

		if (this.teamSelected != null) {
			consult.append("WHERE t.idTeam = :idTeam ");
			SelectItem item = new SelectItem(this.teamSelected.getIdTeam(),
					"idTeam");
			parameters.add(item);
		}
	}

}
