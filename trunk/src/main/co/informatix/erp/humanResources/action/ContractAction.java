package co.informatix.erp.humanResources.action;

import java.io.Serializable;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javax.ejb.EJB;
import javax.ejb.EJBException;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.model.SelectItem;
import javax.inject.Inject;

import co.informatix.erp.humanResources.dao.ContractDao;
import co.informatix.erp.humanResources.entities.Contract;
import co.informatix.erp.humanResources.entities.Hr;
import co.informatix.erp.utils.Constantes;
import co.informatix.erp.utils.ControladorContexto;
import co.informatix.erp.utils.Paginador;
import co.informatix.erp.utils.ValidacionesAction;
import co.informatix.security.action.IdentityAction;

/**
 * This class handles the business logic of contracts which communicates the
 * view (register and manage contracts) with the data model.
 * 
 * The logic is to query, edit and add contracts.
 * 
 * @author Andres.Gomez
 * 
 */
@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ContractAction implements Serializable {

	@EJB
	private ContractDao contractDao;
	@Inject
	private IdentityAction identity;

	private List<Contract> contractList;

	private Contract contract;
	private Paginador pagination = new Paginador();

	private String nameSearch = "";
	private String valid = Constantes.SI;

	/**
	 * @return contractList: List of contracts shown in the user interface.
	 */
	public List<Contract> getContractList() {
		return contractList;
	}

	/**
	 * @param contractList
	 *            :List of contracts shown in the user interface.
	 */
	public void setContractList(List<Contract> contractList) {
		this.contractList = contractList;
	}

	/**
	 * @return contract: Object containing contract information.
	 */
	public Contract getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            :Object containing contract information.
	 */
	public void setContract(Contract contract) {
		this.contract = contract;
	}

	/**
	 * @return pagination: Management paged list of contracts and historical.
	 */
	public Paginador getPagination() {
		return pagination;
	}

	/**
	 * @param pagination
	 *            :Management paged list of contracts and historical.
	 */
	public void setPagination(Paginador pagination) {
		this.pagination = pagination;
	}

	/**
	 * @return nameSearch: Search value you use to query the contract.
	 */
	public String getNameSearch() {
		return nameSearch;
	}

	/**
	 * @param nameSearch
	 *            : Search value you use to query the contract.
	 */
	public void setNameSearch(String nameSearch) {
		this.nameSearch = nameSearch;
	}

	/**
	 * @return valid: Gets the value for the validity management of records.
	 */
	public String getValid() {
		return valid;
	}

	/**
	 * @param valid
	 *            : Sets the value for the validity management of records.
	 */
	public void setValid(String valid) {
		this.valid = valid;
	}

	/**
	 * Method to initialize the parameters of the search and load the initial
	 * list of contracts.
	 * 
	 * @return searchContracts: method consulting contracts, returns to the
	 *         template management.
	 */
	public String initializeSearch() {
		nameSearch = "";
		return searchContracts();
	}

	/**
	 * Look for contracts.
	 * 
	 * @modify 31/08/2015 Andres.Gomez
	 * @modify 08/03/2016 Mabell.Boada
	 * 
	 * @return returns: Redirects to the template to manage contracts.
	 */
	public String searchContracts() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("messageHumanResources");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String inModal = ControladorContexto.getParam("param2");
		contractList = new ArrayList<Contract>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessage = new StringBuilder();
		String searchMessage = "";
		boolean fromModal = (inModal != null && Constantes.SI.equals(inModal)) ? true
				: false;
		String returns = fromModal ? "" : "manContract";
		try {
			advancedSearch(queryBuilder, parameters, bundle, jointSearchMessage);
			Long amount = contractDao.amountContracts(queryBuilder, parameters);
			if (amount != null) {
				if (fromModal) {
					pagination.paginarRangoDefinido(amount, 5);
				} else {
					pagination.paginar(amount);
				}
			}
			contractList = contractDao.searchContracts(pagination.getInicio(),
					pagination.getRango(), queryBuilder, parameters);
			if ((contractList == null || contractList.size() <= 0)
					&& !"".equals(jointSearchMessage.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_no_existen_registros_criterio_busqueda"),
								jointSearchMessage);
			} else if (contractList == null || contractList.size() <= 0) {
				ControladorContexto.mensajeInformacion(null,
						bundle.getString("message_no_existen_registros"));
			} else if (!"".equals(jointSearchMessage.toString())) {
				searchMessage = MessageFormat
						.format(bundle
								.getString("message_existen_registros_criterio_busqueda"),
								bundleLifeCycle.getString("contract_label_s"),
								jointSearchMessage);
			}
			loadContractsDetails();
			if (fromModal) {
				validations.setMensajeBusquedaPopUp(searchMessage);
			} else {
				validations.setMensajeBusqueda(searchMessage);
			}
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return returns;
	}

	/**
	 * This method builds the advanced search and allows to construct display
	 * messages depending on the search criteria selected by the user.
	 * 
	 * @modify 02/05/2016 Mabell.Boada
	 * 
	 * @param query
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * @param unionMessagesSearch
	 *            : Search message.
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append("WHERE UPPER(c.hr.name) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_name") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new contract.
	 * 
	 * @modify 02/05/2016 Mabell.Boada
	 * 
	 * @param contract
	 *            : Contract to add or edit.
	 * @return "regContract": Redirects to the template to register a contract.
	 * @throws Exception
	 */
	public String addEditContract(Contract contract) throws Exception {
		if (contract != null) {
			this.contract = contract;
			loadContractDetails(contract);
		} else {
			this.contract = new Contract();
			this.contract.setHr(new Hr());
		}
		return "regContract";
	}

	/**
	 * This method fills the various objects associated with a contract.
	 * 
	 * @throws Exception
	 */
	public void loadContractsDetails() throws Exception {
		if (this.contractList != null) {
			for (Contract contract : this.contractList) {
				loadContractDetails(contract);
			}
		}
	}

	/**
	 * Method to load the details of a single contract.
	 * 
	 * @modify 02/05/2016 Mabell.Boada
	 * 
	 * @param contract
	 *            : Contract which will carry the details.
	 * @throws Exception
	 */
	public void loadContractDetails(Contract contract) throws Exception {
		int contractId = contract.getId();
		Hr hr = (Hr) this.contractDao.searchContract("hr", contractId);
		contract.setHr(hr);
	}

	/**
	 * Method that eliminates the contract in database.
	 * 
	 * @modify 02/05/2016 Mabell.Boada
	 * 
	 * @return searchContracts: Method that consults contracts; it redirects to
	 *         the contracts management template.
	 */
	public String deleteContract() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			contractDao.deleteContract(contract);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), contract
							.getHr().getName()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					contract.getHr().getName());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchContracts();
	}

	/**
	 * Cleans the person field associated with the contract.
	 */
	public void deleteHr() {
		this.contract.setHr(new Hr());
	}

	/**
	 * Method to load the selected person.
	 * 
	 * @param person
	 *            : Object of the selected person.
	 */
	public void loadHr(Hr hr) {
		this.contract.setHr(hr);
	}

	/**
	 * Method used to save or edit the contracts.
	 * 
	 * @modify 02/05/2016 Mabell.Boada
	 * 
	 * @return initializeSearch: Method to query contracts and redirects to the
	 *         template management.
	 */
	public String saveContract() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = bundle.getString("message_registro_modificar");
		try {
			if (this.contract.getId() != 0) {
				contractDao.editContract(contract);
			} else {
				key = bundle.getString("message_registro_guardar");
				this.contract.setCreated(new Date());
				this.contract.setUserName(identity.getUserName());
				contractDao.saveContract(contract);
			}
			this.nameSearch = "";
			ControladorContexto.mensajeInformacion(null,
					MessageFormat.format(key, contract.getHr().getName()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeSearch();
	}
}