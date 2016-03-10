package co.informatix.erp.recursosHumanos.action;

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

import co.informatix.erp.recursosHumanos.dao.ContratoDao;
import co.informatix.erp.recursosHumanos.entities.Contrato;
import co.informatix.erp.recursosHumanos.entities.Persona;
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
public class ContratoAction implements Serializable {

	@EJB
	private ContratoDao contratoDao;
	@Inject
	private IdentityAction identity;

	private List<Contrato> contractList;

	private Contrato contract;
	private Paginador paginador = new Paginador();

	private String nameSearch = "";
	private String valid = Constantes.SI;

	/**
	 * @return contractList: List of contracts shown in the user interface.
	 */
	public List<Contrato> getContractList() {
		return contractList;
	}

	/**
	 * @param contractList
	 *            :List of contracts shown in the user interface.
	 */
	public void setContractList(List<Contrato> contractList) {
		this.contractList = contractList;
	}

	/**
	 * @return contract: Object containing contract information.
	 */
	public Contrato getContract() {
		return contract;
	}

	/**
	 * @param contract
	 *            :Object containing contract information.
	 */
	public void setContract(Contrato contract) {
		this.contract = contract;
	}

	/**
	 * @return paginador: Management paged list of contracts and historical.
	 */
	public Paginador getPaginador() {
		return paginador;
	}

	/**
	 * @param paginador
	 *            :Management paged list of contracts and historical.
	 */
	public void setPaginador(Paginador paginador) {
		this.paginador = paginador;
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
	 * 
	 */
	public String searchContracts() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		ResourceBundle bundleLifeCycle = ControladorContexto
				.getBundle("mensajeRecursosHumanos");
		ValidacionesAction validations = ControladorContexto
				.getContextBean(ValidacionesAction.class);
		String inModal = ControladorContexto.getParam("param2");
		contractList = new ArrayList<Contrato>();
		List<SelectItem> parameters = new ArrayList<SelectItem>();
		StringBuilder queryBuilder = new StringBuilder();
		StringBuilder jointSearchMessage = new StringBuilder();
		String searchMessage = "";
		boolean fromModal = (inModal != null && Constantes.SI.equals(inModal)) ? true
				: false;
		String returns = fromModal ? "" : "gesContrato";
		try {
			advancedSearch(queryBuilder, parameters, bundle, jointSearchMessage);
			Long amount = contratoDao.amountContracts(queryBuilder, parameters);
			if (amount != null) {
				if (fromModal) {
					paginador.paginarRangoDefinido(amount, 5);
				} else {
					paginador.paginar(amount);
				}
			}
			contractList = contratoDao.searchContracts(paginador.getInicio(),
					paginador.getRango(), queryBuilder, parameters);
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
	 * @param query
	 *            : Query to concatenate.
	 * @param parameters
	 *            : List of search parameters.
	 * @param bundle
	 *            : Context to access language tags.
	 * @param unionMessagesSearch
	 *            : Search message.
	 * 
	 */
	private void advancedSearch(StringBuilder query,
			List<SelectItem> parameters, ResourceBundle bundle,
			StringBuilder unionMessagesSearch) {
		if (this.nameSearch != null && !"".equals(this.nameSearch)) {
			query.append("WHERE UPPER(c.persona.nombres) LIKE UPPER(:keyword) ");
			query.append("OR UPPER(c.persona.apellidos) LIKE UPPER(:keyword) ");
			SelectItem item = new SelectItem("%" + this.nameSearch + "%",
					"keyword");
			parameters.add(item);
			unionMessagesSearch.append(bundle.getString("label_nombre") + ": "
					+ '"' + this.nameSearch + '"');
		}
	}

	/**
	 * Method to edit or create a new contract.
	 * 
	 * @param contract
	 *            : Contract to add or edit.
	 * 
	 * @return "regContrato": Redirects to the template to register a contract.
	 * @throws Exception
	 */
	public String addEditContract(Contrato contract) throws Exception {
		if (contract != null) {
			this.contract = contract;
			loadContractDetails(contract);
		} else {
			this.contract = new Contrato();
			this.contract.setPersona(new Persona());
		}
		return "regContrato";
	}

	/**
	 * This method fills the various objects associated with a contract.
	 * 
	 * @throws Exception
	 */
	public void loadContractsDetails() throws Exception {
		List<Contrato> contracts = new ArrayList<Contrato>();
		if (this.contractList != null) {
			contracts.addAll(this.contractList);
			this.contractList = new ArrayList<Contrato>();
			for (Contrato contract : contracts) {
				loadContractDetails(contract);
				this.contractList.add(contract);
			}
		}
	}

	/**
	 * Method to load the details of a single contract.
	 * 
	 * @param contract
	 *            : Contract which will carry the details.
	 * @throws Exception
	 */
	public void loadContractDetails(Contrato contract) throws Exception {
		int contractId = contract.getId();
		Persona person = (Persona) this.contratoDao.searchContract("persona",
				contractId);
		contract.setPersona(person);
	}

	/**
	 * Method that eliminates the contract in database.
	 * 
	 * @return searchContracts: Method that consults contracts; it redirects to
	 *         the contracts management template.
	 */
	public String deleteContract() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		try {
			contratoDao.deleteContract(contract);
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					bundle.getString("message_registro_eliminar"), contract
							.getPersona().getNombres()));
		} catch (EJBException e) {
			String format = MessageFormat.format(
					bundle.getString("message_existe_relacion_eliminar"),
					contract.getPersona().getNombres());
			ControladorContexto.mensajeError(e, null, format);
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return searchContracts();
	}

	/**
	 * Cleans the person field associated with the contract.
	 */
	public void deletePerson() {
		this.contract.setPersona(new Persona());
	}

	/**
	 * Method to load the selected person.
	 * 
	 * @param person
	 *            : Object of the selected person.
	 */
	public void loadPerson(Persona person) {
		this.contract.setPersona(person);
	}

	/**
	 * Method used to save or edit the contracts.
	 * 
	 * @return initializeSearch(): Method to query contracts and redirects to
	 *         the template management.
	 */
	public String saveContract() {
		ResourceBundle bundle = ControladorContexto.getBundle("mensaje");
		String key = bundle.getString("message_registro_modificar");
		try {
			if (this.contract.getId() != 0) {
				contratoDao.editContract(contract);
			} else {
				key = bundle.getString("message_registro_guardar");
				this.contract.setFechaCreacion(new Date());
				this.contract.setUserName(identity.getUserName());
				contratoDao.saveContract(contract);
			}
			this.nameSearch = "";
			ControladorContexto.mensajeInformacion(null, MessageFormat.format(
					key, contract.getPersona().getNombreCompleto()));
		} catch (Exception e) {
			ControladorContexto.mensajeError(e);
		}
		return initializeSearch();
	}
}
